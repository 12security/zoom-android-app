package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.IMBuddyItemComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;

public class IMBuddyListAdapter extends QuickSearchListDataAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_NORMAL = 0;
    @Nullable
    private Context mContext;
    @NonNull
    private List<IMBuddyItem> mItems = new ArrayList();

    public long getItemId(int i) {
        return (long) i;
    }

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public IMBuddyListAdapter(@Nullable Context context) {
        this.mContext = context;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItem(@Nullable IMBuddyItem iMBuddyItem) {
        this.mItems.add(iMBuddyItem);
    }

    public void updateItem(@Nullable IMBuddyItem iMBuddyItem) {
        int findItem = findItem(iMBuddyItem.userId);
        if (findItem >= 0) {
            this.mItems.set(findItem, iMBuddyItem);
        } else {
            this.mItems.add(iMBuddyItem);
        }
    }

    public int findItem(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mItems.size(); i++) {
            if (str.equals(((IMBuddyItem) this.mItems.get(i)).userId)) {
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
    public IMBuddyItem getItemByJid(String str) {
        for (IMBuddyItem iMBuddyItem : this.mItems) {
            if (iMBuddyItem.userId.equals(str)) {
                return iMBuddyItem;
            }
        }
        return null;
    }

    public void filter(@Nullable String str) {
        for (int size = this.mItems.size() - 1; size >= 0; size--) {
            IMBuddyItem iMBuddyItem = (IMBuddyItem) this.mItems.get(size);
            if (StringUtil.isEmptyOrNull(iMBuddyItem.screenName) || (str != null && !iMBuddyItem.screenName.toLowerCase(CompatUtils.getLocalDefault()).contains(str))) {
                this.mItems.remove(size);
            }
        }
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        return ((IMBuddyItem) getItem(i)).getView(this.mContext, view, null, false);
    }

    public void sort(boolean z) {
        Collections.sort(this.mItems, new IMBuddyItemComparator(CompatUtils.getLocalDefault(), z, true));
    }

    @Nullable
    public String getItemSortKey(Object obj) {
        if (!(obj instanceof IMBuddyItem)) {
            return "";
        }
        IMBuddyItem iMBuddyItem = (IMBuddyItem) obj;
        String str = iMBuddyItem.sortKey;
        if (StringUtil.isEmptyOrNull(str)) {
            str = iMBuddyItem.email;
        }
        if (str == null) {
            str = "";
        }
        return str;
    }
}
