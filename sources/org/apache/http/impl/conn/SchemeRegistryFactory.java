package org.apache.http.impl.conn;

import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;

@Contract(threading = ThreadingBehavior.SAFE)
@Deprecated
public final class SchemeRegistryFactory {
    public static SchemeRegistry createDefault() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, 80, (SchemeSocketFactory) PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, (SchemeSocketFactory) SSLSocketFactory.getSocketFactory()));
        return schemeRegistry;
    }

    public static SchemeRegistry createSystemDefault() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, 80, (SchemeSocketFactory) PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, (SchemeSocketFactory) SSLSocketFactory.getSystemSocketFactory()));
        return schemeRegistry;
    }
}
