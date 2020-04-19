package com.microsoft.aad.adal;

interface Callback<T> {
    void onError(Throwable th);

    void onSuccess(T t);
}
