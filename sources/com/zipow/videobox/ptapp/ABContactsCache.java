package com.zipow.videobox.ptapp;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Process;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.sip.server.ZMPhoneNumberHelper;
import com.zipow.videobox.util.ZMPhoneUtils;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.videomeetings.C4558R;

public class ABContactsCache extends ContentObserver {
    private static final String TAG = "ABContactsCache";
    @Nullable
    private static ABContactsCache instance;
    /* access modifiers changed from: private */
    public static String mPhoneCountryCode;
    @NonNull
    private ArrayList<Contact> mCache = new ArrayList<>();
    @Nullable
    private HashMap<String, Contact> mE164NumberContactMap;
    private boolean mIsCached = false;
    private int mLastReloadContactPermission = -1;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    @Nullable
    private LoadContactsTask mLoadContactsTask;
    @NonNull
    private Object mLockReload = new Object();
    @NonNull
    private HashMap<String, Contact> mMapPhoneNumberToContact = new HashMap<>();
    private boolean mNeedReloadAll = false;
    @Nullable
    private Set<String> mNewPhoneNos = new HashSet();
    private int mPhoneNumberMaxLength = 15;
    private int mPhoneNumberMinLength = 9;

    public static class Contact implements Serializable {
        @NonNull
        private static Contact _invalidInstance = new Contact(-1);
        public Map<String, ContactType> accountMap;
        public ArrayList<ContactType> accounts;
        public int contactId;
        public String displayName;
        private String displayPhoneNumber;

        /* renamed from: id */
        private int f316id;
        @Nullable
        public String normalizeCountryCode;
        public String normalizedNumber;
        public String number;
        public String sortKey;
        public int type;

        public static class ContactType implements Serializable {
            public String name;
            private Map<String, PhoneNumber> phoneNumberMap;
            public ArrayList<PhoneNumber> phoneNumbers;
            public String type;

            public ContactType(String str, String str2) {
                this.type = str;
                this.name = str2;
            }

            public void addPhoneNumber(@Nullable PhoneNumber phoneNumber) {
                if (phoneNumber != null) {
                    if (this.phoneNumbers == null) {
                        this.phoneNumbers = new ArrayList<>();
                    }
                    this.phoneNumbers.add(phoneNumber);
                    if (this.phoneNumberMap == null) {
                        this.phoneNumberMap = new HashMap();
                    }
                    this.phoneNumberMap.put(phoneNumber.normalizedNumber, phoneNumber);
                }
            }

            @Nullable
            public PhoneNumber containsPhoneNumber(@NonNull String str) {
                if (TextUtils.isEmpty(str)) {
                    return null;
                }
                Map<String, PhoneNumber> map = this.phoneNumberMap;
                if (map == null || map.isEmpty()) {
                    return null;
                }
                return (PhoneNumber) this.phoneNumberMap.get(str);
            }
        }

        public static class PhoneNumber implements Serializable {
            private String displayPhoneNumber;
            public String normalizeCountryCode;
            public String normalizedNumber;
            public String number;
            public int type;

            public String getDisplayPhoneNumber() {
                if (this.displayPhoneNumber == null) {
                    this.displayPhoneNumber = ZMPhoneUtils.formatPhoneNumber(this.number, CountryCodeUtil.isoCountryCode2PhoneCountryCode(CountryCodeUtil.getIsoCountryCode(VideoBoxApplication.getInstance())), "", true);
                }
                return this.displayPhoneNumber;
            }

            public void setDisplayPhoneNumber(String str) {
                this.displayPhoneNumber = str;
            }

            public PhoneNumber(String str, String str2, int i, String str3) {
                this.type = i;
                this.number = str;
                this.normalizedNumber = str2;
                this.normalizeCountryCode = str3;
            }

            @Nullable
            public String getLabel() {
                Context globalContext = VideoBoxApplication.getGlobalContext();
                if (globalContext == null) {
                    return null;
                }
                switch (this.type) {
                    case 1:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_home_58879);
                    case 2:
                    case 17:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_mobile_58879);
                    case 3:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_Work_58879);
                    case 4:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_work_fax_100147);
                    case 5:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_home_fax_100147);
                    case 6:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_pager_100147);
                    case 9:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_car_100147);
                    case 10:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_company_100147);
                    case 11:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_isdn_100147);
                    case 12:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_main_100147);
                    case 13:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_other_fax_100147);
                    case 14:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_radio_100147);
                    case 18:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_work_pager_100147);
                    case 19:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_assistant_100147);
                    case 20:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_mms_100147);
                    default:
                        return globalContext.getString(C4558R.string.zm_lbl_phone_type_Other_58879);
                }
            }
        }

        public String getDisplayPhoneNumber() {
            if (this.displayPhoneNumber == null) {
                this.displayPhoneNumber = ZMPhoneUtils.formatPhoneNumber(this.number, ABContactsCache.mPhoneCountryCode, "", true);
            }
            return this.displayPhoneNumber;
        }

        public void setDisplayPhoneNumber(String str) {
            this.displayPhoneNumber = str;
        }

        public Contact() {
        }

        public void addContactType(@Nullable ContactType contactType) {
            if (contactType != null) {
                if (this.accounts == null) {
                    this.accounts = new ArrayList<>();
                }
                this.accounts.add(contactType);
                if (this.accountMap == null) {
                    this.accountMap = new HashMap();
                }
                this.accountMap.put(String.valueOf(contactType.type), contactType);
            }
        }

        @Nullable
        public ContactType containsContactType(@NonNull String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            Map<String, ContactType> map = this.accountMap;
            if (map == null || map.isEmpty()) {
                return null;
            }
            return (ContactType) this.accountMap.get(str);
        }

        @Nullable
        public List<String> getPhoneNumberList() {
            if (CollectionsUtil.isCollectionEmpty(this.accounts)) {
                return null;
            }
            HashSet hashSet = new HashSet();
            Iterator it = this.accounts.iterator();
            while (it.hasNext()) {
                ContactType contactType = (ContactType) it.next();
                if (!CollectionsUtil.isCollectionEmpty(contactType.phoneNumbers)) {
                    Iterator it2 = contactType.phoneNumbers.iterator();
                    while (it2.hasNext()) {
                        hashSet.add(((PhoneNumber) it2.next()).number);
                    }
                }
            }
            return new ArrayList(hashSet);
        }

        public boolean hasPhoneNumber(@Nullable String str) {
            if (CollectionsUtil.isCollectionEmpty(this.accounts) || TextUtils.isEmpty(str)) {
                return false;
            }
            ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
            boolean z = !str.startsWith("+");
            Iterator it = this.accounts.iterator();
            while (it.hasNext()) {
                ContactType contactType = (ContactType) it.next();
                if (!CollectionsUtil.isCollectionEmpty(contactType.phoneNumbers)) {
                    Iterator it2 = contactType.phoneNumbers.iterator();
                    while (it2.hasNext()) {
                        PhoneNumber phoneNumber = (PhoneNumber) it2.next();
                        String str2 = phoneNumber.normalizedNumber == null ? phoneNumber.number : phoneNumber.normalizedNumber;
                        if (str2 != null) {
                            String replaceAll = str2.replaceAll(OAuth.SCOPE_DELIMITER, "");
                            if (zMPhoneNumberHelper != null) {
                                if (zMPhoneNumberHelper.isNumberMatched(str, replaceAll, z || !replaceAll.startsWith("+"))) {
                                    return true;
                                }
                            } else if (str.equals(replaceAll)) {
                                return true;
                            }
                        }
                    }
                    continue;
                }
            }
            return false;
        }

        private Contact(int i) {
            this.contactId = i;
        }

        @NonNull
        public static Contact invalidInstance() {
            return _invalidInstance;
        }

        /* access modifiers changed from: private */
        public boolean isInvalidInstance() {
            return this == _invalidInstance;
        }

        public boolean filter(String str) {
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            Locale localDefault = CompatUtils.getLocalDefault();
            String lowerCase = str.toLowerCase(localDefault);
            String str2 = this.displayName;
            if (str2 != null && str2.toLowerCase(localDefault).contains(lowerCase)) {
                return true;
            }
            String str3 = this.normalizedNumber;
            if (str3 != null && str3.contains(lowerCase)) {
                return true;
            }
            String str4 = this.number;
            if (str4 == null || !str4.replace(OAuth.SCOPE_DELIMITER, "").replace("-", "").contains(lowerCase)) {
                return false;
            }
            return true;
        }
    }

    public static class ContactsComparator implements Comparator<Contact> {
        private Collator mCollator;

        public ContactsComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull Contact contact, @NonNull Contact contact2) {
            String str = contact.sortKey;
            String str2 = contact2.sortKey;
            if (str == null) {
                str = "";
            }
            if (str2 == null) {
                str2 = "";
            }
            return this.mCollator.compare(str, str2);
        }
    }

    public interface IABContactsCacheListener extends IListener {
        void onContactsCacheUpdated();
    }

    static class LoadContactsResult {
        @Nullable
        Set<String> addedPhoneNumbers = null;
        List<Contact> allContacts;
        HashMap<String, Contact> allContactsMap;
        boolean updated = false;

        LoadContactsResult() {
        }
    }

    class LoadContactsTask extends ZMAsyncTask<Void, Integer, LoadContactsResult> {
        @Nullable
        LoadContactsResult result = null;

        public LoadContactsTask() {
        }

        /* access modifiers changed from: protected */
        @Nullable
        public LoadContactsResult doInBackground(Void... voidArr) {
            try {
                System.currentTimeMillis();
                this.result = ABContactsCache.this.reloadAllContactsImpl();
                System.currentTimeMillis();
            } catch (Exception unused) {
            }
            return this.result;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(LoadContactsResult loadContactsResult) {
            if (!isCancelled()) {
                ABContactsCache.this.onLoadContactsTaskComplete(loadContactsResult);
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            ABContactsCache.this.onLoadContactsTaskComplete(this.result);
        }
    }

    @NonNull
    public static synchronized ABContactsCache getInstance() {
        ABContactsCache aBContactsCache;
        synchronized (ABContactsCache.class) {
            if (instance == null) {
                instance = new ABContactsCache();
            }
            aBContactsCache = instance;
        }
        return aBContactsCache;
    }

    private ABContactsCache() {
        super(new Handler());
        registerContentObserver();
    }

    public void registerContentObserver() {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            if (!OsUtil.isAtLeastM() || instance2.checkPermission("android.permission.READ_CONTACTS", Process.myPid(), Process.myUid()) == 0) {
                instance2.getContentResolver().unregisterContentObserver(this);
                instance2.getContentResolver().registerContentObserver(Phone.CONTENT_URI, false, this);
            }
        }
    }

    public void addListener(IABContactsCacheListener iABContactsCacheListener) {
        IListener[] all = this.mListenerList.getAll();
        for (int i = 0; i < all.length; i++) {
            if (all[i] == iABContactsCacheListener) {
                removeListener((IABContactsCacheListener) all[i]);
            }
        }
        this.mListenerList.add(iABContactsCacheListener);
    }

    public void removeListener(IABContactsCacheListener iABContactsCacheListener) {
        this.mListenerList.remove(iABContactsCacheListener);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0059, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x005d, code lost:
        throw r0;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:10:0x0029, B:19:0x0049] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean reloadAllContacts(boolean r6) {
        /*
            r5 = this;
            r0 = 0
            if (r6 != 0) goto L_0x000e
            com.zipow.videobox.ptapp.PTApp r6 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r6 = r6.hasZoomMessenger()
            if (r6 != 0) goto L_0x000e
            return r0
        L_0x000e:
            java.lang.Object r6 = r5.mLockReload
            monitor-enter(r6)
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Throwable -> 0x002d }
            java.lang.String r2 = "android.permission.READ_CONTACTS"
            int r3 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x002d }
            int r4 = android.os.Process.myUid()     // Catch:{ Throwable -> 0x002d }
            int r1 = r1.checkPermission(r2, r3, r4)     // Catch:{ Throwable -> 0x002d }
            r5.mLastReloadContactPermission = r1     // Catch:{ Throwable -> 0x002d }
            int r1 = r5.mLastReloadContactPermission     // Catch:{ Throwable -> 0x002d }
            if (r1 == 0) goto L_0x002d
            monitor-exit(r6)     // Catch:{ all -> 0x002b }
            return r0
        L_0x002b:
            r0 = move-exception
            goto L_0x005c
        L_0x002d:
            com.zipow.videobox.ptapp.ABContactsCache$LoadContactsTask r1 = r5.mLoadContactsTask     // Catch:{ all -> 0x002b }
            if (r1 != 0) goto L_0x005a
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x002b }
            r1.<init>()     // Catch:{ all -> 0x002b }
            java.util.ArrayList<com.zipow.videobox.ptapp.ABContactsCache$Contact> r2 = r5.mCache     // Catch:{ all -> 0x002b }
            r1.addAll(r2)     // Catch:{ all -> 0x002b }
            com.zipow.videobox.ptapp.ABContactsCache$LoadContactsTask r1 = new com.zipow.videobox.ptapp.ABContactsCache$LoadContactsTask     // Catch:{ all -> 0x002b }
            r1.<init>()     // Catch:{ all -> 0x002b }
            r5.mLoadContactsTask = r1     // Catch:{ all -> 0x002b }
            com.zipow.videobox.ptapp.ABContactsCache$LoadContactsTask r1 = r5.mLoadContactsTask     // Catch:{ all -> 0x002b }
            java.lang.Void[] r2 = new java.lang.Void[r0]     // Catch:{ all -> 0x002b }
            r1.execute((Params[]) r2)     // Catch:{ all -> 0x002b }
            com.zipow.videobox.ptapp.ABContactsCache$LoadContactsTask r1 = r5.mLoadContactsTask     // Catch:{ Exception -> 0x0058 }
            r2 = 1000(0x3e8, double:4.94E-321)
            java.util.concurrent.TimeUnit r4 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ Exception -> 0x0058 }
            java.lang.Object r1 = r1.get(r2, r4)     // Catch:{ Exception -> 0x0058 }
            com.zipow.videobox.ptapp.ABContactsCache$LoadContactsResult r1 = (com.zipow.videobox.ptapp.ABContactsCache.LoadContactsResult) r1     // Catch:{ Exception -> 0x0058 }
            monitor-exit(r6)     // Catch:{ all -> 0x002b }
            r6 = 1
            return r6
        L_0x0058:
            monitor-exit(r6)     // Catch:{ all -> 0x002b }
            return r0
        L_0x005a:
            monitor-exit(r6)     // Catch:{ all -> 0x002b }
            return r0
        L_0x005c:
            monitor-exit(r6)     // Catch:{ all -> 0x002b }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.ABContactsCache.reloadAllContacts(boolean):boolean");
    }

    public boolean reloadAllContacts() {
        return reloadAllContacts(false);
    }

    /* access modifiers changed from: private */
    public void onLoadContactsTaskComplete(@Nullable LoadContactsResult loadContactsResult) {
        if (loadContactsResult != null) {
            synchronized (this.mLockReload) {
                this.mLoadContactsTask = null;
                this.mIsCached = true;
                if (loadContactsResult.updated) {
                    this.mNewPhoneNos = loadContactsResult.addedPhoneNumbers;
                }
                if (this.mNewPhoneNos == null) {
                    this.mNewPhoneNos = new HashSet();
                }
                this.mCache.clear();
                if (loadContactsResult.allContacts != null) {
                    this.mCache.addAll(loadContactsResult.allContacts);
                }
                if (loadContactsResult.allContactsMap != null) {
                    this.mE164NumberContactMap = loadContactsResult.allContactsMap;
                }
                this.mMapPhoneNumberToContact.clear();
                this.mNeedReloadAll = false;
                if (loadContactsResult.updated) {
                    notifyContactsCachedUpdated();
                }
            }
        }
    }

    private void notifyContactsCachedUpdated() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IABContactsCacheListener) iListener).onContactsCacheUpdated();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    @NonNull
    public LoadContactsResult reloadAllContactsImpl() {
        LoadContactsResult loadContactsResult;
        LoadContactsResult loadContactsResult2;
        String str;
        int i;
        LoadContactsResult loadContactsResult3 = new LoadContactsResult();
        Set allContactsPhoneNumber = getAllContactsPhoneNumber();
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        ContentResolver contentResolver = instance2.getContentResolver();
        if (contentResolver == null) {
            return loadContactsResult3;
        }
        System.currentTimeMillis();
        mPhoneCountryCode = CountryCodeUtil.isoCountryCode2PhoneCountryCode(CountryCodeUtil.getIsoCountryCode(VideoBoxApplication.getNonNullInstance()));
        Cursor query = contentResolver.query(Phone.CONTENT_URI, new String[]{"contact_id", "display_name", "data1", "data4", "data2", "account_type", "account_name"}, "has_phone_number = ?", new String[]{"1"}, "display_name ASC");
        if (query == null) {
            return loadContactsResult3;
        }
        ArrayList<Contact> arrayList = new ArrayList<>();
        HashMap hashMap = new HashMap(100);
        Locale localDefault = CompatUtils.getLocalDefault();
        try {
            int columnIndex = query.getColumnIndex("contact_id");
            int columnIndex2 = query.getColumnIndex("display_name");
            int columnIndex3 = query.getColumnIndex("data1");
            int columnIndex4 = query.getColumnIndex("data4");
            int columnIndex5 = query.getColumnIndex("data2");
            int columnIndex6 = query.getColumnIndex("account_type");
            int columnIndex7 = query.getColumnIndex("account_name");
            while (query.moveToNext()) {
                int i2 = query.getInt(columnIndex);
                int i3 = columnIndex;
                String string = query.getString(columnIndex2);
                int i4 = columnIndex2;
                String string2 = query.getString(columnIndex6);
                int i5 = columnIndex6;
                String string3 = query.getString(columnIndex7);
                int i6 = columnIndex7;
                String string4 = query.getString(columnIndex3);
                int i7 = columnIndex3;
                String string5 = query.getString(columnIndex4);
                int i8 = columnIndex4;
                int i9 = query.getInt(columnIndex5);
                int i10 = columnIndex5;
                Contact contact = (Contact) hashMap.get(String.valueOf(i2));
                if (contact == null) {
                    contact = new Contact();
                    arrayList.add(contact);
                    loadContactsResult2 = loadContactsResult3;
                    hashMap.put(String.valueOf(i2), contact);
                    contact.contactId = i2;
                    contact.displayName = string;
                    contact.number = string4;
                    contact.type = i9;
                    contact.sortKey = SortUtil.getSortKey(contact.displayName, localDefault);
                    contact.normalizedNumber = string5;
                    contact.normalizeCountryCode = PhoneNumberUtil.getCountryCodeFromFormatedPhoneNumber(contact.normalizedNumber);
                } else {
                    loadContactsResult2 = loadContactsResult3;
                }
                if (!TextUtils.isEmpty(string3)) {
                    ContactType containsContactType = contact.containsContactType(string2);
                    if (containsContactType == null) {
                        containsContactType = new ContactType(string2, string3);
                        contact.addContactType(containsContactType);
                    }
                    if (!TextUtils.isEmpty(string4)) {
                        if (containsContactType.containsPhoneNumber(string4) == null) {
                            if (!TextUtils.isEmpty(string4)) {
                                if (TextUtils.equals(string4, contact.number)) {
                                    string5 = contact.normalizedNumber;
                                    str = contact.normalizeCountryCode;
                                } else {
                                    str = PhoneNumberUtil.getCountryCodeFromFormatedPhoneNumber(string5);
                                }
                                containsContactType.addPhoneNumber(new PhoneNumber(string4, string5, i9, str));
                                if (string5 == null) {
                                    i = 0;
                                } else {
                                    i = string5.length();
                                }
                                if (i > 0) {
                                    if (this.mPhoneNumberMinLength > i) {
                                        this.mPhoneNumberMinLength = i;
                                    }
                                    if (this.mPhoneNumberMaxLength < i) {
                                        this.mPhoneNumberMaxLength = i;
                                    }
                                }
                            }
                        }
                    }
                }
                columnIndex = i3;
                columnIndex2 = i4;
                columnIndex6 = i5;
                columnIndex7 = i6;
                columnIndex3 = i7;
                columnIndex4 = i8;
                columnIndex5 = i10;
                loadContactsResult3 = loadContactsResult2;
            }
            LoadContactsResult loadContactsResult4 = loadContactsResult3;
            query.close();
            HashSet hashSet = new HashSet();
            HashMap<String, Contact> hashMap2 = null;
            if (!CollectionsUtil.isListEmpty(arrayList)) {
                hashMap2 = new HashMap<>();
                String isoCountryCode = CountryCodeUtil.getIsoCountryCode(instance2);
                for (Contact contact2 : arrayList) {
                    if (contact2.accounts != null) {
                        Iterator it = contact2.accounts.iterator();
                        while (it.hasNext()) {
                            ContactType contactType = (ContactType) it.next();
                            if (contactType.phoneNumbers != null) {
                                Iterator it2 = contactType.phoneNumbers.iterator();
                                while (it2.hasNext()) {
                                    PhoneNumber phoneNumber = (PhoneNumber) it2.next();
                                    if (!allContactsPhoneNumber.contains(phoneNumber.normalizedNumber)) {
                                        hashSet.add(phoneNumber.normalizedNumber);
                                    }
                                    hashMap2.put(ZMPhoneUtils.formatPhoneNumberAsE164(phoneNumber.number, isoCountryCode, ""), contact2);
                                }
                            }
                        }
                    }
                }
                loadContactsResult = loadContactsResult4;
            } else {
                loadContactsResult = loadContactsResult4;
            }
            loadContactsResult.addedPhoneNumbers = hashSet;
            loadContactsResult.allContacts = arrayList;
            loadContactsResult.allContactsMap = hashMap2;
            loadContactsResult.updated = !hashSet.isEmpty();
            return loadContactsResult;
        } catch (Throwable th) {
            query.close();
            throw th;
        }
    }

    public boolean isCached() {
        boolean z;
        synchronized (this.mLockReload) {
            z = this.mIsCached;
        }
        return z;
    }

    public int getCachedContactsCount() {
        int size;
        synchronized (this.mLockReload) {
            size = this.mCache.size();
        }
        return size;
    }

    @Nullable
    public Contact getCachedContact(int i) {
        synchronized (this.mLockReload) {
            if (i >= 0) {
                if (i < this.mCache.size()) {
                    Contact contact = (Contact) this.mCache.get(i);
                    return contact;
                }
            }
            return null;
        }
    }

    @Nullable
    public ArrayList<String> getNewPhoneNos() {
        Set<String> set = this.mNewPhoneNos;
        if (set == null) {
            return null;
        }
        return new ArrayList<>(set);
    }

    public void clearNewContacts() {
        synchronized (this.mLockReload) {
            this.mNewPhoneNos = null;
        }
    }

    @Nullable
    public List<Contact> getAllCacheContacts() {
        return this.mCache;
    }

    @NonNull
    public Set<String> getAllContactsPhoneNumber() {
        synchronized (this.mLockReload) {
            HashSet hashSet = new HashSet();
            if (!isCached() && !reloadAllContacts()) {
                return hashSet;
            }
            for (int i = 0; i < getCachedContactsCount(); i++) {
                Contact cachedContact = getCachedContact(i);
                if (cachedContact != null) {
                    if (cachedContact.accounts != null) {
                        Iterator it = cachedContact.accounts.iterator();
                        while (it.hasNext()) {
                            ContactType contactType = (ContactType) it.next();
                            if (contactType.phoneNumbers != null) {
                                Iterator it2 = contactType.phoneNumbers.iterator();
                                while (it2.hasNext()) {
                                    hashSet.add(((PhoneNumber) it2.next()).normalizedNumber);
                                }
                            }
                        }
                    }
                }
            }
            return hashSet;
        }
    }

    @Nullable
    public Contact getFirstContactByPhoneNumber(String str) {
        synchronized (this.mLockReload) {
            if (StringUtil.isEmptyOrNull(str)) {
                return null;
            }
            Contact contact = (Contact) this.mMapPhoneNumberToContact.get(str);
            if (contact != null) {
                if (contact.isInvalidInstance()) {
                    return null;
                }
                return contact;
            } else if (!isCached() && !reloadAllContacts()) {
                return null;
            } else {
                if (isValidLength(str)) {
                    String formatPhoneNumberAsE164 = ZMPhoneUtils.formatPhoneNumberAsE164(str, CountryCodeUtil.getIsoCountryCode(VideoBoxApplication.getGlobalContext()), "");
                    if (this.mE164NumberContactMap != null) {
                        Contact contact2 = (Contact) this.mE164NumberContactMap.get(formatPhoneNumberAsE164);
                        if (contact2 != null) {
                            this.mMapPhoneNumberToContact.put(str, contact2);
                            return contact2;
                        }
                    }
                }
                this.mMapPhoneNumberToContact.put(str, Contact.invalidInstance());
                return null;
            }
        }
    }

    private boolean isValidLength(String str) {
        boolean z = false;
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        int length = str.length();
        if (length >= this.mPhoneNumberMinLength - 1 && length <= this.mPhoneNumberMaxLength + 1) {
            z = true;
        }
        return z;
    }

    public void onChange(boolean z) {
        synchronized (this.mLockReload) {
            this.mNeedReloadAll = true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean needReloadAll() {
        /*
            r7 = this;
            java.lang.Object r0 = r7.mLockReload
            monitor-enter(r0)
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0036 }
            r2 = 23
            r3 = 1
            r4 = 0
            if (r1 < r2) goto L_0x0027
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Throwable -> 0x0027 }
            java.lang.String r2 = "android.permission.READ_CONTACTS"
            int r5 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x0027 }
            int r6 = android.os.Process.myUid()     // Catch:{ Throwable -> 0x0027 }
            int r1 = r1.checkPermission(r2, r5, r6)     // Catch:{ Throwable -> 0x0027 }
            int r2 = r7.mLastReloadContactPermission     // Catch:{ Throwable -> 0x0027 }
            if (r1 == r2) goto L_0x0025
            if (r1 != 0) goto L_0x0025
            r1 = 1
            goto L_0x0028
        L_0x0025:
            r1 = 0
            goto L_0x0028
        L_0x0027:
            r1 = 0
        L_0x0028:
            boolean r2 = r7.mNeedReloadAll     // Catch:{ all -> 0x0036 }
            if (r2 != 0) goto L_0x0034
            boolean r2 = r7.mIsCached     // Catch:{ all -> 0x0036 }
            if (r2 == 0) goto L_0x0034
            if (r1 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r3 = 0
        L_0x0034:
            monitor-exit(r0)     // Catch:{ all -> 0x0036 }
            return r3
        L_0x0036:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0036 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.ABContactsCache.needReloadAll():boolean");
    }

    private int findContactCompareIdNumber(@NonNull Contact contact, @NonNull ArrayList<Contact> arrayList) {
        int binarySearch = Collections.binarySearch(arrayList, contact, new Comparator<Contact>() {
            public int compare(@Nullable Contact contact, @Nullable Contact contact2) {
                if (contact == null && contact2 != null) {
                    return -1;
                }
                if (contact != null && contact2 == null) {
                    return 1;
                }
                if (contact == null) {
                    return 0;
                }
                return contact.contactId - contact2.contactId;
            }
        });
        if (binarySearch < 0 || ((Contact) arrayList.get(binarySearch)).normalizedNumber.equals(contact.normalizedNumber)) {
            return binarySearch;
        }
        int i = binarySearch;
        while (i > 0) {
            Contact contact2 = (Contact) arrayList.get(i - 1);
            if (contact2.contactId != contact.contactId) {
                break;
            } else if (contact2.normalizedNumber.equals(contact.normalizedNumber)) {
                return i;
            } else {
                i--;
            }
        }
        while (binarySearch < arrayList.size() - 1) {
            int i2 = binarySearch + 1;
            Contact contact3 = (Contact) arrayList.get(i2);
            if (contact3.contactId != contact.contactId) {
                break;
            } else if (contact3.normalizedNumber.equals(contact.normalizedNumber)) {
                return binarySearch;
            } else {
                binarySearch = i2;
            }
        }
        return -1;
    }
}
