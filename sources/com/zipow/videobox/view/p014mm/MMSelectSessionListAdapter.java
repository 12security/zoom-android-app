package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.MemCache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSelectSessionListAdapter */
public class MMSelectSessionListAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final String TAG_ACTION_ITEM = "actionItem";
    @NonNull
    private List<MMSelectSessionListItem> filteredItems = new ArrayList();
    @NonNull
    private List<MMSelectSessionListItem> items = new ArrayList();
    private MemCache<String, Drawable> mAvatarCache;
    @Nullable
    private Context mContext;
    private String mFilter;
    private boolean mLazyLoadAvatarDisabled = false;

    /* renamed from: com.zipow.videobox.view.mm.MMSelectSessionListAdapter$ActionItem */
    public static class ActionItem {
        public static final int ACTION_BACK_TO_MEETING = 2;
        public static final int ACTION_HOST_MEETING = 0;
        public static final int ACTION_JOIN_MEETING = 1;
        public int action = 0;
        @Nullable
        public String desc = null;
        public boolean enabled = true;
        public int icon = 0;
        @Nullable
        public String label = null;

        public ActionItem(int i, int i2, @Nullable String str, @Nullable String str2) {
            this.action = i;
            this.icon = i2;
            this.label = str;
            this.desc = str2;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSelectSessionListAdapter$MMSelectSessionListItemComparator */
    static class MMSelectSessionListItemComparator implements Comparator<MMSelectSessionListItem> {
        MMSelectSessionListItemComparator() {
        }

        public int compare(@NonNull MMSelectSessionListItem mMSelectSessionListItem, @NonNull MMSelectSessionListItem mMSelectSessionListItem2) {
            if (mMSelectSessionListItem.getTimeStamp() > mMSelectSessionListItem2.getTimeStamp()) {
                return -1;
            }
            return mMSelectSessionListItem.getTimeStamp() < mMSelectSessionListItem2.getTimeStamp() ? 1 : 0;
        }
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public MMSelectSessionListAdapter(@Nullable Context context) {
        this.mContext = context;
    }

    public void setAvatarCache(MemCache<String, Drawable> memCache) {
        this.mAvatarCache = memCache;
    }

    public void clear() {
        this.items.clear();
        this.filteredItems.clear();
    }

    public void addItem(@Nullable MMSelectSessionListItem mMSelectSessionListItem) {
        int findItem = findItem(mMSelectSessionListItem.getSessionId());
        if (findItem >= 0) {
            this.items.set(findItem, mMSelectSessionListItem);
        } else {
            this.items.add(mMSelectSessionListItem);
        }
    }

    public boolean removeItem(@Nullable String str) {
        int findItem = findItem(str);
        if (findItem >= 0) {
            this.items.remove(findItem);
            return true;
        }
        if (this.mFilter != null) {
            int i = 0;
            while (true) {
                if (i >= this.filteredItems.size()) {
                    break;
                }
                MMSelectSessionListItem mMSelectSessionListItem = (MMSelectSessionListItem) this.filteredItems.get(i);
                if (str != null && str.equals(mMSelectSessionListItem.getSessionId())) {
                    this.filteredItems.remove(i);
                    break;
                }
                i++;
            }
        }
        return false;
    }

    private int findItem(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.items.size(); i++) {
            if (str.equals(((MMSelectSessionListItem) this.items.get(i)).getSessionId())) {
                return i;
            }
        }
        return -1;
    }

    @Nullable
    public MMSelectSessionListItem getItemBySessionId(@Nullable String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < this.items.size(); i++) {
            MMSelectSessionListItem mMSelectSessionListItem = (MMSelectSessionListItem) this.items.get(i);
            if (str.equals(mMSelectSessionListItem.getSessionId())) {
                return mMSelectSessionListItem;
            }
        }
        return null;
    }

    public int getCount() {
        if (this.mFilter != null) {
            return this.filteredItems.size();
        }
        return this.items.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        if (this.mFilter != null) {
            return this.filteredItems.get(i);
        }
        return this.items.get(i);
    }

    public int getChatsItemsCount() {
        return this.items.size();
    }

    @Nullable
    public MMSelectSessionListItem getChatsItem(int i) {
        if (i < 0 || i >= getChatsItemsCount()) {
            return null;
        }
        return (MMSelectSessionListItem) this.items.get(i);
    }

    public int getItemViewType(int i) {
        return this.mFilter != null ? 0 : 0;
    }

    public void setLazyLoadAvatarDisabled(boolean z) {
        this.mLazyLoadAvatarDisabled = z;
    }

    private void sort() {
        Collections.sort(this.items, new MMSelectSessionListItemComparator());
    }

    public void notifyDataSetChanged() {
        sort();
        String str = this.mFilter;
        if (str != null) {
            forceFilter(str);
        }
        super.notifyDataSetChanged();
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        Object item = getItem(i);
        if (item == null) {
            return null;
        }
        if (item instanceof MMSelectSessionListItem) {
            return ((MMSelectSessionListItem) item).getView(this.mContext, view, viewGroup, this.mAvatarCache, this.mLazyLoadAvatarDisabled);
        } else if (item instanceof ActionItem) {
            return getActionItemView((ActionItem) item, this.mContext, view, viewGroup);
        } else {
            return null;
        }
    }

    public boolean isEnabled(int i) {
        Object item = getItem(i);
        if (item == null) {
            return false;
        }
        if (item instanceof MMSelectSessionListItem) {
            return true;
        }
        if (item instanceof ActionItem) {
            return ((ActionItem) item).enabled;
        }
        return false;
    }

    private View getActionItemView(@NonNull ActionItem actionItem, Context context, @Nullable View view, ViewGroup viewGroup) {
        LayoutInflater from = LayoutInflater.from(context);
        if (from == null) {
            return null;
        }
        if (view == null || !TAG_ACTION_ITEM.equals(view.getTag())) {
            view = from.inflate(C4558R.layout.zm_mm_chats_list_action_item, viewGroup, false);
            view.setTag(TAG_ACTION_ITEM);
        }
        ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgIcon);
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtDesc);
        if (imageView != null) {
            imageView.setImageResource(actionItem.icon);
            imageView.setEnabled(actionItem.enabled);
        }
        if (textView != null) {
            textView.setText(actionItem.label);
            textView.setEnabled(actionItem.enabled);
        }
        if (textView2 != null) {
            if (StringUtil.isEmptyOrNull(actionItem.desc)) {
                textView2.setVisibility(8);
            } else {
                textView2.setVisibility(8);
                textView2.setText(actionItem.desc);
                textView2.setEnabled(actionItem.enabled);
            }
        }
        view.setEnabled(actionItem.enabled);
        return view;
    }

    public void filter(String str) {
        String str2;
        if (StringUtil.isEmptyOrNull(str)) {
            str2 = null;
        } else {
            str2 = str.trim().toLowerCase(CompatUtils.getLocalDefault());
        }
        if (!StringUtil.isSameString(this.mFilter, str2)) {
            forceFilter(str2);
            super.notifyDataSetChanged();
        }
    }

    private void forceFilter(@Nullable String str) {
        this.mFilter = str;
        this.filteredItems.clear();
        if (this.mFilter != null) {
            Locale localDefault = CompatUtils.getLocalDefault();
            for (MMSelectSessionListItem mMSelectSessionListItem : this.items) {
                String title = mMSelectSessionListItem.getTitle();
                if (title != null && title.toLowerCase(localDefault).indexOf(str) >= 0) {
                    this.filteredItems.add(mMSelectSessionListItem);
                }
            }
        }
    }
}
