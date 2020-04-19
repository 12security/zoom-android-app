package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.AvatarView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMChatBuddyItemView */
public class MMChatBuddyItemView extends LinearLayout {
    private AvatarView mImgAvatar;
    private View mImgRemove;
    /* access modifiers changed from: private */
    public OnClickListener mOnAvatarClickListener;
    /* access modifiers changed from: private */
    public OnClickListener mOnButtonRemoveClickListener;
    private TextView mTxtRole;
    private TextView mTxtScreenName;
    private View mViewContent;

    public MMChatBuddyItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMChatBuddyItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mImgAvatar = (AvatarView) findViewById(C4558R.C4560id.imgAvatar);
        this.mImgRemove = findViewById(C4558R.C4560id.imgRemove);
        this.mTxtRole = (TextView) findViewById(C4558R.C4560id.txtRole);
        this.mViewContent = findViewById(C4558R.C4560id.viewContent);
        this.mImgRemove.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MMChatBuddyItemView.this.mOnButtonRemoveClickListener != null) {
                    MMChatBuddyItemView.this.mOnButtonRemoveClickListener.onClick(view);
                }
            }
        });
        this.mImgAvatar.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MMChatBuddyItemView.this.mOnAvatarClickListener != null) {
                    MMChatBuddyItemView.this.mOnAvatarClickListener.onClick(view);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_mm_chat_buddy_item, this);
    }

    public void setOnButtonRemoveClickListener(OnClickListener onClickListener) {
        this.mOnButtonRemoveClickListener = onClickListener;
    }

    public void setOnAvatarClickListener(OnClickListener onClickListener) {
        this.mOnAvatarClickListener = onClickListener;
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(charSequence);
            }
        }
    }

    public void loadAvatar(Context context, @NonNull MMBuddyItem mMBuddyItem) {
        this.mImgAvatar.show(mMBuddyItem.getAvatarBuilderParams(context));
    }

    public void setRemoveEnabled(boolean z) {
        View view = this.mImgRemove;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
    }

    public void setContentDes(CharSequence charSequence, boolean z) {
        View view = this.mViewContent;
        if (view != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(charSequence);
            sb.append(z ? getContext().getResources().getString(C4558R.string.zm_mm_lbl_group_owner) : "");
            view.setContentDescription(sb.toString());
        }
    }

    public boolean isRemoveEnabled() {
        View view = this.mImgRemove;
        boolean z = false;
        if (view == null) {
            return false;
        }
        if (view.getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    public void setAdditionalInfo(boolean z, boolean z2) {
        if (z) {
            this.mTxtRole.setText(C4558R.string.zm_mm_lbl_group_owner);
            this.mTxtRole.setVisibility(0);
        } else if (z2) {
            this.mTxtRole.setText(getResources().getString(C4558R.string.zm_lbl_deactivated_62074, new Object[]{""}));
            this.mTxtRole.setVisibility(0);
        } else {
            this.mTxtRole.setVisibility(4);
        }
    }
}
