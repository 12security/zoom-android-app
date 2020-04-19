package p021us.zoom.androidlib.widget.recyclerview;

import android.util.SparseIntArray;
import androidx.annotation.LayoutRes;
import java.util.List;

/* renamed from: us.zoom.androidlib.widget.recyclerview.ZMMultiTypeDelegate */
public abstract class ZMMultiTypeDelegate<T> {
    private static final int DEFAULT_VIEW_TYPE = -255;
    private boolean autoMode;
    private SparseIntArray layouts;
    private boolean selfMode;

    /* access modifiers changed from: protected */
    public abstract int getItemType(T t);

    public ZMMultiTypeDelegate(SparseIntArray sparseIntArray) {
        this.layouts = sparseIntArray;
    }

    public ZMMultiTypeDelegate() {
    }

    public final int getDefItemViewType(List<T> list, int i) {
        Object obj = list.get(i);
        return obj != null ? getItemType(obj) : DEFAULT_VIEW_TYPE;
    }

    public final int getLayoutId(int i) {
        return this.layouts.get(i, ZMBaseMultiItemRecyclerViewAdapter.TYPE_NOT_FOUND);
    }

    private void addItemType(int i, @LayoutRes int i2) {
        if (this.layouts == null) {
            this.layouts = new SparseIntArray();
        }
        this.layouts.put(i, i2);
    }

    public ZMMultiTypeDelegate registerItemTypeAutoIncrease(@LayoutRes int... iArr) {
        this.autoMode = true;
        checkMode(this.selfMode);
        for (int i = 0; i < iArr.length; i++) {
            addItemType(i, iArr[i]);
        }
        return this;
    }

    public ZMMultiTypeDelegate registerItemType(int i, @LayoutRes int i2) {
        this.selfMode = true;
        checkMode(this.autoMode);
        addItemType(i, i2);
        return this;
    }

    private void checkMode(boolean z) {
        if (z) {
            throw new RuntimeException("Don't mess two register mode");
        }
    }
}
