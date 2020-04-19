package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickStatusImageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageCallView */
public class MessageCallView extends AbsMessageView {
    protected AvatarView mAvatarView;
    protected ImageView mImgCallType;
    private ImageView mImgStarred;
    protected ImageView mImgStatus;
    protected MMMessageItem mMessageItem;
    protected View mPanelMessage;
    protected ProgressBar mProgressBar;
    protected TextView mTxtMessage;
    protected TextView mTxtScreenName;

    /* access modifiers changed from: protected */
    @Nullable
    public Drawable getMesageBackgroudDrawable() {
        return null;
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public MessageCallView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageCallView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
        this.mImgStatus = (ImageView) findViewById(C4558R.C4560id.imgStatus);
        this.mPanelMessage = findViewById(C4558R.C4560id.panelMessage);
        this.mImgCallType = (ImageView) findViewById(C4558R.C4560id.imgCallType);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.progressBar1);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        setStatusImage(false, 0);
        View view = this.mPanelMessage;
        if (view != null) {
            view.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnShowContextMenuListener onShowContextMenuListener = MessageCallView.this.getOnShowContextMenuListener();
                    if (onShowContextMenuListener != null) {
                        return onShowContextMenuListener.onShowContextMenu(view, MessageCallView.this.mMessageItem);
                    }
                    return false;
                }
            });
            this.mPanelMessage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessageCallView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessageCallView.this.mMessageItem);
                    }
                }
            });
        }
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickStatusImageListener onClickStatusImageListener = MessageCallView.this.getOnClickStatusImageListener();
                    if (onClickStatusImageListener != null) {
                        onClickStatusImageListener.onClickStatusImage(MessageCallView.this.mMessageItem);
                    }
                }
            });
        }
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessageCallView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessageCallView.this.mMessageItem);
                    }
                }
            });
            this.mAvatarView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnLongClickAvatarListener onLongClickAvatarListener = MessageCallView.this.getOnLongClickAvatarListener();
                    if (onLongClickAvatarListener != null) {
                        return onLongClickAvatarListener.onLongClickAvatar(MessageCallView.this.mMessageItem);
                    }
                    return false;
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_call_receive, this);
    }

    public void changeAvatar(boolean z) {
        if (z) {
            LayoutParams layoutParams = (LayoutParams) this.mAvatarView.getLayoutParams();
            layoutParams.width = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.height = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 16.0f);
            this.mAvatarView.setLayoutParams(layoutParams);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) this.mAvatarView.getLayoutParams();
        layoutParams2.width = UIUtil.dip2px(getContext(), 40.0f);
        layoutParams2.height = UIUtil.dip2px(getContext(), 40.0f);
        this.mAvatarView.setLayoutParams(layoutParams2);
    }

    public void setMessage(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            TextView textView = this.mTxtMessage;
            if (textView != null) {
                textView.setText(charSequence);
            }
        }
    }

    public void setStatusImage(boolean z, int i) {
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
            this.mImgStatus.setImageResource(i);
        }
    }

    public void setScreenName(@Nullable String str) {
        if (str != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }

    public void setCallTypeImage(int i) {
        ImageView imageView = this.mImgCallType;
        if (imageView != null) {
            imageView.setImageResource(i);
        }
    }

    private void updateChatMsgBackground() {
        if (VERSION.SDK_INT < 16) {
            this.mPanelMessage.setBackgroundDrawable(getMesageBackgroudDrawable());
        } else {
            this.mPanelMessage.setBackground(getMesageBackgroudDrawable());
        }
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
        updateChatMsgBackground();
        LinearLayout linearLayout = (LinearLayout) findViewById(C4558R.C4560id.panelMsgLayout);
        if (mMMessageItem.onlyMessageShow) {
            this.mAvatarView.setVisibility(4);
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setVisibility(8);
            }
            linearLayout.setPadding(linearLayout.getPaddingLeft(), 0, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
        } else {
            linearLayout.setPadding(linearLayout.getPaddingLeft(), linearLayout.getPaddingBottom(), linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
            this.mAvatarView.setVisibility(0);
            if (this.mTxtScreenName != null && mMMessageItem.isIncomingMessage()) {
                setScreenName(mMMessageItem.fromScreenName);
                TextView textView2 = this.mTxtScreenName;
                if (textView2 != null) {
                    textView2.setVisibility(0);
                }
            } else if (this.mTxtScreenName == null || !mMMessageItem.isSendingMessage() || getContext() == null) {
                TextView textView3 = this.mTxtScreenName;
                if (textView3 != null) {
                    textView3.setVisibility(8);
                }
            } else {
                setScreenName(getContext().getString(C4558R.string.zm_lbl_content_you));
                this.mTxtScreenName.setVisibility(0);
            }
            if (!isInEditMode()) {
                String str = mMMessageItem.fromJid;
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself == null || str == null || !str.equals(myself.getJid())) {
                        myself = zoomMessenger.getBuddyWithJID(str);
                    }
                    if (mMMessageItem.fromContact == null && myself != null) {
                        mMMessageItem.fromContact = IMAddrBookItem.fromZoomBuddy(myself);
                    }
                    if (mMMessageItem.fromContact != null) {
                        AvatarView avatarView = this.mAvatarView;
                        if (avatarView != null) {
                            avatarView.show(mMMessageItem.fromContact.getAvatarParamsBuilder());
                        }
                    }
                }
            }
        }
        if (mMMessageItem.hideStarView || !mMMessageItem.isMessgeStarred) {
            this.mImgStarred.setVisibility(8);
        } else {
            this.mImgStarred.setVisibility(0);
        }
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), iArr[1] + getHeight());
    }

    public MMMessageItem getMessageItem() {
        return this.mMessageItem;
    }
}
