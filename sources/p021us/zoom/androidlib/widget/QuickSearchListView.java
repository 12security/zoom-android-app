package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import java.util.HashMap;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.PullDownRefreshListView.PullDownRefreshListener;
import p021us.zoom.androidlib.widget.QuickSearchSideBar.IListener;

/* renamed from: us.zoom.androidlib.widget.QuickSearchListView */
public class QuickSearchListView extends FrameLayout implements IListener {
    public static final char LAST_GROUP_CATEGORY_CHAR = '翿';
    public static final char ROBOT_GROUP_CATEGORY_CHAR = '\"';
    protected static final int SIDEBAR_DISPLAY_THRESHOLD = 5;
    public static final char STARRED_GROUP_CATEGORY_CHAR = '!';
    private QuickSearchListAdapter mAdapter;
    private HashMap<Character, String> mCategoryHintsMap;
    private HashMap<Character, String> mCategoryTitlesMap;
    private TextView mEmptyView;
    /* access modifiers changed from: private */
    public PullDownRefreshListView mListView;
    /* access modifiers changed from: private */
    public OnItemClickListener mOnItemClickListener;
    /* access modifiers changed from: private */
    public OnItemLongClickListener mOnItemLongClickListener;
    /* access modifiers changed from: private */
    public OnScrollListener mOnScrollListener;
    /* access modifiers changed from: private */
    public OnTouchListener mOnTouchListener;
    private QuickSearchSideBar mQuickSearchSideBar;
    private TextView mTxtQuickSearchChar;
    private boolean mbQuickSearchEnabled = true;

    /* renamed from: us.zoom.androidlib.widget.QuickSearchListView$QuickSearchListDataAdapter */
    public static abstract class QuickSearchListDataAdapter extends BaseAdapter {
        public abstract String getItemSortKey(Object obj);

        public boolean isDataSorted() {
            return false;
        }
    }

    public QuickSearchListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public QuickSearchListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public QuickSearchListView(Context context) {
        super(context);
        initView(context);
    }

    public ListView getListView() {
        return this.mListView;
    }

    public void setEmptyViewText(@StringRes int i) {
        Context context = getContext();
        if (context != null) {
            setEmptyViewText(context.getString(i));
        }
    }

    public void setEmptyViewText(@NonNull String str) {
        this.mEmptyView.setText(str);
    }

    public void setPullDownRefreshEnabled(boolean z) {
        this.mListView.setPullDownRefreshEnabled(z);
    }

    public boolean isPullDownRefreshEnabled() {
        return this.mListView.isPullDownRefreshEnabled();
    }

    public void setPullDownRefreshListener(PullDownRefreshListener pullDownRefreshListener) {
        this.mListView.setPullDownRefreshListener(pullDownRefreshListener);
    }

    public void showRefreshing(boolean z) {
        this.mListView.showRefreshing(z);
    }

    public boolean isRefreshing() {
        return this.mListView.isRefreshing();
    }

    public void notifyRefreshDone() {
        this.mListView.notifyRefreshDone();
    }

    private void initView(Context context) {
        View.inflate(getContext(), C4409R.layout.zm_quick_search_listview, this);
        this.mListView = (PullDownRefreshListView) findViewById(C4409R.C4411id.listView);
        this.mEmptyView = (TextView) findViewById(C4409R.C4411id.emptyView);
        this.mQuickSearchSideBar = (QuickSearchSideBar) findViewById(C4409R.C4411id.quickSearchSideBar);
        this.mTxtQuickSearchChar = (TextView) findViewById(C4409R.C4411id.txtQuickSearchChar);
        this.mCategoryTitlesMap = buildDefaultCategoryTitlesFromSideBar(this.mQuickSearchSideBar);
        this.mCategoryHintsMap = buildDefaultCategoryTitlesFromSideBar(this.mQuickSearchSideBar);
        this.mQuickSearchSideBar.setQuickSearchSideBarListener(this);
        this.mAdapter = new QuickSearchListAdapter(context, this);
        this.mAdapter.setQuickSearchEnabled(isQuickSearchEnabled());
        this.mListView.setPullDownRefreshEnabled(false);
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setEmptyView(this.mEmptyView);
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if ((QuickSearchListView.this.mListView.getItemAtPosition(i) instanceof DataItem) && QuickSearchListView.this.mOnItemClickListener != null) {
                    QuickSearchListView.this.mOnItemClickListener.onItemClick(adapterView, view, i, j);
                }
            }
        });
        this.mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                if ((QuickSearchListView.this.mListView.getItemAtPosition(i) instanceof DataItem) && QuickSearchListView.this.mOnItemLongClickListener != null) {
                    QuickSearchListView.this.mOnItemLongClickListener.onItemLongClick(adapterView, view, i, j);
                }
                return true;
            }
        });
        this.mListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (QuickSearchListView.this.mOnTouchListener != null) {
                    QuickSearchListView.this.mOnTouchListener.onTouch(view, motionEvent);
                }
                return QuickSearchListView.this.mListView.onTouch(view, motionEvent);
            }
        });
        this.mListView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (QuickSearchListView.this.mOnScrollListener != null) {
                    QuickSearchListView.this.mOnScrollListener.onScrollStateChanged(absListView, i);
                }
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (QuickSearchListView.this.mOnScrollListener != null) {
                    QuickSearchListView.this.mOnScrollListener.onScroll(absListView, i, i2, i3);
                }
            }
        });
        setCategoryTitle(STARRED_GROUP_CATEGORY_CHAR, getContext().getString(C4409R.string.zm_starred_list_head_txt_65147));
        setCategoryTitle(32767, null);
    }

    public QuickSearchSideBar getQuickSearchSideBar() {
        return this.mQuickSearchSideBar;
    }

    public void setPullDownRefreshTextResources(int i, int i2, int i3) {
        this.mListView.setTextResources(i, i2, i3);
    }

    private HashMap<Character, String> buildDefaultCategoryTitlesFromSideBar(QuickSearchSideBar quickSearchSideBar) {
        HashMap<Character, String> hashMap = new HashMap<>();
        String categoryChars = quickSearchSideBar.getCategoryChars();
        String displayCharsFullSize = quickSearchSideBar.getDisplayCharsFullSize();
        String[] strArr = new String[categoryChars.length()];
        for (int i = 0; i < strArr.length; i++) {
            hashMap.put(Character.valueOf(categoryChars.charAt(i)), String.valueOf(displayCharsFullSize.charAt(i)));
        }
        return hashMap;
    }

    public void setCategoryChars(String str, String str2, String str3, String str4, String str5) {
        this.mQuickSearchSideBar.setCategoryChars(str, str2, str3, str4, str5);
        this.mCategoryTitlesMap = buildDefaultCategoryTitlesFromSideBar(this.mQuickSearchSideBar);
        this.mCategoryHintsMap = buildDefaultCategoryTitlesFromSideBar(this.mQuickSearchSideBar);
        QuickSearchListDataAdapter dataAdapter = this.mAdapter.getDataAdapter();
        if (dataAdapter != null) {
            dataAdapter.notifyDataSetChanged();
        }
    }

    public void setCategoryTitle(char c, String str) {
        HashMap<Character, String> hashMap = this.mCategoryTitlesMap;
        if (hashMap != null) {
            hashMap.put(Character.valueOf(c), str);
            QuickSearchListDataAdapter dataAdapter = this.mAdapter.getDataAdapter();
            if (dataAdapter != null) {
                dataAdapter.notifyDataSetChanged();
            }
        }
    }

    @Nullable
    public String getCategoryTitle(char c) {
        HashMap<Character, String> hashMap = this.mCategoryTitlesMap;
        if (hashMap == null) {
            return null;
        }
        return (String) hashMap.get(Character.valueOf(c));
    }

    public void setCategoryHint(char c, String str) {
        HashMap<Character, String> hashMap = this.mCategoryHintsMap;
        if (hashMap != null) {
            hashMap.put(Character.valueOf(c), str);
        }
    }

    @Nullable
    public String getCategoryHint(char c) {
        HashMap<Character, String> hashMap = this.mCategoryHintsMap;
        if (hashMap == null) {
            return null;
        }
        return (String) hashMap.get(Character.valueOf(c));
    }

    public void setQuickSearchEnabled(boolean z) {
        this.mbQuickSearchEnabled = z;
        int i = 8;
        if (getCount() == 0) {
            this.mQuickSearchSideBar.setVisibility(8);
        } else {
            QuickSearchSideBar quickSearchSideBar = this.mQuickSearchSideBar;
            if (this.mbQuickSearchEnabled) {
                i = 0;
            }
            quickSearchSideBar.setVisibility(i);
        }
        this.mAdapter.setQuickSearchEnabled(this.mbQuickSearchEnabled);
    }

    public boolean isQuickSearchEnabled() {
        return this.mbQuickSearchEnabled;
    }

    public void onResume() {
        this.mTxtQuickSearchChar.setVisibility(8);
    }

    private int getCount() {
        return this.mAdapter.getCount();
    }

    public int getDataItemCount() {
        QuickSearchListDataAdapter dataAdapter = this.mAdapter.getDataAdapter();
        if (dataAdapter == null) {
            return 0;
        }
        return dataAdapter.getCount();
    }

    @Nullable
    public Object getItemAtPosition(int i) {
        Object itemAtPosition = this.mListView.getItemAtPosition(i);
        if (itemAtPosition == null) {
            return null;
        }
        if (itemAtPosition instanceof DataItem) {
            return ((DataItem) itemAtPosition).data;
        }
        return ((GroupHeaderItem) itemAtPosition).title;
    }

    public void setSelectionFromTop(int i, int i2) {
        this.mListView.setSelectionFromTop(i, i2);
    }

    public int pointToPosition(int i, int i2) {
        return this.mListView.pointToPosition(i, i2);
    }

    public void setAdapter(QuickSearchListDataAdapter quickSearchListDataAdapter) {
        this.mAdapter.setDataAdapter(quickSearchListDataAdapter);
        this.mListView.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onQuickSearchCharPressed(char c) {
        scrollToChar(c);
        if (!StringUtil.isEmptyOrNull(getCategoryHint(c))) {
            this.mTxtQuickSearchChar.setText(getCategoryHint(c));
            this.mTxtQuickSearchChar.setVisibility(0);
            return;
        }
        this.mTxtQuickSearchChar.setVisibility(8);
    }

    public void onQuickSearchCharReleased(char c) {
        scrollToChar(c);
        this.mTxtQuickSearchChar.setVisibility(8);
    }

    public void scrollToChar(char c) {
        this.mListView.setSelection(this.mAdapter.getFirstItemPosFromChar(c));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;
    }

    public void setmOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setFooterDividersEnabled(boolean z) {
        this.mListView.setFooterDividersEnabled(z);
    }

    public void setHeaderDividersEnabled(boolean z) {
        this.mListView.setHeaderDividersEnabled(z);
    }

    /* access modifiers changed from: protected */
    public ListView getmmListView() {
        return this.mListView;
    }

    /* access modifiers changed from: protected */
    public void onDataChanged() {
        int i = 8;
        if (getDataItemCount() <= 5) {
            this.mQuickSearchSideBar.setVisibility(8);
            return;
        }
        QuickSearchSideBar quickSearchSideBar = this.mQuickSearchSideBar;
        if (isQuickSearchEnabled()) {
            i = 0;
        }
        quickSearchSideBar.setVisibility(i);
    }
}
