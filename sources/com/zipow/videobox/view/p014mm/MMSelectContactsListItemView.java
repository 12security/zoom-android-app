package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.PresenceStateView;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSelectContactsListItemView */
public class MMSelectContactsListItemView extends LinearLayout {
    private AvatarView mAvatarView;
    private CheckedTextView mCheckbox;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsHidePresencePanel = false;
    @Nullable
    private MMSelectContactsListItem mItem;
    private PresenceStateView mPresenceStateView;
    private ProgressBar mProgressBar;
    private boolean mShowPresence = false;
    private TextView mTxtContactsDescrption;
    private TextView mTxtNotes;
    private ZMEllipsisTextView mTxtScreenName;

    public MMSelectContactsListItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMSelectContactsListItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (ZMEllipsisTextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mTxtNotes = (TextView) findViewById(C4558R.C4560id.txtEmail);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.progressBarLoading);
        this.mCheckbox = (CheckedTextView) findViewById(C4558R.C4560id.check);
        this.mPresenceStateView = (PresenceStateView) findViewById(C4558R.C4560id.presenceStateView);
        this.mTxtContactsDescrption = (TextView) findViewById(C4558R.C4560id.txtContactsDescrption);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_mm_select_contacts_list_item, this);
    }

    private boolean isAactivate() {
        MMSelectContactsListItem mMSelectContactsListItem = this.mItem;
        if (mMSelectContactsListItem == null) {
            return true;
        }
        IMAddrBookItem addrBookItem = mMSelectContactsListItem.getAddrBookItem();
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
        TextView textView = this.mTxtNotes;
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

    public void setContactsDesc(String str) {
        this.mTxtContactsDescrption.setText(str);
        this.mTxtContactsDescrption.setVisibility(StringUtil.isEmptyOrNull(str) ? 8 : 0);
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null && this.mTxtScreenName != null) {
            int i = 0;
            MMSelectContactsListItem mMSelectContactsListItem = this.mItem;
            if (mMSelectContactsListItem != null) {
                IMAddrBookItem addrBookItem = mMSelectContactsListItem.getAddrBookItem();
                if (addrBookItem != null) {
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

    public void setNotes(@Nullable String str, boolean z) {
        if (this.mTxtNotes == null) {
            return;
        }
        if (str != null) {
            if (z) {
                this.mProgressBar.setVisibility(4);
                this.mCheckbox.setVisibility(0);
            }
            this.mTxtNotes.setText(str);
            this.mTxtNotes.setVisibility(0);
            return;
        }
        if (z) {
            this.mProgressBar.setVisibility(0);
            this.mCheckbox.setVisibility(4);
        }
        this.mTxtNotes.setVisibility(8);
    }

    private void setChecked(boolean z) {
        CheckedTextView checkedTextView = this.mCheckbox;
        if (checkedTextView != null) {
            checkedTextView.setChecked(z);
        }
    }

    public void setSlashCommand(@Nullable MMSelectContactsListItem mMSelectContactsListItem) {
        if (mMSelectContactsListItem != null) {
            this.mItem = mMSelectContactsListItem;
            IMAddrBookItem addrBookItem = this.mItem.getAddrBookItem();
            if (addrBookItem != null) {
                setContactsDesc(addrBookItem.getRobotCmdPrefix());
            }
        }
    }

    public void setContactItem(@Nullable MMSelectContactsListItem mMSelectContactsListItem, MemCache<String, Bitmap> memCache, boolean z, boolean z2, boolean z3) {
        if (mMSelectContactsListItem != null) {
            this.mItem = mMSelectContactsListItem;
            String str = this.mItem.screenName;
            String str2 = this.mItem.phoneNumber;
            if (str2 == null) {
                str2 = this.mItem.email;
            }
            if (StringUtil.isEmptyOrNull(str)) {
                setNotes(null, z3);
                str = str2;
            } else {
                if (!this.mItem.isShowNotes()) {
                    str2 = null;
                }
                setNotes(str2, z3);
            }
            if (z2 && !StringUtil.isEmptyOrNull(this.mItem.email)) {
                setNotes(this.mItem.email, z3);
            }
            setScreenName(str);
            setChecked(this.mItem.isChecked());
            loadPresenceStatus();
            setActivateStatus();
            if (getContext() != null) {
                if (!mMSelectContactsListItem.isAddrBookItem() || mMSelectContactsListItem.getAddrBookItem() == null) {
                    ParamsBuilder path = new ParamsBuilder().setName(str, this.mItem.getBuddyJid()).setPath(this.mItem.getAvatar());
                    if (this.mItem.localContact != null && this.mItem.localContact.isZoomRoomContact()) {
                        path.setResource(C4558R.C4559drawable.zm_room_icon, this.mItem.getBuddyJid());
                    }
                    this.mAvatarView.show(path);
                } else {
                    this.mAvatarView.show(mMSelectContactsListItem.getAddrBookItem().getAvatarParamsBuilder());
                }
            }
        }
    }

    public void setAvatar(int i) {
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.show(new ParamsBuilder().setResource(i, null));
        }
    }

    private void loadPresenceStatus() {
        if (this.mIsHidePresencePanel) {
            this.mPresenceStateView.setVisibility(8);
        } else if (PTApp.getInstance().getZoomMessenger() == null) {
            this.mPresenceStateView.setVisibility(8);
        } else {
            MMSelectContactsListItem mMSelectContactsListItem = this.mItem;
            if (mMSelectContactsListItem != null) {
                IMAddrBookItem addrBookItem = mMSelectContactsListItem.getAddrBookItem();
                if (addrBookItem != null && this.mShowPresence) {
                    this.mPresenceStateView.setState(addrBookItem);
                }
            }
        }
    }

    public void setCheckDisabled(boolean z) {
        this.mCheckbox.setEnabled(!z);
    }

    public void setCheckVisible(boolean z) {
        this.mCheckbox.setVisibility(z ? 0 : 8);
    }

    public void setShowPresence(boolean z) {
        this.mShowPresence = z;
        loadPresenceStatus();
    }

    public void setHidePresencePanel(boolean z) {
        this.mIsHidePresencePanel = z;
        if (z) {
            this.mPresenceStateView.setVisibility(8);
        } else {
            this.mPresenceStateView.setVisibility(0);
        }
    }
}
