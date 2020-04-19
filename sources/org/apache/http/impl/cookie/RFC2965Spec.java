package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.Obsolete;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.C4377SM;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading = ThreadingBehavior.SAFE)
@Obsolete
public class RFC2965Spec extends RFC2109Spec {
    public int getVersion() {
        return 1;
    }

    public String toString() {
        return CookiePolicy.RFC_2965;
    }

    public RFC2965Spec() {
        this((String[]) null, false);
    }

    public RFC2965Spec(String[] strArr, boolean z) {
        CommonCookieAttributeHandler[] commonCookieAttributeHandlerArr = new CommonCookieAttributeHandler[10];
        commonCookieAttributeHandlerArr[0] = new RFC2965VersionAttributeHandler();
        commonCookieAttributeHandlerArr[1] = new BasicPathHandler() {
            public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
                if (!match(cookie, cookieOrigin)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Illegal 'path' attribute \"");
                    sb.append(cookie.getPath());
                    sb.append("\". Path of origin: \"");
                    sb.append(cookieOrigin.getPath());
                    sb.append("\"");
                    throw new CookieRestrictionViolationException(sb.toString());
                }
            }
        };
        commonCookieAttributeHandlerArr[2] = new RFC2965DomainAttributeHandler();
        commonCookieAttributeHandlerArr[3] = new RFC2965PortAttributeHandler();
        commonCookieAttributeHandlerArr[4] = new BasicMaxAgeHandler();
        commonCookieAttributeHandlerArr[5] = new BasicSecureHandler();
        commonCookieAttributeHandlerArr[6] = new BasicCommentHandler();
        commonCookieAttributeHandlerArr[7] = new BasicExpiresHandler(strArr != null ? (String[]) strArr.clone() : DATE_PATTERNS);
        commonCookieAttributeHandlerArr[8] = new RFC2965CommentUrlAttributeHandler();
        commonCookieAttributeHandlerArr[9] = new RFC2965DiscardAttributeHandler();
        super(z, commonCookieAttributeHandlerArr);
    }

    RFC2965Spec(boolean z, CommonCookieAttributeHandler... commonCookieAttributeHandlerArr) {
        super(z, commonCookieAttributeHandlerArr);
    }

    public List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(cookieOrigin, "Cookie origin");
        if (header.getName().equalsIgnoreCase("Set-Cookie2")) {
            return createCookies(header.getElements(), adjustEffectiveHost(cookieOrigin));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unrecognized cookie header '");
        sb.append(header.toString());
        sb.append("'");
        throw new MalformedCookieException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public List<Cookie> parse(HeaderElement[] headerElementArr, CookieOrigin cookieOrigin) throws MalformedCookieException {
        return createCookies(headerElementArr, adjustEffectiveHost(cookieOrigin));
    }

    private List<Cookie> createCookies(HeaderElement[] headerElementArr, CookieOrigin cookieOrigin) throws MalformedCookieException {
        ArrayList arrayList = new ArrayList(headerElementArr.length);
        for (HeaderElement headerElement : headerElementArr) {
            String name = headerElement.getName();
            String value = headerElement.getValue();
            if (name == null || name.isEmpty()) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            BasicClientCookie2 basicClientCookie2 = new BasicClientCookie2(name, value);
            basicClientCookie2.setPath(getDefaultPath(cookieOrigin));
            basicClientCookie2.setDomain(getDefaultDomain(cookieOrigin));
            basicClientCookie2.setPorts(new int[]{cookieOrigin.getPort()});
            NameValuePair[] parameters = headerElement.getParameters();
            HashMap hashMap = new HashMap(parameters.length);
            for (int length = parameters.length - 1; length >= 0; length--) {
                NameValuePair nameValuePair = parameters[length];
                hashMap.put(nameValuePair.getName().toLowerCase(Locale.ROOT), nameValuePair);
            }
            for (Entry value2 : hashMap.entrySet()) {
                NameValuePair nameValuePair2 = (NameValuePair) value2.getValue();
                String lowerCase = nameValuePair2.getName().toLowerCase(Locale.ROOT);
                basicClientCookie2.setAttribute(lowerCase, nameValuePair2.getValue());
                CookieAttributeHandler findAttribHandler = findAttribHandler(lowerCase);
                if (findAttribHandler != null) {
                    findAttribHandler.parse(basicClientCookie2, nameValuePair2.getValue());
                }
            }
            arrayList.add(basicClientCookie2);
        }
        return arrayList;
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(cookieOrigin, "Cookie origin");
        super.validate(cookie, adjustEffectiveHost(cookieOrigin));
    }

    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(cookieOrigin, "Cookie origin");
        return super.match(cookie, adjustEffectiveHost(cookieOrigin));
    }

    /* access modifiers changed from: protected */
    public void formatCookieAsVer(CharArrayBuffer charArrayBuffer, Cookie cookie, int i) {
        super.formatCookieAsVer(charArrayBuffer, cookie, i);
        if (cookie instanceof ClientCookie) {
            String attribute = ((ClientCookie) cookie).getAttribute("port");
            if (attribute != null) {
                charArrayBuffer.append("; $Port");
                charArrayBuffer.append("=\"");
                if (!attribute.trim().isEmpty()) {
                    int[] ports = cookie.getPorts();
                    if (ports != null) {
                        int length = ports.length;
                        for (int i2 = 0; i2 < length; i2++) {
                            if (i2 > 0) {
                                charArrayBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                            }
                            charArrayBuffer.append(Integer.toString(ports[i2]));
                        }
                    }
                }
                charArrayBuffer.append("\"");
            }
        }
    }

    private static CookieOrigin adjustEffectiveHost(CookieOrigin cookieOrigin) {
        String host = cookieOrigin.getHost();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= host.length()) {
                z = true;
                break;
            }
            char charAt = host.charAt(i);
            if (charAt == '.' || charAt == ':') {
                break;
            }
            i++;
        }
        if (!z) {
            return cookieOrigin;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(host);
        sb.append(".local");
        return new CookieOrigin(sb.toString(), cookieOrigin.getPort(), cookieOrigin.getPath(), cookieOrigin.isSecure());
    }

    public Header getVersionHeader() {
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(40);
        charArrayBuffer.append(C4377SM.COOKIE2);
        charArrayBuffer.append(": ");
        charArrayBuffer.append("$Version=");
        charArrayBuffer.append(Integer.toString(getVersion()));
        return new BufferedHeader(charArrayBuffer);
    }
}
