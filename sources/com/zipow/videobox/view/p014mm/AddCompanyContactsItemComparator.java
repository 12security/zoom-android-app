package com.zipow.videobox.view.p014mm;

import androidx.annotation.NonNull;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.AddCompanyContactsItemComparator */
/* compiled from: AddCompanyContactsListView */
class AddCompanyContactsItemComparator implements Comparator<AddCompanyContactsItem> {
    private Collator mCollator;

    public AddCompanyContactsItemComparator(Locale locale) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
    }

    public int compare(@NonNull AddCompanyContactsItem addCompanyContactsItem, @NonNull AddCompanyContactsItem addCompanyContactsItem2) {
        String sortKey = addCompanyContactsItem.getSortKey();
        String sortKey2 = addCompanyContactsItem2.getSortKey();
        if (StringUtil.isEmptyOrNull(sortKey)) {
            sortKey = addCompanyContactsItem.getEmail();
            if (sortKey == null) {
                sortKey = "";
            }
        }
        if (StringUtil.isEmptyOrNull(sortKey2)) {
            sortKey2 = addCompanyContactsItem2.getEmail();
            if (sortKey2 == null) {
                sortKey2 = "";
            }
        }
        return this.mCollator.compare(sortKey, sortKey2);
    }
}
