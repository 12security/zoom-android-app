package org.apache.http.conn.ssl;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.util.Args;

@Deprecated
public abstract class AbstractVerifier implements X509HostnameVerifier {
    static final String[] BAD_COUNTRY_2LDS = {"ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org"};
    private final Log log = LogFactory.getLog(getClass());

    static {
        Arrays.sort(BAD_COUNTRY_2LDS);
    }

    public final void verify(String str, SSLSocket sSLSocket) throws IOException {
        Args.notNull(str, "Host");
        SSLSession session = sSLSocket.getSession();
        if (session == null) {
            sSLSocket.getInputStream().available();
            session = sSLSocket.getSession();
            if (session == null) {
                sSLSocket.startHandshake();
                session = sSLSocket.getSession();
            }
        }
        verify(str, (X509Certificate) session.getPeerCertificates()[0]);
    }

    public final boolean verify(String str, SSLSession sSLSession) {
        try {
            verify(str, (X509Certificate) sSLSession.getPeerCertificates()[0]);
            return true;
        } catch (SSLException e) {
            if (this.log.isDebugEnabled()) {
                this.log.debug(e.getMessage(), e);
            }
            return false;
        }
    }

    public final void verify(String str, X509Certificate x509Certificate) throws SSLException {
        List<SubjectName> subjectAltNames = DefaultHostnameVerifier.getSubjectAltNames(x509Certificate);
        ArrayList arrayList = new ArrayList();
        if (InetAddressUtils.isIPv4Address(str) || InetAddressUtils.isIPv6Address(str)) {
            for (SubjectName subjectName : subjectAltNames) {
                if (subjectName.getType() == 7) {
                    arrayList.add(subjectName.getValue());
                }
            }
        } else {
            for (SubjectName subjectName2 : subjectAltNames) {
                if (subjectName2.getType() == 2) {
                    arrayList.add(subjectName2.getValue());
                }
            }
        }
        String extractCN = DefaultHostnameVerifier.extractCN(x509Certificate.getSubjectX500Principal().getName("RFC2253"));
        String[] strArr = null;
        String[] strArr2 = extractCN != null ? new String[]{extractCN} : null;
        if (!arrayList.isEmpty()) {
            strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        verify(str, strArr2, strArr);
    }

    public final void verify(String str, String[] strArr, String[] strArr2, boolean z) throws SSLException {
        List<String> list = null;
        String str2 = (strArr == null || strArr.length <= 0) ? null : strArr[0];
        if (strArr2 != null && strArr2.length > 0) {
            list = Arrays.asList(strArr2);
        }
        String normaliseAddress = InetAddressUtils.isIPv6Address(str) ? DefaultHostnameVerifier.normaliseAddress(str.toLowerCase(Locale.ROOT)) : str;
        if (list != null) {
            for (String str3 : list) {
                if (InetAddressUtils.isIPv6Address(str3)) {
                    str3 = DefaultHostnameVerifier.normaliseAddress(str3);
                }
                if (matchIdentity(normaliseAddress, str3, z)) {
                    return;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Certificate for <");
            sb.append(str);
            sb.append("> doesn't match any ");
            sb.append("of the subject alternative names: ");
            sb.append(list);
            throw new SSLException(sb.toString());
        } else if (str2 != null) {
            if (!matchIdentity(normaliseAddress, InetAddressUtils.isIPv6Address(str2) ? DefaultHostnameVerifier.normaliseAddress(str2) : str2, z)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Certificate for <");
                sb2.append(str);
                sb2.append("> doesn't match ");
                sb2.append("common name of the certificate subject: ");
                sb2.append(str2);
                throw new SSLException(sb2.toString());
            }
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Certificate subject for <");
            sb3.append(str);
            sb3.append("> doesn't contain ");
            sb3.append("a common name and does not have alternative names");
            throw new SSLException(sb3.toString());
        }
    }

    private static boolean matchIdentity(String str, String str2, boolean z) {
        boolean z2;
        boolean z3 = false;
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase(Locale.ROOT);
        String lowerCase2 = str2.toLowerCase(Locale.ROOT);
        String[] split = lowerCase2.split("\\.");
        if (!(split.length >= 3 && split[0].endsWith("*") && (!z || validCountryWildcard(split)))) {
            return lowerCase.equals(lowerCase2);
        }
        String str3 = split[0];
        if (str3.length() > 1) {
            String substring = str3.substring(0, str3.length() - 1);
            z2 = lowerCase.startsWith(substring) && lowerCase.substring(substring.length()).endsWith(lowerCase2.substring(str3.length()));
        } else {
            z2 = lowerCase.endsWith(lowerCase2.substring(1));
        }
        if (z2 && (!z || countDots(lowerCase) == countDots(lowerCase2))) {
            z3 = true;
        }
        return z3;
    }

    private static boolean validCountryWildcard(String[] strArr) {
        boolean z = true;
        if (strArr.length != 3 || strArr[2].length() != 2) {
            return true;
        }
        if (Arrays.binarySearch(BAD_COUNTRY_2LDS, strArr[1]) >= 0) {
            z = false;
        }
        return z;
    }

    public static boolean acceptableCountryWildcard(String str) {
        return validCountryWildcard(str.split("\\."));
    }

    public static String[] getCNs(X509Certificate x509Certificate) {
        String[] strArr = null;
        try {
            String extractCN = DefaultHostnameVerifier.extractCN(x509Certificate.getSubjectX500Principal().toString());
            if (extractCN != null) {
                strArr = new String[]{extractCN};
            }
            return strArr;
        } catch (SSLException unused) {
            return null;
        }
    }

    public static String[] getDNSSubjectAlts(X509Certificate x509Certificate) {
        List<SubjectName> subjectAltNames = DefaultHostnameVerifier.getSubjectAltNames(x509Certificate);
        String[] strArr = null;
        if (subjectAltNames == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (SubjectName subjectName : subjectAltNames) {
            if (subjectName.getType() == 2) {
                arrayList.add(subjectName.getValue());
            }
        }
        if (arrayList.isEmpty()) {
            strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        return strArr;
    }

    public static int countDots(String str) {
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == '.') {
                i++;
            }
        }
        return i;
    }
}
