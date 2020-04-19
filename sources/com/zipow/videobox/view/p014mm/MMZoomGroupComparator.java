package com.zipow.videobox.view.p014mm;

import androidx.annotation.NonNull;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;

/* renamed from: com.zipow.videobox.view.mm.MMZoomGroupComparator */
public class MMZoomGroupComparator implements Comparator<MMZoomGroup> {
    private Collator mCollator;

    public MMZoomGroupComparator(Locale locale) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
    }

    public int compare(@NonNull MMZoomGroup mMZoomGroup, @NonNull MMZoomGroup mMZoomGroup2) {
        if (mMZoomGroup == mMZoomGroup2) {
            return 0;
        }
        return this.mCollator.compare(getItemSortKey(mMZoomGroup), getItemSortKey(mMZoomGroup2));
    }

    private String getItemSortKey(MMZoomGroup mMZoomGroup) {
        return SortUtil.getSortKey(mMZoomGroup.getGroupName(), CompatUtils.getLocalDefault());
    }
}
