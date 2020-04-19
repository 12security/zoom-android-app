package com.zipow.videobox.view.sip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class ListCoverView extends FrameLayout {
    public static final int ANIM_DUCATION = 300;
    private static final String TAG = "ListCoverView";
    @Nullable
    private AnimObject mAnimTarget;
    private ObjectAnimator mAnimator;
    private int mCollapsedHeight = 0;
    protected View mContentContainerView;
    protected View mCoverContentView;
    private ExpandListener mExpandListener;
    /* access modifiers changed from: private */
    public int mExpandedHeight = 0;
    private int mExtraTop = 0;
    private int mHideAlpha = 0;
    protected View mListView;
    /* access modifiers changed from: private */
    public int mNeedPadding = 0;
    /* access modifiers changed from: private */
    public int mOriginalListViewMarginTop = 0;
    protected View mSelectListItemView;
    protected boolean mShow = false;
    private int mShowAlpha = 0;

    private class AnimItem {
        public int coverAlpha;
        public int extraTranslationY;
        public int itemHeight;
        public int listMargin;
        public int originalListViewMargin;
        public int translationY;

        private AnimItem() {
        }
    }

    private class AnimObject {
        @Nullable
        private View coverContentView;
        private View coverView;
        /* access modifiers changed from: private */
        @Nullable
        public View itemContentView;
        private ListView listview;

        public AnimObject(View view, @Nullable View view2, @Nullable View view3, View view4) {
            this.coverView = view;
            this.coverContentView = view2;
            this.itemContentView = view3;
            this.listview = (ListView) view4;
        }

        public void setValue(@NonNull AnimItem animItem) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.listview.getLayoutParams();
            marginLayoutParams.topMargin = animItem.listMargin;
            this.listview.setLayoutParams(marginLayoutParams);
            LayoutParams layoutParams = this.itemContentView.getLayoutParams();
            layoutParams.height = animItem.itemHeight;
            this.itemContentView.setLayoutParams(layoutParams);
            this.coverView.getBackground().setAlpha(animItem.coverAlpha);
            LayoutParams layoutParams2 = this.coverContentView.getLayoutParams();
            layoutParams2.height = animItem.itemHeight;
            this.coverContentView.setTranslationY((float) animItem.translationY);
            this.coverContentView.setLayoutParams(layoutParams2);
        }
    }

    public interface ExpandListener {
        void onCollapseEnd();

        void onCollapseStart();

        void onExpandEnd();

        void onExpandStart();
    }

    public ListCoverView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ListCoverView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ListCoverView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    @TargetApi(21)
    public ListCoverView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.ListCoverView);
        this.mShowAlpha = obtainStyledAttributes.getInteger(C4558R.styleable.ListCoverView_showAlpha, 100);
        this.mHideAlpha = obtainStyledAttributes.getInteger(C4558R.styleable.ListCoverView_hideAlpha, 0);
        obtainStyledAttributes.recycle();
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            initAttrs(context, attributeSet);
        }
        setVisibility(8);
        getBackground().setAlpha(this.mHideAlpha);
        setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!ListCoverView.this.isAnimRunning()) {
                    ListCoverView.this.showCoverView(false);
                }
            }
        });
    }

    public void setExpandedHeight(int i) {
        this.mExpandedHeight = i;
    }

    public void setCollapsedHeight(int i) {
        this.mCollapsedHeight = i;
    }

    public void setShowAlpha(int i) {
        this.mShowAlpha = i;
    }

    public void setHideAlpha(int i) {
        this.mHideAlpha = i;
    }

    public void setExpandListener(ExpandListener expandListener) {
        this.mExpandListener = expandListener;
    }

    public void setViews(@NonNull View view, View view2, View view3) {
        this.mCoverContentView = view;
        this.mListView = view2;
        this.mContentContainerView = view3;
        if (view.getParent() == null) {
            addView(view);
        }
    }

    public void setSelectListItemView(View view) {
        this.mSelectListItemView = view;
    }

    public boolean isAnimRunning() {
        ObjectAnimator objectAnimator = this.mAnimator;
        return objectAnimator != null && objectAnimator.isRunning();
    }

    private void check() {
        if (this.mListView == null) {
            throw new NullPointerException("NULL Listview");
        } else if (this.mContentContainerView == null) {
            throw new NullPointerException("NULL ContentContainerView");
        } else if (this.mCoverContentView == null) {
            throw new NullPointerException("NULL CoverContentView");
        } else if (this.mSelectListItemView != null) {
            int i = this.mShowAlpha;
            int i2 = this.mExpandedHeight;
        } else {
            throw new NullPointerException("NULL SelectedItemView");
        }
    }

    public void start() {
        try {
            check();
            this.mContentContainerView.post(new Runnable() {
                public void run() {
                    ListCoverView listCoverView = ListCoverView.this;
                    listCoverView.mOriginalListViewMarginTop = ((MarginLayoutParams) listCoverView.mListView.getLayoutParams()).topMargin;
                    ListCoverView.this.caculateTop();
                    int height = ListCoverView.this.mListView.getHeight();
                    int top = ListCoverView.this.mSelectListItemView.getTop();
                    if (top < 0) {
                        ListCoverView.this.mSelectListItemView.setTop(0);
                        top = 0;
                    }
                    ListCoverView listCoverView2 = ListCoverView.this;
                    listCoverView2.mNeedPadding = listCoverView2.mExpandedHeight - (height - top);
                    ListCoverView.this.showCoverView(true);
                }
            });
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public void showCoverView(boolean z) {
        int i;
        if (this.mSelectListItemView != null) {
            this.mShow = z;
            setVisibility(0);
            int i2 = this.mCollapsedHeight;
            int i3 = this.mExpandedHeight;
            int i4 = this.mHideAlpha;
            int i5 = this.mShowAlpha;
            int i6 = this.mOriginalListViewMarginTop;
            if (this.mNeedPadding <= 0) {
                this.mNeedPadding = 0;
            }
            int i7 = (this.mNeedPadding * -1) + this.mOriginalListViewMarginTop;
            if (!z) {
                setCollapsedHeight(this.mCollapsedHeight);
                i = this.mCollapsedHeight;
                this.mOriginalListViewMarginTop = 0;
                i7 = this.mOriginalListViewMarginTop;
                if (this.mNeedPadding <= 0) {
                    this.mNeedPadding = 0;
                }
                i6 = this.mOriginalListViewMarginTop + (this.mNeedPadding * -1);
                i2 = i3;
                int i8 = i5;
                i5 = i4;
                i4 = i8;
            } else {
                i = i3;
            }
            AnimItem animItem = new AnimItem();
            animItem.itemHeight = i2;
            animItem.coverAlpha = i4;
            animItem.listMargin = i6;
            animItem.originalListViewMargin = this.mOriginalListViewMarginTop;
            animItem.extraTranslationY = this.mSelectListItemView.getTop() + this.mExtraTop;
            AnimItem animItem2 = new AnimItem();
            animItem2.itemHeight = i;
            animItem2.coverAlpha = i5;
            animItem2.listMargin = i7;
            animItem2.originalListViewMargin = this.mOriginalListViewMarginTop;
            animItem2.extraTranslationY = this.mSelectListItemView.getTop() + this.mExtraTop;
            if (this.mAnimator == null) {
                AnimObject animObject = new AnimObject(this, this.mCoverContentView, this.mSelectListItemView, this.mListView);
                this.mAnimTarget = animObject;
                ObjectAnimator ofObject = ObjectAnimator.ofObject(this.mAnimTarget, "value", new TypeEvaluator<AnimItem>() {
                    @NonNull
                    public AnimItem evaluate(float f, @NonNull AnimItem animItem, @NonNull AnimItem animItem2) {
                        AnimItem animItem3 = new AnimItem();
                        animItem3.itemHeight = (int) (((float) animItem.itemHeight) + (((float) (animItem2.itemHeight - animItem.itemHeight)) * f));
                        animItem3.coverAlpha = (int) (((float) animItem.coverAlpha) + (((float) (animItem2.coverAlpha - animItem.coverAlpha)) * f));
                        animItem3.listMargin = (int) (((float) animItem.listMargin) + (f * ((float) (animItem2.listMargin - animItem.listMargin))));
                        animItem3.translationY = Math.min(0, animItem3.listMargin - ListCoverView.this.mOriginalListViewMarginTop) + animItem.extraTranslationY;
                        return animItem3;
                    }
                }, new Object[]{animItem, animItem2});
                ofObject.setInterpolator(new AccelerateDecelerateInterpolator());
                ofObject.setDuration(300);
                ofObject.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                        super.onAnimationStart(animator);
                        ListCoverView.this.post(new Runnable() {
                            public void run() {
                                ListCoverView.this.onAnimationStarted();
                            }
                        });
                    }

                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        ListCoverView.this.post(new Runnable() {
                            public void run() {
                                ListCoverView.this.onAnimationEnded();
                            }
                        });
                    }
                });
                this.mAnimator = ofObject;
            } else {
                this.mAnimTarget.itemContentView = this.mSelectListItemView;
                this.mAnimator.setObjectValues(new Object[]{animItem, animItem2});
            }
            this.mAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public void caculateTop() {
        int[] iArr = new int[2];
        this.mListView.getLocationInWindow(iArr);
        int[] iArr2 = new int[2];
        this.mContentContainerView.getLocationInWindow(iArr2);
        this.mExtraTop = iArr[1] - iArr2[1];
    }

    /* access modifiers changed from: protected */
    public void onAnimationStarted() {
        ExpandListener expandListener = this.mExpandListener;
        if (expandListener == null) {
            return;
        }
        if (this.mShow) {
            expandListener.onExpandStart();
        } else {
            expandListener.onCollapseStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onAnimationEnded() {
        if (!this.mShow) {
            reset();
        }
        ExpandListener expandListener = this.mExpandListener;
        if (expandListener == null) {
            return;
        }
        if (this.mShow) {
            expandListener.onExpandEnd();
        } else {
            expandListener.onCollapseEnd();
        }
    }

    public void end() {
        ObjectAnimator objectAnimator = this.mAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mAnimator.end();
        }
    }

    public void cancel() {
        ObjectAnimator objectAnimator = this.mAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
    }

    public void dismissSmooth() {
        if (this.mShow) {
            end();
            showCoverView(false);
        }
    }

    public void dismiss() {
        end();
        if (this.mShow) {
            reset();
            ExpandListener expandListener = this.mExpandListener;
            if (expandListener != null) {
                expandListener.onCollapseEnd();
            }
        }
    }

    private void reset() {
        setVisibility(8);
        View view = this.mListView;
        if (view != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
            marginLayoutParams.topMargin = this.mOriginalListViewMarginTop;
            this.mListView.setLayoutParams(marginLayoutParams);
        }
        View view2 = this.mSelectListItemView;
        if (view2 != null) {
            LayoutParams layoutParams = view2.getLayoutParams();
            layoutParams.height = -2;
            this.mSelectListItemView.setLayoutParams(layoutParams);
        }
        this.mShow = false;
        this.mSelectListItemView = null;
    }
}
