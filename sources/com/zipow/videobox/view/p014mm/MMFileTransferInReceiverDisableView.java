package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMFileTransferInReceiverDisableView */
public class MMFileTransferInReceiverDisableView extends AbsMessageView {
    protected AvatarView mAvatarView;
    /* access modifiers changed from: private */
    public MMMessageItem mMessageItem;
    protected LinearLayout mPanelMessage;
    protected TextView mTxtMessage;
    protected TextView mTxtMessageForBigEmoji;
    protected TextView mTxtScreenName;

    public MMMessageItem getMessageItem() {
        return null;
    }

    public Rect getMessageLocationOnScreen() {
        return null;
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public MMFileTransferInReceiverDisableView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MMFileTransferInReceiverDisableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMFileTransferInReceiverDisableView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_mm_file_transfer_in_receiver_disable, this);
    }

    /* access modifiers changed from: protected */
    public void initView() {
        inflateLayout();
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mPanelMessage = (LinearLayout) findViewById(C4558R.C4560id.panel_textMessage);
        this.mTxtMessageForBigEmoji = (TextView) findViewById(C4558R.C4560id.txtMessageForBigEmoji);
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MMFileTransferInReceiverDisableView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MMFileTransferInReceiverDisableView.this.mMessageItem);
                    }
                }
            });
            this.mAvatarView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnLongClickAvatarListener onLongClickAvatarListener = MMFileTransferInReceiverDisableView.this.getOnLongClickAvatarListener();
                    if (onLongClickAvatarListener != null) {
                        return onLongClickAvatarListener.onLongClickAvatar(MMFileTransferInReceiverDisableView.this.mMessageItem);
                    }
                    return false;
                }
            });
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

    /* access modifiers changed from: protected */
    @NonNull
    public Drawable getMessageBackgroundDrawable() {
        MMMessageItem mMMessageItem = this.mMessageItem;
        if (mMMessageItem == null || !mMMessageItem.hideStarView || this.mMessageItem.messageType != 11) {
            MMChatMessageBgDrawable mMChatMessageBgDrawable = new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, true, 0, 0, 0, 0);
            return mMChatMessageBgDrawable;
        }
        MMChatMessageBgDrawable mMChatMessageBgDrawable2 = new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, false, 0, 0, 0, 0);
        return mMChatMessageBgDrawable2;
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
        if (!StringUtil.isEmptyOrNull(mMMessageItem.fromScreenName)) {
            this.mTxtMessage.setText(getResources().getString(C4558R.string.zm_msg_file_transfer_disabled_86061, new Object[]{mMMessageItem.fromScreenName}));
        }
        if (VERSION.SDK_INT < 16) {
            this.mPanelMessage.setBackgroundDrawable(getMessageBackgroundDrawable());
        } else {
            this.mPanelMessage.setBackground(getMessageBackgroundDrawable());
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(C4558R.C4560id.panelMsgLayout);
        if (mMMessageItem.onlyMessageShow) {
            this.mAvatarView.setVisibility(4);
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setVisibility(8);
            }
            linearLayout.setPadding(linearLayout.getPaddingLeft(), 0, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
            return;
        }
        linearLayout.setPadding(linearLayout.getPaddingLeft(), linearLayout.getPaddingBottom(), linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
        this.mAvatarView.setVisibility(0);
        if (this.mTxtScreenName == null || !mMMessageItem.isIncomingMessage() || !mMMessageItem.isGroupMessage) {
            TextView textView2 = this.mTxtScreenName;
            if (textView2 != null) {
                textView2.setVisibility(8);
            }
        } else {
            setScreenName(mMMessageItem.fromScreenName);
            TextView textView3 = this.mTxtScreenName;
            if (textView3 != null) {
                textView3.setVisibility(0);
            }
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
                    this.mAvatarView.show(mMMessageItem.fromContact.getAvatarParamsBuilder());
                } else {
                    this.mAvatarView.show(new ParamsBuilder().setName(mMMessageItem.fromScreenName, mMMessageItem.fromJid));
                }
            }
        }
    }
}
