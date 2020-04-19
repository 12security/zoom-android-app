package com.microsoft.aad.adal;

import java.io.PrintWriter;
import java.io.StringWriter;

final class ExceptionExtensions {
    private ExceptionExtensions() {
    }

    static String getExceptionMessage(Exception exc) {
        if (exc == null) {
            return null;
        }
        String message = exc.getMessage();
        if (message != null) {
            return message;
        }
        StringWriter stringWriter = new StringWriter();
        exc.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
