package com.google.api.client.googleapis;

import com.google.api.client.util.SecurityUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

public final class GoogleUtils {
    public static final Integer BUGFIX_VERSION = Integer.valueOf(0);
    public static final Integer MAJOR_VERSION = Integer.valueOf(1);
    public static final Integer MINOR_VERSION = Integer.valueOf(26);
    public static final String VERSION;
    static KeyStore certTrustStore;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(MAJOR_VERSION);
        sb.append(".");
        sb.append(MINOR_VERSION);
        sb.append(".");
        sb.append(BUGFIX_VERSION);
        sb.append("-SNAPSHOT");
        VERSION = sb.toString().toString();
    }

    public static synchronized KeyStore getCertificateTrustStore() throws IOException, GeneralSecurityException {
        KeyStore keyStore;
        synchronized (GoogleUtils.class) {
            if (certTrustStore == null) {
                certTrustStore = SecurityUtils.getJavaKeyStore();
                SecurityUtils.loadKeyStore(certTrustStore, GoogleUtils.class.getResourceAsStream("google.jks"), "notasecret");
            }
            keyStore = certTrustStore;
        }
        return keyStore;
    }

    private GoogleUtils() {
    }
}
