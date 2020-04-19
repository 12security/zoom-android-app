package com.zipow.videobox.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import com.zipow.videobox.dialog.InformationBarriesDialog;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ContactsMatchHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyUserInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.PinnedSectionRecyclerView;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.PinnedSectionRecyclerView.OnPinnedSectionClick;
import p021us.zoom.videomeetings.C4558R;

public class IMDirectoryRecyclerView extends PinnedSectionRecyclerView implements OnPinnedSectionClick {
    private static final String TAG = "IMDirectoryRecyclerView";
    /* access modifiers changed from: private */
    public IMDirectoryAdapter mAdapter;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    @NonNull
    private Runnable mRreshVcardRunnable = new Runnable() {
        public void run() {
            IMDirectoryRecyclerView.this.mHandler.postDelayed(this, 1000);
            List showedBuddies = IMDirectoryRecyclerView.this.mAdapter.getShowedBuddies(true);
            if (IMDirectoryRecyclerView.this.getScrollState() != 2 && !CollectionsUtil.isCollectionEmpty(showedBuddies)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    int childCount = IMDirectoryRecyclerView.this.getChildCount();
                    if (showedBuddies.size() > childCount) {
                        showedBuddies = showedBuddies.subList(showedBuddies.size() - childCount, showedBuddies.size());
                    }
                    zoomMessenger.refreshBuddyVCards(showedBuddies);
                }
            }
        }
    };

    public void onZoomMessengerConnectReturn(int i) {
    }

    public IMDirectoryRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public IMDirectoryRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public IMDirectoryRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void onBlockedByIB(List<String> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            reloadStarItems(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mHandler.postDelayed(this.mRreshVcardRunnable, 2000);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeCallbacks(this.mRreshVcardRunnable);
        super.onDetachedFromWindow();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()) {
            public LayoutParams generateDefaultLayoutParams() {
                return new LayoutParams(-1, -2);
            }
        });
        boolean isSpokenFeedbackEnabled = AccessibilityUtil.isSpokenFeedbackEnabled(getContext());
        this.mAdapter = new IMDirectoryAdapter(isSpokenFeedbackEnabled);
        if (isSpokenFeedbackEnabled) {
            setItemAnimator(null);
            this.mAdapter.setHasStableIds(true);
        }
        this.mAdapter.setRecyclerView(this);
        setAdapter(this.mAdapter);
        showShadow(false);
        setOnPinnedSectionClick(this);
    }

    public void reloadAllItems(boolean z) {
        if (!z || this.mAdapter.isDataEmpty()) {
            this.mAdapter.clearData(false);
            ZMBuddySyncInstance insatance = ZMBuddySyncInstance.getInsatance();
            List<MMZoomBuddyGroup> allBuddyGroup = insatance.getAllBuddyGroup();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                int personalGroupGetOption = zoomMessenger.personalGroupGetOption();
                for (MMZoomBuddyGroup mMZoomBuddyGroup : allBuddyGroup) {
                    if (mMZoomBuddyGroup.getType() != 10 && (personalGroupGetOption == 1 || mMZoomBuddyGroup.getType() != 500)) {
                        Set<String> buddiesInBuddyGroup = insatance.getBuddiesInBuddyGroup(mMZoomBuddyGroup.getId());
                        if (buddiesInBuddyGroup != null) {
                            ArrayList arrayList = new ArrayList();
                            boolean z2 = mMZoomBuddyGroup.getType() == 0;
                            for (String buddyByJid : buddiesInBuddyGroup) {
                                IMAddrBookItem buddyByJid2 = insatance.getBuddyByJid(buddyByJid, buddiesInBuddyGroup.size() < 20);
                                if (buddyByJid2 != null && (!z2 || buddyByJid2.isMyContact())) {
                                    arrayList.add(buddyByJid2);
                                }
                            }
                            this.mAdapter.updateBuddiesInBuddyGroup(mMZoomBuddyGroup, arrayList, false);
                        }
                    }
                }
                reloadPhoneAddressGroup(true);
                reloadStarItems(true);
                reloadRoomSystemsItem();
                this.mAdapter.refresh(true);
            }
        }
    }

    public void reloadPhoneAddressGroup(boolean z) {
        if (!z || this.mAdapter.isPhoneAddrEmpty()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddyGroup addressbookContactBuddyGroup = zoomMessenger.getAddressbookContactBuddyGroup();
                if (addressbookContactBuddyGroup != null) {
                    ABContactsCache instance = ABContactsCache.getInstance();
                    List<Contact> allCacheContacts = instance.getAllCacheContacts();
                    if (!CollectionsUtil.isCollectionEmpty(allCacheContacts)) {
                        HashSet hashSet = new HashSet();
                        ArrayList arrayList = new ArrayList();
                        if (PTApp.getInstance().isPhoneNumberRegistered() || (CmmSIPCallManager.getInstance().isCloudPBXEnabled() && CmmSIPCallManager.getInstance().isPBXActive())) {
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
                        MMZoomBuddyGroup fromZoomBuddyGroup = MMZoomBuddyGroup.fromZoomBuddyGroup(addressbookContactBuddyGroup);
                        fromZoomBuddyGroup.setBuddyCount(arrayList.size());
                        this.mAdapter.updateBuddiesInBuddyGroup(fromZoomBuddyGroup, arrayList, true);
                    }
                }
            }
        }
    }

    public void reloadStarItems(boolean z) {
        if (!z || this.mAdapter.isStarEmpty()) {
            ZMBuddySyncInstance insatance = ZMBuddySyncInstance.getInsatance();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    String jid = myself.getJid();
                    List<String> starSessionGetAll = zoomMessenger.starSessionGetAll();
                    HashSet hashSet = new HashSet();
                    if (starSessionGetAll != null) {
                        for (String str : starSessionGetAll) {
                            if (!TextUtils.equals(jid, str)) {
                                IMAddrBookItem buddyByJid = insatance.getBuddyByJid(str);
                                if (buddyByJid != null && !buddyByJid.isIMBlockedByIB()) {
                                    hashSet.add(buddyByJid);
                                }
                            }
                        }
                    }
                    this.mAdapter.updateStarsBuddiesInBuddyGroup(hashSet, true);
                }
            }
        }
    }

    public void reloadRoomSystemsItem() {
        if (this.mAdapter.isRoomSystemsEmpty()) {
            ZMBuddySyncInstance insatance = ZMBuddySyncInstance.getInsatance();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.getMyself() != null) {
                List<String> roomDevices = zoomMessenger.getRoomDevices();
                HashSet hashSet = new HashSet();
                if (roomDevices != null) {
                    for (String buddyByJid : roomDevices) {
                        IMAddrBookItem buddyByJid2 = insatance.getBuddyByJid(buddyByJid);
                        if (buddyByJid2 != null) {
                            hashSet.add(buddyByJid2);
                        }
                    }
                }
                this.mAdapter.updateRoomSystemsInBuddyGroup(hashSet, false);
            }
        }
    }

    public boolean isDataEmpty() {
        return this.mAdapter.isDataEmpty();
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

    public void onStarSessionDataUpdate() {
        reloadStarItems(false);
        this.mAdapter.refresh(true);
    }

    public void onBuddyListUpdate() {
        reloadAllItems(false);
    }

    public void onPersonalGroupResponse(int i, String str, String str2, int i2, @Nullable List<String> list, String str3, String str4, List<BuddyUserInfo> list2) {
        if (i2 == 0) {
            syncPersonalGroups(i, str, list, str3, str4, list2);
        }
    }

    public void notifyPersonalGroupSync(int i, String str, @Nullable List<String> list, String str2, String str3) {
        syncPersonalGroups(i, str, list, str2, str3, null);
    }

    public void onBuddyInfoUpdate(@Nullable List<String> list, List<String> list2) {
        if (!CollectionsUtil.isCollectionEmpty(list2)) {
            reloadAllItems(false);
        } else if (!CollectionsUtil.isCollectionEmpty(list)) {
            this.mAdapter.updateBuddyInfo(list);
        }
    }

    private void syncPersonalGroups(int i, String str, @Nullable List<String> list, String str2, String str3, @Nullable List<BuddyUserInfo> list2) {
        switch (i) {
            case 1:
                this.mAdapter.updateData(str);
                this.mAdapter.expandGroup(str);
                this.mAdapter.refresh(true);
                int indexByGroupId = this.mAdapter.getIndexByGroupId(str);
                if (indexByGroupId != -1) {
                    scrollToPosition(indexByGroupId);
                    break;
                }
                break;
            case 2:
                this.mAdapter.updateData(str);
                break;
            case 3:
                this.mAdapter.removeGroup(str);
                break;
            case 4:
                this.mAdapter.addBuddiesInBuddyGroup(str, list, false);
                this.mAdapter.expandGroup(str);
                this.mAdapter.refresh(true);
                if (!CollectionsUtil.isListEmpty(list)) {
                    int indexByBuddyId = this.mAdapter.getIndexByBuddyId((String) list.get(0));
                    if (indexByBuddyId != -1) {
                        scrollToPosition(indexByBuddyId);
                        break;
                    }
                }
                break;
            case 5:
                this.mAdapter.removeBuddiesInBuddyGroup(str, list, true);
                break;
            case 6:
                if (!TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str3)) {
                    this.mAdapter.addBuddiesInBuddyGroup(str3, list, false);
                    this.mAdapter.expandGroup(str3);
                    if (!CollectionsUtil.isListEmpty(list)) {
                        int indexByBuddyId2 = this.mAdapter.getIndexByBuddyId((String) list.get(0));
                        if (indexByBuddyId2 != -1) {
                            scrollToPosition(indexByBuddyId2);
                        }
                    }
                    this.mAdapter.removeBuddiesInBuddyGroup(str2, list, true);
                    this.mAdapter.refresh(true);
                    break;
                } else {
                    return;
                }
        }
        if (!CollectionsUtil.isListEmpty(list2)) {
            ArrayList arrayList = new ArrayList();
            StringBuilder sb = new StringBuilder();
            for (BuddyUserInfo buddyUserInfo : list2) {
                if (buddyUserInfo.getNotAllowedReason() == 1) {
                    arrayList.add(buddyUserInfo.getJid());
                    if (!TextUtils.isEmpty(buddyUserInfo.getDisplayName())) {
                        sb.append(buddyUserInfo.getDisplayName());
                        sb.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                }
            }
            if (arrayList.size() > 0) {
                if (sb.length() > 0) {
                    String substring = sb.substring(0, sb.length() - 1);
                    String str4 = "";
                    if (i == 1 || i == 4) {
                        str4 = getContext().getString(C4558R.string.zm_mm_information_barries_personal_group_add_115072, new Object[]{substring});
                    } else if (i == 6) {
                        str4 = getContext().getString(C4558R.string.zm_mm_information_barries_personal_group_move_115072, new Object[]{substring});
                    }
                    if (!TextUtils.isEmpty(str4)) {
                        InformationBarriesDialog.show(getContext(), str4, false);
                    }
                }
                this.mAdapter.removeBuddiesInBuddyGroup(str, arrayList, true);
            }
        }
    }

    public void onPinnedSectionClick(int i) {
        this.mAdapter.onPinnedSectionClick(i);
    }

    public void onPinnedSectionLongClick(int i) {
        this.mAdapter.onPinnedSectionLongClick(i);
    }

    public void collapseGroup(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            int indexByBuddyId = this.mAdapter.getIndexByBuddyId(iMAddrBookItem.getJid());
            if (indexByBuddyId >= 0) {
                int findCurrentPinnedSection = findCurrentPinnedSection(indexByBuddyId);
                if (findCurrentPinnedSection >= 0) {
                    onPinnedSectionClick(findCurrentPinnedSection);
                }
            }
        }
    }
}
