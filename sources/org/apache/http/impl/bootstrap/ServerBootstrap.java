package org.apache.http.impl.bootstrap;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerMapper;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;

public class ServerBootstrap {
    private ConnectionReuseStrategy connStrategy;
    private ConnectionConfig connectionConfig;
    private HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory;
    private ExceptionLogger exceptionLogger;
    private HttpExpectationVerifier expectationVerifier;
    private Map<String, HttpRequestHandler> handlerMap;
    private HttpRequestHandlerMapper handlerMapper;
    private HttpProcessor httpProcessor;
    private int listenerPort;
    private InetAddress localAddress;
    private LinkedList<HttpRequestInterceptor> requestFirst;
    private LinkedList<HttpRequestInterceptor> requestLast;
    private HttpResponseFactory responseFactory;
    private LinkedList<HttpResponseInterceptor> responseFirst;
    private LinkedList<HttpResponseInterceptor> responseLast;
    private String serverInfo;
    private ServerSocketFactory serverSocketFactory;
    private SocketConfig socketConfig;
    private SSLContext sslContext;
    private SSLServerSetupHandler sslSetupHandler;

    private ServerBootstrap() {
    }

    public static ServerBootstrap bootstrap() {
        return new ServerBootstrap();
    }

    public final ServerBootstrap setListenerPort(int i) {
        this.listenerPort = i;
        return this;
    }

    public final ServerBootstrap setLocalAddress(InetAddress inetAddress) {
        this.localAddress = inetAddress;
        return this;
    }

    public final ServerBootstrap setSocketConfig(SocketConfig socketConfig2) {
        this.socketConfig = socketConfig2;
        return this;
    }

    public final ServerBootstrap setConnectionConfig(ConnectionConfig connectionConfig2) {
        this.connectionConfig = connectionConfig2;
        return this;
    }

    public final ServerBootstrap setHttpProcessor(HttpProcessor httpProcessor2) {
        this.httpProcessor = httpProcessor2;
        return this;
    }

    public final ServerBootstrap addInterceptorFirst(HttpResponseInterceptor httpResponseInterceptor) {
        if (httpResponseInterceptor == null) {
            return this;
        }
        if (this.responseFirst == null) {
            this.responseFirst = new LinkedList<>();
        }
        this.responseFirst.addFirst(httpResponseInterceptor);
        return this;
    }

    public final ServerBootstrap addInterceptorLast(HttpResponseInterceptor httpResponseInterceptor) {
        if (httpResponseInterceptor == null) {
            return this;
        }
        if (this.responseLast == null) {
            this.responseLast = new LinkedList<>();
        }
        this.responseLast.addLast(httpResponseInterceptor);
        return this;
    }

    public final ServerBootstrap addInterceptorFirst(HttpRequestInterceptor httpRequestInterceptor) {
        if (httpRequestInterceptor == null) {
            return this;
        }
        if (this.requestFirst == null) {
            this.requestFirst = new LinkedList<>();
        }
        this.requestFirst.addFirst(httpRequestInterceptor);
        return this;
    }

    public final ServerBootstrap addInterceptorLast(HttpRequestInterceptor httpRequestInterceptor) {
        if (httpRequestInterceptor == null) {
            return this;
        }
        if (this.requestLast == null) {
            this.requestLast = new LinkedList<>();
        }
        this.requestLast.addLast(httpRequestInterceptor);
        return this;
    }

    public final ServerBootstrap setServerInfo(String str) {
        this.serverInfo = str;
        return this;
    }

    public final ServerBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy connectionReuseStrategy) {
        this.connStrategy = connectionReuseStrategy;
        return this;
    }

    public final ServerBootstrap setResponseFactory(HttpResponseFactory httpResponseFactory) {
        this.responseFactory = httpResponseFactory;
        return this;
    }

    public final ServerBootstrap setHandlerMapper(HttpRequestHandlerMapper httpRequestHandlerMapper) {
        this.handlerMapper = httpRequestHandlerMapper;
        return this;
    }

    public final ServerBootstrap registerHandler(String str, HttpRequestHandler httpRequestHandler) {
        if (str == null || httpRequestHandler == null) {
            return this;
        }
        if (this.handlerMap == null) {
            this.handlerMap = new HashMap();
        }
        this.handlerMap.put(str, httpRequestHandler);
        return this;
    }

    public final ServerBootstrap setExpectationVerifier(HttpExpectationVerifier httpExpectationVerifier) {
        this.expectationVerifier = httpExpectationVerifier;
        return this;
    }

    public final ServerBootstrap setConnectionFactory(HttpConnectionFactory<? extends DefaultBHttpServerConnection> httpConnectionFactory) {
        this.connectionFactory = httpConnectionFactory;
        return this;
    }

    public final ServerBootstrap setSslSetupHandler(SSLServerSetupHandler sSLServerSetupHandler) {
        this.sslSetupHandler = sSLServerSetupHandler;
        return this;
    }

    public final ServerBootstrap setServerSocketFactory(ServerSocketFactory serverSocketFactory2) {
        this.serverSocketFactory = serverSocketFactory2;
        return this;
    }

    public final ServerBootstrap setSslContext(SSLContext sSLContext) {
        this.sslContext = sSLContext;
        return this;
    }

    public final ServerBootstrap setExceptionLogger(ExceptionLogger exceptionLogger2) {
        this.exceptionLogger = exceptionLogger2;
        return this;
    }

    public HttpServer create() {
        HttpProcessor httpProcessor2;
        HttpRequestHandlerMapper httpRequestHandlerMapper;
        ServerSocketFactory serverSocketFactory2;
        HttpConnectionFactory<? extends DefaultBHttpServerConnection> httpConnectionFactory;
        HttpProcessor httpProcessor3 = this.httpProcessor;
        if (httpProcessor3 == null) {
            HttpProcessorBuilder create = HttpProcessorBuilder.create();
            LinkedList<HttpRequestInterceptor> linkedList = this.requestFirst;
            if (linkedList != null) {
                Iterator it = linkedList.iterator();
                while (it.hasNext()) {
                    create.addFirst((HttpRequestInterceptor) it.next());
                }
            }
            LinkedList<HttpResponseInterceptor> linkedList2 = this.responseFirst;
            if (linkedList2 != null) {
                Iterator it2 = linkedList2.iterator();
                while (it2.hasNext()) {
                    create.addFirst((HttpResponseInterceptor) it2.next());
                }
            }
            String str = this.serverInfo;
            if (str == null) {
                str = "Apache-HttpCore/1.1";
            }
            create.addAll(new ResponseDate(), new ResponseServer(str), new ResponseContent(), new ResponseConnControl());
            LinkedList<HttpRequestInterceptor> linkedList3 = this.requestLast;
            if (linkedList3 != null) {
                Iterator it3 = linkedList3.iterator();
                while (it3.hasNext()) {
                    create.addLast((HttpRequestInterceptor) it3.next());
                }
            }
            LinkedList<HttpResponseInterceptor> linkedList4 = this.responseLast;
            if (linkedList4 != null) {
                Iterator it4 = linkedList4.iterator();
                while (it4.hasNext()) {
                    create.addLast((HttpResponseInterceptor) it4.next());
                }
            }
            httpProcessor2 = create.build();
        } else {
            httpProcessor2 = httpProcessor3;
        }
        HttpRequestHandlerMapper httpRequestHandlerMapper2 = this.handlerMapper;
        if (httpRequestHandlerMapper2 == 0) {
            UriHttpRequestHandlerMapper uriHttpRequestHandlerMapper = new UriHttpRequestHandlerMapper();
            Map<String, HttpRequestHandler> map = this.handlerMap;
            if (map != null) {
                for (Entry entry : map.entrySet()) {
                    uriHttpRequestHandlerMapper.register((String) entry.getKey(), (HttpRequestHandler) entry.getValue());
                }
            }
            httpRequestHandlerMapper = uriHttpRequestHandlerMapper;
        } else {
            httpRequestHandlerMapper = httpRequestHandlerMapper2;
        }
        ConnectionReuseStrategy connectionReuseStrategy = this.connStrategy;
        ConnectionReuseStrategy connectionReuseStrategy2 = connectionReuseStrategy == null ? DefaultConnectionReuseStrategy.INSTANCE : connectionReuseStrategy;
        HttpResponseFactory httpResponseFactory = this.responseFactory;
        HttpService httpService = new HttpService(httpProcessor2, connectionReuseStrategy2, httpResponseFactory == null ? DefaultHttpResponseFactory.INSTANCE : httpResponseFactory, httpRequestHandlerMapper, this.expectationVerifier);
        ServerSocketFactory serverSocketFactory3 = this.serverSocketFactory;
        if (serverSocketFactory3 == null) {
            SSLContext sSLContext = this.sslContext;
            serverSocketFactory2 = sSLContext != null ? sSLContext.getServerSocketFactory() : ServerSocketFactory.getDefault();
        } else {
            serverSocketFactory2 = serverSocketFactory3;
        }
        HttpConnectionFactory<? extends DefaultBHttpServerConnection> httpConnectionFactory2 = this.connectionFactory;
        if (httpConnectionFactory2 == null) {
            ConnectionConfig connectionConfig2 = this.connectionConfig;
            httpConnectionFactory = connectionConfig2 != null ? new DefaultBHttpServerConnectionFactory<>(connectionConfig2) : DefaultBHttpServerConnectionFactory.INSTANCE;
        } else {
            httpConnectionFactory = httpConnectionFactory2;
        }
        ExceptionLogger exceptionLogger2 = this.exceptionLogger;
        ExceptionLogger exceptionLogger3 = exceptionLogger2 == null ? ExceptionLogger.NO_OP : exceptionLogger2;
        int i = this.listenerPort;
        int i2 = i > 0 ? i : 0;
        InetAddress inetAddress = this.localAddress;
        SocketConfig socketConfig2 = this.socketConfig;
        if (socketConfig2 == null) {
            socketConfig2 = SocketConfig.DEFAULT;
        }
        HttpServer httpServer = new HttpServer(i2, inetAddress, socketConfig2, serverSocketFactory2, httpService, httpConnectionFactory, this.sslSetupHandler, exceptionLogger3);
        return httpServer;
    }
}
