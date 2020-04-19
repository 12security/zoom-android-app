package com.zipow.videobox.login.model;

import androidx.annotation.Nullable;

public interface IMultiLoginListener {
    void afterCallLoginAPISuccess(int i, boolean z);

    void beforeShowLoginUI(int i, boolean z);

    boolean isUiAuthorizing();

    void onAuthFailed(@Nullable String str);

    void showAuthorizing(boolean z);
}
