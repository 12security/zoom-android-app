package p021us.zoom.androidlib.widget.pinnedsectionrecyclerview;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.LayoutParams;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;

/* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.GridDecoration */
public class GridDecoration extends ItemDecoration {
    private int mSpaceEdge;
    private int mSpaceMiddle;

    public GridDecoration(int i) {
        this.mSpaceEdge = i;
        this.mSpaceMiddle = i;
    }

    public GridDecoration(int i, int i2) {
        this.mSpaceEdge = i;
        this.mSpaceMiddle = i2;
    }

    public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull State state) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int spanIndex = layoutParams.getSpanIndex();
        int spanCount = ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount();
        if (layoutParams.getSpanSize() != spanCount && layoutParams.getSpanSize() == 1) {
            if (spanIndex == 0) {
                rect.left = this.mSpaceEdge;
                rect.right = this.mSpaceMiddle / 2;
            } else if (spanIndex == spanCount - 1) {
                rect.left = this.mSpaceMiddle / 2;
                rect.right = this.mSpaceEdge;
            } else {
                int i = this.mSpaceMiddle;
                rect.left = i / 2;
                rect.right = i / 2;
            }
            layoutParams.getViewLayoutPosition();
            rect.bottom = this.mSpaceEdge;
        }
    }
}
