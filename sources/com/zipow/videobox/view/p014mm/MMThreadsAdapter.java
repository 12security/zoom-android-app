package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.MMMessageHelper;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMThreadsRecyclerView.ThreadsUICallBack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMThreadsAdapter */
public class MMThreadsAdapter extends Adapter<CommentHolder> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int TYPE_ADD_COMMENT = 3;
    public static final int TYPE_COMMENT_START = 10000000;
    public static final int TYPE_MORE_COMMENT = 2;
    public static final int TYPE_THREAD_START = 10000;
    private boolean isBottomLoading = false;
    private boolean isGroup;
    private boolean isUnreadNewMarkReady;
    /* access modifiers changed from: private */
    public ThreadsUICallBack mCallBack;
    private Context mContext;
    private List<MMMessageItem> mDatas = new ArrayList();
    private List<BaseItem> mDisplayDatas = new ArrayList();
    private Map<String, Map<String, MMMessageItem>> mFailedComments = new HashMap();
    private Map<String, MMMessageItem> mFailedThreads = new HashMap();
    private IMAddrBookItem mIMAddrBookItem;
    private boolean mIsLocalMsgDirty = false;
    @Nullable
    private MMMessageHelper mMessageHelper;
    private boolean mNoMessages;
    private String mSessionId;
    private int mThreadSortType;
    private MMMessageItem mTimeChatTimeItem;
    private MMMessageItem mUnreadNewMarkMsg;

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsAdapter$AddReply */
    static class AddReply extends BaseItem {
        AddReply(@NonNull MMMessageItem mMMessageItem) {
            this.thread = mMMessageItem;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsAdapter$BaseItem */
    static class BaseItem {
        MMMessageItem thread;

        BaseItem() {
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsAdapter$CommentHolder */
    public static class CommentHolder extends ViewHolder {
        private BaseItem item;

        public CommentHolder(@NonNull View view) {
            super(view);
        }

        public BaseItem getItem() {
            return this.item;
        }

        public void setItem(BaseItem baseItem) {
            this.item = baseItem;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsAdapter$CommentItem */
    public static class CommentItem extends BaseItem {
        MMMessageItem comment;

        CommentItem(@NonNull MMMessageItem mMMessageItem, MMMessageItem mMMessageItem2) {
            this.thread = mMMessageItem;
            this.comment = mMMessageItem2;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsAdapter$MoreReply */
    static class MoreReply extends BaseItem {
        MoreReply(@NonNull MMMessageItem mMMessageItem) {
            this.thread = mMMessageItem;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMThreadsAdapter$ThreadItem */
    public static class ThreadItem extends BaseItem {
        ThreadItem(@NonNull MMMessageItem mMMessageItem) {
            this.thread = mMMessageItem;
        }
    }

    public MMThreadsAdapter(@NonNull Context context, @NonNull String str) {
        this.mContext = context;
        this.mSessionId = str;
        setHasStableIds(true);
        registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                MMThreadsAdapter.this.onChanged();
            }

            public void onItemRangeChanged(int i, int i2) {
                MMThreadsAdapter.this.updateRange(i, i2);
            }
        });
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                this.mThreadSortType = threadDataProvider.getThreadSortType();
            }
        }
    }

    public void setMessageHelper(@Nullable MMMessageHelper mMMessageHelper) {
        this.mMessageHelper = mMMessageHelper;
    }

    public void setIsLocalMsgDirty(boolean z) {
        this.mIsLocalMsgDirty = z;
        if (!z && this.mUnreadNewMarkMsg != null && !CollectionsUtil.isCollectionEmpty(this.mDatas)) {
            List<MMMessageItem> list = this.mDatas;
            if (this.mUnreadNewMarkMsg.serverSideTime > ((MMMessageItem) list.get(list.size() - 1)).visibleTime) {
                this.mUnreadNewMarkMsg = null;
            }
        }
    }

    public void checkUnreadMarkWhenrecentMsgFinish() {
        if (this.mUnreadNewMarkMsg == null) {
            return;
        }
        if (!CollectionsUtil.isCollectionEmpty(this.mDatas)) {
            List<MMMessageItem> list = this.mDatas;
            if (this.mUnreadNewMarkMsg.serverSideTime > ((MMMessageItem) list.get(list.size() - 1)).visibleTime) {
                this.mUnreadNewMarkMsg = null;
                return;
            }
            return;
        }
        this.mUnreadNewMarkMsg = null;
    }

    public boolean hasUnreadMark() {
        return this.mUnreadNewMarkMsg != null;
    }

    public void setTimeChatTimeItem(MMMessageItem mMMessageItem) {
        this.mTimeChatTimeItem = mMMessageItem;
    }

    /* access modifiers changed from: 0000 */
    public void clearDatas() {
        this.mDatas.clear();
    }

    /* access modifiers changed from: 0000 */
    public int getThreadIndexById(String str) {
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            Object obj = this.mDisplayDatas.get(i);
            if (obj instanceof ThreadItem) {
                MMMessageItem mMMessageItem = ((ThreadItem) obj).thread;
                if (mMMessageItem != null && TextUtils.equals(str, mMMessageItem.messageId)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem getEarliestThread() {
        MMMessageItem mMMessageItem = null;
        for (MMMessageItem mMMessageItem2 : this.mDatas) {
            if (mMMessageItem2.isThread && (mMMessageItem == null || mMMessageItem2.visibleTime < mMMessageItem.visibleTime)) {
                mMMessageItem = mMMessageItem2;
            }
        }
        return mMMessageItem;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem getLatestThread() {
        MMMessageItem mMMessageItem = null;
        for (MMMessageItem mMMessageItem2 : this.mDatas) {
            if (mMMessageItem2.isThread && (mMMessageItem == null || mMMessageItem2.visibleTime > mMMessageItem.visibleTime)) {
                mMMessageItem = mMMessageItem2;
            }
        }
        return mMMessageItem;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem getThreadById(String str) {
        for (MMMessageItem mMMessageItem : this.mDatas) {
            if (TextUtils.equals(str, mMMessageItem.messageId)) {
                return mMMessageItem;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem findRecentMMMessageItem(int i) {
        if (i == -1 || i >= this.mDisplayDatas.size()) {
            List<MMMessageItem> list = this.mDatas;
            return (MMMessageItem) list.get(list.size() - 1);
        }
        for (int i2 = i; i2 < this.mDisplayDatas.size(); i2++) {
            BaseItem baseItem = (BaseItem) this.mDisplayDatas.get(i);
            if (baseItem.thread != null && baseItem.thread.isThread && !baseItem.thread.isSystemMsg() && baseItem.thread.messageType != 19 && baseItem.thread.messageType != 36) {
                return baseItem.thread;
            }
        }
        List<MMMessageItem> list2 = this.mDatas;
        return (MMMessageItem) list2.get(list2.size() - 1);
    }

    /* access modifiers changed from: 0000 */
    public void setSessionInfo(String str, boolean z, IMAddrBookItem iMAddrBookItem) {
        this.mSessionId = str;
        this.isGroup = z;
        this.mIMAddrBookItem = iMAddrBookItem;
    }

    private boolean isAnnounceMent() {
        boolean z = false;
        if (!this.isGroup) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(this.mSessionId);
        if (groupById != null && groupById.isBroadcast()) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: 0000 */
    public void setUICallBack(ThreadsUICallBack threadsUICallBack) {
        this.mCallBack = threadsUICallBack;
    }

    public boolean isEmpty() {
        return CollectionsUtil.isCollectionEmpty(this.mDatas);
    }

    /* access modifiers changed from: private */
    public void onChanged() {
        checkFailedMsgs();
        rebuildListItems();
        appendFailedMsgs();
    }

    /* access modifiers changed from: private */
    public void updateRange(int i, int i2) {
        if (i >= 0 && i < this.mDisplayDatas.size() && i2 > 0 && this.mMessageHelper != null) {
            for (int i3 = 0; i3 < i2; i3++) {
                int i4 = i + i3;
                if (i4 < this.mDisplayDatas.size()) {
                    BaseItem baseItem = (BaseItem) this.mDisplayDatas.get(i4);
                    if (!(baseItem instanceof ThreadItem)) {
                        continue;
                    } else {
                        MMMessageItem mMMessageItem = baseItem.thread;
                        if (mMMessageItem != null) {
                            ArrayList allAtMsgs = this.mMessageHelper.getAllAtMsgs(mMMessageItem.messageId);
                            HashSet<String> hashSet = new HashSet<>();
                            if (allAtMsgs != null) {
                                hashSet.addAll(allAtMsgs);
                            }
                            int markUnreadCountInThread = this.mMessageHelper.getMarkUnreadCountInThread(mMMessageItem.messageId) - 0;
                            int i5 = 0;
                            int i6 = 0;
                            for (String isMsgAtMe : hashSet) {
                                if (this.mMessageHelper.isMsgAtMe(isMsgAtMe)) {
                                    i6++;
                                } else {
                                    i5++;
                                }
                            }
                            if (markUnreadCountInThread <= 0) {
                                markUnreadCountInThread = 0;
                            }
                            mMMessageItem.markUnreadCommentCount = markUnreadCountInThread;
                            if (i5 <= 0) {
                                i5 = 0;
                            }
                            mMMessageItem.atAllCommentCount = i5;
                            if (i6 <= 0) {
                                i6 = 0;
                            }
                            mMMessageItem.atMeCommentCount = i6;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    private void rebuildListItems() {
        this.mDisplayDatas.clear();
        MMMessageItem mMMessageItem = this.mTimeChatTimeItem;
        if (mMMessageItem != null) {
            this.mDisplayDatas.add(new ThreadItem(mMMessageItem));
        }
        boolean z = this.mUnreadNewMarkMsg == null;
        MMMessageItem mMMessageItem2 = this.mUnreadNewMarkMsg;
        boolean z2 = mMMessageItem2 != null && (mMMessageItem2.serverSideTime == 0 || (this.mDatas.size() > 0 && ((MMMessageItem) this.mDatas.get(0)).visibleTime <= this.mUnreadNewMarkMsg.serverSideTime));
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                boolean z3 = false;
                for (int size = this.mDatas.size() - 1; size >= 0; size--) {
                    MMMessageItem mMMessageItem3 = (MMMessageItem) this.mDatas.get(size);
                    if (mMMessageItem3 != null && mMMessageItem3.canShowReaction()) {
                        if (z3) {
                            mMMessageItem3.showReaction = false;
                        } else {
                            mMMessageItem3.showReaction = true;
                            z3 = true;
                        }
                    }
                }
                boolean z4 = z;
                for (int i = 0; i < this.mDatas.size(); i++) {
                    MMMessageItem mMMessageItem4 = (MMMessageItem) this.mDatas.get(i);
                    if (!this.mFailedThreads.containsKey(mMMessageItem4.messageId) || (this.mThreadSortType != 0 && this.mFailedComments.containsKey(mMMessageItem4.messageId))) {
                        if (i == 0) {
                            mMMessageItem4.onlyMessageShow = false;
                        } else {
                            mMMessageItem4.onlyMessageShow = false;
                            MMMessageItem mMMessageItem5 = (MMMessageItem) this.mDatas.get(i - 1);
                            if (TextUtils.equals(mMMessageItem5.fromJid, mMMessageItem4.fromJid) && !mMMessageItem5.isSystemMsg() && mMMessageItem5.commentsCount == 0 && mMMessageItem5.messageType != 48 && mMMessageItem5.messageType != 50 && !sessionById.isMessageMarkUnread(mMMessageItem4.messageXMPPId) && !sessionById.isMessageMarkUnread(mMMessageItem5.messageXMPPId)) {
                                mMMessageItem4.onlyMessageShow = true;
                            }
                            if (this.mDisplayDatas.size() > 0) {
                                List<BaseItem> list = this.mDisplayDatas;
                                BaseItem baseItem = (BaseItem) list.get(list.size() - 1);
                                if (baseItem.thread != null && baseItem.thread.hasCommentsOdds == 1) {
                                    mMMessageItem4.onlyMessageShow = false;
                                }
                            }
                            if (mMMessageItem4.hasCommentsOdds == 1) {
                                mMMessageItem4.onlyMessageShow = false;
                            }
                        }
                        mMMessageItem4.hasFailedMessage = false;
                        if (!z4 && z2 && mMMessageItem4.visibleTime > this.mUnreadNewMarkMsg.serverSideTime) {
                            this.mDisplayDatas.add(new ThreadItem(this.mUnreadNewMarkMsg));
                            MMMessageItem mMMessageItem6 = new MMMessageItem();
                            mMMessageItem6.sessionId = this.mSessionId;
                            mMMessageItem6.messageTime = mMMessageItem4.visibleTime;
                            mMMessageItem6.serverSideTime = mMMessageItem4.visibleTime;
                            mMMessageItem6.visibleTime = mMMessageItem4.visibleTime;
                            mMMessageItem6.messageType = 19;
                            StringBuilder sb = new StringBuilder();
                            sb.append("time");
                            sb.append(mMMessageItem4.visibleTime);
                            mMMessageItem6.messageId = sb.toString();
                            mMMessageItem4.onlyMessageShow = false;
                            this.mDisplayDatas.add(new ThreadItem(mMMessageItem6));
                            z4 = true;
                        }
                        addItemToListItems(mMMessageItem4, true);
                    }
                }
                if (this.isBottomLoading && this.mDatas.size() > 0) {
                    List<BaseItem> list2 = this.mDisplayDatas;
                    List<MMMessageItem> list3 = this.mDatas;
                    list2.add(new ThreadItem(MMMessageItem.createLoadingMsg(((MMMessageItem) list3.get(list3.size() - 1)).visibleTime)));
                }
            }
        }
    }

    private void checkFailedMsgs() {
        Iterator it;
        Map map;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                List sendFailedMessages = zoomMessenger.getSendFailedMessages(this.mSessionId);
                if (CollectionsUtil.isCollectionEmpty(sendFailedMessages)) {
                    this.mFailedComments.clear();
                    this.mFailedThreads.clear();
                    return;
                }
                HashSet hashSet = new HashSet(sendFailedMessages);
                if (!CollectionsUtil.isCollectionEmpty(hashSet)) {
                    for (String str : this.mFailedComments.keySet()) {
                        Map map2 = (Map) this.mFailedComments.get(str);
                        if (map2 != null) {
                            Iterator it2 = new ArrayList(map2.keySet()).iterator();
                            while (it2.hasNext()) {
                                String str2 = (String) it2.next();
                                if (!hashSet.contains(str2)) {
                                    map2.remove(str2);
                                }
                            }
                            if (map2.isEmpty()) {
                                this.mFailedThreads.remove(str);
                            }
                        }
                    }
                    Iterator it3 = new ArrayList(this.mFailedThreads.keySet()).iterator();
                    while (it3.hasNext()) {
                        String str3 = (String) it3.next();
                        if (!hashSet.contains(str3) && !this.mFailedComments.containsKey(str3)) {
                            this.mFailedThreads.remove(str3);
                        }
                    }
                    ZoomChatSession findSessionById = zoomMessenger.findSessionById(this.mSessionId);
                    if (findSessionById != null) {
                        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                        Iterator it4 = hashSet.iterator();
                        while (it4.hasNext()) {
                            String str4 = (String) it4.next();
                            if (!this.mFailedThreads.containsKey(str4)) {
                                ZoomMessage messageById = findSessionById.getMessageById(str4);
                                if (messageById != null) {
                                    if (messageById.isComment()) {
                                        String threadID = messageById.getThreadID();
                                        if (!StringUtil.isEmptyOrNull(threadID)) {
                                            Map map3 = (Map) this.mFailedComments.get(threadID);
                                            if (map3 == null) {
                                                HashMap hashMap = new HashMap();
                                                this.mFailedComments.put(threadID, hashMap);
                                                map = hashMap;
                                            } else {
                                                map = map3;
                                            }
                                            if (!map.containsKey(str4)) {
                                                it = it4;
                                                Map map4 = map;
                                                MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(messageById, this.mSessionId, zoomMessenger, this.isGroup, true, this.mContext, this.mIMAddrBookItem, zoomFileContentMgr);
                                                if (initWithZoomMessage != null) {
                                                    map4.put(str4, initWithZoomMessage);
                                                }
                                            } else {
                                                it = it4;
                                            }
                                            if (!this.mFailedThreads.containsKey(threadID)) {
                                                ZoomMessage messageById2 = findSessionById.getMessageById(threadID);
                                                if (messageById2 != null) {
                                                    MMMessageItem initWithZoomMessage2 = MMMessageItem.initWithZoomMessage(messageById2, this.mSessionId, zoomMessenger, this.isGroup, true, this.mContext, this.mIMAddrBookItem, zoomFileContentMgr);
                                                    if (initWithZoomMessage2 != null) {
                                                        initWithZoomMessage2.hasCommentsOdds = threadDataProvider.threadHasCommentsOdds(messageById2);
                                                        this.mFailedThreads.put(threadID, initWithZoomMessage2);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        it = it4;
                                        MMMessageItem initWithZoomMessage3 = MMMessageItem.initWithZoomMessage(messageById, this.mSessionId, zoomMessenger, this.isGroup, true, this.mContext, this.mIMAddrBookItem, zoomFileContentMgr);
                                        if (initWithZoomMessage3 != null) {
                                            this.mFailedThreads.put(str4, initWithZoomMessage3);
                                        }
                                    }
                                    it4 = it;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void appendFailedMsgs() {
        ArrayList<MMMessageItem> arrayList = new ArrayList<>(this.mFailedThreads.values());
        if (!CollectionsUtil.isCollectionEmpty(arrayList)) {
            Collections.sort(arrayList, new Comparator<MMMessageItem>() {
                public int compare(MMMessageItem mMMessageItem, MMMessageItem mMMessageItem2) {
                    if (mMMessageItem.messageTime < mMMessageItem2.messageTime) {
                        return -1;
                    }
                    return mMMessageItem.messageTime > mMMessageItem2.messageTime ? 1 : 0;
                }
            });
            for (MMMessageItem mMMessageItem : arrayList) {
                if (this.mThreadSortType == 0 || !this.mFailedComments.containsKey(mMMessageItem.messageId)) {
                    addItemToListItems(mMMessageItem, false);
                }
            }
        }
    }

    public boolean hasMessageFromJid(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (MMMessageItem mMMessageItem : this.mDatas) {
            if (str.equals(mMMessageItem.fromJid)) {
                return true;
            }
            List<MMMessageItem> comments = mMMessageItem.getComments();
            if (!CollectionsUtil.isCollectionEmpty(comments)) {
                for (MMMessageItem mMMessageItem2 : comments) {
                    if (str.equals(mMMessageItem2.fromJid)) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0070  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addItemToListItems(com.zipow.videobox.view.p014mm.MMMessageItem r10, boolean r11) {
        /*
            r9 = this;
            r0 = 0
            r2 = 0
            if (r11 == 0) goto L_0x007c
            java.util.List<com.zipow.videobox.view.mm.MMThreadsAdapter$BaseItem> r11 = r9.mDisplayDatas
            int r11 = r11.size()
            if (r11 <= 0) goto L_0x001e
            int r11 = r9.getLastTimeItemIndex()
            if (r11 < 0) goto L_0x001e
            java.util.List<com.zipow.videobox.view.mm.MMThreadsAdapter$BaseItem> r3 = r9.mDisplayDatas
            java.lang.Object r11 = r3.get(r11)
            com.zipow.videobox.view.mm.MMThreadsAdapter$ThreadItem r11 = (com.zipow.videobox.view.p014mm.MMThreadsAdapter.ThreadItem) r11
            com.zipow.videobox.view.mm.MMMessageItem r11 = r11.thread
            goto L_0x001f
        L_0x001e:
            r11 = 0
        L_0x001f:
            long r3 = r10.visibleTime
            int r3 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r3 != 0) goto L_0x0028
            long r3 = r10.messageTime
            goto L_0x002a
        L_0x0028:
            long r3 = r10.visibleTime
        L_0x002a:
            if (r11 == 0) goto L_0x0040
            long r5 = r11.visibleTime
            long r5 = r3 - r5
            r7 = 300000(0x493e0, double:1.482197E-318)
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 > 0) goto L_0x0040
            r5 = 999(0x3e7, double:4.936E-321)
            long r5 = r5 + r3
            long r7 = r11.visibleTime
            int r11 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r11 >= 0) goto L_0x007c
        L_0x0040:
            com.zipow.videobox.view.mm.MMMessageItem r11 = new com.zipow.videobox.view.mm.MMMessageItem
            r11.<init>()
            java.lang.String r5 = r9.mSessionId
            r11.sessionId = r5
            r11.messageTime = r3
            r11.visibleTime = r3
            r5 = 19
            r11.messageType = r5
            r11.serverSideTime = r3
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "time"
            r5.append(r6)
            r5.append(r3)
            java.lang.String r3 = r5.toString()
            r11.messageId = r3
            java.lang.String r3 = "TIMED_CHAT_MSG_ID"
            java.lang.String r4 = r10.messageId
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x007a
            java.util.List<com.zipow.videobox.view.mm.MMThreadsAdapter$BaseItem> r3 = r9.mDisplayDatas
            com.zipow.videobox.view.mm.MMThreadsAdapter$ThreadItem r4 = new com.zipow.videobox.view.mm.MMThreadsAdapter$ThreadItem
            r4.<init>(r11)
            r3.add(r4)
        L_0x007a:
            r10.onlyMessageShow = r2
        L_0x007c:
            java.util.List<com.zipow.videobox.view.mm.MMThreadsAdapter$BaseItem> r11 = r9.mDisplayDatas
            com.zipow.videobox.view.mm.MMThreadsAdapter$ThreadItem r3 = new com.zipow.videobox.view.mm.MMThreadsAdapter$ThreadItem
            r3.<init>(r10)
            r11.add(r3)
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.zipow.videobox.view.mm.MMMessageItem>> r11 = r9.mFailedComments
            java.lang.String r3 = r10.messageId
            java.lang.Object r11 = r11.get(r3)
            java.util.Map r11 = (java.util.Map) r11
            r3 = 1
            if (r11 == 0) goto L_0x009b
            int r11 = r11.size()
            if (r11 <= 0) goto L_0x009b
            r10.hasFailedMessage = r3
        L_0x009b:
            int r11 = r10.hasCommentsOdds
            if (r11 == r3) goto L_0x00af
            long r3 = r10.commentsCount
            int r11 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r11 > 0) goto L_0x00af
            java.util.List r11 = r10.getComments()
            boolean r11 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r11)
            if (r11 != 0) goto L_0x00fd
        L_0x00af:
            com.zipow.videobox.util.MMMessageHelper r11 = r9.mMessageHelper
            if (r11 == 0) goto L_0x00fd
            java.lang.String r0 = r10.messageId
            java.util.ArrayList r11 = r11.getAllAtMsgs(r0)
            java.util.HashSet r0 = new java.util.HashSet
            r0.<init>()
            if (r11 == 0) goto L_0x00c3
            r0.addAll(r11)
        L_0x00c3:
            com.zipow.videobox.util.MMMessageHelper r11 = r9.mMessageHelper
            java.lang.String r1 = r10.messageId
            int r11 = r11.getMarkUnreadCountInThread(r1)
            int r11 = r11 - r2
            java.util.Iterator r0 = r0.iterator()
            r1 = 0
            r3 = 0
        L_0x00d2:
            boolean r4 = r0.hasNext()
            if (r4 == 0) goto L_0x00ec
            java.lang.Object r4 = r0.next()
            java.lang.String r4 = (java.lang.String) r4
            com.zipow.videobox.util.MMMessageHelper r5 = r9.mMessageHelper
            boolean r4 = r5.isMsgAtMe(r4)
            if (r4 == 0) goto L_0x00e9
            int r3 = r3 + 1
            goto L_0x00d2
        L_0x00e9:
            int r1 = r1 + 1
            goto L_0x00d2
        L_0x00ec:
            if (r11 <= 0) goto L_0x00ef
            goto L_0x00f0
        L_0x00ef:
            r11 = 0
        L_0x00f0:
            r10.markUnreadCommentCount = r11
            if (r1 <= 0) goto L_0x00f5
            goto L_0x00f6
        L_0x00f5:
            r1 = 0
        L_0x00f6:
            r10.atAllCommentCount = r1
            if (r3 <= 0) goto L_0x00fb
            r2 = r3
        L_0x00fb:
            r10.atMeCommentCount = r2
        L_0x00fd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMThreadsAdapter.addItemToListItems(com.zipow.videobox.view.mm.MMMessageItem, boolean):void");
    }

    private int getLastTimeItemIndex() {
        if (this.mDisplayDatas.size() == 0) {
            return -1;
        }
        for (int itemCount = getItemCount() - 1; itemCount >= 0; itemCount--) {
            Object obj = this.mDisplayDatas.get(itemCount);
            if (obj instanceof ThreadItem) {
                MMMessageItem mMMessageItem = ((ThreadItem) obj).thread;
                if (mMMessageItem != null && mMMessageItem.messageType == 19) {
                    return itemCount;
                }
            }
        }
        return -1;
    }

    public void setMessages(List<MMMessageItem> list) {
        this.mDatas.clear();
        if (list != null) {
            this.mDatas.addAll(list);
        }
    }

    public void setReadMsgTime(long j) {
        if (j == 0) {
            this.mUnreadNewMarkMsg = null;
        } else {
            this.mUnreadNewMarkMsg = MMMessageItem.createNewMsgsMark(j);
            this.isUnreadNewMarkReady = true;
        }
        notifyDataSetChanged();
    }

    @NonNull
    public List<MMMessageItem> getAllThreads() {
        return new ArrayList(this.mDatas);
    }

    public void removeLocalMsgs(Set<String> set) {
        if (!CollectionsUtil.isCollectionEmpty(set)) {
            Iterator it = this.mDatas.iterator();
            while (it.hasNext()) {
                if (set.contains(((MMMessageItem) it.next()).messageId)) {
                    it.remove();
                }
            }
        }
    }

    public void addMessages(List<MMMessageItem> list, int i) {
        if (!CollectionsUtil.isCollectionEmpty(list)) {
            if (list.size() > 1) {
                if ((((MMMessageItem) list.get(0)).visibleTime == 0 ? ((MMMessageItem) list.get(0)).messageTime : ((MMMessageItem) list.get(0)).visibleTime) > (((MMMessageItem) list.get(list.size() - 1)).visibleTime == 0 ? ((MMMessageItem) list.get(list.size() - 1)).messageTime : ((MMMessageItem) list.get(list.size() - 1)).visibleTime)) {
                    Collections.reverse(list);
                }
            }
            switch (i) {
                case 1:
                    this.mDatas.addAll(0, list);
                    break;
                case 2:
                    this.mDatas.addAll(list);
                    break;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveToLatest(String str) {
        if (!TextUtils.isEmpty(str)) {
            MMMessageItem mMMessageItem = null;
            Iterator it = this.mDatas.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MMMessageItem mMMessageItem2 = (MMMessageItem) it.next();
                if (TextUtils.equals(mMMessageItem2.messageId, str)) {
                    mMMessageItem = mMMessageItem2;
                    break;
                }
            }
            if (mMMessageItem != null) {
                moveToLatest(mMMessageItem);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateMessageItem(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null && mMMessageItem.isThread) {
            for (int i = 0; i < this.mDatas.size(); i++) {
                if (TextUtils.equals(((MMMessageItem) this.mDatas.get(i)).messageId, mMMessageItem.messageId)) {
                    this.mDatas.set(i, mMMessageItem);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveToLatest(MMMessageItem mMMessageItem) {
        boolean z;
        if (mMMessageItem != null && mMMessageItem.isThread) {
            int i = 0;
            while (true) {
                z = true;
                if (i >= this.mDatas.size()) {
                    break;
                } else if (!TextUtils.equals(((MMMessageItem) this.mDatas.get(i)).messageId, mMMessageItem.messageId)) {
                    i++;
                } else if (this.mThreadSortType == 1) {
                    this.mDatas.set(i, mMMessageItem);
                    return;
                } else {
                    this.mDatas.remove(i);
                }
            }
            int size = this.mDatas.size() - 1;
            while (true) {
                if (size < 0) {
                    z = false;
                    break;
                }
                MMMessageItem mMMessageItem2 = (MMMessageItem) this.mDatas.get(size);
                long j = mMMessageItem2.visibleTime;
                if (j == 0) {
                    j = mMMessageItem2.messageTime;
                }
                long j2 = mMMessageItem.visibleTime;
                if (j2 == 0) {
                    j2 = mMMessageItem.messageTime;
                }
                int i2 = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                if (i2 < 0 || (i2 == 0 && mMMessageItem2.serverSideTime <= mMMessageItem.serverSideTime)) {
                    this.mDatas.add(size + 1, mMMessageItem);
                } else {
                    size--;
                }
            }
            if (!z && !this.mIsLocalMsgDirty) {
                this.mDatas.add(0, mMMessageItem);
            }
        }
    }

    public void addFootLoadingView() {
        this.isBottomLoading = true;
    }

    public void removeLoadingView() {
        this.isBottomLoading = false;
    }

    public BaseItem getItemByPosition(int i) {
        List<BaseItem> list = this.mDisplayDatas;
        if (list != null && i >= 0 && i < list.size()) {
            return (BaseItem) this.mDisplayDatas.get(i);
        }
        return null;
    }

    public int getItemViewType(int i) {
        if (isShowFTE() && i == 0) {
            return 51;
        }
        BaseItem itemByPosition = getItemByPosition(i);
        if (itemByPosition instanceof ThreadItem) {
            return ((ThreadItem) itemByPosition).thread.messageType + 10000;
        }
        if (itemByPosition instanceof CommentItem) {
            return ((CommentItem) itemByPosition).comment.messageType + TYPE_COMMENT_START;
        }
        if (itemByPosition instanceof MoreReply) {
            return 2;
        }
        return itemByPosition instanceof AddReply ? 3 : 3;
    }

    public long getItemId(int i) {
        if (i < 0 || i > this.mDisplayDatas.size() - 1) {
            return -1;
        }
        BaseItem baseItem = (BaseItem) this.mDisplayDatas.get(i);
        if (baseItem == null) {
            return super.getItemId(i);
        }
        if ((baseItem instanceof ThreadItem) && baseItem.thread != null && baseItem.thread.messageId != null) {
            return (long) baseItem.thread.messageId.hashCode();
        }
        if (baseItem instanceof CommentItem) {
            MMMessageItem mMMessageItem = ((CommentItem) baseItem).comment;
            if (!(mMMessageItem == null || mMMessageItem.messageId == null)) {
                return (long) mMMessageItem.messageId.hashCode();
            }
        }
        return super.getItemId(i);
    }

    @NonNull
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 3) {
            return createAddReplyHolder();
        }
        if (i == 51) {
            return createSayHiFTEHolder();
        }
        if (i == 2) {
            return createMoreReplyHolder();
        }
        if (i >= 10000000) {
            return createCommentHolder(i);
        }
        if (i >= 10000) {
            return createThreadHolder(i);
        }
        return new CommentHolder(new View(this.mContext));
    }

    private CommentHolder createSayHiFTEHolder() {
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        View inflate = View.inflate(this.mContext, C4558R.layout.zm_mm_chat_session_fte_view, null);
        inflate.setLayoutParams(layoutParams);
        return new CommentHolder(inflate);
    }

    private CommentHolder createAddReplyHolder() {
        final MMCommentAddReplyView mMCommentAddReplyView = new MMCommentAddReplyView(this.mContext);
        mMCommentAddReplyView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (mMCommentAddReplyView.msgItem != null) {
                    MMThreadsAdapter.this.mCallBack.onAddComment(mMCommentAddReplyView.msgItem);
                }
            }
        });
        return new CommentHolder(mMCommentAddReplyView);
    }

    private CommentHolder createMoreReplyHolder() {
        final MMCommentMoreReplyView mMCommentMoreReplyView = new MMCommentMoreReplyView(this.mContext);
        mMCommentMoreReplyView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (mMCommentMoreReplyView.msgItem != null) {
                    MMThreadsAdapter.this.mCallBack.onMoreComment(mMCommentMoreReplyView.msgItem);
                }
            }
        });
        return new CommentHolder(mMCommentMoreReplyView);
    }

    private CommentHolder createCommentHolder(int i) {
        int i2 = i - TYPE_COMMENT_START;
        if (i2 < 0) {
            return new CommentHolder(new View(this.mContext)) {
            };
        }
        AbsMessageView createCommentView = MMMessageItem.createCommentView(this.mContext, i2);
        createCommentView.setOnShowContextMenuListener(this.mCallBack);
        createCommentView.setOnClickMessageListener(this.mCallBack);
        createCommentView.setOnClickStatusImageListener(this.mCallBack);
        createCommentView.setOnClickAvatarListener(this.mCallBack);
        createCommentView.setOnClickCancelListenter(this.mCallBack);
        createCommentView.setOnLongClickAvatarListener(this.mCallBack);
        createCommentView.setOnClickAddonListener(this.mCallBack);
        createCommentView.setOnClickMeetingNOListener(this.mCallBack);
        createCommentView.setmOnClickActionListener(this.mCallBack);
        createCommentView.setmOnClickActionMoreListener(this.mCallBack);
        createCommentView.setOnClickLinkPreviewListener(this.mCallBack);
        createCommentView.setmOnClickGiphyBtnListener(this.mCallBack);
        createCommentView.setmOnClickTemplateActionMoreListener(this.mCallBack);
        createCommentView.setmOnClickTemplateListener(this.mCallBack);
        createCommentView.setOnClickReactionLabelListener(this.mCallBack);
        return new CommentHolder(createCommentView);
    }

    private CommentHolder createThreadHolder(int i) {
        int i2 = i - 10000;
        if (i2 < 0) {
            return new CommentHolder(new View(this.mContext)) {
            };
        }
        AbsMessageView createView = MMMessageItem.createView(this.mContext, i2);
        if (createView == null) {
            return new CommentHolder(new View(this.mContext)) {
            };
        }
        CommentHolder commentHolder = new CommentHolder(createView);
        createView.setOnShowContextMenuListener(this.mCallBack);
        createView.setOnClickMessageListener(this.mCallBack);
        createView.setOnClickStatusImageListener(this.mCallBack);
        createView.setOnClickAvatarListener(this.mCallBack);
        createView.setOnClickCancelListenter(this.mCallBack);
        createView.setOnLongClickAvatarListener(this.mCallBack);
        createView.setOnClickAddonListener(this.mCallBack);
        createView.setOnClickMeetingNOListener(this.mCallBack);
        createView.setmOnClickActionListener(this.mCallBack);
        createView.setmOnClickActionMoreListener(this.mCallBack);
        createView.setOnClickLinkPreviewListener(this.mCallBack);
        createView.setmOnClickGiphyBtnListener(this.mCallBack);
        createView.setmOnClickTemplateActionMoreListener(this.mCallBack);
        createView.setmOnClickTemplateListener(this.mCallBack);
        createView.setOnClickReactionLabelListener(this.mCallBack);
        return commentHolder;
    }

    public void onBindViewHolder(@NonNull CommentHolder commentHolder, int i) {
        if (commentHolder.getItemViewType() == 51) {
            bindFTEView(commentHolder.itemView);
            return;
        }
        BaseItem itemByPosition = getItemByPosition(i);
        int itemViewType = getItemViewType(i);
        if (itemViewType == 3) {
            bindAddReply(commentHolder, itemByPosition);
        } else if (itemViewType == 2) {
            bindMoreReply(commentHolder, itemByPosition);
        } else if (itemViewType >= 10000000) {
            bindComment(commentHolder, itemByPosition);
        } else if (itemViewType >= 10000) {
            bindThread(commentHolder, itemByPosition);
        }
    }

    private void bindAddReply(@NonNull CommentHolder commentHolder, @NonNull BaseItem baseItem) {
        ((MMCommentAddReplyView) commentHolder.itemView).msgItem = baseItem.thread;
    }

    private void bindMoreReply(@NonNull CommentHolder commentHolder, @NonNull BaseItem baseItem) {
        MMCommentMoreReplyView mMCommentMoreReplyView = (MMCommentMoreReplyView) commentHolder.itemView;
        mMCommentMoreReplyView.msgItem = baseItem.thread;
        int i = (int) baseItem.thread.commentsCount;
        if (i == 0) {
            i = baseItem.thread.getComments().size();
        }
        if (i == 0) {
            mMCommentMoreReplyView.moreReply.setText(C4558R.string.zm_lbl_reply_nosure_count_88133);
        } else {
            mMCommentMoreReplyView.moreReply.setText(this.mContext.getResources().getQuantityString(C4558R.plurals.zm_lbl_comment_more_reply_88133, i, new Object[]{Integer.valueOf(i)}));
        }
        if (mMCommentMoreReplyView.unreadBubble != null) {
            if (baseItem.thread.unreadCommentCount > 0) {
                mMCommentMoreReplyView.txtNoteBubble.setVisibility(8);
                mMCommentMoreReplyView.unreadBubble.setVisibility(0);
            } else {
                mMCommentMoreReplyView.txtNoteBubble.setVisibility(8);
                mMCommentMoreReplyView.unreadBubble.setVisibility(8);
            }
        }
        if (mMCommentMoreReplyView.txtMarkUnread != null) {
            if (baseItem.thread.markUnreadCommentCount > 0) {
                mMCommentMoreReplyView.txtMarkUnread.setText(this.mContext.getResources().getString(C4558R.string.zm_lbl_comment_mark_unread_88133, new Object[]{Integer.valueOf(baseItem.thread.markUnreadCommentCount)}));
                mMCommentMoreReplyView.txtMarkUnread.setVisibility(0);
            } else {
                mMCommentMoreReplyView.txtMarkUnread.setVisibility(8);
            }
        }
        if (mMCommentMoreReplyView.txtAtAll != null) {
            if (baseItem.thread.atAllCommentCount > 0) {
                mMCommentMoreReplyView.txtAtAll.setText(this.mContext.getResources().getString(C4558R.string.zm_lbl_comment_at_all_88133, new Object[]{Integer.valueOf(baseItem.thread.atAllCommentCount)}));
                mMCommentMoreReplyView.txtAtAll.setVisibility(0);
            } else {
                mMCommentMoreReplyView.txtAtAll.setVisibility(8);
            }
        }
        if (mMCommentMoreReplyView.txtAtMe == null) {
            return;
        }
        if (baseItem.thread.atMeCommentCount > 0) {
            mMCommentMoreReplyView.txtAtMe.setText(this.mContext.getResources().getString(C4558R.string.zm_lbl_comment_at_me_88133, new Object[]{Integer.valueOf(baseItem.thread.atMeCommentCount)}));
            mMCommentMoreReplyView.txtAtMe.setVisibility(0);
            return;
        }
        mMCommentMoreReplyView.txtAtMe.setVisibility(8);
    }

    private void bindComment(@NonNull CommentHolder commentHolder, @NonNull BaseItem baseItem) {
        MMMessageItem mMMessageItem = ((CommentItem) baseItem).comment;
        mMMessageItem.bindViewHolder(commentHolder);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-1, -2);
        layoutParams.setMarginStart(UIUtil.dip2px(this.mContext, 48.0f));
        commentHolder.itemView.setLayoutParams(layoutParams);
        ThreadsUICallBack threadsUICallBack = this.mCallBack;
        if (threadsUICallBack != null) {
            threadsUICallBack.onMessageShowed(mMMessageItem);
        }
    }

    private void bindThread(@NonNull CommentHolder commentHolder, @NonNull BaseItem baseItem) {
        MMMessageItem mMMessageItem = baseItem.thread;
        mMMessageItem.bindViewHolder(commentHolder);
        ThreadsUICallBack threadsUICallBack = this.mCallBack;
        if (threadsUICallBack != null) {
            threadsUICallBack.onMessageShowed(mMMessageItem);
        }
    }

    private void bindFTEView(@Nullable View view) {
        if (view != null) {
            String str = "";
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mSessionId);
                if (buddyWithJID != null) {
                    str = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                }
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtMessage);
            String string = this.mContext.getString(C4558R.string.zm_lbl_say_hi_to_somebody_79032, new Object[]{str});
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
            Matcher matcher = Pattern.compile(this.mContext.getString(C4558R.string.zm_lbl_message_body_say_hi_79032)).matcher(string);
            while (matcher.find()) {
                spannableStringBuilder.setSpan(new StyleSpan(1), matcher.start(), matcher.end(), 33);
            }
            textView.setText(spannableStringBuilder);
            ((TextView) view.findViewById(C4558R.C4560id.btnSayHi)).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (MMThreadsAdapter.this.mCallBack != null) {
                        MMThreadsAdapter.this.mCallBack.onSayHi();
                    }
                }
            });
        }
    }

    public void setNoMessage(boolean z) {
        this.mNoMessages = z;
    }

    public boolean isShowFTE() {
        if (!isAnnounceMent() && this.mNoMessages) {
            List<BaseItem> list = this.mDisplayDatas;
            if (list == null || list.size() == 0) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public MMMessageItem getItem(int i) {
        if ((!isShowFTE() || i != 0) && i >= 0 && i < getItemCount()) {
            return (MMMessageItem) this.mDatas.get(i);
        }
        return null;
    }

    public int getItemCount() {
        if (isShowFTE()) {
            return 1;
        }
        List<BaseItem> list = this.mDisplayDatas;
        return list == null ? 0 : list.size();
    }

    public int findItemInAdapter(String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            BaseItem baseItem = (BaseItem) this.mDisplayDatas.get(i);
            if ((baseItem instanceof ThreadItem) && TextUtils.equals(str, ((ThreadItem) baseItem).thread.messageId)) {
                return i;
            }
            if ((baseItem instanceof CommentItem) && TextUtils.equals(str, ((CommentItem) baseItem).comment.messageId)) {
                return i;
            }
        }
        return -1;
    }

    public MMMessageItem findMessageItemInAdapter(long j) {
        if (j <= 0) {
            return null;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            BaseItem baseItem = (BaseItem) this.mDisplayDatas.get(i);
            MMMessageItem mMMessageItem = baseItem.thread;
            if ((baseItem instanceof ThreadItem) && mMMessageItem != null && j == mMMessageItem.serverSideTime && !mMMessageItem.isSystemMsg() && mMMessageItem.messageType != 19) {
                return mMMessageItem;
            }
            if (baseItem instanceof CommentItem) {
                CommentItem commentItem = (CommentItem) baseItem;
                if (j == commentItem.comment.serverSideTime) {
                    return commentItem.comment;
                }
            }
        }
        return null;
    }

    public MMMessageItem findMessageItemInAdapter(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            BaseItem baseItem = (BaseItem) this.mDisplayDatas.get(i);
            if (baseItem instanceof ThreadItem) {
                ThreadItem threadItem = (ThreadItem) baseItem;
                if (TextUtils.equals(str, threadItem.thread.threadId)) {
                    return threadItem.thread;
                }
            }
            if (baseItem instanceof CommentItem) {
                CommentItem commentItem = (CommentItem) baseItem;
                if (TextUtils.equals(str, commentItem.comment.threadId)) {
                    return commentItem.comment;
                }
            }
        }
        return null;
    }

    public int findItemInAdapter(long j) {
        if (j <= 0) {
            return -1;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            BaseItem baseItem = (BaseItem) this.mDisplayDatas.get(i);
            if ((baseItem instanceof ThreadItem) && j == ((ThreadItem) baseItem).thread.serverSideTime) {
                return i;
            }
            if ((baseItem instanceof CommentItem) && j == ((CommentItem) baseItem).comment.serverSideTime) {
                return i;
            }
        }
        return -1;
    }

    public MMMessageItem getItemByMessageId(String str) {
        for (MMMessageItem mMMessageItem : this.mDatas) {
            if (TextUtils.equals(str, mMMessageItem.messageId)) {
                return mMMessageItem;
            }
        }
        return null;
    }

    public MMMessageItem getItemByMessageSVR(long j) {
        for (MMMessageItem mMMessageItem : this.mDatas) {
            if (j == mMMessageItem.serverSideTime) {
                return mMMessageItem;
            }
        }
        return null;
    }

    @Nullable
    public List<MMMessageItem> getItemsByFileId(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mDatas.size(); i++) {
            MMMessageItem mMMessageItem = (MMMessageItem) this.mDatas.get(i);
            if (str.equals(mMMessageItem.fileId)) {
                arrayList.add(mMMessageItem);
            }
        }
        return arrayList;
    }

    public void removeItemByFileId(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Iterator it = this.mDatas.iterator();
            while (it.hasNext()) {
                if (StringUtil.isSameString(((MMMessageItem) it.next()).fileId, str)) {
                    it.remove();
                }
            }
        }
    }

    public MMMessageItem removeItem(String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < this.mDatas.size(); i++) {
            if (str.equals(((MMMessageItem) this.mDatas.get(i)).messageId)) {
                return (MMMessageItem) this.mDatas.remove(i);
            }
        }
        return null;
    }

    public void removeHistory(long j) {
        Iterator it = this.mDatas.iterator();
        while (it.hasNext()) {
            if (((MMMessageItem) it.next()).visibleTime < j) {
                it.remove();
            }
        }
    }

    public void addItemAtFirst(MMMessageItem mMMessageItem) {
        addUpdateItem(0, mMMessageItem);
    }

    private void addUpdateItem(int i, MMMessageItem mMMessageItem) {
        int threadIndexById = getThreadIndexById(mMMessageItem.messageId);
        if (threadIndexById >= 0) {
            this.mDatas.set(threadIndexById, mMMessageItem);
        } else if (i < 0) {
            this.mDatas.add(mMMessageItem);
        } else {
            this.mDatas.add(i, mMMessageItem);
        }
    }

    public List<MMMessageItem> getAllCacheMessages() {
        ArrayList arrayList = new ArrayList();
        for (BaseItem baseItem : this.mDisplayDatas) {
            if (baseItem instanceof ThreadItem) {
                arrayList.add(((ThreadItem) baseItem).thread);
            } else if (baseItem instanceof CommentItem) {
                arrayList.add(((CommentItem) baseItem).comment);
            }
        }
        return arrayList;
    }

    public boolean isLastItem(String str) {
        MMMessageItem latestThread = getLatestThread();
        if (latestThread == null) {
            return false;
        }
        return StringUtil.isSameString(str, latestThread.messageId);
    }

    public boolean isUnreadNewMessage(long j) {
        boolean z = false;
        if (!hasUnreadMark()) {
            return false;
        }
        if (j >= this.mUnreadNewMarkMsg.serverSideTime) {
            z = true;
        }
        return z;
    }
}
