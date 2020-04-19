package com.microsoft.aad.adal;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.atomic.AtomicReference;

final class CallbackExecutor<T> {
    private static final String TAG = "CallbackExecutor";
    private final AtomicReference<Callback<T>> mCallbackReference = new AtomicReference<>(null);
    private final Handler mHandler;

    CallbackExecutor(Callback<T> callback) {
        Handler handler = null;
        if (Looper.myLooper() != null) {
            handler = new Handler();
        }
        this.mHandler = handler;
        this.mCallbackReference.set(callback);
    }

    public void onSuccess(final T t) {
        final Callback callback = (Callback) this.mCallbackReference.getAndSet(null);
        if (callback == null) {
            Logger.m236v(TAG, "Callback does not exist.");
            return;
        }
        Handler handler = this.mHandler;
        if (handler == null) {
            callback.onSuccess(t);
        } else {
            handler.post(new Runnable() {
                public void run() {
                    callback.onSuccess(t);
                }
            });
        }
    }

    public void onError(final Throwable th) {
        final Callback callback = (Callback) this.mCallbackReference.getAndSet(null);
        if (callback == null) {
            Logger.m236v(TAG, "Callback does not exist.");
            return;
        }
        Handler handler = this.mHandler;
        if (handler == null) {
            callback.onError(th);
        } else {
            handler.post(new Runnable() {
                public void run() {
                    callback.onError(th);
                }
            });
        }
    }
}
