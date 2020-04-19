package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthState;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class RequestProxyAuthentication extends RequestAuthenticationBase {
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        Args.notNull(httpRequest, "HTTP request");
        Args.notNull(httpContext, "HTTP context");
        if (!httpRequest.containsHeader("Proxy-Authorization")) {
            HttpRoutedConnection httpRoutedConnection = (HttpRoutedConnection) httpContext.getAttribute("http.connection");
            if (httpRoutedConnection == null) {
                this.log.debug("HTTP connection not set in the context");
            } else if (!httpRoutedConnection.getRoute().isTunnelled()) {
                AuthState authState = (AuthState) httpContext.getAttribute("http.auth.proxy-scope");
                if (authState == null) {
                    this.log.debug("Proxy auth state not set in the context");
                    return;
                }
                if (this.log.isDebugEnabled()) {
                    Log log = this.log;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Proxy auth state: ");
                    sb.append(authState.getState());
                    log.debug(sb.toString());
                }
                process(authState, httpRequest, httpContext);
            }
        }
    }
}
