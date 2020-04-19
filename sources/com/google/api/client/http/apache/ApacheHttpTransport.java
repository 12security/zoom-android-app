package com.google.api.client.http.apache;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.SecurityUtils;
import com.google.api.client.util.SslUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProxySelector;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public final class ApacheHttpTransport extends HttpTransport {
    private final HttpClient httpClient;

    public static final class Builder {
        private HttpParams params = ApacheHttpTransport.newDefaultHttpParams();
        private ProxySelector proxySelector = ProxySelector.getDefault();
        private SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();

        public Builder setProxy(HttpHost httpHost) {
            ConnRouteParams.setDefaultProxy(this.params, httpHost);
            if (httpHost != null) {
                this.proxySelector = null;
            }
            return this;
        }

        public Builder setProxySelector(ProxySelector proxySelector2) {
            this.proxySelector = proxySelector2;
            if (proxySelector2 != null) {
                ConnRouteParams.setDefaultProxy(this.params, null);
            }
            return this;
        }

        public Builder trustCertificatesFromJavaKeyStore(InputStream inputStream, String str) throws GeneralSecurityException, IOException {
            KeyStore javaKeyStore = SecurityUtils.getJavaKeyStore();
            SecurityUtils.loadKeyStore(javaKeyStore, inputStream, str);
            return trustCertificates(javaKeyStore);
        }

        public Builder trustCertificatesFromStream(InputStream inputStream) throws GeneralSecurityException, IOException {
            KeyStore javaKeyStore = SecurityUtils.getJavaKeyStore();
            javaKeyStore.load(null, null);
            SecurityUtils.loadKeyStoreFromCertificates(javaKeyStore, SecurityUtils.getX509CertificateFactory(), inputStream);
            return trustCertificates(javaKeyStore);
        }

        public Builder trustCertificates(KeyStore keyStore) throws GeneralSecurityException {
            SSLContext tlsSslContext = SslUtils.getTlsSslContext();
            SslUtils.initSslContext(tlsSslContext, keyStore, SslUtils.getPkixTrustManagerFactory());
            return setSocketFactory(new SSLSocketFactoryExtension(tlsSslContext));
        }

        @Beta
        public Builder doNotValidateCertificate() throws GeneralSecurityException {
            this.socketFactory = new SSLSocketFactoryExtension(SslUtils.trustAllSSLContext());
            this.socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return this;
        }

        public Builder setSocketFactory(SSLSocketFactory sSLSocketFactory) {
            this.socketFactory = (SSLSocketFactory) Preconditions.checkNotNull(sSLSocketFactory);
            return this;
        }

        public SSLSocketFactory getSSLSocketFactory() {
            return this.socketFactory;
        }

        public HttpParams getHttpParams() {
            return this.params;
        }

        public ApacheHttpTransport build() {
            return new ApacheHttpTransport(ApacheHttpTransport.newDefaultHttpClient(this.socketFactory, this.params, this.proxySelector));
        }
    }

    public boolean supportsMethod(String str) {
        return true;
    }

    public ApacheHttpTransport() {
        this(newDefaultHttpClient());
    }

    public ApacheHttpTransport(HttpClient httpClient2) {
        this.httpClient = httpClient2;
        HttpParams params = httpClient2.getParams();
        if (params == null) {
            params = newDefaultHttpClient().getParams();
        }
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
    }

    public static DefaultHttpClient newDefaultHttpClient() {
        return newDefaultHttpClient(SSLSocketFactory.getSocketFactory(), newDefaultHttpParams(), ProxySelector.getDefault());
    }

    static HttpParams newDefaultHttpParams() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 200);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(20));
        return basicHttpParams;
    }

    static DefaultHttpClient newDefaultHttpClient(SSLSocketFactory sSLSocketFactory, HttpParams httpParams, ProxySelector proxySelector) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, (SocketFactory) PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", (SocketFactory) sSLSocketFactory, 443));
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, schemeRegistry), httpParams);
        defaultHttpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        if (proxySelector != null) {
            defaultHttpClient.setRoutePlanner(new ProxySelectorRoutePlanner(schemeRegistry, proxySelector));
        }
        return defaultHttpClient;
    }

    /* access modifiers changed from: protected */
    public ApacheHttpRequest buildRequest(String str, String str2) {
        HttpRequestBase httpRequestBase;
        if (str.equals("DELETE")) {
            httpRequestBase = new HttpDelete(str2);
        } else if (str.equals("GET")) {
            httpRequestBase = new HttpGet(str2);
        } else if (str.equals("HEAD")) {
            httpRequestBase = new HttpHead(str2);
        } else if (str.equals("POST")) {
            httpRequestBase = new HttpPost(str2);
        } else if (str.equals("PUT")) {
            httpRequestBase = new HttpPut(str2);
        } else if (str.equals("TRACE")) {
            httpRequestBase = new HttpTrace(str2);
        } else if (str.equals("OPTIONS")) {
            httpRequestBase = new HttpOptions(str2);
        } else {
            httpRequestBase = new HttpExtensionMethod(str, str2);
        }
        return new ApacheHttpRequest(this.httpClient, httpRequestBase);
    }

    public void shutdown() {
        this.httpClient.getConnectionManager().shutdown();
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }
}
