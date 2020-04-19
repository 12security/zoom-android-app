package p021us.zoom.androidlib.widget.pinnedsectionrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.common.primitives.Ints;

/* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.PinnedSectionRecyclerView */
public class PinnedSectionRecyclerView extends RecyclerView {
    private static final String TAG = "PinnedSectionRecyclerView";
    private final AdapterDataObserver mDataSetObserver = new AdapterDataObserver() {
        public void onChanged() {
            PinnedSectionRecyclerView.this.onDataSetChanged();
        }
    };
    private MotionEvent mDownEvent;
    private GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public OnPinnedSectionClick mOnPinnedSectionClick;
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
        }

        public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            PinnedSectionRecyclerView.this.updateViews();
        }
    };
    PinnedSection mPinnedSection;
    /* access modifiers changed from: private */
    public int mPinnedSectionIndex = -1;
    PinnedSection mRecycleSection;
    private int mSectionsDistanceY;
    private GradientDrawable mShadowDrawable;
    private int mShadowHeight;
    private final PointF mTouchPoint = new PointF();
    private final Rect mTouchRect = new Rect();
    private int mTouchSlop;
    private View mTouchTarget;
    int mTranslateY;

    /* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.PinnedSectionRecyclerView$OnPinnedSectionClick */
    public interface OnPinnedSectionClick {
        void onPinnedSectionClick(int i);

        void onPinnedSectionLongClick(int i);
    }

    /* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.PinnedSectionRecyclerView$PinnedSection */
    public static class PinnedSection {

        /* renamed from: id */
        public long f520id;
        public int position;
        public ViewHolder viewHolder;
    }

    public PinnedSectionRecyclerView(Context context) {
        super(context);
        initViews();
    }

    public PinnedSectionRecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public PinnedSectionRecyclerView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        addOnScrollListener(this.mOnScrollListener);
        showShadow(true);
        this.mGestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                if (PinnedSectionRecyclerView.this.mOnPinnedSectionClick == null || PinnedSectionRecyclerView.this.mPinnedSectionIndex == -1) {
                    return super.onSingleTapUp(motionEvent);
                }
                PinnedSectionRecyclerView.this.mOnPinnedSectionClick.onPinnedSectionClick(PinnedSectionRecyclerView.this.mPinnedSectionIndex);
                return true;
            }

            public void onLongPress(MotionEvent motionEvent) {
                super.onLongPress(motionEvent);
                if (PinnedSectionRecyclerView.this.mOnPinnedSectionClick != null && PinnedSectionRecyclerView.this.mPinnedSectionIndex != -1) {
                    PinnedSectionRecyclerView.this.mOnPinnedSectionClick.onPinnedSectionLongClick(PinnedSectionRecyclerView.this.mPinnedSectionIndex);
                }
            }
        });
    }

    public void setOnPinnedSectionClick(OnPinnedSectionClick onPinnedSectionClick) {
        this.mOnPinnedSectionClick = onPinnedSectionClick;
    }

    public int getCurrentPinnedSection() {
        return this.mPinnedSectionIndex;
    }

    private void createPinnedSection(int i) {
        if (i == -1) {
            this.mRecycleSection = null;
            return;
        }
        PinnedSection pinnedSection = this.mRecycleSection;
        this.mRecycleSection = null;
        if (pinnedSection == null) {
            pinnedSection = new PinnedSection();
            pinnedSection.position = i;
        }
        Adapter adapter = getAdapter();
        if (adapter != null) {
            ViewHolder createViewHolder = adapter.createViewHolder(this, adapter.getItemViewType(i));
            adapter.bindViewHolder(createViewHolder, i);
            LayoutParams layoutParams = createViewHolder.itemView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = generateDefaultLayoutParams();
                createViewHolder.itemView.setLayoutParams(layoutParams);
            }
            int mode = MeasureSpec.getMode(layoutParams.height);
            int size = MeasureSpec.getSize(layoutParams.height);
            if (mode == 0) {
                mode = Ints.MAX_POWER_OF_TWO;
            }
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            if (size > height) {
                size = height;
            }
            createViewHolder.itemView.measure(MeasureSpec.makeMeasureSpec((getWidth() - getPaddingLeft()) - getPaddingRight(), Ints.MAX_POWER_OF_TWO), MeasureSpec.makeMeasureSpec(size, mode));
            createViewHolder.itemView.layout(0, 0, createViewHolder.itemView.getMeasuredWidth(), createViewHolder.itemView.getMeasuredHeight());
            this.mTranslateY = 0;
            pinnedSection.viewHolder = createViewHolder;
            pinnedSection.position = i;
            pinnedSection.f520id = getAdapter().getItemId(i);
            this.mPinnedSection = pinnedSection;
            this.mPinnedSectionIndex = i;
        }
    }

    public void updatePinnedSection() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (layoutManager.getChildCount() < 2) {
                destroyPinnedShadow();
                return;
            }
            int findFirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (getAdapter() instanceof IPinnedSectionAdapter) {
                createPinnedSection(findCurrentPinnedSection(findFirstVisibleItemPosition));
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateViews() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (layoutManager.getChildCount() < 2) {
                destroyPinnedShadow();
                return;
            }
            int findFirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (getAdapter() instanceof IPinnedSectionAdapter) {
                int findCurrentPinnedSection = findCurrentPinnedSection(findFirstVisibleItemPosition);
                PinnedSection pinnedSection = this.mPinnedSection;
                if (!(pinnedSection == null || pinnedSection.position == findCurrentPinnedSection)) {
                    destroyPinnedShadow();
                }
                if (this.mPinnedSection == null) {
                    createPinnedSection(findCurrentPinnedSection);
                }
                ensureShadowForPosition();
                return;
            }
            throw new IllegalArgumentException("Adapter need to implement IPinnedSectionAdapter!");
        }
    }

    private void ensureShadowForPosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int findCurrentPinnedSection = findCurrentPinnedSection(linearLayoutManager.findFirstVisibleItemPosition());
            if (findCurrentPinnedSection != -1) {
                if (this.mPinnedSection == null) {
                    createPinnedSection(findCurrentPinnedSection);
                }
                View findViewByPosition = layoutManager.findViewByPosition(findNextPinnedSection(this.mPinnedSection.position));
                int i = 0;
                if (findViewByPosition == null) {
                    if (linearLayoutManager.findFirstVisibleItemPosition() != linearLayoutManager.findFirstCompletelyVisibleItemPosition()) {
                        i = this.mShadowHeight;
                    }
                    this.mSectionsDistanceY = i;
                    return;
                }
                this.mSectionsDistanceY = findViewByPosition.getTop() - this.mPinnedSection.viewHolder.itemView.getBottom();
                int i2 = this.mSectionsDistanceY;
                if (i2 < 0) {
                    this.mTranslateY = i2;
                } else {
                    this.mTranslateY = 0;
                }
            }
        }
    }

    private void destroyPinnedShadow() {
        this.mSectionsDistanceY = 0;
        PinnedSection pinnedSection = this.mPinnedSection;
        if (pinnedSection != null) {
            this.mRecycleSection = pinnedSection;
            this.mPinnedSection = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void recreatePinnedShadow() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            destroyPinnedShadow();
            if (layoutManager.getItemCount() > 0) {
                ensureShadowForPosition();
            }
        }
    }

    public int findCurrentPinnedSection(int i) {
        if (!(getLayoutManager() instanceof LinearLayoutManager)) {
            return -1;
        }
        Adapter adapter = getAdapter();
        if (!(adapter instanceof IPinnedSectionAdapter)) {
            return -1;
        }
        while (true) {
            if (i < 0) {
                i = -1;
                break;
            } else if (((IPinnedSectionAdapter) adapter).isPinnedSection(i)) {
                break;
            } else {
                i--;
            }
        }
        return i;
    }

    private int findNextPinnedSection(int i) {
        LayoutManager layoutManager = getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return -1;
        }
        Adapter adapter = getAdapter();
        if (!(adapter instanceof IPinnedSectionAdapter)) {
            return -1;
        }
        while (true) {
            i++;
            if (i < layoutManager.getItemCount()) {
                if (((IPinnedSectionAdapter) adapter).isPinnedSection(i)) {
                    break;
                }
            } else {
                i = -1;
                break;
            }
        }
        return i;
    }

    public void showShadow(boolean z) {
        if (z) {
            if (this.mShadowDrawable == null) {
                this.mShadowDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")});
                this.mShadowHeight = (int) (getResources().getDisplayMetrics().density * 8.0f);
            }
        } else if (this.mShadowDrawable != null) {
            this.mShadowDrawable = null;
            this.mShadowHeight = 0;
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(parcelable);
        post(new Runnable() {
            public void run() {
                PinnedSectionRecyclerView.this.recreatePinnedShadow();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mPinnedSection != null && ((i3 - i) - getPaddingLeft()) - getPaddingRight() != this.mPinnedSection.viewHolder.itemView.getWidth()) {
            recreatePinnedShadow();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        int i;
        super.dispatchDraw(canvas);
        if (this.mPinnedSection != null) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            View view = this.mPinnedSection.viewHolder.itemView;
            canvas.save();
            int height = view.getHeight();
            if (this.mShadowDrawable == null) {
                i = 0;
            } else {
                i = Math.min(this.mShadowHeight, this.mSectionsDistanceY);
            }
            canvas.clipRect(paddingLeft, paddingTop, view.getWidth() + paddingLeft, height + i + paddingTop);
            canvas.translate((float) paddingLeft, (float) (paddingTop + this.mTranslateY));
            drawChild(canvas, view, getDrawingTime());
            GradientDrawable gradientDrawable = this.mShadowDrawable;
            if (gradientDrawable != null && this.mSectionsDistanceY > 0) {
                gradientDrawable.setBounds(view.getLeft(), view.getBottom(), view.getRight(), view.getBottom() + this.mShadowHeight);
                this.mShadowDrawable.draw(canvas);
            }
            canvas.restore();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0 && this.mTouchTarget == null) {
            PinnedSection pinnedSection = this.mPinnedSection;
            if (pinnedSection != null && isPinnedViewTouched(pinnedSection.viewHolder.itemView, x, y)) {
                this.mTouchTarget = this.mPinnedSection.viewHolder.itemView;
                PointF pointF = this.mTouchPoint;
                pointF.x = x;
                pointF.y = y;
                this.mDownEvent = MotionEvent.obtain(motionEvent);
            }
        }
        View view = this.mTouchTarget;
        if (view == null) {
            return super.dispatchTouchEvent(motionEvent);
        }
        if (isPinnedViewTouched(view, x, y)) {
            this.mTouchTarget.dispatchTouchEvent(motionEvent);
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        if (action == 1 || action == 3) {
            super.dispatchTouchEvent(motionEvent);
            clearTouchTarget();
        } else if (action == 2 && Math.abs(y - this.mTouchPoint.y) > ((float) this.mTouchSlop)) {
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            obtain.setAction(3);
            this.mTouchTarget.dispatchTouchEvent(obtain);
            obtain.recycle();
            super.dispatchTouchEvent(this.mDownEvent);
            super.dispatchTouchEvent(motionEvent);
            clearTouchTarget();
        }
        return true;
    }

    private void clearTouchTarget() {
        this.mTouchTarget = null;
    }

    private boolean isPinnedViewTouched(View view, float f, float f2) {
        view.getHitRect(this.mTouchRect);
        this.mTouchRect.top += this.mTranslateY;
        this.mTouchRect.bottom += this.mTranslateY + getPaddingTop();
        this.mTouchRect.left += getPaddingLeft();
        this.mTouchRect.right -= getPaddingRight();
        return this.mTouchRect.contains((int) f, (int) f2);
    }

    public void setAdapter(Adapter adapter) {
        Adapter adapter2 = getAdapter();
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(this.mDataSetObserver);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mDataSetObserver);
        }
        if (adapter2 != adapter) {
            destroyPinnedShadow();
        }
        super.setAdapter(adapter);
    }

    /* access modifiers changed from: private */
    public void onDataSetChanged() {
        recreatePinnedShadow();
        Adapter adapter = getAdapter();
        if (adapter instanceof IPinnedSectionAdapter) {
            ((IPinnedSectionAdapter) adapter).onChanged();
        }
    }
}
