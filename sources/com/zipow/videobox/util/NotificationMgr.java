package com.zipow.videobox.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.RemoteViews;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationManagerCompat;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.LauncherActivity;
import com.zipow.videobox.PTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.nos.NosCallActionRceiver;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomSubscribeRequest;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class NotificationMgr {
    public static final int ACTIVITY_CALLING_NOTICICATION_ID = 11;
    private static final long[] CALLING_VIBRATES = {2000, 1000, 2000, 1000};
    public static final String KEY_NOS_CALL_MESSAGE_BODY = "call_body";
    public static final String KEY_NOS_CALL_MESSAGE_TYPE = "call_type";
    public static final int LOGIN_EXPIRED_NOTICICATION_ID = 5;
    public static final int PT_NOTICICATION_ID = 4;
    public static final int PT_SIP_INCOMING_NOTICICATION_ID = 61;
    public static final int PT_SIP_NOTICICATION_ID = 6;
    private static final String TAG = "NotificationMgr";
    private static final long[] VIBRATES = {0, 200, 200, 200};
    private static final int ZOOM_MESSENGER_MESSAGE_NOTICICATION_ID = 7;
    private static final int ZOOM_MESSENGER_MESSAGE_NOTICICATION_ID_START = 10000;
    public static final String ZOOM_NOS_CALL_ACCEPT_ACTION = "nos_call_accept";
    public static final String ZOOM_NOS_CALL_CANCEL_ACTION = "nos_call_cancel";
    private static final int ZOOM_NOS_CALL_NOTICICATION_ID = 8;
    public static final String ZOOM_NOTIFICATION_CHANNEL_ID = "zoom_notification_channel_id";
    public static final int ZOOM_PBX_MESSAGE_NOTIFICATION_ID_START = 30000;
    public static final String ZOOM_PHONE_INCALL_CHANNEL_ID = "zoom_phone_incall_channel_id";
    public static final String ZOOM_PHONE_INCOME_CALL_CHANNEL_ID = "zoom_phone_income_call_channel_id";
    public static final String ZOOM_SERVICE_NOTIFICATION_CHANNEL_ID = "zoom_service_notification_channel_id";
    public static final int ZOOM_SERVICE_NOTIFY_NOTICICATION_ID = 9;
    public static final int ZOOM_SIP_MISSED_NOTICICATION_ID_START = 20000;
    @NonNull
    private static Handler s_handler = new Handler(Looper.getMainLooper());
    private static long s_lastMessageNotificationTime = 0;
    @NonNull
    private static Map<String, Long> s_lastMessageSoundNotificationTimes = new HashMap();
    private static Ringtone s_ringtone;
    @Nullable
    private static Runnable s_updateNotificationRunnable = null;

    public static class NotificationItem {
        private CharSequence content;
        private String title;

        public NotificationItem(String str, CharSequence charSequence) {
            this.title = str;
            this.content = charSequence;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String str) {
            this.title = str;
        }

        public CharSequence getContent() {
            return this.content;
        }

        public void setContent(CharSequence charSequence) {
            this.content = charSequence;
        }
    }

    public enum NotificationType {
        MEETING_CALL_NOTIFICATION,
        SIP_INCOMING_CALL_NOTIFICATION,
        SIP_INCALL_NOTIFICATION,
        LOGIN_NOTIFICATION
    }

    @NonNull
    public static String getNotificationChannelId() {
        return ZOOM_NOTIFICATION_CHANNEL_ID;
    }

    private static boolean isAlertImMsgEnabled() {
        return PTSettingHelper.getShowChatMessageReminder() != 2;
    }

    public static synchronized void playNotificationSound(Context context) {
        synchronized (NotificationMgr.class) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - s_lastMessageNotificationTime > 3000 || currentTimeMillis < s_lastMessageNotificationTime) {
                playNotificationSoundImpl(context);
            }
            s_lastMessageNotificationTime = currentTimeMillis;
        }
    }

    private static synchronized void playNotificationSoundImpl(@Nullable Context context) {
        int i;
        synchronized (NotificationMgr.class) {
            if (context != null) {
                if (isAlertImMsgEnabled()) {
                    try {
                        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
                        if (audioManager != null) {
                            i = audioManager.getRingerMode();
                            if (PTApp.getInstance().getSettingHelper() != null) {
                                if (i == 2) {
                                    if (PTSettingHelper.getPlayAlertSound()) {
                                        try {
                                            Uri defaultUri = RingtoneManager.getDefaultUri(2);
                                            if (defaultUri != null) {
                                                if (s_ringtone != null && s_ringtone.isPlaying()) {
                                                    s_ringtone.stop();
                                                }
                                                s_ringtone = RingtoneManager.getRingtone(context, defaultUri);
                                                if (s_ringtone != null) {
                                                    s_ringtone.setStreamType(5);
                                                    s_ringtone.play();
                                                }
                                            }
                                        } catch (Exception unused) {
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception unused2) {
                        i = 2;
                    }
                }
            }
        }
    }

    public static synchronized void playNotificationVibrate(Context context) {
        synchronized (NotificationMgr.class) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - s_lastMessageNotificationTime > 3000 || currentTimeMillis < s_lastMessageNotificationTime) {
                playNotificationVibrateImpl(context);
            }
            s_lastMessageNotificationTime = currentTimeMillis;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x004a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized void playNotificationVibrateImpl(@androidx.annotation.Nullable android.content.Context r4) {
        /*
            java.lang.Class<com.zipow.videobox.util.NotificationMgr> r0 = com.zipow.videobox.util.NotificationMgr.class
            monitor-enter(r0)
            if (r4 != 0) goto L_0x0007
            monitor-exit(r0)
            return
        L_0x0007:
            boolean r1 = isAlertImMsgEnabled()     // Catch:{ all -> 0x004b }
            if (r1 != 0) goto L_0x000f
            monitor-exit(r0)
            return
        L_0x000f:
            r1 = 2
            java.lang.String r2 = "audio"
            java.lang.Object r2 = r4.getSystemService(r2)     // Catch:{ Exception -> 0x0021 }
            android.media.AudioManager r2 = (android.media.AudioManager) r2     // Catch:{ Exception -> 0x0021 }
            if (r2 != 0) goto L_0x001c
            monitor-exit(r0)
            return
        L_0x001c:
            int r2 = r2.getRingerMode()     // Catch:{ Exception -> 0x0021 }
            goto L_0x0022
        L_0x0021:
            r2 = 2
        L_0x0022:
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()     // Catch:{ all -> 0x004b }
            com.zipow.videobox.ptapp.PTSettingHelper r3 = r3.getSettingHelper()     // Catch:{ all -> 0x004b }
            if (r3 != 0) goto L_0x002e
            monitor-exit(r0)
            return
        L_0x002e:
            if (r2 == r1) goto L_0x0033
            r1 = 1
            if (r2 != r1) goto L_0x0049
        L_0x0033:
            boolean r1 = com.zipow.videobox.ptapp.PTSettingHelper.getPlayAlertVibrate()     // Catch:{ all -> 0x004b }
            if (r1 == 0) goto L_0x0049
            java.lang.String r1 = "vibrator"
            java.lang.Object r4 = r4.getSystemService(r1)     // Catch:{ all -> 0x004b }
            android.os.Vibrator r4 = (android.os.Vibrator) r4     // Catch:{ all -> 0x004b }
            if (r4 == 0) goto L_0x0049
            long[] r1 = VIBRATES     // Catch:{ all -> 0x004b }
            r2 = -1
            r4.vibrate(r1, r2)     // Catch:{ all -> 0x004b }
        L_0x0049:
            monitor-exit(r0)
            return
        L_0x004b:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.NotificationMgr.playNotificationVibrateImpl(android.content.Context):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0015, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void showMessageNotificationMM(@androidx.annotation.Nullable android.content.Context r2, boolean r3, @androidx.annotation.Nullable java.lang.String r4) {
        /*
            java.lang.Class<com.zipow.videobox.util.NotificationMgr> r0 = com.zipow.videobox.util.NotificationMgr.class
            monitor-enter(r0)
            if (r2 == 0) goto L_0x0014
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r4)     // Catch:{ all -> 0x0011 }
            if (r1 == 0) goto L_0x000c
            goto L_0x0014
        L_0x000c:
            showMessageNotificationMMImpl(r2, r3, r4)     // Catch:{ all -> 0x0011 }
            monitor-exit(r0)
            return
        L_0x0011:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        L_0x0014:
            monitor-exit(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.NotificationMgr.showMessageNotificationMM(android.content.Context, boolean, java.lang.String):void");
    }

    public static NotificationItem generateNotificationItem(Context context, int i, int i2, String str) {
        String string = context.getString(C4558R.string.zm_contact_requests_83123);
        if (i2 == 0) {
            return null;
        }
        if (i2 > 1) {
            string = context.getResources().getQuantityString(C4558R.plurals.zm_msg_notification_unread_num_8295, i2, new Object[]{string, Integer.valueOf(i2)});
        }
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        String str2 = "";
        switch (i) {
            case 0:
                str2 = context.getString(C4558R.string.zm_session_recive_contact_request_107052, new Object[]{str});
                break;
            case 1:
                str2 = context.getString(C4558R.string.zm_session_contact_request_accept_byother, new Object[]{str});
                break;
            case 2:
                str2 = context.getString(C4558R.string.zm_session_contact_request_decline_byother, new Object[]{str});
                break;
        }
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(str2)) {
            return null;
        }
        return new NotificationItem(string, str2);
    }

    public static NotificationItem generateNotificationItem(@NonNull Context context, int i, String str, String str2, int i2, String str3, boolean z) {
        String str4 = "";
        String str5 = "";
        if (!(i == 14 || i == 16)) {
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    break;
                default:
                    str = str4;
                    str2 = str5;
                    break;
            }
        }
        if (z) {
            str = context.getString(C4558R.string.zm_contact_requests_83123);
        } else if (!PTSettingHelper.isImNotificationMessagePreview()) {
            str2 = context.getString(C4558R.string.zm_msg_chat_notification);
        }
        if (i2 == 0) {
            return null;
        }
        if (i2 > 1) {
            str = context.getResources().getQuantityString(C4558R.plurals.zm_msg_notification_unread_num_8295, i2, new Object[]{str, Integer.valueOf(i2)});
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        return new NotificationItem(str, str2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00c8  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00d0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.util.NotificationMgr.NotificationItem generateNotificationItem(@androidx.annotation.NonNull android.content.Context r4, boolean r5, boolean r6, boolean r7, boolean r8, int r9, java.lang.String r10, java.lang.String r11, java.lang.String r12, java.lang.CharSequence r13, int r14) {
        /*
            r12 = 2
            r0 = 0
            r1 = 0
            r2 = 1
            if (r6 == 0) goto L_0x0018
            if (r5 == 0) goto L_0x0010
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_lbl_message_decrypt_group_31105
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x0010:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_lbl_message_decrypt_31105
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x0018:
            r6 = 80
            if (r9 == r6) goto L_0x0131
            r6 = 88
            if (r9 == r6) goto L_0x013b
            switch(r9) {
                case 0: goto L_0x00d7;
                case 1: goto L_0x00c6;
                case 2: goto L_0x00b4;
                default: goto L_0x0023;
            }
        L_0x0023:
            switch(r9) {
                case 4: goto L_0x008e;
                case 5: goto L_0x00c6;
                case 6: goto L_0x00c6;
                default: goto L_0x0026;
            }
        L_0x0026:
            switch(r9) {
                case 10: goto L_0x007c;
                case 11: goto L_0x0070;
                case 12: goto L_0x00c6;
                case 13: goto L_0x005e;
                case 14: goto L_0x0070;
                case 15: goto L_0x007c;
                default: goto L_0x0029;
            }
        L_0x0029:
            switch(r9) {
                case 20: goto L_0x004a;
                case 21: goto L_0x004a;
                case 22: goto L_0x004a;
                case 23: goto L_0x004a;
                case 24: goto L_0x004a;
                case 25: goto L_0x004a;
                default: goto L_0x002c;
            }
        L_0x002c:
            switch(r9) {
                case 70: goto L_0x003e;
                case 71: goto L_0x0032;
                default: goto L_0x002f;
            }
        L_0x002f:
            r13 = r0
            goto L_0x013b
        L_0x0032:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_invite_accepted
            java.lang.Object[] r7 = new java.lang.Object[r2]
            r7[r1] = r10
            java.lang.String r13 = r4.getString(r6, r7)
            goto L_0x013b
        L_0x003e:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_get_invite
            java.lang.Object[] r7 = new java.lang.Object[r2]
            r7[r1] = r10
            java.lang.String r13 = r4.getString(r6, r7)
            goto L_0x013b
        L_0x004a:
            if (r13 != 0) goto L_0x004e
            r6 = r0
            goto L_0x0052
        L_0x004e:
            java.lang.String r6 = r13.toString()
        L_0x0052:
            com.zipow.videobox.ptapp.mm.GroupAction r6 = com.zipow.videobox.ptapp.p013mm.GroupAction.loadFromString(r6)
            if (r6 == 0) goto L_0x013b
            java.lang.String r13 = r6.toMessage(r4)
            goto L_0x013b
        L_0x005e:
            if (r5 == 0) goto L_0x0068
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_nos_message_code_snippet_group_31945
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x0068:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_nos_message_code_snippet_31945
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x0070:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_webhoot_new_notification
            java.lang.Object[] r7 = new java.lang.Object[r2]
            r7[r1] = r10
            java.lang.String r13 = r4.getString(r6, r7)
            goto L_0x013b
        L_0x007c:
            if (r5 == 0) goto L_0x0086
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_nos_message_file_group_31105
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x0086:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_nos_message_file_31105
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x008e:
            if (r5 != 0) goto L_0x0098
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_message_meeting_invitation
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x0098:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r6.append(r10)
            java.lang.String r7 = ": "
            r6.append(r7)
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_message_meeting_invitation
            java.lang.String r7 = r4.getString(r7)
            r6.append(r7)
            java.lang.String r13 = r6.toString()
            goto L_0x013b
        L_0x00b4:
            if (r5 == 0) goto L_0x00be
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_nos_message_voice_group_31105
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x00be:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_nos_message_voice_31105
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x00c6:
            if (r5 == 0) goto L_0x00d0
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_nos_message_picture_group_31105
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x00d0:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_nos_message_picture_31105
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x00d7:
            if (r5 != 0) goto L_0x00e7
            boolean r6 = com.zipow.videobox.ptapp.PTSettingHelper.isImNotificationMessagePreview()
            if (r6 == 0) goto L_0x00e0
            goto L_0x013b
        L_0x00e0:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_chat_notification
            java.lang.String r13 = r4.getString(r6)
            goto L_0x013b
        L_0x00e7:
            boolean r6 = com.zipow.videobox.ptapp.PTSettingHelper.isImNotificationMessagePreview()
            if (r6 == 0) goto L_0x00ee
            goto L_0x00f4
        L_0x00ee:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_chat_notification
            java.lang.String r13 = r4.getString(r6)
        L_0x00f4:
            if (r7 != 0) goto L_0x00f8
            if (r8 == 0) goto L_0x013b
        L_0x00f8:
            if (r7 == 0) goto L_0x00fd
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_message_mentioned_me
            goto L_0x00ff
        L_0x00fd:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_message_mentioned_all
        L_0x00ff:
            java.lang.String r6 = r4.getString(r6)
            android.text.SpannableString r7 = new android.text.SpannableString
            r8 = 3
            java.lang.CharSequence[] r8 = new java.lang.CharSequence[r8]
            r8[r1] = r6
            java.lang.String r3 = " "
            r8[r2] = r3
            r8[r12] = r13
            java.lang.CharSequence r8 = android.text.TextUtils.concat(r8)
            r7.<init>(r8)
            android.text.style.ForegroundColorSpan r8 = new android.text.style.ForegroundColorSpan
            android.content.res.Resources r13 = r4.getResources()
            int r3 = p021us.zoom.videomeetings.C4558R.color.zm_im_mentioned
            int r13 = r13.getColor(r3)
            r8.<init>(r13)
            int r6 = r6.length()
            r13 = 33
            r7.setSpan(r8, r1, r6, r13)
            r13 = r7
            goto L_0x013b
        L_0x0131:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_delete_by_other_24679
            java.lang.Object[] r7 = new java.lang.Object[r2]
            r7[r1] = r10
            java.lang.String r13 = r4.getString(r6, r7)
        L_0x013b:
            if (r5 == 0) goto L_0x0160
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r10)
            java.lang.String r6 = " "
            r5.append(r6)
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_notification_group_in_31105
            java.lang.String r6 = r4.getString(r6)
            r5.append(r6)
            java.lang.String r6 = " "
            r5.append(r6)
            r5.append(r11)
            java.lang.String r10 = r5.toString()
            goto L_0x0165
        L_0x0160:
            r5 = 14
            if (r9 != r5) goto L_0x0165
            r10 = r11
        L_0x0165:
            if (r14 != 0) goto L_0x0168
            return r0
        L_0x0168:
            if (r14 <= r2) goto L_0x017e
            android.content.res.Resources r4 = r4.getResources()
            int r5 = p021us.zoom.videomeetings.C4558R.plurals.zm_msg_notification_unread_num_8295
            java.lang.Object[] r6 = new java.lang.Object[r12]
            r6[r1] = r10
            java.lang.Integer r7 = java.lang.Integer.valueOf(r14)
            r6[r2] = r7
            java.lang.String r10 = r4.getQuantityString(r5, r14, r6)
        L_0x017e:
            boolean r4 = android.text.TextUtils.isEmpty(r10)
            if (r4 != 0) goto L_0x0190
            boolean r4 = android.text.TextUtils.isEmpty(r13)
            if (r4 != 0) goto L_0x0190
            com.zipow.videobox.util.NotificationMgr$NotificationItem r4 = new com.zipow.videobox.util.NotificationMgr$NotificationItem
            r4.<init>(r10, r13)
            return r4
        L_0x0190:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.NotificationMgr.generateNotificationItem(android.content.Context, boolean, boolean, boolean, boolean, int, java.lang.String, java.lang.String, java.lang.String, java.lang.CharSequence, int):com.zipow.videobox.util.NotificationMgr$NotificationItem");
    }

    private static synchronized void showMessageNotificationMMImpl(@Nullable Context context, boolean z, @Nullable String str) {
        NotificationItem notificationItem;
        int i;
        long j;
        String str2;
        CharSequence charSequence;
        String str3;
        String str4;
        CharSequence charSequence2;
        CharSequence charSequence3;
        Context context2 = context;
        String str5 = str;
        synchronized (NotificationMgr.class) {
            if (context2 != null) {
                if (!VideoBoxApplication.getInstance().isSDKMode()) {
                    if (isAlertImMsgEnabled()) {
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            if (str5 != null) {
                                if (str5.equals(zoomMessenger.getContactRequestsSessionID())) {
                                    if (zoomMessenger.getSubscribeRequestCount() != 0) {
                                        int i2 = 0;
                                        ZoomSubscribeRequest subscribeRequestAt = zoomMessenger.getSubscribeRequestAt(0);
                                        if (subscribeRequestAt != null) {
                                            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(subscribeRequestAt.getRequestJID());
                                            if (buddyWithJID != null) {
                                                int unreadRequestCount = zoomMessenger.getUnreadRequestCount();
                                                String email = ((buddyWithJID.isPending() || subscribeRequestAt.getRequestStatus() == 2) && subscribeRequestAt.getRequestType() != 0) ? buddyWithJID.getEmail() : buddyWithJID.getScreenName();
                                                if (StringUtil.isEmptyOrNull(email)) {
                                                    email = buddyWithJID.getScreenName();
                                                }
                                                if (!StringUtil.isEmptyOrNull(email)) {
                                                    if (subscribeRequestAt.getRequestType() != 0) {
                                                        i2 = subscribeRequestAt.getRequestStatus();
                                                    }
                                                    i = unreadRequestCount;
                                                    notificationItem = generateNotificationItem(context2, i2, unreadRequestCount, email);
                                                    j = 0;
                                                    showMessageNotificationMMImpl(context, z, j, i, str, notificationItem);
                                                }
                                                return;
                                            }
                                            return;
                                        }
                                        return;
                                    }
                                    return;
                                }
                            }
                            ZoomChatSession sessionById = zoomMessenger.getSessionById(str5);
                            if (sessionById != null) {
                                ZoomMessage lastMessage = sessionById.getLastMessage();
                                if (lastMessage != null) {
                                    long stamp = lastMessage.getStamp();
                                    boolean isGroup = sessionById.isGroup();
                                    String senderName = lastMessage.getSenderName();
                                    if (TextUtils.isEmpty(senderName)) {
                                        ZoomBuddy buddyWithJID2 = zoomMessenger.getBuddyWithJID(lastMessage.getSenderID());
                                        if (buddyWithJID2 != null) {
                                            str2 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID2, null);
                                        } else {
                                            return;
                                        }
                                    } else {
                                        str2 = senderName;
                                    }
                                    boolean hasUnreadMessageAtMe = sessionById.hasUnreadMessageAtMe();
                                    boolean hasUnreadedMessageAtAllMembers = sessionById.hasUnreadedMessageAtAllMembers();
                                    boolean isE2EMessage = lastMessage.isE2EMessage();
                                    int unreadMessageCount = sessionById.getUnreadMessageCount();
                                    String str6 = "";
                                    String str7 = "";
                                    FileInfo fileInfo = lastMessage.getFileInfo();
                                    String str8 = fileInfo != null ? fileInfo.name : str6;
                                    if (unreadMessageCount != 0) {
                                        int messageType = lastMessage.getMessageType();
                                        if (messageType != 0) {
                                            if (messageType != 14) {
                                                if (messageType != 88) {
                                                    switch (messageType) {
                                                        case 20:
                                                        case 21:
                                                        case 22:
                                                        case 23:
                                                        case 24:
                                                        case 25:
                                                            String str9 = str7;
                                                            charSequence = lastMessage.getBody();
                                                            str3 = str9;
                                                            break;
                                                        default:
                                                            charSequence2 = null;
                                                            break;
                                                    }
                                                } else {
                                                    String str10 = str7;
                                                    charSequence = lastMessage.getBody();
                                                    str3 = str10;
                                                }
                                            } else if (!isGroup) {
                                                ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                                                if (sessionBuddy != null) {
                                                    charSequence3 = null;
                                                    str7 = BuddyNameUtil.getBuddyDisplayName(sessionBuddy, null);
                                                } else {
                                                    charSequence3 = null;
                                                }
                                                str3 = str7;
                                                charSequence = charSequence3;
                                            } else {
                                                charSequence2 = null;
                                            }
                                            str3 = str7;
                                            charSequence = charSequence2;
                                        } else {
                                            String str11 = str7;
                                            charSequence = lastMessage.getBodyWithShortcut();
                                            str3 = str11;
                                        }
                                        if (isGroup) {
                                            ZoomGroup sessionGroup = sessionById.getSessionGroup();
                                            if (sessionGroup != null) {
                                                str4 = sessionGroup.getGroupDisplayName(VideoBoxApplication.getInstance());
                                                notificationItem = generateNotificationItem(context, isGroup, isE2EMessage, hasUnreadMessageAtMe, hasUnreadedMessageAtAllMembers, lastMessage.getMessageType(), str2, str4, str8, charSequence, unreadMessageCount);
                                                j = stamp;
                                                i = unreadMessageCount;
                                                showMessageNotificationMMImpl(context, z, j, i, str, notificationItem);
                                            }
                                        }
                                        str4 = str3;
                                        notificationItem = generateNotificationItem(context, isGroup, isE2EMessage, hasUnreadMessageAtMe, hasUnreadedMessageAtAllMembers, lastMessage.getMessageType(), str2, str4, str8, charSequence, unreadMessageCount);
                                        j = stamp;
                                        i = unreadMessageCount;
                                        showMessageNotificationMMImpl(context, z, j, i, str, notificationItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static synchronized void showMessageNotificationMMImpl(@Nullable Context context, boolean z, long j, int i, String str, @Nullable NotificationItem notificationItem) {
        synchronized (NotificationMgr.class) {
            showMessageNotificationMMImpl(context, z, j, i, str, notificationItem, false);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0139, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void showMessageNotificationMMImpl(@androidx.annotation.Nullable android.content.Context r8, boolean r9, long r10, int r12, java.lang.String r13, @androidx.annotation.Nullable com.zipow.videobox.util.NotificationMgr.NotificationItem r14, boolean r15) {
        /*
            java.lang.Class<com.zipow.videobox.util.NotificationMgr> r0 = com.zipow.videobox.util.NotificationMgr.class
            monitor-enter(r0)
            if (r8 == 0) goto L_0x0138
            if (r14 == 0) goto L_0x0138
            boolean r1 = android.text.TextUtils.isEmpty(r13)     // Catch:{ all -> 0x0135 }
            if (r1 == 0) goto L_0x000f
            goto L_0x0138
        L_0x000f:
            java.lang.String r1 = "notification"
            java.lang.Object r1 = r8.getSystemService(r1)     // Catch:{ all -> 0x0135 }
            android.app.NotificationManager r1 = (android.app.NotificationManager) r1     // Catch:{ all -> 0x0135 }
            android.content.Intent r2 = new android.content.Intent     // Catch:{ all -> 0x0135 }
            java.lang.Class<com.zipow.videobox.IntegrationActivity> r3 = com.zipow.videobox.IntegrationActivity.class
            r2.<init>(r8, r3)     // Catch:{ all -> 0x0135 }
            java.lang.String r3 = com.zipow.videobox.IntegrationActivity.ACTION_SHOW_UNREAD_MESSAGE_MM     // Catch:{ all -> 0x0135 }
            r2.setAction(r3)     // Catch:{ all -> 0x0135 }
            java.lang.String r3 = "unreadMsgSession"
            r2.putExtra(r3, r13)     // Catch:{ all -> 0x0135 }
            java.lang.String r3 = "addContact"
            r2.putExtra(r3, r15)     // Catch:{ all -> 0x0135 }
            r15 = 268435456(0x10000000, float:2.5243549E-29)
            r2.setFlags(r15)     // Catch:{ all -> 0x0135 }
            int r3 = r13.hashCode()     // Catch:{ all -> 0x0135 }
            int r3 = r3 + 10000
            android.app.PendingIntent r15 = android.app.PendingIntent.getActivity(r8, r3, r2, r15)     // Catch:{ all -> 0x0135 }
            android.content.res.Resources r2 = r8.getResources()     // Catch:{ all -> 0x0135 }
            int r3 = p021us.zoom.videomeetings.C4558R.color.zm_notification_icon_bg     // Catch:{ all -> 0x0135 }
            int r2 = r2.getColor(r3)     // Catch:{ all -> 0x0135 }
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_unread_message     // Catch:{ all -> 0x0135 }
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0135 }
            r5 = 21
            if (r4 < r5) goto L_0x0050
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_unread_message_5_0     // Catch:{ all -> 0x0135 }
        L_0x0050:
            android.content.res.Resources r4 = r8.getResources()     // Catch:{ all -> 0x0135 }
            int r6 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_launcher     // Catch:{ all -> 0x0135 }
            android.graphics.Bitmap r4 = android.graphics.BitmapFactory.decodeResource(r4, r6)     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r6 = getNotificationCompatBuilder(r8)     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r6.setWhen(r10)     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setSmallIcon(r3)     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setColor(r2)     // Catch:{ all -> 0x0135 }
            java.lang.String r11 = r14.getTitle()     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setContentTitle(r11)     // Catch:{ all -> 0x0135 }
            java.lang.CharSequence r11 = r14.getContent()     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setContentText(r11)     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setContentIntent(r15)     // Catch:{ all -> 0x0135 }
            r11 = 1
            androidx.core.app.NotificationCompat$Builder r10 = r10.setVisibility(r11)     // Catch:{ all -> 0x0135 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x0135 }
            r15.<init>()     // Catch:{ all -> 0x0135 }
            android.content.res.Resources r2 = r8.getResources()     // Catch:{ all -> 0x0135 }
            int r3 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_message_notifications_19898     // Catch:{ all -> 0x0135 }
            java.lang.String r2 = r2.getString(r3)     // Catch:{ all -> 0x0135 }
            r15.append(r2)     // Catch:{ all -> 0x0135 }
            java.lang.String r2 = "\n"
            r15.append(r2)     // Catch:{ all -> 0x0135 }
            java.lang.String r2 = r14.getTitle()     // Catch:{ all -> 0x0135 }
            r15.append(r2)     // Catch:{ all -> 0x0135 }
            java.lang.String r2 = "\n"
            r15.append(r2)     // Catch:{ all -> 0x0135 }
            java.lang.CharSequence r14 = r14.getContent()     // Catch:{ all -> 0x0135 }
            r15.append(r14)     // Catch:{ all -> 0x0135 }
            java.lang.String r14 = r15.toString()     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setTicker(r14)     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setAutoCancel(r11)     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setOnlyAlertOnce(r11)     // Catch:{ all -> 0x0135 }
            androidx.core.app.NotificationCompat$Builder r10 = r10.setNumber(r12)     // Catch:{ all -> 0x0135 }
            int r14 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0135 }
            if (r14 < r5) goto L_0x00c8
            r10.setPriority(r11)     // Catch:{ all -> 0x0135 }
        L_0x00c8:
            long r14 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0135 }
            java.util.Map<java.lang.String, java.lang.Long> r11 = s_lastMessageSoundNotificationTimes     // Catch:{ all -> 0x0135 }
            java.lang.Object r11 = r11.get(r13)     // Catch:{ all -> 0x0135 }
            java.lang.Long r11 = (java.lang.Long) r11     // Catch:{ all -> 0x0135 }
            if (r11 == 0) goto L_0x00ea
            long r2 = r11.longValue()     // Catch:{ all -> 0x0135 }
            long r2 = r14 - r2
            r6 = 5000(0x1388, double:2.4703E-320)
            int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r2 > 0) goto L_0x00ea
            long r2 = r11.longValue()     // Catch:{ all -> 0x0135 }
            int r11 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1))
            if (r11 >= 0) goto L_0x0105
        L_0x00ea:
            if (r9 == 0) goto L_0x0105
            r9 = 0
            r10.setOnlyAlertOnce(r9)     // Catch:{ all -> 0x0135 }
            boolean r9 = com.zipow.videobox.ptapp.PTSettingHelper.getPlayAlertSound()     // Catch:{ all -> 0x0135 }
            if (r9 == 0) goto L_0x00fa
            r9 = 5
            r10.setDefaults(r9)     // Catch:{ all -> 0x0135 }
        L_0x00fa:
            boolean r9 = com.zipow.videobox.ptapp.PTSettingHelper.getPlayAlertVibrate()     // Catch:{ all -> 0x0135 }
            if (r9 == 0) goto L_0x0105
            long[] r9 = VIBRATES     // Catch:{ all -> 0x0135 }
            r10.setVibrate(r9)     // Catch:{ all -> 0x0135 }
        L_0x0105:
            java.util.Map<java.lang.String, java.lang.Long> r9 = s_lastMessageSoundNotificationTimes     // Catch:{ all -> 0x0135 }
            java.lang.Long r11 = java.lang.Long.valueOf(r14)     // Catch:{ all -> 0x0135 }
            r9.put(r13, r11)     // Catch:{ all -> 0x0135 }
            int r9 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0135 }
            if (r9 < r5) goto L_0x0121
            android.content.res.Resources r9 = r8.getResources()     // Catch:{ all -> 0x0135 }
            int r11 = p021us.zoom.videomeetings.C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above     // Catch:{ all -> 0x0135 }
            boolean r9 = r9.getBoolean(r11)     // Catch:{ all -> 0x0135 }
            if (r9 == 0) goto L_0x0121
            r10.setLargeIcon(r4)     // Catch:{ all -> 0x0135 }
        L_0x0121:
            android.app.Notification r9 = r10.build()     // Catch:{ all -> 0x0135 }
            p021us.zoom.androidlib.util.UIUtil.setNotificationMessageCount(r8, r9, r12)     // Catch:{ all -> 0x0135 }
            if (r1 == 0) goto L_0x0133
            int r8 = r13.hashCode()     // Catch:{ Exception -> 0x0133 }
            int r8 = r8 + 10000
            r1.notify(r8, r9)     // Catch:{ Exception -> 0x0133 }
        L_0x0133:
            monitor-exit(r0)
            return
        L_0x0135:
            r8 = move-exception
            monitor-exit(r0)
            throw r8
        L_0x0138:
            monitor-exit(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.NotificationMgr.showMessageNotificationMMImpl(android.content.Context, boolean, long, int, java.lang.String, com.zipow.videobox.util.NotificationMgr$NotificationItem, boolean):void");
    }

    public static void showMissedSipCallNotification(@Nullable Context context, @Nullable String str, @Nullable NotificationItem notificationItem) {
        if (context != null && notificationItem != null && !TextUtils.isEmpty(str)) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            Intent intent = new Intent(context, IntegrationActivity.class);
            intent.setAction(IntegrationActivity.ACTION_SIP_CALL_MISSED);
            intent.setFlags(268435456);
            PendingIntent activity = PendingIntent.getActivity(context, str.hashCode() + ZOOM_SIP_MISSED_NOTICICATION_ID_START, intent, 268435456);
            int i = C4558R.C4559drawable.zm_conf_notification;
            if (VERSION.SDK_INT >= 21) {
                i = C4558R.C4559drawable.zm_sip_notification_5_0;
            }
            int color = context.getResources().getColor(C4558R.color.zm_notification_icon_bg);
            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), C4558R.C4559drawable.zm_launcher);
            Builder onlyAlertOnce = getNotificationCompatBuilder(context).setWhen(0).setSmallIcon(i).setColor(color).setContentTitle(notificationItem.getTitle()).setContentText(notificationItem.getContent()).setContentIntent(activity).setVisibility(1).setTicker(context.getResources().getString(C4558R.string.zm_sip_missed_sip_call_ticker_111899, new Object[]{notificationItem.getTitle()})).setAutoCancel(true).setOnlyAlertOnce(true);
            if (VERSION.SDK_INT >= 21) {
                onlyAlertOnce.setPriority(1);
            }
            if (VERSION.SDK_INT >= 21 && context.getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
                onlyAlertOnce.setLargeIcon(decodeResource);
            }
            Notification build = onlyAlertOnce.build();
            if (notificationManager != null) {
                try {
                    notificationManager.notify(str.hashCode() + ZOOM_SIP_MISSED_NOTICICATION_ID_START, build);
                } catch (Exception unused) {
                }
            }
        }
    }

    public static synchronized void showMessageNotificationMM(@Nullable Context context, boolean z) {
        synchronized (NotificationMgr.class) {
            if (context != null) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - s_lastMessageNotificationTime > 3000 || currentTimeMillis < s_lastMessageNotificationTime) {
                    showMessageNotificationMMImpl(context, z);
                }
                s_lastMessageNotificationTime = currentTimeMillis;
            }
        }
    }

    public static void nosCallNotificationImpl(@Nullable final Context context, int i, String str) {
        if (context != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            PendingIntent activity = PendingIntent.getActivity(context, 0, new Intent(context, LauncherActivity.class), 268435456);
            Intent intent = new Intent(context, NosCallActionRceiver.class);
            intent.setAction(ZOOM_NOS_CALL_CANCEL_ACTION);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 268435456);
            Intent intent2 = new Intent(context, NosCallActionRceiver.class);
            intent2.setAction(ZOOM_NOS_CALL_ACCEPT_ACTION);
            intent2.putExtra(KEY_NOS_CALL_MESSAGE_TYPE, i);
            intent2.putExtra(KEY_NOS_CALL_MESSAGE_BODY, str);
            PendingIntent broadcast2 = PendingIntent.getBroadcast(context, 0, intent2, 268435456);
            String string = context.getString(C4558R.string.zm_app_name);
            String string2 = context.getString(C4558R.string.zm_msg_chat_notification);
            int color = context.getResources().getColor(C4558R.color.zm_notification_icon_bg);
            int i2 = C4558R.C4559drawable.zm_unread_message;
            if (VERSION.SDK_INT >= 21) {
                i2 = C4558R.C4559drawable.zm_unread_message_5_0;
            }
            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), C4558R.C4559drawable.zm_launcher);
            Builder autoCancel = getNotificationCompatBuilder(context).setWhen(0).setSmallIcon(i2).setColor(color).setVisibility(1).setContentTitle(string).setContentText(string2).setContentIntent(activity).setFullScreenIntent(activity, true).addAction(C4558R.C4559drawable.zm_mm_delete_btn_pressed, "accept", broadcast2).addAction(C4558R.C4559drawable.zm_voice_rcd_cancel_icon, "cancel", broadcast).setAutoCancel(true);
            StringBuilder sb = new StringBuilder();
            sb.append("android.resource://");
            sb.append(context.getPackageName());
            sb.append("/");
            sb.append(C4558R.raw.ring_original);
            autoCancel.setSound(Uri.parse(sb.toString()));
            autoCancel.setVibrate(CALLING_VIBRATES);
            if (VERSION.SDK_INT >= 21 && context.getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
                autoCancel.setLargeIcon(decodeResource);
            }
            Notification build = autoCancel.build();
            build.flags |= 4;
            if (notificationManager != null) {
                try {
                    notificationManager.notify(8, build);
                } catch (Exception unused) {
                }
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            NotificationMgr.removeNosCallNotification(context);
                        }
                    });
                }
            }, 60000);
        }
    }

    public static void removeNosCallNotification(@Nullable Context context) {
        if (context != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                try {
                    notificationManager.cancel(8);
                } catch (Exception unused) {
                }
            }
        }
    }

    private static void showMessageNotificationMMImpl(@Nullable Context context, boolean z) {
        int i;
        int i2;
        if (context != null) {
            if ((VideoBoxApplication.getInstance() == null || !VideoBoxApplication.getInstance().isSDKMode()) && isAlertImMsgEnabled() && PTApp.getInstance().getSettingHelper() != null) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                Intent intent = new Intent(context, IntegrationActivity.class);
                intent.setAction(IntegrationActivity.ACTION_SHOW_UNREAD_MESSAGE_MM);
                int i3 = 0;
                PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    i = zoomMessenger.getTotalUnreadMessageCount();
                    i2 = zoomMessenger.getUnreadRequestCount();
                } else {
                    i2 = 0;
                    i = 0;
                }
                IMHelper iMHelper = PTApp.getInstance().getIMHelper();
                if (iMHelper != null) {
                    i3 = iMHelper.getUnreadMsgCount();
                }
                int i4 = i + i3 + i2;
                String string = context.getString(C4558R.string.zm_app_name);
                String string2 = context.getString(C4558R.string.zm_msg_chat_notification);
                int color = context.getResources().getColor(C4558R.color.zm_notification_icon_bg);
                int i5 = C4558R.C4559drawable.zm_unread_message;
                if (VERSION.SDK_INT >= 21) {
                    i5 = C4558R.C4559drawable.zm_unread_message_5_0;
                }
                Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), C4558R.C4559drawable.zm_launcher);
                Builder autoCancel = getNotificationCompatBuilder(context).setWhen(0).setSmallIcon(i5).setColor(color).setContentTitle(string).setContentText(string2).setContentIntent(activity).setAutoCancel(true);
                if (z) {
                    if (PTSettingHelper.getPlayAlertSound()) {
                        autoCancel.setDefaults(5);
                    }
                    if (PTSettingHelper.getPlayAlertVibrate()) {
                        autoCancel.setVibrate(VIBRATES);
                    }
                }
                if (VERSION.SDK_INT >= 21 && context.getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
                    autoCancel.setLargeIcon(decodeResource);
                }
                Notification build = autoCancel.build();
                UIUtil.setNotificationMessageCount(context, build, i4);
                if (notificationManager != null) {
                    try {
                        notificationManager.notify(7, build);
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    public static void removeMessageNotificationMM(@Nullable Context context, @Nullable String str) {
        if (context != null && !StringUtil.isEmptyOrNull(str)) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                try {
                    notificationManager.cancel(str.hashCode() + 10000);
                } catch (Exception unused) {
                }
            }
        }
    }

    public static void removeAllMessageNotificationMM(@Nullable Context context) {
        if (context != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                try {
                    notificationManager.cancelAll();
                } catch (Exception unused) {
                }
            }
        }
    }

    public static void removeMessageNotificationMM(@Nullable Context context) {
        if (context != null) {
            Runnable runnable = s_updateNotificationRunnable;
            if (runnable != null) {
                s_handler.removeCallbacks(runnable);
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                try {
                    notificationManager.cancel(7);
                } catch (Exception unused) {
                }
            }
        }
    }

    public static void removeConfNotification(@Nullable Context context) {
        if (context != null) {
            if (VideoBoxApplication.getInstance().isSDKMode()) {
                ((NotificationManager) context.getSystemService("notification")).cancel(4);
            } else {
                if (!OsUtil.isAtLeastO()) {
                    ZMServiceHelper.doServiceActionInFront(PTService.ACTION_REMOVE_CONF_NOTIFICATION, PTService.class);
                }
                context.sendBroadcast(new Intent(PTService.ACTION_REMOVE_CONF_NOTIFICATION));
            }
        }
    }

    public static void showConfNotification() {
        if (!OsUtil.isAtLeastO()) {
            ZMServiceHelper.doServiceActionInFront(PTService.ACTION_SHOW_CONF_NOTIFICATION, PTService.class);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean(PTService.ARG_IN_MEETING, true);
        ZMServiceHelper.doServiceActionInFront(PTService.ACTION_DEAMON, bundle, PTService.class);
    }

    public static void showConfNotificationForSDK(@NonNull Context context) {
        Builder builder;
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.setAction(IntegrationActivity.ACTION_RETURN_TO_CONF);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
        String string = context.getString(C4558R.string.zm_app_name);
        String string2 = context.getString(C4558R.string.zm_msg_conf_in_progress);
        int i = C4558R.C4559drawable.zm_conf_notification;
        if (OsUtil.isAtLeastL()) {
            i = C4558R.C4559drawable.zm_conf_notification_5_0;
        }
        int color = context.getResources().getColor(C4558R.color.zm_notification_icon_bg);
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), C4558R.C4559drawable.zm_launcher);
        if (VideoBoxApplication.getInstance().isSDKMode()) {
            builder = new Builder(context.getApplicationContext());
            if (OsUtil.isAtLeastO()) {
                String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.SDK_CONF_NOTIFICATION_CHANNEL_ID, "");
                if (!StringUtil.isEmptyOrNull(readStringValue)) {
                    builder.setChannelId(readStringValue);
                } else {
                    builder = getNotificationCompatBuilder(context.getApplicationContext(), false);
                }
            }
        } else {
            builder = getNotificationCompatBuilder(context.getApplicationContext(), false);
        }
        builder.setWhen(0).setAutoCancel(false).setOngoing(true).setSmallIcon(i).setColor(color).setContentTitle(string).setContentText(string2).setOnlyAlertOnce(true).setContentIntent(activity);
        if (OsUtil.isAtLeastL() && context.getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
            builder.setLargeIcon(decodeResource);
        }
        ((NotificationManager) context.getSystemService("notification")).notify(4, builder.build());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00d9, code lost:
        if (android.text.TextUtils.isEmpty(r12) == false) goto L_0x00dd;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean showSipIncomeNotification(@androidx.annotation.Nullable android.content.Context r11, com.zipow.videobox.sip.server.NosSIPCallItem r12, boolean r13) {
        /*
            r0 = 0
            if (r11 != 0) goto L_0x0004
            return r0
        L_0x0004:
            if (r12 != 0) goto L_0x0007
            return r0
        L_0x0007:
            java.lang.String r1 = "notification"
            java.lang.Object r1 = r11.getSystemService(r1)
            android.app.NotificationManager r1 = (android.app.NotificationManager) r1
            if (r1 == 0) goto L_0x001e
            boolean r2 = p021us.zoom.androidlib.util.OsUtil.isAtLeastN()
            if (r2 == 0) goto L_0x001e
            boolean r2 = r1.areNotificationsEnabled()
            if (r2 != 0) goto L_0x001e
            return r0
        L_0x001e:
            android.content.Intent r2 = new android.content.Intent
            java.lang.Class<com.zipow.videobox.IntegrationActivity> r3 = com.zipow.videobox.IntegrationActivity.class
            r2.<init>(r11, r3)
            java.lang.String r3 = "sip_needInitModule"
            r2.putExtra(r3, r13)
            java.lang.String r3 = "ARG_NOS_SIP_CALL_ITEM"
            com.google.gson.Gson r4 = new com.google.gson.Gson
            r4.<init>()
            java.lang.String r4 = r4.toJson(r12)
            r2.putExtra(r3, r4)
            java.lang.String r3 = com.zipow.videobox.IntegrationActivity.ACTION_RETURN_TO_SIP_INCOME
            r2.setAction(r3)
            r3 = 268435456(0x10000000, float:2.5243549E-29)
            android.app.PendingIntent r2 = android.app.PendingIntent.getActivity(r11, r0, r2, r3)
            android.content.Intent r4 = new android.content.Intent
            java.lang.Class<com.zipow.videobox.IntegrationActivity> r5 = com.zipow.videobox.IntegrationActivity.class
            r4.<init>(r11, r5)
            java.lang.String r5 = com.zipow.videobox.IntegrationActivity.ACTION_RETURN_TO_SIP_ACCEPT
            r4.setAction(r5)
            java.lang.String r5 = "ARG_NOS_SIP_CALL_ITEM"
            r4.putExtra(r5, r12)
            android.app.PendingIntent r4 = android.app.PendingIntent.getActivity(r11, r0, r4, r3)
            androidx.core.app.NotificationCompat$Action r5 = new androidx.core.app.NotificationCompat$Action
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_btn_accept_sip_61381
            java.lang.String r6 = r11.getString(r6)
            r5.<init>(r0, r6, r4)
            android.content.Intent r4 = new android.content.Intent
            java.lang.Class<com.zipow.videobox.IntegrationActivity> r5 = com.zipow.videobox.IntegrationActivity.class
            r4.<init>(r11, r5)
            java.lang.String r5 = com.zipow.videobox.IntegrationActivity.ACTION_RETURN_TO_SIP_DECLINE
            r4.setAction(r5)
            java.lang.String r5 = "ARG_NOS_SIP_CALL_ITEM"
            r4.putExtra(r5, r12)
            android.app.PendingIntent r3 = android.app.PendingIntent.getActivity(r11, r0, r4, r3)
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_sip_btn_decline_61431
            java.lang.String r4 = r11.getString(r4)
            boolean r5 = r12.isCallQueue()
            if (r5 == 0) goto L_0x008a
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_sip_btn_skip_call_114844
            java.lang.String r4 = r11.getString(r4)
        L_0x008a:
            androidx.core.app.NotificationCompat$Action r5 = new androidx.core.app.NotificationCompat$Action
            r5.<init>(r0, r4, r3)
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_conf_notification
            int r4 = android.os.Build.VERSION.SDK_INT
            r5 = 21
            if (r4 < r5) goto L_0x0099
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_sip_notification_5_0
        L_0x0099:
            android.content.res.Resources r4 = r11.getResources()
            int r6 = p021us.zoom.videomeetings.C4558R.color.zm_notification_icon_bg
            int r4 = r4.getColor(r6)
            android.content.res.Resources r6 = r11.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_launcher
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeResource(r6, r7)
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_sip_call_title_111498
            java.lang.String r7 = r11.getString(r7)
            java.lang.String r8 = r12.getFromName()
            if (r13 != 0) goto L_0x00dc
            boolean r13 = android.text.TextUtils.isEmpty(r8)
            if (r13 != 0) goto L_0x00c9
            java.lang.String r13 = r12.getFrom()
            boolean r13 = r8.equals(r13)
            if (r13 == 0) goto L_0x00dc
        L_0x00c9:
            com.zipow.videobox.sip.ZMPhoneSearchHelper r13 = com.zipow.videobox.sip.ZMPhoneSearchHelper.getInstance()
            java.lang.String r12 = r12.getFrom()
            java.lang.String r12 = r13.getDisplayNameByNumber(r12)
            boolean r13 = android.text.TextUtils.isEmpty(r12)
            if (r13 != 0) goto L_0x00dc
            goto L_0x00dd
        L_0x00dc:
            r12 = r8
        L_0x00dd:
            int r13 = p021us.zoom.videomeetings.C4558R.string.zm_sip_incoming_call_text_111498
            java.lang.String r13 = r11.getString(r13)
            android.widget.RemoteViews r8 = new android.widget.RemoteViews
            java.lang.String r9 = r11.getPackageName()
            int r10 = p021us.zoom.videomeetings.C4558R.layout.notification_tip
            r8.<init>(r9, r10)
            int r9 = p021us.zoom.videomeetings.C4558R.C4560id.call_name
            r8.setTextViewText(r9, r12)
            int r12 = p021us.zoom.videomeetings.C4558R.C4560id.call_action
            r8.setTextViewText(r12, r13)
            android.content.Context r12 = r11.getApplicationContext()
            androidx.core.app.NotificationCompat$Builder r12 = getSipIncomeNotificationCompatBuilder(r12)
            androidx.core.app.NotificationCompat$Builder r12 = r12.setContent(r8)
            r8 = 0
            androidx.core.app.NotificationCompat$Builder r12 = r12.setWhen(r8)
            androidx.core.app.NotificationCompat$Builder r12 = r12.setSmallIcon(r3)
            androidx.core.app.NotificationCompat$Builder r12 = r12.setColor(r4)
            androidx.core.app.NotificationCompat$Builder r12 = r12.setContentTitle(r7)
            java.lang.String r13 = "call"
            androidx.core.app.NotificationCompat$Builder r12 = r12.setCategory(r13)
            androidx.core.app.NotificationCompat$Builder r12 = r12.setAutoCancel(r0)
            r13 = 1
            androidx.core.app.NotificationCompat$Builder r12 = r12.setOngoing(r13)
            androidx.core.app.NotificationCompat$Builder r12 = r12.setFullScreenIntent(r2, r13)
            boolean r0 = com.zipow.videobox.ptapp.PTSettingHelper.getPlayAlertSound()
            if (r0 == 0) goto L_0x0133
            r0 = 5
            r12.setDefaults(r0)
        L_0x0133:
            boolean r0 = com.zipow.videobox.ptapp.PTSettingHelper.getPlayAlertVibrate()
            if (r0 == 0) goto L_0x013e
            long[] r0 = VIBRATES
            r12.setVibrate(r0)
        L_0x013e:
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 < r5) goto L_0x0151
            android.content.res.Resources r11 = r11.getResources()
            int r0 = p021us.zoom.videomeetings.C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above
            boolean r11 = r11.getBoolean(r0)
            if (r11 == 0) goto L_0x0151
            r12.setLargeIcon(r6)
        L_0x0151:
            int r11 = android.os.Build.VERSION.SDK_INT
            if (r11 < r5) goto L_0x0158
            r12.setPriority(r13)
        L_0x0158:
            android.app.Notification r11 = r12.build()
            if (r1 == 0) goto L_0x0163
            r12 = 61
            r1.notify(r12, r11)     // Catch:{ Exception -> 0x0163 }
        L_0x0163:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.NotificationMgr.showSipIncomeNotification(android.content.Context, com.zipow.videobox.sip.server.NosSIPCallItem, boolean):boolean");
    }

    public static boolean showSipIncomeNotification(@Nullable Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        CmmSIPCallItem callItemByCallID = CmmSIPCallManager.getInstance().getCallItemByCallID(str);
        if (callItemByCallID == null) {
            return false;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null && OsUtil.isAtLeastN() && !notificationManager.areNotificationsEnabled()) {
            return false;
        }
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.putExtra(IntegrationActivity.ARG_SIP_CALL_ITEM_ID, callItemByCallID.getCallID());
        intent.setAction(IntegrationActivity.ACTION_RETURN_TO_SIP_INCOME);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
        Intent intent2 = new Intent(context, IntegrationActivity.class);
        intent2.setAction(IntegrationActivity.ACTION_RETURN_TO_SIP_ACCEPT);
        intent2.putExtra(IntegrationActivity.ARG_SIP_CALL_ITEM_ID, callItemByCallID.getCallID());
        new Action(0, context.getString(C4558R.string.zm_btn_accept_sip_61381), PendingIntent.getActivity(context, 0, intent2, 268435456));
        Intent intent3 = new Intent(context, IntegrationActivity.class);
        intent3.setAction(IntegrationActivity.ACTION_RETURN_TO_SIP_DECLINE);
        intent3.putExtra(IntegrationActivity.ARG_SIP_CALL_ITEM_ID, callItemByCallID.getCallID());
        PendingIntent activity2 = PendingIntent.getActivity(context, 1, intent3, 268435456);
        String string = context.getString(C4558R.string.zm_sip_btn_decline_61431);
        if (CmmSIPCallManager.getInstance().isCallQueue(str)) {
            string = context.getString(C4558R.string.zm_sip_btn_skip_call_114844);
        }
        new Action(0, string, activity2);
        int i = C4558R.C4559drawable.zm_conf_notification;
        if (VERSION.SDK_INT >= 21) {
            i = C4558R.C4559drawable.zm_sip_notification_5_0;
        }
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), C4558R.C4559drawable.zm_launcher);
        String string2 = context.getString(C4558R.string.zm_sip_call_title_111498);
        String displayName = CmmSIPCallManager.getInstance().getDisplayName(str);
        String string3 = context.getString(C4558R.string.zm_sip_incoming_call_text_111498);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), C4558R.layout.notification_tip);
        remoteViews.setTextViewText(C4558R.C4560id.call_name, displayName);
        remoteViews.setTextViewText(C4558R.C4560id.call_action, string3);
        Builder fullScreenIntent = getSipIncomeNotificationCompatBuilder(context.getApplicationContext()).setContent(remoteViews).setWhen(0).setSmallIcon(i).setContentTitle(string2).setCategory(NotificationCompat.CATEGORY_CALL).setAutoCancel(false).setOngoing(true).setFullScreenIntent(activity, true);
        if (VERSION.SDK_INT >= 21 && context.getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
            fullScreenIntent.setLargeIcon(decodeResource);
        }
        if (VERSION.SDK_INT >= 21) {
            fullScreenIntent.setPriority(1);
        }
        Notification build = fullScreenIntent.build();
        if (notificationManager != null) {
            try {
                notificationManager.notify(61, build);
            } catch (Exception unused) {
            }
        }
        return true;
    }

    public static void removeSipIncomeNotification(@Nullable Context context) {
        if (context != null) {
            removeNotification(context, 61);
        }
    }

    public static void showSipNotification(@Nullable Context context) {
        if (context != null) {
            Notification sipNotification = getSipNotification(context);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                try {
                    notificationManager.notify(6, sipNotification);
                } catch (Exception unused) {
                }
            }
        }
    }

    public static Notification getSipNotification(@Nullable Context context) {
        if (context == null) {
            return null;
        }
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.setAction(IntegrationActivity.ACTION_RETURN_TO_SIP);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
        String string = context.getString(C4558R.string.zm_sip_call_title_111498);
        int sipIdCountInCache = CmmSIPCallManager.getInstance().getSipIdCountInCache();
        String quantityString = context.getResources().getQuantityString(C4558R.plurals.zm_sip_calls_text_111498, sipIdCountInCache, new Object[]{Integer.valueOf(sipIdCountInCache)});
        int i = C4558R.C4559drawable.zm_conf_notification;
        if (VERSION.SDK_INT >= 21) {
            i = C4558R.C4559drawable.zm_sip_notification_5_0;
        }
        int color = context.getResources().getColor(C4558R.color.zm_notification_icon_bg);
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), C4558R.C4559drawable.zm_launcher);
        Builder ongoing = getSipInCallNotificationCompatBuilder(context.getApplicationContext()).setWhen(0).setSmallIcon(i).setColor(color).setContentTitle(string).setContentText(quantityString).setContentIntent(activity).setAutoCancel(false).setOnlyAlertOnce(true).setOngoing(true);
        if (VERSION.SDK_INT >= 21 && context.getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
            ongoing.setLargeIcon(decodeResource);
        }
        if (VERSION.SDK_INT >= 21) {
            ongoing.setPriority(0);
        }
        return ongoing.build();
    }

    public static boolean isNotificationChannelEnabled(@NonNull Context context) {
        return isNotificationChannelEnabled(context, getNotificationChannelId());
    }

    public static boolean isNotificationChannelEnabled(@NonNull Context context, @Nullable String str) {
        boolean areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();
        if (VERSION.SDK_INT < 26) {
            return areNotificationsEnabled;
        }
        getNotificationCompatBuilder(context);
        if (TextUtils.isEmpty(str)) {
            return areNotificationsEnabled;
        }
        NotificationChannel notificationChannel = ((NotificationManager) context.getSystemService("notification")).getNotificationChannel(str);
        if (notificationChannel != null) {
            return areNotificationsEnabled && notificationChannel.getImportance() != 0;
        }
        return areNotificationsEnabled;
    }

    public static void removeSipNotification(@Nullable Context context) {
        if (context != null) {
            removeNotification(context, 6);
        }
    }

    public static void showLoginExpiredNotification(@Nullable Context context, int i) {
        if (context != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            Intent intent = new Intent(context, IntegrationActivity.class);
            intent.setAction(IntegrationActivity.ACTION_LOGIN_EXPIRED);
            intent.putExtra(IntegrationActivity.ARG_LOGIN_TYPE, i);
            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
            String string = context.getString(C4558R.string.zm_app_name);
            Notification build = getNotificationCompatBuilder(context).setWhen(0).setSmallIcon(17301543).setContentTitle(string).setContentText(context.getString(C4558R.string.zm_msg_login_expired)).setContentIntent(activity).setAutoCancel(true).build();
            if (notificationManager != null) {
                try {
                    notificationManager.notify(5, build);
                } catch (Exception unused) {
                }
            }
        }
    }

    private static void showMeetingCallNotification(Context context, Intent intent, Object obj) {
        String str;
        String str2 = "";
        InvitationItem invitationItem = (InvitationItem) intent.getSerializableExtra("invitation");
        if (invitationItem != null) {
            str = invitationItem.getFromUserScreenName();
            if (!StringUtil.isEmptyOrNull(invitationItem.getGroupName())) {
                String groupName = invitationItem.getGroupName();
                int groupmembercount = invitationItem.getGroupmembercount();
                str2 = context.getString(C4558R.string.zm_msg_calling_group_54639, new Object[]{groupName, Integer.valueOf(groupmembercount)});
            } else {
                str2 = context.getString(C4558R.string.zm_msg_calling_11_54639);
            }
        } else if (!StringUtil.isEmptyOrNull(obj.toString())) {
            str = obj.toString();
            str2 = context.getString(C4558R.string.zm_msg_calling_11_54639);
        } else {
            str = intent.getStringExtra(IntegrationActivity.ARG_CALL_CAPTION);
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), C4558R.layout.notification_tip);
        remoteViews.setTextViewText(C4558R.C4560id.call_name, str);
        int i = C4558R.C4560id.call_action;
        if (StringUtil.isEmptyOrNull(str2)) {
            str2 = "";
        }
        remoteViews.setTextViewText(i, str2);
        Builder fullScreenIntent = getNotificationCompatBuilder(context).setContent(remoteViews).setWhen(0).setSmallIcon(C4558R.C4559drawable.zm_unread_message_5_0).setContentTitle(context.getString(C4558R.string.zm_app_name)).setPriority(1).setCategory(NotificationCompat.CATEGORY_CALL).setAutoCancel(true).setOngoing(false).setFullScreenIntent(PendingIntent.getActivity(context, 0, intent, 134217728), true);
        if (context.getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
            fullScreenIntent.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), C4558R.C4559drawable.zm_launcher));
        }
        Notification build = fullScreenIntent.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null) {
            try {
                notificationManager.notify(11, build);
            } catch (Exception unused) {
            }
        }
    }

    private static void showLoginNotification(Context context, Intent intent, Object obj) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), C4558R.layout.notification_tip);
        remoteViews.setTextViewText(C4558R.C4560id.call_name, context.getText(C4558R.string.zm_msg_notification_login_102727));
        Builder fullScreenIntent = getNotificationCompatBuilder(context).setContent(remoteViews).setWhen(0).setSmallIcon(C4558R.C4559drawable.zm_unread_message_5_0).setContentTitle(context.getString(C4558R.string.zm_app_name)).setPriority(1).setCategory(NotificationCompat.CATEGORY_CALL).setAutoCancel(true).setOngoing(false).setFullScreenIntent(PendingIntent.getActivity(context, 0, intent, 134217728), true);
        if (context.getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
            fullScreenIntent.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), C4558R.C4559drawable.zm_launcher));
        }
        Notification build = fullScreenIntent.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null) {
            try {
                notificationManager.notify(11, build);
            } catch (Exception unused) {
            }
        }
    }

    public static void removeLoginExpiredNotificaiton(@Nullable Context context) {
        removeNotification(context, 5);
    }

    public static void removeNotification(@Nullable Context context, int i) {
        if (context != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                try {
                    notificationManager.cancel(i);
                } catch (Exception unused) {
                }
            }
        }
    }

    @RequiresApi(api = 23)
    public static boolean isIncomingNotificationExist(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null) {
            if (OsUtil.isAtLeastN()) {
                return notificationManager.areNotificationsEnabled();
            }
            for (StatusBarNotification id : notificationManager.getActiveNotifications()) {
                if (id.getId() == 61) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean areNotificationsEnabled(@NonNull Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    @NonNull
    public static Builder getNotificationCompatBuilder(@NonNull Context context) {
        return getNotificationCompatBuilder(context, true);
    }

    @NonNull
    public static Builder getNotificationCompatBuilder(@NonNull Context context, boolean z) {
        String notificationChannelId = getNotificationChannelId();
        String string = context.getResources().getString(C4558R.string.zm_notification_channel_name_43235);
        int i = OsUtil.isAtLeastO() ? z ? 4 : 3 : 0;
        return getSipNotificationCompatBuilder(context, notificationChannelId, string, i);
    }

    @NonNull
    public static Builder getSipIncomeNotificationCompatBuilder(@NonNull Context context) {
        return getSipNotificationCompatBuilder(context, ZOOM_PHONE_INCOME_CALL_CHANNEL_ID, context.getResources().getString(C4558R.string.zm_notification_zoom_phone_income_111498), OsUtil.isAtLeastO() ? 4 : 0);
    }

    @NonNull
    public static Builder getSipInCallNotificationCompatBuilder(@NonNull Context context) {
        return getSipNotificationCompatBuilder(context, ZOOM_PHONE_INCALL_CHANNEL_ID, context.getResources().getString(C4558R.string.zm_notification_zoom_phone_incall_111498), OsUtil.isAtLeastO() ? 2 : 0);
    }

    @NonNull
    public static Builder getSipNotificationCompatBuilder(@NonNull Context context, String str, String str2, int i) {
        if (!OsUtil.isAtLeastO()) {
            return new Builder(context);
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(str);
        if (notificationChannel == null) {
            notificationChannel = new NotificationChannel(str, str2, i);
            notificationChannel.enableVibration(PTSettingHelper.getPlayAlertVibrate());
            if (notificationChannel.canShowBadge()) {
                notificationChannel.setShowBadge(false);
            }
        }
        notificationManager.createNotificationChannel(notificationChannel);
        return new Builder(context, str);
    }

    public static String getServiceNotificationChannelName(@Nullable Context context) {
        return context == null ? "" : context.getResources().getString(C4558R.string.zm_service_notification_channel_name_43235);
    }

    public static void startNotification(Context context, Intent intent, String str, Object obj) {
        if (str == null) {
            return;
        }
        if (str.equals(NotificationType.MEETING_CALL_NOTIFICATION.name())) {
            showMeetingCallNotification(context, intent, obj);
        } else if (str.equals(NotificationType.LOGIN_NOTIFICATION.name())) {
            showLoginNotification(context, intent, obj);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0132, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void showPBXMessageNotification(@androidx.annotation.Nullable android.content.Context r10, @androidx.annotation.NonNull java.lang.String r11, @androidx.annotation.Nullable java.lang.String r12, @androidx.annotation.Nullable com.zipow.videobox.util.NotificationMgr.NotificationItem r13) {
        /*
            java.lang.Class<com.zipow.videobox.util.NotificationMgr> r0 = com.zipow.videobox.util.NotificationMgr.class
            monitor-enter(r0)
            if (r10 == 0) goto L_0x0131
            if (r13 != 0) goto L_0x0009
            goto L_0x0131
        L_0x0009:
            java.lang.String r1 = "notification"
            java.lang.Object r1 = r10.getSystemService(r1)     // Catch:{ all -> 0x012e }
            android.app.NotificationManager r1 = (android.app.NotificationManager) r1     // Catch:{ all -> 0x012e }
            android.content.res.Resources r2 = r10.getResources()     // Catch:{ all -> 0x012e }
            int r3 = p021us.zoom.videomeetings.C4558R.color.zm_notification_icon_bg     // Catch:{ all -> 0x012e }
            int r2 = r2.getColor(r3)     // Catch:{ all -> 0x012e }
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_unread_message     // Catch:{ all -> 0x012e }
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x012e }
            r5 = 21
            if (r4 < r5) goto L_0x0025
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_unread_message_5_0     // Catch:{ all -> 0x012e }
        L_0x0025:
            android.content.res.Resources r4 = r10.getResources()     // Catch:{ all -> 0x012e }
            int r6 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_launcher     // Catch:{ all -> 0x012e }
            android.graphics.Bitmap r4 = android.graphics.BitmapFactory.decodeResource(r4, r6)     // Catch:{ all -> 0x012e }
            android.content.Intent r6 = new android.content.Intent     // Catch:{ all -> 0x012e }
            java.lang.Class<com.zipow.videobox.IntegrationActivity> r7 = com.zipow.videobox.IntegrationActivity.class
            r6.<init>(r10, r7)     // Catch:{ all -> 0x012e }
            java.lang.String r7 = com.zipow.videobox.IntegrationActivity.ACTION_PBX_SHOW_UNREAD_MESSAGE     // Catch:{ all -> 0x012e }
            r6.setAction(r7)     // Catch:{ all -> 0x012e }
            java.lang.String r7 = "pbxMessageSessionId"
            r6.putExtra(r7, r11)     // Catch:{ all -> 0x012e }
            boolean r7 = android.text.TextUtils.isEmpty(r12)     // Catch:{ all -> 0x012e }
            if (r7 != 0) goto L_0x004b
            java.lang.String r7 = "pbxMessageSessionProto"
            r6.putExtra(r7, r12)     // Catch:{ all -> 0x012e }
        L_0x004b:
            r12 = 268435456(0x10000000, float:2.5243549E-29)
            r6.setFlags(r12)     // Catch:{ all -> 0x012e }
            int r7 = r11.hashCode()     // Catch:{ all -> 0x012e }
            int r7 = r7 + 30000
            android.app.PendingIntent r12 = android.app.PendingIntent.getActivity(r10, r7, r6, r12)     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r6 = getNotificationCompatBuilder(r10)     // Catch:{ all -> 0x012e }
            r7 = 0
            androidx.core.app.NotificationCompat$Builder r6 = r6.setWhen(r7)     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r3 = r6.setSmallIcon(r3)     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r2 = r3.setColor(r2)     // Catch:{ all -> 0x012e }
            java.lang.String r3 = r13.getTitle()     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r2 = r2.setContentTitle(r3)     // Catch:{ all -> 0x012e }
            java.lang.CharSequence r3 = r13.getContent()     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r2 = r2.setContentText(r3)     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r12 = r2.setContentIntent(r12)     // Catch:{ all -> 0x012e }
            r2 = 1
            androidx.core.app.NotificationCompat$Builder r12 = r12.setVisibility(r2)     // Catch:{ all -> 0x012e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x012e }
            r3.<init>()     // Catch:{ all -> 0x012e }
            android.content.res.Resources r6 = r10.getResources()     // Catch:{ all -> 0x012e }
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_message_notifications_19898     // Catch:{ all -> 0x012e }
            java.lang.String r6 = r6.getString(r7)     // Catch:{ all -> 0x012e }
            r3.append(r6)     // Catch:{ all -> 0x012e }
            java.lang.String r6 = "\n"
            r3.append(r6)     // Catch:{ all -> 0x012e }
            java.lang.String r6 = r13.getTitle()     // Catch:{ all -> 0x012e }
            r3.append(r6)     // Catch:{ all -> 0x012e }
            java.lang.String r6 = "\n"
            r3.append(r6)     // Catch:{ all -> 0x012e }
            java.lang.CharSequence r13 = r13.getContent()     // Catch:{ all -> 0x012e }
            r3.append(r13)     // Catch:{ all -> 0x012e }
            java.lang.String r13 = r3.toString()     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r12 = r12.setTicker(r13)     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r12 = r12.setAutoCancel(r2)     // Catch:{ all -> 0x012e }
            androidx.core.app.NotificationCompat$Builder r12 = r12.setOnlyAlertOnce(r2)     // Catch:{ all -> 0x012e }
            int r13 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x012e }
            if (r13 < r5) goto L_0x00c6
            r12.setPriority(r2)     // Catch:{ all -> 0x012e }
        L_0x00c6:
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x012e }
            java.util.Map<java.lang.String, java.lang.Long> r13 = s_lastMessageSoundNotificationTimes     // Catch:{ all -> 0x012e }
            java.lang.Object r13 = r13.get(r11)     // Catch:{ all -> 0x012e }
            java.lang.Long r13 = (java.lang.Long) r13     // Catch:{ all -> 0x012e }
            if (r13 == 0) goto L_0x00e8
            long r6 = r13.longValue()     // Catch:{ all -> 0x012e }
            long r6 = r2 - r6
            r8 = 5000(0x1388, double:2.4703E-320)
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 > 0) goto L_0x00e8
            long r6 = r13.longValue()     // Catch:{ all -> 0x012e }
            int r13 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r13 >= 0) goto L_0x0101
        L_0x00e8:
            r13 = 0
            r12.setOnlyAlertOnce(r13)     // Catch:{ all -> 0x012e }
            boolean r13 = com.zipow.videobox.ptapp.PTSettingHelper.getPlayAlertSound()     // Catch:{ all -> 0x012e }
            if (r13 == 0) goto L_0x00f6
            r13 = 5
            r12.setDefaults(r13)     // Catch:{ all -> 0x012e }
        L_0x00f6:
            boolean r13 = com.zipow.videobox.ptapp.PTSettingHelper.getPlayAlertVibrate()     // Catch:{ all -> 0x012e }
            if (r13 == 0) goto L_0x0101
            long[] r13 = VIBRATES     // Catch:{ all -> 0x012e }
            r12.setVibrate(r13)     // Catch:{ all -> 0x012e }
        L_0x0101:
            java.util.Map<java.lang.String, java.lang.Long> r13 = s_lastMessageSoundNotificationTimes     // Catch:{ all -> 0x012e }
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x012e }
            r13.put(r11, r2)     // Catch:{ all -> 0x012e }
            int r13 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x012e }
            if (r13 < r5) goto L_0x011d
            android.content.res.Resources r10 = r10.getResources()     // Catch:{ all -> 0x012e }
            int r13 = p021us.zoom.videomeetings.C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above     // Catch:{ all -> 0x012e }
            boolean r10 = r10.getBoolean(r13)     // Catch:{ all -> 0x012e }
            if (r10 == 0) goto L_0x011d
            r12.setLargeIcon(r4)     // Catch:{ all -> 0x012e }
        L_0x011d:
            android.app.Notification r10 = r12.build()     // Catch:{ all -> 0x012e }
            if (r1 == 0) goto L_0x012c
            int r11 = r11.hashCode()     // Catch:{ Exception -> 0x012c }
            int r11 = r11 + 30000
            r1.notify(r11, r10)     // Catch:{ Exception -> 0x012c }
        L_0x012c:
            monitor-exit(r0)
            return
        L_0x012e:
            r10 = move-exception
            monitor-exit(r0)
            throw r10
        L_0x0131:
            monitor-exit(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.NotificationMgr.showPBXMessageNotification(android.content.Context, java.lang.String, java.lang.String, com.zipow.videobox.util.NotificationMgr$NotificationItem):void");
    }

    public static void removePBXMessageNotification(@Nullable Context context, @Nullable String str) {
        if (context != null && !StringUtil.isEmptyOrNull(str)) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                try {
                    notificationManager.cancel(str.hashCode() + ZOOM_PBX_MESSAGE_NOTIFICATION_ID_START);
                } catch (Exception unused) {
                }
            }
        }
    }
}
