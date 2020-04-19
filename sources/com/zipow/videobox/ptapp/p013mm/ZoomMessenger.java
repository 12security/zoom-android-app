package com.zipow.videobox.ptapp.p013mm;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.eventbus.ZMStarEvent;
import com.zipow.videobox.ptapp.IMProtos.EmojiList;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrationInfo;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrations;
import com.zipow.videobox.ptapp.IMProtos.FontStyte;
import com.zipow.videobox.ptapp.IMProtos.GiphyMsgInfo;
import com.zipow.videobox.ptapp.IMProtos.LocalStorageTimeInterval;
import com.zipow.videobox.ptapp.IMProtos.MessageInput;
import com.zipow.videobox.ptapp.IMProtos.MessageInput.Builder;
import com.zipow.videobox.ptapp.IMProtos.RobotCommand;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.AllBuddyInfo;
import com.zipow.videobox.ptapp.PTAppProtos.NumberMatchedBuddyItemList;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.util.EmojiHelper;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomMessenger */
public class ZoomMessenger {
    private static final String TAG = "ZoomMessenger";
    private long mNativeHandle = 0;

    private native boolean FTCancelImpl(long j, String str, String str2, int i);

    private native boolean FTDownloadImpl(long j, String str, String str2, String str3);

    private native boolean FTPauseImpl(long j, String str, String str2);

    private native boolean FTResumeImpl(long j, String str, String str2, String str3);

    private native int GetTotalUnreadMessageCountBySettingImpl(long j);

    private native int TPV2GetContactsPresenceImpl(long j, List<String> list);

    private native int TPV2SubscribePresenceImpl(long j, List<String> list, int i);

    private native int TPV2UnsubscribePresenceImpl(long j, List<String> list);

    private native int accountChatGetOptionImpl(long j);

    private native boolean ackBuddySubscribeImpl(long j, String str, boolean z);

    private native boolean addAvailableAlertBuddyImpl(long j, String str);

    private native boolean addBuddyByEmailImpl(long j, String str);

    private native boolean addBuddyByJIDImpl(long j, String str, String str2, String str3, String str4, String str5);

    private native boolean addBuddyToGroupImpl(long j, String str, List<String> list);

    @Nullable
    private native String addBuddyToPersonalBuddyGroupImpl(long j, List<String> list, String str);

    private native boolean addSameOrgBuddyByJIDImpl(long j, String str);

    private native boolean assignGroupAdminsImpl(long j, String str, List<String> list);

    private native boolean assignGroupAdminsV2Impl(long j, String str, List<String> list);

    private native boolean blackListIsBlockedImpl(long j, String str);

    private native boolean blackListSetImpl(long j, String str, boolean z);

    private native int blockAllGetImpl(long j);

    private native boolean blockAllSetImpl(long j, int i);

    private native boolean blockUserBlockUsersImpl(long j, List<String> list);

    private native boolean blockUserEditBlockedUsersListImpl(long j, List<String> list);

    @Nullable
    private native List<String> blockUserGetAllImpl(long j);

    private native boolean blockUserIsBlockedImpl(long j, String str);

    private native boolean blockUserUnBlockUsersImpl(long j, List<String> list);

    private native boolean canRemoveBuddyImpl(long j, String str);

    private native boolean canSubscribeBuddyImpl(long j, String str);

    private native boolean canSubscribePresenceAlertImpl(long j, String str);

    private native boolean checkGiphyFileIsExistImpl(long j, String str);

    private native boolean checkGroupNameIsExistImpl(long j, String str);

    @Nullable
    private native List<String> checkIfNeedUpdateHotGiphyInfoImpl(long j);

    private native boolean clearAllStarMessageImpl(long j);

    @Nullable
    private native List<String> createPersonalBuddyGroupImpl(long j, String str, List<String> list);

    private native boolean deleteGroupImpl(long j, String str);

    @Nullable
    private native String deletePersonalBuddyGroupImpl(long j, String str);

    private native boolean deleteSessionImpl(long j, String str, boolean z, boolean z2);

    private native boolean deleteSubscribeRequestImpl(long j, int i);

    private native boolean destroyGroupImpl(long j, String str);

    @Nullable
    private native String downloadFileByUrlImpl(long j, String str, String str2, boolean z, boolean z2);

    @Nullable
    private native String downloadFileForEditCodeSnippetImpl(long j, String str, String str2, long j2);

    @Nullable
    private native String downloadGIFFromGiphyByUrlImpl(long j, String str, String str2, String str3, String str4);

    private native int e2eGetAutologoffMinutesImpl(long j);

    private native int e2eGetMyOptionImpl(long j);

    private native int e2eGetMyStateImpl(long j);

    private native boolean e2eIsFTEWithBuddyImpl(long j, String str);

    private native int e2eQuerySessionStateImpl(long j, String str);

    private native int e2eTryDecodeMessageImpl(long j, String str, String str2);

    private native int editGroupChatImpl(long j, String str, String str2, List<String> list, int i);

    private native int editIMSettingGetOptionImpl(long j);

    @Nullable
    private native String emojiVersionGetJsonStrImpl(long j);

    @Nullable
    private native String fetchUserProfileByJidImpl(long j, String str);

    private native long findSessionByIdImpl(long j, String str);

    private native boolean forceSignonImpl(long j);

    @Nullable
    private native String getAddBuddyEmailImpl(long j);

    @Nullable
    private native String getAddBuddySubjectImpl(long j);

    @Nullable
    private native String getAddBuddyUrlImpl(long j);

    private native int getAddContactOptionImpl(long j);

    private native long getAddressbookContactBuddyGroupImpl(long j);

    @Nullable
    private native byte[] getAllBuddiesImpl(long j, boolean z, boolean z2, String[] strArr, String str);

    @Nullable
    private native List<String> getAllRobotBuddiesImpl(long j, String str);

    @Nullable
    private native byte[] getAllRoomsImpl(long j);

    @Nullable
    private native List<String> getAllStarredMessagesImpl(long j, String str);

    private native List<String> getBroadcastsImpl(long j);

    private native int getBuddiesPresenceForMUCImpl(long j, String str, boolean z);

    private native int getBuddiesPresenceImpl(long j, List<String> list, boolean z);

    private native long getBuddyAtImpl(long j, int i);

    private native int getBuddyCountImpl(long j);

    private native long getBuddyGroupAtImpl(long j, int i);

    private native long getBuddyGroupByJidImpl(long j, String str);

    private native long getBuddyGroupByTypeImpl(long j, int i);

    private native long getBuddyGroupByXmppIDImpl(long j, String str);

    private native int getBuddyGroupCountImpl(long j);

    private native long getBuddySearchDataImpl(long j);

    private native long getBuddyWithJIDImpl(long j, String str);

    @Nullable
    private native byte[] getBuddyWithNumberImpl(long j, String str);

    private native long getBuddyWithPbxNumberImpl(long j, String str);

    private native long getBuddyWithPhoneNumberImpl(long j, String str);

    private native long getBuddyWithSipPhoneImpl(long j, String str);

    private native int getChatSessionCountImpl(long j);

    private native int getCoWorkersCountImpl(long j);

    private native int getCodeSnippetOptionImpl(long j);

    @Nullable
    private native String getContactRequestsSessionIDImpl();

    private native int getFileTransferInReceiverOptionImpl(long j);

    private native long getFileWithMessageIDImpl(long j, String str, String str2);

    @Nullable
    private native String getGiphyInfoByStrImpl(long j, String str, String str2, int i, String str3);

    @Nullable
    private native byte[] getGiphyInfoImpl(long j, String str);

    private native int getGiphyOptionImpl(long j);

    private native long getGroupAtImpl(long j, int i);

    private native long getGroupByIdImpl(long j, String str);

    private native int getGroupCountImpl(long j);

    private native int getGroupInviteLimitImpl(long j);

    private native int getGroupLimitCountImpl(long j, boolean z);

    @Nullable
    private native String getHotGiphyInfoImpl(long j, String str, int i, String str2);

    @Nullable
    private native byte[] getLastUsedRobotCommandImpl(long j);

    private native long getLatestRequestTimeStampImpl(long j);

    @Nullable
    private native byte[] getListForFileIntegrationShareImpl(long j);

    @Nullable
    private native byte[] getLocalStorageTimeIntervalImpl(long j);

    private native int getMyPresenceImpl(long j);

    private native int getMyPresenceStatusImpl(long j);

    private native long getMyselfImpl(long j);

    private native long getNewFriendDataImpl(long j);

    private native int getPendingRequestCountImpl(long j, int i);

    private native long getPublicRoomSearchDataImpl(long j);

    @Nullable
    private native List<String> getRoomDevicesImpl(long j);

    private native int getScreenCaptureOptionImpl(long j);

    @Nullable
    private native List<String> getSendFailedMessagesImpl(long j, String str);

    private native long getSessionAtImpl(long j, int i);

    private native long getSessionByIdImpl(long j, String str);

    @Nullable
    private native String getSessionDataFolderImpl(long j, String str);

    private native int getStreamConflictReasonImpl(long j);

    private native long getSubscribeRequestAtImpl(long j, int i);

    private native int getSubscribeRequestCountImpl(long j);

    private native long getThreadDataProviderImpl(long j);

    private native int getTotalMarkedUnreadMsgCountImpl(long j);

    private native int getTotalUnreadMessageCountImpl(long j);

    private native int getUnreadRequestCountImpl(long j);

    private native boolean hasFailedMessageImpl(long j, String str);

    private native boolean hasUpOrDownloadingFileRequestImpl(long j);

    private native int imChatGetOptionImpl(long j);

    private native boolean insertSystemMessageImpl(long j, String str, String str2, String str3, long j2, int i, boolean z, String[] strArr, long j3, long j4, boolean z2);

    private native int inviteToMeetingImpl(long j, String str, String str2, long j2);

    private native boolean isAnyBuddyGroupLargeImpl(long j);

    private native boolean isAutoAcceptBuddyImpl(long j, String str);

    private native boolean isBuddyWithJIDInGroupImpl(long j, String str, String str2);

    private native boolean isChatAvailableImpl(long j, String str);

    private native boolean isCompanyContactImpl(long j, String str);

    private native boolean isConnectionGoodImpl(long j);

    private native boolean isDisableReactionImpl(long j);

    private native boolean isDisableReplyImpl(long j);

    private native boolean isFileTransferResumeEnabledImpl(long j, String str);

    private native boolean isForceSignoutImpl(long j);

    private native boolean isIMChatOptionChangedImpl(long j);

    private native boolean isMyContactImpl(long j, String str);

    private native boolean isMyContactOrPendingImpl(long j, String str);

    private native boolean isMyFriendImpl(long j, String str);

    private native boolean isStarMessageImpl(long j, String str, long j2);

    private native boolean isStarSessionImpl(long j, String str);

    private native boolean isStreamConflictImpl(long j);

    private native boolean isZoomRoomContactImpl(long j, String str);

    @Nullable
    private native List<String> localSearchGroupSessionsByNameImpl(long j, String str, String str2);

    @Nullable
    private native List<String> localStrictSearchBuddiesAdvanceImpl(long j, String str, String str2, int i);

    @Nullable
    private native List<String> localStrictSearchBuddiesImpl(long j, String str, String str2);

    @Nullable
    private native byte[] makeGroupImpl(long j, String[] strArr, String str, long j2, long j3);

    private native boolean modifyGroupNameImpl(long j, String str, String str2);

    private native boolean modifyGroupPropertyImpl(long j, String str, String str2, String str3, boolean z, boolean z2, boolean z3, boolean z4);

    @Nullable
    private native String modifyPersonalBuddyGroupNameImpl(long j, String str, String str2);

    @Nullable
    private native String moveBuddyFromPersonalBuddyGroupImpl(long j, List<String> list, String str, String str2);

    private native int msgCopyGetOptionImpl(long j);

    private native int myNotesGetOptionImpl(long j);

    private native void notifyMissedCallImpl(long j, long j2);

    private native void notifyOpenRobotChatSessionImpl(long j, String str);

    private native int personalGroupGetOptionImpl(long j);

    @Nullable
    private native List<String> queryAvailableAlertBuddyAllImpl(long j);

    private native String queryJidByEmailImpl(long j, String str);

    private native boolean refreshBuddyBigPictureImpl(long j, String str);

    private native boolean refreshBuddyVCardImpl(long j, String str, boolean z);

    private native boolean refreshBuddyVCardsImpl(long j, List<String> list, boolean z);

    private native boolean refreshChatAvailableInfoImpl(long j, String str);

    private native boolean refreshGroupInfoImpl(long j, String str);

    private native boolean removeAvailableAlertBuddyImpl(long j, String str);

    private native boolean removeBuddyFromGroupImpl(long j, String str, String str2);

    private native boolean removeBuddyImpl(long j, String str, String str2);

    @Nullable
    private native String removeBuddyToPersonalBuddyGroupImpl(long j, List<String> list, String str);

    private native boolean savedSessionClearAllImpl(long j);

    @Nullable
    private native List<String> savedSessionGetAllImpl(long j);

    private native boolean savedSessionIsSavedImpl(long j, String str);

    private native boolean savedSessionSetImpl(long j, String str, boolean z);

    private native boolean searchBuddyByKeyImpl(long j, String str);

    private native String searchBuddyByKeyV2Impl(long j, String str, String str2, boolean z);

    private native boolean searchBuddyImpl(long j, String str);

    private native boolean searchSessionLastMessageCtxImpl(long j, List<String> list);

    @Nullable
    private native String sendAudioImpl(long j, String str, String str2, String str3, int i);

    private native int sendFileImpl(long j, String str, String str2, String str3, String[] strArr);

    @Nullable
    private native String sendGetHttpMessageImpl(long j, String str);

    private native int sendMessageForGiphyImpl(long j, String[] strArr, byte[] bArr);

    private native int sendMessageImpl(long j, byte[] bArr, String[] strArr, byte[] bArr2);

    @Nullable
    private native String sendPictureImpl(long j, String str, String str2, String str3);

    @Nullable
    private native String sendPostHttpMessageImpl(long j, String str, String[] strArr, String[] strArr2);

    @Nullable
    private native String sendTextImpl(long j, String str, String str2, String str3, List<String> list, byte[] bArr);

    @Nullable
    private native String sendVideoImpl(long j, String str, String str2, String str3, int i);

    private native boolean setAllRequestAsReadedImpl(long j);

    private native boolean setLastUsedRobotCommandImpl(long j, byte[] bArr);

    private native void setMsgUIImpl(long j, long j2);

    private native boolean setPresenceImpl(long j, int i);

    @Nullable
    private native String setUserSignatureImpl(long j, String str);

    @Nullable
    private native List<String> sortBuddies2Impl(long j, List<String> list, int i, String str);

    private native List<String> sortSessionsByKeyAndMsgTimeImpl(long j, String str, List<String> list);

    @Nullable
    private native List<String> sortSessionsImpl(long j, List<String> list);

    @Nullable
    private native Map<String, List<Long>> starMessageGetAllImpl(long j);

    @Nullable
    private native String starMessageSyncMessagesImpl(long j, Map<String, List<Long>> map);

    private native boolean starSessionClearAllImpl(long j);

    @Nullable
    private native List<String> starSessionGetAllImpl(long j);

    private native boolean starSessionSetStarImpl(long j, String str, boolean z);

    private native int startMeetingImpl(long j, String str, String str2, long j2, int i);

    private native int subBuddyTempPresenceImpl(long j, List<String> list);

    private native int syncAllSubScribeReqAsReadedImpl(long j);

    private native boolean trySignonImpl(long j);

    private native boolean updateAutoAnswerGroupBuddyImpl(long j, String str, boolean z);

    public ZoomMessenger(long j) {
        this.mNativeHandle = j;
    }

    public void setMsgUI(@Nullable ZoomMessengerUI zoomMessengerUI) {
        long j = this.mNativeHandle;
        if (j != 0 && zoomMessengerUI != null) {
            setMsgUIImpl(j, zoomMessengerUI.getNativeHandle());
        }
    }

    public boolean isConnectionGood() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isConnectionGoodImpl(j);
    }

    @Nullable
    public String getSessionDataFolder(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSessionDataFolderImpl(j, str);
    }

    @Nullable
    public ZoomBuddy getMyself() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long myselfImpl = getMyselfImpl(j);
        if (myselfImpl == 0) {
            return null;
        }
        return new ZoomBuddy(myselfImpl);
    }

    public int getBuddyCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getBuddyCountImpl(j);
    }

    @Nullable
    public ZoomBuddy getBuddyAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0 || i < 0) {
            return null;
        }
        long buddyAtImpl = getBuddyAtImpl(j, i);
        if (buddyAtImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyAtImpl);
    }

    @Nullable
    public ZoomBuddy getBuddyWithJID(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long buddyWithJIDImpl = getBuddyWithJIDImpl(this.mNativeHandle, str);
        if (buddyWithJIDImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyWithJIDImpl);
    }

    @Nullable
    public ZoomBuddy getBuddyWithPhoneNumber(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long buddyWithPhoneNumberImpl = getBuddyWithPhoneNumberImpl(this.mNativeHandle, str);
        if (buddyWithPhoneNumberImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyWithPhoneNumberImpl);
    }

    public int getGroupCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getGroupCountImpl(j);
    }

    @Nullable
    public ZoomGroup getGroupAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0 || i < 0) {
            return null;
        }
        long groupAtImpl = getGroupAtImpl(j, i);
        if (groupAtImpl == 0) {
            return null;
        }
        return new ZoomGroup(groupAtImpl);
    }

    @Nullable
    public ZoomGroup getGroupById(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long groupByIdImpl = getGroupByIdImpl(this.mNativeHandle, str);
        if (groupByIdImpl == 0) {
            return null;
        }
        return new ZoomGroup(groupByIdImpl);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x005d A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005e A[SYNTHETIC, Splitter:B:22:0x005e] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.zipow.videobox.ptapp.PTAppProtos.MakeGroupResult makeGroup(@androidx.annotation.Nullable java.util.List<java.lang.String> r11, java.lang.String r12, long r13) {
        /*
            r10 = this;
            long r0 = r10.mNativeHandle
            r9 = 0
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x000a
            return r9
        L_0x000a:
            r0 = 14
            int r0 = (r13 > r0 ? 1 : (r13 == r0 ? 0 : -1))
            if (r0 != 0) goto L_0x001a
            if (r11 != 0) goto L_0x0035
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = r0
            r0 = r12
            goto L_0x0037
        L_0x001a:
            if (r11 == 0) goto L_0x0063
            int r0 = r11.size()
            if (r0 != 0) goto L_0x0023
            goto L_0x0063
        L_0x0023:
            r0 = 80
            int r0 = (r13 > r0 ? 1 : (r13 == r0 ? 0 : -1))
            if (r0 != 0) goto L_0x0035
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r12)
            if (r0 == 0) goto L_0x0035
            java.lang.String r0 = com.zipow.videobox.ptapp.p013mm.ZoomGroup.createDefaultMUCName(r11)
            r1 = r11
            goto L_0x0037
        L_0x0035:
            r1 = r11
            r0 = r12
        L_0x0037:
            if (r0 != 0) goto L_0x003d
            java.lang.String r0 = ""
            r4 = r0
            goto L_0x003e
        L_0x003d:
            r4 = r0
        L_0x003e:
            int r0 = r1.size()
            java.lang.String[] r0 = new java.lang.String[r0]
            java.lang.Object[] r0 = r1.toArray(r0)
            r3 = r0
            java.lang.String[] r3 = (java.lang.String[]) r3
            long r1 = r10.mNativeHandle
            com.zipow.videobox.ptapp.ZoomMessengerUI r0 = com.zipow.videobox.ptapp.ZoomMessengerUI.getInstance()
            long r7 = r0.getNativeHandle()
            r0 = r10
            r5 = r13
            byte[] r0 = r0.makeGroupImpl(r1, r3, r4, r5, r7)
            if (r0 != 0) goto L_0x005e
            return r9
        L_0x005e:
            com.zipow.videobox.ptapp.PTAppProtos$MakeGroupResult r9 = com.zipow.videobox.ptapp.PTAppProtos.MakeGroupResult.parseFrom(r0)     // Catch:{ InvalidProtocolBufferException -> 0x0062 }
        L_0x0062:
            return r9
        L_0x0063:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.p013mm.ZoomMessenger.makeGroup(java.util.List, java.lang.String, long):com.zipow.videobox.ptapp.PTAppProtos$MakeGroupResult");
    }

    public boolean modifyGroupName(String str, String str2) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            return modifyGroupNameImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean deleteGroup(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return deleteGroupImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean isFileTransferResumeEnabled(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return isFileTransferResumeEnabledImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean assignGroupAdminsV2(String str, @Nullable List<String> list) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str) && list != null && list.size() > 0) {
            return assignGroupAdminsV2Impl(this.mNativeHandle, str, list);
        }
        return false;
    }

    public boolean addBuddyToGroup(String str, @Nullable List<String> list) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || list == null) {
            return false;
        }
        return addBuddyToGroupImpl(this.mNativeHandle, str, list);
    }

    public boolean removeBuddyFromGroup(String str, String str2) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            return removeBuddyFromGroupImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public int editGroupChat(String str, String str2, List<String> list, int i) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return 1;
        }
        return editGroupChatImpl(this.mNativeHandle, str, str2, list, i);
    }

    public boolean modifyGroupProperty(String str, String str2, String str3, boolean z, boolean z2, boolean z3, boolean z4) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return false;
        }
        return modifyGroupPropertyImpl(this.mNativeHandle, str, str2, str3, z, z2, z3, z4);
    }

    @Nullable
    public ZoomChatSession getSessionAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0 || i < 0) {
            return null;
        }
        long sessionAtImpl = getSessionAtImpl(j, i);
        if (sessionAtImpl == 0) {
            return null;
        }
        return new ZoomChatSession(sessionAtImpl);
    }

    @Nullable
    public ZoomChatSession getSessionById(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long sessionByIdImpl = getSessionByIdImpl(this.mNativeHandle, str);
        if (sessionByIdImpl == 0) {
            return null;
        }
        return new ZoomChatSession(sessionByIdImpl);
    }

    public int getChatSessionCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getChatSessionCountImpl(j);
    }

    public int getTotalUnreadMessageCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTotalUnreadMessageCountImpl(j);
    }

    public int getTotalMarkedUnreadMsgCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTotalMarkedUnreadMsgCountImpl(j);
    }

    public int sendE2EFTEInvite(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            return 3;
        }
        return sendMessage(-1, null, str, str3, null, null, null, 0, false, null, str2, true, false);
    }

    private int sendMessage(int i, String str, String str2, CharSequence charSequence, List<String> list, EmojiList emojiList, String str3, int i2, boolean z, String[] strArr, String str4, boolean z2, boolean z3) {
        return sendMessage(i, str, str2, charSequence, list, emojiList, str3, i2, z, strArr, str4, z2, z3, null);
    }

    private int sendMessage(int i, @Nullable String str, @Nullable String str2, CharSequence charSequence, @Nullable List<String> list, @Nullable EmojiList emojiList, String str3, int i2, boolean z, @Nullable String[] strArr, String str4, boolean z2, boolean z3, @Nullable FileIntegrationInfo fileIntegrationInfo) {
        int i3;
        boolean z4;
        String[] strArr2;
        int i4 = i;
        List<String> list2 = list;
        EmojiList emojiList2 = emojiList;
        String str5 = str3;
        boolean z5 = z;
        String[] strArr3 = strArr;
        FileIntegrationInfo fileIntegrationInfo2 = fileIntegrationInfo;
        if (this.mNativeHandle == 0) {
            return 3;
        }
        if (StringUtil.isEmptyOrNull(str) && StringUtil.isEmptyOrNull(str2)) {
            return 3;
        }
        String str6 = str == null ? "" : str;
        String str7 = str2 == null ? "" : str2;
        if (TextUtils.isEmpty(str6)) {
            str6 = str7;
        }
        Builder newBuilder = MessageInput.newBuilder();
        newBuilder.setIsMyNote(false);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null && TextUtils.equals(str6, myself.getJid())) {
                newBuilder.setIsMyNote(true);
            }
        }
        if (!TextUtils.isEmpty(charSequence)) {
            newBuilder.setBody(String.valueOf(charSequence));
        }
        if (emojiList2 != null) {
            newBuilder.setEmojiList(emojiList2);
            i3 = i2;
        } else {
            i3 = i2;
        }
        newBuilder.setLenInSeconds(i3);
        if (!TextUtils.isEmpty(str3)) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                String dataPath = FileUtils.getDataPath(VideoBoxApplication.getGlobalContext(), true, true);
                if (dataPath == null || !str5.startsWith(dataPath)) {
                    File file = new File(str5);
                    File createInternalShareCopyFile = FileUtils.createInternalShareCopyFile(globalContext, file.getName());
                    if (createInternalShareCopyFile != null) {
                        FileUtils.copyFile(file.getAbsolutePath(), createInternalShareCopyFile.getAbsolutePath());
                        newBuilder.setLocalFilePath(createInternalShareCopyFile.getAbsolutePath());
                    }
                } else {
                    newBuilder.setLocalFilePath(str5);
                }
            }
        }
        newBuilder.setMsgType(i);
        newBuilder.setMsgSubType(1);
        newBuilder.setSessionID(str6);
        newBuilder.setIsE2EMessage(z5);
        newBuilder.setIsAtAllGroupMembers(z3);
        if (!TextUtils.isEmpty(str4)) {
            newBuilder.setE2EMessageFakeBody(str4);
            z4 = z2;
        } else {
            z4 = z2;
        }
        newBuilder.setIsE2EInvitation(z4);
        if (list2 != null) {
            newBuilder.addAllVecMessageAtList(list2);
        }
        if (i4 == 15 && fileIntegrationInfo2 != null) {
            newBuilder.setFileIntegration(fileIntegrationInfo2);
        }
        FontStyte buildFromCharSequence = FontStyleHelper.buildFromCharSequence(charSequence);
        if (buildFromCharSequence != null) {
            newBuilder.setFontStyte(buildFromCharSequence);
        }
        byte[] previewImgData = (StringUtil.isEmptyOrNull(str3) || !z5 || !(i4 == 1 || i4 == 5 || i4 == 1)) ? null : ImageUtil.getPreviewImgData(str5, 15000);
        if (strArr3 == null || strArr3.length < 1) {
            strArr2 = new String[1];
        } else {
            strArr2 = strArr3;
        }
        return sendMessageImpl(this.mNativeHandle, newBuilder.build().toByteArray(), strArr2, previewImgData);
    }

    @Nullable
    public String sendText(String str, String str2, @Nullable String str3) {
        return sendText(str, str2, str3, false, null, null, false);
    }

    @Nullable
    public String sendText(String str, String str2, @Nullable CharSequence charSequence, boolean z, List<String> list, String str3, boolean z2) {
        return sendText(str, str2, charSequence, z, list, EmojiHelper.getInstance().getEmojiList(charSequence), str3, z2);
    }

    @Nullable
    public String sendMessage(@NonNull MessageInput messageInput) {
        if (messageInput.getCommentInfo() == null) {
            return "";
        }
        String[] strArr = new String[1];
        return (sendMessageImpl(this.mNativeHandle, messageInput.toByteArray(), strArr, null) != 0 || TextUtils.isEmpty(strArr[0])) ? "" : strArr[0];
    }

    private String sendText(String str, String str2, CharSequence charSequence, boolean z, List<String> list, EmojiList emojiList, String str3, boolean z2) {
        if (TextUtils.isEmpty(charSequence)) {
            return null;
        }
        String[] strArr = new String[1];
        return (sendMessage(0, str, str2, charSequence, list, emojiList, null, 0, z, strArr, str3, false, z2) != 0 || TextUtils.isEmpty(strArr[0])) ? "" : strArr[0];
    }

    @Nullable
    public String sendAudio(String str, String str2, @Nullable String str3, int i, boolean z, String str4) {
        if (StringUtil.isEmptyOrNull(str3)) {
            return null;
        }
        File file = new File(str3);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        String[] strArr = new String[1];
        return (sendMessage(2, str, str2, null, null, null, str3, i, z, strArr, str4, false, false) != 0 || TextUtils.isEmpty(strArr[0])) ? "" : strArr[0];
    }

    @Nullable
    public String sendVideo(String str, String str2, @Nullable String str3, int i) {
        if (StringUtil.isEmptyOrNull(str3)) {
            return null;
        }
        File file = new File(str3);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        String[] strArr = new String[1];
        return (sendMessage(3, str, str2, null, null, null, str3, i, false, strArr, null, false, false) != 0 || TextUtils.isEmpty(strArr[0])) ? "" : strArr[0];
    }

    @Nullable
    public String sendGif(String str, String str2, @Nullable String str3, boolean z, String str4) {
        return sendPicture(str, str2, str3, 6, z, str4);
    }

    @Nullable
    public String sendPng(String str, String str2, @Nullable String str3, boolean z, String str4) {
        return sendPicture(str, str2, str3, 5, z, str4);
    }

    @Nullable
    public String sendPicture(String str, String str2, @Nullable String str3, boolean z, String str4) {
        return sendPicture(str, str2, str3, 1, z, str4);
    }

    private String sendPicture(String str, String str2, @Nullable String str3, int i, boolean z, String str4) {
        if (StringUtil.isEmptyOrNull(str3) || !FileUtils.isFile(str3)) {
            return null;
        }
        String[] strArr = new String[1];
        return (sendMessage(i, str, str2, null, null, null, str3, 0, z, strArr, str4, false, false) != 0 || TextUtils.isEmpty(strArr[0])) ? "" : strArr[0];
    }

    public int sendFile(String str, String str2, @Nullable String str3, @Nullable String[] strArr, String str4, boolean z) {
        String[] strArr2;
        String[] strArr3 = strArr;
        if (StringUtil.isEmptyOrNull(str3)) {
            return 3;
        }
        if (!FileUtils.isFile(str3)) {
            return 20;
        }
        if (strArr3 == null || strArr3.length < 1) {
            strArr2 = new String[1];
        } else {
            strArr2 = strArr3;
        }
        return sendMessage(10, str, str2, null, null, null, str3, 0, z, strArr2, str4, false, false);
    }

    public String sendSharedLink(String str, String str2, @Nullable FileIntegrationInfo fileIntegrationInfo, @Nullable String str3, String str4, String str5, boolean z) {
        if (fileIntegrationInfo == null) {
            return "";
        }
        String[] strArr = new String[1];
        return (sendMessage(15, str, str2, str4, null, null, str3, 0, z, strArr, str5, false, false, fileIntegrationInfo) != 0 || TextUtils.isEmpty(strArr[0])) ? "" : strArr[0];
    }

    @Nullable
    public String insertChatUnavailableSystemMessage(String str, String str2, String str3, long j, boolean z, int i) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        if ((StringUtil.isEmptyOrNull(str) && StringUtil.isEmptyOrNull(str2)) || StringUtil.isEmptyOrNull(str3)) {
            return null;
        }
        return insertSystemMessage(str, str2, str3, j, z, i, null, CmmTime.getMMNow(), 0, true);
    }

    @Nullable
    public String insertSystemMessage(String str, String str2, String str3, long j, boolean z, int i, String str4) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        if ((StringUtil.isEmptyOrNull(str) && StringUtil.isEmptyOrNull(str2)) || StringUtil.isEmptyOrNull(str3)) {
            return null;
        }
        return insertSystemMessage(str, str2, str3, j, z, i, str4, CmmTime.getMMNow(), 0, false);
    }

    @Nullable
    public String insertSystemMessage(String str, String str2, String str3, long j, int i, String str4, long j2, long j3) {
        return insertSystemMessage(str, str2, str3, j, true, i, str4, j2, j3, false);
    }

    @Nullable
    public String insertSystemMessage(String str, String str2, String str3, long j, boolean z, int i, @Nullable String str4, long j2, long j3, boolean z2) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        if ((StringUtil.isEmptyOrNull(str) && StringUtil.isEmptyOrNull(str2)) || StringUtil.isEmptyOrNull(str3)) {
            return null;
        }
        String[] strArr = new String[1];
        if (str4 != null) {
            strArr[0] = str4;
        }
        String[] strArr2 = strArr;
        if (insertSystemMessageImpl(this.mNativeHandle, str, str2, str3, j, i, z, strArr, j2, j3, z2)) {
            return strArr2[0];
        }
        return null;
    }

    public int startMeeting(@Nullable String str, @Nullable String str2, long j, int i) {
        if (this.mNativeHandle == 0) {
            return 1;
        }
        if (StringUtil.isEmptyOrNull(str) && StringUtil.isEmptyOrNull(str2)) {
            return 6;
        }
        return startMeetingImpl(this.mNativeHandle, str == null ? "" : str, str2 == null ? "" : str2, j, i);
    }

    public int inviteToMeeting(String str, String str2, long j) {
        if (this.mNativeHandle == 0) {
            return 1;
        }
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2) || j <= 0) {
            return 6;
        }
        return inviteToMeetingImpl(this.mNativeHandle, str, str2, j);
    }

    public boolean deleteSession(String str, boolean z, boolean z2) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return deleteSessionImpl(this.mNativeHandle, str, z, z2);
    }

    public boolean deleteSession(String str) {
        return deleteSession(str, true, false);
    }

    public boolean deleteSession(String str, boolean z) {
        return deleteSession(str, z, false);
    }

    public boolean blackList_IsBlocked(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return blackListIsBlockedImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean blackList_Set(String str, boolean z) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return blackListSetImpl(this.mNativeHandle, str, z);
        }
        return false;
    }

    public boolean blockAll_Set(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return blockAllSetImpl(j, i);
    }

    public int blockAll_Get() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return blockAllGetImpl(j);
    }

    public boolean setPresence(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setPresenceImpl(j, i);
    }

    public boolean addBuddyByJID(String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        if (str2 == null) {
            str2 = "";
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("screenname", str2);
        return addBuddyByJIDImpl(this.mNativeHandle, str, jsonObject.toString(), str3 == null ? "" : str3, str4 == null ? "" : str4, str5 == null ? "" : str5);
    }

    public boolean addBuddyByEmail(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return addBuddyByEmailImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean searchBuddy(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return searchBuddyImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public String queryJidByEmail(String str) {
        if (this.mNativeHandle == 0) {
            return "";
        }
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        return queryJidByEmailImpl(this.mNativeHandle, str);
    }

    public boolean searchBuddyByKey(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return searchBuddyByKeyImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public String searchBuddyByKeyV2(String str, String str2) {
        return searchBuddyByKeyV2(str, str2, false);
    }

    public String searchBuddyByKeyV2(String str, String str2, boolean z) {
        if (this.mNativeHandle == 0) {
            return "";
        }
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        return searchBuddyByKeyV2Impl(this.mNativeHandle, str, str2, z);
    }

    public boolean removeBuddy(String str, @Nullable String str2) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        if (str2 == null) {
            str2 = "";
        }
        return removeBuddyImpl(this.mNativeHandle, str, str2);
    }

    public boolean canRemoveBuddy(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return canRemoveBuddyImpl(this.mNativeHandle, str);
        }
        return false;
    }

    @Nullable
    public ZoomBuddySearchData getBuddySearchData() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddySearchDataImpl = getBuddySearchDataImpl(j);
        if (buddySearchDataImpl == 0) {
            return null;
        }
        return new ZoomBuddySearchData(buddySearchDataImpl);
    }

    @Nullable
    public String getAddBuddyUrl() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getAddBuddyUrlImpl(j);
    }

    @Nullable
    public String getAddBuddyEmail() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getAddBuddyEmailImpl(j);
    }

    @Nullable
    public String getAddBuddySubject() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getAddBuddySubjectImpl(j);
    }

    public boolean refreshBuddyVCard(String str) {
        return refreshBuddyVCard(str, false);
    }

    public boolean refreshBuddyVCards(List<String> list) {
        return refreshBuddyVCards(list, false);
    }

    public boolean refreshBuddyVCard(String str, boolean z) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return refreshBuddyVCardImpl(this.mNativeHandle, str, z);
        }
        return false;
    }

    public boolean refreshBuddyVCards(List<String> list, boolean z) {
        if (this.mNativeHandle != 0 && !CollectionsUtil.isCollectionEmpty(list)) {
            return refreshBuddyVCardsImpl(this.mNativeHandle, list, z);
        }
        return false;
    }

    public boolean forceSignon() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return forceSignonImpl(j);
    }

    public boolean trySignon() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return trySignonImpl(j);
    }

    public boolean isStreamConflict() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isStreamConflictImpl(j);
    }

    public int getStreamConflictReason() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getStreamConflictReasonImpl(j);
    }

    public int getCoWorkersCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCoWorkersCountImpl(j);
    }

    public boolean canSubscribeBuddy(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return canSubscribeBuddyImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean refreshGroupInfo(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return refreshGroupInfoImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public int getGroupLimitCount(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getGroupLimitCountImpl(j, z);
    }

    public boolean refreshBuddyBigPicture(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return refreshBuddyBigPictureImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean isMyFriend(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return isMyFriendImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean canSubscribePresenceAlert(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return canSubscribePresenceAlertImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean addAvailableAlertBuddy(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return addAvailableAlertBuddyImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean removeAvailableAlertBuddy(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return removeAvailableAlertBuddyImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean ackBuddySubscribe(String str, boolean z) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return ackBuddySubscribeImpl(this.mNativeHandle, str, z);
        }
        return false;
    }

    public boolean isMyContact(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return isMyContactImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean isMyContactOrPending(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return isMyContactOrPendingImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean FT_Download(String str, String str2, @Nullable String str3) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return false;
        }
        return FTDownloadImpl(this.mNativeHandle, str, str2, str3 == null ? "" : str3);
    }

    public boolean FT_Resume(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return false;
        }
        return FTResumeImpl(this.mNativeHandle, str, str2, str3);
    }

    public boolean FT_Cancel(String str, String str2, int i) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return false;
        }
        return FTCancelImpl(this.mNativeHandle, str, str2, i);
    }

    public boolean FT_Pause(String str, String str2) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            return FTPauseImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public int getPendingRequestCount(int i) {
        long j = this.mNativeHandle;
        if (j != 0 && i == 0 && i == 1) {
            return getPendingRequestCountImpl(j, i);
        }
        return 0;
    }

    public int getSubscribeRequestCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSubscribeRequestCountImpl(j);
    }

    @Nullable
    public ZoomSubscribeRequest getSubscribeRequestAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0 || i < 0) {
            return null;
        }
        long subscribeRequestAtImpl = getSubscribeRequestAtImpl(j, i);
        if (subscribeRequestAtImpl == 0) {
            return null;
        }
        return new ZoomSubscribeRequest(subscribeRequestAtImpl);
    }

    public boolean deleteSubscribeRequest(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return deleteSubscribeRequestImpl(j, i);
    }

    public boolean setAllRequestAsReaded() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setAllRequestAsReadedImpl(j);
    }

    public int getUnreadRequestCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUnreadRequestCountImpl(j);
    }

    public long getLatestRequestTimeStamp() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getLatestRequestTimeStampImpl(j);
    }

    public boolean isAutoAcceptBuddy(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isAutoAcceptBuddyImpl(j, str);
    }

    public boolean updateAutoAnswerGroupBuddy(String str, boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return updateAutoAnswerGroupBuddyImpl(j, str, z);
    }

    public boolean isBuddyWithJIDInGroup(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isBuddyWithJIDInGroupImpl(j, str, str2);
    }

    public int e2eGetMyOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return e2eGetMyOptionImpl(j);
    }

    public int e2eGetMyState() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return e2eGetMyStateImpl(j);
    }

    public boolean e2eIsFTEWithBuddy(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return e2eIsFTEWithBuddyImpl(j, str);
    }

    public int e2eQuerySessionState(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 4;
        }
        return e2eQuerySessionStateImpl(j, str);
    }

    public int e2eGetAutologoffMinutes() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return e2eGetAutologoffMinutesImpl(j);
    }

    public int e2eTryDecodeMessage(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return e2eTryDecodeMessageImpl(j, str, str2);
    }

    public int subBuddyTempPresence(List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return subBuddyTempPresenceImpl(j, list);
    }

    public int getBuddiesPresence(List<String> list, boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return getBuddiesPresenceImpl(j, list, z);
    }

    public int getBuddiesPresenceForMUC(String str, boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return getBuddiesPresenceForMUCImpl(j, str, z);
    }

    @Nullable
    public List<String> sortBuddies2(@Nullable List<String> list, int i, @Nullable String str) {
        if (this.mNativeHandle == 0 || list == null || list.size() == 0) {
            return null;
        }
        return sortBuddies2Impl(this.mNativeHandle, list, i, str == null ? "" : str);
    }

    @Nullable
    public List<String> localSearchGroupSessionsByName(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return localSearchGroupSessionsByNameImpl(j, str, str2);
    }

    @Nullable
    public List<String> localStrictSearchBuddies(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return localStrictSearchBuddiesImpl(j, str, str2);
    }

    @Nullable
    public List<String> localStrictSearchBuddiesAdvance(String str, String str2, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return localStrictSearchBuddiesAdvanceImpl(j, str, str2, i);
    }

    @Nullable
    public List<String> queryAvailableAlertBuddyAll() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return queryAvailableAlertBuddyAllImpl(j);
    }

    public boolean isZoomRoomContact(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isZoomRoomContactImpl(j, str);
    }

    public boolean addSameOrgBuddyByJID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return addSameOrgBuddyByJIDImpl(j, str);
    }

    public int syncAllSubScribeReqAsReaded() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return syncAllSubScribeReqAsReadedImpl(j);
    }

    @Nullable
    public List<String> savedSessionGetAll() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return savedSessionGetAllImpl(j);
    }

    public boolean savedSessionClearAll() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return savedSessionClearAllImpl(j);
    }

    public boolean savedSessionIsSaved(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return savedSessionIsSavedImpl(j, str);
    }

    public boolean savedSessionSet(String str, boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return savedSessionSetImpl(j, str, z);
    }

    @Nullable
    public ZoomPublicRoomSearchData getPublicRoomSearchData() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long publicRoomSearchDataImpl = getPublicRoomSearchDataImpl(j);
        if (publicRoomSearchDataImpl == 0) {
            return null;
        }
        return new ZoomPublicRoomSearchData(publicRoomSearchDataImpl);
    }

    public boolean isForceSignout() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isForceSignoutImpl(j);
    }

    public boolean isCompanyContact(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isCompanyContactImpl(j, str);
    }

    @Nullable
    public ZoomBuddyGroup getBuddyGroupByType(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyGroupByTypeImpl = getBuddyGroupByTypeImpl(j, i);
        if (buddyGroupByTypeImpl == 0) {
            return null;
        }
        return new ZoomBuddyGroup(buddyGroupByTypeImpl);
    }

    @Nullable
    public ZoomBuddyGroup getBuddyGroupByJid(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyGroupByJidImpl = getBuddyGroupByJidImpl(j, str);
        if (buddyGroupByJidImpl == 0) {
            return null;
        }
        return new ZoomBuddyGroup(buddyGroupByJidImpl);
    }

    @Nullable
    public ZoomBuddyGroup getBuddyGroupByXMPPId(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyGroupByXmppIDImpl = getBuddyGroupByXmppIDImpl(j, str);
        if (buddyGroupByXmppIDImpl == 0) {
            return null;
        }
        return new ZoomBuddyGroup(buddyGroupByXmppIDImpl);
    }

    @Nullable
    public ZoomBuddyGroup getBuddyGroupAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyGroupAtImpl = getBuddyGroupAtImpl(j, i);
        if (buddyGroupAtImpl == 0) {
            return null;
        }
        return new ZoomBuddyGroup(buddyGroupAtImpl);
    }

    public int getBuddyGroupCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getBuddyGroupCountImpl(j);
    }

    public int TPV2_GetContactsPresence(List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return TPV2GetContactsPresenceImpl(j, list);
    }

    public int TPV2_SubscribePresence(List<String> list, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return TPV2SubscribePresenceImpl(j, list, i);
    }

    public int TPV2_UnsubscribePresence(List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return TPV2UnsubscribePresenceImpl(j, list);
    }

    @Nullable
    public AllBuddyInfo getAllBuddies(boolean z, boolean z2, @Nullable String[] strArr, @Nullable String str) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        byte[] allBuddiesImpl = getAllBuddiesImpl(this.mNativeHandle, z, z2, strArr == null ? new String[0] : strArr, str == null ? "" : str);
        if (allBuddiesImpl == null) {
            return null;
        }
        try {
            return AllBuddyInfo.parseFrom(allBuddiesImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public AllBuddyInfo getAllRooms() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            return AllBuddyInfo.parseFrom(getAllRoomsImpl(j));
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean isAnyBuddyGroupLarge() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isAnyBuddyGroupLargeImpl(j);
    }

    public int imChatGetOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return imChatGetOptionImpl(j);
    }

    @Nullable
    public List<String> blockUserGetAll() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return blockUserGetAllImpl(j);
    }

    public boolean blockUserIsBlocked(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return blockUserIsBlockedImpl(this.mNativeHandle, str);
    }

    public boolean blockUserBlockUsers(@Nullable List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0 || list == null) {
            return false;
        }
        return blockUserBlockUsersImpl(j, list);
    }

    public boolean blockUserUnBlockUsers(@Nullable List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0 || list == null) {
            return false;
        }
        return blockUserUnBlockUsersImpl(j, list);
    }

    public boolean blockUserEditBlockedUsersList(@Nullable List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0 || list == null) {
            return false;
        }
        return blockUserEditBlockedUsersListImpl(j, list);
    }

    public boolean assignGroupAdmins(String str, List<String> list) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || CollectionsUtil.isListEmpty(list)) {
            return false;
        }
        return assignGroupAdminsImpl(this.mNativeHandle, str, list);
    }

    public boolean destroyGroup(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return destroyGroupImpl(this.mNativeHandle, str);
    }

    @Nullable
    public String setUserSignature(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return setUserSignatureImpl(j, str);
    }

    @Nullable
    public String fetchUserProfileByJid(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        return fetchUserProfileByJidImpl(this.mNativeHandle, str);
    }

    public int getMyPresence() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMyPresenceImpl(j);
    }

    public int getMyPresenceStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMyPresenceStatusImpl(j);
    }

    @Nullable
    public ZoomBuddy getBuddyWithSipPhone(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long buddyWithSipPhoneImpl = getBuddyWithSipPhoneImpl(this.mNativeHandle, str);
        if (buddyWithSipPhoneImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyWithSipPhoneImpl);
    }

    @Nullable
    public ZoomBuddy getBuddyWithPbxNumber(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long buddyWithPbxNumberImpl = getBuddyWithPbxNumberImpl(this.mNativeHandle, str);
        if (buddyWithPbxNumberImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyWithPbxNumberImpl);
    }

    @Nullable
    public NumberMatchedBuddyItemList getBuddyWithNumber(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        byte[] buddyWithNumberImpl = getBuddyWithNumberImpl(this.mNativeHandle, str);
        if (buddyWithNumberImpl != null && buddyWithNumberImpl.length > 0) {
            try {
                return NumberMatchedBuddyItemList.parseFrom(buddyWithNumberImpl);
            } catch (InvalidProtocolBufferException unused) {
            }
        }
        return null;
    }

    @Nullable
    public String sendGetHttp(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return sendGetHttpMessageImpl(j, str);
    }

    @Nullable
    public String sendPostHttp(String str, @Nullable Map<String, String> map) {
        String[] strArr;
        String[] strArr2;
        if (this.mNativeHandle == 0) {
            return null;
        }
        if (map == null || map.size() <= 0) {
            strArr2 = null;
            strArr = null;
        } else {
            strArr = (String[]) map.values().toArray(new String[map.size()]);
            strArr2 = (String[]) map.keySet().toArray(new String[map.size()]);
        }
        return sendPostHttpMessageImpl(this.mNativeHandle, str, strArr2, strArr);
    }

    @Nullable
    public String downloadFileByUrl(String str, String str2, boolean z) {
        return downloadFileByUrl(str, str2, false, z);
    }

    @Nullable
    public String downloadFileByUrl(String str, String str2, boolean z, boolean z2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return downloadFileByUrlImpl(j, str, str2, z, z2);
    }

    @Nullable
    public String downloadFileForEditCodeSnippet(String str, String str2, long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return null;
        }
        return downloadFileForEditCodeSnippetImpl(j2, str, str2, j);
    }

    @Nullable
    public String getGiphyInfoByStr(String str, String str2, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getGiphyInfoByStrImpl(j, str, str2, i, "pg-13");
    }

    @Nullable
    public String getHotGiphyInfo(String str, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getHotGiphyInfoImpl(j, str, i, "pg-13");
    }

    public int getGiphyOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getGiphyOptionImpl(j);
    }

    @Nullable
    public String downloadGIFFromGiphyByUrl(String str, String str2, String str3, String str4) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return downloadGIFFromGiphyByUrlImpl(j, str, str2, str3, str4);
    }

    @Nullable
    public GiphyMsgInfo getGiphyInfo(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] giphyInfoImpl = getGiphyInfoImpl(j, str);
        if (giphyInfoImpl == null || giphyInfoImpl.length == 0) {
            return null;
        }
        try {
            return GiphyMsgInfo.parseFrom(giphyInfoImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public int sendMessageForGiphy(@NonNull MessageInput messageInput, @Nullable String[] strArr) {
        if (this.mNativeHandle == 0) {
            return 3;
        }
        if (strArr == null || strArr.length < 1) {
            strArr = new String[1];
        }
        return sendMessageForGiphyImpl(this.mNativeHandle, strArr, messageInput.toByteArray());
    }

    public boolean checkGiphyFileIsExist(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return checkGiphyFileIsExistImpl(j, str);
    }

    public boolean checkGroupNameIsExist(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return checkGroupNameIsExistImpl(j, str);
    }

    @Nullable
    public List<String> checkIfNeedUpdateHotGiphyInfo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return checkIfNeedUpdateHotGiphyInfoImpl(j);
    }

    @Nullable
    public List<String> sortSessions(@Nullable List<String> list) {
        if (this.mNativeHandle == 0 || list == null || list.isEmpty()) {
            return null;
        }
        return sortSessionsImpl(this.mNativeHandle, list);
    }

    @Nullable
    public List<String> sortSessionsByKeyAndMsgTime(String str, List<String> list) {
        if (this.mNativeHandle == 0 || list == null || list.isEmpty()) {
            return null;
        }
        return sortSessionsByKeyAndMsgTimeImpl(this.mNativeHandle, str, list);
    }

    @Nullable
    public String emojiVersionGetJsonStr() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return emojiVersionGetJsonStrImpl(j);
    }

    @Nullable
    public ZoomChatSession findSessionById(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long findSessionByIdImpl = findSessionByIdImpl(this.mNativeHandle, str);
        if (findSessionByIdImpl == 0) {
            return null;
        }
        return new ZoomChatSession(findSessionByIdImpl);
    }

    @Nullable
    public ZoomBuddyGroup getAddressbookContactBuddyGroup() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long addressbookContactBuddyGroupImpl = getAddressbookContactBuddyGroupImpl(j);
        if (addressbookContactBuddyGroupImpl == 0) {
            return null;
        }
        return new ZoomBuddyGroup(addressbookContactBuddyGroupImpl);
    }

    public boolean searchSessionLastMessageCtx(List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return searchSessionLastMessageCtxImpl(j, list);
    }

    public int editIMSettingGetOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return editIMSettingGetOptionImpl(j);
    }

    public int msgCopyGetOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return msgCopyGetOptionImpl(j);
    }

    @Nullable
    public LocalStorageTimeInterval getLocalStorageTimeInterval() {
        long j = this.mNativeHandle;
        LocalStorageTimeInterval localStorageTimeInterval = null;
        if (j == 0) {
            return null;
        }
        byte[] localStorageTimeIntervalImpl = getLocalStorageTimeIntervalImpl(j);
        if (localStorageTimeIntervalImpl == null) {
            return null;
        }
        try {
            localStorageTimeInterval = LocalStorageTimeInterval.parseFrom(localStorageTimeIntervalImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return localStorageTimeInterval;
    }

    public void notifyMissedCall(long j) {
        long j2 = this.mNativeHandle;
        if (j2 != 0) {
            notifyMissedCallImpl(j2, j);
        }
    }

    public void notifyOpenRobotChatSession(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            notifyOpenRobotChatSessionImpl(this.mNativeHandle, str);
        }
    }

    @Nullable
    public List<String> getAllRobotBuddies(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getAllRobotBuddiesImpl(j, str);
    }

    public boolean isStarSession(String str) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        if (ZMIMUtils.isAnnouncement(str)) {
            return !isStarSessionImpl(this.mNativeHandle, str);
        }
        return isStarSessionImpl(this.mNativeHandle, str);
    }

    public boolean starSessionSetStar(String str, boolean z) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        if (ZMIMUtils.isAnnouncement(str)) {
            z = !z;
        }
        boolean starSessionSetStarImpl = starSessionSetStarImpl(this.mNativeHandle, str, z);
        if (starSessionSetStarImpl) {
            EventBus.getDefault().postSticky(new ZMStarEvent(str, -1, z));
        }
        return starSessionSetStarImpl;
    }

    @Nullable
    public List<String> starSessionGetAll() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return starSessionGetAllImpl(j);
    }

    public boolean ClearAllStarSession() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return starSessionClearAllImpl(j);
    }

    public boolean clearAllStarMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return clearAllStarMessageImpl(j);
    }

    @Nullable
    public Map<String, List<Long>> starMessageGetAll() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return starMessageGetAllImpl(j);
    }

    @Nullable
    public List<String> getRoomDevices() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getRoomDevicesImpl(j);
    }

    @Nullable
    public List<String> getSendFailedMessages(@NonNull String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSendFailedMessagesImpl(j, str);
    }

    public boolean hasFailedMessage(@NonNull String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasFailedMessageImpl(j, str);
    }

    public boolean isStarMessage(String str, long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return isStarMessageImpl(j2, str, j);
    }

    @Nullable
    public List<String> getAllStarredMessages(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getAllStarredMessagesImpl(j, str);
    }

    @Nullable
    public String starMessageSyncMessages(Map<String, List<Long>> map) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return starMessageSyncMessagesImpl(j, map);
    }

    public boolean refreshChatAvailableInfo(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return refreshChatAvailableInfoImpl(j, str);
    }

    public boolean isChatAvailable(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isChatAvailableImpl(j, str);
    }

    @Nullable
    public ZoomFile getFileWithMessageID(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long fileWithMessageIDImpl = getFileWithMessageIDImpl(j, str, str2);
        if (fileWithMessageIDImpl == 0) {
            return null;
        }
        return new ZoomFile(fileWithMessageIDImpl);
    }

    public int getTotalUnreadMessageCountBySetting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return GetTotalUnreadMessageCountBySettingImpl(j);
    }

    public boolean isAddContactDisable() {
        long j = this.mNativeHandle;
        boolean z = false;
        if (j == 0) {
            return false;
        }
        if (getAddContactOptionImpl(j) == 2) {
            z = true;
        }
        return z;
    }

    public int myNotesGetOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return myNotesGetOptionImpl(j);
    }

    public boolean hasUpOrDownloadingFileRequest() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasUpOrDownloadingFileRequestImpl(j);
    }

    @Nullable
    public List<String> createPersonalBuddyGroup(String str, List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        List<String> createPersonalBuddyGroupImpl = createPersonalBuddyGroupImpl(j, str, list);
        if (createPersonalBuddyGroupImpl == null || createPersonalBuddyGroupImpl.size() != 2) {
            return null;
        }
        return createPersonalBuddyGroupImpl;
    }

    @Nullable
    public String deletePersonalBuddyGroup(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return deletePersonalBuddyGroupImpl(j, str);
    }

    @Nullable
    public String modifyPersonalBuddyGroupName(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return modifyPersonalBuddyGroupNameImpl(j, str, str2);
    }

    @Nullable
    public String addBuddyToPersonalBuddyGroup(List<String> list, String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return addBuddyToPersonalBuddyGroupImpl(j, list, str);
    }

    @Nullable
    public String removeBuddyToPersonalBuddyGroup(List<String> list, String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return removeBuddyToPersonalBuddyGroupImpl(j, list, str);
    }

    @Nullable
    public String moveBuddyFromPersonalBuddyGroup(List<String> list, String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return moveBuddyFromPersonalBuddyGroupImpl(j, list, str, str2);
    }

    @Nullable
    public FileIntegrations getListForFileIntegrationShare() {
        long j = this.mNativeHandle;
        FileIntegrations fileIntegrations = null;
        if (j == 0) {
            return null;
        }
        byte[] listForFileIntegrationShareImpl = getListForFileIntegrationShareImpl(j);
        if (listForFileIntegrationShareImpl == null) {
            return null;
        }
        try {
            fileIntegrations = FileIntegrations.parseFrom(listForFileIntegrationShareImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileIntegrations;
    }

    public int personalGroupGetOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return personalGroupGetOptionImpl(j);
    }

    public int accountChatGetOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return accountChatGetOptionImpl(j);
    }

    public boolean isIMChatOptionChanged() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isIMChatOptionChangedImpl(j);
    }

    @Nullable
    public String getContactRequestsSessionID() {
        return getContactRequestsSessionIDImpl();
    }

    @Nullable
    public List<String> getBroadcast() {
        return getBroadcastsImpl(this.mNativeHandle);
    }

    @Nullable
    public RobotCommand getLastUsedRobotCommand() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] lastUsedRobotCommandImpl = getLastUsedRobotCommandImpl(j);
        if (lastUsedRobotCommandImpl == null) {
            return null;
        }
        try {
            return RobotCommand.parseFrom(lastUsedRobotCommandImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean setLastUsedRobotCommand(@Nullable RobotCommand robotCommand) {
        long j = this.mNativeHandle;
        if (j == 0 || robotCommand == null) {
            return false;
        }
        return setLastUsedRobotCommandImpl(j, robotCommand.toByteArray());
    }

    public boolean isContactRequestsSession(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return StringUtil.safeString(getContactRequestsSessionID()).equals(str);
    }

    public boolean isUnstarredContactRequests() {
        return isStarSession(getContactRequestsSessionID());
    }

    public boolean isUnstarredAnnouncement() {
        if (!CollectionsUtil.isListEmpty(getBroadcast())) {
            return isStarSession((String) getBroadcast().get(0));
        }
        return false;
    }

    public int getFileTransferInReceiverOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 2;
        }
        return getFileTransferInReceiverOptionImpl(j);
    }

    public boolean isCodeSnippetDisabled() {
        return getCodeSnippetOption() == 2;
    }

    public int getCodeSnippetOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 2;
        }
        return getCodeSnippetOptionImpl(j);
    }

    public int getScreenCaptureOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 2;
        }
        return getScreenCaptureOptionImpl(j);
    }

    @Nullable
    public ThreadDataProvider getThreadDataProvider() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long threadDataProviderImpl = getThreadDataProviderImpl(j);
        if (threadDataProviderImpl == 0) {
            return null;
        }
        return new ThreadDataProvider(threadDataProviderImpl);
    }

    public boolean isDisableReply() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDisableReplyImpl(j);
    }

    public boolean isDisableReaction() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDisableReactionImpl(j);
    }

    public int getGroupInviteLimit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getGroupInviteLimitImpl(j);
    }
}
