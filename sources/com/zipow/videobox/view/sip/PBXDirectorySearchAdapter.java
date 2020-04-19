package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;

public class PBXDirectorySearchAdapter extends QuickSearchListDataAdapter {
    private Context mContext;
    @NonNull
    private List<IMAddrBookItem> mData = new ArrayList();
    private String mFilter;
    private int mFilterType;
    private boolean mSearchMode;

    public long getItemId(int i) {
        return 0;
    }

    public boolean isSearchMode() {
        return this.mSearchMode;
    }

    public void setSearchMode(boolean z) {
        this.mSearchMode = z;
    }

    public void setFilterType(int i) {
        this.mFilterType = i;
    }

    public PBXDirectorySearchAdapter(Context context) {
        this.mContext = context;
    }

    public void updateData(@Nullable List<IMAddrBookItem> list, String str) {
        if (list == null) {
            this.mData.clear();
        } else {
            this.mData.clear();
            this.mData.addAll(list);
        }
        this.mFilter = str;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mData.size();
    }

    @Nullable
    public IMAddrBookItem getItem(int i) {
        if (i < 0 || i >= this.mData.size()) {
            return null;
        }
        return (IMAddrBookItem) this.mData.get(i);
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        IMAddrBookItem item = getItem(i);
        if (item == null) {
            return new View(this.mContext);
        }
        return item.getPBXSearchView(this.mContext, view, false, true, this.mSearchMode, this.mFilter, this.mFilterType);
    }

    @Nullable
    public String getItemSortKey(Object obj) {
        if (obj instanceof IMAddrBookItem) {
            return ((IMAddrBookItem) obj).getSortKey();
        }
        return null;
    }
}
