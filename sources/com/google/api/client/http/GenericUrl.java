package com.google.api.client.http;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.escape.CharEscapers;
import com.google.api.client.util.escape.Escaper;
import com.google.api.client.util.escape.PercentEscaper;
import com.zipow.videobox.util.TextCommandHelper;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

public class GenericUrl extends GenericData {
    private static final Escaper URI_FRAGMENT_ESCAPER = new PercentEscaper("=&-_.!~*'()@:$,;/?:", false);
    private String fragment;
    private String host;
    private List<String> pathParts;
    private int port;
    private String scheme;
    private String userInfo;

    public GenericUrl() {
        this.port = -1;
    }

    public GenericUrl(String str) {
        this(parseURL(str));
    }

    public GenericUrl(URI uri) {
        this(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getRawPath(), uri.getRawFragment(), uri.getRawQuery(), uri.getRawUserInfo());
    }

    public GenericUrl(URL url) {
        this(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(), url.getRef(), url.getQuery(), url.getUserInfo());
    }

    private GenericUrl(String str, String str2, int i, String str3, String str4, String str5, String str6) {
        this.port = -1;
        this.scheme = str.toLowerCase(Locale.US);
        this.host = str2;
        this.port = i;
        this.pathParts = toPathParts(str3);
        String str7 = null;
        this.fragment = str4 != null ? CharEscapers.decodeUri(str4) : null;
        if (str5 != null) {
            UrlEncodedParser.parse(str5, (Object) this);
        }
        if (str6 != null) {
            str7 = CharEscapers.decodeUri(str6);
        }
        this.userInfo = str7;
    }

    public int hashCode() {
        return build().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof GenericUrl)) {
            return false;
        }
        return build().equals(((GenericUrl) obj).build());
    }

    public String toString() {
        return build();
    }

    public GenericUrl clone() {
        GenericUrl genericUrl = (GenericUrl) super.clone();
        List<String> list = this.pathParts;
        if (list != null) {
            genericUrl.pathParts = new ArrayList(list);
        }
        return genericUrl;
    }

    public GenericUrl set(String str, Object obj) {
        return (GenericUrl) super.set(str, obj);
    }

    public final String getScheme() {
        return this.scheme;
    }

    public final void setScheme(String str) {
        this.scheme = (String) Preconditions.checkNotNull(str);
    }

    public String getHost() {
        return this.host;
    }

    public final void setHost(String str) {
        this.host = (String) Preconditions.checkNotNull(str);
    }

    public final String getUserInfo() {
        return this.userInfo;
    }

    public final void setUserInfo(String str) {
        this.userInfo = str;
    }

    public int getPort() {
        return this.port;
    }

    public final void setPort(int i) {
        Preconditions.checkArgument(i >= -1, "expected port >= -1");
        this.port = i;
    }

    public List<String> getPathParts() {
        return this.pathParts;
    }

    public void setPathParts(List<String> list) {
        this.pathParts = list;
    }

    public String getFragment() {
        return this.fragment;
    }

    public final void setFragment(String str) {
        this.fragment = str;
    }

    public final String build() {
        StringBuilder sb = new StringBuilder();
        sb.append(buildAuthority());
        sb.append(buildRelativeUrl());
        return sb.toString();
    }

    public final String buildAuthority() {
        StringBuilder sb = new StringBuilder();
        sb.append((String) Preconditions.checkNotNull(this.scheme));
        sb.append("://");
        String str = this.userInfo;
        if (str != null) {
            sb.append(CharEscapers.escapeUriUserInfo(str));
            sb.append(TextCommandHelper.REPLY_AT_CHAR);
        }
        sb.append((String) Preconditions.checkNotNull(this.host));
        int i = this.port;
        if (i != -1) {
            sb.append(':');
            sb.append(i);
        }
        return sb.toString();
    }

    public final String buildRelativeUrl() {
        StringBuilder sb = new StringBuilder();
        if (this.pathParts != null) {
            appendRawPathFromParts(sb);
        }
        addQueryParams(entrySet(), sb);
        String str = this.fragment;
        if (str != null) {
            sb.append(TextCommandHelper.CHANNEL_CMD_CHAR);
            sb.append(URI_FRAGMENT_ESCAPER.escape(str));
        }
        return sb.toString();
    }

    public final URI toURI() {
        return toURI(build());
    }

    public final URL toURL() {
        return parseURL(build());
    }

    public final URL toURL(String str) {
        try {
            return new URL(toURL(), str);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Object getFirst(String str) {
        Object obj = get(str);
        if (!(obj instanceof Collection)) {
            return obj;
        }
        Iterator it = ((Collection) obj).iterator();
        return it.hasNext() ? it.next() : null;
    }

    public Collection<Object> getAll(String str) {
        Object obj = get(str);
        if (obj == null) {
            return Collections.emptySet();
        }
        if (obj instanceof Collection) {
            return Collections.unmodifiableCollection((Collection) obj);
        }
        return Collections.singleton(obj);
    }

    public String getRawPath() {
        if (this.pathParts == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        appendRawPathFromParts(sb);
        return sb.toString();
    }

    public void setRawPath(String str) {
        this.pathParts = toPathParts(str);
    }

    public void appendRawPath(String str) {
        if (str != null && str.length() != 0) {
            List<String> pathParts2 = toPathParts(str);
            List<String> list = this.pathParts;
            if (list == null || list.isEmpty()) {
                this.pathParts = pathParts2;
                return;
            }
            int size = this.pathParts.size();
            List<String> list2 = this.pathParts;
            int i = size - 1;
            StringBuilder sb = new StringBuilder();
            sb.append((String) this.pathParts.get(i));
            sb.append((String) pathParts2.get(0));
            list2.set(i, sb.toString());
            this.pathParts.addAll(pathParts2.subList(1, pathParts2.size()));
        }
    }

    public static List<String> toPathParts(String str) {
        String str2;
        if (str == null || str.length() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        int i = 0;
        while (z) {
            int indexOf = str.indexOf(47, i);
            boolean z2 = indexOf != -1;
            if (z2) {
                str2 = str.substring(i, indexOf);
            } else {
                str2 = str.substring(i);
            }
            arrayList.add(CharEscapers.decodeUri(str2));
            i = indexOf + 1;
            z = z2;
        }
        return arrayList;
    }

    private void appendRawPathFromParts(StringBuilder sb) {
        int size = this.pathParts.size();
        for (int i = 0; i < size; i++) {
            String str = (String) this.pathParts.get(i);
            if (i != 0) {
                sb.append('/');
            }
            if (str.length() != 0) {
                sb.append(CharEscapers.escapeUriPath(str));
            }
        }
    }

    static void addQueryParams(Set<Entry<String, Object>> set, StringBuilder sb) {
        boolean z = true;
        for (Entry entry : set) {
            Object value = entry.getValue();
            if (value != null) {
                String escapeUriQuery = CharEscapers.escapeUriQuery((String) entry.getKey());
                if (value instanceof Collection) {
                    for (Object appendParam : (Collection) value) {
                        z = appendParam(z, sb, escapeUriQuery, appendParam);
                    }
                } else {
                    z = appendParam(z, sb, escapeUriQuery, value);
                }
            }
        }
    }

    private static boolean appendParam(boolean z, StringBuilder sb, String str, Object obj) {
        if (z) {
            z = false;
            sb.append('?');
        } else {
            sb.append('&');
        }
        sb.append(str);
        String escapeUriQuery = CharEscapers.escapeUriQuery(obj.toString());
        if (escapeUriQuery.length() != 0) {
            sb.append('=');
            sb.append(escapeUriQuery);
        }
        return z;
    }

    private static URI toURI(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static URL parseURL(String str) {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
