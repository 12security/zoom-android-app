package com.microsoft.services.msa;

public class LiveOperationException extends Exception {
    private static final long serialVersionUID = 4630383031651156731L;

    LiveOperationException(String str) {
        super(str);
    }

    LiveOperationException(String str, Throwable th) {
        super(str, th);
    }
}
