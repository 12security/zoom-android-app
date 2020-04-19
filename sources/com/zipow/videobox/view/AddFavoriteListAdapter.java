package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.FavoriteItemComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;

/* compiled from: AddFavoriteListView */
class AddFavoriteListAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_NORMAL = 0;
    @Nullable
    private Context mContext;
    @NonNull
    private List<AddFavoriteItem> mItems = new ArrayList();

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public AddFavoriteListAdapter(@Nullable Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItem(AddFavoriteItem addFavoriteItem) {
        updateItem(addFavoriteItem);
    }

    public void updateItem(@Nullable AddFavoriteItem addFavoriteItem) {
        int findItem = findItem(addFavoriteItem.getUserID());
        if (findItem >= 0) {
            this.mItems.set(findItem, addFavoriteItem);
        } else {
            this.mItems.add(addFavoriteItem);
        }
    }

    public int findItem(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mItems.size(); i++) {
            if (str.equals(((AddFavoriteItem) this.mItems.get(i)).getUserID())) {
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
    public AddFavoriteItem getItemByJid(String str) {
        for (AddFavoriteItem addFavoriteItem : this.mItems) {
            if (addFavoriteItem.getUserID().equals(str)) {
                return addFavoriteItem;
            }
        }
        return null;
    }

    public long getItemId(int i) {
        return (long) ((AddFavoriteItem) getItem(i)).getUserID().hashCode();
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = null;
        if (i < 0 || i >= getCount()) {
            return null;
        }
        if (getItemViewType(i) == 0) {
            view2 = createNormalItemView(i, view, viewGroup);
        }
        return view2;
    }

    private View createNormalItemView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return ((AddFavoriteItem) getItem(i)).getView(this.mContext, view);
    }

    public void sort() {
        Collections.sort(this.mItems, new FavoriteItemComparator(CompatUtils.getLocalDefault()));
    }
}
