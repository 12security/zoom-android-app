package com.microsoft.aad.adal;

import android.net.Uri.Builder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

final class Utility {
    private Utility() {
    }

    static Date getImmutableDateObject(Date date) {
        return date != null ? new Date(date.getTime()) : date;
    }

    static boolean isClaimsChallengePresent(AuthenticationRequest authenticationRequest) {
        return !StringExtensions.isNullOrBlank(authenticationRequest.getClaimsChallenge());
    }

    static URL constructAuthorityUrl(URL url, String str) throws MalformedURLException {
        return new URL(new Builder().scheme(url.getProtocol()).authority(str).appendPath(url.getPath().replaceFirst("/", "")).build().toString());
    }
}
