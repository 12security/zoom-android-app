package org.apache.http.util;

import java.util.Collection;

public class Args {
    public static void check(boolean z, String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void check(boolean z, String str, Object... objArr) {
        if (!z) {
            throw new IllegalArgumentException(String.format(str, objArr));
        }
    }

    public static void check(boolean z, String str, Object obj) {
        if (!z) {
            throw new IllegalArgumentException(String.format(str, new Object[]{obj}));
        }
    }

    public static <T> T notNull(T t, String str) {
        if (t != null) {
            return t;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" may not be null");
        throw new IllegalArgumentException(sb.toString());
    }

    public static <T extends CharSequence> T notEmpty(T t, String str) {
        if (t == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" may not be null");
            throw new IllegalArgumentException(sb.toString());
        } else if (!TextUtils.isEmpty(t)) {
            return t;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(" may not be empty");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public static <T extends CharSequence> T notBlank(T t, String str) {
        if (t == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" may not be null");
            throw new IllegalArgumentException(sb.toString());
        } else if (!TextUtils.isBlank(t)) {
            return t;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(" may not be blank");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public static <T extends CharSequence> T containsNoBlanks(T t, String str) {
        if (t == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" may not be null");
            throw new IllegalArgumentException(sb.toString());
        } else if (t.length() == 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(" may not be empty");
            throw new IllegalArgumentException(sb2.toString());
        } else if (!TextUtils.containsBlanks(t)) {
            return t;
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(" may not contain blanks");
            throw new IllegalArgumentException(sb3.toString());
        }
    }

    public static <E, T extends Collection<E>> T notEmpty(T t, String str) {
        if (t == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" may not be null");
            throw new IllegalArgumentException(sb.toString());
        } else if (!t.isEmpty()) {
            return t;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(" may not be empty");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public static int positive(int i, String str) {
        if (i > 0) {
            return i;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" may not be negative or zero");
        throw new IllegalArgumentException(sb.toString());
    }

    public static long positive(long j, String str) {
        if (j > 0) {
            return j;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" may not be negative or zero");
        throw new IllegalArgumentException(sb.toString());
    }

    public static int notNegative(int i, String str) {
        if (i >= 0) {
            return i;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" may not be negative");
        throw new IllegalArgumentException(sb.toString());
    }

    public static long notNegative(long j, String str) {
        if (j >= 0) {
            return j;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" may not be negative");
        throw new IllegalArgumentException(sb.toString());
    }
}
