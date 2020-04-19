package org.apache.http.client.methods;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderGroup;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

public class RequestBuilder {
    private Charset charset;
    private RequestConfig config;
    private HttpEntity entity;
    private HeaderGroup headergroup;
    private String method;
    private List<NameValuePair> parameters;
    private URI uri;
    private ProtocolVersion version;

    static class InternalEntityEclosingRequest extends HttpEntityEnclosingRequestBase {
        private final String method;

        InternalEntityEclosingRequest(String str) {
            this.method = str;
        }

        public String getMethod() {
            return this.method;
        }
    }

    static class InternalRequest extends HttpRequestBase {
        private final String method;

        InternalRequest(String str) {
            this.method = str;
        }

        public String getMethod() {
            return this.method;
        }
    }

    RequestBuilder(String str) {
        this.charset = Consts.UTF_8;
        this.method = str;
    }

    RequestBuilder(String str, URI uri2) {
        this.method = str;
        this.uri = uri2;
    }

    RequestBuilder(String str, String str2) {
        this.method = str;
        this.uri = str2 != null ? URI.create(str2) : null;
    }

    RequestBuilder() {
        this(null);
    }

    public static RequestBuilder create(String str) {
        Args.notBlank(str, "HTTP method");
        return new RequestBuilder(str);
    }

    public static RequestBuilder get() {
        return new RequestBuilder("GET");
    }

    public static RequestBuilder get(URI uri2) {
        return new RequestBuilder("GET", uri2);
    }

    public static RequestBuilder get(String str) {
        return new RequestBuilder("GET", str);
    }

    public static RequestBuilder head() {
        return new RequestBuilder("HEAD");
    }

    public static RequestBuilder head(URI uri2) {
        return new RequestBuilder("HEAD", uri2);
    }

    public static RequestBuilder head(String str) {
        return new RequestBuilder("HEAD", str);
    }

    public static RequestBuilder patch() {
        return new RequestBuilder("PATCH");
    }

    public static RequestBuilder patch(URI uri2) {
        return new RequestBuilder("PATCH", uri2);
    }

    public static RequestBuilder patch(String str) {
        return new RequestBuilder("PATCH", str);
    }

    public static RequestBuilder post() {
        return new RequestBuilder("POST");
    }

    public static RequestBuilder post(URI uri2) {
        return new RequestBuilder("POST", uri2);
    }

    public static RequestBuilder post(String str) {
        return new RequestBuilder("POST", str);
    }

    public static RequestBuilder put() {
        return new RequestBuilder("PUT");
    }

    public static RequestBuilder put(URI uri2) {
        return new RequestBuilder("PUT", uri2);
    }

    public static RequestBuilder put(String str) {
        return new RequestBuilder("PUT", str);
    }

    public static RequestBuilder delete() {
        return new RequestBuilder("DELETE");
    }

    public static RequestBuilder delete(URI uri2) {
        return new RequestBuilder("DELETE", uri2);
    }

    public static RequestBuilder delete(String str) {
        return new RequestBuilder("DELETE", str);
    }

    public static RequestBuilder trace() {
        return new RequestBuilder("TRACE");
    }

    public static RequestBuilder trace(URI uri2) {
        return new RequestBuilder("TRACE", uri2);
    }

    public static RequestBuilder trace(String str) {
        return new RequestBuilder("TRACE", str);
    }

    public static RequestBuilder options() {
        return new RequestBuilder("OPTIONS");
    }

    public static RequestBuilder options(URI uri2) {
        return new RequestBuilder("OPTIONS", uri2);
    }

    public static RequestBuilder options(String str) {
        return new RequestBuilder("OPTIONS", str);
    }

    public static RequestBuilder copy(HttpRequest httpRequest) {
        Args.notNull(httpRequest, "HTTP request");
        return new RequestBuilder().doCopy(httpRequest);
    }

    private RequestBuilder doCopy(HttpRequest httpRequest) {
        if (httpRequest == null) {
            return this;
        }
        this.method = httpRequest.getRequestLine().getMethod();
        this.version = httpRequest.getRequestLine().getProtocolVersion();
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.clear();
        this.headergroup.setHeaders(httpRequest.getAllHeaders());
        this.parameters = null;
        this.entity = null;
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity2 = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
            ContentType contentType = ContentType.get(entity2);
            if (contentType == null || !contentType.getMimeType().equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
                this.entity = entity2;
            } else {
                try {
                    List<NameValuePair> parse = URLEncodedUtils.parse(entity2);
                    if (!parse.isEmpty()) {
                        this.parameters = parse;
                    }
                } catch (IOException unused) {
                }
            }
        }
        if (httpRequest instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest) httpRequest).getURI();
        } else {
            this.uri = URI.create(httpRequest.getRequestLine().getUri());
        }
        if (httpRequest instanceof Configurable) {
            this.config = ((Configurable) httpRequest).getConfig();
        } else {
            this.config = null;
        }
        return this;
    }

    public RequestBuilder setCharset(Charset charset2) {
        this.charset = charset2;
        return this;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getMethod() {
        return this.method;
    }

    public ProtocolVersion getVersion() {
        return this.version;
    }

    public RequestBuilder setVersion(ProtocolVersion protocolVersion) {
        this.version = protocolVersion;
        return this;
    }

    public URI getUri() {
        return this.uri;
    }

    public RequestBuilder setUri(URI uri2) {
        this.uri = uri2;
        return this;
    }

    public RequestBuilder setUri(String str) {
        this.uri = str != null ? URI.create(str) : null;
        return this;
    }

    public Header getFirstHeader(String str) {
        HeaderGroup headerGroup = this.headergroup;
        if (headerGroup != null) {
            return headerGroup.getFirstHeader(str);
        }
        return null;
    }

    public Header getLastHeader(String str) {
        HeaderGroup headerGroup = this.headergroup;
        if (headerGroup != null) {
            return headerGroup.getLastHeader(str);
        }
        return null;
    }

    public Header[] getHeaders(String str) {
        HeaderGroup headerGroup = this.headergroup;
        if (headerGroup != null) {
            return headerGroup.getHeaders(str);
        }
        return null;
    }

    public RequestBuilder addHeader(Header header) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.addHeader(header);
        return this;
    }

    public RequestBuilder addHeader(String str, String str2) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.addHeader(new BasicHeader(str, str2));
        return this;
    }

    public RequestBuilder removeHeader(Header header) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.removeHeader(header);
        return this;
    }

    public RequestBuilder removeHeaders(String str) {
        if (str != null) {
            HeaderGroup headerGroup = this.headergroup;
            if (headerGroup != null) {
                HeaderIterator it = headerGroup.iterator();
                while (it.hasNext()) {
                    if (str.equalsIgnoreCase(it.nextHeader().getName())) {
                        it.remove();
                    }
                }
                return this;
            }
        }
        return this;
    }

    public RequestBuilder setHeader(Header header) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.updateHeader(header);
        return this;
    }

    public RequestBuilder setHeader(String str, String str2) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.updateHeader(new BasicHeader(str, str2));
        return this;
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public RequestBuilder setEntity(HttpEntity httpEntity) {
        this.entity = httpEntity;
        return this;
    }

    public List<NameValuePair> getParameters() {
        List<NameValuePair> list = this.parameters;
        return list != null ? new ArrayList(list) : new ArrayList();
    }

    public RequestBuilder addParameter(NameValuePair nameValuePair) {
        Args.notNull(nameValuePair, "Name value pair");
        if (this.parameters == null) {
            this.parameters = new LinkedList();
        }
        this.parameters.add(nameValuePair);
        return this;
    }

    public RequestBuilder addParameter(String str, String str2) {
        return addParameter(new BasicNameValuePair(str, str2));
    }

    public RequestBuilder addParameters(NameValuePair... nameValuePairArr) {
        for (NameValuePair addParameter : nameValuePairArr) {
            addParameter(addParameter);
        }
        return this;
    }

    public RequestConfig getConfig() {
        return this.config;
    }

    public RequestBuilder setConfig(RequestConfig requestConfig) {
        this.config = requestConfig;
        return this;
    }

    public HttpUriRequest build() {
        HttpRequestBase httpRequestBase;
        URI uri2 = this.uri;
        if (uri2 == null) {
            uri2 = URI.create("/");
        }
        HttpEntity httpEntity = this.entity;
        List<NameValuePair> list = this.parameters;
        if (list != null && !list.isEmpty()) {
            if (httpEntity != null || (!"POST".equalsIgnoreCase(this.method) && !"PUT".equalsIgnoreCase(this.method))) {
                try {
                    uri2 = new URIBuilder(uri2).setCharset(this.charset).addParameters(this.parameters).build();
                } catch (URISyntaxException unused) {
                }
            } else {
                List<NameValuePair> list2 = this.parameters;
                Charset charset2 = this.charset;
                if (charset2 == null) {
                    charset2 = HTTP.DEF_CONTENT_CHARSET;
                }
                httpEntity = new UrlEncodedFormEntity((Iterable<? extends NameValuePair>) list2, charset2);
            }
        }
        if (httpEntity == null) {
            httpRequestBase = new InternalRequest(this.method);
        } else {
            InternalEntityEclosingRequest internalEntityEclosingRequest = new InternalEntityEclosingRequest(this.method);
            internalEntityEclosingRequest.setEntity(httpEntity);
            httpRequestBase = internalEntityEclosingRequest;
        }
        httpRequestBase.setProtocolVersion(this.version);
        httpRequestBase.setURI(uri2);
        HeaderGroup headerGroup = this.headergroup;
        if (headerGroup != null) {
            httpRequestBase.setHeaders(headerGroup.getAllHeaders());
        }
        httpRequestBase.setConfig(this.config);
        return httpRequestBase;
    }
}
