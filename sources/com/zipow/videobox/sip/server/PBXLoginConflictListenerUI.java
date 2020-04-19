package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class PBXLoginConflictListenerUI {
    private static final String TAG = PBXLoginConflictListener.class.getSimpleName();
    @Nullable
    private static PBXLoginConflictListenerUI instance = null;
    @NonNull
    private ListenerList mListenerList = new ListenerList();

    public interface PBXLoginConflictListener extends IListener {
        void onConflict();

        void onResumeFromConflict();
    }

    public static class SimplePBXLoginConflictListener implements PBXLoginConflictListener {
        public void onConflict() {
        }

        public void onResumeFromConflict() {
        }
    }

    @NonNull
    public static synchronized PBXLoginConflictListenerUI getInstance() {
        PBXLoginConflictListenerUI pBXLoginConflictListenerUI;
        synchronized (PBXLoginConflictListenerUI.class) {
            if (instance == null) {
                instance = new PBXLoginConflictListenerUI();
            }
            pBXLoginConflictListenerUI = instance;
        }
        return pBXLoginConflictListenerUI;
    }

    public void addListener(@Nullable PBXLoginConflictListener pBXLoginConflictListener) {
        if (pBXLoginConflictListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == pBXLoginConflictListener) {
                    removeListener((PBXLoginConflictListener) all[i]);
                }
            }
            this.mListenerList.add(pBXLoginConflictListener);
        }
    }

    public void removeListener(PBXLoginConflictListener pBXLoginConflictListener) {
        this.mListenerList.remove(pBXLoginConflictListener);
    }

    public void handleOnConflict() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((PBXLoginConflictListener) iListener).onConflict();
            }
        }
    }

    public void handleOnResumeFromConflict() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((PBXLoginConflictListener) iListener).onResumeFromConflict();
            }
        }
    }
}
