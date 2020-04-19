package p021us.zoom.androidlib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.os.Build.VERSION;
import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* renamed from: us.zoom.androidlib.util.DnsServersDetector */
public class DnsServersDetector {
    private static final String[] FACTORY_DNS_SERVERS = {"8.8.8.8", "8.8.4.4"};
    private static final String METHOD_EXEC_PROP_DELIM = "]: [";
    private static final String TAG = "DnsServersDetector";
    private Context context;

    public DnsServersDetector(Context context2) {
        this.context = context2;
    }

    public String[] getServers() {
        String[] serversMethodSystemProperties = getServersMethodSystemProperties();
        if (serversMethodSystemProperties != null && serversMethodSystemProperties.length > 0) {
            return serversMethodSystemProperties;
        }
        String[] serversMethodConnectivityManager = getServersMethodConnectivityManager();
        if (serversMethodConnectivityManager != null && serversMethodConnectivityManager.length > 0) {
            return serversMethodConnectivityManager;
        }
        String[] serversMethodExec = getServersMethodExec();
        return (serversMethodExec == null || serversMethodExec.length <= 0) ? FACTORY_DNS_SERVERS : serversMethodExec;
    }

    private String[] getServersMethodConnectivityManager() {
        if (VERSION.SDK_INT >= 21) {
            try {
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
                if (connectivityManager != null) {
                    if (VERSION.SDK_INT >= 23) {
                        setServerList(connectivityManager, connectivityManager.getActiveNetwork(), arrayList, arrayList2);
                    } else {
                        for (Network serverList : connectivityManager.getAllNetworks()) {
                            setServerList(connectivityManager, serverList, arrayList, arrayList2);
                        }
                    }
                }
                if (arrayList.isEmpty()) {
                    arrayList.addAll(arrayList2);
                }
                if (arrayList.size() > 0) {
                    return (String[]) arrayList.toArray(new String[0]);
                }
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private void setServerList(ConnectivityManager connectivityManager, Network network, ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (VERSION.SDK_INT >= 21) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo != null && networkInfo.isConnected()) {
                LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
                List<InetAddress> dnsServers = linkProperties.getDnsServers();
                if (linkPropertiesHasDefaultRoute(linkProperties)) {
                    for (InetAddress hostAddress : dnsServers) {
                        arrayList.add(hostAddress.getHostAddress());
                    }
                    return;
                }
                for (InetAddress hostAddress2 : dnsServers) {
                    arrayList2.add(hostAddress2.getHostAddress());
                }
            }
        }
    }

    private String[] getServersMethodSystemProperties() {
        if (VERSION.SDK_INT < 26) {
            ArrayList arrayList = new ArrayList();
            try {
                Method method = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
                String[] strArr = {"net.dns1", "net.dns2", "net.dns3", "net.dns4"};
                for (int i = 0; i < strArr.length; i++) {
                    String str = (String) method.invoke(null, new Object[]{strArr[i]});
                    if (str != null && ((str.matches("^\\d+(\\.\\d+){3}$") || str.matches("^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$")) && !arrayList.contains(str))) {
                        arrayList.add(str);
                    }
                }
                if (arrayList.size() > 0) {
                    return (String[]) arrayList.toArray(new String[0]);
                }
            } catch (Exception unused) {
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x005e, code lost:
        r2 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x005f, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0063, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0064, code lost:
        r5 = r3;
        r3 = r2;
        r2 = r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x005e A[ExcHandler: all (th java.lang.Throwable), Splitter:B:6:0x0015] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String[] getServersMethodExec() {
        /*
            r6 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 0
            r2 = 16
            if (r0 < r2) goto L_0x0079
            java.lang.Runtime r0 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0078 }
            java.lang.String r2 = "getprop"
            java.lang.Process r0 = r0.exec(r2)     // Catch:{ IOException -> 0x0078 }
            java.io.InputStream r0 = r0.getInputStream()     // Catch:{ Exception -> 0x0079 }
            java.io.LineNumberReader r2 = new java.io.LineNumberReader     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
            r3.<init>(r0)     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
            java.util.Set r3 = r6.methodExecParseProps(r2)     // Catch:{ Throwable -> 0x0049, all -> 0x0046 }
            if (r3 == 0) goto L_0x003d
            int r4 = r3.size()     // Catch:{ Throwable -> 0x0049, all -> 0x0046 }
            if (r4 <= 0) goto L_0x003d
            r4 = 0
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x0049, all -> 0x0046 }
            java.lang.Object[] r3 = r3.toArray(r4)     // Catch:{ Throwable -> 0x0049, all -> 0x0046 }
            java.lang.String[] r3 = (java.lang.String[]) r3     // Catch:{ Throwable -> 0x0049, all -> 0x0046 }
            r2.close()     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
            if (r0 == 0) goto L_0x003c
            r0.close()     // Catch:{ Exception -> 0x0079 }
        L_0x003c:
            return r3
        L_0x003d:
            r2.close()     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
            if (r0 == 0) goto L_0x0079
            r0.close()     // Catch:{ Exception -> 0x0079 }
            goto L_0x0079
        L_0x0046:
            r3 = move-exception
            r4 = r1
            goto L_0x004f
        L_0x0049:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x004b }
        L_0x004b:
            r4 = move-exception
            r5 = r4
            r4 = r3
            r3 = r5
        L_0x004f:
            if (r4 == 0) goto L_0x005a
            r2.close()     // Catch:{ Throwable -> 0x0055, all -> 0x005e }
            goto L_0x005d
        L_0x0055:
            r2 = move-exception
            r4.addSuppressed(r2)     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
            goto L_0x005d
        L_0x005a:
            r2.close()     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
        L_0x005d:
            throw r3     // Catch:{ Throwable -> 0x0061, all -> 0x005e }
        L_0x005e:
            r2 = move-exception
            r3 = r1
            goto L_0x0067
        L_0x0061:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0063 }
        L_0x0063:
            r3 = move-exception
            r5 = r3
            r3 = r2
            r2 = r5
        L_0x0067:
            if (r0 == 0) goto L_0x0077
            if (r3 == 0) goto L_0x0074
            r0.close()     // Catch:{ Throwable -> 0x006f }
            goto L_0x0077
        L_0x006f:
            r0 = move-exception
            r3.addSuppressed(r0)     // Catch:{ Exception -> 0x0079 }
            goto L_0x0077
        L_0x0074:
            r0.close()     // Catch:{ Exception -> 0x0079 }
        L_0x0077:
            throw r2     // Catch:{ Exception -> 0x0079 }
        L_0x0078:
            return r1
        L_0x0079:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.DnsServersDetector.getServersMethodExec():java.lang.String[]");
    }

    private Set<String> methodExecParseProps(BufferedReader bufferedReader) throws Exception {
        HashSet hashSet = new HashSet(10);
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return hashSet;
            }
            int indexOf = readLine.indexOf(METHOD_EXEC_PROP_DELIM);
            if (indexOf != -1) {
                String substring = readLine.substring(1, indexOf);
                int i = indexOf + 4;
                int length = readLine.length() - 1;
                if (length >= i) {
                    String substring2 = readLine.substring(i, length);
                    if (!substring2.isEmpty() && (substring.endsWith(".dns") || substring.endsWith(".dns1") || substring.endsWith(".dns2") || substring.endsWith(".dns3") || substring.endsWith(".dns4"))) {
                        InetAddress byName = InetAddress.getByName(substring2);
                        if (byName != null) {
                            String hostAddress = byName.getHostAddress();
                            if (!(hostAddress == null || hostAddress.length() == 0)) {
                                hashSet.add(hostAddress);
                            }
                        }
                    }
                }
            }
        }
    }

    @TargetApi(21)
    private boolean linkPropertiesHasDefaultRoute(LinkProperties linkProperties) {
        for (RouteInfo isDefaultRoute : linkProperties.getRoutes()) {
            if (isDefaultRoute.isDefaultRoute()) {
                return true;
            }
        }
        return false;
    }
}
