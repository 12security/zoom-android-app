package org.apache.http.client.protocol;

import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

@Deprecated
abstract class RequestAuthenticationBase implements HttpRequestInterceptor {
    final Log log = LogFactory.getLog(getClass());

    /* access modifiers changed from: 0000 */
    public void process(AuthState authState, HttpRequest httpRequest, HttpContext httpContext) {
        AuthScheme authScheme = authState.getAuthScheme();
        Credentials credentials = authState.getCredentials();
        switch (authState.getState()) {
            case FAILURE:
                return;
            case SUCCESS:
                ensureAuthScheme(authScheme);
                if (authScheme.isConnectionBased()) {
                    return;
                }
                break;
            case CHALLENGED:
                Queue authOptions = authState.getAuthOptions();
                if (authOptions == null) {
                    ensureAuthScheme(authScheme);
                    break;
                } else {
                    while (!authOptions.isEmpty()) {
                        AuthOption authOption = (AuthOption) authOptions.remove();
                        AuthScheme authScheme2 = authOption.getAuthScheme();
                        Credentials credentials2 = authOption.getCredentials();
                        authState.update(authScheme2, credentials2);
                        if (this.log.isDebugEnabled()) {
                            Log log2 = this.log;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Generating response to an authentication challenge using ");
                            sb.append(authScheme2.getSchemeName());
                            sb.append(" scheme");
                            log2.debug(sb.toString());
                        }
                        try {
                            httpRequest.addHeader(authenticate(authScheme2, credentials2, httpRequest, httpContext));
                            return;
                        } catch (AuthenticationException e) {
                            if (this.log.isWarnEnabled()) {
                                Log log3 = this.log;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(authScheme2);
                                sb2.append(" authentication error: ");
                                sb2.append(e.getMessage());
                                log3.warn(sb2.toString());
                            }
                        }
                    }
                    return;
                }
        }
        if (authScheme != null) {
            try {
                httpRequest.addHeader(authenticate(authScheme, credentials, httpRequest, httpContext));
            } catch (AuthenticationException e2) {
                if (this.log.isErrorEnabled()) {
                    Log log4 = this.log;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(authScheme);
                    sb3.append(" authentication error: ");
                    sb3.append(e2.getMessage());
                    log4.error(sb3.toString());
                }
            }
        }
    }

    private void ensureAuthScheme(AuthScheme authScheme) {
        Asserts.notNull(authScheme, "Auth scheme");
    }

    private Header authenticate(AuthScheme authScheme, Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        Asserts.notNull(authScheme, "Auth scheme");
        if (authScheme instanceof ContextAwareAuthScheme) {
            return ((ContextAwareAuthScheme) authScheme).authenticate(credentials, httpRequest, httpContext);
        }
        return authScheme.authenticate(credentials, httpRequest);
    }
}
