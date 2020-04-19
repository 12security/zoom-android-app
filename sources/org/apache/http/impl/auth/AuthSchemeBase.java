package org.apache.http.impl.auth;

import java.util.Locale;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public abstract class AuthSchemeBase implements ContextAwareAuthScheme {
    protected ChallengeState challengeState;

    /* access modifiers changed from: protected */
    public abstract void parseChallenge(CharArrayBuffer charArrayBuffer, int i, int i2) throws MalformedChallengeException;

    @Deprecated
    public AuthSchemeBase(ChallengeState challengeState2) {
        this.challengeState = challengeState2;
    }

    public AuthSchemeBase() {
    }

    public void processChallenge(Header header) throws MalformedChallengeException {
        int i;
        CharArrayBuffer charArrayBuffer;
        Args.notNull(header, "Header");
        String name = header.getName();
        if (name.equalsIgnoreCase("WWW-Authenticate")) {
            this.challengeState = ChallengeState.TARGET;
        } else if (name.equalsIgnoreCase("Proxy-Authenticate")) {
            this.challengeState = ChallengeState.PROXY;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected header name: ");
            sb.append(name);
            throw new MalformedChallengeException(sb.toString());
        }
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
        String substring = charArrayBuffer.substring(i, i2);
        if (substring.equalsIgnoreCase(getSchemeName())) {
            parseChallenge(charArrayBuffer, i2, charArrayBuffer.length());
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Invalid scheme identifier: ");
        sb2.append(substring);
        throw new MalformedChallengeException(sb2.toString());
    }

    public Header authenticate(Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        return authenticate(credentials, httpRequest);
    }

    public boolean isProxy() {
        ChallengeState challengeState2 = this.challengeState;
        return challengeState2 != null && challengeState2 == ChallengeState.PROXY;
    }

    public ChallengeState getChallengeState() {
        return this.challengeState;
    }

    public String toString() {
        String schemeName = getSchemeName();
        if (schemeName != null) {
            return schemeName.toUpperCase(Locale.ROOT);
        }
        return super.toString();
    }
}
