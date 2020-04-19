package com.zipow.videobox.view.p014mm;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.MMCommentActivity.ThreadUnreadInfo;
import com.zipow.videobox.dialog.InformationBarriesDialog;
import com.zipow.videobox.ptapp.IMProtos.CommentDataResult;
import com.zipow.videobox.ptapp.IMProtos.EmojiCountMap;
import com.zipow.videobox.ptapp.IMProtos.LocalStorageTimeInterval;
import com.zipow.videobox.ptapp.IMProtos.ThreadDataResult;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.LinkPreviewHelper;
import com.zipow.videobox.util.MMMessageHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMContentMessageItem.MMContentMessageAnchorInfo;
import com.zipow.videobox.view.p014mm.MMThreadsRecyclerView.ThreadsUICallBack;
import com.zipow.videobox.view.p014mm.message.MessageTextView;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMCommentsRecyclerView */
public class MMCommentsRecyclerView extends RecyclerView {
    private static final int PAGE_SIZE = 20;
    private static final String TAG = "MMCommentsRecyclerView";
    /* access modifiers changed from: private */
    public MMCommentsAdapter mAdapter;
    private MMContentMessageAnchorInfo mAnchorMessageInfo;
    private CommentReqInfo mCommentReq = new CommentReqInfo();
    private int mFirstSyncRetryTimes = 1;
    private MyHandler mHandler = new MyHandler(this);
    private boolean mHasNewMessageDuringPaused;
    private CommentDataResult mHistoryCommentDataResult;
    private IMAddrBookItem mIMAddrBookItem;
    private boolean mIsFirstPageDirty = false;
    private boolean mIsFirstPageLoading = false;
    private boolean mIsGroup = false;
    private boolean mIsLocalMsgDirty;
    private boolean mIsShow;
    /* access modifiers changed from: private */
    public LinearLayoutManager mLinearLayoutManager;
    private HashMap<String, String> mLinkPreviewReqIds = new HashMap<>();
    private CommentDataResult mPendingRecentData = null;
    private CommentDataResult mRecentCommentDataResult;
    private Runnable mRunnableNotifyDataSetChanged = new Runnable() {
        public void run() {
            if (MMCommentsRecyclerView.this.mAdapter != null) {
                MMCommentsRecyclerView.this.mAdapter.notifyDataSetChanged();
            }
        }
    };
    private String mSessionId;
    private Set<Long> mStarredMsgs;
    private Set<String> mSyncMessageIDs = new HashSet();
    private String mThreadId;
    private MMMessageItem mThreadItem;
    private int mThreadLoadCount = 0;
    private ThreadDataResult mThreadResult = null;
    private int mThreadSortType;
    private long mThreadSvr;
    /* access modifiers changed from: private */
    public ThreadsUICallBack mUICallBack;
    private ThreadUnreadInfo mUnreadInfo;
    private String myJid;

    /* renamed from: com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo */
    private static class CommentReqInfo {
        private SparseArray<ReqInfo> reqInfos = new SparseArray<>();

        /* renamed from: com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo$ReqInfo */
        static class ReqInfo {
            String anchorMsgId;
            int callbackCount;
            CommentDataResult dataResult;

            ReqInfo() {
            }
        }

        CommentReqInfo() {
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            this.reqInfos.clear();
        }

        /* access modifiers changed from: 0000 */
        public void updateReqInfo(CommentDataResult commentDataResult) {
            updateReqInfo(commentDataResult, null);
        }

        /* access modifiers changed from: 0000 */
        public void updateReqInfo(CommentDataResult commentDataResult, String str) {
            if (commentDataResult != null && commentDataResult.getDir() != 0) {
                ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(commentDataResult.getDir());
                if (reqInfo == null) {
                    reqInfo = new ReqInfo();
                }
                this.reqInfos.put(commentDataResult.getDir(), reqInfo);
                reqInfo.callbackCount = 0;
                reqInfo.dataResult = commentDataResult;
                reqInfo.anchorMsgId = str;
                if (!TextUtils.isEmpty(commentDataResult.getDbReqId())) {
                    reqInfo.callbackCount++;
                }
                if (!TextUtils.isEmpty(commentDataResult.getXmsReqId())) {
                    reqInfo.callbackCount++;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean isLoading(int i) {
            ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(i);
            return reqInfo != null && reqInfo.callbackCount > 0;
        }

        /* access modifiers changed from: 0000 */
        public String getAnchorMsgId(CommentDataResult commentDataResult) {
            if (commentDataResult == null) {
                return null;
            }
            ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(commentDataResult.getDir());
            if (reqInfo == null) {
                return null;
            }
            return reqInfo.anchorMsgId;
        }

        /* access modifiers changed from: 0000 */
        public CommentDataResult getLastResult(CommentDataResult commentDataResult) {
            if (commentDataResult == null) {
                return null;
            }
            ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(commentDataResult.getDir());
            if (reqInfo == null) {
                return null;
            }
            if ((TextUtils.isEmpty(commentDataResult.getDbReqId()) || !TextUtils.equals(commentDataResult.getDbReqId(), reqInfo.dataResult.getDbReqId())) && (TextUtils.isEmpty(commentDataResult.getXmsReqId()) || !TextUtils.equals(commentDataResult.getXmsReqId(), reqInfo.dataResult.getXmsReqId()))) {
                return null;
            }
            return reqInfo.dataResult;
        }

        /* access modifiers changed from: 0000 */
        public boolean match(CommentDataResult commentDataResult) {
            if (commentDataResult == null) {
                return false;
            }
            ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(commentDataResult.getDir());
            if (reqInfo == null) {
                return false;
            }
            if ((TextUtils.isEmpty(commentDataResult.getDbReqId()) || !TextUtils.equals(commentDataResult.getDbReqId(), reqInfo.dataResult.getDbReqId())) && (TextUtils.isEmpty(commentDataResult.getXmsReqId()) || !TextUtils.equals(commentDataResult.getXmsReqId(), reqInfo.dataResult.getXmsReqId()))) {
                return false;
            }
            reqInfo.callbackCount--;
            return true;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMCommentsRecyclerView$MyHandler */
    private static class MyHandler extends Handler {
        static final int MSG_SCROLL_BOTTOM = 1;
        private WeakReference<MMCommentsRecyclerView> mView;

        MyHandler(@NonNull MMCommentsRecyclerView mMCommentsRecyclerView) {
            this.mView = new WeakReference<>(mMCommentsRecyclerView);
        }

        public void handleMessage(@NonNull Message message) {
            boolean z = true;
            if (message.what == 1) {
                if (message.arg1 == 0) {
                    z = false;
                }
                scrollBottom(z);
            }
        }

        private void scrollBottom(boolean z) {
            MMCommentsRecyclerView mMCommentsRecyclerView = (MMCommentsRecyclerView) this.mView.get();
            if (mMCommentsRecyclerView != null) {
                int itemCount = mMCommentsRecyclerView.mAdapter.getItemCount() - 1;
                if (z) {
                    mMCommentsRecyclerView.scrollToPosition(itemCount);
                } else if (itemCount - mMCommentsRecyclerView.mLinearLayoutManager.findLastVisibleItemPosition() < 5) {
                    mMCommentsRecyclerView.scrollToPosition(itemCount);
                }
            }
        }
    }

    public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
    }

    public MMCommentsRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public MMCommentsRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMCommentsRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    public int getMsgDirection(String str) {
        if (this.mLinearLayoutManager.findFirstVisibleItemPosition() == -1) {
            return 3;
        }
        int findItemInAdapter = this.mAdapter.findItemInAdapter(str);
        if (findItemInAdapter == -1) {
            return -1;
        }
        if (findItemInAdapter < this.mLinearLayoutManager.findFirstVisibleItemPosition()) {
            return 1;
        }
        return findItemInAdapter > this.mLinearLayoutManager.findLastVisibleItemPosition() ? 2 : 0;
    }

    public void setUnreadInfo(ThreadUnreadInfo threadUnreadInfo) {
        this.mUnreadInfo = threadUnreadInfo;
        ThreadUnreadInfo threadUnreadInfo2 = this.mUnreadInfo;
        if (threadUnreadInfo2 != null) {
            this.mAdapter.setUnreadTime(threadUnreadInfo2.readTime);
        }
    }

    public int getMsgDirection(long j) {
        if (this.mLinearLayoutManager.findFirstVisibleItemPosition() == -1) {
            return 3;
        }
        int findItemInAdapter = this.mAdapter.findItemInAdapter(j);
        if (findItemInAdapter == -1) {
            return -1;
        }
        if (findItemInAdapter < this.mLinearLayoutManager.findFirstVisibleItemPosition()) {
            return 1;
        }
        return findItemInAdapter > this.mLinearLayoutManager.findLastVisibleItemPosition() ? 2 : 0;
    }

    public boolean isLayoutReady() {
        return this.mLinearLayoutManager.findFirstVisibleItemPosition() != -1;
    }

    public boolean isDataEmpty() {
        return this.mAdapter.isEmpty();
    }

    public boolean needJumpToStart() {
        boolean z = true;
        if (!this.mIsGroup) {
            return true;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(this.mSessionId);
        if (groupById == null) {
            return false;
        }
        if (!groupById.isRoom()) {
            return true;
        }
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr == null) {
            return true;
        }
        if (notificationSettingMgr.getHintLineForChannels() != 1) {
            z = false;
        }
        return z;
    }

    public boolean isLoading(int i) {
        return this.mCommentReq.isLoading(i);
    }

    public boolean isLoading() {
        return this.mCommentReq.isLoading(1) || this.mCommentReq.isLoading(2) || this.mThreadResult != null;
    }

    public void updateMessage(String str) {
        updateMessage(str, true);
    }

    public void updateMessage(String str, boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str);
                if (messageByXMPPGuid != null) {
                    updateMessage(messageByXMPPGuid, z);
                    scrollToBottom(false);
                }
            }
        }
    }

    private void checkLinkPreviewAndOther(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            if (!CollectionsUtil.isListEmpty(mMMessageItem.linkPreviewMetaInfos)) {
                List<String> downloadLinkPreview = LinkPreviewHelper.downloadLinkPreview(mMMessageItem);
                if (!CollectionsUtil.isListEmpty(downloadLinkPreview)) {
                    for (String put : downloadLinkPreview) {
                        this.mLinkPreviewReqIds.put(put, mMMessageItem.messageId);
                    }
                }
            }
            CommonEmojiHelper instance = CommonEmojiHelper.getInstance();
            if (!instance.isEmojiInstalled()) {
                boolean z = false;
                if (!mMMessageItem.isE2E) {
                    z = instance.containCommonEmoji(mMMessageItem.message);
                } else if (!mMMessageItem.isMessageE2EWaitDecrypt()) {
                    z = instance.containCommonEmoji(mMMessageItem.message);
                }
                if (z) {
                    ThreadsUICallBack threadsUICallBack = this.mUICallBack;
                    if (threadsUICallBack != null) {
                        threadsUICallBack.onUnSupportEmojiReceived(mMMessageItem.fromJid);
                    }
                }
            }
        }
    }

    public void OnUpdateLinkCrawl(int i, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            String str2 = (String) this.mLinkPreviewReqIds.remove(str);
            if (!StringUtil.isEmptyOrNull(str2) && i == 0) {
                updateMessage(str2);
            }
        }
    }

    public void onConnectReturn(int i) {
        if (this.mAnchorMessageInfo == null) {
            if (i != 0) {
                this.mSyncMessageIDs.clear();
                return;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    CommentDataResult commentData = threadDataProvider.getCommentData(this.mSessionId, 20, this.mThreadId, this.mThreadSvr, "", 1, false);
                    if (commentData != null) {
                        if (commentData.getCurrState() != 1) {
                            this.mIsFirstPageLoading = true;
                        }
                        this.mCommentReq.clear();
                        this.mCommentReq.updateReqInfo(commentData, "0");
                        this.mAdapter.clearDatas();
                        updateComments(commentData, true);
                        scrollToBottom(true);
                        if (this.mThreadSortType == 1 && this.mThreadItem != null) {
                            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                            if (sessionById != null) {
                                sessionById.cleanUnreadCommentsForThread(this.mThreadItem.serverSideTime);
                            }
                        }
                    }
                }
            }
        }
    }

    public void NotifyOutdatedHistoryRemoved(long j) {
        this.mAdapter.removeHistory(j);
        insertTimedChatMsg();
    }

    public void insertTimedChatMsg() {
        if (!UIMgr.isMyNotes(this.mSessionId)) {
            LocalStorageTimeInterval localStorageTimeInterval = PTApp.getInstance().getZoomMessenger().getLocalStorageTimeInterval();
            if (localStorageTimeInterval != null) {
                String timeInterval = MMMessageHelper.timeInterval(getContext(), localStorageTimeInterval.getYear(), localStorageTimeInterval.getMonth(), localStorageTimeInterval.getDay());
                MMMessageItem mMMessageItem = new MMMessageItem();
                mMMessageItem.messageId = MMMessageItem.TIMED_CHAT_MSG_ID;
                mMMessageItem.messageType = 39;
                mMMessageItem.message = getContext().getResources().getString(C4558R.string.zm_mm_msg_remove_history_message_33479, new Object[]{timeInterval});
                this.mAdapter.setTimeChatTimeItem(mMMessageItem);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public boolean isLocalDataDirty() {
        return this.mIsLocalMsgDirty;
    }

    public MMMessageItem updateMessage(ZoomMessage zoomMessage) {
        return updateMessage(zoomMessage, true);
    }

    public MMMessageItem updateMessage(ZoomMessage zoomMessage, boolean z) {
        if (zoomMessage == null) {
            return null;
        }
        if (this.mAdapter.getItemByMessageId(zoomMessage.getMessageID()) == null && !TextUtils.equals(zoomMessage.getMessageID(), this.mThreadId) && z) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || zoomMessenger.getThreadDataProvider() == null) {
            return null;
        }
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        ZoomMessage zoomMessage2 = zoomMessage;
        MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(zoomMessage2, this.mSessionId, zoomMessenger, this.mIsGroup, StringUtil.isSameString(zoomMessage.getSenderID(), this.myJid), getContext(), this.mIMAddrBookItem, zoomFileContentMgr);
        if (initWithZoomMessage == null) {
            return null;
        }
        updateMessageStarStatus(initWithZoomMessage);
        this.mAdapter.updateMessage(initWithZoomMessage, z);
        checkLinkPreviewAndOther(initWithZoomMessage);
        this.mAdapter.notifyDataSetChanged();
        return initWithZoomMessage;
    }

    public void updateMessageStarStatus(@NonNull MMMessageItem mMMessageItem) {
        Set<Long> set = this.mStarredMsgs;
        if (set != null) {
            Iterator it = set.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (mMMessageItem.serverSideTime == ((Long) it.next()).longValue()) {
                    mMMessageItem.isMessgeStarred = true;
                    break;
                }
            }
        }
    }

    public boolean isInAutoScrollBottomMode() {
        return this.mLinearLayoutManager.getItemCount() + -5 < this.mLinearLayoutManager.findLastVisibleItemPosition();
    }

    public void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z) {
        if (list != null) {
            this.mSyncMessageIDs.removeAll(list);
        }
        this.mAdapter.notifyDataSetChanged();
        if (this.mAdapter.isEmpty() && !CollectionsUtil.isCollectionEmpty(list) && !isLoading()) {
            loadComments(false, true);
        }
    }

    public void OnMessageEmojiInfoUpdated(String str, String str2) {
        MMMessageItem itemByMessageId = this.mAdapter.getItemByMessageId(str2);
        if (itemByMessageId != null) {
            updateMessageEmojiCountInfo(itemByMessageId, true);
        }
    }

    public void OnEmojiCountInfoLoadedFromDB(String str) {
        this.mAdapter.notifyDataSetChanged();
    }

    public void updateMessageEmojiCountInfo(MMMessageItem mMMessageItem, boolean z) {
        if (mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    if (threadDataProvider.isMessageEmojiCountInfoDirty(mMMessageItem.sessionId, mMMessageItem.messageXMPPId)) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(mMMessageItem.messageXMPPId);
                        threadDataProvider.syncMessageEmojiCountInfo(mMMessageItem.sessionId, arrayList);
                        return;
                    }
                    EmojiCountMap messageEmojiCountInfo = threadDataProvider.getMessageEmojiCountInfo(z, mMMessageItem.sessionId, mMMessageItem.messageXMPPId);
                    if (messageEmojiCountInfo != null) {
                        mMMessageItem.updateCommentsEmojiCountItems(messageEmojiCountInfo);
                        notifyDataSetChanged(false);
                    }
                }
            }
        }
    }

    public void notifyDataSetChanged(boolean z) {
        if (z) {
            this.mHandler.removeCallbacks(this.mRunnableNotifyDataSetChanged);
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        this.mHandler.removeCallbacks(this.mRunnableNotifyDataSetChanged);
        this.mHandler.postDelayed(this.mRunnableNotifyDataSetChanged, 500);
    }

    public void notifyDataSetChanged() {
        MMCommentsAdapter mMCommentsAdapter = this.mAdapter;
        if (mMCommentsAdapter != null) {
            mMCommentsAdapter.notifyDataSetChanged();
        }
    }

    public boolean deleteMessage(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (this.mAdapter.removeItem(str) != null) {
            notifyDataSetChanged();
            return true;
        } else if (!checkThreadDeleted(str)) {
            return false;
        } else {
            notifyDataSetChanged();
            return true;
        }
    }

    private boolean checkThreadDeleted(String str) {
        if (!TextUtils.equals(str, this.mThreadId)) {
            return false;
        }
        this.mAdapter.setThreadDeleted();
        return true;
    }

    public void onStartToDownloadFileForMessage(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageById = sessionById.getMessageById(mMMessageItem.messageId);
                    if (messageById != null) {
                        MMMessageItem updateMessage = updateMessage(messageById);
                        if (updateMessage != null) {
                            updateMessage.isDownloading = true;
                        }
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void onRecallMessage(boolean z, ZoomMessage zoomMessage, String str) {
        if (z) {
            LinkPreviewHelper.deleteLinkPreview(str);
            if (this.mAdapter.removeItem(str) == null && !checkThreadDeleted(str)) {
                return;
            }
            if (this.mIsShow) {
                this.mAdapter.notifyDataSetChanged();
                if (zoomMessage != null) {
                    scrollToBottom(false);
                }
            } else {
                this.mHasNewMessageDuringPaused = true;
            }
        }
    }

    public void Indicate_FileActionStatus(int i, String str, String str2, String str3, String str4, String str5) {
        if (StringUtil.isSameString(str4, this.mSessionId)) {
            if (i != 1 && i != 2) {
                if (i == 3) {
                    this.mAdapter.notifyDataSetChanged();
                }
            } else if (PTApp.getInstance().getZoomFileContentMgr() != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        List<MMMessageItem> itemsByFileId = this.mAdapter.getItemsByFileId(str);
                        if (!CollectionsUtil.isCollectionEmpty(itemsByFileId)) {
                            for (MMMessageItem mMMessageItem : itemsByFileId) {
                                if (!mMMessageItem.isThread || (CollectionsUtil.isCollectionEmpty(mMMessageItem.getComments()) && mMMessageItem.commentsCount <= 0)) {
                                    this.mAdapter.removeItem(mMMessageItem.messageId);
                                } else {
                                    mMMessageItem.isDeletedThread = true;
                                    mMMessageItem.messageType = 48;
                                }
                            }
                        }
                        if (!StringUtil.isEmptyOrNull(str5)) {
                            ZoomMessage messageById = sessionById.getMessageById(str5);
                            if (messageById != null) {
                                addNewMessage(messageById);
                            }
                        }
                        this.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void Indicate_FileMessageDeleted(String str, String str2) {
        if (this.mAdapter.removeItem(str2) != null && checkThreadDeleted(str2)) {
            if (this.mIsShow) {
                this.mAdapter.notifyDataSetChanged();
            } else {
                this.mHasNewMessageDuringPaused = true;
            }
        }
    }

    public void updateVisibleMessageState() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    if (childAt instanceof MessageTextView) {
                        MMMessageItem messageItem = ((AbsMessageView) childAt).getMessageItem();
                        if (messageItem != null) {
                            updateMessage(sessionById.getMessageByXMPPGuid(messageItem.messageXMPPId), true);
                        }
                    }
                }
            }
        }
    }

    public void setUICallBack(ThreadsUICallBack threadsUICallBack) {
        this.mAdapter.setUICallBack(threadsUICallBack);
        this.mUICallBack = threadsUICallBack;
    }

    public void setIsShow(boolean z) {
        this.mIsShow = z;
    }

    private void init() {
        setItemAnimator(null);
        this.mAdapter = new MMCommentsAdapter(getContext());
        this.mLinearLayoutManager = new LinearLayoutManager(getContext()) {
            private boolean isLayoutReady;

            public LayoutParams generateDefaultLayoutParams() {
                return new LayoutParams(-1, -2);
            }

            public void onLayoutCompleted(State state) {
                super.onLayoutCompleted(state);
                if (!this.isLayoutReady) {
                    this.isLayoutReady = true;
                    if (MMCommentsRecyclerView.this.mUICallBack != null) {
                        MMCommentsRecyclerView.this.mUICallBack.onLayoutCompleted();
                    }
                }
            }
        };
        setLayoutManager(this.mLinearLayoutManager);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    this.mThreadSortType = threadDataProvider.getThreadSortType();
                    this.myJid = myself.getJid();
                    addOnScrollListener(new OnScrollListener() {
                        public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                        }

                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                            MMCommentsRecyclerView.this.onRecyclerViewScrollStateChanged(i);
                        }
                    });
                }
            }
        }
    }

    public void e2eMessageStateUpdate(String str, String str2, int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageById = sessionById.getMessageById(str2);
                if (messageById != null) {
                    MMMessageItem itemByMessageId = this.mAdapter.getItemByMessageId(str2);
                    if (itemByMessageId != null && itemByMessageId.isE2E) {
                        updateMessage(messageById);
                    }
                    if (i == 7) {
                        sessionById.checkAutoDownloadForMessage(str2);
                    }
                }
            }
        }
    }

    public boolean hasVisiableMessageDecryptedTimeout() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof AbsMessageView) {
                MMMessageItem messageItem = ((AbsMessageView) childAt).getMessageItem();
                if (messageItem != null && messageItem.isMessageE2EWaitDecrypt()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getLocalCommentsCount() {
        return this.mAdapter.getLocalCommentsCount();
    }

    public boolean tryDecryptVisiableE2EMesssage() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof AbsMessageView) {
                MMMessageItem messageItem = ((AbsMessageView) childAt).getMessageItem();
                if (messageItem != null && messageItem.isMessageE2EWaitDecrypt()) {
                    zoomMessenger.e2eTryDecodeMessage(this.mSessionId, messageItem.messageId);
                    z = true;
                }
            }
        }
        return z;
    }

    public void checkAndDecodeE2EMsg(ZoomMessenger zoomMessenger, MMMessageItem mMMessageItem) {
        if (zoomMessenger != null && mMMessageItem != null) {
            ArrayList<MMMessageItem> arrayList = new ArrayList<>();
            arrayList.add(mMMessageItem);
            if (mMMessageItem.isThread) {
                arrayList.addAll(mMMessageItem.getComments());
            }
            for (MMMessageItem mMMessageItem2 : arrayList) {
                if (mMMessageItem2.isE2E && mMMessageItem2.isMessageE2EWaitDecrypt()) {
                    int e2eTryDecodeMessage = zoomMessenger.e2eTryDecodeMessage(this.mSessionId, mMMessageItem2.messageId);
                    mMMessageItem2.messageState = 3;
                    if (e2eTryDecodeMessage == 0) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                        if (sessionById != null) {
                            ZoomMessage messageById = sessionById.getMessageById(mMMessageItem2.messageId);
                            if (messageById != null) {
                                mMMessageItem2.message = messageById.getBody();
                                mMMessageItem2.messageState = messageById.getMessageState();
                                sessionById.checkAutoDownloadForMessage(mMMessageItem2.messageId);
                            }
                        }
                    } else if (e2eTryDecodeMessage == 37) {
                        mMMessageItem2.message = getContext().getResources().getString(C4558R.string.zm_msg_e2e_message_decrypting);
                        if (mMMessageItem2.isOutMsg) {
                            mMMessageItem2.messageType = 1;
                        } else {
                            mMMessageItem2.messageType = 0;
                        }
                    }
                }
            }
        }
    }

    public boolean isAllVisiableMessageDecrypted() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
        if (sessionById == null) {
            return false;
        }
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= getChildCount()) {
                return true;
            }
            View childAt = getChildAt(i);
            if (childAt instanceof AbsMessageView) {
                MMMessageItem messageItem = ((AbsMessageView) childAt).getMessageItem();
                if (messageItem != null && messageItem.isE2E) {
                    ZoomMessage messageById = sessionById.getMessageById(messageItem.messageId);
                    if (messageById == null) {
                        return false;
                    }
                    int messageState = messageById.getMessageState();
                    if (!(messageState == 7 || messageState == 4 || messageState == 1 || messageState == 2)) {
                        z = false;
                    }
                    if (!z) {
                        return false;
                    }
                }
            }
            i++;
        }
    }

    public boolean isMsgShow(String str) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (linearLayoutManager == null) {
            return false;
        }
        int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        for (int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
            MMMessageItem itemByPosition = this.mAdapter.getItemByPosition(findFirstVisibleItemPosition);
            if (itemByPosition != null && TextUtils.equals(itemByPosition.messageId, str)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void onRecyclerViewScrollStateChanged(int i) {
        if (i == 0) {
            if (!this.mIsLocalMsgDirty && this.mAnchorMessageInfo == null) {
                this.mAdapter.removeLoadingView();
            } else if (this.mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == this.mAdapter.getItemCount() - 1) {
                loadMoreComments(2);
                if (isLoading(2)) {
                    this.mAdapter.addFootLoadingView();
                    this.mAdapter.notifyDataSetChanged();
                } else {
                    this.mAdapter.removeLoadingView();
                }
            }
            syncMessageEmojiCountInfo();
        } else if (i == 2) {
            UIUtil.closeSoftKeyboard(getContext(), this);
        }
    }

    public void setThreadInfo(@NonNull String str, @NonNull MMMessageItem mMMessageItem, boolean z, String str2, long j) {
        this.mSessionId = str;
        this.mIsGroup = z;
        this.mThreadId = str2;
        this.mThreadSvr = j;
        if (!z) {
            this.mIMAddrBookItem = ZMBuddySyncInstance.getInsatance().getBuddyByJid(str, true);
        }
        this.mThreadItem = mMMessageItem;
        this.mAdapter.setSessionInfo(str, this.mIMAddrBookItem, z, str2);
        this.mAdapter.setThread(mMMessageItem);
        checkAndDecodeE2EMsg(PTApp.getInstance().getZoomMessenger(), mMMessageItem);
        setAdapter(this.mAdapter);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null) {
                sessionById.checkAutoDownloadForMessage(str2);
            }
        }
    }

    public MMMessageItem findMessageItemByStamp(long j) {
        return this.mAdapter.findMessageItemInAdapter(j);
    }

    public MMMessageItem getItemByMessageId(String str) {
        return this.mAdapter.getItemByMessageId(str);
    }

    public void setAnchorMessageItem(MMContentMessageAnchorInfo mMContentMessageAnchorInfo) {
        this.mAnchorMessageInfo = mMContentMessageAnchorInfo;
        setIsLocalMsgDirty(true);
    }

    private void setIsLocalMsgDirty(boolean z) {
        this.mIsLocalMsgDirty = z;
    }

    public void FT_OnSent(String str, String str2, int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageById = sessionById.getMessageById(str2);
                if (messageById != null) {
                    if (i == 4305) {
                        String str3 = "";
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(messageById.getReceiverID());
                        if (buddyWithJID != null) {
                            str3 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                        }
                        InformationBarriesDialog.show(getContext(), String.format(getContext().getString(C4558R.string.zm_mm_information_barries_dialog_chat_msg_115072), new Object[]{str3}));
                    }
                    updateMessage(messageById);
                }
            }
        }
    }

    public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        List<MMMessageItem> itemsByFileId = this.mAdapter.getItemsByFileId(str2);
        if (!CollectionsUtil.isListEmpty(itemsByFileId)) {
            FileTransferInfo fileTransferInfo = new FileTransferInfo();
            fileTransferInfo.bitsPerSecond = (long) i3;
            fileTransferInfo.percentage = i;
            fileTransferInfo.transferredSize = (long) i2;
            fileTransferInfo.state = 10;
            for (MMMessageItem mMMessageItem : itemsByFileId) {
                mMMessageItem.transferInfo = fileTransferInfo;
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void Indicate_FileShared(String str, String str2, String str3, String str4, String str5, int i) {
        if (StringUtil.isSameString(str4, this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str4);
                if (sessionById != null) {
                    ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str5);
                    if (messageByXMPPGuid != null) {
                        MMMessageItem addNewMessage = addNewMessage(messageByXMPPGuid);
                        if (addNewMessage != null) {
                            addNewMessage.messageState = 2;
                            this.mAdapter.notifyDataSetChanged();
                            scrollToBottom(false);
                        }
                    }
                }
            }
        }
    }

    public void Indicate_FileForwarded(String str, String str2, String str3, String str4, int i) {
        if (StringUtil.isSameString(str3, this.mSessionId) && i == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str3);
                if (sessionById != null) {
                    ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str4);
                    if (messageByXMPPGuid != null) {
                        if (i == 0) {
                            MMMessageItem addNewMessage = addNewMessage(messageByXMPPGuid);
                            if (addNewMessage != null) {
                                addNewMessage.messageState = 2;
                                this.mAdapter.notifyDataSetChanged();
                                scrollToBottom(false);
                            }
                        } else if (i == 4305) {
                            String str5 = "";
                            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(messageByXMPPGuid.getReceiverID());
                            if (buddyWithJID != null) {
                                str5 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                            }
                            InformationBarriesDialog.show(getContext(), String.format(getContext().getString(C4558R.string.zm_mm_information_barries_dialog_chat_msg_115072), new Object[]{str5}));
                        }
                    }
                }
            }
        }
    }

    public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    ZoomMessage messageById = sessionById.getMessageById(str2);
                    if (messageById != null) {
                        updateMessage(messageById);
                    }
                }
            }
        }
    }

    public void FT_UploadFileInChatTimeOut(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    ZoomMessage messageById = sessionById.getMessageById(str2);
                    if (messageById != null) {
                        updateMessage(messageById);
                    }
                }
            }
        }
    }

    public void Indicate_FileDownloaded(String str, String str2, int i) {
        if (i == 0) {
            List<MMMessageItem> itemsByFileId = this.mAdapter.getItemsByFileId(str2);
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null && itemsByFileId != null) {
                for (MMMessageItem mMMessageItem : itemsByFileId) {
                    ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str2);
                    if (fileWithWebFileID != null) {
                        mMMessageItem.isFileDownloaded = true;
                        mMMessageItem.localFilePath = fileWithWebFileID.getLocalPath();
                        mMMessageItem.isPreviewDownloadFailed = false;
                        mMMessageItem.transferInfo = new FileTransferInfo();
                        mMMessageItem.transferInfo.state = 13;
                        zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                        this.mAdapter.notifyDataSetChanged();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void FT_OnProgress(String str, String str2, int i, long j, long j2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageById = sessionById.getMessageById(str2);
                if (messageById != null) {
                    MMMessageItem updateMessage = updateMessage(messageById);
                    if (updateMessage != null) {
                        updateMessage.isDownloading = i < 100;
                        updateMessage.fileRatio = i;
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }

    public void FT_OnPaused(String str, String str2) {
        updateMessageUI(str, str2, 0);
    }

    public void FT_OnResumed(String str, String str2, int i) {
        updateMessageUI(str, str2, i);
    }

    public void FT_OnDownloaded(String str, String str2) {
        updateMessageUI(str, str2, 0);
    }

    private void updateMessageUI(String str, String str2, int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageById = sessionById.getMessageById(str2);
                if (messageById != null) {
                    updateMessage(messageById);
                }
            }
        }
    }

    public MMMessageItem addNewMessage(ZoomMessage zoomMessage) {
        return addNewMessage(zoomMessage, false);
    }

    public MMMessageItem addNewMessage(ZoomMessage zoomMessage, boolean z) {
        if (this.mAnchorMessageInfo != null || zoomMessage == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || zoomMessenger.getThreadDataProvider() == null || zoomMessenger.getSessionById(this.mSessionId) == null) {
            return null;
        }
        String threadID = zoomMessage.getThreadID();
        if (!TextUtils.isEmpty(threadID) && zoomMessage.isComment() && TextUtils.equals(threadID, this.mThreadId)) {
            return updateMessage(zoomMessage, false);
        }
        return null;
    }

    @Nullable
    public List<MMMessageItem> getAllShowMsgs() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (linearLayoutManager == null) {
            return null;
        }
        int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        ArrayList arrayList = new ArrayList();
        for (int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
            MMMessageItem itemByPosition = this.mAdapter.getItemByPosition(findFirstVisibleItemPosition);
            if (itemByPosition.isComment) {
                arrayList.add(itemByPosition);
            }
        }
        return arrayList;
    }

    private void parseThreadData4ThreadId(ThreadDataResult threadDataResult) {
        if (threadDataResult != null && this.mThreadResult != null && TextUtils.isEmpty(this.mThreadId)) {
            boolean z = false;
            if (threadDataResult.getThreadIdsCount() > 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                    if (threadDataProvider != null) {
                        ZoomMessage messagePtr = threadDataProvider.getMessagePtr(this.mSessionId, this.mThreadSvr);
                        if (messagePtr != null) {
                            this.mThreadId = messagePtr.getMessageID();
                            loadComments(false);
                            this.mThreadResult = null;
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (threadDataResult.getCurrState() == 1) {
                this.mThreadLoadCount++;
                z = true;
            } else if (threadDataResult.getCurrState() == 2) {
                this.mThreadLoadCount++;
                z = true;
            } else if (threadDataResult.getCurrState() == 16) {
                this.mThreadLoadCount++;
                z = true;
            } else if (threadDataResult.getCurrState() == 0) {
                this.mThreadLoadCount++;
                z = true;
            } else if ((threadDataResult.getCurrState() & 4) == 0 && (threadDataResult.getCurrState() & 8) == 0) {
                this.mThreadLoadCount++;
                z = true;
            }
            if (z) {
                if (this.mThreadLoadCount >= 2) {
                    Context context = getContext();
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                } else {
                    this.mThreadResult = null;
                    loadComments(true);
                }
            }
        }
    }

    public void OnGetThreadData(ThreadDataResult threadDataResult) {
        if (threadDataResult != null && this.mThreadResult != null && TextUtils.isEmpty(this.mThreadId)) {
            if ((!TextUtils.isEmpty(threadDataResult.getDbReqId()) && TextUtils.equals(threadDataResult.getDbReqId(), this.mThreadResult.getDbReqId())) || (!TextUtils.isEmpty(threadDataResult.getXmsReqId()) && TextUtils.equals(threadDataResult.getXmsReqId(), this.mThreadResult.getXmsReqId()))) {
                parseThreadData4ThreadId(threadDataResult);
            }
        }
    }

    public boolean OnGetCommentData(CommentDataResult commentDataResult) {
        if (commentDataResult == null || !this.mCommentReq.match(commentDataResult)) {
            return false;
        }
        if (commentDataResult.getDir() == 2) {
            this.mPendingRecentData = commentDataResult;
        }
        if (commentDataResult.getCurrState() != 16 || this.mCommentReq.isLoading(commentDataResult.getDir())) {
            if (commentDataResult.getCurrState() == 1) {
                if (commentDataResult.getDir() == 1) {
                    this.mHistoryCommentDataResult = null;
                } else if (commentDataResult.getDir() == 2) {
                    this.mRecentCommentDataResult = null;
                }
            }
        } else if (commentDataResult.getDir() == 1) {
            this.mHistoryCommentDataResult = commentDataResult;
        } else if (commentDataResult.getDir() == 2) {
            this.mRecentCommentDataResult = commentDataResult;
        }
        if (isLoading(2)) {
            this.mAdapter.removeLoadingView();
        }
        if (commentDataResult.getDir() == 1 && (commentDataResult.getCurrState() == 1 || (commentDataResult.getCurrState() & 16) != 0)) {
            this.mAdapter.setHistoryLoading(false);
        }
        updateComments(commentDataResult, true);
        syncMessageEmojiCountInfo();
        return true;
    }

    public void loadComments(boolean z) {
        loadComments(z, false, null);
    }

    public void loadComments(boolean z, boolean z2) {
        loadComments(z, z2, null);
    }

    public void loadComments(boolean z, boolean z2, String str) {
        loadComments(z, z2, str, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00ae, code lost:
        if (android.text.TextUtils.equals(r22, com.zipow.videobox.view.p014mm.MMMessageItem.NEW_COMMENT_MARK_MSGID) != false) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x017a, code lost:
        if (r5 == 0) goto L_0x0223;
     */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0139  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadComments(boolean r20, boolean r21, java.lang.String r22, boolean r23) {
        /*
            r19 = this;
            r0 = r19
            java.lang.String r1 = r0.mThreadId
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            r2 = 0
            if (r1 == 0) goto L_0x0041
            long r4 = r0.mThreadSvr
            int r1 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r1 != 0) goto L_0x0013
            return
        L_0x0013:
            com.zipow.videobox.ptapp.IMProtos$ThreadDataResult r1 = r0.mThreadResult
            if (r1 == 0) goto L_0x0018
            return
        L_0x0018:
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r1 = r1.getZoomMessenger()
            if (r1 != 0) goto L_0x0023
            return
        L_0x0023:
            com.zipow.videobox.ptapp.ThreadDataProvider r2 = r1.getThreadDataProvider()
            if (r2 != 0) goto L_0x002a
            return
        L_0x002a:
            java.lang.String r3 = r0.mSessionId
            r4 = 1
            java.lang.String r5 = ""
            long r6 = r0.mThreadSvr
            r8 = 1
            com.zipow.videobox.ptapp.IMProtos$ThreadDataResult r1 = r2.getThreadData(r3, r4, r5, r6, r8)
            r0.mThreadResult = r1
            com.zipow.videobox.ptapp.IMProtos$ThreadDataResult r1 = r0.mThreadResult
            if (r1 != 0) goto L_0x003d
            return
        L_0x003d:
            r0.parseThreadData4ThreadId(r1)
            return
        L_0x0041:
            r1 = 1
            if (r23 == 0) goto L_0x0050
            int r4 = r0.mFirstSyncRetryTimes
            r5 = 2
            if (r4 <= r5) goto L_0x004c
            r0.mIsFirstPageDirty = r1
            return
        L_0x004c:
            int r4 = r4 + r1
            r0.mFirstSyncRetryTimes = r4
            goto L_0x0052
        L_0x0050:
            r0.mFirstSyncRetryTimes = r1
        L_0x0052:
            if (r20 == 0) goto L_0x005d
            com.zipow.videobox.view.mm.MMCommentsAdapter r4 = r0.mAdapter
            boolean r4 = r4.isEmpty()
            if (r4 != 0) goto L_0x005d
            return
        L_0x005d:
            com.zipow.videobox.ptapp.PTApp r4 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r4 = r4.getZoomMessenger()
            if (r4 != 0) goto L_0x0068
            return
        L_0x0068:
            com.zipow.videobox.ptapp.ThreadDataProvider r15 = r4.getThreadDataProvider()
            if (r15 != 0) goto L_0x006f
            return
        L_0x006f:
            java.lang.String r5 = r0.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r4 = r4.getSessionById(r5)
            if (r4 != 0) goto L_0x0078
            return
        L_0x0078:
            com.zipow.videobox.view.mm.MMCommentsAdapter r4 = r0.mAdapter
            r4.clearDatas()
            r4 = 0
            r0.mPendingRecentData = r4
            r0.mRecentCommentDataResult = r4
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r5 = r0.mCommentReq
            r5.clear()
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r5 = r0.mAnchorMessageInfo
            r14 = 0
            if (r5 != 0) goto L_0x0159
            boolean r16 = r19.needJumpToStart()
            r5 = 40
            if (r21 != 0) goto L_0x00d9
            com.zipow.videobox.MMCommentActivity$ThreadUnreadInfo r6 = r0.mUnreadInfo
            if (r6 == 0) goto L_0x00d9
            long r6 = r6.readTime
            int r6 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r6 == 0) goto L_0x00d9
            com.zipow.videobox.MMCommentActivity$ThreadUnreadInfo r6 = r0.mUnreadInfo
            int r6 = r6.unreadCount
            if (r6 <= r5) goto L_0x00d9
            if (r16 != 0) goto L_0x00b0
            java.lang.String r6 = "MSGID_NEW_comment_MARK_ID"
            r7 = r22
            boolean r6 = android.text.TextUtils.equals(r7, r6)
            if (r6 == 0) goto L_0x00d9
        L_0x00b0:
            java.lang.String r6 = r0.mSessionId
            r7 = 20
            java.lang.String r8 = r0.mThreadId
            long r9 = r0.mThreadSvr
            com.zipow.videobox.MMCommentActivity$ThreadUnreadInfo r4 = r0.mUnreadInfo
            long r11 = r4.readTime
            r13 = 2
            r4 = 0
            r5 = r15
            r2 = 0
            r14 = r4
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r4 = r5.getCommentData(r6, r7, r8, r9, r11, r13, r14)
            java.lang.String r6 = r0.mSessionId
            java.lang.String r8 = r0.mThreadId
            long r9 = r0.mThreadSvr
            com.zipow.videobox.MMCommentActivity$ThreadUnreadInfo r3 = r0.mUnreadInfo
            long r11 = r3.readTime
            r13 = 1
            r14 = 0
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r3 = r5.getCommentData(r6, r7, r8, r9, r11, r13, r14)
            r0.setIsLocalMsgDirty(r1)
            goto L_0x0111
        L_0x00d9:
            r2 = 0
            com.zipow.videobox.MMCommentActivity$ThreadUnreadInfo r3 = r0.mUnreadInfo
            r6 = 20
            if (r3 == 0) goto L_0x00eb
            int r3 = r3.unreadCount
            int r3 = r3 + r1
            if (r3 <= r6) goto L_0x00eb
            com.zipow.videobox.MMCommentActivity$ThreadUnreadInfo r3 = r0.mUnreadInfo
            int r3 = r3.unreadCount
            int r3 = r3 + r1
            r6 = r3
        L_0x00eb:
            if (r6 <= r5) goto L_0x00f0
            r7 = 40
            goto L_0x00f1
        L_0x00f0:
            r7 = r6
        L_0x00f1:
            java.lang.String r6 = r0.mSessionId
            java.lang.String r8 = r0.mThreadId
            long r9 = r0.mThreadSvr
            java.lang.String r11 = ""
            r12 = 1
            r13 = 0
            r5 = r15
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r3 = r5.getCommentData(r6, r7, r8, r9, r11, r12, r13)
            r0.setIsLocalMsgDirty(r2)
            if (r3 == 0) goto L_0x0111
            long r5 = r3.getCurrState()
            r7 = 1
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 == 0) goto L_0x0111
            r0.mIsFirstPageLoading = r1
        L_0x0111:
            if (r4 != 0) goto L_0x0116
            if (r3 != 0) goto L_0x0116
            return
        L_0x0116:
            java.lang.String r1 = ""
            if (r21 == 0) goto L_0x011d
            java.lang.String r1 = "LAST_MSG_MARK_MSGID"
            goto L_0x0121
        L_0x011d:
            if (r16 == 0) goto L_0x0121
            java.lang.String r1 = "MSGID_NEW_comment_MARK_ID"
        L_0x0121:
            if (r4 == 0) goto L_0x012b
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r5 = r0.mCommentReq
            r5.updateReqInfo(r4, r1)
            r0.updateComments(r4, r2)
        L_0x012b:
            if (r3 == 0) goto L_0x0135
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r4 = r0.mCommentReq
            r4.updateReqInfo(r3, r1)
            r0.updateComments(r3, r2)
        L_0x0135:
            com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadsUICallBack r1 = r0.mUICallBack
            if (r1 == 0) goto L_0x0222
            com.zipow.videobox.MMCommentActivity$ThreadUnreadInfo r1 = r0.mUnreadInfo
            if (r1 == 0) goto L_0x0222
            long r1 = r1.readTime
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 == 0) goto L_0x0222
            if (r16 == 0) goto L_0x0150
            com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadsUICallBack r1 = r0.mUICallBack
            java.lang.String r2 = "LAST_MSG_MARK_MSGID"
            r1.onNewMsgIdReady(r2)
            goto L_0x0222
        L_0x0150:
            com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadsUICallBack r1 = r0.mUICallBack
            java.lang.String r2 = "MSGID_NEW_comment_MARK_ID"
            r1.onNewMsgIdReady(r2)
            goto L_0x0222
        L_0x0159:
            r2 = 0
            java.lang.String r3 = r5.getThrId()
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r4 = r0.mAnchorMessageInfo
            java.lang.String r4 = r4.getMsgGuid()
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r5 = r0.mAnchorMessageInfo
            long r5 = r5.getSendTime()
            boolean r7 = android.text.TextUtils.isEmpty(r3)
            if (r7 != 0) goto L_0x0223
            boolean r7 = android.text.TextUtils.isEmpty(r4)
            if (r7 == 0) goto L_0x017e
            r7 = 0
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 != 0) goto L_0x0180
            goto L_0x0223
        L_0x017e:
            r7 = 0
        L_0x0180:
            java.lang.String r5 = r0.mSessionId
            java.lang.String r6 = r0.mThreadId
            com.zipow.videobox.ptapp.mm.ZoomMessage r5 = r15.getMessagePtr(r5, r6)
            if (r5 == 0) goto L_0x018b
            goto L_0x018c
        L_0x018b:
            r1 = 0
        L_0x018c:
            if (r1 == 0) goto L_0x0191
            r16 = r7
            goto L_0x0199
        L_0x0191:
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r0.mAnchorMessageInfo
            long r5 = r1.getThrSvr()
            r16 = r5
        L_0x0199:
            java.lang.String r1 = r0.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomMessage r1 = r15.getMessagePtr(r1, r4)
            if (r1 != 0) goto L_0x01c4
            java.lang.String r6 = r0.mSessionId
            r7 = 20
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r0.mAnchorMessageInfo
            long r11 = r1.getSendTime()
            r13 = 2
            r14 = 0
            r5 = r15
            r8 = r3
            r9 = r16
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r1 = r5.getCommentData(r6, r7, r8, r9, r11, r13, r14)
            java.lang.String r6 = r0.mSessionId
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r5 = r0.mAnchorMessageInfo
            long r11 = r5.getSendTime()
            r13 = 1
            r5 = r15
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r3 = r5.getCommentData(r6, r7, r8, r9, r11, r13, r14)
            goto L_0x01ec
        L_0x01c4:
            java.lang.String r6 = r0.mSessionId
            r7 = 20
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r0.mAnchorMessageInfo
            long r12 = r1.getSendTime()
            r14 = 2
            r1 = 0
            r5 = r15
            r8 = r3
            r9 = r16
            r11 = r4
            r18 = r15
            r15 = r1
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r1 = r5.getCommentData(r6, r7, r8, r9, r11, r12, r14, r15)
            java.lang.String r6 = r0.mSessionId
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r5 = r0.mAnchorMessageInfo
            long r12 = r5.getSendTime()
            r14 = 1
            r15 = 0
            r5 = r18
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r3 = r5.getCommentData(r6, r7, r8, r9, r11, r12, r14, r15)
        L_0x01ec:
            if (r3 != 0) goto L_0x01f1
            if (r1 != 0) goto L_0x01f1
            return
        L_0x01f1:
            boolean r5 = android.text.TextUtils.isEmpty(r4)
            if (r5 == 0) goto L_0x020e
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = ""
            r4.append(r5)
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r5 = r0.mAnchorMessageInfo
            long r5 = r5.getSendTime()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
        L_0x020e:
            if (r3 == 0) goto L_0x0218
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r5 = r0.mCommentReq
            r5.updateReqInfo(r3, r4)
            r0.updateComments(r3, r2)
        L_0x0218:
            if (r1 == 0) goto L_0x0222
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r3 = r0.mCommentReq
            r3.updateReqInfo(r1, r4)
            r0.updateComments(r1, r2)
        L_0x0222:
            return
        L_0x0223:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsRecyclerView.loadComments(boolean, boolean, java.lang.String, boolean):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x006e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x006f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean loadMoreComments(int r17) {
        /*
            r16 = this;
            r0 = r16
            r10 = r17
            r1 = 2
            r11 = 1
            r12 = 0
            if (r10 == r1) goto L_0x000c
            if (r10 == r11) goto L_0x000c
            return r12
        L_0x000c:
            boolean r2 = r0.isLoading(r1)
            if (r2 != 0) goto L_0x00dc
            boolean r2 = r0.isLoading(r11)
            if (r2 == 0) goto L_0x001a
            goto L_0x00dc
        L_0x001a:
            boolean r2 = r0.mIsFirstPageDirty
            r13 = 0
            if (r2 == 0) goto L_0x0023
            r0.loadComments(r12, r11, r13)
            return r12
        L_0x0023:
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r14 = r2.getZoomMessenger()
            if (r14 != 0) goto L_0x002e
            return r12
        L_0x002e:
            com.zipow.videobox.ptapp.ThreadDataProvider r2 = r14.getThreadDataProvider()
            if (r2 != 0) goto L_0x0035
            return r12
        L_0x0035:
            if (r10 != r11) goto L_0x003e
            com.zipow.videobox.view.mm.MMCommentsAdapter r3 = r0.mAdapter
            com.zipow.videobox.view.mm.MMMessageItem r3 = r3.getEarliestComment()
            goto L_0x0044
        L_0x003e:
            com.zipow.videobox.view.mm.MMCommentsAdapter r3 = r0.mAdapter
            com.zipow.videobox.view.mm.MMMessageItem r3 = r3.getLatestComment()
        L_0x0044:
            if (r3 != 0) goto L_0x0047
            return r12
        L_0x0047:
            java.lang.String r3 = r3.messageId
            boolean r4 = r14.isConnectionGood()
            if (r4 == 0) goto L_0x0067
            if (r10 != r11) goto L_0x005b
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r4 = r0.mHistoryCommentDataResult
            if (r4 == 0) goto L_0x005b
            java.lang.String r3 = r4.getStartComment()
            r15 = r3
            goto L_0x0068
        L_0x005b:
            if (r10 != r1) goto L_0x0067
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r4 = r0.mRecentCommentDataResult
            if (r4 == 0) goto L_0x0067
            java.lang.String r3 = r4.getStartComment()
            r15 = r3
            goto L_0x0068
        L_0x0067:
            r15 = r3
        L_0x0068:
            boolean r3 = android.text.TextUtils.isEmpty(r15)
            if (r3 == 0) goto L_0x006f
            return r12
        L_0x006f:
            java.lang.String r3 = r0.mThreadId
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x009b
            if (r10 != r11) goto L_0x008e
            java.lang.String r3 = r0.mSessionId
            java.lang.String r4 = r0.mThreadId
            boolean r3 = r2.moreHistoricComments(r3, r4, r15)
            if (r3 != 0) goto L_0x008e
            com.zipow.videobox.view.mm.MMCommentsAdapter r1 = r0.mAdapter
            r1.setCommentHistoryReady()
            com.zipow.videobox.view.mm.MMCommentsAdapter r1 = r0.mAdapter
            r1.notifyDataSetChanged()
            return r11
        L_0x008e:
            if (r10 != r1) goto L_0x009b
            java.lang.String r1 = r0.mSessionId
            java.lang.String r3 = r0.mThreadId
            boolean r1 = r2.moreRecentComments(r1, r3, r15)
            if (r1 != 0) goto L_0x009b
            return r11
        L_0x009b:
            java.lang.String r3 = r0.mSessionId
            r4 = 21
            java.lang.String r5 = r0.mThreadId
            long r6 = r0.mThreadSvr
            r9 = 0
            r1 = r2
            r2 = r3
            r3 = r4
            r4 = r5
            r5 = r6
            r7 = r15
            r8 = r17
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r1 = r1.getCommentData(r2, r3, r4, r5, r7, r8, r9)
            if (r1 != 0) goto L_0x00b3
            return r12
        L_0x00b3:
            long r2 = r1.getCurrState()
            r4 = 1
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x00c8
            if (r10 != r11) goto L_0x00c2
            r0.mHistoryCommentDataResult = r13
            goto L_0x00c4
        L_0x00c2:
            r0.mRecentCommentDataResult = r13
        L_0x00c4:
            r16.syncMessageEmojiCountInfo()
            goto L_0x00d3
        L_0x00c8:
            if (r10 != r11) goto L_0x00d3
            com.zipow.videobox.view.mm.MMCommentsAdapter r2 = r0.mAdapter
            boolean r3 = r14.isConnectionGood()
            r2.setHistoryLoading(r3)
        L_0x00d3:
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r2 = r0.mCommentReq
            r2.updateReqInfo(r1, r15)
            r0.updateComments(r1, r12)
            return r12
        L_0x00dc:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsRecyclerView.loadMoreComments(int):boolean");
    }

    public boolean isCommentHistoryReady() {
        return this.mAdapter.isCommentHistoryReady();
    }

    public boolean isAtBottom() {
        return this.mLinearLayoutManager.findLastVisibleItemPosition() >= this.mAdapter.getItemCount() - 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01ae, code lost:
        if (r9.moreHistoricComments(r0.mSessionId, r0.mThreadId, (java.lang.String) r6.get(0)) != false) goto L_0x01b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x01c6, code lost:
        if (isLoading(2) == false) goto L_0x01c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01c8, code lost:
        r0.mAdapter.setCommentHistoryReady();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateComments(com.zipow.videobox.ptapp.IMProtos.CommentDataResult r23, boolean r24) {
        /*
            r22 = this;
            r0 = r22
            r1 = r23
            if (r1 != 0) goto L_0x0007
            return
        L_0x0007:
            if (r24 != 0) goto L_0x0014
            java.lang.String r2 = r23.getDbReqId()
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x0014
            return
        L_0x0014:
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r2 = r2.getZoomMessenger()
            if (r2 != 0) goto L_0x001f
            return
        L_0x001f:
            com.zipow.videobox.ptapp.ThreadDataProvider r11 = r2.getThreadDataProvider()
            if (r11 != 0) goto L_0x0026
            return
        L_0x0026:
            java.lang.String r3 = r0.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r12 = r2.findSessionById(r3)
            if (r12 != 0) goto L_0x002f
            return
        L_0x002f:
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            java.util.ArrayList r14 = new java.util.ArrayList
            java.util.List r3 = r23.getCommentIdsList()
            r14.<init>(r3)
            java.lang.String r3 = r0.myJid
            if (r3 != 0) goto L_0x0042
            return
        L_0x0042:
            boolean r3 = r0.mIsFirstPageLoading
            r15 = 0
            r16 = 0
            r10 = 0
            r9 = 1
            if (r3 == 0) goto L_0x0099
            long r3 = r23.getCurrState()
            r5 = 1
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x0060
            long r3 = r23.getCurrState()
            r7 = 16
            long r3 = r3 & r7
            int r3 = (r3 > r16 ? 1 : (r3 == r16 ? 0 : -1))
            if (r3 == 0) goto L_0x0099
        L_0x0060:
            int r3 = r23.getDir()
            if (r3 != r9) goto L_0x0099
            com.zipow.videobox.view.mm.MMCommentsAdapter r3 = r0.mAdapter
            r3.clearDatas()
            long r3 = r23.getCurrState()
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x0075
            r3 = 1
            goto L_0x0076
        L_0x0075:
            r3 = 0
        L_0x0076:
            r0.mIsFirstPageDirty = r3
            boolean r3 = r0.mIsFirstPageDirty
            if (r3 == 0) goto L_0x008e
            boolean r3 = r2.isConnectionGood()
            if (r3 == 0) goto L_0x008e
            r0.loadComments(r10, r9, r15, r9)
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r3 = r0.mCommentReq
            boolean r3 = r3.isLoading(r9)
            r0.mIsFirstPageLoading = r3
            goto L_0x0090
        L_0x008e:
            r0.mIsFirstPageLoading = r10
        L_0x0090:
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r3 = r0.mPendingRecentData
            if (r3 == 0) goto L_0x0099
            r0.updateComments(r3, r9)
            r0.mPendingRecentData = r15
        L_0x0099:
            com.zipow.videobox.view.mm.MMCommentsAdapter r3 = r0.mAdapter
            java.util.List r3 = r3.getAllComments()
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
            java.util.Iterator r3 = r3.iterator()
        L_0x00a8:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x00be
            java.lang.Object r4 = r3.next()
            com.zipow.videobox.view.mm.MMMessageItem r4 = (com.zipow.videobox.view.p014mm.MMMessageItem) r4
            java.lang.String r5 = r4.messageId
            if (r5 == 0) goto L_0x00a8
            java.lang.String r5 = r4.messageId
            r8.put(r5, r4)
            goto L_0x00a8
        L_0x00be:
            java.util.HashSet r7 = new java.util.HashSet
            r7.<init>()
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.MMFileContentMgr r18 = r3.getZoomFileContentMgr()
            r6 = 0
        L_0x00cc:
            int r3 = r14.size()
            if (r6 >= r3) goto L_0x0141
            java.lang.Object r3 = r14.get(r6)
            r5 = r3
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r3 = r0.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomMessage r3 = r11.getMessagePtr(r3, r5)
            if (r3 != 0) goto L_0x00ea
            r21 = r6
            r15 = r7
            r20 = r11
            r19 = r14
            r14 = r8
            goto L_0x0135
        L_0x00ea:
            java.lang.String r4 = r3.getSenderID()
            java.lang.String r9 = r0.myJid
            boolean r9 = p021us.zoom.androidlib.util.StringUtil.isSameString(r4, r9)
            java.lang.String r4 = r0.mSessionId
            boolean r10 = r0.mIsGroup
            android.content.Context r19 = r22.getContext()
            com.zipow.videobox.view.IMAddrBookItem r15 = r0.mIMAddrBookItem
            r20 = r11
            r11 = r5
            r5 = r2
            r21 = r6
            r6 = r10
            r10 = r7
            r7 = r9
            r9 = r8
            r8 = r19
            r19 = r14
            r14 = r9
            r9 = r15
            r15 = r10
            r10 = r18
            com.zipow.videobox.view.mm.MMMessageItem r3 = com.zipow.videobox.view.p014mm.MMMessageItem.initWithZoomMessage(r3, r4, r5, r6, r7, r8, r9, r10)
            if (r3 != 0) goto L_0x0118
            goto L_0x0135
        L_0x0118:
            r12.checkAutoDownloadForMessage(r11)
            java.lang.String r4 = r3.messageId
            java.lang.Object r4 = r14.get(r4)
            com.zipow.videobox.view.mm.MMMessageItem r4 = (com.zipow.videobox.view.p014mm.MMMessageItem) r4
            if (r4 == 0) goto L_0x0129
            boolean r4 = r4.isDownloading
            r3.isDownloading = r4
        L_0x0129:
            r0.checkLinkPreviewAndOther(r3)
            r0.checkAndDecodeE2EMsg(r2, r3)
            r13.add(r3)
            r15.add(r11)
        L_0x0135:
            int r6 = r21 + 1
            r8 = r14
            r7 = r15
            r14 = r19
            r11 = r20
            r9 = 1
            r10 = 0
            r15 = 0
            goto L_0x00cc
        L_0x0141:
            r15 = r7
            r20 = r11
            r19 = r14
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r2 = r0.mCommentReq
            com.zipow.videobox.ptapp.IMProtos$CommentDataResult r2 = r2.getLastResult(r1)
            if (r2 == 0) goto L_0x0155
            java.util.List r2 = r2.getCommentIdsList()
            r15.addAll(r2)
        L_0x0155:
            com.zipow.videobox.view.mm.MMCommentsAdapter r2 = r0.mAdapter
            r2.removeLocalMsgs(r15)
            int r2 = r23.getDir()
            r3 = 1
            if (r2 != r3) goto L_0x016c
            com.zipow.videobox.view.mm.MMMessageItem r2 = r22.getFirstVisibleItem()
            if (r2 == 0) goto L_0x016a
            java.lang.String r15 = r2.messageId
            goto L_0x0176
        L_0x016a:
            r15 = 0
            goto L_0x0176
        L_0x016c:
            com.zipow.videobox.view.mm.MMMessageItem r2 = r22.getLastVisibleItem()
            if (r2 == 0) goto L_0x0175
            java.lang.String r15 = r2.messageId
            goto L_0x0176
        L_0x0175:
            r15 = 0
        L_0x0176:
            com.zipow.videobox.view.mm.MMCommentsAdapter r2 = r0.mAdapter
            int r4 = r23.getDir()
            r2.addMessages(r13, r4)
            int r2 = r23.getDir()
            r4 = 2
            if (r2 != r4) goto L_0x0191
            boolean r2 = r0.isLoading(r4)
            if (r2 != 0) goto L_0x0191
            com.zipow.videobox.view.mm.MMCommentsAdapter r2 = r0.mAdapter
            r2.removeLoadingView()
        L_0x0191:
            java.lang.String r2 = r0.mThreadId
            if (r2 == 0) goto L_0x01b1
            int r2 = r19.size()
            if (r2 <= 0) goto L_0x01b1
            java.lang.String r2 = r0.mSessionId
            java.lang.String r5 = r0.mThreadId
            r6 = r19
            r7 = 0
            java.lang.Object r8 = r6.get(r7)
            java.lang.String r8 = (java.lang.String) r8
            r9 = r20
            boolean r2 = r9.moreHistoricComments(r2, r5, r8)
            if (r2 == 0) goto L_0x01c8
            goto L_0x01b6
        L_0x01b1:
            r6 = r19
            r9 = r20
            r7 = 0
        L_0x01b6:
            int r2 = r6.size()
            if (r2 != 0) goto L_0x01cd
            boolean r2 = r0.isLoading(r3)
            if (r2 != 0) goto L_0x01cd
            boolean r2 = r0.isLoading(r4)
            if (r2 != 0) goto L_0x01cd
        L_0x01c8:
            com.zipow.videobox.view.mm.MMCommentsAdapter r2 = r0.mAdapter
            r2.setCommentHistoryReady()
        L_0x01cd:
            r22.refreshStarMsgs()
            com.zipow.videobox.view.mm.MMCommentsAdapter r2 = r0.mAdapter
            r2.notifyDataSetChanged()
            com.zipow.videobox.view.mm.MMCommentsRecyclerView$CommentReqInfo r2 = r0.mCommentReq
            java.lang.String r2 = r2.getAnchorMsgId(r1)
            boolean r5 = android.text.TextUtils.isEmpty(r2)
            if (r5 == 0) goto L_0x01ef
            boolean r2 = android.text.TextUtils.isEmpty(r15)
            if (r2 != 0) goto L_0x01eb
            r0.scrollToMessage(r15)
            goto L_0x0229
        L_0x01eb:
            r0.scrollToBottom(r3)
            goto L_0x0229
        L_0x01ef:
            boolean r5 = r0.scrollToMessage(r2)
            if (r5 != 0) goto L_0x0226
            java.lang.String r5 = "MSGID_NEW_comment_MARK_ID"
            boolean r5 = android.text.TextUtils.equals(r2, r5)
            if (r5 == 0) goto L_0x0211
            com.zipow.videobox.view.mm.MMCommentsAdapter r5 = r0.mAdapter
            boolean r5 = r5.isCommentHistoryReady()
            if (r5 == 0) goto L_0x0211
            com.zipow.videobox.MMCommentActivity$ThreadUnreadInfo r5 = r0.mUnreadInfo
            if (r5 == 0) goto L_0x0211
            int r5 = r5.unreadCount
            if (r5 <= 0) goto L_0x0211
            r0.scrollToPosition(r3)
            goto L_0x0229
        L_0x0211:
            long r5 = java.lang.Long.parseLong(r2)     // Catch:{ Exception -> 0x0216 }
            goto L_0x0218
        L_0x0216:
            r5 = r16
        L_0x0218:
            int r2 = (r5 > r16 ? 1 : (r5 == r16 ? 0 : -1))
            if (r2 == 0) goto L_0x0222
            boolean r2 = r0.scrollToMessage(r5)
            if (r2 != 0) goto L_0x0229
        L_0x0222:
            r0.scrollToBottom(r3)
            goto L_0x0229
        L_0x0226:
            r22.removeScrollBottomMsg()
        L_0x0229:
            java.lang.String r2 = r0.mThreadId
            if (r2 == 0) goto L_0x024a
            int r1 = r23.getDir()
            if (r1 != r4) goto L_0x024a
            com.zipow.videobox.view.mm.MMCommentsAdapter r1 = r0.mAdapter
            com.zipow.videobox.view.mm.MMMessageItem r1 = r1.getLatestComment()
            if (r1 == 0) goto L_0x024a
            java.lang.String r2 = r0.mSessionId
            java.lang.String r3 = r0.mThreadId
            java.lang.String r1 = r1.messageId
            boolean r1 = r9.moreRecentComments(r2, r3, r1)
            if (r1 != 0) goto L_0x024a
            r0.setIsLocalMsgDirty(r7)
        L_0x024a:
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r0.mAnchorMessageInfo
            if (r1 == 0) goto L_0x0277
            boolean r1 = r1.isFromMarkUnread()
            if (r1 == 0) goto L_0x0277
            boolean r1 = r22.isLoading()
            if (r1 != 0) goto L_0x0277
            com.zipow.videobox.view.mm.MMCommentsAdapter r1 = r0.mAdapter
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r2 = r0.mAnchorMessageInfo
            java.lang.String r2 = r2.getMsgGuid()
            int r1 = r1.findItemInAdapter(r2)
            r2 = -1
            if (r1 != r2) goto L_0x0277
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r0.mAnchorMessageInfo
            java.lang.String r1 = r1.getMsgGuid()
            r12.unmarkMessageAsUnread(r1)
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r0.mAnchorMessageInfo
            r1.setFromMarkUnread(r7)
        L_0x0277:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsRecyclerView.updateComments(com.zipow.videobox.ptapp.IMProtos$CommentDataResult, boolean):void");
    }

    public void removeScrollBottomMsg() {
        this.mHandler.removeMessages(1);
    }

    public void refreshStarMsgs() {
        if (!TextUtils.isEmpty(this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                Set<Long> set = this.mStarredMsgs;
                if (set == null) {
                    List<String> allStarredMessages = zoomMessenger.getAllStarredMessages(this.mSessionId);
                    if (allStarredMessages != null) {
                        this.mStarredMsgs = new HashSet();
                        try {
                            for (String parseLong : allStarredMessages) {
                                this.mStarredMsgs.add(Long.valueOf(Long.parseLong(parseLong)));
                            }
                        } catch (Exception unused) {
                        }
                    }
                } else {
                    List<String> allStarredMessages2 = zoomMessenger.getAllStarredMessages(this.mSessionId);
                    this.mStarredMsgs = new HashSet();
                    if (allStarredMessages2 != null) {
                        try {
                            for (String parseLong2 : allStarredMessages2) {
                                this.mStarredMsgs.add(Long.valueOf(Long.parseLong(parseLong2)));
                            }
                        } catch (Exception unused2) {
                        }
                    }
                    if (CollectionsUtil.isCollectionEmpty(this.mStarredMsgs)) {
                        for (Long longValue : set) {
                            MMMessageItem itemByMessageSVR = this.mAdapter.getItemByMessageSVR(longValue.longValue());
                            if (itemByMessageSVR != null) {
                                itemByMessageSVR.isMessgeStarred = false;
                            }
                        }
                    } else {
                        for (Long l : this.mStarredMsgs) {
                            MMMessageItem itemByMessageSVR2 = this.mAdapter.getItemByMessageSVR(l.longValue());
                            if (itemByMessageSVR2 != null) {
                                itemByMessageSVR2.isMessgeStarred = true;
                            }
                            set.remove(l);
                        }
                        for (Long longValue2 : set) {
                            MMMessageItem itemByMessageSVR3 = this.mAdapter.getItemByMessageSVR(longValue2.longValue());
                            if (itemByMessageSVR3 != null) {
                                itemByMessageSVR3.isMessgeStarred = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public void scrollToBottom(boolean z) {
        this.mHandler.obtainMessage(1, z ? 1 : 0, 0).sendToTarget();
    }

    public void showLatestComments() {
        if (this.mIsLocalMsgDirty) {
            loadComments(false, true);
        } else {
            scrollToBottom(true);
        }
    }

    public boolean scrollToMessage(long j) {
        int findItemInAdapter = this.mAdapter.findItemInAdapter(j);
        if (findItemInAdapter == -1) {
            return false;
        }
        this.mHandler.removeMessages(1);
        this.mLinearLayoutManager.scrollToPositionWithOffset(findItemInAdapter, UIUtil.dip2px(getContext(), 100.0f));
        return true;
    }

    public boolean scrollToMessage(String str) {
        int findItemInAdapter = this.mAdapter.findItemInAdapter(str);
        if (findItemInAdapter == -1) {
            return false;
        }
        this.mHandler.removeMessages(1);
        this.mLinearLayoutManager.scrollToPositionWithOffset(findItemInAdapter, UIUtil.dip2px(getContext(), 100.0f));
        return true;
    }

    @Nullable
    private MMMessageItem getFirstVisibleItem() {
        MMMessageItem mMMessageItem;
        int findFirstCompletelyVisibleItemPosition = this.mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (findFirstCompletelyVisibleItemPosition == -1) {
            findFirstCompletelyVisibleItemPosition = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        }
        if (findFirstCompletelyVisibleItemPosition == -1) {
            return null;
        }
        while (true) {
            if (findFirstCompletelyVisibleItemPosition >= this.mAdapter.getItemCount()) {
                mMMessageItem = null;
                break;
            }
            mMMessageItem = this.mAdapter.getItemByPosition(findFirstCompletelyVisibleItemPosition);
            if (mMMessageItem != null && mMMessageItem.messageType != 19) {
                break;
            }
            findFirstCompletelyVisibleItemPosition++;
        }
        return mMMessageItem;
    }

    @Nullable
    private MMMessageItem getLastVisibleItem() {
        int findLastCompletelyVisibleItemPosition = this.mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        if (findLastCompletelyVisibleItemPosition == -1) {
            findLastCompletelyVisibleItemPosition = this.mLinearLayoutManager.findLastVisibleItemPosition();
        }
        MMMessageItem mMMessageItem = null;
        if (findLastCompletelyVisibleItemPosition == -1) {
            return null;
        }
        while (mMMessageItem == null && findLastCompletelyVisibleItemPosition >= 0) {
            MMMessageItem itemByPosition = this.mAdapter.getItemByPosition(findLastCompletelyVisibleItemPosition);
            if (!(itemByPosition == null || itemByPosition.messageType == 19)) {
                mMMessageItem = itemByPosition;
            }
            findLastCompletelyVisibleItemPosition--;
        }
        return mMMessageItem;
    }

    public Rect getMessageLocationOnScreen(@NonNull MMMessageItem mMMessageItem) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            for (int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
                MMMessageItem item = this.mAdapter.getItem(findFirstVisibleItemPosition);
                if (item != null && StringUtil.isSameString(item.messageId, mMMessageItem.messageId)) {
                    ViewHolder findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                    if (findViewHolderForAdapterPosition != null) {
                        return ((AbsMessageView) findViewHolderForAdapterPosition.itemView).getMessageLocationOnScreen();
                    }
                }
            }
        }
        return null;
    }

    public void setMessageViewMargin(MMMessageItem mMMessageItem, int i) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            for (int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
                if (i == 0) {
                    ViewHolder findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                    if (findViewHolderForAdapterPosition != null) {
                        View view = findViewHolderForAdapterPosition.itemView;
                        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                        layoutParams.bottomMargin = i;
                        view.setLayoutParams(layoutParams);
                    }
                } else {
                    MMMessageItem item = this.mAdapter.getItem(findFirstVisibleItemPosition);
                    if (item != null && StringUtil.isSameString(item.messageId, mMMessageItem.messageId)) {
                        ViewHolder findViewHolderForAdapterPosition2 = findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                        if (findViewHolderForAdapterPosition2 != null) {
                            View view2 = findViewHolderForAdapterPosition2.itemView;
                            LayoutParams layoutParams2 = (LayoutParams) view2.getLayoutParams();
                            layoutParams2.bottomMargin = i;
                            view2.setLayoutParams(layoutParams2);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void FT_Cancel(@Nullable MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            FT_OnCancel(mMMessageItem.sessionId, mMMessageItem.messageId);
        }
    }

    public void FT_OnCancel(String str, String str2) {
        updateMessageUI(str, str2, 0);
    }

    private void syncMessageEmojiCountInfo() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                ArrayList arrayList = new ArrayList();
                LayoutManager layoutManager = getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    for (int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
                        MMMessageItem item = this.mAdapter.getItem(findFirstVisibleItemPosition);
                        if (item != null && !StringUtil.isEmptyOrNull(item.messageXMPPId) && threadDataProvider.isMessageEmojiCountInfoDirty(this.mSessionId, item.messageXMPPId) && !this.mSyncMessageIDs.contains(item.messageXMPPId)) {
                            this.mSyncMessageIDs.add(item.messageXMPPId);
                            arrayList.add(item.messageXMPPId);
                        }
                    }
                }
                if (!arrayList.isEmpty()) {
                    threadDataProvider.syncMessageEmojiCountInfo(this.mSessionId, arrayList);
                }
            }
        }
    }

    public boolean isUnreadNewMessage(long j) {
        MMCommentsAdapter mMCommentsAdapter = this.mAdapter;
        if (mMCommentsAdapter == null) {
            return false;
        }
        return mMCommentsAdapter.isUnreadNewMessage(j);
    }
}
