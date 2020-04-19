package p021us.zoom.androidlib.widget.recyclerviewhelper;

import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

/* renamed from: us.zoom.androidlib.widget.recyclerviewhelper.RVHItemTouchHelperCallback */
public class RVHItemTouchHelperCallback extends Callback {
    private final boolean isItemViewSwipeEnabledLeft;
    private final boolean isItemViewSwipeEnabledRight;
    private final boolean isLongPressDragEnabled;
    private final RVHAdapter mAdapter;

    public RVHItemTouchHelperCallback(RVHAdapter rVHAdapter, boolean z, boolean z2, boolean z3) {
        this.mAdapter = rVHAdapter;
        this.isItemViewSwipeEnabledLeft = z2;
        this.isItemViewSwipeEnabledRight = z3;
        this.isLongPressDragEnabled = z;
    }

    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder) {
        int i;
        boolean z = viewHolder instanceof RVHViewHolder;
        int i2 = 0;
        if (!z) {
            i = 0;
        } else {
            i = 3;
            if (this.mAdapter.isCanSwipe()) {
                i2 = (!this.isItemViewSwipeEnabledLeft || !this.isItemViewSwipeEnabledRight) ? this.isItemViewSwipeEnabledRight ? 16 : 32 : 48;
            }
        }
        return Callback.makeMovementFlags(i, i2);
    }

    public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, @NonNull ViewHolder viewHolder2) {
        return viewHolder.getItemViewType() == viewHolder2.getItemViewType();
    }

    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, @NonNull ViewHolder viewHolder2) {
        this.mAdapter.onItemMove(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
        return true;
    }

    public boolean isLongPressDragEnabled() {
        return this.isLongPressDragEnabled;
    }

    public boolean isItemViewSwipeEnabled() {
        return this.isItemViewSwipeEnabledLeft || this.isItemViewSwipeEnabledRight;
    }

    public void onSwiped(@NonNull ViewHolder viewHolder, int i) {
        this.mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), i);
    }

    public void onSelectedChanged(ViewHolder viewHolder, int i) {
        if (i != 0 && (viewHolder instanceof RVHViewHolder)) {
            ((RVHViewHolder) viewHolder).onItemSelected(i);
        }
        super.onSelectedChanged(viewHolder, i);
    }

    public void clearView(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof RVHViewHolder) {
            ((RVHViewHolder) viewHolder).onItemClear();
        }
    }

    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, float f, float f2, int i, boolean z) {
        if (i == 1) {
            viewHolder.itemView.setTranslationX(f);
        } else {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }
    }
}
