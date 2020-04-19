package org.apache.http.util;

public final class TextUtils {
    public static boolean isEmpty(CharSequence charSequence) {
        boolean z = true;
        if (charSequence == null) {
            return true;
        }
        if (charSequence.length() != 0) {
            z = false;
        }
        return z;
    }

    public static boolean isBlank(CharSequence charSequence) {
        if (charSequence == null) {
            return true;
        }
        for (int i = 0; i < charSequence.length(); i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsBlanks(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        for (int i = 0; i < charSequence.length(); i++) {
            if (Character.isWhitespace(charSequence.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
