package com.dropbox.core.p004v1;

import com.dropbox.core.util.StringUtil;

/* renamed from: com.dropbox.core.v1.DbxPathV1 */
public class DbxPathV1 {
    public static boolean isValid(String str) {
        return findError(str) == null;
    }

    public static String findError(String str) {
        if (!str.startsWith("/")) {
            return "must start with \"/\"";
        }
        if (str.length() != 1 && str.endsWith("/")) {
            return "must not end with \"/\"";
        }
        return null;
    }

    public static void checkArg(String str, String str2) {
        if (str2 != null) {
            String findError = findError(str2);
            if (findError != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("'");
                sb.append(str);
                sb.append("': bad path: ");
                sb.append(findError);
                sb.append(": ");
                sb.append(StringUtil.m33jq(str2));
                throw new IllegalArgumentException(sb.toString());
            }
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("'");
        sb2.append(str);
        sb2.append("' should not be null");
        throw new IllegalArgumentException(sb2.toString());
    }

    public static void checkArgNonRoot(String str, String str2) {
        if (!"/".equals(str2)) {
            checkArg(str, str2);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(str);
        sb.append("' should not be the root path (\"/\")");
        throw new IllegalArgumentException(sb.toString());
    }

    public static String getName(String str) {
        if (str == null) {
            throw new IllegalArgumentException("'path' can't be null");
        } else if (!str.startsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append("Not a valid path.  Doesn't start with a \"/\": \"");
            sb.append(str);
            sb.append("\"");
            throw new IllegalArgumentException(sb.toString());
        } else if (str.length() <= 1 || !str.endsWith("/")) {
            int length = str.length() - 1;
            while (str.charAt(length) != '/') {
                length--;
            }
            return str.substring(length + 1);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Not a valid path.  Ends with a \"/\": \"");
            sb2.append(str);
            sb2.append("\"");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public static String[] split(String str) {
        if (str == null) {
            throw new IllegalArgumentException("'path' can't be null");
        } else if (!str.startsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append("Not a valid path.  Doesn't start with a \"/\": \"");
            sb.append(str);
            sb.append("\"");
            throw new IllegalArgumentException(sb.toString());
        } else if (str.length() > 1 && str.endsWith("/")) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Not a valid path.  Ends with a \"/\": \"");
            sb2.append(str);
            sb2.append("\"");
            throw new IllegalArgumentException(sb2.toString());
        } else if (str.length() == 1) {
            return new String[0];
        } else {
            return str.substring(1).split("/");
        }
    }

    public static String getParent(String str) {
        if (str == null) {
            throw new IllegalArgumentException("'path' can't be null");
        } else if (!str.startsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append("Not a valid path.  Doesn't start with a \"/\": \"");
            sb.append(str);
            sb.append("\"");
            throw new IllegalArgumentException(sb.toString());
        } else if (str.length() <= 1 || !str.endsWith("/")) {
            int lastIndexOf = str.lastIndexOf("/");
            if (str.length() == 1) {
                return null;
            }
            if (lastIndexOf == 0) {
                return "/";
            }
            return str.substring(0, lastIndexOf);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Not a valid path.  Ends with a \"/\": \"");
            sb2.append(str);
            sb2.append("\"");
            throw new IllegalArgumentException(sb2.toString());
        }
    }
}
