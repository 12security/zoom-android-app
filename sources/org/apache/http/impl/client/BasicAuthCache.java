package org.apache.http.impl.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.SAFE)
public class BasicAuthCache implements AuthCache {
    private final Log log;
    private final Map<HttpHost, byte[]> map;
    private final SchemePortResolver schemePortResolver;

    public BasicAuthCache(SchemePortResolver schemePortResolver2) {
        this.log = LogFactory.getLog(getClass());
        this.map = new ConcurrentHashMap();
        if (schemePortResolver2 == null) {
            schemePortResolver2 = DefaultSchemePortResolver.INSTANCE;
        }
        this.schemePortResolver = schemePortResolver2;
    }

    public BasicAuthCache() {
        this(null);
    }

    /* access modifiers changed from: protected */
    public HttpHost getKey(HttpHost httpHost) {
        if (httpHost.getPort() > 0) {
            return httpHost;
        }
        try {
            return new HttpHost(httpHost.getHostName(), this.schemePortResolver.resolve(httpHost), httpHost.getSchemeName());
        } catch (UnsupportedSchemeException unused) {
            return httpHost;
        }
    }

    public void put(HttpHost httpHost, AuthScheme authScheme) {
        Args.notNull(httpHost, "HTTP host");
        if (authScheme != null) {
            if (authScheme instanceof Serializable) {
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(authScheme);
                    objectOutputStream.close();
                    this.map.put(getKey(httpHost), byteArrayOutputStream.toByteArray());
                } catch (IOException e) {
                    if (this.log.isWarnEnabled()) {
                        this.log.warn("Unexpected I/O error while serializing auth scheme", e);
                    }
                }
            } else if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Auth scheme ");
                sb.append(authScheme.getClass());
                sb.append(" is not serializable");
                log2.debug(sb.toString());
            }
        }
    }

    public AuthScheme get(HttpHost httpHost) {
        Args.notNull(httpHost, "HTTP host");
        byte[] bArr = (byte[]) this.map.get(getKey(httpHost));
        if (bArr == null) {
            return null;
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bArr));
            AuthScheme authScheme = (AuthScheme) objectInputStream.readObject();
            objectInputStream.close();
            return authScheme;
        } catch (IOException e) {
            if (this.log.isWarnEnabled()) {
                this.log.warn("Unexpected I/O error while de-serializing auth scheme", e);
            }
            return null;
        } catch (ClassNotFoundException e2) {
            if (this.log.isWarnEnabled()) {
                this.log.warn("Unexpected error while de-serializing auth scheme", e2);
            }
            return null;
        }
    }

    public void remove(HttpHost httpHost) {
        Args.notNull(httpHost, "HTTP host");
        this.map.remove(getKey(httpHost));
    }

    public void clear() {
        this.map.clear();
    }

    public String toString() {
        return this.map.toString();
    }
}
