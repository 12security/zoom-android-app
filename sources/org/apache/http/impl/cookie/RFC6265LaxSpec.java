package org.apache.http.impl.cookie;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.CommonCookieAttributeHandler;

@Contract(threading = ThreadingBehavior.SAFE)
public class RFC6265LaxSpec extends RFC6265CookieSpecBase {
    public String toString() {
        return "rfc6265-lax";
    }

    public RFC6265LaxSpec() {
        super(new BasicPathHandler(), new BasicDomainHandler(), new LaxMaxAgeHandler(), new BasicSecureHandler(), new LaxExpiresHandler());
    }

    RFC6265LaxSpec(CommonCookieAttributeHandler... commonCookieAttributeHandlerArr) {
        super(commonCookieAttributeHandlerArr);
    }
}
