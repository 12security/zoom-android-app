package com.google.api.client.http.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class DefaultConnectionFactory implements ConnectionFactory {
    private final Proxy proxy;

    public DefaultConnectionFactory() {
        this(null);
    }

    public DefaultConnectionFactory(Proxy proxy2) {
        this.proxy = proxy2;
    }

    public HttpURLConnection openConnection(URL url) throws IOException {
        Proxy proxy2 = this.proxy;
        return (HttpURLConnection) (proxy2 == null ? url.openConnection() : url.openConnection(proxy2));
    }
}
