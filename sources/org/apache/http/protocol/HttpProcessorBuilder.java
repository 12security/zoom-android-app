package org.apache.http.protocol;

import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

public class HttpProcessorBuilder {
    private ChainBuilder<HttpRequestInterceptor> requestChainBuilder;
    private ChainBuilder<HttpResponseInterceptor> responseChainBuilder;

    public static HttpProcessorBuilder create() {
        return new HttpProcessorBuilder();
    }

    HttpProcessorBuilder() {
    }

    private ChainBuilder<HttpRequestInterceptor> getRequestChainBuilder() {
        if (this.requestChainBuilder == null) {
            this.requestChainBuilder = new ChainBuilder<>();
        }
        return this.requestChainBuilder;
    }

    private ChainBuilder<HttpResponseInterceptor> getResponseChainBuilder() {
        if (this.responseChainBuilder == null) {
            this.responseChainBuilder = new ChainBuilder<>();
        }
        return this.responseChainBuilder;
    }

    public HttpProcessorBuilder addFirst(HttpRequestInterceptor httpRequestInterceptor) {
        if (httpRequestInterceptor == null) {
            return this;
        }
        getRequestChainBuilder().addFirst(httpRequestInterceptor);
        return this;
    }

    public HttpProcessorBuilder addLast(HttpRequestInterceptor httpRequestInterceptor) {
        if (httpRequestInterceptor == null) {
            return this;
        }
        getRequestChainBuilder().addLast(httpRequestInterceptor);
        return this;
    }

    public HttpProcessorBuilder add(HttpRequestInterceptor httpRequestInterceptor) {
        return addLast(httpRequestInterceptor);
    }

    public HttpProcessorBuilder addAllFirst(HttpRequestInterceptor... httpRequestInterceptorArr) {
        if (httpRequestInterceptorArr == null) {
            return this;
        }
        getRequestChainBuilder().addAllFirst((E[]) httpRequestInterceptorArr);
        return this;
    }

    public HttpProcessorBuilder addAllLast(HttpRequestInterceptor... httpRequestInterceptorArr) {
        if (httpRequestInterceptorArr == null) {
            return this;
        }
        getRequestChainBuilder().addAllLast((E[]) httpRequestInterceptorArr);
        return this;
    }

    public HttpProcessorBuilder addAll(HttpRequestInterceptor... httpRequestInterceptorArr) {
        return addAllLast(httpRequestInterceptorArr);
    }

    public HttpProcessorBuilder addFirst(HttpResponseInterceptor httpResponseInterceptor) {
        if (httpResponseInterceptor == null) {
            return this;
        }
        getResponseChainBuilder().addFirst(httpResponseInterceptor);
        return this;
    }

    public HttpProcessorBuilder addLast(HttpResponseInterceptor httpResponseInterceptor) {
        if (httpResponseInterceptor == null) {
            return this;
        }
        getResponseChainBuilder().addLast(httpResponseInterceptor);
        return this;
    }

    public HttpProcessorBuilder add(HttpResponseInterceptor httpResponseInterceptor) {
        return addLast(httpResponseInterceptor);
    }

    public HttpProcessorBuilder addAllFirst(HttpResponseInterceptor... httpResponseInterceptorArr) {
        if (httpResponseInterceptorArr == null) {
            return this;
        }
        getResponseChainBuilder().addAllFirst((E[]) httpResponseInterceptorArr);
        return this;
    }

    public HttpProcessorBuilder addAllLast(HttpResponseInterceptor... httpResponseInterceptorArr) {
        if (httpResponseInterceptorArr == null) {
            return this;
        }
        getResponseChainBuilder().addAllLast((E[]) httpResponseInterceptorArr);
        return this;
    }

    public HttpProcessorBuilder addAll(HttpResponseInterceptor... httpResponseInterceptorArr) {
        return addAllLast(httpResponseInterceptorArr);
    }

    public HttpProcessor build() {
        ChainBuilder<HttpRequestInterceptor> chainBuilder = this.requestChainBuilder;
        LinkedList linkedList = null;
        List build = chainBuilder != null ? chainBuilder.build() : null;
        ChainBuilder<HttpResponseInterceptor> chainBuilder2 = this.responseChainBuilder;
        if (chainBuilder2 != null) {
            linkedList = chainBuilder2.build();
        }
        return new ImmutableHttpProcessor(build, (List<HttpResponseInterceptor>) linkedList);
    }
}
