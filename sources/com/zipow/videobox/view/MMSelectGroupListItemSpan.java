package com.zipow.videobox.view;

import android.content.Context;
import com.zipow.videobox.view.p014mm.MMZoomGroup;

public class MMSelectGroupListItemSpan extends RoundRectBackGroundSpan {
    private MMZoomGroup mItem;

    public MMSelectGroupListItemSpan(Context context, MMZoomGroup mMZoomGroup) {
        super(context);
        this.mItem = mMZoomGroup;
    }

    public MMZoomGroup getItem() {
        return this.mItem;
    }

    public void setItem(MMZoomGroup mMZoomGroup) {
        this.mItem = mMZoomGroup;
    }
}
