package com.zipow.videobox.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.SearchMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMZoomFile;
import java.io.File;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class ZMIMUtils {
    public static boolean isE2EChat(String str) {
        boolean z = false;
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
        if (sessionById == null) {
            return false;
        }
        int e2eGetMyOption = zoomMessenger.e2eGetMyOption();
        if (e2eGetMyOption == 2) {
            return true;
        }
        if (sessionById.isGroup()) {
            ZoomGroup groupById = zoomMessenger.getGroupById(str);
            if (groupById != null) {
                z = groupById.isForceE2EGroup();
            }
        } else {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null && buddyWithJID.getE2EAbility(e2eGetMyOption) == 2) {
                z = true;
            }
        }
        return z;
    }

    public static boolean isMyNoteOn() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || zoomMessenger.myNotesGetOption() != 1) {
            return false;
        }
        return true;
    }

    public static boolean isAddContactDisable() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isAddContactDisable();
    }

    public static boolean isContactEmpty() {
        ABContactsCache instance = ABContactsCache.getInstance();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        if (zoomMessenger.getBuddyCount() == 0 && zoomMessenger.getGroupCount() == 0 && CollectionsUtil.isCollectionEmpty(instance.getAllCacheContacts())) {
            z = true;
        }
        return z;
    }

    public static boolean isFileTransferInReceiverDisable() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        if (2 == zoomMessenger.getFileTransferInReceiverOption()) {
            z = true;
        }
        return z;
    }

    public static void openUrl(@Nullable Context context, String str) {
        if (context != null && !StringUtil.isEmptyOrNull(str)) {
            Uri parse = Uri.parse(str);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(parse);
            try {
                ActivityStartHelper.startActivityForeground(context, intent);
            } catch (Exception unused) {
            }
        }
    }

    @Nullable
    public static MMZoomFile getMMZoomFile(@Nullable MMFileContentMgr mMFileContentMgr, String str, String str2, String str3) {
        ZoomFile zoomFile;
        if (mMFileContentMgr == null) {
            return null;
        }
        if (StringUtil.isEmptyOrNull(str2) || StringUtil.isEmptyOrNull(str)) {
            zoomFile = mMFileContentMgr.getFileWithWebFileID(str3);
        } else {
            zoomFile = mMFileContentMgr.getFileWithMessageID(str, str2);
            if (zoomFile == null) {
                return MMZoomFile.initWithMessage(str, str2);
            }
        }
        if (zoomFile != null) {
            return MMZoomFile.initWithZoomFile(zoomFile, mMFileContentMgr);
        }
        return null;
    }

    public static MMZoomFile getMMZoomFile(String str, String str2, String str3) {
        if (PTApp.getInstance().getZoomMessenger() == null) {
            return null;
        }
        return getMMZoomFile(PTApp.getInstance().getZoomFileContentMgr(), str, str2, str3);
    }

    public static boolean isFileDownloaded(String str, String str2, String str3) {
        MMZoomFile mMZoomFile = getMMZoomFile(str, str2, str3);
        if (mMZoomFile == null) {
            return false;
        }
        if (mMZoomFile.isFileDownloading()) {
            return true;
        }
        if (!mMZoomFile.isFileDownloaded() || StringUtil.isEmptyOrNull(mMZoomFile.getLocalPath()) || !new File(mMZoomFile.getLocalPath()).exists()) {
            return false;
        }
        return true;
    }

    public static boolean isGotoEnhancedFileTransfer(@Nullable MMMessageItem mMMessageItem) {
        boolean z = false;
        if (mMMessageItem == null || StringUtil.isEmptyOrNull(mMMessageItem.sessionId) || mMMessageItem.fileInfo == null || mMMessageItem.transferInfo == null || StringUtil.isEmptyOrNull(mMMessageItem.fileInfo.name)) {
            return false;
        }
        if (!(mMMessageItem.transferInfo.state == 13 || mMMessageItem.transferInfo.state == 4) || !isFileDownloaded(mMMessageItem.sessionId, mMMessageItem.messageXMPPId, mMMessageItem.fileId)) {
            z = true;
        }
        return z;
    }

    public static boolean isFileTransferResumeEnabled(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        return zoomMessenger != null && zoomMessenger.isFileTransferResumeEnabled(str);
    }

    public static boolean isReplyEnable() {
        return !isReplyDisabled();
    }

    public static boolean isReplyDisabled() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isDisableReply();
    }

    public static boolean isReactionEnable() {
        return !isReactionDisabled();
    }

    public static boolean isReactionDisabled() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isDisableReaction();
    }

    public static boolean isAnnouncement(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(str);
        if (groupById != null) {
            return groupById.isBroadcast();
        }
        return false;
    }

    public static boolean isZoomURL(String str) {
        return str.matches("https?://.+\\.zoom\\.us/[j|w]/.+");
    }

    public static boolean isZoomMeetingNo(@NonNull String str) {
        return !StringUtil.isEmptyOrNull(str) && str.matches("^[0-9]{9,11}$");
    }

    public static int getSearchMessageSortType() {
        SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
        if (searchMgr != null) {
            return searchMgr.getSearchMessageSortType();
        }
        return 2;
    }

    public static void setSearchMessageSortType(int i) {
        SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
        if (searchMgr != null) {
            searchMgr.setSearchMessageSortType(i);
        }
    }

    public static void axAnnounceForAccessibility(@NonNull View view, @NonNull String str) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(view.getContext())) {
            AccessibilityUtil.announceForAccessibilityCompat(view, (CharSequence) str);
        }
    }
}
