package org.apache.commons.logging.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.logging.Log;

public class SimpleLog implements Log, Serializable {
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_FATAL = 6;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_OFF = 7;
    public static final int LOG_LEVEL_TRACE = 1;
    public static final int LOG_LEVEL_WARN = 4;
    static /* synthetic */ Class class$java$lang$Thread = null;
    static /* synthetic */ Class class$org$apache$commons$logging$impl$SimpleLog = null;
    protected static DateFormat dateFormatter = null;
    protected static volatile String dateTimeFormat = null;
    private static final long serialVersionUID = 136942970684951178L;
    protected static volatile boolean showDateTime = false;
    protected static volatile boolean showLogName = false;
    protected static volatile boolean showShortName = false;
    protected static final Properties simpleLogProps = new Properties();
    protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
    protected volatile int currentLogLevel;
    protected volatile String logName = null;
    private volatile String shortLogName = null;

    static {
        showLogName = false;
        showShortName = true;
        showDateTime = false;
        dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
        dateFormatter = null;
        InputStream resourceAsStream = getResourceAsStream("simplelog.properties");
        if (resourceAsStream != null) {
            try {
                simpleLogProps.load(resourceAsStream);
                resourceAsStream.close();
            } catch (IOException unused) {
            }
        }
        showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
        showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
        showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime);
        if (showDateTime) {
            dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);
            try {
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            } catch (IllegalArgumentException unused2) {
                dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            }
        }
    }

    private static String getStringProperty(String str) {
        String str2;
        try {
            str2 = System.getProperty(str);
        } catch (SecurityException unused) {
            str2 = null;
        }
        return str2 == null ? simpleLogProps.getProperty(str) : str2;
    }

    private static String getStringProperty(String str, String str2) {
        String stringProperty = getStringProperty(str);
        return stringProperty == null ? str2 : stringProperty;
    }

    private static boolean getBooleanProperty(String str, boolean z) {
        String stringProperty = getStringProperty(str);
        return stringProperty == null ? z : "true".equalsIgnoreCase(stringProperty);
    }

    public SimpleLog(String str) {
        this.logName = str;
        setLevel(3);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("org.apache.commons.logging.simplelog.log.");
        stringBuffer.append(this.logName);
        String stringProperty = getStringProperty(stringBuffer.toString());
        int lastIndexOf = String.valueOf(str).lastIndexOf(".");
        while (stringProperty == null && lastIndexOf > -1) {
            str = str.substring(0, lastIndexOf);
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("org.apache.commons.logging.simplelog.log.");
            stringBuffer2.append(str);
            stringProperty = getStringProperty(stringBuffer2.toString());
            lastIndexOf = String.valueOf(str).lastIndexOf(".");
        }
        if (stringProperty == null) {
            stringProperty = getStringProperty("org.apache.commons.logging.simplelog.defaultlog");
        }
        if (BoxRequestEvent.STREAM_TYPE_ALL.equalsIgnoreCase(stringProperty)) {
            setLevel(0);
        } else if ("trace".equalsIgnoreCase(stringProperty)) {
            setLevel(1);
        } else if ("debug".equalsIgnoreCase(stringProperty)) {
            setLevel(2);
        } else if ("info".equalsIgnoreCase(stringProperty)) {
            setLevel(3);
        } else if ("warn".equalsIgnoreCase(stringProperty)) {
            setLevel(4);
        } else if ("error".equalsIgnoreCase(stringProperty)) {
            setLevel(5);
        } else if ("fatal".equalsIgnoreCase(stringProperty)) {
            setLevel(6);
        } else if ("off".equalsIgnoreCase(stringProperty)) {
            setLevel(7);
        }
    }

    public void setLevel(int i) {
        this.currentLogLevel = i;
    }

    public int getLevel() {
        return this.currentLogLevel;
    }

    /* access modifiers changed from: protected */
    public void log(int i, Object obj, Throwable th) {
        String format;
        StringBuffer stringBuffer = new StringBuffer();
        if (showDateTime) {
            Date date = new Date();
            synchronized (dateFormatter) {
                format = dateFormatter.format(date);
            }
            stringBuffer.append(format);
            stringBuffer.append(OAuth.SCOPE_DELIMITER);
        }
        switch (i) {
            case 1:
                stringBuffer.append("[TRACE] ");
                break;
            case 2:
                stringBuffer.append("[DEBUG] ");
                break;
            case 3:
                stringBuffer.append("[INFO] ");
                break;
            case 4:
                stringBuffer.append("[WARN] ");
                break;
            case 5:
                stringBuffer.append("[ERROR] ");
                break;
            case 6:
                stringBuffer.append("[FATAL] ");
                break;
        }
        if (showShortName) {
            if (this.shortLogName == null) {
                String substring = this.logName.substring(this.logName.lastIndexOf(".") + 1);
                this.shortLogName = substring.substring(substring.lastIndexOf("/") + 1);
            }
            stringBuffer.append(String.valueOf(this.shortLogName));
            stringBuffer.append(" - ");
        } else if (showLogName) {
            stringBuffer.append(String.valueOf(this.logName));
            stringBuffer.append(" - ");
        }
        stringBuffer.append(String.valueOf(obj));
        if (th != null) {
            stringBuffer.append(" <");
            stringBuffer.append(th.toString());
            stringBuffer.append(">");
            StringWriter stringWriter = new StringWriter(1024);
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            printWriter.close();
            stringBuffer.append(stringWriter.toString());
        }
        write(stringBuffer);
    }

    /* access modifiers changed from: protected */
    public void write(StringBuffer stringBuffer) {
        System.err.println(stringBuffer.toString());
    }

    /* access modifiers changed from: protected */
    public boolean isLevelEnabled(int i) {
        return i >= this.currentLogLevel;
    }

    public final void debug(Object obj) {
        if (isLevelEnabled(2)) {
            log(2, obj, null);
        }
    }

    public final void debug(Object obj, Throwable th) {
        if (isLevelEnabled(2)) {
            log(2, obj, th);
        }
    }

    public final void trace(Object obj) {
        if (isLevelEnabled(1)) {
            log(1, obj, null);
        }
    }

    public final void trace(Object obj, Throwable th) {
        if (isLevelEnabled(1)) {
            log(1, obj, th);
        }
    }

    public final void info(Object obj) {
        if (isLevelEnabled(3)) {
            log(3, obj, null);
        }
    }

    public final void info(Object obj, Throwable th) {
        if (isLevelEnabled(3)) {
            log(3, obj, th);
        }
    }

    public final void warn(Object obj) {
        if (isLevelEnabled(4)) {
            log(4, obj, null);
        }
    }

    public final void warn(Object obj, Throwable th) {
        if (isLevelEnabled(4)) {
            log(4, obj, th);
        }
    }

    public final void error(Object obj) {
        if (isLevelEnabled(5)) {
            log(5, obj, null);
        }
    }

    public final void error(Object obj, Throwable th) {
        if (isLevelEnabled(5)) {
            log(5, obj, th);
        }
    }

    public final void fatal(Object obj) {
        if (isLevelEnabled(6)) {
            log(6, obj, null);
        }
    }

    public final void fatal(Object obj, Throwable th) {
        if (isLevelEnabled(6)) {
            log(6, obj, th);
        }
    }

    public final boolean isDebugEnabled() {
        return isLevelEnabled(2);
    }

    public final boolean isErrorEnabled() {
        return isLevelEnabled(5);
    }

    public final boolean isFatalEnabled() {
        return isLevelEnabled(6);
    }

    public final boolean isInfoEnabled() {
        return isLevelEnabled(3);
    }

    public final boolean isTraceEnabled() {
        return isLevelEnabled(1);
    }

    public final boolean isWarnEnabled() {
        return isLevelEnabled(4);
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[ExcHandler: NoSuchMethodException (unused java.lang.NoSuchMethodException), SYNTHETIC, Splitter:B:1:0x0001] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.ClassLoader getContextClassLoader() {
        /*
            r0 = 0
            java.lang.Class r1 = class$java$lang$Thread     // Catch:{ NoSuchMethodException -> 0x003e }
            if (r1 != 0) goto L_0x000e
            java.lang.String r1 = "java.lang.Thread"
            java.lang.Class r1 = class$(r1)     // Catch:{ NoSuchMethodException -> 0x003e }
            class$java$lang$Thread = r1     // Catch:{ NoSuchMethodException -> 0x003e }
            goto L_0x0010
        L_0x000e:
            java.lang.Class r1 = class$java$lang$Thread     // Catch:{ NoSuchMethodException -> 0x003e }
        L_0x0010:
            java.lang.String r2 = "getContextClassLoader"
            r3 = r0
            java.lang.Class[] r3 = (java.lang.Class[]) r3     // Catch:{ NoSuchMethodException -> 0x003e }
            java.lang.reflect.Method r1 = r1.getMethod(r2, r3)     // Catch:{ NoSuchMethodException -> 0x003e }
            java.lang.Thread r2 = java.lang.Thread.currentThread()     // Catch:{ InvocationTargetException -> 0x0028, NoSuchMethodException -> 0x003e }
            r3 = r0
            java.lang.Class[] r3 = (java.lang.Class[]) r3     // Catch:{ InvocationTargetException -> 0x0028, NoSuchMethodException -> 0x003e }
            java.lang.Object r1 = r1.invoke(r2, r3)     // Catch:{ InvocationTargetException -> 0x0028, NoSuchMethodException -> 0x003e }
            java.lang.ClassLoader r1 = (java.lang.ClassLoader) r1     // Catch:{ InvocationTargetException -> 0x0028, NoSuchMethodException -> 0x003e }
            r0 = r1
            goto L_0x003e
        L_0x0028:
            r1 = move-exception
            java.lang.Throwable r2 = r1.getTargetException()     // Catch:{ NoSuchMethodException -> 0x003e }
            boolean r2 = r2 instanceof java.lang.SecurityException     // Catch:{ NoSuchMethodException -> 0x003e }
            if (r2 == 0) goto L_0x0032
            goto L_0x003e
        L_0x0032:
            org.apache.commons.logging.LogConfigurationException r2 = new org.apache.commons.logging.LogConfigurationException     // Catch:{ NoSuchMethodException -> 0x003e }
            java.lang.String r3 = "Unexpected InvocationTargetException"
            java.lang.Throwable r1 = r1.getTargetException()     // Catch:{ NoSuchMethodException -> 0x003e }
            r2.<init>(r3, r1)     // Catch:{ NoSuchMethodException -> 0x003e }
            throw r2     // Catch:{ NoSuchMethodException -> 0x003e }
        L_0x003e:
            if (r0 != 0) goto L_0x0050
            java.lang.Class r0 = class$org$apache$commons$logging$impl$SimpleLog
            if (r0 != 0) goto L_0x004c
            java.lang.String r0 = "org.apache.commons.logging.impl.SimpleLog"
            java.lang.Class r0 = class$(r0)
            class$org$apache$commons$logging$impl$SimpleLog = r0
        L_0x004c:
            java.lang.ClassLoader r0 = r0.getClassLoader()
        L_0x0050:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.impl.SimpleLog.getContextClassLoader():java.lang.ClassLoader");
    }

    private static InputStream getResourceAsStream(final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                ClassLoader access$000 = SimpleLog.getContextClassLoader();
                if (access$000 != null) {
                    return access$000.getResourceAsStream(str);
                }
                return ClassLoader.getSystemResourceAsStream(str);
            }
        });
    }
}
