package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import com.zipow.videobox.view.FavoriteItem;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.StringUtil;

public class FavoriteItemComparator implements Comparator<FavoriteItem> {
    private Collator mCollator;

    public FavoriteItemComparator(Locale locale) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
    }

    public int compare(@NonNull FavoriteItem favoriteItem, @NonNull FavoriteItem favoriteItem2) {
        String sortKey = favoriteItem.getSortKey();
        String sortKey2 = favoriteItem2.getSortKey();
        if (StringUtil.isEmptyOrNull(sortKey)) {
            sortKey = favoriteItem.getEmail();
            if (sortKey == null) {
                sortKey = "";
            }
        }
        if (StringUtil.isEmptyOrNull(sortKey2)) {
            sortKey2 = favoriteItem2.getEmail();
            if (sortKey2 == null) {
                sortKey2 = "";
            }
        }
        return this.mCollator.compare(sortKey, sortKey2);
    }
}
