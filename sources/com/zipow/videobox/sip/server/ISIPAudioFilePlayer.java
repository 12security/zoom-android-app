package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

public class ISIPAudioFilePlayer {
    private long mNativeHandle;

    private native boolean changePlayProgressImpl(long j, int i);

    private native int getCurrentProgressImpl(long j);

    @Nullable
    private native String getPlayingFileNameImpl(long j);

    private native boolean initPlayerImpl(long j);

    private native boolean isPalyPausedImpl(long j);

    private native boolean isPlayerInitedImpl(long j);

    private native boolean isPlayingImpl(long j);

    private native boolean pausePalyImpl(long j);

    private native boolean releasePlayerImpl(long j);

    private native boolean resumePlayImpl(long j);

    private native void setEventSinkImpl(long j, long j2);

    private native boolean startPlayFileImpl(long j, String str);

    private native boolean stopPlayImpl(long j);

    public ISIPAudioFilePlayer(long j) {
        this.mNativeHandle = j;
    }

    public void setEventSink(@NonNull ISIPAudioFilePlayerEventSinkListenerUI iSIPAudioFilePlayerEventSinkListenerUI) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setEventSinkImpl(j, iSIPAudioFilePlayerEventSinkListenerUI.getNativeHandle());
        }
    }

    public boolean initPlayer() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return initPlayerImpl(j);
    }

    public boolean isPlayerInited() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPlayerInitedImpl(j);
    }

    public boolean releasePlayer() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return releasePlayerImpl(j);
    }

    public boolean isPlaying() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPlayingImpl(j);
    }

    @Nullable
    public String getPlayingFileName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPlayingFileNameImpl(j);
    }

    public boolean startPlayFile(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return startPlayFileImpl(j, StringUtil.safeString(str));
    }

    public boolean stopPlay() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return stopPlayImpl(j);
    }

    public boolean pausePaly() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return pausePalyImpl(j);
    }

    public boolean resumePlay() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return resumePlayImpl(j);
    }

    public boolean isPalyPaused() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPalyPausedImpl(j);
    }

    public int getCurrentProgress() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCurrentProgressImpl(j);
    }

    public boolean changePlayProgress(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return changePlayProgressImpl(j, i);
    }
}
