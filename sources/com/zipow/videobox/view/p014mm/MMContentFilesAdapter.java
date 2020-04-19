package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.LocalStorageTimeInterval;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.p014mm.MMZoomFileView.OnShowAllShareActionListener;
import com.zipow.videobox.view.p014mm.contentfile.ContentFileChatListFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.IPinnedSectionAdapter;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentFilesAdapter */
public class MMContentFilesAdapter extends BaseRecyclerViewAdapter<DisplayItem> implements OnShowAllShareActionListener, IPinnedSectionAdapter {
    private static final int ITEM_TYPE_FILES = 1;
    private static final int ITEM_TYPE_FOOTER = 3;
    private static final int ITEM_TYPE_LABEL = 0;
    private static final int ITEM_TYPE_TIME_CHAT = 2;
    private static final String TAG_ITEM_LABEL = "TAG_ITEM_LABEL";
    private long eraseTime = -1;
    private boolean isTimeChat = false;
    @NonNull
    private List<MMZoomFile> mContentFiles = new ArrayList();
    @NonNull
    private List<DisplayItem> mDisplayItems = new ArrayList();
    private boolean mIsDataInited = false;
    private boolean mIsGroupOwner = false;
    private boolean mIsPersonalMode = false;
    private boolean mLoadingStatus = false;
    private MMContentFilesListView mParentListView;
    private String mSessionId;
    @NonNull
    private Set<String> mShowAllSharesFileIds = new HashSet();

    /* renamed from: com.zipow.videobox.view.mm.MMContentFilesAdapter$ContentFileComparator */
    static class ContentFileComparator implements Comparator<MMZoomFile> {
        private boolean mIsPersonalMode;
        private String mSessionId;

        public ContentFileComparator(boolean z, String str) {
            this.mIsPersonalMode = z;
            this.mSessionId = str;
        }

        public int compare(@NonNull MMZoomFile mMZoomFile, @NonNull MMZoomFile mMZoomFile2) {
            long j;
            if (this.mIsPersonalMode) {
                j = mMZoomFile.getTimeStamp() - mMZoomFile2.getTimeStamp();
            } else {
                j = mMZoomFile.getLastedShareTime(this.mSessionId) - mMZoomFile2.getLastedShareTime(this.mSessionId);
            }
            int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
            if (i > 0) {
                return -1;
            }
            return i == 0 ? 0 : 1;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMContentFilesAdapter$DisplayItem */
    static class DisplayItem {
        MMZoomFile file;
        int mItemType;
        String mLabel;

        DisplayItem() {
        }
    }

    public long getItemId(int i) {
        return 0;
    }

    public boolean hasFooter() {
        return true;
    }

    public MMContentFilesAdapter(Context context) {
        super(context);
    }

    public void setMode(boolean z) {
        this.mIsPersonalMode = z;
    }

    public void setSessionId(String str) {
        this.mSessionId = str;
    }

    @Nullable
    public MMZoomFile getItemAtPosition(int i) {
        MMZoomFile mMZoomFile = null;
        if (i < 0 || i >= getItemCount()) {
            return null;
        }
        DisplayItem item = getItem(i);
        if (item != null) {
            mMZoomFile = item.file;
        }
        return mMZoomFile;
    }

    public void addContenFiles(@Nullable List<MMZoomFile> list) {
        if (list != null && list.size() != 0) {
            for (MMZoomFile updateOrAddContentFile : list) {
                updateOrAddContentFile(updateOrAddContentFile);
            }
        }
    }

    public boolean updateContentFileByJid(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str) || CollectionsUtil.isListEmpty(this.mContentFiles)) {
            return false;
        }
        ArrayList<MMZoomFile> arrayList = new ArrayList<>();
        arrayList.addAll(this.mContentFiles);
        for (MMZoomFile mMZoomFile : arrayList) {
            if (mMZoomFile != null && TextUtils.equals(mMZoomFile.getOwnerJid(), str)) {
                updateContentFile(mMZoomFile.getWebID());
                z = true;
            }
        }
        return z;
    }

    public void updateContentFile(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID != null) {
                    MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                    if (!fileWithWebFileID.isDeletePending()) {
                        int findFileIndexByWebId = findFileIndexByWebId(str);
                        if (findFileIndexByWebId != -1) {
                            this.mContentFiles.set(findFileIndexByWebId, initWithZoomFile);
                        }
                    }
                }
            }
        }
    }

    public void updateOrAddContentFile(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID == null || fileWithWebFileID.isDeletePending()) {
                    deleteContentFile(str);
                } else {
                    updateOrAddContentFile(MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr));
                }
            }
        }
    }

    public void insertTimedChat() {
        this.isTimeChat = true;
        notifyDataSetChanged();
    }

    public void updateOrAddContentFile(@Nullable MMZoomFile mMZoomFile) {
        if (this.mIsDataInited) {
            if (mMZoomFile == null || mMZoomFile.isDeletePending() || TextUtils.isEmpty(mMZoomFile.getOwnerJid()) || TextUtils.isEmpty(mMZoomFile.getOwnerName()) || "null".equalsIgnoreCase(mMZoomFile.getOwnerName()) || 6 == mMZoomFile.getFileType()) {
                if (mMZoomFile != null) {
                    deleteContentFile(mMZoomFile.getWebID());
                }
                return;
            }
            int findFileIndexByWebId = findFileIndexByWebId(mMZoomFile.getWebID());
            if (findFileIndexByWebId != -1) {
                this.mContentFiles.set(findFileIndexByWebId, mMZoomFile);
            } else {
                this.mContentFiles.add(mMZoomFile);
            }
        }
    }

    public void setIsGroupOwner(boolean z) {
        this.mIsGroupOwner = z;
    }

    public void setDataInited(boolean z) {
        this.mIsDataInited = z;
    }

    @Nullable
    public MMZoomFile deleteContentFile(@Nullable String str) {
        int findFileIndexByWebId = findFileIndexByWebId(str);
        if (findFileIndexByWebId == -1) {
            return null;
        }
        MMZoomFile mMZoomFile = (MMZoomFile) this.mContentFiles.remove(findFileIndexByWebId);
        notifyDataSetChanged();
        return mMZoomFile;
    }

    @Nullable
    public MMZoomFile findFileByWebId(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        for (int i = 0; i < this.mContentFiles.size(); i++) {
            MMZoomFile mMZoomFile = (MMZoomFile) this.mContentFiles.get(i);
            if (str.equals(mMZoomFile.getWebID())) {
                return mMZoomFile;
            }
        }
        return null;
    }

    private int findFileIndexByWebId(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return -1;
        }
        for (int i = 0; i < this.mContentFiles.size(); i++) {
            if (str.equals(((MMZoomFile) this.mContentFiles.get(i)).getWebID())) {
                return i;
            }
        }
        return -1;
    }

    public boolean isDataEmpty() {
        return this.mContentFiles.isEmpty();
    }

    public long getLastTimeStamp() {
        long j;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return 0;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return 0;
        }
        String jid = myself.getJid();
        if (this.mContentFiles.isEmpty()) {
            return 0;
        }
        if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
            long lastedShareTime = ((MMZoomFile) this.mContentFiles.get(0)).getLastedShareTime(this.mSessionId);
            for (MMZoomFile lastedShareTime2 : this.mContentFiles) {
                long lastedShareTime3 = lastedShareTime2.getLastedShareTime(this.mSessionId);
                if (lastedShareTime3 < lastedShareTime) {
                    lastedShareTime = lastedShareTime3;
                }
            }
            return lastedShareTime;
        } else if (this.mIsPersonalMode) {
            long timeStamp = ((MMZoomFile) this.mContentFiles.get(0)).getTimeStamp();
            for (MMZoomFile mMZoomFile : this.mContentFiles) {
                if (mMZoomFile.getTimeStamp() < timeStamp) {
                    timeStamp = mMZoomFile.getTimeStamp();
                }
            }
            return timeStamp;
        } else {
            long lastedShareTime4 = ((MMZoomFile) this.mContentFiles.get(0)).getLastedShareTime();
            for (MMZoomFile mMZoomFile2 : this.mContentFiles) {
                if (StringUtil.isSameString(mMZoomFile2.getOwnerJid(), jid)) {
                    j = mMZoomFile2.getTimeStamp();
                } else {
                    j = mMZoomFile2.getLastedShareTime();
                }
                if (j < lastedShareTime4) {
                    lastedShareTime4 = j;
                }
            }
            return lastedShareTime4;
        }
    }

    public void clearAll() {
        this.mContentFiles.clear();
        this.mShowAllSharesFileIds.clear();
    }

    private void rebuildDisplayItems() {
        this.mDisplayItems.clear();
        Collections.sort(this.mContentFiles, new ContentFileComparator(this.mIsPersonalMode, this.mSessionId));
        long j = 0;
        int i = 0;
        while (i < this.mContentFiles.size()) {
            MMZoomFile mMZoomFile = (MMZoomFile) this.mContentFiles.get(i);
            if (this.mIsGroupOwner && CollectionsUtil.isListEmpty(mMZoomFile.getOperatorAbleSessions())) {
                mMZoomFile.addOperatorAbleSession(this.mSessionId);
            }
            long lastedShareTime = mMZoomFile.getLastedShareTime(this.mSessionId);
            if (this.isTimeChat) {
                long j2 = this.eraseTime;
                if (j2 != -1 && lastedShareTime < j2) {
                    i++;
                }
            }
            if (j == 0 || !TimeUtil.isInSameMonth(j, lastedShareTime)) {
                DisplayItem displayItem = new DisplayItem();
                displayItem.mItemType = 0;
                displayItem.mLabel = formatTime(lastedShareTime);
                this.mDisplayItems.add(displayItem);
                DisplayItem displayItem2 = new DisplayItem();
                displayItem2.mItemType = 1;
                displayItem2.file = mMZoomFile;
                this.mDisplayItems.add(displayItem2);
                j = lastedShareTime;
                i++;
            } else {
                DisplayItem displayItem3 = new DisplayItem();
                displayItem3.mItemType = 1;
                displayItem3.file = mMZoomFile;
                this.mDisplayItems.add(displayItem3);
                i++;
            }
        }
        if (this.isTimeChat && this.mDisplayItems.size() > 0) {
            DisplayItem displayItem4 = new DisplayItem();
            displayItem4.mItemType = 2;
            displayItem4.mLabel = getTimeChatMsg();
            this.mDisplayItems.add(displayItem4);
        }
    }

    @NonNull
    private String getTimeChatMsg() {
        String str = "";
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return str;
        }
        LocalStorageTimeInterval localStorageTimeInterval = zoomMessenger.getLocalStorageTimeInterval();
        if (localStorageTimeInterval != null) {
            str = this.mContext.getResources().getString(C4558R.string.zm_mm_msg_remove_history_message2_33479, new Object[]{timeInterval(localStorageTimeInterval.getYear(), localStorageTimeInterval.getMonth(), localStorageTimeInterval.getDay())});
        }
        return str;
    }

    private String timeInterval(long j, long j2, long j3) {
        if (j != 0) {
            return this.mContext.getResources().getQuantityString(C4558R.plurals.zm_mm_msg_year_33479, (int) j, new Object[]{Long.valueOf(j)});
        } else if (j2 != 0) {
            return this.mContext.getResources().getQuantityString(C4558R.plurals.zm_mm_msg_month_33479, (int) j2, new Object[]{Long.valueOf(j2)});
        } else if (j3 == 1) {
            return this.mContext.getResources().getQuantityString(C4558R.plurals.zm_mm_msg_hour_33479, 24, new Object[]{Integer.valueOf(24)});
        } else {
            return this.mContext.getResources().getQuantityString(C4558R.plurals.zm_mm_msg_day_33479, (int) j3, new Object[]{Long.valueOf(j3)});
        }
    }

    public boolean isEnabled(int i) {
        DisplayItem item = getItem(i);
        boolean z = false;
        if (item == null) {
            return false;
        }
        if (item.file != null) {
            z = true;
        }
        return z;
    }

    public void setEraseTime(long j, boolean z) {
        this.eraseTime = j;
        this.isTimeChat = z;
    }

    public long getEraseTime() {
        return this.eraseTime;
    }

    public int getItemCount() {
        if (this.mDisplayItems.size() == 0) {
            return 0;
        }
        return hasFooter() ? this.mDisplayItems.size() + 1 : this.mDisplayItems.size();
    }

    @Nullable
    public DisplayItem getItem(int i) {
        if (i < 0 || i >= this.mDisplayItems.size()) {
            return null;
        }
        return (DisplayItem) this.mDisplayItems.get(i);
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        if (i == 0) {
            view = View.inflate(viewGroup.getContext(), C4558R.layout.zm_listview_label_item, null);
            view.setTag(TAG_ITEM_LABEL);
        } else if (i == 2) {
            view = new MMMessageRemoveHistory(viewGroup.getContext());
        } else if (i == 3) {
            view = View.inflate(viewGroup.getContext(), C4558R.layout.zm_recyclerview_footer, null);
        } else {
            view = new MMZoomFileView(viewGroup.getContext());
        }
        view.setLayoutParams(layoutParams);
        return new BaseViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final BaseViewHolder baseViewHolder, int i) {
        int itemViewType = baseViewHolder.getItemViewType();
        if (itemViewType == 3) {
            int i2 = this.mLoadingStatus ? 0 : 4;
            baseViewHolder.itemView.findViewById(C4558R.C4560id.progressBar).setVisibility(i2);
            baseViewHolder.itemView.findViewById(C4558R.C4560id.txtMsg).setVisibility(i2);
            return;
        }
        DisplayItem item = getItem(i);
        if (item != null) {
            if (itemViewType == 0) {
                ((TextView) baseViewHolder.itemView.findViewById(C4558R.C4560id.txtHeaderLabel)).setText(item.mLabel);
            } else if (itemViewType == 2) {
                ((MMMessageRemoveHistory) baseViewHolder.itemView).setMessage(item.mLabel);
            } else {
                ((MMZoomFileView) baseViewHolder.itemView).setOnClickOperatorListener(this.mParentListView);
                ((MMZoomFileView) baseViewHolder.itemView).setOnMoreShareActionListener(this);
                if (item.file != null) {
                    item.file.setShowAllShareActions(this.mShowAllSharesFileIds.contains(item.file.getWebID()));
                    ((MMZoomFileView) baseViewHolder.itemView).setMMZoomFile(item.file, this.mIsPersonalMode, this.mSessionId);
                }
            }
            baseViewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (MMContentFilesAdapter.this.mListener != null) {
                        MMContentFilesAdapter.this.mListener.onItemClick(baseViewHolder.itemView, baseViewHolder.getAdapterPosition());
                    }
                }
            });
            baseViewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (MMContentFilesAdapter.this.mListener != null) {
                        return MMContentFilesAdapter.this.mListener.onItemLongClick(baseViewHolder.itemView, baseViewHolder.getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }

    public int getItemViewType(int i) {
        int i2 = 1;
        if (hasFooter() && i == getItemCount() - 1) {
            return 3;
        }
        DisplayItem item = getItem(i);
        if (item != null) {
            i2 = item.mItemType;
        }
        return i2;
    }

    public void setFooterState(boolean z) {
        this.mLoadingStatus = z;
        notifyDataSetChanged();
    }

    private String formatTime(long j) {
        return new SimpleDateFormat("yyyy-M").format(new Date(j));
    }

    public void setParentListView(MMContentFilesListView mMContentFilesListView) {
        this.mParentListView = mMContentFilesListView;
    }

    public boolean isPinnedSection(int i) {
        return getItemViewType(i) == 0;
    }

    public void onChanged() {
        rebuildDisplayItems();
    }

    public void onShowAllShareAction(String str, ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (!StringUtil.isEmptyOrNull(str) && !CollectionsUtil.isListEmpty(arrayList)) {
            ContentFileChatListFragment.showAsFragment(this.mContext, str, arrayList, arrayList2);
        }
    }
}
