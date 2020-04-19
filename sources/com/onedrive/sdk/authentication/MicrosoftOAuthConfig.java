package com.onedrive.sdk.authentication;

import android.net.Uri;
import com.microsoft.services.msa.OAuthConfig;

class MicrosoftOAuthConfig implements OAuthConfig {
    public static final String HTTPS_LOGIN_LIVE_COM = "https://login.microsoftonline.com/common/oauth2/";
    private final Uri mOAuthAuthorizeUri = Uri.parse("https://login.microsoftonline.com/common/oauth2/authorize");
    private final Uri mOAuthDesktopUri = Uri.parse("https://login.microsoftonline.com/common/oauth2/desktop");
    private final Uri mOAuthLogoutUri = Uri.parse("https://login.microsoftonline.com/common/oauth2/logout");
    private final Uri mOAuthTokenUri = Uri.parse("https://login.microsoftonline.com/common/oauth2/token");

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
