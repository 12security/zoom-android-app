package p021us.zoom.androidlib.widget.pinnedsectionrecyclerview;

import android.content.Context;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.List;

/* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter */
public abstract class BaseRecyclerViewAdapter<T> extends Adapter<BaseViewHolder> {
    public static final int VIEW_TYPE_EMPTY = 4;
    public static final int VIEW_TYPE_FOOTER = 3;
    public static final int VIEW_TYPE_HEADER = 1;
    public static final int VIEW_TYPE_ITEM = 2;
    protected Context mContext;
    protected List<T> mData = new ArrayList();
    /* access modifiers changed from: protected */
    public OnRecyclerViewListener mListener;

    /* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter$BaseViewHolder */
    public static class BaseViewHolder extends ViewHolder {
        public BaseViewHolder(View view) {
            super(view);
        }
    }

    /* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter$OnRecyclerViewListener */
    public interface OnRecyclerViewListener {
        void onItemClick(View view, int i);

        boolean onItemLongClick(View view, int i);
    }

    public int getHeaderViewsCount() {
        return 0;
    }

    public boolean hasFooter() {
        return false;
    }

    public boolean hasHeader() {
        return false;
    }

    public BaseRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public int getItemCount() {
        List<T> list = this.mData;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public List<T> getData() {
        return this.mData;
    }

    public void setData(List<T> list) {
        if (this.mData == null) {
            this.mData = new ArrayList();
        }
        this.mData.clear();
        if (list != null) {
            this.mData.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<T> list) {
        if (list != null) {
            this.mData.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void remove(int i) {
        List<T> list = this.mData;
        if (list != null && i >= 0 && i < list.size()) {
            this.mData.remove(i);
            notifyDataSetChanged();
        }
    }

    public void remove(T t) {
        if (t != null) {
            List<T> list = this.mData;
            if (list != null) {
                list.remove(t);
                notifyDataSetChanged();
            }
        }
    }

    public void add(int i, T t) {
        this.mData.add(i, t);
        notifyDataSetChanged();
    }

    public void add(T t) {
        this.mData.add(t);
        notifyDataSetChanged();
    }

    @Nullable
    public T getItem(int i) {
        if (this.mData == null || i >= getItemCount() || i < 0) {
            return null;
        }
        return this.mData.get(i);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.mListener = onRecyclerViewListener;
    }
}
