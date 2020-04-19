package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.InviteLocalContactsFragment;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.ContactsMatchHelper;
import com.zipow.videobox.ptapp.PTApp;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class InviteLocalContactsListView extends ListView implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = "InviteLocalContactsListView";
    @Nullable
    private static List<LocalContactItem> gCachedNoneZoomItems;
    /* access modifiers changed from: private */
    public InviteLocalContactsListAdapter mAdapter;
    @Nullable
    private String mFilter;
    private InviteLocalContactsFragment mFragment;

    private void onItemClick(LocalContactItem localContactItem) {
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        return false;
    }

    public static void cacheItems(@Nullable InviteLocalContactsListAdapter inviteLocalContactsListAdapter) {
        if (inviteLocalContactsListAdapter != null) {
            gCachedNoneZoomItems = inviteLocalContactsListAdapter.cache();
        }
    }

    public static void clearCaches() {
        gCachedNoneZoomItems = null;
    }

    public static boolean fillAdapterFromCache(@Nullable InviteLocalContactsListAdapter inviteLocalContactsListAdapter) {
        if (inviteLocalContactsListAdapter == null) {
            return false;
        }
        List<LocalContactItem> list = gCachedNoneZoomItems;
        if (list == null) {
            return false;
        }
        inviteLocalContactsListAdapter.setItems(list);
        return true;
    }

    public InviteLocalContactsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public InviteLocalContactsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public InviteLocalContactsListView(Context context) {
        super(context);
        initView();
    }

    public void setParentFragment(InviteLocalContactsFragment inviteLocalContactsFragment) {
        this.mFragment = inviteLocalContactsFragment;
    }

    private void initView() {
        this.mAdapter = new InviteLocalContactsListAdapter(getContext(), this);
        if (isInEditMode()) {
            _editmode_loadAllItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
    }

    private void _editmode_loadAllItems(@NonNull InviteLocalContactsListAdapter inviteLocalContactsListAdapter) {
        String isoCountryCode2PhoneCountryCode = CountryCodeUtil.isoCountryCode2PhoneCountryCode(CountryCodeUtil.getIsoCountryCode(VideoBoxApplication.getInstance()));
        for (int i = 0; i < 10; i++) {
            LocalContactItem localContactItem = new LocalContactItem();
            localContactItem.setContactId(i + 10000);
            StringBuilder sb = new StringBuilder();
            sb.append("Non-zoom User ");
            sb.append(i);
            localContactItem.setScreenName(sb.toString());
            localContactItem.setSortKey(localContactItem.getScreenName());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("+861390000000");
            sb2.append(String.valueOf(i));
            localContactItem.addPhoneNumber(sb2.toString(), null, isoCountryCode2PhoneCountryCode);
            localContactItem.setIsZoomUser(false);
            inviteLocalContactsListAdapter.addItem(localContactItem);
        }
    }

    private void loadAllItemsForInvite(@NonNull InviteLocalContactsListAdapter inviteLocalContactsListAdapter) {
        String str;
        int i;
        InviteLocalContactsListAdapter inviteLocalContactsListAdapter2 = inviteLocalContactsListAdapter;
        if (!PTApp.getInstance().isPhoneNumberRegistered()) {
            inviteLocalContactsListAdapter2.setAddrBookEnabled(false);
            setFooterDividersEnabled(false);
            return;
        }
        inviteLocalContactsListAdapter2.setAddrBookEnabled(true);
        setFooterDividersEnabled(true);
        if (getContext() != null && !fillAdapterFromCache(inviteLocalContactsListAdapter)) {
            ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
            if (aBContactsHelper != null) {
                ArrayList arrayList = new ArrayList();
                aBContactsHelper.getMatchedPhoneNumbers(arrayList);
                Collator instance = Collator.getInstance(Locale.US);
                Collections.sort(arrayList, instance);
                ABContactsCache instance2 = ABContactsCache.getInstance();
                if (instance2.isCached() || instance2.reloadAllContacts()) {
                    LocalContactItem localContactItem = null;
                    String verifiedPhoneNumber = aBContactsHelper.getVerifiedPhoneNumber();
                    int i2 = 0;
                    while (i2 < instance2.getCachedContactsCount()) {
                        Contact cachedContact = instance2.getCachedContact(i2);
                        if (cachedContact == null) {
                            i = i2;
                            str = verifiedPhoneNumber;
                        } else {
                            int i3 = cachedContact.contactId;
                            String str2 = cachedContact.displayName;
                            String str3 = cachedContact.number;
                            String str4 = cachedContact.normalizedNumber;
                            String str5 = cachedContact.sortKey;
                            if (str4.equals(verifiedPhoneNumber)) {
                                i = i2;
                                str = verifiedPhoneNumber;
                            } else {
                                String str6 = this.mFilter;
                                if (str6 != null && str6.length() > 0) {
                                    if (!str2.toLowerCase(CompatUtils.getLocalDefault()).contains(this.mFilter.toLowerCase(CompatUtils.getLocalDefault()))) {
                                        i = i2;
                                        str = verifiedPhoneNumber;
                                    }
                                }
                                if (StringUtil.isEmptyOrNull(str3)) {
                                    i = i2;
                                    str = verifiedPhoneNumber;
                                } else if (localContactItem == null) {
                                    i = i2;
                                    str = verifiedPhoneNumber;
                                    localContactItem = newListItem(i3, str2, str3, str4, null, null, str5, arrayList, instance);
                                } else {
                                    i = i2;
                                    str = verifiedPhoneNumber;
                                    if (localContactItem.getContactId() == i3) {
                                        int binarySearch = Collections.binarySearch(arrayList, str4, instance);
                                        if ((binarySearch < 0 && !localContactItem.getIsZoomUser()) || (binarySearch >= 0 && localContactItem.getIsZoomUser())) {
                                            localContactItem.addPhoneNumber(str3, str4);
                                        } else if (binarySearch >= 0 && !localContactItem.getIsZoomUser()) {
                                            localContactItem.clearPhoneNumbers();
                                            localContactItem.addPhoneNumber(str3, str4);
                                            localContactItem.setIsZoomUser(true);
                                        }
                                    } else {
                                        inviteLocalContactsListAdapter2.addItem(localContactItem);
                                        localContactItem = newListItem(i3, str2, str3, str4, null, null, str5, arrayList, instance);
                                    }
                                }
                            }
                        }
                        i2 = i + 1;
                        verifiedPhoneNumber = str;
                    }
                    if (localContactItem != null) {
                        inviteLocalContactsListAdapter2.addItem(localContactItem);
                    }
                    inviteLocalContactsListAdapter.sort();
                    cacheItems(inviteLocalContactsListAdapter);
                }
            }
        }
    }

    @NonNull
    private LocalContactItem newListItem(int i, String str, String str2, String str3, String str4, String str5, String str6, @NonNull ArrayList<String> arrayList, Collator collator) {
        LocalContactItem localContactItem = new LocalContactItem();
        localContactItem.setContactId(i);
        localContactItem.setScreenName(str);
        localContactItem.setSortKey(str6);
        boolean z = false;
        localContactItem.setNeedIndicateZoomUser(false);
        localContactItem.setJid(str4);
        localContactItem.setAccoutEmail(str5);
        if (Collections.binarySearch(arrayList, localContactItem.addPhoneNumber(str2, str3), collator) >= 0) {
            z = true;
        }
        localContactItem.setIsZoomUser(z);
        return localContactItem;
    }

    public void reloadAllItems() {
        this.mAdapter.clear();
        String str = this.mFilter;
        this.mFilter = null;
        loadAllItemsForInvite(this.mAdapter);
        this.mFilter = str;
        if (!StringUtil.isEmptyOrNull(this.mFilter)) {
            this.mAdapter.filter(this.mFilter);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public int getContactsItemCount() {
        return this.mAdapter.getContactsItemCount();
    }

    public void notifyDataSetChanged() {
        notifyDataSetChanged(false);
    }

    public void notifyDataSetChanged(boolean z) {
        if (z) {
            this.mAdapter.setLazyLoadAvatarDisabled(true);
            postDelayed(new Runnable() {
                public void run() {
                    InviteLocalContactsListView.this.mAdapter.setLazyLoadAvatarDisabled(false);
                }
            }, 1000);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (itemAtPosition != null && (itemAtPosition instanceof LocalContactItem)) {
            onItemClick((LocalContactItem) itemAtPosition);
        }
    }

    public void showUserActions(LocalContactItem localContactItem) {
        if (((ZMActivity) getContext()) != null) {
            this.mFragment.showNonZoomUserActions(localContactItem);
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            Parcelable parcelable2 = bundle.getParcelable("InviteLocalContactsListView.superState");
            this.mFilter = bundle.getString("InviteLocalContactsListView.mFilter");
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("InviteLocalContactsListView.superState", onSaveInstanceState);
        bundle.putString("InviteLocalContactsListView.mFilter", this.mFilter);
        return bundle;
    }

    public void setFilter(@Nullable String str) {
        this.mFilter = str;
    }

    @Nullable
    public String getFilter() {
        return this.mFilter;
    }

    public void filter(@Nullable String str) {
        String str2 = this.mFilter;
        this.mFilter = str;
        if (str2 == null) {
            str2 = "";
        }
        if (str == null) {
            str = "";
        }
        String lowerCase = str.toLowerCase(CompatUtils.getLocalDefault());
        String lowerCase2 = str2.toLowerCase(CompatUtils.getLocalDefault());
        if (!lowerCase2.equals(lowerCase)) {
            if (StringUtil.isEmptyOrNull(lowerCase)) {
                reloadAllItems();
            } else if (StringUtil.isEmptyOrNull(lowerCase2) || lowerCase.contains(lowerCase2)) {
                this.mAdapter.filter(lowerCase);
                this.mAdapter.notifyDataSetChanged();
            } else {
                reloadAllItems();
            }
        }
    }

    public int startABMatching() {
        Context context = getContext();
        if (context == null) {
            return 11;
        }
        if (!PTApp.getInstance().isWebSignedOn()) {
            return 9;
        }
        return ContactsMatchHelper.getInstance().matchAllNumbers(context);
    }

    public void onPhoneABMatchUpdated(long j) {
        clearCaches();
        reloadAllItems();
    }

    public void updateUnreadMessageCountBubble() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void onClickBtnEnableAddrBook() {
        this.mFragment.enableAddrBook();
    }
}
