package com.dropbox.core.p005v2.callbacks;

/* renamed from: com.dropbox.core.v2.callbacks.DbxGlobalCallbackFactory */
public interface DbxGlobalCallbackFactory {
    DbxNetworkErrorCallback createNetworkErrorCallback(String str);

    <T> DbxRouteErrorCallback<T> createRouteErrorCallback(String str, T t);
}
