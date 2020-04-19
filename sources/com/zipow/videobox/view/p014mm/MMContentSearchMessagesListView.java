package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.fragment.MMThreadsFragment;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchMSGFilter;
import com.zipow.videobox.ptapp.IMProtos.MessageContentSearchFilter;
import com.zipow.videobox.ptapp.IMProtos.MessageContentSearchFilter.Builder;
import com.zipow.videobox.ptapp.IMProtos.MessageContentSearchResponse;
import com.zipow.videobox.ptapp.IMProtos.MessageSearchResult;
import com.zipow.videobox.ptapp.IMProtos.MessageSenderFilter;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.p013mm.SearchMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.p014mm.MMContentMessageItem.MMContentMessageAnchorInfo;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.PullDownRefreshListView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentSearchMessagesListView */
public class MMContentSearchMessagesListView extends PullDownRefreshListView implements OnScrollListener, OnItemClickListener {
    private static final int LOCAL_SEARCH_MAX_COUNT = 99999;
    private static final int LOCAL_SEARCH_PAGE_SIZE = 30;
    private static final int MAX_COUNT = 99;
    private static final String TAG = "com.zipow.videobox.view.mm.MMContentSearchMessagesListView";
    private MMContentSearchMessagesAdapter mAdapter;
    @NonNull
    private MemCache<String, Drawable> mAvatarCache = new MemCache<>(10);
    @Nullable
    private String mFilter;
    private TextView mFooterTextView;
    private View mFooterView;
    private boolean mIsLocalLoading = false;
    @Nullable
    private String mLocalSearchMsgReqId;
    @NonNull
    private List<MessageSearchResult> mLocalSearchResult = new ArrayList();
    private int mPageNum = 1;
    private ZMDialogFragment mParentFragment;
    private MessageContentSearchResponse mPreResponse;
    private int mResultCode = 1;
    private int mResultSortType = ZMIMUtils.getSearchMessageSortType();
    @Nullable
    private RetainedFragment mRetainedFragment;
    @Nullable
    private String mSearchMsgReqId;
    /* access modifiers changed from: private */
    public String mSessionId;
    private boolean mWebSearched = false;

    /* renamed from: com.zipow.videobox.view.mm.MMContentSearchMessagesListView$RetainedFragment */
    public static class RetainedFragment extends ZMFragment {
        @Nullable
        private MMContentSearchMessagesAdapter mAdapter = null;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveMMContentSearchMessagesAdapter(MMContentSearchMessagesAdapter mMContentSearchMessagesAdapter) {
            this.mAdapter = mMContentSearchMessagesAdapter;
        }

        @Nullable
        public MMContentSearchMessagesAdapter restoreMMContentSearchMessagesAdapter() {
            return this.mAdapter;
        }
    }

    public MMContentSearchMessagesListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MMContentSearchMessagesListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMContentSearchMessagesListView(Context context) {
        super(context);
        init();
    }

    public void setParentFragment(ZMDialogFragment zMDialogFragment) {
        this.mParentFragment = zMDialogFragment;
    }

    public boolean isResultEmpty() {
        MMContentSearchMessagesAdapter mMContentSearchMessagesAdapter = this.mAdapter;
        boolean z = true;
        if (mMContentSearchMessagesAdapter == null) {
            return true;
        }
        if (mMContentSearchMessagesAdapter.getCount() > 0) {
            z = false;
        }
        return z;
    }

    public void setFilter(@NonNull String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str) && str.trim().length() != 0) {
            this.mFilter = str.trim().toLowerCase(CompatUtils.getLocalDefault());
            searchMessage(str2);
        }
    }

    public void setSortType(int i) {
        this.mResultSortType = i;
    }

    public void searchMessage(String str) {
        this.mSessionId = str;
        localSearch(str);
    }

    /* access modifiers changed from: private */
    public boolean searchMessage(String str, boolean z) {
        return searchMessage(str, z, false);
    }

    private boolean searchMessage(String str, boolean z, boolean z2) {
        String str2;
        if (StringUtil.isEmptyOrNull(this.mFilter)) {
            return false;
        }
        if (this.mFilter.length() <= 1) {
            this.mResultCode = 0;
            return false;
        } else if (ZMIMUtils.isE2EChat(this.mSessionId)) {
            this.mResultCode = 0;
            return false;
        } else {
            if (z) {
                if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(this.mSearchMsgReqId)) {
                    return false;
                }
                MessageContentSearchResponse messageContentSearchResponse = this.mPreResponse;
                if (messageContentSearchResponse == null) {
                    return false;
                }
                if (!messageContentSearchResponse.getHasMore() && !this.mPreResponse.getHasMoreMyNotes()) {
                    return false;
                }
            }
            SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
            if (searchMgr == null) {
                return false;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return false;
            }
            this.mWebSearched = true;
            this.mSessionId = str;
            Builder newBuilder = MessageContentSearchFilter.newBuilder();
            String str3 = this.mFilter;
            if (str3 == null) {
                str3 = "";
            }
            newBuilder.setKeyWord(str3);
            newBuilder.setPageSize(99);
            newBuilder.setSortType(this.mResultSortType);
            MessageSenderFilter.Builder newBuilder2 = MessageSenderFilter.newBuilder();
            if (!TextUtils.isEmpty(str)) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    if (sessionById.isGroup()) {
                        newBuilder2.setSessionId(str);
                        newBuilder2.setType(1);
                        newBuilder2.setOnlyP2P(false);
                    } else {
                        newBuilder2.setSenderJid(str);
                        newBuilder2.setOnlyP2P(true);
                        newBuilder2.setType(2);
                    }
                    newBuilder.addSenderInfo(newBuilder2.build());
                }
            }
            if (!z) {
                newBuilder.setPageNum(1);
                if (this.mResultSortType == 2) {
                    newBuilder.setScope(0);
                } else {
                    newBuilder.setScope(1);
                }
                str2 = searchMgr.searchMessageContent(newBuilder.build());
                if (z2) {
                    this.mFooterTextView.setText(C4558R.string.zm_msg_search_all_messages_68749);
                    this.mFooterView.setVisibility(0);
                }
            } else if (!this.mPreResponse.getHasMore()) {
                return false;
            } else {
                if (this.mResultSortType == 2) {
                    newBuilder.setPageNum(this.mPageNum + 1);
                } else {
                    newBuilder.setPageNum(this.mPageNum);
                }
                newBuilder.setScope(this.mPreResponse.getScope());
                newBuilder.setSearchTime(this.mPreResponse.getSearchTime());
                newBuilder.setLastRecordTime(this.mPreResponse.getLastRecordTime());
                str2 = searchMgr.searchMessageContent(newBuilder.build());
                this.mFooterTextView.setText(C4558R.string.zm_msg_loading);
                this.mFooterView.setVisibility(0);
            }
            if (!StringUtil.isEmptyOrNull(str2)) {
                this.mSearchMsgReqId = str2;
            } else {
                this.mResultCode = 1;
            }
            return true;
        }
    }

    private void localSearch(String str) {
        if (StringUtil.isEmptyOrNull(this.mLocalSearchMsgReqId)) {
            SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
            if (searchMgr != null) {
                this.mWebSearched = false;
                this.mAdapter.clearAll();
                this.mAdapter.notifyDataSetChanged();
                LocalSearchMSGFilter genLocalSearchMSGFilter = genLocalSearchMSGFilter(str);
                if (genLocalSearchMSGFilter != null) {
                    this.mLocalSearchMsgReqId = searchMgr.LocalSearchMessage(genLocalSearchMSGFilter);
                    if (StringUtil.isEmptyOrNull(this.mLocalSearchMsgReqId)) {
                        searchMessage(this.mSessionId, false);
                    }
                }
            }
        }
    }

    public void clearSearch() {
        this.mLocalSearchMsgReqId = null;
        this.mAdapter.clearAll();
        notifyDataSetChanged();
    }

    private boolean localSearchMore() {
        if (this.mLocalSearchResult.size() == 0) {
            return false;
        }
        if (this.mIsLocalLoading) {
            return true;
        }
        this.mIsLocalLoading = true;
        loadLocalSearchData();
        this.mIsLocalLoading = false;
        return true;
    }

    private void loadLocalSearchData() {
        if (this.mLocalSearchResult.size() > 0) {
            List subList = this.mLocalSearchResult.subList(0, Math.min(this.mLocalSearchResult.size(), 30));
            this.mAdapter.addLocalSearchedFiles(subList);
            this.mAdapter.notifyDataSetChanged();
            subList.clear();
        }
    }

    private LocalSearchMSGFilter genLocalSearchMSGFilter(String str) {
        if (PTApp.getInstance().getZoomMessenger() == null) {
            return null;
        }
        LocalSearchMSGFilter.Builder newBuilder = LocalSearchMSGFilter.newBuilder();
        String str2 = this.mFilter;
        if (str2 == null) {
            str2 = "";
        }
        newBuilder.setKeyWord(str2);
        newBuilder.setPageSize(99999);
        newBuilder.setSortType(this.mResultSortType);
        if (!TextUtils.isEmpty(str)) {
            newBuilder.setInSession(str);
        }
        return newBuilder.build();
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        this.mAdapter.onIndicateInfoUpdatedWithJID(str);
    }

    public boolean Indicate_SearchMessageResponse(String str, int i, @Nullable MessageContentSearchResponse messageContentSearchResponse) {
        if (StringUtil.isSameString(this.mSearchMsgReqId, str)) {
            this.mPreResponse = messageContentSearchResponse;
            this.mSearchMsgReqId = null;
            this.mResultCode = i;
            this.mFooterView.setVisibility(8);
            if (i != 0 || messageContentSearchResponse == null) {
                return false;
            }
            this.mPageNum = Math.max(1, messageContentSearchResponse.getPageNum());
            if (messageContentSearchResponse.getSearchResponseCount() > 0) {
                this.mAdapter.addSearchedFiles(messageContentSearchResponse);
                this.mAdapter.notifyDataSetChanged();
            }
            if (this.mAdapter.getCount() < 20 && messageContentSearchResponse.hasHasMore()) {
                return searchMessage(this.mSessionId, true);
            }
        }
        return false;
    }

    public boolean Indicate_LocalSearchMSGResponse(String str, @Nullable MessageContentSearchResponse messageContentSearchResponse) {
        if (StringUtil.isSameString(this.mLocalSearchMsgReqId, str)) {
            this.mResultCode = 0;
            this.mLocalSearchMsgReqId = null;
            this.mFooterView.setVisibility(8);
            if (messageContentSearchResponse == null) {
                return searchMessage(this.mSessionId, false, true);
            }
            this.mLocalSearchResult.clear();
            this.mAdapter.clearAll();
            if (messageContentSearchResponse.getSearchResponseCount() > 0) {
                this.mLocalSearchResult.addAll(messageContentSearchResponse.getSearchResponseList());
                loadLocalSearchData();
            }
            if (messageContentSearchResponse.getSearchResponseList() != null && messageContentSearchResponse.getSearchResponseList().size() < 20) {
                return searchMessage(this.mSessionId, false, true);
            }
        }
        return false;
    }

    private void postDelaySearchMessage(final boolean z) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MMContentSearchMessagesListView mMContentSearchMessagesListView = MMContentSearchMessagesListView.this;
                mMContentSearchMessagesListView.searchMessage(mMContentSearchMessagesListView.mSessionId, z);
            }
        }, 100);
    }

    public boolean isEmpty() {
        MMContentSearchMessagesAdapter mMContentSearchMessagesAdapter = this.mAdapter;
        boolean z = true;
        if (mMContentSearchMessagesAdapter == null) {
            return true;
        }
        if (mMContentSearchMessagesAdapter.getCount() != 0) {
            z = false;
        }
        return z;
    }

    public boolean isLoading() {
        return !StringUtil.isEmptyOrNull(this.mSearchMsgReqId) || !StringUtil.isEmptyOrNull(this.mLocalSearchMsgReqId);
    }

    public boolean isLoadSuccess() {
        return StringUtil.isEmptyOrNull(this.mSearchMsgReqId) && StringUtil.isEmptyOrNull(this.mLocalSearchMsgReqId) && this.mResultCode == 0;
    }

    public int getTotalCount() {
        MMContentSearchMessagesAdapter mMContentSearchMessagesAdapter = this.mAdapter;
        if (mMContentSearchMessagesAdapter == null) {
            return 0;
        }
        return mMContentSearchMessagesAdapter.getCount();
    }

    public boolean isSearchResultEmpty() {
        return !isRefreshing() && !StringUtil.isEmptyOrNull(this.mFilter) && this.mAdapter.getCount() == 0;
    }

    public void reset() {
        this.mWebSearched = false;
        this.mLocalSearchMsgReqId = null;
        this.mSearchMsgReqId = null;
        this.mFilter = null;
        this.mPageNum = 1;
        this.mAdapter.clearAll();
    }

    private void init() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_list_load_more_footer, null);
        addFooterView(inflate);
        this.mFooterView = inflate.findViewById(C4558R.C4560id.panelLoadMoreView);
        this.mFooterTextView = (TextView) inflate.findViewById(C4558R.C4560id.txtMsg);
        this.mAdapter = new MMContentSearchMessagesAdapter(getContext());
        this.mAdapter.setAvatarCache(this.mAvatarCache);
        setOnScrollListener(this);
        setOnItemClickListener(this);
        setPullDownRefreshEnabled(false);
        if (!isInEditMode()) {
            initRetainedFragment();
        }
        setAdapter(this.mAdapter);
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString("mSearchMsgReqId", this.mSearchMsgReqId);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mSearchMsgReqId = bundle.getString("mSearchMsgReqId");
            super.onRestoreInstanceState(bundle.getParcelable("superState"));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public void notifyDataSetChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void onScrollStateChanged(@NonNull AbsListView absListView, int i) {
        if (i == 0) {
            if (absListView.getLastVisiblePosition() >= absListView.getCount() - getHeaderViewsCount() && StringUtil.isEmptyOrNull(this.mSearchMsgReqId) && !localSearchMore()) {
                searchMessage(this.mSessionId, this.mWebSearched);
            }
            refreshVcards();
            MMContentSearchMessagesAdapter mMContentSearchMessagesAdapter = this.mAdapter;
            if (mMContentSearchMessagesAdapter != null) {
                mMContentSearchMessagesAdapter.clearmLoadedNeedRrefreshJids();
            }
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (i == 0 && i2 > 0) {
            refreshVcards();
        }
    }

    private void refreshVcards() {
        MMContentSearchMessagesAdapter mMContentSearchMessagesAdapter = this.mAdapter;
        if (mMContentSearchMessagesAdapter != null) {
            List list = mMContentSearchMessagesAdapter.getmLoadedNeedRrefreshJids();
            if (!CollectionsUtil.isListEmpty(list)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    zoomMessenger.refreshBuddyVCards(list);
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        MMContentMessageItem item = this.mAdapter.getItem(i - getHeaderViewsCount());
        if (item != null) {
            MMContentMessageAnchorInfo mMContentMessageAnchorInfo = new MMContentMessageAnchorInfo();
            mMContentMessageAnchorInfo.setMsgGuid(item.getMsgId());
            mMContentMessageAnchorInfo.setSendTime(item.getSendTime());
            mMContentMessageAnchorInfo.setComment(item.isComment());
            mMContentMessageAnchorInfo.setThrId(item.getThrId());
            mMContentMessageAnchorInfo.setThrSvr(item.getThrSvr());
            if (item.isGroup()) {
                mMContentMessageAnchorInfo.setSessionId(item.getSessionId());
            } else {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        if (!StringUtil.isSameString(myself.getJid(), item.getSessionId())) {
                            mMContentMessageAnchorInfo.setSessionId(item.getSessionId());
                        } else if (!StringUtil.isSameString(myself.getJid(), item.getSenderJid())) {
                            mMContentMessageAnchorInfo.setSessionId(item.getSenderJid());
                        } else if (UIMgr.isMyNotes(item.getSessionId())) {
                            mMContentMessageAnchorInfo.setSessionId(myself.getJid());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (item.isComment()) {
                MMCommentsFragment.showMsgContextInActivity(this.mParentFragment, mMContentMessageAnchorInfo);
            } else {
                MMThreadsFragment.showMsgContextInActivity(this.mParentFragment, mMContentMessageAnchorInfo);
            }
            ZoomLogEventTracking.eventTrackOpenSearchedMessage(item.isGroup(), StringUtil.safeString(this.mFilter));
        }
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            this.mRetainedFragment.saveMMContentSearchMessagesAdapter(this.mAdapter);
            ((ZMActivity) getContext()).getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
            return;
        }
        MMContentSearchMessagesAdapter restoreMMContentSearchMessagesAdapter = retainedFragment.restoreMMContentSearchMessagesAdapter();
        if (restoreMMContentSearchMessagesAdapter != null) {
            this.mAdapter = restoreMMContentSearchMessagesAdapter;
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
}
