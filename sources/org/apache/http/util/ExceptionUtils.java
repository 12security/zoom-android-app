package org.apache.http.util;

import java.lang.reflect.Method;

@Deprecated
public final class ExceptionUtils {
    private static final Method INIT_CAUSE_METHOD = getInitCauseMethod();

    private static Method getInitCauseMethod() {
        try {
            return Throwable.class.getMethod("initCause", new Class[]{Throwable.class});
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    public static void initCause(Throwable th, Throwable th2) {
        Method method = INIT_CAUSE_METHOD;
        if (method != null) {
            try {
                method.invoke(th, new Object[]{th2});
            } catch (Exception unused) {
            }
        }
    }

    private ExceptionUtils() {
    }
}
