package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.sip.server.ISIPAudioFilePlayerEventSinkListenerUI.ISIPAudioFilePlayerEventSinkListener;
import p021us.zoom.androidlib.util.StringUtil;

public class CmmSIPAudioFilePlayerManager {
    private static final String TAG = CmmSIPCallManager.class.getSimpleName();
    private static CmmSIPAudioFilePlayerManager instance;

    public static CmmSIPAudioFilePlayerManager getInstance() {
        if (instance == null) {
            synchronized (CmmSIPAudioFilePlayerManager.class) {
                if (instance == null) {
                    instance = new CmmSIPAudioFilePlayerManager();
                }
            }
        }
        return instance;
    }

    private CmmSIPAudioFilePlayerManager() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer != null) {
            audioFilePlayer.setEventSink(ISIPAudioFilePlayerEventSinkListenerUI.getInstance());
        }
    }

    public void initPlayer() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer != null) {
            audioFilePlayer.initPlayer();
        }
    }

    public void startPlay(String str) {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer != null) {
            if (!audioFilePlayer.isPlayerInited()) {
                audioFilePlayer.initPlayer();
            }
            if (!StringUtil.isEmptyOrNull(audioFilePlayer.getPlayingFileName())) {
                audioFilePlayer.releasePlayer();
                audioFilePlayer.initPlayer();
            }
            audioFilePlayer.startPlayFile(str);
        }
    }

    public boolean isPlaying() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        return audioFilePlayer != null && audioFilePlayer.isPlaying();
    }

    public void pausePlay() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer != null && audioFilePlayer.isPlaying()) {
            audioFilePlayer.pausePaly();
        }
    }

    public void resumePlay() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer != null && audioFilePlayer.isPalyPaused()) {
            audioFilePlayer.resumePlay();
        }
    }

    public boolean isPlayPaused() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        return audioFilePlayer != null && audioFilePlayer.isPalyPaused();
    }

    public void stopPlay() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer == null) {
            return;
        }
        if (audioFilePlayer.isPlaying() || audioFilePlayer.isPalyPaused()) {
            audioFilePlayer.stopPlay();
        }
    }

    public void releasePlayer() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer != null) {
            audioFilePlayer.releasePlayer();
        }
    }

    public int getCurrentProgress() {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer != null) {
            return audioFilePlayer.getCurrentProgress();
        }
        return 0;
    }

    public boolean changePlayProgress(int i) {
        ISIPAudioFilePlayer audioFilePlayer = getAudioFilePlayer();
        if (audioFilePlayer != null) {
            return audioFilePlayer.changePlayProgress(i);
        }
        return false;
    }

    private ISIPAudioFilePlayer getAudioFilePlayer() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        return sipCallAPI.getAudioFilePlayer();
    }

    public void addSIPAudioFilePlayerEventSinkListener(@Nullable ISIPAudioFilePlayerEventSinkListener iSIPAudioFilePlayerEventSinkListener) {
        if (iSIPAudioFilePlayerEventSinkListener != null) {
            ISIPAudioFilePlayerEventSinkListenerUI.getInstance().addListener(iSIPAudioFilePlayerEventSinkListener);
        }
    }

    public void removeSIPAudioFilePlayerEventSinkListener(@Nullable ISIPAudioFilePlayerEventSinkListener iSIPAudioFilePlayerEventSinkListener) {
        if (iSIPAudioFilePlayerEventSinkListener != null) {
            ISIPAudioFilePlayerEventSinkListenerUI.getInstance().removeListener(iSIPAudioFilePlayerEventSinkListener);
        }
    }
}
