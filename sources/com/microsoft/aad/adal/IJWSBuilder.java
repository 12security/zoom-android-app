package com.microsoft.aad.adal;

import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public interface IJWSBuilder {
    String generateSignedJWT(String str, String str2, RSAPrivateKey rSAPrivateKey, RSAPublicKey rSAPublicKey, X509Certificate x509Certificate) throws AuthenticationException;
}
