package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.io.IOException;

public final class BasicAuthentication implements HttpRequestInitializer, HttpExecuteInterceptor {
    private final String password;
    private final String username;

    public BasicAuthentication(String str, String str2) {
        this.username = (String) Preconditions.checkNotNull(str);
        this.password = (String) Preconditions.checkNotNull(str2);
    }

    public void initialize(HttpRequest httpRequest) throws IOException {
        httpRequest.setInterceptor(this);
    }

    public void intercept(HttpRequest httpRequest) throws IOException {
        httpRequest.getHeaders().setBasicAuthentication(this.username, this.password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
