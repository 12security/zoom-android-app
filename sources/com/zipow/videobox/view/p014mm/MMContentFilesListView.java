package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.ptapp.IMProtos.LocalStorageTimeInterval;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.FileQueryResult;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper.PendingFileInfo;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.GridDecoration;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.SwipeRefreshPinnedSectionRecyclerView;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.SwipeRefreshPinnedSectionRecyclerView.OnLoadListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentFilesListView */
public class MMContentFilesListView extends SwipeRefreshPinnedSectionRecyclerView implements OnContentFileOperatorListener, OnRecyclerViewListener, OnLoadListener {
    public static final int FILTER_TYPE_IMAGE = 1;
    public static final int FILTER_TYPE_NONE = 0;
    private static final int MAX_COUNT_PER_PAGE = 30;
    private static final int MSG_REFRESH_BUDDY_VCARDS = 1;
    private final String TAG = MMContentFilesListView.class.getSimpleName();
    private MMContentFilesAdapter mAdapter;
    private long mEraseTime = -1;
    private int mFilterType = 0;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                MMContentFilesListView.this.refreshVCard();
                sendEmptyMessageDelayed(1, 2000);
            }
        }
    };
    /* access modifiers changed from: private */
    public MMContentImagesAdapter mImageAdapter;
    private boolean mIsLoadFinish = false;
    private boolean mIsOwnerMode = false;
    private boolean mIsTimeChat = false;
    private ItemDecoration mItemDecoration;
    private OnContentFileOperatorListener mListener;
    @NonNull
    OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            if (i == 0) {
                MMContentFilesListView.this.mHandler.sendEmptyMessage(1);
            } else {
                MMContentFilesListView.this.mHandler.removeMessages(1);
            }
        }
    };
    private View mPanelEmptyView;
    private long mQueryNextTime;
    @Nullable
    private String mReqId;
    @NonNull
    private Runnable mRunnableNotifyDataSetChanged = new Runnable() {
        public void run() {
            MMContentFilesListView.this.callNotifyDataSetChanged();
        }
    };
    @Nullable
    private String mSessionId;
    private View mTxtContentLoading;
    private View mTxtEmptyView;
    private TextView mTxtLoadingError;

    /* renamed from: com.zipow.videobox.view.mm.MMContentFilesListView$MoreContextMenuItem */
    public static class MoreContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_DELETE = 1;
        public static final int ACTION_SHARE = 5;
        public String fileId;

        public MoreContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public void onZoomFileClick(String str) {
    }

    public void onZoomFileClick(String str, List<String> list) {
    }

    public void onZoomFileIntegrationClick(String str) {
    }

    public MMContentFilesListView(Context context) {
        super(context);
        init();
    }

    public MMContentFilesListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mHandler.sendEmptyMessageDelayed(1, 2000);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mHandler.removeMessages(1);
        if (getRecyclerView() != null) {
            getRecyclerView().removeOnScrollListener(this.mOnScrollListener);
        }
    }

    public void filter(int i) {
        if (i != this.mFilterType) {
            this.mFilterType = i;
            this.mReqId = null;
            this.mIsLoadFinish = false;
            switchLayout();
            refresh();
        }
    }

    public void refresh() {
        if (this.mFilterType == 0) {
            this.mAdapter.setFooterState(false);
            loadFiles(true, true);
            return;
        }
        this.mImageAdapter.setFooterState(false);
        loadImages(true, true);
    }

    public void loadMore() {
        if (!this.mIsLoadFinish && StringUtil.isEmptyOrNull(this.mReqId)) {
            if (this.mFilterType == 0) {
                loadFiles(true, false);
            } else {
                loadImages(true, false);
            }
        }
    }

    private void switchLayout() {
        if (this.mFilterType == 0) {
            this.mAdapter = new MMContentFilesAdapter(getContext());
            this.mAdapter.setEraseTime(this.mEraseTime, this.mIsTimeChat);
            getRecyclerView().setAdapter(this.mAdapter);
            getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
            if (this.mItemDecoration != null) {
                getRecyclerView().removeItemDecoration(this.mItemDecoration);
            }
            this.mAdapter.setParentListView(this);
            this.mAdapter.setOnRecyclerViewListener(this);
            return;
        }
        this.mImageAdapter = new MMContentImagesAdapter(getContext(), this.mIsOwnerMode);
        this.mImageAdapter.setEraseTime(this.mEraseTime, this.mIsTimeChat);
        getRecyclerView().setAdapter(this.mImageAdapter);
        final int integer = getResources().getInteger(C4558R.integer.zm_content_max_images_each_line);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), integer);
        gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int i) {
                if (MMContentFilesListView.this.mImageAdapter == null || (!MMContentFilesListView.this.mImageAdapter.isPinnedSection(i) && !MMContentFilesListView.this.mImageAdapter.isFooter(i) && !MMContentFilesListView.this.mImageAdapter.isTimeChat(i))) {
                    return 1;
                }
                return integer;
            }
        });
        getRecyclerView().setLayoutManager(gridLayoutManager);
        if (this.mItemDecoration == null) {
            this.mItemDecoration = new GridDecoration(10, 10);
        }
        getRecyclerView().addItemDecoration(this.mItemDecoration);
        this.mImageAdapter.setOnRecyclerViewListener(this);
    }

    public void setOnContentFileOperatorListener(OnContentFileOperatorListener onContentFileOperatorListener) {
        this.mListener = onContentFileOperatorListener;
    }

    public void setSessionId(String str) {
        this.mSessionId = str;
        if (this.mFilterType == 0) {
            this.mAdapter.setIsGroupOwner(isGroupOwner());
            this.mAdapter.setSessionId(str);
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        this.mImageAdapter.setIsGroupOwner(isGroupOwner());
        this.mImageAdapter.setSessionId(str);
        this.mImageAdapter.notifyDataSetChanged();
    }

    private void init() {
        switchLayout();
        setOnLoadListener(this);
        if (getRecyclerView() != null) {
            getRecyclerView().removeOnScrollListener(this.mOnScrollListener);
            getRecyclerView().addOnScrollListener(this.mOnScrollListener);
        }
    }

    /* access modifiers changed from: private */
    public void refreshVCard() {
        if (this.mAdapter != null && this.mFilterType == 0) {
            int firstVisiblePosition = getFirstVisiblePosition();
            int i = getlastVisiblePosition();
            if (firstVisiblePosition >= 0 && i >= 0 && i >= firstVisiblePosition) {
                ArrayList arrayList = new ArrayList();
                while (firstVisiblePosition <= i) {
                    MMZoomFile itemAtPosition = this.mAdapter.getItemAtPosition(firstVisiblePosition);
                    if (itemAtPosition != null) {
                        String ownerJid = itemAtPosition.getOwnerJid();
                        if (!TextUtils.isEmpty(ownerJid) && TextUtils.isEmpty(itemAtPosition.getOwnerName())) {
                            arrayList.add(ownerJid);
                        }
                    }
                    firstVisiblePosition++;
                }
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    zoomMessenger.refreshBuddyVCards(arrayList);
                }
            }
        }
    }

    public void setupEmptyView(@Nullable View view) {
        if (view != null) {
            this.mPanelEmptyView = view;
            this.mTxtContentLoading = view.findViewById(C4558R.C4560id.txtContentLoading);
            this.mTxtEmptyView = view.findViewById(C4558R.C4560id.txtEmptyView);
            this.mTxtLoadingError = (TextView) view.findViewById(C4558R.C4560id.txtLoadingError);
        }
    }

    public void Indicate_FileShared(String str, @Nullable String str2, String str3, String str4, String str5, int i) {
        if (i == 0) {
            if (this.mFilterType == 0) {
                this.mAdapter.updateOrAddContentFile(str2);
            } else {
                this.mImageAdapter.updateOrAddContentFile(str2);
            }
            notifyDataSetChanged(false);
            updateEmptyViewStatus(false, 0);
        }
    }

    public void Indicate_FileUnshared(String str, @Nullable String str2, int i) {
        if (i == 0) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str2);
                if (fileWithWebFileID != null) {
                    List<MMZoomShareAction> shareAction = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr).getShareAction();
                    if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
                        boolean z = false;
                        for (MMZoomShareAction sharee : shareAction) {
                            if (StringUtil.isSameString(sharee.getSharee(), this.mSessionId)) {
                                z = true;
                            }
                        }
                        if (z) {
                            if (this.mFilterType == 0) {
                                this.mAdapter.updateOrAddContentFile(str2);
                            } else {
                                this.mImageAdapter.updateOrAddContentFile(str2);
                            }
                        } else if (this.mFilterType == 0) {
                            this.mAdapter.deleteContentFile(str2);
                        } else {
                            this.mImageAdapter.deleteContentFile(str2);
                        }
                    } else if (this.mFilterType == 0) {
                        this.mAdapter.updateOrAddContentFile(str2);
                    } else {
                        this.mImageAdapter.updateOrAddContentFile(str2);
                    }
                    notifyDataSetChanged(true);
                    updateEmptyViewStatus(false, 0);
                }
            }
        }
    }

    public void Indicate_FileStatusUpdated(@Nullable String str) {
        if (this.mFilterType == 0) {
            this.mAdapter.updateContentFile(str);
        } else {
            this.mImageAdapter.updateContentFile(str);
        }
        notifyDataSetChanged(false);
    }

    public void Indicate_FileActionStatus(int i, @Nullable String str, String str2, String str3, String str4, String str5) {
        if (this.mFilterType == 0) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID == null) {
                    if (this.mAdapter.deleteContentFile(str) != null) {
                        notifyDataSetChanged(false);
                        updateEmptyViewStatus(false, 0);
                    }
                    return;
                }
                MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                if (i == 1) {
                    this.mAdapter.deleteContentFile(str);
                } else if (i == 2) {
                    List<MMZoomShareAction> shareAction = initWithZoomFile.getShareAction();
                    if (StringUtil.isEmptyOrNull(this.mSessionId)) {
                        this.mAdapter.updateOrAddContentFile(str);
                    } else {
                        boolean z = false;
                        for (MMZoomShareAction sharee : shareAction) {
                            if (StringUtil.isSameString(sharee.getSharee(), this.mSessionId)) {
                                z = true;
                            }
                        }
                        if (z) {
                            this.mAdapter.updateOrAddContentFile(str);
                        } else {
                            this.mAdapter.deleteContentFile(str);
                        }
                    }
                } else {
                    this.mAdapter.updateContentFile(str);
                }
                notifyDataSetChanged(false);
                updateEmptyViewStatus(false, 0);
            }
        }
    }

    public void Indicate_NewFileSharedByOthers(@Nullable String str) {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            zoomFileContentMgr.downloadImgPreview(str);
            if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID != null) {
                    List<MMZoomShareAction> shareAction = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr).getShareAction();
                    if (shareAction != null && shareAction.size() > 0) {
                        boolean z = false;
                        for (MMZoomShareAction sharee : shareAction) {
                            if (StringUtil.isSameString(sharee.getSharee(), this.mSessionId)) {
                                z = true;
                            }
                        }
                        if (z) {
                            if (this.mFilterType == 0) {
                                this.mAdapter.updateOrAddContentFile(str);
                            } else {
                                this.mImageAdapter.updateOrAddContentFile(str);
                            }
                        }
                    }
                } else {
                    return;
                }
            } else if (!this.mIsOwnerMode) {
                if (this.mFilterType == 0) {
                    this.mAdapter.updateOrAddContentFile(str);
                } else {
                    this.mImageAdapter.updateOrAddContentFile(str);
                }
            }
            notifyDataSetChanged(false);
            updateEmptyViewStatus(false, 0);
        }
    }

    public void Indicate_NewPersonalFile(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(this.mSessionId)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                zoomFileContentMgr.downloadImgPreview(str);
                if (this.mFilterType == 0) {
                    this.mAdapter.updateOrAddContentFile(str);
                } else {
                    this.mImageAdapter.updateOrAddContentFile(str);
                }
                notifyDataSetChanged(true);
                updateEmptyViewStatus(false, 0);
            }
        }
    }

    public void Indicate_FileDownloaded(String str, @Nullable String str2, int i) {
        if (this.mFilterType == 0) {
            if (this.mAdapter.findFileByWebId(str2) != null) {
                this.mAdapter.updateContentFile(str2);
            }
        } else if (this.mImageAdapter.containsFile(str2)) {
            this.mImageAdapter.updateContentFile(str2);
        }
        notifyDataSetChanged(true);
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        if (!TextUtils.isEmpty(str) && this.mFilterType == 0 && this.mAdapter.updateContentFileByJid(str)) {
            notifyDataSetChanged(true);
        }
    }

    public void Indicate_PreviewDownloaded(String str, @Nullable String str2, int i) {
        if (this.mFilterType == 0) {
            if (this.mAdapter.findFileByWebId(str2) != null && i == 0) {
                this.mAdapter.updateOrAddContentFile(str2);
                notifyDataSetChanged(true);
            }
        } else if (this.mImageAdapter.containsFile(str2) && i == 0) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str2);
                if (fileWithWebFileID != null) {
                    this.mImageAdapter.Indicate_PreviewDownloaded(MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr));
                }
            }
        }
    }

    public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
        if (this.mFilterType == 0) {
            this.mAdapter.deleteContentFile(str2);
        } else {
            this.mImageAdapter.Indicate_FileDeleted(str, str2, i);
        }
        updateEmptyViewStatus(false, 0);
    }

    public void Indicate_UploadToMyFiles_Sent(@Nullable String str, @Nullable String str2, int i) {
        if (this.mFilterType == 0) {
            this.mAdapter.deleteContentFile(str);
            if (i == 0 && !StringUtil.isEmptyOrNull(str2)) {
                this.mAdapter.updateOrAddContentFile(str2);
            }
        } else {
            this.mImageAdapter.deleteContentFile(str);
            if (i == 0 && !StringUtil.isEmptyOrNull(str2)) {
                this.mImageAdapter.updateOrAddContentFile(str2);
            }
        }
        notifyDataSetChanged(true);
        updateEmptyViewStatus(false, 0);
    }

    public void Indicate_FileAttachInfoUpdate(String str, @Nullable String str2, int i) {
        if (i == 0) {
            this.mAdapter.updateContentFile(str2);
        }
    }

    public void FT_UploadToMyList_OnProgress(@Nullable String str, int i, int i2, int i3) {
        onUploadToMyListOnProgress(str, i, i2, i3);
    }

    public void setMode(boolean z) {
        this.mIsOwnerMode = z;
        if (this.mFilterType == 0) {
            this.mAdapter.setMode(z);
        } else {
            this.mImageAdapter.setMode(z);
        }
    }

    public void setEraseTime(long j, boolean z) {
        this.mIsTimeChat = z;
        this.mEraseTime = j;
        if (this.mFilterType == 0) {
            this.mAdapter.setEraseTime(j, z);
        } else {
            this.mImageAdapter.setEraseTime(j, z);
        }
        notifyDataSetChanged(true);
        updateEmptyViewStatus(false, 0);
    }

    public void notifyDataSetChanged(boolean z) {
        if (z) {
            this.mHandler.removeCallbacks(this.mRunnableNotifyDataSetChanged);
            callNotifyDataSetChanged();
            return;
        }
        this.mHandler.removeCallbacks(this.mRunnableNotifyDataSetChanged);
        this.mHandler.postDelayed(this.mRunnableNotifyDataSetChanged, 500);
    }

    public void callNotifyDataSetChanged() {
        if (this.mFilterType == 0) {
            this.mAdapter.notifyDataSetChanged();
        } else {
            this.mImageAdapter.notifyDataSetChanged();
        }
    }

    private void insertTimeChatMsg() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            LocalStorageTimeInterval localStorageTimeInterval = zoomMessenger.getLocalStorageTimeInterval();
            if (localStorageTimeInterval != null) {
                setEraseTime(localStorageTimeInterval.getEraseTime(), true);
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString("reqId", this.mReqId);
        bundle.putString("sessionid", this.mSessionId);
        bundle.putBoolean("isOwnerMode", this.mIsOwnerMode);
        return bundle;
    }

    public void onRestoreInstanceState(@Nullable Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mReqId = bundle.getString("reqId");
            this.mSessionId = bundle.getString("sessionid");
            this.mIsOwnerMode = bundle.getBoolean("isOwnerMode", false);
            super.onRestoreInstanceState(bundle.getParcelable("superState"));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public void onUploadFile(@Nullable String str, String str2) {
        MMZoomFile mMZoomFile = new MMZoomFile();
        mMZoomFile.setWebID(str);
        mMZoomFile.setReqId(str);
        mMZoomFile.setFileName(str2);
        this.mAdapter.updateOrAddContentFile(mMZoomFile);
        onUploadToMyListOnProgress(str, 0, 0, 0);
        notifyDataSetChanged(true);
    }

    public void onDownloadByFileIDOnProgress(String str, @Nullable String str2, int i, int i2, int i3) {
        if (this.mFilterType == 0) {
            MMZoomFile findFileByWebId = this.mAdapter.findFileByWebId(str2);
            if (findFileByWebId != null) {
                findFileByWebId.setPending(true);
                findFileByWebId.setRatio(i);
                findFileByWebId.setReqId(str);
                findFileByWebId.setFileDownloading(true);
                findFileByWebId.setCompleteSize(i2);
                findFileByWebId.setBitPerSecond(i3);
            } else {
                return;
            }
        } else {
            this.mImageAdapter.onDownloadByFileIDOnProgress(str, str2, i, i2, i3);
        }
        notifyDataSetChanged(true);
    }

    public void onUploadToMyListOnProgress(@Nullable String str, int i, int i2, int i3) {
        MMZoomFile findFileByWebId = this.mAdapter.findFileByWebId(str);
        if (findFileByWebId != null) {
            findFileByWebId.setPending(true);
            findFileByWebId.setRatio(i);
            findFileByWebId.setCompleteSize(i2);
            findFileByWebId.setBitPerSecond(i3);
            notifyDataSetChanged(true);
        }
    }

    public void loadData(boolean z) {
        if (!isInEditMode()) {
            if (this.mFilterType == 0) {
                loadFiles(z, false);
            } else {
                loadImages(z, false);
            }
        }
    }

    public void addUploadFile(String str, String str2, int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                MMZoomFile mMZoomFile = new MMZoomFile();
                mMZoomFile.setWebID(str);
                mMZoomFile.setReqId(str);
                mMZoomFile.setFileName(str2);
                mMZoomFile.setTimeStamp(System.currentTimeMillis());
                mMZoomFile.setPending(true);
                mMZoomFile.setFileSize(i);
                mMZoomFile.setOwnerJid(myself.getJid());
                mMZoomFile.setOwnerName(myself.getScreenName());
                this.mAdapter.updateOrAddContentFile(mMZoomFile);
                notifyDataSetChanged(true);
                updateEmptyViewStatus(false, 0);
            }
        }
    }

    public void Indicate_QueryMyFilesResponse(String str, int i, @Nullable List<String> list, long j, long j2) {
        if (StringUtil.isSameString(this.mReqId, str)) {
            if (this.mFilterType == 0) {
                updateFiles(list, false);
                this.mAdapter.setFooterState(false);
                notifyDataSetChanged(true);
            } else {
                updateImages(list, false);
                this.mImageAdapter.setFooterState(false);
            }
            if (list != null && list.size() < 30) {
                insertTimeChatMsg();
            }
            updateEmptyViewStatus(false, i);
            setRefreshing(false);
            this.mReqId = null;
            this.mQueryNextTime = j2;
        }
    }

    public void Indicate_QueryFilesSharedWithMeResponse(String str, int i, List<String> list, long j, long j2) {
        if (StringUtil.isSameString(this.mReqId, str)) {
            if (this.mFilterType == 0) {
                updateFiles(list, false);
                this.mAdapter.setFooterState(false);
                notifyDataSetChanged(true);
            } else {
                updateImages(list, false);
                this.mImageAdapter.setFooterState(false);
            }
            updateEmptyViewStatus(false, i);
            setRefreshing(false);
            this.mReqId = null;
            this.mQueryNextTime = j;
        }
    }

    public void Indicate_QueryAllFilesResponse(String str, int i, @Nullable List<String> list, long j, long j2) {
        if (StringUtil.isSameString(this.mReqId, str)) {
            if (this.mFilterType == 0) {
                updateFiles(list, false);
                this.mAdapter.setFooterState(false);
                notifyDataSetChanged(true);
            } else {
                updateImages(list, false);
                this.mImageAdapter.setFooterState(false);
            }
            if (list != null && list.size() < 30) {
                insertTimeChatMsg();
            }
            updateEmptyViewStatus(false, i);
            setRefreshing(false);
            this.mReqId = null;
            this.mQueryNextTime = j2;
        }
    }

    public void Indicate_QuerySessionFilesResponse(String str, String str2, int i, @Nullable List<String> list, long j, long j2) {
        if (StringUtil.isSameString(this.mReqId, str)) {
            if (this.mFilterType == 0) {
                updateFiles(list, false);
                this.mAdapter.setFooterState(false);
                notifyDataSetChanged(true);
            } else {
                updateImages(list, false);
                this.mImageAdapter.setFooterState(false);
            }
            if (list != null && list.size() < 30) {
                insertTimeChatMsg();
            }
            updateEmptyViewStatus(false, i);
            setRefreshing(false);
            this.mReqId = null;
            this.mQueryNextTime = j2;
        }
    }

    public void Indicate_RenameFileResponse(int i, String str, @Nullable String str2, String str3) {
        if (i == 0) {
            if (this.mFilterType == 0) {
                this.mAdapter.updateContentFile(str2);
            } else {
                this.mImageAdapter.updateContentFile(str2);
            }
            notifyDataSetChanged(true);
        }
    }

    private void loadFiles(boolean z, boolean z2) {
        FileQueryResult fileQueryResult;
        long j = this.mQueryNextTime;
        if (j == 0) {
            j = this.mAdapter.getLastTimeStamp();
        }
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i == 0 || z) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        boolean z3 = i == 0;
                        if (z2 || i == 0) {
                            j = CmmTime.getMMNow();
                            this.mIsLoadFinish = false;
                        }
                        String jid = myself.getJid();
                        if (!StringUtil.isEmptyOrNull(jid)) {
                            if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
                                fileQueryResult = zoomFileContentMgr.queryFilesForSession(this.mSessionId, j, 30);
                            } else if (this.mIsOwnerMode) {
                                fileQueryResult = zoomFileContentMgr.queryOwnedFiles(jid, j, 30);
                            } else {
                                fileQueryResult = zoomFileContentMgr.queryAllFiles(j, 30);
                            }
                            if (fileQueryResult != null) {
                                this.mQueryNextTime = 0;
                                this.mReqId = fileQueryResult.getReqid();
                                List fileIdsList = fileQueryResult.getFileIdsList();
                                if (fileQueryResult.getWebSearchTriggered()) {
                                    this.mAdapter.setFooterState(!z3 && !z2);
                                    setRefreshing(fileIdsList.size() > 0 && z3);
                                } else {
                                    setRefreshing(false);
                                }
                                if (!(fileIdsList == null || fileIdsList.size() == 0)) {
                                    updateFiles(fileIdsList, z3 || z2);
                                }
                                addUploadPendingFiles();
                                notifyDataSetChanged(true);
                                if (fileQueryResult.getWebSearchTriggered()) {
                                    updateEmptyViewStatus(true, 0);
                                } else if (StringUtil.isEmptyOrNull(this.mReqId)) {
                                    updateEmptyViewStatus(false, 0);
                                } else {
                                    this.mPanelEmptyView.setVisibility(8);
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.mPanelEmptyView.setVisibility(8);
    }

    private void updateEmptyViewStatus(boolean z, int i) {
        if (this.mPanelEmptyView != null && this.mTxtContentLoading != null && this.mTxtEmptyView != null && this.mTxtLoadingError != null && getVisibility() == 0) {
            int i2 = 0;
            this.mPanelEmptyView.setVisibility(getCount() == 0 ? 0 : 8);
            if (z) {
                this.mTxtContentLoading.setVisibility(0);
                this.mTxtEmptyView.setVisibility(8);
                this.mTxtLoadingError.setVisibility(8);
            } else {
                this.mTxtContentLoading.setVisibility(8);
                this.mTxtEmptyView.setVisibility(i == 0 ? 0 : 8);
                TextView textView = this.mTxtLoadingError;
                if (i == 0) {
                    i2 = 8;
                }
                textView.setVisibility(i2);
            }
        }
    }

    private void addUploadPendingFiles() {
        List<PendingFileInfo> uploadPendingFileInfos = PendingFileDataHelper.getInstance().getUploadPendingFileInfos();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                for (PendingFileInfo pendingFileInfo : uploadPendingFileInfos) {
                    MMZoomFile mMZoomFile = new MMZoomFile();
                    mMZoomFile.setBitPerSecond(pendingFileInfo.bitPerSecond);
                    mMZoomFile.setPending(true);
                    mMZoomFile.setCompleteSize(pendingFileInfo.completeSize);
                    mMZoomFile.setRatio(pendingFileInfo.ratio);
                    mMZoomFile.setWebID(pendingFileInfo.reqId);
                    mMZoomFile.setReqId(pendingFileInfo.reqId);
                    mMZoomFile.setFileName(pendingFileInfo.name);
                    mMZoomFile.setTimeStamp(pendingFileInfo.timestamp);
                    mMZoomFile.setFileSize(pendingFileInfo.totalSize);
                    mMZoomFile.setOwnerJid(myself.getJid());
                    mMZoomFile.setOwnerName(myself.getScreenName());
                    this.mAdapter.updateOrAddContentFile(mMZoomFile);
                }
                notifyDataSetChanged(true);
            }
        }
    }

    private boolean isGroupOwner() {
        if (StringUtil.isEmptyOrNull(this.mSessionId)) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(this.mSessionId);
        if (groupById == null || !groupById.isRoom() || !groupById.isGroupOperatorable()) {
            return false;
        }
        return true;
    }

    private void updateFiles(@Nullable List<String> list, boolean z) {
        this.mAdapter.setDataInited(true);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.getSessionById(this.mSessionId);
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                if (list == null || list.size() == 0) {
                    this.mIsLoadFinish = true;
                    return;
                }
                ArrayList arrayList = new ArrayList();
                if (list.size() > 0) {
                    for (String str : list) {
                        ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                        if (fileWithWebFileID != null) {
                            MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                            if (!initWithZoomFile.isDeletePending() && !StringUtil.isEmptyOrNull(initWithZoomFile.getFileName()) && initWithZoomFile.getLastedShareTime(this.mSessionId) > 0 && initWithZoomFile.getLastedShareTime(this.mSessionId) > this.mAdapter.getEraseTime()) {
                                int fileType = initWithZoomFile.getFileType();
                                if ((fileType == 1 || fileType == 1 || fileType == 4) && StringUtil.isEmptyOrNull(initWithZoomFile.getPicturePreviewPath())) {
                                    zoomFileContentMgr.downloadImgPreview(initWithZoomFile.getWebID());
                                }
                                arrayList.add(initWithZoomFile);
                                if ((!StringUtil.isEmptyOrNull(this.mSessionId) || !this.mIsOwnerMode) && (initWithZoomFile.getShareAction() == null || initWithZoomFile.getShareAction().size() == 0)) {
                                    zoomFileContentMgr.syncFileInfoByFileID(str);
                                }
                            }
                        }
                    }
                }
                if (z) {
                    this.mAdapter.clearAll();
                }
                this.mAdapter.addContenFiles(arrayList);
            }
        }
    }

    public void endFileTransfer(@Nullable String str) {
        this.mAdapter.updateOrAddContentFile(str);
        notifyDataSetChanged(true);
        updateEmptyViewStatus(false, 0);
    }

    public int getCount() {
        int i = 0;
        if (this.mFilterType == 0) {
            MMContentFilesAdapter mMContentFilesAdapter = this.mAdapter;
            if (mMContentFilesAdapter != null) {
                i = mMContentFilesAdapter.getItemCount();
            }
            return i;
        }
        MMContentImagesAdapter mMContentImagesAdapter = this.mImageAdapter;
        if (mMContentImagesAdapter != null) {
            i = mMContentImagesAdapter.getItemCount();
        }
        return i;
    }

    public void onItemClick(View view, int i) {
        if (this.mFilterType == 1) {
            DisplayItem item = this.mImageAdapter.getItem(i);
            if (!(item == null || item.mImages == null)) {
                ArrayList arrayList = new ArrayList();
                for (DisplayItem displayItem : this.mImageAdapter.getData()) {
                    if (!(displayItem == null || displayItem.mImages == null)) {
                        arrayList.add(displayItem.mImages.getWebID());
                    }
                }
                OnContentFileOperatorListener onContentFileOperatorListener = this.mListener;
                if (onContentFileOperatorListener != null) {
                    onContentFileOperatorListener.onZoomFileClick(item.mImages.getWebID(), arrayList);
                }
            }
        } else {
            MMContentFilesAdapter mMContentFilesAdapter = this.mAdapter;
            MMZoomFile itemAtPosition = mMContentFilesAdapter.getItemAtPosition(i - mMContentFilesAdapter.getHeaderViewsCount());
            if (itemAtPosition != null) {
                if (!itemAtPosition.isPending() || !PendingFileDataHelper.getInstance().isFileUploadNow(itemAtPosition.getReqId())) {
                    MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                    if (zoomFileContentMgr != null) {
                        ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(itemAtPosition.getWebID());
                        if (fileWithWebFileID == null) {
                            this.mAdapter.deleteContentFile(itemAtPosition.getWebID());
                            updateEmptyViewStatus(false, 0);
                            return;
                        }
                        zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                        if (this.mListener != null) {
                            if (itemAtPosition.getFileType() == 7) {
                                this.mListener.onZoomFileIntegrationClick(itemAtPosition.getFileIntegrationUrl());
                            } else {
                                this.mListener.onZoomFileClick(itemAtPosition.getWebID());
                            }
                        }
                    }
                }
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

    public boolean onItemLongClick(View view, int i) {
        MMZoomFile mMZoomFile;
        if (this.mFilterType == 1) {
            mMZoomFile = this.mImageAdapter.getItemAtPosition(i);
        } else {
            MMContentFilesAdapter mMContentFilesAdapter = this.mAdapter;
            mMZoomFile = mMContentFilesAdapter.getItemAtPosition(i - mMContentFilesAdapter.getHeaderViewsCount());
        }
        return dealWithItemLongClick(mMZoomFile, this.mFilterType);
    }

    private boolean dealWithItemLongClick(@Nullable MMZoomFile mMZoomFile, int i) {
        if (mMZoomFile == null) {
            return false;
        }
        if (mMZoomFile.isPending() && PendingFileDataHelper.getInstance().isFileUploadNow(mMZoomFile.getReqId())) {
            return false;
        }
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr == null) {
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
        ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(mMZoomFile.getWebID());
        if (fileWithWebFileID == null) {
            if (i == 0) {
                this.mAdapter.deleteContentFile(mMZoomFile.getWebID());
            } else {
                this.mImageAdapter.Indicate_FileDeleted("", mMZoomFile.getWebID(), 0);
            }
            return false;
        }
        zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
        if (PTApp.getInstance().isFileTransferDisabled()) {
            return false;
        }
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getContext(), false);
        ArrayList arrayList = new ArrayList();
        if (!StringUtil.isEmptyOrNull(mMZoomFile.getWebID())) {
            MoreContextMenuItem moreContextMenuItem = new MoreContextMenuItem(getContext().getString(C4558R.string.zm_btn_share), 5);
            moreContextMenuItem.fileId = mMZoomFile.getWebID();
            arrayList.add(moreContextMenuItem);
        }
        if (!StringUtil.isEmptyOrNull(mMZoomFile.getWebID()) && StringUtil.isSameString(myself.getJid(), mMZoomFile.getOwnerJid())) {
            MoreContextMenuItem moreContextMenuItem2 = new MoreContextMenuItem(getContext().getString(C4558R.string.zm_btn_delete), 1);
            moreContextMenuItem2.fileId = mMZoomFile.getWebID();
            arrayList.add(moreContextMenuItem2);
        }
        if (arrayList.size() == 0) {
            return false;
        }
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        ZMAlertDialog create = new Builder(getContext()).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMContentFilesListView.this.onSelectContextMenuItem((MoreContextMenuItem) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
        return true;
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(MoreContextMenuItem moreContextMenuItem) {
        String str = moreContextMenuItem.fileId;
        if (!StringUtil.isEmptyOrNull(str)) {
            int action = moreContextMenuItem.getAction();
            if (action == 1) {
                onSelectContextMenuItemDeleteFile(str);
            } else if (action == 5) {
                onZoomFileShared(str);
            }
        }
    }

    private void onSelectContextMenuItemDeleteFile(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood()) {
                showConnectionError();
            } else if (!StringUtil.isEmptyOrNull(str)) {
                MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                if (zoomFileContentMgr != null) {
                    ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                    if (fileWithWebFileID != null) {
                        final MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                        String shrinkFileName = FileUtils.shrinkFileName(initWithZoomFile.getFileName(), 30);
                        String string = getContext().getString(C4558R.string.zm_msg_delete_file_confirm_89710);
                        if (TextUtils.isEmpty(this.mSessionId)) {
                            new Builder(getContext()).setTitle((CharSequence) string).setMessage(C4558R.string.zm_msg_delete_file_warning_89710).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).setPositiveButton(C4558R.string.zm_btn_delete, (OnClickListener) new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MMContentFilesListView.this.deleteFile(initWithZoomFile);
                                }
                            }).create().show();
                        } else {
                            deleteFile(initWithZoomFile);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void deleteFile(@Nullable MMZoomFile mMZoomFile) {
        if (mMZoomFile != null) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                String deleteFile = zoomFileContentMgr.deleteFile(mMZoomFile, this.mSessionId);
                if (!StringUtil.isEmptyOrNull(deleteFile)) {
                    Indicate_FileDeleted(deleteFile, mMZoomFile.getWebID(), 0);
                } else {
                    Context context = getContext();
                    if (context instanceof FragmentActivity) {
                        ErrorMsgDialog.newInstance(getResources().getString(C4558R.string.zm_alert_unshare_file_failed), -1).show(((FragmentActivity) context).getSupportFragmentManager(), ErrorMsgDialog.class.getName());
                    }
                }
            }
        }
    }

    private void showConnectionError() {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, C4558R.string.zm_msg_disconnected_try_again, 0).show();
        }
    }

    public void Indicate_FileDeletedByOthers(@Nullable String str) {
        this.mImageAdapter.Indicate_FileDeletedByOthers(str);
    }

    private void loadImages(boolean z, boolean z2) {
        FileQueryResult fileQueryResult;
        long j = this.mQueryNextTime;
        if (j == 0) {
            j = this.mImageAdapter.getLastTimeStamp();
        }
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i == 0 || z) {
            boolean z3 = i == 0;
            if (z2 || i == 0) {
                j = CmmTime.getMMNow();
                this.mIsLoadFinish = false;
            }
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        String jid = myself.getJid();
                        if (!StringUtil.isEmptyOrNull(jid)) {
                            if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
                                fileQueryResult = zoomFileContentMgr.queryImagesForSession(this.mSessionId, j, 30);
                            } else if (this.mIsOwnerMode) {
                                fileQueryResult = zoomFileContentMgr.queryOwnedImageFiles(jid, j, 30);
                            } else {
                                fileQueryResult = zoomFileContentMgr.queryAllImages(j, 30);
                            }
                            if (fileQueryResult != null) {
                                this.mQueryNextTime = 0;
                                this.mReqId = fileQueryResult.getReqid();
                                List fileIdsList = fileQueryResult.getFileIdsList();
                                if (fileQueryResult.getWebSearchTriggered()) {
                                    this.mImageAdapter.setFooterState(!z3 && !z2);
                                    setRefreshing(fileIdsList.size() > 0 && z3);
                                } else {
                                    setRefreshing(false);
                                }
                                updateImages(fileIdsList, z3 || z2);
                                notifyDataSetChanged(true);
                                if (fileQueryResult.getWebSearchTriggered()) {
                                    updateEmptyViewStatus(true, 0);
                                } else if (StringUtil.isEmptyOrNull(this.mReqId)) {
                                    updateEmptyViewStatus(false, 0);
                                } else {
                                    this.mPanelEmptyView.setVisibility(8);
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.mPanelEmptyView.setVisibility(8);
    }

    private void updateImages(@Nullable List<String> list, boolean z) {
        if (list == null || list.size() == 0) {
            this.mIsLoadFinish = true;
            return;
        }
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            ArrayList arrayList = new ArrayList();
            if (list.size() > 0) {
                for (String str : list) {
                    ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                    if (fileWithWebFileID != null) {
                        MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                        if (!initWithZoomFile.isDeletePending() && !initWithZoomFile.isIntegrationType() && !StringUtil.isEmptyOrNull(initWithZoomFile.getFileName()) && initWithZoomFile.getLastedShareTime(this.mSessionId) > 0 && initWithZoomFile.getLastedShareTime(this.mSessionId) > this.mImageAdapter.getEraseTime()) {
                            int fileType = initWithZoomFile.getFileType();
                            if (ZmPtUtils.isImageFile(fileType) && fileType != 5 && StringUtil.isEmptyOrNull(initWithZoomFile.getPicturePreviewPath())) {
                                zoomFileContentMgr.downloadImgPreview(initWithZoomFile.getWebID());
                            }
                            arrayList.add(initWithZoomFile);
                            if ((!StringUtil.isEmptyOrNull(this.mSessionId) || !this.mIsOwnerMode) && (initWithZoomFile.getShareAction() == null || initWithZoomFile.getShareAction().size() == 0)) {
                                zoomFileContentMgr.syncFileInfoByFileID(str);
                            }
                        }
                    }
                }
            }
            if (z) {
                this.mImageAdapter.clearAll();
            }
            this.mImageAdapter.addContentImages(arrayList);
            this.mImageAdapter.setFooterState(!z && list.size() > 0);
            notifyDataSetChanged(true);
        }
    }
}
