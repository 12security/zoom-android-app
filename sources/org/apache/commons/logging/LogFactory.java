package org.apache.commons.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public abstract class LogFactory {
    public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
    public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
    public static final String FACTORY_PROPERTIES = "commons-logging.properties";
    public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
    public static final String PRIORITY_KEY = "priority";
    protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
    public static final String TCCL_KEY = "use_tccl";
    private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
    static /* synthetic */ Class class$org$apache$commons$logging$LogFactory;
    private static final String diagnosticPrefix;
    private static PrintStream diagnosticsStream = initDiagnostics();
    protected static Hashtable factories = createFactoryStore();
    protected static volatile LogFactory nullClassLoaderFactory;
    private static final ClassLoader thisClassLoader;

    public abstract Object getAttribute(String str);

    public abstract String[] getAttributeNames();

    public abstract Log getInstance(Class cls) throws LogConfigurationException;

    public abstract Log getInstance(String str) throws LogConfigurationException;

    public abstract void release();

    public abstract void removeAttribute(String str);

    public abstract void setAttribute(String str, Object obj);

    protected LogFactory() {
    }

    private static final Hashtable createFactoryStore() {
        String str;
        Hashtable hashtable = null;
        try {
            str = getSystemProperty(HASHTABLE_IMPLEMENTATION_PROPERTY, null);
        } catch (SecurityException unused) {
            str = null;
        }
        if (str == null) {
            str = WEAK_HASHTABLE_CLASSNAME;
        }
        try {
            hashtable = (Hashtable) Class.forName(str).newInstance();
        } catch (Throwable th) {
            handleThrowable(th);
            if (!WEAK_HASHTABLE_CLASSNAME.equals(str)) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
                } else {
                    System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
                }
            }
        }
        return hashtable == null ? new Hashtable() : hashtable;
    }

    private static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    protected static void handleThrowable(Throwable th) {
        if (th instanceof ThreadDeath) {
            throw ((ThreadDeath) th);
        } else if (th instanceof VirtualMachineError) {
            throw ((VirtualMachineError) th);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:50|51|52|53|54|(3:58|(1:60)|61)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:52:0x0115 */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006d A[Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0096 A[Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0194  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01e6  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x01fb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.apache.commons.logging.LogFactory getFactory() throws org.apache.commons.logging.LogConfigurationException {
        /*
            java.lang.ClassLoader r0 = getContextClassLoaderInternal()
            if (r0 != 0) goto L_0x0011
            boolean r1 = isDiagnosticsEnabled()
            if (r1 == 0) goto L_0x0011
            java.lang.String r1 = "Context classloader is null."
            logDiagnostic(r1)
        L_0x0011:
            org.apache.commons.logging.LogFactory r1 = getCachedFactory(r0)
            if (r1 == 0) goto L_0x0018
            return r1
        L_0x0018:
            boolean r2 = isDiagnosticsEnabled()
            if (r2 == 0) goto L_0x003b
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r2.<init>()
            java.lang.String r3 = "[LOOKUP] LogFactory implementation requested for the first time for context classloader "
            r2.append(r3)
            java.lang.String r3 = objectId(r0)
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            logDiagnostic(r2)
            java.lang.String r2 = "[LOOKUP] "
            logHierarchy(r2, r0)
        L_0x003b:
            java.lang.String r2 = "commons-logging.properties"
            java.util.Properties r2 = getConfigurationFile(r0, r2)
            if (r2 == 0) goto L_0x0058
            java.lang.String r3 = "use_tccl"
            java.lang.String r3 = r2.getProperty(r3)
            if (r3 == 0) goto L_0x0058
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
            boolean r3 = r3.booleanValue()
            if (r3 != 0) goto L_0x0058
            java.lang.ClassLoader r3 = thisClassLoader
            goto L_0x0059
        L_0x0058:
            r3 = r0
        L_0x0059:
            boolean r4 = isDiagnosticsEnabled()
            if (r4 == 0) goto L_0x0064
            java.lang.String r4 = "[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use..."
            logDiagnostic(r4)
        L_0x0064:
            java.lang.String r4 = "org.apache.commons.logging.LogFactory"
            r5 = 0
            java.lang.String r4 = getSystemProperty(r4, r5)     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            if (r4 == 0) goto L_0x0096
            boolean r5 = isDiagnosticsEnabled()     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            if (r5 == 0) goto L_0x0091
            java.lang.StringBuffer r5 = new java.lang.StringBuffer     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            r5.<init>()     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            java.lang.String r6 = "[LOOKUP] Creating an instance of LogFactory class '"
            r5.append(r6)     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            r5.append(r4)     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            java.lang.String r6 = "' as specified by system property "
            r5.append(r6)     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            java.lang.String r6 = "org.apache.commons.logging.LogFactory"
            r5.append(r6)     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            java.lang.String r5 = r5.toString()     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            logDiagnostic(r5)     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
        L_0x0091:
            org.apache.commons.logging.LogFactory r1 = newFactory(r4, r3, r0)     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            goto L_0x00f3
        L_0x0096:
            boolean r4 = isDiagnosticsEnabled()     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            if (r4 == 0) goto L_0x00f3
            java.lang.String r4 = "[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined."
            logDiagnostic(r4)     // Catch:{ SecurityException -> 0x00cb, RuntimeException -> 0x00a2 }
            goto L_0x00f3
        L_0x00a2:
            r0 = move-exception
            boolean r1 = isDiagnosticsEnabled()
            if (r1 == 0) goto L_0x00ca
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r2 = "[LOOKUP] An exception occurred while trying to create an instance of the custom factory class: ["
            r1.append(r2)
            java.lang.String r2 = r0.getMessage()
            java.lang.String r2 = trim(r2)
            r1.append(r2)
            java.lang.String r2 = "] as specified by a system property."
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            logDiagnostic(r1)
        L_0x00ca:
            throw r0
        L_0x00cb:
            r4 = move-exception
            boolean r5 = isDiagnosticsEnabled()
            if (r5 == 0) goto L_0x00f3
            java.lang.StringBuffer r5 = new java.lang.StringBuffer
            r5.<init>()
            java.lang.String r6 = "[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: ["
            r5.append(r6)
            java.lang.String r4 = r4.getMessage()
            java.lang.String r4 = trim(r4)
            r5.append(r4)
            java.lang.String r4 = "]. Trying alternative implementations..."
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            logDiagnostic(r4)
        L_0x00f3:
            if (r1 != 0) goto L_0x0192
            boolean r4 = isDiagnosticsEnabled()
            if (r4 == 0) goto L_0x0100
            java.lang.String r4 = "[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use..."
            logDiagnostic(r4)
        L_0x0100:
            java.lang.String r4 = "META-INF/services/org.apache.commons.logging.LogFactory"
            java.io.InputStream r4 = getResourceAsStream(r0, r4)     // Catch:{ Exception -> 0x016a }
            if (r4 == 0) goto L_0x015e
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ UnsupportedEncodingException -> 0x0115 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ UnsupportedEncodingException -> 0x0115 }
            java.lang.String r7 = "UTF-8"
            r6.<init>(r4, r7)     // Catch:{ UnsupportedEncodingException -> 0x0115 }
            r5.<init>(r6)     // Catch:{ UnsupportedEncodingException -> 0x0115 }
            goto L_0x011f
        L_0x0115:
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ Exception -> 0x016a }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x016a }
            r6.<init>(r4)     // Catch:{ Exception -> 0x016a }
            r5.<init>(r6)     // Catch:{ Exception -> 0x016a }
        L_0x011f:
            java.lang.String r4 = r5.readLine()     // Catch:{ Exception -> 0x016a }
            r5.close()     // Catch:{ Exception -> 0x016a }
            if (r4 == 0) goto L_0x0192
            java.lang.String r5 = ""
            boolean r5 = r5.equals(r4)     // Catch:{ Exception -> 0x016a }
            if (r5 != 0) goto L_0x0192
            boolean r5 = isDiagnosticsEnabled()     // Catch:{ Exception -> 0x016a }
            if (r5 == 0) goto L_0x0159
            java.lang.StringBuffer r5 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x016a }
            r5.<init>()     // Catch:{ Exception -> 0x016a }
            java.lang.String r6 = "[LOOKUP]  Creating an instance of LogFactory class "
            r5.append(r6)     // Catch:{ Exception -> 0x016a }
            r5.append(r4)     // Catch:{ Exception -> 0x016a }
            java.lang.String r6 = " as specified by file '"
            r5.append(r6)     // Catch:{ Exception -> 0x016a }
            java.lang.String r6 = "META-INF/services/org.apache.commons.logging.LogFactory"
            r5.append(r6)     // Catch:{ Exception -> 0x016a }
            java.lang.String r6 = "' which was present in the path of the context classloader."
            r5.append(r6)     // Catch:{ Exception -> 0x016a }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x016a }
            logDiagnostic(r5)     // Catch:{ Exception -> 0x016a }
        L_0x0159:
            org.apache.commons.logging.LogFactory r1 = newFactory(r4, r3, r0)     // Catch:{ Exception -> 0x016a }
            goto L_0x0192
        L_0x015e:
            boolean r4 = isDiagnosticsEnabled()     // Catch:{ Exception -> 0x016a }
            if (r4 == 0) goto L_0x0192
            java.lang.String r4 = "[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found."
            logDiagnostic(r4)     // Catch:{ Exception -> 0x016a }
            goto L_0x0192
        L_0x016a:
            r4 = move-exception
            boolean r5 = isDiagnosticsEnabled()
            if (r5 == 0) goto L_0x0192
            java.lang.StringBuffer r5 = new java.lang.StringBuffer
            r5.<init>()
            java.lang.String r6 = "[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: ["
            r5.append(r6)
            java.lang.String r4 = r4.getMessage()
            java.lang.String r4 = trim(r4)
            r5.append(r4)
            java.lang.String r4 = "]. Trying alternative implementations..."
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            logDiagnostic(r4)
        L_0x0192:
            if (r1 != 0) goto L_0x01e4
            if (r2 == 0) goto L_0x01d9
            boolean r4 = isDiagnosticsEnabled()
            if (r4 == 0) goto L_0x01a1
            java.lang.String r4 = "[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use..."
            logDiagnostic(r4)
        L_0x01a1:
            java.lang.String r4 = "org.apache.commons.logging.LogFactory"
            java.lang.String r4 = r2.getProperty(r4)
            if (r4 == 0) goto L_0x01cd
            boolean r1 = isDiagnosticsEnabled()
            if (r1 == 0) goto L_0x01c8
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r5 = "[LOOKUP] Properties file specifies LogFactory subclass '"
            r1.append(r5)
            r1.append(r4)
            java.lang.String r5 = "'"
            r1.append(r5)
            java.lang.String r1 = r1.toString()
            logDiagnostic(r1)
        L_0x01c8:
            org.apache.commons.logging.LogFactory r1 = newFactory(r4, r3, r0)
            goto L_0x01e4
        L_0x01cd:
            boolean r3 = isDiagnosticsEnabled()
            if (r3 == 0) goto L_0x01e4
            java.lang.String r3 = "[LOOKUP] Properties file has no entry specifying LogFactory subclass."
            logDiagnostic(r3)
            goto L_0x01e4
        L_0x01d9:
            boolean r3 = isDiagnosticsEnabled()
            if (r3 == 0) goto L_0x01e4
            java.lang.String r3 = "[LOOKUP] No properties file available to determine LogFactory subclass from.."
            logDiagnostic(r3)
        L_0x01e4:
            if (r1 != 0) goto L_0x01f9
            boolean r1 = isDiagnosticsEnabled()
            if (r1 == 0) goto L_0x01f1
            java.lang.String r1 = "[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader)."
            logDiagnostic(r1)
        L_0x01f1:
            java.lang.String r1 = "org.apache.commons.logging.impl.LogFactoryImpl"
            java.lang.ClassLoader r3 = thisClassLoader
            org.apache.commons.logging.LogFactory r1 = newFactory(r1, r3, r0)
        L_0x01f9:
            if (r1 == 0) goto L_0x0218
            cacheFactory(r0, r1)
            if (r2 == 0) goto L_0x0218
            java.util.Enumeration r0 = r2.propertyNames()
        L_0x0204:
            boolean r3 = r0.hasMoreElements()
            if (r3 == 0) goto L_0x0218
            java.lang.Object r3 = r0.nextElement()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = r2.getProperty(r3)
            r1.setAttribute(r3, r4)
            goto L_0x0204
        L_0x0218:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.LogFactory.getFactory():org.apache.commons.logging.LogFactory");
    }

    public static Log getLog(Class cls) throws LogConfigurationException {
        return getFactory().getInstance(cls);
    }

    public static Log getLog(String str) throws LogConfigurationException {
        return getFactory().getInstance(str);
    }

    public static void release(ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Releasing factory for classloader ");
            stringBuffer.append(objectId(classLoader));
            logDiagnostic(stringBuffer.toString());
        }
        Hashtable hashtable = factories;
        synchronized (hashtable) {
            if (classLoader != null) {
                LogFactory logFactory = (LogFactory) hashtable.get(classLoader);
                if (logFactory != null) {
                    logFactory.release();
                    hashtable.remove(classLoader);
                }
            } else if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    public static void releaseAll() {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Releasing factory for all classloaders.");
        }
        Hashtable hashtable = factories;
        synchronized (hashtable) {
            Enumeration elements = hashtable.elements();
            while (elements.hasMoreElements()) {
                ((LogFactory) elements.nextElement()).release();
            }
            hashtable.clear();
            if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    protected static ClassLoader getClassLoader(Class cls) {
        try {
            return cls.getClassLoader();
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Unable to get classloader for class '");
                stringBuffer.append(cls);
                stringBuffer.append("' due to security restrictions - ");
                stringBuffer.append(e.getMessage());
                logDiagnostic(stringBuffer.toString());
            }
            throw e;
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return directGetContextClassLoader();
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return LogFactory.directGetContextClassLoader();
            }
        });
    }

    protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
        try {
            return Thread.currentThread().getContextClassLoader();
        } catch (SecurityException unused) {
            return null;
        }
    }

    private static LogFactory getCachedFactory(ClassLoader classLoader) {
        if (classLoader == null) {
            return nullClassLoaderFactory;
        }
        return (LogFactory) factories.get(classLoader);
    }

    private static void cacheFactory(ClassLoader classLoader, LogFactory logFactory) {
        if (logFactory == null) {
            return;
        }
        if (classLoader == null) {
            nullClassLoaderFactory = logFactory;
        } else {
            factories.put(classLoader, logFactory);
        }
    }

    protected static LogFactory newFactory(final String str, final ClassLoader classLoader, ClassLoader classLoader2) throws LogConfigurationException {
        Object doPrivileged = AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return LogFactory.createFactory(str, classLoader);
            }
        });
        if (doPrivileged instanceof LogConfigurationException) {
            LogConfigurationException logConfigurationException = (LogConfigurationException) doPrivileged;
            if (isDiagnosticsEnabled()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("An error occurred while loading the factory class:");
                stringBuffer.append(logConfigurationException.getMessage());
                logDiagnostic(stringBuffer.toString());
            }
            throw logConfigurationException;
        }
        if (isDiagnosticsEnabled()) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Created object ");
            stringBuffer2.append(objectId(doPrivileged));
            stringBuffer2.append(" to manage classloader ");
            stringBuffer2.append(objectId(classLoader2));
            logDiagnostic(stringBuffer2.toString());
        }
        return (LogFactory) doPrivileged;
    }

    protected static LogFactory newFactory(String str, ClassLoader classLoader) {
        return newFactory(str, classLoader, null);
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x00a1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.lang.Object createFactory(java.lang.String r4, java.lang.ClassLoader r5) {
        /*
            r0 = 0
            if (r5 == 0) goto L_0x0184
            java.lang.Class r0 = r5.loadClass(r4)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.Class r1 = class$org$apache$commons$logging$LogFactory     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            if (r1 != 0) goto L_0x0014
            java.lang.String r1 = "org.apache.commons.logging.LogFactory"
            java.lang.Class r1 = class$(r1)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            class$org$apache$commons$logging$LogFactory = r1     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            goto L_0x0016
        L_0x0014:
            java.lang.Class r1 = class$org$apache$commons$logging$LogFactory     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
        L_0x0016:
            boolean r1 = r1.isAssignableFrom(r0)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            if (r1 == 0) goto L_0x0047
            boolean r1 = isDiagnosticsEnabled()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            if (r1 == 0) goto L_0x009a
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            r1.<init>()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = "Loaded class "
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = r0.getName()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = " from classloader "
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = objectId(r5)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r1 = r1.toString()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            logDiagnostic(r1)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            goto L_0x009a
        L_0x0047:
            boolean r1 = isDiagnosticsEnabled()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            if (r1 == 0) goto L_0x009a
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            r1.<init>()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = "Factory class "
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = r0.getName()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = " loaded from classloader "
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.ClassLoader r2 = r0.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = objectId(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = " does not extend '"
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.Class r2 = class$org$apache$commons$logging$LogFactory     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            if (r2 != 0) goto L_0x0080
            java.lang.String r2 = "org.apache.commons.logging.LogFactory"
            java.lang.Class r2 = class$(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            class$org$apache$commons$logging$LogFactory = r2     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            goto L_0x0082
        L_0x0080:
            java.lang.Class r2 = class$org$apache$commons$logging$LogFactory     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
        L_0x0082:
            java.lang.String r2 = r2.getName()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r2 = "' as loaded by this classloader."
            r1.append(r2)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r1 = r1.toString()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            logDiagnostic(r1)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            java.lang.String r1 = "[BAD CL TREE] "
            logHierarchy(r1, r5)     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
        L_0x009a:
            java.lang.Object r1 = r0.newInstance()     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            org.apache.commons.logging.LogFactory r1 = (org.apache.commons.logging.LogFactory) r1     // Catch:{ ClassNotFoundException -> 0x0158, NoClassDefFoundError -> 0x0122, ClassCastException -> 0x00a1 }
            return r1
        L_0x00a1:
            java.lang.ClassLoader r1 = thisClassLoader     // Catch:{ Exception -> 0x01b2 }
            if (r5 != r1) goto L_0x0184
            boolean r5 = implementsLogFactory(r0)     // Catch:{ Exception -> 0x01b2 }
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x01b2 }
            r1.<init>()     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r2 = "The application has specified that a custom LogFactory implementation "
            r1.append(r2)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r2 = "should be used but Class '"
            r1.append(r2)     // Catch:{ Exception -> 0x01b2 }
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "' cannot be converted to '"
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.Class r4 = class$org$apache$commons$logging$LogFactory     // Catch:{ Exception -> 0x01b2 }
            if (r4 != 0) goto L_0x00cd
            java.lang.String r4 = "org.apache.commons.logging.LogFactory"
            java.lang.Class r4 = class$(r4)     // Catch:{ Exception -> 0x01b2 }
            class$org$apache$commons$logging$LogFactory = r4     // Catch:{ Exception -> 0x01b2 }
            goto L_0x00cf
        L_0x00cd:
            java.lang.Class r4 = class$org$apache$commons$logging$LogFactory     // Catch:{ Exception -> 0x01b2 }
        L_0x00cf:
            java.lang.String r4 = r4.getName()     // Catch:{ Exception -> 0x01b2 }
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "'. "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            if (r5 == 0) goto L_0x0101
            java.lang.String r4 = "The conflict is caused by the presence of multiple LogFactory classes "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "in incompatible classloaders. "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "Background can be found in http://commons.apache.org/logging/tech.html. "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "If you have not explicitly specified a custom LogFactory then it is likely "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "that the container has set one without your knowledge. "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "In this case, consider using the commons-logging-adapters.jar file or "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "specifying the standard LogFactory from the command line. "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            goto L_0x0106
        L_0x0101:
            java.lang.String r4 = "Please check the custom implementation. "
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
        L_0x0106:
            java.lang.String r4 = "Help can be found @http://commons.apache.org/logging/troubleshooting.html."
            r1.append(r4)     // Catch:{ Exception -> 0x01b2 }
            boolean r4 = isDiagnosticsEnabled()     // Catch:{ Exception -> 0x01b2 }
            if (r4 == 0) goto L_0x0118
            java.lang.String r4 = r1.toString()     // Catch:{ Exception -> 0x01b2 }
            logDiagnostic(r4)     // Catch:{ Exception -> 0x01b2 }
        L_0x0118:
            java.lang.ClassCastException r4 = new java.lang.ClassCastException     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r5 = r1.toString()     // Catch:{ Exception -> 0x01b2 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x01b2 }
            throw r4     // Catch:{ Exception -> 0x01b2 }
        L_0x0122:
            r1 = move-exception
            java.lang.ClassLoader r2 = thisClassLoader     // Catch:{ Exception -> 0x01b2 }
            if (r5 != r2) goto L_0x0184
            boolean r2 = isDiagnosticsEnabled()     // Catch:{ Exception -> 0x01b2 }
            if (r2 == 0) goto L_0x0157
            java.lang.StringBuffer r2 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x01b2 }
            r2.<init>()     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r3 = "Class '"
            r2.append(r3)     // Catch:{ Exception -> 0x01b2 }
            r2.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "' cannot be loaded"
            r2.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = " via classloader "
            r2.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = objectId(r5)     // Catch:{ Exception -> 0x01b2 }
            r2.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = " - it depends on some other class that cannot be found."
            r2.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = r2.toString()     // Catch:{ Exception -> 0x01b2 }
            logDiagnostic(r4)     // Catch:{ Exception -> 0x01b2 }
        L_0x0157:
            throw r1     // Catch:{ Exception -> 0x01b2 }
        L_0x0158:
            r1 = move-exception
            java.lang.ClassLoader r2 = thisClassLoader     // Catch:{ Exception -> 0x01b2 }
            if (r5 != r2) goto L_0x0184
            boolean r2 = isDiagnosticsEnabled()     // Catch:{ Exception -> 0x01b2 }
            if (r2 == 0) goto L_0x0183
            java.lang.StringBuffer r2 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x01b2 }
            r2.<init>()     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r3 = "Unable to locate any class called '"
            r2.append(r3)     // Catch:{ Exception -> 0x01b2 }
            r2.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = "' via classloader "
            r2.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = objectId(r5)     // Catch:{ Exception -> 0x01b2 }
            r2.append(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r4 = r2.toString()     // Catch:{ Exception -> 0x01b2 }
            logDiagnostic(r4)     // Catch:{ Exception -> 0x01b2 }
        L_0x0183:
            throw r1     // Catch:{ Exception -> 0x01b2 }
        L_0x0184:
            boolean r1 = isDiagnosticsEnabled()     // Catch:{ Exception -> 0x01b2 }
            if (r1 == 0) goto L_0x01a7
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x01b2 }
            r1.<init>()     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r2 = "Unable to load factory class via classloader "
            r1.append(r2)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r5 = objectId(r5)     // Catch:{ Exception -> 0x01b2 }
            r1.append(r5)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r5 = " - trying the classloader associated with this LogFactory."
            r1.append(r5)     // Catch:{ Exception -> 0x01b2 }
            java.lang.String r5 = r1.toString()     // Catch:{ Exception -> 0x01b2 }
            logDiagnostic(r5)     // Catch:{ Exception -> 0x01b2 }
        L_0x01a7:
            java.lang.Class r0 = java.lang.Class.forName(r4)     // Catch:{ Exception -> 0x01b2 }
            java.lang.Object r4 = r0.newInstance()     // Catch:{ Exception -> 0x01b2 }
            org.apache.commons.logging.LogFactory r4 = (org.apache.commons.logging.LogFactory) r4     // Catch:{ Exception -> 0x01b2 }
            return r4
        L_0x01b2:
            r4 = move-exception
            boolean r5 = isDiagnosticsEnabled()
            if (r5 == 0) goto L_0x01be
            java.lang.String r5 = "Unable to create LogFactory instance."
            logDiagnostic(r5)
        L_0x01be:
            if (r0 == 0) goto L_0x01da
            java.lang.Class r5 = class$org$apache$commons$logging$LogFactory
            if (r5 != 0) goto L_0x01cc
            java.lang.String r5 = "org.apache.commons.logging.LogFactory"
            java.lang.Class r5 = class$(r5)
            class$org$apache$commons$logging$LogFactory = r5
        L_0x01cc:
            boolean r5 = r5.isAssignableFrom(r0)
            if (r5 != 0) goto L_0x01da
            org.apache.commons.logging.LogConfigurationException r5 = new org.apache.commons.logging.LogConfigurationException
            java.lang.String r0 = "The chosen LogFactory implementation does not extend LogFactory. Please check your configuration."
            r5.<init>(r0, r4)
            return r5
        L_0x01da:
            org.apache.commons.logging.LogConfigurationException r5 = new org.apache.commons.logging.LogConfigurationException
            r5.<init>(r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.LogFactory.createFactory(java.lang.String, java.lang.ClassLoader):java.lang.Object");
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static boolean implementsLogFactory(Class cls) {
        boolean z = false;
        if (cls != null) {
            try {
                ClassLoader classLoader = cls.getClassLoader();
                if (classLoader == null) {
                    logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
                } else {
                    logHierarchy("[CUSTOM LOG FACTORY] ", classLoader);
                    z = Class.forName(FACTORY_PROPERTY, false, classLoader).isAssignableFrom(cls);
                    if (z) {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("[CUSTOM LOG FACTORY] ");
                        stringBuffer.append(cls.getName());
                        stringBuffer.append(" implements LogFactory but was loaded by an incompatible classloader.");
                        logDiagnostic(stringBuffer.toString());
                    } else {
                        StringBuffer stringBuffer2 = new StringBuffer();
                        stringBuffer2.append("[CUSTOM LOG FACTORY] ");
                        stringBuffer2.append(cls.getName());
                        stringBuffer2.append(" does not implement LogFactory.");
                        logDiagnostic(stringBuffer2.toString());
                    }
                }
            } catch (SecurityException e) {
                StringBuffer stringBuffer3 = new StringBuffer();
                stringBuffer3.append("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ");
                stringBuffer3.append(e.getMessage());
                logDiagnostic(stringBuffer3.toString());
            } catch (LinkageError e2) {
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ");
                stringBuffer4.append(e2.getMessage());
                logDiagnostic(stringBuffer4.toString());
            } catch (ClassNotFoundException unused) {
                logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
            }
        }
        return z;
    }

    private static InputStream getResourceAsStream(final ClassLoader classLoader, final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                ClassLoader classLoader = classLoader;
                if (classLoader != null) {
                    return classLoader.getResourceAsStream(str);
                }
                return ClassLoader.getSystemResourceAsStream(str);
            }
        });
    }

    private static Enumeration getResources(final ClassLoader classLoader, final String str) {
        return (Enumeration) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    if (classLoader != null) {
                        return classLoader.getResources(str);
                    }
                    return ClassLoader.getSystemResources(str);
                } catch (IOException e) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("Exception while trying to find configuration file ");
                        stringBuffer.append(str);
                        stringBuffer.append(":");
                        stringBuffer.append(e.getMessage());
                        LogFactory.logDiagnostic(stringBuffer.toString());
                    }
                    return null;
                } catch (NoSuchMethodError unused) {
                    return null;
                }
            }
        });
    }

    private static Properties getProperties(final URL url) {
        return (Properties) AccessController.doPrivileged(new PrivilegedAction() {
            /* JADX WARNING: Can't wrap try/catch for region: R(4:19|20|(1:22)|(2:24|25)) */
            /* JADX WARNING: Code restructure failed: missing block: B:21:0x004a, code lost:
                if (org.apache.commons.logging.LogFactory.isDiagnosticsEnabled() != false) goto L_0x004c;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:22:0x004c, code lost:
                r2 = new java.lang.StringBuffer();
                r2.append("Unable to read URL ");
                r2.append(r1);
                org.apache.commons.logging.LogFactory.access$000(r2.toString());
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:0x0062, code lost:
                if (r1 != null) goto L_0x0064;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
                r1.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:28:0x006c, code lost:
                if (org.apache.commons.logging.LogFactory.isDiagnosticsEnabled() != false) goto L_0x006e;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:29:0x006e, code lost:
                r1 = new java.lang.StringBuffer();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:31:0x0075, code lost:
                r0 = th;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
                r1.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:37:0x0080, code lost:
                if (org.apache.commons.logging.LogFactory.isDiagnosticsEnabled() != false) goto L_0x0082;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:38:0x0082, code lost:
                r1 = new java.lang.StringBuffer();
                r1.append("Unable to close stream for URL ");
                r1.append(r1);
                org.apache.commons.logging.LogFactory.access$000(r1.toString());
             */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0046 */
            /* JADX WARNING: Removed duplicated region for block: B:33:0x0078 A[SYNTHETIC, Splitter:B:33:0x0078] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.lang.Object run() {
                /*
                    r5 = this;
                    r0 = 0
                    java.net.URL r1 = r1     // Catch:{ IOException -> 0x0045, all -> 0x0040 }
                    java.net.URLConnection r1 = r1.openConnection()     // Catch:{ IOException -> 0x0045, all -> 0x0040 }
                    r2 = 0
                    r1.setUseCaches(r2)     // Catch:{ IOException -> 0x0045, all -> 0x0040 }
                    java.io.InputStream r1 = r1.getInputStream()     // Catch:{ IOException -> 0x0045, all -> 0x0040 }
                    if (r1 == 0) goto L_0x001d
                    java.util.Properties r2 = new java.util.Properties     // Catch:{ IOException -> 0x0046 }
                    r2.<init>()     // Catch:{ IOException -> 0x0046 }
                    r2.load(r1)     // Catch:{ IOException -> 0x0046 }
                    r1.close()     // Catch:{ IOException -> 0x0046 }
                    return r2
                L_0x001d:
                    if (r1 == 0) goto L_0x0074
                    r1.close()     // Catch:{ IOException -> 0x0023 }
                    goto L_0x0074
                L_0x0023:
                    boolean r1 = org.apache.commons.logging.LogFactory.isDiagnosticsEnabled()
                    if (r1 == 0) goto L_0x0074
                    java.lang.StringBuffer r1 = new java.lang.StringBuffer
                    r1.<init>()
                L_0x002e:
                    java.lang.String r2 = "Unable to close stream for URL "
                    r1.append(r2)
                    java.net.URL r2 = r1
                    r1.append(r2)
                    java.lang.String r1 = r1.toString()
                    org.apache.commons.logging.LogFactory.logDiagnostic(r1)
                    goto L_0x0074
                L_0x0040:
                    r1 = move-exception
                    r4 = r1
                    r1 = r0
                    r0 = r4
                    goto L_0x0076
                L_0x0045:
                    r1 = r0
                L_0x0046:
                    boolean r2 = org.apache.commons.logging.LogFactory.isDiagnosticsEnabled()     // Catch:{ all -> 0x0075 }
                    if (r2 == 0) goto L_0x0062
                    java.lang.StringBuffer r2 = new java.lang.StringBuffer     // Catch:{ all -> 0x0075 }
                    r2.<init>()     // Catch:{ all -> 0x0075 }
                    java.lang.String r3 = "Unable to read URL "
                    r2.append(r3)     // Catch:{ all -> 0x0075 }
                    java.net.URL r3 = r1     // Catch:{ all -> 0x0075 }
                    r2.append(r3)     // Catch:{ all -> 0x0075 }
                    java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0075 }
                    org.apache.commons.logging.LogFactory.logDiagnostic(r2)     // Catch:{ all -> 0x0075 }
                L_0x0062:
                    if (r1 == 0) goto L_0x0074
                    r1.close()     // Catch:{ IOException -> 0x0068 }
                    goto L_0x0074
                L_0x0068:
                    boolean r1 = org.apache.commons.logging.LogFactory.isDiagnosticsEnabled()
                    if (r1 == 0) goto L_0x0074
                    java.lang.StringBuffer r1 = new java.lang.StringBuffer
                    r1.<init>()
                    goto L_0x002e
                L_0x0074:
                    return r0
                L_0x0075:
                    r0 = move-exception
                L_0x0076:
                    if (r1 == 0) goto L_0x0098
                    r1.close()     // Catch:{ IOException -> 0x007c }
                    goto L_0x0098
                L_0x007c:
                    boolean r1 = org.apache.commons.logging.LogFactory.isDiagnosticsEnabled()
                    if (r1 == 0) goto L_0x0098
                    java.lang.StringBuffer r1 = new java.lang.StringBuffer
                    r1.<init>()
                    java.lang.String r2 = "Unable to close stream for URL "
                    r1.append(r2)
                    java.net.URL r2 = r1
                    r1.append(r2)
                    java.lang.String r1 = r1.toString()
                    org.apache.commons.logging.LogFactory.logDiagnostic(r1)
                L_0x0098:
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.LogFactory.C43625.run():java.lang.Object");
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x00f7  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0102  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final java.util.Properties getConfigurationFile(java.lang.ClassLoader r12, java.lang.String r13) {
        /*
            r0 = 0
            java.util.Enumeration r12 = getResources(r12, r13)     // Catch:{ SecurityException -> 0x00f0 }
            if (r12 != 0) goto L_0x0008
            return r0
        L_0x0008:
            r1 = 0
            r3 = r0
            r4 = r1
        L_0x000c:
            boolean r6 = r12.hasMoreElements()     // Catch:{ SecurityException -> 0x00ee }
            if (r6 == 0) goto L_0x00fc
            java.lang.Object r6 = r12.nextElement()     // Catch:{ SecurityException -> 0x00ee }
            java.net.URL r6 = (java.net.URL) r6     // Catch:{ SecurityException -> 0x00ee }
            java.util.Properties r7 = getProperties(r6)     // Catch:{ SecurityException -> 0x00ee }
            if (r7 == 0) goto L_0x000c
            if (r0 != 0) goto L_0x005d
            java.lang.String r0 = "priority"
            java.lang.String r0 = r7.getProperty(r0)     // Catch:{ SecurityException -> 0x0059 }
            if (r0 == 0) goto L_0x002d
            double r3 = java.lang.Double.parseDouble(r0)     // Catch:{ SecurityException -> 0x0059 }
            goto L_0x002e
        L_0x002d:
            r3 = r1
        L_0x002e:
            boolean r0 = isDiagnosticsEnabled()     // Catch:{ SecurityException -> 0x0059 }
            if (r0 == 0) goto L_0x0055
            java.lang.StringBuffer r0 = new java.lang.StringBuffer     // Catch:{ SecurityException -> 0x0059 }
            r0.<init>()     // Catch:{ SecurityException -> 0x0059 }
            java.lang.String r5 = "[LOOKUP] Properties file found at '"
            r0.append(r5)     // Catch:{ SecurityException -> 0x0059 }
            r0.append(r6)     // Catch:{ SecurityException -> 0x0059 }
            java.lang.String r5 = "'"
            r0.append(r5)     // Catch:{ SecurityException -> 0x0059 }
            java.lang.String r5 = " with priority "
            r0.append(r5)     // Catch:{ SecurityException -> 0x0059 }
            r0.append(r3)     // Catch:{ SecurityException -> 0x0059 }
            java.lang.String r0 = r0.toString()     // Catch:{ SecurityException -> 0x0059 }
            logDiagnostic(r0)     // Catch:{ SecurityException -> 0x0059 }
        L_0x0055:
            r4 = r3
            r3 = r6
            r0 = r7
            goto L_0x000c
        L_0x0059:
            r3 = r6
            r0 = r7
            goto L_0x00f1
        L_0x005d:
            java.lang.String r8 = "priority"
            java.lang.String r8 = r7.getProperty(r8)     // Catch:{ SecurityException -> 0x00ee }
            if (r8 == 0) goto L_0x006a
            double r8 = java.lang.Double.parseDouble(r8)     // Catch:{ SecurityException -> 0x00ee }
            goto L_0x006b
        L_0x006a:
            r8 = r1
        L_0x006b:
            int r10 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r10 <= 0) goto L_0x00b0
            boolean r10 = isDiagnosticsEnabled()     // Catch:{ SecurityException -> 0x00ee }
            if (r10 == 0) goto L_0x00ab
            java.lang.StringBuffer r10 = new java.lang.StringBuffer     // Catch:{ SecurityException -> 0x00ee }
            r10.<init>()     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r11 = "[LOOKUP] Properties file at '"
            r10.append(r11)     // Catch:{ SecurityException -> 0x00ee }
            r10.append(r6)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r11 = "'"
            r10.append(r11)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r11 = " with priority "
            r10.append(r11)     // Catch:{ SecurityException -> 0x00ee }
            r10.append(r8)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r11 = " overrides file at '"
            r10.append(r11)     // Catch:{ SecurityException -> 0x00ee }
            r10.append(r3)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r11 = "'"
            r10.append(r11)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r11 = " with priority "
            r10.append(r11)     // Catch:{ SecurityException -> 0x00ee }
            r10.append(r4)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r4 = r10.toString()     // Catch:{ SecurityException -> 0x00ee }
            logDiagnostic(r4)     // Catch:{ SecurityException -> 0x00ee }
        L_0x00ab:
            r3 = r6
            r0 = r7
            r4 = r8
            goto L_0x000c
        L_0x00b0:
            boolean r7 = isDiagnosticsEnabled()     // Catch:{ SecurityException -> 0x00ee }
            if (r7 == 0) goto L_0x000c
            java.lang.StringBuffer r7 = new java.lang.StringBuffer     // Catch:{ SecurityException -> 0x00ee }
            r7.<init>()     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r10 = "[LOOKUP] Properties file at '"
            r7.append(r10)     // Catch:{ SecurityException -> 0x00ee }
            r7.append(r6)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r6 = "'"
            r7.append(r6)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r6 = " with priority "
            r7.append(r6)     // Catch:{ SecurityException -> 0x00ee }
            r7.append(r8)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r6 = " does not override file at '"
            r7.append(r6)     // Catch:{ SecurityException -> 0x00ee }
            r7.append(r3)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r6 = "'"
            r7.append(r6)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r6 = " with priority "
            r7.append(r6)     // Catch:{ SecurityException -> 0x00ee }
            r7.append(r4)     // Catch:{ SecurityException -> 0x00ee }
            java.lang.String r6 = r7.toString()     // Catch:{ SecurityException -> 0x00ee }
            logDiagnostic(r6)     // Catch:{ SecurityException -> 0x00ee }
            goto L_0x000c
        L_0x00ee:
            goto L_0x00f1
        L_0x00f0:
            r3 = r0
        L_0x00f1:
            boolean r12 = isDiagnosticsEnabled()
            if (r12 == 0) goto L_0x00fc
            java.lang.String r12 = "SecurityException thrown while trying to find/read config files."
            logDiagnostic(r12)
        L_0x00fc:
            boolean r12 = isDiagnosticsEnabled()
            if (r12 == 0) goto L_0x013f
            if (r0 != 0) goto L_0x011e
            java.lang.StringBuffer r12 = new java.lang.StringBuffer
            r12.<init>()
            java.lang.String r1 = "[LOOKUP] No properties file of name '"
            r12.append(r1)
            r12.append(r13)
            java.lang.String r13 = "' found."
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            logDiagnostic(r12)
            goto L_0x013f
        L_0x011e:
            java.lang.StringBuffer r12 = new java.lang.StringBuffer
            r12.<init>()
            java.lang.String r1 = "[LOOKUP] Properties file of name '"
            r12.append(r1)
            r12.append(r13)
            java.lang.String r13 = "' found at '"
            r12.append(r13)
            r12.append(r3)
            r13 = 34
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            logDiagnostic(r12)
        L_0x013f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.LogFactory.getConfigurationFile(java.lang.ClassLoader, java.lang.String):java.util.Properties");
    }

    private static String getSystemProperty(final String str, final String str2) throws SecurityException {
        return (String) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return System.getProperty(str, str2);
            }
        });
    }

    private static PrintStream initDiagnostics() {
        try {
            String systemProperty = getSystemProperty(DIAGNOSTICS_DEST_PROPERTY, null);
            if (systemProperty == null) {
                return null;
            }
            if (systemProperty.equals("STDOUT")) {
                return System.out;
            }
            if (systemProperty.equals("STDERR")) {
                return System.err;
            }
            try {
                return new PrintStream(new FileOutputStream(systemProperty, true));
            } catch (IOException unused) {
                return null;
            }
        } catch (SecurityException unused2) {
            return null;
        }
    }

    protected static boolean isDiagnosticsEnabled() {
        return diagnosticsStream != null;
    }

    /* access modifiers changed from: private */
    public static final void logDiagnostic(String str) {
        PrintStream printStream = diagnosticsStream;
        if (printStream != null) {
            printStream.print(diagnosticPrefix);
            diagnosticsStream.println(str);
            diagnosticsStream.flush();
        }
    }

    protected static final void logRawDiagnostic(String str) {
        PrintStream printStream = diagnosticsStream;
        if (printStream != null) {
            printStream.println(str);
            diagnosticsStream.flush();
        }
    }

    private static void logClassLoaderEnvironment(Class cls) {
        if (isDiagnosticsEnabled()) {
            try {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("[ENV] Extension directories (java.ext.dir): ");
                stringBuffer.append(System.getProperty("java.ext.dir"));
                logDiagnostic(stringBuffer.toString());
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("[ENV] Application classpath (java.class.path): ");
                stringBuffer2.append(System.getProperty("java.class.path"));
                logDiagnostic(stringBuffer2.toString());
            } catch (SecurityException unused) {
                logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
            }
            String name = cls.getName();
            try {
                ClassLoader classLoader = getClassLoader(cls);
                StringBuffer stringBuffer3 = new StringBuffer();
                stringBuffer3.append("[ENV] Class ");
                stringBuffer3.append(name);
                stringBuffer3.append(" was loaded via classloader ");
                stringBuffer3.append(objectId(classLoader));
                logDiagnostic(stringBuffer3.toString());
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append("[ENV] Ancestry of classloader which loaded ");
                stringBuffer4.append(name);
                stringBuffer4.append(" is ");
                logHierarchy(stringBuffer4.toString(), classLoader);
            } catch (SecurityException unused2) {
                StringBuffer stringBuffer5 = new StringBuffer();
                stringBuffer5.append("[ENV] Security forbids determining the classloader for ");
                stringBuffer5.append(name);
                logDiagnostic(stringBuffer5.toString());
            }
        }
    }

    private static void logHierarchy(String str, ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            if (classLoader != null) {
                String obj = classLoader.toString();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                stringBuffer.append(objectId(classLoader));
                stringBuffer.append(" == '");
                stringBuffer.append(obj);
                stringBuffer.append("'");
                logDiagnostic(stringBuffer.toString());
            }
            try {
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                if (classLoader != null) {
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append(str);
                    stringBuffer2.append("ClassLoader tree:");
                    StringBuffer stringBuffer3 = new StringBuffer(stringBuffer2.toString());
                    do {
                        stringBuffer3.append(objectId(classLoader));
                        if (classLoader == systemClassLoader) {
                            stringBuffer3.append(" (SYSTEM) ");
                        }
                        try {
                            classLoader = classLoader.getParent();
                            stringBuffer3.append(" --> ");
                        } catch (SecurityException unused) {
                            stringBuffer3.append(" --> SECRET");
                        }
                    } while (classLoader != null);
                    stringBuffer3.append("BOOT");
                    logDiagnostic(stringBuffer3.toString());
                }
            } catch (SecurityException unused2) {
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append(str);
                stringBuffer4.append("Security forbids determining the system classloader.");
                logDiagnostic(stringBuffer4.toString());
            }
        }
    }

    public static String objectId(Object obj) {
        if (obj == null) {
            return "null";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(obj.getClass().getName());
        stringBuffer.append("@");
        stringBuffer.append(System.identityHashCode(obj));
        return stringBuffer.toString();
    }

    static {
        String str;
        Class cls = class$org$apache$commons$logging$LogFactory;
        if (cls == null) {
            cls = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = cls;
        }
        thisClassLoader = getClassLoader(cls);
        try {
            str = thisClassLoader == null ? "BOOTLOADER" : objectId(thisClassLoader);
        } catch (SecurityException unused) {
            str = "UNKNOWN";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[LogFactory from ");
        stringBuffer.append(str);
        stringBuffer.append("] ");
        diagnosticPrefix = stringBuffer.toString();
        Class cls2 = class$org$apache$commons$logging$LogFactory;
        if (cls2 == null) {
            cls2 = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = cls2;
        }
        logClassLoaderEnvironment(cls2);
        if (isDiagnosticsEnabled()) {
            logDiagnostic("BOOTSTRAP COMPLETED");
        }
    }
}
