package org.apache.commons.logging.impl;

import androidx.core.p002os.EnvironmentCompat;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk13LumberjackLogger implements Log, Serializable {
    protected static final Level dummyLevel = Level.FINE;
    private static final long serialVersionUID = -8649807923527610591L;
    private boolean classAndMethodFound = false;
    protected transient Logger logger = null;
    protected String name = null;
    private String sourceClassName = EnvironmentCompat.MEDIA_UNKNOWN;
    private String sourceMethodName = EnvironmentCompat.MEDIA_UNKNOWN;

    public Jdk13LumberjackLogger(String str) {
        this.name = str;
        this.logger = getLogger();
    }

    private void log(Level level, String str, Throwable th) {
        if (getLogger().isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str);
            if (!this.classAndMethodFound) {
                getClassAndMethod();
            }
            logRecord.setSourceClassName(this.sourceClassName);
            logRecord.setSourceMethodName(this.sourceMethodName);
            if (th != null) {
                logRecord.setThrown(th);
            }
            getLogger().log(logRecord);
        }
    }

    private void getClassAndMethod() {
        try {
            Throwable th = new Throwable();
            th.fillInStackTrace();
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            StringTokenizer stringTokenizer = new StringTokenizer(stringWriter.getBuffer().toString(), FontStyleHelper.SPLITOR);
            stringTokenizer.nextToken();
            String nextToken = stringTokenizer.nextToken();
            while (nextToken.indexOf(getClass().getName()) == -1) {
                nextToken = stringTokenizer.nextToken();
            }
            while (nextToken.indexOf(getClass().getName()) >= 0) {
                nextToken = stringTokenizer.nextToken();
            }
            String substring = nextToken.substring(nextToken.indexOf("at ") + 3, nextToken.indexOf(40));
            int lastIndexOf = substring.lastIndexOf(46);
            this.sourceClassName = substring.substring(0, lastIndexOf);
            this.sourceMethodName = substring.substring(lastIndexOf + 1);
        } catch (Exception unused) {
        }
        this.classAndMethodFound = true;
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
