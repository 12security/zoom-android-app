package com.zipow.videobox.util;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxSharedLink;
import com.microsoft.aad.adal.AuthenticationConstants;
import com.zipow.cmmlib.AppContext;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.fragment.InviteFragment;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.view.ScheduledMeetingItem;
import java.util.HashMap;
import java.util.TimeZone;
import org.apache.http.message.TokenParser;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.template.Template;
import p021us.zoom.videomeetings.C4558R;

public class MeetingInvitationUtil {
    private static final String TAG = "MeetingInvitationUtil";

    @Nullable
    public static String buildEmailInvitationContent(@Nullable Context context, @Nullable MeetingInfoProto meetingInfoProto, boolean z) {
        if (context == null || meetingInfoProto == null) {
            return null;
        }
        return buildEmailInvitationContent(context, ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto), z);
    }

    @Nullable
    public static String buildEmailInvitationContent(@Nullable Context context, @Nullable ScheduledMeetingItem scheduledMeetingItem, boolean z) {
        Object obj;
        Context context2 = context;
        Object obj2 = null;
        if (context2 == null || scheduledMeetingItem == null) {
            return null;
        }
        String invitationEmailContent = !z ? scheduledMeetingItem.getInvitationEmailContent() : null;
        if (StringUtil.isEmptyOrNull(invitationEmailContent)) {
            invitationEmailContent = scheduledMeetingItem.getInvitationEmailContentWithTime();
        }
        if (!StringUtil.isEmptyOrNull(invitationEmailContent)) {
            return invitationEmailContent;
        }
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return null;
        }
        String queryWithKey = new AppContext(AppContext.PREFER_NAME_CHAT).queryWithKey(ConfigReader.KEY_WEBSERVER, AppContext.APP_NAME_CHAT);
        if (StringUtil.isEmptyOrNull(queryWithKey)) {
            queryWithKey = ZMDomainUtil.getWebDomainWithHttps();
        }
        String formatConfNumber = StringUtil.formatConfNumber(scheduledMeetingItem.getMeetingNo(), (char) TokenParser.f498SP);
        String valueOf = String.valueOf(scheduledMeetingItem.getMeetingNo());
        String joinMeetingUrlForInvite = scheduledMeetingItem.getJoinMeetingUrlForInvite();
        String valueOf2 = String.valueOf(scheduledMeetingItem.isPSTNEnabled());
        String callInNumber = scheduledMeetingItem.getCallInNumber();
        Object[] h323Gateways = scheduledMeetingItem.getH323Gateways();
        String str = "false";
        if (h323Gateways == null || h323Gateways.length <= 0) {
            obj = null;
        } else {
            str = "true";
            obj = h323Gateways[0];
            if (h323Gateways.length > 1) {
                obj2 = h323Gateways[1];
            }
        }
        Template template = new Template(loadTemplate(context));
        HashMap hashMap = new HashMap();
        hashMap.put(IntegrationActivity.ARG_USERNAME, currentUserProfile.getUserName());
        hashMap.put("meetingUrl", joinMeetingUrlForInvite);
        hashMap.put("webServer", queryWithKey);
        hashMap.put(InviteFragment.ARG_MEETING_NUMBER, formatConfNumber);
        hashMap.put("number", valueOf);
        hashMap.put("enablePSTN", valueOf2);
        hashMap.put("usCallInNumber", callInNumber);
        hashMap.put("accessCode", formatConfNumber);
        hashMap.put("enableH323", str);
        if (obj != null) {
            hashMap.put("h323Gateway1", obj);
        }
        if (obj2 != null) {
            hashMap.put("h323Gateway2", obj2);
        }
        if (!scheduledMeetingItem.isRecurring() && z) {
            StringBuilder sb = new StringBuilder();
            sb.append(TimeFormatUtil.formatDateTime(context2, scheduledMeetingItem.getStartTime(), true, false));
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(TimeZone.getDefault().getID());
            sb.append("(");
            sb.append(getTimeZoneOffset());
            sb.append(")");
            hashMap.put("meetingTime", sb.toString());
        }
        if (scheduledMeetingItem.hasPassword()) {
            hashMap.put(BoxSharedLink.FIELD_PASSWORD, scheduledMeetingItem.getPassword());
        }
        return template.format(hashMap);
    }

    @NonNull
    private static String getTimeZoneOffset() {
        int rawOffset = TimeZone.getDefault().getRawOffset();
        int i = (rawOffset / 1000) / AuthenticationConstants.DEFAULT_EXPIRATION_TIME_SEC;
        int i2 = ((rawOffset - ((i * 1000) * AuthenticationConstants.DEFAULT_EXPIRATION_TIME_SEC)) / 1000) / 60;
        if (i == 0 && i2 == 0) {
            return "GMT";
        }
        return String.format(CompatUtils.getLocalDefault(), "GMT%+d:%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r6 == null) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r6.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String loadTemplate(android.content.Context r6) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            android.content.res.Resources r6 = r6.getResources()     // Catch:{ Exception -> 0x0065 }
            int r1 = p021us.zoom.videomeetings.C4558R.raw.zm_invitation_email_template     // Catch:{ Exception -> 0x0065 }
            java.io.InputStream r6 = r6.openRawResource(r1)     // Catch:{ Exception -> 0x0065 }
            r1 = 0
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x0052 }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x0052 }
            r3.<init>(r6)     // Catch:{ Throwable -> 0x0052 }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x0052 }
            r3 = r1
        L_0x001b:
            java.lang.String r4 = r2.readLine()     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
            if (r4 == 0) goto L_0x002b
            if (r3 == 0) goto L_0x0028
            r3 = 10
            r0.append(r3)     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
        L_0x0028:
            r0.append(r4)     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
        L_0x002b:
            if (r4 != 0) goto L_0x0036
            r2.close()     // Catch:{ Throwable -> 0x0052 }
            if (r6 == 0) goto L_0x0065
            r6.close()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0065
        L_0x0036:
            r3 = r4
            goto L_0x001b
        L_0x0038:
            r3 = move-exception
            r4 = r1
            goto L_0x0041
        L_0x003b:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x003d }
        L_0x003d:
            r4 = move-exception
            r5 = r4
            r4 = r3
            r3 = r5
        L_0x0041:
            if (r4 == 0) goto L_0x004c
            r2.close()     // Catch:{ Throwable -> 0x0047 }
            goto L_0x004f
        L_0x0047:
            r2 = move-exception
            r4.addSuppressed(r2)     // Catch:{ Throwable -> 0x0052 }
            goto L_0x004f
        L_0x004c:
            r2.close()     // Catch:{ Throwable -> 0x0052 }
        L_0x004f:
            throw r3     // Catch:{ Throwable -> 0x0052 }
        L_0x0050:
            r2 = move-exception
            goto L_0x0054
        L_0x0052:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0050 }
        L_0x0054:
            if (r6 == 0) goto L_0x0064
            if (r1 == 0) goto L_0x0061
            r6.close()     // Catch:{ Throwable -> 0x005c }
            goto L_0x0064
        L_0x005c:
            r6 = move-exception
            r1.addSuppressed(r6)     // Catch:{ Exception -> 0x0065 }
            goto L_0x0064
        L_0x0061:
            r6.close()     // Catch:{ Exception -> 0x0065 }
        L_0x0064:
            throw r2     // Catch:{ Exception -> 0x0065 }
        L_0x0065:
            java.lang.String r6 = r0.toString()
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.MeetingInvitationUtil.loadTemplate(android.content.Context):java.lang.String");
    }

    public static boolean copyInviteURL(Activity activity) {
        if (activity == null) {
            return false;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null) {
            return false;
        }
        String joinMeetingUrl = meetingItem.getJoinMeetingUrl();
        if (joinMeetingUrl == null) {
            return false;
        }
        long meetingNumber = meetingItem.getMeetingNumber();
        CmmUser myself = ConfMgr.getInstance().getMyself();
        String screenName = myself != null ? myself.getScreenName() : null;
        String password = meetingItem.getPassword();
        String rawMeetingPassword = confContext.getRawMeetingPassword();
        HashMap hashMap = new HashMap();
        hashMap.put("joinMeetingUrl", joinMeetingUrl);
        hashMap.put(InviteFragment.ARG_MEETING_ID, String.valueOf(meetingNumber));
        String format = new Template(activity.getString(C4558R.string.zm_msg_meeting_url_for_copy_to_clipboard)).format(hashMap);
        try {
            String genCopyUrlText = ((InviteContentGenerator) Class.forName(ResourcesUtil.getString((Context) activity, C4558R.string.zm_config_invite_content_generator)).newInstance()).genCopyUrlText(VideoBoxApplication.getInstance(), meetingNumber, joinMeetingUrl, screenName, password, rawMeetingPassword);
            if (!StringUtil.isEmptyOrNull(genCopyUrlText)) {
                format = genCopyUrlText;
            }
        } catch (Exception unused) {
        }
        return AndroidAppUtil.copyText(activity, format);
    }
}
