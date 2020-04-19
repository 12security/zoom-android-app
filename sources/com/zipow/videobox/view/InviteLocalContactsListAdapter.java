package com.zipow.videobox.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

public class InviteLocalContactsListAdapter extends QuickSearchListDataAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_ENABLE_ADDRBOOK = 1;
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final String TAG = "InviteLocalContactsListAdapter";
    @Nullable
    private Context mContext;
    private boolean mIsAddrBookEnabled = false;
    @NonNull
    private String mItemEnableAddrBook = "enableAddrBook";
    @NonNull
    private List<LocalContactItem> mItems = new ArrayList();
    private boolean mLazyLoadAvatarDisabled = false;
    /* access modifiers changed from: private */
    public InviteLocalContactsListView mListView;

    static class LocalContactItemComparator implements Comparator<LocalContactItem> {
        private Collator mCollator;

        public LocalContactItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull LocalContactItem localContactItem, @NonNull LocalContactItem localContactItem2) {
            if (localContactItem == localContactItem2) {
                return 0;
            }
            if (localContactItem.getIsZoomUser() && !localContactItem2.getIsZoomUser()) {
                return 1;
            }
            if (!localContactItem.getIsZoomUser() && localContactItem2.getIsZoomUser()) {
                return -1;
            }
            String sortKey = localContactItem.getSortKey();
            String sortKey2 = localContactItem2.getSortKey();
            if (sortKey == null) {
                sortKey = "";
            }
            if (sortKey2 == null) {
                sortKey2 = "";
            }
            return this.mCollator.compare(sortKey, sortKey2);
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public InviteLocalContactsListAdapter(@Nullable Context context, InviteLocalContactsListView inviteLocalContactsListView) {
        this.mContext = context;
        this.mListView = inviteLocalContactsListView;
    }

    public void setAddrBookEnabled(boolean z) {
        this.mIsAddrBookEnabled = z;
    }

    @NonNull
    public List<LocalContactItem> cache() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mItems);
        return arrayList;
    }

    public void setItems(@NonNull List<LocalContactItem> list) {
        this.mItems.clear();
        this.mItems.addAll(list);
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItem(@Nullable LocalContactItem localContactItem) {
        this.mItems.add(localContactItem);
    }

    public int getCount() {
        if (this.mIsAddrBookEnabled) {
            return this.mItems.size();
        }
        return 1;
    }

    public int getContactsItemCount() {
        return this.mItems.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        if (this.mIsAddrBookEnabled || i != 0) {
            return this.mItems.get(i);
        }
        return this.mItemEnableAddrBook;
    }

    public int getItemViewType(int i) {
        return (this.mIsAddrBookEnabled || i != 0) ? 0 : 1;
    }

    public void setLazyLoadAvatarDisabled(boolean z) {
        this.mLazyLoadAvatarDisabled = z;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return new View(this.mContext);
        }
        Object item = getItem(i);
        if (item instanceof LocalContactItem) {
            return ((LocalContactItem) item).getView(this.mContext, view, this.mListView, this.mLazyLoadAvatarDisabled);
        }
        if (this.mItemEnableAddrBook.equals(item)) {
            return getEnableAddrbookView(this.mContext, view, viewGroup);
        }
        return new View(this.mContext);
    }

    @Nullable
    private View getEnableAddrbookView(@Nullable Context context, @Nullable View view, ViewGroup viewGroup) {
        if (context == null) {
            return null;
        }
        if (view == null || !"enableAddrBook".equals(view.getTag())) {
            view = LayoutInflater.from(context).inflate(C4558R.layout.zm_addrbook_item_enable_addrbook_matching, viewGroup, false);
            view.setTag("enableAddrBook");
        }
        Button button = (Button) view.findViewById(C4558R.C4560id.btnEnable);
        if (button != null) {
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    InviteLocalContactsListAdapter.this.mListView.onClickBtnEnableAddrBook();
                }
            });
        }
        return view;
    }

    public void filter(@NonNull String str) {
        filter(this.mItems, str);
    }

    private void filter(List<LocalContactItem> list, @NonNull String str) {
        for (int size = list.size() - 1; size >= 0; size--) {
            String screenName = ((LocalContactItem) list.get(size)).getScreenName();
            if (StringUtil.isEmptyOrNull(screenName) || !screenName.toLowerCase(CompatUtils.getLocalDefault()).contains(str)) {
                list.remove(size);
            }
        }
    }

    public String getItemSortKey(Object obj) {
        if (!(obj instanceof LocalContactItem)) {
            return "*";
        }
        String sortKey = ((LocalContactItem) obj).getSortKey();
        if (sortKey == null) {
            sortKey = "";
        }
        return sortKey;
    }

    public void sort() {
        Collections.sort(this.mItems, new LocalContactItemComparator(CompatUtils.getLocalDefault()));
    }
}
