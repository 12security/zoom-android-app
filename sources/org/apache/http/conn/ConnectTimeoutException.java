package org.apache.http.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.util.Arrays;
import org.apache.http.HttpHost;

public class ConnectTimeoutException extends InterruptedIOException {
    private static final long serialVersionUID = -4816682903149535989L;
    private final HttpHost host;

    public ConnectTimeoutException() {
        this.host = null;
    }

    public ConnectTimeoutException(String str) {
        super(str);
        this.host = null;
    }

    public ConnectTimeoutException(IOException iOException, HttpHost httpHost, InetAddress... inetAddressArr) {
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
            str2 = " timed out";
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
