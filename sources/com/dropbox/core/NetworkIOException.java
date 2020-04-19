package com.dropbox.core;

import java.io.IOException;
import java.security.cert.CertPathValidatorException;
import javax.net.ssl.SSLHandshakeException;

public class NetworkIOException extends DbxException {
    private static final long serialVersionUID = 0;

    public NetworkIOException(IOException iOException) {
        super(computeMessage(iOException), (Throwable) iOException);
    }

    private static String computeMessage(IOException iOException) {
        String message = iOException.getMessage();
        if (!(iOException instanceof SSLHandshakeException)) {
            return message;
        }
        Throwable cause = iOException.getCause();
        if (!(cause instanceof CertPathValidatorException)) {
            return message;
        }
        CertPathValidatorException certPathValidatorException = (CertPathValidatorException) cause;
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append("[CERT PATH: ");
        sb.append(certPathValidatorException.getCertPath());
        sb.append("]");
        return sb.toString();
    }

    public IOException getCause() {
        return (IOException) super.getCause();
    }
}
