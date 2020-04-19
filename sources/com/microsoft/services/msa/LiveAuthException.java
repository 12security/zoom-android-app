package com.microsoft.services.msa;

public class LiveAuthException extends RuntimeException {
    private static final long serialVersionUID = 3368677530670470856L;
    private final String error;
    private final String errorUri;

    public LiveAuthException(String str) {
        super(str);
        this.error = "";
        this.errorUri = "";
    }

    public LiveAuthException(String str, Throwable th) {
        super(str, th);
        this.error = "";
        this.errorUri = "";
    }

    public LiveAuthException(String str, String str2, String str3) {
        super(str2);
        if (str != null) {
            this.error = str;
            this.errorUri = str3;
            return;
        }
        throw new AssertionError();
    }

    public LiveAuthException(String str, String str2, String str3, Throwable th) {
        super(str2, th);
        if (str != null) {
            this.error = str;
            this.errorUri = str3;
            return;
        }
        throw new AssertionError();
    }

    public String getError() {
        return this.error;
    }

    public String getErrorUri() {
        return this.errorUri;
    }
}
