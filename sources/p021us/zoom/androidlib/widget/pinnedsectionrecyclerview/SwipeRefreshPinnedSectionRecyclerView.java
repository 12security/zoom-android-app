package p021us.zoom.androidlib.widget.pinnedsectionrecyclerview;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import java.util.Arrays;

/* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.SwipeRefreshPinnedSectionRecyclerView */
public class SwipeRefreshPinnedSectionRecyclerView extends SwipeRefreshLayout {
    private boolean mIsSwipeLoadMoreEnable = true;
    protected OnLoadListener mListener;
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            SwipeRefreshPinnedSectionRecyclerView.this.checkLoadMore(recyclerView);
        }
    };
    private PinnedSectionRecyclerView mRecyclerView;

    /* renamed from: us.zoom.androidlib.widget.pinnedsectionrecyclerview.SwipeRefreshPinnedSectionRecyclerView$OnLoadListener */
    public interface OnLoadListener {
        void loadMore();

        void refresh();
    }

    public SwipeRefreshPinnedSectionRecyclerView(Context context) {
        super(context);
        initViews();
    }

    public SwipeRefreshPinnedSectionRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    private void initViews() {
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        this.mRecyclerView = new PinnedSectionRecyclerView(getContext());
        this.mRecyclerView.setLayoutParams(layoutParams);
        addView(this.mRecyclerView);
        setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (SwipeRefreshPinnedSectionRecyclerView.this.mListener != null) {
                    SwipeRefreshPinnedSectionRecyclerView.this.mListener.refresh();
                }
            }
        });
        setOnScrollListener();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (VERSION.SDK_INT >= 16) {
                    SwipeRefreshPinnedSectionRecyclerView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    SwipeRefreshPinnedSectionRecyclerView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (SwipeRefreshPinnedSectionRecyclerView.this.mListener != null) {
                    SwipeRefreshPinnedSectionRecyclerView.this.mListener.refresh();
                }
            }
        });
    }

    private void setOnScrollListener() {
        this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }

    /* access modifiers changed from: private */
    public void checkLoadMore(RecyclerView recyclerView) {
        int i;
        if (this.mListener != null && this.mIsSwipeLoadMoreEnable) {
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null) {
                if (layoutManager instanceof GridLayoutManager) {
                    i = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int[] iArr = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastVisibleItemPositions(iArr);
                    Arrays.sort(iArr);
                    i = iArr[iArr.length - 1];
                } else {
                    i = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if (layoutManager.getChildCount() > 0 && layoutManager.getItemCount() > layoutManager.getChildCount() && i >= layoutManager.getItemCount() - 1) {
                    OnLoadListener onLoadListener = this.mListener;
                    if (onLoadListener != null) {
                        onLoadListener.loadMore();
                    }
                }
            }
        }
    }

    public PinnedSectionRecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public int getFirstVisiblePosition() {
        PinnedSectionRecyclerView pinnedSectionRecyclerView = this.mRecyclerView;
        int i = -1;
        if (pinnedSectionRecyclerView != null) {
            LayoutManager layoutManager = pinnedSectionRecyclerView.getLayoutManager();
            if (layoutManager == null) {
                return -1;
            }
            if (layoutManager instanceof GridLayoutManager) {
                i = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] iArr = new int[staggeredGridLayoutManager.getSpanCount()];
                if (iArr.length > 0) {
                    staggeredGridLayoutManager.findFirstVisibleItemPositions(iArr);
                    Arrays.sort(iArr);
                    i = iArr[0];
                }
            } else {
                i = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
        }
        return i;
    }

    public int getlastVisiblePosition() {
        PinnedSectionRecyclerView pinnedSectionRecyclerView = this.mRecyclerView;
        int i = -1;
        if (pinnedSectionRecyclerView != null) {
            LayoutManager layoutManager = pinnedSectionRecyclerView.getLayoutManager();
            if (layoutManager == null) {
                return -1;
            }
            if (layoutManager instanceof GridLayoutManager) {
                i = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] iArr = new int[staggeredGridLayoutManager.getSpanCount()];
                if (iArr.length > 0) {
                    staggeredGridLayoutManager.findLastVisibleItemPositions(iArr);
                    Arrays.sort(iArr);
                    i = iArr[iArr.length - 1];
                }
            } else {
                i = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        }
        return i;
    }

    public void enableSwipeLoadMore(boolean z) {
        this.mIsSwipeLoadMoreEnable = z;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mListener = onLoadListener;
    }
}
