package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.zipow.videobox.util.EmojiHelper.EmojiIndex;
import com.zipow.videobox.view.p014mm.ReactionEmojiSampleView.OnReactionEmojiSampleListener;
import com.zipow.videobox.view.p014mm.sticker.CommonEmoji;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiPanelView;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiPanelView.OnCommonEmojiClickListener;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDialog */
public class ReactionEmojiDialog extends Dialog implements DialogInterface, OnClickListener, OnReactionEmojiSampleListener, OnCommonEmojiClickListener {
    private static final String TAG = "ReactionEmojiDialog";
    /* access modifiers changed from: private */
    public DialogHolder holder;
    /* access modifiers changed from: private */
    public View mBlank;
    protected Context mContext;
    /* access modifiers changed from: private */
    public View mEmojiPanelLayout;
    /* access modifiers changed from: private */
    public CommonEmojiPanelView mEmojiPanelView;
    /* access modifiers changed from: private */
    public FrameLayout mMessageView;
    /* access modifiers changed from: private */
    public ReactionEmojiSampleView mReactionEmojiSampleView;
    private View mViewWrapper;
    /* access modifiers changed from: private */
    public int mWindowOffset;

    /* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDialog$Builder */
    public static class Builder {
        private DialogHolder holder;

        public Builder(Context context) {
            this.holder = new DialogHolder(context);
        }

        public ReactionEmojiDialog create() {
            DialogHolder dialogHolder = this.holder;
            ReactionEmojiDialog reactionEmojiDialog = new ReactionEmojiDialog(dialogHolder, dialogHolder.getTheme());
            this.holder.setDialog(reactionEmojiDialog);
            reactionEmojiDialog.setCancelable(this.holder.isCancelable());
            return reactionEmojiDialog;
        }

        public Builder setAnchor(int i, int i2, int i3, int i4, OnReactionEmojiListener onReactionEmojiListener) {
            this.holder.setAnchor(i, i2, i3, i4, onReactionEmojiListener);
            return this;
        }

        public Builder setData(Object obj) {
            this.holder.setData(obj);
            return this;
        }

        public void show() {
            if (this.holder.getDialog() == null) {
                create();
            }
            this.holder.getDialog().show();
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDialog$OnReactionEmojiListener */
    public interface OnReactionEmojiListener {
        void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj);

        void onReactionEmojiDialogShowed(boolean z, int i);
    }

    public void onZoomEmojiClick(EmojiIndex emojiIndex) {
    }

    public ReactionEmojiDialog(DialogHolder dialogHolder) {
        this(dialogHolder, C4558R.style.ZMDialog_Material_Transparent);
    }

    public ReactionEmojiDialog(DialogHolder dialogHolder, int i) {
        super(dialogHolder.getContext(), i);
        this.mWindowOffset = 0;
        this.holder = dialogHolder;
        this.mContext = dialogHolder.getContext();
    }

    public ReactionEmojiDialog(Context context, int i) {
        super(context, i);
        this.mWindowOffset = 0;
        this.holder = new DialogHolder(context);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getWindow() != null) {
            getWindow().requestFeature(1);
        }
        super.setCancelable(this.holder.isCancelable());
        setContentView(C4558R.layout.zm_reaction_emoji_dialog);
        this.mViewWrapper = findViewById(C4558R.C4560id.floating_view_wrapper);
        this.mViewWrapper.setOnClickListener(this);
        this.mEmojiPanelLayout = findViewById(C4558R.C4560id.emoji_panel_layout);
        this.mBlank = findViewById(C4558R.C4560id.blank);
        this.mMessageView = (FrameLayout) findViewById(C4558R.C4560id.message_view);
        this.mMessageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ReactionEmojiDialog.this.dismiss();
            }
        });
        if (this.holder.getData() != null && (this.holder.getData() instanceof MMMessageItem)) {
            MMMessageItem mMMessageItem = (MMMessageItem) this.holder.getData();
            AbsMessageView createView = MMMessageItem.createView(getContext(), mMMessageItem.messageType);
            if (createView != null) {
                LayoutParams layoutParams = new LayoutParams(-1, -2);
                createView.setMessageItem(mMMessageItem, true);
                this.mMessageView.addView(createView, layoutParams);
            }
        }
        this.mReactionEmojiSampleView = (ReactionEmojiSampleView) findViewById(C4558R.C4560id.reaction_emoji_sample_view);
        this.mReactionEmojiSampleView.bindData(this.holder.getData());
        this.mReactionEmojiSampleView.setOnReactionEmojiSampleListener(this);
        this.mEmojiPanelLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int i;
                int i2;
                int i3;
                if (VERSION.SDK_INT >= 16) {
                    ReactionEmojiDialog.this.mEmojiPanelLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    ReactionEmojiDialog.this.mEmojiPanelLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                int titleHeight = ReactionEmojiDialog.this.holder.getTitleHeight();
                int anchorOffsetY = ReactionEmojiDialog.this.holder.getAnchorOffsetY();
                int remainRange = ReactionEmojiDialog.this.holder.getRemainRange();
                int measuredHeight = ReactionEmojiDialog.this.mMessageView.getMeasuredHeight();
                int measuredHeight2 = ReactionEmojiDialog.this.mReactionEmojiSampleView.getMeasuredHeight();
                int dip2px = UIUtil.dip2px(ReactionEmojiDialog.this.mContext, 270.0f);
                if (dip2px >= measuredHeight2) {
                    measuredHeight2 = dip2px;
                }
                int displayHeight = UIUtil.getDisplayHeight(ReactionEmojiDialog.this.mContext);
                int statusBarHeight = UIUtil.getStatusBarHeight(ReactionEmojiDialog.this.mContext);
                int i4 = measuredHeight2 + measuredHeight;
                int i5 = anchorOffsetY > 0 ? displayHeight - anchorOffsetY : displayHeight;
                boolean z = false;
                boolean z2 = true;
                if (i5 > i4) {
                    if (ReactionEmojiDialog.this.holder.getAnchorHeight() + anchorOffsetY < titleHeight + statusBarHeight) {
                        int i6 = (anchorOffsetY - statusBarHeight) - titleHeight;
                        i = titleHeight;
                        i2 = i6;
                        z = true;
                    } else {
                        i = anchorOffsetY - statusBarHeight;
                        i2 = 0;
                        z = true;
                        z2 = false;
                    }
                } else if (ReactionEmojiDialog.this.holder.getAnchorHeight() + anchorOffsetY < titleHeight + statusBarHeight) {
                    if (measuredHeight < displayHeight / 2) {
                        i3 = (anchorOffsetY - statusBarHeight) - titleHeight;
                    } else {
                        int anchorHeight = ((anchorOffsetY + ReactionEmojiDialog.this.holder.getAnchorHeight()) - statusBarHeight) - titleHeight;
                        titleHeight += -ReactionEmojiDialog.this.holder.getAnchorHeight();
                        i3 = anchorHeight;
                    }
                    i = titleHeight;
                    i2 = i3;
                    z = true;
                } else if (ReactionEmojiDialog.this.holder.getAnchorHeight() + anchorOffsetY < displayHeight - measuredHeight2) {
                    i = anchorOffsetY - statusBarHeight;
                    i2 = 0;
                    z = true;
                    z2 = false;
                } else {
                    if (remainRange >= measuredHeight + anchorOffsetY + measuredHeight2) {
                        z = true;
                    }
                    i2 = i4 - i5;
                    i = (anchorOffsetY - i2) - statusBarHeight;
                }
                if (z2 && ReactionEmojiDialog.this.holder.getReactionEmojiListener() != null) {
                    ReactionEmojiDialog.this.holder.getReactionEmojiListener().onReactionEmojiDialogShowed(z, i2);
                }
                ReactionEmojiDialog.this.mWindowOffset = i;
                ReactionEmojiDialog.this.mReactionEmojiSampleView.setWindowOffset(ReactionEmojiDialog.this.mWindowOffset);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ReactionEmojiDialog.this.mMessageView.getLayoutParams();
                layoutParams.topMargin = i;
                ReactionEmojiDialog.this.mMessageView.setLayoutParams(layoutParams);
                Window window = ReactionEmojiDialog.this.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams attributes = window.getAttributes();
                    attributes.gravity = 48;
                    attributes.width = -1;
                    attributes.height = -2;
                    window.setAttributes(attributes);
                }
            }
        });
    }

    private void showEmojiPanelView() {
        ViewStub viewStub = (ViewStub) findViewById(C4558R.C4560id.emoji_panel_view_stub);
        viewStub.setOnInflateListener(new OnInflateListener() {
            public void onInflate(ViewStub viewStub, View view) {
                ReactionEmojiDialog.this.mEmojiPanelView = (CommonEmojiPanelView) view.findViewById(C4558R.C4560id.reaction_emoji_panel_view);
                ReactionEmojiDialog.this.mBlank.setVisibility(0);
            }
        });
        viewStub.inflate();
    }

    public void onDetachedFromWindow() {
        DialogHolder dialogHolder = this.holder;
        if (!(dialogHolder == null || dialogHolder.getReactionEmojiListener() == null)) {
            this.holder.getReactionEmojiListener().onReactionEmojiDialogShowed(false, 0);
        }
        super.onDetachedFromWindow();
    }

    public void onClick(View view) {
        if (view.getId() == C4558R.C4560id.floating_view_wrapper) {
            dismiss();
        }
    }

    public void onMoreEmojiClick(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            showEmojiPanelView();
            CommonEmojiPanelView commonEmojiPanelView = this.mEmojiPanelView;
            if (commonEmojiPanelView != null) {
                commonEmojiPanelView.setOnCommonEmojiClickListener(this);
                Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), C4558R.anim.zm_slide_in_bottom);
                loadAnimation.setDuration(500);
                loadAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                this.mEmojiPanelView.startAnimation(loadAnimation);
                this.mReactionEmojiSampleView.setVisibility(8);
            }
        }
    }

    public void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj) {
        DialogHolder dialogHolder = this.holder;
        if (dialogHolder != null && dialogHolder.getReactionEmojiListener() != null) {
            this.holder.getReactionEmojiListener().onReactionEmojiClick(null, 0, charSequence, this.holder.getData());
        }
    }

    public void onCommonEmojiClick(CommonEmoji commonEmoji) {
        if (!(commonEmoji == null || this.mEmojiPanelView == null)) {
            DialogHolder dialogHolder = this.holder;
            if (!(dialogHolder == null || dialogHolder.getReactionEmojiListener() == null)) {
                this.holder.getReactionEmojiListener().onReactionEmojiClick(null, 0, commonEmoji.getOutput(), this.holder.getData());
            }
        }
    }
}
