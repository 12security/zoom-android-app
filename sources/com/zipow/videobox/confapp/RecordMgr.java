package com.zipow.videobox.confapp;

import androidx.annotation.Nullable;

public class RecordMgr {
    private static final String TAG = "RecordMgr";
    private long mNativeHandle = 0;
    private int mViewHeight = 0;
    private int mViewWidth = 0;

    private native void agreeContinueRecordingImpl(long j);

    private native boolean canControlCMRImpl(long j);

    private native boolean canStartCMRImpl(long j);

    private native boolean canStartRecordImpl(long j);

    private native void disagreeContinueRecordingImpl(long j);

    @Nullable
    private native String getCurrentRecPathImpl(long j);

    private native boolean isCMRInProgressImpl(long j);

    private native boolean isCMRPausedImpl(long j);

    private native boolean isLocalRecordingInProgressImpl(long j);

    private native boolean isRecordingInProgressImpl(long j);

    private native boolean pauseCMRImpl(long j);

    private native boolean recordingMeetingOnCloudImpl(long j);

    private native boolean resumeCMRImpl(long j);

    private native boolean startCMRImpl(long j);

    private native boolean stopRecordImpl(long j, boolean z);

    private native boolean theMeetingisBeingRecordingImpl(long j);

    public RecordMgr(long j) {
        this.mNativeHandle = j;
    }

    public boolean isRecordingInProgress() {
        return isRecordingInProgressImpl(this.mNativeHandle);
    }

    public boolean theMeetingisBeingRecording() {
        return theMeetingisBeingRecordingImpl(this.mNativeHandle);
    }

    public boolean canStartCMR() {
        return canStartCMRImpl(this.mNativeHandle);
    }

    public boolean canStartRecord() {
        return canStartRecordImpl(this.mNativeHandle);
    }

    public boolean isCMRInProgress() {
        return isCMRInProgressImpl(this.mNativeHandle);
    }

    public boolean isLocalRecordingInProgress() {
        return isLocalRecordingInProgressImpl(this.mNativeHandle);
    }

    @Nullable
    public String getCurrentRecPath() {
        return getCurrentRecPathImpl(this.mNativeHandle);
    }

    public boolean recordingMeetingOnCloud() {
        return recordingMeetingOnCloudImpl(this.mNativeHandle);
    }

    public boolean startCMR() {
        return startCMRImpl(this.mNativeHandle);
    }

    public boolean stopRecord(boolean z) {
        return stopRecordImpl(this.mNativeHandle, z);
    }

    public boolean pauseCMR() {
        return pauseCMRImpl(this.mNativeHandle);
    }

    public boolean resumeCMR() {
        return resumeCMRImpl(this.mNativeHandle);
    }

    public boolean canControlCMR() {
        return canControlCMRImpl(this.mNativeHandle);
    }

    public boolean isCMRPaused() {
        return isCMRPausedImpl(this.mNativeHandle);
    }

    public void agreeContinueRecording() {
        agreeContinueRecordingImpl(this.mNativeHandle);
    }

    public void disagreeContinueRecording() {
        disagreeContinueRecordingImpl(this.mNativeHandle);
    }
}
