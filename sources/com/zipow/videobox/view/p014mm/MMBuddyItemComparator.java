package com.zipow.videobox.view.p014mm;

import androidx.annotation.NonNull;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.MMBuddyItemComparator */
public class MMBuddyItemComparator implements Comparator<MMBuddyItem> {
    private Collator mCollator;

    public MMBuddyItemComparator(Locale locale) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
    }

    public int compare(@NonNull MMBuddyItem mMBuddyItem, @NonNull MMBuddyItem mMBuddyItem2) {
        if (mMBuddyItem == mMBuddyItem2) {
            return 0;
        }
        return this.mCollator.compare(getItemSortKey(mMBuddyItem), getItemSortKey(mMBuddyItem2));
    }

    @NonNull
    private String getItemSortKey(MMBuddyItem mMBuddyItem) {
        String str = mMBuddyItem.sortKey;
        if (!StringUtil.isEmptyOrNull(str)) {
            return str;
        }
        String str2 = mMBuddyItem.email;
        return str2 == null ? "" : str2;
    }
}
