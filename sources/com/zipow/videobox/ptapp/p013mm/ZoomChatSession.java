package com.zipow.videobox.ptapp.p013mm;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.eventbus.ZMStarEvent;
import com.zipow.videobox.ptapp.IMProtos.AtInfoItem;
import com.zipow.videobox.ptapp.IMProtos.AtInfoList;
import com.zipow.videobox.ptapp.IMProtos.EmojiList;
import com.zipow.videobox.ptapp.IMProtos.FontStyte;
import com.zipow.videobox.ptapp.IMProtos.MessageInfoList;
import com.zipow.videobox.ptapp.IMProtos.MessageInput;
import com.zipow.videobox.ptapp.IMProtos.MessageInput.Builder;
import com.zipow.videobox.ptapp.IMProtos.ThrCommentStates;
import com.zipow.videobox.util.EmojiHelper;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomChatSession */
public class ZoomChatSession {
    public static final int DEFAULT_QUERY_MESSAGE_COUNT = 30;
    private static final String TAG = "ZoomChatSession";
    private long mNativeHandle = 0;

    private native void checkAudioDownloadForMessageImpl(long j, String str);

    private native boolean cleanUnreadCommentsForThreadImpl(long j, long j2);

    private native void cleanUnreadMessageCountImpl(long j);

    private native boolean clearAllMarkedUnreadMessageImpl(long j);

    private native boolean deleteLocalMessageImpl(long j, String str);

    private native boolean deleteMessageImpl(long j, String str);

    private native boolean discardStarMessageImpl(long j, long j2);

    private native boolean downloadFileForMessageImpl(long j, String str, String str2);

    private native boolean editMessageByXMPPGuidImpl(long j, String str, byte[] bArr);

    @NonNull
    private native List<String> fetchLocaldbLinkedMessagesImpl(long j, int i);

    private native long getLastMessageImpl(long j);

    private native long getLastSearchAndOpenSessionTimeImpl(long j);

    private native long getLastTextMessageImpl(long j);

    private native int getMarkUnreadMessageCountImpl(long j);

    @Nullable
    private native byte[] getMarkUnreadMessagesImpl(long j);

    private native long getMessageByIdImpl(long j, String str);

    private native long getMessageByIndexImpl(long j, int i);

    private native long getMessageByServerTimeImpl(long j, long j2, boolean z);

    private native long getMessageByXMPPGuidImpl(long j, String str);

    private native int getMessageCountImpl(long j);

    @Nullable
    private native String getMessageDraftImpl(long j);

    private native long getMessageDraftTimeImpl(long j);

    private native long getReadedMsgTimeImpl(long j);

    private native long getSessionBuddyImpl(long j);

    private native long getSessionGroupImpl(long j);

    @Nullable
    private native String getSessionIdImpl(long j);

    private native byte[] getSessionUnreadCommentCountImpl(long j);

    @Nullable
    private native List<String> getUnreadAllMentionedMessagesImpl(long j);

    @Nullable
    private native List<String> getUnreadAtAllMessagesImpl(long j);

    @Nullable
    private native List<String> getUnreadAtMeMessagesImpl(long j);

    private native long getUnreadCommentCountBySettingImpl(long j, long j2);

    private native long getUnreadCommentCountImpl(long j, long j2);

    private native int getUnreadMessageCountBySettingImpl(long j);

    private native int getUnreadMessageCountImpl(long j);

    private native int getUnreadThreadsCountImpl(long j);

    private native boolean hasUnreadMessageAtMeImpl(long j);

    private native boolean hasUnreadedMessageAtAllMembersImpl(long j);

    private native boolean isGroupImpl(long j);

    private native boolean isLastMessageUnreadedAtAllMembersImpl(long j);

    private native boolean isMessageMarkUnreadImpl(long j, String str);

    private native boolean markMessageAsUnreadBySvrTimeImpl(long j, long j2);

    private native boolean markMessageAsUnreadImpl(long j, String str);

    private native boolean resendPendingE2EImageMessageImpl(long j, String str, String str2, byte[] bArr);

    private native boolean resendPendingMessageImpl(long j, String str, String str2);

    private native boolean revokeMessageByXMPPGuidImpl(long j, String str, boolean z);

    @Nullable
    private native String searchMarkUnreadMessageCtxImpl(long j, long j2, int i, int i2);

    @Nullable
    private native String sendAddonCommandImpl(long j, String str, String str2);

    private native boolean starMessageImpl(long j, long j2);

    private native boolean storeLastSearchAndOpenSessionTimeImpl(long j, long j2);

    private native boolean storeMessageDraftImpl(long j, String str);

    private native boolean storeMessageDraftTimeImpl(long j, long j2);

    private native boolean unmarkMessageAsUnreadImpl(long j, String str);

    private native boolean unmarkUnreadMessageBySvrTimeImpl(long j, long j2);

    public ZoomChatSession(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getSessionId() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSessionIdImpl(j);
    }

    public boolean isGroup() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isGroupImpl(j);
    }

    @Nullable
    public ZoomBuddy getSessionBuddy() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long sessionBuddyImpl = getSessionBuddyImpl(j);
        if (sessionBuddyImpl == 0) {
            return null;
        }
        return new ZoomBuddy(sessionBuddyImpl);
    }

    @Nullable
    public ZoomGroup getSessionGroup() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long sessionGroupImpl = getSessionGroupImpl(j);
        if (sessionGroupImpl == 0) {
            return null;
        }
        return new ZoomGroup(sessionGroupImpl);
    }

    @Nullable
    public ZoomMessage getLastMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long lastMessageImpl = getLastMessageImpl(j);
        if (lastMessageImpl == 0) {
            return null;
        }
        return new ZoomMessage(lastMessageImpl);
    }

    @Nullable
    public ZoomMessage getLastTextMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long lastTextMessageImpl = getLastTextMessageImpl(j);
        if (lastTextMessageImpl == 0) {
            return null;
        }
        return new ZoomMessage(lastTextMessageImpl);
    }

    public int getUnreadMessageCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUnreadMessageCountImpl(j);
    }

    public int getMarkUnreadMessageCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMarkUnreadMessageCountImpl(j);
    }

    public ZoomMessage getMessageById(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long messageByIdImpl = getMessageByIdImpl(this.mNativeHandle, str);
        if (messageByIdImpl == 0) {
            return null;
        }
        return new ZoomMessage(messageByIdImpl);
    }

    @Nullable
    public ZoomMessage getMessageByIndex(int i) {
        if (this.mNativeHandle == 0 || i < 0 || i >= getMessageCount()) {
            return null;
        }
        long messageByIndexImpl = getMessageByIndexImpl(this.mNativeHandle, i);
        if (messageByIndexImpl == 0) {
            return null;
        }
        return new ZoomMessage(messageByIndexImpl);
    }

    public int getMessageCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMessageCountImpl(j);
    }

    public boolean downloadFileForMessage(String str) {
        return downloadFileForMessage(str, null);
    }

    public boolean downloadFileForMessage(@Nullable String str, @Nullable String str2) {
        if (this.mNativeHandle == 0 || str == null) {
            return false;
        }
        if (str2 == null) {
            str2 = "";
        }
        return downloadFileForMessageImpl(this.mNativeHandle, str, str2);
    }

    public boolean resendPendingMessage(String str, String str2) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return resendPendingMessageImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean deleteLocalMessage(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return deleteLocalMessageImpl(this.mNativeHandle, str);
        }
        return false;
    }

    private boolean deleteMessageInternal(@Nullable String str) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        if (str == null) {
            str = "";
        }
        return deleteMessageImpl(this.mNativeHandle, str);
    }

    public boolean deleteMessage(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return deleteMessageInternal(str);
    }

    public boolean clearAllMessages() {
        return deleteMessageInternal("");
    }

    public void cleanUnreadMessageCount() {
        long j = this.mNativeHandle;
        if (j != 0) {
            cleanUnreadMessageCountImpl(j);
        }
    }

    public boolean hasUnreadMessageAtMe() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasUnreadMessageAtMeImpl(j);
    }

    public void checkAutoDownloadForMessage(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            checkAudioDownloadForMessageImpl(this.mNativeHandle, str);
        }
    }

    public boolean hasUnreadedMessageAtAllMembers() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasUnreadedMessageAtAllMembersImpl(j);
    }

    public boolean isLastMessageUnreadedAtAllMembers() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLastMessageUnreadedAtAllMembersImpl(j);
    }

    @Nullable
    public ZoomMessage getMessageByXMPPGuid(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long messageByXMPPGuidImpl = getMessageByXMPPGuidImpl(this.mNativeHandle, str);
        if (messageByXMPPGuidImpl == 0) {
            return null;
        }
        return new ZoomMessage(messageByXMPPGuidImpl);
    }

    public boolean revokeMessageByXMPPGuid(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return revokeMessageByXMPPGuidImpl(this.mNativeHandle, str, false);
    }

    public boolean storeMessageDraft(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        if (str == null) {
            str = "";
        }
        return storeMessageDraftImpl(j, str);
    }

    @Nullable
    public String getMessageDraft() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getMessageDraftImpl(j);
    }

    public boolean storeMessageDraftTime(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return storeMessageDraftTimeImpl(j2, j);
    }

    public long getMessageDraftTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMessageDraftTimeImpl(j);
    }

    @Nullable
    public String sendAddonCommand(String str) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        return sendAddonCommandImpl(this.mNativeHandle, str, "");
    }

    @Nullable
    public String sendAddonCommand(String str, String str2) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        if (TextUtils.isEmpty(str2)) {
            return sendAddonCommand(str);
        }
        return sendAddonCommandImpl(this.mNativeHandle, str, str2);
    }

    public boolean resendPendingE2EImageMessage(String str, String str2, @Nullable String str3) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str3) || !ImageUtil.isValidImageFile(str3) || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        byte[] previewImgData = ImageUtil.getPreviewImgData(str3, 60000);
        if (previewImgData == null) {
            return false;
        }
        return resendPendingE2EImageMessageImpl(this.mNativeHandle, str, str2, previewImgData);
    }

    public boolean editMessageByXMPPGuid(CharSequence charSequence, String str, String str2, @Nullable List<AtInfoItem> list, boolean z) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(charSequence) || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        String realMsg = EmojiHelper.getInstance().getRealMsg(charSequence.toString());
        EmojiList emojiList = EmojiHelper.getInstance().getEmojiList(realMsg);
        Builder newBuilder = MessageInput.newBuilder();
        if (!TextUtils.isEmpty(realMsg)) {
            newBuilder.setBody(realMsg);
        }
        if (emojiList != null) {
            newBuilder.setEmojiList(emojiList);
        }
        newBuilder.setMsgType(0);
        newBuilder.setSessionID(str2);
        newBuilder.setIsAtAllGroupMembers(z);
        FontStyte buildFromCharSequence = FontStyleHelper.buildFromCharSequence(charSequence);
        if (buildFromCharSequence != null) {
            newBuilder.setFontStyte(buildFromCharSequence);
        }
        if (!CollectionsUtil.isListEmpty(list)) {
            AtInfoList.Builder newBuilder2 = AtInfoList.newBuilder();
            newBuilder2.addAllAtInfoItem(list);
            newBuilder.setAtInfoList(newBuilder2.build());
        }
        return editMessageByXMPPGuidImpl(this.mNativeHandle, str, newBuilder.build().toByteArray());
    }

    public boolean markMessageAsUnread(String str) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str)) {
            return markMessageAsUnreadImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean unmarkMessageAsUnread(String str) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str)) {
            return unmarkMessageAsUnreadImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean unmarkUnreadMessageBySvrTime(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return unmarkUnreadMessageBySvrTimeImpl(j2, j);
    }

    public boolean markMessageAsUnreadBySvrTime(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return markMessageAsUnreadBySvrTimeImpl(j2, j);
    }

    public boolean clearAllMarkedUnreadMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return clearAllMarkedUnreadMessageImpl(j);
    }

    public boolean isMessageMarkUnread(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMessageMarkUnreadImpl(j, str);
    }

    public MessageInfoList getMarkUnreadMessages() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            return MessageInfoList.parseFrom(getMarkUnreadMessagesImpl(j));
        } catch (Exception unused) {
            return null;
        }
    }

    public String searchMarkUnreadMessageCtx(long j, int i, int i2) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return null;
        }
        return searchMarkUnreadMessageCtxImpl(j2, j, i, i2);
    }

    @Nullable
    public List<String> getUnreadAtMeMessages() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getUnreadAtMeMessagesImpl(j);
    }

    @Nullable
    public List<String> getUnreadAtAllMessages() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getUnreadAtAllMessagesImpl(j);
    }

    @Nullable
    public List<String> getUnreadAllMentionedMessages() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getUnreadAllMentionedMessagesImpl(j);
    }

    @Nullable
    public List<String> fetchLocaldbLinkedMessages(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return fetchLocaldbLinkedMessagesImpl(j, i);
    }

    public boolean starMessage(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        boolean starMessageImpl = starMessageImpl(j2, j);
        EventBus.getDefault().post(new ZMStarEvent(getSessionId(), j, true));
        return starMessageImpl;
    }

    public boolean discardStarMessage(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        boolean discardStarMessageImpl = discardStarMessageImpl(j2, j);
        EventBus.getDefault().post(new ZMStarEvent(getSessionId(), j, false));
        return discardStarMessageImpl;
    }

    @Nullable
    public ZoomMessage getMessageByServerTime(long j, boolean z) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return null;
        }
        long messageByServerTimeImpl = getMessageByServerTimeImpl(j2, j, z);
        if (messageByServerTimeImpl == 0) {
            return null;
        }
        return new ZoomMessage(messageByServerTimeImpl);
    }

    public int getUnreadMessageCountBySetting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUnreadMessageCountBySettingImpl(j);
    }

    public long getReadedMsgTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getReadedMsgTimeImpl(j);
    }

    public long getUnreadCommentCount(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return 0;
        }
        return getUnreadCommentCountImpl(j2, j);
    }

    public long getUnreadCommentCountBySetting(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return 0;
        }
        return getUnreadCommentCountBySettingImpl(j2, j);
    }

    public ThrCommentStates getSessionUnreadCommentCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] sessionUnreadCommentCountImpl = getSessionUnreadCommentCountImpl(j);
        if (sessionUnreadCommentCountImpl == null) {
            return null;
        }
        try {
            return ThrCommentStates.parseFrom(sessionUnreadCommentCountImpl);
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean cleanUnreadCommentsForThread(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return cleanUnreadCommentsForThreadImpl(j2, j);
    }

    public int getUnreadThreadsCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUnreadThreadsCountImpl(j);
    }

    public boolean storeLastSearchAndOpenSessionTime(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return storeLastSearchAndOpenSessionTimeImpl(j2, j);
    }

    public long getLastSearchAndOpenSessionTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getLastSearchAndOpenSessionTimeImpl(j);
    }
}
