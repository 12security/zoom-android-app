package com.zipow.videobox.view.sip;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.zipow.videobox.dialog.ConfirmAlertDialog;
import com.zipow.videobox.dialog.ConfirmAlertDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRemoteMemberProto;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.PBXJoinMeetingRequest;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineCallItemBean;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.BigRoundListDialog;
import com.zipow.videobox.view.BigRoundListDialog.DialogCallback;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IZMListItemView.IActionClickListener;
import com.zipow.videobox.view.MergeCallListItem;
import com.zipow.videobox.view.MergeSelectCallListItem;
import com.zipow.videobox.view.ZMListAdapter;
import com.zipow.videobox.view.sip.AbstractSharedLineItem.OnItemClickListener;
import java.util.List;
import java.util.Stack;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.PinnedSectionRecyclerView;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXSharedLineRecyclerView extends PinnedSectionRecyclerView implements IActionClickListener {
    private static final String TAG = "PhonePBXSharedLineRecyclerView";
    /* access modifiers changed from: private */
    public SharedLineAdapter mAdapter;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private SimpleISIPLineMgrEventSinkListener mLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            PhonePBXSharedLineRecyclerView.this.onLineRegistered(str);
        }

        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onSelfInfoUpdated();
        }

        public void OnNewSharedUserAdded(String str) {
            super.OnNewSharedUserAdded(str);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLineUserUpdated(0, str);
        }

        public void OnSharedUserDeleted(String str) {
            super.OnSharedUserDeleted(str);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLineUserUpdated(2, str);
        }

        public void OnSharedUserUpdated(String str) {
            super.OnSharedUserUpdated(str);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLineUserUpdated(1, str);
        }

        public void OnSharedLineUpdated(String str, boolean z, int i) {
            super.OnSharedLineUpdated(str, z, i);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLineUpdated(str);
        }

        public void OnLineCallItemCreated(String str, int i) {
            super.OnLineCallItemCreated(str, i);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLineCallItemUpdated(0, str);
            PhonePBXSharedLineRecyclerView.this.startUpdateCallDuration();
        }

        public void OnLineCallItemTerminate(String str) {
            super.OnLineCallItemTerminate(str);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLineCallItemUpdated(2, str);
            PhonePBXSharedLineRecyclerView.this.startUpdateCallDuration();
        }

        public void OnLineCallItemUpdate(String str, int i) {
            super.OnLineCallItemUpdate(str, i);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLineCallItemUpdated(1, str);
            PhonePBXSharedLineRecyclerView.this.startUpdateCallDuration();
        }

        public void OnLineCallItemMerged(String str, String str2) {
            super.OnLineCallItemMerged(str, str2);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLineCallItemMerged(str, str2);
        }
    };
    private BigRoundListDialog mOnActionListDialog;
    private SimplePTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, long j) {
            if (i == 9) {
                PhonePBXSharedLineRecyclerView.this.mAdapter.notifyBuddyInfoChanged(PhonePBXSharedLineRecyclerView.this.mSelfJid);
            }
        }
    };
    /* access modifiers changed from: private */
    public IPhonePBXLinesParentFragment mParentFragment;
    private Runnable mRefreshRunnable = new Runnable() {
        public void run() {
            PhonePBXSharedLineRecyclerView.this.updateVisibleItems();
        }
    };
    @NonNull
    private SimpleSIPCallEventListener mSIPCallListener = new SimpleSIPCallEventListener() {
        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            super.OnRequestDoneForQueryPBXUserInfo(z);
            PhonePBXSharedLineRecyclerView.this.mAdapter.initData();
        }

        public void OnCallStatusUpdate(String str, int i) {
            super.OnCallStatusUpdate(str, i);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLocalCallStateUpdate(str);
        }

        public void OnCallTerminate(String str, int i) {
            super.OnCallTerminate(str, i);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLocalCallStateUpdate(CmmSIPCallManager.getInstance().getCurrentCallID());
        }

        public void OnReceivedJoinMeetingRequest(String str, long j, String str2) {
            super.OnReceivedJoinMeetingRequest(str, j, str2);
            PhonePBXSharedLineRecyclerView.this.mAdapter.onLocalCallStateUpdate(str);
        }

        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            super.OnPBXFeatureOptionsChanged(list);
            PhonePBXSharedLineRecyclerView.this.onPBXFeatureOptionsChanged(list);
        }
    };
    /* access modifiers changed from: private */
    public String mSelfJid;
    /* access modifiers changed from: private */
    public Runnable mUpdateCallDurationRunnable = new Runnable() {
        public void run() {
            if (PhonePBXSharedLineRecyclerView.this.updateCallDuration()) {
                PhonePBXSharedLineRecyclerView.this.mHandler.postDelayed(this, 1000);
            } else {
                PhonePBXSharedLineRecyclerView.this.mHandler.removeCallbacks(this);
            }
        }
    };
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void On_MyPresenceChanged(int i, int i2) {
            super.On_MyPresenceChanged(i, i2);
            PhonePBXSharedLineRecyclerView.this.mAdapter.notifyBuddyInfoChanged(PhonePBXSharedLineRecyclerView.this.mSelfJid);
        }

        public void Indicate_BuddyPresenceChanged(String str) {
            super.Indicate_BuddyPresenceChanged(str);
            PhonePBXSharedLineRecyclerView.this.mAdapter.notifyBuddyInfoChanged(str);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            PhonePBXSharedLineRecyclerView.this.refresh();
        }

        public void onConnectReturn(int i) {
            super.onConnectReturn(i);
            PhonePBXSharedLineRecyclerView.this.refresh();
        }

        public void Indicate_OnlineBuddies(List<String> list) {
            super.Indicate_OnlineBuddies(list);
            PhonePBXSharedLineRecyclerView.this.mAdapter.notifyBuddyInfoChanged(list);
        }
    };

    public PhonePBXSharedLineRecyclerView(Context context) {
        super(context);
        initView();
    }

    public PhonePBXSharedLineRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public void onAction(String str, int i) {
        switch (i) {
            case 1:
                mergeCall(str);
                break;
            case 2:
                CmmSIPCallManager.getInstance().hangupCall(str);
                break;
            case 3:
                resumeCall(str);
                break;
        }
        checkDialog();
    }

    private void initView() {
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineMgrEventSinkListener);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        PTUI.getInstance().addPTUIListener(this.mPTUIListener);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                this.mSelfJid = myself.getJid();
            }
        }
        this.mAdapter = new SharedLineAdapter(getContext(), new OnItemClickListener() {
            public void onClick(View view, int i) {
                int id = view.getId();
                AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) PhonePBXSharedLineRecyclerView.this.mAdapter.getItem(i);
                if (abstractSharedLineItem instanceof SharedLineUserItem) {
                    if (id == C4558R.C4560id.iv_fast_dial) {
                        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
                        if (instance.checkNetwork(PhonePBXSharedLineRecyclerView.this.getContext()) && instance.isPBXActive()) {
                            PhonePBXSharedLineRecyclerView.this.mParentFragment.onPickSipResult(((SharedLineUserItem) abstractSharedLineItem).getExtension(), null);
                        }
                    } else {
                        PhonePBXSharedLineRecyclerView.this.mParentFragment.onSelectLastAccessibilityId(String.valueOf(i));
                    }
                } else if (abstractSharedLineItem instanceof SharedLineCallItem) {
                    if (id == C4558R.C4560id.layout_line) {
                        PhonePBXSharedLineRecyclerView.this.mParentFragment.onSelectLastAccessibilityId(String.valueOf(i));
                    } else if (id == C4558R.C4560id.btn_hang_up) {
                        CmmSIPCallManager.getInstance().declineCallWithBusy(((SharedLineCallItem) abstractSharedLineItem).getLocalCallId());
                    } else if (id == C4558R.C4560id.btn_accept) {
                        PhonePBXSharedLineRecyclerView.this.mParentFragment.onAcceptCallResult(((SharedLineCallItem) abstractSharedLineItem).getLocalCallId());
                    } else if (id == C4558R.C4560id.iv_more_options) {
                        SharedLineCallItem sharedLineCallItem = (SharedLineCallItem) abstractSharedLineItem;
                        CmmSIPLineCallItemBean lineCallItemBean = sharedLineCallItem.getLineCallItemBean();
                        if (lineCallItemBean != null) {
                            CmmSIPCallItem localCallItem = sharedLineCallItem.getLocalCallItem();
                            if (localCallItem == null || !localCallItem.isInConference()) {
                                PhonePBXSharedLineRecyclerView.this.showCallerIdDialog(lineCallItemBean);
                            } else {
                                PhonePBXSharedLineRecyclerView.this.showMergedListDialog(localCallItem.getCallID());
                            }
                        }
                    } else if (id == C4558R.C4560id.iv_call_status) {
                        SharedLineCallItem sharedLineCallItem2 = (SharedLineCallItem) abstractSharedLineItem;
                        CmmSIPLineCallItemBean lineCallItemBean2 = sharedLineCallItem2.getLineCallItemBean();
                        if (lineCallItemBean2 != null) {
                            int status = lineCallItemBean2.getStatus();
                            if (lineCallItemBean2.isItBelongToMe()) {
                                String relatedLocalCallID = lineCallItemBean2.getRelatedLocalCallID();
                                if (CmmSIPCallManager.getInstance().isInJoinMeeingRequest(relatedLocalCallID)) {
                                    PhonePBXSharedLineRecyclerView.this.onActionJoinMeeting(relatedLocalCallID);
                                } else if (status == 2) {
                                    if (PhonePBXSharedLineRecyclerView.this.resumeCall(relatedLocalCallID)) {
                                        SipInCallActivity.returnToSip(PhonePBXSharedLineRecyclerView.this.getContext());
                                    }
                                } else if (status == 3) {
                                    if (CmmSIPCallManager.isPhoneCallOffHook()) {
                                        CmmSIPCallManager.getInstance().showErrorDialogImmediately(PhonePBXSharedLineRecyclerView.this.getContext().getString(C4558R.string.zm_title_error), PhonePBXSharedLineRecyclerView.this.getContext().getString(C4558R.string.zm_sip_can_not_merge_call_on_phone_call_111899), 0);
                                        return;
                                    }
                                    PhonePBXSharedLineRecyclerView.this.showMergeSelectDialog();
                                }
                            } else if (status == 2) {
                                PhonePBXSharedLineRecyclerView.this.mParentFragment.onPickupCallResult(sharedLineCallItem2.getLineCallId());
                            }
                        }
                    }
                }
            }
        });
        setAdapter(this.mAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i == 0) {
                    PhonePBXSharedLineRecyclerView.this.startUpdateCallDuration();
                } else {
                    PhonePBXSharedLineRecyclerView.this.mHandler.removeCallbacks(PhonePBXSharedLineRecyclerView.this.mUpdateCallDurationRunnable);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onLineRegistered(String str) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            if (findFirstVisibleItemPosition >= 0 && findLastVisibleItemPosition >= 0 && findLastVisibleItemPosition >= findFirstVisibleItemPosition) {
                while (findFirstVisibleItemPosition <= findLastVisibleItemPosition) {
                    AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mAdapter.getItem(findFirstVisibleItemPosition);
                    if ((abstractSharedLineItem instanceof SharedLineUserItem) && ((SharedLineUserItem) abstractSharedLineItem).containsLineItem(str)) {
                        this.mAdapter.notifyItemChanged(findFirstVisibleItemPosition);
                    }
                    findFirstVisibleItemPosition++;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean updateCallDuration() {
        LayoutManager layoutManager = getLayoutManager();
        boolean z = false;
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return false;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        if (findFirstVisibleItemPosition < 0 || findLastVisibleItemPosition < 0 || findLastVisibleItemPosition < findFirstVisibleItemPosition) {
            return false;
        }
        while (findFirstVisibleItemPosition <= findLastVisibleItemPosition) {
            AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mAdapter.getItem(findFirstVisibleItemPosition);
            if (abstractSharedLineItem instanceof SharedLineCallItem) {
                SharedLineCallItem sharedLineCallItem = (SharedLineCallItem) abstractSharedLineItem;
                int lineCallItemStatus = sharedLineCallItem.getLineCallItemStatus();
                if (lineCallItemStatus < 0 || lineCallItemStatus == 0) {
                    this.mAdapter.onLineCallItemUpdated(2, sharedLineCallItem.getLineCallId());
                } else {
                    this.mAdapter.updateCallDuration(findFirstVisibleItemPosition);
                    z = true;
                }
            }
            findFirstVisibleItemPosition++;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void updateVisibleItems() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            if (findFirstVisibleItemPosition >= 0 && findLastVisibleItemPosition >= 0 && findLastVisibleItemPosition >= findFirstVisibleItemPosition) {
                this.mAdapter.notifyItemRangeChanged(findFirstVisibleItemPosition, (findLastVisibleItemPosition - findFirstVisibleItemPosition) + 1);
            }
        }
    }

    /* access modifiers changed from: private */
    public void showMergedListDialog(String str) {
        Context context = getContext();
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        final ZMListAdapter zMListAdapter = new ZMListAdapter(context, this);
        zMListAdapter.setShowSelect(false);
        if (!TextUtils.isEmpty(str)) {
            CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(str);
            if (callItemByCallID != null) {
                MergeCallListItem mergeCallListItem = new MergeCallListItem(str);
                mergeCallListItem.init(context.getApplicationContext());
                zMListAdapter.addItem(mergeCallListItem);
                List remoteMergerMembers = callItemByCallID.getRemoteMergerMembers();
                if (remoteMergerMembers != null && !remoteMergerMembers.isEmpty()) {
                    for (int i = 0; i < remoteMergerMembers.size(); i++) {
                        CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto = (CmmSIPCallRemoteMemberProto) remoteMergerMembers.get(i);
                        zMListAdapter.addItem(new MergeCallListItem(instance.getRemoteMemberDisplayName(cmmSIPCallRemoteMemberProto), cmmSIPCallRemoteMemberProto.getDisplayNumber()));
                    }
                }
                int conferenceParticipantsCount = callItemByCallID.getConferenceParticipantsCount();
                for (int i2 = 0; i2 < conferenceParticipantsCount; i2++) {
                    String conferenceParticipantCallItemByIndex = callItemByCallID.getConferenceParticipantCallItemByIndex(i2);
                    MergeCallListItem mergeCallListItem2 = new MergeCallListItem(conferenceParticipantCallItemByIndex);
                    mergeCallListItem2.init(getContext());
                    zMListAdapter.addItem(mergeCallListItem2);
                    CmmSIPCallItem callItemByCallID2 = instance.getCallItemByCallID(conferenceParticipantCallItemByIndex);
                    if (callItemByCallID2 != null) {
                        List remoteMergerMembers2 = callItemByCallID2.getRemoteMergerMembers();
                        if (remoteMergerMembers2 != null && !remoteMergerMembers2.isEmpty()) {
                            for (int i3 = 0; i3 < remoteMergerMembers2.size(); i3++) {
                                CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto2 = (CmmSIPCallRemoteMemberProto) remoteMergerMembers2.get(i3);
                                zMListAdapter.addItem(new MergeCallListItem(instance.getRemoteMemberDisplayName(cmmSIPCallRemoteMemberProto2), cmmSIPCallRemoteMemberProto2.getDisplayNumber()));
                            }
                        }
                    }
                }
                zMListAdapter.addItem(new MergeCallListItem(PTApp.getInstance().getMyName(), CmmSIPCallManager.getInstance().getCallerId(getContext(), callItemByCallID)));
                showMoreActionDialog(context, context.getString(C4558R.string.zm_sip_call_item_callers_title_85311), zMListAdapter, new DialogCallback() {
                    public void onCancel() {
                    }

                    public void onClose() {
                    }

                    public void onItemSelected(int i) {
                        String id = ((MergeCallListItem) zMListAdapter.getItem(i)).getId();
                        if (!TextUtils.isEmpty(id)) {
                            CmmSIPCallManager.getInstance().hangupCall(id);
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void onActionJoinMeeting(String str) {
        PBXJoinMeetingRequest joinMeetingRequest = CmmSIPCallManager.getInstance().getJoinMeetingRequest(str);
        if (joinMeetingRequest != null) {
            SipInCallActivity.returnToSipForMeetingRequest(getContext(), joinMeetingRequest);
        }
    }

    /* access modifiers changed from: private */
    public void showMergeSelectDialog() {
        Context context = getContext();
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        final ZMListAdapter zMListAdapter = new ZMListAdapter(context, this);
        zMListAdapter.setShowSelect(false);
        Stack sipCallIds = instance.getSipCallIds();
        int currentIndexInCallCache = instance.getCurrentIndexInCallCache();
        if (sipCallIds != null) {
            for (int size = sipCallIds.size() - 1; size >= 0; size--) {
                if (currentIndexInCallCache != size) {
                    String str = (String) sipCallIds.get(size);
                    if (!instance.isInJoinMeeingRequest(str)) {
                        MergeSelectCallListItem mergeSelectCallListItem = new MergeSelectCallListItem(str);
                        mergeSelectCallListItem.init(context.getApplicationContext());
                        zMListAdapter.addItem(mergeSelectCallListItem);
                    }
                }
            }
        }
        showMoreActionDialog(context, context.getString(C4558R.string.zm_sip_phone_calls_on_hold_31368, new Object[]{Integer.valueOf(zMListAdapter.getCount())}), zMListAdapter, new DialogCallback() {
            public void onCancel() {
            }

            public void onClose() {
            }

            public void onItemSelected(int i) {
                PhonePBXSharedLineRecyclerView.this.mergeCall(((MergeSelectCallListItem) zMListAdapter.getItem(i)).getId());
            }
        });
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009a, code lost:
        if (p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0) == false) goto L_0x009e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void showCallerIdDialog(com.zipow.videobox.sip.server.CmmSIPLineCallItemBean r11) {
        /*
            r10 = this;
            if (r11 != 0) goto L_0x0003
            return
        L_0x0003:
            com.zipow.videobox.sip.server.CmmSIPCallManager r0 = com.zipow.videobox.sip.server.CmmSIPCallManager.getInstance()
            if (r0 != 0) goto L_0x000a
            return
        L_0x000a:
            android.content.Context r1 = r10.getContext()
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_sip_call_item_callers_title_85311
            java.lang.String r2 = r1.getString(r2)
            com.zipow.videobox.view.ZMListAdapter r3 = new com.zipow.videobox.view.ZMListAdapter
            r4 = 0
            r3.<init>(r1, r4)
            r5 = 0
            r3.setShowSelect(r5)
            java.lang.String r6 = r11.getPeerDisplayName()
            java.lang.String r7 = r11.getPeerNumber()
            com.zipow.videobox.view.IMAddrBookItem r7 = r10.getAddrItemByNumber(r7)
            if (r7 == 0) goto L_0x0037
            java.lang.String r7 = r7.getScreenName()
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r7)
            if (r8 != 0) goto L_0x0037
            r6 = r7
        L_0x0037:
            com.zipow.videobox.view.CallItemCallerIdListItem r7 = new com.zipow.videobox.view.CallItemCallerIdListItem
            r7.<init>()
            java.lang.String r8 = r11.getPeerDisplayNumber()
            r7.init(r1, r6, r8)
            r3.addItem(r7)
            java.lang.String r6 = r11.getRelatedLocalCallID()
            boolean r7 = android.text.TextUtils.isEmpty(r6)
            if (r7 != 0) goto L_0x0084
            com.zipow.videobox.sip.server.CmmSIPCallItem r6 = r0.getCallItemByCallID(r6)
            if (r6 == 0) goto L_0x0084
            java.util.List r6 = r6.getRemoteMergerMembers()
            if (r6 == 0) goto L_0x0084
            boolean r7 = r6.isEmpty()
            if (r7 != 0) goto L_0x0084
        L_0x0062:
            int r7 = r6.size()
            if (r5 >= r7) goto L_0x0084
            java.lang.Object r7 = r6.get(r5)
            com.zipow.videobox.ptapp.PTAppProtos$CmmSIPCallRemoteMemberProto r7 = (com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRemoteMemberProto) r7
            com.zipow.videobox.view.CallItemCallerIdListItem r8 = new com.zipow.videobox.view.CallItemCallerIdListItem
            r8.<init>()
            java.lang.String r9 = r0.getRemoteMemberDisplayName(r7)
            java.lang.String r7 = r7.getDisplayNumber()
            r8.init(r1, r9, r7)
            r3.addItem(r8)
            int r5 = r5 + 1
            goto L_0x0062
        L_0x0084:
            java.lang.String r0 = r11.getOwnerNumber()
            java.lang.String r5 = r11.getOwnerDisplayName()
            com.zipow.videobox.view.IMAddrBookItem r0 = r10.getAddrItemByNumber(r0)
            if (r0 == 0) goto L_0x009d
            java.lang.String r0 = r0.getScreenName()
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
            if (r6 != 0) goto L_0x009d
            goto L_0x009e
        L_0x009d:
            r0 = r5
        L_0x009e:
            com.zipow.videobox.view.CallItemCallerIdListItem r5 = new com.zipow.videobox.view.CallItemCallerIdListItem
            r5.<init>()
            java.lang.String r11 = r11.getOwnerDisplayNumber()
            r5.init(r1, r0, r11)
            r3.addItem(r5)
            r10.showMoreActionDialog(r1, r2, r3, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.sip.PhonePBXSharedLineRecyclerView.showCallerIdDialog(com.zipow.videobox.sip.server.CmmSIPLineCallItemBean):void");
    }

    private void showMoreActionDialog(Context context, String str, ZMListAdapter zMListAdapter, DialogCallback dialogCallback) {
        if (!(context instanceof Activity) || DialogUtils.isCanShowDialog((ZMActivity) context)) {
            BigRoundListDialog bigRoundListDialog = this.mOnActionListDialog;
            if (bigRoundListDialog == null || !bigRoundListDialog.isShowing()) {
                this.mOnActionListDialog = new BigRoundListDialog(context);
                this.mOnActionListDialog.setTitle(str);
                this.mOnActionListDialog.setAdapter(zMListAdapter);
                this.mOnActionListDialog.setDialogCallback(dialogCallback);
                this.mOnActionListDialog.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void mergeCall(final String str) {
        if (!CmmSIPCallManager.getInstance().hasMeetings() || !CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
            confirmMergeCall(str);
        } else {
            ConfirmAlertDialog.show(getContext(), getContext().getString(C4558R.string.zm_sip_callpeer_inmeeting_title_108086), getContext().getString(C4558R.string.zm_sip_merge_call_inmeeting_msg_108086), new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    PhonePBXSharedLineRecyclerView.this.confirmMergeCall(str);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void confirmMergeCall(String str) {
        CmmSIPCallManager.getInstance().mergeCall(CmmSIPCallManager.getInstance().getCurrentCallID(), str);
    }

    /* access modifiers changed from: private */
    public boolean resumeCall(String str) {
        if (CmmSIPCallManager.isPhoneCallOffHook()) {
            CmmSIPCallManager.getInstance().showErrorDialogImmediately(getContext().getString(C4558R.string.zm_title_error), getContext().getString(C4558R.string.zm_sip_can_not_unhold_on_phone_call_111899), 0);
            return false;
        }
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        instance.resetCurrentCall(str);
        return instance.resumeCall(str);
    }

    private void checkDialog() {
        dismissActionListDialog();
    }

    private void dismissActionListDialog() {
        BigRoundListDialog bigRoundListDialog = this.mOnActionListDialog;
        if (bigRoundListDialog != null && bigRoundListDialog.isShowing()) {
            this.mOnActionListDialog.dismiss();
            this.mOnActionListDialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void refresh() {
        this.mHandler.removeCallbacks(this.mRefreshRunnable);
        this.mHandler.postDelayed(this.mRefreshRunnable, 1000);
    }

    /* access modifiers changed from: private */
    public void startUpdateCallDuration() {
        this.mHandler.removeCallbacks(this.mUpdateCallDurationRunnable);
        this.mHandler.postDelayed(this.mUpdateCallDurationRunnable, 1000);
    }

    private IMAddrBookItem getAddrItemByNumber(String str) {
        ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(str);
        if (zoomBuddyByNumber == null) {
            zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getSelfBuddyByNumber(str);
        }
        if (zoomBuddyByNumber != null) {
            return IMAddrBookItem.fromZoomBuddy(zoomBuddyByNumber);
        }
        return null;
    }

    public int getDataCount() {
        SharedLineAdapter sharedLineAdapter = this.mAdapter;
        if (sharedLineAdapter == null) {
            return 0;
        }
        return sharedLineAdapter.getItemCount();
    }

    public void setParentFragment(IPhonePBXLinesParentFragment iPhonePBXLinesParentFragment) {
        this.mParentFragment = iPhonePBXLinesParentFragment;
    }

    public void onResume() {
        startUpdateCallDuration();
    }

    public void onPause() {
        this.mHandler.removeCallbacks(this.mUpdateCallDurationRunnable);
    }

    public void onDestroy() {
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineMgrEventSinkListener);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
        this.mHandler.removeCallbacksAndMessages(null);
    }

    /* access modifiers changed from: private */
    public void onPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
        if (!(list == null || list.size() == 0 || !ZMPhoneUtils.isPBXFeatureOptionChanged(list, CmmSIPCallManager.getInstance().getLBREnabledBit()))) {
            updateVisibleItems();
        }
    }
}
