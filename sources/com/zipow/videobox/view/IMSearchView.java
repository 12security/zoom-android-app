package com.zipow.videobox.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchContactFilter;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchContactFilter.Builder;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.SearchMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMSearchAdapter.ItemViewMore;
import com.zipow.videobox.view.IMSearchAdapter.ItemWebSearching;
import com.zipow.videobox.view.p014mm.MMChatListFooter;
import com.zipow.videobox.view.p014mm.MMChatsListItem;
import com.zipow.videobox.view.p014mm.MMChatsListView.ChatsListContextMenuItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.videomeetings.C4558R;

public class IMSearchView extends ListView implements OnItemClickListener, OnItemLongClickListener {
    public static final int FOOTER_TYPE_HIDE_CONTENT = 3;
    public static final int FOOTER_TYPE_NONE = 0;
    public static final int FOOTER_TYPE_NORMAL = 1;
    public static final int FOOTER_TYPE_ONLY_CONTACT = 2;
    private static final int MAX_LOCAL_CONTACTS_NUM = 8;
    private static final String TAG = "IMSearchView";
    @Nullable
    private List<ISearchableItem> mContactsCache;
    private View mEmptyView;
    /* access modifiers changed from: private */
    @Nullable
    public String mFilter;
    private Runnable mFilterRunnabel;
    private int mFooterType = 0;
    @NonNull
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public IMSearchAdapter mIMSearchAdapter;
    private SimpleISIPLineMgrEventSinkListener mISIPLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            IMSearchView.this.refreshSearchResult(true, true);
            IMSearchView.this.refreshVcards();
        }
    };
    private boolean mIsWebSearchMode = false;
    private boolean mJumpChats = false;
    /* access modifiers changed from: private */
    @NonNull
    public List<String> mLocalBuddies = new ArrayList();
    @Nullable
    private String mLocalSearchReqID;
    private WebSearchResult mWebSearchResult;

    public IMSearchView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public IMSearchView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public IMSearchView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mIMSearchAdapter = new IMSearchAdapter(getContext());
        setAdapter(this.mIMSearchAdapter);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
        setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 0) {
                    IMSearchView.this.refreshVcards();
                    if (IMSearchView.this.mIMSearchAdapter != null) {
                        IMSearchView.this.mIMSearchAdapter.clearmLoadedContactJids();
                    }
                }
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (i == 0 && i2 > 0) {
                    IMSearchView.this.refreshVcards();
                }
            }
        });
    }

    public void indicate_BuddyBlockedByIB(final List<String> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            postDelayed(new Runnable() {
                public void run() {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        for (String str : list) {
                            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                            if (buddyWithJID != null) {
                                IMSearchView.this.mLocalBuddies.remove(str);
                                IMSearchView.this.mIMSearchAdapter.removeItem(IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                            }
                        }
                    }
                }
            }, 100);
        }
    }

    /* access modifiers changed from: private */
    public void refreshVcards() {
        IMSearchAdapter iMSearchAdapter = this.mIMSearchAdapter;
        if (iMSearchAdapter != null) {
            List list = iMSearchAdapter.getmLoadedContactJids();
            if (!CollectionsUtil.isListEmpty(list)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    zoomMessenger.refreshBuddyVCards(list);
                }
            }
        }
    }

    public void setFooterType(int i) {
        this.mFooterType = i;
    }

    public void onResume() {
        refreshSearchResult(true);
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putBoolean("mIsWebSearchMode", this.mIsWebSearchMode);
        bundle.putSerializable("mWebSearchResult", this.mWebSearchResult);
        bundle.putString("mFilter", this.mFilter);
        bundle.putInt("hasFooter", this.mFooterType);
        return bundle;
    }

    public void onRestoreInstanceState(@Nullable Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mIsWebSearchMode = bundle.getBoolean("mIsWebSearchMode");
            Parcelable parcelable2 = bundle.getParcelable("superState");
            this.mFilter = bundle.getString("mFilter");
            this.mWebSearchResult = (WebSearchResult) bundle.getSerializable("mWebSearchResult");
            this.mFooterType = bundle.getInt("hasFooter", 0);
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public void setJumpChats(boolean z) {
        this.mJumpChats = z;
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
    }

    public void setFilter(@Nullable final String str) {
        this.mIsWebSearchMode = false;
        this.mWebSearchResult = null;
        Runnable runnable = this.mFilterRunnabel;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
        this.mFilterRunnabel = new Runnable() {
            public void run() {
                if (!TextUtils.equals(IMSearchView.this.mFilter, str)) {
                    if (!TextUtils.isEmpty(str)) {
                        IMSearchView.this.mFilter = str.toLowerCase(CompatUtils.getLocalDefault());
                    } else {
                        IMSearchView.this.mFilter = str;
                    }
                    IMSearchView.this.localSearchContact();
                    ZoomLogEventTracking.eventTrackSearch();
                }
            }
        };
        this.mHandler.postDelayed(this.mFilterRunnabel, 300);
    }

    public boolean isResultEmpty() {
        IMSearchAdapter iMSearchAdapter = this.mIMSearchAdapter;
        boolean z = true;
        if (iMSearchAdapter == null) {
            return true;
        }
        if (iMSearchAdapter.getCount() > 0) {
            z = false;
        }
        return z;
    }

    public void Indicate_LocalSearchContactResponse(@Nullable String str, @Nullable List<String> list) {
        if (!StringUtil.isEmptyOrNull(str) && StringUtil.isSameString(str, this.mLocalSearchReqID) && list != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (list.size() > 0 && zoomMessenger.isAnyBuddyGroupLarge()) {
                    zoomMessenger.getBuddiesPresence(list, true);
                }
                ArrayList arrayList = new ArrayList();
                for (String str2 : list) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str2);
                    if (buddyWithJID == null || !buddyWithJID.isIMBlockedByIB()) {
                        arrayList.add(str2);
                    }
                }
                this.mLocalSearchReqID = null;
                if (arrayList.size() > 0) {
                    this.mLocalBuddies.addAll(arrayList);
                }
                refreshSearchResult(true, true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void localSearchContact() {
        if (!TextUtils.isEmpty(this.mFilter)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
                if (searchMgr != null) {
                    this.mLocalBuddies.clear();
                    String str = "";
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(myself);
                        if (!(fromZoomBuddy == null || getContext() == null)) {
                            str = getContext().getString(C4558R.string.zm_mm_msg_my_notes_65147, new Object[]{fromZoomBuddy.getScreenName()});
                        }
                    }
                    Builder newBuilder = LocalSearchContactFilter.newBuilder();
                    newBuilder.setKeyWord(this.mFilter);
                    newBuilder.setMaxCount(500);
                    newBuilder.setNeedSearchBuddy(true);
                    newBuilder.setNeedSearchChannel(true);
                    newBuilder.setMyNoteL10N(str);
                    this.mLocalSearchReqID = searchMgr.localSearchContact(newBuilder.build());
                    if (StringUtil.isEmptyOrNull(this.mLocalSearchReqID)) {
                        refreshSearchResult(true, true);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:82:0x016f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0170  */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.zipow.videobox.view.ISearchableItem> getSearchResult(@androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.ZoomMessenger r14) {
        /*
            r13 = this;
            r0 = 0
            if (r14 == 0) goto L_0x01cc
            java.lang.String r1 = r13.mFilter
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x000d
            goto L_0x01cc
        L_0x000d:
            java.util.HashSet r1 = new java.util.HashSet
            r1.<init>()
            java.util.List<java.lang.String> r2 = r13.mLocalBuddies
            r3 = 1
            if (r2 == 0) goto L_0x0028
            int r2 = r2.size()
            if (r2 <= 0) goto L_0x0028
            boolean r2 = r14.isAnyBuddyGroupLarge()
            if (r2 == 0) goto L_0x0028
            java.util.List<java.lang.String> r2 = r13.mLocalBuddies
            r14.getBuddiesPresence(r2, r3)
        L_0x0028:
            java.util.List<java.lang.String> r2 = r13.mLocalBuddies
            if (r2 == 0) goto L_0x002f
            r1.addAll(r2)
        L_0x002f:
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData r2 = r14.getBuddySearchData()
            r4 = 0
            if (r2 == 0) goto L_0x008d
            java.lang.String r5 = r13.mFilter
            if (r5 == 0) goto L_0x008d
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData$SearchKey r5 = r2.getSearchKey()
            java.lang.String r5 = r5.getKey()
            java.lang.String r6 = r13.mFilter
            boolean r5 = p021us.zoom.androidlib.util.StringUtil.isSameString(r5, r6)
            if (r5 == 0) goto L_0x008d
            com.zipow.videobox.view.WebSearchResult r5 = new com.zipow.videobox.view.WebSearchResult
            r5.<init>()
            r13.mWebSearchResult = r5
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            com.zipow.videobox.view.WebSearchResult r6 = r13.mWebSearchResult
            java.lang.String r7 = r13.mFilter
            r6.setKey(r7)
            r6 = 0
        L_0x005e:
            int r7 = r2.getBuddyCount()
            if (r6 >= r7) goto L_0x0086
            com.zipow.videobox.ptapp.mm.ZoomBuddy r7 = r2.getBuddyAt(r6)
            if (r7 == 0) goto L_0x0083
            java.lang.String r8 = r7.getJid()
            r5.add(r8)
            com.zipow.videobox.view.IMAddrBookItem r7 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r7)
            if (r7 == 0) goto L_0x0083
            boolean r9 = r7.isIMBlockedByIB()
            if (r9 == 0) goto L_0x007e
            goto L_0x0083
        L_0x007e:
            com.zipow.videobox.view.WebSearchResult r9 = r13.mWebSearchResult
            r9.putItem(r8, r7)
        L_0x0083:
            int r6 = r6 + 1
            goto L_0x005e
        L_0x0086:
            r1.addAll(r5)
            r14.getBuddiesPresence(r5, r4)
            goto L_0x00a6
        L_0x008d:
            com.zipow.videobox.view.WebSearchResult r2 = r13.mWebSearchResult
            if (r2 == 0) goto L_0x00a6
            java.lang.String r5 = r13.mFilter
            java.lang.String r2 = r2.getKey()
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isSameString(r5, r2)
            if (r2 == 0) goto L_0x00a6
            com.zipow.videobox.view.WebSearchResult r2 = r13.mWebSearchResult
            java.util.Set r2 = r2.getJids()
            r1.addAll(r2)
        L_0x00a6:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r2 = r14.getMyself()
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>(r1)
            java.util.List r1 = com.zipow.videobox.ptapp.p013mm.ZMSortUtil.sortContactSearchResult(r5)
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            java.util.HashSet r6 = new java.util.HashSet
            r6.<init>()
            if (r1 == 0) goto L_0x014d
            int r7 = r1.size()
            if (r7 <= 0) goto L_0x014d
            int r7 = r1.size()
            boolean r8 = r13.mIsWebSearchMode
            if (r8 != 0) goto L_0x00dc
            int r7 = r1.size()
            r8 = 8
            if (r7 <= r8) goto L_0x00d8
            r7 = 8
            goto L_0x00dc
        L_0x00d8:
            int r7 = r1.size()
        L_0x00dc:
            if (r2 == 0) goto L_0x014d
            java.lang.String r2 = r2.getJid()
            r8 = 0
            r9 = 0
            r10 = 0
        L_0x00e5:
            int r11 = r5.size()
            if (r11 >= r7) goto L_0x014f
            int r11 = r1.size()
            if (r8 >= r11) goto L_0x014f
            java.lang.Object r11 = r1.get(r8)
            java.lang.String r11 = (java.lang.String) r11
            com.zipow.videobox.ptapp.mm.ZoomBuddy r12 = r14.getBuddyWithJID(r11)
            if (r12 == 0) goto L_0x0102
            com.zipow.videobox.view.IMAddrBookItem r12 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r12)
            goto L_0x0103
        L_0x0102:
            r12 = r0
        L_0x0103:
            if (r12 == 0) goto L_0x0137
            boolean r11 = r12.isIMBlockedByIB()
            if (r11 != 0) goto L_0x0135
            java.lang.String r10 = r12.getJid()
            boolean r10 = android.text.TextUtils.equals(r10, r2)
            if (r10 == 0) goto L_0x0119
            r5.add(r12)
            goto L_0x0133
        L_0x0119:
            com.zipow.videobox.ptapp.ABContactsCache r10 = com.zipow.videobox.ptapp.ABContactsCache.getInstance()
            java.lang.String r11 = r12.getBuddyPhoneNumber()
            com.zipow.videobox.ptapp.ABContactsCache$Contact r10 = r10.getFirstContactByPhoneNumber(r11)
            if (r10 == 0) goto L_0x0130
            int r10 = r10.contactId
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
            r6.add(r10)
        L_0x0130:
            r5.add(r12)
        L_0x0133:
            r10 = 1
            goto L_0x014a
        L_0x0135:
            r9 = 1
            goto L_0x014a
        L_0x0137:
            com.zipow.videobox.ptapp.mm.ZoomChatSession r11 = r14.getSessionById(r11)
            if (r11 == 0) goto L_0x014a
            android.content.Context r12 = r13.getContext()
            com.zipow.videobox.view.mm.MMChatsListItem r11 = com.zipow.videobox.view.p014mm.MMChatsListItem.fromZoomChatSession(r11, r14, r12)
            if (r11 == 0) goto L_0x014a
            r5.add(r11)
        L_0x014a:
            int r8 = r8 + 1
            goto L_0x00e5
        L_0x014d:
            r9 = 0
            r10 = 0
        L_0x014f:
            org.greenrobot.eventbus.EventBus r14 = org.greenrobot.eventbus.EventBus.getDefault()
            com.zipow.videobox.eventbus.ZMTipsIBEvent r0 = new com.zipow.videobox.eventbus.ZMTipsIBEvent
            if (r9 == 0) goto L_0x015a
            if (r10 != 0) goto L_0x015a
            goto L_0x015b
        L_0x015a:
            r3 = 0
        L_0x015b:
            r0.<init>(r3)
            r14.post(r0)
            com.zipow.videobox.ptapp.ABContactsCache r14 = com.zipow.videobox.ptapp.ABContactsCache.getInstance()
            java.util.List r14 = r14.getAllCacheContacts()
            boolean r0 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r14)
            if (r0 == 0) goto L_0x0170
            return r5
        L_0x0170:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.Iterator r14 = r14.iterator()
        L_0x0179:
            boolean r1 = r14.hasNext()
            if (r1 == 0) goto L_0x01be
            java.lang.Object r1 = r14.next()
            com.zipow.videobox.ptapp.ABContactsCache$Contact r1 = (com.zipow.videobox.ptapp.ABContactsCache.Contact) r1
            int r2 = r1.contactId
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            boolean r2 = r6.contains(r2)
            if (r2 == 0) goto L_0x0192
            goto L_0x0179
        L_0x0192:
            com.zipow.videobox.view.IMAddrBookItem r1 = com.zipow.videobox.view.IMAddrBookItem.fromContact(r1)
            if (r1 == 0) goto L_0x0179
            boolean r2 = r1.isIMBlockedByIB()
            if (r2 == 0) goto L_0x019f
            goto L_0x0179
        L_0x019f:
            java.lang.String r2 = r1.getScreenName()
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x0179
            java.lang.String r2 = r2.toLowerCase()
            java.lang.String r3 = r13.mFilter
            java.lang.String r3 = r3.toLowerCase()
            boolean r2 = r2.contains(r3)
            if (r2 != 0) goto L_0x01ba
            goto L_0x0179
        L_0x01ba:
            r0.add(r1)
            goto L_0x0179
        L_0x01be:
            boolean r14 = r0.isEmpty()
            if (r14 != 0) goto L_0x01cb
            java.util.List r14 = com.zipow.videobox.ptapp.p013mm.ZMSortUtil.sortSessionsAndBuddies(r0)
            r5.addAll(r14)
        L_0x01cb:
            return r5
        L_0x01cc:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.IMSearchView.getSearchResult(com.zipow.videobox.ptapp.mm.ZoomMessenger):java.util.List");
    }

    /* access modifiers changed from: private */
    public void refreshSearchResult(boolean z, boolean z2) {
        this.mIMSearchAdapter.clear();
        if (TextUtils.isEmpty(this.mFilter)) {
            this.mIMSearchAdapter.notifyDataSetChanged();
            View view = this.mEmptyView;
            if (view != null) {
                view.setVisibility(8);
            }
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ArrayList arrayList = new ArrayList();
            if (z || this.mContactsCache == null) {
                this.mContactsCache = getSearchResult(zoomMessenger);
            }
            if (CollectionsUtil.isListEmpty(this.mContactsCache) && z2 && zoomMessenger.searchBuddyByKey(this.mFilter)) {
                this.mIsWebSearchMode = true;
            }
            List<ISearchableItem> list = this.mContactsCache;
            if (list != null) {
                arrayList.addAll(list);
            }
            ArrayList arrayList2 = new ArrayList();
            boolean z3 = false;
            if (this.mIsWebSearchMode || arrayList.size() <= 8) {
                arrayList2.addAll(arrayList);
            } else {
                arrayList2.addAll(arrayList.subList(0, 8));
            }
            if (this.mIsWebSearchMode) {
                if (!isWebSearchOver()) {
                    arrayList2.add(new ItemWebSearching());
                }
            } else if (this.mFilter.trim().length() >= 3 && arrayList.size() > 0) {
                arrayList2.add(new ItemViewMore());
            }
            int i = this.mFooterType;
            if (i != 0) {
                if (i == 3) {
                    arrayList2.add(new MMChatListFooter(this.mFilter, false, true));
                } else {
                    String str = this.mFilter;
                    if (i == 2) {
                        z3 = true;
                    }
                    arrayList2.add(new MMChatListFooter(str, z3));
                }
            }
            this.mIMSearchAdapter.clear();
            this.mIMSearchAdapter.addAllItems(arrayList2);
            this.mIMSearchAdapter.notifyDataSetChanged();
        }
    }

    private boolean isWebSearchOver() {
        WebSearchResult webSearchResult = this.mWebSearchResult;
        return webSearchResult != null && StringUtil.isSameString(this.mFilter, webSearchResult.getKey());
    }

    public void refreshSearchResult(boolean z) {
        refreshSearchResult(z, false);
    }

    public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            String groupId = groupAction.getGroupId();
            if (!StringUtil.isEmptyOrNull(groupId)) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(groupId);
                if (sessionById == null) {
                    this.mIMSearchAdapter.removeItem(groupId);
                } else {
                    this.mIMSearchAdapter.updateItem(MMChatsListItem.fromZoomChatSession(sessionById, zoomMessenger, getContext()));
                }
                this.mIMSearchAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onReceiveMessage(String str, String str2, String str3) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(str3) && !StringUtil.isEmptyOrNull(str)) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById == null) {
                this.mIMSearchAdapter.removeItem(str);
            } else {
                this.mIMSearchAdapter.updateItem(MMChatsListItem.fromZoomChatSession(sessionById, zoomMessenger, getContext()));
            }
        }
    }

    public void onNotify_ChatSessionListUpdate() {
        refreshSearchResult(true);
    }

    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(str)) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById == null) {
                this.mIMSearchAdapter.removeItem(str);
            } else {
                this.mIMSearchAdapter.updateItem(MMChatsListItem.fromZoomChatSession(sessionById, zoomMessenger, getContext()));
            }
        }
    }

    public void onConfirm_MessageSent(String str, String str2, int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str)) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById == null) {
                this.mIMSearchAdapter.removeItem(str);
            } else {
                this.mIMSearchAdapter.updateItem(MMChatsListItem.fromZoomChatSession(sessionById, zoomMessenger, getContext()));
            }
        }
    }

    public void onIndicateZoomMessengerBuddyListUpdated() {
        refreshSearchResult(true);
    }

    public void updateBuddyInfoWithJid(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                if (fromZoomBuddy != null) {
                    if (this.mContactsCache != null) {
                        for (int i = 0; i < this.mContactsCache.size(); i++) {
                            ISearchableItem iSearchableItem = (ISearchableItem) this.mContactsCache.get(i);
                            if ((iSearchableItem instanceof IMAddrBookItem) && TextUtils.equals(((IMAddrBookItem) iSearchableItem).getJid(), fromZoomBuddy.getJid())) {
                                this.mContactsCache.set(i, fromZoomBuddy);
                            }
                        }
                    }
                    this.mIMSearchAdapter.updateItem(fromZoomBuddy);
                }
            }
        }
    }

    public void onZoomMessengerRemoveBuddy(String str, int i) {
        refreshSearchResult(true);
    }

    private void onClickChatItem(@Nullable MMChatsListItem mMChatsListItem) {
        if (mMChatsListItem != null) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(mMChatsListItem.getSessionId());
                    if (sessionById != null) {
                        if (sessionById.isGroup()) {
                            ZoomGroup sessionGroup = sessionById.getSessionGroup();
                            if (sessionGroup != null) {
                                String groupID = sessionGroup.getGroupID();
                                if (!StringUtil.isEmptyOrNull(groupID)) {
                                    sessionById.storeLastSearchAndOpenSessionTime(CmmTime.getMMNow() / 1000);
                                    startGroupChat(zMActivity, groupID);
                                    ZoomLogEventTracking.eventTrackJumpToChat(true);
                                }
                            }
                        } else {
                            ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                            if (sessionBuddy == null) {
                                if (UIMgr.isMyNotes(mMChatsListItem.getSessionId())) {
                                    sessionBuddy = zoomMessenger.getMyself();
                                }
                                if (sessionBuddy == null) {
                                    return;
                                }
                            }
                            sessionById.storeLastSearchAndOpenSessionTime(CmmTime.getMMNow() / 1000);
                            startOneToOneChat(zMActivity, sessionBuddy);
                            ZoomLogEventTracking.eventTrackJumpToChat(false);
                        }
                    }
                }
            }
        }
    }

    private static void startOneToOneChat(ZMActivity zMActivity, ZoomBuddy zoomBuddy) {
        MMChatActivity.showAsOneToOneChat(zMActivity, zoomBuddy);
    }

    private static void startGroupChat(@NonNull ZMActivity zMActivity, String str) {
        MMChatActivity.showAsGroupChat(zMActivity, str);
    }

    private void onClickContactItem(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null && !iMAddrBookItem.isPending()) {
            showUserActions(iMAddrBookItem);
        }
    }

    private void showUserActions(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (!this.mJumpChats || zoomMessenger.imChatGetOption() == 2) {
                        AddrBookItemDetailsActivity.show(zMActivity, iMAddrBookItem, 106, true);
                    } else {
                        MMChatActivity.showAsOneToOneChat(zMActivity, iMAddrBookItem, iMAddrBookItem.getJid(), true);
                    }
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mIMSearchAdapter.getItem(i);
        if (item != null) {
            if (item instanceof IMAddrBookItem) {
                onClickContactItem((IMAddrBookItem) item);
            } else if (item instanceof MMChatsListItem) {
                onClickChatItem((MMChatsListItem) item);
            } else if (item instanceof ItemViewMore) {
                onClickSearchMore();
            }
        }
    }

    private void onClickSearchMore() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.searchBuddyByKey(this.mFilter)) {
            this.mIsWebSearchMode = true;
            refreshSearchResult(false);
        }
    }

    public void onSearchBuddyByKey(String str, int i) {
        if (TextUtils.equals(str, this.mFilter)) {
            refreshSearchResult(true);
        }
    }

    private boolean onLongClickChatItem(@NonNull final MMChatsListItem mMChatsListItem) {
        String str;
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity == null) {
            return false;
        }
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
        ArrayList arrayList = new ArrayList();
        if (!mMChatsListItem.isGroup()) {
            str = mMChatsListItem.getTitle();
            arrayList.add(new ChatsListContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_lbl_delete_chat_20762), 0));
        } else if (mMChatsListItem.isRoom()) {
            str = zMActivity.getString(C4558R.string.zm_mm_title_chatslist_context_menu_channel_chat_59554);
            arrayList.add(new ChatsListContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_lbl_delete_channel_chat_59554), 0));
        } else {
            str = zMActivity.getString(C4558R.string.zm_mm_title_chatslist_context_menu_muc_chat_59554);
            arrayList.add(new ChatsListContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_lbl_delete_muc_chat_59554), 0));
        }
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        ZMAlertDialog create = new ZMAlertDialog.Builder(zMActivity).setTitle((CharSequence) str).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                IMSearchView.this.onSelectContextMenuItem(mMChatsListItem, (ChatsListContextMenuItem) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
        return true;
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(@NonNull MMChatsListItem mMChatsListItem, ChatsListContextMenuItem chatsListContextMenuItem) {
        if (chatsListContextMenuItem.getAction() == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.deleteSession(mMChatsListItem.getSessionId())) {
                refreshSearchResult(true);
            }
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mIMSearchAdapter.getItem(i);
        if (item != null && (item instanceof MMChatsListItem)) {
            return onLongClickChatItem((MMChatsListItem) item);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }
}
