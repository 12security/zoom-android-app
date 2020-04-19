package org.apache.http.auth;

import com.zipow.videobox.util.TextCommandHelper;
import java.util.Locale;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.TokenParser;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class AuthScope {
    public static final AuthScope ANY = new AuthScope(ANY_HOST, -1, ANY_REALM, ANY_SCHEME);
    public static final String ANY_HOST = null;
    public static final int ANY_PORT = -1;
    public static final String ANY_REALM = null;
    public static final String ANY_SCHEME = null;
    private final String host;
    private final HttpHost origin;
    private final int port;
    private final String realm;
    private final String scheme;

    public AuthScope(String str, int i, String str2, String str3) {
        this.host = str == null ? ANY_HOST : str.toLowerCase(Locale.ROOT);
        if (i < 0) {
            i = -1;
        }
        this.port = i;
        if (str2 == null) {
            str2 = ANY_REALM;
        }
        this.realm = str2;
        this.scheme = str3 == null ? ANY_SCHEME : str3.toUpperCase(Locale.ROOT);
        this.origin = null;
    }

    public AuthScope(HttpHost httpHost, String str, String str2) {
        Args.notNull(httpHost, "Host");
        this.host = httpHost.getHostName().toLowerCase(Locale.ROOT);
        this.port = httpHost.getPort() < 0 ? -1 : httpHost.getPort();
        if (str == null) {
            str = ANY_REALM;
        }
        this.realm = str;
        this.scheme = str2 == null ? ANY_SCHEME : str2.toUpperCase(Locale.ROOT);
        this.origin = httpHost;
    }

    public AuthScope(HttpHost httpHost) {
        this(httpHost, ANY_REALM, ANY_SCHEME);
    }

    public AuthScope(String str, int i, String str2) {
        this(str, i, str2, ANY_SCHEME);
    }

    public AuthScope(String str, int i) {
        this(str, i, ANY_REALM, ANY_SCHEME);
    }

    public AuthScope(AuthScope authScope) {
        Args.notNull(authScope, "Scope");
        this.host = authScope.getHost();
        this.port = authScope.getPort();
        this.realm = authScope.getRealm();
        this.scheme = authScope.getScheme();
        this.origin = authScope.getOrigin();
    }

    public HttpHost getOrigin() {
        return this.origin;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getRealm() {
        return this.realm;
    }

    public String getScheme() {
        return this.scheme;
    }

    public int match(AuthScope authScope) {
        int i;
        if (LangUtils.equals((Object) this.scheme, (Object) authScope.scheme)) {
            i = 1;
        } else {
            String str = this.scheme;
            String str2 = ANY_SCHEME;
            if (str != str2 && authScope.scheme != str2) {
                return -1;
            }
            i = 0;
        }
        if (LangUtils.equals((Object) this.realm, (Object) authScope.realm)) {
            i += 2;
        } else {
            String str3 = this.realm;
            String str4 = ANY_REALM;
            if (!(str3 == str4 || authScope.realm == str4)) {
                return -1;
            }
        }
        int i2 = this.port;
        int i3 = authScope.port;
        if (i2 == i3) {
            i += 4;
        } else if (!(i2 == -1 || i3 == -1)) {
            return -1;
        }
        if (LangUtils.equals((Object) this.host, (Object) authScope.host)) {
            i += 8;
        } else {
            String str5 = this.host;
            String str6 = ANY_HOST;
            if (str5 == str6 || authScope.host == str6) {
                return i;
            }
            return -1;
        }
        return i;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AuthScope)) {
            return super.equals(obj);
        }
        AuthScope authScope = (AuthScope) obj;
        if (LangUtils.equals((Object) this.host, (Object) authScope.host) && this.port == authScope.port && LangUtils.equals((Object) this.realm, (Object) authScope.realm) && LangUtils.equals((Object) this.scheme, (Object) authScope.scheme)) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String str = this.scheme;
        if (str != null) {
            sb.append(str.toUpperCase(Locale.ROOT));
            sb.append(TokenParser.f498SP);
        }
        if (this.realm != null) {
            sb.append('\'');
            sb.append(this.realm);
            sb.append('\'');
        } else {
            sb.append("<any realm>");
        }
        if (this.host != null) {
            sb.append(TextCommandHelper.REPLY_AT_CHAR);
            sb.append(this.host);
            if (this.port >= 0) {
                sb.append(':');
                sb.append(this.port);
            }
        }
        return sb.toString();
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, (Object) this.host), this.port), (Object) this.realm), (Object) this.scheme);
    }
}
