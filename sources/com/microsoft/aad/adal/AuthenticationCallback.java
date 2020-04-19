package com.microsoft.aad.adal;

public interface AuthenticationCallback<T> {
    void onError(Exception exc);

    void onSuccess(T t);
}
