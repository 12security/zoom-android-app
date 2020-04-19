package org.apache.http.impl.auth;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

public class HttpAuthenticator {
    private final Log log;

    public HttpAuthenticator(Log log2) {
        if (log2 == null) {
            log2 = LogFactory.getLog(getClass());
        }
        this.log = log2;
    }

    public HttpAuthenticator() {
        this(null);
    }

    public boolean isAuthenticationRequested(HttpHost httpHost, HttpResponse httpResponse, AuthenticationStrategy authenticationStrategy, AuthState authState, HttpContext httpContext) {
        if (authenticationStrategy.isAuthenticationRequested(httpHost, httpResponse, httpContext)) {
            this.log.debug("Authentication required");
            if (authState.getState() == AuthProtocolState.SUCCESS) {
                authenticationStrategy.authFailed(httpHost, authState.getAuthScheme(), httpContext);
            }
            return true;
        }
        switch (authState.getState()) {
            case CHALLENGED:
            case HANDSHAKE:
                this.log.debug("Authentication succeeded");
                authState.setState(AuthProtocolState.SUCCESS);
                authenticationStrategy.authSucceeded(httpHost, authState.getAuthScheme(), httpContext);
                break;
            case SUCCESS:
                break;
            default:
                authState.setState(AuthProtocolState.UNCHALLENGED);
                break;
        }
        return false;
    }

    public boolean handleAuthChallenge(HttpHost httpHost, HttpResponse httpResponse, AuthenticationStrategy authenticationStrategy, AuthState authState, HttpContext httpContext) {
        try {
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append(httpHost.toHostString());
                sb.append(" requested authentication");
                log2.debug(sb.toString());
            }
            Map challenges = authenticationStrategy.getChallenges(httpHost, httpResponse, httpContext);
            if (challenges.isEmpty()) {
                this.log.debug("Response contains no authentication challenges");
                return false;
            }
            AuthScheme authScheme = authState.getAuthScheme();
            switch (authState.getState()) {
                case CHALLENGED:
                case HANDSHAKE:
                    if (authScheme == null) {
                        this.log.debug("Auth scheme is null");
                        authenticationStrategy.authFailed(httpHost, null, httpContext);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                    break;
                case SUCCESS:
                    authState.reset();
                    break;
                case FAILURE:
                    return false;
                case UNCHALLENGED:
                    break;
            }
            if (authScheme != null) {
                Header header = (Header) challenges.get(authScheme.getSchemeName().toLowerCase(Locale.ROOT));
                if (header != null) {
                    this.log.debug("Authorization challenge processed");
                    authScheme.processChallenge(header);
                    if (authScheme.isComplete()) {
                        this.log.debug("Authentication failed");
                        authenticationStrategy.authFailed(httpHost, authState.getAuthScheme(), httpContext);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                    authState.setState(AuthProtocolState.HANDSHAKE);
                    return true;
                }
                authState.reset();
            }
            Queue select = authenticationStrategy.select(challenges, httpHost, httpResponse, httpContext);
            if (select == null || select.isEmpty()) {
                return false;
            }
            if (this.log.isDebugEnabled()) {
                Log log3 = this.log;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Selected authentication options: ");
                sb2.append(select);
                log3.debug(sb2.toString());
            }
            authState.setState(AuthProtocolState.CHALLENGED);
            authState.update(select);
            return true;
        } catch (MalformedChallengeException e) {
            if (this.log.isWarnEnabled()) {
                Log log4 = this.log;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Malformed challenge: ");
                sb3.append(e.getMessage());
                log4.warn(sb3.toString());
            }
            authState.reset();
            return false;
        }
    }

    public void generateAuthResponse(HttpRequest httpRequest, AuthState authState, HttpContext httpContext) throws HttpException, IOException {
        AuthScheme authScheme = authState.getAuthScheme();
        Credentials credentials = authState.getCredentials();
        int i = C43791.$SwitchMap$org$apache$http$auth$AuthProtocolState[authState.getState().ordinal()];
        if (i != 1) {
            switch (i) {
                case 3:
                    ensureAuthScheme(authScheme);
                    if (authScheme.isConnectionBased()) {
                        return;
                    }
                    break;
                case 4:
                    return;
            }
        } else {
            Queue authOptions = authState.getAuthOptions();
            if (authOptions != null) {
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
                        httpRequest.addHeader(doAuth(authScheme2, credentials2, httpRequest, httpContext));
                        break;
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
            ensureAuthScheme(authScheme);
        }
        if (authScheme != null) {
            try {
                httpRequest.addHeader(doAuth(authScheme, credentials, httpRequest, httpContext));
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

    private Header doAuth(AuthScheme authScheme, Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        if (authScheme instanceof ContextAwareAuthScheme) {
            return ((ContextAwareAuthScheme) authScheme).authenticate(credentials, httpRequest, httpContext);
        }
        return authScheme.authenticate(credentials, httpRequest);
    }
}
