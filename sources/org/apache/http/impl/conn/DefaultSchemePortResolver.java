package org.apache.http.impl.conn;

import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class DefaultSchemePortResolver implements SchemePortResolver {
    public static final DefaultSchemePortResolver INSTANCE = new DefaultSchemePortResolver();

    public int resolve(HttpHost httpHost) throws UnsupportedSchemeException {
        Args.notNull(httpHost, "HTTP host");
        int port = httpHost.getPort();
        if (port > 0) {
            return port;
        }
        String schemeName = httpHost.getSchemeName();
        if (schemeName.equalsIgnoreCase(HttpHost.DEFAULT_SCHEME_NAME)) {
            return 80;
        }
        if (schemeName.equalsIgnoreCase("https")) {
            return 443;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(schemeName);
        sb.append(" protocol is not supported");
        throw new UnsupportedSchemeException(sb.toString());
    }
}
