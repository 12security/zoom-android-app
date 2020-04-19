package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.State;
import com.google.android.gms.common.util.CollectionUtils;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessage;
import com.zipow.videobox.sip.server.IPBXMessageAPI;
import com.zipow.videobox.sip.server.IPBXMessageSession;
import com.zipow.videobox.sip.server.ISIPCallAPI;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnClickLinkPreviewListener;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnClickMeetingNOListener;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnClickMessageListener;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnClickStatusImageListener;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnShowContextMenuListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.UIUtil;

public class PbxSmsRecyleView extends RecyclerView {
    private static final int PAGE_COUNT = 20;
    private static final int PAGE_WEB_SYNC_COUNT = 50;
    @NonNull
    private static final String TAG = "PbxSmsRecyleView";
    /* access modifiers changed from: private */
    @NonNull
    public PbxSmsAdapter mAdapter = new PbxSmsAdapter(getContext());
    @NonNull
    private MyHandler mHandler = new MyHandler(this);
    /* access modifiers changed from: private */
    @NonNull
    public LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext()) {
        private boolean isLayoutReady;

        public LayoutParams generateDefaultLayoutParams() {
            return new LayoutParams(-1, -2);
        }

        public void onLayoutCompleted(State state) {
            super.onLayoutCompleted(state);
            if (!this.isLayoutReady) {
                this.isLayoutReady = true;
                if (PbxSmsRecyleView.this.mUICallBack != null) {
                    PbxSmsRecyleView.this.mUICallBack.onLayoutCompleted();
                }
            }
        }
    };
    @Nullable
    private String mSessionid;
    @Nullable
    private String mSyncOldReqId;
    /* access modifiers changed from: private */
    @Nullable
    public PbxSmsUICallBack mUICallBack;

    private static class MyHandler extends Handler {
        static final int MSG_SCROLL_BOTTOM = 1;
        private WeakReference<PbxSmsRecyleView> mView;

        MyHandler(@NonNull PbxSmsRecyleView pbxSmsRecyleView) {
            this.mView = new WeakReference<>(pbxSmsRecyleView);
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
            PbxSmsRecyleView pbxSmsRecyleView = (PbxSmsRecyleView) this.mView.get();
            if (pbxSmsRecyleView != null) {
                int itemCount = pbxSmsRecyleView.mAdapter.getItemCount() - 1;
                if (z) {
                    pbxSmsRecyleView.scrollToPosition(itemCount);
                } else if (itemCount - pbxSmsRecyleView.mLinearLayoutManager.findLastVisibleItemPosition() < 5) {
                    pbxSmsRecyleView.scrollToPosition(itemCount);
                }
            }
        }
    }

    public interface PbxSmsUICallBack extends OnShowContextMenuListener, OnClickMessageListener, OnClickStatusImageListener, OnClickMeetingNOListener, OnClickLinkPreviewListener {
        void onLayoutCompleted();

        void onMessageShowed(@NonNull PBXMessageItem pBXMessageItem);
    }

    static class SmsItemComparator implements Comparator<PBXMessageItem> {
        SmsItemComparator() {
        }

        public int compare(PBXMessageItem pBXMessageItem, PBXMessageItem pBXMessageItem2) {
            if (pBXMessageItem == null && pBXMessageItem2 == null) {
                return 0;
            }
            if (pBXMessageItem == null) {
                return -1;
            }
            if (pBXMessageItem2 == null) {
                return 1;
            }
            return Long.compare(pBXMessageItem.getTimestamp(), pBXMessageItem2.getTimestamp());
        }
    }

    public PbxSmsRecyleView(@NonNull Context context) {
        super(context);
        init();
    }

    public PbxSmsRecyleView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PbxSmsRecyleView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setAdapter(this.mAdapter);
        this.mAdapter.setSessionId(this.mSessionid);
        setLayoutManager(this.mLinearLayoutManager);
    }

    public void loadAllMsgs(boolean z) {
        if (!TextUtils.isEmpty(this.mSessionid)) {
            if (!z || this.mAdapter.isEmpty()) {
                ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
                if (sipCallAPI != null) {
                    IPBXMessageAPI messageAPI = sipCallAPI.getMessageAPI();
                    if (messageAPI != null) {
                        IPBXMessageSession sessionById = messageAPI.getSessionById(this.mSessionid);
                        if (sessionById == null) {
                            List<IPBXMessage> allMessagesInLocalSession = messageAPI.getAllMessagesInLocalSession(this.mSessionid);
                            if (allMessagesInLocalSession != null) {
                                ArrayList arrayList = new ArrayList();
                                for (IPBXMessage initFromPBXMessage : allMessagesInLocalSession) {
                                    arrayList.add(PBXMessageItem.initFromPBXMessage(initFromPBXMessage));
                                }
                                this.mAdapter.addMessages(arrayList, false);
                                this.mAdapter.notifyDataSetChanged();
                                scrollToBottom(true);
                            }
                            return;
                        }
                        String lastViewedMessageId = sessionById.getLastViewedMessageId();
                        this.mAdapter.clearData();
                        this.mSyncOldReqId = null;
                        ArrayList arrayList2 = new ArrayList();
                        for (int i = 0; i < sessionById.getCountOfMessage(); i++) {
                            IPBXMessage messageByIndex = sessionById.getMessageByIndex(i);
                            if (messageByIndex != null) {
                                arrayList2.add(PBXMessageItem.initFromPBXMessage(messageByIndex));
                            }
                        }
                        sessionById.requestUpdateAllMessageAsRead();
                        Collections.sort(arrayList2, new SmsItemComparator());
                        this.mAdapter.addMessages(arrayList2, false);
                        this.mAdapter.notifyDataSetChanged();
                        if (!scrollToMessage(lastViewedMessageId)) {
                            scrollToBottom(true);
                        }
                        if (arrayList2.isEmpty()) {
                            this.mSyncOldReqId = sessionById.requestSyncOldMessages(50);
                        }
                    }
                }
            }
        }
    }

    public boolean isLoading() {
        return !TextUtils.isEmpty(this.mSyncOldReqId);
    }

    public boolean hasMoreHistory() {
        if (TextUtils.isEmpty(this.mSessionid)) {
            return false;
        }
        IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionid);
        if (sessionById != null && sessionById.hasMoreOldMessagesToSync()) {
            return true;
        }
        return false;
    }

    public void addMsgsFromSyncOld(@Nullable List<String> list) {
        if (!CollectionsUtil.isCollectionEmpty(list) && !TextUtils.isEmpty(this.mSessionid)) {
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionid);
            if (sessionById != null) {
                PBXMessageItem firstVisibleItem = getFirstVisibleItem();
                ArrayList arrayList = new ArrayList();
                for (String messageByID : list) {
                    IPBXMessage messageByID2 = sessionById.getMessageByID(messageByID);
                    if (messageByID2 != null) {
                        arrayList.add(PBXMessageItem.initFromPBXMessage(messageByID2));
                    }
                }
                Collections.sort(arrayList, new SmsItemComparator());
                this.mAdapter.addMessages(arrayList, false);
                this.mAdapter.notifyDataSetChanged();
                if (firstVisibleItem != null) {
                    scrollToMessage(firstVisibleItem.getId());
                }
            }
        }
    }

    public void loadMoreMsgs() {
        if (!TextUtils.isEmpty(this.mSessionid) && hasMoreHistory()) {
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionid);
            if (sessionById != null) {
                this.mSyncOldReqId = sessionById.requestSyncOldMessages(50);
            }
        }
    }

    public void addNewMessage(String str) {
        IPBXMessage iPBXMessage;
        if (!TextUtils.isEmpty(str) && this.mSessionid != null) {
            ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
            if (sipCallAPI != null) {
                IPBXMessageAPI messageAPI = sipCallAPI.getMessageAPI();
                if (messageAPI != null) {
                    IPBXMessageSession sessionById = messageAPI.getSessionById(this.mSessionid);
                    if (sessionById == null) {
                        iPBXMessage = messageAPI.getMessageByIdInLocalSession(this.mSessionid, str);
                    } else {
                        iPBXMessage = sessionById.getMessageByID(str);
                    }
                    if (iPBXMessage != null) {
                        this.mAdapter.addOrUpdateMsg(PBXMessageItem.initFromPBXMessage(iPBXMessage));
                        this.mAdapter.notifyDataSetChanged();
                        scrollToBottom(false);
                    }
                }
            }
        }
    }

    public void addSystemMessage(String str) {
        PBXMessageItem pBXMessageItem;
        if (!this.mAdapter.isEmpty()) {
            PbxSmsAdapter pbxSmsAdapter = this.mAdapter;
            pBXMessageItem = pbxSmsAdapter.getItem(pbxSmsAdapter.getItemCount() - 1);
        } else {
            pBXMessageItem = null;
        }
        this.mAdapter.addOrUpdateMsg(PBXMessageItem.createSystemMessage(str, pBXMessageItem == null ? System.currentTimeMillis() : pBXMessageItem.getTimestamp() + 1));
        this.mAdapter.notifyDataSetChanged();
        scrollToBottom(false);
    }

    @Nullable
    private PBXMessageItem getFirstVisibleItem() {
        PBXMessageItem pBXMessageItem;
        int findFirstCompletelyVisibleItemPosition = this.mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (findFirstCompletelyVisibleItemPosition == -1) {
            findFirstCompletelyVisibleItemPosition = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        }
        if (findFirstCompletelyVisibleItemPosition == -1) {
            return null;
        }
        while (true) {
            if (findFirstCompletelyVisibleItemPosition >= this.mAdapter.getItemCount()) {
                pBXMessageItem = null;
                break;
            }
            pBXMessageItem = this.mAdapter.getItem(findFirstCompletelyVisibleItemPosition);
            if (pBXMessageItem != null && pBXMessageItem.getMessageType() != 1 && pBXMessageItem.getMessageType() != 2) {
                break;
            }
            findFirstCompletelyVisibleItemPosition++;
        }
        return pBXMessageItem;
    }

    @Nullable
    private PBXMessageItem getLastVisibleItem() {
        int findLastCompletelyVisibleItemPosition = this.mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        if (findLastCompletelyVisibleItemPosition == -1) {
            findLastCompletelyVisibleItemPosition = this.mLinearLayoutManager.findLastVisibleItemPosition();
        }
        PBXMessageItem pBXMessageItem = null;
        if (findLastCompletelyVisibleItemPosition == -1) {
            return null;
        }
        while (pBXMessageItem == null && findLastCompletelyVisibleItemPosition >= 0) {
            PBXMessageItem item = this.mAdapter.getItem(findLastCompletelyVisibleItemPosition);
            if (!(item == null || item.getMessageType() == 1 || item.getMessageType() == 2)) {
                pBXMessageItem = item;
            }
            findLastCompletelyVisibleItemPosition--;
        }
        return pBXMessageItem;
    }

    public void scrollToBottom(boolean z) {
        this.mHandler.obtainMessage(1, z ? 1 : 0, 0).sendToTarget();
    }

    public boolean scrollToMessage(String str) {
        int findIndexInAdapter = this.mAdapter.findIndexInAdapter(str);
        if (findIndexInAdapter == -1) {
            return false;
        }
        this.mHandler.removeMessages(1);
        this.mLinearLayoutManager.scrollToPositionWithOffset(findIndexInAdapter, UIUtil.dip2px(getContext(), 100.0f));
        return true;
    }

    public void OnSyncedNewMessages(int i, String str, List<String> list, List<String> list2, List<String> list3) {
        if (i == 0) {
            loadAllMsgs(false);
        }
    }

    public void OnRequestDoneForDeleteMessage(int i, String str, String str2, List<String> list) {
        if (list != null && this.mAdapter.deleteMsgs(list)) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void updateMessage(@NonNull String str) {
        if (!TextUtils.isEmpty(this.mSessionid)) {
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionid);
            if (sessionById != null) {
                IPBXMessage messageByID = sessionById.getMessageByID(str);
                if (messageByID != null && this.mAdapter.addOrUpdateMsg(PBXMessageItem.initFromPBXMessage(messageByID), false)) {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void OnRequestDoneForSyncOldMessages(int i, String str, String str2, List<String> list) {
        if (TextUtils.equals(str, this.mSyncOldReqId)) {
            this.mSyncOldReqId = null;
            if (i == 0) {
                addMsgsFromSyncOld(list);
            }
        }
    }

    public void OnRequestDoneForUpdateMessageReadStatus(int i, String str, String str2, List<String> list) {
        if (i == 0 && !CollectionUtils.isEmpty(list) && !TextUtils.isEmpty(this.mSessionid)) {
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(this.mSessionid);
            if (sessionById != null) {
                boolean z = false;
                for (String str3 : list) {
                    PBXMessageItem itemById = this.mAdapter.getItemById(str3);
                    if (itemById != null) {
                        IPBXMessage messageByID = sessionById.getMessageByID(str3);
                        if (messageByID != null) {
                            z = true;
                            itemById.setReadStatus(messageByID.getReadStatus());
                        }
                    }
                }
                if (z) {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void setSessionId(String str) {
        this.mSessionid = str;
        this.mAdapter.setSessionId(this.mSessionid);
    }

    public void setUICallBack(PbxSmsUICallBack pbxSmsUICallBack) {
        this.mAdapter.setUICallBack(pbxSmsUICallBack);
        this.mUICallBack = pbxSmsUICallBack;
    }
}
