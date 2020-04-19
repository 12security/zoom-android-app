package org.apache.http.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.config.Lookup;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.SAFE)
@Deprecated
public final class AuthSchemeRegistry implements Lookup<AuthSchemeProvider> {
    private final ConcurrentHashMap<String, AuthSchemeFactory> registeredSchemes = new ConcurrentHashMap<>();

    public void register(String str, AuthSchemeFactory authSchemeFactory) {
        Args.notNull(str, "Name");
        Args.notNull(authSchemeFactory, "Authentication scheme factory");
        this.registeredSchemes.put(str.toLowerCase(Locale.ENGLISH), authSchemeFactory);
    }

    public void unregister(String str) {
        Args.notNull(str, "Name");
        this.registeredSchemes.remove(str.toLowerCase(Locale.ENGLISH));
    }

    public AuthScheme getAuthScheme(String str, HttpParams httpParams) throws IllegalStateException {
        Args.notNull(str, "Name");
        AuthSchemeFactory authSchemeFactory = (AuthSchemeFactory) this.registeredSchemes.get(str.toLowerCase(Locale.ENGLISH));
        if (authSchemeFactory != null) {
            return authSchemeFactory.newInstance(httpParams);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported authentication scheme: ");
        sb.append(str);
        throw new IllegalStateException(sb.toString());
    }

    public List<String> getSchemeNames() {
        return new ArrayList(this.registeredSchemes.keySet());
    }

    public void setItems(Map<String, AuthSchemeFactory> map) {
        if (map != null) {
            this.registeredSchemes.clear();
            this.registeredSchemes.putAll(map);
        }
    }

    public AuthSchemeProvider lookup(final String str) {
        return new AuthSchemeProvider() {
            public AuthScheme create(HttpContext httpContext) {
                return AuthSchemeRegistry.this.getAuthScheme(str, ((HttpRequest) httpContext.getAttribute("http.request")).getParams());
            }
        };
    }
}
