package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import com.zipow.videobox.ptapp.IMProtos.LocalStorageTimeInterval;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.LazyLoadDrawable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMSquareImageView;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.IPinnedSectionAdapter;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentImagesAdapter */
public class MMContentImagesAdapter extends BaseRecyclerViewAdapter<DisplayItem> implements IPinnedSectionAdapter {
    private static final int ITEM_TYPE_FOOTER = 2;
    private static final int ITEM_TYPE_IMAGES = 1;
    private static final int ITEM_TYPE_LABEL = 0;
    private static final int ITEM_TYPE_TIME_CHAT = 3;
    private long eraseTime = -1;
    private boolean isTimeChat = false;
    @NonNull
    private List<MMZoomFile> mContentImages = new ArrayList();
    private Context mContext;
    @NonNull
    private List<DisplayItem> mDisplayItems = new ArrayList();
    private boolean mIsGroupOwner = false;
    private boolean mIsPersonalMode = false;
    private boolean mLoadingStatus = false;
    private String mSessionId;

    /* renamed from: com.zipow.videobox.view.mm.MMContentImagesAdapter$ContentFileComparator */
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

    /* renamed from: com.zipow.videobox.view.mm.MMContentImagesAdapter$DisplayItem */
    static class DisplayItem {
        MMZoomFile mImages;
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

    public MMContentImagesAdapter(Context context, boolean z) {
        super(context);
        this.mContext = context;
        this.mIsPersonalMode = z;
    }

    public void setMode(boolean z) {
        this.mIsPersonalMode = z;
    }

    public void setSessionId(String str) {
        this.mSessionId = str;
    }

    public void setIsGroupOwner(boolean z) {
        this.mIsGroupOwner = z;
    }

    public void setEraseTime(long j, boolean z) {
        this.eraseTime = j;
        this.isTimeChat = z;
    }

    public long getEraseTime() {
        return this.eraseTime;
    }

    public void insertTimedChat() {
        this.isTimeChat = true;
        notifyDataSetChanged();
    }

    public void addContentImages(@Nullable List<MMZoomFile> list) {
        if (list != null && list.size() != 0) {
            for (MMZoomFile mMZoomFile : list) {
                int findFileByWebId = findFileByWebId(mMZoomFile.getWebID());
                if (findFileByWebId == -1) {
                    this.mContentImages.add(mMZoomFile);
                } else {
                    this.mContentImages.set(findFileByWebId, mMZoomFile);
                }
            }
        }
    }

    public void updateOrAddContentFile(@Nullable MMZoomFile mMZoomFile) {
        if (mMZoomFile == null || mMZoomFile.isDeletePending() || TextUtils.isEmpty(mMZoomFile.getOwnerJid()) || TextUtils.isEmpty(mMZoomFile.getOwnerName()) || "null".equalsIgnoreCase(mMZoomFile.getOwnerName()) || 6 == mMZoomFile.getFileType()) {
            if (mMZoomFile != null) {
                deleteContentFile(mMZoomFile.getWebID());
            }
            return;
        }
        int findFileByWebId = findFileByWebId(mMZoomFile.getWebID());
        if (findFileByWebId != -1) {
            this.mContentImages.set(findFileByWebId, mMZoomFile);
        } else {
            this.mContentImages.add(mMZoomFile);
        }
    }

    public void updateContentFile(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID != null) {
                    MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, zoomFileContentMgr);
                    if (!fileWithWebFileID.isDeletePending()) {
                        int findFileByWebId = findFileByWebId(str);
                        if (findFileByWebId != -1) {
                            this.mContentImages.set(findFileByWebId, initWithZoomFile);
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

    public boolean containsFile(@Nullable String str) {
        return findFileByWebId(str) != -1;
    }

    private int findFileByWebId(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return -1;
        }
        for (int i = 0; i < this.mContentImages.size(); i++) {
            if (str.equals(((MMZoomFile) this.mContentImages.get(i)).getWebID())) {
                return i;
            }
        }
        return -1;
    }

    public void Indicate_FileDeletedByOthers(@Nullable String str) {
        int findFileByWebId = findFileByWebId(str);
        if (findFileByWebId != -1) {
            this.mContentImages.remove(findFileByWebId);
            notifyDataSetChanged();
        }
    }

    public void Indicate_FileDeleted(String str, @Nullable String str2, int i) {
        deleteContentFile(str2);
    }

    @Nullable
    public MMZoomFile deleteContentFile(@Nullable String str) {
        int findFileByWebId = findFileByWebId(str);
        if (findFileByWebId == -1) {
            return null;
        }
        MMZoomFile mMZoomFile = (MMZoomFile) this.mContentImages.remove(findFileByWebId);
        notifyDataSetChanged();
        return mMZoomFile;
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
        if (this.mContentImages.isEmpty()) {
            return 0;
        }
        if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
            long lastedShareTime = ((MMZoomFile) this.mContentImages.get(0)).getLastedShareTime(this.mSessionId);
            for (MMZoomFile lastedShareTime2 : this.mContentImages) {
                long lastedShareTime3 = lastedShareTime2.getLastedShareTime(this.mSessionId);
                if (lastedShareTime3 < lastedShareTime) {
                    lastedShareTime = lastedShareTime3;
                }
            }
            return lastedShareTime;
        } else if (this.mIsPersonalMode) {
            long timeStamp = ((MMZoomFile) this.mContentImages.get(0)).getTimeStamp();
            for (MMZoomFile mMZoomFile : this.mContentImages) {
                if (mMZoomFile.getTimeStamp() < timeStamp) {
                    timeStamp = mMZoomFile.getTimeStamp();
                }
            }
            return timeStamp;
        } else {
            long lastedShareTime4 = ((MMZoomFile) this.mContentImages.get(0)).getLastedShareTime();
            for (MMZoomFile mMZoomFile2 : this.mContentImages) {
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
        this.mContentImages.clear();
        this.mDisplayItems.clear();
    }

    public void onDownloadByFileIDOnProgress(String str, @Nullable String str2, int i, int i2, int i3) {
        int findFileByWebId = findFileByWebId(str2);
        if (findFileByWebId >= 0) {
            MMZoomFile mMZoomFile = (MMZoomFile) this.mContentImages.get(findFileByWebId);
            mMZoomFile.setPending(true);
            mMZoomFile.setRatio(i);
            mMZoomFile.setReqId(str);
            mMZoomFile.setCompleteSize(i2);
            mMZoomFile.setBitPerSecond(i3);
        }
    }

    public void Indicate_PreviewDownloaded(@NonNull MMZoomFile mMZoomFile) {
        int findFileByWebId = findFileByWebId(mMZoomFile.getWebID());
        if (findFileByWebId != -1) {
            ((MMZoomFile) this.mContentImages.get(findFileByWebId)).setPicturePreviewPath(mMZoomFile.getPicturePreviewPath());
        }
        notifyDataSetChangedWithoutUpdateDisplayData();
    }

    private void rebuildDisplayItems() {
        this.mDisplayItems.clear();
        Collections.sort(this.mContentImages, new ContentFileComparator(this.mIsPersonalMode, this.mSessionId));
        long j = 0;
        int i = 0;
        while (i < this.mContentImages.size()) {
            MMZoomFile mMZoomFile = (MMZoomFile) this.mContentImages.get(i);
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
                displayItem2.mImages = mMZoomFile;
                this.mDisplayItems.add(displayItem2);
                j = lastedShareTime;
                i++;
            } else {
                DisplayItem displayItem3 = new DisplayItem();
                displayItem3.mItemType = 1;
                displayItem3.mImages = mMZoomFile;
                this.mDisplayItems.add(displayItem3);
                i++;
            }
        }
        if (this.isTimeChat && this.mDisplayItems.size() > 0) {
            DisplayItem displayItem4 = new DisplayItem();
            displayItem4.mItemType = 3;
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

    private void notifyDataSetChangedWithoutUpdateDisplayData() {
        super.notifyDataSetChanged();
    }

    private String formatTime(long j) {
        return new SimpleDateFormat("yyyy-M").format(new Date(j));
    }

    @Nullable
    public MMZoomFile getItemAtPosition(int i) {
        if (i < 0 || i >= getItemCount()) {
            return null;
        }
        DisplayItem item = getItem(i);
        if (item == null) {
            return null;
        }
        return item.mImages;
    }

    public int getItemCount() {
        if (this.mDisplayItems.size() == 0) {
            return 0;
        }
        return hasFooter() ? this.mDisplayItems.size() + 1 : this.mDisplayItems.size();
    }

    @Nullable
    public DisplayItem getItem(int i) {
        if (i < 0 || i > this.mDisplayItems.size()) {
            return null;
        }
        return (DisplayItem) this.mDisplayItems.get(i);
    }

    @NonNull
    public List<DisplayItem> getData() {
        return this.mDisplayItems;
    }

    public void setFooterState(boolean z) {
        this.mLoadingStatus = z;
        notifyDataSetChanged();
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == 1) {
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            ZMSquareImageView zMSquareImageView = new ZMSquareImageView(this.mContext);
            zMSquareImageView.setLayoutParams(layoutParams);
            zMSquareImageView.setScaleType(ScaleType.CENTER_CROP);
            return new BaseViewHolder(zMSquareImageView);
        }
        if (i == 2) {
            view = View.inflate(viewGroup.getContext(), C4558R.layout.zm_recyclerview_footer, null);
        } else if (i == 3) {
            view = new MMMessageRemoveHistory(viewGroup.getContext());
        } else {
            LayoutParams layoutParams2 = new LayoutParams(-1, -2);
            view = View.inflate(this.mContext, C4558R.layout.zm_listview_label_item, null);
            view.setLayoutParams(layoutParams2);
        }
        return new BaseViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final BaseViewHolder baseViewHolder, int i) {
        int itemViewType = baseViewHolder.getItemViewType();
        if (itemViewType == 2) {
            int i2 = this.mLoadingStatus ? 0 : 4;
            baseViewHolder.itemView.findViewById(C4558R.C4560id.progressBar).setVisibility(i2);
            baseViewHolder.itemView.findViewById(C4558R.C4560id.txtMsg).setVisibility(i2);
            return;
        }
        DisplayItem item = getItem(i);
        if (item != null) {
            if (itemViewType == 1) {
                if (item.mImages != null) {
                    MMZoomFile mMZoomFile = item.mImages;
                    if (ImageUtil.isValidImageFile(mMZoomFile.getPicturePreviewPath())) {
                        LazyLoadDrawable lazyLoadDrawable = new LazyLoadDrawable(mMZoomFile.getPicturePreviewPath());
                        int width = baseViewHolder.itemView.getWidth();
                        if (width == 0) {
                            width = UIUtil.dip2px(baseViewHolder.itemView.getContext(), 40.0f);
                        }
                        lazyLoadDrawable.setMaxArea(width * width);
                        ((ZMSquareImageView) baseViewHolder.itemView).setImageDrawable(lazyLoadDrawable);
                    } else if (ImageUtil.isValidImageFile(mMZoomFile.getLocalPath())) {
                        LazyLoadDrawable lazyLoadDrawable2 = new LazyLoadDrawable(mMZoomFile.getLocalPath());
                        int width2 = baseViewHolder.itemView.getWidth();
                        if (width2 == 0) {
                            width2 = UIUtil.dip2px(baseViewHolder.itemView.getContext(), 40.0f);
                        }
                        lazyLoadDrawable2.setMaxArea(width2 * width2);
                        ((ZMSquareImageView) baseViewHolder.itemView).setImageDrawable(lazyLoadDrawable2);
                    } else {
                        ((ZMSquareImageView) baseViewHolder.itemView).setImageResource(C4558R.C4559drawable.zm_image_placeholder);
                    }
                }
            } else if (itemViewType == 3) {
                ((MMMessageRemoveHistory) baseViewHolder.itemView).setMessage(item.mLabel);
            } else {
                ((TextView) baseViewHolder.itemView.findViewById(C4558R.C4560id.txtHeaderLabel)).setText(item.mLabel);
            }
            baseViewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (MMContentImagesAdapter.this.mListener != null) {
                        MMContentImagesAdapter.this.mListener.onItemClick(baseViewHolder.itemView, baseViewHolder.getAdapterPosition());
                    }
                }
            });
            baseViewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (MMContentImagesAdapter.this.mListener != null) {
                        return MMContentImagesAdapter.this.mListener.onItemLongClick(baseViewHolder.itemView, baseViewHolder.getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }

    public int getItemViewType(int i) {
        if (hasFooter() && i == getItemCount() - 1) {
            return 2;
        }
        DisplayItem item = getItem(i);
        if (item == null) {
            return 0;
        }
        return item.mItemType;
    }

    public boolean isFooter(int i) {
        return hasFooter() && i == getItemCount() - 1;
    }

    public boolean isPinnedSection(int i) {
        return getItemViewType(i) == 0;
    }

    public void onChanged() {
        rebuildDisplayItems();
    }

    public boolean isTimeChat(int i) {
        return getItemViewType(i) == 3;
    }
}
