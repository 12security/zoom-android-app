package com.zipow.videobox.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class LocalContactItemView extends LinearLayout {
    private AvatarView mAvatarView;
    private View mBtnInvite;
    @NonNull
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    @Nullable
    public LocalContactItem mItem;
    /* access modifiers changed from: private */
    public InviteLocalContactsListView mParentListView;
    private View mTxtAdded;
    private TextView mTxtScreenName;

    public LocalContactItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public LocalContactItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mBtnInvite = findViewById(C4558R.C4560id.btnInvite);
        this.mTxtAdded = findViewById(C4558R.C4560id.txtAdded);
        this.mBtnInvite.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (LocalContactItemView.this.mParentListView != null && LocalContactItemView.this.mItem != null) {
                    LocalContactItemView.this.mParentListView.showUserActions(LocalContactItemView.this.mItem);
                }
            }
        });
    }

    public void setParentListView(InviteLocalContactsListView inviteLocalContactsListView) {
        this.mParentListView = inviteLocalContactsListView;
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_local_contact_item, this);
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            this.mTxtScreenName.setText(charSequence);
        }
    }

    public void setLocalContactItem(@Nullable LocalContactItem localContactItem, boolean z, boolean z2) {
        if (localContactItem != null) {
            this.mItem = localContactItem;
            setScreenName(this.mItem.getScreenName());
            if (this.mItem.getIsZoomUser()) {
                this.mBtnInvite.setVisibility(8);
                this.mTxtAdded.setVisibility(0);
            } else {
                this.mBtnInvite.setVisibility(0);
                this.mTxtAdded.setVisibility(8);
            }
            if (!isInEditMode()) {
                AvatarView avatarView = this.mAvatarView;
                if (avatarView != null) {
                    avatarView.show(this.mItem.getAvatarParamsBuilder());
                }
            }
        }
    }
}
