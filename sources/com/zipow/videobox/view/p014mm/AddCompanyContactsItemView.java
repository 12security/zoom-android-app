package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.AddCompanyContactsItemView */
/* compiled from: AddCompanyContactsItem */
class AddCompanyContactsItemView extends LinearLayout {
    private AvatarView mAvatarView;
    private CheckedTextView mCheckbox;
    @Nullable
    private AddCompanyContactsItem mItem;
    private TextView mTxtEmail;
    private TextView mTxtScreenName;

    public AddCompanyContactsItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public AddCompanyContactsItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mTxtEmail = (TextView) findViewById(C4558R.C4560id.txtEmail);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mCheckbox = (CheckedTextView) findViewById(C4558R.C4560id.check);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_add_favorite_item, this);
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(charSequence);
            }
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

    public void setAvatar(String str) {
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.show(new ParamsBuilder().setPath(str));
        }
    }

    private void setChecked(boolean z) {
        CheckedTextView checkedTextView = this.mCheckbox;
        if (checkedTextView != null) {
            checkedTextView.setChecked(z);
        }
    }

    public void setFavoriteItem(@Nullable AddCompanyContactsItem addCompanyContactsItem) {
        if (addCompanyContactsItem != null) {
            this.mItem = addCompanyContactsItem;
            String screenName = this.mItem.getScreenName();
            if (StringUtil.isEmptyOrNull(screenName)) {
                screenName = this.mItem.getEmail();
                setEmail(null);
            } else {
                setEmail(this.mItem.getEmail());
            }
            setScreenName(screenName);
            setAvatar(this.mItem.getAvatar());
            setChecked(this.mItem.isChecked());
        }
    }
}
