package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLineCallItem;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineBean;
import com.zipow.videobox.sip.server.CmmSIPLineCallItemBean;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.view.sip.AbstractSharedLineItem.OnItemClickListener;
import com.zipow.videobox.view.sip.AbstractSharedLineItem.SharedLineItemType;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.videomeetings.C4558R;

public class SharedLineCallItem extends AbstractSharedLineItem {
    public static final String PAYLOAD_UPDATE_CALL_DURATION = "UPDATE_CALL_DURATION";
    /* access modifiers changed from: private */
    public boolean mIsLast;
    /* access modifiers changed from: private */
    public String mLineCallId;
    private String mLineId;

    public static class SharedLineItemViewHolder extends BaseViewHolder {
        private View mBottomDivider;
        private Button mBtnAccept;
        private Button mBtnHangup;
        private SharedLineCallItem mItem;
        private ImageView mIvCallStatusIv;
        private ImageView mIvMoreOptions;
        private TextView mTvCalleeUserName;
        private TextView mTvCallerUserName;
        private TextView mTvDivider;
        private TextView mTvDuration;

        public SharedLineItemViewHolder(View view, final OnItemClickListener onItemClickListener) {
            super(view);
            C41021 r0 = new OnClickListener() {
                public void onClick(View view) {
                    OnItemClickListener onItemClickListener = onItemClickListener;
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(view, SharedLineItemViewHolder.this.getAdapterPosition());
                    }
                }
            };
            view.setOnClickListener(r0);
            this.mTvCallerUserName = (TextView) view.findViewById(C4558R.C4560id.tv_caller_user_name);
            this.mTvCalleeUserName = (TextView) view.findViewById(C4558R.C4560id.tv_callee_user_name);
            this.mTvDivider = (TextView) view.findViewById(C4558R.C4560id.tv_divider);
            this.mTvDuration = (TextView) view.findViewById(C4558R.C4560id.tv_duration);
            this.mBtnAccept = (Button) view.findViewById(C4558R.C4560id.btn_accept);
            this.mBtnAccept.setOnClickListener(r0);
            this.mBtnHangup = (Button) view.findViewById(C4558R.C4560id.btn_hang_up);
            this.mBtnHangup.setOnClickListener(r0);
            this.mIvCallStatusIv = (ImageView) view.findViewById(C4558R.C4560id.iv_call_status);
            this.mIvCallStatusIv.setOnClickListener(r0);
            this.mIvMoreOptions = (ImageView) view.findViewById(C4558R.C4560id.iv_more_options);
            this.mIvMoreOptions.setOnClickListener(r0);
            this.mBottomDivider = view.findViewById(C4558R.C4560id.bottom_divider);
        }

        /* access modifiers changed from: private */
        public void setData(SharedLineCallItem sharedLineCallItem) {
            int i;
            SharedLineCallItem sharedLineCallItem2 = sharedLineCallItem;
            this.mItem = sharedLineCallItem2;
            if (sharedLineCallItem2 != null) {
                CmmSIPLineCallItemBean lineCallItemBean = this.mItem.getLineCallItemBean();
                if (lineCallItemBean != null) {
                    CmmSIPCallItem callItemByCallID = CmmSIPCallManager.getInstance().getCallItemByCallID(lineCallItemBean.getRelatedLocalCallID());
                    int status = lineCallItemBean.getStatus();
                    if (status != 0) {
                        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
                        boolean isItBelongToMe = lineCallItemBean.isItBelongToMe();
                        if (!isItBelongToMe || callItemByCallID != null) {
                            Context context = this.itemView.getContext();
                            if (!instance.isCallingout(instance.getCurrentCallID()) && isItBelongToMe && instance.isMultiCalls()) {
                                boolean hasConference = instance.hasConference();
                                boolean isTransferring = instance.isTransferring(callItemByCallID);
                                boolean isInJoinMeeingRequest = instance.isInJoinMeeingRequest(callItemByCallID.getCallID());
                                CmmSIPCallItem otherSIPCallItem = instance.getOtherSIPCallItem(callItemByCallID, isTransferring);
                                boolean isInJoinMeeingRequest2 = instance.isInJoinMeeingRequest(otherSIPCallItem != null ? otherSIPCallItem.getCallID() : "");
                                if (isInJoinMeeingRequest) {
                                    this.mIvCallStatusIv.setVisibility(0);
                                    this.mIvCallStatusIv.setImageResource(C4558R.C4559drawable.zm_sip_btn_join_meeting_request_inline);
                                    this.mIvCallStatusIv.setContentDescription(context.getString(C4558R.string.zm_accessbility_sip_join_meeting_action_53992));
                                } else if (status == 2) {
                                    int callStatus = callItemByCallID.getCallStatus();
                                    if (callStatus == 27 || callStatus == 31) {
                                        this.mIvCallStatusIv.setVisibility(0);
                                        this.mIvCallStatusIv.setImageResource(C4558R.C4559drawable.zm_ic_shared_line_hold);
                                        this.mIvCallStatusIv.setContentDescription(context.getString(C4558R.string.zm_sip_on_hold_61381));
                                    } else {
                                        this.mIvCallStatusIv.setVisibility(8);
                                    }
                                } else if (hasConference || isTransferring || isInJoinMeeingRequest2 || CmmSIPCallManager.getInstance().isLBREnabled()) {
                                    this.mIvCallStatusIv.setVisibility(8);
                                } else {
                                    this.mIvCallStatusIv.setVisibility(0);
                                    this.mIvCallStatusIv.setImageResource(C4558R.C4559drawable.zm_sip_btn_merge_call);
                                    this.mIvCallStatusIv.setContentDescription(context.getString(C4558R.string.zm_accessbility_btn_merge_call_14480));
                                }
                            } else if (status != 2) {
                                this.mIvCallStatusIv.setVisibility(8);
                            } else if (isItBelongToMe) {
                                int callStatus2 = callItemByCallID.getCallStatus();
                                if (callStatus2 == 27 || callStatus2 == 31) {
                                    this.mIvCallStatusIv.setVisibility(0);
                                    this.mIvCallStatusIv.setImageResource(C4558R.C4559drawable.zm_ic_shared_line_hold);
                                    this.mIvCallStatusIv.setContentDescription(context.getString(C4558R.string.zm_sip_on_hold_61381));
                                } else {
                                    this.mIvCallStatusIv.setVisibility(8);
                                }
                            } else if (sharedLineCallItem.canPickUpCall()) {
                                this.mIvCallStatusIv.setVisibility(0);
                                this.mIvCallStatusIv.setImageResource(C4558R.C4559drawable.zm_ic_shared_line_hold);
                                this.mIvCallStatusIv.setContentDescription(context.getString(C4558R.string.zm_sip_sla_accessibility_pick_up_button_82852));
                            } else {
                                this.mIvCallStatusIv.setVisibility(8);
                            }
                            if (status == 2) {
                                this.mTvDuration.setText(C4558R.string.zm_sip_sla_hold_82852);
                            } else if (status == 3) {
                                updateCallDuration();
                            }
                            if (status == 1) {
                                this.mBtnAccept.setVisibility(0);
                                this.mBtnHangup.setVisibility(0);
                                i = 8;
                                this.mTvDivider.setVisibility(8);
                                this.mTvDuration.setVisibility(8);
                                this.mIvCallStatusIv.setVisibility(8);
                                this.mIvMoreOptions.setVisibility(8);
                            } else {
                                i = 8;
                                this.mBtnAccept.setVisibility(8);
                                this.mBtnHangup.setVisibility(8);
                                this.mTvDivider.setVisibility(0);
                                this.mTvDuration.setVisibility(0);
                                this.mIvMoreOptions.setVisibility(0);
                            }
                            String displayNameByNumber = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(lineCallItemBean.getPeerNumber());
                            if (StringUtil.isEmptyOrNull(displayNameByNumber)) {
                                displayNameByNumber = lineCallItemBean.getPeerDisplayName();
                            }
                            if (isItBelongToMe) {
                                if (lineCallItemBean.isMergedLineCallHost()) {
                                    displayNameByNumber = CmmSIPCallManager.getInstance().getSipCallDisplayName(callItemByCallID);
                                }
                                if (status == 1) {
                                    this.mTvCallerUserName.setText(displayNameByNumber);
                                    this.mTvCalleeUserName.setText(C4558R.string.zm_mm_unknow_call_35364);
                                } else {
                                    int callGenerate = callItemByCallID.getCallGenerate();
                                    if (callGenerate == 2 || callGenerate == 6 || callGenerate == 0) {
                                        this.mTvCallerUserName.setText(displayNameByNumber);
                                        this.mTvCalleeUserName.setText(C4558R.string.zm_qa_you);
                                    } else {
                                        this.mTvCallerUserName.setText(C4558R.string.zm_qa_you);
                                        this.mTvCalleeUserName.setText(displayNameByNumber);
                                    }
                                }
                            } else {
                                String displayNameByNumber2 = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(lineCallItemBean.getOwnerNumber());
                                if (StringUtil.isEmptyOrNull(displayNameByNumber2)) {
                                    displayNameByNumber2 = lineCallItemBean.getOwnerName();
                                }
                                this.mTvCallerUserName.setText(displayNameByNumber);
                                this.mTvCalleeUserName.setText(displayNameByNumber2);
                            }
                            if (!isItBelongToMe || status != 3) {
                                int color = context.getResources().getColor(C4558R.color.zm_text_light_dark);
                                int color2 = context.getResources().getColor(C4558R.color.zm_text_deep_grey);
                                this.mTvCallerUserName.setTextColor(color);
                                this.mTvCalleeUserName.setTextColor(color2);
                                this.mTvDivider.setTextColor(color);
                                this.mTvDuration.setTextColor(color);
                            } else {
                                int color3 = context.getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB);
                                this.mTvCallerUserName.setTextColor(color3);
                                this.mTvCalleeUserName.setTextColor(color3);
                                this.mTvDivider.setTextColor(color3);
                                this.mTvDuration.setTextColor(color3);
                            }
                            View view = this.mBottomDivider;
                            if (sharedLineCallItem.mIsLast) {
                                i = 0;
                            }
                            view.setVisibility(i);
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: private */
        public void updateCallDuration() {
            CmmSIPLineCallItem GetLineCallItemProtoByID = CmmSIPLineManager.getInstance().GetLineCallItemProtoByID(this.mItem.mLineCallId);
            if (GetLineCallItemProtoByID != null && GetLineCallItemProtoByID.getStatus() == 3) {
                long durationTime = (long) GetLineCallItemProtoByID.getDurationTime();
                if (durationTime < 0) {
                    durationTime = 0;
                }
                this.mTvDuration.setText(TimeUtil.formateDuration(durationTime));
            }
        }
    }

    public SharedLineCallItem(@NonNull CmmSIPLineCallItemBean cmmSIPLineCallItemBean, boolean z) {
        this.mLineId = cmmSIPLineCallItemBean.getLineID();
        this.mLineCallId = cmmSIPLineCallItemBean.getLineCallID();
        this.mIsLast = z;
    }

    public static BaseViewHolder createViewHolder(ViewGroup viewGroup, OnItemClickListener onItemClickListener) {
        return new SharedLineItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_shared_line_item, viewGroup, false), onItemClickListener);
    }

    public int getViewType() {
        return SharedLineItemType.ITEM_SHARED_LINE.ordinal();
    }

    public void bindViewHolder(BaseViewHolder baseViewHolder, @Nullable List<Object> list) {
        if (!(baseViewHolder instanceof SharedLineItemViewHolder)) {
            return;
        }
        if (list == null || !list.contains(PAYLOAD_UPDATE_CALL_DURATION)) {
            ((SharedLineItemViewHolder) baseViewHolder).setData(this);
        } else {
            ((SharedLineItemViewHolder) baseViewHolder).updateCallDuration();
        }
    }

    public int getLineCallItemStatus() {
        CmmSIPLineCallItemBean lineCallItemBean = getLineCallItemBean();
        if (lineCallItemBean == null) {
            return 0;
        }
        return lineCallItemBean.getStatus();
    }

    public boolean canPickUpCall() {
        CmmSIPLineBean sharedLineBeanById = CmmSIPLineManager.getInstance().getSharedLineBeanById(this.mLineId);
        if (sharedLineBeanById == null) {
            return false;
        }
        return sharedLineBeanById.canPickUpCall();
    }

    public CmmSIPLineCallItemBean getLineCallItemBean() {
        return CmmSIPLineManager.getInstance().getLineCallItemBeanById(this.mLineCallId);
    }

    public String getLineCallId() {
        return this.mLineCallId;
    }

    @Nullable
    public String getLocalCallId() {
        CmmSIPLineCallItemBean lineCallItemBean = getLineCallItemBean();
        if (lineCallItemBean == null) {
            return null;
        }
        return lineCallItemBean.getRelatedLocalCallID();
    }

    @Nullable
    public CmmSIPCallItem getLocalCallItem() {
        CmmSIPLineCallItemBean lineCallItemBean = getLineCallItemBean();
        if (lineCallItemBean == null) {
            return null;
        }
        return CmmSIPCallManager.getInstance().getCallItemByCallID(lineCallItemBean.getRelatedLocalCallID());
    }

    public String getLineId() {
        return this.mLineId;
    }

    public void setIsLast(boolean z) {
        this.mIsLast = z;
    }

    public boolean isLast() {
        return this.mIsLast;
    }
}
