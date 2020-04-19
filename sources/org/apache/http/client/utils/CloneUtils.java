package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;

public class CloneUtils {
    public static <T> T cloneObject(T t) throws CloneNotSupportedException {
        if (t == null) {
            return null;
        }
        if (t instanceof Cloneable) {
            try {
                try {
                    return t.getClass().getMethod("clone", null).invoke(t, null);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof CloneNotSupportedException) {
                        throw ((CloneNotSupportedException) cause);
                    }
                    throw new Error("Unexpected exception", cause);
                } catch (IllegalAccessException e2) {
                    throw new IllegalAccessError(e2.getMessage());
                }
            } catch (NoSuchMethodException e3) {
                throw new NoSuchMethodError(e3.getMessage());
            }
        } else {
            throw new CloneNotSupportedException();
        }
    }

    public static Object clone(Object obj) throws CloneNotSupportedException {
        return cloneObject(obj);
    }

    private CloneUtils() {
    }
}
