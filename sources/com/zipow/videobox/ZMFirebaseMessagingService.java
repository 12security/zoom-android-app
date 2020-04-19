package com.zipow.videobox;

import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.nos.NOSMgr;
import com.zipow.videobox.util.PreferenceUtil;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class ZMFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "ZMFirebaseMessagingService";
    private static final long TIMEOUT = 300000;
    private static long lastNormalMsgTimeStamp;

    public static class PushLogger {
        private static final String LOG_FILE = "ZmFCMPush.log";
        @NonNull
        private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());

        private static void logWithAndroidJavaLogger(int i, String str, String str2, Throwable th) {
        }

        private static File getLogFile() {
            StringBuilder sb = new StringBuilder();
            sb.append(AppUtil.getLogParentPath());
            sb.append(File.separator);
            sb.append("logs");
            sb.append(File.separator);
            sb.append(LOG_FILE);
            File file = new File(sb.toString());
            if (!file.exists()) {
                try {
                    File parentFile = file.getParentFile();
                    if (parentFile == null) {
                        return null;
                    }
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file.createNewFile();
                } catch (IOException unused) {
                    return null;
                }
            }
            return file;
        }

        private static void log(int i, String str, String str2, @Nullable Throwable th) {
            logWithAndroidJavaLogger(i, str, str2, th);
            try {
                File logFile = getLogFile();
                if (logFile != null) {
                    PrintWriter printWriter = new PrintWriter(new FileWriter(logFile, true));
                    StringBuilder sb = new StringBuilder();
                    sb.append(format.format(new Date()));
                    sb.append(OAuth.SCOPE_DELIMITER);
                    switch (i) {
                        case 1:
                            sb.append("Info");
                            break;
                        case 2:
                            sb.append("Warning");
                            break;
                        case 3:
                            sb.append("ERROR");
                            break;
                        case 4:
                            sb.append("ERROR_REPORT");
                            break;
                        case 5:
                            sb.append("FATAL");
                            break;
                        default:
                            sb.append("Debug");
                            break;
                    }
                    sb.append(OAuth.SCOPE_DELIMITER);
                    sb.append(str);
                    sb.append(OAuth.SCOPE_DELIMITER);
                    sb.append(str2);
                    printWriter.write(sb.toString());
                    printWriter.println();
                    if (th != null) {
                        th.printStackTrace(printWriter);
                    }
                    printWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* renamed from: e */
        public static void m251e(String str, String str2) {
            log(3, str, str2, null);
        }

        /* renamed from: e */
        public static void m252e(String str, Throwable th, String str2) {
            log(3, str, str2, th);
        }

        /* renamed from: i */
        public static void m253i(String str, String str2) {
            log(1, str, str2, null);
        }
    }

    public void onNewToken(String str) {
        PreferenceUtil.saveStringValue(PreferenceUtil.FCM_REGISTRATION_TOKEN, str);
        PreferenceUtil.saveIntValue(PreferenceUtil.FCM_REGISTRATION_VERSION_CODE, AndroidAppUtil.getAppVersionCode(this));
        if (Looper.getMainLooper() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    NOSMgr.getInstance().register();
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:104:0x0237  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x026c  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x0271 A[SYNTHETIC, Splitter:B:117:0x0271] */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0278  */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x027f  */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x02bd  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x021b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMessageReceived(@androidx.annotation.NonNull com.google.firebase.messaging.RemoteMessage r21) {
        /*
            r20 = this;
            r8 = r20
            java.util.Map r0 = r21.getData()
            if (r0 == 0) goto L_0x02f7
            int r1 = r0.size()
            if (r1 <= 0) goto L_0x02f7
            com.zipow.videobox.util.PreferenceUtil.initialize(r20)
            java.lang.String r1 = "account_login"
            r2 = 0
            boolean r1 = com.zipow.videobox.util.PreferenceUtil.readBooleanValue(r1, r2)
            if (r1 != 0) goto L_0x001b
            return
        L_0x001b:
            java.lang.String r1 = "pbxbody"
            boolean r1 = r0.containsKey(r1)
            if (r1 == 0) goto L_0x0053
            java.lang.String r1 = "pbxbody"
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r2 != 0) goto L_0x0052
            java.lang.String r2 = "incoming_call"
            boolean r2 = r1.contains(r2)
            if (r2 != 0) goto L_0x0041
            java.lang.String r2 = "cancel_call"
            boolean r2 = r1.contains(r2)
            if (r2 == 0) goto L_0x0052
        L_0x0041:
            java.lang.String r2 = "time"
            java.lang.Object r0 = r0.get(r2)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 == 0) goto L_0x0052
            long r2 = java.lang.Long.parseLong(r0)     // Catch:{ Exception -> 0x0052 }
            r8.parseSipCall(r1, r2)     // Catch:{ Exception -> 0x0052 }
        L_0x0052:
            return
        L_0x0053:
            java.lang.String r1 = "phoneMessage"
            boolean r1 = r0.containsKey(r1)
            if (r1 == 0) goto L_0x007f
            java.lang.String r1 = "phoneMessage"
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            java.lang.String r2 = "mb"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            java.lang.String r3 = "sendername"
            java.lang.Object r3 = r0.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "senderid"
            java.lang.Object r0 = r0.get(r4)
            java.lang.String r0 = (java.lang.String) r0
            com.zipow.videobox.sip.server.CmmPBXMessageNotificationManager.onNewMessageReceived(r8, r0, r3, r2, r1)
            return
        L_0x007f:
            java.lang.String r1 = "forceDisableGCM"
            boolean r1 = com.zipow.videobox.util.PreferenceUtil.readBooleanValue(r1, r2)
            if (r1 == 0) goto L_0x0088
            return
        L_0x0088:
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()     // Catch:{ Exception -> 0x0099 }
            com.zipow.videobox.ptapp.mm.ZoomMessenger r1 = r1.getZoomMessenger()     // Catch:{ Exception -> 0x0099 }
            if (r1 == 0) goto L_0x0099
            boolean r1 = r1.isConnectionGood()     // Catch:{ Exception -> 0x0099 }
            if (r1 == 0) goto L_0x0099
            return
        L_0x0099:
            java.lang.String r1 = "caption"
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            java.lang.String r3 = "body"
            java.lang.Object r3 = r0.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "time"
            java.lang.Object r4 = r0.get(r4)
            java.lang.String r4 = (java.lang.String) r4
            java.lang.String r5 = "xmppmt"
            java.lang.Object r5 = r0.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r6 = "mb"
            java.lang.Object r6 = r0.get(r6)
            java.lang.String r6 = (java.lang.String) r6
            java.lang.String r7 = "sendername"
            java.lang.Object r7 = r0.get(r7)
            java.lang.String r7 = (java.lang.String) r7
            java.lang.String r9 = "senderid"
            java.lang.Object r9 = r0.get(r9)
            java.lang.String r9 = (java.lang.String) r9
            java.lang.String r10 = "badge"
            java.lang.Object r10 = r0.get(r10)
            java.lang.String r10 = (java.lang.String) r10
            java.lang.String r11 = "addContact"
            java.lang.Object r11 = r0.get(r11)
            java.lang.String r11 = (java.lang.String) r11
            java.lang.String r12 = "action-loc-key"
            java.lang.Object r12 = r0.get(r12)
            java.lang.String r12 = (java.lang.String) r12
            java.lang.String r12 = "loc-key"
            java.lang.Object r12 = r0.get(r12)
            java.lang.String r12 = (java.lang.String) r12
            java.lang.String r13 = "loc-args"
            java.lang.Object r13 = r0.get(r13)
            java.lang.String r13 = (java.lang.String) r13
            java.lang.String r14 = "available-alert"
            java.lang.Object r0 = r0.get(r14)
            java.lang.String r0 = (java.lang.String) r0
            boolean r14 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r12)
            if (r14 != 0) goto L_0x01fd
            boolean r14 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r13)
            if (r14 != 0) goto L_0x01fd
            java.lang.String r14 = "%[123][$]@"
            java.util.regex.Pattern r2 = java.util.regex.Pattern.compile(r14)     // Catch:{ Exception -> 0x01f8 }
            java.util.regex.Matcher r2 = r2.matcher(r12)     // Catch:{ Exception -> 0x01f8 }
            org.json.JSONArray r15 = new org.json.JSONArray     // Catch:{ Exception -> 0x01f8 }
            r15.<init>(r13)     // Catch:{ Exception -> 0x01f8 }
            boolean r2 = r2.find()     // Catch:{ Exception -> 0x01f8 }
            if (r2 == 0) goto L_0x01ed
            int r2 = r15.length()     // Catch:{ Exception -> 0x01f8 }
            r18 = r6
            r6 = 1
            if (r2 != r6) goto L_0x0154
            r2 = 0
            java.lang.Object r17 = r15.get(r2)     // Catch:{ Exception -> 0x0151 }
            java.lang.String r2 = r17.toString()     // Catch:{ Exception -> 0x0151 }
            java.lang.String r7 = r12.trim()     // Catch:{ Exception -> 0x014e }
            java.lang.String r12 = "%s"
            java.lang.String r7 = r7.replaceAll(r14, r12)     // Catch:{ Exception -> 0x014e }
            java.lang.Object[] r12 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x014e }
            r6 = 0
            java.lang.Object r14 = r15.get(r6)     // Catch:{ Exception -> 0x014e }
            r12[r6] = r14     // Catch:{ Exception -> 0x014e }
            java.lang.String r6 = java.lang.String.format(r7, r12)     // Catch:{ Exception -> 0x014e }
            r7 = r2
            goto L_0x01f5
        L_0x014e:
            r7 = r2
            goto L_0x0203
        L_0x0151:
            goto L_0x0203
        L_0x0154:
            int r2 = r15.length()     // Catch:{ Exception -> 0x01fa }
            r6 = 2
            if (r2 != r6) goto L_0x0185
            r2 = 0
            java.lang.Object r19 = r15.get(r2)     // Catch:{ Exception -> 0x0151 }
            java.lang.String r2 = r19.toString()     // Catch:{ Exception -> 0x0151 }
            java.lang.String r7 = r12.trim()     // Catch:{ Exception -> 0x014e }
            java.lang.String r12 = "%s"
            java.lang.String r7 = r7.replaceAll(r14, r12)     // Catch:{ Exception -> 0x014e }
            java.lang.Object[] r12 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x014e }
            r6 = 0
            java.lang.Object r14 = r15.get(r6)     // Catch:{ Exception -> 0x014e }
            r12[r6] = r14     // Catch:{ Exception -> 0x014e }
            r6 = 1
            java.lang.Object r14 = r15.get(r6)     // Catch:{ Exception -> 0x014e }
            r12[r6] = r14     // Catch:{ Exception -> 0x014e }
            java.lang.String r6 = java.lang.String.format(r7, r12)     // Catch:{ Exception -> 0x014e }
            r7 = r2
            goto L_0x01f5
        L_0x0185:
            int r2 = r15.length()     // Catch:{ Exception -> 0x01fa }
            r6 = 3
            if (r2 != r6) goto L_0x01ea
            java.lang.String r2 = ":"
            java.lang.String[] r2 = r12.split(r2)     // Catch:{ Exception -> 0x01fa }
            int r6 = r2.length     // Catch:{ Exception -> 0x01fa }
            if (r6 <= 0) goto L_0x01be
            r6 = 0
            r12 = r2[r6]     // Catch:{ Exception -> 0x01fa }
            java.lang.String r6 = r12.trim()     // Catch:{ Exception -> 0x01fa }
            java.lang.String r12 = "[$]@"
            r19 = r7
            java.lang.String r7 = "$s"
            java.lang.String r6 = r6.replaceAll(r12, r7)     // Catch:{ Exception -> 0x01e7 }
            r7 = 2
            java.lang.Object[] r12 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x01e7 }
            r7 = 0
            java.lang.Object r16 = r15.get(r7)     // Catch:{ Exception -> 0x01e7 }
            r12[r7] = r16     // Catch:{ Exception -> 0x01e7 }
            r7 = 1
            java.lang.Object r17 = r15.get(r7)     // Catch:{ Exception -> 0x01e7 }
            r12[r7] = r17     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r7 = java.lang.String.format(r6, r12)     // Catch:{ Exception -> 0x01e7 }
            r19 = r7
            goto L_0x01c0
        L_0x01be:
            r19 = r7
        L_0x01c0:
            int r6 = r2.length     // Catch:{ Exception -> 0x01e7 }
            r7 = 1
            if (r6 <= r7) goto L_0x01e2
            r2 = r2[r7]     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r2 = r2.trim()     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r6 = "%s"
            java.lang.String r2 = r2.replaceAll(r14, r6)     // Catch:{ Exception -> 0x01e7 }
            java.lang.Object[] r6 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x01e7 }
            r7 = 2
            java.lang.Object r12 = r15.get(r7)     // Catch:{ Exception -> 0x01e7 }
            r7 = 0
            r6[r7] = r12     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r2 = java.lang.String.format(r2, r6)     // Catch:{ Exception -> 0x01e7 }
            r6 = r2
            r7 = r19
            goto L_0x01f5
        L_0x01e2:
            r6 = r18
            r7 = r19
            goto L_0x01f5
        L_0x01e7:
            r7 = r19
            goto L_0x0203
        L_0x01ea:
            r19 = r7
            goto L_0x01f1
        L_0x01ed:
            r18 = r6
            r19 = r7
        L_0x01f1:
            r6 = r18
            r7 = r19
        L_0x01f5:
            r18 = r6
            goto L_0x0203
        L_0x01f8:
            r18 = r6
        L_0x01fa:
            r19 = r7
            goto L_0x0203
        L_0x01fd:
            r18 = r6
            r19 = r7
            r7 = r19
        L_0x0203:
            boolean r2 = android.text.TextUtils.isEmpty(r7)
            if (r2 != 0) goto L_0x0211
            java.lang.String r2 = "[\"\"]"
            boolean r2 = r7.equals(r2)
            if (r2 == 0) goto L_0x0213
        L_0x0211:
            java.lang.String r7 = ""
        L_0x0213:
            java.lang.String r2 = "[\""
            boolean r2 = r7.startsWith(r2)
            if (r2 == 0) goto L_0x022e
            java.lang.String r2 = "\"]"
            boolean r2 = r7.endsWith(r2)
            if (r2 == 0) goto L_0x022e
            int r2 = r7.length()
            r6 = 2
            int r2 = r2 - r6
            java.lang.String r2 = r7.substring(r6, r2)
            goto L_0x022f
        L_0x022e:
            r2 = r7
        L_0x022f:
            java.lang.String r6 = "true"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L_0x026c
            if (r13 == 0) goto L_0x026a
            java.lang.String r0 = "[\""
            boolean r0 = r13.startsWith(r0)
            if (r0 == 0) goto L_0x026a
            java.lang.String r0 = "\"]"
            boolean r0 = r13.endsWith(r0)
            if (r0 == 0) goto L_0x026a
            int r0 = r13.length()
            r6 = 2
            int r0 = r0 - r6
            java.lang.String r0 = r13.substring(r6, r0)
            com.zipow.videobox.VideoBoxApplication r6 = com.zipow.videobox.VideoBoxApplication.getInstance()
            if (r6 == 0) goto L_0x0268
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_alert_when_available_notification_65420
            r12 = 1
            java.lang.Object[] r13 = new java.lang.Object[r12]
            r14 = 0
            r13[r14] = r0
            java.lang.String r0 = r6.getString(r7, r13)
            r18 = r0
            goto L_0x026d
        L_0x0268:
            r12 = 1
            goto L_0x026d
        L_0x026a:
            r12 = 1
            goto L_0x026d
        L_0x026c:
            r12 = 1
        L_0x026d:
            r6 = 0
            if (r5 == 0) goto L_0x0278
            int r15 = java.lang.Integer.parseInt(r5)     // Catch:{ Exception -> 0x0276 }
            goto L_0x0279
        L_0x0276:
            r15 = 1
            goto L_0x0279
        L_0x0278:
            r15 = 1
        L_0x0279:
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            if (r0 == 0) goto L_0x02bd
            if (r10 == 0) goto L_0x0286
            int r0 = java.lang.Integer.parseInt(r10)     // Catch:{ Exception -> 0x0288 }
            goto L_0x0287
        L_0x0286:
            r0 = 1
        L_0x0287:
            r12 = r0
        L_0x0288:
            if (r12 != 0) goto L_0x0293
            r0 = 0
            r1 = 0
            p021us.zoom.androidlib.util.UIUtil.setNotificationMessageCount(r8, r0, r1)
            com.zipow.videobox.util.NotificationMgr.removeAllMessageNotificationMM(r20)
            return
        L_0x0293:
            java.lang.String r0 = "true"
            boolean r6 = r0.equalsIgnoreCase(r11)
            r0 = r20
            r1 = r15
            r3 = r18
            r4 = r12
            r5 = r9
            com.zipow.videobox.util.NotificationMgr$NotificationItem r6 = com.zipow.videobox.util.NotificationMgr.generateNotificationItem(r0, r1, r2, r3, r4, r5, r6)
            if (r6 == 0) goto L_0x02b6
            r1 = 1
            r2 = 0
            java.lang.String r0 = "true"
            boolean r7 = r0.equalsIgnoreCase(r11)
            r0 = r20
            r4 = r12
            r5 = r9
            com.zipow.videobox.util.NotificationMgr.showMessageNotificationMMImpl(r0, r1, r2, r4, r5, r6, r7)
        L_0x02b6:
            long r0 = java.lang.System.currentTimeMillis()
            lastNormalMsgTimeStamp = r0
            goto L_0x02f7
        L_0x02bd:
            if (r4 == 0) goto L_0x02c3
            long r6 = java.lang.Long.parseLong(r4)     // Catch:{ Exception -> 0x02c3 }
        L_0x02c3:
            long r4 = java.lang.System.currentTimeMillis()
            long r4 = r4 - r6
            r6 = 300000(0x493e0, double:1.482197E-318)
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 >= 0) goto L_0x02f7
            java.lang.String r0 = r8.parseFormUser(r3)
            android.content.Intent r2 = new android.content.Intent
            java.lang.Class<com.zipow.videobox.IntegrationActivity> r4 = com.zipow.videobox.IntegrationActivity.class
            r2.<init>(r8, r4)
            r4 = 268435456(0x10000000, float:2.5243549E-29)
            r2.setFlags(r4)
            java.lang.String r4 = com.zipow.videobox.IntegrationActivity.ACTION_NOS_CALL
            r2.setAction(r4)
            java.lang.String r4 = "callCaption"
            r2.putExtra(r4, r1)
            java.lang.String r1 = "callBody"
            r2.putExtra(r1, r3)
            com.zipow.videobox.util.NotificationMgr$NotificationType r1 = com.zipow.videobox.util.NotificationMgr.NotificationType.MEETING_CALL_NOTIFICATION
            java.lang.String r1 = r1.name()
            com.zipow.videobox.util.ActivityStartHelper.startActivity(r8, r2, r1, r0)
        L_0x02f7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ZMFirebaseMessagingService.onMessageReceived(com.google.firebase.messaging.RemoteMessage):void");
    }

    public static long getLastNormalMsgTimeStamp() {
        return lastNormalMsgTimeStamp;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:137:0x033c, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0347, code lost:
        if (r5 != null) goto L_0x0334;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x033c A[ExcHandler: all (th java.lang.Throwable), Splitter:B:9:0x0020] */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x0342 A[SYNTHETIC, Splitter:B:141:0x0342] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseSipCall(@androidx.annotation.Nullable java.lang.String r22, long r23) {
        /*
            r21 = this;
            r1 = r21
            r2 = r23
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            boolean r0 = r0.isSDKMode()
            if (r0 == 0) goto L_0x000f
            return
        L_0x000f:
            boolean r0 = android.text.TextUtils.isEmpty(r22)
            if (r0 == 0) goto L_0x0016
            return
        L_0x0016:
            r4 = 0
            java.io.ByteArrayInputStream r5 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0346, all -> 0x033e }
            byte[] r0 = r22.getBytes()     // Catch:{ Exception -> 0x0346, all -> 0x033e }
            r5.<init>(r0)     // Catch:{ Exception -> 0x0346, all -> 0x033e }
            javax.xml.parsers.DocumentBuilderFactory r0 = javax.xml.parsers.DocumentBuilderFactory.newInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r4 = 0
            r0.setExpandEntityReferences(r4)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            javax.xml.parsers.DocumentBuilder r0 = r0.newDocumentBuilder()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            org.w3c.dom.Document r0 = r0.parse(r5)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 == 0) goto L_0x0338
            org.w3c.dom.NodeList r6 = r0.getChildNodes()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r6 != 0) goto L_0x003a
            goto L_0x0338
        L_0x003a:
            org.w3c.dom.Node r0 = r0.getFirstChild()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            org.w3c.dom.Node r0 = r0.getFirstChild()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            org.w3c.dom.Element r0 = (org.w3c.dom.Element) r0     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r6 = "action"
            java.lang.String r6 = r0.getAttribute(r6)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r7 = "incoming_call"
            boolean r7 = r6.equals(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r8 = 2
            r9 = 1000(0x3e8, double:4.94E-321)
            r11 = 1
            if (r7 == 0) goto L_0x0228
            org.w3c.dom.NodeList r0 = r0.getChildNodes()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.NosSIPCallItem r6 = new com.zipow.videobox.sip.server.NosSIPCallItem     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r6.<init>()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r7 = 0
        L_0x0060:
            int r12 = r0.getLength()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r7 >= r12) goto L_0x013d
            org.w3c.dom.Node r12 = r0.item(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r13 = r12.getNodeName()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r14 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x0076
            goto L_0x0139
        L_0x0076:
            java.lang.String r12 = r12.getTextContent()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r14 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x0082
            goto L_0x0139
        L_0x0082:
            java.lang.String r14 = "extensionId"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x008f
            r6.setExtensionId(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x008f:
            java.lang.String r14 = "serverId"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x009c
            r6.setServerId(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x009c:
            java.lang.String r14 = "from"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x00a9
            r6.setFrom(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x00a9:
            java.lang.String r14 = "fromName"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x00b6
            r6.setFromName(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x00b6:
            java.lang.String r14 = "to"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x00c3
            r6.setTo(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x00c3:
            java.lang.String r14 = "sid"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x00d0
            r6.setSid(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x00d0:
            java.lang.String r14 = "domainName"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x00dc
            r6.setDomainName(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x00dc:
            java.lang.String r14 = "timestamp"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x00ee
            long r12 = java.lang.Long.parseLong(r12)     // Catch:{ Exception -> 0x0139, all -> 0x033c }
            long r12 = r12 * r9
            r6.setTimestamp(r12)     // Catch:{ Exception -> 0x0139, all -> 0x033c }
            goto L_0x0139
        L_0x00ee:
            java.lang.String r14 = "siplb"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x00fa
            r6.setSiplb(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x00fa:
            java.lang.String r14 = "traceId"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x0106
            r6.setTraceId(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x0106:
            java.lang.String r14 = "calledNumber"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x0112
            r6.setCalledNumber(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x0112:
            java.lang.String r14 = "thirdtype"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x0122
            int r12 = com.zipow.videobox.sip.server.NosSIPCallItem.parseThirdType(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r6.setThirdtype(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x0122:
            java.lang.String r14 = "thirdname"
            boolean r14 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r14 == 0) goto L_0x012e
            r6.setThirdname(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0139
        L_0x012e:
            java.lang.String r14 = "thirdnumber"
            boolean r13 = r14.equals(r13)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r13 == 0) goto L_0x0139
            r6.setThirdnumber(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
        L_0x0139:
            int r7 = r7 + 1
            goto L_0x0060
        L_0x013d:
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            long r12 = r6.getTimestamp()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            long r9 = r9 - r12
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            long r12 = r12 - r2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.<init>()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r7 = "[ZMFirebaseMessagingService]parseSipCall,incoming_call,xmpp:"
            r0.append(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = ",xmpp elapse:"
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.append(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = ",pbx:"
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            long r2 = r6.getTimestamp()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = ",pbx elapse:"
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.append(r9)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r18 = r0.toString()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.CmmSIPNosManager r14 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r15 = 0
            java.lang.String r16 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r17 = r6.getTraceId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r19 = r9
            r14.printPushCallLog(r15, r16, r17, r18, r19)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r0 = r6.getFrom()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 != 0) goto L_0x0224
            java.lang.String r0 = r6.getDomainName()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 != 0) goto L_0x0224
            java.lang.String r0 = r6.getServerId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 != 0) goto L_0x0224
            java.lang.String r0 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 == 0) goto L_0x01b3
            goto L_0x0224
        L_0x01b3:
            boolean r0 = com.zipow.videobox.sip.server.CmmSIPCallManager.isInit()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 == 0) goto L_0x01d3
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r3 = r6.getTraceId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r7 = "[ZMFirebaseMessagingService]parseSipCall,CmmSIPCallManager.isInit()"
            r0.printPushCallLog(r4, r2, r3, r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.handleDuplicateCheckIncomingPushCall(r6)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0334
        L_0x01d3:
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r3 = r6.getTraceId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r7 = "[ZMFirebaseMessagingService]parseSipCall,not CmmSIPCallManager.isInit()"
            r0.printPushCallLog(r4, r2, r3, r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r0 = p021us.zoom.androidlib.util.OsUtil.isAtLeastO()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 == 0) goto L_0x01ed
            r1.scheduler(r1, r11)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
        L_0x01ed:
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r0 = r0.handleIncomingPushCallInBG(r6)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 == 0) goto L_0x0211
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.onNewNosCallInBG()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r3 = r6.getTraceId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r6 = "[ZMFirebaseMessagingService]parseSipCall,handleIncomingPushCallInBG"
            r0.printPushCallLog(r4, r2, r3, r6)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0334
        L_0x0211:
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r3 = r6.getTraceId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r4 = "[ZMFirebaseMessagingService]parseSipCall,handleIncomingPushCallInBG"
            r0.printPushCallLog(r8, r2, r3, r4)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0334
        L_0x0224:
            r5.close()     // Catch:{ IOException -> 0x0227 }
        L_0x0227:
            return
        L_0x0228:
            java.lang.String r7 = "cancel_call"
            boolean r6 = r6.equals(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r6 == 0) goto L_0x0334
            org.w3c.dom.NodeList r0 = r0.getChildNodes()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.NosCancelSIPCallItem r6 = new com.zipow.videobox.sip.server.NosCancelSIPCallItem     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r6.<init>()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
        L_0x0239:
            int r7 = r0.getLength()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r4 >= r7) goto L_0x029b
            org.w3c.dom.Node r7 = r0.item(r4)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r12 = r7.getNodeName()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r7 = r7.getTextContent()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r13 = "sid"
            boolean r13 = r13.equals(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r13 == 0) goto L_0x0257
            r6.setSid(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0298
        L_0x0257:
            java.lang.String r13 = "reason"
            boolean r13 = r13.equals(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r13 == 0) goto L_0x0263
            r6.setReason(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0298
        L_0x0263:
            java.lang.String r13 = "platformType"
            boolean r13 = r13.equals(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r13 == 0) goto L_0x026f
            r6.setPlatformType(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0298
        L_0x026f:
            java.lang.String r13 = "platformInstanceId"
            boolean r13 = r13.equals(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r13 == 0) goto L_0x027b
            r6.setPlatformInstanceId(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0298
        L_0x027b:
            java.lang.String r13 = "traceId"
            boolean r13 = r13.equals(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r13 == 0) goto L_0x0287
            r6.setTraceId(r7)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0298
        L_0x0287:
            java.lang.String r13 = "timestamp"
            boolean r12 = r13.equals(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r12 == 0) goto L_0x0298
            long r12 = java.lang.Long.parseLong(r7)     // Catch:{ Exception -> 0x0298, all -> 0x033c }
            long r12 = r12 * r9
            r6.setTimestamp(r12)     // Catch:{ Exception -> 0x0298, all -> 0x033c }
        L_0x0298:
            int r4 = r4 + 1
            goto L_0x0239
        L_0x029b:
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            long r12 = r6.getTimestamp()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            long r9 = r9 - r12
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            long r12 = r12 - r2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.<init>()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r4 = "[ZMFirebaseMessagingService]parseSipCall,cancel_call,xmpp:"
            r0.append(r4)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = ",xmpp elapse:"
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.append(r12)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = ",pbx:"
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            long r2 = r6.getTimestamp()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = ",pbx elapse:"
            r0.append(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.append(r9)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r18 = r0.toString()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.CmmSIPNosManager r14 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r15 = 0
            java.lang.String r16 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r17 = r6.getTraceId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r19 = r9
            r14.printPushCallLog(r15, r16, r17, r18, r19)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.addNosSIPCallItemCancelled(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r0 = r0.isCancelNosSIPCall(r6)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 == 0) goto L_0x0323
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            r0.cancelNosSIPCall(r2)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r3 = r6.getTraceId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r4 = "[ZMFirebaseMessagingService]parseSipCall,CmmSIPNosManager.getInstance().isCancelNosSIPCall(item)"
            r0.printPushCallLog(r11, r2, r3, r4)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            boolean r0 = p021us.zoom.androidlib.util.OsUtil.isAtLeastO()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            if (r0 == 0) goto L_0x0334
            r1.scheduler(r1, r8)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            goto L_0x0334
        L_0x0323:
            com.zipow.videobox.sip.server.CmmSIPNosManager r0 = com.zipow.videobox.sip.server.CmmSIPNosManager.getInstance()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r2 = r6.getSid()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r3 = r6.getTraceId()     // Catch:{ Exception -> 0x0347, all -> 0x033c }
            java.lang.String r4 = "[ZMFirebaseMessagingService]parseSipCall, not CmmSIPNosManager.getInstance().isCancelNosSIPCall(item)"
            r0.printPushCallLog(r11, r2, r3, r4)     // Catch:{ Exception -> 0x0347, all -> 0x033c }
        L_0x0334:
            r5.close()     // Catch:{ IOException -> 0x034a }
            goto L_0x034a
        L_0x0338:
            r5.close()     // Catch:{ IOException -> 0x033b }
        L_0x033b:
            return
        L_0x033c:
            r0 = move-exception
            goto L_0x0340
        L_0x033e:
            r0 = move-exception
            r5 = r4
        L_0x0340:
            if (r5 == 0) goto L_0x0345
            r5.close()     // Catch:{ IOException -> 0x0345 }
        L_0x0345:
            throw r0
        L_0x0346:
            r5 = r4
        L_0x0347:
            if (r5 == 0) goto L_0x034a
            goto L_0x0334
        L_0x034a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ZMFirebaseMessagingService.parseSipCall(java.lang.String, long):void");
    }

    private String parseFormUser(String str) {
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return "";
        }
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        String str2 = "";
        String[] split = str.split(";");
        if (split.length > 7) {
            str2 = split[7];
        }
        return str2;
    }

    @RequiresApi(api = 26)
    private void scheduler(@Nullable Context context, int i) {
        if (OsUtil.isAtLeastO() && context != null) {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JobScheduler.class);
            Builder builder = new Builder(i, new ComponentName(context, PBXJobService.class));
            builder.setOverrideDeadline(100);
            if (jobScheduler != null) {
                jobScheduler.schedule(builder.build());
            }
        }
    }
}
