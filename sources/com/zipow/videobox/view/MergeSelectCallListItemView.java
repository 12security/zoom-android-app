package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.IZMListItemView.IActionClickListener;
import p021us.zoom.androidlib.widget.IZMSIPListItem;
import p021us.zoom.videomeetings.C4558R;

public class MergeSelectCallListItemView extends LinearLayout {
    /* access modifiers changed from: private */
    public IActionClickListener mIActionClickListener;
    private ImageView mIvAction;
    private PresenceStateView mPresenceStateView;
    @Nullable
    protected IZMSIPListItem mSipListItem;
    private TextView mTxtLabel;
    private TextView mTxtSubLabel;

    public MergeSelectCallListItemView(Context context) {
        super(context);
        initViews();
    }

    public MergeSelectCallListItemView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public MergeSelectCallListItemView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        initLayout();
        this.mTxtLabel = (TextView) findViewById(C4558R.C4560id.txtLabel);
        this.mTxtSubLabel = (TextView) findViewById(C4558R.C4560id.txtSubLabel);
        this.mIvAction = (ImageView) findViewById(C4558R.C4560id.ivAction);
        this.mIvAction.setImageResource(C4558R.C4559drawable.zm_sip_btn_merge_call_small);
        this.mIvAction.setContentDescription(getContext().getString(C4558R.string.zm_accessbility_sip_merge_call_61394));
        this.mPresenceStateView = (PresenceStateView) findViewById(C4558R.C4560id.presenceStateView);
        this.mIvAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MergeSelectCallListItemView.this.mIActionClickListener != null && MergeSelectCallListItemView.this.mSipListItem != null) {
                    MergeSelectCallListItemView.this.mIActionClickListener.onAction(MergeSelectCallListItemView.this.mSipListItem.getId(), 1);
                }
            }
        });
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

    public void bindView(@Nullable MergeSelectCallListItem mergeSelectCallListItem, IActionClickListener iActionClickListener) {
        if (mergeSelectCallListItem != null) {
            this.mIActionClickListener = iActionClickListener;
            this.mSipListItem = mergeSelectCallListItem;
            setTxtLabel(mergeSelectCallListItem.getLabel());
            setTxtSubLabel(mergeSelectCallListItem.getSubLabel());
            setPresenceState(mergeSelectCallListItem.getAddrBookItem());
        }
    }
}
