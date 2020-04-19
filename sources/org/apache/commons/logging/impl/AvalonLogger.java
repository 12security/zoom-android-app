package org.apache.commons.logging.impl;

import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;

public class AvalonLogger implements Log {
    private static volatile Logger defaultLogger;
    private final transient Logger logger;

    public AvalonLogger(Logger logger2) {
        this.logger = logger2;
    }

    public AvalonLogger(String str) {
        if (defaultLogger != null) {
            this.logger = defaultLogger.getChildLogger(str);
            return;
        }
        throw new NullPointerException("default logger has to be specified if this constructor is used!");
    }

    public Logger getLogger() {
        return this.logger;
    }

    public static void setDefaultLogger(Logger logger2) {
        defaultLogger = logger2;
    }

    public void debug(Object obj, Throwable th) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.valueOf(obj), th);
        }
    }

    public void debug(Object obj) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.valueOf(obj));
        }
    }

    public void error(Object obj, Throwable th) {
        if (getLogger().isErrorEnabled()) {
            getLogger().error(String.valueOf(obj), th);
        }
    }

    public void error(Object obj) {
        if (getLogger().isErrorEnabled()) {
            getLogger().error(String.valueOf(obj));
        }
    }

    public void fatal(Object obj, Throwable th) {
        if (getLogger().isFatalErrorEnabled()) {
            getLogger().fatalError(String.valueOf(obj), th);
        }
    }

    public void fatal(Object obj) {
        if (getLogger().isFatalErrorEnabled()) {
            getLogger().fatalError(String.valueOf(obj));
        }
    }

    public void info(Object obj, Throwable th) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(String.valueOf(obj), th);
        }
    }

    public void info(Object obj) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(String.valueOf(obj));
        }
    }

    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return getLogger().isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return getLogger().isFatalErrorEnabled();
    }

    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return getLogger().isDebugEnabled();
    }

    public boolean isWarnEnabled() {
        return getLogger().isWarnEnabled();
    }

    public void trace(Object obj, Throwable th) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.valueOf(obj), th);
        }
    }

    public void trace(Object obj) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.valueOf(obj));
        }
    }

    public void warn(Object obj, Throwable th) {
        if (getLogger().isWarnEnabled()) {
            getLogger().warn(String.valueOf(obj), th);
        }
    }

    public void warn(Object obj) {
        if (getLogger().isWarnEnabled()) {
            getLogger().warn(String.valueOf(obj));
        }
    }
}
