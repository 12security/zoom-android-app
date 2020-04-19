package org.apache.http.client.protocol;

import com.google.api.client.http.HttpMethods;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthState;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class RequestTargetAuthentication extends RequestAuthenticationBase {
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        Args.notNull(httpRequest, "HTTP request");
        Args.notNull(httpContext, "HTTP context");
        if (!httpRequest.getRequestLine().getMethod().equalsIgnoreCase(HttpMethods.CONNECT) && !httpRequest.containsHeader("Authorization")) {
            AuthState authState = (AuthState) httpContext.getAttribute("http.auth.target-scope");
            if (authState == null) {
                this.log.debug("Target auth state not set in the context");
                return;
            }
            if (this.log.isDebugEnabled()) {
                Log log = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Target auth state: ");
                sb.append(authState.getState());
                log.debug(sb.toString());
            }
            process(authState, httpRequest, httpContext);
        }
    }
}
