package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.eventbus.ZMChatsSession;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.widget.recyclerview.OnRecyclerViewListener;

/* renamed from: com.zipow.videobox.view.mm.MMChatsListAdapter */
public class MMChatsListAdapter extends Adapter<BaseViewHolder> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final String TAG = "MMChatsListAdapter";
    @NonNull
    private List<MMChatsListItem> items = new ArrayList();
    @Nullable
    private Context mContext;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private boolean mLazyLoadAvatarDisabled = false;
    protected OnRecyclerViewListener mListener;
    @NonNull
    private List<String> mLoadedItems = new ArrayList();

    /* renamed from: com.zipow.videobox.view.mm.MMChatsListAdapter$BaseViewHolder */
    public static class BaseViewHolder extends ViewHolder {
        public BaseViewHolder(View view) {
            super(view);
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMChatsListAdapter$MMChatsListItemComparator */
    static class MMChatsListItemComparator implements Comparator<MMChatsListItem> {
        private boolean mUnreadAtTop;

        MMChatsListItemComparator() {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                this.mUnreadAtTop = notificationSettingMgr.keepAllUnreadChannelOnTop();
            }
        }

        public int compare(@NonNull MMChatsListItem mMChatsListItem, @NonNull MMChatsListItem mMChatsListItem2) {
            if (mMChatsListItem.isStarredMyNotes()) {
                return -1;
            }
            if (mMChatsListItem2.isStarredMyNotes()) {
                return 1;
            }
            if (mMChatsListItem.getMarkUnreadMessageCount() > 0 && mMChatsListItem2.getMarkUnreadMessageCount() == 0) {
                return -1;
            }
            if (mMChatsListItem.getMarkUnreadMessageCount() == 0 && mMChatsListItem2.getMarkUnreadMessageCount() > 0) {
                return 1;
            }
            if (this.mUnreadAtTop) {
                if (mMChatsListItem.getUnreadMessageCount() > 0 && mMChatsListItem2.getUnreadMessageCount() <= 0) {
                    return -1;
                }
                if (mMChatsListItem.getUnreadMessageCount() <= 0 && mMChatsListItem2.getUnreadMessageCount() > 0) {
                    return 1;
                }
            }
            int i = (max(mMChatsListItem.getDraftTimeStamp(), mMChatsListItem.getTimeStamp(), mMChatsListItem.getSearchOpenTimeStamp()) > max(mMChatsListItem2.getDraftTimeStamp(), mMChatsListItem2.getTimeStamp(), mMChatsListItem2.getSearchOpenTimeStamp()) ? 1 : (max(mMChatsListItem.getDraftTimeStamp(), mMChatsListItem.getTimeStamp(), mMChatsListItem.getSearchOpenTimeStamp()) == max(mMChatsListItem2.getDraftTimeStamp(), mMChatsListItem2.getTimeStamp(), mMChatsListItem2.getSearchOpenTimeStamp()) ? 0 : -1));
            if (i > 0) {
                return -1;
            }
            if (i < 0) {
                return 1;
            }
            return 0;
        }

        private long max(long j, long j2, long j3) {
            return Math.max(Math.max(j, j2), j3);
        }
    }

    public MMChatsListAdapter(@Nullable Context context) {
        this.mContext = context;
        registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                MMChatsListAdapter.this.rebuildChatsList();
            }
        });
    }

    public void addHeaderView(View view) {
        this.mHeaderViews.put(getHeaderItemCount() + BASE_ITEM_TYPE_HEADER, view);
    }

    public int getHeaderItemCount() {
        return this.mHeaderViews.size();
    }

    public boolean isHeaderView(int i) {
        return i >= 0 && i < getHeaderItemCount();
    }

    public void clearLoadedItemCache() {
        this.mLoadedItems.clear();
    }

    @NonNull
    public List<String> getLoadedItems() {
        return this.mLoadedItems;
    }

    public void clear() {
        this.items.clear();
    }

    public void addItem(@Nullable MMChatsListItem mMChatsListItem) {
        int findItem = findItem(mMChatsListItem.getSessionId());
        if (findItem >= 0) {
            this.items.set(findItem, mMChatsListItem);
        } else {
            this.items.add(mMChatsListItem);
        }
    }

    public boolean removeItem(@Nullable String str) {
        int findItem = findItem(str);
        if (findItem < 0) {
            return false;
        }
        this.items.remove(findItem);
        return true;
    }

    private int findItem(@Nullable String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < this.items.size(); i++) {
            if (str.equals(((MMChatsListItem) this.items.get(i)).getSessionId())) {
                return i;
            }
        }
        return -1;
    }

    @Nullable
    public MMChatsListItem getItemBySessionId(@Nullable String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < this.items.size(); i++) {
            MMChatsListItem mMChatsListItem = (MMChatsListItem) this.items.get(i);
            if (str.equals(mMChatsListItem.getSessionId())) {
                return mMChatsListItem;
            }
        }
        return null;
    }

    public int getItemCount() {
        return this.items.size() + getHeaderItemCount();
    }

    @Nullable
    public MMChatsListItem getItem(int i) {
        if (!isHeaderView(i) && i < getItemCount() && i >= getHeaderItemCount()) {
            return (MMChatsListItem) this.items.get(i - getHeaderItemCount());
        }
        return null;
    }

    public int getChatsItemsCount() {
        return this.items.size();
    }

    @Nullable
    public MMChatsListItem getChatsItem(int i) {
        if (i < 0 || i >= getChatsItemsCount()) {
            return null;
        }
        return (MMChatsListItem) this.items.get(i);
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        if (this.mHeaderViews.get(i) != null) {
            view = (View) this.mHeaderViews.get(i);
        } else {
            view = new MMChatsListItemView(viewGroup.getContext());
        }
        view.setLayoutParams(layoutParams);
        return new BaseViewHolder(view);
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (!isHeaderView(i) && baseViewHolder.getItemViewType() == 0) {
            bindView(baseViewHolder, i);
        }
    }

    public int getItemViewType(int i) {
        if (isHeaderView(i)) {
            return this.mHeaderViews.keyAt(i);
        }
        return 0;
    }

    private void bindView(@NonNull final BaseViewHolder baseViewHolder, int i) {
        MMChatsListItem item = getItem(i);
        if (item != null) {
            ((MMChatsListItemView) baseViewHolder.itemView).bindViews(item);
            this.mLoadedItems.add(item.getSessionId());
            baseViewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (MMChatsListAdapter.this.mListener != null) {
                        MMChatsListAdapter.this.mListener.onItemClick(baseViewHolder.itemView, baseViewHolder.getAdapterPosition());
                    }
                }
            });
            baseViewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (MMChatsListAdapter.this.mListener != null) {
                        return MMChatsListAdapter.this.mListener.onItemLongClick(baseViewHolder.itemView, baseViewHolder.getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }

    public void setLazyLoadAvatarDisabled(boolean z) {
        this.mLazyLoadAvatarDisabled = z;
    }

    private void sort() {
        Collections.sort(this.items, new MMChatsListItemComparator());
    }

    public void rebuildChatsList() {
        sort();
        if (this.items.size() > 0) {
            EventBus.getDefault().post(new ZMChatsSession());
        }
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.mListener = onRecyclerViewListener;
    }
}
