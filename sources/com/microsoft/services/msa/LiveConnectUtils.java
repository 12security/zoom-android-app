package com.microsoft.services.msa;

import android.text.TextUtils;

final class LiveConnectUtils {
    public static void assertNotNull(Object obj, String str) {
        if (TextUtils.isEmpty(str)) {
            throw new AssertionError();
        } else if (obj == null) {
            throw new NullPointerException(String.format(ErrorMessages.NULL_PARAMETER, new Object[]{str}));
        }
    }

    public static void assertNotNullOrEmpty(String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            assertNotNull(str, str2);
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException(String.format(ErrorMessages.EMPTY_PARAMETER, new Object[]{str2}));
            }
            return;
        }
        throw new AssertionError();
    }

    private LiveConnectUtils() {
        throw new AssertionError(ErrorMessages.NON_INSTANTIABLE_CLASS);
    }
}
