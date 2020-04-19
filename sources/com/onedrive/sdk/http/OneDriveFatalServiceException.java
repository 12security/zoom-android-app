package com.onedrive.sdk.http;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class OneDriveFatalServiceException extends OneDriveServiceException {
    public static final String SDK_BUG_URL = "https://github.com/OneDrive/onedrive-sdk-android/issues";

    protected OneDriveFatalServiceException(String str, String str2, List<String> list, String str3, int i, String str4, List<String> list2, OneDriveErrorResponse oneDriveErrorResponse) {
        super(str, str2, list, str3, i, str4, list2, oneDriveErrorResponse);
    }

    public String getMessage(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("[This is an unexpected error from OneDrive, please report this at ");
        sb.append(SDK_BUG_URL);
        Iterator it = getResponseHeaders().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String str = (String) it.next();
            if (str.toLowerCase(Locale.ROOT).startsWith("x-throwsite")) {
                sb.append(", ID = ");
                sb.append(str);
                break;
            }
        }
        sb.append(']');
        sb.append(10);
        sb.append(super.getMessage(true));
        return sb.toString();
    }
}
