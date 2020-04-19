package org.apache.http.util;

public class Asserts {
    public static void check(boolean z, String str) {
        if (!z) {
            throw new IllegalStateException(str);
        }
    }

    public static void check(boolean z, String str, Object... objArr) {
        if (!z) {
            throw new IllegalStateException(String.format(str, objArr));
        }
    }

    public static void check(boolean z, String str, Object obj) {
        if (!z) {
            throw new IllegalStateException(String.format(str, new Object[]{obj}));
        }
    }

    public static void notNull(Object obj, String str) {
        if (obj == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" is null");
            throw new IllegalStateException(sb.toString());
        }
    }

    public static void notEmpty(CharSequence charSequence, String str) {
        if (TextUtils.isEmpty(charSequence)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" is empty");
            throw new IllegalStateException(sb.toString());
        }
    }

    public static void notBlank(CharSequence charSequence, String str) {
        if (TextUtils.isBlank(charSequence)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" is blank");
            throw new IllegalStateException(sb.toString());
        }
    }
}
