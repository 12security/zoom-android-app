package com.dropbox.core.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import java.util.Arrays;

public class Auth {
    public static void startOAuth2Authentication(Context context, String str) {
        startOAuth2Authentication(context, str, null, null, null);
    }

    public static void startOAuth2Authentication(Context context, String str, String str2, String[] strArr, String str3) {
        startOAuth2Authentication(context, str, str2, strArr, str3, "www.dropbox.com");
    }

    public static void startOAuth2Authentication(Context context, String str, String str2, String[] strArr, String str3, String str4) {
        if (AuthActivity.checkAppBeforeAuth(context, str, true)) {
            if (strArr == null || !Arrays.asList(strArr).contains(str2)) {
                Intent makeIntent = AuthActivity.makeIntent(context, str, str2, strArr, str3, str4, "1");
                if (!(context instanceof Activity)) {
                    makeIntent.addFlags(268435456);
                }
                context.startActivity(makeIntent);
                return;
            }
            throw new IllegalArgumentException("desiredUid cannot be present in alreadyAuthedUids");
        }
    }

    public static String getOAuth2Token() {
        Intent intent = AuthActivity.result;
        if (intent == null) {
            return null;
        }
        String stringExtra = intent.getStringExtra(AuthActivity.EXTRA_ACCESS_TOKEN);
        String stringExtra2 = intent.getStringExtra(AuthActivity.EXTRA_ACCESS_SECRET);
        String stringExtra3 = intent.getStringExtra(AuthActivity.EXTRA_UID);
        if (stringExtra == null || stringExtra.equals("") || stringExtra2 == null || stringExtra2.equals("") || stringExtra3 == null || stringExtra3.equals("")) {
            return null;
        }
        return stringExtra2;
    }

    public static String getUid() {
        Intent intent = AuthActivity.result;
        if (intent == null) {
            return null;
        }
        String stringExtra = intent.getStringExtra(AuthActivity.EXTRA_ACCESS_TOKEN);
        String stringExtra2 = intent.getStringExtra(AuthActivity.EXTRA_ACCESS_SECRET);
        String stringExtra3 = intent.getStringExtra(AuthActivity.EXTRA_UID);
        if (stringExtra == null || stringExtra.equals("") || stringExtra2 == null || stringExtra2.equals("") || stringExtra3 == null || stringExtra3.equals("")) {
            return null;
        }
        return stringExtra3;
    }
}
