package com.zipow.videobox.poll;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class PollingResultListAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_NORMAL = 0;
    private Context mContext;
    @NonNull
    private ArrayList<PollingResultItem> mItems = new ArrayList<>();

    public long getItemId(int i) {
        return (long) i;
    }

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEnabled(int i) {
        return false;
    }

    public PollingResultListAdapter(Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItem(@Nullable PollingResultItem pollingResultItem) {
        this.mItems.add(pollingResultItem);
    }

    public int getCount() {
        return this.mItems.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return this.mItems.get(i);
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        PollingResultItem pollingResultItem = (PollingResultItem) getItem(i);
        if (pollingResultItem == null) {
            return null;
        }
        return pollingResultItem.getView(i, this.mContext, view, viewGroup);
    }
}
