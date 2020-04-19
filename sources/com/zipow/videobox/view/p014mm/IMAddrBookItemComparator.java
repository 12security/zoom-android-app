package com.zipow.videobox.view.p014mm;

import androidx.annotation.NonNull;
import com.zipow.videobox.view.IMAddrBookItem;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/* renamed from: com.zipow.videobox.view.mm.IMAddrBookItemComparator */
public class IMAddrBookItemComparator implements Comparator<IMAddrBookItem> {
    private Collator mCollator;

    public IMAddrBookItemComparator(Locale locale) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
    }

    public int compare(@NonNull IMAddrBookItem iMAddrBookItem, @NonNull IMAddrBookItem iMAddrBookItem2) {
        if (iMAddrBookItem == iMAddrBookItem2) {
            return 0;
        }
        String sortKey = iMAddrBookItem.getSortKey();
        String sortKey2 = iMAddrBookItem2.getSortKey();
        if (sortKey == null) {
            sortKey = "";
        }
        if (sortKey2 == null) {
            sortKey2 = "";
        }
        return this.mCollator.compare(sortKey, sortKey2);
    }
}
