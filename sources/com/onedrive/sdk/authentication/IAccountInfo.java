package com.onedrive.sdk.authentication;

public interface IAccountInfo {
    String getAccessToken();

    AccountType getAccountType();

    String getServiceRoot();

    boolean isExpired();

    void refresh();
}
