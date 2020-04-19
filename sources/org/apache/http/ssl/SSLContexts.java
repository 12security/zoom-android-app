package org.apache.http.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;

public class SSLContexts {
    public static SSLContext createDefault() throws SSLInitializationException {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, null, null);
            return instance;
        } catch (NoSuchAlgorithmException e) {
            throw new SSLInitializationException(e.getMessage(), e);
        } catch (KeyManagementException e2) {
            throw new SSLInitializationException(e2.getMessage(), e2);
        }
    }

    public static SSLContext createSystemDefault() throws SSLInitializationException {
        try {
            return SSLContext.getDefault();
        } catch (NoSuchAlgorithmException unused) {
            return createDefault();
        }
    }

    public static SSLContextBuilder custom() {
        return SSLContextBuilder.create();
    }
}
