package com.zipow.videobox.ptapp;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.LauncherActivity;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.nos.NOSMgr;
import com.zipow.videobox.ptapp.IMProtos.BuddyUserInfo;
import com.zipow.videobox.ptapp.IMProtos.CallAvailableInfo;
import com.zipow.videobox.ptapp.IMProtos.GroupCallBackInfo;
import com.zipow.videobox.ptapp.IMProtos.RoomEditInfo;
import com.zipow.videobox.ptapp.IMProtos.SessionMessageInfoMap;
import com.zipow.videobox.ptapp.PTAppProtos.ChangedBuddyGroups;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTAppProtos.UserProfileResult;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZMSessionsMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.SIPConfiguration;
import com.zipow.videobox.util.AlertWhenAvailableHelper;
import com.zipow.videobox.util.MMMessageHelper.MessageSyncer;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.view.p014mm.sticker.StickerManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ForegroundTaskManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ZoomMessengerUI {
    public static final int CONNECTION_STATUS_CONNECTED = 1;
    public static final int CONNECTION_STATUS_CONNECTING = 2;
    public static final int CONNECTION_STATUS_DISCONNECTED = 0;
    public static final int CONNECTION_STATUS_UNKNOWN = -1;
    private static final String TAG = "ZoomMessengerUI";
    @Nullable
    private static ZoomMessengerUI instance;
    private int mConnectionStatus = -1;
    @NonNull
    private Handler mHandler = new Handler();
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface IZoomMessengerUIListener extends IListener {
        void Confirm_HistoryReqComplete(String str, String str2, int i, int i2);

        void E2E_MessageStateUpdate(String str, String str2, int i);

        void E2E_MyStateUpdate(int i);

        void E2E_NotifyAutoLogoff();

        void E2E_SessionStateUpdate(String str, String str2, int i, int i2);

        void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3);

        void FT_OnDownloadByFileIDTimeOut(String str, String str2);

        void FT_OnDownloadByMsgIDTimeOut(String str, String str2);

        void FT_OnProgress(String str, String str2, int i, long j, long j2);

        void FT_OnResumed(String str, String str2, int i);

        void FT_OnSent(String str, String str2, int i);

        void FT_UploadFileInChatTimeOut(String str, String str2);

        void FT_UploadToMyList_OnProgress(String str, int i, int i2, int i3);

        void FT_UploadToMyList_TimeOut(String str);

        void Indicate_AddAvailableAlert(String str, boolean z);

        void Indicate_AvailableAlert(String str, String str2);

        void Indicate_BlockedUsersAdded(List<String> list);

        void Indicate_BlockedUsersRemoved(List<String> list);

        void Indicate_BlockedUsersUpdated();

        void Indicate_BuddyAccountStatusChange(String str, int i);

        void Indicate_BuddyAdded(String str, List<String> list);

        void Indicate_BuddyGroupAdded(String str);

        void Indicate_BuddyGroupInfoUpdated(String str);

        void Indicate_BuddyGroupMembersAdded(String str, List<String> list);

        void Indicate_BuddyGroupMembersChanged(ChangedBuddyGroups changedBuddyGroups, boolean z);

        void Indicate_BuddyGroupMembersRemoved(String str, List<String> list);

        void Indicate_BuddyGroupMembersUpdated(String str, List<String> list);

        void Indicate_BuddyGroupsRemoved(List<String> list);

        void Indicate_BuddyPresenceChanged(String str);

        void Indicate_DownloadFileByUrlIml(String str, int i);

        void Indicate_DownloadGIFFromGiphyResultIml(int i, String str, String str2, String str3, String str4, String str5);

        void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z);

        void Indicate_FetchUserProfileResult(UserProfileResult userProfileResult);

        void Indicate_FileActionStatus(int i, String str, String str2, String str3, String str4, String str5);

        void Indicate_FileAttachInfoUpdate(String str, String str2, int i);

        void Indicate_FileDeleted(String str, String str2, int i);

        void Indicate_FileDownloaded(String str, String str2, int i);

        void Indicate_FileForwarded(String str, String str2, String str3, String str4, int i);

        void Indicate_FileMessageDeleted(String str, String str2);

        void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i);

        void Indicate_FileStatusUpdated(String str);

        void Indicate_FileUnshared(String str, String str2, int i);

        void Indicate_GetAllAvailableAlert();

        void Indicate_GetContactsPresence(List<String> list, List<String> list2);

        void Indicate_GetGIFFromGiphyResultIml(int i, String str, List<String> list, String str2, String str3);

        void Indicate_GetHotGiphyInfoResult(int i, String str, List<String> list, String str2, String str3);

        void Indicate_LoginOfflineMessageFinished();

        void Indicate_MessageContext(int i, String str, String str2, List<String> list);

        void Indicate_MessageDeleted(String str, String str2);

        void Indicate_MobileOnlineBuddiesFromDB(List<String> list);

        void Indicate_NewFileSharedByOthers(String str);

        void Indicate_NewPersonalFile(String str);

        void Indicate_OnlineBuddies(List<String> list);

        void Indicate_OutgoingCallAction(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z);

        void Indicate_PreviewDownloaded(String str, String str2, int i);

        void Indicate_QueryAllFilesResponse(String str, int i, List<String> list, long j, long j2);

        void Indicate_QueryFilesSharedWithMeResponse(String str, int i, List<String> list, long j, long j2);

        void Indicate_QueryMyFilesResponse(String str, int i, List<String> list, long j, long j2);

        void Indicate_QuerySessionFilesResponse(String str, String str2, int i, List<String> list, long j, long j2);

        void Indicate_RemoveAvailableAlert(String str, boolean z);

        void Indicate_RenameFileResponse(int i, String str, String str2, String str3);

        void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, long j, long j2, boolean z);

        void Indicate_SendAddonCommandResultIml(String str, boolean z);

        void Indicate_SessionOfflineMessageFinished(String str);

        void Indicate_SignatureSet(String str, int i);

        void Indicate_SyncAvailableAlert(String str);

        void Indicate_TPV2_GetContactsPresence(List<String> list, List<String> list2);

        void Indicate_TPV2_SubscribePresence(List<String> list);

        void Indicate_TPV2_WillExpirePresence(List<String> list, int i);

        void Indicate_UploadToMyFiles_Sent(String str, String str2, int i);

        void Indicate_VCardInfoReady(String str);

        void NotifyCallUnavailable(String str, long j);

        void NotifyChatAvailableInfoUpdateIml(String str);

        void NotifyChatUnavailable(String str, String str2);

        void NotifyDeleteMsgFailed(String str, String str2);

        void NotifyEditMsgFailed(String str, String str2);

        void NotifyIMWebSettingUpdated(int i);

        void NotifyInfoBarriesMsg(String str, String str2);

        void NotifyLocalAddressChanged(String str, String str2);

        void NotifyOutdatedHistoryRemoved(List<String> list, long j);

        void NotifyPersonalGroupSync(int i, String str, List<String> list, String str2, String str3);

        void Notify_BroadcastsReady();

        void Notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap);

        void Notify_ChatSessionUnreadCountReady(List<String> list);

        void Notify_DBLoadSessionLastMessagesDone();

        void Notify_SubscribeRequestSent(String str, int i);

        void Notify_SubscriptionIsRestrict(String str, boolean z);

        void Notify_SubscriptionIsRestrictV2(String str, int i);

        void OnPersonalGroupResponse(byte[] bArr);

        void On_AddLocalPendingBuddy(String str, int i, String str2);

        void On_AssignGroupAdmins(int i, String str, String str2, List<String> list, long j);

        void On_BroadcastUpdate(int i, String str, boolean z);

        void On_DestroyGroup(int i, String str, String str2, String str3, long j);

        void On_MyPresenceChanged(int i, int i2);

        void On_NotifyGroupDestroy(String str, String str2, long j);

        void ZoomPrensece_OnUserOptionUpated();

        void confirm_EditedFileDownloadedIml(int i, Map<String, String> map);

        void indicate_BuddyBlockedByIB(List<String> list);

        void indicate_CallActionRespondedIml(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z);

        void notifyStarSessionDataUpdate();

        void notifyWebSipStatus(SIPConfiguration sIPConfiguration);

        void notify_ChatSessionResetUnreadCount(String str);

        void notify_StarMessageDataUpdate();

        void notify_StarMessagesData(String str, int i, byte[] bArr);

        void onAddBuddy(String str, int i, String str2);

        void onAddBuddyByEmail(String str, int i);

        void onBeginConnect();

        void onConfirmFileDownloaded(String str, String str2, int i);

        void onConfirmPreviewPicFileDownloaded(String str, String str2, int i);

        void onConfirm_MessageSent(String str, String str2, int i);

        void onConnectReturn(int i);

        void onGroupAction(int i, GroupAction groupAction, String str);

        void onIndicateBuddyInfoUpdated(String str);

        void onIndicateBuddyListUpdated();

        void onIndicateIMCMDReceivedImpl(String str, String str2, String str3, long j, int i);

        void onIndicateInfoUpdatedWithJID(String str);

        void onIndicateInputStateChanged(String str, int i);

        boolean onIndicateMessageReceived(String str, String str2, String str3);

        void onIndicate_BuddyBigPictureDownloaded(String str, int i);

        void onNotifyBuddyJIDUpgrade(String str, String str2, String str3);

        boolean onNotifySubscribeRequest(String str, String str2);

        void onNotifySubscribeRequestUpdated(String str);

        boolean onNotifySubscriptionAccepted(String str);

        boolean onNotifySubscriptionDenied(String str);

        void onNotifyUnsubscribeRequest(String str, String str2);

        void onNotify_ChatSessionListUpdate();

        void onNotify_ChatSessionUnreadUpdate(String str);

        void onNotify_ChatSessionUpdate(String str);

        void onNotify_JIDUpdated();

        void onNotify_MUCGroupInfoUpdatedImpl(String str);

        void onNotify_SessionMarkUnreadCtx(String str, int i, String str2, List<String> list);

        void onQueryJidByEmail(String str, int i);

        void onReceivedCall(String str, String str2, InvitationItem invitationItem);

        void onRemoveBuddy(String str, int i);

        void onSearchBuddy(String str, int i);

        void onSearchBuddyByKey(String str, int i);

        void onSearchBuddyByKeyV2(String str, String str2, String str3, int i);

        void onSearchBuddyPicDownloaded(String str);
    }

    public static abstract class SimpleZoomMessengerUIListener implements IZoomMessengerUIListener {
        public void Confirm_HistoryReqComplete(String str, String str2, int i, int i2) {
        }

        public void E2E_MessageStateUpdate(String str, String str2, int i) {
        }

        public void E2E_MyStateUpdate(int i) {
        }

        public void E2E_NotifyAutoLogoff() {
        }

        public void E2E_SessionStateUpdate(String str, String str2, int i, int i2) {
        }

        public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        }

        public void FT_OnDownloadByFileIDTimeOut(String str, String str2) {
        }

        public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
        }

        public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
        }

        public void FT_OnResumed(String str, String str2, int i) {
        }

        public void FT_OnSent(String str, String str2, int i) {
        }

        public void FT_UploadFileInChatTimeOut(String str, String str2) {
        }

        public void FT_UploadToMyList_OnProgress(String str, int i, int i2, int i3) {
        }

        public void FT_UploadToMyList_TimeOut(String str) {
        }

        public void Indicate_AddAvailableAlert(String str, boolean z) {
        }

        public void Indicate_AvailableAlert(String str, String str2) {
        }

        public void Indicate_BlockedUsersAdded(List<String> list) {
        }

        public void Indicate_BlockedUsersRemoved(List<String> list) {
        }

        public void Indicate_BlockedUsersUpdated() {
        }

        public void Indicate_BuddyAccountStatusChange(String str, int i) {
        }

        public void Indicate_BuddyAdded(String str, List<String> list) {
        }

        public void Indicate_BuddyGroupAdded(String str) {
        }

        public void Indicate_BuddyGroupInfoUpdated(String str) {
        }

        public void Indicate_BuddyGroupMembersAdded(String str, List<String> list) {
        }

        public void Indicate_BuddyGroupMembersChanged(ChangedBuddyGroups changedBuddyGroups, boolean z) {
        }

        public void Indicate_BuddyGroupMembersRemoved(String str, List<String> list) {
        }

        public void Indicate_BuddyGroupMembersUpdated(String str, List<String> list) {
        }

        public void Indicate_BuddyGroupsRemoved(List<String> list) {
        }

        public void Indicate_BuddyPresenceChanged(String str) {
        }

        public void Indicate_DownloadFileByUrlIml(String str, int i) {
        }

        public void Indicate_DownloadGIFFromGiphyResultIml(int i, String str, String str2, String str3, String str4, String str5) {
        }

        public void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z) {
        }

        public void Indicate_FetchUserProfileResult(UserProfileResult userProfileResult) {
        }

        public void Indicate_FileActionStatus(int i, String str, String str2, String str3, String str4, String str5) {
        }

        public void Indicate_FileAttachInfoUpdate(String str, String str2, int i) {
        }

        public void Indicate_FileDeleted(String str, String str2, int i) {
        }

        public void Indicate_FileDownloaded(String str, String str2, int i) {
        }

        public void Indicate_FileForwarded(String str, String str2, String str3, String str4, int i) {
        }

        public void Indicate_FileMessageDeleted(String str, String str2) {
        }

        public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
        }

        public void Indicate_FileStatusUpdated(String str) {
        }

        public void Indicate_FileUnshared(String str, String str2, int i) {
        }

        public void Indicate_GetAllAvailableAlert() {
        }

        public void Indicate_GetContactsPresence(List<String> list, List<String> list2) {
        }

        public void Indicate_GetGIFFromGiphyResultIml(int i, String str, List<String> list, String str2, String str3) {
        }

        public void Indicate_GetHotGiphyInfoResult(int i, String str, List<String> list, String str2, String str3) {
        }

        public void Indicate_LoginOfflineMessageFinished() {
        }

        public void Indicate_MessageContext(int i, String str, String str2, List<String> list) {
        }

        public void Indicate_MessageDeleted(String str, String str2) {
        }

        public void Indicate_MobileOnlineBuddiesFromDB(List<String> list) {
        }

        public void Indicate_NewFileSharedByOthers(String str) {
        }

        public void Indicate_NewPersonalFile(String str) {
        }

        public void Indicate_OnlineBuddies(List<String> list) {
        }

        public void Indicate_OutgoingCallAction(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        }

        public void Indicate_PreviewDownloaded(String str, String str2, int i) {
        }

        public void Indicate_QueryAllFilesResponse(String str, int i, List<String> list, long j, long j2) {
        }

        public void Indicate_QueryFilesSharedWithMeResponse(String str, int i, List<String> list, long j, long j2) {
        }

        public void Indicate_QueryMyFilesResponse(String str, int i, List<String> list, long j, long j2) {
        }

        public void Indicate_QuerySessionFilesResponse(String str, String str2, int i, List<String> list, long j, long j2) {
        }

        public void Indicate_RemoveAvailableAlert(String str, boolean z) {
        }

        public void Indicate_RenameFileResponse(int i, String str, String str2, String str3) {
        }

        public void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, long j, long j2, boolean z) {
        }

        public void Indicate_SendAddonCommandResultIml(String str, boolean z) {
        }

        public void Indicate_SessionOfflineMessageFinished(String str) {
        }

        public void Indicate_SignatureSet(String str, int i) {
        }

        public void Indicate_SyncAvailableAlert(String str) {
        }

        public void Indicate_TPV2_GetContactsPresence(List<String> list, List<String> list2) {
        }

        public void Indicate_TPV2_SubscribePresence(List<String> list) {
        }

        public void Indicate_TPV2_WillExpirePresence(List<String> list, int i) {
        }

        public void Indicate_UploadToMyFiles_Sent(String str, String str2, int i) {
        }

        public void Indicate_VCardInfoReady(String str) {
        }

        public void NotifyCallUnavailable(String str, long j) {
        }

        public void NotifyChatAvailableInfoUpdateIml(String str) {
        }

        public void NotifyChatUnavailable(String str, String str2) {
        }

        public void NotifyDeleteMsgFailed(String str, String str2) {
        }

        public void NotifyEditMsgFailed(String str, String str2) {
        }

        public void NotifyIMWebSettingUpdated(int i) {
        }

        public void NotifyInfoBarriesMsg(String str, String str2) {
        }

        public void NotifyLocalAddressChanged(String str, String str2) {
        }

        public void NotifyOutdatedHistoryRemoved(List<String> list, long j) {
        }

        public void NotifyPersonalGroupSync(int i, String str, List<String> list, String str2, String str3) {
        }

        public void Notify_BroadcastsReady() {
        }

        public void Notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap) {
        }

        public void Notify_ChatSessionUnreadCountReady(List<String> list) {
        }

        public void Notify_DBLoadSessionLastMessagesDone() {
        }

        public void Notify_SubscribeRequestSent(String str, int i) {
        }

        public void Notify_SubscriptionIsRestrict(String str, boolean z) {
        }

        public void Notify_SubscriptionIsRestrictV2(String str, int i) {
        }

        public void OnPersonalGroupResponse(byte[] bArr) {
        }

        public void On_AddLocalPendingBuddy(String str, int i, String str2) {
        }

        public void On_AssignGroupAdmins(int i, String str, String str2, List<String> list, long j) {
        }

        public void On_BroadcastUpdate(int i, String str, boolean z) {
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
        }

        public void On_MyPresenceChanged(int i, int i2) {
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
        }

        public void ZoomPrensece_OnUserOptionUpated() {
        }

        public void confirm_EditedFileDownloadedIml(int i, Map<String, String> map) {
        }

        public void indicate_BuddyBlockedByIB(List<String> list) {
        }

        public void indicate_CallActionRespondedIml(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        }

        public void notifyStarSessionDataUpdate() {
        }

        public void notifyWebSipStatus(SIPConfiguration sIPConfiguration) {
        }

        public void notify_ChatSessionResetUnreadCount(String str) {
        }

        public void notify_StarMessageDataUpdate() {
        }

        public void notify_StarMessagesData(String str, int i, byte[] bArr) {
        }

        public void onAddBuddy(String str, int i, String str2) {
        }

        public void onAddBuddyByEmail(String str, int i) {
        }

        public void onBeginConnect() {
        }

        public void onConfirmFileDownloaded(String str, String str2, int i) {
        }

        public void onConfirmPreviewPicFileDownloaded(String str, String str2, int i) {
        }

        public void onConfirm_MessageSent(String str, String str2, int i) {
        }

        public void onConnectReturn(int i) {
        }

        public void onGroupAction(int i, GroupAction groupAction, String str) {
        }

        public void onIndicateBuddyInfoUpdated(String str) {
        }

        public void onIndicateBuddyListUpdated() {
        }

        public void onIndicateIMCMDReceivedImpl(String str, String str2, String str3, long j, int i) {
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
        }

        public void onIndicateInputStateChanged(String str, int i) {
        }

        public boolean onIndicateMessageReceived(String str, String str2, String str3) {
            return false;
        }

        public void onIndicate_BuddyBigPictureDownloaded(String str, int i) {
        }

        public void onNotifyBuddyJIDUpgrade(String str, String str2, String str3) {
        }

        public boolean onNotifySubscribeRequest(String str, String str2) {
            return false;
        }

        public void onNotifySubscribeRequestUpdated(String str) {
        }

        public boolean onNotifySubscriptionAccepted(String str) {
            return false;
        }

        public boolean onNotifySubscriptionDenied(String str) {
            return false;
        }

        public void onNotifyUnsubscribeRequest(String str, String str2) {
        }

        public void onNotify_ChatSessionListUpdate() {
        }

        public void onNotify_ChatSessionUnreadUpdate(String str) {
        }

        public void onNotify_ChatSessionUpdate(String str) {
        }

        public void onNotify_JIDUpdated() {
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        }

        public void onNotify_SessionMarkUnreadCtx(String str, int i, String str2, List<String> list) {
        }

        public void onQueryJidByEmail(String str, int i) {
        }

        public void onReceivedCall(String str, String str2, InvitationItem invitationItem) {
        }

        public void onRemoveBuddy(String str, int i) {
        }

        public void onSearchBuddy(String str, int i) {
        }

        public void onSearchBuddyByKey(String str, int i) {
        }

        public void onSearchBuddyByKeyV2(String str, String str2, String str3, int i) {
        }

        public void onSearchBuddyPicDownloaded(String str) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    /* access modifiers changed from: protected */
    public void dataReady() {
    }

    @NonNull
    public static synchronized ZoomMessengerUI getInstance() {
        ZoomMessengerUI zoomMessengerUI;
        synchronized (ZoomMessengerUI.class) {
            if (instance == null) {
                instance = new ZoomMessengerUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            zoomMessengerUI = instance;
        }
        return zoomMessengerUI;
    }

    private ZoomMessengerUI() {
        init();
    }

    private boolean initialized() {
        return this.mNativeHandle != 0;
    }

    private void init() {
        try {
            this.mNativeHandle = nativeInit();
        } catch (Throwable unused) {
        }
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        long j = this.mNativeHandle;
        if (j != 0) {
            nativeUninit(j);
        }
        super.finalize();
    }

    public void addListener(@Nullable IZoomMessengerUIListener iZoomMessengerUIListener) {
        if (iZoomMessengerUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iZoomMessengerUIListener) {
                    removeListener((IZoomMessengerUIListener) all[i]);
                }
            }
            this.mListenerList.add(iZoomMessengerUIListener);
        }
    }

    public void removeListener(IZoomMessengerUIListener iZoomMessengerUIListener) {
        this.mListenerList.remove(iZoomMessengerUIListener);
    }

    public int getConnectionStatus() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            boolean isConnectionGood = zoomMessenger.isConnectionGood();
            if (isConnectionGood && this.mConnectionStatus != 1) {
                this.mConnectionStatus = 1;
            } else if (!isConnectionGood && this.mConnectionStatus == 1) {
                this.mConnectionStatus = 0;
            }
        }
        return this.mConnectionStatus;
    }

    public void resetStatus() {
        this.mConnectionStatus = -1;
    }

    /* access modifiers changed from: protected */
    public void on_BeginConnect() {
        try {
            on_BeginConnectImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_BeginConnectImpl() {
        this.mConnectionStatus = 2;
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onBeginConnect();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_ConnectReturn(int i) {
        try {
            on_ConnectReturnImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_ConnectReturnImpl(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            this.mConnectionStatus = zoomMessenger.isConnectionGood() ? 1 : 0;
        }
        MessageSyncer.getInstance().onXMPPConnect();
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onConnectReturn(i);
            }
        }
        if (i == 0) {
            NOSMgr.getInstance().onXMPPConnectSuccess();
        }
    }

    /* access modifiers changed from: protected */
    public void indicate_BuddyInfoUpdated(String str) {
        try {
            indicate_BuddyInfoUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void indicate_BuddyInfoUpdatedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onIndicateBuddyInfoUpdated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_MakeGroup(@NonNull byte[] bArr) {
        try {
            on_MakeGroupImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_MakeGroupImpl(@NonNull byte[] bArr) {
        String str;
        boolean z;
        boolean z2;
        String str2;
        ZoomMessengerUI zoomMessengerUI;
        int i;
        GroupCallBackInfo groupCallBackInfo;
        String str3;
        String str4;
        Iterator it;
        try {
            GroupCallBackInfo parseFrom = GroupCallBackInfo.parseFrom(bArr);
            if (parseFrom != null) {
                int result = parseFrom.getResult();
                String jid = parseFrom.getActionOwner().getJid();
                String msgID = parseFrom.getMsgID();
                String msgID2 = parseFrom.getMsgID();
                String groupID = parseFrom.getGroupID();
                boolean groupIsExist = parseFrom.getGroupIsExist();
                long tm = parseFrom.getTm();
                long tmServerside = parseFrom.getTmServerside();
                long prevMsgtime = parseFrom.getPrevMsgtime();
                int maxAllowed = parseFrom.getMaxAllowed();
                ArrayList arrayList = new ArrayList();
                Iterator it2 = parseFrom.getNotAllowedBuddiesList().iterator();
                long j = prevMsgtime;
                int i2 = 0;
                while (it2.hasNext()) {
                    BuddyUserInfo buddyUserInfo = (BuddyUserInfo) it2.next();
                    if (buddyUserInfo != null) {
                        if (buddyUserInfo.hasNotAllowedReason()) {
                            it = it2;
                            if (buddyUserInfo.getNotAllowedReason() == 1) {
                                i2 = 1;
                            }
                        } else {
                            it = it2;
                        }
                        arrayList.add(buddyUserInfo.getDisplayName());
                    } else {
                        it = it2;
                    }
                    it2 = it;
                }
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        String jid2 = myself.getJid();
                        if (jid2 != null) {
                            boolean equals = jid.equals(jid2);
                            String str5 = "";
                            long j2 = tmServerside;
                            if (equals) {
                                str5 = BuddyNameUtil.getMyDisplayName(myself);
                                str = msgID;
                            } else {
                                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(jid);
                                if (buddyWithJID != null) {
                                    str = msgID;
                                    str5 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null, false);
                                } else {
                                    str = msgID;
                                }
                            }
                            String displayName = TextUtils.isEmpty(str5) ? parseFrom.getActionOwner().getDisplayName() : str5;
                            ZoomGroup groupById = zoomMessenger.getGroupById(groupID);
                            ArrayList arrayList2 = new ArrayList();
                            if (!CollectionsUtil.isCollectionEmpty(parseFrom.getBuddiesList())) {
                                int i3 = 0;
                                boolean z3 = false;
                                while (true) {
                                    z = groupIsExist;
                                    if (i3 >= parseFrom.getBuddiesList().size()) {
                                        break;
                                    }
                                    String jid3 = ((BuddyUserInfo) parseFrom.getBuddiesList().get(i3)).getJid();
                                    if (jid2.equals(jid3)) {
                                        groupCallBackInfo = parseFrom;
                                        str3 = jid2;
                                        z3 = true;
                                    } else if (StringUtil.isSameString(jid, jid3)) {
                                        groupCallBackInfo = parseFrom;
                                        str3 = jid2;
                                    } else {
                                        str3 = jid2;
                                        String displayName2 = ((BuddyUserInfo) parseFrom.getBuddiesList().get(i3)).getDisplayName();
                                        if (StringUtil.isEmptyOrNull(displayName2)) {
                                            ZoomBuddy buddyWithJID2 = zoomMessenger.getBuddyWithJID(jid3);
                                            if (buddyWithJID2 == null) {
                                                groupCallBackInfo = parseFrom;
                                            } else {
                                                groupCallBackInfo = parseFrom;
                                                str4 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID2, null, false);
                                            }
                                        } else {
                                            groupCallBackInfo = parseFrom;
                                            str4 = displayName2;
                                        }
                                        arrayList2.add(str4);
                                    }
                                    i3++;
                                    parseFrom = groupCallBackInfo;
                                    jid2 = str3;
                                    groupIsExist = z;
                                }
                                z2 = z3;
                            } else {
                                z = groupIsExist;
                                z2 = false;
                            }
                            GroupAction groupAction = new GroupAction(0, displayName, (String[]) arrayList2.toArray(new String[arrayList2.size()]), equals, z2, null);
                            groupAction.setActionOwnerId(jid);
                            groupAction.setGroupId(groupID);
                            groupAction.setTime(tm);
                            groupAction.setNotAllowBuddies(arrayList);
                            groupAction.setBuddyNotAllowReason(i2);
                            groupAction.setReqId(msgID2);
                            groupAction.setMaxAllowed(maxAllowed);
                            if (groupById != null) {
                                groupAction.setChannel(groupById.isRoom());
                            }
                            if (result != 0 || z) {
                                i = 0;
                                zoomMessengerUI = this;
                                str2 = str;
                            } else {
                                i = 0;
                                str2 = zoomMessenger.insertSystemMessage(groupID, jid, GroupAction.serializeToString(groupAction), tm, 20, str, j2, j);
                                zoomMessengerUI = this;
                            }
                            IListener[] all = zoomMessengerUI.mListenerList.getAll();
                            if (all != null) {
                                int length = all.length;
                                while (i < length) {
                                    ((IZoomMessengerUIListener) all[i]).onGroupAction(result, groupAction, str2);
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void on_ModifyGroupName(@NonNull byte[] bArr) {
        try {
            on_ModifyGroupNameImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_ModifyGroupNameImpl(@NonNull byte[] bArr) {
        String str;
        int i;
        ZoomMessengerUI zoomMessengerUI;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                try {
                    GroupCallBackInfo parseFrom = GroupCallBackInfo.parseFrom(bArr);
                    if (parseFrom != null) {
                        int result = parseFrom.getResult();
                        String jid = parseFrom.getActionOwner().getJid();
                        String msgID = parseFrom.getMsgID();
                        parseFrom.getMsgID();
                        String groupID = parseFrom.getGroupID();
                        long tm = parseFrom.getTm();
                        long tmServerside = parseFrom.getTmServerside();
                        long prevMsgtime = parseFrom.getPrevMsgtime();
                        parseFrom.getMaxAllowed();
                        ArrayList arrayList = new ArrayList();
                        for (BuddyUserInfo buddyUserInfo : parseFrom.getNotAllowedBuddiesList()) {
                            if (buddyUserInfo != null) {
                                arrayList.add(buddyUserInfo.getDisplayName());
                            }
                        }
                        boolean equals = jid.equals(myself.getJid());
                        if (equals) {
                            str = BuddyNameUtil.getMyDisplayName(myself);
                        } else {
                            str = BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(jid), null, false);
                        }
                        GroupAction groupAction = new GroupAction(1, TextUtils.isEmpty(str) ? parseFrom.getActionOwner().getDisplayName() : str, null, equals, false, parseFrom.getNewName());
                        groupAction.setActionOwnerId(jid);
                        groupAction.setGroupId(groupID);
                        groupAction.setTime(tm);
                        ZoomGroup groupById = zoomMessenger.getGroupById(groupID);
                        if (groupById != null) {
                            groupAction.setChannel(groupById.isRoom());
                        }
                        if (result == 0) {
                            String str2 = groupID;
                            String serializeToString = GroupAction.serializeToString(groupAction);
                            i = 0;
                            msgID = zoomMessenger.insertSystemMessage(str2, jid, serializeToString, tm, 24, msgID, tmServerside, prevMsgtime);
                            zoomMessengerUI = this;
                        } else {
                            i = 0;
                            zoomMessengerUI = this;
                        }
                        IListener[] all = zoomMessengerUI.mListenerList.getAll();
                        if (all != null) {
                            int length = all.length;
                            while (i < length) {
                                ((IZoomMessengerUIListener) all[i]).onGroupAction(result, groupAction, msgID);
                                i++;
                            }
                        }
                    }
                } catch (IOException unused) {
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_DeleteGroup(int i, @NonNull String str, String str2, long j) {
        try {
            on_DeleteGroupImpl(i, str, str2, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_DeleteGroupImpl(int i, @NonNull String str, String str2, long j) {
        String str3;
        int i2 = i;
        String str4 = str;
        String str5 = str2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            deleteCachedGroupAvatar(str5);
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                boolean equals = str4.equals(myself.getJid());
                String str6 = null;
                if (equals) {
                    str3 = BuddyNameUtil.getMyDisplayName(myself);
                } else {
                    str3 = BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(str4), null, false);
                }
                GroupAction groupAction = new GroupAction(2, str3, null, equals, false, null);
                groupAction.setActionOwnerId(str4);
                groupAction.setGroupId(str5);
                groupAction.setTime(j);
                ZoomGroup groupById = zoomMessenger.getGroupById(str5);
                if (groupById != null) {
                    groupAction.setChannel(groupById.isRoom());
                }
                if (i2 == 0) {
                    str6 = zoomMessenger.insertSystemMessage(str2, str, GroupAction.serializeToString(groupAction), j, 23, null, 0, 0);
                }
                IListener[] all = this.mListenerList.getAll();
                if (all != null) {
                    for (IListener iListener : all) {
                        ((IZoomMessengerUIListener) iListener).onGroupAction(i2, groupAction, str6);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_ModifyGroupOption(int i, @NonNull String str, String str2, int i2, long j, long j2, long j3) {
        try {
            on_ModifyGroupOptionImpl(i, str, str2, i2, j, j2, j3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_ModifyGroupOptionImpl(int i, @NonNull String str, String str2, int i2, long j, long j2, long j3) {
        String str3;
        String str4 = str;
        String str5 = str2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                boolean equals = str.equals(myself.getJid());
                if (equals) {
                    str3 = BuddyNameUtil.getMyDisplayName(myself);
                } else {
                    str3 = BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(str), null, false);
                }
                GroupAction groupAction = new GroupAction(6, str3, null, equals, false, null);
                groupAction.setActionOwnerId(str);
                groupAction.setGroupId(str5);
                groupAction.setTime(j);
                groupAction.setMucFlag(i2);
                ZoomGroup groupById = zoomMessenger.getGroupById(str5);
                if (groupById != null) {
                    groupAction.setChannel(groupById.isRoom());
                }
                IListener[] all = this.mListenerList.getAll();
                if (all != null) {
                    for (IListener iListener : all) {
                        int i3 = i;
                        ((IZoomMessengerUIListener) iListener).onGroupAction(i, groupAction, null);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_ModifyGroupProperty(int i, @NonNull byte[] bArr) {
        try {
            on_ModifyGroupPropertyImpl(i, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_ModifyGroupPropertyImpl(int i, @NonNull byte[] bArr) {
        String str;
        String str2;
        ZoomMessengerUI zoomMessengerUI;
        GroupAction groupAction;
        String str3;
        int i2 = i;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                try {
                    RoomEditInfo parseFrom = RoomEditInfo.parseFrom(bArr);
                    if (parseFrom != null) {
                        String actionOwner = parseFrom.getActionOwner();
                        boolean equals = actionOwner.equals(myself.getJid());
                        if (equals) {
                            str = BuddyNameUtil.getMyDisplayName(myself);
                        } else {
                            str = BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(actionOwner), null, false);
                        }
                        if (TextUtils.isEmpty(str) && parseFrom.getNameModified()) {
                            str = parseFrom.getActionOwnerName();
                        }
                        GroupAction groupAction2 = new GroupAction(parseFrom.getNameModified() ? 1 : 6, str, null, equals, false, parseFrom.getNameModified() ? parseFrom.getName() : null);
                        groupAction2.setActionOwnerId(actionOwner);
                        groupAction2.setGroupId(parseFrom.getGroupId());
                        groupAction2.setTime(parseFrom.getLocalTime());
                        groupAction2.setGroupDesc(parseFrom.getDesc());
                        groupAction2.setGroupDescAction(parseFrom.getDescAction());
                        if (parseFrom.getOptionModified()) {
                            groupAction2.setMucFlag(parseFrom.getRoomOption());
                        }
                        ZoomGroup groupById = zoomMessenger.getGroupById(parseFrom.getGroupId());
                        if (groupById != null) {
                            groupAction2.setChannel(groupById.isRoom());
                        }
                        if (parseFrom.getNameModified()) {
                            String msgId = parseFrom.getMsgId();
                            if (i2 == 0) {
                                str2 = zoomMessenger.insertSystemMessage(parseFrom.getGroupId(), actionOwner, GroupAction.serializeToString(groupAction2), parseFrom.getLocalTime(), 24, msgId, parseFrom.getTmServerSide(), parseFrom.getPrevMsgTime());
                                zoomMessengerUI = this;
                                groupAction = groupAction2;
                            } else {
                                zoomMessengerUI = this;
                                str2 = msgId;
                                groupAction = groupAction2;
                            }
                        } else if (i2 != 0 || parseFrom.getDescAction() == 0) {
                            groupAction = groupAction2;
                            str2 = null;
                            zoomMessengerUI = this;
                        } else if (groupById != null) {
                            String groupDesc = groupById.getGroupDesc();
                            VideoBoxApplication nonNullInstance = VideoBoxApplication.getNonNullInstance();
                            if (equals) {
                                str = nonNullInstance.getString(C4558R.string.zm_lbl_content_you);
                            }
                            if (str != null) {
                                if (parseFrom.getDescAction() == 1) {
                                    str3 = nonNullInstance.getString(groupById.isRoom() ? C4558R.string.zm_mm_description_add_channel_msg_128527 : C4558R.string.zm_mm_description_add_chat_msg_128527, new Object[]{str, groupDesc});
                                } else if (parseFrom.getDescAction() == 3) {
                                    str3 = nonNullInstance.getString(groupById.isRoom() ? C4558R.string.zm_mm_description_update_channel_msg_128527 : C4558R.string.zm_mm_description_update_chat_msg_128527, new Object[]{str, groupDesc});
                                } else if (parseFrom.getDescAction() == 2) {
                                    str3 = nonNullInstance.getString(groupById.isRoom() ? C4558R.string.zm_mm_description_remove_channel_msg_108993 : C4558R.string.zm_mm_description_remove_chat_msg_108993, new Object[]{str});
                                } else {
                                    str3 = null;
                                }
                                String groupId = parseFrom.getGroupId();
                                long localTime = parseFrom.getLocalTime();
                                long tmServerSide = parseFrom.getTmServerSide();
                                long prevMsgTime = parseFrom.getPrevMsgTime();
                                String str4 = groupId;
                                groupAction = groupAction2;
                                str2 = zoomMessenger.insertSystemMessage(str4, actionOwner, str3, localTime, true, 26, null, tmServerSide, prevMsgTime, false);
                                zoomMessengerUI = this;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                        IListener[] all = zoomMessengerUI.mListenerList.getAll();
                        if (all != null) {
                            for (IListener iListener : all) {
                                ((IZoomMessengerUIListener) iListener).onGroupAction(i2, groupAction, str2);
                            }
                        }
                    }
                } catch (IOException unused) {
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_AddedToGroup(@NonNull byte[] bArr) {
        try {
            on_AddedToGroupImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_AddedToGroupImpl(@NonNull byte[] bArr) {
        long j;
        String str;
        boolean z;
        GroupAction groupAction;
        ZoomMessengerUI zoomMessengerUI;
        GroupCallBackInfo groupCallBackInfo;
        String str2;
        Object obj;
        try {
            GroupCallBackInfo parseFrom = GroupCallBackInfo.parseFrom(bArr);
            if (parseFrom != null) {
                if (parseFrom.getBuddiesList() != null || parseFrom.getNotAllowedBuddiesList() != null) {
                    int result = parseFrom.getResult();
                    String jid = parseFrom.getActionOwner().getJid();
                    String msgID = parseFrom.getMsgID();
                    parseFrom.getMsgID();
                    String groupID = parseFrom.getGroupID();
                    long tm = parseFrom.getTm();
                    long tmServerside = parseFrom.getTmServerside();
                    long prevMsgtime = parseFrom.getPrevMsgtime();
                    int maxAllowed = parseFrom.getMaxAllowed();
                    ArrayList arrayList = new ArrayList();
                    int i = 0;
                    for (BuddyUserInfo buddyUserInfo : parseFrom.getNotAllowedBuddiesList()) {
                        if (buddyUserInfo != null) {
                            arrayList.add(buddyUserInfo.getDisplayName());
                            if (buddyUserInfo.hasNotAllowedReason() && buddyUserInfo.getNotAllowedReason() == 1) {
                                i = 1;
                            }
                        }
                    }
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        deleteCachedGroupAvatar(groupID);
                        ZoomBuddy myself = zoomMessenger.getMyself();
                        if (myself != null) {
                            String jid2 = myself.getJid();
                            boolean equals = jid.equals(jid2);
                            if (equals) {
                                str = BuddyNameUtil.getMyDisplayName(myself);
                                j = prevMsgtime;
                            } else {
                                j = prevMsgtime;
                                str = BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(jid), null, false);
                            }
                            String displayName = TextUtils.isEmpty(str) ? parseFrom.getActionOwner().getDisplayName() : str;
                            ArrayList arrayList2 = new ArrayList();
                            if (jid2 == null || CollectionsUtil.isCollectionEmpty(parseFrom.getBuddiesList())) {
                                z = false;
                            } else {
                                int i2 = 0;
                                z = false;
                                while (i2 < parseFrom.getBuddiesList().size()) {
                                    String jid3 = ((BuddyUserInfo) parseFrom.getBuddiesList().get(i2)).getJid();
                                    if (jid2.equals(jid3)) {
                                        groupCallBackInfo = parseFrom;
                                        str2 = jid2;
                                        obj = null;
                                        z = true;
                                    } else {
                                        str2 = jid2;
                                        String displayName2 = ((BuddyUserInfo) parseFrom.getBuddiesList().get(i2)).getDisplayName();
                                        if (StringUtil.isEmptyOrNull(displayName2)) {
                                            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(jid3);
                                            if (buddyWithJID == null) {
                                                groupCallBackInfo = parseFrom;
                                                obj = null;
                                            } else {
                                                groupCallBackInfo = parseFrom;
                                                obj = null;
                                                displayName2 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null, false);
                                            }
                                        } else {
                                            groupCallBackInfo = parseFrom;
                                            obj = null;
                                        }
                                        arrayList2.add(displayName2);
                                    }
                                    i2++;
                                    Object obj2 = obj;
                                    jid2 = str2;
                                    parseFrom = groupCallBackInfo;
                                }
                            }
                            GroupAction groupAction2 = new GroupAction(3, displayName, (String[]) arrayList2.toArray(new String[arrayList2.size()]), equals, z, null);
                            groupAction2.setActionOwnerId(jid);
                            groupAction2.setGroupId(groupID);
                            groupAction2.setTime(tm);
                            groupAction2.setNotAllowBuddies(arrayList);
                            groupAction2.setBuddyNotAllowReason(i);
                            groupAction2.setMaxAllowed(maxAllowed);
                            ZoomGroup groupById = zoomMessenger.getGroupById(groupID);
                            if (groupById != null) {
                                groupAction2.setChannel(groupById.isRoom());
                            }
                            if (result == 0) {
                                String serializeToString = GroupAction.serializeToString(groupAction2);
                                if (equals || !z) {
                                    groupAction = groupAction2;
                                    msgID = zoomMessenger.insertSystemMessage(groupID, jid, serializeToString, tm, 21, msgID, tmServerside, j);
                                    zoomMessengerUI = this;
                                } else {
                                    ZoomMessenger zoomMessenger2 = zoomMessenger;
                                    groupAction = groupAction2;
                                    msgID = zoomMessenger.insertSystemMessage(groupID, jid, serializeToString, tm, 25, msgID, tmServerside, j);
                                    if (groupById != null && !TextUtils.isEmpty(groupById.getGroupDesc())) {
                                        String string = VideoBoxApplication.getNonNullInstance().getString(groupById.isRoom() ? C4558R.string.zm_mm_description_join_channel_first_msg_108993 : C4558R.string.zm_mm_description_join_chat_first_msg_108993, new Object[]{groupById.getGroupDesc()});
                                        long mMNow = CmmTime.getMMNow();
                                        long j2 = mMNow < tmServerside ? tmServerside + 1000 : mMNow;
                                        zoomMessenger2.insertSystemMessage(groupID, "", string, j2, true, 27, null, j2, 0, true);
                                    }
                                    zoomMessengerUI = this;
                                }
                            } else {
                                groupAction = groupAction2;
                                zoomMessengerUI = this;
                            }
                            IListener[] all = zoomMessengerUI.mListenerList.getAll();
                            if (all != null) {
                                for (IListener iListener : all) {
                                    ((IZoomMessengerUIListener) iListener).onGroupAction(result, groupAction, msgID);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void on_RemovedFromGroup(@NonNull byte[] bArr) {
        try {
            on_RemovedFromGroupImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_RemovedFromGroupImpl(@NonNull byte[] bArr) {
        boolean z;
        int i;
        String str;
        String[] strArr;
        String[] strArr2;
        String str2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String jid = myself.getJid();
                try {
                    GroupCallBackInfo parseFrom = GroupCallBackInfo.parseFrom(bArr);
                    if (parseFrom != null) {
                        List buddiesList = parseFrom.getBuddiesList();
                        if (buddiesList != null) {
                            int result = parseFrom.getResult();
                            String jid2 = parseFrom.getActionOwner().getJid();
                            String msgID = parseFrom.getMsgID();
                            parseFrom.getMsgID();
                            String groupID = parseFrom.getGroupID();
                            long tm = parseFrom.getTm();
                            long tmServerside = parseFrom.getTmServerside();
                            long prevMsgtime = parseFrom.getPrevMsgtime();
                            ArrayList arrayList = new ArrayList();
                            int i2 = 1;
                            if (jid == null || CollectionsUtil.isCollectionEmpty(buddiesList)) {
                                i = 0;
                                z = false;
                            } else {
                                int i3 = 0;
                                boolean z2 = false;
                                int i4 = 0;
                                while (i3 < buddiesList.size()) {
                                    if (((BuddyUserInfo) buddiesList.get(i3)).hasNotAllowedReason() && ((BuddyUserInfo) buddiesList.get(i3)).getNotAllowedReason() == i2) {
                                        i4 = 1;
                                    }
                                    String jid3 = ((BuddyUserInfo) buddiesList.get(i3)).getJid();
                                    if (jid.equals(jid3)) {
                                        z2 = true;
                                    } else {
                                        String displayName = ((BuddyUserInfo) buddiesList.get(i3)).getDisplayName();
                                        if (StringUtil.isEmptyOrNull(displayName)) {
                                            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(jid3);
                                            if (buddyWithJID != null) {
                                                str2 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null, false);
                                            }
                                        } else {
                                            str2 = displayName;
                                        }
                                        arrayList.add(str2);
                                    }
                                    i3++;
                                    i2 = 1;
                                }
                                z = z2;
                                i = i4;
                            }
                            if (buddiesList.size() != 1 || !StringUtil.isSameString(jid2, ((BuddyUserInfo) buddiesList.get(0)).getJid()) || StringUtil.isEmptyOrNull(jid2)) {
                                deleteCachedGroupAvatar(groupID);
                                boolean equals = jid2.equals(jid);
                                if (equals) {
                                    str = BuddyNameUtil.getMyDisplayName(myself);
                                } else {
                                    str = BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(jid2), null, false);
                                }
                                String displayName2 = TextUtils.isEmpty(str) ? parseFrom.getActionOwner().getDisplayName() : str;
                                if (buddiesList.size() == 1) {
                                    String displayName3 = ((BuddyUserInfo) buddiesList.get(0)).getDisplayName();
                                    if (StringUtil.isEmptyOrNull(displayName3)) {
                                        ZoomBuddy buddyWithJID2 = zoomMessenger.getBuddyWithJID(((BuddyUserInfo) buddiesList.get(0)).getJid());
                                        if (buddyWithJID2 != null) {
                                            strArr2 = null;
                                            displayName3 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID2, null, false);
                                        } else {
                                            return;
                                        }
                                    } else {
                                        strArr2 = null;
                                    }
                                    strArr = !z ? new String[]{displayName3} : strArr2;
                                } else {
                                    strArr = !arrayList.isEmpty() ? (String[]) arrayList.toArray(new String[arrayList.size()]) : null;
                                }
                                GroupAction groupAction = new GroupAction(4, displayName2, strArr, equals, z, null);
                                groupAction.setActionOwnerId(jid2);
                                groupAction.setGroupId(groupID);
                                groupAction.setTime(tm);
                                groupAction.setBuddyNotAllowReason(i);
                                ZoomGroup groupById = zoomMessenger.getGroupById(groupID);
                                if (groupById != null) {
                                    groupAction.setChannel(groupById.isRoom());
                                }
                                String insertSystemMessage = result == 0 ? zoomMessenger.insertSystemMessage(groupID, jid2, GroupAction.serializeToString(groupAction), tm, 22, msgID, tmServerside, prevMsgtime) : msgID;
                                IListener[] all = this.mListenerList.getAll();
                                if (all != null) {
                                    for (IListener iListener : all) {
                                        ((IZoomMessengerUIListener) iListener).onGroupAction(result, groupAction, insertSystemMessage);
                                    }
                                }
                                return;
                            }
                            on_QuitGroup(result, jid2, arrayList.isEmpty() ? null : (String) arrayList.get(0), groupID, tm, tmServerside, prevMsgtime, msgID);
                        }
                    }
                } catch (IOException unused) {
                }
            }
        }
    }

    private void on_QuitGroup(int i, @NonNull String str, String str2, String str3, long j, long j2, long j3, String str4) {
        int i2 = i;
        String str5 = str;
        String str6 = str3;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            deleteCachedGroupAvatar(str6);
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                boolean equals = str5.equals(myself.getJid());
                String str7 = equals ? BuddyNameUtil.getMyDisplayName(myself) : StringUtil.isEmptyOrNull(str2) ? BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(str5), null, false) : str2;
                GroupAction groupAction = new GroupAction(5, str7, null, equals, false, null);
                groupAction.setActionOwnerId(str5);
                groupAction.setGroupId(str6);
                groupAction.setTime(j);
                ZoomGroup groupById = zoomMessenger.getGroupById(str6);
                if (groupById != null) {
                    groupAction.setChannel(groupById.isRoom());
                }
                String insertSystemMessage = i2 == 0 ? zoomMessenger.insertSystemMessage(str3, str, GroupAction.serializeToString(groupAction), j, 23, str4, j2, j3) : str4;
                IListener[] all = this.mListenerList.getAll();
                if (all != null) {
                    for (IListener iListener : all) {
                        ((IZoomMessengerUIListener) iListener).onGroupAction(i2, groupAction, insertSystemMessage);
                    }
                }
            }
        }
    }

    private void deleteCachedGroupAvatar(String str) {
        String groupAvatarPath = ZoomGroup.getGroupAvatarPath(str);
        if (groupAvatarPath != null) {
            File file = new File(groupAvatarPath);
            if (file.exists()) {
                file.delete();
            }
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            String sessionDataFolder = zoomMessenger.getSessionDataFolder(str);
            if (!StringUtil.isEmptyOrNull(sessionDataFolder)) {
                File file2 = new File(sessionDataFolder);
                if (file2.exists()) {
                    file2.delete();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_MUCGroupInfoUpdated(String str) {
        try {
            notify_MUCGroupInfoUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_MUCGroupInfoUpdatedImpl(String str) {
        ZoomMessengerUI zoomMessengerUI;
        String str2 = str;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (PreferenceUtil.readBooleanValue(PreferenceUtil.ZM_MM_GROUP_DESC_JOIN_FIRST, false)) {
                ZoomGroup groupById = zoomMessenger.getGroupById(str2);
                if (groupById != null) {
                    if (!groupById.isGroupInfoReady() || TextUtils.isEmpty(groupById.getGroupDesc())) {
                        zoomMessengerUI = this;
                    } else {
                        if (zoomMessenger.insertSystemMessage(str, "", VideoBoxApplication.getNonNullInstance().getString(groupById.isRoom() ? C4558R.string.zm_mm_description_join_channel_first_msg_108993 : C4558R.string.zm_mm_description_join_chat_first_msg_108993, new Object[]{groupById.getGroupDesc()}), CmmTime.getMMNow(), true, 27, null, CmmTime.getMMNow(), 0, true) != null) {
                            PreferenceUtil.saveBooleanValue(PreferenceUtil.ZM_MM_GROUP_DESC_JOIN_FIRST, false);
                            zoomMessengerUI = this;
                        } else {
                            zoomMessengerUI = this;
                        }
                    }
                } else {
                    return;
                }
            } else {
                zoomMessengerUI = this;
            }
            IListener[] all = zoomMessengerUI.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IZoomMessengerUIListener) iListener).onNotify_MUCGroupInfoUpdatedImpl(str2);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_SessionMarkUnreadCtx(String str, int i, String str2, List<String> list) {
        try {
            notify_SessionMarkUnreadCtxImpl(str, i, str2, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_SessionMarkUnreadCtxImpl(String str, int i, String str2, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onNotify_SessionMarkUnreadCtx(str, i, str2, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_ChatSessionResetUnreadCount(@NonNull String str) {
        try {
            notify_ChatSessionResetUnreadCountImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_ChatSessionResetUnreadCountImpl(@NonNull String str) {
        if (!TextUtils.isEmpty(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    if (sessionById.getUnreadMessageCount() <= 0) {
                        NotificationMgr.removeMessageNotificationMM(VideoBoxApplication.getGlobalContext(), str);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).notify_ChatSessionResetUnreadCount(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_ChatSessionUnreadUpdate(String str) {
        try {
            notify_ChatSessionUnreadUpdateImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_ChatSessionUnreadUpdateImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onNotify_ChatSessionUnreadUpdate(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_ChatSessionListUpdate() {
        try {
            notify_ChatSessionListUpdateImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_ChatSessionListUpdateImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onNotify_ChatSessionListUpdate();
            }
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.getTotalUnreadMessageCount() <= 0) {
            NotificationMgr.removeMessageNotificationMM(VideoBoxApplication.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void notify_ChatSessionUpdate(String str) {
        try {
            notify_ChatSessionUpdateImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_ChatSessionUpdateImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onNotify_ChatSessionUpdate(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void confirm_MessageSent(String str, String str2, int i) {
        try {
            confirm_MessageSentImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void confirm_MessageSentImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onConfirm_MessageSent(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void confirm_FileDownloaded(String str, String str2, int i) {
        try {
            confirm_FileDownloadedImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void confirm_FileDownloadedImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onConfirmFileDownloaded(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void confirm_PreviewPicFileDownloaded(String str, String str2, int i) {
        try {
            confirm_PreviewPicFileDownloadedImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void confirm_PreviewPicFileDownloadedImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onConfirmPreviewPicFileDownloaded(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void indicate_MessageReceived(@NonNull String str, String str2, String str3) {
        try {
            indicate_MessageReceivedImpl(str, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    /* access modifiers changed from: protected */
    public void indicate_IMCMD_Received(String str, String str2, String str3, long j, int i) {
        try {
            indicate_IMCMD_ReceivedImpl(str, str2, str3, j, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void indicate_MessageReceivedImpl(@NonNull String str, String str2, String str3) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.imChatGetOption() != 2) {
            MessageSyncer.getInstance().onReceiveMsg(str, str2, str3);
            IListener[] all = this.mListenerList.getAll();
            int i = 0;
            if (all != null) {
                int length = all.length;
                int i2 = 0;
                while (i < length) {
                    if (((IZoomMessengerUIListener) all[i]).onIndicateMessageReceived(str, str2, str3)) {
                        i2 = 1;
                    }
                    i++;
                }
                i = i2;
            }
            if (i == 0) {
                showMmMessageUnreadNotificationDelayed(str, str3, 1500);
            }
        }
    }

    private void indicate_IMCMD_ReceivedImpl(String str, String str2, String str3, long j, int i) {
        ZoomMessengerUI zoomMessengerUI;
        if (i == 50) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (!StringUtil.isEmptyOrNull(str) && zoomMessenger != null) {
                zoomMessenger.insertSystemMessage("", str, VideoBoxApplication.getInstance().getString(C4558R.string.zm_mm_miss_call), j / 1000, false, 50, null);
                zoomMessengerUI = this;
            } else {
                return;
            }
        } else {
            zoomMessengerUI = this;
        }
        IListener[] all = zoomMessengerUI.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onIndicateIMCMDReceivedImpl(str, str2, str3, j, i);
            }
        }
    }

    private void showMmMessageUnreadNotificationDelayed(@NonNull final String str, final String str2, long j) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                    if (sessionById != null) {
                        ZoomMessage messageById = sessionById.getMessageById(str2);
                        if (messageById != null && messageById.getMessageFilterResult() == 1) {
                            NotificationMgr.showMessageNotificationMM(VideoBoxApplication.getInstance(), true, str);
                        }
                    }
                }
            }
        }, j);
    }

    /* access modifiers changed from: protected */
    public void indicate_InputStateChanged(String str, int i) {
        try {
            indicate_InputStateChangedImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void indicate_InputStateChangedImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onIndicateInputStateChanged(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_ReceivedCall(String str, String str2, @NonNull byte[] bArr) {
        try {
            on_ReceivedCallImpl(str, str2, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_ReceivedCallImpl(String str, String str2, @NonNull byte[] bArr) {
        try {
            InvitationItem parseFrom = InvitationItem.parseFrom(bArr);
            if (PTApp.getInstance().getActiveMeetingNo() != parseFrom.getMeetingNumber()) {
                onConfInvitation(parseFrom);
                IListener[] all = this.mListenerList.getAll();
                if (all != null) {
                    for (IListener iListener : all) {
                        ((IZoomMessengerUIListener) iListener).onReceivedCall(str, str2, parseFrom);
                    }
                }
            }
        } catch (InvalidProtocolBufferException unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void notify_JIDUpdated() {
        try {
            notify_JIDUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_JIDUpdatedImpl() {
        ContactsMatchHelper.getInstance().matchAllNumbers(VideoBoxApplication.getInstance());
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onNotify_JIDUpdated();
            }
        }
    }

    private void onConfInvitation(InvitationItem invitationItem) {
        IncomingCallManager.getInstance().onConfInvitation(invitationItem);
    }

    /* access modifiers changed from: protected */
    public void indicate_BuddyListUpdated() {
        try {
            indicate_BuddyListUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void indicate_BuddyListUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onIndicateBuddyListUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void indicate_BuddyBigPictureDownloaded(String str, int i) {
        try {
            indicate_BuddyBigPictureDownloadedImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void indicate_BuddyBigPictureDownloadedImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onIndicate_BuddyBigPictureDownloaded(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void indicate_BuddyInfoUpdatedWithJID(@NonNull String str) {
        try {
            indicate_BuddyInfoUpdatedWithJIDImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void indicate_BuddyInfoUpdatedWithJIDImpl(@NonNull String str) {
        ZMBuddySyncInstance.getInsatance().indicate_BuddyInfoUpdatedWithJID(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onIndicateInfoUpdatedWithJID(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_AddBuddy(String str, int i, String str2) {
        try {
            on_AddBuddyImpl(str, i, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_AddBuddyImpl(String str, int i, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onAddBuddy(str, i, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_AddBuddyByEmail(String str, int i) {
        try {
            on_AddBuddyByEmailImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_AddBuddyByEmailImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onAddBuddyByEmail(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_RemoveBuddy(String str, int i) {
        try {
            on_RemoveBuddyImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_RemoveBuddyImpl(String str, int i) {
        ZMBuddySyncInstance.getInsatance().on_RemoveBuddy(str, i);
        if (i == 0) {
            deleteChatSession(str);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onRemoveBuddy(str, i);
            }
        }
    }

    private void deleteChatSession(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null) {
                sessionById.storeMessageDraft(null);
                sessionById.storeMessageDraftTime(0);
            }
            zoomMessenger.deleteSession(str, false);
        }
    }

    /* access modifiers changed from: protected */
    public void on_SearchBuddy(String str, int i) {
        try {
            on_SearchBuddyImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_SearchBuddyImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onSearchBuddy(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_QueryJidByEmail(String str, int i) {
        try {
            on_QueryJidByEmailImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_QueryJidByEmailImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onQueryJidByEmail(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_SearchBuddyByKey(String str, int i) {
        try {
            on_SearchBuddyByKeyImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_SearchBuddyByKeyImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onSearchBuddyByKey(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSearchBuddyByKeyV2(String str, String str2, String str3, int i) {
        try {
            on_SearchBuddyByKeyV2Impl(str, str2, str3, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_SearchBuddyByKeyV2Impl(String str, String str2, String str3, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onSearchBuddyByKeyV2(str, str2, str3, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void on_SearchBuddyPicDownloaded(String str) {
        try {
            on_SearchBuddyPicDownloadedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void on_SearchBuddyPicDownloadedImpl(String str) {
        ZMBuddySyncInstance.getInsatance().onSearchBuddyPicDownloaded(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onSearchBuddyPicDownloaded(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_SubscribeRequest(String str, String str2) {
        try {
            notify_SubscribeRequestImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_SubscribeRequestImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        int i = 0;
        if (all != null) {
            boolean z = 0;
            while (i < all.length) {
                z |= ((IZoomMessengerUIListener) all[i]).onNotifySubscribeRequest(str, str2);
                i++;
            }
            i = z;
        }
        if (i == 0) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        NotificationMgr.showMessageNotificationMM(VideoBoxApplication.getInstance(), true, zoomMessenger.getContactRequestsSessionID());
                    }
                }
            }, 1500);
        }
    }

    /* access modifiers changed from: protected */
    public void notify_SubscriptionAccepted(@NonNull String str) {
        try {
            notify_SubscriptionAcceptedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_SubscriptionAcceptedImpl(@NonNull String str) {
        ZMBuddySyncInstance.getInsatance().onNotifySubscriptionAccepted(str);
        IListener[] all = this.mListenerList.getAll();
        int i = 0;
        if (all != null) {
            boolean z = 0;
            while (i < all.length) {
                z |= ((IZoomMessengerUIListener) all[i]).onNotifySubscriptionAccepted(str);
                i++;
            }
            i = z;
        }
        if (i == 0) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        NotificationMgr.showMessageNotificationMM(VideoBoxApplication.getInstance(), true, zoomMessenger.getContactRequestsSessionID());
                    }
                }
            }, 1500);
        }
    }

    /* access modifiers changed from: protected */
    public void notify_SubscriptionDenied(String str) {
        try {
            notify_SubscriptionDeniedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_SubscriptionDeniedImpl(String str) {
        ZMBuddySyncInstance.getInsatance().onNotifySubscriptionDenied(str);
        IListener[] all = this.mListenerList.getAll();
        int i = 0;
        if (all != null) {
            boolean z = 0;
            while (i < all.length) {
                z |= ((IZoomMessengerUIListener) all[i]).onNotifySubscriptionDenied(str);
                i++;
            }
            i = z;
        }
        if (i == 0) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        NotificationMgr.showMessageNotificationMM(VideoBoxApplication.getInstance(), true, zoomMessenger.getContactRequestsSessionID());
                    }
                }
            }, 1500);
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_TPV2_GetContactsPresence(List<String> list, List<String> list2) {
        try {
            Indicate_TPV2_GetContactsPresenceImpl(list, list2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_TPV2_GetContactsPresenceImpl(List<String> list, List<String> list2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_TPV2_GetContactsPresence(list, list2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_TPV2_SubscribePresence(List<String> list) {
        try {
            Indicate_TPV2_SubscribePresenceImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_TPV2_SubscribePresenceImpl(List<String> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            for (String Indicate_BuddyPresenceChanged : list) {
                ZMBuddySyncInstance.getInsatance().Indicate_BuddyPresenceChanged(Indicate_BuddyPresenceChanged);
            }
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_TPV2_SubscribePresence(list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_TPV2_WillExpirePresence(List<String> list, int i) {
        try {
            Indicate_TPV2_WillExpirePresenceImpl(list, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_TPV2_WillExpirePresenceImpl(List<String> list, int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && i == 3) {
            zoomMessenger.TPV2_UnsubscribePresence(list);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_TPV2_WillExpirePresence(list, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_UnsubscribeRequest(String str, String str2) {
        try {
            notify_UnsubscribeRequestImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_UnsubscribeRequestImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onNotifyUnsubscribeRequest(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifyBuddyJIDUpgrade(String str, String str2, String str3) {
        try {
            notifyBuddyJIDUpgradeImpl(str, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyBuddyJIDUpgradeImpl(String str, String str2, String str3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onNotifyBuddyJIDUpgrade(str, str2, str3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_OnSent(String str, String str2, int i) {
        try {
            FT_OnSentImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_OnSentImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).FT_OnSent(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
        try {
            FT_OnProgressImpl(str, str2, i, j, j2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_OnProgressImpl(String str, String str2, int i, long j, long j2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).FT_OnProgress(str, str2, i, j, j2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void E2E_MyStateUpdate(int i) {
        try {
            E2E_MyStateUpdateImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void E2E_MyStateUpdateImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).E2E_MyStateUpdate(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void E2E_SessionStateUpdate(String str, String str2, int i, int i2) {
        try {
            E2E_SessionStateUpdateImpl(str, str2, i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void E2E_SessionStateUpdateImpl(String str, String str2, int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).E2E_SessionStateUpdate(str, str2, i, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void E2E_MessageStateUpdate(String str, String str2, int i) {
        try {
            E2E_MessageStateUpdateImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void E2E_MessageStateUpdateImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).E2E_MessageStateUpdate(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void E2E_NotifyAutoLogoff() {
        try {
            E2E_NotifyAutoLogoffImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void E2E_NotifyAutoLogoffImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).E2E_NotifyAutoLogoff();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_OnResumed(String str, String str2, int i) {
        try {
            FT_OnResumedImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_OnResumedImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).FT_OnResumed(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Notify_SubscribeRequestUpdated(@NonNull String str) {
        try {
            Notify_SubscribeRequestUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Notify_SubscribeRequestUpdatedImpl(@NonNull String str) {
        ZMBuddySyncInstance.getInsatance().onNotifySubscribeRequestUpdated(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).onNotifySubscribeRequestUpdated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Confirm_HistoryReqComplete(String str, String str2, int i, int i2) {
        try {
            Confirm_HistoryReqCompleteImpl(str, str2, i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Confirm_HistoryReqCompleteImpl(String str, String str2, int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Confirm_HistoryReqComplete(str, str2, i, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_OnlineBuddies(@NonNull List<String> list) {
        try {
            Indicate_OnlineBuddiesImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_OnlineBuddiesImpl(@NonNull List<String> list) {
        ZMBuddySyncInstance.getInsatance().Indicate_OnlineBuddies(list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_OnlineBuddies(list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyGroupsRemoved(List<String> list) {
        try {
            Indicate_BuddyGroupsRemovedImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BuddyGroupsRemovedImpl(List<String> list) {
        ZMBuddySyncInstance.getInsatance().Indicate_BuddyGroupsRemoved(list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyGroupsRemoved(list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyPresenceChanged(@NonNull String str) {
        try {
            Indicate_BuddyPresenceChangedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BuddyPresenceChangedImpl(@NonNull String str) {
        ZMBuddySyncInstance.getInsatance().Indicate_BuddyPresenceChanged(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyPresenceChanged(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyGroupAdded(String str) {
        try {
            Indicate_BuddyGroupAddedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BuddyGroupAddedImpl(String str) {
        ZMBuddySyncInstance.getInsatance().Indicate_BuddyGroupAdded(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyGroupAdded(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyGroupInfoUpdated(String str) {
        try {
            Indicate_BuddyGroupInfoUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BuddyGroupInfoUpdatedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyGroupInfoUpdated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyGroupMembersUpdated(String str, @NonNull List<String> list) {
        try {
            Indicate_BuddyGroupMembersUpdatedImpl(str, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BuddyGroupMembersUpdatedImpl(String str, @NonNull List<String> list) {
        ZMBuddySyncInstance.getInsatance().Indicate_BuddyGroupMembersUpdated(str, list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyGroupMembersUpdated(str, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyGroupMembersAdded(@NonNull String str, @NonNull List<String> list) {
        try {
            Indicate_BuddyGroupMembersAddedImpl(str, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BuddyGroupMembersAddedImpl(@NonNull String str, @NonNull List<String> list) {
        ZMBuddySyncInstance.getInsatance().Indicate_BuddyGroupMembersAdded(str, list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyGroupMembersAdded(str, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyGroupMembersRemoved(String str, @NonNull List<String> list) {
        try {
            Indicate_BuddyGroupMembersRemoveddImpl(str, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BuddyGroupMembersRemoveddImpl(String str, @NonNull List<String> list) {
        ZMBuddySyncInstance.getInsatance().Indicate_BuddyGroupMembersRemoved(str, list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyGroupMembersRemoved(str, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyGroupMembersChanged(@Nullable byte[] bArr, boolean z) {
        if (bArr != null) {
            try {
                Indicate_BuddyGroupMembersChangedImpl(ChangedBuddyGroups.parseFrom(bArr), z);
            } catch (Throwable th) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            }
        }
    }

    private void Indicate_BuddyGroupMembersChangedImpl(ChangedBuddyGroups changedBuddyGroups, boolean z) {
        ZMBuddySyncInstance.getInsatance().Indicate_BuddyGroupMembersChanged(changedBuddyGroups, z);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyGroupMembersChanged(changedBuddyGroups, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void On_AddLocalPendingBuddy(String str, int i, String str2) {
        try {
            On_AddLocalPendingBuddyImpl(str, i, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void On_AddLocalPendingBuddyImpl(String str, int i, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).On_AddLocalPendingBuddy(str, i, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyAdded(@NonNull String str, @NonNull List<String> list) {
        try {
            Indicate_BuddyAddedImpl(str, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BuddyAddedImpl(@NonNull String str, @NonNull List<String> list) {
        ZMBuddySyncInstance.getInsatance().Indicate_BuddyAdded(str, list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyAdded(str, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        try {
            FT_DownloadByFileID_OnProgressImpl(str, str2, i, i2, i3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_DownloadByFileID_OnProgressImpl(String str, String str2, int i, int i2, int i3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i4 = 0; i4 < length; i4++) {
                ((IZoomMessengerUIListener) all[i4]).FT_DownloadByFileID_OnProgress(str, str2, i, i2, i3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_UploadToMyList_OnProgress(String str, int i, int i2, int i3) {
        try {
            FT_UploadToMyList_OnProgressImpl(str, i, i2, i3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_UploadToMyList_OnProgressImpl(String str, int i, int i2, int i3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).FT_UploadToMyList_OnProgress(str, i, i2, i3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FileDeleted(String str, String str2, int i) {
        try {
            Indicate_FileDeletedImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FileDeletedImpl(String str, String str2, int i) {
        if (i != 0) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                promptIMErrorMsg(globalContext.getString(C4558R.string.zm_alert_delete_file_failed), i);
            }
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_FileDeleted(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FileForwarded(String str, String str2, String str3, String str4, int i) {
        try {
            Indicate_FileForwardedImpl(str, str2, str3, str4, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FileForwardedImpl(String str, String str2, String str3, String str4, int i) {
        String str5;
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (!(globalContext == null || i == 4305)) {
            if (i == 0) {
                str5 = globalContext.getString(C4558R.string.zm_alert_msg_success);
            } else {
                str5 = globalContext.getString(C4558R.string.zm_alert_share_file_failed);
            }
            promptIMErrorMsg(str5, i);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_FileForwarded(str, str2, str3, str4, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
        try {
            Indicate_FileSharedImpl(str, str2, str3, str4, str5, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FileSharedImpl(String str, String str2, String str3, String str4, String str5, int i) {
        String str6;
        int i2 = i;
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext != null) {
            if (i2 == 0) {
                str6 = globalContext.getString(C4558R.string.zm_alert_msg_success);
            } else {
                str6 = globalContext.getString(C4558R.string.zm_alert_share_file_failed);
            }
            promptIMErrorMsg(str6, i2);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_FileShared(str, str2, str3, str4, str5, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FileUnshared(String str, String str2, int i) {
        try {
            Indicate_FileUnsharedImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FileUnsharedImpl(String str, String str2, int i) {
        if (i != 0) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                promptIMErrorMsg(globalContext.getString(C4558R.string.zm_alert_unshare_file_failed), i);
            }
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_FileUnshared(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_UploadToMyFiles_Sent(String str, String str2, int i) {
        try {
            Indicate_UploadToMyFiles_SentImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_UploadToMyFiles_SentImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_UploadToMyFiles_Sent(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_QuerySessionFilesResponse(String str, String str2, int i, List<String> list, long j, long j2) {
        try {
            Indicate_QuerySessionFilesResponseImpl(str, str2, i, list, j, j2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_QuerySessionFilesResponseImpl(String str, String str2, int i, List<String> list, long j, long j2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_QuerySessionFilesResponse(str, str2, i, list, j, j2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_QueryMyFilesResponse(String str, int i, List<String> list, long j, long j2) {
        try {
            Indicate_QueryMyFilesResponseImpl(str, i, list, j, j2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_QueryMyFilesResponseImpl(String str, int i, List<String> list, long j, long j2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_QueryMyFilesResponse(str, i, list, j, j2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_QueryAllFilesResponse(String str, int i, List<String> list, long j, long j2) {
        try {
            Indicate_QueryAllFilesResponseImpl(str, i, list, j, j2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_QueryAllFilesResponseImpl(String str, int i, List<String> list, long j, long j2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_QueryAllFilesResponse(str, i, list, j, j2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_QueryFilesSharedWithMeResponse(String str, int i, List<String> list, long j, long j2) {
        try {
            Indicate_QueryFilesSharedWithMeResponseImpl(str, i, list, j, j2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_QueryFilesSharedWithMeResponseImpl(String str, int i, List<String> list, long j, long j2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_QueryFilesSharedWithMeResponse(str, i, list, j, j2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_RenameFileResponse(int i, String str, String str2, String str3) {
        try {
            Indicate_RenameFileResponseImpl(i, str, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_RenameFileResponseImpl(int i, String str, String str2, String str3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_RenameFileResponse(i, str, str2, str3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_PreviewDownloaded(String str, String str2, int i) {
        try {
            Indicate_PreviewDownloadedImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_PreviewDownloadedImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_PreviewDownloaded(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FileDownloaded(String str, String str2, int i) {
        try {
            Indicate_FileDownloadedImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FileDownloadedImpl(String str, String str2, int i) {
        if (i != 0) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                promptIMErrorMsg(globalContext.getString(C4558R.string.zm_alert_download_file_failed), i);
            }
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_FileDownloaded(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FileStatusUpdated(String str) {
        try {
            Indicate_FileStatusUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FileStatusUpdatedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_FileStatusUpdated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_GetContactsPresence(@NonNull List<String> list, @NonNull List<String> list2) {
        try {
            Indicate_GetContactsPresenceImpl(list, list2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_GetContactsPresenceImpl(@NonNull List<String> list, @NonNull List<String> list2) {
        ZMBuddySyncInstance.getInsatance().Indicate_GetContactsPresence(list, list2);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_GetContactsPresence(list, list2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FileActionStatus(int i, String str, String str2, String str3, List<String> list, long j, long j2, String str4) {
        try {
            Indicate_FileActionStatusImpl(i, str, str2, str3, list, j, j2, str4);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00bd, code lost:
        if (r2.getSessionGroup().isGroupOperatorable() == false) goto L_0x00bf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00bf, code lost:
        if (r3 == false) goto L_0x00e3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00de, code lost:
        if (r3 == false) goto L_0x00e3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0106  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void Indicate_FileActionStatusImpl(int r22, java.lang.String r23, java.lang.String r24, java.lang.String r25, @androidx.annotation.Nullable java.util.List<java.lang.String> r26, long r27, long r29, java.lang.String r31) {
        /*
            r21 = this;
            r7 = r22
            r6 = r24
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r5 = r0.getZoomMessenger()
            if (r5 != 0) goto L_0x000f
            return
        L_0x000f:
            if (r26 == 0) goto L_0x012f
            int r0 = r26.size()
            if (r0 <= 0) goto L_0x012f
            java.util.Iterator r20 = r26.iterator()
            r15 = r31
        L_0x001d:
            boolean r0 = r20.hasNext()
            if (r0 == 0) goto L_0x012c
            java.lang.Object r0 = r20.next()
            r4 = r0
            java.lang.String r4 = (java.lang.String) r4
            java.lang.String r0 = ""
            java.lang.String r1 = ""
            com.zipow.videobox.ptapp.mm.ZoomChatSession r2 = r5.getSessionById(r4)
            if (r2 != 0) goto L_0x0037
            r14 = r23
            goto L_0x001d
        L_0x0037:
            r8 = 1
            if (r7 == r8) goto L_0x003d
            r9 = 2
            if (r7 != r9) goto L_0x00fc
        L_0x003d:
            int r9 = (r27 > r29 ? 1 : (r27 == r29 ? 0 : -1))
            if (r9 != 0) goto L_0x0043
            goto L_0x00fc
        L_0x0043:
            boolean r9 = r2.isGroup()
            if (r9 == 0) goto L_0x004c
            r10 = r1
            r9 = r4
            goto L_0x004e
        L_0x004c:
            r9 = r0
            r10 = r4
        L_0x004e:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r0 = r5.getBuddyWithJID(r6)
            if (r0 != 0) goto L_0x0055
            return
        L_0x0055:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r1 = r5.getMyself()
            if (r1 != 0) goto L_0x005c
            return
        L_0x005c:
            java.lang.String r11 = r1.getJid()
            boolean r11 = android.text.TextUtils.equals(r11, r4)
            if (r11 != 0) goto L_0x00e1
            com.zipow.videobox.ptapp.PTApp r11 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.MMFileContentMgr r11 = r11.getZoomFileContentMgr()
            if (r11 != 0) goto L_0x0071
            return
        L_0x0071:
            r14 = r23
            com.zipow.videobox.ptapp.mm.ZoomFile r12 = r11.getFileWithWebFileID(r14)
            if (r12 == 0) goto L_0x0089
            java.lang.String r13 = r12.getOwner()
            java.lang.String r3 = r1.getJid()
            boolean r3 = p021us.zoom.androidlib.util.StringUtil.isSameString(r13, r3)
            r11.destroyFileObject(r12)
            goto L_0x008a
        L_0x0089:
            r3 = 0
        L_0x008a:
            boolean r11 = r2.isGroup()
            if (r11 == 0) goto L_0x00a2
            com.zipow.videobox.ptapp.mm.ZoomGroup r11 = r2.getSessionGroup()
            if (r11 == 0) goto L_0x00a2
            com.zipow.videobox.ptapp.mm.ZoomGroup r11 = r2.getSessionGroup()
            boolean r11 = r11.isRoom()
            if (r11 == 0) goto L_0x00a2
            r11 = 1
            goto L_0x00a3
        L_0x00a2:
            r11 = 0
        L_0x00a3:
            if (r11 == 0) goto L_0x00c2
            java.lang.String r0 = r1.getJid()
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isSameString(r6, r0)
            if (r0 != 0) goto L_0x00e4
            com.zipow.videobox.ptapp.mm.ZoomGroup r0 = r2.getSessionGroup()
            if (r0 == 0) goto L_0x00bf
            com.zipow.videobox.ptapp.mm.ZoomGroup r0 = r2.getSessionGroup()
            boolean r0 = r0.isGroupOperatorable()
            if (r0 != 0) goto L_0x00e4
        L_0x00bf:
            if (r3 == 0) goto L_0x00e3
            goto L_0x00e4
        L_0x00c2:
            boolean r11 = r2.isGroup()
            if (r11 == 0) goto L_0x00d0
            com.zipow.videobox.ptapp.mm.ZoomGroup r2 = r2.getSessionGroup()
            if (r2 != 0) goto L_0x00d0
            goto L_0x001d
        L_0x00d0:
            java.lang.String r0 = r0.getJid()
            java.lang.String r1 = r1.getJid()
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isSameString(r0, r1)
            if (r0 != 0) goto L_0x00e4
            if (r3 == 0) goto L_0x00e3
            goto L_0x00e4
        L_0x00e1:
            r14 = r23
        L_0x00e3:
            r8 = 0
        L_0x00e4:
            if (r8 == 0) goto L_0x00fc
            r0 = 1000(0x3e8, double:4.94E-321)
            long r12 = r27 / r0
            r0 = 80
            r8 = r5
            r11 = r24
            r14 = r0
            r16 = r27
            r18 = r29
            java.lang.String r0 = r8.insertSystemMessage(r9, r10, r11, r12, r14, r15, r16, r18)
            r8 = r21
            r15 = r0
            goto L_0x00fe
        L_0x00fc:
            r8 = r21
        L_0x00fe:
            us.zoom.androidlib.util.ListenerList r0 = r8.mListenerList
            us.zoom.androidlib.util.IListener[] r9 = r0.getAll()
            if (r9 == 0) goto L_0x0126
            int r10 = r9.length
            r11 = 0
        L_0x0108:
            if (r11 >= r10) goto L_0x0124
            r0 = r9[r11]
            com.zipow.videobox.ptapp.ZoomMessengerUI$IZoomMessengerUIListener r0 = (com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener) r0
            r1 = r22
            r2 = r23
            r3 = r24
            r12 = r4
            r4 = r25
            r13 = r5
            r5 = r12
            r6 = r15
            r0.Indicate_FileActionStatus(r1, r2, r3, r4, r5, r6)
            int r11 = r11 + 1
            r6 = r24
            r4 = r12
            r5 = r13
            goto L_0x0108
        L_0x0124:
            r13 = r5
            goto L_0x0127
        L_0x0126:
            r13 = r5
        L_0x0127:
            r6 = r24
            r5 = r13
            goto L_0x001d
        L_0x012c:
            r8 = r21
            goto L_0x0131
        L_0x012f:
            r8 = r21
        L_0x0131:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.ZoomMessengerUI.Indicate_FileActionStatusImpl(int, java.lang.String, java.lang.String, java.lang.String, java.util.List, long, long, java.lang.String):void");
    }

    /* access modifiers changed from: protected */
    public void Indicate_NewFileSharedByOthers(String str) {
        try {
            Indicate_NewFileSharedByOthersImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_NewFileSharedByOthersImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_NewFileSharedByOthers(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_NewPersonalFile(String str) {
        try {
            Indicate_NewPersonalFileImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_NewPersonalFileImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_NewPersonalFile(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
        try {
            FT_OnDownloadByMsgIDTimeOutImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_OnDownloadByMsgIDTimeOutImpl(String str, String str2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.FT_Cancel(str, str2, 1);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).FT_OnDownloadByMsgIDTimeOut(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_AvailableAlert(@NonNull String str, String str2) {
        try {
            Indicate_AvailableAlertImpl(str, str2);
            AlertWhenAvailableHelper.getInstance().showAlertNotification(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_AvailableAlertImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_AvailableAlert(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_UploadFileInChatTimeOut(String str, String str2) {
        try {
            FT_UploadFileInChatTimeOutImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_UploadFileInChatTimeOutImpl(String str, String str2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.FT_Cancel(str, str2, 1);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).FT_UploadFileInChatTimeOut(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_OnDownloadByFileIDTimeOut(@NonNull String str, @NonNull String str2) {
        try {
            FT_OnDownloadByFileIDTimeOutImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_OnDownloadByFileIDTimeOutImpl(@NonNull String str, @NonNull String str2) {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            zoomFileContentMgr.cancelFileTransfer(str, str2);
        }
        StickerManager.FT_OnDownloadByFileIDTimeOutImpl(str, str2);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).FT_OnDownloadByFileIDTimeOut(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void FT_UploadToMyList_TimeOut(String str) {
        try {
            FT_UploadToMyList_TimeOutImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void FT_UploadToMyList_TimeOutImpl(String str) {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            zoomFileContentMgr.cancelFileTransfer(str, str);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).FT_UploadToMyList_TimeOut(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_MessageDeleted(String str, String str2) {
        try {
            Indicate_MessageDeletedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_MessageDeletedImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_MessageDeleted(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_MessageContext(int i, String str, String str2, List<String> list) {
        try {
            Indicate_MessageContextImpl(i, str, str2, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_MessageContextImpl(int i, String str, String str2, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_MessageContext(i, str, str2, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BlockedUsersUpdated() {
        try {
            Indicate_BlockedUsersUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BlockedUsersUpdatedImpl() {
        ZMBuddySyncInstance.getInsatance().Indicate_BlockedUsersUpdated();
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BlockedUsersUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BlockedUsersAdded(List<String> list) {
        try {
            Indicate_BlockedUsersAddedImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BlockedUsersAddedImpl(List<String> list) {
        ZMBuddySyncInstance.getInsatance().Indicate_BlockedUsersAdded(list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BlockedUsersAdded(list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BlockedUsersRemoved(List<String> list) {
        try {
            Indicate_BlockedUsersRemovedImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_BlockedUsersRemovedImpl(List<String> list) {
        ZMBuddySyncInstance.getInsatance().Indicate_BlockedUsersRemoved(list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BlockedUsersRemoved(list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        try {
            On_NotifyGroupDestroyImpl(str, str2, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void On_NotifyGroupDestroyImpl(String str, String str2, long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).On_NotifyGroupDestroy(str, str2, j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
        try {
            On_DestroyGroupImpl(i, str, str2, str3, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void On_DestroyGroupImpl(int i, String str, String str2, String str3, long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).On_DestroyGroup(i, str, str2, str3, j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void On_AssignGroupAdmins(@NonNull byte[] bArr) {
        try {
            On_AssignGroupAdminsImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void On_AssignGroupAdminsImpl(@NonNull byte[] bArr) {
        boolean z;
        String str;
        String str2;
        long j;
        int i;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                try {
                    GroupCallBackInfo parseFrom = GroupCallBackInfo.parseFrom(bArr);
                    if (parseFrom != null) {
                        List buddiesList = parseFrom.getBuddiesList();
                        if (buddiesList != null) {
                            String jid = myself.getJid();
                            if (jid != null) {
                                ArrayList arrayList = new ArrayList();
                                if (!CollectionsUtil.isCollectionEmpty(buddiesList)) {
                                    boolean z2 = false;
                                    for (int i2 = 0; i2 < buddiesList.size(); i2++) {
                                        String jid2 = ((BuddyUserInfo) buddiesList.get(i2)).getJid();
                                        if (jid.equals(jid2)) {
                                            z2 = true;
                                        }
                                        arrayList.add(jid2);
                                    }
                                    z = z2;
                                } else {
                                    z = false;
                                }
                                int result = parseFrom.getResult();
                                String jid3 = parseFrom.getActionOwner().getJid();
                                String msgID = parseFrom.getMsgID();
                                parseFrom.getMsgID();
                                String groupID = parseFrom.getGroupID();
                                long tm = parseFrom.getTm();
                                long tmServerside = parseFrom.getTmServerside();
                                long prevMsgtime = parseFrom.getPrevMsgtime();
                                parseFrom.getMaxAllowed();
                                String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
                                boolean equals = jid3.equals(jid);
                                String str3 = "";
                                if (equals) {
                                    str3 = BuddyNameUtil.getMyDisplayName(myself);
                                } else {
                                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(jid3);
                                    if (buddyWithJID != null) {
                                        str3 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null, false);
                                    }
                                }
                                GroupAction groupAction = new GroupAction(7, TextUtils.isEmpty(str3) ? parseFrom.getActionOwner().getDisplayName() : str3, strArr, equals, z, null);
                                groupAction.setActionOwnerId(jid3);
                                groupAction.setGroupId(groupID);
                                groupAction.setTime(tm);
                                ZoomGroup groupById = zoomMessenger.getGroupById(groupID);
                                if (groupById != null) {
                                    groupAction.setChannel(groupById.isRoom());
                                }
                                if (result == 0) {
                                    String serializeToString = GroupAction.serializeToString(groupAction);
                                    j = tm;
                                    str2 = groupID;
                                    i = result;
                                    str = jid3;
                                    String insertSystemMessage = zoomMessenger.insertSystemMessage(groupID, jid3, serializeToString, tm, 24, msgID, tmServerside, prevMsgtime);
                                    IListener[] all = this.mListenerList.getAll();
                                    if (all != null) {
                                        for (IListener iListener : all) {
                                            ((IZoomMessengerUIListener) iListener).onGroupAction(i, groupAction, insertSystemMessage);
                                        }
                                    }
                                } else {
                                    j = tm;
                                    str2 = groupID;
                                    i = result;
                                    str = jid3;
                                }
                                IListener[] all2 = this.mListenerList.getAll();
                                if (all2 != null) {
                                    int length = all2.length;
                                    for (int i3 = 0; i3 < length; i3++) {
                                        ((IZoomMessengerUIListener) all2[i3]).On_AssignGroupAdmins(i, str, str2, arrayList, j);
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException unused) {
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FetchUserProfileResult(byte[] bArr) {
        try {
            Indicate_FetchUserProfileResultImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FetchUserProfileResultImpl(@Nullable byte[] bArr) {
        if (bArr != null) {
            try {
                UserProfileResult parseFrom = UserProfileResult.parseFrom(bArr);
                IListener[] all = this.mListenerList.getAll();
                if (all != null) {
                    for (IListener iListener : all) {
                        ((IZoomMessengerUIListener) iListener).Indicate_FetchUserProfileResult(parseFrom);
                    }
                }
            } catch (InvalidProtocolBufferException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_SignatureSet(String str, int i) {
        try {
            Indicate_SignatureSetImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_SignatureSetImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_SignatureSet(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_VCardInfoReady(@NonNull String str) {
        try {
            Indicate_VCardInfoReadyImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_VCardInfoReadyImpl(@NonNull String str) {
        ZMBuddySyncInstance.getInsatance().Indicate_VCardInfoReady(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_VCardInfoReady(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_BuddyAccountStatusChange(String str, int i) {
        try {
            InIndicate_BuddyAccountStatusChangeImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void InIndicate_BuddyAccountStatusChangeImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_BuddyAccountStatusChange(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_RevokeMessageResult(String str, String str2, String str3, boolean z, long j, String str4, int i, long j2, long j3, String str5, long j4, boolean z2) {
        try {
            Indicate_RevokeMessageResultImpl(str, str2, str3, z, j, str4, i, j2, j3, str5, j4, z2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_RevokeMessageResultImpl(String str, String str2, String str3, boolean z, long j, String str4, int i, long j2, long j3, String str5, long j4, boolean z2) {
        String str6;
        ZoomMessengerUI zoomMessengerUI;
        ZoomMessenger zoomMessenger;
        ZoomChatSession zoomChatSession;
        String str7;
        String str8;
        String str9 = str;
        String str10 = str2;
        int i2 = i;
        long j5 = j4;
        String str11 = null;
        if (z) {
            ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
            if (!StringUtil.isEmptyOrNull(str2) && zoomMessenger2 != null) {
                ZoomChatSession sessionById = zoomMessenger2.getSessionById(str10);
                if (sessionById != null) {
                    ZoomBuddy myself = zoomMessenger2.getMyself();
                    if (myself != null) {
                        if (!z2) {
                            boolean z3 = sessionById.isGroup() && sessionById.getSessionGroup() != null && sessionById.getSessionGroup().isRoom();
                            boolean z4 = i2 == 0 || i2 == 2;
                            if (!TextUtils.equals(myself.getJid(), str10)) {
                                if (!TextUtils.equals(str9, myself.getJid())) {
                                    if (!TextUtils.equals(str4, myself.getJid()) && (z4 || !z3 || !sessionById.getSessionGroup().isGroupOperatorable())) {
                                        zoomChatSession = sessionById;
                                        zoomMessenger = zoomMessenger2;
                                    }
                                }
                                if (sessionById.getMessageById(str3) == null) {
                                    if (zoomMessenger2.getGroupById(str10) != null) {
                                        str7 = "";
                                        str8 = str10;
                                    } else {
                                        str8 = "";
                                        str7 = str10;
                                    }
                                    zoomChatSession = sessionById;
                                    zoomMessenger = zoomMessenger2;
                                    str11 = zoomMessenger2.insertSystemMessage(str8, str7, str, j / 1000, true, 80, str5, j2, j3, false);
                                } else {
                                    zoomChatSession = sessionById;
                                    zoomMessenger = zoomMessenger2;
                                }
                            } else {
                                zoomChatSession = sessionById;
                                zoomMessenger = zoomMessenger2;
                            }
                        } else {
                            zoomChatSession = sessionById;
                            zoomMessenger = zoomMessenger2;
                        }
                        long j6 = j4;
                        if (j6 != 0 && TextUtils.equals(str9, myself.getJid())) {
                            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                            ZoomChatSession zoomChatSession2 = zoomChatSession;
                            ZoomMessage messageByServerTime = zoomChatSession2.getMessageByServerTime(j6, true);
                            if (!(threadDataProvider == null || messageByServerTime == null || !threadDataProvider.needRecallDeletedThread(messageByServerTime))) {
                                zoomChatSession2.revokeMessageByXMPPGuid(messageByServerTime.getMessageXMPPGuid());
                            }
                        }
                        zoomMessengerUI = this;
                        str6 = str11;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            long j7 = j5;
            zoomMessengerUI = this;
            str6 = null;
        }
        IListener[] all = zoomMessengerUI.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            int i3 = 0;
            while (i3 < length) {
                int i4 = length;
                int i5 = i3;
                IListener[] iListenerArr = all;
                ((IZoomMessengerUIListener) all[i3]).Indicate_RevokeMessageResult(str, str2, str3, str6, j2, j4, z);
                i3 = i5 + 1;
                length = i4;
                all = iListenerArr;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Notify_DBLoadSessionLastMessagesDone() {
        try {
            Notify_DBLoadSessionLastMessagesDoneImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Notify_DBLoadSessionLastMessagesDoneImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Notify_DBLoadSessionLastMessagesDone();
            }
        }
    }

    private void NotifyOutdatedHistoryRemoved(@Nullable List<String> list, long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).NotifyOutdatedHistoryRemoved(list, j);
            }
        }
    }

    public void ZoomPrensece_OnUserOptionUpated() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).ZoomPrensece_OnUserOptionUpated();
            }
        }
    }

    public void Indicate_SyncAvailableAlert(String str) {
        AlertWhenAvailableHelper.getInstance().refreshRingBellOnUI(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_SyncAvailableAlert(str);
            }
        }
    }

    public void Indicate_GetAllAvailableAlert() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_GetAllAvailableAlert();
            }
        }
    }

    public void notifyWebSipStatus(SIPConfiguration sIPConfiguration) {
        PTApp.getInstance().updateSipPhoneStatus(sIPConfiguration);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).notifyWebSipStatus(sIPConfiguration);
            }
        }
    }

    public void NotifyLocalAddressChanged(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).NotifyLocalAddressChanged(str, str2);
            }
        }
    }

    private void promptIMInformationBarries() {
        ForegroundTaskManager.getInstance().runInForeground(new ForegroundTask("promptIMInformationBarries") {
            public void run(ZMActivity zMActivity) {
                IntegrationActivity.promptInfomationBarries(VideoBoxApplication.getInstance());
            }

            public boolean isValidActivity(String str) {
                if (LauncherActivity.class.getName().equals(str) || LoginActivity.class.getName().equals(str)) {
                    return false;
                }
                return super.isValidActivity(str);
            }

            public boolean hasAnotherProcessAtFront() {
                IConfService confService = VideoBoxApplication.getInstance().getConfService();
                if (confService != null) {
                    try {
                        return confService.isConfAppAtFront();
                    } catch (RemoteException unused) {
                    }
                }
                return false;
            }
        });
    }

    private void promptIMErrorMsg(final String str, final int i) {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity == null || !frontActivity.isActive() || frontActivity.isFinishing()) {
            ForegroundTaskManager.getInstance().runInForeground(new ForegroundTask("promptIMErrorMsg") {
                public void run(ZMActivity zMActivity) {
                    IntegrationActivity.promptIMErrorMsg(VideoBoxApplication.getInstance(), str, i);
                }

                public boolean isValidActivity(String str) {
                    if (LauncherActivity.class.getName().equals(str)) {
                        return false;
                    }
                    return super.isValidActivity(str);
                }

                public boolean hasAnotherProcessAtFront() {
                    IConfService confService = VideoBoxApplication.getInstance().getConfService();
                    if (confService != null) {
                        try {
                            return confService.isConfAppAtFront();
                        } catch (RemoteException unused) {
                        }
                    }
                    return false;
                }
            });
            return;
        }
        IntegrationActivity.promptIMErrorMsg(VideoBoxApplication.getInstance(), str, i);
    }

    /* access modifiers changed from: protected */
    public void Indicate_DownloadFileByUrl(String str, int i) {
        try {
            Indicate_DownloadFileByUrlIml(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_DownloadFileByUrlIml(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_DownloadFileByUrlIml(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_SendAddonCommandResult(String str, boolean z) {
        try {
            Indicate_SendAddonCommandResultIml(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_SendAddonCommandResultIml(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_SendAddonCommandResultIml(str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_AddAvailableAlert(String str, boolean z) {
        try {
            Indicate_AddAvailableAlertIml(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_AddAvailableAlertIml(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_AddAvailableAlert(str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_RemoveAvailableAlert(String str, boolean z) {
        try {
            Indicate_RemoveAvailableAlertIml(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_RemoveAvailableAlertIml(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_RemoveAvailableAlert(str, z);
            }
        }
    }

    private void Indicate_EditMessageResult(String str, String str2, String str3, long j, long j2, boolean z) {
        try {
            Indicate_EditMessageResultIml(str, str2, str3, j, j2, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i = 0; i < length; i++) {
                ((IZoomMessengerUIListener) all[i]).Indicate_EditMessageResultIml(str, str2, str3, j, j2, z);
            }
        }
    }

    private void Indicate_GetHotGiphyInfoResult(int i, String str, List<String> list, String str2, String str3) {
        try {
            Indicate_GetHotGiphyInfoResultIml(i, str, list, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_GetHotGiphyInfoResultIml(int i, String str, List<String> list, String str2, String str3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_GetHotGiphyInfoResult(i, str, list, str2, str3);
            }
        }
    }

    private void Indicate_OutgoingCallAction(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        try {
            Indicate_OutgoingCallActionIml(str, str2, str3, str4, str5, j, i, str6, j2, j3, j4, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_OutgoingCallActionIml(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        IncomingCallManager.getInstance().handleCallActionMessage(str, str2, str3, str4, str5, j, i, str6, j2, j3, j4, z);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_OutgoingCallAction(str, str2, str3, str4, str5, j, i, str6, j2, j3, j4, z);
            }
        }
    }

    private void indicate_CallActionResponded(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        try {
            indicate_CallActionRespondedIml(str, str2, str3, str4, str5, j, i, str6, j2, j3, j4, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void indicate_CallActionRespondedIml(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        IncomingCallManager.getInstance().handleCallActionMessage(str, str2, str3, str4, str5, j, i, str6, j2, j3, j4, z);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).indicate_CallActionRespondedIml(str, str2, str3, str4, str5, j, i, str6, j2, j3, j4, z);
            }
        }
    }

    private void confirm_EditedFileDownloaded(Map<String, String> map, int i) {
        try {
            confirm_EditedFileDownloadedIml(i, map);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void confirm_EditedFileDownloadedIml(int i, Map<String, String> map) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).confirm_EditedFileDownloadedIml(i, map);
            }
        }
    }

    private void NotifyChatAvailableInfoUpdate(String str) {
        try {
            NotifyChatAvailableInfoUpdateIml(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void NotifyChatAvailableInfoUpdateIml(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).NotifyChatAvailableInfoUpdateIml(str);
            }
        }
    }

    private void Indicate_GetGIFFromGiphyResult(int i, String str, List<String> list, String str2, String str3) {
        try {
            Indicate_GetGIFFromGiphyResultIml(i, str, list, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_GetGIFFromGiphyResultIml(int i, String str, List<String> list, String str2, String str3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_GetGIFFromGiphyResultIml(i, str, list, str2, str3);
            }
        }
    }

    private void Indicate_DownloadGIFFromGiphyResult(int i, String str, String str2, String str3, String str4, String str5) {
        try {
            Indicate_DownloadGIFFromGiphyResultIml(i, str, str2, str3, str4, str5);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_DownloadGIFFromGiphyResultIml(int i, String str, String str2, String str3, String str4, String str5) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).Indicate_DownloadGIFFromGiphyResultIml(i, str, str2, str3, str4, str5);
            }
        }
    }

    private void On_MyPresenceChanged(int i, int i2) {
        try {
            On_MyPresenceChangedIml(i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void On_MyPresenceChangedIml(int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).On_MyPresenceChanged(i, i2);
            }
        }
    }

    private void notify_StarMessageDataUpdate() {
        try {
            notify_StarMessageDataUpdateIml();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_StarMessageDataUpdateIml() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).notify_StarMessageDataUpdate();
            }
        }
    }

    private void notify_StarMessagesData(String str, int i, byte[] bArr) {
        try {
            notify_StarMessagesDataIml(str, i, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_StarMessagesDataIml(String str, int i, byte[] bArr) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).notify_StarMessagesData(str, i, bArr);
            }
        }
    }

    private void notifyStarSessionDataUpdated() {
        try {
            notifyStarSessionDataUpdatedIml();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyStarSessionDataUpdatedIml() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).notifyStarSessionDataUpdate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Notify_SubscriptionIsRestrictV2(String str, int i) {
        try {
            Notify_SubscriptionIsRestrictImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Notify_SubscriptionIsRestrictImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Notify_SubscriptionIsRestrictV2(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Notify_SubscriptionIsRestrict(String str, boolean z) {
        try {
            Notify_SubscriptionIsRestrictImpl(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Notify_SubscriptionIsRestrictImpl(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Notify_SubscriptionIsRestrict(str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Notify_SubscribeRequestSent(String str, int i) {
        try {
            Notify_SubscribeRequestSentImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Notify_SubscribeRequestSentImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Notify_SubscribeRequestSent(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void NotifyChatUnavailable(String str, int i) {
        try {
            NotifyChatUnavailableImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void NotifyChatUnavailableImpl(String str, int i) {
        String str2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null && !sessionById.isGroup()) {
                String str3 = "";
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                if (buddyWithJID != null) {
                    str3 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                }
                String str4 = "";
                VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
                if (instance2 != null) {
                    int i2 = 0;
                    if (i == 1 || i == 3) {
                        str2 = String.format(instance2.getString(C4558R.string.zm_mm_lbl_chat_disabled_by_their_admin_51246), new Object[]{str3});
                    } else if (i == 2) {
                        str2 = instance2.getString(C4558R.string.zm_mm_lbl_chat_disabled_by_own_admin_51246);
                    } else if (i == 5) {
                        str2 = instance2.getString(C4558R.string.zm_lbl_user_not_exist);
                    } else if (i == 4) {
                        str2 = String.format(instance2.getString(C4558R.string.zm_mm_lbl_blocked_by_opposite_side_62107), new Object[]{str3});
                    } else if (i == 9) {
                        String format = String.format(instance2.getString(C4558R.string.zm_mm_lbl_block_can_not_delete_62698), new Object[]{str3});
                        IListener[] all = this.mListenerList.getAll();
                        if (all != null) {
                            int length = all.length;
                            while (i2 < length) {
                                ((IZoomMessengerUIListener) all[i2]).NotifyDeleteMsgFailed(str, format);
                                i2++;
                            }
                        }
                        return;
                    } else if (i == 10) {
                        String format2 = String.format(instance2.getString(C4558R.string.zm_mm_lbl_block_can_not_edit_62698), new Object[]{str3});
                        IListener[] all2 = this.mListenerList.getAll();
                        if (all2 != null) {
                            int length2 = all2.length;
                            while (i2 < length2) {
                                ((IZoomMessengerUIListener) all2[i2]).NotifyEditMsgFailed(str, format2);
                                i2++;
                            }
                        }
                        return;
                    } else if (i == 12) {
                        String format3 = String.format(instance2.getString(C4558R.string.zm_mm_information_barries_dialog_chat_msg_115072), new Object[]{str3});
                        IListener[] all3 = this.mListenerList.getAll();
                        if (all3 != null) {
                            int length3 = all3.length;
                            while (i2 < length3) {
                                ((IZoomMessengerUIListener) all3[i2]).NotifyInfoBarriesMsg(str, format3);
                                i2++;
                            }
                        }
                        return;
                    } else {
                        str2 = i == 11 ? instance2.getString(C4558R.string.zm_mm_msg_chat_disable_dialog_content_83185) : str4;
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        String insertChatUnavailableSystemMessage = zoomMessenger.insertChatUnavailableSystemMessage("", str, str2, CmmTime.getMMNow(), true, 88);
                        IListener[] all4 = this.mListenerList.getAll();
                        if (all4 != null) {
                            int length4 = all4.length;
                            while (i2 < length4) {
                                ((IZoomMessengerUIListener) all4[i2]).NotifyChatUnavailable(str, insertChatUnavailableSystemMessage);
                                i2++;
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void NotifyCallUnavailable(@NonNull byte[] bArr) {
        try {
            CallAvailableInfo parseFrom = CallAvailableInfo.parseFrom(bArr);
            if (parseFrom != null && parseFrom.getState() == 6) {
                String buddyJid = parseFrom.getBuddyJid();
                if (!TextUtils.isEmpty(buddyJid)) {
                    IListener[] all = this.mListenerList.getAll();
                    if (all != null) {
                        for (IListener iListener : all) {
                            ((IZoomMessengerUIListener) iListener).NotifyCallUnavailable(buddyJid, parseFrom.getMeetingNumber());
                        }
                    }
                }
            }
        } catch (IOException unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void Notify_ChatSessionMarkUnreadUpdate(byte[] bArr) {
        try {
            Notify_ChatSessionMarkUnreadUpdateImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Notify_ChatSessionMarkUnreadUpdateImpl(byte[] bArr) {
        SessionMessageInfoMap sessionMessageInfoMap;
        try {
            sessionMessageInfoMap = SessionMessageInfoMap.parseFrom(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            sessionMessageInfoMap = null;
        }
        if (sessionMessageInfoMap != null) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IZoomMessengerUIListener) iListener).Notify_ChatSessionMarkUnreadUpdate(sessionMessageInfoMap);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_FileMessageDeleted(String str, String str2) {
        try {
            Indicate_FileMessageDeletedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FileMessageDeletedImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_FileMessageDeleted(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnPersonalGroupResponse(byte[] bArr) {
        try {
            OnPersonalGroupResponseImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPersonalGroupResponseImpl(byte[] bArr) {
        ZMBuddySyncInstance.getInsatance().onPersonalGroupResponse(bArr);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).OnPersonalGroupResponse(bArr);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void NotifyPersonalGroupSync(int i, String str, List<String> list, String str2, String str3) {
        try {
            NotifyPersonalGroupSyncImpl(i, str, list, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void NotifyPersonalGroupSyncImpl(int i, String str, List<String> list, String str2, String str3) {
        ZMBuddySyncInstance.getInsatance().notifyPersonalGroupSync(i, str, list, str2, str3);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IZoomMessengerUIListener) all[i2]).NotifyPersonalGroupSync(i, str, list, str2, str3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Notify_ChatSessionUnreadCountReady(List<String> list) {
        try {
            Notify_ChatSessionUnreadCountReadyImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Notify_ChatSessionUnreadCountReadyImpl(List<String> list) {
        ZMSessionsMgr.getInstance().onChatSessionUnreadCountReady(list);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Notify_ChatSessionUnreadCountReady(list);
            }
        }
        MessageSyncer.getInstance().onChatSessionUnreadCountReady();
    }

    /* access modifiers changed from: protected */
    public void Indicate_SessionOfflineMessageFinished(String str) {
        try {
            Indicate_SessionOfflineMessageFinishedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_SessionOfflineMessageFinishedImpl(String str) {
        ZMSessionsMgr.getInstance().Indicate_SessionOfflineMessageFinished(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_SessionOfflineMessageFinished(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_LoginOfflineMessageFinished() {
        try {
            Indicate_LoginOfflineMessageFinishedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_LoginOfflineMessageFinishedImpl() {
        ZMSessionsMgr.getInstance().Indicate_LoginOfflineMessageFinished();
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_LoginOfflineMessageFinished();
            }
        }
    }

    private void Indicate_FileAttachInfoUpdate(String str, String str2, int i) {
        try {
            Indicate_FileAttachInfoUpdateImpl(str, str2, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_FileAttachInfoUpdateImpl(String str, String str2, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_FileAttachInfoUpdate(str, str2, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void indicate_BuddyBlockedByIB(List<String> list) {
        try {
            indicate_BuddyBlockedByIBImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void indicate_BuddyBlockedByIBImpl(List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).indicate_BuddyBlockedByIB(list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_MobileOnlineBuddiesFromDB(List<String> list) {
        try {
            Indicate_MobileOnlineBuddiesFromDBImpl(list);
            Indicate_OnlineBuddiesImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_MobileOnlineBuddiesFromDBImpl(List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Indicate_MobileOnlineBuddiesFromDB(list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void On_BroadcastUpdate(int i, String str, boolean z) {
        try {
            On_BroadcastUpdateImpl(i, str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void On_BroadcastUpdateImpl(int i, String str, boolean z) {
        setBroadcastName();
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).On_BroadcastUpdate(i, str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Notify_BroadcastsReady() {
        try {
            Notify_BroadcastsReadyImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void setBroadcastName() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            List broadcast = zoomMessenger.getBroadcast();
            if (!CollectionsUtil.isCollectionEmpty(broadcast)) {
                String str = (String) broadcast.get(0);
                if (!StringUtil.isEmptyOrNull(str)) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(str);
                    if (groupById != null) {
                        groupById.setBroadcastName(VideoBoxApplication.getInstance().getString(C4558R.string.zm_msg_announcements_108966));
                    }
                }
            }
        }
    }

    private void Notify_BroadcastsReadyImpl() {
        setBroadcastName();
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).Notify_BroadcastsReady();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void NotifyIMWebSettingUpdated(int i) {
        try {
            NotifyIMWebSettingUpdatedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void NotifyIMWebSettingUpdatedImpl(int i) {
        if (i == 3) {
            promptIMInformationBarries();
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessengerUIListener) iListener).NotifyIMWebSettingUpdated(i);
            }
        }
    }
}
