package com.zipow.videobox.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.p014mm.IMAddrBookItemComparator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

public class IMAddrBookListAdapter extends QuickSearchListDataAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_OTHER_CONTACTS = 1;
    private static final int ITEM_TYPE_SEARCH_MORE = 2;
    public static final char SEARCH_MODE_SORT_KEY = '翿';
    private static final String TAG = "IMAddrBookListAdapter";
    @Nullable
    private Context mContext;
    @Nullable
    private String mFilter = null;
    private boolean mHasWebSearchResults = false;
    @Nullable
    private ItemOtherContacts mItemAccountContacts = null;
    @NonNull
    private String mItemSearchMore = "searchMode";
    @NonNull
    private List<IMAddrBookItem> mItems = new ArrayList();
    private boolean mLazyLoadAvatarDisabled = false;
    private IMAddrBookListView mListView;
    @NonNull
    private HashMap<String, IMAddrBookItem> mSearchMap = new HashMap<>();
    @NonNull
    private List<String> mWaitRefreshJids = new ArrayList();

    public static class ItemOtherContacts {
        public static final int TYPE_FACEBOOK_CONTACTS = 2;
        public static final int TYPE_GOOGLE_CONTACTS = 1;
        public static final int TYPE_ZOOM_CONTACTS = 0;
        public int type = 0;

        public ItemOtherContacts(int i) {
            this.type = i;
        }

        @Nullable
        public View getView(@Nullable Context context, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !"ItemOtherContacts".equals(view.getTag())) {
                if (context == null) {
                    return null;
                }
                view = LayoutInflater.from(context).inflate(C4558R.layout.zm_addrbook_item_other_contacts, viewGroup, false);
                view.setTag("ItemOtherContacts");
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtTitle);
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.icon);
            switch (this.type) {
                case 0:
                    textView.setText(C4558R.string.zm_lbl_zoom_contacts);
                    imageView.setImageResource(C4558R.C4559drawable.zm_ic_other_contacts_fav);
                    break;
                case 1:
                    textView.setText(C4558R.string.zm_lbl_google_contacts);
                    imageView.setImageResource(C4558R.C4559drawable.zm_ic_other_contacts_google);
                    break;
                case 2:
                    textView.setText(C4558R.string.zm_lbl_facebook_contacts);
                    imageView.setImageResource(C4558R.C4559drawable.zm_ic_other_contacts_fb);
                    break;
            }
            return view;
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getViewTypeCount() {
        return 4;
    }

    public IMAddrBookListAdapter(@Nullable Context context, IMAddrBookListView iMAddrBookListView) {
        this.mContext = context;
        this.mListView = iMAddrBookListView;
        initItemAccountContacts();
    }

    private void initItemAccountContacts() {
        int pTLoginType = PTApp.getInstance().getPTLoginType();
        if (pTLoginType != 0) {
            if (pTLoginType != 2) {
                switch (pTLoginType) {
                }
            } else if (PTApp.getInstance().isGoogleImEnabled()) {
                this.mItemAccountContacts = new ItemOtherContacts(1);
            }
        } else if (PTApp.getInstance().isFacebookImEnabled()) {
            this.mItemAccountContacts = new ItemOtherContacts(2);
        }
    }

    @NonNull
    public List<IMAddrBookItem> cache() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mItems);
        return arrayList;
    }

    public void addItems(@NonNull Collection<IMAddrBookItem> collection) {
        for (IMAddrBookItem iMAddrBookItem : collection) {
            if (!iMAddrBookItem.isZoomRoomContact()) {
                this.mItems.add(iMAddrBookItem);
                this.mSearchMap.put(iMAddrBookItem.getJid(), iMAddrBookItem);
            }
        }
        Collections.sort(this.mItems, new IMAddrBookItemComparator(CompatUtils.getLocalDefault()));
    }

    public void addItems(@Nullable Collection<IMAddrBookItem> collection, boolean z, @Nullable String str) {
        if (str != null) {
            str = str.toLowerCase(CompatUtils.getLocalDefault());
        }
        this.mFilter = str;
        this.mItems.clear();
        this.mSearchMap.clear();
        if (!z || !StringUtil.isEmptyOrNull(str) || collection == null) {
            if (collection != null) {
                for (IMAddrBookItem iMAddrBookItem : collection) {
                    if (!TextUtils.isEmpty(iMAddrBookItem.getScreenName()) && !iMAddrBookItem.isZoomRoomContact()) {
                        if (!StringUtil.isEmptyOrNull(str)) {
                            String screenName = iMAddrBookItem.getScreenName();
                            String accountEmail = iMAddrBookItem.getAccountEmail();
                            if ((screenName != null && screenName.toLowerCase(CompatUtils.getLocalDefault()).contains(str)) || (accountEmail != null && accountEmail.toLowerCase(CompatUtils.getLocalDefault()).contains(str))) {
                                this.mItems.add(iMAddrBookItem);
                                this.mSearchMap.put(iMAddrBookItem.getJid(), iMAddrBookItem);
                            }
                        } else if (z) {
                            this.mItems.add(iMAddrBookItem);
                            this.mSearchMap.put(iMAddrBookItem.getJid(), iMAddrBookItem);
                        } else if (iMAddrBookItem.getIsDesktopOnline() || iMAddrBookItem.getIsMobileOnline()) {
                            this.mItems.add(iMAddrBookItem);
                            this.mSearchMap.put(iMAddrBookItem.getJid(), iMAddrBookItem);
                        }
                    }
                }
            }
            Collections.sort(this.mItems, new IMAddrBookItemComparator(CompatUtils.getLocalDefault()));
            return;
        }
        for (IMAddrBookItem iMAddrBookItem2 : collection) {
            if (!TextUtils.isEmpty(iMAddrBookItem2.getScreenName()) && !iMAddrBookItem2.isZoomRoomContact()) {
                this.mItems.add(iMAddrBookItem2);
                this.mSearchMap.put(iMAddrBookItem2.getJid(), iMAddrBookItem2);
            }
        }
    }

    public void clear() {
        this.mItems.clear();
        this.mSearchMap.clear();
    }

    public void addItem(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (!TextUtils.isEmpty(iMAddrBookItem.getScreenName()) && !iMAddrBookItem.isZoomRoomContact() && !hasItemWithJid(iMAddrBookItem.getJid())) {
            this.mItems.add(iMAddrBookItem);
            this.mSearchMap.put(iMAddrBookItem.getJid(), iMAddrBookItem);
        }
    }

    public boolean hasItemWithJid(String str) {
        return this.mSearchMap.get(str) != null;
    }

    public int getCount() {
        int size = this.mItems.size();
        if (this.mItemAccountContacts != null) {
            size++;
        }
        return hasSearchMoreItem() ? size + 1 : size;
    }

    private boolean hasSearchMoreItem() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        int coWorkersCount = zoomMessenger.getCoWorkersCount();
        if (!this.mHasWebSearchResults) {
            String str = this.mFilter;
            if (str != null && str.length() >= 3 && coWorkersCount >= 199) {
                z = true;
            }
        }
        return z;
    }

    public void setHasWebSearchResults(boolean z) {
        this.mHasWebSearchResults = z;
    }

    public int getContactsItemCount() {
        return this.mItems.size();
    }

    @NonNull
    public List<String> getWaitRefreshJids() {
        return this.mWaitRefreshJids;
    }

    public void clearWaitRefreshJids() {
        this.mWaitRefreshJids.clear();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        ItemOtherContacts itemOtherContacts = this.mItemAccountContacts;
        if (itemOtherContacts != null && i == 0) {
            return itemOtherContacts;
        }
        if (!hasSearchMoreItem() || i != getCount() - 1) {
            return this.mItems.get(i);
        }
        return this.mItemSearchMore;
    }

    public int getItemViewType(int i) {
        if (this.mItemAccountContacts == null || i != 0) {
            return (!hasSearchMoreItem() || i != getCount() - 1) ? 0 : 2;
        }
        return 1;
    }

    public void setLazyLoadAvatarDisabled(boolean z) {
        this.mLazyLoadAvatarDisabled = z;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return new View(this.mContext);
        }
        Object item = getItem(i);
        if (item instanceof IMAddrBookItem) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) item;
            this.mWaitRefreshJids.add(iMAddrBookItem.getJid());
            return iMAddrBookItem.getView(this.mContext, view, this.mLazyLoadAvatarDisabled, false);
        } else if (item instanceof ItemOtherContacts) {
            return ((ItemOtherContacts) item).getView(this.mContext, view, viewGroup);
        } else {
            if (this.mItemSearchMore.equals(item)) {
                return getSearchMoreItem(this.mContext, view, viewGroup);
            }
            return new View(this.mContext);
        }
    }

    @NonNull
    private View getSearchMoreItem(Context context, @Nullable View view, ViewGroup viewGroup) {
        if (view == null || !this.mItemSearchMore.equals(view.getTag())) {
            view = LayoutInflater.from(context).inflate(C4558R.layout.zm_addrbook_item_search_more, viewGroup, false);
            view.setTag(this.mItemSearchMore);
        }
        view.findViewById(C4558R.C4560id.btnSearchMore).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                IMAddrBookListAdapter.this.onClickSearchMore();
            }
        });
        return view;
    }

    /* access modifiers changed from: private */
    public void onClickSearchMore() {
        IMAddrBookListView iMAddrBookListView = this.mListView;
        if (iMAddrBookListView != null) {
            iMAddrBookListView.onClickSearchMore();
        }
    }

    public void filter(@NonNull String str) {
        this.mFilter = str;
        if (!StringUtil.isEmptyOrNull(str)) {
            filter(this.mItems, str);
        }
    }

    private void filter(List<IMAddrBookItem> list, @NonNull String str) {
        for (int size = list.size() - 1; size >= 0; size--) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) list.get(size);
            String screenName = iMAddrBookItem.getScreenName();
            String accountEmail = iMAddrBookItem.getAccountEmail();
            boolean z = false;
            boolean z2 = screenName != null && screenName.toLowerCase(CompatUtils.getLocalDefault()).contains(str);
            if (accountEmail != null && accountEmail.toLowerCase(CompatUtils.getLocalDefault()).contains(str)) {
                z = true;
            }
            if (!z2 && !z) {
                list.remove(size);
            }
        }
    }

    public String getItemSortKey(Object obj) {
        if (!(obj instanceof IMAddrBookItem)) {
            return this.mItemSearchMore.equals(obj) ? String.valueOf(32767) : "*";
        }
        String sortKey = ((IMAddrBookItem) obj).getSortKey();
        if (sortKey == null) {
            sortKey = "";
        }
        return sortKey;
    }
}
