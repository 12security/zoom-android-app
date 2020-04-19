package com.microsoft.services.msa;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.Log;
import java.util.Iterator;
import java.util.LinkedList;

class UriBuilder {
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";
    private static final char FORWARD_SLASH = '/';
    private String host;
    private StringBuilder path;
    private final LinkedList<QueryParameter> queryParameters = new LinkedList<>();
    private String scheme;

    public static class QueryParameter {
        private final String key;
        private final String value;

        public QueryParameter(String str) {
            if (str != null) {
                this.key = str;
                this.value = null;
                return;
            }
            throw new AssertionError();
        }

        public QueryParameter(String str, String str2) {
            if (str == null) {
                throw new AssertionError();
            } else if (str2 != null) {
                this.key = str;
                this.value = str2;
            } else {
                throw new AssertionError();
            }
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

        public boolean hasValue() {
            return this.value != null;
        }

        public String toString() {
            if (!hasValue()) {
                return this.key;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.key);
            sb.append(UriBuilder.EQUAL);
            sb.append(this.value);
            return sb.toString();
        }
    }

    public static UriBuilder newInstance(Uri uri) {
        return new UriBuilder().scheme(uri.getScheme()).host(uri.getHost()).path(uri.getPath()).query(uri.getQuery());
    }

    public UriBuilder appendQueryParameter(String str, String str2) {
        if (str == null) {
            throw new AssertionError();
        } else if (str2 != null) {
            this.queryParameters.add(new QueryParameter(str, str2));
            return this;
        } else {
            throw new AssertionError();
        }
    }

    public UriBuilder appendQueryString(String str) {
        String[] split;
        if (str == null) {
            return this;
        }
        for (String str2 : TextUtils.split(str, AMPERSAND)) {
            String[] split2 = TextUtils.split(str2, EQUAL);
            if (split2.length == 2) {
                this.queryParameters.add(new QueryParameter(split2[0], split2[1]));
            } else if (split2.length == 1) {
                this.queryParameters.add(new QueryParameter(split2[0]));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid query parameter: ");
                sb.append(str2);
                Log.w("live.auth.UriBuilder", sb.toString());
            }
        }
        return this;
    }

    public UriBuilder appendToPath(String str) {
        boolean z;
        if (str != null) {
            StringBuilder sb = this.path;
            if (sb == null) {
                this.path = new StringBuilder(str);
            } else {
                boolean z2 = false;
                if (!TextUtils.isEmpty(sb)) {
                    StringBuilder sb2 = this.path;
                    if (sb2.charAt(sb2.length() - 1) == '/') {
                        z = true;
                        boolean isEmpty = TextUtils.isEmpty(str);
                        if (!isEmpty && str.charAt(0) == '/') {
                            z2 = true;
                        }
                        if (z || !z2) {
                            if (!z || z2) {
                                this.path.append(str);
                            } else if (!isEmpty) {
                                StringBuilder sb3 = this.path;
                                sb3.append('/');
                                sb3.append(str);
                            }
                        } else if (str.length() > 1) {
                            this.path.append(str.substring(1));
                        }
                    }
                }
                z = false;
                boolean isEmpty2 = TextUtils.isEmpty(str);
                z2 = true;
                if (z) {
                }
                if (!z) {
                }
                this.path.append(str);
            }
            return this;
        }
        throw new AssertionError();
    }

    public Uri build() {
        Builder authority = new Builder().scheme(this.scheme).authority(this.host);
        StringBuilder sb = this.path;
        return authority.path(sb == null ? "" : sb.toString()).encodedQuery(TextUtils.join(AMPERSAND, this.queryParameters)).build();
    }

    public UriBuilder host(String str) {
        if (str != null) {
            this.host = str;
            return this;
        }
        throw new AssertionError();
    }

    public UriBuilder path(String str) {
        if (str != null) {
            this.path = new StringBuilder(str);
            return this;
        }
        throw new AssertionError();
    }

    public UriBuilder query(String str) {
        this.queryParameters.clear();
        return appendQueryString(str);
    }

    public UriBuilder removeQueryParametersWithKey(String str) {
        Iterator it = this.queryParameters.iterator();
        while (it.hasNext()) {
            if (((QueryParameter) it.next()).getKey().equals(str)) {
                it.remove();
            }
        }
        return this;
    }

    public UriBuilder scheme(String str) {
        if (str != null) {
            this.scheme = str;
            return this;
        }
        throw new AssertionError();
    }

    public String toString() {
        return build().toString();
    }
}
