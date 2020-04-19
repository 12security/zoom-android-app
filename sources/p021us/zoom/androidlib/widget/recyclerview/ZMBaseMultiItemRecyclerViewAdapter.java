package p021us.zoom.androidlib.widget.recyclerview;

import android.util.SparseIntArray;
import android.view.ViewGroup;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import java.util.List;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewItemHolder;
import p021us.zoom.androidlib.widget.recyclerview.ZMMultiItemEntity;

/* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseMultiItemRecyclerViewAdapter */
public abstract class ZMBaseMultiItemRecyclerViewAdapter<T extends ZMMultiItemEntity, K extends ZMBaseRecyclerViewItemHolder> extends ZMBaseRecyclerViewAdapter<T, K> {
    private static final int DEFAULT_VIEW_TYPE = -255;
    public static final int TYPE_NOT_FOUND = -404;
    private SparseIntArray layouts;

    public ZMBaseMultiItemRecyclerViewAdapter(List<T> list) {
        super(list);
    }

    /* access modifiers changed from: protected */
    public int getDefItemViewType(int i) {
        ZMMultiItemEntity zMMultiItemEntity = (ZMMultiItemEntity) this.mData.get(i);
        return zMMultiItemEntity != null ? zMMultiItemEntity.getItemType() : DEFAULT_VIEW_TYPE;
    }

    /* access modifiers changed from: protected */
    public void setDefaultViewTypeLayout(@LayoutRes int i) {
        addItemType(DEFAULT_VIEW_TYPE, i);
    }

    /* access modifiers changed from: protected */
    public K onCreateDefViewHolder(ViewGroup viewGroup, int i) {
        return createBaseViewHolder(viewGroup, getLayoutId(i));
    }

    private int getLayoutId(int i) {
        return this.layouts.get(i, TYPE_NOT_FOUND);
    }

    /* access modifiers changed from: protected */
    public void addItemType(int i, @LayoutRes int i2) {
        if (this.layouts == null) {
            this.layouts = new SparseIntArray();
        }
        this.layouts.put(i, i2);
    }

    public void remove(@IntRange(from = 0) int i) {
        if (this.mData != null && i >= 0 && i < this.mData.size()) {
            ZMMultiItemEntity zMMultiItemEntity = (ZMMultiItemEntity) this.mData.get(i);
            super.remove(i);
        }
    }
}
