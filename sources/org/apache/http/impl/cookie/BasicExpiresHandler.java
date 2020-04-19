package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class BasicExpiresHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
    private final String[] datepatterns;

    public String getAttributeName() {
        return ClientCookie.EXPIRES_ATTR;
    }

    public BasicExpiresHandler(String[] strArr) {
        Args.notNull(strArr, "Array of date patterns");
        this.datepatterns = strArr;
    }

    public void parse(SetCookie setCookie, String str) throws MalformedCookieException {
        Args.notNull(setCookie, "Cookie");
        if (str != null) {
            Date parseDate = DateUtils.parseDate(str, this.datepatterns);
            if (parseDate != null) {
                setCookie.setExpiryDate(parseDate);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid 'expires' attribute: ");
            sb.append(str);
            throw new MalformedCookieException(sb.toString());
        }
        throw new MalformedCookieException("Missing value for 'expires' attribute");
    }
}
