package com.zipow.videobox.poll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public abstract class AbsPollingMgr implements IPollingMgr {
    @NonNull
    private ListenerList mListeners = new ListenerList();

    @Nullable
    public IPollingDoc getPollingAtIdx(int i) {
        return null;
    }

    public int getPollingCount() {
        return 0;
    }

    @Nullable
    public IPollingDoc getPollingDocById(String str) {
        return null;
    }

    public boolean submitPoll(String str) {
        return false;
    }

    public void addListener(IPollingListener iPollingListener) {
        this.mListeners.add(iPollingListener);
    }

    public void removeListener(IPollingListener iPollingListener) {
        this.mListeners.remove(iPollingListener);
    }

    /* access modifiers changed from: protected */
    public void notifySubmitResult(String str, int i) {
        for (IListener iListener : this.mListeners.getAll()) {
            ((IPollingListener) iListener).onPollingSubmitResult(str, i);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyPollingStatusChanged(String str, int i) {
        for (IListener iListener : this.mListeners.getAll()) {
            ((IPollingListener) iListener).onPollingStatusChanged(str, i);
        }
    }

    @NonNull
    public PollingRole getPollingRole() {
        return PollingRole.Host;
    }
}
