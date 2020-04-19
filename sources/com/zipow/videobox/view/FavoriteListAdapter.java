package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.FavoriteItemComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;

/* compiled from: FavoriteListView */
class FavoriteListAdapter extends QuickSearchListDataAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_NORMAL = 0;
    @Nullable
    private Context mContext;
    @NonNull
    private List<FavoriteItem> mItems = new ArrayList();

    public long getItemId(int i) {
        return (long) i;
    }

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public FavoriteListAdapter(@Nullable Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItem(FavoriteItem favoriteItem) {
        updateItem(favoriteItem);
    }

    public void updateItem(@Nullable FavoriteItem favoriteItem) {
        int findItem = findItem(favoriteItem.getUserID());
        if (findItem >= 0) {
            this.mItems.set(findItem, favoriteItem);
        } else {
            this.mItems.add(favoriteItem);
        }
    }

    public int findItem(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mItems.size(); i++) {
            if (str.equals(((FavoriteItem) this.mItems.get(i)).getUserID())) {
                return i;
            }
        }
        return -1;
    }

    public void removeItem(String str) {
        int findItem = findItem(str);
        if (findItem >= 0) {
            removeItemAt(findItem);
        }
    }

    public void removeItemAt(int i) {
        if (i >= 0 && i < this.mItems.size()) {
            this.mItems.remove(i);
        }
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
    public FavoriteItem getItemByJid(String str) {
        for (FavoriteItem favoriteItem : this.mItems) {
            if (favoriteItem.getUserID().equals(str)) {
                return favoriteItem;
            }
        }
        return null;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return ((FavoriteItem) getItem(i)).getView(this.mContext, view);
    }

    public void sort(boolean z) {
        Collections.sort(this.mItems, new FavoriteItemComparator(CompatUtils.getLocalDefault()));
    }

    public String getItemSortKey(Object obj) {
        if (!(obj instanceof FavoriteItem)) {
            return "";
        }
        FavoriteItem favoriteItem = (FavoriteItem) obj;
        String sortKey = favoriteItem.getSortKey();
        if (StringUtil.isEmptyOrNull(sortKey)) {
            sortKey = favoriteItem.getEmail();
        }
        if (sortKey == null) {
            sortKey = "";
        }
        return sortKey;
    }
}
