package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class RFC2109DomainHandler implements CommonCookieAttributeHandler {
    public String getAttributeName() {
        return ClientCookie.DOMAIN_ATTR;
    }

    public void parse(SetCookie setCookie, String str) throws MalformedCookieException {
        Args.notNull(setCookie, "Cookie");
        if (str == null) {
            throw new MalformedCookieException("Missing value for domain attribute");
        } else if (!str.trim().isEmpty()) {
            setCookie.setDomain(str);
        } else {
            throw new MalformedCookieException("Blank value for domain attribute");
        }
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(cookieOrigin, "Cookie origin");
        String host = cookieOrigin.getHost();
        String domain = cookie.getDomain();
        if (domain == null) {
            throw new CookieRestrictionViolationException("Cookie domain may not be null");
        } else if (domain.equals(host)) {
        } else {
            if (domain.indexOf(46) == -1) {
                StringBuilder sb = new StringBuilder();
                sb.append("Domain attribute \"");
                sb.append(domain);
                sb.append("\" does not match the host \"");
                sb.append(host);
                sb.append("\"");
                throw new CookieRestrictionViolationException(sb.toString());
            } else if (domain.startsWith(".")) {
                int indexOf = domain.indexOf(46, 1);
                if (indexOf < 0 || indexOf == domain.length() - 1) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Domain attribute \"");
                    sb2.append(domain);
                    sb2.append("\" violates RFC 2109: domain must contain an embedded dot");
                    throw new CookieRestrictionViolationException(sb2.toString());
                }
                String lowerCase = host.toLowerCase(Locale.ROOT);
                if (!lowerCase.endsWith(domain)) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Illegal domain attribute \"");
                    sb3.append(domain);
                    sb3.append("\". Domain of origin: \"");
                    sb3.append(lowerCase);
                    sb3.append("\"");
                    throw new CookieRestrictionViolationException(sb3.toString());
                } else if (lowerCase.substring(0, lowerCase.length() - domain.length()).indexOf(46) != -1) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Domain attribute \"");
                    sb4.append(domain);
                    sb4.append("\" violates RFC 2109: host minus domain may not contain any dots");
                    throw new CookieRestrictionViolationException(sb4.toString());
                }
            } else {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Domain attribute \"");
                sb5.append(domain);
                sb5.append("\" violates RFC 2109: domain must start with a dot");
                throw new CookieRestrictionViolationException(sb5.toString());
            }
        }
    }

    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(cookieOrigin, "Cookie origin");
        String host = cookieOrigin.getHost();
        String domain = cookie.getDomain();
        boolean z = false;
        if (domain == null) {
            return false;
        }
        if (host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain))) {
            z = true;
        }
        return z;
    }
}
