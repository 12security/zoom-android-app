package org.apache.http.impl.auth;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class SPNegoSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
    private final boolean stripPort;
    private final boolean useCanonicalHostname;

    public SPNegoSchemeFactory(boolean z, boolean z2) {
        this.stripPort = z;
        this.useCanonicalHostname = z2;
    }

    public SPNegoSchemeFactory(boolean z) {
        this.stripPort = z;
        this.useCanonicalHostname = true;
    }

    public SPNegoSchemeFactory() {
        this(true, true);
    }

    public boolean isStripPort() {
        return this.stripPort;
    }

    public boolean isUseCanonicalHostname() {
        return this.useCanonicalHostname;
    }

    public AuthScheme newInstance(HttpParams httpParams) {
        return new SPNegoScheme(this.stripPort, this.useCanonicalHostname);
    }

    public AuthScheme create(HttpContext httpContext) {
        return new SPNegoScheme(this.stripPort, this.useCanonicalHostname);
    }
}
