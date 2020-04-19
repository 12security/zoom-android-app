package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.ptapp.IMProtos.AtInfoItem;
import com.zipow.videobox.ptapp.IMProtos.AtInfoList;
import com.zipow.videobox.ptapp.IMProtos.EmojiCountInfo;
import com.zipow.videobox.ptapp.IMProtos.EmojiCountMap;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrationInfo;
import com.zipow.videobox.ptapp.IMProtos.FontStyte;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.tempbean.IZoomMessageTemplate;
import com.zipow.videobox.util.TextCommandHelper;
import com.zipow.videobox.view.AtNameInfo;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.message.CommentSplitView;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import com.zipow.videobox.view.p014mm.message.MessageAddonView;
import com.zipow.videobox.view.p014mm.message.MessageAudioReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageAudioSendView;
import com.zipow.videobox.view.p014mm.message.MessageBelowNewCommentView;
import com.zipow.videobox.view.p014mm.message.MessageBelowNewMsgView;
import com.zipow.videobox.view.p014mm.message.MessageCallReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageCallSendView;
import com.zipow.videobox.view.p014mm.message.MessageCodeSnippetReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageCodeSnippetSendView;
import com.zipow.videobox.view.p014mm.message.MessageFileIntegrationReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageFileIntegrationSendView;
import com.zipow.videobox.view.p014mm.message.MessageFileReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageFileSendView;
import com.zipow.videobox.view.p014mm.message.MessageGiphyReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageGiphySendView;
import com.zipow.videobox.view.p014mm.message.MessageLinkPreviewReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageLinkPreviewSendView;
import com.zipow.videobox.view.p014mm.message.MessageLoadingMoreView;
import com.zipow.videobox.view.p014mm.message.MessageLodingView;
import com.zipow.videobox.view.p014mm.message.MessagePicReceiveView;
import com.zipow.videobox.view.p014mm.message.MessagePicSendView;
import com.zipow.videobox.view.p014mm.message.MessageRemoveHistoryView;
import com.zipow.videobox.view.p014mm.message.MessageSystemView;
import com.zipow.videobox.view.p014mm.message.MessageTemplateView;
import com.zipow.videobox.view.p014mm.message.MessageTextReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageTextSendView;
import com.zipow.videobox.view.p014mm.message.MessageThreadDeletedView;
import com.zipow.videobox.view.p014mm.message.MessageThreadNotExistView;
import com.zipow.videobox.view.p014mm.message.MessageTimeView;
import com.zipow.videobox.view.p014mm.message.MessageUnSupportReceiveView;
import com.zipow.videobox.view.p014mm.message.MessageUnSupportSendView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMMessageItem */
public class MMMessageItem {
    public static final String COMMENT_SPLIT_MSGID = "COMMENT_SPLIT_MSGID";
    public static final String E2E_SYSTEM_MSG_ID = "E2E_SYSTEM_MSG_ID";
    public static final String E2E_SYSTEM_STATE_READY_MSG_ID = "E2E_SYSTEM_STATE_READY_MSG_ID";
    public static final int ITEM_TYPE_ACCEPT_INVITE = 17;
    public static final int ITEM_TYPE_ADDON = 29;
    public static final int ITEM_TYPE_ADD_TO_GROUP = 13;
    public static final int ITEM_TYPE_AUDIO_RECEIVED = 2;
    public static final int ITEM_TYPE_AUDIO_SENT = 3;
    public static final int ITEM_TYPE_CALL_ACCEPTED_RECEIVED = 22;
    public static final int ITEM_TYPE_CALL_CANCEL_RECEIVED = 43;
    public static final int ITEM_TYPE_CALL_DECLINED_RECEIVED = 23;
    public static final int ITEM_TYPE_CALL_MISSED_RECEIVED = 21;
    public static final int ITEM_TYPE_CALL_OUTGOING = 44;
    public static final int ITEM_TYPE_CALL_UNKNOW_RECEIVED = 40;
    public static final int ITEM_TYPE_CODE_SNIPPT_RECEIVED = 37;
    public static final int ITEM_TYPE_CODE_SNIPPT_SENT = 38;
    public static final int ITEM_TYPE_COMMENT_SPLIT = 47;
    public static final int ITEM_TYPE_CREATE_GROUP = 12;
    public static final int ITEM_TYPE_E2E_ACCEPT_RECEIVED = 25;
    public static final int ITEM_TYPE_E2E_INVITE_RECEIVED = 24;
    public static final int ITEM_TYPE_E2E_SYSTEM_MESSAGE = 26;
    public static final int ITEM_TYPE_EDIT_GROUP_DESCRIPTION = 54;
    public static final int ITEM_TYPE_FILE_INTEGRATION_RECEIVED = 46;
    public static final int ITEM_TYPE_FILE_INTEGRATION_SENT = 45;
    public static final int ITEM_TYPE_FILE_TRANSFER_IN_RECEIVER_DISABLE = 52;
    public static final int ITEM_TYPE_GIF_RECEIVED = 27;
    public static final int ITEM_TYPE_GIF_SENT = 28;
    public static final int ITEM_TYPE_GIPHY_RECEIVED = 33;
    public static final int ITEM_TYPE_GIPHY_SENT = 32;
    public static final int ITEM_TYPE_GROUP_HAS_DESCRIPTION = 55;
    public static final int ITEM_TYPE_LINK_PREVIEW_RECEIVED = 35;
    public static final int ITEM_TYPE_LINK_PREVIEW_SENT = 34;
    public static final int ITEM_TYPE_LOADING = 42;
    public static final int ITEM_TYPE_LOADING_MORE = 53;
    public static final int ITEM_TYPE_MEETING_INVITATION_RECEIVED = 8;
    public static final int ITEM_TYPE_MEETING_INVITATION_SENT = 9;
    public static final int ITEM_TYPE_MESSAGE_TEMPLATE = 41;
    public static final int ITEM_TYPE_MODIFY_GROUP = 16;
    public static final int ITEM_TYPE_NOTE = 18;
    public static final int ITEM_TYPE_OTHER_FILE_RECEIVED = 10;
    public static final int ITEM_TYPE_OTHER_FILE_SENT = 11;
    public static final int ITEM_TYPE_PICTURE_RECEIVED = 4;
    public static final int ITEM_TYPE_PICTURE_SENT = 5;
    public static final int ITEM_TYPE_QUIT_GROUP = 15;
    public static final int ITEM_TYPE_REMOVE_FROM_GROUP = 14;
    public static final int ITEM_TYPE_REMOVE_HISTORY = 39;
    public static final int ITEM_TYPE_SAY_HI_FTE = 51;
    public static final int ITEM_TYPE_SYSTEM_NEW_COMMENT_BELOW = 49;
    public static final int ITEM_TYPE_SYSTEM_NEW_MSG_BELOW = 36;
    public static final int ITEM_TYPE_TEXT_RECEIVED = 0;
    public static final int ITEM_TYPE_TEXT_SENT = 1;
    public static final int ITEM_TYPE_THREAD_DELETED = 48;
    public static final int ITEM_TYPE_THREAD_NOT_EXIT = 50;
    public static final int ITEM_TYPE_TIME_STAMP = 19;
    public static final int ITEM_TYPE_TOTAL_COUNT = 56;
    public static final int ITEM_TYPE_UNKNOWN = 20;
    public static final int ITEM_TYPE_UNSUPPORT_RECEIVED = 31;
    public static final int ITEM_TYPE_UNSUPPORT_SENT = 30;
    public static final int ITEM_TYPE_VIDEO_RECEIVED = 6;
    public static final int ITEM_TYPE_VIDEO_SENT = 7;
    public static final String LAST_MSG_MARK_MSGID = "LAST_MSG_MARK_MSGID";
    public static final String LOADING_MORE = "LOADING_MORE";
    public static final long MSG_DELETEABLE_TIME = 63072000000L;
    public static final String NEW_COMMENT_MARK_MSGID = "MSGID_NEW_comment_MARK_ID";
    public static final String NEW_MSG_MARK_MSGID = "MSGID_NEW_MSG_MARK_ID";
    public static final int REACTION_LIMIT_COUNT = 12;
    private static final String TAG = "MMMessageItem";
    public static final String TIMED_CHAT_MSG_ID = "TIMED_CHAT_MSG_ID";
    @Nullable
    public MMAddonMessage addonMsg;
    public int atAllCommentCount;
    public AtInfoList atInfoLis;
    @Nullable
    public List<String> atList;
    public int atMeCommentCount;
    public List<MMMessageItemAtNameSpan> atNameSpanList;
    public int audioVideoLength = 0;
    private List<MMMessageItem> comments = new ArrayList();
    public long commentsCount;
    public String draftReply;
    public long editMessageTime;
    private List<MMCommentsEmojiCountItem> emojiCountItems;
    public int fileDownloadResultCode = 0;
    @Nullable
    public String fileId;
    @Nullable
    public FileInfo fileInfo;
    @Nullable
    public FileIntegrationInfo fileIntegrationInfo;
    public int fileRatio = -1;
    @Nullable
    public FontStyte fontStyte;
    @Nullable
    public IMAddrBookItem fromContact;
    @Nullable
    public String fromJid;
    @Nullable
    public String fromScreenName;
    @Nullable
    public String giphyID;
    public int hasCommentsOdds = 2;
    public boolean hasFailedMessage;
    public boolean hasMoreReply;
    public boolean hideStarView = false;
    public boolean isAtEveryone = false;
    public boolean isBodyAllEmojis = false;
    public boolean isComment;
    public boolean isDeletedThread;
    public boolean isDownloading = false;
    public boolean isE2E = false;
    public boolean isFileDownloaded = false;
    public boolean isFirstSync = false;
    public boolean isFollowedThread;
    public boolean isGroupMessage = false;
    public boolean isHistorySyncMessage = false;
    public boolean isMessgeStarred;
    public boolean isMsgAtMe = false;
    public boolean isNotExitThread;
    public boolean isOutMsg = false;
    public boolean isPlayed = false;
    public boolean isPlaying = false;
    public boolean isPreviewDownloadFailed = false;
    private boolean isScreenShot = false;
    public boolean isStikcerMsg;
    public boolean isSupportMsg;
    private boolean isSystemMsg;
    public boolean isThread;
    public boolean isUnread = false;
    public long lastEmojiTime;
    public long lastLocalCommentTime;
    public List<LinkPreviewMetaInfo> linkPreviewMetaInfos;
    @Nullable
    public String localFilePath;
    public int markUnreadCommentCount;
    @Nullable
    public CharSequence message;
    @Nullable
    public String messageId;
    public int messageState = 0;
    public long messageTime;
    public int messageType = 0;
    @Nullable
    public String messageXMPPId;
    public boolean needTriggerUpdate;
    public boolean onlyMessageShow = false;
    @Nullable
    public String picturePreviewPath;
    public int replyExpandNum;
    public long serverSideTime;
    public String sessionId;
    public boolean showReaction;
    @Nullable
    public IZoomMessageTemplate template;
    public String threadId;
    public long threadTime;
    @Nullable
    public String toJid;
    @Nullable
    public String toScreenName;
    @Nullable
    public FileTransferInfo transferInfo;
    public long unreadCommentCount;
    public long unreadCommentCountBySetting;
    public long visibleTime;

    public boolean isReachReactionLimit() {
        return false;
    }

    public static MMMessageItem initWithZoomMessage(String str, String str2, @Nullable ZoomMessenger zoomMessenger, String str3, String str4, boolean z, boolean z2) {
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2) || StringUtil.isEmptyOrNull(str3) || StringUtil.isEmptyOrNull(str4) || zoomMessenger == null) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return null;
        }
        MMMessageItem mMMessageItem = new MMMessageItem();
        mMMessageItem.messageId = str;
        mMMessageItem.sessionId = str2;
        mMMessageItem.fromJid = myself.getJid();
        mMMessageItem.toJid = myself.getJid();
        mMMessageItem.messageTime = System.currentTimeMillis();
        mMMessageItem.giphyID = str3;
        mMMessageItem.message = str4;
        mMMessageItem.isGroupMessage = z;
        mMMessageItem.isOutMsg = z2;
        if (z2) {
            mMMessageItem.messageType = 32;
        } else {
            mMMessageItem.messageType = 33;
        }
        return mMMessageItem;
    }

    @NonNull
    public static MMMessageItem createNewMsgsMark(long j) {
        MMMessageItem mMMessageItem = new MMMessageItem();
        mMMessageItem.messageId = NEW_MSG_MARK_MSGID;
        mMMessageItem.messageTime = j;
        mMMessageItem.serverSideTime = j;
        mMMessageItem.visibleTime = j;
        mMMessageItem.messageType = 36;
        return mMMessageItem;
    }

    public static MMMessageItem createNewCommentMark(long j) {
        MMMessageItem mMMessageItem = new MMMessageItem();
        mMMessageItem.messageId = NEW_COMMENT_MARK_MSGID;
        mMMessageItem.messageTime = j;
        mMMessageItem.serverSideTime = j;
        mMMessageItem.visibleTime = j;
        mMMessageItem.messageType = 49;
        return mMMessageItem;
    }

    public static MMMessageItem createLoadingMsg(long j) {
        MMMessageItem mMMessageItem = new MMMessageItem();
        mMMessageItem.messageId = COMMENT_SPLIT_MSGID;
        mMMessageItem.messageTime = j;
        mMMessageItem.serverSideTime = j;
        mMMessageItem.visibleTime = j;
        mMMessageItem.messageType = 42;
        return mMMessageItem;
    }

    public static MMMessageItem createCommentSplit(long j) {
        MMMessageItem mMMessageItem = new MMMessageItem();
        mMMessageItem.messageId = COMMENT_SPLIT_MSGID;
        mMMessageItem.messageTime = j;
        mMMessageItem.serverSideTime = j;
        mMMessageItem.visibleTime = j;
        mMMessageItem.messageType = 47;
        mMMessageItem.isThread = true;
        return mMMessageItem;
    }

    public static MMMessageItem createDeletedThread(long j) {
        MMMessageItem mMMessageItem = new MMMessageItem();
        mMMessageItem.messageTime = j;
        mMMessageItem.serverSideTime = j;
        mMMessageItem.visibleTime = j;
        mMMessageItem.messageType = 48;
        mMMessageItem.isDeletedThread = true;
        mMMessageItem.isThread = true;
        return mMMessageItem;
    }

    public static MMMessageItem createNotExitThread(long j) {
        MMMessageItem mMMessageItem = new MMMessageItem();
        mMMessageItem.messageTime = j;
        mMMessageItem.serverSideTime = j;
        mMMessageItem.visibleTime = j;
        mMMessageItem.messageType = 50;
        mMMessageItem.isNotExitThread = true;
        mMMessageItem.isThread = true;
        return mMMessageItem;
    }

    public static MMMessageItem createLoadingMore() {
        MMMessageItem mMMessageItem = new MMMessageItem();
        mMMessageItem.messageType = 53;
        return mMMessageItem;
    }

    @Nullable
    public static MMMessageItem initWithZoomMessage(ZoomMessage zoomMessage, String str, ZoomMessenger zoomMessenger, boolean z, boolean z2, Context context, IMAddrBookItem iMAddrBookItem, MMFileContentMgr mMFileContentMgr) {
        return initWithZoomMessage(zoomMessage, str, zoomMessenger, z, z2, context, iMAddrBookItem, mMFileContentMgr, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:135:0x03f0, code lost:
        p021us.zoom.androidlib.util.ZMLog.m280e(TAG, "addMessageToListAdapter, MessageType unknow %d", java.lang.Integer.valueOf(r6));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:203:0x0541, code lost:
        if (r0 != 27) goto L_0x0547;
     */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.view.p014mm.MMMessageItem initWithZoomMessage(@androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.ZoomMessage r16, java.lang.String r17, @androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.ZoomMessenger r18, boolean r19, boolean r20, @androidx.annotation.Nullable android.content.Context r21, com.zipow.videobox.view.IMAddrBookItem r22, @androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.MMFileContentMgr r23, boolean r24) {
        /*
            r0 = r16
            r1 = r17
            r2 = r18
            r3 = r19
            r4 = r20
            r5 = r21
            r6 = r22
            r7 = r23
            r8 = r24
            r9 = 0
            if (r0 == 0) goto L_0x05ed
            boolean r10 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r17)
            if (r10 != 0) goto L_0x05ed
            if (r2 == 0) goto L_0x05ed
            if (r5 != 0) goto L_0x0021
            goto L_0x05ed
        L_0x0021:
            boolean r10 = r16.IsDeletedThread()
            if (r10 != 0) goto L_0x059a
            boolean r10 = r16.isNotExistThread()
            if (r10 == 0) goto L_0x002f
            goto L_0x059a
        L_0x002f:
            com.zipow.videobox.view.mm.MMMessageItem r10 = new com.zipow.videobox.view.mm.MMMessageItem
            r10.<init>()
            r10.sessionId = r1
            r10.isGroupMessage = r3
            int r3 = r16.getMessageState()
            r10.messageState = r3
            java.lang.String r3 = r16.getSenderID()
            r10.fromJid = r3
            java.lang.String r3 = r16.getReceiverID()
            r10.toJid = r3
            long r11 = r16.getStamp()
            r10.messageTime = r11
            long r11 = r16.getServerSideTime()
            r10.serverSideTime = r11
            java.lang.String r3 = r16.getMessageID()
            r10.messageId = r3
            boolean r3 = r16.isUnread()
            r10.isUnread = r3
            boolean r3 = r16.isPlayed()
            r10.isPlayed = r3
            boolean r3 = r16.isE2EMessage()
            r10.isE2E = r3
            java.util.List r3 = r16.getMsgAtList()
            r10.atList = r3
            com.zipow.videobox.ptapp.IMProtos$AtInfoList r3 = r16.getMsgAtInfoList()
            r10.atInfoLis = r3
            com.zipow.videobox.ptapp.IMProtos$FontStyte r3 = r16.getFontStyte()
            r10.fontStyte = r3
            boolean r3 = r16.isMessageAtEveryone()
            r10.isAtEveryone = r3
            java.lang.String r3 = r16.getMessageXMPPGuid()
            r10.messageXMPPId = r3
            boolean r3 = r16.isStickerMessage()
            r10.isStikcerMsg = r3
            boolean r3 = r16.couldReallySupport()
            r10.isSupportMsg = r3
            long r11 = r16.getEditActionMilliSecTime()
            r10.editMessageTime = r11
            boolean r3 = r16.isHistorySyncMessage()
            r10.isHistorySyncMessage = r3
            java.lang.String r3 = r16.getGiphyID()
            r10.giphyID = r3
            boolean r3 = r16.isMessageAtMe()
            r10.isMsgAtMe = r3
            boolean r3 = r16.needTriggerUpdate()
            r10.needTriggerUpdate = r3
            r10.hideStarView = r8
            r10.isOutMsg = r4
            boolean r3 = r16.isThread()
            r10.isThread = r3
            boolean r3 = r16.isComment()
            r10.isComment = r3
            java.lang.String r3 = r16.getThreadID()
            r10.threadId = r3
            long r11 = r16.getLastEmojiTime()
            r10.lastEmojiTime = r11
            long r11 = r16.getTotalCommentsCount()
            r10.commentsCount = r11
            long r11 = r16.getLastLocalCommentTime()
            r10.lastLocalCommentTime = r11
            boolean r3 = r16.IsFollowedThread()
            r10.isFollowedThread = r3
            boolean r3 = r16.IsDeletedThread()
            r10.isDeletedThread = r3
            long r11 = r16.getThreadTime()
            r10.threadTime = r11
            boolean r3 = r10.isThread
            if (r3 == 0) goto L_0x00f7
            r10.updateThreadInfo()
        L_0x00f7:
            java.lang.String r3 = r10.fromJid
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r2.getBuddyWithJID(r3)
            r8 = 14
            r11 = 1
            if (r3 != 0) goto L_0x0109
            java.lang.String r3 = r16.getSenderName()
            r10.fromScreenName = r3
            goto L_0x0157
        L_0x0109:
            java.lang.String r12 = r3.getPhoneNumber()
            if (r4 != 0) goto L_0x0151
            if (r6 != 0) goto L_0x011c
            com.zipow.videobox.view.IMAddrBookItem r12 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r3)
            java.lang.String r3 = com.zipow.videobox.ptapp.p013mm.BuddyNameUtil.getBuddyDisplayName(r3, r12)
            r10.fromScreenName = r3
            goto L_0x0149
        L_0x011c:
            boolean r13 = r22.getIsRobot()
            if (r13 != 0) goto L_0x0133
            int r13 = r16.getMessageType()
            if (r13 != r8) goto L_0x0133
            com.zipow.videobox.view.IMAddrBookItem r12 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r3)
            java.lang.String r3 = com.zipow.videobox.ptapp.p013mm.BuddyNameUtil.getBuddyDisplayName(r3, r12)
            r10.fromScreenName = r3
            goto L_0x0149
        L_0x0133:
            java.lang.String r13 = com.zipow.videobox.ptapp.p013mm.BuddyNameUtil.getBuddyDisplayName(r3, r6)
            r10.fromScreenName = r13
            r6.addPhoneNumber(r12, r12)
            java.lang.String r3 = r3.getEmail()
            r6.setAccoutEmail(r3)
            java.lang.String r3 = r10.fromScreenName
            r6.setScreenName(r3)
            r12 = r6
        L_0x0149:
            if (r12 == 0) goto L_0x014e
            r12.setIsZoomUser(r11)
        L_0x014e:
            r10.fromContact = r12
            goto L_0x0157
        L_0x0151:
            java.lang.String r3 = com.zipow.videobox.ptapp.p013mm.BuddyNameUtil.getMyDisplayName(r3)
            r10.fromScreenName = r3
        L_0x0157:
            if (r7 == 0) goto L_0x0170
            java.lang.String r3 = r10.messageXMPPId
            com.zipow.videobox.ptapp.mm.ZoomFile r3 = r7.getFileWithMessageID(r1, r3)
            if (r3 == 0) goto L_0x0170
            boolean r12 = r3.isScreenShot()
            r10.isScreenShot = r12
            java.lang.String r12 = r3.getWebFileID()
            r10.fileId = r12
            r7.destroyFileObject(r3)
        L_0x0170:
            java.lang.String r3 = r10.toJid
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r2.getBuddyWithJID(r3)
            if (r3 == 0) goto L_0x0182
            if (r4 == 0) goto L_0x017b
            goto L_0x017c
        L_0x017b:
            r6 = r9
        L_0x017c:
            java.lang.String r3 = com.zipow.videobox.ptapp.p013mm.BuddyNameUtil.getBuddyDisplayName(r3, r6)
            r10.toScreenName = r3
        L_0x0182:
            android.content.res.Resources r3 = r21.getResources()
            if (r3 != 0) goto L_0x0189
            return r9
        L_0x0189:
            boolean r6 = r10.isSupportMsg
            r7 = 30
            r12 = 31
            if (r6 != 0) goto L_0x0199
            if (r4 == 0) goto L_0x0196
            r10.messageType = r7
            goto L_0x0198
        L_0x0196:
            r10.messageType = r12
        L_0x0198:
            return r10
        L_0x0199:
            int r6 = r16.getMessageType()
            r13 = 80
            r15 = 13
            r14 = 0
            if (r6 == r13) goto L_0x0509
            r13 = 88
            if (r6 == r13) goto L_0x04f8
            r13 = 2
            switch(r6) {
                case 0: goto L_0x043c;
                case 1: goto L_0x041e;
                case 2: goto L_0x0401;
                case 3: goto L_0x03f0;
                case 4: goto L_0x03e2;
                case 5: goto L_0x041e;
                case 6: goto L_0x03c2;
                default: goto L_0x01ac;
            }
        L_0x01ac:
            switch(r6) {
                case 10: goto L_0x039c;
                case 11: goto L_0x0376;
                case 12: goto L_0x0350;
                case 13: goto L_0x0331;
                case 14: goto L_0x02f1;
                case 15: goto L_0x02c6;
                default: goto L_0x01af;
            }
        L_0x01af:
            switch(r6) {
                case 20: goto L_0x02b1;
                case 21: goto L_0x029e;
                case 22: goto L_0x028b;
                case 23: goto L_0x0276;
                case 24: goto L_0x0261;
                case 25: goto L_0x029e;
                case 26: goto L_0x0250;
                case 27: goto L_0x023f;
                default: goto L_0x01b2;
            }
        L_0x01b2:
            switch(r6) {
                case 50: goto L_0x0233;
                case 51: goto L_0x0227;
                case 52: goto L_0x021b;
                case 53: goto L_0x020f;
                case 54: goto L_0x0203;
                case 55: goto L_0x01f7;
                default: goto L_0x01b5;
            }
        L_0x01b5:
            switch(r6) {
                case 70: goto L_0x01e1;
                case 71: goto L_0x01cb;
                default: goto L_0x01b8;
            }
        L_0x01b8:
            switch(r6) {
                case 99: goto L_0x01c3;
                case 100: goto L_0x03f0;
                default: goto L_0x01bb;
            }
        L_0x01bb:
            if (r4 == 0) goto L_0x01c0
            r10.messageType = r7
            goto L_0x01c2
        L_0x01c0:
            r10.messageType = r12
        L_0x01c2:
            return r10
        L_0x01c3:
            r0 = 19
            r10.messageType = r0
            r10.isSystemMsg = r11
            goto L_0x0519
        L_0x01cb:
            r0 = 25
            r10.messageType = r0
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_invite_accepted
            java.lang.Object[] r1 = new java.lang.Object[r11]
            java.lang.String r2 = r10.fromScreenName
            r1[r14] = r2
            java.lang.String r0 = r3.getString(r0, r1)
            r10.message = r0
            r10.isSystemMsg = r11
            goto L_0x0519
        L_0x01e1:
            r0 = 24
            r10.messageType = r0
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_get_invite
            java.lang.Object[] r1 = new java.lang.Object[r11]
            java.lang.String r2 = r10.fromScreenName
            r1[r14] = r2
            java.lang.String r0 = r3.getString(r0, r1)
            r10.message = r0
            r10.isSystemMsg = r11
            goto L_0x0519
        L_0x01f7:
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r0 = 44
            r10.messageType = r0
            goto L_0x0519
        L_0x0203:
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r0 = 43
            r10.messageType = r0
            goto L_0x0519
        L_0x020f:
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r0 = 40
            r10.messageType = r0
            goto L_0x0519
        L_0x021b:
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r0 = 23
            r10.messageType = r0
            goto L_0x0519
        L_0x0227:
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r0 = 22
            r10.messageType = r0
            goto L_0x0519
        L_0x0233:
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r0 = 21
            r10.messageType = r0
            goto L_0x0519
        L_0x023f:
            r1 = 55
            r10.messageType = r1
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r10.isSystemMsg = r11
            java.lang.CharSequence r0 = r10.message
            if (r0 != 0) goto L_0x0519
            return r9
        L_0x0250:
            r1 = 54
            r10.messageType = r1
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r10.isSystemMsg = r11
            java.lang.CharSequence r0 = r10.message
            if (r0 != 0) goto L_0x0519
            return r9
        L_0x0261:
            r1 = 16
            r10.messageType = r1
            java.lang.CharSequence r0 = r16.getBody()
            java.lang.CharSequence r0 = buildGroupMessageBody(r0, r5)
            r10.message = r0
            r10.isSystemMsg = r11
            java.lang.CharSequence r0 = r10.message
            if (r0 != 0) goto L_0x0519
            return r9
        L_0x0276:
            r1 = 15
            r10.messageType = r1
            java.lang.CharSequence r0 = r16.getBody()
            java.lang.CharSequence r0 = buildGroupMessageBody(r0, r5)
            r10.message = r0
            r10.isSystemMsg = r11
            java.lang.CharSequence r0 = r10.message
            if (r0 != 0) goto L_0x0519
            return r9
        L_0x028b:
            r10.messageType = r8
            java.lang.CharSequence r0 = r16.getBody()
            java.lang.CharSequence r0 = buildGroupMessageBody(r0, r5)
            r10.message = r0
            r10.isSystemMsg = r11
            java.lang.CharSequence r0 = r10.message
            if (r0 != 0) goto L_0x0519
            return r9
        L_0x029e:
            r10.messageType = r15
            java.lang.CharSequence r0 = r16.getBody()
            java.lang.CharSequence r0 = buildGroupMessageBody(r0, r5)
            r10.message = r0
            r10.isSystemMsg = r11
            java.lang.CharSequence r0 = r10.message
            if (r0 != 0) goto L_0x0519
            return r9
        L_0x02b1:
            r1 = 12
            r10.messageType = r1
            java.lang.CharSequence r0 = r16.getBody()
            java.lang.CharSequence r0 = buildGroupMessageBody(r0, r5)
            r10.message = r0
            r10.isSystemMsg = r11
            java.lang.CharSequence r0 = r10.message
            if (r0 != 0) goto L_0x0519
            return r9
        L_0x02c6:
            boolean r1 = r16.isFileDownloaded()
            r10.isFileDownloaded = r1
            java.lang.CharSequence r1 = r16.getBody()
            r10.message = r1
            com.zipow.videobox.ptapp.mm.ZoomMessage$FileInfo r1 = r16.getFileInfo()
            r10.fileInfo = r1
            com.zipow.videobox.ptapp.mm.ZoomMessage$FileTransferInfo r1 = r16.getFileTransferInfo()
            r10.transferInfo = r1
            if (r4 == 0) goto L_0x02e5
            r1 = 45
            r10.messageType = r1
            goto L_0x02e9
        L_0x02e5:
            r1 = 46
            r10.messageType = r1
        L_0x02e9:
            com.zipow.videobox.ptapp.IMProtos$FileIntegrationInfo r0 = r16.getFileIntegrationShareInfo()
            r10.fileIntegrationInfo = r0
            goto L_0x0519
        L_0x02f1:
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessageTemplate r2 = r2.getZoomMessageTemplate()
            if (r2 == 0) goto L_0x0519
            java.lang.String r3 = r16.getMessageXMPPGuid()
            com.zipow.videobox.ptapp.IMProtos$RobotMsg r1 = r2.robotDecode(r1, r3)
            if (r1 == 0) goto L_0x0325
            boolean r0 = r1.getIsUnSupportRobotMessage()
            if (r0 == 0) goto L_0x0315
            if (r4 == 0) goto L_0x0311
            r10.messageType = r7
            goto L_0x0519
        L_0x0311:
            r10.messageType = r12
            goto L_0x0519
        L_0x0315:
            r0 = 41
            r10.messageType = r0
            java.lang.String r0 = r1.getJsonMsg()
            com.zipow.videobox.tempbean.IZoomMessageTemplate r0 = com.zipow.videobox.tempbean.parse.MessageTemplateParse.parse(r0)
            r10.template = r0
            goto L_0x0519
        L_0x0325:
            r1 = 18
            r10.messageType = r1
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            goto L_0x0519
        L_0x0331:
            java.lang.String r1 = r16.getLocalFilePath()
            r10.localFilePath = r1
            boolean r1 = r16.isFileDownloaded()
            r10.isFileDownloaded = r1
            if (r4 == 0) goto L_0x0344
            r1 = 38
            r10.messageType = r1
            goto L_0x0348
        L_0x0344:
            r1 = 37
            r10.messageType = r1
        L_0x0348:
            com.zipow.videobox.ptapp.mm.ZoomMessage$FileInfo r0 = r16.getFileInfo()
            r10.fileInfo = r0
            goto L_0x0519
        L_0x0350:
            java.lang.String r1 = r16.getLocalFilePath()
            r10.localFilePath = r1
            java.lang.String r1 = r16.getPicturePreviewPath()
            r10.picturePreviewPath = r1
            boolean r1 = r16.isFileDownloaded()
            r10.isFileDownloaded = r1
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            if (r4 == 0) goto L_0x0370
            r0 = 32
            r10.messageType = r0
            goto L_0x0519
        L_0x0370:
            r0 = 33
            r10.messageType = r0
            goto L_0x0519
        L_0x0376:
            java.lang.CharSequence r0 = r16.getBody()
            java.lang.String r0 = r0.toString()
            com.zipow.videobox.view.mm.MMAddonMessage r0 = com.zipow.videobox.view.p014mm.MMAddonMessage.initFromMsgBody(r0)
            if (r0 == 0) goto L_0x0519
            boolean r1 = r0.isPlainText()
            if (r1 == 0) goto L_0x0394
            java.lang.String r0 = r0.getPlainText()
            r10.message = r0
            r10.messageType = r14
            goto L_0x0519
        L_0x0394:
            r1 = 29
            r10.messageType = r1
            r10.addonMsg = r0
            goto L_0x0519
        L_0x039c:
            java.lang.String r1 = r16.getLocalFilePath()
            r10.localFilePath = r1
            boolean r1 = r16.isFileDownloaded()
            r10.isFileDownloaded = r1
            com.zipow.videobox.ptapp.mm.ZoomMessage$FileInfo r1 = r16.getFileInfo()
            r10.fileInfo = r1
            com.zipow.videobox.ptapp.mm.ZoomMessage$FileTransferInfo r0 = r16.getFileTransferInfo()
            r10.transferInfo = r0
            if (r4 == 0) goto L_0x03bc
            r0 = 11
            r10.messageType = r0
            goto L_0x0519
        L_0x03bc:
            r0 = 10
            r10.messageType = r0
            goto L_0x0519
        L_0x03c2:
            java.lang.String r1 = r16.getLocalFilePath()
            r10.localFilePath = r1
            java.lang.String r1 = r16.getPicturePreviewPath()
            r10.picturePreviewPath = r1
            boolean r0 = r16.isFileDownloaded()
            r10.isFileDownloaded = r0
            if (r4 == 0) goto L_0x03dc
            r0 = 28
            r10.messageType = r0
            goto L_0x0519
        L_0x03dc:
            r0 = 27
            r10.messageType = r0
            goto L_0x0519
        L_0x03e2:
            if (r4 == 0) goto L_0x03ea
            r0 = 9
            r10.messageType = r0
            goto L_0x051a
        L_0x03ea:
            r0 = 8
            r10.messageType = r0
            goto L_0x051a
        L_0x03f0:
            java.lang.String r0 = TAG
            java.lang.String r1 = "addMessageToListAdapter, MessageType unknow %d"
            java.lang.Object[] r2 = new java.lang.Object[r11]
            java.lang.Integer r3 = java.lang.Integer.valueOf(r6)
            r2[r14] = r3
            p021us.zoom.androidlib.util.ZMLog.m280e(r0, r1, r2)
            goto L_0x051a
        L_0x0401:
            java.lang.String r1 = r16.getLocalFilePath()
            r10.localFilePath = r1
            boolean r1 = r16.isFileDownloaded()
            r10.isFileDownloaded = r1
            int r0 = r16.getAudioLength()
            r10.audioVideoLength = r0
            if (r4 == 0) goto L_0x041a
            r0 = 3
            r10.messageType = r0
            goto L_0x0519
        L_0x041a:
            r10.messageType = r13
            goto L_0x0519
        L_0x041e:
            java.lang.String r1 = r16.getLocalFilePath()
            r10.localFilePath = r1
            java.lang.String r1 = r16.getPicturePreviewPath()
            r10.picturePreviewPath = r1
            boolean r0 = r16.isFileDownloaded()
            r10.isFileDownloaded = r0
            if (r4 == 0) goto L_0x0437
            r0 = 5
            r10.messageType = r0
            goto L_0x0519
        L_0x0437:
            r0 = 4
            r10.messageType = r0
            goto L_0x0519
        L_0x043c:
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            int r0 = r10.messageState
            r3 = 3
            if (r0 == r3) goto L_0x0449
            if (r0 != r13) goto L_0x04c2
        L_0x0449:
            boolean r0 = com.zipow.videobox.ptapp.PTSettingHelper.isImLlinkPreviewDescription()
            if (r0 == 0) goto L_0x04c2
            boolean r0 = r10.isE2E
            if (r0 != 0) goto L_0x04c2
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.CrawlerLinkPreview r0 = r0.getLinkCrawler()
            if (r0 == 0) goto L_0x04c2
            boolean r3 = r0.isLinkPreviewEnable()
            if (r3 == 0) goto L_0x04c2
            java.util.LinkedHashSet r3 = new java.util.LinkedHashSet
            r3.<init>()
            java.lang.CharSequence r6 = r10.message
            java.util.List r6 = p021us.zoom.androidlib.util.StringUtil.getUrls(r6)
            if (r6 == 0) goto L_0x0473
            r3.addAll(r6)
        L_0x0473:
            int r6 = r3.size()
            if (r6 <= 0) goto L_0x04c2
            int r6 = r3.size()
            r7 = 4
            if (r6 > r7) goto L_0x04c2
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            java.util.Iterator r3 = r3.iterator()
        L_0x0489:
            boolean r7 = r3.hasNext()
            if (r7 == 0) goto L_0x04ba
            java.lang.Object r7 = r3.next()
            java.lang.String r7 = (java.lang.String) r7
            com.zipow.videobox.ptapp.IMProtos$CrawlLinkMetaInfo r7 = r0.FuzzyGetLinkMetaInfo(r7)
            java.lang.String r8 = r10.messageXMPPId
            com.zipow.videobox.view.mm.LinkPreviewMetaInfo r7 = com.zipow.videobox.view.p014mm.LinkPreviewMetaInfo.createFromProto(r7, r1, r8)
            if (r7 == 0) goto L_0x0489
            java.lang.String r8 = r7.getDesc()
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r8)
            if (r8 == 0) goto L_0x04b6
            java.lang.String r8 = r7.getImgUrl()
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r8)
            if (r8 == 0) goto L_0x04b6
            goto L_0x0489
        L_0x04b6:
            r6.add(r7)
            goto L_0x0489
        L_0x04ba:
            int r0 = r6.size()
            if (r0 <= 0) goto L_0x04c2
            r10.linkPreviewMetaInfos = r6
        L_0x04c2:
            java.util.List<com.zipow.videobox.view.mm.LinkPreviewMetaInfo> r0 = r10.linkPreviewMetaInfos
            boolean r0 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r0)
            if (r0 != 0) goto L_0x04d6
            if (r4 == 0) goto L_0x04d1
            r0 = 34
            r10.messageType = r0
            goto L_0x04dd
        L_0x04d1:
            r0 = 35
            r10.messageType = r0
            goto L_0x04dd
        L_0x04d6:
            if (r4 == 0) goto L_0x04db
            r10.messageType = r11
            goto L_0x04dd
        L_0x04db:
            r10.messageType = r14
        L_0x04dd:
            highLightAtNameSpanInMessageItem(r10, r2, r5)
            installFontstyle(r10)
            com.zipow.videobox.view.mm.sticker.CommonEmojiHelper r0 = com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper.getInstance()
            java.lang.CharSequence r1 = r10.message
            com.zipow.videobox.view.mm.sticker.CommonEmojiHelper$ZMEmojiSpannableStringBuilder r1 = r0.tranToEmojiText(r1)
            r10.message = r1
            java.lang.CharSequence r1 = r10.message
            boolean r0 = r0.isAllEmojis(r1)
            r10.isBodyAllEmojis = r0
            goto L_0x0519
        L_0x04f8:
            r1 = 18
            r10.messageType = r1
            java.lang.CharSequence r0 = r16.getBody()
            r10.message = r0
            r10.isSystemMsg = r11
            java.lang.CharSequence r0 = r10.message
            if (r0 != 0) goto L_0x0519
            return r9
        L_0x0509:
            r1 = 18
            r10.messageType = r1
            java.lang.CharSequence r0 = r16.getBody()
            java.lang.CharSequence r0 = buildRecallMessageBody(r0, r5)
            r10.message = r0
            r10.isSystemMsg = r11
        L_0x0519:
            r9 = r10
        L_0x051a:
            boolean r0 = com.zipow.videobox.util.ZMIMUtils.isFileTransferInReceiverDisable()
            if (r0 == 0) goto L_0x0547
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r0 = r0.isFileTransferDisabled()
            if (r0 == 0) goto L_0x0547
            if (r9 == 0) goto L_0x0547
            int r0 = r9.messageType
            r1 = 4
            if (r0 != r1) goto L_0x0535
            boolean r0 = r9.isScreenShot
            if (r0 == 0) goto L_0x0543
        L_0x0535:
            int r0 = r9.messageType
            r1 = 10
            if (r0 == r1) goto L_0x0543
            r1 = 46
            if (r0 == r1) goto L_0x0543
            r1 = 27
            if (r0 != r1) goto L_0x0547
        L_0x0543:
            r0 = 52
            r9.messageType = r0
        L_0x0547:
            if (r9 == 0) goto L_0x0599
            boolean r0 = r9.isE2E
            if (r0 == 0) goto L_0x0599
            int r0 = r9.messageState
            r1 = 8
            if (r0 == r1) goto L_0x0586
            r1 = 9
            if (r0 == r1) goto L_0x0586
            r1 = 10
            if (r0 != r1) goto L_0x055c
            goto L_0x0586
        L_0x055c:
            r1 = 3
            if (r0 != r1) goto L_0x056c
            android.content.res.Resources r0 = r21.getResources()
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_message_decrypting
            java.lang.String r0 = r0.getString(r1)
            r9.message = r0
            goto L_0x0599
        L_0x056c:
            r1 = 11
            if (r0 == r1) goto L_0x0572
            if (r0 != r15) goto L_0x0599
        L_0x0572:
            android.content.res.Resources r0 = r21.getResources()
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_unable_decrypt
            java.lang.String r0 = r0.getString(r1)
            r9.message = r0
            if (r4 == 0) goto L_0x0583
            r9.messageType = r11
            goto L_0x0599
        L_0x0583:
            r9.messageType = r14
            goto L_0x0599
        L_0x0586:
            android.content.res.Resources r0 = r21.getResources()
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_decrypt_failed_12310
            java.lang.String r0 = r0.getString(r1)
            r9.message = r0
            if (r4 == 0) goto L_0x0597
            r9.messageType = r11
            goto L_0x0599
        L_0x0597:
            r9.messageType = r14
        L_0x0599:
            return r9
        L_0x059a:
            com.zipow.videobox.ptapp.ThreadDataProvider r2 = r18.getThreadDataProvider()
            if (r2 != 0) goto L_0x05a1
            return r9
        L_0x05a1:
            java.lang.String r4 = r16.getMessageID()
            long r5 = r2.getServerVisibleTime(r1, r4)
            boolean r7 = r16.IsDeletedThread()
            if (r7 == 0) goto L_0x05b4
            com.zipow.videobox.view.mm.MMMessageItem r5 = createDeletedThread(r5)
            goto L_0x05b8
        L_0x05b4:
            com.zipow.videobox.view.mm.MMMessageItem r5 = createNotExitThread(r5)
        L_0x05b8:
            r5.messageId = r4
            r5.messageXMPPId = r4
            r5.sessionId = r1
            java.lang.String r6 = r16.getSenderID()
            r5.fromJid = r6
            java.lang.String r6 = r16.getReceiverID()
            r5.toJid = r6
            r5.isGroupMessage = r3
            int r3 = r2.threadHasCommentsOdds(r0)
            r5.hasCommentsOdds = r3
            long r6 = r16.getTotalCommentsCount()
            r5.commentsCount = r6
            long r6 = r16.getServerSideTime()
            r5.serverSideTime = r6
            java.lang.String r1 = r2.getThreadReplyDraft(r1, r4)
            r5.draftReply = r1
            boolean r0 = r16.isE2EMessage()
            r5.isE2E = r0
            r5.hideStarView = r8
            return r5
        L_0x05ed:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMMessageItem.initWithZoomMessage(com.zipow.videobox.ptapp.mm.ZoomMessage, java.lang.String, com.zipow.videobox.ptapp.mm.ZoomMessenger, boolean, boolean, android.content.Context, com.zipow.videobox.view.IMAddrBookItem, com.zipow.videobox.ptapp.mm.MMFileContentMgr, boolean):com.zipow.videobox.view.mm.MMMessageItem");
    }

    @NonNull
    public String getFileIntegrationUrl() {
        FileIntegrationInfo fileIntegrationInfo2 = this.fileIntegrationInfo;
        if (fileIntegrationInfo2 == null) {
            return "";
        }
        if (!StringUtil.isEmptyOrNull(fileIntegrationInfo2.getPreviewUrl())) {
            return this.fileIntegrationInfo.getPreviewUrl();
        }
        if (!StringUtil.isEmptyOrNull(this.fileIntegrationInfo.getDownloadUrl())) {
            return this.fileIntegrationInfo.getDownloadUrl();
        }
        return !StringUtil.isEmptyOrNull(this.fileIntegrationInfo.getThumbnailUrl()) ? this.fileIntegrationInfo.getThumbnailUrl() : "";
    }

    public boolean canShowReaction() {
        boolean z = false;
        boolean z2 = this.messageState == 4;
        boolean z3 = this.messageState == 1;
        boolean z4 = this.messageState == 6;
        if (z2 || z3 || z4) {
            return false;
        }
        int i = this.messageType;
        if (i == 0 || i == 1 || i == 4 || i == 5 || i == 10 || i == 11 || i == 27 || i == 28 || i == 32 || i == 33 || i == 34 || i == 35 || i == 37 || i == 38 || i == 2 || i == 3 || i == 45 || i == 46) {
            z = true;
        }
        return z;
    }

    public void updateCommentsEmojiCountItems(EmojiCountMap emojiCountMap) {
        this.emojiCountItems = new ArrayList();
        if (emojiCountMap != null && emojiCountMap.getEmojiCountInfosCount() != 0) {
            for (EmojiCountInfo mMCommentsEmojiCountItem : emojiCountMap.getEmojiCountInfosList()) {
                this.emojiCountItems.add(new MMCommentsEmojiCountItem(mMCommentsEmojiCountItem));
            }
        }
    }

    public void updateThreadInfo() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                this.visibleTime = threadDataProvider.getServerVisibleTime(this.sessionId, this.messageId);
                this.draftReply = threadDataProvider.getThreadReplyDraft(this.sessionId, this.messageId);
            }
        }
    }

    public void updateCommentsInThread(List<String> list, boolean z, @NonNull ThreadDataProvider threadDataProvider, int i, ZoomMessenger zoomMessenger, MMFileContentMgr mMFileContentMgr, IMAddrBookItem iMAddrBookItem) {
        ThreadDataProvider threadDataProvider2 = threadDataProvider;
        if (z) {
            this.comments = new ArrayList();
        }
        if (!CollectionsUtil.isCollectionEmpty(list)) {
            List<String> list2 = list;
            HashSet hashSet = new HashSet(list);
            Iterator it = this.comments.iterator();
            while (it.hasNext()) {
                if (hashSet.contains(((MMMessageItem) it.next()).messageId)) {
                    it.remove();
                }
            }
            ArrayList arrayList = new ArrayList();
            Context globalContext = VideoBoxApplication.getGlobalContext();
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String jid = myself.getJid();
                for (String messagePtr : list) {
                    ZoomMessage messagePtr2 = threadDataProvider2.getMessagePtr(this.sessionId, messagePtr);
                    if (messagePtr2 != null) {
                        ZoomMessenger zoomMessenger2 = zoomMessenger;
                        MMMessageItem initWithZoomMessage = initWithZoomMessage(messagePtr2, this.sessionId, zoomMessenger2, this.isGroupMessage, TextUtils.equals(jid, messagePtr2.getSenderID()), globalContext, iMAddrBookItem, mMFileContentMgr);
                        if (initWithZoomMessage != null) {
                            arrayList.add(initWithZoomMessage);
                        }
                    }
                }
                if (i == 1) {
                    this.comments.addAll(0, arrayList);
                } else {
                    this.comments.addAll(arrayList);
                }
            } else {
                return;
            }
        }
        ZoomMessage messagePtr3 = threadDataProvider2.getMessagePtr(this.sessionId, this.messageId);
        if (messagePtr3 != null) {
            this.hasCommentsOdds = threadDataProvider2.threadHasCommentsOdds(messagePtr3);
        }
    }

    public void clearComments() {
        this.comments.clear();
    }

    @NonNull
    public List<MMMessageItem> getComments() {
        return this.comments;
    }

    private static void highLightAtNameSpanInMessageItem(@Nullable MMMessageItem mMMessageItem, @Nullable ZoomMessenger zoomMessenger, @Nullable Context context) {
        if (mMMessageItem != null && zoomMessenger != null && context != null) {
            List atNameInfoListInMessageItem = getAtNameInfoListInMessageItem(mMMessageItem, zoomMessenger, context);
            if (atNameInfoListInMessageItem != null && !atNameInfoListInMessageItem.isEmpty()) {
                spanAtNameInfo(atNameInfoListInMessageItem, mMMessageItem, context);
            }
        }
    }

    private static void installFontstyle(MMMessageItem mMMessageItem) {
        mMMessageItem.message = FontStyleHelper.getCharSequenceFromMMMessageItem(mMMessageItem.message, mMMessageItem.fontStyte);
    }

    private static List<AtNameInfo> getAtNameInfoListInMessageItem(MMMessageItem mMMessageItem, @NonNull ZoomMessenger zoomMessenger, @NonNull Context context) {
        AtNameInfo atNameInfo;
        AtInfoList atInfoList = mMMessageItem.atInfoLis;
        if (atInfoList == null || atInfoList.getAtInfoItemCount() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(atInfoList.getAtInfoItemCount());
        for (int i = 0; i < atInfoList.getAtInfoItemCount(); i++) {
            AtInfoItem atInfoItem = atInfoList.getAtInfoItem(i);
            if (atInfoItem != null) {
                if (atInfoItem.getType() == 1 || atInfoItem.getType() == 0) {
                    if (atInfoItem.getType() != 0 || !mMMessageItem.isAtEveryone) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(atInfoItem.getJid());
                        atNameInfo = buddyWithJID != null ? new AtNameInfo(BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null), atInfoItem.getPositionStart(), atInfoItem.getPositionEnd(), atInfoItem.getType(), atInfoItem.getJid()) : null;
                    } else {
                        AtNameInfo atNameInfo2 = new AtNameInfo(context.getString(C4558R.string.zm_lbl_select_everyone), atInfoItem.getPositionStart(), atInfoItem.getPositionEnd(), 2, atInfoItem.getJid());
                        atNameInfo = atNameInfo2;
                    }
                } else if (atInfoItem.getType() == 3) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(atInfoItem.getJid());
                    atNameInfo = groupById != null ? new AtNameInfo(groupById.getGroupDisplayName(context), atInfoItem.getPositionStart(), atInfoItem.getPositionEnd(), atInfoItem.getType(), atInfoItem.getJid()) : null;
                } else {
                    AtNameInfo atNameInfo3 = new AtNameInfo(context.getString(C4558R.string.zm_lbl_select_everyone), atInfoItem.getPositionStart(), atInfoItem.getPositionEnd(), 2, atInfoItem.getJid());
                    atNameInfo = atNameInfo3;
                }
                if (atNameInfo != null) {
                    arrayList.add(atNameInfo);
                }
            }
        }
        return arrayList;
    }

    private static void spanAtNameInfo(List<AtNameInfo> list, MMMessageItem mMMessageItem, @NonNull Context context) {
        boolean z;
        String str;
        boolean z2;
        MMMessageItem mMMessageItem2 = mMMessageItem;
        Context context2 = context;
        int length = mMMessageItem2.message.length();
        int size = list.size();
        SpannableString spannableString = new SpannableString(mMMessageItem2.message);
        mMMessageItem2.atNameSpanList = new ArrayList(list.size());
        for (int i = 0; i < size; i++) {
            AtNameInfo atNameInfo = (AtNameInfo) list.get(i);
            if (atNameInfo != null) {
                int startIndex = atNameInfo.getStartIndex();
                int endIndex = atNameInfo.getEndIndex();
                if (atNameInfo.getType() == 3) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= zoomMessenger.getGroupCount()) {
                                break;
                            }
                            ZoomGroup groupAt = zoomMessenger.getGroupAt(i2);
                            if (groupAt != null && groupAt.isRoom() && TextUtils.equals(groupAt.getGroupID(), atNameInfo.getJid())) {
                                z2 = true;
                                break;
                            }
                            i2++;
                        }
                    }
                    z2 = false;
                    z = z2;
                } else {
                    z = true;
                }
                if (z) {
                    if (atNameInfo.getType() == 0 || !UIUtil.isLegalSpanIndex(spannableString, startIndex, endIndex)) {
                        if (atNameInfo.getType() == 3) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(TextCommandHelper.CHANNEL_CMD_CHAR);
                            sb.append(atNameInfo.getDisplayName());
                            str = sb.toString();
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(TextCommandHelper.REPLY_AT_CHAR);
                            sb2.append(atNameInfo.getDisplayName());
                            str = sb2.toString();
                        }
                        int i3 = 0;
                        while (i3 < length) {
                            int indexOf = TextUtils.indexOf(mMMessageItem2.message, str, i3);
                            if (indexOf < 0) {
                                break;
                            }
                            MMMessageItemAtNameSpan mMMessageItemAtNameSpan = r0;
                            int i4 = indexOf;
                            String str2 = str;
                            MMMessageItemAtNameSpan mMMessageItemAtNameSpan2 = new MMMessageItemAtNameSpan(ContextCompat.getColor(context2, C4558R.color.zm_chat_msg_highlight), indexOf, indexOf + str.length(), atNameInfo.getJid(), mMMessageItem);
                            spannableString.setSpan(mMMessageItemAtNameSpan, i4, i4 + str2.length(), 33);
                            mMMessageItem2.atNameSpanList.add(mMMessageItemAtNameSpan);
                            i3 = str2.length() + i4;
                            str = str2;
                        }
                    } else {
                        MMMessageItemAtNameSpan mMMessageItemAtNameSpan3 = new MMMessageItemAtNameSpan(ContextCompat.getColor(context2, C4558R.color.zm_chat_msg_highlight), atNameInfo, mMMessageItem2);
                        int i5 = endIndex + 1;
                        if (i5 > spannableString.length()) {
                            i5 = spannableString.length();
                        }
                        spannableString.setSpan(mMMessageItemAtNameSpan3, startIndex, i5, 33);
                        mMMessageItem2.atNameSpanList.add(mMMessageItemAtNameSpan3);
                    }
                }
            }
        }
        mMMessageItem2.message = spannableString;
    }

    @Nullable
    private static CharSequence buildRecallMessageBody(@Nullable CharSequence charSequence, @Nullable Context context) {
        if (context == null || charSequence == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        String charSequence2 = charSequence.toString();
        if (myself != null && StringUtil.isSameString(myself.getJid(), charSequence2)) {
            return context.getString(C4558R.string.zm_msg_delete_by_me_24679);
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(charSequence2);
        if (buddyWithJID == null) {
            return null;
        }
        return context.getString(C4558R.string.zm_msg_delete_by_other_24679, new Object[]{buddyWithJID.getScreenName()});
    }

    @Nullable
    private static CharSequence buildGroupMessageBody(@Nullable CharSequence charSequence, @Nullable Context context) {
        GroupAction loadFromString = GroupAction.loadFromString(charSequence == null ? null : charSequence.toString());
        if (loadFromString == null || context == null) {
            return charSequence;
        }
        return loadFromString.toMessage(context);
    }

    public List<MMCommentsEmojiCountItem> getEmojiCountItems() {
        return this.emojiCountItems;
    }

    @Nullable
    private static CharSequence buildGroupDescJoinFirstMessageBody(@Nullable CharSequence charSequence, @Nullable Context context) {
        if (context == null || charSequence == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        String charSequence2 = charSequence.toString();
        if (myself != null && StringUtil.isSameString(myself.getJid(), charSequence2)) {
            return context.getString(C4558R.string.zm_msg_delete_by_me_24679);
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(charSequence2);
        if (buddyWithJID == null) {
            return null;
        }
        return context.getString(C4558R.string.zm_msg_delete_by_other_24679, new Object[]{buddyWithJID.getScreenName()});
    }

    public boolean isNormalFileMsg() {
        if (!TextUtils.isEmpty(this.fileId)) {
            return true;
        }
        switch (this.messageType) {
            case 4:
            case 5:
            case 10:
            case 11:
            case 27:
            case 28:
            case 45:
            case 46:
                return true;
            default:
                return false;
        }
    }

    public boolean isNormalImageMsg() {
        switch (this.messageType) {
            case 10:
            case 11:
            case 37:
            case 38:
            case 45:
            case 46:
                return false;
            default:
                if (!TextUtils.isEmpty(this.fileId)) {
                    return true;
                }
                switch (this.messageType) {
                    case 4:
                    case 5:
                    case 27:
                    case 28:
                        return true;
                    default:
                        return false;
                }
        }
    }

    public boolean isMessageE2EWaitDecrypt() {
        boolean z = false;
        if (!this.isE2E) {
            return false;
        }
        int i = this.messageState;
        if (i == 3 || i == 12 || i == 13 || i == 11) {
            z = true;
        }
        return z;
    }

    public void bindViewHolder(ViewHolder viewHolder) {
        if (viewHolder.itemView instanceof AbsMessageView) {
            ((AbsMessageView) viewHolder.itemView).setMessageItem(this);
        }
    }

    public static AbsMessageView getStarredMessageView(@NonNull Context context, int i, @Nullable View view) {
        switch (i) {
            case 0:
            case 1:
                return createMessageTextReceiveView(context, view);
            case 2:
            case 3:
                return createAudioReceiveView(context, view);
            case 4:
            case 5:
            case 27:
            case 28:
                return createPictureReceiveView(context, view);
            case 10:
            case 11:
                return createOtherFileReceiveView(context, view);
            case 21:
            case 22:
            case 23:
            case 40:
            case 43:
            case 44:
                return createMessageCallReceiveView(context, view);
            case 29:
                return createAddonMsgView(context, view);
            case 32:
            case 33:
                return createGiphyReceiveView(context, view);
            case 34:
            case 35:
                return createLinkPreviewMsgReceiveView(context, view);
            case 37:
            case 38:
                return createCodeSnippetReceiveView(context, view);
            case 41:
                return createTemplateView(context, view);
            case 45:
            case 46:
                return createFileIntegrationReceiveView(context, view);
            case 48:
                return createThreadDeletedView(context, view);
            case 50:
                return createThreadNotExitView(context, view);
            case 52:
                return createFileTransferInReceiverDisableView(context, view);
            default:
                return null;
        }
    }

    @Nullable
    public static AbsMessageView createView(Context context, int i) {
        switch (i) {
            case 0:
                return createMessageTextReceiveView(context, null);
            case 1:
                return createMessageTextSendView(context, null);
            case 2:
                return createAudioReceiveView(context, null);
            case 3:
                return createAudioSendView(context, null);
            case 4:
            case 27:
                return createPictureReceiveView(context, null);
            case 5:
            case 28:
                return createPictureSendView(context, null);
            case 10:
                return createOtherFileReceiveView(context, null);
            case 11:
                return createOtherFileSendView(context, null);
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 24:
            case 25:
            case 26:
            case 54:
            case 55:
                return createSystemMessageView(context, null);
            case 19:
                return createMessageTimeView(context, null);
            case 21:
            case 22:
            case 23:
            case 40:
            case 43:
                return createMessageCallReceiveView(context, null);
            case 29:
                return createAddonMsgView(context, null);
            case 30:
                return createUnSupportMsgSendView(context, null);
            case 31:
                return createUnSupportMsgReceiveView(context, null);
            case 32:
                return createGiphySendView(context, null);
            case 33:
                return createGiphyReceiveView(context, null);
            case 34:
                return createLinkPreviewMsgSendView(context, null);
            case 35:
                return createLinkPreviewMsgReceiveView(context, null);
            case 36:
                return createNewMsgBelowView(context, null);
            case 37:
                return createCodeSnippetReceiveView(context, null);
            case 38:
                return createCodeSnippetSendView(context, null);
            case 39:
                return createRemoveHistoryView(context, null);
            case 41:
                return createTemplateView(context, null);
            case 42:
                return createLoadingView(context, null);
            case 44:
                return createMessageCallSendView(context, null);
            case 45:
                return createFileIntegrationSendView(context, null);
            case 46:
                return createFileIntegrationReceiveView(context, null);
            case 47:
                return createCommentSplitView(context, null);
            case 48:
                return createThreadDeletedView(context, null);
            case 50:
                return createThreadNotExitView(context, null);
            case 52:
                return createFileTransferInReceiverDisableView(context, null);
            default:
                return null;
        }
    }

    public static AbsMessageView createCommentView(Context context, int i) {
        switch (i) {
            case 0:
                return createMessageTextReceiveView(context, null, true);
            case 1:
                return createMessageTextSendView(context, null, true);
            case 2:
                return createAudioReceiveView(context, null, true);
            case 3:
                return createAudioSendView(context, null, true);
            case 4:
            case 27:
                return createPictureReceiveView(context, null, true);
            case 5:
            case 28:
                return createPictureSendView(context, null, true);
            case 10:
                return createOtherFileReceiveView(context, null, true);
            case 11:
                return createOtherFileSendView(context, null, true);
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 24:
            case 25:
            case 26:
            case 54:
            case 55:
                return createSystemMessageView(context, null);
            case 19:
                return createMessageTimeView(context, null);
            case 21:
            case 22:
            case 23:
            case 40:
            case 43:
                return createMessageCallReceiveView(context, null, true);
            case 29:
                return createAddonMsgView(context, null, true);
            case 30:
                return createUnSupportMsgSendView(context, null, true);
            case 31:
                return createUnSupportMsgReceiveView(context, null, true);
            case 32:
                return createGiphySendView(context, null, true);
            case 33:
                return createGiphyReceiveView(context, null, true);
            case 34:
                return createLinkPreviewMsgSendView(context, null, true);
            case 35:
                return createLinkPreviewMsgReceiveView(context, null, true);
            case 36:
                return createNewMsgBelowView(context, null);
            case 37:
                return createCodeSnippetReceiveView(context, null, true);
            case 38:
                return createCodeSnippetSendView(context, null, true);
            case 39:
                return createRemoveHistoryView(context, null);
            case 41:
                return createTemplateView(context, null, true);
            case 42:
                return createLoadingView(context, null);
            case 44:
                return createMessageCallSendView(context, null, true);
            case 45:
                return createFileIntegrationSendView(context, null, true);
            case 46:
                return createFileIntegrationReceiveView(context, null, true);
            case 49:
                return createNewCommentBelowView(context, null);
            case 52:
                return createFileTransferInReceiverDisableView(context, null);
            case 53:
                return createLoadingMoreView(context, null);
            default:
                return null;
        }
    }

    private boolean recallMessage(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        if (CmmTime.getMMNow() - this.messageTime > MSG_DELETEABLE_TIME) {
            Toast.makeText(context, C4558R.string.zm_msg_delete_timeout_19888, 1).show();
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        if (!zoomMessenger.isConnectionGood()) {
            Toast.makeText(context, C4558R.string.zm_msg_disconnected_try_again, 1).show();
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.sessionId);
        if (sessionById == null) {
            return false;
        }
        boolean revokeMessageByXMPPGuid = sessionById.revokeMessageByXMPPGuid(this.messageXMPPId);
        if (!revokeMessageByXMPPGuid) {
            Toast.makeText(context, C4558R.string.zm_mm_lbl_delete_failed_64189, 1).show();
        }
        return revokeMessageByXMPPGuid;
    }

    private boolean unshareFile(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr == null) {
            return false;
        }
        if (TextUtils.isEmpty(this.fileId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return false;
            }
            ZoomFile fileWithMessageID = zoomMessenger.getFileWithMessageID(this.sessionId, this.messageXMPPId);
            if (fileWithMessageID != null) {
                this.fileId = fileWithMessageID.getWebFileID();
            }
        }
        if (TextUtils.isEmpty(this.fileId)) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.sessionId);
        if (!StringUtil.isEmptyOrNull(zoomFileContentMgr.unshareFile(this.fileId, arrayList))) {
            return true;
        }
        if (context instanceof FragmentActivity) {
            ErrorMsgDialog.newInstance(context.getString(C4558R.string.zm_alert_unshare_file_failed), -1).show(((FragmentActivity) context).getSupportFragmentManager(), ErrorMsgDialog.class.getName());
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        if (r1.isStikcerMsg != false) goto L_0x0035;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
        if (r1.isE2E == false) goto L_0x0030;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return recallMessage(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return recallMessage(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return unshareFile(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        return unshareFile(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return recallMessage(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
        if (r1.isE2E == false) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean deleteMessage(android.content.Context r2) {
        /*
            r1 = this;
            int r0 = r1.messageType
            switch(r0) {
                case 0: goto L_0x003f;
                case 1: goto L_0x003f;
                case 2: goto L_0x003a;
                case 3: goto L_0x003a;
                case 4: goto L_0x0027;
                case 5: goto L_0x0027;
                case 6: goto L_0x0025;
                case 7: goto L_0x0025;
                case 8: goto L_0x0025;
                case 9: goto L_0x0025;
                case 10: goto L_0x0017;
                case 11: goto L_0x0017;
                default: goto L_0x0005;
            }
        L_0x0005:
            switch(r0) {
                case 27: goto L_0x0027;
                case 28: goto L_0x0027;
                default: goto L_0x0008;
            }
        L_0x0008:
            switch(r0) {
                case 32: goto L_0x0012;
                case 33: goto L_0x0012;
                case 34: goto L_0x003f;
                case 35: goto L_0x003f;
                default: goto L_0x000b;
            }
        L_0x000b:
            switch(r0) {
                case 37: goto L_0x0017;
                case 38: goto L_0x0017;
                default: goto L_0x000e;
            }
        L_0x000e:
            switch(r0) {
                case 45: goto L_0x0017;
                case 46: goto L_0x0017;
                default: goto L_0x0011;
            }
        L_0x0011:
            goto L_0x0025
        L_0x0012:
            boolean r2 = r1.recallMessage(r2)
            goto L_0x0043
        L_0x0017:
            boolean r0 = r1.isE2E
            if (r0 == 0) goto L_0x0020
            boolean r2 = r1.recallMessage(r2)
            goto L_0x0043
        L_0x0020:
            boolean r2 = r1.unshareFile(r2)
            goto L_0x0043
        L_0x0025:
            r2 = 0
            goto L_0x0043
        L_0x0027:
            boolean r0 = r1.isStikcerMsg
            if (r0 != 0) goto L_0x0035
            boolean r0 = r1.isE2E
            if (r0 == 0) goto L_0x0030
            goto L_0x0035
        L_0x0030:
            boolean r2 = r1.unshareFile(r2)
            goto L_0x0043
        L_0x0035:
            boolean r2 = r1.recallMessage(r2)
            goto L_0x0043
        L_0x003a:
            boolean r2 = r1.recallMessage(r2)
            goto L_0x0043
        L_0x003f:
            boolean r2 = r1.recallMessage(r2)
        L_0x0043:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMMessageItem.deleteMessage(android.content.Context):boolean");
    }

    public boolean isIncomingMessage() {
        switch (this.messageType) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 8:
            case 10:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 27:
            case 29:
            case 31:
            case 33:
            case 35:
            case 37:
            case 40:
            case 41:
            case 43:
            case 46:
            case 52:
                return true;
            default:
                return false;
        }
    }

    public boolean isSendingMessage() {
        switch (this.messageType) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 9:
            case 11:
            case 28:
            case 30:
            case 32:
            case 34:
            case 38:
            case 44:
            case 45:
                return true;
            default:
                return false;
        }
    }

    public boolean isE2EMessagePending() {
        boolean z = false;
        if (!this.isE2E) {
            return false;
        }
        int i = this.messageState;
        if (!(i == 7 || i == 8 || i == 9)) {
            z = true;
        }
        return z;
    }

    public boolean isSystemMsg() {
        return this.isSystemMsg;
    }

    public boolean isDeleteable(String str) {
        boolean z;
        boolean z2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z3 = false;
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return false;
        }
        if (!TextUtils.isEmpty(str)) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null && sessionById.isGroup()) {
                boolean z4 = this.isE2E || zoomMessenger.e2eGetMyOption() == 2;
                ZoomGroup sessionGroup = sessionById.getSessionGroup();
                boolean z5 = sessionGroup != null && sessionGroup.isGroupOperatorable();
                z = sessionGroup != null && sessionGroup.isBroadcast();
                z2 = z5 && !z4;
                if ((!z && z2) || ((z && z2 && StringUtil.isSameString(myself.getJid(), this.fromJid)) || (StringUtil.isSameString(myself.getJid(), this.fromJid) && CmmTime.getMMNow() - this.messageTime <= MSG_DELETEABLE_TIME))) {
                    z3 = true;
                }
                return z3;
            }
        }
        z = false;
        z2 = false;
        z3 = true;
        return z3;
    }

    public boolean isEditable() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return false;
        }
        if (StringUtil.isSameString(myself.getJid(), this.fromJid) && CmmTime.getMMNow() - this.messageTime <= MSG_DELETEABLE_TIME) {
            z = true;
        }
        return z;
    }

    @NonNull
    private static AbsMessageView createGiphySendView(Context context, View view) {
        return createGiphySendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createGiphySendView(Context context, View view, boolean z) {
        MessageGiphySendView messageGiphySendView;
        if (!(view instanceof MessageGiphySendView) || !"GiphyTo".equals(view.getTag())) {
            messageGiphySendView = new MessageGiphySendView(context);
            messageGiphySendView.setTag("GiphyTo");
        } else {
            messageGiphySendView = (MessageGiphySendView) view;
        }
        messageGiphySendView.changeAvatar(z);
        return messageGiphySendView;
    }

    @NonNull
    private static AbsMessageView createGiphyReceiveView(Context context, View view) {
        return createGiphyReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createGiphyReceiveView(Context context, View view, boolean z) {
        MessageGiphyReceiveView messageGiphyReceiveView;
        if (!(view instanceof MessageGiphyReceiveView) || !"GiphyFrom".equals(view.getTag())) {
            messageGiphyReceiveView = new MessageGiphyReceiveView(context);
            messageGiphyReceiveView.setTag("GiphyFrom");
        } else {
            messageGiphyReceiveView = (MessageGiphyReceiveView) view;
        }
        messageGiphyReceiveView.changeAvatar(z);
        return messageGiphyReceiveView;
    }

    @NonNull
    private static AbsMessageView createLinkPreviewMsgSendView(Context context, View view) {
        return createLinkPreviewMsgSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createLinkPreviewMsgSendView(Context context, View view, boolean z) {
        MessageLinkPreviewSendView messageLinkPreviewSendView;
        if (!(view instanceof MessageLinkPreviewSendView) || !"LinkPreviewTo".equals(view.getTag())) {
            messageLinkPreviewSendView = new MessageLinkPreviewSendView(context);
            messageLinkPreviewSendView.setTag("LinkPreviewTo");
        } else {
            messageLinkPreviewSendView = (MessageLinkPreviewSendView) view;
        }
        messageLinkPreviewSendView.changeAvatar(z);
        return messageLinkPreviewSendView;
    }

    @NonNull
    private static AbsMessageView createLinkPreviewMsgReceiveView(Context context, View view) {
        return createLinkPreviewMsgReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createLinkPreviewMsgReceiveView(Context context, View view, boolean z) {
        MessageLinkPreviewReceiveView messageLinkPreviewReceiveView;
        if (!(view instanceof MessageLinkPreviewReceiveView) || !"LinkPreviewFrom".equals(view.getTag())) {
            messageLinkPreviewReceiveView = new MessageLinkPreviewReceiveView(context);
            messageLinkPreviewReceiveView.setTag("LinkPreviewFrom");
        } else {
            messageLinkPreviewReceiveView = (MessageLinkPreviewReceiveView) view;
        }
        messageLinkPreviewReceiveView.changeAvatar(z);
        return messageLinkPreviewReceiveView;
    }

    @NonNull
    private static AbsMessageView createUnSupportMsgSendView(Context context, View view) {
        return createUnSupportMsgSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createUnSupportMsgSendView(Context context, View view, boolean z) {
        MessageUnSupportSendView messageUnSupportSendView;
        if (!(view instanceof MessageUnSupportSendView) || !"UnSupportTo".equals(view.getTag())) {
            messageUnSupportSendView = new MessageUnSupportSendView(context);
            messageUnSupportSendView.setTag("UnSupportTo");
        } else {
            messageUnSupportSendView = (MessageUnSupportSendView) view;
        }
        messageUnSupportSendView.changeAvatar(z);
        return messageUnSupportSendView;
    }

    @NonNull
    private static AbsMessageView createUnSupportMsgReceiveView(Context context, View view) {
        return createUnSupportMsgReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createUnSupportMsgReceiveView(Context context, View view, boolean z) {
        MessageUnSupportReceiveView messageUnSupportReceiveView;
        if (!(view instanceof MessageUnSupportReceiveView) || !"UnSupportFrom".equals(view.getTag())) {
            messageUnSupportReceiveView = new MessageUnSupportReceiveView(context);
            messageUnSupportReceiveView.setTag("UnSupportFrom");
        } else {
            messageUnSupportReceiveView = (MessageUnSupportReceiveView) view;
        }
        messageUnSupportReceiveView.changeAvatar(z);
        return messageUnSupportReceiveView;
    }

    @NonNull
    private static AbsMessageView createMessageTextSendView(Context context, View view) {
        return createMessageTextSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createMessageTextSendView(Context context, View view, boolean z) {
        MessageTextSendView messageTextSendView;
        if (!(view instanceof MessageTextSendView) || !"textTo".equals(view.getTag())) {
            messageTextSendView = new MessageTextSendView(context);
            messageTextSendView.setTag("textTo");
        } else {
            messageTextSendView = (MessageTextSendView) view;
        }
        messageTextSendView.changeAvatar(z);
        return messageTextSendView;
    }

    @NonNull
    private static AbsMessageView createMessageCallReceiveView(Context context, View view) {
        return createMessageCallReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createMessageCallReceiveView(Context context, View view, boolean z) {
        MessageCallReceiveView messageCallReceiveView;
        if (!(view instanceof MessageCallReceiveView) || !"callFrom".equals(view.getTag())) {
            messageCallReceiveView = new MessageCallReceiveView(context);
            messageCallReceiveView.setTag("callFrom");
        } else {
            messageCallReceiveView = (MessageCallReceiveView) view;
        }
        messageCallReceiveView.changeAvatar(z);
        return messageCallReceiveView;
    }

    @NonNull
    private static AbsMessageView createMessageCallSendView(Context context, View view) {
        return createMessageCallSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createMessageCallSendView(Context context, View view, boolean z) {
        MessageCallSendView messageCallSendView;
        if (!(view instanceof MessageCallSendView) || !"callTo".equals(view.getTag())) {
            messageCallSendView = new MessageCallSendView(context);
            messageCallSendView.setTag("callTo");
        } else {
            messageCallSendView = (MessageCallSendView) view;
        }
        messageCallSendView.changeAvatar(z);
        return messageCallSendView;
    }

    @NonNull
    private static AbsMessageView createMessageTextReceiveView(Context context, View view) {
        return createMessageTextReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createMessageTextReceiveView(Context context, View view, boolean z) {
        MessageTextReceiveView messageTextReceiveView;
        if (!(view instanceof MessageTextReceiveView) || !"textFrom".equals(view.getTag())) {
            messageTextReceiveView = new MessageTextReceiveView(context);
            messageTextReceiveView.setTag("textFrom");
        } else {
            messageTextReceiveView = (MessageTextReceiveView) view;
        }
        messageTextReceiveView.changeAvatar(z);
        return messageTextReceiveView;
    }

    @NonNull
    private static AbsMessageView createMessageTimeView(Context context, View view) {
        if ((view instanceof MessageTimeView) && "systemMessageTime".equals(view.getTag())) {
            return (MessageTimeView) view;
        }
        MessageTimeView messageTimeView = new MessageTimeView(context);
        messageTimeView.setTag("systemMessageTime");
        return messageTimeView;
    }

    @NonNull
    private static AbsMessageView createAudioSendView(Context context, View view) {
        return createAudioSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createAudioSendView(Context context, View view, boolean z) {
        MessageAudioSendView messageAudioSendView;
        if (!(view instanceof MessageAudioSendView) || !"audioTo".equals(view.getTag())) {
            messageAudioSendView = new MessageAudioSendView(context);
            messageAudioSendView.setTag("audioTo");
        } else {
            messageAudioSendView = (MessageAudioSendView) view;
        }
        messageAudioSendView.changeAvatar(z);
        return messageAudioSendView;
    }

    @NonNull
    private static AbsMessageView createAudioReceiveView(Context context, View view) {
        return createAudioReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createAudioReceiveView(Context context, View view, boolean z) {
        MessageAudioReceiveView messageAudioReceiveView;
        if (!(view instanceof MessageAudioReceiveView) || !"audioFrom".equals(view.getTag())) {
            messageAudioReceiveView = new MessageAudioReceiveView(context);
            messageAudioReceiveView.setTag("audioFrom");
        } else {
            messageAudioReceiveView = (MessageAudioReceiveView) view;
        }
        messageAudioReceiveView.changeAvatar(z);
        return messageAudioReceiveView;
    }

    @NonNull
    private static AbsMessageView createPictureSendView(Context context, View view) {
        return createPictureSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createPictureSendView(Context context, View view, boolean z) {
        MessagePicSendView messagePicSendView;
        if (!(view instanceof MessagePicSendView) || !"picTo".equals(view.getTag())) {
            messagePicSendView = new MessagePicSendView(context);
            messagePicSendView.setTag("picTo");
        } else {
            messagePicSendView = (MessagePicSendView) view;
        }
        messagePicSendView.changeAvatar(z);
        return messagePicSendView;
    }

    @NonNull
    private static AbsMessageView createPictureReceiveView(Context context, View view) {
        return createPictureReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createPictureReceiveView(Context context, View view, boolean z) {
        MessagePicReceiveView messagePicReceiveView;
        if (!(view instanceof MessagePicReceiveView) || !"picFrom".equals(view.getTag())) {
            messagePicReceiveView = new MessagePicReceiveView(context);
            messagePicReceiveView.setTag("picFrom");
        } else {
            messagePicReceiveView = (MessagePicReceiveView) view;
        }
        messagePicReceiveView.changeAvatar(z);
        return messagePicReceiveView;
    }

    @NonNull
    private static AbsMessageView createSystemMessageView(Context context, View view) {
        if ((view instanceof MessageSystemView) && "systemMessage".equals(view.getTag())) {
            return (MessageSystemView) view;
        }
        MessageSystemView messageSystemView = new MessageSystemView(context);
        messageSystemView.setTag("systemMessage");
        return messageSystemView;
    }

    @NonNull
    private static AbsMessageView createAddonMsgView(Context context, View view) {
        return createAddonMsgView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createAddonMsgView(Context context, View view, boolean z) {
        MessageAddonView messageAddonView;
        if (!(view instanceof MessageAddonView) || !"addonView".equals(view.getTag())) {
            messageAddonView = new MessageAddonView(context);
            messageAddonView.setTag("addonView");
        } else {
            messageAddonView = (MessageAddonView) view;
        }
        messageAddonView.changeAvatar(z);
        return messageAddonView;
    }

    @NonNull
    private static AbsMessageView createTemplateView(Context context, View view) {
        return createTemplateView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createTemplateView(Context context, View view, boolean z) {
        MessageTemplateView messageTemplateView;
        if (!(view instanceof MessageTemplateView) || !"templateView".equals(view.getTag())) {
            messageTemplateView = new MessageTemplateView(context);
            messageTemplateView.setTag("templateView");
        } else {
            messageTemplateView = (MessageTemplateView) view;
        }
        messageTemplateView.changeAvatar(z);
        return messageTemplateView;
    }

    @NonNull
    private static AbsMessageView createOtherFileSendView(Context context, View view) {
        return createOtherFileSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createOtherFileSendView(Context context, View view, boolean z) {
        MessageFileSendView messageFileSendView;
        if (!(view instanceof MessageFileSendView) || !"fileTo".equals(view.getTag())) {
            messageFileSendView = new MessageFileSendView(context);
            messageFileSendView.setTag("fileTo");
        } else {
            messageFileSendView = (MessageFileSendView) view;
        }
        messageFileSendView.changeAvatar(z);
        return messageFileSendView;
    }

    @NonNull
    private static AbsMessageView createFileIntegrationSendView(Context context, View view) {
        return createFileIntegrationSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createFileIntegrationSendView(Context context, View view, boolean z) {
        MessageFileIntegrationSendView messageFileIntegrationSendView;
        if (!(view instanceof MessageFileIntegrationSendView) || !"fileIntegrationTo".equals(view.getTag())) {
            messageFileIntegrationSendView = new MessageFileIntegrationSendView(context);
            messageFileIntegrationSendView.setTag("fileIntegrationTo");
        } else {
            messageFileIntegrationSendView = (MessageFileIntegrationSendView) view;
        }
        messageFileIntegrationSendView.changeAvatar(z);
        return messageFileIntegrationSendView;
    }

    @NonNull
    private static AbsMessageView createFileIntegrationReceiveView(Context context, View view) {
        return createFileIntegrationReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createFileIntegrationReceiveView(Context context, View view, boolean z) {
        MessageFileIntegrationReceiveView messageFileIntegrationReceiveView;
        if (!(view instanceof MessageFileIntegrationReceiveView) || !"fileIntegrationFrom".equals(view.getTag())) {
            messageFileIntegrationReceiveView = new MessageFileIntegrationReceiveView(context);
            messageFileIntegrationReceiveView.setTag("fileIntegrationFrom");
        } else {
            messageFileIntegrationReceiveView = (MessageFileIntegrationReceiveView) view;
        }
        messageFileIntegrationReceiveView.changeAvatar(z);
        return messageFileIntegrationReceiveView;
    }

    @NonNull
    private static AbsMessageView createOtherFileReceiveView(Context context, View view) {
        return createOtherFileReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createOtherFileReceiveView(Context context, View view, boolean z) {
        MessageFileReceiveView messageFileReceiveView;
        if (!(view instanceof MessageFileReceiveView) || !"fileFrom".equals(view.getTag())) {
            messageFileReceiveView = new MessageFileReceiveView(context);
            messageFileReceiveView.setTag("fileFrom");
        } else {
            messageFileReceiveView = (MessageFileReceiveView) view;
        }
        messageFileReceiveView.changeAvatar(z);
        return messageFileReceiveView;
    }

    @NonNull
    private static AbsMessageView createNewMsgBelowView(Context context, View view) {
        if ((view instanceof MessageBelowNewMsgView) && "newMsgBelow".equals(view.getTag())) {
            return (MessageBelowNewMsgView) view;
        }
        MessageBelowNewMsgView messageBelowNewMsgView = new MessageBelowNewMsgView(context);
        messageBelowNewMsgView.setTag("newMsgBelow");
        return messageBelowNewMsgView;
    }

    @NonNull
    private static AbsMessageView createNewCommentBelowView(Context context, View view) {
        if ((view instanceof MessageBelowNewCommentView) && "MessageBelowNewCommentView".equals(view.getTag())) {
            return (MessageBelowNewCommentView) view;
        }
        MessageBelowNewCommentView messageBelowNewCommentView = new MessageBelowNewCommentView(context);
        messageBelowNewCommentView.setTag("MessageBelowNewCommentView");
        return messageBelowNewCommentView;
    }

    @NonNull
    private static AbsMessageView createLoadingMoreView(Context context, View view) {
        if ((view instanceof MessageLoadingMoreView) && "MessageLoadingMoreView".equals(view.getTag())) {
            return (MessageLoadingMoreView) view;
        }
        MessageLoadingMoreView messageLoadingMoreView = new MessageLoadingMoreView(context);
        messageLoadingMoreView.setTag("MessageLoadingMoreView");
        return messageLoadingMoreView;
    }

    @NonNull
    private static AbsMessageView createCodeSnippetReceiveView(Context context, View view) {
        return createCodeSnippetReceiveView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createCodeSnippetReceiveView(Context context, View view, boolean z) {
        MessageCodeSnippetReceiveView messageCodeSnippetReceiveView;
        if (!(view instanceof MessageCodeSnippetReceiveView) || !"codeSnippetFrom".equals(view.getTag())) {
            messageCodeSnippetReceiveView = new MessageCodeSnippetReceiveView(context);
            messageCodeSnippetReceiveView.setTag("codeSnippetFrom");
        } else {
            messageCodeSnippetReceiveView = (MessageCodeSnippetReceiveView) view;
        }
        messageCodeSnippetReceiveView.changeAvatar(z);
        return messageCodeSnippetReceiveView;
    }

    @NonNull
    private static AbsMessageView createCodeSnippetSendView(Context context, View view) {
        return createCodeSnippetSendView(context, view, false);
    }

    @NonNull
    private static AbsMessageView createCodeSnippetSendView(Context context, View view, boolean z) {
        MessageCodeSnippetSendView messageCodeSnippetSendView;
        if (!(view instanceof MessageCodeSnippetSendView) || !"codeSnippetTo".equals(view.getTag())) {
            messageCodeSnippetSendView = new MessageCodeSnippetSendView(context);
            messageCodeSnippetSendView.setTag("codeSnippetTo");
        } else {
            messageCodeSnippetSendView = (MessageCodeSnippetSendView) view;
        }
        messageCodeSnippetSendView.changeAvatar(z);
        return messageCodeSnippetSendView;
    }

    @NonNull
    private static AbsMessageView createRemoveHistoryView(Context context, View view) {
        if ((view instanceof MessageRemoveHistoryView) && "removeHistory".equals(view.getTag())) {
            return (MessageRemoveHistoryView) view;
        }
        MessageRemoveHistoryView messageRemoveHistoryView = new MessageRemoveHistoryView(context);
        messageRemoveHistoryView.setTag("removeHistory");
        return messageRemoveHistoryView;
    }

    @NonNull
    private static AbsMessageView createLoadingView(Context context, View view) {
        if ((view instanceof MessageLodingView) && "LodingView".equals(view.getTag())) {
            return (MessageLodingView) view;
        }
        MessageLodingView messageLodingView = new MessageLodingView(context);
        messageLodingView.setTag("LodingView");
        return messageLodingView;
    }

    @NonNull
    private static AbsMessageView createCommentSplitView(Context context, View view) {
        if ((view instanceof CommentSplitView) && "CommentSplitView".equals(view.getTag())) {
            return (CommentSplitView) view;
        }
        CommentSplitView commentSplitView = new CommentSplitView(context);
        commentSplitView.setTag("CommentSplitView");
        return commentSplitView;
    }

    @NonNull
    private static AbsMessageView createCommentView(Context context, View view) {
        if ((view instanceof MessageTextReceiveView) && "createCommentView".equals(view.getTag())) {
            return (MessageTextReceiveView) view;
        }
        MessageTextReceiveView messageTextReceiveView = new MessageTextReceiveView(context);
        messageTextReceiveView.setTag("createCommentView");
        return messageTextReceiveView;
    }

    @NonNull
    private static AbsMessageView createThreadDeletedView(Context context, View view) {
        if ((view instanceof MessageThreadDeletedView) && "MessageThreadDeletedView".equals(view.getTag())) {
            return (MessageThreadDeletedView) view;
        }
        MessageThreadDeletedView messageThreadDeletedView = new MessageThreadDeletedView(context);
        messageThreadDeletedView.setTag("MessageThreadDeletedView");
        return messageThreadDeletedView;
    }

    @NonNull
    private static AbsMessageView createThreadNotExitView(Context context, View view) {
        if ((view instanceof MessageThreadNotExistView) && "MessageThreadNotExistView".equals(view.getTag())) {
            return (MessageThreadNotExistView) view;
        }
        MessageThreadNotExistView messageThreadNotExistView = new MessageThreadNotExistView(context);
        messageThreadNotExistView.setTag("MessageThreadNotExistView");
        return messageThreadNotExistView;
    }

    @NonNull
    private static AbsMessageView createFileTransferInReceiverDisableView(Context context, View view) {
        if ((view instanceof MMFileTransferInReceiverDisableView) && "FTInReceiverDisableView".equals(view.getTag())) {
            return (MMFileTransferInReceiverDisableView) view;
        }
        MMFileTransferInReceiverDisableView mMFileTransferInReceiverDisableView = new MMFileTransferInReceiverDisableView(context);
        mMFileTransferInReceiverDisableView.setTag("FTInReceiverDisableView");
        return mMFileTransferInReceiverDisableView;
    }
}
