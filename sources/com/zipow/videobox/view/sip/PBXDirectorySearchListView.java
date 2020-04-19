package com.zipow.videobox.view.sip;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.ContactType;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.PhoneNumber;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.WebSearchResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.QuickSearchListView;

public class PBXDirectorySearchListView extends QuickSearchListView {
    private static final int DEFAUIT_SCORE = 9999;
    private static final int MAX_LOCAL_CONTACTS_NUM = 200;
    private static final String TAG = "PBXDirectorySearchListV";
    Comparator<IMAddrBookItem> comparator = new Comparator<IMAddrBookItem>() {
        public int compare(IMAddrBookItem iMAddrBookItem, IMAddrBookItem iMAddrBookItem2) {
            if (iMAddrBookItem.getLastMatchScore() != iMAddrBookItem2.getLastMatchScore()) {
                return iMAddrBookItem.getLastMatchScore() - iMAddrBookItem2.getLastMatchScore();
            }
            if (iMAddrBookItem.getTimeStamp() == iMAddrBookItem2.getTimeStamp()) {
                return 0;
            }
            return iMAddrBookItem2.getTimeStamp() > iMAddrBookItem.getTimeStamp() ? 1 : -1;
        }
    };
    private PBXDirectorySearchAdapter mAdapter;
    private List<IMAddrBookItem> mAllContactCache;
    private Set<IMAddrBookItem> mAllContactCacheSet = new HashSet();
    private Set<String> mAllContactJIDCacheSet = new HashSet();
    private View mEmptyView;
    private String mFilter;
    private List<String> mFilterList = new ArrayList();
    private int mFilterType = 1;
    private WebSearchResult mWebSearchResult = new WebSearchResult();

    public PBXDirectorySearchListView(Context context) {
        super(context);
        init();
    }

    public PBXDirectorySearchListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PBXDirectorySearchListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void onBuddyInfoUpdate(List<String> list, List<String> list2) {
        PBXDirectorySearchAdapter pBXDirectorySearchAdapter = this.mAdapter;
        if (pBXDirectorySearchAdapter != null) {
            pBXDirectorySearchAdapter.notifyDataSetChanged();
        }
    }

    public void onBuddyListUpdate() {
        PBXDirectorySearchAdapter pBXDirectorySearchAdapter = this.mAdapter;
        if (pBXDirectorySearchAdapter != null) {
            pBXDirectorySearchAdapter.notifyDataSetChanged();
        }
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
    }

    public void refresh() {
        getSearchedBuddiesNormal(this.mFilter, true);
    }

    public void filter(String str) {
        getSearchedBuddiesNormal(str, false);
    }

    public void filter(String str, boolean z) {
        if (z || !TextUtils.equals(str, this.mFilter)) {
            if (this.mAllContactCache == null) {
                this.mAllContactCache = ZMBuddySyncInstance.getInsatance().getAllPbxBuddies();
                mergePhoneAddressToAllContactCache();
            }
            ArrayList arrayList = new ArrayList();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    String jid = myself.getJid();
                    this.mFilter = str;
                    boolean z2 = true;
                    if (this.mFilterType == 1 || !TextUtils.isEmpty(this.mFilter)) {
                        setQuickSearchEnabled(TextUtils.isEmpty(this.mFilter));
                        PBXDirectorySearchAdapter pBXDirectorySearchAdapter = this.mAdapter;
                        if (this.mFilterType != 2 && TextUtils.isEmpty(this.mFilter)) {
                            z2 = false;
                        }
                        pBXDirectorySearchAdapter.setSearchMode(z2);
                        int size = this.mAllContactCache.size();
                        for (int i = 0; i < size; i++) {
                            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mAllContactCache.get(i);
                            if (!(iMAddrBookItem == null || iMAddrBookItem.getScreenName() == null)) {
                                if (this.mFilter != null && !iMAddrBookItem.getScreenName().toLowerCase().contains(this.mFilter.toLowerCase())) {
                                } else if (!TextUtils.equals(iMAddrBookItem.getJid(), jid)) {
                                    arrayList.add(iMAddrBookItem);
                                }
                            }
                            if (arrayList.size() >= 200) {
                                break;
                            }
                        }
                    } else {
                        this.mAdapter.updateData(null, "");
                        return;
                    }
                }
            }
            this.mAdapter.updateData(arrayList, this.mFilter);
            if (this.mEmptyView != null) {
                if (CollectionsUtil.isListEmpty(arrayList)) {
                    this.mEmptyView.setVisibility(0);
                } else {
                    this.mEmptyView.setVisibility(8);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:134:0x00ce A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00c9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getSearchedBuddiesNormal(java.lang.String r13, boolean r14) {
        /*
            r12 = this;
            if (r14 != 0) goto L_0x000b
            java.lang.String r14 = r12.mFilter
            boolean r14 = android.text.TextUtils.equals(r13, r14)
            if (r14 == 0) goto L_0x000b
            return
        L_0x000b:
            com.zipow.videobox.ptapp.PTApp r14 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r14 = r14.getZoomMessenger()
            if (r14 != 0) goto L_0x0016
            return
        L_0x0016:
            r12.mFilter = r13
            int r0 = r12.mFilterType
            r1 = 0
            r2 = 1
            if (r0 == r2) goto L_0x002e
            java.lang.String r0 = r12.mFilter
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x002e
            com.zipow.videobox.view.sip.PBXDirectorySearchAdapter r13 = r12.mAdapter
            java.lang.String r14 = ""
            r13.updateData(r1, r14)
            return
        L_0x002e:
            r12.setmFilterList()
            java.util.HashSet r0 = new java.util.HashSet
            r0.<init>()
            java.lang.String r3 = r12.mFilter
            r4 = 200(0xc8, float:2.8E-43)
            java.util.List r3 = r14.localStrictSearchBuddiesAdvance(r3, r1, r4)
            if (r3 == 0) goto L_0x004f
            int r4 = r3.size()
            if (r4 <= 0) goto L_0x004f
            boolean r4 = r14.isAnyBuddyGroupLarge()
            if (r4 == 0) goto L_0x004f
            r14.getBuddiesPresence(r3, r2)
        L_0x004f:
            if (r3 == 0) goto L_0x0054
            r0.addAll(r3)
        L_0x0054:
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData r3 = r14.getBuddySearchData()
            r4 = 8
            r5 = 0
            if (r3 == 0) goto L_0x00d8
            java.lang.String r6 = r12.mFilter
            if (r6 == 0) goto L_0x00d8
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData$SearchKey r6 = r3.getSearchKey()
            if (r6 == 0) goto L_0x00d8
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData$SearchKey r6 = r3.getSearchKey()
            java.lang.String r6 = r6.getKey()
            java.lang.String r7 = r12.mFilter
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isSameString(r6, r7)
            if (r6 == 0) goto L_0x00d8
            com.zipow.videobox.view.WebSearchResult r6 = new com.zipow.videobox.view.WebSearchResult
            r6.<init>()
            r12.mWebSearchResult = r6
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            com.zipow.videobox.view.WebSearchResult r7 = r12.mWebSearchResult
            java.lang.String r8 = r12.mFilter
            r7.setKey(r8)
            r7 = 0
        L_0x008b:
            int r8 = r3.getBuddyCount()
            if (r7 >= r8) goto L_0x00d1
            com.zipow.videobox.ptapp.mm.ZoomBuddy r8 = r3.getBuddyAt(r7)
            if (r8 == 0) goto L_0x00ce
            java.lang.String r9 = r8.getJid()
            int r10 = r8.getBuddyType()
            r6.add(r9)
            r11 = 6
            if (r10 == r11) goto L_0x00bc
            r11 = 4
            if (r10 == r11) goto L_0x00bc
            r11 = 5
            if (r10 == r11) goto L_0x00bc
            if (r10 == r4) goto L_0x00bc
            r11 = 7
            if (r10 != r11) goto L_0x00b1
            goto L_0x00bc
        L_0x00b1:
            if (r9 == 0) goto L_0x00c5
            com.zipow.videobox.ptapp.mm.ZMBuddySyncInstance r8 = com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.getInsatance()
            com.zipow.videobox.view.IMAddrBookItem r8 = r8.getBuddyByJid(r9)
            goto L_0x00c6
        L_0x00bc:
            int r10 = r12.mFilterType
            if (r10 != r2) goto L_0x00c5
            com.zipow.videobox.view.IMAddrBookItem r8 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r8)
            goto L_0x00c6
        L_0x00c5:
            r8 = r1
        L_0x00c6:
            if (r8 != 0) goto L_0x00c9
            goto L_0x00ce
        L_0x00c9:
            com.zipow.videobox.view.WebSearchResult r10 = r12.mWebSearchResult
            r10.putItem(r9, r8)
        L_0x00ce:
            int r7 = r7 + 1
            goto L_0x008b
        L_0x00d1:
            r0.addAll(r6)
            r14.getBuddiesPresence(r6, r5)
            goto L_0x00f1
        L_0x00d8:
            com.zipow.videobox.view.WebSearchResult r1 = r12.mWebSearchResult
            if (r1 == 0) goto L_0x00f1
            java.lang.String r3 = r12.mFilter
            java.lang.String r1 = r1.getKey()
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isSameString(r3, r1)
            if (r1 == 0) goto L_0x00f1
            com.zipow.videobox.view.WebSearchResult r1 = r12.mWebSearchResult
            java.util.Set r1 = r1.getJids()
            r0.addAll(r1)
        L_0x00f1:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>(r0)
            java.lang.String r0 = r12.mFilter
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
            if (r0 == 0) goto L_0x0104
            java.lang.String r0 = r12.mFilter
            java.util.List r1 = com.zipow.videobox.ptapp.p013mm.ZMSortUtil.sortBuddies(r1, r5, r0)
        L_0x0104:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            boolean r3 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r1)
            if (r3 != 0) goto L_0x01d3
            int r3 = r1.size()
            com.zipow.videobox.ptapp.mm.ZoomBuddy r6 = r14.getMyself()
            if (r6 == 0) goto L_0x01d3
            java.lang.String r7 = r12.mFilter
            boolean r7 = android.text.TextUtils.isEmpty(r7)
            r12.setQuickSearchEnabled(r7)
            com.zipow.videobox.view.sip.PBXDirectorySearchAdapter r7 = r12.mAdapter
            java.lang.String r8 = r12.mFilter
            boolean r8 = android.text.TextUtils.isEmpty(r8)
            r8 = r8 ^ r2
            r7.setSearchMode(r8)
            r7 = 0
        L_0x012f:
            int r8 = r0.size()
            if (r8 >= r3) goto L_0x01d3
            int r8 = r1.size()
            if (r7 >= r8) goto L_0x01d3
            int r8 = r7 + 1
            java.lang.Object r7 = r1.get(r7)
            java.lang.String r7 = (java.lang.String) r7
            com.zipow.videobox.view.WebSearchResult r9 = r12.mWebSearchResult
            com.zipow.videobox.view.IMAddrBookItem r9 = r9.findByJid(r7)
            if (r9 != 0) goto L_0x0157
            com.zipow.videobox.ptapp.mm.ZMBuddySyncInstance r9 = com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.getInsatance()
            com.zipow.videobox.view.IMAddrBookItem r9 = r9.getBuddyByJid(r7)
            if (r9 != 0) goto L_0x0157
            goto L_0x01d0
        L_0x0157:
            java.lang.String r10 = r12.mFilter
            boolean r10 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r10)
            if (r10 != 0) goto L_0x0171
            com.zipow.videobox.ptapp.mm.ZoomBuddy r7 = r14.getBuddyWithJID(r7)
            if (r7 == 0) goto L_0x0171
            java.util.List<java.lang.String> r10 = r12.mFilterList
            r7.strictMatch(r10, r5, r5)
            int r7 = r7.getLastMatchScore()
            r9.setLastMatchScore(r7)
        L_0x0171:
            java.util.Set<com.zipow.videobox.view.IMAddrBookItem> r7 = r12.mAllContactCacheSet
            r7.add(r9)
            java.util.Set<java.lang.String> r7 = r12.mAllContactJIDCacheSet
            java.lang.String r10 = r9.getJid()
            r7.add(r10)
            java.lang.String r7 = r9.getJid()
            java.lang.String r10 = r6.getJid()
            boolean r7 = android.text.TextUtils.equals(r7, r10)
            if (r7 != 0) goto L_0x01d0
            com.zipow.videobox.ptapp.mm.ContactCloudSIP r7 = r9.getICloudSIPCallNumber()
            if (r7 == 0) goto L_0x01a1
            int r10 = r12.mFilterType
            if (r10 != r2) goto L_0x01a1
            java.lang.String r10 = r7.getCompanyNumber()
            boolean r10 = r9.isSameCompany(r10)
            if (r10 != 0) goto L_0x01cd
        L_0x01a1:
            boolean r10 = r9.isFromPhoneContacts()
            if (r10 != 0) goto L_0x01cd
            boolean r10 = r9.isSharedGlobalDirectory()
            if (r10 != 0) goto L_0x01cd
            if (r7 == 0) goto L_0x01b9
            java.util.ArrayList r7 = r7.getDirectNumber()
            boolean r7 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r7)
            if (r7 == 0) goto L_0x01cd
        L_0x01b9:
            java.lang.String r7 = r9.getProfilePhoneNumber()
            boolean r7 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r7)
            if (r7 == 0) goto L_0x01cd
            java.lang.String r7 = r9.getBuddyPhoneNumber()
            boolean r7 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r7)
            if (r7 != 0) goto L_0x01d0
        L_0x01cd:
            r0.add(r9)
        L_0x01d0:
            r7 = r8
            goto L_0x012f
        L_0x01d3:
            java.util.HashSet r14 = new java.util.HashSet
            r14.<init>()
            java.util.Iterator r1 = r0.iterator()
        L_0x01dc:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x01f4
            java.lang.Object r2 = r1.next()
            com.zipow.videobox.view.IMAddrBookItem r2 = (com.zipow.videobox.view.IMAddrBookItem) r2
            int r2 = r2.getContactId()
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r14.add(r2)
            goto L_0x01dc
        L_0x01f4:
            java.util.List r1 = r12.getMatchedIMAddrBookItems(r13)
            r0.addAll(r1)
            java.util.List r1 = r12.getPhoneAddress()
            java.util.Iterator r1 = r1.iterator()
        L_0x0203:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x025c
            java.lang.Object r2 = r1.next()
            com.zipow.videobox.view.IMAddrBookItem r2 = (com.zipow.videobox.view.IMAddrBookItem) r2
            java.lang.String r3 = r2.getScreenName()
            boolean r3 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r3)
            if (r3 != 0) goto L_0x0203
            java.lang.String r3 = r12.mFilter
            if (r3 == 0) goto L_0x0237
            java.lang.String r3 = r2.getScreenName()
            java.lang.String r3 = r3.toLowerCase()
            java.lang.String r6 = r12.mFilter
            java.lang.String r6 = r6.toLowerCase()
            boolean r3 = r3.contains(r6)
            if (r3 != 0) goto L_0x0237
            boolean r3 = r12.matchedIMAddrBookItemFromPhone(r2, r13)
            if (r3 == 0) goto L_0x0203
        L_0x0237:
            int r3 = r2.getContactId()
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            boolean r3 = r14.contains(r3)
            if (r3 != 0) goto L_0x0203
            r0.add(r2)
            java.lang.String r3 = r12.mFilter
            boolean r3 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r3)
            if (r3 != 0) goto L_0x0203
            java.lang.String r3 = r2.getScreenName()
            int r3 = r12.scoreFromCloudContact(r3)
            r2.setLastMatchScore(r3)
            goto L_0x0203
        L_0x025c:
            java.lang.String r13 = r12.mFilter
            boolean r13 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r13)
            if (r13 != 0) goto L_0x0269
            java.util.Comparator<com.zipow.videobox.view.IMAddrBookItem> r13 = r12.comparator
            java.util.Collections.sort(r0, r13)
        L_0x0269:
            com.zipow.videobox.view.sip.PBXDirectorySearchAdapter r13 = r12.mAdapter
            java.lang.String r14 = r12.mFilter
            r13.updateData(r0, r14)
            android.view.View r13 = r12.mEmptyView
            if (r13 == 0) goto L_0x0285
            boolean r13 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r0)
            if (r13 == 0) goto L_0x0280
            android.view.View r13 = r12.mEmptyView
            r13.setVisibility(r5)
            goto L_0x0285
        L_0x0280:
            android.view.View r13 = r12.mEmptyView
            r13.setVisibility(r4)
        L_0x0285:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.sip.PBXDirectorySearchListView.getSearchedBuddiesNormal(java.lang.String, boolean):void");
    }

    private int scoreFromCloudContact(String str) {
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str.trim())) {
            return DEFAUIT_SCORE;
        }
        String[] split = str.split("[\\s]+");
        if (split.length > 2) {
            return DEFAUIT_SCORE;
        }
        int i = 0;
        String lowerCase = split[0].toLowerCase();
        String lowerCase2 = split.length == 1 ? "" : split[1].toLowerCase();
        if (this.mFilterList.size() == 1 || StringUtil.isEmptyOrNull(lowerCase2)) {
            String str2 = (String) this.mFilterList.get(0);
            if (StringUtil.isEmptyOrNull(lowerCase2) && this.mFilterList.size() == 2) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append((String) this.mFilterList.get(1));
                str2 = sb.toString();
            }
            if (!StringUtil.isEmptyOrNull(lowerCase)) {
                int indexOf = lowerCase.indexOf(str2);
                if (indexOf == -1) {
                    i = lowerCase.length() + 1;
                } else if (indexOf == 0) {
                    return 0;
                } else {
                    return indexOf + 1;
                }
            }
            if (!StringUtil.isEmptyOrNull(lowerCase2)) {
                int indexOf2 = lowerCase2.indexOf(str2);
                if (indexOf2 != -1) {
                    if (indexOf2 == 0) {
                        return 1;
                    }
                    return i + indexOf2;
                }
            }
            return DEFAUIT_SCORE;
        } else if (this.mFilterList.size() != 2) {
            return DEFAUIT_SCORE;
        } else {
            String str3 = (String) this.mFilterList.get(1);
            if (lowerCase.indexOf((String) this.mFilterList.get(0)) == -1) {
                return DEFAUIT_SCORE;
            }
            int indexOf3 = lowerCase2.indexOf(str3);
            if (indexOf3 != 0) {
                return DEFAUIT_SCORE;
            }
            return indexOf3;
        }
    }

    private void setmFilterList() {
        if (!StringUtil.isEmptyOrNull(this.mFilter) && !StringUtil.isEmptyOrNull(this.mFilter.trim())) {
            this.mFilterList = Arrays.asList(this.mFilter.toLowerCase().split("[\\s]+"));
        }
    }

    public boolean isNumber(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return Pattern.compile("^[+]?[\\d]+$").matcher(str).matches();
    }

    private boolean matchedIMAddrBookItemFromPhone(IMAddrBookItem iMAddrBookItem, String str) {
        if (StringUtil.isEmptyOrNull(str) || iMAddrBookItem.getContact() == null) {
            return false;
        }
        ArrayList<ContactType> arrayList = iMAddrBookItem.getContact().accounts;
        if (CollectionsUtil.isCollectionEmpty(arrayList)) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ContactType contactType = (ContactType) it.next();
            if (contactType != null && !CollectionsUtil.isCollectionEmpty(contactType.phoneNumbers)) {
                Iterator it2 = contactType.phoneNumbers.iterator();
                while (it2.hasNext()) {
                    String str2 = ((PhoneNumber) it2.next()).number;
                    if (!StringUtil.isEmptyOrNull(str2) && str2.contains(str)) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    private List<IMAddrBookItem> getMatchedIMAddrBookItems(String str) {
        ArrayList arrayList = new ArrayList();
        if (isNumber(str) && !CollectionsUtil.isCollectionEmpty(this.mAllContactCacheSet)) {
            for (IMAddrBookItem iMAddrBookItem : this.mAllContactCacheSet) {
                if (!this.mAllContactJIDCacheSet.contains(iMAddrBookItem.getJid()) && matchedIMAddrBookItem(iMAddrBookItem, str)) {
                    arrayList.add(iMAddrBookItem);
                }
            }
        }
        this.mAllContactJIDCacheSet.clear();
        return arrayList;
    }

    private boolean matchedIMAddrBookItem(IMAddrBookItem iMAddrBookItem, String str) {
        if (iMAddrBookItem.getICloudSIPCallNumber() != null) {
            String extension = iMAddrBookItem.getICloudSIPCallNumber().getExtension();
            ArrayList<String> directNumber = iMAddrBookItem.getICloudSIPCallNumber().getDirectNumber();
            if (!StringUtil.isEmptyOrNull(extension) && extension.contains(str)) {
                return true;
            }
            if (!CollectionsUtil.isListEmpty(directNumber)) {
                for (String str2 : directNumber) {
                    if (!StringUtil.isEmptyOrNull(str2) && str2.contains(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void mergePhoneAddressToAllContactCache() {
        HashSet hashSet = new HashSet();
        for (IMAddrBookItem contactId : this.mAllContactCache) {
            hashSet.add(Integer.valueOf(contactId.getContactId()));
        }
        for (IMAddrBookItem iMAddrBookItem : getPhoneAddress()) {
            if (!hashSet.contains(Integer.valueOf(iMAddrBookItem.getContactId()))) {
                this.mAllContactCache.add(iMAddrBookItem);
            }
        }
    }

    @NonNull
    private List<IMAddrBookItem> getPhoneAddress() {
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            ZMLog.m286i(TAG, "getPhoneAddress  getZoomMessenger", new Object[0]);
            return arrayList;
        }
        ZoomBuddyGroup addressbookContactBuddyGroup = zoomMessenger.getAddressbookContactBuddyGroup();
        if (addressbookContactBuddyGroup == null) {
            ZMLog.m286i(TAG, "getPhoneAddress  getAddressbookContactBuddyGroup", new Object[0]);
            return arrayList;
        }
        ABContactsCache instance = ABContactsCache.getInstance();
        List<Contact> allCacheContacts = instance.getAllCacheContacts();
        if (CollectionsUtil.isCollectionEmpty(allCacheContacts)) {
            ZMLog.m286i(TAG, "getPhoneAddress find buddy size %d ", Integer.valueOf(arrayList.size()));
            return arrayList;
        }
        HashSet hashSet = new HashSet();
        if (PTApp.getInstance().isPhoneNumberRegistered()) {
            for (int i = 0; i < addressbookContactBuddyGroup.getBuddyCount(); i++) {
                ZoomBuddy buddyAt = addressbookContactBuddyGroup.getBuddyAt(i);
                if (buddyAt != null) {
                    ZMLog.m286i(TAG, "loadAllZoomPhoneContacts find buddy %s ", buddyAt.getJid());
                    String phoneNumber = buddyAt.getPhoneNumber();
                    if (StringUtil.isEmptyOrNull(phoneNumber)) {
                        ZMLog.m280e(TAG, "loadAllZoomPhoneContacts buddy in AddressbookContactBuddyGroup but no phone %s ", buddyAt.getJid());
                    } else {
                        Contact firstContactByPhoneNumber = instance.getFirstContactByPhoneNumber(phoneNumber);
                        if (firstContactByPhoneNumber == null) {
                            ZMLog.m280e(TAG, "loadAllZoomPhoneContacts buddy in AddressbookContactBuddyGroup but can not match %s number:%s", buddyAt.getJid(), phoneNumber);
                        } else {
                            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyAt);
                            if (fromZoomBuddy != null) {
                                hashSet.add(Integer.valueOf(firstContactByPhoneNumber.contactId));
                                fromZoomBuddy.setContact(firstContactByPhoneNumber);
                                if (fromZoomBuddy.isMyContact() || fromZoomBuddy.isPending()) {
                                    arrayList.add(fromZoomBuddy);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Contact contact : allCacheContacts) {
            if (!hashSet.contains(Integer.valueOf(contact.contactId))) {
                IMAddrBookItem fromContact = IMAddrBookItem.fromContact(contact);
                if (fromContact != null) {
                    arrayList.add(fromContact);
                }
            }
        }
        return arrayList;
    }

    private void init() {
        this.mAdapter = new PBXDirectorySearchAdapter(getContext());
        setAdapter(this.mAdapter);
        loadData();
    }

    private void loadData() {
        filter(null);
    }

    public boolean hasData() {
        PBXDirectorySearchAdapter pBXDirectorySearchAdapter = this.mAdapter;
        return pBXDirectorySearchAdapter != null && pBXDirectorySearchAdapter.getCount() > 0;
    }

    public void setFilterType(int i) {
        this.mFilterType = i;
        this.mAdapter.setFilterType(i);
        if (!TextUtils.isEmpty(this.mFilter)) {
            filter(this.mFilter, true);
        }
    }
}
