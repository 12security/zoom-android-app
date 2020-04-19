package com.google.api.client.googleapis.auth.clientlogin;

import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.ErrorInfo;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpResponseException.Builder;
import com.google.api.client.util.Beta;

@Beta
public class ClientLoginResponseException extends HttpResponseException {
    private static final long serialVersionUID = 4974317674023010928L;
    private final transient ErrorInfo details;

    ClientLoginResponseException(Builder builder, ErrorInfo errorInfo) {
        super(builder);
        this.details = errorInfo;
    }

    public final ErrorInfo getDetails() {
        return this.details;
    }
}
