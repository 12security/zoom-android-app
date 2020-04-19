package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;

public class ServletContextCleaner implements ServletContextListener {
    private static final Class[] RELEASE_SIGNATURE;
    static /* synthetic */ Class class$java$lang$ClassLoader;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    static {
        Class[] clsArr = new Class[1];
        Class cls = class$java$lang$ClassLoader;
        if (cls == null) {
            cls = class$("java.lang.ClassLoader");
            class$java$lang$ClassLoader = cls;
        }
        clsArr[0] = cls;
        RELEASE_SIGNATURE = clsArr;
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Object[] objArr = {contextClassLoader};
        ClassLoader classLoader = contextClassLoader;
        while (classLoader != null) {
            try {
                Class loadClass = classLoader.loadClass(LogFactory.FACTORY_PROPERTY);
                loadClass.getMethod("release", RELEASE_SIGNATURE).invoke(null, objArr);
                classLoader = loadClass.getClassLoader().getParent();
            } catch (ClassNotFoundException unused) {
                classLoader = null;
            } catch (NoSuchMethodException unused2) {
                System.err.println("LogFactory instance found which does not support release method!");
                classLoader = null;
            } catch (IllegalAccessException unused3) {
                System.err.println("LogFactory instance found which is not accessable!");
                classLoader = null;
            } catch (InvocationTargetException unused4) {
                System.err.println("LogFactory instance release method failed!");
                classLoader = null;
            }
        }
        LogFactory.release(contextClassLoader);
    }
}
