package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
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
import com.zipow.videobox.dialog.InformationBarriesDialog;
import com.zipow.videobox.fragment.MMThreadsFragment;
import com.zipow.videobox.ptapp.IMProtos.CommentDataResult;
import com.zipow.videobox.ptapp.IMProtos.CrawlLinkResponse;
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
import com.zipow.videobox.util.MMMessageHelper.MessageSyncer;
import com.zipow.videobox.util.MMMessageHelper.ThrCommentState;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickActionListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickActionMoreListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAddonListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickCancelListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickGiphyBtnListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickLinkPreviewListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMeetingNOListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickReactionLabelListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickStatusImageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickTemplateActionMoreListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickTemplateListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMContentMessageItem.MMContentMessageAnchorInfo;
import com.zipow.videobox.view.p014mm.MMThreadsAdapter.CommentItem;
import com.zipow.videobox.view.p014mm.MMThreadsAdapter.ThreadItem;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMThreadsRecyclerView */
public class MMThreadsRecyclerView extends RecyclerView {
    public static final int PAGE_SIZE = 20;
    private static final String TAG = "MMThreadsRecyclerView";
    /* access modifiers changed from: private */
    public boolean isLayoutReady;
    /* access modifiers changed from: private */
    public MMThreadsAdapter mAdapter;
    /* access modifiers changed from: private */
    public MMContentMessageAnchorInfo mAnchorMessageInfo;
    private Set<String> mCommentsLoadingReqIds = new HashSet();
    private Map<String, CommentReqInfo> mCommentsReqIds = new HashMap();
    private int mFirstSyncRetryTimes = 1;
    @Nullable
    private GestureDetector mGestureDetector;
    private HashMap<String, String> mGiphyReqIds = new HashMap<>();
    private MyHandler mHandler = new MyHandler(this);
    private boolean mHasNewMessageDuringPaused = true;
    private ThreadDataResult mHistoryThreadDataResult;
    private IMAddrBookItem mIMAddrBookItem;
    private boolean mIsE2EChat = false;
    private boolean mIsFirstPageDirty = false;
    private boolean mIsFirstPageLoading;
    private boolean mIsGroupMessage;
    private boolean mIsLocalMsgDirty = false;
    private boolean mIsResume;
    /* access modifiers changed from: private */
    public LinearLayoutManager mLinearLayoutManager;
    private HashMap<String, String> mLinkPreviewReqIds = new HashMap<>();
    @Nullable
    private MMMessageHelper mMessageHelper;
    private Handler mNotifyHandler = new Handler();
    /* access modifiers changed from: private */
    public MMThreadsFragment mParentFragment;
    private ThreadDataResult mPendingRecentData = null;
    private ThreadDataResult mRecentThreadDataResult;
    private Runnable mRunnableNotifyDataSetChanged = new Runnable() {
        public void run() {
            if (MMThreadsRecyclerView.this.mAdapter != null) {
                MMThreadsRecyclerView.this.mAdapter.notifyDataSetChanged();
            }
        }
    };
    /* access modifiers changed from: private */
    public String mSessionId;
    private Set<Long> mStarredMsgs;
    private Set<String> mSyncMessageIDs = new HashSet();
    private ThreadReqInfo mTheadReq = new ThreadReqInfo();
    private int mThreadSortType;
    /* access modifiers changed from: private */
    public ThreadsUICallBack mUICallBack;
    private int mUnreadMsgCount;
    private long mUnreadMsgTimestamp;
    private String myJid;

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsRecyclerView$CommentReqInfo */
    private static class CommentReqInfo {
        int callbackCount;
        String dbReqId;
        String xmsReqId;

        CommentReqInfo(String str, String str2) {
            this.dbReqId = str;
            this.xmsReqId = str2;
            if (!TextUtils.isEmpty(str)) {
                this.callbackCount++;
            }
            if (!TextUtils.isEmpty(str2)) {
                this.callbackCount++;
            }
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsRecyclerView$MyHandler */
    private static class MyHandler extends Handler {
        static final int MSG_SCROLL_BOTTOM = 1;
        private WeakReference<MMThreadsRecyclerView> mView;

        MyHandler(@NonNull MMThreadsRecyclerView mMThreadsRecyclerView) {
            this.mView = new WeakReference<>(mMThreadsRecyclerView);
        }

        public void handleMessage(Message message) {
            boolean z = true;
            if (message.what == 1) {
                if (message.arg1 == 0) {
                    z = false;
                }
                scrollBottom(z);
            }
        }

        private void scrollBottom(boolean z) {
            MMThreadsRecyclerView mMThreadsRecyclerView = (MMThreadsRecyclerView) this.mView.get();
            if (mMThreadsRecyclerView != null && mMThreadsRecyclerView.isShown()) {
                boolean z2 = true;
                int itemCount = mMThreadsRecyclerView.mAdapter.getItemCount() - 1;
                if (z) {
                    mMThreadsRecyclerView.scrollToPosition(itemCount);
                } else if (itemCount - mMThreadsRecyclerView.mLinearLayoutManager.findLastVisibleItemPosition() < 5) {
                    mMThreadsRecyclerView.scrollToPosition(itemCount);
                } else {
                    z2 = false;
                }
                if (z2 && mMThreadsRecyclerView.mAnchorMessageInfo == null) {
                    MessageSyncer.getInstance().cleanUnreadMessageCount(mMThreadsRecyclerView.mSessionId);
                }
            }
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadReqInfo */
    private static class ThreadReqInfo {
        private SparseArray<ReqInfo> reqInfos = new SparseArray<>();

        /* renamed from: com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadReqInfo$ReqInfo */
        static class ReqInfo {
            String anchorMsgId;
            int callbackCount;
            ThreadDataResult dataResult;

            ReqInfo() {
            }
        }

        ThreadReqInfo() {
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            this.reqInfos.clear();
        }

        /* access modifiers changed from: 0000 */
        public void updateReqInfo(ThreadDataResult threadDataResult) {
            updateReqInfo(threadDataResult, null);
        }

        /* access modifiers changed from: 0000 */
        public void updateReqInfo(ThreadDataResult threadDataResult, String str) {
            if (threadDataResult != null && threadDataResult.getDir() != 0) {
                ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(threadDataResult.getDir());
                if (reqInfo == null) {
                    reqInfo = new ReqInfo();
                }
                this.reqInfos.put(threadDataResult.getDir(), reqInfo);
                reqInfo.callbackCount = 0;
                reqInfo.dataResult = threadDataResult;
                reqInfo.anchorMsgId = str;
                if (!TextUtils.isEmpty(threadDataResult.getDbReqId())) {
                    reqInfo.callbackCount++;
                }
                if (!TextUtils.isEmpty(threadDataResult.getXmsReqId())) {
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
        public String getAnchorMsgId(ThreadDataResult threadDataResult) {
            if (threadDataResult == null) {
                return null;
            }
            ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(threadDataResult.getDir());
            if (reqInfo == null) {
                return null;
            }
            return reqInfo.anchorMsgId;
        }

        /* access modifiers changed from: 0000 */
        public void updateDataResult(ThreadDataResult threadDataResult) {
            if (threadDataResult != null) {
                ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(threadDataResult.getDir());
                if (reqInfo != null) {
                    reqInfo.dataResult = threadDataResult;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public ThreadDataResult getLastResult(ThreadDataResult threadDataResult) {
            if (threadDataResult == null) {
                return null;
            }
            ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(threadDataResult.getDir());
            if (reqInfo == null) {
                return null;
            }
            if ((TextUtils.isEmpty(threadDataResult.getDbReqId()) || !TextUtils.equals(threadDataResult.getDbReqId(), reqInfo.dataResult.getDbReqId())) && (TextUtils.isEmpty(threadDataResult.getXmsReqId()) || !TextUtils.equals(threadDataResult.getXmsReqId(), reqInfo.dataResult.getXmsReqId()))) {
                return null;
            }
            return reqInfo.dataResult;
        }

        /* access modifiers changed from: 0000 */
        public boolean match(ThreadDataResult threadDataResult) {
            if (threadDataResult == null) {
                return false;
            }
            ReqInfo reqInfo = (ReqInfo) this.reqInfos.get(threadDataResult.getDir());
            if (reqInfo == null) {
                return false;
            }
            if ((TextUtils.isEmpty(threadDataResult.getDbReqId()) || !TextUtils.equals(threadDataResult.getDbReqId(), reqInfo.dataResult.getDbReqId())) && (TextUtils.isEmpty(threadDataResult.getXmsReqId()) || !TextUtils.equals(threadDataResult.getXmsReqId(), reqInfo.dataResult.getXmsReqId()))) {
                return false;
            }
            reqInfo.callbackCount--;
            return true;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadsUICallBack */
    public interface ThreadsUICallBack extends OnShowContextMenuListener, OnClickMessageListener, OnClickStatusImageListener, OnClickAvatarListener, OnClickCancelListener, OnLongClickAvatarListener, OnClickAddonListener, OnClickMeetingNOListener, OnClickActionListener, OnClickActionMoreListener, OnClickLinkPreviewListener, OnClickGiphyBtnListener, OnClickTemplateActionMoreListener, OnClickTemplateListener, OnClickReactionLabelListener {
        void onAddComment(MMMessageItem mMMessageItem);

        void onClickAddReactionLabel(View view, MMMessageItem mMMessageItem);

        void onClickReactionLabel(View view, MMMessageItem mMMessageItem, MMCommentsEmojiCountItem mMCommentsEmojiCountItem, boolean z);

        void onHideComment(MMMessageItem mMMessageItem);

        void onJumpResult(boolean z);

        void onLayoutCompleted();

        void onLoadingMore();

        boolean onLongClickAddReactionLabel(View view, MMMessageItem mMMessageItem);

        boolean onLongClickReactionLabel(View view, MMMessageItem mMMessageItem, MMCommentsEmojiCountItem mMCommentsEmojiCountItem);

        void onMessageShowed(MMMessageItem mMMessageItem);

        void onMoreComment(MMMessageItem mMMessageItem);

        void onNewMsgIdReady(String str);

        void onSayHi();

        void onShowFloatingText(View view, int i, boolean z);

        void onUnSupportEmojiReceived(String str);
    }

    public boolean Indicate_MarkUnreadContext(String str, int i, String str2, List<String> list) {
        return false;
    }

    public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
    }

    public MMThreadsRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public MMThreadsRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMThreadsRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void setMessageHelper(MMMessageHelper mMMessageHelper) {
        this.mMessageHelper = mMMessageHelper;
        this.mAdapter.setMessageHelper(mMMessageHelper);
    }

    public boolean isLoading(int i) {
        return this.mTheadReq.isLoading(i);
    }

    public boolean isLoading() {
        return this.mTheadReq.isLoading(2) || this.mTheadReq.isLoading(1);
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
                    ThreadDataResult threadData = threadDataProvider.getThreadData(this.mSessionId, 20, "", 1);
                    if (threadData != null) {
                        if (threadData.getCurrState() != 1) {
                            this.mIsFirstPageLoading = true;
                        }
                        this.mTheadReq.clear();
                        this.mTheadReq.updateReqInfo(threadData, "0");
                        this.mAdapter.clearDatas();
                        updateThreads(threadData, true);
                        scrollToBottom(true);
                    }
                }
            }
        }
    }

    private void setIsLocalMsgDirty(boolean z) {
        this.mIsLocalMsgDirty = z;
        this.mAdapter.setIsLocalMsgDirty(z);
    }

    public void showLatestThreads() {
        if (this.mIsLocalMsgDirty) {
            loadThreads(false, true);
        } else {
            scrollToBottom(true);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setIsResume(boolean z) {
        this.mIsResume = z;
    }

    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MMMessageListView.superState", onSaveInstanceState);
        bundle.putSerializable("MMMessageListView.mLinkPreviewReqIds", this.mLinkPreviewReqIds);
        bundle.putSerializable("MMMessageListView.mAnchorMessageInfo", this.mAnchorMessageInfo);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            Parcelable parcelable2 = bundle.getParcelable("MMMessageListView.superState");
            this.mAnchorMessageInfo = (MMContentMessageAnchorInfo) bundle.getSerializable("MMMessageListView.mAnchorMessageInfo");
            HashMap<String, String> hashMap = (HashMap) bundle.getSerializable("MMMessageListView.mLinkPreviewReqIds");
            if (hashMap != null) {
                this.mLinkPreviewReqIds = hashMap;
            }
            HashMap<String, String> hashMap2 = (HashMap) bundle.getSerializable("MMMessageListView.mGiphyReqIds");
            if (hashMap2 != null) {
                this.mGiphyReqIds = hashMap2;
            }
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public MMMessageItem addNewMessage(ZoomMessage zoomMessage) {
        return addNewMessage(zoomMessage, false);
    }

    public MMMessageItem addNewMessage(ZoomMessage zoomMessage, boolean z) {
        MMMessageItem mMMessageItem = null;
        if (this.mAnchorMessageInfo != null || zoomMessage == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
        if (threadDataProvider == null) {
            return null;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
        if (sessionById == null) {
            return null;
        }
        if (zoomMessage.isComment()) {
            String threadID = zoomMessage.getThreadID();
            if (TextUtils.isEmpty(threadID)) {
                return null;
            }
            int threadIndexById = this.mAdapter.getThreadIndexById(threadID);
            if (threadIndexById != -1) {
                BaseItem itemByPosition = this.mAdapter.getItemByPosition(threadIndexById);
                if (!(itemByPosition instanceof ThreadItem)) {
                    return null;
                }
                MMMessageItem mMMessageItem2 = itemByPosition.thread;
                if (mMMessageItem2 == null) {
                    return null;
                }
                ZoomMessage messageById = sessionById.getMessageById(mMMessageItem2.messageId);
                if (messageById != null) {
                    mMMessageItem2.hasCommentsOdds = 1;
                    mMMessageItem2.commentsCount = messageById.getTotalCommentsCount();
                    MMMessageHelper mMMessageHelper = this.mMessageHelper;
                    if (mMMessageHelper != null) {
                        ThrCommentState unreadCommentState = mMMessageHelper.getUnreadCommentState(messageById.getServerSideTime());
                        if (unreadCommentState != null) {
                            mMMessageItem2.unreadCommentCount = (long) unreadCommentState.getAllUnreadCount();
                        }
                    }
                }
                if (threadDataProvider.isThreadDirty(this.mSessionId, mMMessageItem2.messageId)) {
                    MessageSyncer.getInstance().syncThread(this.mSessionId, mMMessageItem2.messageId, mMMessageItem2.serverSideTime);
                }
                if (this.mThreadSortType != 0 || isMsgShow(threadID)) {
                    this.mAdapter.updateMessageItem(mMMessageItem2);
                    this.mAdapter.notifyItemChanged(threadIndexById);
                } else {
                    if (!this.mIsLocalMsgDirty) {
                        mMMessageItem2.updateThreadInfo();
                    }
                    this.mAdapter.moveToLatest(mMMessageItem2);
                    this.mAdapter.notifyDataSetChanged();
                }
                if (!isMsgDirty()) {
                    scrollToBottom(false);
                }
                return mMMessageItem2;
            } else if (this.mIsLocalMsgDirty) {
                return null;
            } else {
                if (this.mThreadSortType == 0) {
                    ZoomMessage messagePtr = threadDataProvider.getMessagePtr(this.mSessionId, threadID);
                    if (messagePtr != null) {
                        mMMessageItem = addNewMessage(messagePtr);
                    }
                }
                return mMMessageItem;
            }
        } else {
            sessionById.checkAutoDownloadForMessage(zoomMessage.getMessageID());
            ZoomMessage zoomMessage2 = zoomMessage;
            ZoomMessenger zoomMessenger2 = zoomMessenger;
            MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(zoomMessage2, this.mSessionId, zoomMessenger2, this.mIsGroupMessage, StringUtil.isSameString(zoomMessage.getSenderID(), this.myJid), getContext(), this.mIMAddrBookItem, PTApp.getInstance().getZoomFileContentMgr());
            if (initWithZoomMessage == null) {
                return null;
            }
            initWithZoomMessage.hasCommentsOdds = threadDataProvider.threadHasCommentsOdds(zoomMessage);
            MMMessageHelper mMMessageHelper2 = this.mMessageHelper;
            if (mMMessageHelper2 != null) {
                ThrCommentState unreadCommentState2 = mMMessageHelper2.getUnreadCommentState(initWithZoomMessage.serverSideTime);
                if (unreadCommentState2 != null) {
                    initWithZoomMessage.unreadCommentCount = (long) unreadCommentState2.getAllUnreadCount();
                }
            }
            checkAndDecodeE2EMsg(zoomMessenger, initWithZoomMessage);
            if (!isMsgDirty()) {
                this.mAdapter.moveToLatest(initWithZoomMessage);
                this.mAdapter.notifyDataSetChanged();
                scrollToBottom(false);
                return initWithZoomMessage;
            } else if (this.mThreadSortType != 1) {
                return null;
            } else {
                this.mAdapter.updateMessageItem(initWithZoomMessage);
                this.mAdapter.notifyDataSetChanged();
                return initWithZoomMessage;
            }
        }
    }

    public void cleanUnreadCommentCount(String str) {
        if (str != null) {
            MMMessageItem findMessageItemInAdapter = this.mAdapter.findMessageItemInAdapter(str);
            if (findMessageItemInAdapter != null) {
                findMessageItemInAdapter.unreadCommentCount = 0;
            }
        }
    }

    public boolean isThreadVisible(int i) {
        int findFirstVisibleItemPosition = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        int findLastVisibleItemPosition = this.mLinearLayoutManager.findLastVisibleItemPosition();
        boolean z = true;
        if (i >= findFirstVisibleItemPosition && i <= findLastVisibleItemPosition) {
            return true;
        }
        BaseItem itemByPosition = this.mAdapter.getItemByPosition(findFirstVisibleItemPosition);
        if (!(itemByPosition instanceof CommentItem)) {
            return false;
        }
        MMMessageItem mMMessageItem = ((CommentItem) itemByPosition).thread;
        if (mMMessageItem == null) {
            return false;
        }
        BaseItem itemByPosition2 = this.mAdapter.getItemByPosition(i);
        if (itemByPosition2 == null) {
            return false;
        }
        if (itemByPosition2.thread != mMMessageItem) {
            z = false;
        }
        return z;
    }

    public boolean isThreadVisible(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return isThreadVisible(this.mAdapter.getThreadIndexById(str));
    }

    public void OnGetCommentData(CommentDataResult commentDataResult) {
        if (commentDataResult != null) {
            if (((!TextUtils.isEmpty(commentDataResult.getDbReqId()) && this.mCommentsLoadingReqIds.remove(commentDataResult.getDbReqId())) || (!TextUtils.isEmpty(commentDataResult.getXmsReqId()) && this.mCommentsLoadingReqIds.remove(commentDataResult.getXmsReqId()))) || this.mAdapter.getItemByMessageId(commentDataResult.getThreadId()) != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                    if (threadDataProvider != null) {
                        ZoomMessage zoomMessage = commentDataResult.getThreadSvrT() != 0 ? threadDataProvider.getMessagePtr(this.mSessionId, commentDataResult.getThreadSvrT()) : !TextUtils.isEmpty(commentDataResult.getThreadId()) ? threadDataProvider.getMessagePtr(this.mSessionId, commentDataResult.getThreadId()) : null;
                        if (zoomMessage != null && !isMsgDirty()) {
                            MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(zoomMessage, this.mSessionId, zoomMessenger, this.mIsGroupMessage, TextUtils.equals(zoomMessage.getSenderID(), this.myJid), getContext(), this.mIMAddrBookItem, PTApp.getInstance().getZoomFileContentMgr());
                            if (initWithZoomMessage != null) {
                                initWithZoomMessage.hasCommentsOdds = threadDataProvider.threadHasCommentsOdds(zoomMessage);
                                checkAndDecodeE2EMsg(zoomMessenger, initWithZoomMessage);
                                if (this.mThreadSortType == 0) {
                                    this.mAdapter.moveToLatest(initWithZoomMessage);
                                } else {
                                    this.mAdapter.updateMessageItem(initWithZoomMessage);
                                }
                                this.mAdapter.notifyDataSetChanged();
                                scrollToBottom(false);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean OnGetThreadData(ThreadDataResult threadDataResult) {
        boolean z = false;
        if (threadDataResult == null || !TextUtils.equals(threadDataResult.getChannelId(), this.mSessionId) || !this.mTheadReq.match(threadDataResult)) {
            return false;
        }
        if (threadDataResult.getDir() == 2) {
            this.mPendingRecentData = threadDataResult;
        }
        if (threadDataResult.getCurrState() != 16 || this.mTheadReq.isLoading(threadDataResult.getDir())) {
            if (threadDataResult.getCurrState() == 1) {
                if (threadDataResult.getDir() == 1) {
                    this.mHistoryThreadDataResult = null;
                } else if (threadDataResult.getDir() == 2) {
                    this.mRecentThreadDataResult = null;
                }
            }
        } else if (threadDataResult.getDir() == 1) {
            this.mHistoryThreadDataResult = threadDataResult;
        } else if (threadDataResult.getDir() == 2) {
            this.mRecentThreadDataResult = threadDataResult;
        }
        if (this.mAnchorMessageInfo != null && this.mTheadReq.isLoading(threadDataResult.getDir())) {
            return false;
        }
        if (!isLoading(2)) {
            this.mAdapter.removeLoadingView();
        }
        updateThreads(threadDataResult, true);
        syncMessageEmojiCountInfo();
        if (this.mAnchorMessageInfo != null) {
            ThreadsUICallBack threadsUICallBack = this.mUICallBack;
            if (threadsUICallBack != null) {
                if (!isDataEmpty() || (threadDataResult.getCurrState() & 16) == 0) {
                    z = true;
                }
                threadsUICallBack.onJumpResult(z);
            }
        }
        return true;
    }

    public void OnThreadContextUpdate(String str, String str2) {
        onMessageSync(str, str2);
    }

    public MMMessageItem onMessageSync(String str, String str2) {
        if (isMsgDirty() && this.mAdapter.getItemByMessageId(str2) == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || zoomMessenger.getSessionById(str) == null) {
            return null;
        }
        ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
        if (threadDataProvider == null) {
            return null;
        }
        ZoomMessage messagePtr = threadDataProvider.getMessagePtr(str, str2);
        if (messagePtr == null) {
            return null;
        }
        boolean z = true;
        if (this.mThreadSortType == 1 && findThread(str2) == null) {
            long serverSideTime = messagePtr.getServerSideTime();
            MMMessageItem earliestThread = this.mAdapter.getEarliestThread();
            MMMessageItem latestThread = this.mAdapter.getLatestThread();
            if (earliestThread == null || latestThread == null || earliestThread.visibleTime >= serverSideTime || latestThread.visibleTime <= serverSideTime) {
                z = false;
            }
            if (!z) {
                return null;
            }
        }
        return addNewMessage(messagePtr);
    }

    public void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z) {
        if (list != null) {
            this.mSyncMessageIDs.removeAll(list);
        }
        this.mAdapter.notifyDataSetChanged();
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

    public void OnSyncThreadCommentCount(String str, String str2, List<String> list, boolean z) {
        if (z && !CollectionsUtil.isCollectionEmpty(list)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    boolean z2 = false;
                    for (String str3 : list) {
                        MMMessageItem itemByMessageId = this.mAdapter.getItemByMessageId(str3);
                        if (itemByMessageId != null) {
                            z2 = true;
                            ZoomMessage messagePtr = threadDataProvider.getMessagePtr(this.mSessionId, str3);
                            if (messagePtr != null) {
                                itemByMessageId.hasCommentsOdds = threadDataProvider.threadHasCommentsOdds(messagePtr);
                                itemByMessageId.commentsCount = messagePtr.getTotalCommentsCount();
                            }
                        }
                    }
                    if (z2) {
                        this.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void init() {
        setItemAnimator(null);
        this.mLinearLayoutManager = new LinearLayoutManager(getContext()) {
            public LayoutParams generateDefaultLayoutParams() {
                return new LayoutParams(-1, -2);
            }

            public void onLayoutCompleted(State state) {
                super.onLayoutCompleted(state);
                if (!MMThreadsRecyclerView.this.isLayoutReady) {
                    MMThreadsRecyclerView.this.isLayoutReady = true;
                    if (MMThreadsRecyclerView.this.mUICallBack != null) {
                        MMThreadsRecyclerView.this.mUICallBack.onLayoutCompleted();
                    }
                }
            }
        };
        setLayoutManager(this.mLinearLayoutManager);
        this.mAdapter = new MMThreadsAdapter(getContext(), this.mSessionId);
        setAdapter(this.mAdapter);
        addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                MMThreadsRecyclerView.this.onRecyclerViewScrollStateChanged(i);
            }

            public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                if (MMThreadsRecyclerView.this.mLinearLayoutManager.findLastVisibleItemPosition() == MMThreadsRecyclerView.this.mAdapter.getItemCount() - 1 && MMThreadsRecyclerView.this.mAnchorMessageInfo == null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(MMThreadsRecyclerView.this.mSessionId);
                        if (sessionById != null && sessionById.getUnreadMessageCount() > 0) {
                            MessageSyncer.getInstance().cleanUnreadMessageCount(MMThreadsRecyclerView.this.mSessionId);
                        }
                    }
                }
            }
        });
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                this.myJid = myself.getJid();
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    this.mThreadSortType = threadDataProvider.getThreadSortType();
                    this.mGestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
                        boolean isHappen;

                        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                            if (!this.isHappen) {
                                if (f2 > 60.0f) {
                                    MMThreadsRecyclerView.this.mParentFragment.showSearchView(false);
                                    this.isHappen = true;
                                } else if ((-f2) > 60.0f) {
                                    MMThreadsRecyclerView.this.mParentFragment.showSearchView(true);
                                    this.isHappen = true;
                                }
                            }
                            return super.onScroll(motionEvent, motionEvent2, f, f2);
                        }

                        public boolean onDown(MotionEvent motionEvent) {
                            this.isHappen = false;
                            return super.onDown(motionEvent);
                        }
                    });
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onRecyclerViewScrollStateChanged(int i) {
        if (i == 0) {
            if (this.mIsLocalMsgDirty && this.mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == this.mAdapter.getItemCount() - 1) {
                loadMoreThreads(2);
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

    public void setSessionInfo(@NonNull String str, boolean z) {
        this.mSessionId = str;
        this.mIsGroupMessage = z;
        if (!z) {
            this.mIMAddrBookItem = ZMBuddySyncInstance.getInsatance().getBuddyByJid(str, true);
        }
        this.mAdapter.setSessionInfo(str, z, this.mIMAddrBookItem);
    }

    public MMMessageItem findThread(String str) {
        return this.mAdapter.getThreadById(str);
    }

    public MMMessageItem findMessageItemByStamp(long j) {
        return this.mAdapter.findMessageItemInAdapter(j);
    }

    public void loadThreads(boolean z) {
        loadThreads(z, false, null);
    }

    public void loadThreads(boolean z, String str) {
        loadThreads(z, false, str);
    }

    public void loadThreads(boolean z, boolean z2) {
        loadThreads(z, z2, null);
    }

    public void loadThreads(boolean z, boolean z2, String str) {
        loadThreads(z, z2, str, false);
    }

    public void loadThreads(boolean z, boolean z2, String str, boolean z3) {
        ThreadDataResult threadDataResult;
        ThreadDataResult threadDataResult2;
        if (z3) {
            int i = this.mFirstSyncRetryTimes;
            if (i > 2) {
                this.mIsFirstPageDirty = true;
                return;
            }
            this.mFirstSyncRetryTimes = i + 1;
        } else {
            this.mFirstSyncRetryTimes = 1;
        }
        if (!z || this.mAdapter.isEmpty()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                if (threadDataProvider != null) {
                    this.mAdapter.clearDatas();
                    this.mTheadReq.clear();
                    this.mPendingRecentData = null;
                    this.mRecentThreadDataResult = null;
                    this.mHistoryThreadDataResult = null;
                    MMContentMessageAnchorInfo mMContentMessageAnchorInfo = this.mAnchorMessageInfo;
                    int i2 = 20;
                    if (mMContentMessageAnchorInfo == null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                        if (sessionById != null) {
                            if (TextUtils.equals(str, MMMessageItem.LAST_MSG_MARK_MSGID)) {
                                str = null;
                                z2 = true;
                            }
                            if (TextUtils.isEmpty(str)) {
                                boolean needJumpToStart = needJumpToStart();
                                int i3 = 40;
                                if (!z2) {
                                    long j = this.mUnreadMsgTimestamp;
                                    if (j != 0 && needJumpToStart && this.mUnreadMsgCount >= 40) {
                                        ThreadDataProvider threadDataProvider2 = threadDataProvider;
                                        ThreadDataResult threadData = threadDataProvider2.getThreadData(this.mSessionId, 20, j, 2);
                                        ThreadDataResult threadData2 = threadDataProvider2.getThreadData(this.mSessionId, 20, this.mUnreadMsgTimestamp, 1);
                                        if (threadData != null || threadData2 != null) {
                                            setIsLocalMsgDirty(true);
                                            this.mTheadReq.updateReqInfo(threadData, MMMessageItem.NEW_MSG_MARK_MSGID);
                                            this.mTheadReq.updateReqInfo(threadData2, MMMessageItem.NEW_MSG_MARK_MSGID);
                                            if (this.mTheadReq.isLoading(2) || this.mTheadReq.isLoading(1)) {
                                                this.mIsFirstPageLoading = true;
                                            }
                                            if (threadData2 != null) {
                                                updateThreads(threadData2, false);
                                            }
                                            if (threadData != null) {
                                                updateThreads(threadData, false);
                                            }
                                            ThreadsUICallBack threadsUICallBack = this.mUICallBack;
                                            if (threadsUICallBack != null) {
                                                threadsUICallBack.onNewMsgIdReady(MMMessageItem.LAST_MSG_MARK_MSGID);
                                            }
                                        } else {
                                            return;
                                        }
                                    }
                                }
                                int i4 = this.mUnreadMsgCount;
                                if (i4 + 1 > 20) {
                                    i2 = i4 + 1;
                                }
                                if (i2 <= 40) {
                                    i3 = i2;
                                }
                                ThreadDataResult threadData3 = threadDataProvider.getThreadData(this.mSessionId, i3, "", 1);
                                if (threadData3 != null) {
                                    if (threadData3.getCurrState() != 1) {
                                        this.mIsFirstPageLoading = true;
                                    }
                                    setIsLocalMsgDirty(false);
                                    if (this.mUnreadMsgCount <= 0 || !needJumpToStart || z2) {
                                        this.mTheadReq.updateReqInfo(threadData3);
                                    } else {
                                        this.mTheadReq.updateReqInfo(threadData3, MMMessageItem.NEW_MSG_MARK_MSGID);
                                    }
                                    updateThreads(threadData3, false);
                                    if (z2 || !needJumpToStart) {
                                        scrollToBottom(true);
                                    }
                                    ThreadsUICallBack threadsUICallBack2 = this.mUICallBack;
                                    if (!(threadsUICallBack2 == null || this.mUnreadMsgTimestamp == 0 || this.mUnreadMsgCount <= 0)) {
                                        if (needJumpToStart) {
                                            threadsUICallBack2.onNewMsgIdReady(MMMessageItem.LAST_MSG_MARK_MSGID);
                                        } else {
                                            threadsUICallBack2.onNewMsgIdReady(MMMessageItem.NEW_MSG_MARK_MSGID);
                                        }
                                    }
                                }
                            } else if (TextUtils.equals(str, MMMessageItem.NEW_MSG_MARK_MSGID)) {
                                ThreadDataProvider threadDataProvider3 = threadDataProvider;
                                ThreadDataResult threadData4 = threadDataProvider3.getThreadData(this.mSessionId, 20, this.mUnreadMsgTimestamp, 1);
                                ThreadDataResult threadData5 = threadDataProvider3.getThreadData(this.mSessionId, 20, this.mUnreadMsgTimestamp, 2);
                                if (threadData5 != null || threadData4 != null) {
                                    this.mTheadReq.updateReqInfo(threadData5, MMMessageItem.NEW_MSG_MARK_MSGID);
                                    this.mTheadReq.updateReqInfo(threadData4, MMMessageItem.NEW_MSG_MARK_MSGID);
                                    if (this.mTheadReq.isLoading(2) || this.mTheadReq.isLoading(1)) {
                                        this.mIsFirstPageLoading = true;
                                    }
                                    if (threadData4 != null) {
                                        updateThreads(threadData4, false);
                                    }
                                    if (threadData5 != null) {
                                        updateThreads(threadData5, false);
                                    }
                                    setIsLocalMsgDirty(true);
                                }
                            } else {
                                ZoomMessage messageById = sessionById.getMessageById(str);
                                if (messageById != null && !messageById.isComment()) {
                                    ThreadDataProvider threadDataProvider4 = threadDataProvider;
                                    String str2 = str;
                                    ThreadDataResult threadData6 = threadDataProvider4.getThreadData(this.mSessionId, 20, str2, messageById.getServerSideTime(), 1);
                                    ThreadDataResult threadData7 = threadDataProvider4.getThreadData(this.mSessionId, 20, str2, messageById.getServerSideTime(), 2);
                                    if (threadData7 != null || threadData6 != null) {
                                        this.mTheadReq.updateReqInfo(threadData7, str);
                                        this.mTheadReq.updateReqInfo(threadData6, str);
                                        if (this.mTheadReq.isLoading(2) || this.mTheadReq.isLoading(1)) {
                                            this.mIsFirstPageLoading = true;
                                        }
                                        if (threadData6 != null) {
                                            updateThreads(threadData6, false);
                                        }
                                        if (threadData7 != null) {
                                            updateThreads(threadData7, false);
                                        }
                                        setIsLocalMsgDirty(true);
                                    }
                                }
                            }
                        }
                    } else if (!mMContentMessageAnchorInfo.isComment() && TextUtils.isEmpty(this.mAnchorMessageInfo.getThrId())) {
                        String msgGuid = this.mAnchorMessageInfo.getMsgGuid();
                        if (!TextUtils.isEmpty(msgGuid) || this.mAnchorMessageInfo.getSendTime() != 0) {
                            ZoomMessage messagePtr = this.mAnchorMessageInfo.getSendTime() != 0 ? threadDataProvider.getMessagePtr(this.mSessionId, this.mAnchorMessageInfo.getSendTime()) : threadDataProvider.getMessagePtr(this.mSessionId, msgGuid);
                            if (messagePtr == null || !threadDataProvider.isThreadCommentInfoAccurate(messagePtr)) {
                                ThreadDataProvider threadDataProvider5 = threadDataProvider;
                                String str3 = msgGuid;
                                threadDataResult2 = threadDataProvider5.getThreadData(this.mSessionId, 20, str3, this.mAnchorMessageInfo.getSendTime(), 2);
                                threadDataResult = threadDataProvider5.getThreadData(this.mSessionId, 20, str3, this.mAnchorMessageInfo.getSendTime(), 1);
                            } else {
                                threadDataResult2 = threadDataProvider.getThreadData(this.mSessionId, 20, msgGuid, 2);
                                threadDataResult = threadDataProvider.getThreadData(this.mSessionId, 20, msgGuid, 1);
                            }
                            if (threadDataResult != null || threadDataResult2 != null) {
                                if (TextUtils.isEmpty(msgGuid)) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("");
                                    sb.append(this.mAnchorMessageInfo.getSendTime());
                                    msgGuid = sb.toString();
                                }
                                if (threadDataResult2 != null) {
                                    this.mTheadReq.updateReqInfo(threadDataResult2, msgGuid);
                                    if (TextUtils.isEmpty(threadDataResult2.getDbReqId()) && TextUtils.isEmpty(threadDataResult2.getXmsReqId())) {
                                        updateThreads(threadDataResult2, false);
                                    }
                                }
                                if (threadDataResult != null) {
                                    this.mTheadReq.updateReqInfo(threadDataResult, msgGuid);
                                    if (TextUtils.isEmpty(threadDataResult.getDbReqId()) && TextUtils.isEmpty(threadDataResult.getXmsReqId())) {
                                        updateThreads(threadDataResult, false);
                                    }
                                }
                                if (!isDataEmpty()) {
                                    this.mUICallBack.onJumpResult(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean loadMoreThreads(int i) {
        MMMessageItem mMMessageItem;
        if ((i != 2 && i != 1) || isLoading(2) || isLoading(1)) {
            return false;
        }
        if (this.mIsFirstPageDirty) {
            loadThreads(false, true, null);
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
        if (threadDataProvider == null) {
            return false;
        }
        if (i == 1) {
            mMMessageItem = this.mAdapter.getEarliestThread();
        } else {
            mMMessageItem = this.mAdapter.getLatestThread();
        }
        if (mMMessageItem == null) {
            loadThreads(false, true, null);
            return false;
        }
        String str = mMMessageItem.messageId;
        if (zoomMessenger.isConnectionGood()) {
            if (i == 1) {
                ThreadDataResult threadDataResult = this.mHistoryThreadDataResult;
                if (threadDataResult != null) {
                    str = threadDataResult.getStartThread();
                }
            }
            if (i == 2) {
                ThreadDataResult threadDataResult2 = this.mRecentThreadDataResult;
                if (threadDataResult2 != null) {
                    str = threadDataResult2.getStartThread();
                }
            }
        }
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (i == 1 && !threadDataProvider.moreHistoricThreads(this.mSessionId, str)) {
            return true;
        }
        if (i == 2 && !threadDataProvider.moreRecentThreads(this.mSessionId, str)) {
            return true;
        }
        ThreadDataResult threadData = threadDataProvider.getThreadData(this.mSessionId, 21, str, i);
        if (threadData == null) {
            return false;
        }
        this.mTheadReq.updateReqInfo(threadData, str);
        updateThreads(threadData, false);
        if (threadData.getCurrState() == 1) {
            if (i == 1) {
                this.mHistoryThreadDataResult = null;
            } else {
                this.mRecentThreadDataResult = null;
            }
            syncMessageEmojiCountInfo();
        }
        return false;
    }

    public boolean isAtBottom() {
        return this.mLinearLayoutManager.findLastVisibleItemPosition() >= this.mAdapter.getItemCount() - 1;
    }

    public boolean needJumpToStart() {
        boolean z = true;
        if (!this.mIsGroupMessage) {
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

    public boolean isResultLoadingFinish(@Nullable ThreadDataResult threadDataResult) {
        return threadDataResult != null && (threadDataResult.getCurrState() == 1 || (threadDataResult.getCurrState() & 16) != 0);
    }

    public boolean hasUnreadMark() {
        return this.mAdapter.hasUnreadMark();
    }

    public boolean isMsgDirty() {
        return this.mIsLocalMsgDirty;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:117:0x02f4, code lost:
        if (r4.moreRecentThreads(r0.mSessionId, (java.lang.String) r23.get(r23.size() - 1)) == false) goto L_0x02f6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateThreads(com.zipow.videobox.ptapp.IMProtos.ThreadDataResult r32, boolean r33) {
        /*
            r31 = this;
            r0 = r31
            r1 = r32
            r2 = 1
            r5 = 0
            if (r1 == 0) goto L_0x0361
            int r6 = r32.getThreadIdsCount()
            if (r6 != 0) goto L_0x0011
            goto L_0x0361
        L_0x0011:
            if (r33 != 0) goto L_0x001e
            java.lang.String r6 = r32.getDbReqId()
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 != 0) goto L_0x001e
            return
        L_0x001e:
            com.zipow.videobox.ptapp.PTApp r6 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r6 = r6.getZoomMessenger()
            if (r6 != 0) goto L_0x0029
            return
        L_0x0029:
            com.zipow.videobox.ptapp.ThreadDataProvider r15 = r6.getThreadDataProvider()
            if (r15 != 0) goto L_0x0030
            return
        L_0x0030:
            java.lang.String r7 = r0.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r14 = r6.findSessionById(r7)
            if (r14 != 0) goto L_0x0039
            return
        L_0x0039:
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            java.util.ArrayList r12 = new java.util.ArrayList
            java.util.List r7 = r32.getThreadIdsList()
            r12.<init>(r7)
            boolean r7 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r12)
            if (r7 == 0) goto L_0x004e
            return
        L_0x004e:
            java.lang.String r7 = r0.myJid
            if (r7 != 0) goto L_0x0053
            return
        L_0x0053:
            boolean r7 = r0.mIsFirstPageLoading
            r16 = 0
            r11 = 1
            if (r7 == 0) goto L_0x00a7
            long r7 = r32.getCurrState()
            int r7 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r7 == 0) goto L_0x006d
            long r7 = r32.getCurrState()
            r9 = 16
            long r7 = r7 & r9
            int r7 = (r7 > r16 ? 1 : (r7 == r16 ? 0 : -1))
            if (r7 == 0) goto L_0x00a7
        L_0x006d:
            int r7 = r32.getDir()
            if (r7 != r11) goto L_0x00a7
            com.zipow.videobox.view.mm.MMThreadsAdapter r7 = r0.mAdapter
            r7.clearDatas()
            long r7 = r32.getCurrState()
            int r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x0082
            r2 = 1
            goto L_0x0083
        L_0x0082:
            r2 = 0
        L_0x0083:
            r0.mIsFirstPageDirty = r2
            boolean r2 = r0.mIsFirstPageDirty
            r3 = 0
            if (r2 == 0) goto L_0x009c
            boolean r2 = r6.isConnectionGood()
            if (r2 == 0) goto L_0x009c
            r0.loadThreads(r5, r11, r3, r11)
            com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadReqInfo r1 = r0.mTheadReq
            boolean r1 = r1.isLoading(r11)
            r0.mIsFirstPageLoading = r1
            return
        L_0x009c:
            r0.mIsFirstPageLoading = r5
            com.zipow.videobox.ptapp.IMProtos$ThreadDataResult r2 = r0.mPendingRecentData
            if (r2 == 0) goto L_0x00a7
            r0.updateThreads(r2, r11)
            r0.mPendingRecentData = r3
        L_0x00a7:
            com.zipow.videobox.view.mm.MMThreadsAdapter r2 = r0.mAdapter
            java.util.List r2 = r2.getAllThreads()
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>()
            java.util.Iterator r2 = r2.iterator()
        L_0x00b6:
            boolean r7 = r2.hasNext()
            if (r7 == 0) goto L_0x00d0
            java.lang.Object r7 = r2.next()
            com.zipow.videobox.view.mm.MMMessageItem r7 = (com.zipow.videobox.view.p014mm.MMMessageItem) r7
            java.lang.String r8 = r7.messageId
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r8)
            if (r8 != 0) goto L_0x00b6
            java.lang.String r8 = r7.messageId
            r3.put(r8, r7)
            goto L_0x00b6
        L_0x00d0:
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>()
            com.zipow.videobox.ptapp.PTApp r7 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.MMFileContentMgr r18 = r7.getZoomFileContentMgr()
            int r7 = r14.getUnreadMessageCount()
            java.util.HashSet r10 = new java.util.HashSet
            r10.<init>()
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            boolean r19 = r31.isResultLoadingFinish(r32)
            r8 = 0
        L_0x00f0:
            int r7 = r12.size()
            if (r8 >= r7) goto L_0x025b
            java.lang.Object r7 = r12.get(r8)
            java.lang.String r7 = (java.lang.String) r7
            java.lang.String r11 = r0.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomMessage r11 = r15.getMessagePtr(r11, r7)
            if (r11 != 0) goto L_0x0112
            r11 = r2
            r5 = r9
            r22 = r10
            r23 = r12
            r9 = r13
            r7 = r14
            r4 = r15
            r10 = r3
            r3 = r8
            r8 = r6
            goto L_0x0249
        L_0x0112:
            boolean r20 = r11.containCommentFeature()
            if (r20 == 0) goto L_0x0131
            long r20 = r11.getServerSideTime()
            java.lang.Long r7 = java.lang.Long.valueOf(r20)
            r10.add(r7)
            r11 = r2
            r5 = r9
            r22 = r10
            r23 = r12
            r9 = r13
            r7 = r14
            r4 = r15
            r10 = r3
            r3 = r8
            r8 = r6
            goto L_0x0249
        L_0x0131:
            java.lang.String r5 = r11.getSenderID()
            java.lang.String r4 = r0.myJid
            boolean r4 = p021us.zoom.androidlib.util.StringUtil.isSameString(r5, r4)
            java.lang.String r5 = r0.mSessionId
            r22 = r10
            boolean r10 = r0.mIsGroupMessage
            android.content.Context r23 = r31.getContext()
            r24 = r13
            com.zipow.videobox.view.IMAddrBookItem r13 = r0.mIMAddrBookItem
            r25 = r2
            r2 = r7
            r7 = r11
            r26 = r3
            r3 = r8
            r8 = r5
            r5 = r9
            r9 = r6
            r33 = r6
            r27 = r11
            r6 = 1
            r11 = r4
            r4 = r12
            r12 = r23
            r28 = r24
            r29 = r14
            r14 = r18
            com.zipow.videobox.view.mm.MMMessageItem r14 = com.zipow.videobox.view.p014mm.MMMessageItem.initWithZoomMessage(r7, r8, r9, r10, r11, r12, r13, r14)
            if (r14 != 0) goto L_0x0177
            r8 = r33
            r23 = r4
            r4 = r15
            r11 = r25
            r10 = r26
            r9 = r28
            r7 = r29
            goto L_0x0249
        L_0x0177:
            int r7 = r14.hasCommentsOdds
            if (r7 != r6) goto L_0x0180
            com.zipow.videobox.view.mm.MMThreadsAdapter r7 = r0.mAdapter
            r7.removeItem(r2)
        L_0x0180:
            com.zipow.videobox.util.MMMessageHelper r7 = r0.mMessageHelper
            if (r7 == 0) goto L_0x0193
            long r8 = r14.serverSideTime
            com.zipow.videobox.util.MMMessageHelper$ThrCommentState r7 = r7.getUnreadCommentState(r8)
            if (r7 == 0) goto L_0x0193
            int r7 = r7.getAllUnreadCount()
            long r7 = (long) r7
            r14.unreadCommentCount = r7
        L_0x0193:
            java.lang.String r7 = r0.mSessionId
            long r8 = r14.serverSideTime
            boolean r7 = r15.isThreadCommentCountSynced(r7, r8)
            if (r7 != 0) goto L_0x01a6
            long r7 = r14.serverSideTime
            java.lang.Long r7 = java.lang.Long.valueOf(r7)
            r5.add(r7)
        L_0x01a6:
            com.zipow.videobox.ptapp.IMProtos$CommentIndexList r7 = r1.getThreadComments(r3)
            java.util.List r8 = r7.getCommonIdList()
            r9 = 1
            r11 = 2
            com.zipow.videobox.view.IMAddrBookItem r13 = r0.mIMAddrBookItem
            r7 = r14
            r10 = r15
            r12 = r33
            r23 = r13
            r13 = r18
            r6 = r14
            r14 = r23
            r7.updateCommentsInThread(r8, r9, r10, r11, r12, r13, r14)
            r14 = r27
            int r7 = r15.threadHasCommentsOdds(r14)
            r6.hasCommentsOdds = r7
            int r7 = r6.hasCommentsOdds
            r8 = 2
            if (r7 == r8) goto L_0x01e6
            java.lang.String r7 = r0.mSessionId
            boolean r7 = r15.isThreadDirty(r7, r2)
            if (r7 == 0) goto L_0x01e6
            com.zipow.videobox.util.MMMessageHelper$MessageSyncer r7 = com.zipow.videobox.util.MMMessageHelper.MessageSyncer.getInstance()
            java.lang.String r8 = r0.mSessionId
            long r9 = r6.serverSideTime
            r7.syncThread(r8, r2, r9)
            r23 = r4
            r4 = r15
            r7 = r29
            goto L_0x0221
        L_0x01e6:
            if (r19 == 0) goto L_0x021c
            int r7 = r6.hasCommentsOdds
            if (r7 != 0) goto L_0x021c
            long r7 = r6.commentsCount
            int r7 = (r7 > r16 ? 1 : (r7 == r16 ? 0 : -1))
            if (r7 != 0) goto L_0x021c
            long r7 = r6.serverSideTime
            int r7 = (r7 > r16 ? 1 : (r7 == r16 ? 0 : -1))
            if (r7 == 0) goto L_0x021c
            java.lang.String r8 = r0.mSessionId
            r9 = 1
            r11 = 0
            java.lang.String r13 = ""
            r23 = 1
            r27 = 0
            r7 = r15
            r10 = r2
            r30 = r14
            r14 = r23
            r23 = r4
            r4 = r15
            r15 = r27
            r7.getCommentData(r8, r9, r10, r11, r13, r14, r15)
            r7 = r30
            int r7 = r4.threadHasCommentsOdds(r7)
            r6.hasCommentsOdds = r7
            r7 = r29
            goto L_0x0221
        L_0x021c:
            r23 = r4
            r4 = r15
            r7 = r29
        L_0x0221:
            r7.checkAutoDownloadForMessage(r2)
            r8 = r33
            r0.checkAndDecodeE2EMsg(r8, r6)
            java.lang.String r9 = r6.messageId
            r10 = r26
            java.lang.Object r9 = r10.get(r9)
            com.zipow.videobox.view.mm.MMMessageItem r9 = (com.zipow.videobox.view.p014mm.MMMessageItem) r9
            if (r9 == 0) goto L_0x023c
            boolean r9 = r9.isDownloading
            r6.isDownloading = r9
            r9 = r28
            goto L_0x023e
        L_0x023c:
            r9 = r28
        L_0x023e:
            r9.add(r6)
            r11 = r25
            r11.add(r2)
            r0.checkLinkPreviewAndOther(r6)
        L_0x0249:
            int r2 = r3 + 1
            r15 = r4
            r14 = r7
            r6 = r8
            r13 = r9
            r3 = r10
            r10 = r22
            r12 = r23
            r8 = r2
            r9 = r5
            r2 = r11
            r5 = 0
            r11 = 1
            goto L_0x00f0
        L_0x025b:
            r11 = r2
            r5 = r9
            r22 = r10
            r23 = r12
            r9 = r13
            r4 = r15
            boolean r2 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r5)
            if (r2 != 0) goto L_0x026e
            java.lang.String r2 = r0.mSessionId
            r4.syncThreadCommentCount(r2, r5)
        L_0x026e:
            boolean r2 = r22.isEmpty()
            if (r2 != 0) goto L_0x02b6
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.UnSupportMessageMgr r2 = r2.getUnsupportMessageMgr()
            if (r2 == 0) goto L_0x02b6
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            java.util.Iterator r5 = r22.iterator()
        L_0x0287:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x02ab
            java.lang.Object r6 = r5.next()
            java.lang.Long r6 = (java.lang.Long) r6
            java.lang.String r6 = r6.toString()
            r3.add(r6)
            int r6 = r3.size()
            r7 = 20
            if (r6 != r7) goto L_0x0287
            java.lang.String r6 = r0.mSessionId
            r2.searchUnSupportMessages(r6, r3)
            r3.clear()
            goto L_0x0287
        L_0x02ab:
            boolean r5 = r3.isEmpty()
            if (r5 != 0) goto L_0x02b6
            java.lang.String r5 = r0.mSessionId
            r2.searchUnSupportMessages(r5, r3)
        L_0x02b6:
            com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadReqInfo r2 = r0.mTheadReq
            com.zipow.videobox.ptapp.IMProtos$ThreadDataResult r2 = r2.getLastResult(r1)
            if (r2 == 0) goto L_0x02c5
            java.util.List r2 = r2.getThreadIdsList()
            r11.addAll(r2)
        L_0x02c5:
            com.zipow.videobox.view.mm.MMThreadsAdapter r2 = r0.mAdapter
            r2.removeLocalMsgs(r11)
            com.zipow.videobox.view.mm.MMThreadsAdapter r2 = r0.mAdapter
            int r3 = r32.getDir()
            r2.addMessages(r9, r3)
            r2 = 2
            boolean r3 = r0.isLoading(r2)
            if (r3 != 0) goto L_0x02fa
            boolean r2 = r23.isEmpty()
            if (r2 != 0) goto L_0x02f6
            java.lang.String r2 = r0.mSessionId
            int r3 = r23.size()
            r5 = 1
            int r3 = r3 - r5
            r5 = r23
            java.lang.Object r3 = r5.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r2 = r4.moreRecentThreads(r2, r3)
            if (r2 != 0) goto L_0x02fa
        L_0x02f6:
            r2 = 0
            r0.setIsLocalMsgDirty(r2)
        L_0x02fa:
            r31.refreshStarMsgs()
            r31.updateEmptyView()
            com.zipow.videobox.view.mm.MMThreadsAdapter r2 = r0.mAdapter
            r2.notifyDataSetChanged()
            com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadReqInfo r2 = r0.mTheadReq
            java.lang.String r2 = r2.getAnchorMsgId(r1)
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 == 0) goto L_0x0316
            r3 = 1
            r0.scrollToBottom(r3)
            goto L_0x0335
        L_0x0316:
            boolean r3 = r0.scrollToMessage(r2)
            if (r3 != 0) goto L_0x0332
            long r2 = java.lang.Long.parseLong(r2)     // Catch:{ Exception -> 0x0321 }
            goto L_0x0323
        L_0x0321:
            r2 = r16
        L_0x0323:
            int r5 = (r2 > r16 ? 1 : (r2 == r16 ? 0 : -1))
            if (r5 == 0) goto L_0x032d
            boolean r2 = r0.scrollToMessage(r2)
            if (r2 != 0) goto L_0x0335
        L_0x032d:
            r2 = 1
            r0.scrollToBottom(r2)
            goto L_0x0335
        L_0x0332:
            r31.removeScrollBottomMsg()
        L_0x0335:
            com.zipow.videobox.view.mm.MMThreadsRecyclerView$ThreadReqInfo r2 = r0.mTheadReq
            r2.updateDataResult(r1)
            int r1 = r32.getDir()
            r2 = 2
            if (r1 != r2) goto L_0x0360
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r0.mAnchorMessageInfo
            if (r1 != 0) goto L_0x0360
            com.zipow.videobox.view.mm.MMThreadsAdapter r1 = r0.mAdapter
            com.zipow.videobox.view.mm.MMMessageItem r1 = r1.getLatestThread()
            if (r1 == 0) goto L_0x0360
            java.lang.String r2 = r0.mSessionId
            java.lang.String r1 = r1.messageId
            boolean r1 = r4.moreRecentThreads(r2, r1)
            if (r1 != 0) goto L_0x0360
            r1 = 0
            r0.setIsLocalMsgDirty(r1)
            com.zipow.videobox.view.mm.MMThreadsAdapter r1 = r0.mAdapter
            r1.removeLoadingView()
        L_0x0360:
            return
        L_0x0361:
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r4 = r0.mAnchorMessageInfo
            if (r4 != 0) goto L_0x037f
            if (r1 == 0) goto L_0x037f
            long r4 = r32.getCurrState()
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 != 0) goto L_0x037f
            int r2 = r32.getDir()
            r3 = 2
            if (r2 != r3) goto L_0x037f
            r2 = 0
            r0.setIsLocalMsgDirty(r2)
            com.zipow.videobox.view.mm.MMThreadsAdapter r2 = r0.mAdapter
            r2.checkUnreadMarkWhenrecentMsgFinish()
        L_0x037f:
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r2 = r0.mAnchorMessageInfo
            if (r2 == 0) goto L_0x03c2
            boolean r2 = r2.isFromMarkUnread()
            if (r2 == 0) goto L_0x03c2
            boolean r2 = r31.isLoading()
            if (r2 != 0) goto L_0x03c2
            if (r1 == 0) goto L_0x03c2
            java.util.List r1 = r32.getThreadIdsList()
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r2 = r0.mAnchorMessageInfo
            java.lang.String r2 = r2.getMsgGuid()
            boolean r1 = r1.contains(r2)
            if (r1 != 0) goto L_0x03c2
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r1 = r1.getZoomMessenger()
            if (r1 == 0) goto L_0x03c2
            java.lang.String r2 = r0.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r1 = r1.getSessionById(r2)
            if (r1 == 0) goto L_0x03c2
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r2 = r0.mAnchorMessageInfo
            long r2 = r2.getServerTime()
            r1.unmarkUnreadMessageBySvrTime(r2)
            com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo r1 = r0.mAnchorMessageInfo
            r2 = 0
            r1.setFromMarkUnread(r2)
        L_0x03c2:
            r31.updateEmptyView()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMThreadsRecyclerView.updateThreads(com.zipow.videobox.ptapp.IMProtos$ThreadDataResult, boolean):void");
    }

    public void notifyStarMessageDataUpdate() {
        if (this.mAdapter != null) {
            refreshStarMsgs();
            this.mAdapter.notifyDataSetChanged();
        }
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

    private void checkAndDecodeE2EMsg(ZoomMessenger zoomMessenger, MMMessageItem mMMessageItem) {
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

    private void jumpToAnchor() {
        MMContentMessageAnchorInfo mMContentMessageAnchorInfo = this.mAnchorMessageInfo;
        if (mMContentMessageAnchorInfo != null) {
            scrollToMessage(mMContentMessageAnchorInfo.getMsgGuid());
        }
    }

    @Nullable
    public MMMessageItem getFirstVisibleItem() {
        int findFirstCompletelyVisibleItemPosition = this.mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (findFirstCompletelyVisibleItemPosition == -1) {
            findFirstCompletelyVisibleItemPosition = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        }
        MMMessageItem mMMessageItem = null;
        if (findFirstCompletelyVisibleItemPosition == -1) {
            return null;
        }
        while (mMMessageItem == null && findFirstCompletelyVisibleItemPosition < this.mAdapter.getItemCount()) {
            BaseItem itemByPosition = this.mAdapter.getItemByPosition(findFirstCompletelyVisibleItemPosition);
            if (itemByPosition instanceof ThreadItem) {
                MMMessageItem mMMessageItem2 = ((ThreadItem) itemByPosition).thread;
                if (mMMessageItem2.messageType == 19) {
                    mMMessageItem2 = mMMessageItem;
                }
                mMMessageItem = mMMessageItem2;
            } else if (itemByPosition instanceof CommentItem) {
                mMMessageItem = ((CommentItem) itemByPosition).comment;
            }
            findFirstCompletelyVisibleItemPosition++;
        }
        return mMMessageItem;
    }

    @Nullable
    public MMMessageItem getLastVisibleItem() {
        int findLastCompletelyVisibleItemPosition = this.mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        if (findLastCompletelyVisibleItemPosition == -1) {
            findLastCompletelyVisibleItemPosition = this.mLinearLayoutManager.findLastVisibleItemPosition();
        }
        MMMessageItem mMMessageItem = null;
        if (findLastCompletelyVisibleItemPosition == -1) {
            return null;
        }
        while (mMMessageItem == null && findLastCompletelyVisibleItemPosition >= 0) {
            BaseItem itemByPosition = this.mAdapter.getItemByPosition(findLastCompletelyVisibleItemPosition);
            if (itemByPosition instanceof ThreadItem) {
                MMMessageItem mMMessageItem2 = ((ThreadItem) itemByPosition).thread;
                if (mMMessageItem2.messageType == 19) {
                    mMMessageItem2 = mMMessageItem;
                }
                mMMessageItem = mMMessageItem2;
            } else if (itemByPosition instanceof CommentItem) {
                mMMessageItem = ((CommentItem) itemByPosition).comment;
            }
            findLastCompletelyVisibleItemPosition--;
        }
        return mMMessageItem;
    }

    public boolean isInAutoScrollBottomMode() {
        if (this.mLinearLayoutManager.getItemCount() - 5 < this.mLinearLayoutManager.findLastVisibleItemPosition() || this.mHandler.hasMessages(1)) {
            return true;
        }
        return false;
    }

    public boolean isLayoutReady() {
        return this.isLayoutReady || this.mLinearLayoutManager.findFirstVisibleItemPosition() != -1;
    }

    public void scrollToBottom(boolean z) {
        if (z || this.mLinearLayoutManager.getItemCount() - 5 < this.mLinearLayoutManager.findLastVisibleItemPosition()) {
            this.mHandler.obtainMessage(1, z ? 1 : 0, 0).sendToTarget();
        }
    }

    public void removeScrollBottomMsg() {
        this.mHandler.removeMessages(1);
    }

    public void updateThread(String str) {
        if (!TextUtils.isEmpty(str) && this.mAdapter.getThreadById(str) != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public MMMessageItem updateThread(ZoomMessage zoomMessage) {
        if (zoomMessage == null || !zoomMessage.isThread() || this.mAdapter.getThreadById(zoomMessage.getMessageID()) == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
        if (threadDataProvider == null) {
            return null;
        }
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        ZoomMessage zoomMessage2 = zoomMessage;
        MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(zoomMessage2, this.mSessionId, zoomMessenger, this.mIsGroupMessage, StringUtil.isSameString(zoomMessage.getSenderID(), this.myJid), getContext(), this.mIMAddrBookItem, zoomFileContentMgr);
        if (initWithZoomMessage == null) {
            return null;
        }
        initWithZoomMessage.hasCommentsOdds = threadDataProvider.threadHasCommentsOdds(zoomMessage);
        MMMessageHelper mMMessageHelper = this.mMessageHelper;
        if (mMMessageHelper != null) {
            ThrCommentState unreadCommentState = mMMessageHelper.getUnreadCommentState(initWithZoomMessage.serverSideTime);
            if (unreadCommentState != null) {
                initWithZoomMessage.unreadCommentCount = (long) unreadCommentState.getAllUnreadCount();
            }
        }
        updateMessageStarStatus(initWithZoomMessage);
        this.mAdapter.moveToLatest(initWithZoomMessage);
        this.mAdapter.notifyDataSetChanged();
        checkLinkPreviewAndOther(initWithZoomMessage);
        return initWithZoomMessage;
    }

    public MMMessageItem updateMessage(ZoomMessage zoomMessage) {
        if (zoomMessage == null) {
            return null;
        }
        if (zoomMessage.isThread()) {
            return updateThread(zoomMessage);
        }
        return updateComment(zoomMessage);
    }

    public MMMessageItem updateComment(ZoomMessage zoomMessage) {
        if (zoomMessage == null || !zoomMessage.isComment()) {
            return null;
        }
        MMMessageItem threadById = this.mAdapter.getThreadById(zoomMessage.getThreadID());
        if (threadById == null || CollectionsUtil.isCollectionEmpty(threadById.getComments())) {
            return null;
        }
        List comments = threadById.getComments();
        String messageID = zoomMessage.getMessageID();
        int i = 0;
        while (true) {
            if (i >= comments.size()) {
                i = -1;
                break;
            } else if (TextUtils.equals(messageID, ((MMMessageItem) comments.get(i)).messageId)) {
                break;
            } else {
                i++;
            }
        }
        if (i == -1) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        ZoomMessage zoomMessage2 = zoomMessage;
        MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(zoomMessage2, this.mSessionId, zoomMessenger, this.mIsGroupMessage, StringUtil.isSameString(zoomMessage.getSenderID(), this.myJid), getContext(), this.mIMAddrBookItem, zoomFileContentMgr);
        if (initWithZoomMessage == null) {
            return null;
        }
        updateMessageStarStatus(initWithZoomMessage);
        comments.set(i, initWithZoomMessage);
        this.mAdapter.notifyDataSetChanged();
        return initWithZoomMessage;
    }

    public void notifyDataSetChanged() {
        MMThreadsAdapter mMThreadsAdapter = this.mAdapter;
        if (mMThreadsAdapter != null) {
            mMThreadsAdapter.notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged(boolean z) {
        if (z) {
            this.mNotifyHandler.removeCallbacks(this.mRunnableNotifyDataSetChanged);
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        this.mNotifyHandler.removeCallbacks(this.mRunnableNotifyDataSetChanged);
        this.mHandler.postDelayed(this.mRunnableNotifyDataSetChanged, 500);
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

    public void setAnchorMessageItem(MMContentMessageAnchorInfo mMContentMessageAnchorInfo) {
        this.mAnchorMessageInfo = mMContentMessageAnchorInfo;
        setIsLocalMsgDirty(true);
    }

    public void setUICallBack(ThreadsUICallBack threadsUICallBack) {
        this.mAdapter.setUICallBack(threadsUICallBack);
        this.mUICallBack = threadsUICallBack;
    }

    public void setIsE2EChat(boolean z) {
        this.mIsE2EChat = z;
        if (!z) {
            this.mHasNewMessageDuringPaused = false;
        }
    }

    public void setUnreadMsgInfo(int i, long j) {
        this.mUnreadMsgCount = i;
        if (i > 0) {
            this.mAdapter.setReadMsgTime(j);
            this.mUnreadMsgTimestamp = j;
            return;
        }
        this.mAdapter.setReadMsgTime(0);
        this.mUnreadMsgTimestamp = 0;
    }

    public boolean isMsgShow(String str) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (linearLayoutManager == null) {
            return false;
        }
        int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        for (int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
            BaseItem itemByPosition = this.mAdapter.getItemByPosition(findFirstVisibleItemPosition);
            if ((itemByPosition instanceof ThreadItem) && TextUtils.equals(str, ((ThreadItem) itemByPosition).thread.messageId)) {
                return true;
            }
            if ((itemByPosition instanceof CommentItem) && TextUtils.equals(str, ((CommentItem) itemByPosition).comment.messageId)) {
                return true;
            }
        }
        return false;
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
            BaseItem itemByPosition = this.mAdapter.getItemByPosition(findFirstVisibleItemPosition);
            if (itemByPosition instanceof ThreadItem) {
                arrayList.add(((ThreadItem) itemByPosition).thread);
            }
            if (itemByPosition instanceof CommentItem) {
                arrayList.add(((CommentItem) itemByPosition).comment);
            }
        }
        return arrayList;
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

    public boolean showMessage(String str) {
        int findItemInAdapter = this.mAdapter.findItemInAdapter(str);
        if (findItemInAdapter == -1) {
            return false;
        }
        scrollToPosition(findItemInAdapter);
        return true;
    }

    public void markALlMsgReadBeforeMsgId(String str) {
        MMMessageItem mMMessageItem;
        int findItemInAdapter = this.mAdapter.findItemInAdapter(str);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.getSessionById(this.mSessionId) != null) {
            for (int i = 0; i <= findItemInAdapter; i++) {
                BaseItem itemByPosition = this.mAdapter.getItemByPosition(i);
                if (itemByPosition instanceof ThreadItem) {
                    mMMessageItem = ((ThreadItem) itemByPosition).thread;
                } else if (itemByPosition instanceof CommentItem) {
                    mMMessageItem = ((CommentItem) itemByPosition).comment;
                }
                if (mMMessageItem != null && mMMessageItem.isUnread) {
                    mMMessageItem.isUnread = false;
                }
            }
        }
    }

    public void updateUI() {
        this.mAdapter.notifyDataSetChanged();
        if (this.mHasNewMessageDuringPaused) {
            scrollToBottom(false);
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

    public void onIndicateInfoUpdatedWithJID(String str) {
        MMMessageItem mMMessageItem;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null && zoomMessenger.getSessionById(this.mSessionId) != null) {
                int itemCount = this.mAdapter.getItemCount();
                for (int i = 0; i < itemCount; i++) {
                    BaseItem itemByPosition = this.mAdapter.getItemByPosition(i);
                    if (itemByPosition != null) {
                        if (itemByPosition instanceof ThreadItem) {
                            mMMessageItem = ((ThreadItem) itemByPosition).thread;
                        } else if (itemByPosition instanceof CommentItem) {
                            mMMessageItem = ((CommentItem) itemByPosition).comment;
                        }
                        if (mMMessageItem != null) {
                            if (mMMessageItem.isUnread) {
                                mMMessageItem.isUnread = false;
                            }
                            if (StringUtil.isSameString(mMMessageItem.fromJid, str)) {
                                mMMessageItem.fromScreenName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, mMMessageItem.isIncomingMessage() ? this.mIMAddrBookItem : null);
                                if (mMMessageItem.fromContact != null) {
                                    mMMessageItem.fromContact.setAvatarPath(buddyWithJID.getLocalPicturePath());
                                }
                            }
                        }
                    }
                }
                if (this.mIsResume) {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void onGroupMessage(int i, ZoomMessage zoomMessage) {
        if (i == 0) {
            addNewMessage(zoomMessage);
            if (this.mIsResume) {
                this.mAdapter.notifyDataSetChanged();
                scrollToBottom(false);
                return;
            }
            this.mHasNewMessageDuringPaused = true;
        }
    }

    public boolean isDataEmpty() {
        return this.mAdapter.isEmpty();
    }

    public boolean hasMessageFromJid(String str) {
        return this.mAdapter.hasMessageFromJid(str);
    }

    public MMMessageItem getItemByMessageId(String str) {
        return this.mAdapter.getItemByMessageId(str);
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

    public void FT_OnPause(String str, String str2) {
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

    public void FT_OnResumed(String str, String str2, int i) {
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

    public void Indicate_MessageDeleted(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            MMMessageItem itemByMessageId = this.mAdapter.getItemByMessageId(str2);
            if (itemByMessageId != null) {
                boolean z = true;
                if (!itemByMessageId.isThread || (itemByMessageId.commentsCount <= 0 && CollectionsUtil.isCollectionEmpty(itemByMessageId.getComments()))) {
                    this.mAdapter.removeItem(str2);
                } else {
                    itemByMessageId.isDeletedThread = true;
                    itemByMessageId.messageType = 48;
                }
                MMContentMessageAnchorInfo mMContentMessageAnchorInfo = this.mAnchorMessageInfo;
                if (mMContentMessageAnchorInfo == null || !isMsgShow(mMContentMessageAnchorInfo.getMsgGuid())) {
                    z = false;
                }
                this.mAdapter.notifyDataSetChanged();
                if (z) {
                    scrollToMessage(this.mAnchorMessageInfo.getMsgGuid());
                }
            }
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
        if (StringUtil.isSameString(str3, this.mSessionId)) {
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

    public void onRecallComment(String str, String str2) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            MMThreadsAdapter mMThreadsAdapter = this.mAdapter;
            if (mMThreadsAdapter != null) {
                MMMessageItem threadById = mMThreadsAdapter.getThreadById(str2);
                if (threadById != null && threadById.commentsCount > 0) {
                    threadById.commentsCount--;
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void onRecallMessage(boolean z, ZoomMessage zoomMessage, String str, long j) {
        if (z) {
            LinkPreviewHelper.deleteLinkPreview(str);
            MMMessageItem itemByMessageId = this.mAdapter.getItemByMessageId(str);
            long j2 = 0;
            if (itemByMessageId != null) {
                if (!itemByMessageId.isThread || (CollectionsUtil.isCollectionEmpty(itemByMessageId.getComments()) && itemByMessageId.commentsCount <= 0)) {
                    this.mAdapter.removeItem(str);
                } else {
                    itemByMessageId.isDeletedThread = true;
                    itemByMessageId.messageType = 48;
                }
            } else if (j != 0) {
                MMMessageItem itemByMessageSVR = this.mAdapter.getItemByMessageSVR(j);
                if (itemByMessageSVR != null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                        if (threadDataProvider != null) {
                            ZoomMessage messagePtr = threadDataProvider.getMessagePtr(this.mSessionId, j);
                            if (messagePtr != null) {
                                itemByMessageSVR.commentsCount = messagePtr.getTotalCommentsCount();
                                if (itemByMessageSVR.commentsCount == 0) {
                                    itemByMessageSVR.hasCommentsOdds = threadDataProvider.threadHasCommentsOdds(messagePtr);
                                }
                            }
                        }
                    }
                    MMMessageHelper mMMessageHelper = this.mMessageHelper;
                    if (mMMessageHelper != null) {
                        ThrCommentState unreadCommentState = mMMessageHelper.getUnreadCommentState(j);
                        if (unreadCommentState != null) {
                            j2 = (long) unreadCommentState.getAllUnreadCount();
                        }
                        itemByMessageSVR.unreadCommentCount = j2;
                    }
                }
            }
            if (zoomMessage != null) {
                addNewMessage(zoomMessage);
            }
            if (this.mIsResume) {
                this.mAdapter.notifyDataSetChanged();
            } else {
                this.mHasNewMessageDuringPaused = true;
            }
        }
    }

    public void Indicate_SendAddonCommandResultIml(String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageById = sessionById.getMessageById(str);
                    if (messageById != null) {
                        updateMessage(messageById);
                    }
                }
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
                    if (childAt instanceof AbsMessageView) {
                        MMMessageItem messageItem = ((AbsMessageView) childAt).getMessageItem();
                        if (messageItem != null) {
                            updateMessage(sessionById.getMessageByXMPPGuid(messageItem.messageXMPPId));
                        }
                    }
                }
            }
        }
    }

    public boolean containAnchorMessage(List<String> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        MMContentMessageAnchorInfo mMContentMessageAnchorInfo = this.mAnchorMessageInfo;
        if (mMContentMessageAnchorInfo == null) {
            return false;
        }
        if (mMContentMessageAnchorInfo.getmType() == 0) {
            if (!TextUtils.isEmpty(this.mAnchorMessageInfo.getMsgGuid())) {
                return list.contains(this.mAnchorMessageInfo.getMsgGuid());
            }
        } else if (this.mAnchorMessageInfo.getmType() == 1) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return false;
            }
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById == null) {
                return false;
            }
            for (String str : list) {
                if (!TextUtils.isEmpty(str)) {
                    ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(str);
                    if (messageByXMPPGuid != null && messageByXMPPGuid.getServerSideTime() > 0 && messageByXMPPGuid.getServerSideTime() == this.mAnchorMessageInfo.getServerTime()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void NotifyOutdatedHistoryRemoved(long j) {
        this.mAdapter.removeHistory(j);
        insertTimedChatMsg();
    }

    public void insertTimedChatMsg() {
        if (!UIMgr.isMyNotes(this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                LocalStorageTimeInterval localStorageTimeInterval = zoomMessenger.getLocalStorageTimeInterval();
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
    }

    public void OnLinkCrawlResult(CrawlLinkResponse crawlLinkResponse) {
        if (crawlLinkResponse != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(crawlLinkResponse.getMsgGuid());
                    if (messageByXMPPGuid != null) {
                        updateMessage(messageByXMPPGuid);
                        if (isAtBottom()) {
                            scrollToBottom(true);
                        }
                    }
                }
            }
        }
    }

    public void OnDownloadFavicon(int i, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            String str2 = (String) this.mLinkPreviewReqIds.remove(str);
            if (!StringUtil.isEmptyOrNull(str2) && i == 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        updateMessage(sessionById.getMessageById(str2));
                    }
                }
            }
        }
    }

    public void OnDownloadImage(int i, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            String str2 = (String) this.mLinkPreviewReqIds.remove(str);
            if (!StringUtil.isEmptyOrNull(str2) && i == 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        updateMessage(sessionById.getMessageById(str2));
                        if (isAtBottom()) {
                            scrollToBottom(true);
                        }
                    }
                }
            }
        }
    }

    public void OnUnsupportMessageRecevied(int i, String str, String str2, String str3) {
        if (this.mAdapter.getItemByMessageId(str3) != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str2);
                if (sessionById != null) {
                    ZoomMessage messageById = sessionById.getMessageById(str3);
                    if (messageById != null) {
                        updateMessage(messageById);
                    }
                }
            }
        }
    }

    public List<MMMessageItem> getAllCacheMessages() {
        return this.mAdapter.getAllCacheMessages();
    }

    public Rect getMessageLocationOnScreen(@NonNull MMMessageItem mMMessageItem) {
        LayoutManager layoutManager = getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return null;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        for (int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
            BaseItem itemByPosition = this.mAdapter.getItemByPosition(findFirstVisibleItemPosition);
            if (itemByPosition != null) {
                MMMessageItem mMMessageItem2 = itemByPosition instanceof ThreadItem ? itemByPosition.thread : itemByPosition instanceof CommentItem ? ((CommentItem) itemByPosition).comment : null;
                if (mMMessageItem2 != null && StringUtil.isSameString(mMMessageItem2.messageId, mMMessageItem.messageId)) {
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
                    BaseItem itemByPosition = this.mAdapter.getItemByPosition(findFirstVisibleItemPosition);
                    if (itemByPosition == null) {
                        continue;
                    } else {
                        MMMessageItem mMMessageItem2 = null;
                        if (itemByPosition instanceof ThreadItem) {
                            mMMessageItem2 = itemByPosition.thread;
                        } else if (itemByPosition instanceof CommentItem) {
                            mMMessageItem2 = ((CommentItem) itemByPosition).comment;
                        }
                        if (mMMessageItem2 != null && StringUtil.isSameString(mMMessageItem2.messageId, mMMessageItem.messageId)) {
                            ViewHolder findViewHolderForAdapterPosition2 = findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                            if (findViewHolderForAdapterPosition2 != null) {
                                View view2 = findViewHolderForAdapterPosition2.itemView;
                                LayoutParams layoutParams2 = (LayoutParams) view2.getLayoutParams();
                                layoutParams2.bottomMargin = i;
                                view2.setLayoutParams(layoutParams2);
                                return;
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public void onSentMessage(@NonNull ZoomMessage zoomMessage) {
        if (PTApp.getInstance().getZoomMessenger() != null) {
            addNewMessage(zoomMessage);
            if (isParentFragmentResumed()) {
                this.mAdapter.notifyDataSetChanged();
                if (this.mAdapter.isLastItem(zoomMessage.getMessageID())) {
                    scrollToBottom(false);
                }
            } else {
                this.mHasNewMessageDuringPaused = true;
            }
        }
    }

    public void setParentFragment(MMThreadsFragment mMThreadsFragment) {
        this.mParentFragment = mMThreadsFragment;
    }

    public boolean isParentFragmentResumed() {
        MMThreadsFragment mMThreadsFragment = this.mParentFragment;
        if (mMThreadsFragment == null) {
            return false;
        }
        return mMThreadsFragment.isResumed();
    }

    public void updateEmptyView() {
        if (!this.mIsGroupMessage) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mSessionId);
                if (buddyWithJID == null || !buddyWithJID.isRobot()) {
                    ZoomChatSession zoomChatSession = null;
                    if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
                        zoomChatSession = zoomMessenger.getSessionById(this.mSessionId);
                    }
                    if (zoomChatSession != null && !StringUtil.isEmptyOrNull(this.mSessionId) && !UIMgr.isMyNotes(this.mSessionId)) {
                        ZoomBuddy buddyWithJID2 = zoomMessenger.getBuddyWithJID(this.mSessionId);
                        boolean z = false;
                        boolean z2 = buddyWithJID2 != null ? buddyWithJID2.getAccountStatus() == 0 : true;
                        if (z2) {
                            int messageCount = zoomChatSession.getMessageCount();
                            StringBuilder sb = new StringBuilder();
                            sb.append(PreferenceUtil.FTE_CHAT_SESSION_SAY_HI);
                            sb.append(this.mSessionId);
                            boolean readSayHiFTE = PreferenceUtil.readSayHiFTE(sb.toString(), false);
                            if (messageCount == 0) {
                                z = !readSayHiFTE;
                            } else if (!readSayHiFTE) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(PreferenceUtil.FTE_CHAT_SESSION_SAY_HI);
                                sb2.append(this.mSessionId);
                                PreferenceUtil.saveSayHiFTE(sb2.toString(), true);
                            }
                            this.mAdapter.setNoMessage(z);
                        } else {
                            this.mAdapter.setNoMessage(false);
                        }
                        this.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void deleteLocalMessage(MMMessageItem mMMessageItem, ZoomChatSession zoomChatSession) {
        zoomChatSession.deleteLocalMessage(mMMessageItem.messageId);
        this.mAdapter.removeItem(mMMessageItem.messageId);
        this.mAdapter.notifyDataSetChanged();
    }

    public void FT_Cancel(@Nullable MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                boolean z = mMMessageItem.messageState == 1;
                zoomMessenger.FT_Cancel(mMMessageItem.sessionId, mMMessageItem.messageId, 1);
                ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                if (sessionById != null) {
                    if (z) {
                        deleteLocalMessage(mMMessageItem, sessionById);
                    } else {
                        ZoomMessage messageById = sessionById.getMessageById(mMMessageItem.messageId);
                        if (messageById != null) {
                            updateMessage(messageById);
                        }
                    }
                }
            }
        }
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
                        BaseItem itemByPosition = this.mAdapter.getItemByPosition(findFirstVisibleItemPosition);
                        if (itemByPosition != null) {
                            MMMessageItem mMMessageItem = null;
                            if (itemByPosition instanceof ThreadItem) {
                                mMMessageItem = itemByPosition.thread;
                            } else if (itemByPosition instanceof CommentItem) {
                                mMMessageItem = ((CommentItem) itemByPosition).comment;
                            }
                            if (mMMessageItem != null && !StringUtil.isEmptyOrNull(mMMessageItem.messageXMPPId) && threadDataProvider.isMessageEmojiCountInfoDirty(this.mSessionId, mMMessageItem.messageXMPPId) && !this.mSyncMessageIDs.contains(mMMessageItem.messageXMPPId)) {
                                this.mSyncMessageIDs.add(mMMessageItem.messageXMPPId);
                                arrayList.add(mMMessageItem.messageXMPPId);
                            }
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
        MMThreadsAdapter mMThreadsAdapter = this.mAdapter;
        if (mMThreadsAdapter == null) {
            return false;
        }
        return mMThreadsAdapter.isUnreadNewMessage(j);
    }
}
