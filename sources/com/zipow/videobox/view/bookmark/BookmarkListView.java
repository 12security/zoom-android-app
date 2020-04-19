package com.zipow.videobox.view.bookmark;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.bookmark.BookmarkListItemView.onBookmarkItemNotify;

public class BookmarkListView extends ListView implements onBookmarkItemNotify {
    /* access modifiers changed from: private */
    public boolean bEditMode = false;
    /* access modifiers changed from: private */
    public BookmarkAdapter mAdapter;
    /* access modifiers changed from: private */
    public BookmarkMgr mBookmarkMgr;
    /* access modifiers changed from: private */
    public onChangeListener mListener;

    public interface onChangeListener {
        void onDataChange();

        void onEditItem(int i);

        void onSelectItem(BookmarkItem bookmarkItem);
    }

    public BookmarkListView(Context context) {
        super(context);
        initView(context);
    }

    public BookmarkListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public BookmarkListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        if (!isInEditMode()) {
            this.mBookmarkMgr = new BookmarkMgr();
            this.mAdapter = new BookmarkAdapter(context, this);
            if (isInEditMode()) {
                for (int i = 0; i < 5; i++) {
                    this.mAdapter.addItem(new BookmarkItem());
                }
            }
            setAdapter(this.mAdapter);
            setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    if (i < BookmarkListView.this.mAdapter.getCount() && i >= 0) {
                        Object itemAtPosition = BookmarkListView.this.getItemAtPosition(i);
                        if (itemAtPosition != null && (itemAtPosition instanceof BookmarkItem)) {
                            int indexOf = BookmarkListView.this.mBookmarkMgr.indexOf(itemAtPosition);
                            if (BookmarkListView.this.bEditMode) {
                                if (BookmarkListView.this.mListener != null) {
                                    BookmarkListView.this.mListener.onEditItem(indexOf);
                                }
                            } else if (BookmarkListView.this.mListener != null) {
                                BookmarkListView.this.mListener.onSelectItem((BookmarkItem) itemAtPosition);
                            }
                        }
                    }
                }
            });
        }
    }

    private void notifyDataChange() {
        onChangeListener onchangelistener = this.mListener;
        if (onchangelistener != null) {
            onchangelistener.onDataChange();
        }
    }

    public void registerListener(onChangeListener onchangelistener) {
        this.mListener = onchangelistener;
    }

    public void setMode(boolean z) {
        boolean z2 = this.bEditMode;
        this.bEditMode = z;
        boolean z3 = this.bEditMode;
        if (z3 != z2) {
            this.mAdapter.setMode(z3);
            notifyDataChange();
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void reloadAllItems() {
        this.mAdapter.clear();
        loadAllItems();
        notifyDataChange();
        this.mAdapter.notifyDataSetChanged();
    }

    public void loadAllItems() {
        this.mBookmarkMgr.reload();
        this.mAdapter.addAll(this.mBookmarkMgr.getAll());
    }

    public int getItemCount() {
        BookmarkAdapter bookmarkAdapter = this.mAdapter;
        if (bookmarkAdapter == null) {
            return 0;
        }
        return bookmarkAdapter.getCount();
    }

    public void onDelete(@Nullable BookmarkItem bookmarkItem) {
        if (bookmarkItem != null) {
            this.mBookmarkMgr.remove((Object) bookmarkItem);
            this.mAdapter.remove(bookmarkItem);
            notifyDataChange();
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
