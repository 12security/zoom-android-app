package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthCache;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
abstract class AuthenticationStrategyImpl implements AuthenticationStrategy {
    private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList(new String[]{"Negotiate", "Kerberos", "NTLM", AuthSchemes.CREDSSP, "Digest", "Basic"}));
    private final int challengeCode;
    private final String headerName;
    private final Log log = LogFactory.getLog(getClass());

    /* access modifiers changed from: 0000 */
    public abstract Collection<String> getPreferredAuthSchemes(RequestConfig requestConfig);

    AuthenticationStrategyImpl(int i, String str) {
        this.challengeCode = i;
        this.headerName = str;
    }

    public boolean isAuthenticationRequested(HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) {
        Args.notNull(httpResponse, "HTTP response");
        return httpResponse.getStatusLine().getStatusCode() == this.challengeCode;
    }

    public Map<String, Header> getChallenges(HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) throws MalformedChallengeException {
        CharArrayBuffer charArrayBuffer;
        int i;
        Args.notNull(httpResponse, "HTTP response");
        Header[] headers = httpResponse.getHeaders(this.headerName);
        HashMap hashMap = new HashMap(headers.length);
        for (Header header : headers) {
            if (header instanceof FormattedHeader) {
                FormattedHeader formattedHeader = (FormattedHeader) header;
                charArrayBuffer = formattedHeader.getBuffer();
                i = formattedHeader.getValuePos();
            } else {
                String value = header.getValue();
                if (value != null) {
                    charArrayBuffer = new CharArrayBuffer(value.length());
                    charArrayBuffer.append(value);
                    i = 0;
                } else {
                    throw new MalformedChallengeException("Header value is null");
                }
            }
            while (i < charArrayBuffer.length() && HTTP.isWhitespace(charArrayBuffer.charAt(i))) {
                i++;
            }
            int i2 = i;
            while (i2 < charArrayBuffer.length() && !HTTP.isWhitespace(charArrayBuffer.charAt(i2))) {
                i2++;
            }
            hashMap.put(charArrayBuffer.substring(i, i2).toLowerCase(Locale.ROOT), header);
        }
        return hashMap;
    }

    public Queue<AuthOption> select(Map<String, Header> map, HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) throws MalformedChallengeException {
        Args.notNull(map, "Map of auth challenges");
        Args.notNull(httpHost, "Host");
        Args.notNull(httpResponse, "HTTP response");
        Args.notNull(httpContext, "HTTP context");
        HttpClientContext adapt = HttpClientContext.adapt(httpContext);
        LinkedList linkedList = new LinkedList();
        Lookup authSchemeRegistry = adapt.getAuthSchemeRegistry();
        if (authSchemeRegistry == null) {
            this.log.debug("Auth scheme registry not set in the context");
            return linkedList;
        }
        CredentialsProvider credentialsProvider = adapt.getCredentialsProvider();
        if (credentialsProvider == null) {
            this.log.debug("Credentials provider not set in the context");
            return linkedList;
        }
        Collection<String> preferredAuthSchemes = getPreferredAuthSchemes(adapt.getRequestConfig());
        if (preferredAuthSchemes == null) {
            preferredAuthSchemes = DEFAULT_SCHEME_PRIORITY;
        }
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Authentication schemes in the order of preference: ");
            sb.append(preferredAuthSchemes);
            log2.debug(sb.toString());
        }
        for (String str : preferredAuthSchemes) {
            Header header = (Header) map.get(str.toLowerCase(Locale.ROOT));
            if (header != null) {
                AuthSchemeProvider authSchemeProvider = (AuthSchemeProvider) authSchemeRegistry.lookup(str);
                if (authSchemeProvider != null) {
                    AuthScheme create = authSchemeProvider.create(httpContext);
                    create.processChallenge(header);
                    Credentials credentials = credentialsProvider.getCredentials(new AuthScope(httpHost, create.getRealm(), create.getSchemeName()));
                    if (credentials != null) {
                        linkedList.add(new AuthOption(create, credentials));
                    }
                } else if (this.log.isWarnEnabled()) {
                    Log log3 = this.log;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Authentication scheme ");
                    sb2.append(str);
                    sb2.append(" not supported");
                    log3.warn(sb2.toString());
                }
            } else if (this.log.isDebugEnabled()) {
                Log log4 = this.log;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Challenge for ");
                sb3.append(str);
                sb3.append(" authentication scheme not available");
                log4.debug(sb3.toString());
            }
        }
        return linkedList;
    }

    public void authSucceeded(HttpHost httpHost, AuthScheme authScheme, HttpContext httpContext) {
        Args.notNull(httpHost, "Host");
        Args.notNull(authScheme, "Auth scheme");
        Args.notNull(httpContext, "HTTP context");
        HttpClientContext adapt = HttpClientContext.adapt(httpContext);
        if (isCachable(authScheme)) {
            AuthCache authCache = adapt.getAuthCache();
            if (authCache == null) {
                authCache = new BasicAuthCache();
                adapt.setAuthCache(authCache);
            }
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Caching '");
                sb.append(authScheme.getSchemeName());
                sb.append("' auth scheme for ");
                sb.append(httpHost);
                log2.debug(sb.toString());
            }
            authCache.put(httpHost, authScheme);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isCachable(AuthScheme authScheme) {
        if (authScheme == null || !authScheme.isComplete()) {
            return false;
        }
        return authScheme.getSchemeName().equalsIgnoreCase("Basic");
    }

    public void authFailed(HttpHost httpHost, AuthScheme authScheme, HttpContext httpContext) {
        Args.notNull(httpHost, "Host");
        Args.notNull(httpContext, "HTTP context");
        AuthCache authCache = HttpClientContext.adapt(httpContext).getAuthCache();
        if (authCache != null) {
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Clearing cached auth scheme for ");
                sb.append(httpHost);
                log2.debug(sb.toString());
            }
            authCache.remove(httpHost);
        }
    }
}
