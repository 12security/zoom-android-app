package org.apache.http.impl.client;

import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.PasswordAuthentication;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.SAFE)
public class SystemDefaultCredentialsProvider implements CredentialsProvider {
    private static final Map<String, String> SCHEME_MAP = new ConcurrentHashMap();
    private final BasicCredentialsProvider internal = new BasicCredentialsProvider();

    static {
        SCHEME_MAP.put("Basic".toUpperCase(Locale.ROOT), "Basic");
        SCHEME_MAP.put("Digest".toUpperCase(Locale.ROOT), "Digest");
        SCHEME_MAP.put("NTLM".toUpperCase(Locale.ROOT), "NTLM");
        SCHEME_MAP.put("Negotiate".toUpperCase(Locale.ROOT), "SPNEGO");
        SCHEME_MAP.put("Kerberos".toUpperCase(Locale.ROOT), "Kerberos");
    }

    private static String translateScheme(String str) {
        if (str == null) {
            return null;
        }
        String str2 = (String) SCHEME_MAP.get(str);
        if (str2 != null) {
            str = str2;
        }
        return str;
    }

    public void setCredentials(AuthScope authScope, Credentials credentials) {
        this.internal.setCredentials(authScope, credentials);
    }

    private static PasswordAuthentication getSystemCreds(String str, AuthScope authScope, RequestorType requestorType) {
        return Authenticator.requestPasswordAuthentication(authScope.getHost(), null, authScope.getPort(), str, null, translateScheme(authScope.getScheme()), null, requestorType);
    }

    public Credentials getCredentials(AuthScope authScope) {
        Args.notNull(authScope, "Auth scope");
        Credentials credentials = this.internal.getCredentials(authScope);
        if (credentials != null) {
            return credentials;
        }
        if (authScope.getHost() != null) {
            HttpHost origin = authScope.getOrigin();
            String str = origin != null ? origin.getSchemeName() : authScope.getPort() == 443 ? "https" : HttpHost.DEFAULT_SCHEME_NAME;
            PasswordAuthentication systemCreds = getSystemCreds(str, authScope, RequestorType.SERVER);
            if (systemCreds == null) {
                systemCreds = getSystemCreds(str, authScope, RequestorType.PROXY);
            }
            if (systemCreds == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(".proxyHost");
                String property = System.getProperty(sb.toString());
                if (property != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(".proxyPort");
                    String property2 = System.getProperty(sb2.toString());
                    if (property2 != null) {
                        try {
                            if (authScope.match(new AuthScope(property, Integer.parseInt(property2))) >= 0) {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str);
                                sb3.append(".proxyUser");
                                String property3 = System.getProperty(sb3.toString());
                                if (property3 != null) {
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append(str);
                                    sb4.append(".proxyPassword");
                                    String property4 = System.getProperty(sb4.toString());
                                    systemCreds = new PasswordAuthentication(property3, property4 != null ? property4.toCharArray() : new char[0]);
                                }
                            }
                        } catch (NumberFormatException unused) {
                        }
                    }
                }
            }
            if (systemCreds != null) {
                String property5 = System.getProperty("http.auth.ntlm.domain");
                if (property5 != null) {
                    return new NTCredentials(systemCreds.getUserName(), new String(systemCreds.getPassword()), null, property5);
                }
                if ("NTLM".equalsIgnoreCase(authScope.getScheme())) {
                    return new NTCredentials(systemCreds.getUserName(), new String(systemCreds.getPassword()), null, null);
                }
                return new UsernamePasswordCredentials(systemCreds.getUserName(), new String(systemCreds.getPassword()));
            }
        }
        return null;
    }

    public void clear() {
        this.internal.clear();
    }
}
