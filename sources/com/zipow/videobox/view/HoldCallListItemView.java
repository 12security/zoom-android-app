package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.view.IZMListItemView.IActionClickListener;
import p021us.zoom.androidlib.widget.IZMSIPListItem;
import p021us.zoom.videomeetings.C4558R;

public class HoldCallListItemView extends LinearLayout {
    /* access modifiers changed from: private */
    public IActionClickListener mIActionClickListener;
    private ImageView mIvAction;
    private PresenceStateView mPresenceStateView;
    @Nullable
    protected IZMSIPListItem mSipListItem;
    private TextView mTxtLabel;
    private TextView mTxtSubLabel;

    public HoldCallListItemView(Context context) {
        super(context);
        initViews();
    }

    public HoldCallListItemView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public HoldCallListItemView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        initLayout();
        this.mTxtLabel = (TextView) findViewById(C4558R.C4560id.txtLabel);
        this.mTxtSubLabel = (TextView) findViewById(C4558R.C4560id.txtSubLabel);
        this.mIvAction = (ImageView) findViewById(C4558R.C4560id.ivAction);
        this.mPresenceStateView = (PresenceStateView) findViewById(C4558R.C4560id.presenceStateView);
    }

    private void initLayout() {
        View.inflate(getContext(), C4558R.layout.zm_sip_hold_list_item, this);
    }

    public void setTxtLabel(String str) {
        TextView textView = this.mTxtLabel;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setTxtSubLabel(String str) {
        TextView textView = this.mTxtSubLabel;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setPresenceState(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            this.mPresenceStateView.setState(iMAddrBookItem);
            this.mPresenceStateView.setmTxtDeviceTypeGone();
            return;
        }
        this.mPresenceStateView.setVisibility(8);
    }

    public void bindView(@Nullable HoldCallListItem holdCallListItem, IActionClickListener iActionClickListener) {
        if (holdCallListItem != null) {
            this.mIActionClickListener = iActionClickListener;
            this.mSipListItem = holdCallListItem;
            setTxtLabel(holdCallListItem.getLabel());
            setTxtSubLabel(holdCallListItem.getSubLabel());
            setPresenceState(holdCallListItem.getAddrBookItem());
            this.mIvAction.setVisibility(holdCallListItem.isShowAction() ? 0 : 4);
            final boolean isInJoinMeeingRequest = CmmSIPCallManager.getInstance().isInJoinMeeingRequest(holdCallListItem.getId());
            this.mIvAction.setImageResource(isInJoinMeeingRequest ? C4558R.C4559drawable.zm_sip_btn_join_meeting_request : C4558R.C4559drawable.zm_sip_btn_tap_to_swap);
            this.mIvAction.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (HoldCallListItemView.this.mIActionClickListener != null) {
                        int i = 3;
                        if (isInJoinMeeingRequest) {
                            i = 4;
                        }
                        HoldCallListItemView.this.mIActionClickListener.onAction(HoldCallListItemView.this.mSipListItem.getId(), i);
                    }
                }
            });
        }
    }
}
