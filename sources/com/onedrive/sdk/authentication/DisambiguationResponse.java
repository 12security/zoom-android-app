package com.onedrive.sdk.authentication;

class DisambiguationResponse {
    private final String mAccount;
    private final AccountType mAccountType;

    public DisambiguationResponse(AccountType accountType, String str) {
        this.mAccountType = accountType;
        this.mAccount = str;
    }

    public AccountType getAccountType() {
        return this.mAccountType;
    }

    public String getAccount() {
        return this.mAccount;
    }
}
