package com.zipow.videobox.view.p014mm.sticker;

import android.content.Context;
import android.widget.LinearLayout;
import java.util.List;

/* renamed from: com.zipow.videobox.view.mm.sticker.StickerPanelView */
public abstract class StickerPanelView extends LinearLayout {
    private int mIndexInCategor;
    protected OnStickerEventLisener mOnStickerEventLisener;

    /* renamed from: com.zipow.videobox.view.mm.sticker.StickerPanelView$OnStickerEventLisener */
    public interface OnStickerEventLisener {
        void onStickerEvent(StickerEvent stickerEvent);
    }

    public abstract int getCategory();

    public abstract int getMaxStickerSize();

    public void onStickerDownloaded(String str, int i) {
    }

    public abstract void setContent(List<StickerEvent> list);

    public StickerPanelView(Context context) {
        super(context);
    }

    public void setOnStickerEventListener(OnStickerEventLisener onStickerEventLisener) {
        this.mOnStickerEventLisener = onStickerEventLisener;
    }

    public int getIndexInCategory() {
        return this.mIndexInCategor;
    }

    public void setIndexInCategory(int i) {
        this.mIndexInCategor = i;
    }
}
