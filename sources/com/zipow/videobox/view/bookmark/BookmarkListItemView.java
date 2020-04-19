package com.zipow.videobox.view.bookmark;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class BookmarkListItemView extends LinearLayout implements OnClickListener {
    private boolean bEditMode = false;
    private ImageView mDelIcon;
    private ImageView mEditIcon;
    private BookmarkItem mItem;
    @Nullable
    private onBookmarkItemNotify mListener;
    private TextView mTextName;

    public interface onBookmarkItemNotify {
        void onDelete(BookmarkItem bookmarkItem);
    }

    public BookmarkListItemView(Context context) {
        super(context);
        initView(context);
    }

    public BookmarkListItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        if (from != null) {
            View inflate = from.inflate(C4558R.layout.zm_bookmark_item_view, this, true);
            this.mTextName = (TextView) inflate.findViewById(C4558R.C4560id.txtName);
            this.mDelIcon = (ImageView) inflate.findViewById(C4558R.C4560id.ImageDelIcon);
            this.mEditIcon = (ImageView) inflate.findViewById(C4558R.C4560id.ImageEditIcon);
            this.mDelIcon.setVisibility(8);
            this.mEditIcon.setVisibility(8);
            this.mDelIcon.setOnClickListener(this);
            this.mListener = null;
        }
    }

    public void setBookmarkListItem(BookmarkItem bookmarkItem) {
        this.mItem = bookmarkItem;
        if (!isInEditMode()) {
            BookmarkItem bookmarkItem2 = this.mItem;
            if (bookmarkItem2 != null) {
                String itemName = bookmarkItem2.getItemName();
                String itemUrl = this.mItem.getItemUrl();
                if (!StringUtil.isEmptyOrNull(itemUrl)) {
                    if (StringUtil.isEmptyOrNull(itemName)) {
                        itemName = itemUrl;
                    }
                    this.mTextName.setText(itemName);
                }
            }
        }
    }

    public void setMode(boolean z) {
        if (this.bEditMode != z) {
            this.bEditMode = z;
            if (z) {
                this.mDelIcon.setVisibility(0);
                this.mEditIcon.setVisibility(0);
                return;
            }
            this.mDelIcon.setVisibility(8);
            this.mEditIcon.setVisibility(8);
        }
    }

    public void onClick(View view) {
        onBookmarkItemNotify onbookmarkitemnotify = this.mListener;
        if (onbookmarkitemnotify != null && view == this.mDelIcon) {
            onbookmarkitemnotify.onDelete(this.mItem);
        }
    }

    public void registerListener(onBookmarkItemNotify onbookmarkitemnotify) {
        this.mListener = onbookmarkitemnotify;
    }

    public void unRegisterListener() {
        this.mListener = null;
    }

    @Nullable
    public String getItemTitle() {
        BookmarkItem bookmarkItem = this.mItem;
        if (bookmarkItem == null) {
            return null;
        }
        return bookmarkItem.getItemName();
    }

    @Nullable
    public String getItemUrl() {
        BookmarkItem bookmarkItem = this.mItem;
        if (bookmarkItem == null) {
            return null;
        }
        return bookmarkItem.getItemUrl();
    }
}
