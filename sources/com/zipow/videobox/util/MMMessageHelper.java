package com.zipow.videobox.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.MMCommentActivity;
import com.zipow.videobox.MMCommentActivity.ThreadUnreadInfo;
import com.zipow.videobox.MMImageListActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.FileTransferFragment;
import com.zipow.videobox.fragment.MMThreadsFragment;
import com.zipow.videobox.ptapp.IMProtos.DBExistResult;
import com.zipow.videobox.ptapp.IMProtos.MessageInfo;
import com.zipow.videobox.ptapp.IMProtos.MessageInfoList;
import com.zipow.videobox.ptapp.IMProtos.ThrCommentStates;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.tempbean.IMessageTemplateAttachmentDescription;
import com.zipow.videobox.tempbean.IMessageTemplateAttachmentInfo;
import com.zipow.videobox.tempbean.IMessageTemplateAttachmentTitle;
import com.zipow.videobox.tempbean.IMessageTemplateAttachments;
import com.zipow.videobox.tempbean.IMessageTemplateBase;
import com.zipow.videobox.tempbean.IMessageTemplateFieldItem;
import com.zipow.videobox.tempbean.IMessageTemplateFields;
import com.zipow.videobox.tempbean.IMessageTemplateMessage;
import com.zipow.videobox.tempbean.IMessageTemplateSection;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMCommentsFragment;
import com.zipow.videobox.view.p014mm.MMContentFileViewerFragment;
import com.zipow.videobox.view.p014mm.MMContentMessageItem.MMContentMessageAnchorInfo;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMThreadsRecyclerView;
import com.zipow.videobox.view.p014mm.MMZoomFile;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil.MimeType;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.ZMToast;
import p021us.zoom.videomeetings.C4558R;

public class MMMessageHelper implements SensorEventListener {
    private static final String TAG = "MMMessageHelper";
    private Set<String> mAtAllListFromSyncHint = new HashSet();
    @NonNull
    private ArrayList<UnreadMessage> mAtListFromSyncHint = new ArrayList<>();
    private Set<String> mAtMeListFromSyncHint = new HashSet();
    /* access modifiers changed from: private */
    public MMThreadsFragment mFragment;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public MediaPlayer mMediaPlayer;
    @Nullable
    private List<MessageInfo> mOldMarkUnreadMessages = null;
    private int mOldVolume = -1;
    private String mPendingPlayMsgId;
    /* access modifiers changed from: private */
    public MMMessageItem mPlayingMessage;
    private String mSessionId;
    private Map<String, List<String>> mThreadATMessages = new HashMap();
    @NonNull
    private Map<String, List<MessageInfo>> mThreadMarkUnreadMessages = new HashMap();
    private int mThreadSortType;
    /* access modifiers changed from: private */
    public MMThreadsRecyclerView mThreadsRecyclerView;
    private HashMap<Long, ThrCommentState> mUnreadCommentState = new HashMap<>();
    private Map<String, UnreadMessage> mUnreadMessages = new HashMap();
    private ArrayList<Long> mVisiableUnreadMsgs = new ArrayList<>();
    private int mVolumeChangedTo = -1;
    private boolean mbVolumeChanged = false;

    static class MessageSendErrorMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_DELETE_MESSAGE = 1;
        public static final int ACTION_TRY = 0;

        public MessageSendErrorMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public static class MessageSyncer {
        private static MessageSyncer instance = new MessageSyncer();
        private Set<String> mCleanUnreadPendingSessions = new HashSet();
        private boolean mIsOfflineReady = false;
        private ListenerList mListenerList = new ListenerList();
        private Map<String, Long> mMsgIdSvrs = new HashMap();
        private Set<String> mThreadSyncSet = new HashSet();

        public interface OnMessageSync extends IListener {
            void onMessageSync(String str, String str2);
        }

        public static MessageSyncer getInstance() {
            return instance;
        }

        public void addListener(OnMessageSync onMessageSync) {
            if (onMessageSync != null) {
                IListener[] all = this.mListenerList.getAll();
                for (int i = 0; i < all.length; i++) {
                    if (all[i] == onMessageSync) {
                        removeListener((OnMessageSync) all[i]);
                    }
                }
                this.mListenerList.add(onMessageSync);
            }
        }

        public void removeListener(OnMessageSync onMessageSync) {
            this.mListenerList.remove(onMessageSync);
        }

        public void onXMPPConnect() {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || !zoomMessenger.isConnectionGood()) {
                this.mIsOfflineReady = false;
                this.mThreadSyncSet.clear();
            }
        }

        public void onChatSessionUnreadCountReady() {
            this.mIsOfflineReady = true;
            if (!this.mCleanUnreadPendingSessions.isEmpty()) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    for (String findSessionById : this.mCleanUnreadPendingSessions) {
                        ZoomChatSession findSessionById2 = zoomMessenger.findSessionById(findSessionById);
                        if (findSessionById2 != null) {
                            findSessionById2.cleanUnreadMessageCount();
                        }
                    }
                } else {
                    return;
                }
            }
            this.mCleanUnreadPendingSessions.clear();
        }

        public void cleanUnreadMessageCount(String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                if (!this.mIsOfflineReady) {
                    this.mCleanUnreadPendingSessions.add(str);
                } else {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession findSessionById = zoomMessenger.findSessionById(str);
                        if (findSessionById != null) {
                            findSessionById.cleanUnreadMessageCount();
                        }
                    }
                }
            }
        }

        public void onReceiveMsg(String str, String str2, String str3) {
            if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str3)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                    if (threadDataProvider != null) {
                        ZoomMessage messagePtr = threadDataProvider.getMessagePtr(str, str3);
                        if (messagePtr != null && messagePtr.isComment()) {
                            if (!messagePtr.isOfflineMessage() || messagePtr.commentThreadCloudStoreState() != 1) {
                                String threadID = messagePtr.getThreadID();
                                long threadTime = messagePtr.getThreadTime();
                                if (!StringUtil.isEmptyOrNull(threadID) && threadTime != 0) {
                                    ZoomMessage messagePtr2 = threadDataProvider.getMessagePtr(str, threadID);
                                    if (messagePtr2 == null) {
                                        DBExistResult isMessageExistInDB = threadDataProvider.isMessageExistInDB(str, threadID);
                                        if (isMessageExistInDB == null || !isMessageExistInDB.getExist()) {
                                            threadDataProvider.syncSingleThreadContext(str, threadID, threadTime);
                                            this.mThreadSyncSet.add(threadID);
                                        } else if (isMessageExistInDB.getLoading()) {
                                            this.mMsgIdSvrs.put(threadID, Long.valueOf(threadTime));
                                        }
                                    } else if (threadDataProvider.isThreadDirty(str, threadID) && threadDataProvider.threadHasCommentsOdds(messagePtr2) == 1 && !this.mThreadSyncSet.contains(threadID)) {
                                        threadDataProvider.syncSingleThreadContext(str, threadID, threadTime);
                                        this.mThreadSyncSet.add(threadID);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public void OnThreadContextSynced(String str, String str2, String str3) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((OnMessageSync) iListener).onMessageSync(str, str2);
                }
            }
        }

        public void OnMSGDBExistence(String str, String str2, String str3, boolean z) {
            if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str3)) {
                Long l = (Long) this.mMsgIdSvrs.remove(str3);
                if (z) {
                    IListener[] all = this.mListenerList.getAll();
                    if (all != null) {
                        for (IListener iListener : all) {
                            ((OnMessageSync) iListener).onMessageSync(str2, str3);
                        }
                    }
                }
                if (l != null && !this.mThreadSyncSet.contains(str3)) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                        if (threadDataProvider != null) {
                            if (z) {
                                if (threadDataProvider.isThreadDirty(str2, str3)) {
                                    ZoomMessage messagePtr = threadDataProvider.getMessagePtr(str2, str3);
                                    if (messagePtr == null || threadDataProvider.threadHasCommentsOdds(messagePtr) != 1) {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                            threadDataProvider.syncSingleThreadContext(str2, str3, l.longValue());
                            this.mThreadSyncSet.add(str3);
                        }
                    }
                }
            }
        }

        public String syncThread(String str, String str2, long j) {
            if (j == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2) || this.mThreadSyncSet.contains(str2)) {
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
            String syncSingleThreadContext = threadDataProvider.syncSingleThreadContext(str, str2, j);
            this.mThreadSyncSet.add(str2);
            return syncSingleThreadContext;
        }
    }

    public static class ThrCommentState {
        private int allUnreadCount;
        private boolean isRead;
        public String threadId;
        private int unreadCount;
        public long unreadSvr;

        ThrCommentState(long j, int i, String str) {
            this.unreadCount = i;
            this.unreadSvr = j;
            this.threadId = str;
            this.allUnreadCount = i;
        }

        public int getUnreadCount() {
            return this.unreadCount;
        }

        public int getAllUnreadCount() {
            return this.allUnreadCount;
        }

        public void onReceiveNewReply() {
            this.unreadCount++;
            this.allUnreadCount++;
        }

        public void onRecallReply() {
            int i = this.allUnreadCount;
            boolean z = true;
            if (i > 0) {
                this.allUnreadCount = i - 1;
            }
            int i2 = this.unreadCount;
            if (i2 > 0) {
                this.unreadCount = i2 - 1;
            }
            if (this.unreadCount != 0) {
                z = false;
            }
            this.isRead = z;
        }

        public boolean isRead() {
            return this.isRead;
        }

        public void setRead(boolean z) {
            this.isRead = z;
            if (z) {
                this.unreadCount = 0;
            }
        }

        public boolean hasUnradMsg() {
            return this.allUnreadCount != 0;
        }
    }

    static class UnreadMessage {
        boolean isComment;
        String messageId;
        long svr;
        long thrSvr;
        String threadId;

        UnreadMessage(String str, String str2, boolean z, long j, long j2) {
            this.messageId = str;
            this.threadId = str2;
            this.isComment = z;
            this.svr = j;
            this.thrSvr = j2;
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (obj == null || !(obj instanceof UnreadMessage)) {
                return false;
            }
            UnreadMessage unreadMessage = (UnreadMessage) obj;
            if (StringUtil.isSameString(unreadMessage.messageId, this.messageId) && StringUtil.isSameString(unreadMessage.threadId, this.threadId) && unreadMessage.isComment == this.isComment && unreadMessage.svr == this.svr && unreadMessage.thrSvr == this.thrSvr) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = (StringUtil.isEmptyOrNull(this.threadId) ? 0 : this.threadId.hashCode()) * 31;
            if (!StringUtil.isEmptyOrNull(this.messageId)) {
                i = this.messageId.hashCode();
            }
            return hashCode + i;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public MMMessageHelper(@NonNull String str, @NonNull MMThreadsRecyclerView mMThreadsRecyclerView, @NonNull MMThreadsFragment mMThreadsFragment) {
        this.mSessionId = str;
        this.mThreadsRecyclerView = mMThreadsRecyclerView;
        this.mFragment = mMThreadsFragment;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                this.mThreadSortType = threadDataProvider.getThreadSortType();
            }
        }
    }

    public void addVisiableUnreadMsg(long j) {
        if (j != 0) {
            this.mVisiableUnreadMsgs.add(Long.valueOf(j));
        }
    }

    public void removeVisiableUnreadMsg(long j) {
        this.mVisiableUnreadMsgs.remove(Long.valueOf(j));
    }

    public void checkUnreadComments(boolean z) {
        if (!TextUtils.isEmpty(this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ThrCommentStates sessionUnreadCommentCount = sessionById.getSessionUnreadCommentCount();
                    if (sessionUnreadCommentCount != null) {
                        for (com.zipow.videobox.ptapp.IMProtos.ThrCommentState thrCommentState : sessionUnreadCommentCount.getStatesList()) {
                            if (!this.mUnreadCommentState.containsKey(Long.valueOf(thrCommentState.getThrT()))) {
                                this.mUnreadCommentState.put(Long.valueOf(thrCommentState.getThrT()), new ThrCommentState(thrCommentState.getReadTime(), (int) thrCommentState.getUnreadCommentCount(), null));
                            } else if (z) {
                                this.mUnreadCommentState.put(Long.valueOf(thrCommentState.getThrT()), new ThrCommentState(thrCommentState.getReadTime(), (int) thrCommentState.getUnreadCommentCount(), null));
                            }
                        }
                    }
                }
            }
        }
    }

    public ThrCommentState getUnreadCommentState(long j) {
        return (ThrCommentState) this.mUnreadCommentState.get(Long.valueOf(j));
    }

    public void clearAllChatInfo() {
        this.mAtMeListFromSyncHint.clear();
        this.mAtListFromSyncHint.clear();
        this.mAtAllListFromSyncHint.clear();
        this.mUnreadCommentState.clear();
        this.mUnreadMessages.clear();
        this.mThreadMarkUnreadMessages.clear();
    }

    public boolean clearUnreadCommentState(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
        if (sessionById == null) {
            return false;
        }
        Iterator it = this.mUnreadMessages.entrySet().iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            if (TextUtils.equals(((UnreadMessage) ((Entry) it.next()).getValue()).threadId, str)) {
                it.remove();
                z2 = true;
            }
        }
        ZoomMessage messageById = sessionById.getMessageById(str);
        if (messageById == null) {
            return z2;
        }
        if (this.mThreadSortType == 1) {
            sessionById.cleanUnreadCommentsForThread(messageById.getServerSideTime());
        }
        if (this.mUnreadCommentState.remove(Long.valueOf(messageById.getServerSideTime())) != null) {
            z = true;
        }
        return z2 | z;
    }

    public int getAtMsgCount() {
        ArrayList<UnreadMessage> arrayList = this.mAtListFromSyncHint;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public String getFirstUnVisableATMessage() {
        String str = null;
        if (this.mAtListFromSyncHint == null) {
            return null;
        }
        while (true) {
            if (this.mAtListFromSyncHint.size() <= 0) {
                break;
            }
            UnreadMessage unreadMessage = (UnreadMessage) this.mAtListFromSyncHint.get(0);
            if (this.mThreadsRecyclerView.isMsgShow(unreadMessage.messageId)) {
                this.mAtListFromSyncHint.remove(0);
            } else {
                str = unreadMessage.threadId == null ? unreadMessage.messageId : unreadMessage.threadId;
            }
        }
        return str;
    }

    public boolean isAtMsgEmpty() {
        return CollectionsUtil.isCollectionEmpty(this.mAtListFromSyncHint);
    }

    public void jumpToNextATMsg() {
        if (this.mThreadsRecyclerView != null && !CollectionsUtil.isListEmpty(this.mAtListFromSyncHint)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    while (this.mAtListFromSyncHint.size() > 0) {
                        UnreadMessage unreadMessage = (UnreadMessage) this.mAtListFromSyncHint.remove(0);
                        int msgDirection = this.mThreadsRecyclerView.getMsgDirection(unreadMessage.messageId);
                        if (msgDirection != 0) {
                            if (msgDirection == -1) {
                                ZoomMessage messageById = sessionById.getMessageById(unreadMessage.messageId);
                                if (messageById != null) {
                                    if (messageById.isComment()) {
                                        MMContentMessageAnchorInfo mMContentMessageAnchorInfo = new MMContentMessageAnchorInfo();
                                        mMContentMessageAnchorInfo.setThrSvr(messageById.getThreadTime());
                                        mMContentMessageAnchorInfo.setThrId(messageById.getThreadID());
                                        mMContentMessageAnchorInfo.setComment(true);
                                        mMContentMessageAnchorInfo.setMsgGuid(unreadMessage.messageId);
                                        mMContentMessageAnchorInfo.setSendTime(messageById.getStamp());
                                        mMContentMessageAnchorInfo.setServerTime(messageById.getServerSideTime());
                                        mMContentMessageAnchorInfo.setmType(1);
                                        mMContentMessageAnchorInfo.setSessionId(this.mSessionId);
                                        ThreadUnreadInfo threadUnreadInfo = new ThreadUnreadInfo();
                                        threadUnreadInfo.mMarkUnreadMsgs = getAllMarkUnreadInThread(messageById.getThreadID());
                                        MMCommentsFragment.showMsgContextInActivity(this.mFragment, mMContentMessageAnchorInfo, threadUnreadInfo, 117);
                                        return;
                                    }
                                }
                            }
                            if (this.mThreadsRecyclerView.scrollToMessage(unreadMessage.messageId)) {
                                this.mThreadsRecyclerView.markALlMsgReadBeforeMsgId(unreadMessage.messageId);
                                this.mHandler.post(new Runnable() {
                                    public void run() {
                                        MMMessageHelper.this.mFragment.checkAllShowMsgs();
                                        MMMessageHelper.this.mFragment.updateBottomHint();
                                    }
                                });
                            } else {
                                this.mThreadsRecyclerView.loadThreads(false, unreadMessage.messageId);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public boolean removeAtMessage(String str) {
        return this.mAtListFromSyncHint.remove(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0101  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x010c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.zipow.videobox.ptapp.p013mm.ZoomMessage checkReceivedMessage(java.lang.String r12) {
        /*
            r11 = this;
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r0 = r0.getZoomMessenger()
            r1 = 0
            if (r0 != 0) goto L_0x000c
            return r1
        L_0x000c:
            java.lang.String r2 = r11.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r2 = r0.getSessionById(r2)
            if (r2 != 0) goto L_0x0015
            return r1
        L_0x0015:
            com.zipow.videobox.ptapp.mm.ZoomMessage r2 = r2.getMessageById(r12)
            if (r2 != 0) goto L_0x001c
            return r1
        L_0x001c:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r0 = r0.getMyself()
            if (r0 != 0) goto L_0x0023
            return r1
        L_0x0023:
            java.lang.String r0 = r0.getJid()
            boolean r3 = r2.isComment()
            if (r3 == 0) goto L_0x00a7
            java.lang.String r3 = r2.getSenderID()
            boolean r0 = android.text.TextUtils.equals(r0, r3)
            if (r0 != 0) goto L_0x00bf
            com.zipow.videobox.util.MMMessageHelper$UnreadMessage r0 = new com.zipow.videobox.util.MMMessageHelper$UnreadMessage
            java.lang.String r5 = r2.getThreadID()
            r6 = 1
            long r7 = r2.getServerSideTime()
            long r9 = r2.getThreadTime()
            r3 = r0
            r4 = r12
            r3.<init>(r4, r5, r6, r7, r9)
            java.util.HashMap<java.lang.Long, com.zipow.videobox.util.MMMessageHelper$ThrCommentState> r1 = r11.mUnreadCommentState
            long r3 = r2.getThreadTime()
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            java.lang.Object r1 = r1.get(r3)
            com.zipow.videobox.util.MMMessageHelper$ThrCommentState r1 = (com.zipow.videobox.util.MMMessageHelper.ThrCommentState) r1
            r3 = 0
            if (r1 != 0) goto L_0x007b
            com.zipow.videobox.util.MMMessageHelper$ThrCommentState r1 = new com.zipow.videobox.util.MMMessageHelper$ThrCommentState
            long r4 = r2.getServerSideTime()
            r6 = 1
            long r4 = r4 - r6
            java.lang.String r6 = r2.getThreadID()
            r1.<init>(r4, r3, r6)
            java.util.HashMap<java.lang.Long, com.zipow.videobox.util.MMMessageHelper$ThrCommentState> r4 = r11.mUnreadCommentState
            long r5 = r2.getThreadTime()
            java.lang.Long r5 = java.lang.Long.valueOf(r5)
            r4.put(r5, r1)
        L_0x007b:
            java.lang.String r4 = r1.threadId
            if (r4 != 0) goto L_0x0085
            java.lang.String r4 = r2.getThreadID()
            r1.threadId = r4
        L_0x0085:
            boolean r4 = r2.isOfflineMessage()
            if (r4 != 0) goto L_0x008e
            r1.onReceiveNewReply()
        L_0x008e:
            com.zipow.videobox.view.mm.MMThreadsRecyclerView r4 = r11.mThreadsRecyclerView
            boolean r4 = r4.isLoading()
            if (r4 != 0) goto L_0x00a3
            com.zipow.videobox.view.mm.MMThreadsRecyclerView r4 = r11.mThreadsRecyclerView
            java.lang.String r5 = r2.getThreadID()
            boolean r4 = r4.isMsgShow(r5)
            if (r4 == 0) goto L_0x00a3
            r3 = 1
        L_0x00a3:
            r1.setRead(r3)
            goto L_0x00c0
        L_0x00a7:
            com.zipow.videobox.view.mm.MMThreadsRecyclerView r0 = r11.mThreadsRecyclerView
            boolean r0 = r0.isMsgShow(r12)
            if (r0 != 0) goto L_0x00bf
            com.zipow.videobox.util.MMMessageHelper$UnreadMessage r0 = new com.zipow.videobox.util.MMMessageHelper$UnreadMessage
            r5 = 0
            r6 = 0
            long r7 = r2.getServerSideTime()
            r9 = 0
            r3 = r0
            r4 = r12
            r3.<init>(r4, r5, r6, r7, r9)
            goto L_0x00c0
        L_0x00bf:
            r0 = r1
        L_0x00c0:
            if (r0 == 0) goto L_0x00c7
            java.util.Map<java.lang.String, com.zipow.videobox.util.MMMessageHelper$UnreadMessage> r1 = r11.mUnreadMessages
            r1.put(r12, r0)
        L_0x00c7:
            boolean r0 = r2.isUnread()
            if (r0 == 0) goto L_0x0127
            boolean r0 = r2.isMessageAtEveryone()
            if (r0 != 0) goto L_0x00d9
            boolean r0 = r2.isMessageAtMe()
            if (r0 == 0) goto L_0x0127
        L_0x00d9:
            java.util.ArrayList<com.zipow.videobox.util.MMMessageHelper$UnreadMessage> r0 = r11.mAtListFromSyncHint
            com.zipow.videobox.util.MMMessageHelper$UnreadMessage r1 = new com.zipow.videobox.util.MMMessageHelper$UnreadMessage
            java.lang.String r5 = r2.getThreadID()
            boolean r6 = r2.isComment()
            long r7 = r2.getServerSideTime()
            long r9 = r2.getThreadTime()
            r3 = r1
            r4 = r12
            r3.<init>(r4, r5, r6, r7, r9)
            r0.add(r1)
            boolean r0 = r2.isMessageAtEveryone()
            if (r0 == 0) goto L_0x0101
            java.util.Set<java.lang.String> r0 = r11.mAtAllListFromSyncHint
            r0.add(r12)
            goto L_0x0106
        L_0x0101:
            java.util.Set<java.lang.String> r0 = r11.mAtMeListFromSyncHint
            r0.add(r12)
        L_0x0106:
            boolean r0 = r2.isComment()
            if (r0 == 0) goto L_0x0127
            java.lang.String r0 = r2.getThreadID()
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r1 = r11.mThreadATMessages
            java.lang.Object r1 = r1.get(r0)
            java.util.List r1 = (java.util.List) r1
            if (r1 != 0) goto L_0x0124
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r3 = r11.mThreadATMessages
            r3.put(r0, r1)
        L_0x0124:
            r1.add(r12)
        L_0x0127:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.MMMessageHelper.checkReceivedMessage(java.lang.String):com.zipow.videobox.ptapp.mm.ZoomMessage");
    }

    public boolean checkEditMessage(@NonNull ZoomMessage zoomMessage) {
        String messageXMPPGuid = zoomMessage.getMessageXMPPGuid();
        UnreadMessage unreadMessage = new UnreadMessage(messageXMPPGuid, zoomMessage.getThreadID(), zoomMessage.isComment(), zoomMessage.getServerSideTime(), zoomMessage.getThreadTime());
        List list = (List) this.mThreadATMessages.get(zoomMessage.getThreadID());
        if (zoomMessage.isMessageAtEveryone() || zoomMessage.isMessageAtMe()) {
            if (!this.mAtListFromSyncHint.contains(unreadMessage)) {
                this.mAtListFromSyncHint.add(unreadMessage);
            }
            if (list != null && !list.contains(messageXMPPGuid)) {
                list.add(messageXMPPGuid);
            }
        } else {
            if (this.mAtListFromSyncHint.contains(unreadMessage)) {
                this.mAtListFromSyncHint.remove(unreadMessage);
            }
            if (list != null && list.contains(messageXMPPGuid)) {
                list.remove(messageXMPPGuid);
            }
        }
        if (zoomMessage.isMessageAtMe()) {
            this.mAtMeListFromSyncHint.add(messageXMPPGuid);
        } else {
            this.mAtMeListFromSyncHint.remove(messageXMPPGuid);
        }
        if (zoomMessage.isMessageAtEveryone()) {
            this.mAtAllListFromSyncHint.add(messageXMPPGuid);
        } else {
            this.mAtAllListFromSyncHint.remove(messageXMPPGuid);
        }
        return true;
    }

    public boolean checkEditComment(@NonNull ZoomMessage zoomMessage) {
        if (!zoomMessage.isComment()) {
            return false;
        }
        checkEditMessage(zoomMessage);
        return true;
    }

    public boolean onMessageShowed(MMMessageItem mMMessageItem) {
        Set set;
        boolean z = false;
        if (mMMessageItem == null || mMMessageItem.isSystemMsg() || !this.mThreadsRecyclerView.isLayoutReady() || this.mThreadsRecyclerView.isLoading()) {
            return false;
        }
        if (this.mUnreadMessages.remove(mMMessageItem.messageId) != null) {
            z = true;
        }
        ThrCommentState thrCommentState = (ThrCommentState) this.mUnreadCommentState.get(Long.valueOf(mMMessageItem.serverSideTime));
        if (thrCommentState != null && !thrCommentState.isRead()) {
            thrCommentState.setRead(true);
            z = true;
        }
        List list = (List) this.mThreadATMessages.get(mMMessageItem.messageId);
        if (list == null) {
            set = null;
        } else {
            set = new HashSet(list);
        }
        if (this.mAtAllListFromSyncHint.remove(mMMessageItem.messageId) || this.mAtMeListFromSyncHint.remove(mMMessageItem.messageId) || !CollectionsUtil.isCollectionEmpty(set)) {
            Iterator it = this.mAtListFromSyncHint.iterator();
            while (it.hasNext()) {
                UnreadMessage unreadMessage = (UnreadMessage) it.next();
                if (TextUtils.equals(unreadMessage.messageId, mMMessageItem.messageId) || (set != null && set.contains(unreadMessage.messageId))) {
                    it.remove();
                    z = true;
                }
            }
        }
        return z;
    }

    public boolean isFirstUnreadThreadAtTop() {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        boolean z = false;
        if (mMThreadsRecyclerView == null || !mMThreadsRecyclerView.isLayoutReady()) {
            return false;
        }
        ArrayList arrayList = new ArrayList(this.mUnreadCommentState.keySet());
        if (this.mThreadSortType == 0) {
            Collections.sort(arrayList, new Comparator<Long>() {
                public int compare(Long l, Long l2) {
                    MMMessageItem findMessageItemByStamp = MMMessageHelper.this.mThreadsRecyclerView.findMessageItemByStamp(l.longValue());
                    MMMessageItem findMessageItemByStamp2 = MMMessageHelper.this.mThreadsRecyclerView.findMessageItemByStamp(l2.longValue());
                    if (findMessageItemByStamp == findMessageItemByStamp2) {
                        return 0;
                    }
                    if (findMessageItemByStamp == null) {
                        return -1;
                    }
                    if (findMessageItemByStamp2 == null) {
                        return 1;
                    }
                    return Long.compare(findMessageItemByStamp.visibleTime, findMessageItemByStamp2.visibleTime);
                }
            });
        } else {
            Collections.sort(arrayList);
        }
        MMMessageItem lastVisibleItem = this.mThreadsRecyclerView.getLastVisibleItem();
        if (lastVisibleItem == null) {
            if (this.mThreadSortType != 0) {
                z = true;
            }
            return z;
        }
        Long l = null;
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Long l2 = (Long) it.next();
            ThrCommentState thrCommentState = (ThrCommentState) this.mUnreadCommentState.get(l2);
            if (thrCommentState != null) {
                if (!thrCommentState.isRead()) {
                    l = l2;
                    break;
                }
            } else {
                this.mUnreadCommentState.remove(l2);
            }
        }
        if (l == null || l.longValue() == 0) {
            return false;
        }
        if (this.mThreadSortType == 0) {
            MMMessageItem findMessageItemByStamp = this.mThreadsRecyclerView.findMessageItemByStamp(l.longValue());
            if (findMessageItemByStamp == null) {
                return false;
            }
            if (findMessageItemByStamp.visibleTime < (lastVisibleItem.visibleTime == 0 ? lastVisibleItem.messageTime : lastVisibleItem.visibleTime)) {
                z = true;
            }
            return z;
        }
        if (l.longValue() < (lastVisibleItem.serverSideTime == 0 ? lastVisibleItem.messageTime : lastVisibleItem.serverSideTime)) {
            z = true;
        }
        return z;
    }

    public void setAllCommentStateAsRead() {
        for (Entry value : this.mUnreadCommentState.entrySet()) {
            ((ThrCommentState) value.getValue()).setRead(true);
        }
    }

    public int getUnreadMessageCount(@NonNull ZoomChatSession zoomChatSession) {
        return getUnreadMessageCount(zoomChatSession, false);
    }

    public int getUnreadMessageCount(@NonNull ZoomChatSession zoomChatSession, boolean z) {
        MMThreadsRecyclerView mMThreadsRecyclerView = this.mThreadsRecyclerView;
        if (mMThreadsRecyclerView == null || !mMThreadsRecyclerView.isLayoutReady()) {
            return 0;
        }
        int unreadThreadsCount = zoomChatSession.getUnreadThreadsCount();
        if (z) {
            return unreadThreadsCount;
        }
        for (Entry entry : this.mUnreadCommentState.entrySet()) {
            if (!((ThrCommentState) entry.getValue()).isRead()) {
                ((Long) entry.getKey()).longValue();
                unreadThreadsCount += ((ThrCommentState) entry.getValue()).getUnreadCount();
            }
        }
        return unreadThreadsCount;
    }

    public void onRecallMessage(String str, long j, long j2) {
        if (j2 != 0) {
            ThrCommentState thrCommentState = (ThrCommentState) this.mUnreadCommentState.get(Long.valueOf(j2));
            if (thrCommentState != null && thrCommentState.unreadSvr < j) {
                thrCommentState.onRecallReply();
                if (thrCommentState.threadId == null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                        if (sessionById != null) {
                            ZoomMessage messageByServerTime = sessionById.getMessageByServerTime(j2, true);
                            if (messageByServerTime != null) {
                                thrCommentState.threadId = messageByServerTime.getMessageID();
                            }
                        }
                    }
                }
                if (thrCommentState.threadId != null) {
                    List list = (List) this.mThreadATMessages.get(thrCommentState.threadId);
                    if (list != null) {
                        list.remove(str);
                    }
                }
            }
        }
        boolean z = false;
        Iterator it = this.mAtListFromSyncHint.iterator();
        while (it.hasNext()) {
            if (TextUtils.equals(((UnreadMessage) it.next()).messageId, str)) {
                it.remove();
                z = true;
            }
        }
        if (z) {
            MMThreadsFragment mMThreadsFragment = this.mFragment;
            if (mMThreadsFragment != null) {
                mMThreadsFragment.updateBottomHint();
            }
        }
    }

    public boolean jumpToFirstUnreadThread4Reply() {
        String str;
        Long l;
        String str2;
        ArrayList arrayList = new ArrayList(this.mUnreadCommentState.keySet());
        if (this.mThreadSortType == 0) {
            Collections.sort(arrayList, new Comparator<Long>() {
                public int compare(Long l, Long l2) {
                    MMMessageItem findMessageItemByStamp = MMMessageHelper.this.mThreadsRecyclerView.findMessageItemByStamp(l.longValue());
                    MMMessageItem findMessageItemByStamp2 = MMMessageHelper.this.mThreadsRecyclerView.findMessageItemByStamp(l2.longValue());
                    if (findMessageItemByStamp == findMessageItemByStamp2) {
                        return 0;
                    }
                    if (findMessageItemByStamp == null) {
                        return -1;
                    }
                    if (findMessageItemByStamp2 == null) {
                        return 1;
                    }
                    return Long.compare(findMessageItemByStamp.visibleTime, findMessageItemByStamp2.visibleTime);
                }
            });
        } else {
            Collections.sort(arrayList);
        }
        Iterator it = arrayList.iterator();
        while (true) {
            str = null;
            if (!it.hasNext()) {
                l = null;
                break;
            }
            l = (Long) it.next();
            ThrCommentState thrCommentState = (ThrCommentState) this.mUnreadCommentState.get(l);
            if (thrCommentState != null) {
                if (!thrCommentState.isRead()) {
                    break;
                }
            } else {
                this.mUnreadCommentState.remove(l);
            }
        }
        if (l == null || l.longValue() == 0) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
        if (sessionById == null) {
            return false;
        }
        ThrCommentState unreadCommentState = getUnreadCommentState(l.longValue());
        if (unreadCommentState != null) {
            str = unreadCommentState.threadId;
        }
        if (str == null) {
            ZoomMessage messageByServerTime = sessionById.getMessageByServerTime(l.longValue(), true);
            if (messageByServerTime != null) {
                str2 = messageByServerTime.getMessageID();
                if ((!TextUtils.isEmpty(str2) || sessionById.getMessageById(str2) == null) && !zoomMessenger.isConnectionGood()) {
                    MMThreadsFragment mMThreadsFragment = this.mFragment;
                    if (!(mMThreadsFragment == null || mMThreadsFragment.getContext() == null)) {
                        Context context = this.mFragment.getContext();
                        Toast.makeText(context, context.getResources().getString(C4558R.string.zm_mm_msg_network_unavailable), 1).show();
                    }
                    return true;
                }
                ThreadUnreadInfo threadUnreadInfo = new ThreadUnreadInfo();
                if (!TextUtils.isEmpty(str2)) {
                    threadUnreadInfo.mAtAllMsgIds = getAtMsgs(str2, false);
                    threadUnreadInfo.mAtMsgIds = getAllAtMsgs(str2);
                    threadUnreadInfo.mMarkUnreadMsgs = getAllMarkUnreadInThread(str2);
                    threadUnreadInfo.mAtMeMsgIds = getAtMsgs(str2, true);
                }
                if (unreadCommentState != null) {
                    threadUnreadInfo.readTime = unreadCommentState.unreadSvr;
                    threadUnreadInfo.unreadCount = unreadCommentState.getAllUnreadCount();
                }
                this.mUnreadCommentState.remove(l);
                if (sessionById.isGroup()) {
                    MMCommentActivity.showAsGroupChat(this.mFragment, this.mSessionId, str2, l.longValue(), null, threadUnreadInfo, 117);
                } else {
                    MMCommentActivity.showAsOneToOneChat((Fragment) this.mFragment, IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy()), this.mSessionId, str2, l.longValue(), threadUnreadInfo, 117);
                }
                return true;
            }
        }
        str2 = str;
        if (!TextUtils.isEmpty(str2)) {
        }
        MMThreadsFragment mMThreadsFragment2 = this.mFragment;
        Context context2 = this.mFragment.getContext();
        Toast.makeText(context2, context2.getResources().getString(C4558R.string.zm_mm_msg_network_unavailable), 1).show();
        return true;
    }

    public void checkAllATMessages() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                List<String> unreadAllMentionedMessages = sessionById.getUnreadAllMentionedMessages();
                HashSet hashSet = new HashSet();
                Iterator it = this.mAtListFromSyncHint.iterator();
                while (it.hasNext()) {
                    hashSet.add(((UnreadMessage) it.next()).messageId);
                }
                this.mAtMeListFromSyncHint.clear();
                List unreadAtMeMessages = sessionById.getUnreadAtMeMessages();
                if (unreadAtMeMessages != null) {
                    this.mAtMeListFromSyncHint.addAll(unreadAtMeMessages);
                }
                this.mAtAllListFromSyncHint.clear();
                List unreadAtAllMessages = sessionById.getUnreadAtAllMessages();
                if (unreadAtAllMessages != null) {
                    this.mAtAllListFromSyncHint.addAll(unreadAtAllMessages);
                }
                this.mAtListFromSyncHint.clear();
                this.mThreadATMessages.clear();
                if (unreadAllMentionedMessages != null) {
                    for (String str : unreadAllMentionedMessages) {
                        ZoomMessage messageById = sessionById.getMessageById(str);
                        if (messageById != null) {
                            if (messageById.isComment()) {
                                String threadID = messageById.getThreadID();
                                List list = (List) this.mThreadATMessages.get(threadID);
                                if (list == null) {
                                    list = new ArrayList();
                                    this.mThreadATMessages.put(threadID, list);
                                }
                                list.add(str);
                            }
                            ArrayList<UnreadMessage> arrayList = this.mAtListFromSyncHint;
                            UnreadMessage unreadMessage = new UnreadMessage(str, messageById.getThreadID(), messageById.isComment(), messageById.getServerSideTime(), messageById.getThreadTime());
                            arrayList.add(unreadMessage);
                        }
                    }
                }
                Collections.sort(this.mAtListFromSyncHint, new Comparator<UnreadMessage>() {
                    public int compare(UnreadMessage unreadMessage, UnreadMessage unreadMessage2) {
                        if (unreadMessage.svr > unreadMessage2.svr) {
                            return 1;
                        }
                        return unreadMessage.svr == unreadMessage2.svr ? 0 : -1;
                    }
                });
            }
        }
    }

    public int getAtMeCountInThread(String str) {
        int i = 0;
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        List<String> list = (List) this.mThreadATMessages.get(str);
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return 0;
        }
        for (String contains : list) {
            if (this.mAtMeListFromSyncHint.contains(contains)) {
                i++;
            }
        }
        return i;
    }

    public int getAtAllCountInThread(String str) {
        int i = 0;
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        List<String> list = (List) this.mThreadATMessages.get(str);
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return 0;
        }
        for (String contains : list) {
            if (this.mAtAllListFromSyncHint.contains(contains)) {
                i++;
            }
        }
        return i;
    }

    public boolean isMsgAtMe(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (this.mAtMeListFromSyncHint.contains(str)) {
            return true;
        }
        List<String> list = (List) this.mThreadATMessages.get(str);
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return false;
        }
        for (String contains : list) {
            if (this.mAtMeListFromSyncHint.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMessageAtMe() {
        Iterator it = this.mAtListFromSyncHint.iterator();
        while (it.hasNext()) {
            if (this.mAtMeListFromSyncHint.contains(((UnreadMessage) it.next()).messageId)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public ArrayList<String> getAllAtMsgs(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        List list = (List) this.mThreadATMessages.get(str);
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return null;
        }
        return new ArrayList<>(list);
    }

    @Nullable
    public ArrayList<String> getAtMsgs(String str, boolean z) {
        Set<String> set;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        List<String> list = (List) this.mThreadATMessages.get(str);
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return null;
        }
        if (z) {
            set = this.mAtMeListFromSyncHint;
        } else {
            set = this.mAtAllListFromSyncHint;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str2 : list) {
            if (set.contains(str2)) {
                arrayList.add(str2);
            }
        }
        return arrayList;
    }

    public void clearAllUnreadThreadInfo(String str) {
        if (!TextUtils.isEmpty(str)) {
            List list = (List) this.mThreadATMessages.remove(str);
            if (!CollectionsUtil.isCollectionEmpty(list)) {
                HashSet hashSet = new HashSet(list);
                Iterator it = this.mAtListFromSyncHint.iterator();
                while (it.hasNext()) {
                    if (hashSet.contains(((UnreadMessage) it.next()).messageId)) {
                        it.remove();
                    }
                }
                this.mAtListFromSyncHint.removeAll(list);
            }
            refreshMarkUnreadMessage(str);
            Iterator it2 = this.mUnreadMessages.entrySet().iterator();
            while (it2.hasNext()) {
                if (TextUtils.equals(((UnreadMessage) ((Entry) it2.next()).getValue()).threadId, str)) {
                    it2.remove();
                }
            }
            clearUnreadCommentState(str);
        }
    }

    public boolean refreshMarkUnreadMessage(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        List list = (List) this.mThreadMarkUnreadMessages.get(str);
        if (!CollectionsUtil.isCollectionEmpty(list)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    HashSet hashSet = new HashSet();
                    for (MessageInfo svrTime : sessionById.getMarkUnreadMessages().getInfoListList()) {
                        hashSet.add(Long.valueOf(svrTime.getSvrTime()));
                    }
                    List<MessageInfo> list2 = this.mOldMarkUnreadMessages;
                    if (list2 != null) {
                        Iterator it = list2.iterator();
                        while (it.hasNext()) {
                            if (!hashSet.contains(Long.valueOf(((MessageInfo) it.next()).getSvrTime()))) {
                                it.remove();
                            }
                        }
                    }
                    Iterator it2 = list.iterator();
                    while (it2.hasNext()) {
                        if (!hashSet.contains(Long.valueOf(((MessageInfo) it2.next()).getSvrTime()))) {
                            it2.remove();
                            z = true;
                        }
                    }
                }
            }
        }
        return z;
    }

    public int getMarkUnreadCountInThread(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        List list = (List) this.mThreadMarkUnreadMessages.get(str);
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return 0;
        }
        return list.size();
    }

    public ArrayList<MessageInfo> getAllMarkUnreadInThread(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        List list = (List) this.mThreadMarkUnreadMessages.get(str);
        if (CollectionsUtil.isCollectionEmpty(list)) {
            return null;
        }
        return new ArrayList<>(list);
    }

    public boolean isOldMarkUnreadMessage(String str) {
        if (TextUtils.isEmpty(str) || CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
            return false;
        }
        for (MessageInfo guid : this.mOldMarkUnreadMessages) {
            if (TextUtils.equals(str, guid.getGuid())) {
                return true;
            }
        }
        return false;
    }

    public void checkMarkUnreadInfo() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                MessageInfoList markUnreadMessages = sessionById.getMarkUnreadMessages();
                if (markUnreadMessages == null || markUnreadMessages.getInfoListCount() <= 0) {
                    this.mVisiableUnreadMsgs.clear();
                    this.mOldMarkUnreadMessages = null;
                    this.mThreadMarkUnreadMessages.clear();
                } else {
                    ArrayList<Long> arrayList = new ArrayList<>();
                    this.mOldMarkUnreadMessages = new ArrayList();
                    for (MessageInfo messageInfo : markUnreadMessages.getInfoListList()) {
                        if (!this.mVisiableUnreadMsgs.contains(Long.valueOf(messageInfo.getSvrTime()))) {
                            this.mOldMarkUnreadMessages.add(messageInfo);
                        } else {
                            arrayList.add(Long.valueOf(messageInfo.getSvrTime()));
                        }
                    }
                    this.mVisiableUnreadMsgs = arrayList;
                    this.mThreadMarkUnreadMessages.clear();
                    sortOldMarkUnread();
                    for (MessageInfo messageInfo2 : this.mOldMarkUnreadMessages) {
                        if (messageInfo2.getIsComment()) {
                            List list = (List) this.mThreadMarkUnreadMessages.get(messageInfo2.getThr());
                            if (list == null) {
                                list = new ArrayList();
                                this.mThreadMarkUnreadMessages.put(messageInfo2.getThr(), list);
                            }
                            list.add(messageInfo2);
                        }
                    }
                }
            }
        }
    }

    private boolean isContainsInMarkUnread(@Nullable String str) {
        boolean z = false;
        if (!StringUtil.isEmptyOrNull(str)) {
            List<MessageInfo> list = this.mOldMarkUnreadMessages;
            if (list != null) {
                Iterator it = list.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (StringUtil.isSameString(((MessageInfo) it.next()).getGuid(), str)) {
                            z = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                return z;
            }
        }
        return false;
    }

    public void checkMarkUnreadInfo4Reply(@Nullable List<String> list) {
        if (!CollectionsUtil.isCollectionEmpty(list)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    for (String str : list) {
                        if (sessionById.isMessageMarkUnread(str)) {
                            ZoomMessage messageById = sessionById.getMessageById(str);
                            if (messageById != null && !messageById.isThread()) {
                                MessageInfo build = MessageInfo.newBuilder().setGuid(str).setIsComment(true).setSession(this.mSessionId).setSvrTime(messageById.getServerSideTime()).setThr(messageById.getThreadID()).setThrSvrT(messageById.getThreadTime()).build();
                                if (this.mOldMarkUnreadMessages == null) {
                                    this.mOldMarkUnreadMessages = new ArrayList();
                                }
                                if (!isContainsInMarkUnread(str)) {
                                    this.mOldMarkUnreadMessages.add(build);
                                }
                            }
                        }
                    }
                    sortOldMarkUnread();
                    this.mThreadMarkUnreadMessages.clear();
                    List<MessageInfo> list2 = this.mOldMarkUnreadMessages;
                    if (list2 != null) {
                        for (MessageInfo messageInfo : list2) {
                            if (messageInfo.getIsComment()) {
                                List list3 = (List) this.mThreadMarkUnreadMessages.get(messageInfo.getThr());
                                if (list3 == null) {
                                    list3 = new ArrayList();
                                    this.mThreadMarkUnreadMessages.put(messageInfo.getThr(), list3);
                                }
                                list3.add(messageInfo);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean resetOldMarkUnreadMsgsItem(long j) {
        if (CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
            return false;
        }
        for (int i = 0; i < this.mOldMarkUnreadMessages.size(); i++) {
            if (((MessageInfo) this.mOldMarkUnreadMessages.get(i)).getSvrTime() == j) {
                this.mOldMarkUnreadMessages.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isContainInOldMarkUnreads(long j) {
        if (CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
            return false;
        }
        for (MessageInfo svrTime : this.mOldMarkUnreadMessages) {
            if (svrTime.getSvrTime() == j) {
                return true;
            }
        }
        return false;
    }

    public int getFirstMarkUnreadMessage() {
        if (this.mOldMarkUnreadMessages == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.mOldMarkUnreadMessages.size(); i2++) {
            MessageInfo messageInfo = (MessageInfo) this.mOldMarkUnreadMessages.get(i2);
            int msgDirection = this.mThreadsRecyclerView.getMsgDirection(messageInfo.getSvrTime());
            if (msgDirection != -1 || !messageInfo.getIsComment()) {
                i = msgDirection;
            } else {
                i = this.mThreadsRecyclerView.getMsgDirection(messageInfo.getThrSvrT());
                if (i == 0) {
                    return 1;
                }
            }
            if (i != 0) {
                break;
            }
        }
        return i;
    }

    public int getOldMarkUnreadMsgCount() {
        List<MessageInfo> list = this.mOldMarkUnreadMessages;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void jumpToNextMarkUnreadMsg() {
        if (!CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
            int i = 0;
            while (true) {
                if (i >= this.mOldMarkUnreadMessages.size()) {
                    break;
                }
                MessageInfo messageInfo = (MessageInfo) this.mOldMarkUnreadMessages.get(i);
                if (messageInfo.getIsComment()) {
                    MMContentMessageAnchorInfo mMContentMessageAnchorInfo = new MMContentMessageAnchorInfo();
                    mMContentMessageAnchorInfo.setThrSvr(messageInfo.getThrSvrT());
                    mMContentMessageAnchorInfo.setThrId(messageInfo.getThr());
                    mMContentMessageAnchorInfo.setComment(true);
                    mMContentMessageAnchorInfo.setMsgGuid(messageInfo.getGuid());
                    mMContentMessageAnchorInfo.setSendTime(messageInfo.getSvrTime());
                    mMContentMessageAnchorInfo.setServerTime(messageInfo.getSvrTime());
                    mMContentMessageAnchorInfo.setmType(1);
                    mMContentMessageAnchorInfo.setSessionId(this.mSessionId);
                    mMContentMessageAnchorInfo.setFromMarkUnread(true);
                    ThreadUnreadInfo threadUnreadInfo = new ThreadUnreadInfo();
                    threadUnreadInfo.mMarkUnreadMsgs = getAllMarkUnreadInThread(messageInfo.getThr());
                    MMCommentsFragment.showMsgContextInActivity(this.mFragment, mMContentMessageAnchorInfo, threadUnreadInfo, 116);
                    this.mOldMarkUnreadMessages.remove(i);
                    this.mVisiableUnreadMsgs.remove(Long.valueOf(messageInfo.getSvrTime()));
                    i++;
                } else {
                    long svrTime = messageInfo.getSvrTime();
                    if (this.mThreadsRecyclerView.getMsgDirection(svrTime) != 0) {
                        if (this.mThreadsRecyclerView.scrollToMessage(svrTime)) {
                            this.mHandler.post(new Runnable() {
                                public void run() {
                                    MMMessageHelper.this.mFragment.checkAllShowMsgs();
                                    MMMessageHelper.this.mFragment.updateBottomHint();
                                }
                            });
                        } else {
                            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                            if (zoomMessenger != null) {
                                ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                                if (threadDataProvider != null) {
                                    ZoomMessage messagePtr = threadDataProvider.getMessagePtr(this.mSessionId, svrTime);
                                    if (messagePtr == null || messagePtr.isComment()) {
                                        MMContentMessageAnchorInfo mMContentMessageAnchorInfo2 = new MMContentMessageAnchorInfo();
                                        mMContentMessageAnchorInfo2.setSendTime(svrTime);
                                        mMContentMessageAnchorInfo2.setServerTime(svrTime);
                                        mMContentMessageAnchorInfo2.setmType(1);
                                        mMContentMessageAnchorInfo2.setSessionId(this.mSessionId);
                                        mMContentMessageAnchorInfo2.setMsgGuid(messageInfo.getGuid());
                                        mMContentMessageAnchorInfo2.setFromMarkUnread(true);
                                        MMThreadsFragment.showMsgContextInActivity(this.mFragment, mMContentMessageAnchorInfo2, 116);
                                        this.mOldMarkUnreadMessages.remove(i);
                                    } else {
                                        this.mThreadsRecyclerView.loadThreads(false, messagePtr.getMessageID());
                                        this.mFragment.setHistoryRefreshing();
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                    }
                    this.mVisiableUnreadMsgs.add(Long.valueOf(messageInfo.getSvrTime()));
                }
            }
        }
    }

    public boolean isOldMarkUnreadMessagesEmpty() {
        return CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages);
    }

    public void checkOldMarkUnreadMsg() {
        checkMarkUnreadInfo();
        this.mFragment.updateBottomHint();
    }

    private void sortOldMarkUnread() {
        if (!CollectionsUtil.isCollectionEmpty(this.mOldMarkUnreadMessages)) {
            Collections.sort(this.mOldMarkUnreadMessages, new Comparator<MessageInfo>() {
                public int compare(MessageInfo messageInfo, MessageInfo messageInfo2) {
                    return Long.compare(messageInfo.getSvrTime(), messageInfo2.getSvrTime());
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:63:0x00d0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clickStatusImage(com.zipow.videobox.view.p014mm.MMMessageItem r9) {
        /*
            r8 = this;
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r0 = r0.getZoomMessenger()
            if (r0 != 0) goto L_0x000b
            return
        L_0x000b:
            java.lang.String r1 = r8.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r1 = r0.getSessionById(r1)
            if (r1 != 0) goto L_0x0014
            return
        L_0x0014:
            boolean r2 = r1.isGroup()
            if (r2 == 0) goto L_0x0027
            com.zipow.videobox.ptapp.mm.ZoomGroup r2 = r1.getSessionGroup()
            if (r2 == 0) goto L_0x0026
            boolean r2 = r2.amIInGroup()
            if (r2 != 0) goto L_0x0027
        L_0x0026:
            return
        L_0x0027:
            boolean r2 = r9.isE2E
            if (r2 == 0) goto L_0x0072
            boolean r2 = r0.isConnectionGood()
            if (r2 != 0) goto L_0x0032
            return
        L_0x0032:
            boolean r2 = r9.isMessageE2EWaitDecrypt()
            if (r2 == 0) goto L_0x0072
            java.lang.String r2 = r8.mSessionId
            java.lang.String r3 = r9.messageId
            int r0 = r0.e2eTryDecodeMessage(r2, r3)
            if (r0 != 0) goto L_0x0057
            java.lang.String r0 = r9.messageId
            com.zipow.videobox.ptapp.mm.ZoomMessage r0 = r1.getMessageById(r0)
            if (r0 == 0) goto L_0x006c
            java.lang.CharSequence r1 = r0.getBody()
            r9.message = r1
            int r0 = r0.getMessageState()
            r9.messageState = r0
            goto L_0x006c
        L_0x0057:
            r1 = 37
            if (r0 != r1) goto L_0x006c
            r0 = 3
            r9.messageState = r0
            com.zipow.videobox.fragment.MMThreadsFragment r0 = r8.mFragment
            android.content.res.Resources r0 = r0.getResources()
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_message_decrypting
            java.lang.String r0 = r0.getString(r1)
            r9.message = r0
        L_0x006c:
            com.zipow.videobox.view.mm.MMThreadsRecyclerView r9 = r8.mThreadsRecyclerView
            r9.notifyDataSetChanged()
            return
        L_0x0072:
            int r0 = r9.messageType
            r2 = 11
            r3 = 5
            r4 = 0
            if (r0 == r2) goto L_0x008a
            int r0 = r9.messageType
            r2 = 45
            if (r0 == r2) goto L_0x008a
            int r0 = r9.messageType
            if (r0 == r3) goto L_0x008a
            int r0 = r9.messageType
            r2 = 28
            if (r0 != r2) goto L_0x00bd
        L_0x008a:
            com.zipow.videobox.ptapp.mm.ZoomMessage$FileTransferInfo r0 = r9.transferInfo
            r2 = 18
            r5 = 1
            if (r0 == 0) goto L_0x009d
            int r6 = r0.state
            r7 = 2
            if (r6 == r7) goto L_0x00be
            int r0 = r0.state
            if (r0 != r2) goto L_0x009b
            goto L_0x00be
        L_0x009b:
            r5 = 0
            goto L_0x00be
        L_0x009d:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.MMFileContentMgr r0 = r0.getZoomFileContentMgr()
            if (r0 == 0) goto L_0x00bd
            java.lang.String r6 = r8.mSessionId
            java.lang.String r7 = r9.messageXMPPId
            com.zipow.videobox.ptapp.mm.ZoomFile r6 = r0.getFileWithMessageID(r6, r7)
            if (r6 == 0) goto L_0x00bd
            int r7 = r6.getFileTransferState()
            if (r7 != r2) goto L_0x00b8
            goto L_0x00b9
        L_0x00b8:
            r5 = 0
        L_0x00b9:
            r0.destroyFileObject(r6)
            goto L_0x00be
        L_0x00bd:
            r5 = 0
        L_0x00be:
            r0 = 4
            if (r5 != 0) goto L_0x00c9
            int r2 = r9.messageState
            if (r2 == r0) goto L_0x00c9
            int r2 = r9.messageState
            if (r2 != r3) goto L_0x00cc
        L_0x00c9:
            r8.showMessageSendErrorMenu(r9)
        L_0x00cc:
            int r2 = r9.messageType
            if (r2 != r0) goto L_0x00dc
            java.lang.String r0 = r9.messageId
            r1.checkAutoDownloadForMessage(r0)
            r9.isPreviewDownloadFailed = r4
            com.zipow.videobox.view.mm.MMThreadsRecyclerView r9 = r8.mThreadsRecyclerView
            r9.notifyDataSetChanged()
        L_0x00dc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.MMMessageHelper.clickStatusImage(com.zipow.videobox.view.mm.MMMessageItem):void");
    }

    public void showMessageSendErrorMenu(final MMMessageItem mMMessageItem) {
        Context context = this.mFragment.getContext();
        if (context != null) {
            ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
            final ArrayList arrayList = new ArrayList(2);
            arrayList.add(new MessageSendErrorMenuItem(context.getString(C4558R.string.zm_mm_lbl_try_again_70196), 0));
            arrayList.add(new MessageSendErrorMenuItem(context.getString(C4558R.string.zm_mm_lbl_delete_message_70196), 1));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            ZMAlertDialog create = new Builder(context).setTitle((CharSequence) context.getString(C4558R.string.zm_mm_msg_could_not_send_70196)).setAdapter(zMMenuAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMMessageHelper.this.onSelectMessageSendErrorMenuItem((MessageSendErrorMenuItem) arrayList.get(i), mMMessageItem);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    public void onSelectMessageSendErrorMenuItem(MessageSendErrorMenuItem messageSendErrorMenuItem, MMMessageItem mMMessageItem) {
        if (messageSendErrorMenuItem != null && mMMessageItem != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    boolean isConnectionGood = zoomMessenger.isConnectionGood();
                    switch (messageSendErrorMenuItem.getAction()) {
                        case 0:
                            if (!isConnectionGood) {
                                Context context = this.mFragment.getContext();
                                if (context != null) {
                                    ZMToast.show(context, (CharSequence) context.getString(C4558R.string.zm_mm_msg_network_unavailable), 0);
                                    break;
                                }
                            } else {
                                this.mFragment.resendMessage(mMMessageItem);
                                break;
                            }
                            break;
                        case 1:
                            this.mFragment.deleteLocalMessage(mMMessageItem, sessionById);
                            break;
                    }
                }
            }
        }
    }

    public void handleMessageItem(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            boolean z = false;
            switch (mMMessageItem.messageType) {
                case 2:
                case 3:
                    if (mMMessageItem.isPlaying) {
                        stopPlayAudioMessage();
                        return;
                    }
                    if (StringUtil.isEmptyOrNull(mMMessageItem.localFilePath) || !new File(mMMessageItem.localFilePath).exists()) {
                        z = true;
                    } else if (!playAudioMessage(mMMessageItem)) {
                        new File(mMMessageItem.localFilePath).delete();
                        z = true;
                    }
                    if (z) {
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            if (mMMessageItem.messageType != 3 || mMMessageItem.messageState == 2 || mMMessageItem.messageState == 3) {
                                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                                if (sessionById != null) {
                                    if (sessionById.downloadFileForMessage(mMMessageItem.messageId)) {
                                        mMMessageItem.isDownloading = true;
                                        this.mPendingPlayMsgId = mMMessageItem.messageId;
                                        this.mThreadsRecyclerView.onStartToDownloadFileForMessage(mMMessageItem);
                                        break;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                    break;
                case 4:
                case 5:
                case 27:
                case 28:
                case 32:
                case 33:
                    if ((mMMessageItem.messageType != 5 && mMMessageItem.messageType != 32 && mMMessageItem.messageType != 28) || (mMMessageItem.messageState != 4 && mMMessageItem.messageState != 1)) {
                        FragmentActivity activity = this.mFragment.getActivity();
                        if (activity != null) {
                            MMImageListActivity.launch((Context) activity, mMMessageItem.sessionId, mMMessageItem.messageXMPPId, this.mThreadsRecyclerView.getAllCacheMessages());
                            break;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    break;
                case 10:
                case 11:
                    FragmentActivity activity2 = this.mFragment.getActivity();
                    if (activity2 != null) {
                        if (!ZMIMUtils.isGotoEnhancedFileTransfer(mMMessageItem)) {
                            MMContentFileViewerFragment.showAsActivity((ZMActivity) activity2, this.mSessionId, mMMessageItem.messageId, mMMessageItem.messageXMPPId, mMMessageItem.fileId, 0);
                            break;
                        } else {
                            FileTransferFragment.showAsActivity((ZMActivity) activity2, mMMessageItem, 0);
                            break;
                        }
                    } else {
                        return;
                    }
                case 45:
                case 46:
                    if (mMMessageItem.messageType != 45 || (mMMessageItem.messageState != 4 && mMMessageItem.messageState != 1)) {
                        openWithOtherApp(mMMessageItem);
                        break;
                    } else {
                        return;
                    }
                    break;
            }
            eventTrackOpenFile(mMMessageItem.messageType);
        }
    }

    public static String timeInterval(@NonNull Context context, long j, long j2, long j3) {
        if (j != 0) {
            return context.getResources().getQuantityString(C4558R.plurals.zm_mm_msg_year_33479, (int) j, new Object[]{Long.valueOf(j)});
        } else if (j2 != 0) {
            return context.getResources().getQuantityString(C4558R.plurals.zm_mm_msg_month_33479, (int) j2, new Object[]{Long.valueOf(j2)});
        } else if (j3 == 1) {
            return context.getResources().getQuantityString(C4558R.plurals.zm_mm_msg_hour_33479, 24, new Object[]{Integer.valueOf(24)});
        } else {
            return context.getResources().getQuantityString(C4558R.plurals.zm_mm_msg_day_33479, (int) j3, new Object[]{Long.valueOf(j3)});
        }
    }

    public boolean stopPlayAudioMessage() {
        MMMessageItem mMMessageItem = this.mPlayingMessage;
        if (mMMessageItem != null) {
            mMMessageItem.isPlaying = false;
            this.mPlayingMessage = null;
        }
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            return true;
        }
        try {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
        } catch (Exception unused) {
        }
        this.mMediaPlayer = null;
        this.mThreadsRecyclerView.notifyDataSetChanged();
        stopMonitorProximity();
        restoreVolume();
        return true;
    }

    public boolean playAudioMessage(MMMessageItem mMMessageItem) {
        if (mMMessageItem == null) {
            return false;
        }
        if (this.mPlayingMessage != null) {
            stopPlayAudioMessage();
        }
        this.mMediaPlayer = new MediaPlayer();
        this.mPlayingMessage = mMMessageItem;
        try {
            this.mbVolumeChanged = false;
            this.mOldVolume = -1;
            this.mVolumeChangedTo = -1;
            startMonitorProximity();
            this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    } catch (Exception unused) {
                    }
                    MMMessageHelper.this.mMediaPlayer = null;
                    if (MMMessageHelper.this.mPlayingMessage != null) {
                        MMMessageHelper.this.mPlayingMessage.isPlaying = false;
                        MMMessageHelper.this.mPlayingMessage = null;
                    }
                    MMMessageHelper.this.mThreadsRecyclerView.notifyDataSetChanged();
                    MMMessageHelper.this.stopMonitorProximity();
                    MMMessageHelper.this.restoreVolume();
                }
            });
            if (!StringUtil.isEmptyOrNull(mMMessageItem.localFilePath)) {
                this.mMediaPlayer.setDataSource(mMMessageItem.localFilePath);
            }
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.start();
            mMMessageItem.isPlaying = true;
            setMessageAsPlayed(mMMessageItem);
            this.mThreadsRecyclerView.notifyDataSetChanged();
            FragmentActivity activity = this.mFragment.getActivity();
            if (activity == null) {
                return false;
            }
            AudioManager audioManager = (AudioManager) activity.getSystemService("audio");
            if (audioManager != null) {
                this.mOldVolume = audioManager.getStreamVolume(3);
                double streamMaxVolume = (double) audioManager.getStreamMaxVolume(3);
                if (((double) this.mOldVolume) <= 0.6d * streamMaxVolume) {
                    this.mVolumeChangedTo = (int) (streamMaxVolume * 0.8d);
                    audioManager.setStreamVolume(3, this.mVolumeChangedTo, 0);
                    this.mbVolumeChanged = true;
                }
            }
            return true;
        } catch (Exception unused) {
            this.mPlayingMessage = null;
            stopMonitorProximity();
            return false;
        }
    }

    public void setMessageAsPlayed(MMMessageItem mMMessageItem) {
        mMMessageItem.isPlayed = true;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageById = sessionById.getMessageById(mMMessageItem.messageId);
                if (messageById != null) {
                    messageById.setAsPlayed(true);
                }
            }
        }
    }

    public void restoreVolume() {
        try {
            FragmentActivity activity = this.mFragment.getActivity();
            if (activity == null) {
                this.mbVolumeChanged = false;
                this.mOldVolume = -1;
                this.mVolumeChangedTo = -1;
                return;
            }
            if (this.mbVolumeChanged && this.mOldVolume >= 0) {
                AudioManager audioManager = (AudioManager) activity.getSystemService("audio");
                if (audioManager != null && audioManager.getStreamVolume(3) == this.mVolumeChangedTo) {
                    audioManager.setStreamVolume(3, this.mOldVolume, 0);
                }
            }
            this.mbVolumeChanged = false;
            this.mOldVolume = -1;
            this.mVolumeChangedTo = -1;
        } catch (Exception unused) {
        } catch (Throwable th) {
            this.mbVolumeChanged = false;
            this.mOldVolume = -1;
            this.mVolumeChangedTo = -1;
            throw th;
        }
    }

    public void startMonitorProximity() {
        try {
            FragmentActivity activity = this.mFragment.getActivity();
            if (activity != null) {
                SensorManager sensorManager = (SensorManager) activity.getSystemService("sensor");
                if (sensorManager != null) {
                    Sensor defaultSensor = sensorManager.getDefaultSensor(8);
                    if (defaultSensor != null) {
                        sensorManager.registerListener(this, defaultSensor, 3);
                    }
                }
            }
        } catch (Exception unused) {
        }
    }

    public void stopMonitorProximity() {
        try {
            FragmentActivity activity = this.mFragment.getActivity();
            if (activity != null) {
                SensorManager sensorManager = (SensorManager) activity.getSystemService("sensor");
                if (sensorManager != null) {
                    sensorManager.unregisterListener(this);
                }
            }
        } catch (Exception unused) {
        }
    }

    public void eventTrackOpenFile(int i) {
        switch (i) {
            case 4:
            case 5:
            case 27:
            case 28:
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                    if (sessionById != null) {
                        ZoomLogEventTracking.eventTrackOpenFile(sessionById.isGroup());
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public static void openWithOtherApp(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            if (mMMessageItem.messageType == 46 || mMMessageItem.messageType == 45) {
                openSharedLink(mMMessageItem);
            } else if (mMMessageItem.messageType == 10 || mMMessageItem.messageType == 11) {
                openFileWithOtherApp(mMMessageItem);
            }
        }
    }

    private static void openSharedLink(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null && mMMessageItem.fileIntegrationInfo != null) {
            String fileIntegrationUrl = mMMessageItem.getFileIntegrationUrl();
            if (!StringUtil.isEmptyOrNull(fileIntegrationUrl)) {
                Uri parse = Uri.parse(fileIntegrationUrl);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(parse);
                intent.setFlags(268435456);
                try {
                    VideoBoxApplication.getGlobalContext().startActivity(intent);
                } catch (Exception unused) {
                }
            }
        }
    }

    public static void copySharedFileLink(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null && mMMessageItem.fileIntegrationInfo != null) {
            String previewUrl = mMMessageItem.fileIntegrationInfo.getPreviewUrl();
            if (!StringUtil.isEmptyOrNull(previewUrl)) {
                AndroidAppUtil.copyText(VideoBoxApplication.getGlobalContext(), previewUrl);
            }
        }
    }

    private static void openFileWithOtherApp(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(mMMessageItem.fileId);
                if (fileWithWebFileID != null) {
                    MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                    if (initWithZoomFile != null && !StringUtil.isEmptyOrNull(initWithZoomFile.getLocalPath())) {
                        MimeType mimeTypeOfFile = AndroidAppUtil.getMimeTypeOfFile(initWithZoomFile.getFileName());
                        if (mimeTypeOfFile != null) {
                            if (mimeTypeOfFile.fileType == 7) {
                                AndroidAppUtil.openFile(VideoBoxApplication.getGlobalContext(), new File(initWithZoomFile.getLocalPath()), true);
                            } else {
                                AndroidAppUtil.openFile(VideoBoxApplication.getGlobalContext(), new File(initWithZoomFile.getLocalPath()));
                            }
                        }
                    }
                }
            }
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent != null && sensorEvent.sensor != null && sensorEvent.sensor.getType() == 8 && sensorEvent.values != null && sensorEvent.values.length > 0 && !HeadsetUtil.getInstance().isWiredHeadsetOn() && !HeadsetUtil.getInstance().isBluetoothScoAudioOn()) {
            boolean z = true;
            if (((int) sensorEvent.sensor.getMaximumRange()) > 3) {
                if (sensorEvent.values[0] > 3.0f) {
                    z = false;
                }
                routeAudioToEarSpeaker(z);
                return;
            }
            if (sensorEvent.values[0] >= sensorEvent.sensor.getMaximumRange()) {
                z = false;
            }
            routeAudioToEarSpeaker(z);
        }
    }

    private void routeAudioToEarSpeaker(boolean z) {
        boolean z2;
        Context context = this.mFragment.getContext();
        if (context != null) {
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                try {
                    this.mMediaPlayer.pause();
                    z2 = true;
                } catch (Exception unused) {
                    z2 = false;
                }
                AudioManager audioManager = (AudioManager) context.getSystemService("audio");
                if (z) {
                    if (!(audioManager == null || audioManager.getMode() == 2)) {
                        audioManager.setMode(2);
                    }
                } else if (!(audioManager == null || audioManager.getMode() == 0)) {
                    audioManager.setMode(0);
                }
                if (z2) {
                    try {
                        this.mMediaPlayer.start();
                    } catch (Exception unused2) {
                    }
                }
            }
        }
    }

    public void onConfirmFileDownloaded(String str, String str2, int i) {
        FragmentActivity activity = this.mFragment.getActivity();
        if (activity != null && StringUtil.isSameString(this.mSessionId, str)) {
            MMMessageItem itemByMessageId = this.mThreadsRecyclerView.getItemByMessageId(str2);
            if (itemByMessageId != null) {
                switch (itemByMessageId.messageType) {
                    case 2:
                    case 3:
                        if (StringUtil.isSameString(this.mPendingPlayMsgId, str2)) {
                            this.mPendingPlayMsgId = null;
                            if (itemByMessageId.isFileDownloaded && !StringUtil.isEmptyOrNull(itemByMessageId.localFilePath) && new File(itemByMessageId.localFilePath).exists()) {
                                if (!playAudioMessage(itemByMessageId)) {
                                    Toast.makeText(activity, C4558R.string.zm_mm_msg_play_audio_failed, 1).show();
                                    break;
                                }
                            } else if (i != 0) {
                                Toast.makeText(activity, C4558R.string.zm_mm_msg_download_audio_failed, 1).show();
                                break;
                            }
                        }
                        break;
                    case 10:
                    case 11:
                        this.mThreadsRecyclerView.Indicate_FileDownloaded("", itemByMessageId.fileId, i);
                        break;
                }
            }
        }
    }

    public void cancelPendingPlay() {
        this.mPendingPlayMsgId = null;
    }

    public static void copyTemplateMessage(List<IMessageTemplateBase> list, StringBuffer stringBuffer) {
        if (!CollectionsUtil.isListEmpty(list) && stringBuffer != null) {
            for (IMessageTemplateBase iMessageTemplateBase : list) {
                if (iMessageTemplateBase != null && !iMessageTemplateBase.isSupportItem()) {
                    stringBuffer.append(iMessageTemplateBase.getFall_back());
                    stringBuffer.append(FontStyleHelper.SPLITOR);
                } else if (iMessageTemplateBase instanceof IMessageTemplateMessage) {
                    stringBuffer.append(((IMessageTemplateMessage) iMessageTemplateBase).getText());
                    stringBuffer.append(FontStyleHelper.SPLITOR);
                } else if (iMessageTemplateBase instanceof IMessageTemplateFields) {
                    List<IMessageTemplateFieldItem> items = ((IMessageTemplateFields) iMessageTemplateBase).getItems();
                    if (items != null) {
                        for (IMessageTemplateFieldItem iMessageTemplateFieldItem : items) {
                            if (iMessageTemplateFieldItem != null) {
                                stringBuffer.append(iMessageTemplateFieldItem.getKey());
                                stringBuffer.append(":");
                                stringBuffer.append(iMessageTemplateFieldItem.getValue());
                                stringBuffer.append(FontStyleHelper.SPLITOR);
                            }
                        }
                    }
                } else if (iMessageTemplateBase instanceof IMessageTemplateAttachments) {
                    IMessageTemplateAttachments iMessageTemplateAttachments = (IMessageTemplateAttachments) iMessageTemplateBase;
                    IMessageTemplateAttachmentInfo information = iMessageTemplateAttachments.getInformation();
                    if (information != null) {
                        IMessageTemplateAttachmentDescription description = information.getDescription();
                        IMessageTemplateAttachmentTitle title = information.getTitle();
                        if (title != null) {
                            stringBuffer.append(title.getText());
                            stringBuffer.append(FontStyleHelper.SPLITOR);
                        }
                        if (description != null) {
                            stringBuffer.append(description.getText());
                            stringBuffer.append(FontStyleHelper.SPLITOR);
                        }
                    }
                    stringBuffer.append(FileUtils.toTemplateFileSizeString(VideoBoxApplication.getGlobalContext(), iMessageTemplateAttachments.getSize()));
                    stringBuffer.append(FontStyleHelper.SPLITOR);
                } else if (iMessageTemplateBase instanceof IMessageTemplateSection) {
                    IMessageTemplateSection iMessageTemplateSection = (IMessageTemplateSection) iMessageTemplateBase;
                    if (!CollectionsUtil.isListEmpty(iMessageTemplateSection.getSections())) {
                        copyTemplateMessage(iMessageTemplateSection.getSections(), stringBuffer);
                    }
                    if (!TextUtils.isEmpty(iMessageTemplateSection.getFooter())) {
                        stringBuffer.append(iMessageTemplateSection.getFooter());
                        stringBuffer.append("  ");
                    }
                    if (iMessageTemplateSection.getTs() > 0) {
                        stringBuffer.append(TimeUtil.formatTemplateDateTime(VideoBoxApplication.getGlobalContext(), iMessageTemplateSection.getTs()));
                    }
                    if (!TextUtils.isEmpty(iMessageTemplateSection.getFooter()) || iMessageTemplateSection.getTs() > 0) {
                        stringBuffer.append(FontStyleHelper.SPLITOR);
                    }
                }
            }
        }
    }
}
