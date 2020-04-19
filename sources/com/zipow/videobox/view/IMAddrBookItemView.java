package com.zipow.videobox.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.AlertWhenAvailableHelper;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.videomeetings.C4558R;

public class IMAddrBookItemView extends LinearLayout implements OnClickListener {
    private static final int MSG_LAZY_REFRESH_UI = 1;
    private AvatarView mAvatarView;
    protected TextView mEmail;
    @NonNull
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            boolean z = true;
            if (message.what == 1) {
                boolean z2 = message.arg1 == 1;
                if (message.arg2 != 1) {
                    z = false;
                }
                IMAddrBookItemView.this.refreshUI(z2, z);
            }
        }
    };
    protected ImageView mImageCall;
    private ImageView mImgBell;
    @Nullable
    protected IMAddrBookItem mItem;
    private OnActionClickListner mOnActionClickListner;
    private int mPbxMode;
    protected PresenceStateView mPresenceStateView;
    protected TextView mTxtCustomMessage;
    private ZMEllipsisTextView mTxtScreenName;
    private TextView mWaitApproval;

    public interface OnActionClickListner {
        void onCallClick(IMAddrBookItem iMAddrBookItem);
    }

    public IMAddrBookItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public IMAddrBookItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (ZMEllipsisTextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mTxtCustomMessage = (TextView) findViewById(C4558R.C4560id.txtCustomMessage);
        this.mWaitApproval = (TextView) findViewById(C4558R.C4560id.waitApproval);
        this.mEmail = (TextView) findViewById(C4558R.C4560id.email);
        this.mPresenceStateView = (PresenceStateView) findViewById(C4558R.C4560id.presenceStateView);
        this.mImgBell = (ImageView) findViewById(C4558R.C4560id.imgBell);
        this.mImageCall = (ImageView) findViewById(C4558R.C4560id.imageCall);
        this.mPresenceStateView.setmTxtDeviceTypeGone();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_addrbook_item, this);
    }

    public void setOnActionClickListner(OnActionClickListner onActionClickListner) {
        this.mOnActionClickListner = onActionClickListner;
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            int i = 0;
            IMAddrBookItem iMAddrBookItem = this.mItem;
            if (iMAddrBookItem == null || !iMAddrBookItem.isMyNote()) {
                IMAddrBookItem iMAddrBookItem2 = this.mItem;
                if (iMAddrBookItem2 != null) {
                    if (iMAddrBookItem2.getAccountStatus() == 1) {
                        i = C4558R.string.zm_lbl_deactivated_62074;
                    } else if (this.mItem.getAccountStatus() == 2) {
                        i = C4558R.string.zm_lbl_terminated_62074;
                    }
                }
            } else {
                i = C4558R.string.zm_mm_msg_my_notes_65147;
            }
            this.mTxtScreenName.setEllipsisText((String) charSequence, i);
        }
    }

    @Nullable
    public IMAddrBookItem getDataItem() {
        return this.mItem;
    }

    /* access modifiers changed from: private */
    public void refreshUI(boolean z, boolean z2) {
        IMAddrBookItem iMAddrBookItem = this.mItem;
        if (iMAddrBookItem != null) {
            setScreenName(BuddyNameUtil.getPedingDisplayName(iMAddrBookItem));
            if (!isInEditMode()) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null && this.mItem != null && getContext() != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mItem.getJid());
                    AvatarView avatarView = this.mAvatarView;
                    if (avatarView != null) {
                        avatarView.show(this.mItem.getAvatarParamsBuilder());
                    }
                    boolean z3 = true;
                    if (this.mPbxMode == 1) {
                        this.mImageCall.setVisibility(0);
                    } else {
                        this.mImageCall.setVisibility(8);
                    }
                    if (buddyWithJID == null) {
                        this.mWaitApproval.setVisibility(8);
                        String str = "";
                        if (!this.mItem.isFromPhoneContacts() || this.mItem.getContact() == null) {
                            z3 = false;
                        } else {
                            str = this.mItem.getContact().getDisplayPhoneNumber();
                            if (StringUtil.isEmptyOrNull(str)) {
                                z3 = false;
                            }
                        }
                        if (z3) {
                            this.mTxtCustomMessage.setVisibility(0);
                            this.mTxtCustomMessage.setText(str);
                        } else {
                            this.mTxtCustomMessage.setVisibility(8);
                        }
                        this.mPresenceStateView.setVisibility(4);
                        return;
                    }
                    this.mImgBell.setVisibility(AlertWhenAvailableHelper.getInstance().isInAlertQueen(this.mItem.getJid()) ? 0 : 8);
                    this.mPresenceStateView.setState(this.mItem);
                    if (this.mItem.isPending()) {
                        this.mTxtCustomMessage.setVisibility(8);
                        this.mPresenceStateView.setVisibility(8);
                        this.mWaitApproval.setVisibility(0);
                    } else {
                        this.mWaitApproval.setVisibility(8);
                        if (this.mItem.isMyNote() || this.mItem.getIsRoomDevice() || this.mItem.isSharedGlobalDirectory()) {
                            this.mPresenceStateView.setVisibility(8);
                        } else {
                            this.mPresenceStateView.setVisibility(0);
                        }
                    }
                    String signature = this.mItem.getSignature();
                    int i = this.mPbxMode;
                    if (i == 1) {
                        signature = this.mItem.getCloudDefaultPhoneNo(false);
                    } else if (i == 2) {
                        signature = this.mItem.getCloudDefaultPhoneNo(true);
                    }
                    if (TextUtils.isEmpty(signature)) {
                        this.mTxtCustomMessage.setVisibility(8);
                    } else {
                        this.mTxtCustomMessage.setVisibility(0);
                        this.mTxtCustomMessage.setText(signature);
                    }
                }
            }
        }
    }

    public void setAddrBookItem(@Nullable IMAddrBookItem iMAddrBookItem, boolean z, boolean z2, boolean z3) {
        setAddrBookItem(iMAddrBookItem, z, z2, z3, 0);
    }

    public void setAddrBookItem(@Nullable IMAddrBookItem iMAddrBookItem, boolean z, boolean z2, boolean z3, int i) {
        if (iMAddrBookItem != null) {
            this.mItem = iMAddrBookItem;
            this.mPbxMode = i;
            setScreenName(BuddyNameUtil.getPedingDisplayName(this.mItem));
            this.mHandler.removeMessages(1);
            if (iMAddrBookItem.isPropertyInit() || z3) {
                refreshUI(z, z2);
            } else {
                updateViewWhenBuddyNotInit();
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, z ? 1 : 0, z2 ? 1 : 0), 150);
            }
        }
    }

    public void showPresenceView(boolean z) {
        PresenceStateView presenceStateView = this.mPresenceStateView;
        if (presenceStateView != null) {
            presenceStateView.setVisibility(z ? 0 : 8);
        }
    }

    private void updateViewWhenBuddyNotInit() {
        this.mPresenceStateView.resetState();
    }

    public void onClick(@Nullable View view) {
        if (view != null && view.getId() == C4558R.C4560id.imageCall) {
            onClickImageCall();
        }
    }

    private void onClickImageCall() {
        OnActionClickListner onActionClickListner = this.mOnActionClickListner;
        if (onActionClickListner != null) {
            onActionClickListner.onCallClick(this.mItem);
        }
    }
}
