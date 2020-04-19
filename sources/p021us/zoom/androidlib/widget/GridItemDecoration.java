package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/* renamed from: us.zoom.androidlib.widget.GridItemDecoration */
public class GridItemDecoration extends ItemDecoration {
    private Drawable mDivider;
    private int mHorizonSpan;
    private boolean mShowLastLine;
    private int mVerticalSpan;

    /* renamed from: us.zoom.androidlib.widget.GridItemDecoration$Builder */
    public static class Builder {
        private int mColor = -1;
        private Context mContext;
        private int mHorizonSpan = 0;
        private Resources mResources;
        private boolean mShowLastLine = true;
        private int mVerticalSpan = 0;

        public Builder(Context context) {
            this.mContext = context;
            this.mResources = context.getResources();
        }

        public Builder setColorResource(@ColorRes int i) {
            setColor(ContextCompat.getColor(this.mContext, i));
            return this;
        }

        public Builder setColor(@ColorInt int i) {
            this.mColor = i;
            return this;
        }

        public Builder setVerticalSpan(@DimenRes int i) {
            this.mVerticalSpan = this.mResources.getDimensionPixelSize(i);
            return this;
        }

        public Builder setVerticalSpan(float f) {
            this.mVerticalSpan = (int) TypedValue.applyDimension(0, f, this.mResources.getDisplayMetrics());
            return this;
        }

        public Builder setHorizontalSpan(@DimenRes int i) {
            this.mHorizonSpan = this.mResources.getDimensionPixelSize(i);
            return this;
        }

        public Builder setHorizontalSpan(float f) {
            this.mHorizonSpan = (int) TypedValue.applyDimension(0, f, this.mResources.getDisplayMetrics());
            return this;
        }

        public Builder setShowLastLine(boolean z) {
            this.mShowLastLine = z;
            return this;
        }

        public GridItemDecoration build() {
            GridItemDecoration gridItemDecoration = new GridItemDecoration(this.mHorizonSpan, this.mVerticalSpan, this.mColor, this.mShowLastLine);
            return gridItemDecoration;
        }
    }

    private GridItemDecoration(int i, int i2, int i3, boolean z) {
        this.mHorizonSpan = i;
        this.mShowLastLine = z;
        this.mVerticalSpan = i2;
        this.mDivider = new ColorDrawable(i3);
    }

    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull State state) {
        drawHorizontal(canvas, recyclerView);
        drawVertical(canvas, recyclerView);
    }

    private void drawHorizontal(Canvas canvas, RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            if (!isLastRaw(recyclerView, i, getSpanCount(recyclerView), childCount) || this.mShowLastLine) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int left = childAt.getLeft() - layoutParams.leftMargin;
                int right = childAt.getRight() + layoutParams.rightMargin;
                int bottom = childAt.getBottom() + layoutParams.bottomMargin;
                this.mDivider.setBounds(left, bottom, right, this.mHorizonSpan + bottom);
                this.mDivider.draw(canvas);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            if ((recyclerView.getChildViewHolder(childAt).getAdapterPosition() + 1) % getSpanCount(recyclerView) != 0) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int top = childAt.getTop() - layoutParams.topMargin;
                int bottom = childAt.getBottom() + layoutParams.bottomMargin + this.mHorizonSpan;
                int right = childAt.getRight() + layoutParams.rightMargin;
                int i2 = this.mVerticalSpan;
                int i3 = right + i2;
                if (i == childCount - 1) {
                    i3 -= i2;
                }
                this.mDivider.setBounds(right, top, i3, bottom);
                this.mDivider.draw(canvas);
            }
        }
    }

    public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull State state) {
        int spanCount = getSpanCount(recyclerView);
        int itemCount = recyclerView.getAdapter().getItemCount();
        int viewLayoutPosition = ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (viewLayoutPosition >= 0) {
            int i = viewLayoutPosition % spanCount;
            int i2 = this.mVerticalSpan;
            int i3 = (i * i2) / spanCount;
            int i4 = i2 - (((i + 1) * i2) / spanCount);
            int i5 = isLastRaw(recyclerView, viewLayoutPosition, spanCount, itemCount) ? this.mShowLastLine ? this.mHorizonSpan : 0 : this.mHorizonSpan;
            rect.set(i3, 0, i4, i5);
        }
    }

    private int getSpanCount(RecyclerView recyclerView) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return -1;
    }

    private boolean isLastRaw(RecyclerView recyclerView, int i, int i2, int i3) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return getResult(i, i2, i3);
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (((StaggeredGridLayoutManager) layoutManager).getOrientation() == 1) {
                return getResult(i, i2, i3);
            }
            if ((i + 1) % i2 == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean getResult(int i, int i2, int i3) {
        int i4 = i3 % i2;
        if (i4 == 0) {
            if (i >= i3 - i2) {
                return true;
            }
        } else if (i >= i3 - i4) {
            return true;
        }
        return false;
    }
}
