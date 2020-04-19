package com.zipow.videobox.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.fragment.WebViewFragment;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AndroidAppUtil;

public class ZMWebPageUtil {
    public static void startWebPage(@NonNull Fragment fragment, String str, String str2) {
        if (fragment != null) {
            Context context = fragment.getContext();
            if (context == null || !AndroidAppUtil.hasBrowserApp(context)) {
                startWithWebview(fragment, str, str2);
            } else {
                Uri parse = Uri.parse(str);
                if (parse != null) {
                    Intent intent = new Intent("android.intent.action.VIEW", parse);
                    intent.addFlags(268435456);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    try {
                        ActivityStartHelper.startActivityForeground(context, intent);
                    } catch (Exception unused) {
                        startWithWebview(fragment, str, str2);
                    }
                }
            }
        }
    }

    public static void startWebPage(@NonNull ZMActivity zMActivity, String str, String str2) {
        if (AndroidAppUtil.hasBrowserApp(zMActivity)) {
            Uri parse = Uri.parse(str);
            if (parse != null) {
                Intent intent = new Intent("android.intent.action.VIEW", parse);
                intent.addFlags(268435456);
                intent.addCategory("android.intent.category.BROWSABLE");
                try {
                    ActivityStartHelper.startActivityForeground(zMActivity, intent);
                } catch (Exception unused) {
                    startWithWebview(zMActivity, str, str2);
                }
            }
        } else {
            startWithWebview(zMActivity, str, str2);
        }
    }

    private static void startWithWebview(@NonNull Fragment fragment, String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("url", str);
        bundle.putString("title", str2);
        WebViewFragment.showAsActivity(fragment, bundle);
    }

    private static void startWithWebview(@NonNull ZMActivity zMActivity, String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("url", str);
        bundle.putString("title", str2);
        WebViewFragment.show(zMActivity, bundle);
    }
}
