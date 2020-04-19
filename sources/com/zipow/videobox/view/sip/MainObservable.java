package com.zipow.videobox.view.sip;

import androidx.annotation.NonNull;
import java.util.Observable;

public class MainObservable extends Observable {
    private static final MainObservable instance = new MainObservable();

    private MainObservable() {
    }

    @NonNull
    public static MainObservable getInstance() {
        return instance;
    }

    public void notifyObservers(Object obj) {
        setChanged();
        super.notifyObservers(obj);
    }
}
