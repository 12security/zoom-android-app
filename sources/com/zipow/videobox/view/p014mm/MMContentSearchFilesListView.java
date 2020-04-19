package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.ptapp.IMProtos.FileFilterSearchResult;
import com.zipow.videobox.ptapp.IMProtos.FileFilterSearchResults;
import com.zipow.videobox.ptapp.IMProtos.FileSearchFilter;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchFileFilter;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchFileFilter.Builder;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.SearchMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMIMUtils;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.PullDownRefreshListView;

/* renamed from: com.zipow.videobox.view.mm.MMContentSearchFilesListView */
public class MMContentSearchFilesListView extends PullDownRefreshListView implements OnScrollListener, OnItemClickListener, OnContentFileOperatorListener {
    private static final int LOCAL_SEARCH_MAX_COUNT = 99999;
    private static final int MAX_COUNT_PER_PAGE = 99;
    private static final String TAG = "com.zipow.videobox.view.mm.MMContentSearchFilesListView";
    private MMContentSearchFilesAdapter mAdapter;
    @Nullable
    private String mFilter;
    private boolean mIsOwnerMode;
    private OnContentFileOperatorListener mListener;
    @Nullable
    private String mLocalSearchMsgReqId;
    private int mPageNum = 1;
    @Nullable
    private String mReqId;
    private int mResultCode = 1;
    @Nullable
    private RetainedFragment mRetainedFragment;
    private String mSessionId;
    private boolean mWebSearched = false;

    /* renamed from: com.zipow.videobox.view.mm.MMContentSearchFilesListView$RetainedFragment */
    public static class RetainedFragment extends ZMFragment {
        @Nullable
        private MMContentSearchFilesAdapter mAdapter = null;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveMMContentSearchFilesAdapter(MMContentSearchFilesAdapter mMContentSearchFilesAdapter) {
            this.mAdapter = mMContentSearchFilesAdapter;
        }

        @Nullable
        public MMContentSearchFilesAdapter restoreMMContentSearchFilesAdapter() {
            return this.mAdapter;
        }
    }

    public void onPullDownRefresh() {
    }

    public void onZoomFileClick(String str) {
    }

    public void onZoomFileClick(String str, List<String> list) {
    }

    public void onZoomFileIntegrationClick(String str) {
    }

    public MMContentSearchFilesListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MMContentSearchFilesListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMContentSearchFilesListView(Context context) {
        super(context);
        init();
    }

    public boolean isResultEmpty() {
        MMContentSearchFilesAdapter mMContentSearchFilesAdapter = this.mAdapter;
        boolean z = true;
        if (mMContentSearchFilesAdapter == null) {
            return true;
        }
        if (mMContentSearchFilesAdapter.getCount() > 0) {
            z = false;
        }
        return z;
    }

    public void setIsOwnerMode(boolean z) {
        this.mIsOwnerMode = z;
    }

    public void setFilter(@Nullable String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str) && str.trim().length() != 0) {
            this.mFilter = str.trim().toLowerCase(CompatUtils.getLocalDefault());
            searchContent(str2);
        }
    }

    public void searchContent(String str) {
        this.mSessionId = str;
        localSearchContent(str);
    }

    public void localSearchContent(String str) {
        if (StringUtil.isEmptyOrNull(this.mLocalSearchMsgReqId)) {
            SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
            if (searchMgr != null) {
                this.mWebSearched = false;
                this.mAdapter.clearAll();
                this.mAdapter.notifyDataSetChanged();
                LocalSearchFileFilter genLocalSearchContentFilter = genLocalSearchContentFilter(str);
                if (genLocalSearchContentFilter != null) {
                    this.mLocalSearchMsgReqId = searchMgr.LocalSearchFile(genLocalSearchContentFilter);
                    if (StringUtil.isEmptyOrNull(this.mLocalSearchMsgReqId)) {
                        webSearchContent(this.mSessionId);
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

    private LocalSearchFileFilter genLocalSearchContentFilter(String str) {
        if (PTApp.getInstance().getZoomMessenger() == null) {
            return null;
        }
        Builder newBuilder = LocalSearchFileFilter.newBuilder();
        String str2 = this.mFilter;
        if (str2 == null) {
            str2 = "";
        }
        newBuilder.setKeyWord(str2);
        newBuilder.setPageSize(99999);
        if (!TextUtils.isEmpty(str)) {
            newBuilder.setInSession(str);
        }
        return newBuilder.build();
    }

    public boolean webSearchContent(String str) {
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
            SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
            if (searchMgr == null) {
                return false;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return false;
            }
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself == null) {
                return false;
            }
            this.mWebSearched = true;
            FileSearchFilter.Builder newBuilder = FileSearchFilter.newBuilder();
            String str2 = this.mFilter;
            if (str2 == null) {
                str2 = "";
            }
            newBuilder.setKeyWord(str2);
            newBuilder.setPageNum(this.mPageNum);
            newBuilder.setPageSize(99);
            newBuilder.setType(1);
            if (!TextUtils.isEmpty(str)) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    if (sessionById.isGroup()) {
                        newBuilder.setOnlyP2P(false);
                        newBuilder.setSessionId(str);
                    } else {
                        newBuilder.setOnlyP2P(true);
                        newBuilder.setSenderJid(str);
                    }
                }
            }
            if (this.mIsOwnerMode) {
                newBuilder.setSendbyId(myself.getJid());
            }
            String searchFilesContent = searchMgr.searchFilesContent(newBuilder.build());
            if (!StringUtil.isEmptyOrNull(searchFilesContent)) {
                this.mReqId = searchFilesContent;
            } else {
                this.mResultCode = 1;
            }
            return true;
        }
    }

    public boolean isSearchResultEmpty() {
        return !isRefreshing() && !StringUtil.isEmptyOrNull(this.mFilter) && this.mAdapter.getCount() == 0;
    }

    public void setListener(OnContentFileOperatorListener onContentFileOperatorListener) {
        this.mListener = onContentFileOperatorListener;
    }

    public void endFileTransfer(@Nullable String str) {
        this.mAdapter.endFileTransfer(str);
    }

    public void Indicate_PreviewDownloaded(String str, @Nullable String str2, int i) {
        if (this.mAdapter.containsFile(str2) && i == 0) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str2);
                if (fileWithWebFileID != null) {
                    this.mAdapter.Indicate_PreviewDownloaded(MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr));
                }
            }
        }
    }

    private void init() {
        this.mAdapter = new MMContentSearchFilesAdapter(getContext(), this.mIsOwnerMode);
        this.mAdapter.setParentListView(this);
        setOnScrollListener(this);
        setOnItemClickListener(this);
        setPullDownRefreshEnabled(false);
        if (!isInEditMode()) {
            initRetainedFragment();
        }
        setAdapter(this.mAdapter);
    }

    public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
        this.mAdapter.Indicate_FileDeleted(str, str2, i);
    }

    public void Indicate_FileShared(String str, @Nullable String str2, String str3, String str4, String str5, int i) {
        this.mAdapter.updateZoomFile(str2);
    }

    public void Indicate_FileUnshared(String str, @Nullable String str2, int i) {
        this.mAdapter.updateZoomFile(str2);
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        this.mAdapter.onIndicateInfoUpdatedWithJID(str);
    }

    public boolean Indicate_SearchFileResponse(String str, int i, @Nullable FileFilterSearchResults fileFilterSearchResults) {
        if (!StringUtil.isSameString(str, this.mReqId)) {
            return false;
        }
        this.mReqId = null;
        this.mResultCode = i;
        if (i != 0 || fileFilterSearchResults == null) {
            return false;
        }
        this.mAdapter.addSearchedFiles(fileFilterSearchResults);
        this.mAdapter.notifyDataSetChanged();
        downloadImgPreview(fileFilterSearchResults);
        if (fileFilterSearchResults.getHasMoreMyNotes()) {
            SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
            if (searchMgr != null) {
                this.mReqId = searchMgr.searchMyNotesFileForTimedChat(this.mFilter);
            }
        }
        return !StringUtil.isEmptyOrNull(this.mReqId);
    }

    public boolean Indicate_LocalSearchFileResponse(String str, @Nullable FileFilterSearchResults fileFilterSearchResults) {
        if (!StringUtil.isSameString(str, this.mLocalSearchMsgReqId)) {
            return false;
        }
        this.mLocalSearchMsgReqId = null;
        this.mResultCode = 0;
        this.mAdapter.clearAll();
        if (fileFilterSearchResults != null) {
            this.mAdapter.addLocalSearchedFiles(fileFilterSearchResults);
            this.mAdapter.notifyDataSetChanged();
            downloadImgPreview(fileFilterSearchResults);
        }
        return webSearchContent(this.mSessionId);
    }

    public void Indicate_FileAttachInfoUpdate(String str, String str2, int i) {
        if (i == 0) {
            this.mAdapter.Indicate_FileAttachInfoUpdate(str2);
        }
    }

    public boolean isEmpty() {
        MMContentSearchFilesAdapter mMContentSearchFilesAdapter = this.mAdapter;
        boolean z = true;
        if (mMContentSearchFilesAdapter == null) {
            return true;
        }
        if (mMContentSearchFilesAdapter.getCount() != 0) {
            z = false;
        }
        return z;
    }

    public boolean isLoading() {
        return !StringUtil.isEmptyOrNull(this.mReqId) || !StringUtil.isEmptyOrNull(this.mLocalSearchMsgReqId);
    }

    public boolean isLoadSuccess() {
        return StringUtil.isEmptyOrNull(this.mReqId) && StringUtil.isEmptyOrNull(this.mLocalSearchMsgReqId) && this.mResultCode == 0;
    }

    public int getTotalCount() {
        MMContentSearchFilesAdapter mMContentSearchFilesAdapter = this.mAdapter;
        if (mMContentSearchFilesAdapter == null) {
            return 0;
        }
        return mMContentSearchFilesAdapter.getCount();
    }

    private void downloadImgPreview(@Nullable FileFilterSearchResults fileFilterSearchResults) {
        if (fileFilterSearchResults != null && fileFilterSearchResults.getSearchResultCount() != 0) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                for (FileFilterSearchResult fileId : fileFilterSearchResults.getSearchResultList()) {
                    zoomFileContentMgr.downloadImgPreview(fileId.getFileId());
                }
            }
        }
    }

    public void reset() {
        this.mReqId = null;
        this.mIsOwnerMode = false;
        this.mFilter = null;
        this.mPageNum = 1;
        this.mWebSearched = false;
        this.mLocalSearchMsgReqId = null;
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString("reqId", this.mReqId);
        bundle.putBoolean("ownerMode", this.mIsOwnerMode);
        return bundle;
    }

    public void onRestoreInstanceState(@Nullable Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mReqId = bundle.getString("reqId");
            this.mIsOwnerMode = bundle.getBoolean("ownerMode");
            super.onRestoreInstanceState(bundle.getParcelable("superState"));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public void notifyDataSetChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 0) {
            refreshVcards();
            MMContentSearchFilesAdapter mMContentSearchFilesAdapter = this.mAdapter;
            if (mMContentSearchFilesAdapter != null) {
                mMContentSearchFilesAdapter.clearmLoadedNeedRrefreshFileJids();
            }
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (i > 0 && i2 + i == i3) {
            StringUtil.isEmptyOrNull(this.mReqId);
        }
        if (i == 0 && i2 > 0) {
            refreshVcards();
        }
    }

    private void refreshVcards() {
        MMContentSearchFilesAdapter mMContentSearchFilesAdapter = this.mAdapter;
        if (mMContentSearchFilesAdapter != null) {
            List list = mMContentSearchFilesAdapter.getmLoadedNeedRrefreshFileJids();
            if (!CollectionsUtil.isListEmpty(list)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    zoomMessenger.refreshBuddyVCards(list);
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        MMZoomFile itemAtPosition = this.mAdapter.getItemAtPosition(i - getHeaderViewsCount());
        if (!(itemAtPosition == null || this.mListener == null)) {
            if (itemAtPosition.getFileType() == 7) {
                this.mListener.onZoomFileIntegrationClick(itemAtPosition.getFileIntegrationUrl());
            } else {
                this.mListener.onZoomFileClick(itemAtPosition.getWebID());
            }
        }
    }

    public void onZoomFileShared(String str) {
        OnContentFileOperatorListener onContentFileOperatorListener = this.mListener;
        if (onContentFileOperatorListener != null) {
            onContentFileOperatorListener.onZoomFileShared(str);
        }
    }

    public void onZoomFileCancelTransfer(String str) {
        OnContentFileOperatorListener onContentFileOperatorListener = this.mListener;
        if (onContentFileOperatorListener != null) {
            onContentFileOperatorListener.onZoomFileCancelTransfer(str);
        }
    }

    public void onZoomFileSharerAction(String str, MMZoomShareAction mMZoomShareAction, boolean z, boolean z2) {
        OnContentFileOperatorListener onContentFileOperatorListener = this.mListener;
        if (onContentFileOperatorListener != null) {
            onContentFileOperatorListener.onZoomFileSharerAction(str, mMZoomShareAction, z, z2);
        }
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            this.mRetainedFragment.saveMMContentSearchFilesAdapter(this.mAdapter);
            ((ZMActivity) getContext()).getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
            return;
        }
        MMContentSearchFilesAdapter restoreMMContentSearchFilesAdapter = retainedFragment.restoreMMContentSearchFilesAdapter();
        if (restoreMMContentSearchFilesAdapter != null) {
            this.mAdapter = restoreMMContentSearchFilesAdapter;
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
