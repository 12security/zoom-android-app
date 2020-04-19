package com.zipow.videobox.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.zipow.videobox.ptapp.IMProtos.GiphyMsgInfo;
import com.zipow.videobox.util.ImageLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class GiphyPreviewView extends LinearLayout {
    public static final int MODE_EMPTY_VIEW_NET_ERROR = 2;
    public static final int MODE_EMPTY_VIEW_NO_MATCH = 1;
    public static final int MODE_EMPTY_VIEW_PROGRESS = 0;
    private TextView mBtnBack;
    private TextView mEmptyViewTxt;
    /* access modifiers changed from: private */
    public GiphyPreviewAdapter mGiphyAdapter;
    /* access modifiers changed from: private */
    public OnGiphyPreviewItemClickListener mGiphyPreviewItemClickListener;
    private GridView mGridView;
    @NonNull
    private List<GiphyPreviewItem> mList = new ArrayList();
    private int mMode = 0;
    /* access modifiers changed from: private */
    public OnBackClickListener mOnBackClickListener;
    private View mPreviewLinear;
    private ProgressBar mProgressBar;
    /* access modifiers changed from: private */
    public EditText mSearchBar;
    /* access modifiers changed from: private */
    public TextView mSearchBtn;
    private OnSearchListener mSearchListener;

    private class GiphyPreviewAdapter extends BaseAdapter {
        private Context mContext;
        private ZMGifView mGifView;
        private List<GiphyPreviewItem> mList;

        public long getItemId(int i) {
            return (long) i;
        }

        public GiphyPreviewAdapter(Context context, List<GiphyPreviewItem> list) {
            this.mContext = context;
            this.mList = list;
        }

        public int getCount() {
            List<GiphyPreviewItem> list = this.mList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Nullable
        public Object getItem(int i) {
            List<GiphyPreviewItem> list = this.mList;
            if (list == null) {
                return null;
            }
            return (GiphyPreviewItem) list.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_mm_giphy_preview_item, viewGroup, false);
            }
            this.mGifView = (ZMGifView) view.findViewById(C4558R.C4560id.giphy_preview_item_gifView);
            this.mGifView.setImageResource(C4558R.color.zm_gray_2);
            GiphyPreviewItem giphyPreviewItem = (GiphyPreviewItem) getItem(i);
            if (giphyPreviewItem != null) {
                int dataNetworkType = NetworkUtil.getDataNetworkType(this.mContext);
                if (dataNetworkType == 1 || dataNetworkType == 4 || dataNetworkType == 3) {
                    ImageLoader.getInstance().displayGif(this.mGifView, (View) null, giphyPreviewItem.getInfo().getPcUrl());
                } else {
                    File cacheFile = ImageLoader.getInstance().getCacheFile(giphyPreviewItem.getInfo().getPcUrl());
                    if (cacheFile == null || !cacheFile.exists()) {
                        ImageLoader.getInstance().displayGif(this.mGifView, (View) null, giphyPreviewItem.getInfo().getMobileUrl());
                    } else {
                        ImageLoader.getInstance().displayGif(this.mGifView, (View) null, giphyPreviewItem.getInfo().getPcUrl());
                    }
                }
            }
            return view;
        }
    }

    public class GiphyPreviewItem {
        private String giphyStr;
        private GiphyMsgInfo info;

        public GiphyPreviewItem() {
        }

        public String getGiphyStr() {
            return this.giphyStr;
        }

        public void setGiphyStr(String str) {
            this.giphyStr = str;
        }

        public GiphyMsgInfo getInfo() {
            return this.info;
        }

        public void setInfo(GiphyMsgInfo giphyMsgInfo) {
            this.info = giphyMsgInfo;
        }
    }

    public interface OnBackClickListener {
        void onBackClick(View view);
    }

    public interface OnGiphyPreviewItemClickListener {
        void onGiphyPreviewItemClick(GiphyPreviewItem giphyPreviewItem);
    }

    public interface OnSearchListener {
        void onSearch(String str);
    }

    public GiphyPreviewView(Context context) {
        super(context);
        initView(context);
    }

    public GiphyPreviewView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public GiphyPreviewView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    @RequiresApi(api = 21)
    public GiphyPreviewView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    public void setPreviewVisible(int i) {
        this.mPreviewLinear.setVisibility(i);
    }

    private void initView(Context context) {
        inflate(context, C4558R.layout.zm_giphy_preview, this);
        this.mGridView = (GridView) findViewById(C4558R.C4560id.giphy_preview_gridView);
        this.mGridView.setEmptyView(findViewById(C4558R.C4560id.giphy_preview_emptyView));
        this.mBtnBack = (TextView) findViewById(C4558R.C4560id.giphy_preview_btn_back);
        this.mSearchBar = (EditText) findViewById(C4558R.C4560id.giphy_preview_search_bar);
        this.mSearchBtn = (TextView) findViewById(C4558R.C4560id.giphy_preview_search_btn);
        this.mPreviewLinear = findViewById(C4558R.C4560id.giphy_preview_linear);
        this.mEmptyViewTxt = (TextView) findViewById(C4558R.C4560id.giphy_preview_text);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.giphy_preview_progress);
        updateEmptyViewMode(this.mMode);
        if (hasData()) {
            this.mGridView.setVisibility(0);
            this.mGiphyAdapter = new GiphyPreviewAdapter(getContext(), this.mList);
            setAdapter(this.mGiphyAdapter);
        }
        this.mBtnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (GiphyPreviewView.this.mOnBackClickListener != null) {
                    GiphyPreviewView.this.mOnBackClickListener.onBackClick(view);
                }
            }
        });
        this.mSearchBar.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, @Nullable KeyEvent keyEvent) {
                if ((i != 3 && (keyEvent == null || keyEvent.getKeyCode() != 84)) || (keyEvent != null && keyEvent.getAction() != 1)) {
                    return false;
                }
                GiphyPreviewView.this.onSearch(GiphyPreviewView.this.mSearchBar.getText().toString().trim());
                return true;
            }
        });
        this.mSearchBar.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    GiphyPreviewView.this.onSearch("");
                    GiphyPreviewView.this.mSearchBtn.setVisibility(8);
                    return;
                }
                GiphyPreviewView.this.mSearchBtn.setVisibility(0);
            }
        });
        this.mSearchBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                GiphyPreviewView.this.onSearch(GiphyPreviewView.this.mSearchBar.getText().toString().trim());
            }
        });
        this.mGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (GiphyPreviewView.this.mGiphyAdapter != null && GiphyPreviewView.this.mGiphyPreviewItemClickListener != null) {
                    GiphyPreviewView.this.mGiphyPreviewItemClickListener.onGiphyPreviewItemClick((GiphyPreviewItem) GiphyPreviewView.this.mGiphyAdapter.getItem(i));
                }
            }
        });
    }

    public void updateEmptyViewMode(int i) {
        switch (i) {
            case 0:
                this.mMode = i;
                this.mProgressBar.setVisibility(0);
                this.mEmptyViewTxt.setVisibility(8);
                return;
            case 1:
                this.mMode = i;
                this.mProgressBar.setVisibility(8);
                this.mEmptyViewTxt.setVisibility(0);
                this.mEmptyViewTxt.setText(getResources().getString(C4558R.string.zm_mm_giphy_preview_no_match_22379));
                return;
            case 2:
                this.mMode = i;
                this.mProgressBar.setVisibility(8);
                this.mEmptyViewTxt.setVisibility(0);
                this.mEmptyViewTxt.setText(getResources().getString(C4558R.string.zm_mm_giphy_preview_net_error_22379));
                return;
            default:
                this.mProgressBar.setVisibility(0);
                this.mEmptyViewTxt.setVisibility(8);
                return;
        }
    }

    /* access modifiers changed from: private */
    public void onSearch(String str) {
        if (NetworkUtil.hasDataNetwork(getContext())) {
            updateEmptyViewMode(0);
        } else {
            updateEmptyViewMode(2);
        }
        this.mList.clear();
        GiphyPreviewAdapter giphyPreviewAdapter = this.mGiphyAdapter;
        if (giphyPreviewAdapter != null) {
            giphyPreviewAdapter.notifyDataSetChanged();
        }
        UIUtil.closeSoftKeyboard(getContext(), this.mSearchBar);
        OnSearchListener onSearchListener = this.mSearchListener;
        if (onSearchListener != null) {
            onSearchListener.onSearch(str);
        }
    }

    public void setDatas(@Nullable String str, @Nullable List<GiphyMsgInfo> list) {
        if (list == null || list.isEmpty()) {
            updateEmptyViewMode(1);
            return;
        }
        if (str == null) {
            str = "";
        }
        this.mGridView.setVisibility(0);
        this.mList.clear();
        for (GiphyMsgInfo giphyMsgInfo : list) {
            GiphyPreviewItem giphyPreviewItem = new GiphyPreviewItem();
            giphyPreviewItem.setGiphyStr(str);
            giphyPreviewItem.setInfo(giphyMsgInfo);
            this.mList.add(giphyPreviewItem);
        }
        this.mGiphyAdapter = new GiphyPreviewAdapter(getContext(), this.mList);
        setAdapter(this.mGiphyAdapter);
    }

    public boolean hasData() {
        return !this.mList.isEmpty();
    }

    private void setAdapter(ListAdapter listAdapter) {
        this.mGridView.setAdapter(listAdapter);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mGridView.setOnItemClickListener(onItemClickListener);
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.mSearchListener = onSearchListener;
    }

    public void setmGiphyPreviewItemClickListener(OnGiphyPreviewItemClickListener onGiphyPreviewItemClickListener) {
        this.mGiphyPreviewItemClickListener = onGiphyPreviewItemClickListener;
    }

    public void setmOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.mOnBackClickListener = onBackClickListener;
    }
}
