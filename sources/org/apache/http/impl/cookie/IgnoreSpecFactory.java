package org.apache.http.impl.cookie;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider {
    public CookieSpec newInstance(HttpParams httpParams) {
        return new IgnoreSpec();
    }

    public CookieSpec create(HttpContext httpContext) {
        return new IgnoreSpec();
    }
}
