package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
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
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMCommentsAdapter */
public class MMCommentsAdapter extends Adapter<ViewHolder> {
    public static final int TYPE_THREAD_START = 10000;
    private boolean isBottomLoading = false;
    private boolean isCommentHistoryReady = false;
    private boolean isGroup;
    private boolean isHistoryLoading = false;
    /* access modifiers changed from: private */
    public ThreadsUICallBack mCallBack;
    private Context mContext;
    private List<MMMessageItem> mDatas = new ArrayList();
    private List<MMMessageItem> mDisplayDatas = new ArrayList();
    private Map<String, MMMessageItem> mFailedCommentss = new HashMap();
    private IMAddrBookItem mIMAddrBookItem;
    private OnClickListener mLoadingMoreClickListener = new OnClickListener() {
        public void onClick(View view) {
            if (MMCommentsAdapter.this.mCallBack != null) {
                MMCommentsAdapter.this.mCallBack.onLoadingMore();
            }
        }
    };
    private String mSessionId;
    private MMMessageItem mThreadItem;
    private MMMessageItem mTimeChatTimeItem;
    private MMMessageItem mUnreadNewMarkMsg;
    private String threadId;

    MMCommentsAdapter(@NonNull Context context) {
        this.mContext = context;
        setHasStableIds(true);
        registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                MMCommentsAdapter.this.checkFailedMsgs();
                MMCommentsAdapter.this.rebuildListItems();
                MMCommentsAdapter.this.appendFailedMsgs();
            }
        });
    }

    public void setSessionInfo(@NonNull String str, IMAddrBookItem iMAddrBookItem, boolean z, @NonNull String str2) {
        this.mSessionId = str;
        this.mIMAddrBookItem = iMAddrBookItem;
        this.isGroup = z;
        this.threadId = str2;
    }

    public void setThread(MMMessageItem mMMessageItem) {
        this.mThreadItem = mMMessageItem;
        updateThreadForCommentShow();
    }

    public void setCommentHistoryReady() {
        this.isCommentHistoryReady = true;
    }

    public boolean isCommentHistoryReady() {
        return this.isCommentHistoryReady;
    }

    public void setHistoryLoading(boolean z) {
        this.isHistoryLoading = z;
    }

    /* access modifiers changed from: 0000 */
    public void setThreadDeleted() {
        MMMessageItem mMMessageItem = this.mThreadItem;
        if (mMMessageItem != null) {
            mMMessageItem.isDeletedThread = true;
            mMMessageItem.messageType = 48;
        }
    }

    private void updateThreadForCommentShow() {
        MMMessageItem mMMessageItem = this.mThreadItem;
        if (mMMessageItem != null) {
            mMMessageItem.hasCommentsOdds = 2;
            mMMessageItem.commentsCount = 0;
            mMMessageItem.draftReply = null;
            if (mMMessageItem.messageType == 1) {
                MMMessageItem mMMessageItem2 = this.mThreadItem;
                mMMessageItem2.messageType = 0;
                Context context = this.mContext;
                if (context != null) {
                    mMMessageItem2.fromScreenName = context.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 3) {
                MMMessageItem mMMessageItem3 = this.mThreadItem;
                mMMessageItem3.messageType = 2;
                mMMessageItem3.isPlayed = true;
                Context context2 = this.mContext;
                if (context2 != null) {
                    mMMessageItem3.fromScreenName = context2.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 5) {
                MMMessageItem mMMessageItem4 = this.mThreadItem;
                mMMessageItem4.messageType = 4;
                Context context3 = this.mContext;
                if (context3 != null) {
                    mMMessageItem4.fromScreenName = context3.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 7) {
                MMMessageItem mMMessageItem5 = this.mThreadItem;
                mMMessageItem5.messageType = 6;
                Context context4 = this.mContext;
                if (context4 != null) {
                    mMMessageItem5.fromScreenName = context4.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 11) {
                MMMessageItem mMMessageItem6 = this.mThreadItem;
                mMMessageItem6.messageType = 10;
                Context context5 = this.mContext;
                if (context5 != null) {
                    mMMessageItem6.fromScreenName = context5.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 28) {
                MMMessageItem mMMessageItem7 = this.mThreadItem;
                mMMessageItem7.messageType = 27;
                Context context6 = this.mContext;
                if (context6 != null) {
                    mMMessageItem7.fromScreenName = context6.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 32) {
                MMMessageItem mMMessageItem8 = this.mThreadItem;
                mMMessageItem8.messageType = 33;
                Context context7 = this.mContext;
                if (context7 != null) {
                    mMMessageItem8.fromScreenName = context7.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 34) {
                MMMessageItem mMMessageItem9 = this.mThreadItem;
                mMMessageItem9.messageType = 35;
                Context context8 = this.mContext;
                if (context8 != null) {
                    mMMessageItem9.fromScreenName = context8.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 38) {
                MMMessageItem mMMessageItem10 = this.mThreadItem;
                mMMessageItem10.messageType = 37;
                Context context9 = this.mContext;
                if (context9 != null) {
                    mMMessageItem10.fromScreenName = context9.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            if (this.mThreadItem.messageType == 45) {
                MMMessageItem mMMessageItem11 = this.mThreadItem;
                mMMessageItem11.messageType = 46;
                Context context10 = this.mContext;
                if (context10 != null) {
                    mMMessageItem11.fromScreenName = context10.getString(C4558R.string.zm_lbl_content_you);
                }
            }
            checkThreadItemStarred();
        }
    }

    private void checkThreadItemStarred() {
        if (this.mThreadItem != null && this.mSessionId != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                MMMessageItem mMMessageItem = this.mThreadItem;
                mMMessageItem.isMessgeStarred = zoomMessenger.isStarMessage(this.mSessionId, mMMessageItem.serverSideTime);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public List<MMMessageItem> getAllComments() {
        return new ArrayList(this.mDatas);
    }

    /* access modifiers changed from: 0000 */
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

    /* access modifiers changed from: 0000 */
    public int getLocalCommentsCount() {
        return this.mDatas.size();
    }

    /* access modifiers changed from: 0000 */
    public void setUnreadTime(long j) {
        if (j == 0) {
            this.mUnreadNewMarkMsg = null;
        } else {
            this.mUnreadNewMarkMsg = MMMessageItem.createNewCommentMark(j);
        }
        notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    public void setTimeChatTimeItem(MMMessageItem mMMessageItem) {
        this.mTimeChatTimeItem = mMMessageItem;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem findRecentMMMessageItem(int i) {
        if (i == -1 || i >= this.mDisplayDatas.size()) {
            List<MMMessageItem> list = this.mDatas;
            return (MMMessageItem) list.get(list.size() - 1);
        }
        for (int i2 = i; i2 < this.mDisplayDatas.size(); i2++) {
            MMMessageItem mMMessageItem = (MMMessageItem) this.mDisplayDatas.get(i);
            if (mMMessageItem.isThread && !mMMessageItem.isSystemMsg() && mMMessageItem.messageType != 19 && mMMessageItem.messageType != 36) {
                return mMMessageItem;
            }
        }
        List<MMMessageItem> list2 = this.mDatas;
        return (MMMessageItem) list2.get(list2.size() - 1);
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem getItemByPosition(int i) {
        List<MMMessageItem> list = this.mDisplayDatas;
        if (list != null && i >= 0 && i < list.size()) {
            return (MMMessageItem) this.mDisplayDatas.get(i);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem getEarliestComment() {
        MMMessageItem mMMessageItem = null;
        for (MMMessageItem mMMessageItem2 : this.mDatas) {
            if (!mMMessageItem2.isThread && (mMMessageItem == null || mMMessageItem2.messageTime < mMMessageItem.messageTime || (mMMessageItem2.messageTime == mMMessageItem.messageTime && mMMessageItem2.serverSideTime < mMMessageItem.serverSideTime))) {
                mMMessageItem = mMMessageItem2;
            }
        }
        return mMMessageItem;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem getLatestComment() {
        MMMessageItem mMMessageItem = null;
        for (MMMessageItem mMMessageItem2 : this.mDatas) {
            if (!mMMessageItem2.isThread && (mMMessageItem == null || mMMessageItem2.messageTime > mMMessageItem.messageTime || (mMMessageItem2.messageTime == mMMessageItem.messageTime && mMMessageItem2.serverSideTime > mMMessageItem.serverSideTime))) {
                mMMessageItem = mMMessageItem2;
            }
        }
        return mMMessageItem;
    }

    /* access modifiers changed from: private */
    public void checkFailedMsgs() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            List sendFailedMessages = zoomMessenger.getSendFailedMessages(this.mSessionId);
            if (CollectionsUtil.isCollectionEmpty(sendFailedMessages)) {
                this.mFailedCommentss.clear();
                return;
            }
            HashSet<String> hashSet = new HashSet<>(sendFailedMessages);
            if (!CollectionsUtil.isCollectionEmpty(hashSet)) {
                ZoomChatSession findSessionById = zoomMessenger.findSessionById(this.mSessionId);
                if (findSessionById != null) {
                    Iterator it = new ArrayList(this.mFailedCommentss.keySet()).iterator();
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        if (!hashSet.contains(str)) {
                            this.mFailedCommentss.remove(str);
                        }
                    }
                    MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                    for (String str2 : hashSet) {
                        if (!this.mFailedCommentss.containsKey(str2)) {
                            ZoomMessage messageById = findSessionById.getMessageById(str2);
                            if (messageById != null && messageById.isComment() && TextUtils.equals(messageById.getThreadID(), this.threadId)) {
                                MMMessageItem initWithZoomMessage = MMMessageItem.initWithZoomMessage(messageById, this.mSessionId, zoomMessenger, this.isGroup, true, this.mContext, this.mIMAddrBookItem, zoomFileContentMgr);
                                if (initWithZoomMessage != null) {
                                    this.mFailedCommentss.put(str2, initWithZoomMessage);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void appendFailedMsgs() {
        ArrayList<MMMessageItem> arrayList = new ArrayList<>(this.mFailedCommentss.values());
        if (!CollectionsUtil.isCollectionEmpty(arrayList)) {
            Collections.sort(arrayList, new Comparator<MMMessageItem>() {
                public int compare(MMMessageItem mMMessageItem, MMMessageItem mMMessageItem2) {
                    if (mMMessageItem.messageTime < mMMessageItem2.messageTime) {
                        return -1;
                    }
                    return mMMessageItem.messageTime > mMMessageItem2.messageTime ? 1 : 0;
                }
            });
            for (MMMessageItem add : arrayList) {
                this.mDisplayDatas.add(add);
            }
        }
    }

    /* access modifiers changed from: private */
    public void rebuildListItems() {
        this.mDisplayDatas.clear();
        MMMessageItem mMMessageItem = this.mThreadItem;
        if (mMMessageItem != null) {
            this.mDisplayDatas.add(mMMessageItem);
            this.mDisplayDatas.add(MMMessageItem.createCommentSplit(this.mThreadItem.serverSideTime));
            if (!this.isCommentHistoryReady) {
                if (this.isHistoryLoading) {
                    this.mDisplayDatas.add(MMMessageItem.createLoadingMsg(this.mThreadItem.serverSideTime));
                } else {
                    this.mDisplayDatas.add(MMMessageItem.createLoadingMore());
                }
            }
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                boolean z = false;
                for (int size = this.mDatas.size() - 1; size >= 0; size--) {
                    MMMessageItem mMMessageItem2 = (MMMessageItem) this.mDatas.get(size);
                    if (mMMessageItem2 != null && mMMessageItem2.canShowReaction()) {
                        if (z) {
                            mMMessageItem2.showReaction = false;
                        } else {
                            mMMessageItem2.showReaction = true;
                            z = true;
                        }
                    }
                }
                boolean z2 = this.mUnreadNewMarkMsg == null;
                for (int i = 0; i < this.mDatas.size(); i++) {
                    MMMessageItem mMMessageItem3 = (MMMessageItem) this.mDatas.get(i);
                    if (!this.mFailedCommentss.containsKey(mMMessageItem3.messageId)) {
                        if (i == 0) {
                            mMMessageItem3.onlyMessageShow = false;
                        } else {
                            mMMessageItem3.onlyMessageShow = false;
                            MMMessageItem mMMessageItem4 = (MMMessageItem) this.mDatas.get(i - 1);
                            if (TextUtils.equals(mMMessageItem4.fromJid, mMMessageItem3.fromJid) && !mMMessageItem4.isSystemMsg() && !sessionById.isMessageMarkUnread(mMMessageItem3.messageXMPPId) && !sessionById.isMessageMarkUnread(mMMessageItem4.messageXMPPId)) {
                                mMMessageItem3.onlyMessageShow = true;
                            }
                        }
                        if (!z2 && mMMessageItem3.serverSideTime > this.mUnreadNewMarkMsg.serverSideTime) {
                            if (i != 0 || !this.isCommentHistoryReady) {
                                this.mDisplayDatas.add(this.mUnreadNewMarkMsg);
                                MMMessageItem mMMessageItem5 = new MMMessageItem();
                                mMMessageItem5.sessionId = this.mSessionId;
                                mMMessageItem5.messageTime = mMMessageItem3.serverSideTime;
                                mMMessageItem5.serverSideTime = mMMessageItem3.serverSideTime;
                                mMMessageItem5.visibleTime = mMMessageItem3.serverSideTime;
                                mMMessageItem5.messageType = 19;
                                StringBuilder sb = new StringBuilder();
                                sb.append("time");
                                sb.append(mMMessageItem3.serverSideTime);
                                mMMessageItem5.messageId = sb.toString();
                                mMMessageItem3.onlyMessageShow = false;
                                this.mDisplayDatas.add(mMMessageItem5);
                            }
                            z2 = true;
                        }
                        addItemToListItems(mMMessageItem3);
                    }
                }
                if (this.isBottomLoading && this.mDatas.size() > 0) {
                    List<MMMessageItem> list = this.mDisplayDatas;
                    List<MMMessageItem> list2 = this.mDatas;
                    list.add(MMMessageItem.createLoadingMsg(((MMMessageItem) list2.get(list2.size() - 1)).visibleTime));
                }
                if (this.mThreadItem != null) {
                    MMMessageItem mMMessageItem6 = new MMMessageItem();
                    mMMessageItem6.sessionId = this.mSessionId;
                    mMMessageItem6.messageTime = this.mThreadItem.serverSideTime;
                    mMMessageItem6.serverSideTime = this.mThreadItem.serverSideTime;
                    mMMessageItem6.visibleTime = this.mThreadItem.serverSideTime;
                    mMMessageItem6.messageType = 19;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("time");
                    sb2.append(System.currentTimeMillis());
                    mMMessageItem6.messageId = sb2.toString();
                    this.mDisplayDatas.add(0, mMMessageItem6);
                }
            }
        }
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

    /* access modifiers changed from: 0000 */
    public MMMessageItem findMessageItemInAdapter(long j) {
        MMMessageItem mMMessageItem = this.mThreadItem;
        if (mMMessageItem != null && j == mMMessageItem.serverSideTime) {
            return this.mThreadItem;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            MMMessageItem mMMessageItem2 = (MMMessageItem) this.mDisplayDatas.get(i);
            if (mMMessageItem2.serverSideTime == j && !mMMessageItem2.isSystemMsg()) {
                return mMMessageItem2;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public int findItemInAdapter(String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            if (TextUtils.equals(str, ((MMMessageItem) this.mDisplayDatas.get(i)).messageId)) {
                return i;
            }
        }
        return -1;
    }

    private int findItemInData(String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mDatas.size(); i++) {
            if (TextUtils.equals(str, ((MMMessageItem) this.mDatas.get(i)).messageId)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public int findItemInAdapter(long j) {
        if (j <= 0) {
            return -1;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            if (j == ((MMMessageItem) this.mDisplayDatas.get(i)).serverSideTime) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem getItemByMessageId(String str) {
        MMMessageItem mMMessageItem = this.mThreadItem;
        if (mMMessageItem != null && TextUtils.equals(str, mMMessageItem.messageId)) {
            return this.mThreadItem;
        }
        for (MMMessageItem mMMessageItem2 : this.mDatas) {
            if (TextUtils.equals(str, mMMessageItem2.messageId)) {
                return mMMessageItem2;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public MMMessageItem getItemByMessageSVR(long j) {
        for (MMMessageItem mMMessageItem : this.mDatas) {
            if (j == mMMessageItem.serverSideTime) {
                return mMMessageItem;
            }
        }
        MMMessageItem mMMessageItem2 = this.mThreadItem;
        if (mMMessageItem2 == null || j != mMMessageItem2.serverSideTime) {
            return null;
        }
        return this.mThreadItem;
    }

    /* access modifiers changed from: 0000 */
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

    /* access modifiers changed from: 0000 */
    public void removeHistory(long j) {
        Iterator it = this.mDatas.iterator();
        while (it.hasNext()) {
            if (((MMMessageItem) it.next()).messageTime < j) {
                it.remove();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void clearDatas() {
        this.mDatas.clear();
        this.isCommentHistoryReady = false;
    }

    /* access modifiers changed from: 0000 */
    public boolean isEmpty() {
        return this.mDatas.isEmpty();
    }

    /* access modifiers changed from: 0000 */
    public void setUICallBack(ThreadsUICallBack threadsUICallBack) {
        this.mCallBack = threadsUICallBack;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0020  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addItemToListItems(com.zipow.videobox.view.p014mm.MMMessageItem r8) {
        /*
            r7 = this;
            java.util.List<com.zipow.videobox.view.mm.MMMessageItem> r0 = r7.mDisplayDatas
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0017
            int r0 = r7.getLastTimeItemIndex()
            if (r0 < 0) goto L_0x0017
            java.util.List<com.zipow.videobox.view.mm.MMMessageItem> r1 = r7.mDisplayDatas
            java.lang.Object r0 = r1.get(r0)
            com.zipow.videobox.view.mm.MMMessageItem r0 = (com.zipow.videobox.view.p014mm.MMMessageItem) r0
            goto L_0x0018
        L_0x0017:
            r0 = 0
        L_0x0018:
            long r1 = r8.serverSideTime
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 != 0) goto L_0x0023
            long r1 = r8.messageTime
            goto L_0x0025
        L_0x0023:
            long r1 = r8.serverSideTime
        L_0x0025:
            if (r0 == 0) goto L_0x003b
            long r3 = r0.serverSideTime
            long r3 = r1 - r3
            r5 = 300000(0x493e0, double:1.482197E-318)
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 > 0) goto L_0x003b
            r3 = 999(0x3e7, double:4.936E-321)
            long r3 = r3 + r1
            long r5 = r0.serverSideTime
            int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r0 >= 0) goto L_0x0071
        L_0x003b:
            com.zipow.videobox.view.mm.MMMessageItem r0 = new com.zipow.videobox.view.mm.MMMessageItem
            r0.<init>()
            java.lang.String r3 = r7.mSessionId
            r0.sessionId = r3
            r0.messageTime = r1
            r3 = 19
            r0.messageType = r3
            r0.serverSideTime = r1
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "time"
            r3.append(r4)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            r0.messageId = r1
            java.lang.String r1 = r8.messageId
            java.lang.String r2 = "TIMED_CHAT_MSG_ID"
            boolean r1 = android.text.TextUtils.equals(r1, r2)
            if (r1 != 0) goto L_0x006e
            java.util.List<com.zipow.videobox.view.mm.MMMessageItem> r1 = r7.mDisplayDatas
            r1.add(r0)
        L_0x006e:
            r0 = 0
            r8.onlyMessageShow = r0
        L_0x0071:
            java.util.List<com.zipow.videobox.view.mm.MMMessageItem> r0 = r7.mDisplayDatas
            r0.add(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMCommentsAdapter.addItemToListItems(com.zipow.videobox.view.mm.MMMessageItem):void");
    }

    /* access modifiers changed from: 0000 */
    public void addFootLoadingView() {
        this.isBottomLoading = true;
    }

    /* access modifiers changed from: 0000 */
    public void removeLoadingView() {
        this.isBottomLoading = false;
    }

    /* access modifiers changed from: 0000 */
    public void addMessages(List<MMMessageItem> list, int i) {
        if (!CollectionsUtil.isCollectionEmpty(list)) {
            if (list.size() > 1 && ((MMMessageItem) list.get(0)).messageTime > ((MMMessageItem) list.get(list.size() - 1)).messageTime) {
                Collections.reverse(list);
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

    private int getLastTimeItemIndex() {
        if (this.mDisplayDatas.size() == 0) {
            return -1;
        }
        for (int itemCount = getItemCount() - 1; itemCount >= 0; itemCount--) {
            if (((MMMessageItem) this.mDisplayDatas.get(itemCount)).messageType == 19) {
                return itemCount;
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public void updateMessage(MMMessageItem mMMessageItem, boolean z) {
        if (mMMessageItem != null) {
            if (this.mThreadItem == null || !TextUtils.equals(mMMessageItem.messageId, this.mThreadItem.messageId)) {
                int findItemInData = findItemInData(mMMessageItem.messageId);
                if (findItemInData >= 0) {
                    this.mDatas.set(findItemInData, mMMessageItem);
                } else if (!z) {
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= this.mDatas.size()) {
                            break;
                        }
                        MMMessageItem mMMessageItem2 = (MMMessageItem) this.mDatas.get(i2);
                        if (mMMessageItem2.messageTime > mMMessageItem.messageTime || (mMMessageItem2.messageTime == mMMessageItem.messageTime && mMMessageItem2.serverSideTime > mMMessageItem.serverSideTime)) {
                            i = i2;
                        } else {
                            i2++;
                        }
                    }
                    i = i2;
                    if (i < 0) {
                        this.mDatas.add(mMMessageItem);
                    } else {
                        this.mDatas.add(i, mMMessageItem);
                    }
                } else {
                    return;
                }
                return;
            }
            this.mThreadItem = mMMessageItem;
            updateThreadForCommentShow();
        }
    }

    public int getItemViewType(int i) {
        int i2;
        MMMessageItem item = getItem(i);
        if (item != null) {
            i2 = item.messageType;
            if (item.isThread) {
                return i2 + 10000;
            }
        } else {
            i2 = 0;
        }
        return i2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        AbsMessageView absMessageView;
        if (i >= 10000) {
            absMessageView = MMMessageItem.createView(this.mContext, i - 10000);
        } else {
            AbsMessageView createCommentView = MMMessageItem.createCommentView(this.mContext, i);
            createCommentView.reSizeTitleBarForReplyPage();
            absMessageView = createCommentView;
        }
        if (i == 53) {
            absMessageView.setOnClickListener(this.mLoadingMoreClickListener);
        }
        C36334 r4 = new ViewHolder(absMessageView == 0 ? new View(this.mContext) : absMessageView) {
        };
        if (absMessageView != 0) {
            absMessageView.setOnShowContextMenuListener(this.mCallBack);
            absMessageView.setOnClickMessageListener(this.mCallBack);
            absMessageView.setOnClickStatusImageListener(this.mCallBack);
            absMessageView.setOnClickAvatarListener(this.mCallBack);
            absMessageView.setOnClickCancelListenter(this.mCallBack);
            absMessageView.setOnLongClickAvatarListener(this.mCallBack);
            absMessageView.setOnClickAddonListener(this.mCallBack);
            absMessageView.setOnClickMeetingNOListener(this.mCallBack);
            absMessageView.setmOnClickActionListener(this.mCallBack);
            absMessageView.setmOnClickActionMoreListener(this.mCallBack);
            absMessageView.setOnClickLinkPreviewListener(this.mCallBack);
            absMessageView.setmOnClickGiphyBtnListener(this.mCallBack);
            absMessageView.setmOnClickTemplateActionMoreListener(this.mCallBack);
            absMessageView.setmOnClickTemplateListener(this.mCallBack);
            absMessageView.setOnClickReactionLabelListener(this.mCallBack);
        }
        return r4;
    }

    public MMMessageItem getItem(int i) {
        if (i < 0 || i >= getItemCount()) {
            return null;
        }
        return (MMMessageItem) this.mDisplayDatas.get(i);
    }

    public long getItemId(int i) {
        if (i < 0 || i > this.mDisplayDatas.size() - 1) {
            return -1;
        }
        MMMessageItem mMMessageItem = (MMMessageItem) this.mDisplayDatas.get(i);
        if (mMMessageItem == null || mMMessageItem.messageId == null) {
            return super.getItemId(i);
        }
        return (long) mMMessageItem.messageId.hashCode();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MMMessageItem item = getItem(i);
        if (item != null) {
            item.bindViewHolder(viewHolder);
            ThreadsUICallBack threadsUICallBack = this.mCallBack;
            if (threadsUICallBack != null) {
                threadsUICallBack.onMessageShowed(item);
            }
        }
    }

    public int getItemCount() {
        return this.mDisplayDatas.size();
    }

    public boolean hasUnreadMark() {
        return this.mUnreadNewMarkMsg != null;
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
