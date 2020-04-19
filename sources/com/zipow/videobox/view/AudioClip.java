package com.zipow.videobox.view;

import android.media.AudioTrack;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import java.io.InputStream;

public class AudioClip {
    private static final String TAG = "AudioClip";
    @Nullable
    private PlayThread mPlayThread;
    private int mRawDataResId = 0;
    private int mStreamType = 0;

    static class PlayThread extends Thread {
        AudioClip mAudioClip;
        AudioTrack mAudioTrack;
        int mLoopCount = -1;
        boolean mStopPlay = false;

        PlayThread(AudioClip audioClip, int i) {
            super("PlayThread");
            this.mAudioClip = audioClip;
            this.mLoopCount = i;
        }

        /* access modifiers changed from: 0000 */
        public void stopPlay() {
            this.mStopPlay = true;
        }

        public void run() {
            InputStream openRawResource;
            Throwable th;
            int minBufferSize = AudioTrack.getMinBufferSize(8000, 4, 2);
            if (minBufferSize <= 0) {
                this.mStopPlay = true;
                return;
            }
            try {
                AudioTrack audioTrack = new AudioTrack(this.mAudioClip.getStreamType(), 8000, 4, 2, minBufferSize, 1);
                this.mAudioTrack = audioTrack;
                this.mAudioTrack.play();
                try {
                    openRawResource = VideoBoxApplication.getNonNullInstance().getResources().openRawResource(this.mAudioClip.getRawDataResId());
                    byte[] bArr = new byte[openRawResource.available()];
                    int read = openRawResource.read(bArr);
                    if (openRawResource != null) {
                        openRawResource.close();
                    }
                    if (read > 0) {
                        int i = 0;
                        int i2 = 0;
                        do {
                            int i3 = read - i;
                            if (i3 >= 1600) {
                                i3 = 1600;
                            }
                            if (i3 > 0) {
                                if (this.mAudioTrack.getState() == 1 && this.mAudioTrack.getPlayState() == 3) {
                                    this.mAudioTrack.write(bArr, i, i3);
                                } else {
                                    try {
                                        Thread.sleep((long) ((i3 * 100) / 1600));
                                    } catch (InterruptedException unused) {
                                    }
                                }
                            }
                            i += i3;
                            if (i >= read) {
                                i2++;
                                int i4 = this.mLoopCount;
                                if (i4 > 0 && i4 <= i2) {
                                    this.mStopPlay = true;
                                }
                                i = 0;
                            }
                        } while (!this.mStopPlay);
                        try {
                            this.mAudioTrack.pause();
                            this.mAudioTrack.flush();
                            this.mAudioTrack.stop();
                            this.mAudioTrack.release();
                        } catch (IllegalStateException unused2) {
                            return;
                        }
                    }
                    return;
                } catch (Exception unused3) {
                    this.mAudioTrack.stop();
                    this.mAudioTrack.release();
                    return;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            } catch (Exception unused4) {
                this.mStopPlay = true;
                return;
            }
            throw th;
        }
    }

    public AudioClip(int i, int i2) {
        this.mRawDataResId = i;
        this.mStreamType = i2;
        if (this.mStreamType < 0) {
            this.mStreamType = 0;
        }
    }

    public void startPlay() {
        startPlay(-1);
    }

    public void startPlay(int i) {
        PlayThread playThread = this.mPlayThread;
        if (playThread == null || !playThread.isAlive()) {
            this.mPlayThread = new PlayThread(this, i);
            this.mPlayThread.start();
        }
    }

    public boolean isPlaying() {
        PlayThread playThread = this.mPlayThread;
        return playThread != null && playThread.isAlive();
    }

    public void stopPlay() {
        PlayThread playThread = this.mPlayThread;
        if (playThread != null && playThread.isAlive()) {
            this.mPlayThread.stopPlay();
        }
        this.mPlayThread = null;
    }

    public int getRawDataResId() {
        return this.mRawDataResId;
    }

    public int getStreamType() {
        return this.mStreamType;
    }
}
