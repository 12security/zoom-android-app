package org.apache.http.util;

import com.zipow.videobox.util.TextCommandHelper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class VersionInfo {
    public static final String PROPERTY_MODULE = "info.module";
    public static final String PROPERTY_RELEASE = "info.release";
    public static final String PROPERTY_TIMESTAMP = "info.timestamp";
    public static final String UNAVAILABLE = "UNAVAILABLE";
    public static final String VERSION_PROPERTY_FILE = "version.properties";
    private final String infoClassloader;
    private final String infoModule;
    private final String infoPackage;
    private final String infoRelease;
    private final String infoTimestamp;

    protected VersionInfo(String str, String str2, String str3, String str4, String str5) {
        Args.notNull(str, "Package identifier");
        this.infoPackage = str;
        if (str2 == null) {
            str2 = UNAVAILABLE;
        }
        this.infoModule = str2;
        if (str3 == null) {
            str3 = UNAVAILABLE;
        }
        this.infoRelease = str3;
        if (str4 == null) {
            str4 = UNAVAILABLE;
        }
        this.infoTimestamp = str4;
        if (str5 == null) {
            str5 = UNAVAILABLE;
        }
        this.infoClassloader = str5;
    }

    public final String getPackage() {
        return this.infoPackage;
    }

    public final String getModule() {
        return this.infoModule;
    }

    public final String getRelease() {
        return this.infoRelease;
    }

    public final String getTimestamp() {
        return this.infoTimestamp;
    }

    public final String getClassloader() {
        return this.infoClassloader;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.infoPackage.length() + 20 + this.infoModule.length() + this.infoRelease.length() + this.infoTimestamp.length() + this.infoClassloader.length());
        sb.append("VersionInfo(");
        sb.append(this.infoPackage);
        sb.append(':');
        sb.append(this.infoModule);
        if (!UNAVAILABLE.equals(this.infoRelease)) {
            sb.append(':');
            sb.append(this.infoRelease);
        }
        if (!UNAVAILABLE.equals(this.infoTimestamp)) {
            sb.append(':');
            sb.append(this.infoTimestamp);
        }
        sb.append(')');
        if (!UNAVAILABLE.equals(this.infoClassloader)) {
            sb.append(TextCommandHelper.REPLY_AT_CHAR);
            sb.append(this.infoClassloader);
        }
        return sb.toString();
    }

    public static VersionInfo[] loadVersionInfo(String[] strArr, ClassLoader classLoader) {
        Args.notNull(strArr, "Package identifier array");
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String loadVersionInfo : strArr) {
            VersionInfo loadVersionInfo2 = loadVersionInfo(loadVersionInfo, classLoader);
            if (loadVersionInfo2 != null) {
                arrayList.add(loadVersionInfo2);
            }
        }
        return (VersionInfo[]) arrayList.toArray(new VersionInfo[arrayList.size()]);
    }

    public static VersionInfo loadVersionInfo(String str, ClassLoader classLoader) {
        Properties properties;
        InputStream resourceAsStream;
        Args.notNull(str, "Package identifier");
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(str.replace('.', '/'));
            sb.append("/");
            sb.append(VERSION_PROPERTY_FILE);
            resourceAsStream = classLoader.getResourceAsStream(sb.toString());
            if (resourceAsStream != null) {
                properties = new Properties();
                properties.load(resourceAsStream);
                try {
                    resourceAsStream.close();
                } catch (IOException unused) {
                }
            } else {
                properties = null;
            }
        } catch (IOException unused2) {
            properties = null;
        } catch (Throwable th) {
            resourceAsStream.close();
            throw th;
        }
        if (properties != null) {
            return fromMap(str, properties, classLoader);
        }
        return null;
    }

    protected static VersionInfo fromMap(String str, Map<?, ?> map, ClassLoader classLoader) {
        String str2;
        String str3;
        String str4;
        Args.notNull(str, "Package identifier");
        if (map != null) {
            String str5 = (String) map.get(PROPERTY_MODULE);
            if (str5 != null && str5.length() < 1) {
                str5 = null;
            }
            String str6 = (String) map.get(PROPERTY_RELEASE);
            if (str6 != null && (str6.length() < 1 || str6.equals("${pom.version}"))) {
                str6 = null;
            }
            String str7 = (String) map.get(PROPERTY_TIMESTAMP);
            if (str7 == null || (str7.length() >= 1 && !str7.equals("${mvn.timestamp}"))) {
                str2 = str7;
                str4 = str5;
                str3 = str6;
            } else {
                str2 = null;
                str4 = str5;
                str3 = str6;
            }
        } else {
            str4 = null;
            str3 = null;
            str2 = null;
        }
        VersionInfo versionInfo = new VersionInfo(str, str4, str3, str2, classLoader != null ? classLoader.toString() : null);
        return versionInfo;
    }

    public static String getUserAgent(String str, String str2, Class<?> cls) {
        VersionInfo loadVersionInfo = loadVersionInfo(str2, cls.getClassLoader());
        return String.format("%s/%s (Java/%s)", new Object[]{str, loadVersionInfo != null ? loadVersionInfo.getRelease() : UNAVAILABLE, System.getProperty("java.version")});
    }
}
