package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.SAFE)
public abstract class CookieSpecBase extends AbstractCookieSpec {
    public CookieSpecBase() {
    }

    protected CookieSpecBase(HashMap<String, CookieAttributeHandler> hashMap) {
        super(hashMap);
    }

    protected CookieSpecBase(CommonCookieAttributeHandler... commonCookieAttributeHandlerArr) {
        super(commonCookieAttributeHandlerArr);
    }

    protected static String getDefaultPath(CookieOrigin cookieOrigin) {
        String path = cookieOrigin.getPath();
        int lastIndexOf = path.lastIndexOf(47);
        if (lastIndexOf < 0) {
            return path;
        }
        if (lastIndexOf == 0) {
            lastIndexOf = 1;
        }
        return path.substring(0, lastIndexOf);
    }

    protected static String getDefaultDomain(CookieOrigin cookieOrigin) {
        return cookieOrigin.getHost();
    }

    /* access modifiers changed from: protected */
    public List<Cookie> parse(HeaderElement[] headerElementArr, CookieOrigin cookieOrigin) throws MalformedCookieException {
        ArrayList arrayList = new ArrayList(headerElementArr.length);
        for (HeaderElement headerElement : headerElementArr) {
            String name = headerElement.getName();
            String value = headerElement.getValue();
            if (name != null && !name.isEmpty()) {
                BasicClientCookie basicClientCookie = new BasicClientCookie(name, value);
                basicClientCookie.setPath(getDefaultPath(cookieOrigin));
                basicClientCookie.setDomain(getDefaultDomain(cookieOrigin));
                NameValuePair[] parameters = headerElement.getParameters();
                for (int length = parameters.length - 1; length >= 0; length--) {
                    NameValuePair nameValuePair = parameters[length];
                    String lowerCase = nameValuePair.getName().toLowerCase(Locale.ROOT);
                    basicClientCookie.setAttribute(lowerCase, nameValuePair.getValue());
                    CookieAttributeHandler findAttribHandler = findAttribHandler(lowerCase);
                    if (findAttribHandler != null) {
                        findAttribHandler.parse(basicClientCookie, nameValuePair.getValue());
                    }
                }
                arrayList.add(basicClientCookie);
            }
        }
        return arrayList;
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(cookieOrigin, "Cookie origin");
        for (CookieAttributeHandler validate : getAttribHandlers()) {
            validate.validate(cookie, cookieOrigin);
        }
    }

    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(cookieOrigin, "Cookie origin");
        for (CookieAttributeHandler match : getAttribHandlers()) {
            if (!match.match(cookie, cookieOrigin)) {
                return false;
            }
        }
        return true;
    }
}
