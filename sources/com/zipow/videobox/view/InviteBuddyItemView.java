package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.videomeetings.C4558R;

public class InviteBuddyItemView extends LinearLayout {
    private AvatarView mAvatarView;
    private CheckedTextView mCheckbox;
    @NonNull
    private Handler mHandler = new Handler();
    @Nullable
    private InviteBuddyItem mItem;
    private PresenceStateView mPresenceStateView;
    private TextView mTxtEmail;
    private ZMEllipsisTextView mTxtScreenName;

    public InviteBuddyItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public InviteBuddyItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (ZMEllipsisTextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mTxtEmail = (TextView) findViewById(C4558R.C4560id.txtEmail);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mCheckbox = (CheckedTextView) findViewById(C4558R.C4560id.check);
        this.mPresenceStateView = (PresenceStateView) findViewById(C4558R.C4560id.presenceStateView);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_invite_buddy_item, this);
    }

    private boolean isAactivate() {
        InviteBuddyItem inviteBuddyItem = this.mItem;
        if (inviteBuddyItem == null) {
            return true;
        }
        IMAddrBookItem addrBookItem = inviteBuddyItem.getAddrBookItem();
        if (addrBookItem == null || addrBookItem.getAccountStatus() == 0) {
            return true;
        }
        return false;
    }

    private void setActivateStatus() {
        boolean isAactivate = isAactivate();
        ZMEllipsisTextView zMEllipsisTextView = this.mTxtScreenName;
        if (zMEllipsisTextView != null) {
            zMEllipsisTextView.setTextAppearance(getContext(), isAactivate() ? C4558R.style.UIKitTextView_BuddyName_Normal : C4558R.style.UIKitTextView_BuddyName_Deactivate);
        }
        TextView textView = this.mTxtEmail;
        if (textView != null) {
            textView.setTextAppearance(getContext(), isAactivate() ? C4558R.style.UIKitTextView_SecondaryText_Dimmed : C4558R.style.UIKitTextView_SecondaryText_Deactivate);
        }
        AvatarView avatarView = this.mAvatarView;
        float f = 1.0f;
        if (avatarView != null) {
            avatarView.setAlpha(isAactivate ? 1.0f : 0.5f);
        }
        CheckedTextView checkedTextView = this.mCheckbox;
        if (checkedTextView != null) {
            if (!isAactivate) {
                f = 0.5f;
            }
            checkedTextView.setAlpha(f);
        }
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null && this.mTxtScreenName != null) {
            int i = 0;
            InviteBuddyItem inviteBuddyItem = this.mItem;
            if (inviteBuddyItem != null) {
                IMAddrBookItem addrBookItem = inviteBuddyItem.getAddrBookItem();
                if (addrBookItem != null) {
                    int accountStatus = addrBookItem.getAccountStatus();
                    if (addrBookItem.getAccountStatus() == 1) {
                        i = C4558R.string.zm_lbl_deactivated_62074;
                    } else if (addrBookItem.getAccountStatus() == 2) {
                        i = C4558R.string.zm_lbl_terminated_62074;
                    }
                }
            }
            this.mTxtScreenName.setEllipsisText((String) charSequence, i);
        }
    }

    public void setEmail(@Nullable String str) {
        TextView textView = this.mTxtEmail;
        if (textView == null) {
            return;
        }
        if (str != null) {
            textView.setText(str);
            this.mTxtEmail.setVisibility(0);
            return;
        }
        textView.setVisibility(8);
    }

    private void setChecked(boolean z) {
        CheckedTextView checkedTextView = this.mCheckbox;
        if (checkedTextView != null) {
            checkedTextView.setChecked(z);
        }
    }

    public void setBuddyListItem(@Nullable InviteBuddyItem inviteBuddyItem, MemCache<String, Bitmap> memCache, boolean z) {
        if (inviteBuddyItem != null) {
            this.mItem = inviteBuddyItem;
            String str = this.mItem.screenName;
            if (StringUtil.isEmptyOrNull(str)) {
                str = this.mItem.email;
                setEmail(null);
            } else {
                setEmail(this.mItem.email);
            }
            setScreenName(str);
            if (this.mItem.isAddrBookItem()) {
                if (!this.mItem.isPresenceSupported) {
                    this.mPresenceStateView.setVisibility(8);
                    return;
                } else {
                    this.mPresenceStateView.setState(this.mItem.getAddrBookItem());
                }
            } else if (!this.mItem.isPresenceSupported) {
                this.mPresenceStateView.setVisibility(8);
                return;
            } else if (this.mPresenceStateView.setState(this.mItem.presence)) {
                this.mTxtScreenName.setTextColor(getResources().getColor(C4558R.color.zm_im_buddyname_online));
            } else {
                this.mTxtScreenName.setTextColor(getResources().getColor(C4558R.color.zm_im_buddyname_offline));
            }
            setChecked(this.mItem.isChecked);
            setActivateStatus();
            if (getContext() != null) {
                if (!this.mItem.isAddrBookItem() || this.mItem.getAddrBookItem() == null) {
                    this.mAvatarView.show(new ParamsBuilder().setPath(this.mItem.avatar).setName(str, this.mItem.userId));
                } else {
                    this.mAvatarView.show(this.mItem.getAddrBookItem().getAvatarParamsBuilder());
                }
            }
        }
    }

    private int getItemPresence(InviteBuddyItem inviteBuddyItem) {
        if (inviteBuddyItem.isAddrBookItem()) {
            return 0;
        }
        return inviteBuddyItem.presence;
    }
}
