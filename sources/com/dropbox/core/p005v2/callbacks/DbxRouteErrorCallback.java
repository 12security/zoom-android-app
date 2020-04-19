package com.dropbox.core.p005v2.callbacks;

/* renamed from: com.dropbox.core.v2.callbacks.DbxRouteErrorCallback */
public abstract class DbxRouteErrorCallback<T> implements Runnable {
    private T routeError = null;

    public T getRouteError() {
        return this.routeError;
    }

    public void setRouteError(T t) {
        this.routeError = t;
    }
}
