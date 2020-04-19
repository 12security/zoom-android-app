package com.zipow.videobox.view;

import android.content.Context;
import com.zipow.videobox.view.p014mm.MMSelectContactsListItem;

public class MMSelectContactsListItemSpan extends RoundRectBackGroundSpan {
    private MMSelectContactsListItem mItem;

    public MMSelectContactsListItemSpan(Context context, MMSelectContactsListItem mMSelectContactsListItem) {
        super(context);
        this.mItem = mMSelectContactsListItem;
    }

    public MMSelectContactsListItem getItem() {
        return this.mItem;
    }

    public void setItem(MMSelectContactsListItem mMSelectContactsListItem) {
        this.mItem = mMSelectContactsListItem;
    }
}
