package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import com.zipow.videobox.view.IMBuddyItem;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.StringUtil;

public class IMBuddyItemComparator implements Comparator<IMBuddyItem> {
    private Collator mCollator;
    private boolean mComparePresence;
    private boolean mCompareUnreadMsgCount;

    private int comparePresence(int i, int i2) {
        if (i != 0 || i2 == 0) {
            return (i2 != 0 || i == 0) ? 0 : 1;
        }
        return -1;
    }

    public IMBuddyItemComparator(Locale locale, boolean z, boolean z2) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
        this.mCompareUnreadMsgCount = z;
        this.mComparePresence = z2;
    }

    public int compare(@NonNull IMBuddyItem iMBuddyItem, @NonNull IMBuddyItem iMBuddyItem2) {
        if (iMBuddyItem == iMBuddyItem2) {
            return 0;
        }
        if (this.mCompareUnreadMsgCount) {
            if (iMBuddyItem.unreadMessageCount > 0 && iMBuddyItem2.unreadMessageCount == 0) {
                return -1;
            }
            if (iMBuddyItem2.unreadMessageCount > 0 && iMBuddyItem.unreadMessageCount == 0) {
                return 1;
            }
        }
        if (this.mComparePresence) {
            if (iMBuddyItem.isNoneFriend && !iMBuddyItem2.isNoneFriend) {
                return 1;
            }
            if (!iMBuddyItem.isNoneFriend && iMBuddyItem2.isNoneFriend) {
                return -1;
            }
            if (iMBuddyItem.isPending && !iMBuddyItem2.isPending) {
                return 1;
            }
            if (iMBuddyItem2.isPending && !iMBuddyItem.isPending) {
                return -1;
            }
            if (iMBuddyItem.presence != iMBuddyItem2.presence) {
                int comparePresence = comparePresence(iMBuddyItem.presence, iMBuddyItem2.presence);
                if (comparePresence != 0) {
                    return comparePresence;
                }
            }
        }
        return this.mCollator.compare(getItemSortKey(iMBuddyItem), getItemSortKey(iMBuddyItem2));
    }

    private String getItemSortKey(IMBuddyItem iMBuddyItem) {
        String str = iMBuddyItem.sortKey;
        if (!StringUtil.isEmptyOrNull(str)) {
            return str;
        }
        String str2 = iMBuddyItem.email;
        return str2 == null ? "" : str2;
    }
}
