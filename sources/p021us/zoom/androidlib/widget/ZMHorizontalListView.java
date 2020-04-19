package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.Scroller;
import androidx.annotation.Nullable;
import com.google.common.primitives.Ints;
import java.util.LinkedList;
import java.util.Queue;

/* renamed from: us.zoom.androidlib.widget.ZMHorizontalListView */
public class ZMHorizontalListView extends AdapterView<ListAdapter> {
    protected ListAdapter mAdapter;
    public boolean mAlwaysOverrideTouch = true;
    protected int mCurrentX;
    /* access modifiers changed from: private */
    public boolean mDataChanged = false;
    private DataSetObserver mDataObserver = new DataSetObserver() {
        public void onChanged() {
            synchronized (ZMHorizontalListView.this) {
                ZMHorizontalListView.this.mDataChanged = true;
            }
            ZMHorizontalListView.this.invalidate();
            ZMHorizontalListView.this.requestLayout();
        }

        public void onInvalidated() {
            ZMHorizontalListView.this.reset();
            ZMHorizontalListView.this.invalidate();
            ZMHorizontalListView.this.requestLayout();
        }
    };
    private int mDisplayOffset = 0;
    private GestureDetector mGesture;
    /* access modifiers changed from: private */
    public int mLeftViewIndex = -1;
    private int mMaxX = Integer.MAX_VALUE;
    protected int mNextX;
    private OnGestureListener mOnGesture = new SimpleOnGestureListener() {
        public boolean onDown(MotionEvent motionEvent) {
            return ZMHorizontalListView.this.onDown(motionEvent);
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return ZMHorizontalListView.this.onFling(motionEvent, motionEvent2, f, f2);
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            synchronized (ZMHorizontalListView.this) {
                ZMHorizontalListView.this.mNextX += (int) f;
            }
            ZMHorizontalListView.this.requestLayout();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Rect rect = new Rect();
            int i = 0;
            while (true) {
                if (i >= ZMHorizontalListView.this.getChildCount()) {
                    break;
                }
                View childAt = ZMHorizontalListView.this.getChildAt(i);
                rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    if (ZMHorizontalListView.this.mOnItemClicked != null) {
                        OnItemClickListener access$200 = ZMHorizontalListView.this.mOnItemClicked;
                        ZMHorizontalListView zMHorizontalListView = ZMHorizontalListView.this;
                        access$200.onItemClick(zMHorizontalListView, childAt, zMHorizontalListView.mLeftViewIndex + 1 + i, ZMHorizontalListView.this.mAdapter.getItemId(ZMHorizontalListView.this.mLeftViewIndex + 1 + i));
                    }
                    if (ZMHorizontalListView.this.mOnItemSelected != null) {
                        OnItemSelectedListener access$400 = ZMHorizontalListView.this.mOnItemSelected;
                        ZMHorizontalListView zMHorizontalListView2 = ZMHorizontalListView.this;
                        access$400.onItemSelected(zMHorizontalListView2, childAt, zMHorizontalListView2.mLeftViewIndex + 1 + i, ZMHorizontalListView.this.mAdapter.getItemId(ZMHorizontalListView.this.mLeftViewIndex + 1 + i));
                    }
                } else {
                    i++;
                }
            }
            return true;
        }
    };
    /* access modifiers changed from: private */
    public OnItemClickListener mOnItemClicked;
    /* access modifiers changed from: private */
    public OnItemSelectedListener mOnItemSelected;
    private Runnable mRelayoutRunnable = new Runnable() {
        public void run() {
            ZMHorizontalListView.this.requestLayout();
        }
    };
    private Queue<View> mRemovedViewQueue = new LinkedList();
    private int mRightViewIndex = 0;
    protected Scroller mScroller;

    @Nullable
    public View getSelectedView() {
        return null;
    }

    public void setSelection(int i) {
    }

    public ZMHorizontalListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private synchronized void initView() {
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = Integer.MAX_VALUE;
        this.mScroller = new Scroller(getContext());
        if (!isInEditMode()) {
            this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelected = onItemSelectedListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClicked = onItemClickListener;
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setAdapter(ListAdapter listAdapter) {
        ListAdapter listAdapter2 = this.mAdapter;
        if (listAdapter2 != null) {
            listAdapter2.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = listAdapter;
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        reset();
    }

    /* access modifiers changed from: private */
    public synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    private void addAndMeasureChild(View view, int i) {
        int i2;
        int i3;
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-1, -1);
        }
        addViewInLayout(view, i, layoutParams, true);
        if (layoutParams.width == -1 || layoutParams.width == -2) {
            i2 = MeasureSpec.makeMeasureSpec(getWidth(), Integer.MIN_VALUE);
        } else {
            i2 = MeasureSpec.makeMeasureSpec(layoutParams.width, Ints.MAX_POWER_OF_TWO);
        }
        if (layoutParams.height == -2) {
            i3 = MeasureSpec.makeMeasureSpec(getHeight(), Integer.MIN_VALUE);
        } else if (layoutParams.height == -1) {
            i3 = MeasureSpec.makeMeasureSpec(getHeight(), Ints.MAX_POWER_OF_TWO);
        } else {
            i3 = MeasureSpec.makeMeasureSpec(layoutParams.height, Ints.MAX_POWER_OF_TWO);
        }
        view.measure(i2, i3);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0077, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onLayout(boolean r1, int r2, int r3, int r4, int r5) {
        /*
            r0 = this;
            monitor-enter(r0)
            super.onLayout(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x0078 }
            android.widget.ListAdapter r1 = r0.mAdapter     // Catch:{ all -> 0x0078 }
            if (r1 != 0) goto L_0x000a
            monitor-exit(r0)
            return
        L_0x000a:
            boolean r1 = r0.mDataChanged     // Catch:{ all -> 0x0078 }
            r2 = 0
            if (r1 == 0) goto L_0x001b
            int r1 = r0.mCurrentX     // Catch:{ all -> 0x0078 }
            r0.initView()     // Catch:{ all -> 0x0078 }
            r0.removeAllViewsInLayout()     // Catch:{ all -> 0x0078 }
            r0.mNextX = r1     // Catch:{ all -> 0x0078 }
            r0.mDataChanged = r2     // Catch:{ all -> 0x0078 }
        L_0x001b:
            android.widget.Scroller r1 = r0.mScroller     // Catch:{ all -> 0x0078 }
            boolean r1 = r1.computeScrollOffset()     // Catch:{ all -> 0x0078 }
            if (r1 == 0) goto L_0x002b
            android.widget.Scroller r1 = r0.mScroller     // Catch:{ all -> 0x0078 }
            int r1 = r1.getCurrX()     // Catch:{ all -> 0x0078 }
            r0.mNextX = r1     // Catch:{ all -> 0x0078 }
        L_0x002b:
            int r1 = r0.mNextX     // Catch:{ all -> 0x0078 }
            r3 = 1
            if (r1 >= 0) goto L_0x0037
            r0.mNextX = r2     // Catch:{ all -> 0x0078 }
            android.widget.Scroller r1 = r0.mScroller     // Catch:{ all -> 0x0078 }
            r1.forceFinished(r3)     // Catch:{ all -> 0x0078 }
        L_0x0037:
            int r1 = r0.mNextX     // Catch:{ all -> 0x0078 }
            int r4 = r0.mMaxX     // Catch:{ all -> 0x0078 }
            if (r1 <= r4) goto L_0x004a
            int r1 = r0.mMaxX     // Catch:{ all -> 0x0078 }
            if (r1 <= 0) goto L_0x004a
            int r1 = r0.mMaxX     // Catch:{ all -> 0x0078 }
            r0.mNextX = r1     // Catch:{ all -> 0x0078 }
            android.widget.Scroller r1 = r0.mScroller     // Catch:{ all -> 0x0078 }
            r1.forceFinished(r3)     // Catch:{ all -> 0x0078 }
        L_0x004a:
            int r1 = r0.mCurrentX     // Catch:{ all -> 0x0078 }
            int r4 = r0.mNextX     // Catch:{ all -> 0x0078 }
            int r1 = r1 - r4
            r0.removeNonVisibleItems(r1)     // Catch:{ all -> 0x0078 }
            r0.fillList(r1)     // Catch:{ all -> 0x0078 }
            r0.positionItems(r1)     // Catch:{ all -> 0x0078 }
            int r1 = r0.mNextX     // Catch:{ all -> 0x0078 }
            r0.mCurrentX = r1     // Catch:{ all -> 0x0078 }
            int r1 = r0.mNextX     // Catch:{ all -> 0x0078 }
            int r4 = r0.mMaxX     // Catch:{ all -> 0x0078 }
            if (r1 <= r4) goto L_0x0067
            int r1 = r0.mMaxX     // Catch:{ all -> 0x0078 }
            if (r1 <= 0) goto L_0x0067
            r2 = 1
        L_0x0067:
            android.widget.Scroller r1 = r0.mScroller     // Catch:{ all -> 0x0078 }
            boolean r1 = r1.isFinished()     // Catch:{ all -> 0x0078 }
            if (r1 == 0) goto L_0x0071
            if (r2 == 0) goto L_0x0076
        L_0x0071:
            java.lang.Runnable r1 = r0.mRelayoutRunnable     // Catch:{ all -> 0x0078 }
            r0.post(r1)     // Catch:{ all -> 0x0078 }
        L_0x0076:
            monitor-exit(r0)
            return
        L_0x0078:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.widget.ZMHorizontalListView.onLayout(boolean, int, int, int, int):void");
    }

    private void fillList(int i) {
        View childAt = getChildAt(getChildCount() - 1);
        int i2 = 0;
        fillListRight(childAt != null ? childAt.getRight() : 0, i);
        View childAt2 = getChildAt(0);
        if (childAt2 != null) {
            i2 = childAt2.getLeft();
        }
        fillListLeft(i2, i);
    }

    private void fillListRight(int i, int i2) {
        while (i + i2 < getWidth() && this.mRightViewIndex < this.mAdapter.getCount()) {
            View view = this.mAdapter.getView(this.mRightViewIndex, (View) this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(view, -1);
            i += view.getMeasuredWidth();
            if (this.mRightViewIndex == this.mAdapter.getCount() - 1) {
                this.mMaxX = (this.mCurrentX + i) - getWidth();
            }
            this.mRightViewIndex++;
        }
    }

    private void fillListLeft(int i, int i2) {
        while (i + i2 > 0) {
            int i3 = this.mLeftViewIndex;
            if (i3 >= 0) {
                View view = this.mAdapter.getView(i3, (View) this.mRemovedViewQueue.poll(), this);
                addAndMeasureChild(view, 0);
                i -= view.getMeasuredWidth();
                this.mLeftViewIndex--;
                this.mDisplayOffset -= view.getMeasuredWidth();
            } else {
                return;
            }
        }
    }

    private void removeNonVisibleItems(int i) {
        View childAt = getChildAt(0);
        while (childAt != null && childAt.getRight() + i <= 0) {
            this.mDisplayOffset += childAt.getMeasuredWidth();
            this.mRemovedViewQueue.offer(childAt);
            removeViewInLayout(childAt);
            this.mLeftViewIndex++;
            childAt = getChildAt(0);
        }
        View childAt2 = getChildAt(getChildCount() - 1);
        while (childAt2 != null && childAt2.getLeft() + i >= getWidth()) {
            this.mRemovedViewQueue.offer(childAt2);
            removeViewInLayout(childAt2);
            this.mRightViewIndex--;
            childAt2 = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(int i) {
        if (getChildCount() > 0) {
            this.mDisplayOffset += i;
            int i2 = this.mDisplayOffset;
            int i3 = 0;
            while (i3 < getChildCount()) {
                View childAt = getChildAt(i3);
                int measuredWidth = childAt.getMeasuredWidth() + i2;
                childAt.layout(i2, 0, measuredWidth, childAt.getMeasuredHeight());
                i3++;
                i2 = measuredWidth;
            }
        }
    }

    public synchronized void scrollTo(int i) {
        this.mScroller.startScroll(this.mNextX, 0, i - this.mNextX, 0);
        requestLayout();
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = this.mGesture.onTouchEvent(motionEvent);
        if (onTouchEvent || motionEvent.getAction() != 1) {
            return onTouchEvent;
        }
        if (this.mCurrentX > 0) {
            synchronized (this) {
                this.mScroller.fling(this.mCurrentX, 0, -100, 0, 0, this.mMaxX, 0, 0);
            }
            requestLayout();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        synchronized (this) {
            this.mScroller.fling(this.mNextX, 0, (int) (-f), 0, 0, this.mMaxX, 0, 0);
        }
        requestLayout();
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onDown(MotionEvent motionEvent) {
        this.mScroller.forceFinished(true);
        return true;
    }
}
