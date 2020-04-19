package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.NTCredentials;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class NTLMScheme extends AuthSchemeBase {
    private String challenge;
    private final NTLMEngine engine;
    private State state;

    enum State {
        UNINITIATED,
        CHALLENGE_RECEIVED,
        MSG_TYPE1_GENERATED,
        MSG_TYPE2_RECEVIED,
        MSG_TYPE3_GENERATED,
        FAILED
    }

    public String getParameter(String str) {
        return null;
    }

    public String getRealm() {
        return null;
    }

    public String getSchemeName() {
        return "ntlm";
    }

    public boolean isConnectionBased() {
        return true;
    }

    public NTLMScheme(NTLMEngine nTLMEngine) {
        Args.notNull(nTLMEngine, "NTLM engine");
        this.engine = nTLMEngine;
        this.state = State.UNINITIATED;
        this.challenge = null;
    }

    public NTLMScheme() {
        this(new NTLMEngineImpl());
    }

    /* access modifiers changed from: protected */
    public void parseChallenge(CharArrayBuffer charArrayBuffer, int i, int i2) throws MalformedChallengeException {
        this.challenge = charArrayBuffer.substringTrimmed(i, i2);
        if (this.challenge.isEmpty()) {
            if (this.state == State.UNINITIATED) {
                this.state = State.CHALLENGE_RECEIVED;
            } else {
                this.state = State.FAILED;
            }
        } else if (this.state.compareTo(State.MSG_TYPE1_GENERATED) < 0) {
            this.state = State.FAILED;
            throw new MalformedChallengeException("Out of sequence NTLM response message");
        } else if (this.state == State.MSG_TYPE1_GENERATED) {
            this.state = State.MSG_TYPE2_RECEVIED;
        }
    }

    public Header authenticate(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException {
        String str;
        try {
            NTCredentials nTCredentials = (NTCredentials) credentials;
            if (this.state != State.FAILED) {
                if (this.state == State.CHALLENGE_RECEIVED) {
                    str = this.engine.generateType1Msg(nTCredentials.getDomain(), nTCredentials.getWorkstation());
                    this.state = State.MSG_TYPE1_GENERATED;
                } else if (this.state == State.MSG_TYPE2_RECEVIED) {
                    str = this.engine.generateType3Msg(nTCredentials.getUserName(), nTCredentials.getPassword(), nTCredentials.getDomain(), nTCredentials.getWorkstation(), this.challenge);
                    this.state = State.MSG_TYPE3_GENERATED;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected state: ");
                    sb.append(this.state);
                    throw new AuthenticationException(sb.toString());
                }
                CharArrayBuffer charArrayBuffer = new CharArrayBuffer(32);
                if (isProxy()) {
                    charArrayBuffer.append("Proxy-Authorization");
                } else {
                    charArrayBuffer.append("Authorization");
                }
                charArrayBuffer.append(": NTLM ");
                charArrayBuffer.append(str);
                return new BufferedHeader(charArrayBuffer);
            }
            throw new AuthenticationException("NTLM authentication failed");
        } catch (ClassCastException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Credentials cannot be used for NTLM authentication: ");
            sb2.append(credentials.getClass().getName());
            throw new InvalidCredentialsException(sb2.toString());
        }
    }

    public boolean isComplete() {
        return this.state == State.MSG_TYPE3_GENERATED || this.state == State.FAILED;
    }
}
