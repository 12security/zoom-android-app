package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import com.zipow.videobox.view.p014mm.MMBuddyItem;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.StringUtil;

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
        String sortKey = mMBuddyItem.getSortKey();
        if (!StringUtil.isEmptyOrNull(sortKey)) {
            return sortKey;
        }
        String screenName = mMBuddyItem.getScreenName();
        if (screenName == null) {
            screenName = mMBuddyItem.getContactName();
        }
        if (screenName == null) {
            screenName = mMBuddyItem.getPhoneNumber();
        }
        return screenName == null ? "" : screenName;
    }
}
