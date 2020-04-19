package com.microsoft.services.msa;

import android.net.Uri;

public class MicrosoftOAuthConfig implements OAuthConfig {
    public static final String HTTPS_LOGIN_LIVE_COM = "https://login.live.com/";
    private static MicrosoftOAuthConfig sInstance;
    private Uri mOAuthAuthorizeUri = Uri.parse("https://login.live.com/oauth20_authorize.srf");
    private Uri mOAuthDesktopUri = Uri.parse("https://login.live.com/oauth20_desktop.srf");
    private Uri mOAuthLogoutUri = Uri.parse("https://login.live.com/oauth20_logout.srf");
    private Uri mOAuthTokenUri = Uri.parse("https://login.live.com/oauth20_token.srf");

    public static MicrosoftOAuthConfig getInstance() {
        if (sInstance == null) {
            sInstance = new MicrosoftOAuthConfig();
        }
        return sInstance;
    }

    public Uri getAuthorizeUri() {
        return this.mOAuthAuthorizeUri;
    }

    public Uri getDesktopUri() {
        return this.mOAuthDesktopUri;
    }

    public Uri getLogoutUri() {
        return this.mOAuthLogoutUri;
    }

    public Uri getTokenUri() {
        return this.mOAuthTokenUri;
    }
}
