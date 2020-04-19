package com.zipow.videobox.view.p014mm.message;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.C0971R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zipow.videobox.util.EmojiHelper.EmojiIndex;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.ReactionEmojiContextMenuHeaderView;
import com.zipow.videobox.view.p014mm.message.ReactionContextMenuListAdapter.OnReactionConextMenuListListener;
import com.zipow.videobox.view.p014mm.sticker.CommonEmoji;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiPanelView;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiPanelView.OnCommonEmojiClickListener;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.ReactionContextMenuDialog */
public class ReactionContextMenuDialog extends BottomSheetDialogFragment implements OnRecyclerViewListener, OnClickListener, OnClickMessageListener, OnCommonEmojiClickListener, OnReactionConextMenuListListener {
    private static final String TAG = "ReactionContextMenuDialog";
    private ReactionContextMenuListAdapter adapter;
    /* access modifiers changed from: private */
    public Context context;
    private View dialogView;
    private boolean hasItemDecoration;
    /* access modifiers changed from: private */
    public ReactionEmojiContextMenuHeaderView headerView;
    private boolean isReplyStyle = false;
    /* access modifiers changed from: private */
    public OnContextMenuClickListener listener;
    /* access modifiers changed from: private */
    public View mEmojiPanelLayout;
    /* access modifiers changed from: private */
    public CommonEmojiPanelView mEmojiPanelView;
    private MMMessageItem messageItem;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public int remainRange;
    /* access modifiers changed from: private */
    public int viewHeight;
    /* access modifiers changed from: private */
    public int viewOffset;

    /* renamed from: com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$Builder */
    public static class Builder {
        /* access modifiers changed from: private */
        public ReactionContextMenuListAdapter adapter;
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public boolean hasItemDecoration = true;
        /* access modifiers changed from: private */
        public boolean isReplyStyle;
        /* access modifiers changed from: private */
        public OnContextMenuClickListener listener;
        /* access modifiers changed from: private */
        public MMMessageItem messageItem;
        /* access modifiers changed from: private */
        public int remainRange;
        /* access modifiers changed from: private */
        public int viewHeight;
        /* access modifiers changed from: private */
        public int viewOffset;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setAdapter(ReactionContextMenuListAdapter reactionContextMenuListAdapter, OnContextMenuClickListener onContextMenuClickListener) {
            this.adapter = reactionContextMenuListAdapter;
            this.listener = onContextMenuClickListener;
            return this;
        }

        public Builder setLocation(int i, int i2, int i3) {
            this.viewOffset = i;
            this.viewHeight = i2;
            this.remainRange = i3;
            return this;
        }

        public Builder setHasItemDecoration(boolean z) {
            this.hasItemDecoration = z;
            return this;
        }

        public Builder setMessageItem(MMMessageItem mMMessageItem) {
            this.messageItem = mMMessageItem;
            return this;
        }

        public Builder setReplyStyle(boolean z) {
            this.isReplyStyle = z;
            return this;
        }

        public ReactionContextMenuDialog build() {
            return ReactionContextMenuDialog.newInstance(this);
        }

        public ReactionContextMenuDialog show(FragmentManager fragmentManager) {
            ReactionContextMenuDialog build = build();
            build.show(fragmentManager);
            return build;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.message.ReactionContextMenuDialog$OnContextMenuClickListener */
    public interface OnContextMenuClickListener {
        void onContextMenuClick(View view, int i);

        void onContextMenuShowed(boolean z, int i);

        void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj);
    }

    public boolean onItemLongClick(View view, int i) {
        return false;
    }

    public void onZoomEmojiClick(EmojiIndex emojiIndex) {
    }

    public void onClick(View view) {
        if (view.getId() == C4558R.C4560id.dialog_view) {
            dismiss();
        }
    }

    public void onClickMessage(MMMessageItem mMMessageItem) {
        dismiss();
    }

    public void onCommonEmojiClick(CommonEmoji commonEmoji) {
        if (commonEmoji != null && this.mEmojiPanelView != null) {
            OnContextMenuClickListener onContextMenuClickListener = this.listener;
            if (onContextMenuClickListener != null) {
                onContextMenuClickListener.onReactionEmojiClick(null, 0, commonEmoji.getOutput(), this.messageItem);
            }
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
                this.recyclerView.setVisibility(8);
            }
        }
    }

    public void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj) {
        OnContextMenuClickListener onContextMenuClickListener = this.listener;
        if (onContextMenuClickListener != null) {
            onContextMenuClickListener.onReactionEmojiClick(null, 0, charSequence, this.messageItem);
        }
    }

    /* access modifiers changed from: private */
    public static ReactionContextMenuDialog newInstance(Builder builder) {
        ReactionContextMenuDialog reactionContextMenuDialog = new ReactionContextMenuDialog();
        reactionContextMenuDialog.setHasItemDecoration(builder.hasItemDecoration);
        reactionContextMenuDialog.setAdapter(builder.adapter);
        reactionContextMenuDialog.setListener(builder.listener);
        reactionContextMenuDialog.setContext(builder.context);
        reactionContextMenuDialog.setMessageItem(builder.messageItem);
        reactionContextMenuDialog.setReplyStyle(builder.isReplyStyle);
        reactionContextMenuDialog.setLocation(builder.viewOffset, builder.viewHeight, builder.remainRange);
        return reactionContextMenuDialog;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this.context, C4558R.style.ZMDialog_Material_Transparent);
        bottomSheetDialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                try {
                    final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                    FrameLayout frameLayout = (FrameLayout) bottomSheetDialog.findViewById(C0971R.C0973id.design_bottom_sheet);
                    if (frameLayout != null) {
                        BottomSheetBehavior.from(frameLayout).setState(3);
                        BottomSheetBehavior.from(frameLayout).setSkipCollapsed(true);
                        BottomSheetBehavior.from(frameLayout).setBottomSheetCallback(new BottomSheetCallback() {
                            public void onStateChanged(@NonNull View view, int i) {
                                if (i == 5) {
                                    bottomSheetDialog.dismiss();
                                }
                            }

                            public void onSlide(@NonNull View view, float f) {
                                if (ReactionContextMenuDialog.this.headerView != null) {
                                    if (((double) f) == 1.0d) {
                                        ReactionContextMenuDialog.this.headerView.setVisibility(0);
                                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                                        alphaAnimation.setDuration(300);
                                        ReactionContextMenuDialog.this.headerView.startAnimation(alphaAnimation);
                                    } else if (ReactionContextMenuDialog.this.headerView.getVisibility() != 4) {
                                        ReactionContextMenuDialog.this.headerView.clearAnimation();
                                        ReactionContextMenuDialog.this.headerView.setVisibility(4);
                                    }
                                }
                            }
                        });
                    }
                } catch (Exception unused) {
                }
            }
        });
        return bottomSheetDialog;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C4558R.layout.zm_reaction_context_menu_dialog, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.adapter.setShowReplyStyle(this.isReplyStyle);
        this.adapter.setOnReactionConextMenuListListener(this);
        this.dialogView = view.findViewById(C4558R.C4560id.dialog_view);
        this.dialogView.setOnClickListener(this);
        this.headerView = (ReactionEmojiContextMenuHeaderView) view.findViewById(C4558R.C4560id.header_view);
        this.mEmojiPanelLayout = view.findViewById(C4558R.C4560id.emoji_panel_layout);
        this.recyclerView = (RecyclerView) view.findViewById(C4558R.C4560id.menu_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.adapter);
        if (this.hasItemDecoration) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.context, 1);
            dividerItemDecoration.setDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_divider_line_decoration));
            this.recyclerView.addItemDecoration(dividerItemDecoration);
        }
        boolean hasHeader = this.adapter.hasHeader();
        this.headerView.setVisibility(hasHeader ? 0 : 8);
        if (hasHeader) {
            MMMessageItem mMMessageItem = this.messageItem;
            if (mMMessageItem != null && mMMessageItem.isComment) {
                LayoutParams layoutParams = (LayoutParams) this.headerView.getLayoutParams();
                if (!this.isReplyStyle) {
                    layoutParams.setMarginStart(UIUtil.dip2px(this.context, 48.0f));
                }
            }
            this.headerView.bindData(this.messageItem, this.isReplyStyle, this);
            this.mEmojiPanelLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    int i;
                    if (VERSION.SDK_INT >= 16) {
                        ReactionContextMenuDialog.this.mEmojiPanelLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        ReactionContextMenuDialog.this.mEmojiPanelLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    LayoutParams layoutParams = (LayoutParams) ReactionContextMenuDialog.this.headerView.getLayoutParams();
                    int measuredHeight = ReactionContextMenuDialog.this.headerView.getMeasuredHeight();
                    int measuredHeight2 = ReactionContextMenuDialog.this.recyclerView.getMeasuredHeight();
                    int dip2px = UIUtil.dip2px(ReactionContextMenuDialog.this.context, 270.0f);
                    if (measuredHeight2 < dip2px) {
                        measuredHeight2 = dip2px;
                    }
                    int displayHeight = UIUtil.getDisplayHeight(ReactionContextMenuDialog.this.context);
                    int statusBarHeight = UIUtil.getStatusBarHeight(ReactionContextMenuDialog.this.context);
                    int i2 = measuredHeight2 + measuredHeight;
                    if (ReactionContextMenuDialog.this.viewOffset > 0) {
                        displayHeight -= ReactionContextMenuDialog.this.viewOffset;
                    }
                    if (displayHeight > i2) {
                        layoutParams.topMargin = ReactionContextMenuDialog.this.viewOffset - statusBarHeight;
                        ReactionContextMenuDialog.this.headerView.setLayoutParams(layoutParams);
                        return;
                    }
                    boolean z = true;
                    if (ReactionContextMenuDialog.this.viewHeight > measuredHeight) {
                        i = (ReactionContextMenuDialog.this.viewOffset + ReactionContextMenuDialog.this.viewHeight) - ReactionContextMenuDialog.this.recyclerView.getTop();
                        if (ReactionContextMenuDialog.this.remainRange < i2) {
                            z = false;
                        }
                    } else if (ReactionContextMenuDialog.this.viewOffset < 0) {
                        i = ((ReactionContextMenuDialog.this.viewOffset + ReactionContextMenuDialog.this.viewHeight) - ReactionContextMenuDialog.this.recyclerView.getTop()) + layoutParams.bottomMargin;
                        if (ReactionContextMenuDialog.this.remainRange < i2 + layoutParams.topMargin) {
                            z = false;
                        }
                    } else {
                        i = i2 - displayHeight;
                        if (ReactionContextMenuDialog.this.remainRange < i2 + layoutParams.topMargin) {
                            z = false;
                        }
                    }
                    layoutParams.topMargin = (ReactionContextMenuDialog.this.viewOffset - i) - statusBarHeight;
                    ReactionContextMenuDialog.this.headerView.setLayoutParams(layoutParams);
                    if (ReactionContextMenuDialog.this.listener != null) {
                        ReactionContextMenuDialog.this.listener.onContextMenuShowed(z, i);
                    }
                }
            });
        }
    }

    public void show(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null && !fragmentManager.isStateSaved()) {
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            Fragment findFragmentByTag = fragmentManager.findFragmentByTag(TAG);
            if (findFragmentByTag != null) {
                beginTransaction.remove(findFragmentByTag);
            }
            beginTransaction.addToBackStack(null);
            try {
                show(beginTransaction, TAG);
            } catch (Exception unused) {
            }
        }
    }

    private void showEmojiPanelView() {
        View view = getView();
        if (view != null) {
            ViewStub viewStub = (ViewStub) view.findViewById(C4558R.C4560id.emoji_panel_view_stub);
            viewStub.setOnInflateListener(new OnInflateListener() {
                public void onInflate(ViewStub viewStub, View view) {
                    ReactionContextMenuDialog.this.mEmojiPanelView = (CommonEmojiPanelView) view.findViewById(C4558R.C4560id.reaction_emoji_panel_view);
                }
            });
            viewStub.inflate();
        }
    }

    public void onDetach() {
        ReactionContextMenuListAdapter reactionContextMenuListAdapter = this.adapter;
        if (reactionContextMenuListAdapter != null && reactionContextMenuListAdapter.hasHeader()) {
            OnContextMenuClickListener onContextMenuClickListener = this.listener;
            if (onContextMenuClickListener != null) {
                onContextMenuClickListener.onContextMenuShowed(false, 0);
            }
        }
        super.onDetach();
    }

    public void dismiss() {
        if (isAdded() && !isStateSaved()) {
            ReactionEmojiContextMenuHeaderView reactionEmojiContextMenuHeaderView = this.headerView;
            if (reactionEmojiContextMenuHeaderView != null) {
                reactionEmojiContextMenuHeaderView.setVisibility(4);
            }
            try {
                super.dismissAllowingStateLoss();
            } catch (Exception unused) {
            }
        }
    }

    private void setAdapter(ReactionContextMenuListAdapter reactionContextMenuListAdapter) {
        this.adapter = reactionContextMenuListAdapter;
    }

    private void setListener(OnContextMenuClickListener onContextMenuClickListener) {
        this.listener = onContextMenuClickListener;
    }

    private void setContext(Context context2) {
        this.context = context2;
    }

    private void setHasItemDecoration(boolean z) {
        this.hasItemDecoration = z;
    }

    public void setLocation(int i, int i2, int i3) {
        this.viewOffset = i;
        this.viewHeight = i2;
        this.remainRange = i3;
    }

    public void setMessageItem(MMMessageItem mMMessageItem) {
        this.messageItem = mMMessageItem;
    }

    public void setReplyStyle(boolean z) {
        this.isReplyStyle = z;
    }

    public static Builder builder(Context context2) {
        return new Builder(context2);
    }

    public void onItemClick(View view, int i) {
        OnContextMenuClickListener onContextMenuClickListener = this.listener;
        if (onContextMenuClickListener != null) {
            onContextMenuClickListener.onContextMenuClick(view, i);
        }
        dismiss();
    }
}
