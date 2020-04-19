package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import p021us.zoom.videomeetings.C4558R;

public class IMBuddyItemView extends LinearLayout {
    private AvatarView mAvatarView;
    private ImageView mImgPresence;
    @Nullable
    private IMBuddyItem mItem;
    private TextView mTxtInvited;
    private TextView mTxtScreenName;
    private TextView mTxtUnreadMessageCount;

    public IMBuddyItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public IMBuddyItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mImgPresence = (ImageView) findViewById(C4558R.C4560id.imgPresence);
        this.mTxtUnreadMessageCount = (TextView) findViewById(C4558R.C4560id.txtUnreadMessageCount);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mTxtInvited = (TextView) findViewById(C4558R.C4560id.txtInvited);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_im_buddy_item, this);
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            this.mTxtScreenName.setText(charSequence);
        }
    }

    public void setAvatar(String str, int i) {
        this.mAvatarView.show(new ParamsBuilder().setPath(str));
    }

    public void setPresence(int i) {
        if (i != 0) {
            switch (i) {
                case 2:
                    this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_status_idle);
                    ImageView imageView = this.mImgPresence;
                    imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_idle));
                    this.mTxtScreenName.setTextColor(getResources().getColor(C4558R.color.zm_im_buddyname_online));
                    return;
                case 3:
                    this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_status_dnd);
                    ImageView imageView2 = this.mImgPresence;
                    imageView2.setContentDescription(imageView2.getResources().getString(C4558R.string.zm_description_mm_presence_dnd_19903));
                    this.mTxtScreenName.setTextColor(getResources().getColor(C4558R.color.zm_im_buddyname_online));
                    return;
                case 4:
                    this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_status_dnd);
                    ImageView imageView3 = this.mImgPresence;
                    imageView3.setContentDescription(imageView3.getResources().getString(C4558R.string.zm_description_mm_presence_xa_19903));
                    this.mTxtScreenName.setTextColor(getResources().getColor(C4558R.color.zm_im_buddyname_online));
                    return;
                default:
                    this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_offline);
                    ImageView imageView4 = this.mImgPresence;
                    imageView4.setContentDescription(imageView4.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
                    this.mTxtScreenName.setTextColor(getResources().getColor(C4558R.color.zm_im_buddyname_offline));
                    return;
            }
        } else {
            this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_status_available);
            ImageView imageView5 = this.mImgPresence;
            imageView5.setContentDescription(imageView5.getResources().getString(C4558R.string.zm_description_mm_presence_available));
            this.mTxtScreenName.setTextColor(getResources().getColor(C4558R.color.zm_im_buddyname_online));
        }
    }

    public void setUnreadMessageCount(int i) {
        this.mTxtUnreadMessageCount.setVisibility(i <= 0 ? 8 : 0);
        if (i <= 99) {
            this.mTxtUnreadMessageCount.setText(String.valueOf(i));
        } else {
            this.mTxtUnreadMessageCount.setText("99+");
        }
    }

    public void setBuddyListItem(@Nullable IMBuddyItem iMBuddyItem) {
        if (iMBuddyItem != null) {
            this.mItem = iMBuddyItem;
            setScreenName(this.mItem.screenName);
            setPresence(this.mItem.presence);
            setAvatar(this.mItem.avatar, this.mItem.presence);
            setUnreadMessageCount(iMBuddyItem.unreadMessageCount);
            if (iMBuddyItem.isNoneFriend) {
                this.mTxtInvited.setVisibility(8);
                this.mImgPresence.setVisibility(8);
            } else if (iMBuddyItem.isPending) {
                this.mTxtInvited.setVisibility(0);
                this.mImgPresence.setVisibility(8);
            } else {
                this.mTxtInvited.setVisibility(8);
                this.mImgPresence.setVisibility(0);
            }
        }
    }
}
