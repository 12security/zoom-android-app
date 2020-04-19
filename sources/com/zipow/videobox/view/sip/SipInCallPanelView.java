package com.zipow.videobox.view.sip;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.GridItemDecoration.Builder;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.videomeetings.C4558R;

public class SipInCallPanelView extends RecyclerView implements OnRecyclerViewListener {
    private static final int MAX_ACTION_COUNT_INPANEL = 9;
    private static final int SPAN_COUNT = 3;
    public final String TAG = SipInCallPanelView.class.getSimpleName();
    private SipInCallPanelAdapter mAdapter;
    private boolean mIsDTMFMode = false;
    private List<Integer> mMoreActionList = new ArrayList();
    private List<Integer> mPanelActionList = new ArrayList();
    private OnInCallPanelListener mPanelListener;

    public interface OnInCallPanelListener {
        void onPanelItemClick(int i);
    }

    public static class PanelButtonItem extends ZMSimpleMenuItem {
        public static final int ACTION_ADD_CALL = 3;
        public static final int ACTION_DTMF = 1;
        public static final int ACTION_HOLD = 4;
        public static final int ACTION_MINIMIZE = 8;
        public static final int ACTION_MORE = 9;
        public static final int ACTION_MUTE = 0;
        public static final int ACTION_RECORD = 6;
        public static final int ACTION_SPEAKER = 2;
        public static final int ACTION_SWITCH_TO_CARRIER = 10;
        public static final int ACTION_TO_MEETING = 7;
        public static final int ACTION_TRANSFER = 5;
        private boolean clickableInDisabled = false;

        public PanelButtonItem(int i, String str, Drawable drawable) {
            super(i, str, drawable, false);
        }

        public void updateItem(String str, boolean z) {
            super.updateMenuItem(str, z, false);
        }

        public void updateItem(String str, boolean z, boolean z2) {
            super.updateMenuItem(str, z, z2);
        }

        public void updateItem(String str, Drawable drawable, boolean z) {
            super.updateMenuItem(str, drawable, z, false);
        }

        public void setEnable(boolean z) {
            super.setmDisable(!z);
        }

        public boolean isClickableInDisabled() {
            return this.clickableInDisabled;
        }

        public void setClickableInDisabled(boolean z) {
            this.clickableInDisabled = z;
        }
    }

    public boolean onItemLongClick(View view, int i) {
        return false;
    }

    public SipInCallPanelView(@NonNull Context context) {
        super(context);
        initViews();
    }

    public SipInCallPanelView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public SipInCallPanelView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        setLayoutManager(new GridLayoutManager(getContext(), 3));
        addItemDecoration(new Builder(getContext()).setColorResource(C4558R.color.zm_transparent).setShowLastLine(false).setHorizontalSpan(((float) UIUtil.dip2px(getContext(), 15.0f)) * 1.0f).setVerticalSpan(0.0f).build());
        this.mAdapter = new SipInCallPanelAdapter(getContext());
        this.mAdapter.setData(initActionList());
        this.mAdapter.setOnRecyclerViewListener(this);
        setAdapter(this.mAdapter);
    }

    private List<PanelButtonItem> initActionList() {
        LinkedList actionList = getActionList();
        int size = actionList.size();
        this.mPanelActionList.clear();
        this.mMoreActionList.clear();
        if (size > 9) {
            this.mPanelActionList.addAll(actionList.subList(0, 8));
            this.mPanelActionList.add(Integer.valueOf(9));
            this.mMoreActionList.addAll(actionList.subList(8, size));
        } else {
            this.mPanelActionList.addAll(actionList);
            this.mMoreActionList.clear();
        }
        int size2 = this.mPanelActionList.size();
        ArrayList arrayList = new ArrayList(size2);
        Context context = getContext();
        for (int i = 0; i < size2; i++) {
            PanelButtonItem generatePanelButtonItem = generatePanelButtonItem(context, ((Integer) this.mPanelActionList.get(i)).intValue());
            if (generatePanelButtonItem != null) {
                arrayList.add(generatePanelButtonItem);
            }
        }
        return arrayList;
    }

    @NonNull
    private LinkedList<Integer> getActionList() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        boolean isCloudPBXEnabled = instance.isCloudPBXEnabled();
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(Integer.valueOf(0));
        linkedList.add(Integer.valueOf(1));
        linkedList.add(Integer.valueOf(2));
        linkedList.add(Integer.valueOf(3));
        linkedList.add(Integer.valueOf(4));
        linkedList.add(Integer.valueOf(5));
        if (isCloudPBXEnabled && !instance.isCallOffLoad(instance.getCurrentCallID())) {
            if (instance.isEnableADHocCallRecording() || instance.isAutoRecordingPoliciesEnable(instance.getCurrentCallItem())) {
                linkedList.add(Integer.valueOf(6));
            }
            linkedList.add(Integer.valueOf(7));
        }
        linkedList.add(Integer.valueOf(8));
        if (isCloudPBXEnabled && instance.isEnableHasCallingPlan()) {
            linkedList.add(Integer.valueOf(10));
        }
        return linkedList;
    }

    public List<Integer> getMoreActionList() {
        return this.mMoreActionList;
    }

    public void setDTMFMode(boolean z) {
        this.mIsDTMFMode = z;
        updatePanelInCall();
    }

    public void setOnInCallPanelListener(OnInCallPanelListener onInCallPanelListener) {
        this.mPanelListener = onInCallPanelListener;
    }

    public void updatePanelInCall() {
        boolean z;
        boolean z2;
        boolean z3;
        String str;
        String str2;
        if (!this.mIsDTMFMode && this.mAdapter != null) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
            if (currentCallItem == null) {
                ZMLog.m288w(this.TAG, "[updatePanelInCall], item is null", new Object[0]);
                return;
            }
            String callID = currentCallItem.getCallID();
            boolean isSpeakerOn = CmmSipAudioMgr.getInstance().isSpeakerOn();
            int currentAudioSourceType = CmmSipAudioMgr.getInstance().getCurrentAudioSourceType();
            boolean isAudioInMeeting = CmmSipAudioMgr.getInstance().isAudioInMeeting();
            PanelButtonItem itemByAction = this.mAdapter.getItemByAction(2);
            if (itemByAction != null) {
                if (!isSpeakerOn && HeadsetUtil.getInstance().isBluetoothHeadsetOn() && currentAudioSourceType == 3) {
                    itemByAction.updateItem(getResources().getString(C4558R.string.zm_btn_bluetooth_61381), getResources().getDrawable(C4558R.C4559drawable.zm_sip_ic_bluetooth), true);
                } else if (isSpeakerOn || !HeadsetUtil.getInstance().isWiredHeadsetOn() || currentAudioSourceType != 2) {
                    itemByAction.updateItem(getResources().getString(C4558R.string.zm_btn_speaker_61381), getResources().getDrawable(C4558R.C4559drawable.zm_sip_ic_speaker), isSpeakerOn);
                } else {
                    itemByAction.updateItem(getResources().getString(C4558R.string.zm_btn_headphones_61381), getResources().getDrawable(C4558R.C4559drawable.zm_sip_ic_headset), true);
                }
                itemByAction.setEnable(!isAudioInMeeting);
            }
            PanelButtonItem itemByAction2 = this.mAdapter.getItemByAction(0);
            if (itemByAction2 != null) {
                boolean isCallMuted = instance.isCallMuted();
                if (isCallMuted) {
                    str2 = getResources().getString(C4558R.string.zm_btn_unmute_61381);
                } else {
                    str2 = getResources().getString(C4558R.string.zm_btn_mute_61381);
                }
                itemByAction2.updateItem(str2, isCallMuted, isAudioInMeeting);
            }
            boolean isInCall = instance.isInCall(currentCallItem);
            boolean isAccepted = instance.isAccepted(currentCallItem);
            int callStatus = currentCallItem.getCallStatus();
            if (callStatus == 31) {
                z2 = true;
                z = true;
            } else if (callStatus == 30) {
                z2 = true;
                z = false;
            } else if (callStatus == 27) {
                z2 = false;
                z = true;
            } else {
                z2 = false;
                z = false;
            }
            boolean hasDataNetwork = NetworkUtil.hasDataNetwork(getContext());
            boolean z4 = (isInCall || z2 || z || isAccepted) && hasDataNetwork && !CmmSIPCallManager.isPhoneCallOffHook();
            boolean z5 = instance.isEnableADHocCallRecording() || instance.isAutoRecordingPoliciesEnable(currentCallItem);
            PanelButtonItem itemByAction3 = this.mAdapter.getItemByAction(4);
            if (itemByAction3 != null) {
                if (z) {
                    str = getResources().getString(C4558R.string.zm_sip_on_hold_61381);
                } else {
                    str = getResources().getString(C4558R.string.zm_sip_hold_61381);
                }
                itemByAction3.updateItem(str, z, !z4);
            }
            boolean isTransferring = instance.isTransferring(currentCallItem);
            boolean isInJoinMeeingRequest = instance.isInJoinMeeingRequest(callID);
            boolean isInSwitchingToCarrier = instance.isInSwitchingToCarrier(callID);
            PanelButtonItem itemByAction4 = this.mAdapter.getItemByAction(7);
            if (itemByAction4 != null) {
                if (instance.isCloudPBXEnabled()) {
                    itemByAction4.setEnable(z4 && !isInJoinMeeingRequest && !isTransferring && !isInSwitchingToCarrier);
                } else {
                    itemByAction4.setEnable(false);
                }
            }
            boolean z6 = z4 && !isTransferring && instance.isInMaxCallsCount() && !isInSwitchingToCarrier;
            PanelButtonItem itemByAction5 = this.mAdapter.getItemByAction(3);
            if (itemByAction5 != null) {
                itemByAction5.setEnable(z6);
            }
            boolean isInConference = currentCallItem.isInConference();
            PanelButtonItem itemByAction6 = this.mAdapter.getItemByAction(5);
            if (itemByAction6 != null) {
                itemByAction6.setEnable(z6 && !isInConference && !isInJoinMeeingRequest);
            }
            PanelButtonItem itemByAction7 = this.mAdapter.getItemByAction(10);
            if (itemByAction7 != null) {
                itemByAction7.setEnable(z4 && !isTransferring && !isInConference && !isInSwitchingToCarrier && instance.isEnableHasCallingPlan());
            }
            PanelButtonItem itemByAction8 = this.mAdapter.getItemByAction(6);
            if (itemByAction8 != null) {
                if (instance.isCloudPBXEnabled()) {
                    View panelRecordView = getPanelRecordView();
                    if (!(panelRecordView instanceof SipInCallPanelRecordView)) {
                        panelRecordView = null;
                    }
                    int callRecordingStatus = currentCallItem.getCallRecordingStatus();
                    boolean z7 = (isInCall || isAccepted) && hasDataNetwork && z5 && !isInSwitchingToCarrier && !CmmSIPCallManager.isPhoneCallOffHook();
                    String str3 = "";
                    if (!z7) {
                        if (!z5 && callRecordingStatus == 1 && instance.handleRecording(currentCallItem.getCallID(), 1) && panelRecordView != null) {
                            ((SipInCallPanelRecordView) panelRecordView).stop();
                        }
                        if (panelRecordView != null) {
                            ((SipInCallPanelRecordView) panelRecordView).stopped();
                        }
                        str3 = getResources().getString(C4558R.string.zm_sip_record_61381);
                        z3 = false;
                    } else if (instance.isAutoRecordingPoliciesEnable(currentCallItem)) {
                        if (panelRecordView != null) {
                            ((SipInCallPanelRecordView) panelRecordView).recording();
                        }
                        str3 = getResources().getString(C4558R.string.zm_sip_record_104213);
                        z3 = true;
                    } else if (callRecordingStatus == 0 || callRecordingStatus == 4) {
                        if (panelRecordView != null) {
                            ((SipInCallPanelRecordView) panelRecordView).stopped();
                        }
                        str3 = getResources().getString(C4558R.string.zm_sip_record_61381);
                        z3 = false;
                    } else if (callRecordingStatus == 1) {
                        if (panelRecordView != null) {
                            ((SipInCallPanelRecordView) panelRecordView).recording();
                        }
                        str3 = getResources().getString(C4558R.string.zm_sip_stop_record_61381);
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    itemByAction8.updateItem(str3, z3, true ^ z7);
                } else {
                    itemByAction8.setEnable(false);
                }
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public boolean isPanelHoldEnable() {
        SipInCallPanelAdapter sipInCallPanelAdapter = this.mAdapter;
        if (sipInCallPanelAdapter == null) {
            return false;
        }
        PanelButtonItem itemByAction = sipInCallPanelAdapter.getItemByAction(4);
        if (itemByAction != null) {
            return !itemByAction.isDisable();
        }
        return false;
    }

    public View getPanelHoldView() {
        return getViewByAction(4);
    }

    public View getPanelRecordView() {
        return getViewByAction(6);
    }

    private View getViewByAction(int i) {
        View view = null;
        if (this.mAdapter == null) {
            return null;
        }
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int findFirstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
            while (true) {
                if (findFirstVisibleItemPosition > findLastVisibleItemPosition) {
                    break;
                }
                PanelButtonItem panelButtonItem = (PanelButtonItem) this.mAdapter.getItem(findFirstVisibleItemPosition);
                if (panelButtonItem != null && panelButtonItem.getAction() == i) {
                    ViewHolder findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                    if (findViewHolderForAdapterPosition != null) {
                        view = findViewHolderForAdapterPosition.itemView;
                        break;
                    }
                }
                findFirstVisibleItemPosition++;
            }
        }
        return view;
    }

    public void onItemClick(View view, int i) {
        PanelButtonItem panelButtonItem = (PanelButtonItem) this.mAdapter.getItem(i);
        if (panelButtonItem != null) {
            if (panelButtonItem.isClickableInDisabled() || !panelButtonItem.isDisable()) {
                OnInCallPanelListener onInCallPanelListener = this.mPanelListener;
                if (onInCallPanelListener != null) {
                    onInCallPanelListener.onPanelItemClick(panelButtonItem.getAction());
                }
            }
        }
    }

    public void onClickPanelMute(boolean z) {
        int i;
        Resources resources;
        PanelButtonItem itemByAction = this.mAdapter.getItemByAction(0);
        if (itemByAction != null) {
            if (z) {
                resources = getResources();
                i = C4558R.string.zm_btn_unmute_61381;
            } else {
                resources = getResources();
                i = C4558R.string.zm_btn_mute_61381;
            }
            itemByAction.updateItem(resources.getString(i), z);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void togglePanelMuteTalkingState(boolean z) {
        View viewByAction = getViewByAction(0);
        if (viewByAction instanceof SipInCallPanelMuteView) {
            ((SipInCallPanelMuteView) viewByAction).togglePanelMuteTalkingState(z);
        }
    }

    public void onClickPanelHold() {
        if (CmmSIPCallManager.isPhoneCallOffHook()) {
            CmmSIPCallManager.getInstance().showErrorDialogImmediately(getContext().getString(C4558R.string.zm_title_error), getContext().getString(C4558R.string.zm_sip_can_not_unhold_on_phone_call_111899), 0);
            return;
        }
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        PanelButtonItem itemByAction = this.mAdapter.getItemByAction(4);
        if (!(currentCallItem == null || itemByAction == null)) {
            if (!itemByAction.isSelected() || (!instance.isInLocalHold(currentCallItem) && !instance.isInBothHold(currentCallItem))) {
                if (!itemByAction.isSelected() && (instance.isInCall(currentCallItem) || instance.isAccepted(currentCallItem) || instance.isInRemoteHold(currentCallItem))) {
                    if (instance.holdCall(currentCallItem.getCallID())) {
                        itemByAction.updateItem(getResources().getString(C4558R.string.zm_sip_on_hold_61381), true);
                        PanelButtonItem itemByAction2 = this.mAdapter.getItemByAction(6);
                        if (itemByAction2 != null) {
                            itemByAction2.setEnable(false);
                        }
                        this.mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), C4558R.string.zm_sip_hold_failed_27110, 1).show();
                    }
                }
            } else if (instance.resumeCall(currentCallItem.getCallID())) {
                itemByAction.updateItem(getResources().getString(C4558R.string.zm_sip_hold_61381), false);
                this.mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), C4558R.string.zm_sip_unhold_failed_27110, 1).show();
            }
        }
    }

    public void onClickPanelRecord() {
        PanelButtonItem itemByAction = this.mAdapter.getItemByAction(6);
        View panelRecordView = getPanelRecordView();
        if (itemByAction != null && (panelRecordView instanceof SipInCallPanelRecordView)) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
            if (currentCallItem != null) {
                boolean isAutoRecordingPoliciesEnable = instance.isAutoRecordingPoliciesEnable(currentCallItem);
                int callRecordingStatus = currentCallItem.getCallRecordingStatus();
                if (isAutoRecordingPoliciesEnable) {
                    instance.showTipsOnUITop(getContext().getString(C4558R.string.zm_pbx_auto_recording_104213));
                    return;
                }
                if (callRecordingStatus == 0) {
                    if (instance.handleRecording(currentCallItem.getCallID(), 0)) {
                        itemByAction.setLabel(getResources().getString(C4558R.string.zm_sip_record_preparing_37980));
                        ((SipInCallPanelRecordView) panelRecordView).start();
                    }
                } else if (callRecordingStatus == 1 && instance.handleRecording(currentCallItem.getCallID(), 1)) {
                    ((SipInCallPanelRecordView) panelRecordView).stop();
                    itemByAction.setLabel("");
                    panelRecordView.setContentDescription(getResources().getString(C4558R.string.zm_accessibility_sip_call_keypad_44057, new Object[]{getResources().getString(C4558R.string.zm_sip_stop_record_61381)}));
                }
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setMediaMode() {
        this.mAdapter.setData(initActionList());
        updatePanelInCall();
    }

    public void OnCallAutoRecordingEvent(int i) {
        if (i == 2) {
            this.mAdapter.setData(initActionList());
        }
    }

    @Nullable
    private static PanelButtonItem generatePanelButtonItem(Context context, int i, boolean z) {
        int i2;
        int i3;
        int i4;
        boolean z2 = false;
        switch (i) {
            case 0:
                i2 = C4558R.string.zm_btn_mute_61381;
                i3 = C4558R.C4559drawable.zm_sip_ic_mute;
                break;
            case 1:
                i2 = C4558R.string.zm_btn_keypad_61381;
                i3 = C4558R.C4559drawable.zm_sip_ic_dtmf;
                break;
            case 2:
                i2 = C4558R.string.zm_btn_speaker_61381;
                i3 = C4558R.C4559drawable.zm_sip_ic_speaker;
                break;
            case 3:
                i2 = C4558R.string.zm_sip_add_call_61381;
                i3 = C4558R.C4559drawable.zm_sip_ic_add_call;
                break;
            case 4:
                i2 = C4558R.string.zm_sip_hold_61381;
                i3 = C4558R.C4559drawable.zm_sip_ic_hold;
                break;
            case 5:
                i2 = C4558R.string.zm_sip_transfer_31432;
                i3 = C4558R.C4559drawable.zm_sip_ic_transfer;
                break;
            case 6:
                i2 = C4558R.string.zm_sip_record_61381;
                i3 = C4558R.C4559drawable.zm_sip_ic_record_off;
                z2 = true;
                break;
            case 7:
                i2 = C4558R.string.zm_sip_upgrade_to_video_call_53992;
                i3 = C4558R.C4559drawable.zm_sip_ic_call_to_meeting;
                break;
            case 8:
                if (!z) {
                    int i5 = C4558R.C4559drawable.zm_sip_ic_minimize;
                    i3 = i5;
                    i2 = C4558R.string.zm_sip_minimize_85332;
                    break;
                } else {
                    i2 = C4558R.string.zm_sip_minimize_85332;
                    i3 = C4558R.C4559drawable.zm_sip_ic_minimize_s;
                    break;
                }
            case 9:
                i2 = C4558R.string.zm_pbx_action_more_102668;
                i3 = C4558R.C4559drawable.zm_sip_ic_more_normal;
                break;
            case 10:
                if (z) {
                    i2 = C4558R.string.zm_pbx_switch_to_carrier_title_102668;
                    i4 = C4558R.C4559drawable.zm_sip_ic_switch_to_carrier_s;
                } else {
                    i2 = C4558R.string.zm_pbx_switch_to_carrier_102668;
                    i4 = C4558R.C4559drawable.zm_sip_ic_switch_to_carrier;
                }
                i3 = i4;
                z2 = true;
                break;
            default:
                return null;
        }
        PanelButtonItem panelButtonItem = new PanelButtonItem(i, context.getResources().getString(i2), context.getResources().getDrawable(i3));
        panelButtonItem.setClickableInDisabled(z2);
        return panelButtonItem;
    }

    @Nullable
    public static PanelButtonItem generatePanelButtonItem(Context context, int i) {
        return generatePanelButtonItem(context, i, false);
    }

    @Nullable
    public static PanelButtonItem generateMoreButtonItem(Context context, int i) {
        return generatePanelButtonItem(context, i, true);
    }
}
