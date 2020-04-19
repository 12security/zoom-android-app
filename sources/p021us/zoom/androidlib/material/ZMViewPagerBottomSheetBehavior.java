package p021us.zoom.androidlib.material;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import androidx.customview.widget.ViewDragHelper.Callback;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.LayoutParams;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.material.ZMViewPagerBottomSheetBehavior */
public class ZMViewPagerBottomSheetBehavior<V extends View> extends Behavior<V> {
    private static final float HIDE_FRICTION = 0.1f;
    private static final float HIDE_THRESHOLD = 0.5f;
    public static final int PEEK_HEIGHT_AUTO = -1;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_SETTLING = 2;
    int mActivePointerId;
    private BottomSheetCallback mCallback;
    private final Callback mDragCallback = new Callback() {
        public boolean tryCaptureView(View view, int i) {
            boolean z = true;
            if (ZMViewPagerBottomSheetBehavior.this.mState == 1 || ZMViewPagerBottomSheetBehavior.this.mTouchingScrollingChild) {
                return false;
            }
            if (ZMViewPagerBottomSheetBehavior.this.mState == 3 && ZMViewPagerBottomSheetBehavior.this.mActivePointerId == i) {
                View view2 = (View) ZMViewPagerBottomSheetBehavior.this.mNestedScrollingChildRef.get();
                if (view2 != null && view2.canScrollVertically(-1)) {
                    return false;
                }
            }
            if (ZMViewPagerBottomSheetBehavior.this.mViewRef == null || ZMViewPagerBottomSheetBehavior.this.mViewRef.get() != view) {
                z = false;
            }
            return z;
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            ZMViewPagerBottomSheetBehavior.this.dispatchOnSlide(i2);
        }

        public void onViewDragStateChanged(int i) {
            if (i == 1) {
                ZMViewPagerBottomSheetBehavior.this.setStateInternal(1);
            }
        }

        public void onViewReleased(View view, float f, float f2) {
            int i;
            int i2 = 3;
            if (f2 < 0.0f) {
                i = ZMViewPagerBottomSheetBehavior.this.mMinOffset;
            } else if (ZMViewPagerBottomSheetBehavior.this.mHideable && ZMViewPagerBottomSheetBehavior.this.shouldHide(view, f2)) {
                i = ZMViewPagerBottomSheetBehavior.this.mParentHeight;
                i2 = 5;
            } else if (f2 == 0.0f) {
                int top = view.getTop();
                if (Math.abs(top - ZMViewPagerBottomSheetBehavior.this.mMinOffset) < Math.abs(top - ZMViewPagerBottomSheetBehavior.this.mMaxOffset)) {
                    i = ZMViewPagerBottomSheetBehavior.this.mMinOffset;
                } else {
                    i = ZMViewPagerBottomSheetBehavior.this.mMaxOffset;
                    i2 = 4;
                }
            } else {
                i = ZMViewPagerBottomSheetBehavior.this.mMaxOffset;
                i2 = 4;
            }
            if (ZMViewPagerBottomSheetBehavior.this.mViewDragHelper.settleCapturedViewAt(view.getLeft(), i)) {
                ZMViewPagerBottomSheetBehavior.this.setStateInternal(2);
                ViewCompat.postOnAnimation(view, new SettleRunnable(view, i2));
                return;
            }
            ZMViewPagerBottomSheetBehavior.this.setStateInternal(i2);
        }

        public int clampViewPositionVertical(View view, int i, int i2) {
            return MathUtils.clamp(i, ZMViewPagerBottomSheetBehavior.this.mMinOffset, ZMViewPagerBottomSheetBehavior.this.mHideable ? ZMViewPagerBottomSheetBehavior.this.mParentHeight : ZMViewPagerBottomSheetBehavior.this.mMaxOffset);
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            return view.getLeft();
        }

        public int getViewVerticalDragRange(View view) {
            if (ZMViewPagerBottomSheetBehavior.this.mHideable) {
                return ZMViewPagerBottomSheetBehavior.this.mParentHeight - ZMViewPagerBottomSheetBehavior.this.mMinOffset;
            }
            return ZMViewPagerBottomSheetBehavior.this.mMaxOffset - ZMViewPagerBottomSheetBehavior.this.mMinOffset;
        }
    };
    boolean mHideable;
    private boolean mIgnoreEvents;
    private int mInitialY;
    private int mLastNestedScrollDy;
    int mMaxOffset;
    private float mMaximumVelocity;
    int mMinOffset;
    private boolean mNestedScrolled;
    WeakReference<View> mNestedScrollingChildRef;
    int mParentHeight;
    private int mPeekHeight;
    private boolean mPeekHeightAuto;
    private int mPeekHeightMin;
    private boolean mSkipCollapsed;
    int mState = 4;
    boolean mTouchingScrollingChild;
    private VelocityTracker mVelocityTracker;
    ViewDragHelper mViewDragHelper;
    WeakReference<V> mViewRef;

    /* renamed from: us.zoom.androidlib.material.ZMViewPagerBottomSheetBehavior$BottomSheetCallback */
    public static abstract class BottomSheetCallback {
        public abstract void onSlide(@NonNull View view, float f);

        public abstract void onStateChanged(@NonNull View view, int i);
    }

    /* renamed from: us.zoom.androidlib.material.ZMViewPagerBottomSheetBehavior$SavedState */
    protected static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, (ClassLoader) null);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        final int state;

        public SavedState(Parcel parcel) {
            this(parcel, (ClassLoader) null);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.state = parcel.readInt();
        }

        public SavedState(Parcelable parcelable, int i) {
            super(parcelable);
            this.state = i;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.state);
        }
    }

    /* renamed from: us.zoom.androidlib.material.ZMViewPagerBottomSheetBehavior$SettleRunnable */
    private class SettleRunnable implements Runnable {
        private final int mTargetState;
        private final View mView;

        SettleRunnable(View view, int i) {
            this.mView = view;
            this.mTargetState = i;
        }

        public void run() {
            if (ZMViewPagerBottomSheetBehavior.this.mViewDragHelper == null || !ZMViewPagerBottomSheetBehavior.this.mViewDragHelper.continueSettling(true)) {
                ZMViewPagerBottomSheetBehavior.this.setStateInternal(this.mTargetState);
            } else {
                ViewCompat.postOnAnimation(this.mView, this);
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: us.zoom.androidlib.material.ZMViewPagerBottomSheetBehavior$State */
    public @interface State {
    }

    public ZMViewPagerBottomSheetBehavior() {
    }

    public ZMViewPagerBottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.BottomSheetBehavior_Layout);
        TypedValue peekValue = obtainStyledAttributes.peekValue(C4409R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (peekValue == null || peekValue.data != -1) {
            setPeekHeight(obtainStyledAttributes.getDimensionPixelSize(C4409R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        } else {
            setPeekHeight(peekValue.data);
        }
        setHideable(obtainStyledAttributes.getBoolean(C4409R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        setSkipCollapsed(obtainStyledAttributes.getBoolean(C4409R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        obtainStyledAttributes.recycle();
        this.mMaximumVelocity = (float) ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v) {
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v), this.mState);
    }

    public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v, Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v, savedState.getSuperState());
        if (savedState.state == 1 || savedState.state == 2) {
            this.mState = 4;
        } else {
            this.mState = savedState.state;
        }
    }

    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int i) {
        int i2;
        if (ViewCompat.getFitsSystemWindows(coordinatorLayout) && !ViewCompat.getFitsSystemWindows(v)) {
            ViewCompat.setFitsSystemWindows(v, true);
        }
        int top = v.getTop();
        coordinatorLayout.onLayoutChild(v, i);
        this.mParentHeight = coordinatorLayout.getHeight();
        if (this.mPeekHeightAuto) {
            if (this.mPeekHeightMin == 0) {
                this.mPeekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(C4409R.dimen.design_bottom_sheet_peek_height_min);
            }
            i2 = Math.max(this.mPeekHeightMin, this.mParentHeight - ((coordinatorLayout.getWidth() * 9) / 16));
        } else {
            i2 = this.mPeekHeight;
        }
        this.mMinOffset = Math.max(0, this.mParentHeight - v.getHeight());
        this.mMaxOffset = Math.max(this.mParentHeight - i2, this.mMinOffset);
        int i3 = this.mState;
        if (i3 == 3) {
            ViewCompat.offsetTopAndBottom(v, this.mMinOffset);
        } else if (!this.mHideable || i3 != 5) {
            int i4 = this.mState;
            if (i4 == 4) {
                ViewCompat.offsetTopAndBottom(v, this.mMaxOffset);
            } else if (i4 == 1 || i4 == 2) {
                ViewCompat.offsetTopAndBottom(v, top - v.getTop());
            }
        } else {
            ViewCompat.offsetTopAndBottom(v, this.mParentHeight);
        }
        if (this.mViewDragHelper == null) {
            this.mViewDragHelper = ViewDragHelper.create(coordinatorLayout, this.mDragCallback);
        }
        this.mViewRef = new WeakReference<>(v);
        this.mNestedScrollingChildRef = new WeakReference<>(findScrollingChild(v));
        return true;
    }

    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        boolean z = false;
        if (!v.isShown()) {
            this.mIgnoreEvents = true;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            reset();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        if (actionMasked != 3) {
            switch (actionMasked) {
                case 0:
                    int x = (int) motionEvent.getX();
                    this.mInitialY = (int) motionEvent.getY();
                    WeakReference<View> weakReference = this.mNestedScrollingChildRef;
                    View view = weakReference != null ? (View) weakReference.get() : null;
                    if (view != null && coordinatorLayout.isPointInChildBounds(view, x, this.mInitialY)) {
                        this.mActivePointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                        this.mTouchingScrollingChild = true;
                    }
                    this.mIgnoreEvents = this.mActivePointerId == -1 && !coordinatorLayout.isPointInChildBounds(v, x, this.mInitialY);
                    break;
                case 1:
                    break;
            }
        }
        this.mTouchingScrollingChild = false;
        this.mActivePointerId = -1;
        if (this.mIgnoreEvents) {
            this.mIgnoreEvents = false;
            return false;
        }
        if (!this.mIgnoreEvents && this.mViewDragHelper.shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        View view2 = (View) this.mNestedScrollingChildRef.get();
        if (actionMasked == 2 && view2 != null && !this.mIgnoreEvents && this.mState != 1 && !coordinatorLayout.isPointInChildBounds(view2, (int) motionEvent.getX(), (int) motionEvent.getY()) && Math.abs(((float) this.mInitialY) - motionEvent.getY()) > ((float) this.mViewDragHelper.getTouchSlop())) {
            z = true;
        }
        return z;
    }

    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (!v.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (this.mState == 1 && actionMasked == 0) {
            return true;
        }
        ViewDragHelper viewDragHelper = this.mViewDragHelper;
        if (viewDragHelper != null) {
            viewDragHelper.processTouchEvent(motionEvent);
        }
        if (actionMasked == 0) {
            reset();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        if (actionMasked == 2 && !this.mIgnoreEvents && Math.abs(((float) this.mInitialY) - motionEvent.getY()) > ((float) this.mViewDragHelper.getTouchSlop())) {
            this.mViewDragHelper.captureChildView(v, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return !this.mIgnoreEvents;
    }

    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int i) {
        this.mLastNestedScrollDy = 0;
        this.mNestedScrolled = false;
        if ((i & 2) != 0) {
            return true;
        }
        return false;
    }

    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr) {
        if (view == ((View) this.mNestedScrollingChildRef.get())) {
            int top = v.getTop();
            int i3 = top - i2;
            if (i2 > 0) {
                int i4 = this.mMinOffset;
                if (i3 < i4) {
                    iArr[1] = top - i4;
                    ViewCompat.offsetTopAndBottom(v, -iArr[1]);
                    setStateInternal(3);
                } else {
                    iArr[1] = i2;
                    ViewCompat.offsetTopAndBottom(v, -i2);
                    setStateInternal(1);
                }
            } else if (i2 < 0 && !view.canScrollVertically(-1)) {
                int i5 = this.mMaxOffset;
                if (i3 <= i5 || this.mHideable) {
                    iArr[1] = i2;
                    ViewCompat.offsetTopAndBottom(v, -i2);
                    setStateInternal(1);
                } else {
                    iArr[1] = top - i5;
                    ViewCompat.offsetTopAndBottom(v, -iArr[1]);
                    setStateInternal(4);
                }
            }
            dispatchOnSlide(v.getTop());
            this.mLastNestedScrollDy = i2;
            this.mNestedScrolled = true;
        }
    }

    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view) {
        int i;
        int i2 = 3;
        if (v.getTop() == this.mMinOffset) {
            setStateInternal(3);
            return;
        }
        WeakReference<View> weakReference = this.mNestedScrollingChildRef;
        if (weakReference != null && view == weakReference.get() && this.mNestedScrolled) {
            if (this.mLastNestedScrollDy > 0) {
                i = this.mMinOffset;
            } else if (this.mHideable && shouldHide(v, getYVelocity())) {
                i = this.mParentHeight;
                i2 = 5;
            } else if (this.mLastNestedScrollDy == 0) {
                int top = v.getTop();
                if (Math.abs(top - this.mMinOffset) < Math.abs(top - this.mMaxOffset)) {
                    i = this.mMinOffset;
                } else {
                    i = this.mMaxOffset;
                    i2 = 4;
                }
            } else {
                i = this.mMaxOffset;
                i2 = 4;
            }
            if (this.mViewDragHelper.smoothSlideViewTo(v, v.getLeft(), i)) {
                setStateInternal(2);
                ViewCompat.postOnAnimation(v, new SettleRunnable(v, i2));
            } else {
                setStateInternal(i2);
            }
            this.mNestedScrolled = false;
        }
    }

    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2) {
        return view == this.mNestedScrollingChildRef.get() && (this.mState != 3 || super.onNestedPreFling(coordinatorLayout, v, view, f, f2));
    }

    /* access modifiers changed from: 0000 */
    public void invalidateScrollingChild() {
        this.mNestedScrollingChildRef = new WeakReference<>(findScrollingChild((View) this.mViewRef.get()));
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:20:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setPeekHeight(int r4) {
        /*
            r3 = this;
            r0 = 1
            r1 = 0
            r2 = -1
            if (r4 != r2) goto L_0x000c
            boolean r4 = r3.mPeekHeightAuto
            if (r4 != 0) goto L_0x0015
            r3.mPeekHeightAuto = r0
            goto L_0x0024
        L_0x000c:
            boolean r2 = r3.mPeekHeightAuto
            if (r2 != 0) goto L_0x0017
            int r2 = r3.mPeekHeight
            if (r2 == r4) goto L_0x0015
            goto L_0x0017
        L_0x0015:
            r0 = 0
            goto L_0x0024
        L_0x0017:
            r3.mPeekHeightAuto = r1
            int r1 = java.lang.Math.max(r1, r4)
            r3.mPeekHeight = r1
            int r1 = r3.mParentHeight
            int r1 = r1 - r4
            r3.mMaxOffset = r1
        L_0x0024:
            if (r0 == 0) goto L_0x003a
            int r4 = r3.mState
            r0 = 4
            if (r4 != r0) goto L_0x003a
            java.lang.ref.WeakReference<V> r4 = r3.mViewRef
            if (r4 == 0) goto L_0x003a
            java.lang.Object r4 = r4.get()
            android.view.View r4 = (android.view.View) r4
            if (r4 == 0) goto L_0x003a
            r4.requestLayout()
        L_0x003a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.material.ZMViewPagerBottomSheetBehavior.setPeekHeight(int):void");
    }

    public final int getPeekHeight() {
        if (this.mPeekHeightAuto) {
            return -1;
        }
        return this.mPeekHeight;
    }

    public void setHideable(boolean z) {
        this.mHideable = z;
    }

    public boolean isHideable() {
        return this.mHideable;
    }

    public void setSkipCollapsed(boolean z) {
        this.mSkipCollapsed = z;
    }

    public boolean getSkipCollapsed() {
        return this.mSkipCollapsed;
    }

    public void setBottomSheetCallback(BottomSheetCallback bottomSheetCallback) {
        this.mCallback = bottomSheetCallback;
    }

    public final void setState(final int i) {
        if (i != this.mState) {
            WeakReference<V> weakReference = this.mViewRef;
            if (weakReference == null) {
                if (i == 4 || i == 3 || (this.mHideable && i == 5)) {
                    this.mState = i;
                }
                return;
            }
            final View view = (View) weakReference.get();
            if (view != null) {
                ViewParent parent = view.getParent();
                if (parent == null || !parent.isLayoutRequested() || !ViewCompat.isAttachedToWindow(view)) {
                    startSettlingAnimation(view, i);
                } else {
                    view.post(new Runnable() {
                        public void run() {
                            ZMViewPagerBottomSheetBehavior.this.startSettlingAnimation(view, i);
                        }
                    });
                }
            }
        }
    }

    public final int getState() {
        return this.mState;
    }

    /* access modifiers changed from: 0000 */
    public void setStateInternal(int i) {
        if (this.mState != i) {
            this.mState = i;
            View view = (View) this.mViewRef.get();
            if (view != null) {
                BottomSheetCallback bottomSheetCallback = this.mCallback;
                if (bottomSheetCallback != null) {
                    bottomSheetCallback.onStateChanged(view, i);
                }
            }
        }
    }

    private void reset() {
        this.mActivePointerId = -1;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldHide(View view, float f) {
        boolean z = true;
        if (this.mSkipCollapsed) {
            return true;
        }
        if (view.getTop() < this.mMaxOffset) {
            return false;
        }
        if (Math.abs((((float) view.getTop()) + (f * HIDE_FRICTION)) - ((float) this.mMaxOffset)) / ((float) this.mPeekHeight) <= HIDE_THRESHOLD) {
            z = false;
        }
        return z;
    }

    public View getCurrentView(ViewPager viewPager) {
        int currentItem = viewPager.getCurrentItem();
        for (int i = 0; i < viewPager.getChildCount(); i++) {
            View childAt = viewPager.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            try {
                Field declaredField = layoutParams.getClass().getDeclaredField("position");
                declaredField.setAccessible(true);
                int i2 = declaredField.getInt(layoutParams);
                if (!layoutParams.isDecor && currentItem == i2) {
                    return childAt;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    @VisibleForTesting
    private View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (view instanceof ViewPager) {
            View currentView = getCurrentView((ViewPager) view);
            if (currentView == null) {
                return null;
            }
            View findScrollingChild = findScrollingChild(currentView);
            if (findScrollingChild != null) {
                return findScrollingChild;
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View findScrollingChild2 = findScrollingChild(viewGroup.getChildAt(i));
                if (findScrollingChild2 != null) {
                    return findScrollingChild2;
                }
            }
        }
        return null;
    }

    private float getYVelocity() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        return this.mVelocityTracker.getYVelocity(this.mActivePointerId);
    }

    /* access modifiers changed from: 0000 */
    public void startSettlingAnimation(View view, int i) {
        int i2;
        if (i == 4) {
            i2 = this.mMaxOffset;
        } else if (i == 3) {
            i2 = this.mMinOffset;
        } else if (!this.mHideable || i != 5) {
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal state argument: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        } else {
            i2 = this.mParentHeight;
        }
        if (this.mViewDragHelper.smoothSlideViewTo(view, view.getLeft(), i2)) {
            setStateInternal(2);
            ViewCompat.postOnAnimation(view, new SettleRunnable(view, i));
            return;
        }
        setStateInternal(i);
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnSlide(int i) {
        View view = (View) this.mViewRef.get();
        if (view != null) {
            BottomSheetCallback bottomSheetCallback = this.mCallback;
            if (bottomSheetCallback != null) {
                int i2 = this.mMaxOffset;
                if (i > i2) {
                    bottomSheetCallback.onSlide(view, ((float) (i2 - i)) / ((float) (this.mParentHeight - i2)));
                } else {
                    bottomSheetCallback.onSlide(view, ((float) (i2 - i)) / ((float) (i2 - this.mMinOffset)));
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public int getPeekHeightMin() {
        return this.mPeekHeightMin;
    }

    public static <V extends View> ZMViewPagerBottomSheetBehavior<V> from(V v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
            if (behavior instanceof ZMViewPagerBottomSheetBehavior) {
                return (ZMViewPagerBottomSheetBehavior) behavior;
            }
            throw new IllegalArgumentException("The view is not associated with ZMViewPagerBottomSheetBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }
}
