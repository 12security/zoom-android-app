package org.apache.http.impl.cookie;

import java.util.Collections;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class IgnoreSpec extends CookieSpecBase {
    public int getVersion() {
        return 0;
    }

    public Header getVersionHeader() {
        return null;
    }

    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        return false;
    }

    public List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException {
        return Collections.emptyList();
    }

    public List<Header> formatCookies(List<Cookie> list) {
        return Collections.emptyList();
    }
}
