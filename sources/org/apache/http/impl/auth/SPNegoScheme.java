package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

public class SPNegoScheme extends GGSSchemeBase {
    private static final String SPNEGO_OID = "1.3.6.1.5.5.2";

    public String getRealm() {
        return null;
    }

    public String getSchemeName() {
        return "Negotiate";
    }

    public boolean isConnectionBased() {
        return true;
    }

    public SPNegoScheme(boolean z, boolean z2) {
        super(z, z2);
    }

    public SPNegoScheme(boolean z) {
        super(z);
    }

    public SPNegoScheme() {
    }

    public Header authenticate(Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        return super.authenticate(credentials, httpRequest, httpContext);
    }

    /* access modifiers changed from: protected */
    public byte[] generateToken(byte[] bArr, String str) throws GSSException {
        return super.generateToken(bArr, str);
    }

    /* access modifiers changed from: protected */
    public byte[] generateToken(byte[] bArr, String str, Credentials credentials) throws GSSException {
        return generateGSSToken(bArr, new Oid(SPNEGO_OID), str, credentials);
    }

    public String getParameter(String str) {
        Args.notNull(str, "Parameter name");
        return null;
    }
}
