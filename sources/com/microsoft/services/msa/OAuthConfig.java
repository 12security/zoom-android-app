package com.microsoft.services.msa;

import android.net.Uri;

public interface OAuthConfig {
    Uri getAuthorizeUri();

    Uri getDesktopUri();

    Uri getLogoutUri();

    Uri getTokenUri();
}
