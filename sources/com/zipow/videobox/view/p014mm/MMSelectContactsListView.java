package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.zipow.videobox.confapp.meeting.SelectAlterHostItem;
import com.zipow.videobox.fragment.MMSelectContactsFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.MMLocalHelper;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSelectContactsListView */
public class MMSelectContactsListView extends QuickSearchListView implements OnItemClickListener {
    public static final int CHOICE_MODE_MULTIPLE_CHOICE = 0;
    public static final int CHOICE_MODE_SINGLE_CHOICE = 1;
    private static final int DELAY_LOAD_EMAILS = 300;
    private static final int MAX_ADDRBOOKITEM_COUNT_FOR_NO_FILTER = 250;
    private static final String TAG = "MMSelectContactsListView";
    /* access modifiers changed from: private */
    public MMSelectContactsListAdapter mAdapter;
    private final WebSearchResult mAutoWebSearchResult = new WebSearchResult();
    private Button mBtnSearchMore;
    private final HashMap<String, String> mCacheJidEmail = new HashMap<>();
    private int mChoiceMode = 0;
    @Nullable
    private String mFilter;
    private boolean mFilterZoomRooms = false;
    private String mGroupId;
    @NonNull
    private Handler mHandler = new Handler();
    @Nullable
    private Set<String> mHistoryEmails;
    private boolean mIncludeMe = false;
    private boolean mIncludeRobot = false;
    private boolean mIsAlterHost = false;
    private boolean mIsAutoWebSearch = false;
    private boolean mIsDisabledForPreSelected = true;
    private boolean mIsNeedHaveEmail = false;
    private boolean mIsNeedSortSelectedItems = true;
    private boolean mIsShowEmail = false;
    private int mLastTopPosition = 0;
    /* access modifiers changed from: private */
    public Listener mListener;
    @NonNull
    private Runnable mLoadEmailsRunnable = new Runnable() {
        public void run() {
            MMSelectContactsListView.this.loadEmailForVisibleItems();
        }
    };
    private int mMaxSelectCount = -1;
    private boolean mOnlyRobot = false;
    private boolean mOnlySameOrganization = false;
    private MMSelectContactsFragment mParentFragment;
    @Nullable
    private List<String> mPreSelectedItems;
    @Nullable
    private RetainedFragment mRetainedFragment;
    @NonNull
    private List<MMSelectContactsListItem> mSelectedItems = new ArrayList();
    @NonNull
    private WebSearchResult mWebSearchResult = new WebSearchResult();
    private OnBlockedByIBListener onBlockedByIBListener;

    /* renamed from: com.zipow.videobox.view.mm.MMSelectContactsListView$Listener */
    public interface Listener {
        void onSelectCountReachMax();

        void onSelected(boolean z, MMSelectContactsListItem mMSelectContactsListItem);

        void onSelectionChanged();

        void onViewMoreClick();
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSelectContactsListView$OnBlockedByIBListener */
    public interface OnBlockedByIBListener {
        void onBlockedByIB();
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSelectContactsListView$RetainedFragment */
    public static class RetainedFragment extends ZMFragment {
        @Nullable
        private List<MMSelectContactsListItem> mSelectedItems = null;
        @Nullable
        private WebSearchResult mWebSearchResult = null;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveSelectedItems(List<MMSelectContactsListItem> list) {
            this.mSelectedItems = list;
        }

        @Nullable
        public List<MMSelectContactsListItem> restoreSelectedItems() {
            return this.mSelectedItems;
        }

        public void saveWebSearchResult(WebSearchResult webSearchResult) {
            this.mWebSearchResult = webSearchResult;
        }

        @Nullable
        public WebSearchResult restoreWebSearchResult() {
            return this.mWebSearchResult;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult */
    static class WebSearchResult {
        @Nullable
        String key;
        @NonNull
        Map<String, MMSelectContactsListItem> results = new HashMap();

        WebSearchResult() {
        }

        @Nullable
        public MMSelectContactsListItem findByJid(String str) {
            return (MMSelectContactsListItem) this.results.get(str);
        }

        @NonNull
        public Set<String> getAllJids() {
            return this.results.keySet();
        }

        public void putInviteBuddyItem(@NonNull String str, @NonNull MMSelectContactsListItem mMSelectContactsListItem) {
            this.results.put(str, mMSelectContactsListItem);
        }

        public void clear() {
            this.key = null;
            this.results.clear();
        }
    }

    public MMSelectContactsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MMSelectContactsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMSelectContactsListView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        initAddMoreFooter();
        this.mAdapter = new MMSelectContactsListAdapter(getContext(), this);
        setOnItemClickListener(this);
        setmOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 0) {
                    MMSelectContactsListView.this.refreshVcards();
                    if (MMSelectContactsListView.this.mAdapter != null) {
                        MMSelectContactsListView.this.mAdapter.clearmLoadedContactJids();
                    }
                }
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (i == 0 && i2 > 0) {
                    MMSelectContactsListView.this.refreshVcards();
                }
            }
        });
        setHeaderDividersEnabled(false);
        if (!isInEditMode()) {
            initRetainedFragment();
        }
    }

    public OnBlockedByIBListener getOnBlockedByIBListener() {
        return this.onBlockedByIBListener;
    }

    public void setOnBlockedByIBListener(OnBlockedByIBListener onBlockedByIBListener2) {
        this.onBlockedByIBListener = onBlockedByIBListener2;
    }

    /* access modifiers changed from: private */
    public void refreshVcards() {
        MMSelectContactsListAdapter mMSelectContactsListAdapter = this.mAdapter;
        if (mMSelectContactsListAdapter != null) {
            List list = mMSelectContactsListAdapter.getmLoadedContactJids();
            if (!CollectionsUtil.isListEmpty(list)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    zoomMessenger.refreshBuddyVCards(list);
                }
            }
        }
    }

    public void loadEmailForVisibleItems() {
        ZoomMessenger goodConnectedZoomMessenger = MMLocalHelper.getGoodConnectedZoomMessenger();
        if (goodConnectedZoomMessenger != null) {
            ListView listView = getmmListView();
            if (listView != null) {
                goodConnectedZoomMessenger.refreshBuddyVCards(getNoEmailList(listView.getFirstVisiblePosition(), listView.getLastVisiblePosition()));
            }
        }
    }

    @NonNull
    private List<String> getNoEmailList(int i, int i2) {
        int i3 = i2 - i;
        ArrayList arrayList = new ArrayList();
        if (i3 > 1) {
            for (int i4 = i; i4 <= i2; i4++) {
                Object itemAtPosition = getItemAtPosition(i4);
                if (itemAtPosition instanceof MMSelectContactsListItem) {
                    MMSelectContactsListItem mMSelectContactsListItem = (MMSelectContactsListItem) itemAtPosition;
                    if (StringUtil.isEmptyOrNull(mMSelectContactsListItem.getEmail()) && !StringUtil.isEmptyOrNull(mMSelectContactsListItem.getBuddyJid())) {
                        arrayList.add(mMSelectContactsListItem.getBuddyJid());
                    }
                }
            }
            int i5 = i - i3;
            if (i5 < 0) {
                i5 = 0;
            }
            while (i5 < i) {
                Object itemAtPosition2 = getItemAtPosition(i5);
                if (itemAtPosition2 instanceof MMSelectContactsListItem) {
                    MMSelectContactsListItem mMSelectContactsListItem2 = (MMSelectContactsListItem) itemAtPosition2;
                    if (StringUtil.isEmptyOrNull(mMSelectContactsListItem2.getEmail()) && !StringUtil.isEmptyOrNull(mMSelectContactsListItem2.getBuddyJid())) {
                        arrayList.add(mMSelectContactsListItem2.getBuddyJid());
                    }
                }
                i5++;
            }
            int i6 = i3 + i2;
            if (i6 > getChildCount()) {
                i6 = getChildCount();
            }
            while (i2 < i6) {
                Object itemAtPosition3 = getItemAtPosition(i2);
                if (itemAtPosition3 instanceof MMSelectContactsListItem) {
                    MMSelectContactsListItem mMSelectContactsListItem3 = (MMSelectContactsListItem) itemAtPosition3;
                    if (StringUtil.isEmptyOrNull(mMSelectContactsListItem3.getEmail()) && !StringUtil.isEmptyOrNull(mMSelectContactsListItem3.getBuddyJid())) {
                        arrayList.add(mMSelectContactsListItem3.getBuddyJid());
                    }
                }
                i2++;
            }
        }
        return arrayList;
    }

    private void _editmode_loadAllBuddyItems(@NonNull MMSelectContactsListAdapter mMSelectContactsListAdapter) {
        for (int i = 0; i < 20; i++) {
            MMSelectContactsListItem mMSelectContactsListItem = new MMSelectContactsListItem();
            StringBuilder sb = new StringBuilder();
            sb.append("Buddy ");
            sb.append(i);
            mMSelectContactsListItem.screenName = sb.toString();
            mMSelectContactsListItem.sortKey = mMSelectContactsListItem.screenName;
            mMSelectContactsListItem.itemId = String.valueOf(i);
            mMSelectContactsListItem.setIsChecked(i % 2 == 0);
            mMSelectContactsListAdapter.addItem(mMSelectContactsListItem);
        }
    }

    private void loadAllBuddyItems(@NonNull MMSelectContactsListAdapter mMSelectContactsListAdapter) {
        setQuickSearchEnabled(true);
        loadAllAddrBookItems(mMSelectContactsListAdapter);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0040  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadAllAddrBookItems(@androidx.annotation.NonNull com.zipow.videobox.view.p014mm.MMSelectContactsListAdapter r15) {
        /*
            r14 = this;
            android.content.Context r0 = r14.getContext()
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r0 = r0.isPhoneNumberRegistered()
            r1 = 0
            if (r0 == 0) goto L_0x002e
            com.zipow.videobox.ptapp.ABContactsCache r0 = com.zipow.videobox.ptapp.ABContactsCache.getInstance()
            boolean r2 = r0.isCached()
            if (r2 != 0) goto L_0x001f
            r0.reloadAllContacts()
        L_0x001f:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.ABContactsHelper r0 = r0.getABContactsHelper()
            if (r0 == 0) goto L_0x002e
            java.lang.String r0 = r0.getVerifiedPhoneNumber()
            goto L_0x002f
        L_0x002e:
            r0 = r1
        L_0x002f:
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r9 = r2.getZoomMessenger()
            if (r9 != 0) goto L_0x0040
            r14.loadHistoryEmails(r15)
            r15.sort()
            return
        L_0x0040:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r10 = r9.getMyself()
            if (r10 != 0) goto L_0x0047
            return
        L_0x0047:
            java.lang.String r2 = r14.mGroupId
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r2)
            r3 = 0
            if (r2 != 0) goto L_0x0099
            java.lang.String r1 = r14.mGroupId
            com.zipow.videobox.ptapp.mm.ZoomGroup r1 = r9.getGroupById(r1)
            if (r1 != 0) goto L_0x0059
            return
        L_0x0059:
            java.lang.String r11 = r10.getJid()
            int r12 = r1.getBuddyCount()
            r13 = 0
        L_0x0062:
            if (r13 >= r12) goto L_0x01ad
            com.zipow.videobox.ptapp.mm.ZoomBuddy r4 = r1.getBuddyAt(r13)
            if (r4 != 0) goto L_0x006b
            goto L_0x0096
        L_0x006b:
            boolean r2 = r14.mIncludeMe
            if (r2 != 0) goto L_0x007a
            java.lang.String r2 = r4.getJid()
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isSameString(r2, r11)
            if (r2 == 0) goto L_0x007a
            goto L_0x0096
        L_0x007a:
            boolean r2 = r4.isZoomRoom()
            if (r2 != 0) goto L_0x0096
            boolean r2 = r4.getIsRoomDevice()
            if (r2 == 0) goto L_0x0087
            goto L_0x0096
        L_0x0087:
            r7 = 1
            r2 = r14
            r3 = r9
            r5 = r0
            r6 = r15
            r8 = r10
            com.zipow.videobox.view.mm.MMSelectContactsListItem r2 = r2.newItemFromZoomBuddy(r3, r4, r5, r6, r7, r8)
            if (r2 == 0) goto L_0x0096
            r15.addItem(r2)
        L_0x0096:
            int r13 = r13 + 1
            goto L_0x0062
        L_0x0099:
            java.lang.String r2 = r14.mFilter
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r2)
            r11 = 250(0xfa, float:3.5E-43)
            if (r2 == 0) goto L_0x00de
            r1 = 0
        L_0x00a4:
            int r2 = r9.getBuddyCount()
            if (r1 >= r2) goto L_0x01ad
            com.zipow.videobox.ptapp.mm.ZoomBuddy r4 = r9.getBuddyAt(r1)
            if (r4 != 0) goto L_0x00b1
            goto L_0x00db
        L_0x00b1:
            boolean r2 = r4.isZoomRoom()
            if (r2 != 0) goto L_0x00db
            boolean r2 = r4.isPending()
            if (r2 != 0) goto L_0x00db
            boolean r2 = r4.getIsRoomDevice()
            if (r2 == 0) goto L_0x00c4
            goto L_0x00db
        L_0x00c4:
            r7 = 0
            r2 = r14
            r3 = r9
            r5 = r0
            r6 = r15
            r8 = r10
            com.zipow.videobox.view.mm.MMSelectContactsListItem r2 = r2.newItemFromZoomBuddy(r3, r4, r5, r6, r7, r8)
            if (r2 == 0) goto L_0x00d3
            r15.addItem(r2)
        L_0x00d3:
            int r2 = r15.getCount()
            if (r2 < r11) goto L_0x00db
            goto L_0x01ad
        L_0x00db:
            int r1 = r1 + 1
            goto L_0x00a4
        L_0x00de:
            java.lang.String r2 = r14.mFilter
            java.util.List r1 = r9.localStrictSearchBuddies(r2, r1)
            boolean r2 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r1)
            if (r2 != 0) goto L_0x0154
            java.util.Iterator r1 = r1.iterator()
        L_0x00ee:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0159
            java.lang.Object r2 = r1.next()
            java.lang.String r2 = (java.lang.String) r2
            com.zipow.videobox.ptapp.mm.ZoomBuddy r4 = r9.getBuddyWithJID(r2)
            if (r4 == 0) goto L_0x00ee
            java.lang.String r2 = r4.getJid()
            if (r2 != 0) goto L_0x0107
            goto L_0x00ee
        L_0x0107:
            boolean r2 = r4.isZoomRoom()
            if (r2 != 0) goto L_0x00ee
            boolean r2 = r4.isPending()
            if (r2 != 0) goto L_0x00ee
            boolean r2 = r4.getIsRoomDevice()
            if (r2 == 0) goto L_0x011a
            goto L_0x00ee
        L_0x011a:
            com.zipow.videobox.ptapp.mm.ZMBuddySyncInstance r2 = com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.getInsatance()
            java.lang.String r3 = r4.getJid()
            com.zipow.videobox.view.IMAddrBookItem r2 = r2.getBuddyByJid(r3)
            if (r2 != 0) goto L_0x0129
            goto L_0x00ee
        L_0x0129:
            r7 = 1
            r2 = r14
            r3 = r9
            r5 = r0
            r6 = r15
            r8 = r10
            com.zipow.videobox.view.mm.MMSelectContactsListItem r2 = r2.newItemFromZoomBuddy(r3, r4, r5, r6, r7, r8)
            if (r2 == 0) goto L_0x014d
            boolean r3 = r2.isBlockedByIB()
            if (r3 == 0) goto L_0x014a
            com.zipow.videobox.view.mm.MMSelectContactsListView$OnBlockedByIBListener r3 = r14.onBlockedByIBListener
            if (r3 == 0) goto L_0x0142
            r3.onBlockedByIB()
        L_0x0142:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r3 = r14.mAdapter
            java.lang.String r2 = r2.itemId
            r3.removeItem(r2)
            goto L_0x014d
        L_0x014a:
            r15.addItem(r2)
        L_0x014d:
            int r2 = r15.getCount()
            if (r2 < r11) goto L_0x00ee
            goto L_0x0159
        L_0x0154:
            java.lang.String r1 = r14.mFilter
            com.zipow.videobox.util.MMLocalHelper.searchBuddyByKey(r1)
        L_0x0159:
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r1 = r14.mWebSearchResult
            java.lang.String r1 = r1.key
            java.lang.String r2 = r14.mFilter
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isSameString(r1, r2)
            if (r1 == 0) goto L_0x01ad
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r1 = r14.mWebSearchResult
            java.util.Set r1 = r1.getAllJids()
            java.util.Iterator r1 = r1.iterator()
        L_0x016f:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x01ad
            java.lang.Object r2 = r1.next()
            java.lang.String r2 = (java.lang.String) r2
            com.zipow.videobox.ptapp.mm.ZoomBuddy r4 = r9.getBuddyWithJID(r2)
            if (r4 == 0) goto L_0x01a5
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r3 = r14.mWebSearchResult
            com.zipow.videobox.view.mm.MMSelectContactsListItem r2 = r3.findByJid(r2)
            if (r2 != 0) goto L_0x0194
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r6 = r14.mAdapter
            r7 = 1
            r2 = r14
            r3 = r9
            r5 = r0
            r8 = r10
            com.zipow.videobox.view.mm.MMSelectContactsListItem r2 = r2.newItemFromZoomBuddy(r3, r4, r5, r6, r7, r8)
        L_0x0194:
            if (r2 != 0) goto L_0x0197
            goto L_0x016f
        L_0x0197:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r3 = r14.mAdapter
            r3.updateItem(r2)
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r2 = r14.mAdapter
            int r2 = r2.getCount()
            if (r2 < r11) goto L_0x01a5
            goto L_0x01ad
        L_0x01a5:
            android.widget.Button r2 = r14.mBtnSearchMore
            r3 = 8
            r2.setVisibility(r3)
            goto L_0x016f
        L_0x01ad:
            r14.loadHistoryEmails(r15)
            r15.sort()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMSelectContactsListView.loadAllAddrBookItems(com.zipow.videobox.view.mm.MMSelectContactsListAdapter):void");
    }

    private void loadHistoryEmails(@NonNull MMSelectContactsListAdapter mMSelectContactsListAdapter) {
        if (this.mIsAlterHost) {
            Set<String> set = this.mHistoryEmails;
            if (set != null && !set.isEmpty()) {
                for (String newItemFromEmail : this.mHistoryEmails) {
                    MMSelectContactsListItem newItemFromEmail2 = newItemFromEmail(newItemFromEmail, this.mAdapter);
                    if (newItemFromEmail2 != null) {
                        newItemFromEmail2.setAlternativeHost(true);
                        mMSelectContactsListAdapter.addItem(newItemFromEmail2);
                    }
                }
            }
        }
    }

    @Nullable
    private MMSelectContactsListItem newItemFromEmail(@Nullable String str, @NonNull MMSelectContactsListAdapter mMSelectContactsListAdapter) {
        if (str == null) {
            return null;
        }
        IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
        iMAddrBookItem.setAccoutEmail(str);
        iMAddrBookItem.setScreenName(str);
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null && StringUtil.isSameStringForNotAllowNull(currentUserProfile.getEmail(), str)) {
            return null;
        }
        String str2 = this.mFilter;
        if (str2 != null && str2.length() > 0) {
            Locale localDefault = CompatUtils.getLocalDefault();
            String lowerCase = this.mFilter.toLowerCase(localDefault);
            String lowerCase2 = str.toLowerCase(localDefault);
            String lowerCase3 = str.toLowerCase(localDefault);
            if (!lowerCase2.contains(lowerCase) && !lowerCase3.contains(lowerCase)) {
                return null;
            }
        }
        mMSelectContactsListAdapter.setmIsShowEmail(this.mIsShowEmail);
        mMSelectContactsListAdapter.setmIsAlterHost(this.mIsAlterHost);
        MMSelectContactsListItem mMSelectContactsListItem = new MMSelectContactsListItem(iMAddrBookItem);
        boolean z = false;
        MMSelectContactsListItem findFirstItemWithScreenName = mMSelectContactsListAdapter.findFirstItemWithScreenName(str, 0);
        if (findFirstItemWithScreenName != null) {
            findFirstItemWithScreenName.setShowNotes(true);
            mMSelectContactsListItem.setShowNotes(true);
        }
        Iterator it = this.mSelectedItems.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MMSelectContactsListItem mMSelectContactsListItem2 = (MMSelectContactsListItem) it.next();
            if (mMSelectContactsListItem2.isAlternativeHost() && StringUtil.isSameStringForNotAllowNull(str, mMSelectContactsListItem2.getEmail())) {
                this.mSelectedItems.set(i, mMSelectContactsListItem);
                z = true;
                break;
            }
            i++;
        }
        if (z) {
            mMSelectContactsListItem.setIsDisabled(this.mIsDisabledForPreSelected);
            mMSelectContactsListItem.setIsChecked(true);
        }
        return mMSelectContactsListItem;
    }

    @Nullable
    private MMSelectContactsListItem newItemFromZoomBuddy(@NonNull ZoomMessenger zoomMessenger, @Nullable ZoomBuddy zoomBuddy, @Nullable String str, @NonNull MMSelectContactsListAdapter mMSelectContactsListAdapter, boolean z, @Nullable ZoomBuddy zoomBuddy2) {
        if (zoomBuddy == null || zoomBuddy.getJid() == null) {
            return null;
        }
        IMAddrBookItem buddyByJid = ZMBuddySyncInstance.getInsatance().getBuddyByJid(zoomBuddy.getJid());
        if (buddyByJid == null || buddyByJid.getJid() == null) {
            return null;
        }
        if (!StringUtil.isEmptyOrNull(str) && str.equals(buddyByJid.getBuddyPhoneNumber())) {
            return null;
        }
        String jid = buddyByJid.getJid();
        if (!this.mIncludeMe && zoomBuddy2 != null && StringUtil.isSameString(zoomBuddy2.getJid(), jid)) {
            return null;
        }
        if (!z && !StringUtil.isEmptyOrNull(jid) && !zoomMessenger.isMyContact(jid) && buddyByJid.getContactId() < 0 && (!StringUtil.isSameString(this.mWebSearchResult.key, this.mFilter) || this.mWebSearchResult.findByJid(zoomBuddy.getJid()) == null)) {
            return null;
        }
        String screenName = buddyByJid.getScreenName();
        String accountEmail = buddyByJid.getAccountEmail();
        if (this.mIsNeedHaveEmail && StringUtil.isEmptyOrNull(accountEmail) && !StringUtil.isEmptyOrNull(jid)) {
            buddyByJid.setAccoutEmail((String) this.mCacheJidEmail.get(jid));
        }
        if (buddyByJid.isZoomRoomContact() || buddyByJid.getIsRoomDevice()) {
            return null;
        }
        if (this.mFilterZoomRooms && (buddyByJid.isZoomRoomContact() || zoomMessenger.isZoomRoomContact(buddyByJid.getJid()))) {
            return null;
        }
        if (!this.mOnlyRobot && !this.mIncludeRobot && buddyByJid.getIsRobot()) {
            return null;
        }
        if (this.mOnlyRobot && !buddyByJid.getIsRobot()) {
            return null;
        }
        if (this.mOnlyRobot) {
            mMSelectContactsListAdapter.setmIsSlashCommand(true);
        }
        mMSelectContactsListAdapter.setmIsShowEmail(this.mIsShowEmail);
        mMSelectContactsListAdapter.setmIsAlterHost(this.mIsAlterHost);
        MMSelectContactsListItem mMSelectContactsListItem = new MMSelectContactsListItem(buddyByJid);
        boolean z2 = false;
        MMSelectContactsListItem findFirstItemWithScreenName = mMSelectContactsListAdapter.findFirstItemWithScreenName(screenName, 0);
        if (findFirstItemWithScreenName != null) {
            findFirstItemWithScreenName.setShowNotes(true);
            mMSelectContactsListItem.setShowNotes(true);
        }
        if (this.mIsAlterHost) {
            Iterator it = this.mSelectedItems.iterator();
            int i = 0;
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MMSelectContactsListItem mMSelectContactsListItem2 = (MMSelectContactsListItem) it.next();
                if (mMSelectContactsListItem2.isAlternativeHost() && StringUtil.isSameStringForNotAllowNull(accountEmail, mMSelectContactsListItem2.getEmail())) {
                    this.mSelectedItems.set(i, mMSelectContactsListItem);
                    z2 = true;
                    break;
                }
                i++;
            }
        } else if (this.mPreSelectedItems != null && !StringUtil.isEmptyOrNull(jid) && this.mPreSelectedItems.indexOf(jid) >= 0) {
            z2 = true;
        }
        if (z2) {
            mMSelectContactsListItem.setIsDisabled(this.mIsDisabledForPreSelected);
            mMSelectContactsListItem.setIsChecked(true);
        } else {
            mMSelectContactsListItem.setIsChecked(isAddrBookItemSelected(buddyByJid));
        }
        String email = mMSelectContactsListItem.getEmail();
        if (this.mIsAlterHost && !StringUtil.isEmptyOrNull(email) && !CollectionsUtil.isCollectionEmpty(this.mHistoryEmails)) {
            this.mHistoryEmails.remove(email);
        }
        return mMSelectContactsListItem;
    }

    public void sort() {
        this.mAdapter.sort();
        this.mAdapter.notifyDataSetChanged();
    }

    public void setAvatarMemCache(MemCache<String, Bitmap> memCache) {
        this.mAdapter.setAvatarMemCache(memCache);
    }

    public void setParentFragment(MMSelectContactsFragment mMSelectContactsFragment) {
        this.mParentFragment = mMSelectContactsFragment;
    }

    public void setMaxSelectCount(int i) {
        this.mMaxSelectCount = i;
    }

    public void onClickEveryone() {
        MMSelectContactsFragment mMSelectContactsFragment = this.mParentFragment;
        if (mMSelectContactsFragment != null) {
            mMSelectContactsFragment.onClickEveryone();
        }
    }

    private boolean isBuddySelected(@Nullable String str) {
        if (str == null) {
            return false;
        }
        for (MMSelectContactsListItem mMSelectContactsListItem : this.mSelectedItems) {
            if (str.equals(mMSelectContactsListItem.itemId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAddrBookItemSelected(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem == null) {
            return false;
        }
        for (MMSelectContactsListItem addrBookItem : this.mSelectedItems) {
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

    public void setChoiceMode(int i) {
        if (i != 1) {
            i = 0;
        }
        this.mChoiceMode = i;
        this.mAdapter.setChoiceMode(this.mChoiceMode);
        if (isShown()) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void initAddMoreFooter() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_search_view_more, null);
        this.mBtnSearchMore = (Button) inflate.findViewById(C4558R.C4560id.btnSearchMore);
        this.mBtnSearchMore.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MMSelectContactsListView.this.mListener.onViewMoreClick();
            }
        });
        this.mBtnSearchMore.setVisibility(8);
        getListView().addFooterView(inflate);
    }

    private void updateViewMore() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.isConnectionGood()) {
            if (StringUtil.isEmptyOrNull(this.mFilter) || this.mFilter.length() < 3) {
                this.mBtnSearchMore.setVisibility(8);
            } else {
                this.mBtnSearchMore.setVisibility(0);
            }
        }
    }

    public void setPreSelectedItems(@Nullable List<String> list) {
        this.mPreSelectedItems = list;
    }

    public void setPreSelectedItems(@Nullable List<String> list, boolean z) {
        this.mIsAlterHost = z;
        MMSelectContactsListAdapter mMSelectContactsListAdapter = this.mAdapter;
        if (mMSelectContactsListAdapter != null) {
            mMSelectContactsListAdapter.setmIsAlterHost(this.mIsAlterHost);
        }
        if (z) {
            ArrayList arrayList = new ArrayList();
            if (list != null && !list.isEmpty()) {
                Gson gson = new Gson();
                for (String fromJson : list) {
                    SelectAlterHostItem selectAlterHostItem = (SelectAlterHostItem) gson.fromJson(fromJson, SelectAlterHostItem.class);
                    addSelectedItem(MMLocalHelper.transformSelectAlterHostToMMSelectContactsListItem(selectAlterHostItem));
                    if (!StringUtil.isEmptyOrNull(selectAlterHostItem.getEmail())) {
                        arrayList.add(selectAlterHostItem.getEmail());
                    }
                }
            }
            this.mPreSelectedItems = arrayList;
            this.mHistoryEmails = ConfLocalHelper.loadHistoryEmailsForAlterHosts();
            return;
        }
        this.mPreSelectedItems = list;
    }

    public void setGroupId(String str, boolean z) {
        this.mGroupId = str;
        if (StringUtil.isEmptyOrNull(str) || !z) {
            this.mAdapter.setHasEveryone(false);
        } else {
            this.mAdapter.setHasEveryone(true);
        }
    }

    public void reloadAllBuddyItems() {
        System.currentTimeMillis();
        this.mAdapter.clear();
        loadAllBuddyItems(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0085  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateBuddyInfoWithJid(java.lang.String r10) {
        /*
            r9 = this;
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r2 = r0.getZoomMessenger()
            if (r2 != 0) goto L_0x000b
            return
        L_0x000b:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r0 = r2.getBuddyWithJID(r10)
            if (r0 != 0) goto L_0x0012
            return
        L_0x0012:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r7 = r2.getMyself()
            if (r7 != 0) goto L_0x0019
            return
        L_0x0019:
            java.lang.String r1 = r9.mGroupId
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            r8 = 1
            if (r1 != 0) goto L_0x004b
            java.lang.String r1 = r9.mGroupId
            com.zipow.videobox.ptapp.mm.ZoomGroup r1 = r2.getGroupById(r1)
            if (r1 != 0) goto L_0x002b
            return
        L_0x002b:
            int r3 = r1.getBuddyCount()
            r4 = 0
            r5 = 0
        L_0x0031:
            if (r5 >= r3) goto L_0x0048
            com.zipow.videobox.ptapp.mm.ZoomBuddy r6 = r1.getBuddyAt(r5)
            if (r6 == 0) goto L_0x0045
            java.lang.String r6 = r6.getJid()
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isSameString(r10, r6)
            if (r6 == 0) goto L_0x0045
            r4 = 1
            goto L_0x0048
        L_0x0045:
            int r5 = r5 + 1
            goto L_0x0031
        L_0x0048:
            if (r4 != 0) goto L_0x004b
            return
        L_0x004b:
            r10 = 0
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r1 = r1.isPhoneNumberRegistered()
            if (r1 == 0) goto L_0x0069
            com.zipow.videobox.ptapp.ABContactsCache.getInstance()
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.ABContactsHelper r1 = r1.getABContactsHelper()
            if (r1 == 0) goto L_0x0069
            java.lang.String r10 = r1.getVerifiedPhoneNumber()
            r4 = r10
            goto L_0x006a
        L_0x0069:
            r4 = r10
        L_0x006a:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r10 = r9.mAdapter
            java.lang.String r1 = r0.getJid()
            r10.removeItem(r1)
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r5 = r9.mAdapter
            java.lang.String r10 = r9.mGroupId
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            r6 = r10 ^ 1
            r1 = r9
            r3 = r0
            com.zipow.videobox.view.mm.MMSelectContactsListItem r10 = r1.newItemFromZoomBuddy(r2, r3, r4, r5, r6, r7)
            if (r10 == 0) goto L_0x0097
            boolean r1 = r9.mIsAlterHost
            if (r1 == 0) goto L_0x0092
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r9.mAdapter
            java.lang.String r0 = r0.getEmail()
            r1.removeItemByEmail(r0)
        L_0x0092:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r0 = r9.mAdapter
            r0.addItem(r10)
        L_0x0097:
            r9.notifyDataSetChanged(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMSelectContactsListView.updateBuddyInfoWithJid(java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onIndicationZoomMessengerSearchBuddyByKeyForNoProgressDialog(java.lang.String r18, int r19) {
        /*
            r17 = this;
            r7 = r17
            r0 = r18
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r8 = r1.getZoomMessenger()
            if (r8 != 0) goto L_0x000f
            return
        L_0x000f:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r9 = r8.getMyself()
            if (r9 != 0) goto L_0x0016
            return
        L_0x0016:
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r1 = r1.isPhoneNumberRegistered()
            r2 = 0
            if (r1 == 0) goto L_0x0034
            com.zipow.videobox.ptapp.ABContactsCache.getInstance()
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.ABContactsHelper r1 = r1.getABContactsHelper()
            if (r1 == 0) goto L_0x0034
            java.lang.String r1 = r1.getVerifiedPhoneNumber()
            r10 = r1
            goto L_0x0035
        L_0x0034:
            r10 = r2
        L_0x0035:
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r1 = r7.mAutoWebSearchResult
            r1.clear()
            java.util.HashSet r11 = new java.util.HashSet
            r11.<init>()
            java.util.List r1 = r8.localStrictSearchBuddies(r0, r2)
            if (r1 == 0) goto L_0x0048
            r11.addAll(r1)
        L_0x0048:
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData r12 = r8.getBuddySearchData()
            if (r12 != 0) goto L_0x004f
            return
        L_0x004f:
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData$SearchKey r1 = r12.getSearchKey()
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r2 = r7.mAutoWebSearchResult
            r2.key = r0
            if (r1 == 0) goto L_0x00b8
            r15 = 0
        L_0x005f:
            int r0 = r12.getBuddyCount()
            if (r15 >= r0) goto L_0x00b4
            com.zipow.videobox.ptapp.mm.ZoomBuddy r16 = r12.getBuddyAt(r15)
            if (r16 == 0) goto L_0x00b1
            java.lang.String r6 = r16.getJid()
            java.lang.String r0 = r16.getEmail()
            boolean r1 = r7.mIsNeedHaveEmail
            if (r1 == 0) goto L_0x0088
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r6)
            if (r1 != 0) goto L_0x0088
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
            if (r1 != 0) goto L_0x0088
            java.util.HashMap<java.lang.String, java.lang.String> r1 = r7.mCacheJidEmail
            r1.put(r6, r0)
        L_0x0088:
            r13.add(r6)
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r4 = r7.mAdapter
            r5 = 1
            r0 = r17
            r1 = r8
            r2 = r16
            r3 = r10
            r14 = r6
            r6 = r9
            com.zipow.videobox.view.mm.MMSelectContactsListItem r0 = r0.newItemFromZoomBuddy(r1, r2, r3, r4, r5, r6)
            if (r0 != 0) goto L_0x009d
            goto L_0x00b1
        L_0x009d:
            boolean r1 = r7.mIsAlterHost
            if (r1 == 0) goto L_0x00aa
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r7.mAdapter
            java.lang.String r2 = r16.getEmail()
            r1.removeItemByEmail(r2)
        L_0x00aa:
            if (r14 == 0) goto L_0x00b1
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r1 = r7.mAutoWebSearchResult
            r1.putInviteBuddyItem(r14, r0)
        L_0x00b1:
            int r15 = r15 + 1
            goto L_0x005f
        L_0x00b4:
            r0 = 0
            r8.getBuddiesPresence(r13, r0)
        L_0x00b8:
            r11.addAll(r13)
            com.zipow.videobox.ptapp.mm.ZoomBuddy r0 = r8.getMyself()
            if (r0 == 0) goto L_0x0127
            java.util.Iterator r11 = r11.iterator()
        L_0x00c5:
            boolean r0 = r11.hasNext()
            if (r0 == 0) goto L_0x0127
            java.lang.Object r0 = r11.next()
            java.lang.String r0 = (java.lang.String) r0
            com.zipow.videobox.ptapp.mm.ZoomBuddy r12 = r8.getBuddyWithJID(r0)
            if (r12 == 0) goto L_0x00c5
            boolean r1 = r12.isPending()
            if (r1 == 0) goto L_0x00de
            goto L_0x00c5
        L_0x00de:
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r1 = r7.mAutoWebSearchResult
            com.zipow.videobox.view.mm.MMSelectContactsListItem r0 = r1.findByJid(r0)
            if (r0 != 0) goto L_0x00f3
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r4 = r7.mAdapter
            r5 = 1
            r0 = r17
            r1 = r8
            r2 = r12
            r3 = r10
            r6 = r9
            com.zipow.videobox.view.mm.MMSelectContactsListItem r0 = r0.newItemFromZoomBuddy(r1, r2, r3, r4, r5, r6)
        L_0x00f3:
            if (r0 != 0) goto L_0x00f6
            goto L_0x00c5
        L_0x00f6:
            boolean r1 = r7.mIsAlterHost
            if (r1 == 0) goto L_0x0103
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r7.mAdapter
            java.lang.String r2 = r12.getEmail()
            r1.removeItemByEmail(r2)
        L_0x0103:
            boolean r1 = r0.isBlockedByIB()
            if (r1 == 0) goto L_0x0118
            com.zipow.videobox.view.mm.MMSelectContactsListView$OnBlockedByIBListener r1 = r7.onBlockedByIBListener
            if (r1 == 0) goto L_0x0110
            r1.onBlockedByIB()
        L_0x0110:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r7.mAdapter
            java.lang.String r0 = r0.itemId
            r1.removeItem(r0)
            goto L_0x011d
        L_0x0118:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r7.mAdapter
            r1.updateItem(r0)
        L_0x011d:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r0 = r7.mAdapter
            int r0 = r0.getCount()
            r1 = 250(0xfa, float:3.5E-43)
            if (r0 < r1) goto L_0x00c5
        L_0x0127:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r0 = r7.mAdapter
            r0.notifyDataSetChanged()
            android.widget.Button r0 = r7.mBtnSearchMore
            r1 = 8
            r0.setVisibility(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMSelectContactsListView.onIndicationZoomMessengerSearchBuddyByKeyForNoProgressDialog(java.lang.String, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0054 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0055  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onIndicationZoomMessengerSearchBuddyByKey(java.lang.String r18, int r19) {
        /*
            r17 = this;
            r7 = r17
            java.lang.String r0 = r7.mFilter
            r1 = r18
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isSameString(r1, r0)
            if (r0 != 0) goto L_0x000d
            return
        L_0x000d:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r8 = r0.getZoomMessenger()
            if (r8 != 0) goto L_0x0018
            return
        L_0x0018:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r9 = r8.getMyself()
            if (r9 != 0) goto L_0x001f
            return
        L_0x001f:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r0 = r0.isPhoneNumberRegistered()
            r1 = 0
            if (r0 == 0) goto L_0x003d
            com.zipow.videobox.ptapp.ABContactsCache.getInstance()
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.ABContactsHelper r0 = r0.getABContactsHelper()
            if (r0 == 0) goto L_0x003d
            java.lang.String r0 = r0.getVerifiedPhoneNumber()
            r10 = r0
            goto L_0x003e
        L_0x003d:
            r10 = r1
        L_0x003e:
            java.util.HashSet r11 = new java.util.HashSet
            r11.<init>()
            java.lang.String r0 = r7.mFilter
            java.util.List r0 = r8.localStrictSearchBuddies(r0, r1)
            if (r0 == 0) goto L_0x004e
            r11.addAll(r0)
        L_0x004e:
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData r12 = r8.getBuddySearchData()
            if (r12 != 0) goto L_0x0055
            return
        L_0x0055:
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData$SearchKey r0 = r12.getSearchKey()
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r1 = r7.mWebSearchResult
            java.lang.String r2 = r7.mFilter
            r1.key = r2
            com.zipow.videobox.ptapp.mm.ZoomBuddySearchData$SearchKey r1 = r12.getSearchKey()
            if (r0 == 0) goto L_0x00b9
            if (r1 == 0) goto L_0x00b9
            java.lang.String r0 = r1.getKey()
            java.lang.String r1 = r7.mFilter
            boolean r0 = android.text.TextUtils.equals(r0, r1)
            if (r0 == 0) goto L_0x00b9
            r15 = 0
        L_0x0079:
            int r0 = r12.getBuddyCount()
            if (r15 >= r0) goto L_0x00b5
            com.zipow.videobox.ptapp.mm.ZoomBuddy r16 = r12.getBuddyAt(r15)
            if (r16 == 0) goto L_0x00b2
            java.lang.String r6 = r16.getJid()
            r13.add(r6)
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r4 = r7.mAdapter
            r5 = 1
            r0 = r17
            r1 = r8
            r2 = r16
            r3 = r10
            r14 = r6
            r6 = r9
            com.zipow.videobox.view.mm.MMSelectContactsListItem r0 = r0.newItemFromZoomBuddy(r1, r2, r3, r4, r5, r6)
            if (r0 != 0) goto L_0x009e
            goto L_0x00b2
        L_0x009e:
            boolean r1 = r7.mIsAlterHost
            if (r1 == 0) goto L_0x00ab
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r7.mAdapter
            java.lang.String r2 = r16.getEmail()
            r1.removeItemByEmail(r2)
        L_0x00ab:
            if (r14 == 0) goto L_0x00b2
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r1 = r7.mWebSearchResult
            r1.putInviteBuddyItem(r14, r0)
        L_0x00b2:
            int r15 = r15 + 1
            goto L_0x0079
        L_0x00b5:
            r0 = 0
            r8.getBuddiesPresence(r13, r0)
        L_0x00b9:
            r11.addAll(r13)
            com.zipow.videobox.ptapp.mm.ZoomBuddy r0 = r8.getMyself()
            if (r0 == 0) goto L_0x0128
            java.util.Iterator r11 = r11.iterator()
        L_0x00c6:
            boolean r0 = r11.hasNext()
            if (r0 == 0) goto L_0x0128
            java.lang.Object r0 = r11.next()
            java.lang.String r0 = (java.lang.String) r0
            com.zipow.videobox.ptapp.mm.ZoomBuddy r12 = r8.getBuddyWithJID(r0)
            if (r12 == 0) goto L_0x00c6
            boolean r1 = r12.isPending()
            if (r1 == 0) goto L_0x00df
            goto L_0x00c6
        L_0x00df:
            com.zipow.videobox.view.mm.MMSelectContactsListView$WebSearchResult r1 = r7.mWebSearchResult
            com.zipow.videobox.view.mm.MMSelectContactsListItem r0 = r1.findByJid(r0)
            if (r0 != 0) goto L_0x00f4
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r4 = r7.mAdapter
            r5 = 1
            r0 = r17
            r1 = r8
            r2 = r12
            r3 = r10
            r6 = r9
            com.zipow.videobox.view.mm.MMSelectContactsListItem r0 = r0.newItemFromZoomBuddy(r1, r2, r3, r4, r5, r6)
        L_0x00f4:
            if (r0 != 0) goto L_0x00f7
            goto L_0x00c6
        L_0x00f7:
            boolean r1 = r7.mIsAlterHost
            if (r1 == 0) goto L_0x0104
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r7.mAdapter
            java.lang.String r2 = r12.getEmail()
            r1.removeItemByEmail(r2)
        L_0x0104:
            boolean r1 = r0.isBlockedByIB()
            if (r1 == 0) goto L_0x0119
            com.zipow.videobox.view.mm.MMSelectContactsListView$OnBlockedByIBListener r1 = r7.onBlockedByIBListener
            if (r1 == 0) goto L_0x0111
            r1.onBlockedByIB()
        L_0x0111:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r7.mAdapter
            java.lang.String r0 = r0.itemId
            r1.removeItem(r0)
            goto L_0x011e
        L_0x0119:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r1 = r7.mAdapter
            r1.updateItem(r0)
        L_0x011e:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r0 = r7.mAdapter
            int r0 = r0.getCount()
            r1 = 250(0xfa, float:3.5E-43)
            if (r0 < r1) goto L_0x00c6
        L_0x0128:
            com.zipow.videobox.view.mm.MMSelectContactsListAdapter r0 = r7.mAdapter
            r0.notifyDataSetChanged()
            android.widget.Button r0 = r7.mBtnSearchMore
            r1 = 8
            r0.setVisibility(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMSelectContactsListView.onIndicationZoomMessengerSearchBuddyByKey(java.lang.String, int):void");
    }

    public void clearSelection() {
        this.mSelectedItems.clear();
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            Object item = this.mAdapter.getItem(i);
            if (item instanceof MMSelectContactsListItem) {
                ((MMSelectContactsListItem) item).setIsChecked(false);
                this.mAdapter.notifyDataSetChanged();
            }
        }
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSelectionChanged();
        }
    }

    @NonNull
    public List<MMSelectContactsListItem> getSelectedBuddies() {
        return this.mSelectedItems;
    }

    public void unselectBuddy(@Nullable MMSelectContactsListItem mMSelectContactsListItem) {
        if (mMSelectContactsListItem != null) {
            MMSelectContactsListItem itemById = this.mAdapter.getItemById(mMSelectContactsListItem.itemId);
            if (itemById == null && this.mIsAlterHost) {
                itemById = this.mAdapter.getItemByEmail(mMSelectContactsListItem.email);
            }
            if (itemById != null) {
                itemById.setIsChecked(false);
                this.mAdapter.notifyDataSetChanged();
            }
            removeSelectedItem(mMSelectContactsListItem);
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
            if (StringUtil.isEmptyOrNull(this.mGroupId)) {
                this.mWebSearchResult.clear();
                updateViewMore();
                reloadAllBuddyItems();
            } else if (StringUtil.isEmptyOrNull(lowerCase)) {
                this.mAdapter.filter(null);
                reloadAllBuddyItems();
            } else if (StringUtil.isEmptyOrNull(str2) || (!StringUtil.isEmptyOrNull(str2) && lowerCase.contains(str2))) {
                this.mAdapter.filter(lowerCase);
                this.mAdapter.notifyDataSetChanged();
            } else {
                reloadAllBuddyItems();
            }
            if (this.mIsAutoWebSearch && this.mAdapter.isEmpty()) {
                this.mBtnSearchMore.setVisibility(8);
                MMLocalHelper.searchBuddyByKey(this.mFilter);
            }
            if (this.mIsAlterHost && !this.mAdapter.isEmpty()) {
                this.mHandler.removeCallbacks(this.mLoadEmailsRunnable);
                this.mHandler.postDelayed(this.mLoadEmailsRunnable, 300);
            }
        }
    }

    public void setFilter(@Nullable String str) {
        this.mFilter = str;
    }

    @Nullable
    public String getFilter() {
        return this.mFilter;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (itemAtPosition instanceof MMSelectContactsListItem) {
            MMSelectContactsListItem mMSelectContactsListItem = (MMSelectContactsListItem) itemAtPosition;
            IMAddrBookItem addrBookItem = mMSelectContactsListItem.getAddrBookItem();
            if ((addrBookItem == null || addrBookItem.getAccountStatus() == 0) && !mMSelectContactsListItem.isDisabled()) {
                if (!mMSelectContactsListItem.isChecked()) {
                    if (this.mMaxSelectCount > 0) {
                        int i2 = 0;
                        List<String> list = this.mPreSelectedItems;
                        if (list != null) {
                            i2 = list.size();
                        }
                        if (this.mSelectedItems.size() + i2 >= this.mMaxSelectCount) {
                            Listener listener = this.mListener;
                            if (listener != null) {
                                listener.onSelectCountReachMax();
                            }
                            return;
                        }
                    }
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        int groupInviteLimit = zoomMessenger.getGroupInviteLimit();
                        if (groupInviteLimit > 0 && this.mSelectedItems.size() >= groupInviteLimit) {
                            Listener listener2 = this.mListener;
                            if (listener2 != null) {
                                listener2.onSelectCountReachMax();
                            }
                            return;
                        }
                    }
                }
                if (!this.mIsAlterHost || !StringUtil.isEmptyOrNull(mMSelectContactsListItem.getEmail())) {
                    mMSelectContactsListItem.setIsChecked(!mMSelectContactsListItem.isChecked());
                    this.mAdapter.notifyDataSetChanged();
                    if (mMSelectContactsListItem.isChecked()) {
                        addSelectedItem(mMSelectContactsListItem);
                    } else {
                        removeSelectedItem(mMSelectContactsListItem);
                    }
                    Listener listener3 = this.mListener;
                    if (listener3 != null) {
                        listener3.onSelectionChanged();
                    }
                } else {
                    ZoomMessenger goodConnectedZoomMessenger = MMLocalHelper.getGoodConnectedZoomMessenger();
                    if (goodConnectedZoomMessenger != null) {
                        goodConnectedZoomMessenger.refreshBuddyVCard(mMSelectContactsListItem.getBuddyJid(), true);
                    }
                }
            }
        }
    }

    public void stop() {
        this.mHandler.removeCallbacks(this.mLoadEmailsRunnable);
    }

    private void removeSelectedItem(@NonNull MMSelectContactsListItem mMSelectContactsListItem) {
        for (int size = this.mSelectedItems.size() - 1; size >= 0; size--) {
            if (MMLocalHelper.isSameMMSelectContactsListItem(this.mIsAlterHost, mMSelectContactsListItem, (MMSelectContactsListItem) this.mSelectedItems.get(size))) {
                this.mSelectedItems.remove(size);
                Listener listener = this.mListener;
                if (listener != null) {
                    listener.onSelected(false, mMSelectContactsListItem);
                    return;
                }
                return;
            }
        }
    }

    public void addSelectedItem(@NonNull MMSelectContactsListItem mMSelectContactsListItem) {
        mMSelectContactsListItem.setIsChecked(true);
        for (int size = this.mSelectedItems.size() - 1; size >= 0; size--) {
            if (MMLocalHelper.isSameMMSelectContactsListItem(this.mIsAlterHost, mMSelectContactsListItem, (MMSelectContactsListItem) this.mSelectedItems.get(size))) {
                this.mSelectedItems.set(size, mMSelectContactsListItem);
                return;
            }
        }
        this.mSelectedItems.add(mMSelectContactsListItem);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSelected(true, mMSelectContactsListItem);
        }
        if (this.mIsNeedSortSelectedItems) {
            Collections.sort(this.mSelectedItems, new MMBuddyItemComparator(CompatUtils.getLocalDefault()));
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
            return;
        }
        List<MMSelectContactsListItem> restoreSelectedItems = retainedFragment.restoreSelectedItems();
        if (restoreSelectedItems != null) {
            this.mSelectedItems = restoreSelectedItems;
        }
        WebSearchResult restoreWebSearchResult = this.mRetainedFragment.restoreWebSearchResult();
        if (restoreWebSearchResult != null) {
            this.mWebSearchResult = restoreWebSearchResult;
        }
    }

    @Nullable
    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        return (RetainedFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(RetainedFragment.class.getName());
    }

    public void notifyDataSetChanged(boolean z) {
        if (z) {
            this.mAdapter.setLazyLoadAvatarDisabled(true);
            postDelayed(new Runnable() {
                public void run() {
                    MMSelectContactsListView.this.mAdapter.setLazyLoadAvatarDisabled(false);
                }
            }, 1000);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void setOnlySameOrganization(boolean z) {
        this.mOnlySameOrganization = z;
    }

    public void setIncludeRobot(boolean z) {
        this.mIncludeRobot = z;
    }

    public void setmIncludeMe(boolean z) {
        this.mIncludeMe = z;
    }

    public void setmOnlyRobot(boolean z) {
        this.mOnlyRobot = z;
        MMSelectContactsListAdapter mMSelectContactsListAdapter = this.mAdapter;
        if (mMSelectContactsListAdapter != null) {
            mMSelectContactsListAdapter.setmIsSlashCommand(z);
        }
    }

    public void setmFilterZoomRooms(boolean z) {
        this.mFilterZoomRooms = z;
    }

    public void setmIsShowEmail(boolean z) {
        this.mIsShowEmail = z;
        MMSelectContactsListAdapter mMSelectContactsListAdapter = this.mAdapter;
        if (mMSelectContactsListAdapter != null) {
            mMSelectContactsListAdapter.setmIsShowEmail(z);
        }
    }

    public void setmIsDisabledForPreSelected(boolean z) {
        this.mIsDisabledForPreSelected = z;
    }

    public void setmIsNeedHaveEmail(boolean z) {
        this.mIsNeedHaveEmail = z;
    }

    public void setmIsNeedSortSelectedItems(boolean z) {
        this.mIsNeedSortSelectedItems = z;
    }

    public void setmIsAutoWebSearch(boolean z) {
        this.mIsAutoWebSearch = z;
    }
}
