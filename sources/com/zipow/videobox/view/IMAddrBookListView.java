package com.zipow.videobox.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.IMAddrBookListFragment;
import com.zipow.videobox.fragment.IMBuddyListFragment;
import com.zipow.videobox.fragment.IMFavoriteListFragment;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.ContactsMatchHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookListAdapter.ItemOtherContacts;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.PullDownRefreshListView.PullDownRefreshListener;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class IMAddrBookListView extends QuickSearchListView implements OnItemClickListener, OnItemLongClickListener, PullDownRefreshListener, OnScrollListener {
    private static final int MSG_REFRESH_BUDDY_VCARDS = 1;
    private static final String TAG = "IMAddrBookListView";
    @Nullable
    private static HashMap<String, IMAddrBookItem> gCachedZoomOnlyItems;
    private IMAddrBookListAdapter mAdapter;
    @Nullable
    private String mFilter;
    /* access modifiers changed from: private */
    public IMAddrBookListFragment mFragment;
    @NonNull
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                IMAddrBookListView.this.refreshBuddyVcards();
                sendEmptyMessageDelayed(1, 2000);
            }
        }
    };
    @NonNull
    private List<String> mPendingInfoChangeJids = new ArrayList();
    private boolean mPendingNeedNotifyDataSetChange;
    @NonNull
    private List<String> mPendingPresenceChangeJids = new ArrayList();
    private int mScorllState;

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        return false;
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    public static void cacheItems(HashMap<String, IMAddrBookItem> hashMap) {
        gCachedZoomOnlyItems = hashMap;
    }

    public static void clearCaches() {
        gCachedZoomOnlyItems = null;
    }

    private static void updateCachedItemWithJid(String str, IMAddrBookItem iMAddrBookItem) {
        if (!StringUtil.isEmptyOrNull(str)) {
            HashMap<String, IMAddrBookItem> hashMap = gCachedZoomOnlyItems;
            if (!(hashMap == null || ((IMAddrBookItem) hashMap.get(str)) == null)) {
                gCachedZoomOnlyItems.put(str, iMAddrBookItem);
            }
        }
    }

    private static void addCachedItemWithJid(String str, IMAddrBookItem iMAddrBookItem) {
        if (!StringUtil.isEmptyOrNull(str)) {
            HashMap<String, IMAddrBookItem> hashMap = gCachedZoomOnlyItems;
            if (hashMap != null) {
                hashMap.put(str, iMAddrBookItem);
            }
        }
    }

    public static boolean fillAdapterFromCache(@Nullable IMAddrBookListAdapter iMAddrBookListAdapter, String str) {
        if (iMAddrBookListAdapter == null) {
            return false;
        }
        HashMap<String, IMAddrBookItem> hashMap = gCachedZoomOnlyItems;
        if (hashMap == null) {
            return false;
        }
        iMAddrBookListAdapter.addItems(hashMap.values(), PTSettingHelper.getShowOfflineBuddies(), str);
        iMAddrBookListAdapter.clear();
        iMAddrBookListAdapter.addItems(gCachedZoomOnlyItems.values());
        return true;
    }

    public static boolean removeCachedItemWithJid(String str) {
        boolean z = false;
        if (!StringUtil.isEmptyOrNull(str)) {
            HashMap<String, IMAddrBookItem> hashMap = gCachedZoomOnlyItems;
            if (hashMap != null) {
                if (hashMap.remove(str) != null) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public IMAddrBookListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public IMAddrBookListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public IMAddrBookListView(Context context) {
        super(context);
        initView();
    }

    public void setParentFragment(IMAddrBookListFragment iMAddrBookListFragment) {
        this.mFragment = iMAddrBookListFragment;
    }

    /* access modifiers changed from: private */
    public void refreshBuddyVcards() {
        List waitRefreshJids = this.mAdapter.getWaitRefreshJids();
        HashSet hashSet = new HashSet();
        int childCount = getListView().getChildCount() * 2;
        for (int size = waitRefreshJids.size() - 1; size >= 0; size--) {
            hashSet.add(waitRefreshJids.get(size));
            if (hashSet.size() >= childCount) {
                break;
            }
        }
        if (hashSet.size() != 0) {
            this.mAdapter.clearWaitRefreshJids();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(hashSet);
                zoomMessenger.refreshBuddyVCards(arrayList);
                if (arrayList.size() > 0 && zoomMessenger.isAnyBuddyGroupLarge()) {
                    zoomMessenger.getBuddiesPresence(arrayList, false);
                }
            }
        }
    }

    public void onBuddyListUpdate() {
        IMAddrBookListFragment iMAddrBookListFragment = this.mFragment;
        if (iMAddrBookListFragment == null || iMAddrBookListFragment.isResumed()) {
            loadAllBuddies(this.mAdapter);
        } else {
            this.mPendingNeedNotifyDataSetChange = true;
        }
    }

    public void onBuddyInfoUpdate(@NonNull List<String> list, @NonNull List<String> list2) {
        IMAddrBookListFragment iMAddrBookListFragment = this.mFragment;
        if (iMAddrBookListFragment != null && !iMAddrBookListFragment.isResumed()) {
            this.mPendingInfoChangeJids.addAll(list2);
            this.mPendingPresenceChangeJids.addAll(list);
        } else if (isShowOfflineBuddies() || (CollectionsUtil.isListEmpty(list2) && CollectionsUtil.isListEmpty(list))) {
            if (!CollectionsUtil.isListEmpty(list2)) {
                this.mAdapter.notifyDataSetChanged();
            } else if (this.mScorllState != 2 && !CollectionsUtil.isListEmpty(list)) {
                if (this.mAdapter.getCount() < 10) {
                    loadAllBuddies(this.mAdapter);
                    return;
                }
                ListView listView = getListView();
                for (int i = 0; i < listView.getChildCount(); i++) {
                    View childAt = listView.getChildAt(i);
                    if (childAt instanceof IMAddrBookItemView) {
                        IMAddrBookItemView iMAddrBookItemView = (IMAddrBookItemView) childAt;
                        IMAddrBookItem dataItem = iMAddrBookItemView.getDataItem();
                        if (dataItem != null && list.contains(dataItem.getJid())) {
                            iMAddrBookItemView.setAddrBookItem(dataItem, dataItem.getNeedIndicateZoomUser(), false, false);
                        }
                    }
                }
            }
        } else {
            loadAllBuddies(this.mAdapter);
        }
    }

    private void initView() {
        this.mAdapter = new IMAddrBookListAdapter(getContext(), this);
        if (isInEditMode()) {
            _editmode_loadAllItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
        setPullDownRefreshTextResources(C4558R.string.zm_lbl_release_to_refresh, C4558R.string.zm_lbl_pull_down_to_refresh, C4558R.string.zm_msg_loading);
        setPullDownRefreshListener(this);
        getListView().setOnScrollListener(this);
        getListView().setDivider(null);
        getListView().setDividerHeight(0);
        addHeaderViews();
    }

    private void addHeaderViews() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_addrbook_item, null);
        inflate.setPadding(0, UIUtil.dip2px(getContext(), 10.0f), 0, UIUtil.dip2px(getContext(), 10.0f));
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtCustomMessage);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtScreenName)).setText(C4558R.string.zm_lbl_my_contacts_33300);
        textView.setText(C4558R.string.zm_lbl_my_contacts_des_33300);
        inflate.findViewById(C4558R.C4560id.presenceStateView).setVisibility(8);
        AvatarView avatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.avatarView);
        avatarView.show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_ic_my_phone_contacts, null));
        avatarView.setCornerRadiusRatio(0.0f);
        getListView().addHeaderView(inflate);
        inflate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (IMAddrBookListView.this.mFragment != null) {
                    IMAddrBookListView.this.mFragment.showPhoneContactsInZoom();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mHandler.sendEmptyMessageDelayed(1, 2000);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeMessages(1);
        super.onDetachedFromWindow();
    }

    private void _editmode_loadAllItems(@NonNull IMAddrBookListAdapter iMAddrBookListAdapter) {
        String isoCountryCode2PhoneCountryCode = CountryCodeUtil.isoCountryCode2PhoneCountryCode(CountryCodeUtil.getIsoCountryCode(VideoBoxApplication.getInstance()));
        for (int i = 0; i < 4; i++) {
            IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
            iMAddrBookItem.setContactId(i + NotificationMgr.ZOOM_SIP_MISSED_NOTICICATION_ID_START);
            StringBuilder sb = new StringBuilder();
            sb.append("Zoom User ");
            sb.append(i);
            iMAddrBookItem.setScreenName(sb.toString());
            iMAddrBookItem.setSortKey(iMAddrBookItem.getScreenName());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("+861380000000");
            sb2.append(String.valueOf(i));
            iMAddrBookItem.addPhoneNumber(sb2.toString(), null, isoCountryCode2PhoneCountryCode);
            iMAddrBookItem.setIsZoomUser(true);
            iMAddrBookListAdapter.addItem(iMAddrBookItem);
        }
    }

    private void loadAllBuddies(IMAddrBookListAdapter iMAddrBookListAdapter) {
        ABContactsCache aBContactsCache;
        setFooterDividersEnabled(true);
        setPullDownRefreshEnabled(PTApp.getInstance().isPhoneNumberRegistered());
        if (getContext() != null) {
            String str = null;
            if (PTApp.getInstance().isPhoneNumberRegistered()) {
                aBContactsCache = ABContactsCache.getInstance();
                if (aBContactsCache.isCached() || aBContactsCache.reloadAllContacts()) {
                    ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
                    if (aBContactsHelper != null) {
                        str = aBContactsHelper.getVerifiedPhoneNumber();
                    }
                } else {
                    return;
                }
            } else {
                aBContactsCache = null;
            }
            HashMap hashMap = new HashMap();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                List<IMAddrBookItem> allBuddies = ZMBuddySyncInstance.getInsatance().getAllBuddies();
                List<String> starSessionGetAll = zoomMessenger.starSessionGetAll();
                if (starSessionGetAll != null) {
                    for (String buddyWithJID : starSessionGetAll) {
                        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomMessenger.getBuddyWithJID(buddyWithJID));
                        if (fromZoomBuddy != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(QuickSearchListView.STARRED_GROUP_CATEGORY_CHAR);
                            sb.append(fromZoomBuddy.getSortKey());
                            fromZoomBuddy.setSortKey(sb.toString());
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(fromZoomBuddy.getJid());
                            sb2.append(fromZoomBuddy.getSortKey());
                            hashMap.put(sb2.toString(), fromZoomBuddy);
                        }
                    }
                }
                ZoomBuddyGroup buddyGroupByType = zoomMessenger.getBuddyGroupByType(61);
                if (buddyGroupByType != null) {
                    int buddyCount = buddyGroupByType.getBuddyCount();
                    if (buddyCount > 0) {
                        setCategoryTitle('\"', buddyGroupByType.getName());
                    }
                    for (int i = 0; i < buddyCount; i++) {
                        IMAddrBookItem fromZoomBuddy2 = IMAddrBookItem.fromZoomBuddy(buddyGroupByType.getBuddyAt(i));
                        if (fromZoomBuddy2 != null) {
                            hashMap.put(fromZoomBuddy2.getJid(), fromZoomBuddy2);
                        }
                    }
                }
                for (IMAddrBookItem iMAddrBookItem : allBuddies) {
                    if (!iMAddrBookItem.isZoomRoomContact()) {
                        if (iMAddrBookItem.isPropertyInit()) {
                            if (!zoomMessenger.isMyContactOrPending(iMAddrBookItem.getJid())) {
                            }
                        } else if (!iMAddrBookItem.isMyContact() && !iMAddrBookItem.isPending()) {
                        }
                        if (StringUtil.isEmptyOrNull(str) || !StringUtil.isSameString(str, iMAddrBookItem.getBuddyPhoneNumber())) {
                            if (aBContactsCache != null) {
                                Contact firstContactByPhoneNumber = aBContactsCache.getFirstContactByPhoneNumber(iMAddrBookItem.getBuddyPhoneNumber());
                                iMAddrBookItem.setIsMobileOnline(iMAddrBookItem.getIsMobileOnline() || ((firstContactByPhoneNumber != null ? firstContactByPhoneNumber.contactId : 0) >= 0 && !iMAddrBookItem.getIsDesktopOnline() && !iMAddrBookItem.isMyContact()));
                            }
                            if (isShowOfflineBuddies() || iMAddrBookItem.getIsDesktopOnline() || iMAddrBookItem.getIsMobileOnline()) {
                                hashMap.put(iMAddrBookItem.getJid(), iMAddrBookItem);
                            }
                        }
                    }
                }
                cacheItems(hashMap);
                fillAdapterFromCache(iMAddrBookListAdapter, this.mFilter);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    private IMAddrBookItem newAddrBookItemFromZoomBuddy(@NonNull ZoomMessenger zoomMessenger, @NonNull ZoomBuddy zoomBuddy, String str, ABContactsCache aBContactsCache, boolean z, boolean z2) {
        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomBuddy);
        if (!StringUtil.isEmptyOrNull(str) && str.equals(zoomBuddy.getPhoneNumber())) {
            return null;
        }
        boolean isMyContact = zoomMessenger.isMyContact(zoomBuddy.getJid());
        if (z || isMyContact || (z2 && (fromZoomBuddy == null || fromZoomBuddy.getContactId() >= 0))) {
            return fromZoomBuddy;
        }
        return null;
    }

    public void addBuddyWithJid(String str) {
        ABContactsCache aBContactsCache;
        String str2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                boolean isPhoneNumberRegistered = PTApp.getInstance().isPhoneNumberRegistered();
                if (isPhoneNumberRegistered) {
                    ABContactsCache instance = ABContactsCache.getInstance();
                    ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
                    if (aBContactsHelper != null) {
                        str2 = aBContactsHelper.getVerifiedPhoneNumber();
                        aBContactsCache = instance;
                    } else {
                        str2 = null;
                        aBContactsCache = instance;
                    }
                } else {
                    str2 = null;
                    aBContactsCache = null;
                }
                IMAddrBookItem newAddrBookItemFromZoomBuddy = newAddrBookItemFromZoomBuddy(zoomMessenger, buddyWithJID, str2, aBContactsCache, false, isPhoneNumberRegistered);
                if (newAddrBookItemFromZoomBuddy != null && !newAddrBookItemFromZoomBuddy.isZoomRoomContact()) {
                    if (isShowOfflineBuddies() || newAddrBookItemFromZoomBuddy.getIsDesktopOnline() || newAddrBookItemFromZoomBuddy.getIsMobileOnline()) {
                        addCachedItemWithJid(str, newAddrBookItemFromZoomBuddy);
                        fillAdapterFromCache(this.mAdapter, this.mFilter);
                    }
                }
            }
        }
    }

    public void onAddBuddyByJidSuccess(String str) {
        addBuddyWithJid(str);
    }

    public void updateBuddyInfoWithJid(String str) {
        ABContactsCache aBContactsCache;
        String str2;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                boolean isPhoneNumberRegistered = PTApp.getInstance().isPhoneNumberRegistered();
                if (isPhoneNumberRegistered) {
                    ABContactsCache instance = ABContactsCache.getInstance();
                    ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
                    if (aBContactsHelper != null) {
                        str2 = aBContactsHelper.getVerifiedPhoneNumber();
                        aBContactsCache = instance;
                    } else {
                        str2 = null;
                        aBContactsCache = instance;
                    }
                } else {
                    str2 = null;
                    aBContactsCache = null;
                }
                IMAddrBookItem newAddrBookItemFromZoomBuddy = newAddrBookItemFromZoomBuddy(zoomMessenger, buddyWithJID, str2, aBContactsCache, false, isPhoneNumberRegistered);
                if (newAddrBookItemFromZoomBuddy != null && !newAddrBookItemFromZoomBuddy.isZoomRoomContact()) {
                    if (isShowOfflineBuddies()) {
                        updateCachedItemWithJid(str, newAddrBookItemFromZoomBuddy);
                    } else if (newAddrBookItemFromZoomBuddy.getIsDesktopOnline() || newAddrBookItemFromZoomBuddy.getIsMobileOnline()) {
                        if (PTApp.getInstance().isSyncUserGroupON()) {
                            ZoomBuddyGroup buddyGroupByType = zoomMessenger.getBuddyGroupByType(1);
                            if (buddyGroupByType == null || !buddyGroupByType.hasBuddy(newAddrBookItemFromZoomBuddy.getJid())) {
                                ZoomBuddyGroup buddyGroupByType2 = zoomMessenger.getBuddyGroupByType(2);
                                if (buddyGroupByType2 != null && buddyGroupByType2.hasBuddy(newAddrBookItemFromZoomBuddy.getJid())) {
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        addCachedItemWithJid(str, newAddrBookItemFromZoomBuddy);
                    } else {
                        removeCachedItemWithJid(str);
                    }
                    fillAdapterFromCache(this.mAdapter, this.mFilter);
                }
            }
        }
    }

    public void reloadAllItems() {
        reloadAllItems(false);
    }

    public void reloadAllItems(boolean z) {
        IMAddrBookListFragment iMAddrBookListFragment = this.mFragment;
        if (iMAddrBookListFragment == null || iMAddrBookListFragment.isResumed()) {
            if (this.mAdapter.getCount() == 0 || this.mPendingNeedNotifyDataSetChange) {
                this.mAdapter.clear();
                loadAllBuddies(this.mAdapter);
                this.mAdapter.notifyDataSetChanged();
            } else {
                onBuddyInfoUpdate(this.mPendingPresenceChangeJids, this.mPendingInfoChangeJids);
            }
            this.mPendingNeedNotifyDataSetChange = false;
            this.mPendingInfoChangeJids.clear();
            this.mPendingPresenceChangeJids.clear();
        }
    }

    public int getContactsItemCount() {
        return this.mAdapter.getContactsItemCount();
    }

    public void onNotifySubscribeRequestUpdated(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (zoomMessenger.isMyContact(str)) {
                    addBuddyWithJid(str);
                } else if (removeCachedItemWithJid(str)) {
                    fillAdapterFromCache(this.mAdapter, this.mFilter);
                }
            }
        }
    }

    public void onNotifySubscriptionDenied(String str) {
        if (removeCachedItemWithJid(str)) {
            fillAdapterFromCache(this.mAdapter, this.mFilter);
        }
    }

    public void onZoomMessengerConnectReturn() {
        IMAddrBookListFragment iMAddrBookListFragment = this.mFragment;
        boolean z = iMAddrBookListFragment != null && !iMAddrBookListFragment.isResumed();
        ListView listView = getListView();
        for (int i = 0; i < listView.getChildCount(); i++) {
            View childAt = listView.getChildAt(i);
            if (childAt instanceof IMAddrBookItemView) {
                IMAddrBookItemView iMAddrBookItemView = (IMAddrBookItemView) childAt;
                IMAddrBookItem dataItem = iMAddrBookItemView.getDataItem();
                if (dataItem != null) {
                    if (z) {
                        this.mPendingPresenceChangeJids.add(dataItem.getJid());
                    } else {
                        iMAddrBookItemView.setAddrBookItem(dataItem, dataItem.getNeedIndicateZoomUser(), false, false);
                    }
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (itemAtPosition != null) {
            if (!(itemAtPosition instanceof IMAddrBookItem)) {
                if (itemAtPosition instanceof ItemOtherContacts) {
                    switch (((ItemOtherContacts) itemAtPosition).type) {
                        case 0:
                            showZoomContacts();
                            break;
                        case 1:
                            showGoogleContacts();
                            break;
                        case 2:
                            showFacebookContacts();
                            break;
                    }
                }
            } else {
                onItemClick((IMAddrBookItem) itemAtPosition);
            }
        }
    }

    private void showFacebookContacts() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            IMBuddyListFragment.showAsActivity(zMActivity);
        }
    }

    private void showGoogleContacts() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            IMBuddyListFragment.showAsActivity(zMActivity);
        }
    }

    private void showZoomContacts() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            IMFavoriteListFragment.showAsActivity(zMActivity);
        }
    }

    private void onItemClick(final IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem.isPending()) {
            new Builder(getContext()).setTitle((CharSequence) getContext().getString(C4558R.string.zm_title_remove_contact, new Object[]{iMAddrBookItem.getScreenName()})).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    iMAddrBookItem.removeItem(IMAddrBookListView.this.getContext());
                }
            }).create().show();
            return;
        }
        showUserActions(iMAddrBookItem);
    }

    public void showUserActions(@Nullable IMAddrBookItem iMAddrBookItem) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (iMAddrBookItem != null && iMAddrBookItem.getAccountStatus() == 2) {
                    return;
                }
                if (iMAddrBookItem == null || !iMAddrBookItem.getIsRobot()) {
                    AddrBookItemDetailsActivity.show(zMActivity, iMAddrBookItem, 106);
                    return;
                }
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(iMAddrBookItem.getJid());
                if (buddyWithJID != null) {
                    MMChatActivity.showAsOneToOneChat(zMActivity, buddyWithJID);
                }
            }
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            Parcelable parcelable2 = bundle.getParcelable("IMAddrBookListView.superState");
            this.mFilter = bundle.getString("IMAddrBookListView.mFilter");
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("IMAddrBookListView.superState", onSaveInstanceState);
        bundle.putString("IMAddrBookListView.mFilter", this.mFilter);
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
        if (str == null) {
            str = "";
        }
        String lowerCase = str.trim().toLowerCase(CompatUtils.getLocalDefault());
        String str2 = this.mFilter;
        this.mFilter = lowerCase;
        if (str2 == null) {
            str2 = "";
        }
        if (!str2.equals(lowerCase)) {
            if (StringUtil.isEmptyOrNull(lowerCase)) {
                reloadAllItems();
            } else if (StringUtil.isEmptyOrNull(str2) || !lowerCase.contains(str2)) {
                reloadAllItems();
            } else {
                this.mAdapter.setHasWebSearchResults(false);
                this.mAdapter.filter(lowerCase);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onPullDownRefresh() {
        if (startABMatching() != 0) {
            notifyRefreshDone();
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
        notifyRefreshDone();
    }

    public void updateUnreadMessageCountBubble() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void onClickSearchMore() {
        if (!StringUtil.isEmptyOrNull(this.mFilter)) {
            IMAddrBookListFragment iMAddrBookListFragment = this.mFragment;
            if (iMAddrBookListFragment != null) {
                iMAddrBookListFragment.searchMore(this.mFilter);
            }
        }
    }

    public void onClickSavedSession() {
        IMAddrBookListFragment iMAddrBookListFragment = this.mFragment;
        if (iMAddrBookListFragment != null) {
            iMAddrBookListFragment.gotoSavedSessions();
        }
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        this.mScorllState = i;
        if (i == 2) {
            this.mHandler.removeMessages(1);
        } else if (!this.mHandler.hasMessages(1)) {
            this.mHandler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    private boolean isShowOfflineBuddies() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || !zoomMessenger.isAnyBuddyGroupLarge()) {
            return PTSettingHelper.getShowOfflineBuddies();
        }
        return true;
    }
}
