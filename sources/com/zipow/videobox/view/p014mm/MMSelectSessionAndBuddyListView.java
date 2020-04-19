package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.MMSelectSessionAndBuddyFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZMSortUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddySearchData;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddySearchData.SearchKey;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.WebSearchResult;
import com.zipow.videobox.view.p014mm.MMSelectSessionAndBuddyListAdapter.ItemViewMore;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListView */
public class MMSelectSessionAndBuddyListView extends ListView implements OnItemClickListener {
    private static final int MAX_LOCAL_CONTACTS_NUM = 5;
    private static final String TAG = "MMSelectSessionAndBuddyListView";
    /* access modifiers changed from: private */
    public MMSelectSessionAndBuddyListAdapter mAdapter;
    private List<MMZoomGroup> mAllGroupsCache;
    @Nullable
    private List<IMAddrBookItem> mContactsCache;
    private boolean mContainMyNotes;
    private boolean mContainsBlock;
    private boolean mContainsE2E;
    @Nullable
    private String mFilter;
    private Runnable mFilterRunnabel;
    @Nullable
    private List<MMZoomGroup> mGroupsCache;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsWebSearchMode;
    /* access modifiers changed from: private */
    public MMSelectSessionAndBuddyFragment mParentFragment;
    @Nullable
    private List<Object> mRecentSessionCache;
    @Nullable
    private Runnable mTaskLazyNotifyDataSetChanged = null;
    @Nullable
    private WebSearchResult mWebSearchResult;
    OnInformationBarriesListener onInformationBarriesListener;

    /* renamed from: com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListView$MMZoomGroupComparator */
    static class MMZoomGroupComparator implements Comparator<MMZoomGroup> {
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

    /* renamed from: com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListView$OnInformationBarriesListener */
    public interface OnInformationBarriesListener {
        void onInfoBarries(boolean z);
    }

    public OnInformationBarriesListener getOnInformationBarriesListener() {
        return this.onInformationBarriesListener;
    }

    public void setOnInformationBarriesListener(OnInformationBarriesListener onInformationBarriesListener2) {
        this.onInformationBarriesListener = onInformationBarriesListener2;
    }

    public MMSelectSessionAndBuddyListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MMSelectSessionAndBuddyListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMSelectSessionAndBuddyListView(Context context) {
        super(context);
        initView();
    }

    public void setParentFragment(MMSelectSessionAndBuddyFragment mMSelectSessionAndBuddyFragment) {
        this.mParentFragment = mMSelectSessionAndBuddyFragment;
    }

    public boolean isParentFragmentResumed() {
        MMSelectSessionAndBuddyFragment mMSelectSessionAndBuddyFragment = this.mParentFragment;
        if (mMSelectSessionAndBuddyFragment == null) {
            return false;
        }
        return mMSelectSessionAndBuddyFragment.isResumed();
    }

    public void onParentFragmentResume() {
        loadData();
        this.mAdapter.notifyDataSetChanged();
    }

    public void onParentFragmentStart() {
        this.mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        this.mAdapter = new MMSelectSessionAndBuddyListAdapter(getContext());
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 0) {
                    MMSelectSessionAndBuddyListView.this.refreshVCards();
                    if (MMSelectSessionAndBuddyListView.this.mAdapter != null) {
                        MMSelectSessionAndBuddyListView.this.mAdapter.clearmLoadedContactJids();
                    }
                }
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (i == 0 && i2 > 0) {
                    MMSelectSessionAndBuddyListView.this.refreshVCards();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshVCards() {
        MMSelectSessionAndBuddyListAdapter mMSelectSessionAndBuddyListAdapter = this.mAdapter;
        if (mMSelectSessionAndBuddyListAdapter != null) {
            refreshVcards(mMSelectSessionAndBuddyListAdapter.getmLoadedContactJids());
        }
    }

    private void refreshVcards(List<String> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.refreshBuddyVCards(list);
            }
        }
    }

    public void loadData() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            List blockUserGetAll = zoomMessenger.blockUserGetAll();
            if (blockUserGetAll == null) {
                blockUserGetAll = new ArrayList();
            }
            if (StringUtil.isEmptyOrNull(this.mFilter)) {
                int chatSessionCount = zoomMessenger.getChatSessionCount();
                int e2eGetMyOption = zoomMessenger.e2eGetMyOption();
                ArrayList arrayList = new ArrayList();
                this.mRecentSessionCache = new ArrayList();
                ZoomBuddy myself = zoomMessenger.getMyself();
                String str = null;
                if (myself != null) {
                    str = myself.getJid();
                }
                for (int i = 0; i < chatSessionCount; i++) {
                    ZoomChatSession sessionAt = zoomMessenger.getSessionAt(i);
                    if (sessionAt != null && !TextUtils.equals(str, sessionAt.getSessionId())) {
                        MMChatsListItem fromZoomChatSession = MMChatsListItem.fromZoomChatSession(sessionAt, zoomMessenger, getContext());
                        if (fromZoomChatSession != null) {
                            arrayList.add(fromZoomChatSession);
                        }
                    }
                }
                for (MMChatsListItem mMChatsListItem : ZMSortUtil.sortSessions(arrayList)) {
                    if (!mMChatsListItem.isGroup()) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(mMChatsListItem.getSessionId());
                        if (buddyWithJID != null && ((this.mContainsBlock || !blockUserGetAll.contains(buddyWithJID.getJid())) && !buddyWithJID.isRobot())) {
                            if ((this.mContainsE2E || buddyWithJID.getE2EAbility(e2eGetMyOption) != 2) && !buddyWithJID.getIsRoomDevice() && !buddyWithJID.isZoomRoom()) {
                                this.mRecentSessionCache.add(IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                            }
                        }
                    } else {
                        ZoomGroup groupById = zoomMessenger.getGroupById(mMChatsListItem.getSessionId());
                        if (groupById != null) {
                            MMZoomGroup initWithZoomGroup = MMZoomGroup.initWithZoomGroup(groupById);
                            if ((this.mContainsE2E || !initWithZoomGroup.isE2E()) && !ZMIMUtils.isAnnouncement(initWithZoomGroup.getGroupId())) {
                                this.mRecentSessionCache.add(initWithZoomGroup);
                            }
                        }
                    }
                }
            } else {
                this.mContactsCache = getSearchedBuddies(zoomMessenger);
                this.mGroupsCache = getSearchedGroups(zoomMessenger);
            }
            updateListData();
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putBoolean("mIsWebSearchMode", this.mIsWebSearchMode);
        bundle.putSerializable("mWebSearchResult", this.mWebSearchResult);
        bundle.putString("mFilter", this.mFilter);
        return bundle;
    }

    public void onRestoreInstanceState(@Nullable Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mIsWebSearchMode = bundle.getBoolean("mIsWebSearchMode");
            Parcelable parcelable2 = bundle.getParcelable("superState");
            this.mFilter = bundle.getString("mFilter");
            this.mWebSearchResult = (WebSearchResult) bundle.getSerializable("mWebSearchResult");
            loadData();
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001e, code lost:
        if (r1.size() > 0) goto L_0x0020;
     */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0031  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateListData() {
        /*
            r8 = this;
            com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListAdapter r0 = r8.mAdapter
            r0.clear()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.lang.String r1 = r8.mFilter
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r1 == 0) goto L_0x005a
            boolean r1 = r8.mContainMyNotes
            if (r1 != 0) goto L_0x0020
            java.util.List<java.lang.Object> r1 = r8.mRecentSessionCache
            if (r1 == 0) goto L_0x002d
            int r1 = r1.size()
            if (r1 <= 0) goto L_0x002d
        L_0x0020:
            android.content.Context r1 = r8.getContext()
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_share_category_recent_99868
            java.lang.String r1 = r1.getString(r2)
            r0.add(r1)
        L_0x002d:
            boolean r1 = r8.mContainMyNotes
            if (r1 == 0) goto L_0x004a
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r1 = r1.getZoomMessenger()
            if (r1 == 0) goto L_0x004a
            com.zipow.videobox.ptapp.mm.ZoomBuddy r1 = r1.getMyself()
            if (r1 == 0) goto L_0x004a
            com.zipow.videobox.view.IMAddrBookItem r1 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r1)
            if (r1 == 0) goto L_0x004a
            r0.add(r1)
        L_0x004a:
            java.util.List<java.lang.Object> r1 = r8.mRecentSessionCache
            if (r1 == 0) goto L_0x00b9
            int r1 = r1.size()
            if (r1 <= 0) goto L_0x00b9
            java.util.List<java.lang.Object> r1 = r8.mRecentSessionCache
            r0.addAll(r1)
            goto L_0x00b9
        L_0x005a:
            java.util.List<com.zipow.videobox.view.IMAddrBookItem> r1 = r8.mContactsCache
            if (r1 == 0) goto L_0x0076
            int r1 = r1.size()
            if (r1 <= 0) goto L_0x0076
            android.content.Context r1 = r8.getContext()
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_share_category_contact
            java.lang.String r1 = r1.getString(r2)
            r0.add(r1)
            java.util.List<com.zipow.videobox.view.IMAddrBookItem> r1 = r8.mContactsCache
            r0.addAll(r1)
        L_0x0076:
            boolean r1 = r8.mIsWebSearchMode
            if (r1 != 0) goto L_0x009d
            java.lang.String r1 = r8.mFilter
            if (r1 == 0) goto L_0x009d
            int r1 = r1.length()
            r2 = 3
            if (r1 < r2) goto L_0x009d
            java.util.List<com.zipow.videobox.view.IMAddrBookItem> r1 = r8.mContactsCache
            if (r1 == 0) goto L_0x0098
            int r1 = r1.size()
            if (r1 <= 0) goto L_0x0098
            com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListAdapter$ItemViewMore r1 = new com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListAdapter$ItemViewMore
            r1.<init>()
            r0.add(r1)
            goto L_0x009d
        L_0x0098:
            com.zipow.videobox.fragment.MMSelectSessionAndBuddyFragment r1 = r8.mParentFragment
            r1.onClickViewMore()
        L_0x009d:
            java.util.List<com.zipow.videobox.view.mm.MMZoomGroup> r1 = r8.mGroupsCache
            if (r1 == 0) goto L_0x00b9
            int r1 = r1.size()
            if (r1 <= 0) goto L_0x00b9
            android.content.Context r1 = r8.getContext()
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_share_category_groups_chats_59554
            java.lang.String r1 = r1.getString(r2)
            r0.add(r1)
            java.util.List<com.zipow.videobox.view.mm.MMZoomGroup> r1 = r8.mGroupsCache
            r0.addAll(r1)
        L_0x00b9:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.Iterator r0 = r0.iterator()
            r2 = 0
            r3 = 0
            r4 = 0
        L_0x00c5:
            boolean r5 = r0.hasNext()
            r6 = 1
            if (r5 == 0) goto L_0x00f1
            java.lang.Object r5 = r0.next()
            boolean r7 = r5 instanceof com.zipow.videobox.view.IMAddrBookItem
            if (r7 == 0) goto L_0x00e4
            r7 = r5
            com.zipow.videobox.view.IMAddrBookItem r7 = (com.zipow.videobox.view.IMAddrBookItem) r7
            boolean r7 = r7.isIMBlockedByIB()
            if (r7 == 0) goto L_0x00df
            r3 = 1
            goto L_0x00c5
        L_0x00df:
            r1.add(r5)
            r4 = 1
            goto L_0x00c5
        L_0x00e4:
            boolean r7 = r5 instanceof com.zipow.videobox.view.p014mm.MMZoomGroup
            if (r7 == 0) goto L_0x00ed
            r1.add(r5)
            r4 = 1
            goto L_0x00c5
        L_0x00ed:
            r1.add(r5)
            goto L_0x00c5
        L_0x00f1:
            com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListView$OnInformationBarriesListener r0 = r8.onInformationBarriesListener
            if (r0 == 0) goto L_0x00fd
            if (r3 == 0) goto L_0x00fa
            if (r4 != 0) goto L_0x00fa
            r2 = 1
        L_0x00fa:
            r0.onInfoBarries(r2)
        L_0x00fd:
            com.zipow.videobox.view.mm.MMSelectSessionAndBuddyListAdapter r0 = r8.mAdapter
            if (r4 == 0) goto L_0x0102
            goto L_0x0107
        L_0x0102:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
        L_0x0107:
            r0.addItems(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMSelectSessionAndBuddyListView.updateListData():void");
    }

    @Nullable
    private List<IMAddrBookItem> getSearchedBuddies(@Nullable ZoomMessenger zoomMessenger) {
        if (zoomMessenger == null || TextUtils.isEmpty(this.mFilter)) {
            return null;
        }
        HashSet hashSet = new HashSet();
        List localStrictSearchBuddies = zoomMessenger.localStrictSearchBuddies(this.mFilter, null);
        if (localStrictSearchBuddies != null) {
            hashSet.addAll(localStrictSearchBuddies);
        }
        int i = 0;
        if (this.mIsWebSearchMode) {
            this.mWebSearchResult = new WebSearchResult();
            ZoomBuddySearchData buddySearchData = zoomMessenger.getBuddySearchData();
            if (buddySearchData != null) {
                SearchKey searchKey = buddySearchData.getSearchKey();
                ArrayList arrayList = new ArrayList();
                SearchKey searchKey2 = buddySearchData.getSearchKey();
                if (searchKey == null || searchKey2 == null || !TextUtils.equals(buddySearchData.getSearchKey().getKey(), this.mFilter)) {
                    WebSearchResult webSearchResult = this.mWebSearchResult;
                    if (webSearchResult != null && StringUtil.isSameString(this.mFilter, webSearchResult.getKey())) {
                        for (String add : this.mWebSearchResult.getJids()) {
                            arrayList.add(add);
                        }
                    }
                } else {
                    this.mWebSearchResult.setKey(this.mFilter);
                    for (int i2 = 0; i2 < buddySearchData.getBuddyCount(); i2++) {
                        ZoomBuddy buddyAt = buddySearchData.getBuddyAt(i2);
                        if (buddyAt != null) {
                            String jid = buddyAt.getJid();
                            arrayList.add(jid);
                            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyAt);
                            if (fromZoomBuddy != null) {
                                this.mWebSearchResult.putItem(jid, fromZoomBuddy);
                            }
                        }
                    }
                    zoomMessenger.getBuddiesPresence(arrayList, false);
                }
                hashSet.addAll(arrayList);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.addAll(hashSet);
        List sortBuddies = ZMSortUtil.sortBuddies(arrayList2, 0, this.mFilter);
        ArrayList arrayList3 = new ArrayList();
        if (sortBuddies != null && sortBuddies.size() > 0) {
            int size = sortBuddies.size();
            if (!this.mIsWebSearchMode) {
                size = sortBuddies.size() > 5 ? 5 : sortBuddies.size();
            }
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                List blockUserGetAll = zoomMessenger.blockUserGetAll();
                if (blockUserGetAll == null) {
                    blockUserGetAll = new ArrayList();
                }
                int e2eGetMyOption = zoomMessenger.e2eGetMyOption();
                while (arrayList3.size() < size && i < sortBuddies.size()) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID((String) sortBuddies.get(i));
                    if (buddyWithJID != null && ((this.mContainsBlock || !blockUserGetAll.contains(buddyWithJID.getJid())) && !buddyWithJID.isRobot() && ((this.mContainsE2E || buddyWithJID.getE2EAbility(e2eGetMyOption) != 2) && !buddyWithJID.getIsRoomDevice() && !buddyWithJID.isZoomRoom()))) {
                        IMAddrBookItem fromZoomBuddy2 = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                        if (fromZoomBuddy2 != null && (this.mContainMyNotes || !TextUtils.equals(fromZoomBuddy2.getJid(), myself.getJid()))) {
                            arrayList3.add(fromZoomBuddy2);
                        }
                    }
                    i++;
                }
            }
        }
        return arrayList3;
    }

    @Nullable
    private List<MMZoomGroup> getSearchedGroups(@Nullable ZoomMessenger zoomMessenger) {
        if (zoomMessenger == null) {
            return null;
        }
        Locale localDefault = CompatUtils.getLocalDefault();
        if (this.mAllGroupsCache == null) {
            this.mAllGroupsCache = new ArrayList();
            int groupCount = zoomMessenger.getGroupCount();
            for (int i = 0; i < groupCount; i++) {
                ZoomGroup groupAt = zoomMessenger.getGroupAt(i);
                if (groupAt != null) {
                    MMZoomGroup initWithZoomGroup = MMZoomGroup.initWithZoomGroup(groupAt);
                    if (!ZMIMUtils.isAnnouncement(initWithZoomGroup.getGroupId()) && (this.mContainsE2E || !initWithZoomGroup.isE2E())) {
                        this.mAllGroupsCache.add(initWithZoomGroup);
                    }
                }
            }
            this.mAllGroupsCache = ZMSortUtil.sortGroups(this.mAllGroupsCache);
        }
        ArrayList arrayList = new ArrayList();
        for (MMZoomGroup mMZoomGroup : this.mAllGroupsCache) {
            if (!StringUtil.isEmptyOrNull(this.mFilter)) {
                String groupName = mMZoomGroup.getGroupName();
                if (!StringUtil.isEmptyOrNull(groupName)) {
                    if (!groupName.toLowerCase(localDefault).contains(this.mFilter)) {
                    }
                }
            }
            arrayList.add(mMZoomGroup);
        }
        return ZMSortUtil.sortGroups(arrayList);
    }

    public void notifyDataSetChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void onIndicateBuddyInfoUpdatedWithJID(String str) {
        if (this.mParentFragment.isResumed()) {
            List<IMAddrBookItem> list = this.mContactsCache;
            if (list != null && list.size() != 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    boolean z = false;
                    int i = 0;
                    while (true) {
                        if (i >= this.mContactsCache.size()) {
                            break;
                        }
                        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mContactsCache.get(i);
                        if (iMAddrBookItem != null && StringUtil.isSameString(iMAddrBookItem.getJid(), str)) {
                            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                            if (buddyWithJID != null) {
                                IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                                if (fromZoomBuddy != null) {
                                    this.mContactsCache.set(i, fromZoomBuddy);
                                }
                                z = true;
                            }
                        }
                        i++;
                    }
                    if (z && this.mParentFragment.isResumed()) {
                        lazyNotifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void lazyNotifyDataSetChanged() {
        if (this.mTaskLazyNotifyDataSetChanged == null) {
            this.mTaskLazyNotifyDataSetChanged = new Runnable() {
                public void run() {
                    if (MMSelectSessionAndBuddyListView.this.mParentFragment.isResumed()) {
                        MMSelectSessionAndBuddyListView.this.updateListData();
                        MMSelectSessionAndBuddyListView.this.notifyDataSetChanged();
                    }
                }
            };
        }
        this.mHandler.removeCallbacks(this.mTaskLazyNotifyDataSetChanged);
        this.mHandler.postDelayed(this.mTaskLazyNotifyDataSetChanged, 1000);
    }

    public void setContainsE2E(boolean z) {
        this.mContainsE2E = z;
    }

    public void setmContainMyNotes(boolean z) {
        this.mContainMyNotes = z;
    }

    public void setContainsBlock(boolean z) {
        this.mContainsBlock = z;
    }

    public void setIsWebSearchMode(boolean z) {
        this.mIsWebSearchMode = z;
    }

    public void filter(@NonNull String str) {
        if (!TextUtils.equals(this.mFilter, str)) {
            if (!TextUtils.isEmpty(str)) {
                this.mFilter = str.toLowerCase(CompatUtils.getLocalDefault());
            } else {
                this.mFilter = str;
            }
            this.mIsWebSearchMode = false;
            this.mWebSearchResult = null;
            loadData();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mAdapter.getItem(i);
        if (item instanceof MMZoomGroup) {
            MMZoomGroup mMZoomGroup = (MMZoomGroup) item;
            MMSelectSessionAndBuddyFragment mMSelectSessionAndBuddyFragment = this.mParentFragment;
            if (mMSelectSessionAndBuddyFragment != null) {
                mMSelectSessionAndBuddyFragment.onSelect(mMZoomGroup.getGroupId(), true);
            }
        } else if (item instanceof IMAddrBookItem) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) item;
            if (iMAddrBookItem.getAccountStatus() == 0) {
                MMSelectSessionAndBuddyFragment mMSelectSessionAndBuddyFragment2 = this.mParentFragment;
                if (mMSelectSessionAndBuddyFragment2 != null) {
                    mMSelectSessionAndBuddyFragment2.onSelect(iMAddrBookItem.getJid(), false);
                }
            }
        } else if (item instanceof ItemViewMore) {
            MMSelectSessionAndBuddyFragment mMSelectSessionAndBuddyFragment3 = this.mParentFragment;
            if (mMSelectSessionAndBuddyFragment3 != null) {
                mMSelectSessionAndBuddyFragment3.onClickViewMore();
            }
        }
    }

    public void onGroupAction(int i, @Nullable GroupAction groupAction, String str) {
        if (groupAction != null) {
            updateZoomGroupByJid(groupAction.getGroupId());
        }
    }

    private void updateZoomGroupByJid(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(str)) {
            int i = 0;
            boolean z = zoomMessenger.getGroupById(str) == null;
            if (this.mAllGroupsCache != null) {
                while (true) {
                    if (i >= this.mAllGroupsCache.size()) {
                        break;
                    }
                    MMZoomGroup mMZoomGroup = (MMZoomGroup) this.mAllGroupsCache.get(i);
                    if (!StringUtil.isSameString(mMZoomGroup.getGroupId(), str)) {
                        i++;
                    } else if (z) {
                        this.mAllGroupsCache.remove(i);
                    } else {
                        mMZoomGroup.syncGroupWithSDK(zoomMessenger);
                    }
                }
            }
            if (isParentFragmentResumed()) {
                updateListData();
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        updateZoomGroupByJid(str);
    }

    public void onSearchBuddyByKey(String str, int i) {
        if (StringUtil.isSameString(str, this.mFilter)) {
            loadData();
        }
    }
}
