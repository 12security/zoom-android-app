package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class BrowserCompatSpecFactory implements CookieSpecFactory, CookieSpecProvider {
    private final CookieSpec cookieSpec;
    private final SecurityLevel securityLevel;

    public enum SecurityLevel {
        SECURITYLEVEL_DEFAULT,
        SECURITYLEVEL_IE_MEDIUM
    }

    public BrowserCompatSpecFactory(String[] strArr, SecurityLevel securityLevel2) {
        this.securityLevel = securityLevel2;
        this.cookieSpec = new BrowserCompatSpec(strArr, securityLevel2);
    }

    public BrowserCompatSpecFactory(String[] strArr) {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    public BrowserCompatSpecFactory() {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    public CookieSpec newInstance(HttpParams httpParams) {
        String[] strArr = null;
        if (httpParams == null) {
            return new BrowserCompatSpec(null, this.securityLevel);
        }
        Collection collection = (Collection) httpParams.getParameter(CookieSpecPNames.DATE_PATTERNS);
        if (collection != null) {
            strArr = (String[]) collection.toArray(new String[collection.size()]);
        }
        return new BrowserCompatSpec(strArr, this.securityLevel);
    }

    public CookieSpec create(HttpContext httpContext) {
        return this.cookieSpec;
    }
}
