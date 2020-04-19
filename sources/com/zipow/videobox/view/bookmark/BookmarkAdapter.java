package com.zipow.videobox.view.bookmark;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.bookmark.BookmarkListItemView.onBookmarkItemNotify;
import java.util.ArrayList;
import java.util.Collection;

class BookmarkAdapter extends BaseAdapter {
    private boolean bEditMode = false;
    private Context mContext;
    @NonNull
    private ArrayList<BookmarkItem> mItems = new ArrayList<>();
    private onBookmarkItemNotify mObserver;

    public boolean areAllItemsEnabled() {
        return true;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEnabled(int i) {
        return true;
    }

    public BookmarkAdapter(Context context, onBookmarkItemNotify onbookmarkitemnotify) {
        this.mContext = context;
        this.mObserver = onbookmarkitemnotify;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItem(@Nullable BookmarkItem bookmarkItem) {
        if (bookmarkItem != null) {
            this.mItems.add(bookmarkItem);
        }
    }

    public boolean addAll(@Nullable Collection<? extends BookmarkItem> collection) {
        if (collection == null) {
            return false;
        }
        return this.mItems.addAll(collection);
    }

    public boolean remove(@Nullable BookmarkItem bookmarkItem) {
        if (bookmarkItem == null) {
            return false;
        }
        return this.mItems.remove(bookmarkItem);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        BookmarkListItemView bookmarkListItemView;
        Object item = getItem(i);
        if (item == null) {
            return null;
        }
        BookmarkItem bookmarkItem = (BookmarkItem) item;
        if (view instanceof BookmarkListItemView) {
            bookmarkListItemView = (BookmarkListItemView) view;
        } else {
            bookmarkListItemView = new BookmarkListItemView(this.mContext);
            bookmarkListItemView.registerListener(this.mObserver);
        }
        bookmarkListItemView.setMode(this.bEditMode);
        bookmarkListItemView.setBookmarkListItem(bookmarkItem);
        return bookmarkListItemView;
    }

    public void setMode(boolean z) {
        this.bEditMode = z;
    }
}
