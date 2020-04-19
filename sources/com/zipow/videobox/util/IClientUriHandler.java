package com.zipow.videobox.util;

import android.app.Activity;
import android.net.Uri;

public interface IClientUriHandler {
    boolean handleUri(Activity activity, Uri uri);
}
