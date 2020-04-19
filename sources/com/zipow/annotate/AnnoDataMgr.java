package com.zipow.annotate;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Environment;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnotateDrawingView.AnnoTipType;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.RecordMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.share.IDrawingViewListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.UIUtil;

public class AnnoDataMgr {
    public static final int ANNO_DEFAULT_ALPHA = 255;
    public static final int ANNO_HIGHLIGHTER_ALPHA = 97;
    public static final int ANNO_LINE_WIDTH_12 = 12;
    public static final int ANNO_LINE_WIDTH_2 = 2;
    public static final int ANNO_LINE_WIDTH_4 = 4;
    public static final int ANNO_LINE_WIDTH_8 = 8;
    public static final int DEFAULT_FONT_SIZE = 48;
    public static final String TAG = "com.zipow.annotate.AnnoDataMgr";
    public static final int WB_MULTI_PAGE_MAX = 12;
    private static AnnoDataMgr mInstance;
    public static List<AnnoPageInfo> mPageList;
    private AnnoToolType mAnnoToolType = AnnoToolType.ANNO_TOOL_TYPE_NONE;
    private boolean mAttendeeAnnotateDisable = false;
    private boolean mBPresenter = false;
    private boolean mBShareScreen = false;
    private int[] mColors = null;
    private boolean mCopyToAlbum;
    private boolean mHdpi = false;
    private IDrawingViewListener mListener;
    private List<PointF> mLocalObjList;
    private String mRecordPath = "";
    public int mVideoGalleryHeight = 0;
    public int mVideoGalleryWidth = 0;
    private long mViewHandle = 0;

    public enum AnnoPageOperation {
        ANNO_PAGE_OPRATION_NONE,
        ANNO_PAGE_OPRATION_ADD,
        ANNO_PAGE_OPRATION_REMOVE,
        ANNO_PAGE_OPRATION_RESTORE,
        ANNO_PAGE_OPRATION_SWITCH,
        ANNO_PAGE_OPRATION_NUMBER
    }

    public static AnnoDataMgr getInstance() {
        if (mInstance == null) {
            mInstance = new AnnoDataMgr();
        }
        return mInstance;
    }

    public AnnoDataMgr() {
        mPageList = new ArrayList();
        this.mCopyToAlbum = false;
        this.mLocalObjList = new ArrayList();
    }

    public void registerUpdateListener(IDrawingViewListener iDrawingViewListener) {
        this.mListener = iDrawingViewListener;
    }

    public boolean isPresenter() {
        return this.mBPresenter;
    }

    public boolean isShareScreen() {
        return this.mBShareScreen;
    }

    public void setIsHDPI(boolean z) {
        this.mHdpi = z;
    }

    public boolean getIsHDPI() {
        return this.mHdpi;
    }

    public void setAttendeeAnnotateDisable(boolean z) {
        this.mAttendeeAnnotateDisable = z;
    }

    public boolean getAttendeeAnnotateDisable() {
        return this.mAttendeeAnnotateDisable;
    }

    public void updateVideoGallerySize(long j, int i, int i2) {
        if (this.mBPresenter) {
            j = 0;
        }
        this.mViewHandle = j;
        this.mVideoGalleryWidth = i;
        this.mVideoGalleryHeight = i2;
        setAnnotateInfoToNative();
    }

    public void saveLocalDrawing(float f, float f2) {
        this.mLocalObjList.add(new PointF((float) ((int) f), (float) ((int) f2)));
    }

    public boolean isLocalDrawing(float f, float f2) {
        if (this.mLocalObjList == null) {
            return false;
        }
        return this.mLocalObjList.contains(new PointF((float) ((int) f), (float) ((int) f2)));
    }

    public void startAnnotaion(boolean z, boolean z2, long j) {
        deleteTempDir(new File(getPageSnapshotTempDir()));
        this.mBShareScreen = z2;
        this.mBPresenter = z;
        if (this.mBPresenter) {
            j = 0;
        }
        this.mViewHandle = j;
        setAnnotateInfoToNative();
    }

    public void stopAnnotation() {
        this.mLocalObjList.clear();
        this.mBPresenter = false;
        this.mAnnoToolType = AnnoToolType.ANNO_TOOL_TYPE_NONE;
        setAnnotateInfoToNative();
        if (!mPageList.isEmpty()) {
            deleteTempDir(new File(getPageSnapshotTempDir()));
            mPageList.clear();
        }
    }

    private void setAnnotateInfoToNative() {
        ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
        if (zoomAnnotateMgr != null) {
            zoomAnnotateMgr.setAnnoInfoToNative(this.mBPresenter, this.mBShareScreen, this.mViewHandle);
        }
    }

    public long getActiveUserID() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return 0;
        }
        return shareObj.getActiveUserID();
    }

    public boolean setLineWidth(int i) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return false;
        }
        return shareObj.setLineWidth(this.mViewHandle, i);
    }

    public int getLineWidth(AnnoToolType annoToolType) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return 0;
        }
        return shareObj.getLineWidth(this.mViewHandle, annoToolType.ordinal());
    }

    public void setTool(AnnoToolType annoToolType) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.setTool(this.mViewHandle, annoToolType.ordinal());
            this.mAnnoToolType = annoToolType;
        }
    }

    public AnnoToolType getTool() {
        return this.mAnnoToolType;
    }

    public void setColor(int i) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.setColor(this.mViewHandle, Color.argb(255, Color.blue(i), Color.green(i), Color.red(i)));
        }
    }

    public int getColor(AnnoToolType annoToolType) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return 0;
        }
        int color = shareObj.getColor(this.mViewHandle, annoToolType.ordinal());
        return Color.rgb((int) ((long) (color & 255)), (int) ((long) ((color >> 8) & 255)), (int) ((long) ((color >> 16) & 255)));
    }

    public int getCurToolColor() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return 0;
        }
        int color = shareObj.getColor(this.mViewHandle, getTool().ordinal());
        return Color.rgb((int) ((long) (color & 255)), (int) ((long) ((color >> 8) & 255)), (int) ((long) ((color >> 16) & 255)));
    }

    public void undo() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.undo(this.mViewHandle);
        }
    }

    public void redo() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.redo(this.mViewHandle);
        }
    }

    public void eraser(int i) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.eraser(this.mViewHandle, i);
        }
    }

    public int getCompserVersion() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return 0;
        }
        return shareObj.getCompserVersion(this.mViewHandle);
    }

    public void newPage() {
        saveCurPageSnapahot();
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.newPage(this.mViewHandle);
        }
    }

    public void closePage(int i) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null && shareObj.closePage(this.mViewHandle, (long) i) && removePageSnapahot(i)) {
            for (int i2 = 0; i2 < mPageList.size(); i2++) {
                AnnoPageInfo annoPageInfo = (AnnoPageInfo) mPageList.get(i2);
                if (i == annoPageInfo.mPageId) {
                    mPageList.remove(annoPageInfo);
                    return;
                }
            }
        }
    }

    public void prevPage() {
        saveCurPageSnapahot();
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.prevPage(this.mViewHandle);
        }
    }

    public void nextPage() {
        saveCurPageSnapahot();
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.nextPage(this.mViewHandle);
        }
    }

    @Nullable
    public AnnoPageInfo getAnnoPageNum() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return null;
        }
        return shareObj.getAnnoPageNum(this.mViewHandle);
    }

    public void switchPage(long j) {
        saveCurPageSnapahot();
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.switchPage(this.mViewHandle, j);
        }
    }

    public int getPageSnapshot(int i) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return 0;
        }
        return shareObj.getPageSnapshot(this.mViewHandle, i);
    }

    public boolean isSharingWhiteboard() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return false;
        }
        return shareObj.isSharingWhiteboard(this.mViewHandle);
    }

    @Nullable
    public int[] getColorList() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return null;
        }
        long[] colorArray = shareObj.getColorArray(this.mViewHandle);
        if (colorArray == null) {
            return null;
        }
        this.mColors = new int[colorArray.length];
        for (int i = 0; i < colorArray.length; i++) {
            long j = colorArray[i];
            long j2 = (j >> 16) & 255;
            long j3 = (j >> 8) & 255;
            this.mColors[i] = Color.argb(255, (int) (j & 255), (int) j3, (int) j2);
        }
        return this.mColors;
    }

    public int getColorByIndex(int i) {
        if (this.mColors == null) {
            getColorList();
        }
        int[] iArr = this.mColors;
        if (i < iArr.length) {
            return iArr[i];
        }
        return iArr[0];
    }

    public void setIsCopyToAlbum(boolean z) {
        this.mCopyToAlbum = z;
    }

    public String getPageSnapshotTempDir() {
        StringBuilder sb = new StringBuilder();
        sb.append(AppUtil.getDataPath(true, true));
        sb.append(File.separator);
        sb.append("temp");
        sb.append(File.separator);
        return sb.toString();
    }

    private String getSavePageSnapshotDir() {
        String recordPath = getRecordPath();
        if (OsUtil.isAtLeastQ()) {
            StringBuilder sb = new StringBuilder();
            sb.append("DCIM");
            sb.append(File.separator);
            sb.append("zoom");
            sb.append(File.separator);
            sb.append(recordPath);
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb2.append(File.separator);
        sb2.append("DCIM");
        sb2.append(File.separator);
        sb2.append("zoom");
        sb2.append(File.separator);
        sb2.append(recordPath);
        return sb2.toString();
    }

    public String getSavePageSnapshotPath(int i) {
        if (OsUtil.isAtLeastQ()) {
            return getSavePageSnapshotDir();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getSavePageSnapshotDir());
        sb.append(getPageSnapshotFileName(i));
        return sb.toString();
    }

    public String getPageSnapshotFileName(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(i));
        sb.append(".png");
        return sb.toString();
    }

    public String getPageSnapshotPath(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(getPageSnapshotTempDir());
        sb.append(File.separator);
        sb.append(getPageSnapshotFileName(i));
        return sb.toString();
    }

    private String getRecordPath() {
        if (this.mRecordPath.isEmpty()) {
            RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
            if (recordMgr != null) {
                String currentRecPath = recordMgr.getCurrentRecPath();
                if (currentRecPath == null) {
                    return this.mRecordPath;
                }
                int lastIndexOf = currentRecPath.lastIndexOf("/") + 1;
                StringBuilder sb = new StringBuilder();
                sb.append(currentRecPath.substring(lastIndexOf));
                sb.append(File.separator);
                this.mRecordPath = sb.toString();
            }
        }
        return this.mRecordPath;
    }

    public void pageNumChanged(int i, int i2) {
        if (i == AnnoPageOperation.ANNO_PAGE_OPRATION_ADD.ordinal() || i == AnnoPageOperation.ANNO_PAGE_OPRATION_RESTORE.ordinal()) {
            AnnoPageInfo annoPageNum = getAnnoPageNum();
            if (annoPageNum != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(getPageSnapshotTempDir());
                sb.append(getPageSnapshotFileName(i2));
                annoPageNum.mSavePath = sb.toString();
                annoPageNum.mPageId = i2;
                mPageList.add(annoPageNum);
            }
        } else if (i == AnnoPageOperation.ANNO_PAGE_OPRATION_REMOVE.ordinal()) {
            for (int i3 = 0; i3 < mPageList.size(); i3++) {
                AnnoPageInfo annoPageInfo = (AnnoPageInfo) mPageList.get(i3);
                if (annoPageInfo.mPageId == i2) {
                    mPageList.remove(annoPageInfo);
                    return;
                }
            }
        }
    }

    public void onSavePageSnapshot(int i, Bitmap bitmap) {
        String pageSnapshotTempDir = getPageSnapshotTempDir();
        String pageSnapshotFileName = getPageSnapshotFileName(i);
        try {
            File file = new File(pageSnapshotTempDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(pageSnapshotTempDir);
            sb.append(pageSnapshotFileName);
            File file2 = new File(sb.toString());
            if (file2.exists()) {
                file2.delete();
            }
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            savePageSnapshotSuccess(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePageSnapshotSuccess(int i) {
        int i2 = 0;
        while (true) {
            if (i2 >= mPageList.size()) {
                break;
            }
            AnnoPageInfo annoPageInfo = (AnnoPageInfo) mPageList.get(i2);
            if (annoPageInfo.mPageId == i) {
                annoPageInfo.mbSaveSnapahot = true;
                break;
            }
            i2++;
        }
        if (mPageList.size() == 1 && this.mCopyToAlbum) {
            copyPageSnapahotToAlbum();
            this.mCopyToAlbum = false;
        }
    }

    public void saveCurPageSnapahot() {
        AnnoPageInfo annoPageNum = getAnnoPageNum();
        if (annoPageNum != null) {
            getPageSnapshot(annoPageNum.mCurrentPageNum);
        }
    }

    public void resetSaveStatus() {
        for (int i = 0; i < mPageList.size(); i++) {
            ((AnnoPageInfo) mPageList.get(i)).mbSaveSnapahot = false;
        }
    }

    public void copyPageSnapahotToAlbum() {
        try {
            File file = new File(getSavePageSnapshotDir());
            if (!file.exists()) {
                file.mkdirs();
            }
            int i = 0;
            for (int i2 = 0; i2 < mPageList.size(); i2++) {
                AnnoPageInfo annoPageInfo = (AnnoPageInfo) mPageList.get(i2);
                String savePageSnapshotPath = getSavePageSnapshotPath(annoPageInfo.mPageId);
                String pageSnapshotFileName = getPageSnapshotFileName(annoPageInfo.mPageId);
                if (annoPageInfo.mbSaveSnapahot && FileUtils.copyImageFileToDCIM(VideoBoxApplication.getNonNullInstance(), annoPageInfo.mSavePath, savePageSnapshotPath, pageSnapshotFileName)) {
                    i++;
                    UIUtil.updateFileFromDatabase(ConfMgr.getApplicationContext(), new File(savePageSnapshotPath));
                }
            }
            if (i > 0) {
                this.mListener.onShowAnnoTip(AnnoTipType.ANNO_SAVE_TIP, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean removePageSnapahot(int i) {
        String pageSnapshotTempDir = getPageSnapshotTempDir();
        String pageSnapshotFileName = getPageSnapshotFileName(i);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(pageSnapshotTempDir);
            sb.append(pageSnapshotFileName);
            File file = new File(sb.toString());
            if (file.exists()) {
                file.delete();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTempDir(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (file2.isFile()) {
                        file2.delete();
                        UIUtil.updateFileFromDatabase(ConfMgr.getApplicationContext(), file2);
                    } else if (file2.isDirectory()) {
                        deleteTempDir(file2);
                    }
                }
                file.delete();
            }
        }
    }

    public static List<AnnoPageInfo> getPageList() {
        return mPageList;
    }
}
