package com.zipow.videobox.confapp;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.annotate.AnnoPageInfo;
import com.zipow.nydus.VideoSize;
import java.nio.ByteBuffer;

public class ShareSessionMgr {
    public static final Object SHARE_SESSION_LOCK = new Object();
    private static final String TAG = "ShareSessionMgr";
    private boolean mIsConfCleaned = false;
    private long mNativeHandle = 0;
    private int mViewHeight = 0;
    private int mViewWidth = 0;

    private native long addPicImpl(long j, long j2, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private native boolean assignRemoteControlPrivilegeImpl(long j, long j2, long j3, boolean z);

    private native boolean checkHasRemoteControlPrivilegeImpl(long j, long j2, long j3);

    private native void clearRendererImpl(long j, long j2);

    private native boolean closePageImpl(long j, long j2, long j3);

    private native long createRendererInfo(long j, boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7);

    private native void declineRemoteControlImpl(long j, long j2);

    private native void destAreaChangedImpl(long j, long j2, int i, int i2, int i3, int i4);

    private native boolean destroyRenderer(long j, long j2);

    private native boolean destroyRendererInfo(long j, long j2);

    private native boolean disableAttendeeAnnotationForMySharedContentImpl(long j, boolean z);

    private native boolean enableShowAnnotatorNameImpl(long j, boolean z);

    private native boolean eraserImpl(long j, long j2, int i);

    private native long getActiveUserIDImpl(long j);

    private native AnnoPageInfo getAnnoPageNumImpl(long j, long j2);

    private native long[] getColorArrayImpl(long j, long j2);

    private native int getColorImpl(long j, long j2, int i);

    private native int getCompserVersionImpl(long j, long j2);

    private native int getLineWidthImpl(long j, long j2, int i);

    private native int getPageSnapshotImpl(long j, long j2, int i);

    private native long getPureComputerAudioSharingUserIDImpl(long j);

    private native long getShareDataResolutionImpl(long j, long j2);

    private native int getShareSettingTypeImpl(long j);

    private native int getShareStatusImpl(long j);

    private native int getToolImpl(long j, long j2);

    private native boolean giveupRemoteControlImpl(long j, long j2);

    private native void glViewSizeChangedImpl(long j, long j2, int i, int i2);

    private native boolean grabRemoteControlImpl(long j, long j2);

    private native boolean grabRemoteControllingStatusImpl(long j, long j2, long j3, boolean z);

    private native boolean hasRemoteControlPrivilegeWithUserIdImpl(long j, long j2);

    private native boolean isAttendeeAnnotationDisabledForMySharedContentImpl(long j);

    private native boolean isRemoteControllerImpl(long j, long j2);

    private native boolean isShareSourceInRemoteControllingStatusByUserImpl(long j, long j2, long j3);

    private native boolean isShareSourceInRemoteControllingStatusImpl(long j, long j2);

    private native boolean isShareWhiteboardImpl(long j, long j2);

    private native boolean isShowAnnotatorNameImpl(long j);

    private native boolean isVideoSharingInProgressImpl(long j);

    private native boolean isViewingPureComputerAudioImpl(long j);

    private native long movePic2Impl(long j, long j2, int i, int i2, int i3, int i4, int i5);

    private native long movePicImpl(long j, long j2, int i, long j3, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private native boolean newPageImpl(long j, long j2);

    private native boolean nextPageImpl(long j, long j2);

    private native boolean prepareRenderer(long j, long j2);

    private native boolean presenterIsSharingAudioImpl(long j);

    private native boolean prevPageImpl(long j, long j2);

    private native boolean redoImpl(long j, long j2);

    private native boolean remoteControlCharInputImpl(long j, String str);

    private native boolean remoteControlDoubleScrollImpl(long j, float f, float f2);

    private native boolean remoteControlDoubleTapImpl(long j, float f, float f2);

    private native boolean remoteControlKeyInputImpl(long j, int i);

    private native boolean remoteControlLongPressImpl(long j, float f, float f2);

    private native boolean remoteControlMouseValidateImpl(long j, float f, float f2);

    private native boolean remoteControlMutiShareCharInputImpl(long j, long j2, String str);

    private native boolean remoteControlMutiShareDoubleScrollImpl(long j, long j2, float f, float f2);

    private native boolean remoteControlMutiShareDoubleTapImpl(long j, long j2, float f, float f2);

    private native boolean remoteControlMutiShareKeyInputImpl(long j, long j2, int i);

    private native boolean remoteControlMutiShareLongPressImpl(long j, long j2, float f, float f2);

    private native boolean remoteControlMutiShareMouseValidateImpl(long j, long j2, float f, float f2);

    private native boolean remoteControlMutiShareSingleMoveImpl(long j, long j2, float f, float f2);

    private native boolean remoteControlMutiShareSingleTapImpl(long j, long j2, float f, float f2);

    private native boolean remoteControlSingleMoveImpl(long j, float f, float f2);

    private native boolean remoteControlSingleTapImpl(long j, float f, float f2);

    private native boolean removePicImpl(long j, long j2, int i);

    private native boolean requestRemoteControlImpl(long j, long j2);

    private native boolean requestToStopPureComputerAudioShareImpl(long j, long j2);

    private native boolean saveSnapshotToPathImpl(long j, long j2, String str);

    private native boolean senderSupportAnnotationImpl(long j, long j2);

    private native boolean setCaptureBitmapDataImpl(long j, Bitmap bitmap);

    private native boolean setCaptureObjectImpl(long j, boolean z);

    private native boolean setCaptureRawDataImpl(long j, int i, int i2, int i3, ByteBuffer byteBuffer);

    private native boolean setColorImpl(long j, long j2, int i);

    private native boolean setLineWidthImpl(long j, long j2, int i);

    private native void setRendererBackgroudColorImpl(long j, long j2, int i);

    private native void setShareEventSinkImpl(long j, long j2);

    private native boolean setToolImpl(long j, long j2, int i);

    private native boolean setViewModeImpl(long j, int i, int i2);

    private native boolean showShareContentImpl(long j, long j2, long j3, boolean z);

    private native boolean startRemoteControlImpl(long j);

    private native boolean startShareImpl(long j);

    private native boolean stopShareImpl(long j);

    private native boolean stopViewShareContentImpl(long j, long j2, boolean z);

    private native boolean switchPageImpl(long j, long j2, long j3);

    private native boolean undoImpl(long j, long j2);

    private native boolean updateRendererInfo(long j, long j2, int i, int i2, int i3, int i4, int i5, int i6);

    public ShareSessionMgr(long j) {
        this.mNativeHandle = j;
    }

    public int getShareStatus() {
        return getShareStatusImpl(this.mNativeHandle);
    }

    public int getShareSettingType() {
        return getShareSettingTypeImpl(this.mNativeHandle);
    }

    public boolean presenterIsSharingAudio() {
        return presenterIsSharingAudioImpl(this.mNativeHandle);
    }

    public void setGLViewSize(int i, int i2) {
        this.mViewWidth = i;
        this.mViewHeight = i2;
    }

    public boolean startShare() {
        return startShareImpl(this.mNativeHandle);
    }

    public boolean setCaptureObject(boolean z) {
        return setCaptureObjectImpl(this.mNativeHandle, z);
    }

    public boolean stopShare() {
        return stopShareImpl(this.mNativeHandle);
    }

    public boolean setCaptureFrame(int i, int i2, int i3, ByteBuffer byteBuffer) {
        synchronized (SHARE_SESSION_LOCK) {
            if (this.mIsConfCleaned) {
                return false;
            }
            boolean captureRawDataImpl = setCaptureRawDataImpl(this.mNativeHandle, i, i2, i3, byteBuffer);
            return captureRawDataImpl;
        }
    }

    public boolean setCaptureFrame(Bitmap bitmap) {
        synchronized (SHARE_SESSION_LOCK) {
            if (this.mIsConfCleaned) {
                return false;
            }
            boolean captureBitmapDataImpl = setCaptureBitmapDataImpl(this.mNativeHandle, bitmap);
            return captureBitmapDataImpl;
        }
    }

    public void setConfCleaned(boolean z) {
        synchronized (SHARE_SESSION_LOCK) {
            this.mIsConfCleaned = z;
        }
    }

    public static void setAnnotateDisableWhenStopShare() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.DisableAttendeeAnnotationForMySharedContent(AnnoDataMgr.getInstance().getAttendeeAnnotateDisable());
        }
    }

    @Nullable
    public ShareUnit createShareUnit(@NonNull RendererUnitInfo rendererUnitInfo) {
        long createRendererInfo = createRendererInfo(this.mNativeHandle, false, 0, this.mViewWidth, this.mViewHeight, rendererUnitInfo.left, rendererUnitInfo.top, rendererUnitInfo.width, rendererUnitInfo.height);
        if (createRendererInfo != 0) {
            if (prepareRenderer(this.mNativeHandle, createRendererInfo)) {
                return new ShareUnit(createRendererInfo, rendererUnitInfo);
            }
            destroyRendererInfo(this.mNativeHandle, createRendererInfo);
        }
        return null;
    }

    public void destroyShareUnit(@Nullable ShareUnit shareUnit) {
        if (shareUnit != null) {
            long rendererInfo = shareUnit.getRendererInfo();
            destroyRenderer(this.mNativeHandle, rendererInfo);
            destroyRendererInfo(this.mNativeHandle, rendererInfo);
        }
    }

    public void updateUnitLayout(long j, @Nullable RendererUnitInfo rendererUnitInfo) {
        if (rendererUnitInfo != null) {
            updateRendererInfo(this.mNativeHandle, j, this.mViewWidth, this.mViewHeight, rendererUnitInfo.left, rendererUnitInfo.top, rendererUnitInfo.width, rendererUnitInfo.height);
        }
    }

    public long addPic(long j, int i, @Nullable Bitmap bitmap, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (j == 0 || bitmap == null || i6 < i4 || i7 < i5) {
            return 0;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        int i8 = width;
        bitmap.getPixels(iArr, 0, width, 0, 0, i8, height);
        return addPicImpl(this.mNativeHandle, j, i, iArr, i8, height, i2, i3, i4, i5, i6, i7);
    }

    public long movePic(long j, int i, long j2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        if (j == 0 || j2 <= 0 || i8 < i6 || i9 < i7) {
            return 0;
        }
        return movePicImpl(this.mNativeHandle, j, i, j2, i2, i3, i4, i5, i6, i7, i8, i9);
    }

    public long movePic2(long j, int i, int i2, int i3, int i4, int i5) {
        if (j == 0) {
            return 0;
        }
        if (i4 < i2 || i5 < i3) {
            return 0;
        }
        return movePic2Impl(this.mNativeHandle, j, i, i2, i3, i4, i5);
    }

    public boolean removePic(long j, int i) {
        if (j == 0) {
            return false;
        }
        return removePicImpl(this.mNativeHandle, j, i);
    }

    public void glViewSizeChanged(long j, int i, int i2) {
        if (j != 0) {
            glViewSizeChangedImpl(this.mNativeHandle, j, i, i2);
        }
    }

    public void destAreaChanged(long j, int i, int i2, int i3, int i4) {
        if (j != 0) {
            destAreaChangedImpl(this.mNativeHandle, j, i, i2, i3, i4);
        }
    }

    public void clearRenderer(long j) {
        if (j != 0) {
            clearRendererImpl(this.mNativeHandle, j);
        }
    }

    public boolean showShareContent(long j, long j2, boolean z) {
        return showShareContentImpl(this.mNativeHandle, j, j2, z);
    }

    public boolean stopViewShareContent(long j, boolean z) {
        return stopViewShareContentImpl(this.mNativeHandle, j, z);
    }

    @NonNull
    public VideoSize getShareDataResolution(long j) {
        long shareDataResolutionImpl = getShareDataResolutionImpl(this.mNativeHandle, j);
        VideoSize videoSize = new VideoSize();
        videoSize.height = (int) ((shareDataResolutionImpl >> 16) & 65535);
        videoSize.width = (int) (shareDataResolutionImpl & 65535);
        return videoSize;
    }

    public boolean setViewMode(int i, int i2) {
        return setViewModeImpl(this.mNativeHandle, i, i2);
    }

    public void setRendererBackgroudColor(long j, int i) {
        setRendererBackgroudColorImpl(this.mNativeHandle, j, i);
    }

    public boolean isVideoSharingInProgress() {
        return isVideoSharingInProgressImpl(this.mNativeHandle);
    }

    public void setShareEventSink(long j) {
        setShareEventSinkImpl(this.mNativeHandle, j);
    }

    public long getActiveUserID() {
        return getActiveUserIDImpl(this.mNativeHandle);
    }

    public boolean senderSupportAnnotation(long j) {
        return senderSupportAnnotationImpl(this.mNativeHandle, j);
    }

    public boolean isAttendeeAnnotationDisabledForMySharedContent() {
        return isAttendeeAnnotationDisabledForMySharedContentImpl(this.mNativeHandle);
    }

    public boolean DisableAttendeeAnnotationForMySharedContent(boolean z) {
        return disableAttendeeAnnotationForMySharedContentImpl(this.mNativeHandle, z);
    }

    public boolean isShowAnnotatorName() {
        return isShowAnnotatorNameImpl(this.mNativeHandle);
    }

    public boolean EnableShowAnnotatorName(boolean z) {
        return enableShowAnnotatorNameImpl(this.mNativeHandle, z);
    }

    public boolean hasRemoteControlPrivilegeWithUserId(long j) {
        return hasRemoteControlPrivilegeWithUserIdImpl(this.mNativeHandle, j);
    }

    public boolean isRemoteController(long j) {
        return isRemoteControllerImpl(this.mNativeHandle, j);
    }

    public boolean startRemoteControl() {
        return startRemoteControlImpl(this.mNativeHandle);
    }

    public boolean grabRemoteControl(long j) {
        return grabRemoteControlImpl(this.mNativeHandle, j);
    }

    public boolean remoteControlSingleTap(float f, float f2) {
        return remoteControlSingleTapImpl(this.mNativeHandle, f, f2);
    }

    public boolean remoteControlDoubleTap(float f, float f2) {
        return remoteControlDoubleTapImpl(this.mNativeHandle, f, f2);
    }

    public boolean remoteControlLongPress(float f, float f2) {
        return remoteControlLongPressImpl(this.mNativeHandle, f, f2);
    }

    public boolean remoteControlDoubleScroll(float f, float f2) {
        return remoteControlDoubleScrollImpl(this.mNativeHandle, f, f2);
    }

    public boolean remoteControlSingleMove(float f, float f2) {
        return remoteControlSingleMoveImpl(this.mNativeHandle, f, f2);
    }

    public boolean remoteControlCharInput(String str) {
        return remoteControlCharInputImpl(this.mNativeHandle, str);
    }

    public boolean remoteControlKeyInput(int i) {
        return remoteControlKeyInputImpl(this.mNativeHandle, i);
    }

    public boolean remoteControlMouseValidate(float f, float f2) {
        return remoteControlMouseValidateImpl(this.mNativeHandle, f, f2);
    }

    public boolean requestRemoteControl(long j) {
        return requestRemoteControlImpl(this.mNativeHandle, j);
    }

    public boolean giveupRemoteControl(long j) {
        return giveupRemoteControlImpl(this.mNativeHandle, j);
    }

    public void declineRemoteControl(long j) {
        declineRemoteControlImpl(this.mNativeHandle, j);
    }

    public boolean assignRemoteControlPrivilege(long j, long j2, boolean z) {
        return assignRemoteControlPrivilegeImpl(this.mNativeHandle, j, j2, z);
    }

    public boolean grabRemoteControllingStatus(long j, long j2, boolean z) {
        return grabRemoteControllingStatusImpl(this.mNativeHandle, j, j2, z);
    }

    public boolean checkHasRemoteControlPrivilege(long j, long j2) {
        return checkHasRemoteControlPrivilegeImpl(this.mNativeHandle, j, j2);
    }

    public boolean isShareSourceInRemoteControllingStatus(long j) {
        return isShareSourceInRemoteControllingStatusImpl(this.mNativeHandle, j);
    }

    public boolean isShareSourceInRemoteControllingStatusByUser(long j, long j2) {
        return isShareSourceInRemoteControllingStatusByUserImpl(this.mNativeHandle, j, j2);
    }

    public boolean remoteControlMutiShareSingleTap(long j, float f, float f2) {
        return remoteControlMutiShareSingleTapImpl(this.mNativeHandle, j, f, f2);
    }

    public boolean remoteControlMutiShareDoubleTap(long j, float f, float f2) {
        return remoteControlMutiShareDoubleTapImpl(this.mNativeHandle, j, f, f2);
    }

    public boolean remoteControlMutiShareLongPress(long j, float f, float f2) {
        return remoteControlMutiShareLongPressImpl(this.mNativeHandle, j, f, f2);
    }

    public boolean remoteControlMutiShareDoubleScroll(long j, float f, float f2) {
        return remoteControlMutiShareDoubleScrollImpl(this.mNativeHandle, j, f, f2);
    }

    public boolean remoteControlMutiShareSingleMove(long j, float f, float f2) {
        return remoteControlMutiShareSingleMoveImpl(this.mNativeHandle, j, f, f2);
    }

    public boolean remoteControlMutiShareCharInput(long j, String str) {
        return remoteControlMutiShareCharInputImpl(this.mNativeHandle, j, str);
    }

    public boolean remoteControlMutiShareKeyInput(long j, int i) {
        return remoteControlMutiShareKeyInputImpl(this.mNativeHandle, j, i);
    }

    public boolean remoteControlMutiShareMouseValidate(long j, float f, float f2) {
        return remoteControlMutiShareMouseValidateImpl(this.mNativeHandle, j, f, f2);
    }

    public boolean isViewingPureComputerAudio() {
        return isViewingPureComputerAudioImpl(this.mNativeHandle);
    }

    public boolean requestToStopPureComputerAudioShare(long j) {
        if (j == 0) {
            return false;
        }
        return requestToStopPureComputerAudioShareImpl(this.mNativeHandle, j);
    }

    public long getPureComputerAudioSharingUserID() {
        return getPureComputerAudioSharingUserIDImpl(this.mNativeHandle);
    }

    public long[] getColorArray(long j) {
        return getColorArrayImpl(this.mNativeHandle, j);
    }

    public boolean isSharingWhiteboard(long j) {
        return isShareWhiteboardImpl(this.mNativeHandle, j);
    }

    public int getCompserVersion(long j) {
        return getCompserVersionImpl(this.mNativeHandle, j);
    }

    public boolean setLineWidth(long j, int i) {
        return setLineWidthImpl(this.mNativeHandle, j, i);
    }

    public int getLineWidth(long j, int i) {
        return getLineWidthImpl(this.mNativeHandle, j, i);
    }

    public boolean setTool(long j, int i) {
        return setToolImpl(this.mNativeHandle, j, i);
    }

    public int getTool(long j) {
        return getToolImpl(this.mNativeHandle, j);
    }

    public boolean setColor(long j, int i) {
        return setColorImpl(this.mNativeHandle, j, i);
    }

    public int getColor(long j, int i) {
        return getColorImpl(this.mNativeHandle, j, i);
    }

    public boolean undo(long j) {
        return undoImpl(this.mNativeHandle, j);
    }

    public boolean redo(long j) {
        return redoImpl(this.mNativeHandle, j);
    }

    public boolean eraser(long j, int i) {
        return eraserImpl(this.mNativeHandle, j, i);
    }

    public boolean saveSnapshotToPath(long j, String str) {
        return saveSnapshotToPathImpl(this.mNativeHandle, j, str);
    }

    public boolean newPage(long j) {
        return newPageImpl(this.mNativeHandle, j);
    }

    public boolean closePage(long j, long j2) {
        return closePageImpl(this.mNativeHandle, j, j2);
    }

    public boolean prevPage(long j) {
        return prevPageImpl(this.mNativeHandle, j);
    }

    public boolean nextPage(long j) {
        return nextPageImpl(this.mNativeHandle, j);
    }

    public boolean switchPage(long j, long j2) {
        return switchPageImpl(this.mNativeHandle, j, j2);
    }

    public int getPageSnapshot(long j, int i) {
        return getPageSnapshotImpl(this.mNativeHandle, j, i);
    }

    @Nullable
    public AnnoPageInfo getAnnoPageNum(long j) {
        return getAnnoPageNumImpl(this.mNativeHandle, j);
    }
}
