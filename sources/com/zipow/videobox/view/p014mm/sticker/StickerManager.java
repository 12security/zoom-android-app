package com.zipow.videobox.view.p014mm.sticker;

import android.content.Context;
import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.ptapp.IMProtos.StickerInfo;
import com.zipow.videobox.ptapp.IMProtos.StickerInfoList;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: com.zipow.videobox.view.mm.sticker.StickerManager */
public class StickerManager {
    public static final int CATEGROY_STICKER_PRIVATE = 2;
    public static final int CATEGROY_STICKER_ZOOM_EMOJI = 1;
    public static final int EMOJI_CELL_HEIGHT_DP = 50;
    public static final int PRIVATE_STICKER_CELL_HEIGHT_DP = 70;
    public static final int PRIVATE_STICKER_MAX_SIZE = 8388608;
    private static final String TAG = "StickerManager";
    @NonNull
    private static Map<String, String> mPendingDownloadLocalPathStickers = new HashMap();
    @NonNull
    private static Map<String, String> mPendingDownloadPreviewStickers = new HashMap();
    @NonNull
    private SparseIntArray mCateCount = new SparseIntArray();
    private Context mContext;
    private List<StickerPanelView> mStickerPanelViews;

    /* renamed from: com.zipow.videobox.view.mm.sticker.StickerManager$PrivateStickerComparator */
    public static class PrivateStickerComparator implements Comparator<StickerEvent> {
        @NonNull
        private Map<String, Long> mCreateTimes = new HashMap();
        @Nullable
        private MMFileContentMgr mgr = PTApp.getInstance().getZoomFileContentMgr();

        public int compare(@Nullable StickerEvent stickerEvent, @Nullable StickerEvent stickerEvent2) {
            if (stickerEvent == null || stickerEvent2 == null) {
                return 0;
            }
            int i = ((getStickerFileCreateTime(stickerEvent) - getStickerFileCreateTime(stickerEvent2)) > 0 ? 1 : ((getStickerFileCreateTime(stickerEvent) - getStickerFileCreateTime(stickerEvent2)) == 0 ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            return i == 0 ? 0 : -1;
        }

        private long getStickerFileCreateTime(@Nullable StickerEvent stickerEvent) {
            if (stickerEvent == null) {
                return 0;
            }
            String stickerId = stickerEvent.getStickerId();
            if (StringUtil.isEmptyOrNull(stickerId)) {
                return 0;
            }
            Long l = (Long) this.mCreateTimes.get(stickerId);
            if (l != null) {
                return l.longValue();
            }
            MMFileContentMgr mMFileContentMgr = this.mgr;
            if (mMFileContentMgr == null) {
                return 0;
            }
            ZoomFile fileWithWebFileID = mMFileContentMgr.getFileWithWebFileID(stickerId);
            if (fileWithWebFileID != null) {
                Long valueOf = Long.valueOf(fileWithWebFileID.getTimeStamp());
                this.mgr.destroyFileObject(fileWithWebFileID);
                this.mCreateTimes.put(stickerId, valueOf);
                return valueOf.longValue();
            } else if (StringUtil.isEmptyOrNull(stickerEvent.getStickerPath())) {
                return 0;
            } else {
                Long l2 = (Long) this.mCreateTimes.get(stickerId);
                if (l2 == null) {
                    l2 = Long.valueOf(CmmTime.getMMNow());
                    this.mCreateTimes.put(stickerId, l2);
                }
                return l2.longValue();
            }
        }
    }

    public static boolean isStickerPreviewDownloading(String str) {
        return mPendingDownloadPreviewStickers.containsKey(str);
    }

    public static boolean isStickerLocalPathDownloading(String str) {
        return mPendingDownloadLocalPathStickers.containsKey(str);
    }

    public static String getStickerPreviewFileIdByReqId(String str) {
        for (Entry entry : mPendingDownloadPreviewStickers.entrySet()) {
            if (((String) entry.getValue()).equals(str)) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    public static String getStickerLocalPathFileIdByReqId(String str) {
        for (Entry entry : mPendingDownloadLocalPathStickers.entrySet()) {
            if (((String) entry.getValue()).equals(str)) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    public static void removeStickerPendingDownloadByReqId(String str) {
        String stickerLocalPathFileIdByReqId = getStickerLocalPathFileIdByReqId(str);
        if (!StringUtil.isEmptyOrNull(stickerLocalPathFileIdByReqId)) {
            mPendingDownloadLocalPathStickers.remove(stickerLocalPathFileIdByReqId);
            return;
        }
        String stickerPreviewFileIdByReqId = getStickerPreviewFileIdByReqId(str);
        if (!StringUtil.isEmptyOrNull(stickerPreviewFileIdByReqId)) {
            mPendingDownloadPreviewStickers.remove(stickerPreviewFileIdByReqId);
        }
    }

    public static void addStickerPreviewReqId(@Nullable String str, @Nullable String str2) {
        if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            mPendingDownloadPreviewStickers.put(str, str2);
        }
    }

    public static void addStickerLocalPathReqId(@Nullable String str, @Nullable String str2) {
        if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            mPendingDownloadLocalPathStickers.put(str, str2);
        }
    }

    public static void FT_OnDownloadByFileIDTimeOutImpl(@Nullable String str, @Nullable String str2) {
        if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            String stickerLocalPathFileIdByReqId = getStickerLocalPathFileIdByReqId(str);
            if (StringUtil.isSameString(stickerLocalPathFileIdByReqId, str2)) {
                MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
                if (zoomPrivateStickerMgr != null) {
                    MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                    if (zoomFileContentMgr != null) {
                        ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str2);
                        if (fileWithWebFileID != null && !StringUtil.isEmptyOrNull(zoomPrivateStickerMgr.downloadSticker(stickerLocalPathFileIdByReqId, PendingFileDataHelper.getContenFilePath(str2, fileWithWebFileID.getFileName())))) {
                            removeStickerPendingDownloadByReqId(str);
                            addStickerLocalPathReqId(str2, str);
                            zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                        }
                    }
                }
            }
        }
    }

    public StickerManager(Context context) {
        this.mContext = context;
        init();
    }

    public int getPanelMiniHeight() {
        return UIUtil.dip2px(this.mContext, 215.0f);
    }

    private void init() {
        this.mStickerPanelViews = generatorAllStickerView();
    }

    public List<StickerPanelView> getAllStickerView() {
        return this.mStickerPanelViews;
    }

    public int getCountByCategory(int i) {
        return this.mCateCount.get(i);
    }

    public int getFirstItemPositionAtCategory(int i) {
        if (CollectionsUtil.isListEmpty(this.mStickerPanelViews)) {
            return -1;
        }
        for (int i2 = 0; i2 < this.mStickerPanelViews.size(); i2++) {
            if (((StickerPanelView) this.mStickerPanelViews.get(i2)).getCategory() == i) {
                return i2;
            }
        }
        return -1;
    }

    public void refreshAllStickerView() {
        this.mStickerPanelViews = generatorAllStickerView();
    }

    @NonNull
    private List<StickerPanelView> generatorAllStickerView() {
        ArrayList arrayList = new ArrayList();
        if (PTApp.getInstance().isFileTransferDisabled()) {
            return arrayList;
        }
        MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
        if (zoomPrivateStickerMgr != null) {
            StickerInfoList stickers = zoomPrivateStickerMgr.getStickers();
            if (stickers == null || stickers.getStickersCount() <= 0) {
                PrivateStickerPanelView privateStickerPanelView = new PrivateStickerPanelView(this.mContext);
                privateStickerPanelView.setContent(new ArrayList());
                arrayList.add(privateStickerPanelView);
                this.mCateCount.put(2, 1);
            } else {
                PrivateStickerPanelView privateStickerPanelView2 = new PrivateStickerPanelView(this.mContext);
                ArrayList arrayList2 = new ArrayList();
                for (int i = 0; i < stickers.getStickersCount(); i++) {
                    StickerInfo stickers2 = stickers.getStickers(i);
                    if (stickers2 != null) {
                        StickerEvent stickerEvent = new StickerEvent(stickers2.getFileId());
                        stickerEvent.setStickerPath(stickers2.getUploadingPath());
                        stickerEvent.setStatus(stickers2.getStatus());
                        arrayList2.add(stickerEvent);
                    }
                }
                Collections.sort(arrayList2, new PrivateStickerComparator());
                ArrayList arrayList3 = new ArrayList();
                PrivateStickerPanelView privateStickerPanelView3 = privateStickerPanelView2;
                int i2 = 0;
                for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                    arrayList3.add(arrayList2.get(i3));
                    if (arrayList3.size() >= privateStickerPanelView3.getMaxStickerSize() || i3 == stickers.getStickersCount() - 1) {
                        privateStickerPanelView3.setContent(arrayList3);
                        privateStickerPanelView3.setIndexInCategory(i2);
                        arrayList.add(privateStickerPanelView3);
                        i2++;
                        if (i3 != arrayList2.size() - 1) {
                            PrivateStickerPanelView privateStickerPanelView4 = new PrivateStickerPanelView(this.mContext);
                            ArrayList arrayList4 = new ArrayList();
                            privateStickerPanelView3 = privateStickerPanelView4;
                            arrayList3 = arrayList4;
                        }
                    }
                }
                this.mCateCount.put(2, i2);
            }
        }
        return arrayList;
    }
}
