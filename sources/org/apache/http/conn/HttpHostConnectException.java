package org.apache.http.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Arrays;
import org.apache.http.HttpHost;

public class HttpHostConnectException extends ConnectException {
    private static final long serialVersionUID = -3194482710275220224L;
    private final HttpHost host;

    @Deprecated
    public HttpHostConnectException(HttpHost httpHost, ConnectException connectException) {
        this(connectException, httpHost, null);
    }

    public HttpHostConnectException(IOException iOException, HttpHost httpHost, InetAddress... inetAddressArr) {
        String str;
        String str2;
        StringBuilder sb = new StringBuilder();
        sb.append("Connect to ");
        sb.append(httpHost != null ? httpHost.toHostString() : "remote host");
        if (inetAddressArr == null || inetAddressArr.length <= 0) {
            str = "";
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(OAuth.SCOPE_DELIMITER);
            sb2.append(Arrays.asList(inetAddressArr));
            str = sb2.toString();
        }
        sb.append(str);
        if (iOException == null || iOException.getMessage() == null) {
            str2 = " refused";
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(" failed: ");
            sb3.append(iOException.getMessage());
            str2 = sb3.toString();
        }
        sb.append(str2);
        super(sb.toString());
        this.host = httpHost;
        initCause(iOException);
    }

    public HttpHost getHost() {
        return this.host;
    }
}
