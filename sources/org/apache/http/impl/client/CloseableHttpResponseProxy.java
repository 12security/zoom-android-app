package org.apache.http.impl.client;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

@Deprecated
class CloseableHttpResponseProxy implements InvocationHandler {
    private static final Constructor<?> CONSTRUCTOR;
    private final HttpResponse original;

    static {
        try {
            CONSTRUCTOR = Proxy.getProxyClass(CloseableHttpResponseProxy.class.getClassLoader(), new Class[]{CloseableHttpResponse.class}).getConstructor(new Class[]{InvocationHandler.class});
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    CloseableHttpResponseProxy(HttpResponse httpResponse) {
        this.original = httpResponse;
    }

    public void close() throws IOException {
        EntityUtils.consume(this.original.getEntity());
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        if (method.getName().equals("close")) {
            close();
            return null;
        }
        try {
            return method.invoke(this.original, objArr);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                throw cause;
            }
            throw e;
        }
    }

    public static CloseableHttpResponse newProxy(HttpResponse httpResponse) {
        try {
            return (CloseableHttpResponse) CONSTRUCTOR.newInstance(new Object[]{new CloseableHttpResponseProxy(httpResponse)});
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e2) {
            throw new IllegalStateException(e2);
        } catch (IllegalAccessException e3) {
            throw new IllegalStateException(e3);
        }
    }
}
