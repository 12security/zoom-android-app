package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.IMBuddyItemComparator;
import com.zipow.videobox.util.MemCache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;

public class InviteBuddyListAdapter extends QuickSearchListDataAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_NORMAL = 0;
    private MemCache<String, Bitmap> mAvatarCache;
    @Nullable
    private Context mContext;
    @NonNull
    private List<InviteBuddyItem> mItems = new ArrayList();
    private boolean mLazyLoadAvatarDisabled = false;
    private List<String> mLoadedJids = new ArrayList();

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public InviteBuddyListAdapter(@Nullable Context context) {
        this.mContext = context;
    }

    public List<String> getmLoadedJids() {
        return this.mLoadedJids;
    }

    public void clearLoadedJids() {
        this.mLoadedJids.clear();
    }

    public void setmLoadedJids(List<String> list) {
        this.mLoadedJids = list;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void setAvatarMemCache(MemCache<String, Bitmap> memCache) {
        this.mAvatarCache = memCache;
    }

    public void addItem(@Nullable InviteBuddyItem inviteBuddyItem) {
        this.mItems.add(inviteBuddyItem);
    }

    public void updateItem(@Nullable InviteBuddyItem inviteBuddyItem) {
        int findItem = findItem(inviteBuddyItem.userId);
        if (findItem >= 0) {
            this.mItems.set(findItem, inviteBuddyItem);
        } else {
            this.mItems.add(inviteBuddyItem);
        }
    }

    public int findItem(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.mItems.size(); i++) {
            if (str.equals(((InviteBuddyItem) this.mItems.get(i)).userId)) {
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
    public InviteBuddyItem getItemByJid(String str) {
        for (InviteBuddyItem inviteBuddyItem : this.mItems) {
            if (inviteBuddyItem.userId.equals(str)) {
                return inviteBuddyItem;
            }
        }
        return null;
    }

    public long getItemId(int i) {
        return (long) ((InviteBuddyItem) getItem(i)).userId.hashCode();
    }

    public void filter(@NonNull String str) {
        for (int size = this.mItems.size() - 1; size >= 0; size--) {
            InviteBuddyItem inviteBuddyItem = (InviteBuddyItem) this.mItems.get(size);
            boolean z = false;
            boolean z2 = inviteBuddyItem.screenName != null && inviteBuddyItem.screenName.toLowerCase(CompatUtils.getLocalDefault()).contains(str);
            if (inviteBuddyItem.email != null && inviteBuddyItem.email.toLowerCase(CompatUtils.getLocalDefault()).contains(str)) {
                z = true;
            }
            if (!z2 && !z) {
                this.mItems.remove(size);
            }
        }
    }

    public void setLazyLoadAvatarDisabled(boolean z) {
        this.mLazyLoadAvatarDisabled = z;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = null;
        if (i < 0 || i >= getCount()) {
            return null;
        }
        if (getItemViewType(i) == 0) {
            view2 = createNormalItemView(i, view, viewGroup, this.mLazyLoadAvatarDisabled);
        }
        return view2;
    }

    private View createNormalItemView(int i, View view, ViewGroup viewGroup, boolean z) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        InviteBuddyItem inviteBuddyItem = (InviteBuddyItem) getItem(i);
        this.mLoadedJids.remove(inviteBuddyItem.userId);
        this.mLoadedJids.add(inviteBuddyItem.userId);
        return inviteBuddyItem.getView(this.mContext, view, this.mAvatarCache, z);
    }

    public void sort() {
        Collections.sort(this.mItems, new IMBuddyItemComparator(CompatUtils.getLocalDefault(), false, true));
    }

    @Nullable
    public String getItemSortKey(Object obj) {
        if (!(obj instanceof InviteBuddyItem)) {
            return "";
        }
        InviteBuddyItem inviteBuddyItem = (InviteBuddyItem) obj;
        String str = inviteBuddyItem.sortKey;
        if (StringUtil.isEmptyOrNull(str)) {
            str = inviteBuddyItem.email;
        }
        if (str == null) {
            str = "";
        }
        return str;
    }
}
