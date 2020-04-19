package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomContact;
import com.zipow.videobox.ptapp.delegate.FavoriteMgrDelegation;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.delegate.PTBuddyHelperDelegation;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZMSortUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddySearchData;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddySearchData.SearchKey;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.IMBuddyItemComparator;
import com.zipow.videobox.util.MemCache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.videomeetings.C4558R;

public class InviteBuddyListView extends QuickSearchListView implements OnItemClickListener {
    private static final int MAX_ADDRBOOKITEM_COUNT_FOR_NO_FILTER = 250;
    private static final String TAG = "InviteBuddyListView";
    /* access modifiers changed from: private */
    public InviteBuddyListAdapter mAdapter;
    private Button mBtnSearchMore;
    @Nullable
    private String mFilter;
    private boolean mIsInviteAddrBook = false;
    private boolean mIsInviteZoomRooms = false;
    private int mLastTopPosition = 0;
    /* access modifiers changed from: private */
    public Listener mListener;
    @Nullable
    private RetainedFragment mRetainedFragment;
    @NonNull
    private List<InviteBuddyItem> mSelectedItems = new ArrayList();
    @NonNull
    private WebSearchResult mWebSearchResult = new WebSearchResult();

    public interface Listener {
        void onSelected(boolean z, InviteBuddyItem inviteBuddyItem);

        void onSelectionChanged();

        void onViewMoreClick();
    }

    public static class RetainedFragment extends ZMFragment implements IABContactsCacheListener {
        @Nullable
        private InviteBuddyListView mListView = null;
        @Nullable
        private List<InviteBuddyItem> mSelectedItems = null;
        @Nullable
        private WebSearchResult mWebSearchResult = null;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void setParentListView(InviteBuddyListView inviteBuddyListView) {
            this.mListView = inviteBuddyListView;
        }

        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);
            ABContactsCache.getInstance().addListener(this);
        }

        public void onDestroy() {
            super.onDestroy();
            ABContactsCache.getInstance().removeListener(this);
        }

        public void saveSelectedItems(List<InviteBuddyItem> list) {
            this.mSelectedItems = list;
        }

        @Nullable
        public List<InviteBuddyItem> restoreSelectedItems() {
            return this.mSelectedItems;
        }

        public void saveWebSearchResult(WebSearchResult webSearchResult) {
            this.mWebSearchResult = webSearchResult;
        }

        @Nullable
        public WebSearchResult restoreWebSearchResult() {
            return this.mWebSearchResult;
        }

        public void onContactsCacheUpdated() {
            if (isResumed()) {
                InviteBuddyListView inviteBuddyListView = this.mListView;
                if (inviteBuddyListView != null) {
                    inviteBuddyListView.reloadAllBuddyItems();
                }
            }
        }
    }

    static class WebSearchResult {
        @Nullable
        String key;
        @NonNull
        Map<String, InviteBuddyItem> results = new HashMap();

        WebSearchResult() {
        }

        @Nullable
        public InviteBuddyItem findByJid(String str) {
            return (InviteBuddyItem) this.results.get(str);
        }

        @NonNull
        public Set<String> getAllJids() {
            return this.results.keySet();
        }

        public void putInviteBuddyItem(@NonNull String str, @NonNull InviteBuddyItem inviteBuddyItem) {
            this.results.put(str, inviteBuddyItem);
        }

        public void clear() {
            this.key = null;
            this.results.clear();
        }
    }

    public InviteBuddyListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public InviteBuddyListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public InviteBuddyListView(Context context) {
        super(context);
        initView();
    }

    public void setIsInviteAddrBook(boolean z) {
        this.mIsInviteAddrBook = z;
        this.mIsInviteZoomRooms = false;
    }

    public void setIsInviteZoomRooms(boolean z) {
        this.mIsInviteAddrBook = true;
        this.mIsInviteZoomRooms = z;
    }

    private void initView() {
        this.mAdapter = new InviteBuddyListAdapter(getContext());
        setOnItemClickListener(this);
        initAddMoreFooter();
        setmOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 0) {
                    InviteBuddyListView.this.refreshVcards();
                    if (InviteBuddyListView.this.mAdapter != null) {
                        InviteBuddyListView.this.mAdapter.clearLoadedJids();
                    }
                }
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (i == 0 && i2 > 0) {
                    InviteBuddyListView.this.refreshVcards();
                }
            }
        });
        if (!isInEditMode()) {
            initRetainedFragment();
        }
    }

    /* access modifiers changed from: private */
    public void refreshVcards() {
        if (this.mAdapter != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.refreshBuddyVCards(this.mAdapter.getmLoadedJids());
            }
        }
    }

    private void _editmode_loadAllBuddyItems(@NonNull InviteBuddyListAdapter inviteBuddyListAdapter) {
        for (int i = 0; i < 20; i++) {
            InviteBuddyItem inviteBuddyItem = new InviteBuddyItem();
            StringBuilder sb = new StringBuilder();
            sb.append("Buddy ");
            sb.append(i);
            inviteBuddyItem.screenName = sb.toString();
            inviteBuddyItem.sortKey = inviteBuddyItem.screenName;
            inviteBuddyItem.userId = String.valueOf(i);
            inviteBuddyItem.isChecked = i % 2 == 0;
            inviteBuddyListAdapter.addItem(inviteBuddyItem);
        }
    }

    private void loadAllBuddyItems(@NonNull InviteBuddyListAdapter inviteBuddyListAdapter) {
        if (this.mIsInviteAddrBook) {
            setQuickSearchEnabled(true);
            loadAllAddrBookItems(inviteBuddyListAdapter);
            return;
        }
        int pTLoginType = PTAppDelegation.getInstance().getPTLoginType();
        if (pTLoginType == 0 || pTLoginType == 2) {
            setQuickSearchEnabled(false);
            loadAllIMBuddyItems(inviteBuddyListAdapter);
            return;
        }
        switch (pTLoginType) {
            case 100:
            case 101:
                setQuickSearchEnabled(true);
                loadAllZoomFavoriteItems(inviteBuddyListAdapter);
                return;
            default:
                return;
        }
    }

    private void initAddMoreFooter() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_search_view_more, null);
        this.mBtnSearchMore = (Button) inflate.findViewById(C4558R.C4560id.btnSearchMore);
        this.mBtnSearchMore.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                InviteBuddyListView.this.mListener.onViewMoreClick();
            }
        });
        this.mBtnSearchMore.setVisibility(8);
        getListView().addFooterView(inflate);
    }

    private void updateViewMore() {
        if (!this.mIsInviteAddrBook) {
            this.mBtnSearchMore.setVisibility(8);
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.isConnectionGood()) {
            if (StringUtil.isEmptyOrNull(this.mFilter) || this.mFilter.length() < 3) {
                this.mBtnSearchMore.setVisibility(8);
            } else {
                this.mBtnSearchMore.setVisibility(0);
            }
        }
    }

    private void loadAllIMBuddyItems(@NonNull InviteBuddyListAdapter inviteBuddyListAdapter) {
        System.currentTimeMillis();
        PTBuddyHelperDelegation buddyHelper = PTAppDelegation.getInstance().getBuddyHelper();
        int buddyItemCount = buddyHelper.getBuddyItemCount();
        String str = this.mFilter;
        int i = 0;
        if (str == null || str.length() <= 0) {
            while (i < buddyItemCount) {
                BuddyItem buddyItem = buddyHelper.getBuddyItem(i);
                if (buddyItem != null && buddyItem.getIsOnline() && !buddyItem.getIsPending() && !buddyItem.getIsNoneFriend()) {
                    InviteBuddyItem inviteBuddyItem = new InviteBuddyItem(buddyItem);
                    inviteBuddyItem.isChecked = isBuddySelected(inviteBuddyItem.userId);
                    inviteBuddyListAdapter.addItem(inviteBuddyItem);
                }
                i++;
            }
        } else {
            String lowerCase = this.mFilter.toLowerCase(CompatUtils.getLocalDefault());
            while (i < buddyItemCount) {
                BuddyItem buddyItem2 = buddyHelper.getBuddyItem(i);
                if (buddyItem2 != null && !buddyItem2.getIsPending() && !buddyItem2.getIsNoneFriend()) {
                    String screenName = buddyItem2.getScreenName();
                    if (screenName == null) {
                        screenName = "";
                    }
                    if (screenName.toLowerCase(CompatUtils.getLocalDefault()).indexOf(lowerCase) >= 0) {
                        InviteBuddyItem inviteBuddyItem2 = new InviteBuddyItem(buddyItem2);
                        inviteBuddyItem2.isChecked = isBuddySelected(inviteBuddyItem2.userId);
                        inviteBuddyListAdapter.addItem(inviteBuddyItem2);
                    }
                }
                i++;
            }
        }
        inviteBuddyListAdapter.sort();
    }

    private void loadAllZoomFavoriteItems(@NonNull InviteBuddyListAdapter inviteBuddyListAdapter) {
        FavoriteMgrDelegation favoriteMgr = PTAppDelegation.getInstance().getFavoriteMgr();
        ArrayList<ZoomContact> arrayList = new ArrayList<>();
        if (favoriteMgr.getFavoriteListWithFilter("", arrayList)) {
            String str = "";
            String str2 = this.mFilter;
            if (str2 != null && str2.length() > 0) {
                str = this.mFilter.toLowerCase(CompatUtils.getLocalDefault());
            }
            for (ZoomContact inviteBuddyItem : arrayList) {
                InviteBuddyItem inviteBuddyItem2 = new InviteBuddyItem(inviteBuddyItem);
                String str3 = inviteBuddyItem2.screenName != null ? inviteBuddyItem2.screenName : "";
                String str4 = inviteBuddyItem2.email != null ? inviteBuddyItem2.email : "";
                if (str.length() <= 0 || str3.toLowerCase(CompatUtils.getLocalDefault()).indexOf(str) >= 0 || str4.toLowerCase(CompatUtils.getLocalDefault()).indexOf(str) >= 0) {
                    inviteBuddyItem2.isChecked = isBuddySelected(inviteBuddyItem2.userId);
                    inviteBuddyListAdapter.addItem(inviteBuddyItem2);
                }
            }
        }
        inviteBuddyListAdapter.sort();
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x003d A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadAllAddrBookItems(@androidx.annotation.NonNull com.zipow.videobox.view.InviteBuddyListAdapter r7) {
        /*
            r6 = this;
            android.content.Context r0 = r6.getContext()
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r0 = r0.isPhoneNumberRegistered()
            r1 = 0
            if (r0 == 0) goto L_0x0032
            com.zipow.videobox.ptapp.ABContactsCache r0 = com.zipow.videobox.ptapp.ABContactsCache.getInstance()
            boolean r2 = r0.isCached()
            if (r2 != 0) goto L_0x0023
            boolean r0 = r0.reloadAllContacts()
            if (r0 != 0) goto L_0x0023
            return
        L_0x0023:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.ABContactsHelper r0 = r0.getABContactsHelper()
            if (r0 == 0) goto L_0x0032
            java.lang.String r0 = r0.getVerifiedPhoneNumber()
            goto L_0x0033
        L_0x0032:
            r0 = r1
        L_0x0033:
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r2 = r2.getZoomMessenger()
            if (r2 != 0) goto L_0x003e
            return
        L_0x003e:
            java.lang.String r3 = r6.mFilter
            boolean r3 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r3)
            r4 = 250(0xfa, float:3.5E-43)
            if (r3 == 0) goto L_0x0098
            r1 = 0
        L_0x0049:
            int r3 = r2.getBuddyCount()
            if (r1 >= r3) goto L_0x0171
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r2.getBuddyAt(r1)
            if (r3 == 0) goto L_0x0095
            boolean r5 = r3.isPending()
            if (r5 != 0) goto L_0x0095
            boolean r5 = r3.isRobot()
            if (r5 != 0) goto L_0x0095
            boolean r5 = r3.getIsRoomDevice()
            if (r5 == 0) goto L_0x0068
            goto L_0x0095
        L_0x0068:
            com.zipow.videobox.view.InviteBuddyItem r3 = r6.newInviteBuddyItemFromBuddy(r2, r3, r0)
            if (r3 == 0) goto L_0x008d
            java.lang.String r5 = r3.userId
            boolean r5 = r6.isBuddySelected(r5)
            r3.isChecked = r5
            boolean r5 = r6.mIsInviteZoomRooms
            if (r5 == 0) goto L_0x008a
            com.zipow.videobox.view.IMAddrBookItem r5 = r3.getAddrBookItem()
            if (r5 == 0) goto L_0x008d
            boolean r5 = r5.isZoomRoomContact()
            if (r5 == 0) goto L_0x008d
            r7.addItem(r3)
            goto L_0x008d
        L_0x008a:
            r7.addItem(r3)
        L_0x008d:
            int r3 = r7.getCount()
            if (r3 < r4) goto L_0x0095
            goto L_0x0171
        L_0x0095:
            int r1 = r1 + 1
            goto L_0x0049
        L_0x0098:
            java.lang.String r0 = r6.mFilter
            java.util.List r0 = r2.localStrictSearchBuddies(r0, r1)
            if (r0 == 0) goto L_0x0112
            java.util.Iterator r0 = r0.iterator()
        L_0x00a4:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0112
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            com.zipow.videobox.ptapp.mm.ZoomBuddy r1 = r2.getBuddyWithJID(r1)
            if (r1 == 0) goto L_0x00a4
            boolean r3 = r1.isRobot()
            if (r3 != 0) goto L_0x00a4
            boolean r3 = r1.getIsRoomDevice()
            if (r3 == 0) goto L_0x00c3
            goto L_0x00a4
        L_0x00c3:
            com.zipow.videobox.view.IMAddrBookItem r1 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r1)
            if (r1 != 0) goto L_0x00ca
            goto L_0x00a4
        L_0x00ca:
            com.zipow.videobox.view.InviteBuddyItem r3 = new com.zipow.videobox.view.InviteBuddyItem
            r3.<init>(r1)
            java.lang.String r5 = r3.userId
            boolean r5 = r6.isBuddySelected(r5)
            r3.isChecked = r5
            boolean r5 = r6.mIsInviteAddrBook
            if (r5 == 0) goto L_0x00e7
            boolean r1 = r1.isMeetingBlockedByIB()
            if (r1 == 0) goto L_0x00f3
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_mm_information_barries_search_contact_115072
            r6.setEmptyViewText(r1)
            goto L_0x00a4
        L_0x00e7:
            boolean r1 = r1.isIMBlockedByIB()
            if (r1 == 0) goto L_0x00f3
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_mm_information_barries_search_contact_115072
            r6.setEmptyViewText(r1)
            goto L_0x00a4
        L_0x00f3:
            boolean r1 = r6.mIsInviteZoomRooms
            if (r1 == 0) goto L_0x0107
            com.zipow.videobox.view.IMAddrBookItem r1 = r3.getAddrBookItem()
            if (r1 == 0) goto L_0x010c
            boolean r1 = r1.isZoomRoomContact()
            if (r1 == 0) goto L_0x010c
            r7.addItem(r3)
            goto L_0x010c
        L_0x0107:
            com.zipow.videobox.view.InviteBuddyListAdapter r1 = r6.mAdapter
            r1.addItem(r3)
        L_0x010c:
            int r1 = r7.getCount()
            if (r1 < r4) goto L_0x00a4
        L_0x0112:
            com.zipow.videobox.view.InviteBuddyListView$WebSearchResult r0 = r6.mWebSearchResult
            java.lang.String r0 = r0.key
            java.lang.String r1 = r6.mFilter
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isSameString(r0, r1)
            if (r0 == 0) goto L_0x0171
            com.zipow.videobox.view.InviteBuddyListView$WebSearchResult r0 = r6.mWebSearchResult
            java.util.Set r0 = r0.getAllJids()
            java.util.Iterator r0 = r0.iterator()
        L_0x0128:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0171
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r2.getBuddyWithJID(r1)
            if (r3 == 0) goto L_0x0169
            boolean r5 = r3.isRobot()
            if (r5 != 0) goto L_0x0169
            boolean r5 = r3.getIsRoomDevice()
            if (r5 != 0) goto L_0x0169
            com.zipow.videobox.view.InviteBuddyListView$WebSearchResult r5 = r6.mWebSearchResult
            com.zipow.videobox.view.InviteBuddyItem r1 = r5.findByJid(r1)
            if (r1 != 0) goto L_0x015b
            com.zipow.videobox.view.IMAddrBookItem r1 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r3)
            if (r1 != 0) goto L_0x0155
            goto L_0x0128
        L_0x0155:
            com.zipow.videobox.view.InviteBuddyItem r3 = new com.zipow.videobox.view.InviteBuddyItem
            r3.<init>(r1)
            r1 = r3
        L_0x015b:
            com.zipow.videobox.view.InviteBuddyListAdapter r3 = r6.mAdapter
            r3.updateItem(r1)
            com.zipow.videobox.view.InviteBuddyListAdapter r1 = r6.mAdapter
            int r1 = r1.getCount()
            if (r1 < r4) goto L_0x0169
            goto L_0x0171
        L_0x0169:
            android.widget.Button r1 = r6.mBtnSearchMore
            r3 = 8
            r1.setVisibility(r3)
            goto L_0x0128
        L_0x0171:
            r7.sort()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.InviteBuddyListView.loadAllAddrBookItems(com.zipow.videobox.view.InviteBuddyListAdapter):void");
    }

    @Nullable
    private IMAddrBookItem newAddrBookItem(ZoomBuddy zoomBuddy) {
        return IMAddrBookItem.fromZoomBuddy(zoomBuddy);
    }

    private InviteBuddyItem newInviteBuddyItemFromBuddy(@NonNull ZoomMessenger zoomMessenger, @NonNull ZoomBuddy zoomBuddy, @Nullable String str) {
        String str2;
        IMAddrBookItem newAddrBookItem = newAddrBookItem(zoomBuddy);
        if (newAddrBookItem == null) {
            return null;
        }
        String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(zoomBuddy, null);
        String phoneNumber = zoomBuddy.getPhoneNumber();
        String email = zoomBuddy.getEmail();
        if (!StringUtil.isSameString(this.mWebSearchResult.key, this.mFilter) || this.mWebSearchResult.findByJid(zoomBuddy.getJid()) == null) {
            if (!StringUtil.isEmptyOrNull(str) && str.equals(phoneNumber)) {
                return null;
            }
            String str3 = this.mFilter;
            if (str3 != null && str3.length() > 0) {
                String lowerCase = this.mFilter.toLowerCase(CompatUtils.getLocalDefault());
                String lowerCase2 = buddyDisplayName.toLowerCase(CompatUtils.getLocalDefault());
                if (email == null) {
                    str2 = "";
                } else {
                    str2 = email.toLowerCase(CompatUtils.getLocalDefault());
                }
                if (!lowerCase2.contains(lowerCase) && !str2.contains(lowerCase)) {
                    return null;
                }
            }
            if (!(zoomMessenger.isMyContact(zoomBuddy.getJid()) && !zoomBuddy.isNoneFriend()) && newAddrBookItem.getContactId() < 0) {
                return null;
            }
        }
        InviteBuddyItem inviteBuddyItem = new InviteBuddyItem(newAddrBookItem);
        inviteBuddyItem.isChecked = isAddrBookItemSelected(newAddrBookItem);
        inviteBuddyItem.avatar = newAddrBookItem.getAvatarPath();
        return inviteBuddyItem;
    }

    public void updateBuddyItem(@Nullable BuddyItem buddyItem) {
        if (buddyItem != null && !this.mIsInviteAddrBook) {
            String str = this.mFilter;
            if (str == null || str.length() <= 0) {
                if (buddyItem.getIsOnline()) {
                    InviteBuddyItem inviteBuddyItem = new InviteBuddyItem(buddyItem);
                    InviteBuddyItem itemByJid = this.mAdapter.getItemByJid(inviteBuddyItem.userId);
                    this.mAdapter.updateItem(inviteBuddyItem);
                    if (itemByJid != null && itemByJid.isChecked) {
                        addSelectedItem(inviteBuddyItem);
                        Listener listener = this.mListener;
                        if (listener != null) {
                            listener.onSelectionChanged();
                        }
                    }
                    this.mAdapter.sort();
                } else {
                    this.mAdapter.removeItem(buddyItem.getJid());
                }
                this.mAdapter.notifyDataSetChanged();
            } else {
                String screenName = buddyItem.getScreenName();
                if (!StringUtil.isEmptyOrNull(screenName)) {
                    String lowerCase = this.mFilter.toLowerCase(CompatUtils.getLocalDefault());
                    if (!buddyItem.getIsOnline() || screenName.toLowerCase(CompatUtils.getLocalDefault()).indexOf(lowerCase) < 0) {
                        this.mAdapter.removeItem(buddyItem.getJid());
                    } else {
                        InviteBuddyItem inviteBuddyItem2 = new InviteBuddyItem(buddyItem);
                        InviteBuddyItem itemByJid2 = this.mAdapter.getItemByJid(inviteBuddyItem2.userId);
                        this.mAdapter.updateItem(inviteBuddyItem2);
                        if (itemByJid2 != null && itemByJid2.isChecked) {
                            addSelectedItem(inviteBuddyItem2);
                            Listener listener2 = this.mListener;
                            if (listener2 != null) {
                                listener2.onSelectionChanged();
                            }
                        }
                        this.mAdapter.sort();
                    }
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void sort() {
        this.mAdapter.sort();
        this.mAdapter.notifyDataSetChanged();
    }

    public void setAvatarMemCache(MemCache<String, Bitmap> memCache) {
        this.mAdapter.setAvatarMemCache(memCache);
    }

    private boolean isBuddySelected(@Nullable String str) {
        if (str == null) {
            return false;
        }
        for (InviteBuddyItem inviteBuddyItem : this.mSelectedItems) {
            if (str.equals(inviteBuddyItem.userId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAddrBookItemSelected(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem == null) {
            return false;
        }
        for (InviteBuddyItem addrBookItem : this.mSelectedItems) {
            IMAddrBookItem addrBookItem2 = addrBookItem.getAddrBookItem();
            if (addrBookItem2 != null && StringUtil.isSameString(addrBookItem2.getJid(), iMAddrBookItem.getJid())) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            _editmode_loadAllBuddyItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
        int i = this.mLastTopPosition;
        if (i >= 0) {
            setSelectionFromTop(i, 0);
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            Parcelable parcelable2 = bundle.getParcelable("InviteBuddyListView.superState");
            this.mFilter = bundle.getString("InviteBuddyListView.mFilter");
            this.mLastTopPosition = bundle.getInt("InviteBuddyListView.topPosition", -1);
            updateViewMore();
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("InviteBuddyListView.superState", onSaveInstanceState);
        bundle.putString("InviteBuddyListView.mFilter", this.mFilter);
        bundle.putInt("InviteBuddyListView.topPosition", pointToPosition(10, 10));
        return bundle;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void reloadAllBuddyItems() {
        System.currentTimeMillis();
        this.mAdapter.clear();
        loadAllBuddyItems(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void clearSelection() {
        this.mSelectedItems.clear();
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            InviteBuddyItem inviteBuddyItem = (InviteBuddyItem) this.mAdapter.getItem(i);
            if (inviteBuddyItem != null) {
                inviteBuddyItem.isChecked = false;
            }
            this.mAdapter.notifyDataSetChanged();
        }
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSelectionChanged();
        }
    }

    @NonNull
    public List<InviteBuddyItem> getSelectedBuddies() {
        return this.mSelectedItems;
    }

    public void unselectBuddy(@Nullable InviteBuddyItem inviteBuddyItem) {
        if (inviteBuddyItem != null) {
            InviteBuddyItem itemByJid = this.mAdapter.getItemByJid(inviteBuddyItem.userId);
            if (itemByJid != null) {
                itemByJid.isChecked = false;
                this.mAdapter.notifyDataSetChanged();
            }
            removeSelectedItem(inviteBuddyItem);
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onSelectionChanged();
            }
        }
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
            if (StringUtil.isEmptyOrNull(lowerCase) || this.mIsInviteAddrBook) {
                this.mWebSearchResult.clear();
                reloadAllBuddyItems();
            } else if (StringUtil.isEmptyOrNull(str2) || !lowerCase.contains(str2)) {
                reloadAllBuddyItems();
            } else {
                this.mAdapter.filter(lowerCase);
                this.mAdapter.notifyDataSetChanged();
            }
            setEmptyViewText("");
            updateViewMore();
        }
    }

    public void setFilter(String str) {
        this.mFilter = str;
        updateViewMore();
    }

    @Nullable
    public String getFilter() {
        return this.mFilter;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (itemAtPosition instanceof InviteBuddyItem) {
            InviteBuddyItem inviteBuddyItem = (InviteBuddyItem) itemAtPosition;
            IMAddrBookItem addrBookItem = inviteBuddyItem.getAddrBookItem();
            if (addrBookItem == null || addrBookItem.getAccountStatus() == 0) {
                inviteBuddyItem.isChecked = !inviteBuddyItem.isChecked;
                this.mAdapter.notifyDataSetChanged();
                if (inviteBuddyItem.isChecked) {
                    addSelectedItem(inviteBuddyItem);
                } else {
                    removeSelectedItem(inviteBuddyItem);
                }
                Listener listener = this.mListener;
                if (listener != null) {
                    listener.onSelectionChanged();
                }
            }
        }
    }

    private void removeSelectedItem(@NonNull InviteBuddyItem inviteBuddyItem) {
        int size = this.mSelectedItems.size() - 1;
        while (size >= 0) {
            InviteBuddyItem inviteBuddyItem2 = (InviteBuddyItem) this.mSelectedItems.get(size);
            if (inviteBuddyItem.userId == null || !inviteBuddyItem.userId.equals(inviteBuddyItem2.userId)) {
                size--;
            } else {
                this.mSelectedItems.remove(size);
                Listener listener = this.mListener;
                if (listener != null) {
                    listener.onSelected(false, inviteBuddyItem2);
                    return;
                }
                return;
            }
        }
    }

    private void addSelectedItem(InviteBuddyItem inviteBuddyItem) {
        inviteBuddyItem.isChecked = true;
        int size = this.mSelectedItems.size() - 1;
        while (size >= 0) {
            InviteBuddyItem inviteBuddyItem2 = (InviteBuddyItem) this.mSelectedItems.get(size);
            if (inviteBuddyItem.userId == null || !inviteBuddyItem.userId.equals(inviteBuddyItem2.userId)) {
                size--;
            } else {
                this.mSelectedItems.set(size, inviteBuddyItem);
                return;
            }
        }
        this.mSelectedItems.add(inviteBuddyItem);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSelected(true, inviteBuddyItem);
        }
        Collections.sort(this.mSelectedItems, new IMBuddyItemComparator(CompatUtils.getLocalDefault(), false, true));
    }

    public void notifyDataSetChanged(boolean z) {
        InviteBuddyListAdapter inviteBuddyListAdapter = this.mAdapter;
        if (inviteBuddyListAdapter != null) {
            if (z) {
                inviteBuddyListAdapter.setLazyLoadAvatarDisabled(true);
                postDelayed(new Runnable() {
                    public void run() {
                        InviteBuddyListView.this.mAdapter.setLazyLoadAvatarDisabled(false);
                    }
                }, 1000);
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void onIndicationZoomMessengerSearchBuddyByKey(String str, int i) {
        if (StringUtil.isSameString(str, this.mFilter)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                HashSet hashSet = new HashSet();
                List localStrictSearchBuddies = zoomMessenger.localStrictSearchBuddies(this.mFilter, null);
                if (localStrictSearchBuddies != null && localStrictSearchBuddies.size() > 0 && zoomMessenger.isAnyBuddyGroupLarge()) {
                    zoomMessenger.getBuddiesPresence(localStrictSearchBuddies, false);
                }
                if (localStrictSearchBuddies != null) {
                    hashSet.addAll(localStrictSearchBuddies);
                }
                this.mWebSearchResult.key = this.mFilter;
                ZoomBuddySearchData buddySearchData = zoomMessenger.getBuddySearchData();
                if (buddySearchData != null) {
                    SearchKey searchKey = buddySearchData.getSearchKey();
                    ArrayList arrayList = new ArrayList();
                    SearchKey searchKey2 = buddySearchData.getSearchKey();
                    if (!(searchKey == null || searchKey2 == null || !TextUtils.equals(searchKey2.getKey(), this.mFilter))) {
                        for (int i2 = 0; i2 < buddySearchData.getBuddyCount(); i2++) {
                            ZoomBuddy buddyAt = buddySearchData.getBuddyAt(i2);
                            if (buddyAt != null && !hashSet.contains(buddyAt.getJid())) {
                                String jid = buddyAt.getJid();
                                arrayList.add(jid);
                                if (jid != null) {
                                    this.mWebSearchResult.putInviteBuddyItem(jid, new InviteBuddyItem(IMAddrBookItem.fromZoomBuddy(buddyAt)));
                                }
                            }
                        }
                        zoomMessenger.getBuddiesPresence(arrayList, false);
                        hashSet.addAll(arrayList);
                    }
                }
                ArrayList arrayList2 = new ArrayList();
                arrayList2.addAll(hashSet);
                List<String> sortBuddies = ZMSortUtil.sortBuddies(arrayList2, 0, this.mFilter);
                if (sortBuddies != null && sortBuddies.size() > 0 && zoomMessenger.getMyself() != null) {
                    for (String str2 : sortBuddies) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str2);
                        if (buddyWithJID != null) {
                            if (this.mIsInviteAddrBook) {
                                if (buddyWithJID.isMeetingBlockedByIB()) {
                                    setEmptyViewText(C4558R.string.zm_mm_information_barries_search_contact_115072);
                                }
                            } else if (buddyWithJID.isIMBlockedByIB()) {
                                setEmptyViewText(C4558R.string.zm_mm_information_barries_search_contact_115072);
                            }
                            InviteBuddyItem findByJid = this.mWebSearchResult.findByJid(str2);
                            if (findByJid == null) {
                                findByJid = new InviteBuddyItem(IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                            }
                            findByJid.isChecked = isBuddySelected(findByJid.userId);
                            if (this.mIsInviteZoomRooms) {
                                IMAddrBookItem addrBookItem = findByJid.getAddrBookItem();
                                if (addrBookItem != null && addrBookItem.isZoomRoomContact()) {
                                    this.mAdapter.updateItem(findByJid);
                                }
                            } else {
                                this.mAdapter.updateItem(findByJid);
                            }
                            if (this.mAdapter.getCount() >= 250) {
                                break;
                            }
                        }
                    }
                }
                this.mAdapter.notifyDataSetChanged();
                this.mBtnSearchMore.setVisibility(8);
            }
        }
    }

    public void onIndicateZoomMessengerOnlineBuddies(@Nullable List<String> list) {
        if (list != null && list.size() != 0) {
            for (String str : list) {
                if (this.mAdapter.getItemByJid(str) != null) {
                    updateBuddyInfoWithJid(str);
                }
            }
        }
    }

    public void onIndicateZoomMessengerGetContactsPresence(@Nullable List<String> list, @Nullable List<String> list2) {
        if (list != null && list.size() > 0) {
            for (String str : list) {
                if (this.mAdapter.getItemByJid(str) != null) {
                    updateBuddyInfoWithJid(str);
                }
            }
        }
        if (list2 != null && list2.size() > 0) {
            for (String str2 : list2) {
                if (this.mAdapter.getItemByJid(str2) != null) {
                    updateBuddyInfoWithJid(str2);
                }
            }
        }
    }

    public void updateBuddyInfoWithJid(@Nullable String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                String str2 = null;
                if (PTApp.getInstance().isPhoneNumberRegistered()) {
                    ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
                    if (aBContactsHelper != null) {
                        str2 = aBContactsHelper.getVerifiedPhoneNumber();
                    }
                }
                this.mAdapter.removeItem(buddyWithJID.getJid());
                InviteBuddyItem newInviteBuddyItemFromBuddy = newInviteBuddyItemFromBuddy(zoomMessenger, buddyWithJID, str2);
                if (newInviteBuddyItemFromBuddy != null) {
                    if (this.mIsInviteZoomRooms) {
                        IMAddrBookItem addrBookItem = newInviteBuddyItemFromBuddy.getAddrBookItem();
                        if (addrBookItem != null && addrBookItem.isZoomRoomContact()) {
                            this.mAdapter.addItem(newInviteBuddyItemFromBuddy);
                        }
                    } else {
                        this.mAdapter.addItem(newInviteBuddyItemFromBuddy);
                    }
                    if (!(str == null || this.mWebSearchResult.findByJid(str) == null)) {
                        this.mWebSearchResult.putInviteBuddyItem(str, newInviteBuddyItemFromBuddy);
                    }
                }
                notifyDataSetChanged(true);
            }
        }
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            this.mRetainedFragment.saveSelectedItems(this.mSelectedItems);
            this.mRetainedFragment.saveWebSearchResult(this.mWebSearchResult);
            ((ZMActivity) getContext()).getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
        } else {
            List<InviteBuddyItem> restoreSelectedItems = retainedFragment.restoreSelectedItems();
            if (restoreSelectedItems != null) {
                this.mSelectedItems = restoreSelectedItems;
            }
            WebSearchResult restoreWebSearchResult = this.mRetainedFragment.restoreWebSearchResult();
            if (restoreWebSearchResult != null) {
                this.mWebSearchResult = restoreWebSearchResult;
            }
        }
        this.mRetainedFragment.setParentListView(this);
    }

    @Nullable
    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        return (RetainedFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(RetainedFragment.class.getName());
    }
}
