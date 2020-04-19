package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.TextCommandHelper;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;

/* renamed from: us.zoom.androidlib.widget.QuickSearchListAdapter */
/* compiled from: QuickSearchListView */
class QuickSearchListAdapter extends BaseAdapter {
    private static final int ITEM_TYPE_GROUP_HEADER = 0;
    private Context mContext;
    private QuickSearchListDataAdapter mDataAdapter;
    private DataSetObserver mDataAdapterObserver = new DataSetObserver() {
        public void onChanged() {
            rebuildItemsLater();
        }

        public void onInvalidated() {
            QuickSearchListAdapter.this.rebuildItems();
            QuickSearchListAdapter.this.notifyDataSetInvalidated();
        }

        private void rebuildItemsLater() {
            QuickSearchListAdapter.this.mHandler.removeCallbacks(QuickSearchListAdapter.this.mRebuildItemsRunnable);
            QuickSearchListAdapter.this.mHandler.postAtFrontOfQueue(QuickSearchListAdapter.this.mRebuildItemsRunnable);
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private ArrayList<Object> mItems = new ArrayList<>();
    private QuickSearchListView mListView;
    /* access modifiers changed from: private */
    public Runnable mRebuildItemsRunnable = new Runnable() {
        public void run() {
            QuickSearchListAdapter.this.rebuildItems();
            QuickSearchListAdapter.this.notifyDataSetChanged();
        }
    };
    private boolean mbQuickSearchEnabled = true;

    /* renamed from: us.zoom.androidlib.widget.QuickSearchListAdapter$DataItem */
    /* compiled from: QuickSearchListView */
    static class DataItem {
        Object data;
        int index;

        public DataItem(Object obj, int i) {
            this.data = obj;
            this.index = i;
        }
    }

    /* renamed from: us.zoom.androidlib.widget.QuickSearchListAdapter$DataItemComparator */
    /* compiled from: QuickSearchListView */
    static class DataItemComparator implements Comparator<DataItem> {
        private Collator mCollator;
        private QuickSearchListDataAdapter mDataAdapter;

        public DataItemComparator(Locale locale, QuickSearchListDataAdapter quickSearchListDataAdapter) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
            this.mDataAdapter = quickSearchListDataAdapter;
        }

        public int compare(DataItem dataItem, DataItem dataItem2) {
            if (dataItem == dataItem2) {
                return 0;
            }
            String itemSortKey = this.mDataAdapter.getItemSortKey(dataItem.data);
            String itemSortKey2 = this.mDataAdapter.getItemSortKey(dataItem2.data);
            if (itemSortKey == null) {
                itemSortKey = "";
            }
            if (itemSortKey2 == null) {
                itemSortKey2 = "";
            }
            if (itemSortKey.length() == 1 && itemSortKey.charAt(0) == 32767) {
                return 1;
            }
            if (itemSortKey2.length() == 1 && itemSortKey2.charAt(0) == 32767) {
                return -1;
            }
            return SortUtil.fastCompare(itemSortKey, itemSortKey2);
        }
    }

    /* renamed from: us.zoom.androidlib.widget.QuickSearchListAdapter$GroupHeaderItem */
    /* compiled from: QuickSearchListView */
    static class GroupHeaderItem {
        char key;
        String title;

        public GroupHeaderItem(char c, String str) {
            this.key = c;
            this.title = str;
        }
    }

    /* renamed from: us.zoom.androidlib.widget.QuickSearchListAdapter$QuickSearchListItemComparator */
    /* compiled from: QuickSearchListView */
    static class QuickSearchListItemComparator implements Comparator<Object> {
        private Collator mCollator;
        private QuickSearchListDataAdapter mDataAdapter;

        public QuickSearchListItemComparator(Locale locale, QuickSearchListDataAdapter quickSearchListDataAdapter) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
            this.mDataAdapter = quickSearchListDataAdapter;
        }

        public int compare(Object obj, Object obj2) {
            String str;
            String str2;
            if (obj == obj2) {
                return 0;
            }
            if (obj instanceof DataItem) {
                str = this.mDataAdapter.getItemSortKey(((DataItem) obj).data);
            } else if (obj instanceof GroupHeaderItem) {
                str = String.valueOf(((GroupHeaderItem) obj).key);
            } else {
                str = obj.toString();
            }
            if (obj2 instanceof DataItem) {
                str2 = this.mDataAdapter.getItemSortKey(((DataItem) obj2).data);
            } else if (obj2 instanceof GroupHeaderItem) {
                str2 = String.valueOf(((GroupHeaderItem) obj2).key);
            } else {
                str2 = obj2.toString();
            }
            return SortUtil.fastCompare(str, str2);
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public QuickSearchListAdapter(Context context, QuickSearchListView quickSearchListView) {
        this.mContext = context;
        this.mListView = quickSearchListView;
    }

    public void setQuickSearchEnabled(boolean z) {
        if (z != this.mbQuickSearchEnabled) {
            this.mbQuickSearchEnabled = z;
            rebuildItems();
        }
    }

    public void setDataAdapter(QuickSearchListDataAdapter quickSearchListDataAdapter) {
        this.mDataAdapter = quickSearchListDataAdapter;
        this.mDataAdapter.registerDataSetObserver(this.mDataAdapterObserver);
        rebuildItems();
    }

    public QuickSearchListDataAdapter getDataAdapter() {
        return this.mDataAdapter;
    }

    public int getFirstItemPosFromChar(char c) {
        int binarySearch = Collections.binarySearch(this.mItems, String.valueOf(c), new QuickSearchListItemComparator(CompatUtils.getLocalDefault(), this.mDataAdapter));
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 1;
        }
        return binarySearch + 1;
    }

    /* access modifiers changed from: private */
    public void rebuildItems() {
        if (this.mDataAdapter != null) {
            this.mItems.clear();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.mDataAdapter.getCount(); i++) {
                arrayList.add(new DataItem(this.mDataAdapter.getItem(i), i));
            }
            Locale localDefault = CompatUtils.getLocalDefault();
            if (this.mbQuickSearchEnabled && !this.mDataAdapter.isDataSorted()) {
                Collections.sort(arrayList, new DataItemComparator(localDefault, this.mDataAdapter));
            }
            Collator instance = Collator.getInstance(localDefault);
            instance.setStrength(0);
            GroupHeaderItem groupHeaderItem = null;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                DataItem dataItem = (DataItem) arrayList.get(i2);
                if (this.mbQuickSearchEnabled) {
                    char groupChar = getGroupChar(dataItem, instance);
                    if (groupHeaderItem == null || groupChar != groupHeaderItem.key) {
                        QuickSearchListView quickSearchListView = this.mListView;
                        String categoryTitle = quickSearchListView != null ? quickSearchListView.getCategoryTitle(groupChar) : null;
                        if (categoryTitle != null) {
                            groupHeaderItem = new GroupHeaderItem(groupChar, categoryTitle);
                            this.mItems.add(groupHeaderItem);
                        }
                    }
                }
                this.mItems.add(dataItem);
            }
            QuickSearchListView quickSearchListView2 = this.mListView;
            if (quickSearchListView2 != null) {
                quickSearchListView2.onDataChanged();
            }
        }
    }

    private char getGroupChar(DataItem dataItem, Collator collator) {
        String itemSortKey = this.mDataAdapter.getItemSortKey(dataItem.data);
        if (StringUtil.isEmptyOrNull(itemSortKey)) {
            return TextCommandHelper.CHANNEL_CMD_CHAR;
        }
        char charAt = itemSortKey.charAt(0);
        if ('\"' == charAt) {
            return '\"';
        }
        if ('!' == charAt) {
            return QuickSearchListView.STARRED_GROUP_CATEGORY_CHAR;
        }
        if (32767 == charAt) {
            return 32767;
        }
        if (charAt >= 'a' && charAt <= 'z') {
            return (char) (charAt - ' ');
        }
        if (charAt >= 'A' && charAt <= 'Z') {
            return charAt;
        }
        QuickSearchSideBar quickSearchSideBar = this.mListView.getQuickSearchSideBar();
        if (quickSearchSideBar == null || quickSearchSideBar.getCategoryChars().indexOf(itemSortKey) < 0) {
            return TextCommandHelper.CHANNEL_CMD_CHAR;
        }
        return itemSortKey.charAt(0);
    }

    public int getCount() {
        return this.mItems.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= this.mItems.size()) {
            return null;
        }
        return this.mItems.get(i);
    }

    public int getItemViewType(int i) {
        if (i < 0 || i >= this.mItems.size()) {
            return -1;
        }
        Object item = getItem(i);
        if (item instanceof DataItem) {
            return this.mDataAdapter.getItemViewType(((DataItem) item).index) + 1;
        } else if (item instanceof GroupHeaderItem) {
            return 0;
        } else {
            return -1;
        }
    }

    public boolean isEnabled(int i) {
        boolean z = true;
        if (!UIUtil.isTV(this.mContext)) {
            return true;
        }
        if (getItemViewType(i) == 0) {
            z = false;
        }
        return z;
    }

    public int getViewTypeCount() {
        QuickSearchListDataAdapter quickSearchListDataAdapter = this.mDataAdapter;
        if (quickSearchListDataAdapter == null) {
            return 2;
        }
        return quickSearchListDataAdapter.getViewTypeCount() + 1;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= this.mItems.size()) {
            return new View(this.mContext);
        }
        Object item = getItem(i);
        if (item instanceof DataItem) {
            View view2 = this.mDataAdapter.getView(((DataItem) item).index, view, viewGroup);
            if (view2 == null) {
                return new View(this.mContext);
            }
            int i2 = C4409R.C4411id.zm_quick_search_list_item_reset_padding_flag;
            if (!this.mListView.isQuickSearchEnabled() || this.mDataAdapter.getCount() <= 5) {
                Boolean bool = (Boolean) view2.getTag(i2);
                if (bool != null && bool.booleanValue()) {
                    view2.setPadding(view2.getPaddingLeft(), view2.getPaddingTop(), view2.getPaddingRight() - UIUtil.dip2px(this.mContext, 25.0f), view2.getPaddingBottom());
                    view2.setTag(i2, Boolean.FALSE);
                }
            } else {
                Boolean bool2 = (Boolean) view2.getTag(i2);
                if (bool2 == null || !bool2.booleanValue()) {
                    view2.setPadding(view2.getPaddingLeft(), view2.getPaddingTop(), view2.getPaddingRight() + UIUtil.dip2px(this.mContext, 25.0f), view2.getPaddingBottom());
                    view2.setTag(i2, Boolean.TRUE);
                }
            }
            return view2;
        } else if (item instanceof GroupHeaderItem) {
            return newGroupHeaderView(i, (GroupHeaderItem) item, view, viewGroup);
        } else {
            return new View(this.mContext);
        }
    }

    private View newGroupHeaderView(int i, GroupHeaderItem groupHeaderItem, View view, ViewGroup viewGroup) {
        if (view == null || !"us.zoom.androidlib.widget.QuickSearchListView.header".equals(view.getTag())) {
            view = LayoutInflater.from(this.mContext).inflate(C4409R.layout.zm_quick_search_list_items_header, viewGroup, false);
            view.setTag("us.zoom.androidlib.widget.QuickSearchListView.header");
        }
        ImageView imageView = (ImageView) view.findViewById(C4409R.C4411id.starredIcon);
        TextView textView = (TextView) view.findViewById(C4409R.C4411id.txtHeader);
        if (textView != null) {
            textView.setText(groupHeaderItem.title);
        }
        if (groupHeaderItem.key == '!') {
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(8);
        }
        return view;
    }
}
