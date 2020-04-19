package com.dropbox.core.p005v2;

/* renamed from: com.dropbox.core.v2.DbxPathV2 */
public class DbxPathV2 {
    public static boolean isValid(String str) {
        return findError(str) == null;
    }

    public static String findError(String str) {
        if (str.length() == 0) {
            return null;
        }
        if (!str.startsWith("/")) {
            return "expecting first character to be \"/\"";
        }
        if (str.endsWith("/")) {
            return "must not end with \"/\"";
        }
        return null;
    }

    public static String getName(String str) {
        if (str == null) {
            throw new IllegalArgumentException("'path' can't be null");
        } else if (str.length() == 0) {
            return null;
        } else {
            if (!str.startsWith("/")) {
                StringBuilder sb = new StringBuilder();
                sb.append("Not a valid path.  Doesn't start with a \"/\": \"");
                sb.append(str);
                sb.append("\"");
                throw new IllegalArgumentException(sb.toString());
            } else if (!str.endsWith("/")) {
                int length = str.length();
                while (true) {
                    length--;
                    if (str.charAt(length) == '/') {
                        return str.substring(length + 1);
                    }
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Not a valid path.  Ends with a \"/\": \"");
                sb2.append(str);
                sb2.append("\"");
                throw new IllegalArgumentException(sb2.toString());
            }
        }
    }

    public static String[] split(String str) {
        if (str == null) {
            throw new IllegalArgumentException("'path' can't be null");
        } else if (str.length() == 0) {
            return new String[0];
        } else {
            if (!str.startsWith("/")) {
                StringBuilder sb = new StringBuilder();
                sb.append("Not a valid path.  Doesn't start with a \"/\": \"");
                sb.append(str);
                sb.append("\"");
                throw new IllegalArgumentException(sb.toString());
            } else if (!str.endsWith("/")) {
                return str.substring(1).split("/");
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Not a valid path.  Ends with a \"/\": \"");
                sb2.append(str);
                sb2.append("\"");
                throw new IllegalArgumentException(sb2.toString());
            }
        }
    }

    public static String getParent(String str) {
        if (str == null) {
            throw new IllegalArgumentException("'path' can't be null");
        } else if (str.length() == 0) {
            return null;
        } else {
            if (!str.startsWith("/")) {
                StringBuilder sb = new StringBuilder();
                sb.append("Not a valid path.  Doesn't start with a \"/\": \"");
                sb.append(str);
                sb.append("\"");
                throw new IllegalArgumentException(sb.toString());
            } else if (!str.endsWith("/")) {
                return str.substring(0, str.lastIndexOf("/"));
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Not a valid path.  Ends with a \"/\": \"");
                sb2.append(str);
                sb2.append("\"");
                throw new IllegalArgumentException(sb2.toString());
            }
        }
    }
}
