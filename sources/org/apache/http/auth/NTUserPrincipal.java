package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.TokenParser;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class NTUserPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -6870169797924406894L;
    private final String domain;
    private final String ntname;
    private final String username;

    public NTUserPrincipal(String str, String str2) {
        Args.notNull(str2, "User name");
        this.username = str2;
        if (str != null) {
            this.domain = str.toUpperCase(Locale.ROOT);
        } else {
            this.domain = null;
        }
        String str3 = this.domain;
        if (str3 == null || str3.isEmpty()) {
            this.ntname = this.username;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.domain);
        sb.append(TokenParser.ESCAPE);
        sb.append(this.username);
        this.ntname = sb.toString();
    }

    public String getName() {
        return this.ntname;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getUsername() {
        return this.username;
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(17, (Object) this.username), (Object) this.domain);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof NTUserPrincipal) {
            NTUserPrincipal nTUserPrincipal = (NTUserPrincipal) obj;
            if (LangUtils.equals((Object) this.username, (Object) nTUserPrincipal.username) && LangUtils.equals((Object) this.domain, (Object) nTUserPrincipal.domain)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return this.ntname;
    }
}
