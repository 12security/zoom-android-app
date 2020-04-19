package com.zipow.videobox.ptapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class ContactsMatchHelper {
    private static final int FULL_MATCH_DURATION = 86400000;
    private static final String TAG = "ContactsMatchHelper";
    @Nullable
    private static ContactsMatchHelper instance;
    private static long mLastestSyncTime;

    @NonNull
    public static synchronized ContactsMatchHelper getInstance() {
        ContactsMatchHelper contactsMatchHelper;
        synchronized (ContactsMatchHelper.class) {
            if (instance == null) {
                instance = new ContactsMatchHelper();
            }
            contactsMatchHelper = instance;
        }
        return contactsMatchHelper;
    }

    public int matchAllNumbers(@Nullable Context context) {
        if (context == null) {
            return 11;
        }
        ABContactsCache instance2 = ABContactsCache.getInstance();
        if (!instance2.isCached() && !instance2.reloadAllContacts()) {
            return -1;
        }
        boolean z = CmmTime.getMMNow() - mLastestSyncTime > 86400000;
        mLastestSyncTime = CmmTime.getMMNow();
        ArrayList arrayList = new ArrayList();
        Collator instance3 = Collator.getInstance(Locale.US);
        for (int i = 0; i < instance2.getCachedContactsCount(); i++) {
            Contact cachedContact = instance2.getCachedContact(i);
            if (cachedContact != null) {
                String str = cachedContact.normalizedNumber;
                if (!StringUtil.isEmptyOrNull(str)) {
                    int binarySearch = Collections.binarySearch(arrayList, str, instance3);
                    if (binarySearch < 0) {
                        arrayList.add((-binarySearch) - 1, str);
                    }
                }
            }
        }
        int matchNumbers = matchNumbers(arrayList, z);
        if (matchNumbers == 0) {
            instance2.clearNewContacts();
        }
        return matchNumbers;
    }

    public int matchNewNumbers(@Nullable Context context) {
        if (context == null) {
            return 11;
        }
        ABContactsCache instance2 = ABContactsCache.getInstance();
        ArrayList newPhoneNos = instance2.getNewPhoneNos();
        if (CollectionsUtil.isCollectionEmpty(newPhoneNos)) {
            return -1;
        }
        int matchNumbers = matchNumbers(newPhoneNos, false);
        if (matchNumbers == 0) {
            instance2.clearNewContacts();
        }
        return matchNumbers;
    }

    private int matchNumbers(@Nullable ArrayList<String> arrayList, boolean z) {
        if (arrayList == null || arrayList.size() == 0) {
            return -1;
        }
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper == null) {
            return 11;
        }
        return aBContactsHelper.matchPhoneNumbers(arrayList, z);
    }
}
