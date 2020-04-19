package org.apache.http.impl.cookie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.util.PublicSuffixList;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class PublicSuffixDomainFilter implements CommonCookieAttributeHandler {
    private final CommonCookieAttributeHandler handler;
    private final Map<String, Boolean> localDomainMap = createLocalDomainMap();
    private final PublicSuffixMatcher publicSuffixMatcher;

    private static Map<String, Boolean> createLocalDomainMap() {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put(".localhost.", Boolean.TRUE);
        concurrentHashMap.put(".test.", Boolean.TRUE);
        concurrentHashMap.put(".local.", Boolean.TRUE);
        concurrentHashMap.put(".local", Boolean.TRUE);
        concurrentHashMap.put(".localdomain", Boolean.TRUE);
        return concurrentHashMap;
    }

    public PublicSuffixDomainFilter(CommonCookieAttributeHandler commonCookieAttributeHandler, PublicSuffixMatcher publicSuffixMatcher2) {
        this.handler = (CommonCookieAttributeHandler) Args.notNull(commonCookieAttributeHandler, "Cookie handler");
        this.publicSuffixMatcher = (PublicSuffixMatcher) Args.notNull(publicSuffixMatcher2, "Public suffix matcher");
    }

    public PublicSuffixDomainFilter(CommonCookieAttributeHandler commonCookieAttributeHandler, PublicSuffixList publicSuffixList) {
        Args.notNull(commonCookieAttributeHandler, "Cookie handler");
        Args.notNull(publicSuffixList, "Public suffix list");
        this.handler = commonCookieAttributeHandler;
        this.publicSuffixMatcher = new PublicSuffixMatcher(publicSuffixList.getRules(), publicSuffixList.getExceptions());
    }

    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        String domain = cookie.getDomain();
        int indexOf = domain.indexOf(46);
        if (indexOf >= 0) {
            if (!this.localDomainMap.containsKey(domain.substring(indexOf)) && this.publicSuffixMatcher.matches(domain)) {
                return false;
            }
        } else if (!domain.equalsIgnoreCase(cookieOrigin.getHost()) && this.publicSuffixMatcher.matches(domain)) {
            return false;
        }
        return this.handler.match(cookie, cookieOrigin);
    }

    public void parse(SetCookie setCookie, String str) throws MalformedCookieException {
        this.handler.parse(setCookie, str);
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        this.handler.validate(cookie, cookieOrigin);
    }

    public String getAttributeName() {
        return this.handler.getAttributeName();
    }

    public static CommonCookieAttributeHandler decorate(CommonCookieAttributeHandler commonCookieAttributeHandler, PublicSuffixMatcher publicSuffixMatcher2) {
        Args.notNull(commonCookieAttributeHandler, "Cookie attribute handler");
        return publicSuffixMatcher2 != null ? new PublicSuffixDomainFilter(commonCookieAttributeHandler, publicSuffixMatcher2) : commonCookieAttributeHandler;
    }
}
