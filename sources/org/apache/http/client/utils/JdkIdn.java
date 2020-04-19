package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class JdkIdn implements Idn {
    private final Method toUnicode;

    public JdkIdn() throws ClassNotFoundException {
        try {
            this.toUnicode = Class.forName("java.net.IDN").getMethod("toUnicode", new Class[]{String.class});
        } catch (SecurityException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (NoSuchMethodException e2) {
            throw new IllegalStateException(e2.getMessage(), e2);
        }
    }

    public String toUnicode(String str) {
        try {
            return (String) this.toUnicode.invoke(null, new Object[]{str});
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            throw new RuntimeException(cause.getMessage(), cause);
        }
    }
}
