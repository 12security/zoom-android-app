package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.FileFilterSearchResult;
import com.zipow.videobox.ptapp.IMProtos.FileFilterSearchResults;
import com.zipow.videobox.ptapp.IMProtos.FileMatchInfo;
import com.zipow.videobox.ptapp.IMProtos.HighlightPositionItem;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.view.p014mm.MMZoomFile.HighlightPosition;
import com.zipow.videobox.view.p014mm.MMZoomFileView.OnShowAllShareActionListener;
import com.zipow.videobox.view.p014mm.contentfile.ContentFileChatListFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.MMContentSearchFilesAdapter */
public class MMContentSearchFilesAdapter extends BaseAdapter implements OnShowAllShareActionListener {
    @NonNull
    private List<DisplayItem> mContentFiles = new ArrayList();
    private Context mContext;
    private boolean mIsPersonalMode = false;
    @NonNull
    private List<String> mLoadedNeedRrefreshFileJids = new ArrayList();
    private MMContentSearchFilesListView mParentListView;
    @NonNull
    private Set<String> mShowAllSharesFileIds = new HashSet();

    /* renamed from: com.zipow.videobox.view.mm.MMContentSearchFilesAdapter$ContentFileComparator */
    static class ContentFileComparator implements Comparator<DisplayItem> {
        private boolean mIsPersonalMode;

        public ContentFileComparator(boolean z) {
            this.mIsPersonalMode = z;
        }

        public int compare(@NonNull DisplayItem displayItem, @NonNull DisplayItem displayItem2) {
            long j;
            if (this.mIsPersonalMode) {
                j = displayItem.mMMZoomFile.getTimeStamp() - displayItem2.mMMZoomFile.getTimeStamp();
            } else {
                j = displayItem.mMMZoomFile.getLastedShareTime(null) - displayItem2.mMMZoomFile.getLastedShareTime(null);
            }
            int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
            if (i > 0) {
                return -1;
            }
            return i == 0 ? 0 : 1;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMContentSearchFilesAdapter$DisplayItem */
    public static class DisplayItem {
        String mLabel;
        @NonNull
        public MMZoomFile mMMZoomFile;

        DisplayItem(@NonNull MMZoomFile mMZoomFile) {
            this.mMMZoomFile = mMZoomFile;
        }

        @Nullable
        public static DisplayItem initWithFileFilterSearchResult(@Nullable FileFilterSearchResult fileFilterSearchResult) {
            if (fileFilterSearchResult == null || StringUtil.isEmptyOrNull(fileFilterSearchResult.getFileId())) {
                return null;
            }
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr == null) {
                return null;
            }
            ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(fileFilterSearchResult.getFileId());
            if (fileWithWebFileID == null) {
                return null;
            }
            DisplayItem displayItem = new DisplayItem(MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr));
            ArrayList arrayList = new ArrayList();
            for (FileMatchInfo fileMatchInfo : fileFilterSearchResult.getMatchInfosList()) {
                MMZoomFile.FileMatchInfo fileMatchInfo2 = new MMZoomFile.FileMatchInfo();
                fileMatchInfo2.mContent = fileMatchInfo.getContent();
                fileMatchInfo2.mType = fileMatchInfo.getType();
                for (HighlightPositionItem highlightPositionItem : fileMatchInfo.getMatchInfosList()) {
                    HighlightPosition highlightPosition = new HighlightPosition();
                    if (fileMatchInfo.getHighlightType() == 1) {
                        highlightPosition.start = Math.max(StringUtil.utf8ToUtf16Index(displayItem.mMMZoomFile.getFileName(), highlightPositionItem.getStart()), 0);
                        highlightPosition.end = Math.max(StringUtil.utf8ToUtf16Index(displayItem.mMMZoomFile.getFileName(), highlightPositionItem.getEnd()), 0);
                    } else {
                        highlightPosition.end = highlightPositionItem.getEnd();
                        highlightPosition.start = highlightPositionItem.getStart();
                    }
                    fileMatchInfo2.mHighlightPositions.add(highlightPosition);
                }
                arrayList.add(fileMatchInfo2);
            }
            displayItem.mMMZoomFile.setMatchInfos(arrayList);
            return displayItem;
        }

        public int hashCode() {
            String webID = this.mMMZoomFile.getWebID();
            if (!StringUtil.isEmptyOrNull(webID)) {
                return webID.hashCode();
            }
            return super.hashCode();
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof DisplayItem)) {
                return false;
            }
            return StringUtil.isSameString(this.mMMZoomFile.getWebID(), ((DisplayItem) obj).mMMZoomFile.getWebID());
        }
    }

    public long getItemId(int i) {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public MMContentSearchFilesAdapter(Context context, boolean z) {
        this.mContext = context;
        this.mIsPersonalMode = z;
    }

    @NonNull
    public List<String> getmLoadedNeedRrefreshFileJids() {
        return this.mLoadedNeedRrefreshFileJids;
    }

    public void clearmLoadedNeedRrefreshFileJids() {
        if (!CollectionsUtil.isListEmpty(this.mLoadedNeedRrefreshFileJids)) {
            this.mLoadedNeedRrefreshFileJids.clear();
        }
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        if (!TextUtils.isEmpty(str)) {
            ArrayList<DisplayItem> arrayList = new ArrayList<>();
            arrayList.addAll(this.mContentFiles);
            for (DisplayItem displayItem : arrayList) {
                if (displayItem != null && TextUtils.equals(displayItem.mMMZoomFile.getOwnerJid(), str)) {
                    updateZoomFile(displayItem.mMMZoomFile.getWebID());
                }
            }
        }
    }

    public void addSearchedFiles(@Nullable FileFilterSearchResults fileFilterSearchResults) {
        if (fileFilterSearchResults != null && fileFilterSearchResults.getTotalSize() != 0) {
            ArrayList arrayList = new ArrayList();
            for (FileFilterSearchResult initWithFileFilterSearchResult : fileFilterSearchResults.getSearchResultList()) {
                DisplayItem initWithFileFilterSearchResult2 = DisplayItem.initWithFileFilterSearchResult(initWithFileFilterSearchResult);
                if (!(initWithFileFilterSearchResult2 == null || initWithFileFilterSearchResult2.mMMZoomFile.getFileType() == 6)) {
                    arrayList.add(initWithFileFilterSearchResult2);
                }
            }
            Collections.sort(arrayList, new ContentFileComparator(this.mIsPersonalMode));
            mergeMessages(arrayList);
        }
    }

    public void addLocalSearchedFiles(@Nullable FileFilterSearchResults fileFilterSearchResults) {
        if (fileFilterSearchResults != null && fileFilterSearchResults.getTotalSize() != 0) {
            for (FileFilterSearchResult initWithFileFilterSearchResult : fileFilterSearchResults.getSearchResultList()) {
                DisplayItem initWithFileFilterSearchResult2 = DisplayItem.initWithFileFilterSearchResult(initWithFileFilterSearchResult);
                if (!(initWithFileFilterSearchResult2 == null || initWithFileFilterSearchResult2.mMMZoomFile.getFileType() == 6)) {
                    this.mContentFiles.add(initWithFileFilterSearchResult2);
                }
            }
            Collections.sort(this.mContentFiles, new ContentFileComparator(this.mIsPersonalMode));
        }
    }

    private void mergeMessages(@Nullable List<DisplayItem> list) {
        if (list != null && list.size() != 0) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            linkedHashSet.addAll(this.mContentFiles);
            linkedHashSet.addAll(list);
            this.mContentFiles = new ArrayList(linkedHashSet);
        }
    }

    public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
        int findFileByWebId = findFileByWebId(str2);
        if (findFileByWebId != -1 && i == 0) {
            this.mContentFiles.remove(findFileByWebId);
            notifyDataSetChanged();
        }
    }

    public void updateZoomFile(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID != null) {
                    MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                    int findFileByWebId = findFileByWebId(str);
                    DisplayItem displayItem = new DisplayItem(initWithZoomFile);
                    if (findFileByWebId != -1) {
                        DisplayItem displayItem2 = (DisplayItem) this.mContentFiles.get(findFileByWebId);
                        if (displayItem2 != null) {
                            initWithZoomFile.setMatchInfos(displayItem2.mMMZoomFile.getMatchInfos());
                        }
                        this.mContentFiles.set(findFileByWebId, displayItem);
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void Indicate_FileAttachInfoUpdate(String str) {
        if (!TextUtils.isEmpty(str)) {
            ArrayList<DisplayItem> arrayList = new ArrayList<>();
            arrayList.addAll(this.mContentFiles);
            for (DisplayItem displayItem : arrayList) {
                if (displayItem != null && TextUtils.equals(displayItem.mMMZoomFile.getWebID(), str)) {
                    updateZoomFile(displayItem.mMMZoomFile.getWebID());
                }
            }
        }
    }

    public void onDownloadByFileIDOnProgress(String str, @Nullable String str2, int i, int i2, int i3) {
        int findFileByWebId = findFileByWebId(str2);
        if (findFileByWebId >= 0) {
            MMZoomFile mMZoomFile = ((DisplayItem) this.mContentFiles.get(findFileByWebId)).mMMZoomFile;
            mMZoomFile.setPending(true);
            mMZoomFile.setRatio(i);
            mMZoomFile.setReqId(str);
            mMZoomFile.setFileDownloading(true);
            mMZoomFile.setCompleteSize(i2);
            mMZoomFile.setBitPerSecond(i3);
            notifyDataSetChanged();
        }
    }

    public void Indicate_FileDownloaded(@NonNull MMZoomFile mMZoomFile) {
        int findFileByWebId = findFileByWebId(mMZoomFile.getWebID());
        if (findFileByWebId != -1) {
            MMZoomFile mMZoomFile2 = ((DisplayItem) this.mContentFiles.get(findFileByWebId)).mMMZoomFile;
            mMZoomFile2.setFileDownloaded(true);
            mMZoomFile2.setPending(false);
            mMZoomFile2.setFileDownloading(false);
        }
        notifyDataSetChanged();
    }

    public void Indicate_PreviewDownloaded(@NonNull MMZoomFile mMZoomFile) {
        int findFileByWebId = findFileByWebId(mMZoomFile.getWebID());
        if (findFileByWebId != -1) {
            ((DisplayItem) this.mContentFiles.get(findFileByWebId)).mMMZoomFile.setPicturePreviewPath(mMZoomFile.getPicturePreviewPath());
        }
        notifyDataSetChanged();
    }

    public boolean containsFile(@Nullable String str) {
        return findFileByWebId(str) != -1;
    }

    private int findFileByWebId(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return -1;
        }
        for (int i = 0; i < this.mContentFiles.size(); i++) {
            if (str.equals(((DisplayItem) this.mContentFiles.get(i)).mMMZoomFile.getWebID())) {
                return i;
            }
        }
        return -1;
    }

    public void endFileTransfer(@Nullable String str) {
        int findFileByWebId = findFileByWebId(str);
        if (findFileByWebId != -1) {
            MMZoomFile mMZoomFile = ((DisplayItem) this.mContentFiles.get(findFileByWebId)).mMMZoomFile;
            if (!mMZoomFile.isFileDownloading()) {
                this.mContentFiles.remove(findFileByWebId);
            } else {
                mMZoomFile.setPending(false);
                mMZoomFile.setFileDownloading(false);
            }
            notifyDataSetChanged();
        }
    }

    public void Indicate_RenameFileResponse(int i, String str, @Nullable String str2, String str3) {
        if (i == 0) {
            int findFileByWebId = findFileByWebId(str2);
            if (findFileByWebId != -1) {
                ((DisplayItem) this.mContentFiles.get(findFileByWebId)).mMMZoomFile.setFileName(str3);
                notifyDataSetChanged();
            }
        }
    }

    public void Indicate_FileActionStatus(int i, @Nullable String str, String str2, String str3, String str4, String str5) {
        int findFileByWebId = findFileByWebId(str);
        if (findFileByWebId != -1) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID != null) {
                    MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                    if (i == 1 || (i == 2 && (initWithZoomFile.getShareAction() == null || initWithZoomFile.getShareAction().size() == 0))) {
                        this.mContentFiles.remove(findFileByWebId);
                    } else {
                        ((DisplayItem) this.mContentFiles.get(findFileByWebId)).mMMZoomFile = initWithZoomFile;
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }

    public boolean isDataEmpty() {
        return this.mContentFiles.isEmpty();
    }

    public void clearAll() {
        this.mContentFiles.clear();
        this.mShowAllSharesFileIds.clear();
    }

    public int getCount() {
        return this.mContentFiles.size();
    }

    public DisplayItem getItem(int i) {
        return (DisplayItem) this.mContentFiles.get(i);
    }

    @Nullable
    public MMZoomFile getItemAtPosition(int i) {
        if (i < 0 || i >= this.mContentFiles.size()) {
            return null;
        }
        return ((DisplayItem) this.mContentFiles.get(i)).mMMZoomFile;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        return createFileItemView(i, view, viewGroup);
    }

    private View createFileItemView(int i, View view, ViewGroup viewGroup) {
        MMZoomFileView mMZoomFileView;
        DisplayItem item = getItem(i);
        if (item == null) {
            return null;
        }
        if (view instanceof MMZoomFileView) {
            mMZoomFileView = (MMZoomFileView) view;
        } else {
            mMZoomFileView = new MMZoomFileView(this.mContext);
            mMZoomFileView.setOnClickOperatorListener(this.mParentListView);
            mMZoomFileView.setOnMoreShareActionListener(this);
        }
        if (item.mMMZoomFile.getOwnerJid() != null && TextUtils.isEmpty(item.mMMZoomFile.getOwnerName())) {
            this.mLoadedNeedRrefreshFileJids.remove(item.mMMZoomFile.getOwnerJid());
            this.mLoadedNeedRrefreshFileJids.add(item.mMMZoomFile.getOwnerJid());
        }
        item.mMMZoomFile.setShowAllShareActions(this.mShowAllSharesFileIds.contains(item.mMMZoomFile.getWebID()));
        mMZoomFileView.setMMZoomFile(item.mMMZoomFile, false);
        return mMZoomFileView;
    }

    public void setParentListView(MMContentSearchFilesListView mMContentSearchFilesListView) {
        this.mParentListView = mMContentSearchFilesListView;
    }

    public void onShowAllShareAction(String str, ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (!StringUtil.isEmptyOrNull(str) && !CollectionsUtil.isListEmpty(arrayList)) {
            ContentFileChatListFragment.showAsFragment(this.mContext, str, arrayList, arrayList2);
        }
    }
}
