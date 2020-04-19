package org.apache.commons.logging.impl;

import androidx.core.p002os.EnvironmentCompat;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk14Logger implements Log, Serializable {
    protected static final Level dummyLevel = Level.FINE;
    private static final long serialVersionUID = 4784713551416303804L;
    protected transient Logger logger = null;
    protected String name = null;

    public Jdk14Logger(String str) {
        this.name = str;
        this.logger = getLogger();
    }

    /* access modifiers changed from: protected */
    public void log(Level level, String str, Throwable th) {
        Logger logger2 = getLogger();
        if (logger2.isLoggable(level)) {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            String str2 = this.name;
            String str3 = EnvironmentCompat.MEDIA_UNKNOWN;
            if (stackTrace != null && stackTrace.length > 2) {
                str3 = stackTrace[2].getMethodName();
            }
            if (th == null) {
                logger2.logp(level, str2, str3, str);
            } else {
                logger2.logp(level, str2, str3, str, th);
            }
        }
    }

    public void debug(Object obj) {
        log(Level.FINE, String.valueOf(obj), null);
    }

    public void debug(Object obj, Throwable th) {
        log(Level.FINE, String.valueOf(obj), th);
    }

    public void error(Object obj) {
        log(Level.SEVERE, String.valueOf(obj), null);
    }

    public void error(Object obj, Throwable th) {
        log(Level.SEVERE, String.valueOf(obj), th);
    }

    public void fatal(Object obj) {
        log(Level.SEVERE, String.valueOf(obj), null);
    }

    public void fatal(Object obj, Throwable th) {
        log(Level.SEVERE, String.valueOf(obj), th);
    }

    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = Logger.getLogger(this.name);
        }
        return this.logger;
    }

    public void info(Object obj) {
        log(Level.INFO, String.valueOf(obj), null);
    }

    public void info(Object obj, Throwable th) {
        log(Level.INFO, String.valueOf(obj), th);
    }

    public boolean isDebugEnabled() {
        return getLogger().isLoggable(Level.FINE);
    }

    public boolean isErrorEnabled() {
        return getLogger().isLoggable(Level.SEVERE);
    }

    public boolean isFatalEnabled() {
        return getLogger().isLoggable(Level.SEVERE);
    }

    public boolean isInfoEnabled() {
        return getLogger().isLoggable(Level.INFO);
    }

    public boolean isTraceEnabled() {
        return getLogger().isLoggable(Level.FINEST);
    }

    public boolean isWarnEnabled() {
        return getLogger().isLoggable(Level.WARNING);
    }

    public void trace(Object obj) {
        log(Level.FINEST, String.valueOf(obj), null);
    }

    public void trace(Object obj, Throwable th) {
        log(Level.FINEST, String.valueOf(obj), th);
    }

    public void warn(Object obj) {
        log(Level.WARNING, String.valueOf(obj), null);
    }

    public void warn(Object obj, Throwable th) {
        log(Level.WARNING, String.valueOf(obj), th);
    }
}
