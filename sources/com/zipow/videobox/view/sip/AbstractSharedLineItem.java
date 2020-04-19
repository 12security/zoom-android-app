package com.zipow.videobox.view.sip;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;

public abstract class AbstractSharedLineItem {

    /* renamed from: com.zipow.videobox.view.sip.AbstractSharedLineItem$1 */
    static /* synthetic */ class C39821 {

        /* renamed from: $SwitchMap$com$zipow$videobox$view$sip$AbstractSharedLineItem$SharedLineItemType */
        static final /* synthetic */ int[] f348x5b013909 = new int[SharedLineItemType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                com.zipow.videobox.view.sip.AbstractSharedLineItem$SharedLineItemType[] r0 = com.zipow.videobox.view.sip.AbstractSharedLineItem.SharedLineItemType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f348x5b013909 = r0
                int[] r0 = f348x5b013909     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.zipow.videobox.view.sip.AbstractSharedLineItem$SharedLineItemType r1 = com.zipow.videobox.view.sip.AbstractSharedLineItem.SharedLineItemType.ITEM_SHARED_LINE_USER     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = f348x5b013909     // Catch:{ NoSuchFieldError -> 0x001f }
                com.zipow.videobox.view.sip.AbstractSharedLineItem$SharedLineItemType r1 = com.zipow.videobox.view.sip.AbstractSharedLineItem.SharedLineItemType.ITEM_SHARED_LINE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.sip.AbstractSharedLineItem.C39821.<clinit>():void");
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int i);
    }

    public enum SharedLineItemType {
        ITEM_SHARED_LINE_USER,
        ITEM_SHARED_LINE
    }

    public abstract void bindViewHolder(BaseViewHolder baseViewHolder, @Nullable List<Object> list);

    public abstract int getViewType();

    @NonNull
    public static BaseViewHolder createViewHolder(ViewGroup viewGroup, int i, OnItemClickListener onItemClickListener) {
        if (i >= SharedLineItemType.values().length) {
            i = SharedLineItemType.ITEM_SHARED_LINE.ordinal();
        }
        if (C39821.f348x5b013909[SharedLineItemType.values()[i].ordinal()] != 1) {
            return SharedLineCallItem.createViewHolder(viewGroup, onItemClickListener);
        }
        return SharedLineUserItem.createViewHolder(viewGroup, onItemClickListener);
    }
}
