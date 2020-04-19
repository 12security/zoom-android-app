package com.zipow.videobox.view;

import android.content.Context;
import com.zipow.videobox.tempbean.IMessageTemplateSelectItem;

public class MMSelectCustomListItemSpan extends RoundRectBackGroundSpan {
    IMessageTemplateSelectItem item;

    public MMSelectCustomListItemSpan(Context context, IMessageTemplateSelectItem iMessageTemplateSelectItem) {
        super(context);
        this.item = iMessageTemplateSelectItem;
    }

    public IMessageTemplateSelectItem getItem() {
        return this.item;
    }

    public void setItem(IMessageTemplateSelectItem iMessageTemplateSelectItem) {
        this.item = iMessageTemplateSelectItem;
    }
}
