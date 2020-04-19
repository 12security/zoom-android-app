package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import com.box.androidsdk.content.models.BoxFile;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMActionMsgUtil;
import java.util.HashMap;
import java.util.Map;
import p021us.zoom.androidlib.util.StringUtil;

public class ZoomLogEventTracking {
    public static final String ACTION_BOX = "Box";
    public static final String ACTION_DROPBOX = "Dropbox";
    public static final String ACTION_GOOGLE_DRIVE = "Google Drive";
    public static final String ACTION_NATIVE_FILES = "Local Files";
    public static final String ACTION_ONE_DRIVE = "Microsoft OneDrive";
    public static final String ACTION_ONE_DRIVE_BUSINESS = "Microsoft OneDrive for Business";
    private static final String CONTACTS = "Contacts";

    public static void eventTrackFileUpload(@NonNull String str, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        if (!StringUtil.isEmptyOrNull(str)) {
            hashMap.put(Integer.valueOf(50), str);
        }
        MonitorLogService.eventTrack(1, 4, 22, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackContactProfileVideoCall() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 5, 20, 30, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackContactProfileAudioCall() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 5, 28, 30, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackMMChat() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 5, 29, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackAddToContactsList() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 5, 27, 33, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackUnblockContact() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 5, 27, 32, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackBlockContact() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 5, 27, 31, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackClearPersonalNote() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 7, 35, 38, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSetPersonalNote() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 7, 35, 37, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSearch(@NonNull String str, String str2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str2);
            if (sessionById != null) {
                HashMap hashMap = new HashMap();
                hashMap.put(Integer.valueOf(42), Boolean.valueOf(sessionById.isGroup()));
                hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
                hashMap.put(Integer.valueOf(52), str);
                MonitorLogService.eventTrack(1, 3, 17, 27, (Map<Integer, Object>) hashMap);
            }
        }
    }

    public static void eventTrackJoinGroup() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 8, 38, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackEditMessage(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 3, 14, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSaveEmoji(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 3, 16, 25, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSendText(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(43), ZMActionMsgUtil.KEY_EVENT);
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 3, 7, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSendImage(@NonNull String str, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(49), str);
        MonitorLogService.eventTrack(1, 4, 23, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackCapturePhoto(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 4, 24, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackVideoCall(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        MonitorLogService.eventTrack(1, 4, 20, z ? 29 : 30, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackVoiceCall(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        MonitorLogService.eventTrack(1, 4, 21, z ? 29 : 30, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackNotificationSetting(@NonNull String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(true));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(47), str);
        MonitorLogService.eventTrack(1, 6, 31, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackBrowseContent(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null) {
                HashMap hashMap = new HashMap();
                hashMap.put(Integer.valueOf(42), Boolean.valueOf(sessionById.isGroup()));
                hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
                MonitorLogService.eventTrack(1, 6, 32, 36, (Map<Integer, Object>) hashMap);
            }
        }
    }

    public static void eventTrackClearHistory(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 6, 34, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSearch() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 9, 40, 39, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackStartNewChat() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 9, 39, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackJumpToChat(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 9, 40, 40, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackOpenSearchedContent() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 3, 19, 28, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackAddBuddy(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 6, 30, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackDownloadFile(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        MonitorLogService.eventTrack(1, 3, 15, 20, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSaveImage(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null) {
                HashMap hashMap = new HashMap();
                hashMap.put(Integer.valueOf(42), Boolean.valueOf(sessionById.isGroup()));
                hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
                MonitorLogService.eventTrack(1, 3, 16, 24, (Map<Integer, Object>) hashMap);
            }
        }
    }

    public static void eventTrackOpenSearchedMessage(boolean z, @NonNull String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(52), str);
        MonitorLogService.eventTrack(1, 3, 18, 27, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackPrivateGroup() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 8, 37, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackPublicGroup() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 8, 36, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackCancelDownload(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 3, 15, 21, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackDeleteMessage(boolean z, @NonNull String str, @NonNull String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        if (!StringUtil.isEmptyOrNull(str)) {
            hashMap.put(Integer.valueOf(43), str);
        }
        if (!StringUtil.isEmptyOrNull(str2)) {
            hashMap.put(Integer.valueOf(49), str2);
        }
        MonitorLogService.eventTrack(1, 9, 13, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackShare(boolean z, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        int i = "image".equals(str) ? 16 : BoxFile.TYPE.equals(str) ? 15 : -1;
        if (i != -1) {
            MonitorLogService.eventTrack(1, 9, i, 22, (Map<Integer, Object>) hashMap);
        }
    }

    public static void eventTrackOpenFile(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 3, 16, 23, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackOpenContent(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 6, 33, 36, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSelectGiphy(@NonNull String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(54), str);
        MonitorLogService.eventTrack(1, 4, 25, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSearchGiphy(@NonNull String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(52), str);
        MonitorLogService.eventTrack(1, 4, 25, 39, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackSelectEmoji(@NonNull String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(54), str);
        MonitorLogService.eventTrack(1, 4, 26, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackMarkUnread(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(z));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 3, 12, 59, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackRemoveContact() {
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(42), Boolean.valueOf(false));
        hashMap.put(Integer.valueOf(44), Boolean.valueOf(false));
        MonitorLogService.eventTrack(1, 5, 27, 34, (Map<Integer, Object>) hashMap);
    }

    public static void eventTrackHostEndMeeting(int i) {
        MonitorLogService.eventTrack(0, 1, 45, i, null);
    }

    public static void eventTrackSwitchTabToMeeting() {
        MonitorLogService.eventTrack(0, 10, 46, 48, null);
    }

    public static void eventTrackHostSearch(boolean z) {
        if (z) {
            HashMap hashMap = new HashMap();
            hashMap.put(Integer.valueOf(52), CONTACTS);
            MonitorLogService.eventTrack(1, 9, 17, 59, (Map<Integer, Object>) hashMap);
            return;
        }
        MonitorLogService.eventTrack(1, 9, 17, 59, null);
    }

    public static void eventTrackForceSignIn() {
        MonitorLogService.eventTrack(0, 1, 47, 59, null);
    }

    public static void eventTrackHandleAppDisclaimer(int i) {
        MonitorLogService.eventTrack(0, 2, 49, i, null);
    }
}
