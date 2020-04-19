package p021us.zoom.androidlib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import com.box.androidsdk.content.models.BoxUser;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/* renamed from: us.zoom.androidlib.util.NetworkUtil */
public class NetworkUtil {
    private static boolean IPV4_FIRST = true;
    public static final String IP_NULL = "0.0.0.0";
    public static final int NETWORK_TYPE_ETHERNET = 4;
    public static final int NETWORK_TYPE_MOBILE = 2;
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_UNKNOWN = 5;
    public static final int NETWORK_TYPE_WIFI = 1;
    public static final int NETWORK_TYPE_WiMAX = 3;
    private static final String TAG = "NetworkUtil";

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean hasDataNetwork(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return false;
            }
            boolean isConnected = activeNetworkInfo.isConnected();
            return (!isConnected || activeNetworkInfo.getType() != 0) ? isConnected : checkCellular(connectivityManager, context);
        } catch (Exception unused) {
            return false;
        }
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static int getDataNetworkType(Context context) {
        if (context == null) {
            return 5;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return 5;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        int i = 0;
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return 0;
        }
        int type = activeNetworkInfo.getType();
        if (type == 9) {
            return 4;
        }
        switch (type) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
                if (checkCellular(connectivityManager, context)) {
                    i = 2;
                }
                return i;
            case 1:
                return 1;
            case 6:
                return 3;
            default:
                return 5;
        }
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    private static boolean checkCellular(ConnectivityManager connectivityManager, Context context) {
        if (!OsUtil.isAtLeastO() || !checkCellularNew(connectivityManager)) {
            return checkCellularOld(context);
        }
        return true;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    @TargetApi(26)
    private static boolean checkCellularNew(ConnectivityManager connectivityManager) {
        boolean z = false;
        try {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null && networkCapabilities.hasTransport(0)) {
                z = true;
            }
            return z;
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean checkCellularOld(Context context) {
        boolean z = false;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(BoxUser.FIELD_PHONE);
            if (telephonyManager != null && telephonyManager.getDataState() == 2) {
                z = true;
            }
            return z;
        } catch (Exception unused) {
            return false;
        }
    }

    public static String getNetworkIP(Context context) {
        if (context == null) {
            return IP_NULL;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return IP_NULL;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return IP_NULL;
        }
        String str = IP_NULL;
        int type = activeNetworkInfo.getType();
        if (type != 9) {
            switch (type) {
                case 0:
                case 2:
                case 3:
                case 4:
                case 5:
                    str = getLocalIpAddress(context, connectivityManager);
                    break;
                case 1:
                    str = getWifiIpAddress(context);
                    break;
            }
        } else {
            str = getEtherNetIP();
        }
        return str;
    }

    private static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager == null) {
            return IP_NULL;
        }
        try {
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                ipAddress = Integer.reverseBytes(ipAddress);
            }
            try {
                return InetAddress.getByAddress(BigInteger.valueOf((long) ipAddress).toByteArray()).getHostAddress();
            } catch (UnknownHostException unused) {
                return null;
            }
        } catch (Exception unused2) {
            return IP_NULL;
        }
    }

    private static String getLocalIpAddress(Context context, ConnectivityManager connectivityManager) {
        String str;
        String localIpAddressAboveM = OsUtil.isAtLeastM() ? getLocalIpAddressAboveM(context) : null;
        if (!TextUtils.isEmpty(localIpAddressAboveM) && !IP_NULL.equals(localIpAddressAboveM)) {
            return localIpAddressAboveM;
        }
        String localIpAddressByReflect = getLocalIpAddressByReflect(connectivityManager);
        if (!TextUtils.isEmpty(localIpAddressByReflect) && !IP_NULL.equals(localIpAddressByReflect)) {
            return localIpAddressByReflect;
        }
        if (OsUtil.isAtLeastL()) {
            str = getLocalIpAddressAboveL(context);
        } else {
            str = getLocalIpAddressBelowL();
        }
        if (!TextUtils.isEmpty(str) && !IP_NULL.equals(str)) {
            return str;
        }
        String localIpAddressInNetworkInterface = getLocalIpAddressInNetworkInterface(context, connectivityManager);
        return (TextUtils.isEmpty(localIpAddressInNetworkInterface) || IP_NULL.equals(localIpAddressInNetworkInterface)) ? IP_NULL : localIpAddressInNetworkInterface;
    }

    private static String getLocalIpAddressBelowL() {
        try {
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                Iterator it2 = Collections.list(((NetworkInterface) it.next()).getInetAddresses()).iterator();
                while (true) {
                    if (it2.hasNext()) {
                        InetAddress inetAddress = (InetAddress) it2.next();
                        if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException unused) {
        }
        return IP_NULL;
    }

    private static String getLocalIpAddressAboveL(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return null;
        }
        String str = "";
        Network[] allNetworks = connectivityManager.getAllNetworks();
        if (allNetworks != null) {
            int length = allNetworks.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String localIpAddressFromNetwork = getLocalIpAddressFromNetwork(connectivityManager, allNetworks[i]);
                if (!TextUtils.isEmpty(localIpAddressFromNetwork)) {
                    str = localIpAddressFromNetwork;
                    break;
                }
                i++;
            }
        }
        return !TextUtils.isEmpty(str) ? str : IP_NULL;
    }

    @RequiresApi(api = 23)
    private static String getLocalIpAddressAboveM(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return null;
        }
        String localIpAddressFromNetwork = getLocalIpAddressFromNetwork(connectivityManager, connectivityManager.getActiveNetwork());
        return !TextUtils.isEmpty(localIpAddressFromNetwork) ? localIpAddressFromNetwork : IP_NULL;
    }

    private static String getLocalIpAddressFromNetwork(ConnectivityManager connectivityManager, Network network) {
        String str = null;
        if (VERSION.SDK_INT >= 21 && network != null) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo != null && networkInfo.isConnected()) {
                LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
                if (linkProperties == null) {
                    return null;
                }
                List<LinkAddress> linkAddresses = linkProperties.getLinkAddresses();
                if (linkAddresses == null) {
                    return null;
                }
                String str2 = null;
                for (LinkAddress address : linkAddresses) {
                    InetAddress address2 = address.getAddress();
                    if (!address2.isLoopbackAddress() && !address2.isLinkLocalAddress()) {
                        if (str == null && (address2 instanceof Inet4Address)) {
                            String hostAddress = address2.getHostAddress();
                            if (!isValidIPv4(hostAddress)) {
                                hostAddress = str;
                            }
                            str = hostAddress;
                        } else if (str2 == null && (address2 instanceof Inet6Address)) {
                            String hostAddress2 = address2.getHostAddress();
                            if (isValidIPv6(hostAddress2)) {
                                str2 = hostAddress2;
                            }
                        }
                    }
                }
                str = getBestIp(str, str2);
            }
        }
        return str;
    }

    private static String getBestIp(String str, String str2) {
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) {
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        return (!IPV4_FIRST && !TextUtils.isEmpty(str2)) ? str2 : str;
    }

    @TargetApi(21)
    private static boolean linkPropertiesHasDefaultRoute(LinkProperties linkProperties) {
        for (RouteInfo isDefaultRoute : linkProperties.getRoutes()) {
            if (isDefaultRoute.isDefaultRoute()) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x0068 A[Catch:{ Throwable -> 0x00d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x003e A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getLocalIpAddressByReflect(android.net.ConnectivityManager r12) {
        /*
            java.lang.String r0 = ""
            java.lang.Class r1 = r12.getClass()     // Catch:{ Throwable -> 0x00d7 }
            java.lang.String r2 = "getActiveLinkProperties"
            r3 = 0
            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch:{ Throwable -> 0x00d7 }
            java.lang.reflect.Method r1 = r1.getDeclaredMethod(r2, r4)     // Catch:{ Throwable -> 0x00d7 }
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00d7 }
            java.lang.Object r12 = r1.invoke(r12, r2)     // Catch:{ Throwable -> 0x00d7 }
            r1 = 0
            if (r12 != 0) goto L_0x0019
            return r1
        L_0x0019:
            java.lang.Class r2 = r12.getClass()     // Catch:{ Throwable -> 0x00d7 }
            java.lang.String r4 = "getLinkAddresses"
            java.lang.Class[] r5 = new java.lang.Class[r3]     // Catch:{ NoSuchMethodException -> 0x0026 }
            java.lang.reflect.Method r4 = r2.getDeclaredMethod(r4, r5)     // Catch:{ NoSuchMethodException -> 0x0026 }
            goto L_0x0027
        L_0x0026:
            r4 = r1
        L_0x0027:
            if (r4 == 0) goto L_0x0030
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00d7 }
            java.lang.Object r4 = r4.invoke(r12, r5)     // Catch:{ Throwable -> 0x00d7 }
            goto L_0x0031
        L_0x0030:
            r4 = r1
        L_0x0031:
            boolean r5 = r4 instanceof java.util.Collection     // Catch:{ Throwable -> 0x00d7 }
            if (r5 == 0) goto L_0x0082
            r5 = r4
            java.util.Collection r5 = (java.util.Collection) r5     // Catch:{ Throwable -> 0x00d7 }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ Throwable -> 0x00d7 }
            r6 = r1
            r7 = r6
        L_0x003e:
            boolean r8 = r5.hasNext()     // Catch:{ Throwable -> 0x00d7 }
            if (r8 == 0) goto L_0x007e
            java.lang.Object r8 = r5.next()     // Catch:{ Throwable -> 0x00d7 }
            if (r8 == 0) goto L_0x0065
            java.lang.Class r9 = r8.getClass()     // Catch:{ Throwable -> 0x00d7 }
            java.lang.String r10 = "getAddress"
            java.lang.Class[] r11 = new java.lang.Class[r3]     // Catch:{ Exception -> 0x0057 }
            java.lang.reflect.Method r9 = r9.getDeclaredMethod(r10, r11)     // Catch:{ Exception -> 0x0057 }
            goto L_0x0058
        L_0x0057:
            r9 = r1
        L_0x0058:
            if (r9 == 0) goto L_0x0065
            java.lang.Object[] r10 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00d7 }
            java.lang.Object r8 = r9.invoke(r8, r10)     // Catch:{ Throwable -> 0x00d7 }
            if (r8 == 0) goto L_0x0065
            java.net.InetAddress r8 = (java.net.InetAddress) r8     // Catch:{ Throwable -> 0x00d7 }
            goto L_0x0066
        L_0x0065:
            r8 = r1
        L_0x0066:
            if (r8 == 0) goto L_0x003e
            if (r6 != 0) goto L_0x0073
            boolean r9 = r8 instanceof java.net.Inet4Address     // Catch:{ Throwable -> 0x00d7 }
            if (r9 == 0) goto L_0x0073
            java.lang.String r6 = getIpAddressFromInetAddress(r8)     // Catch:{ Throwable -> 0x00d7 }
            goto L_0x003e
        L_0x0073:
            if (r7 != 0) goto L_0x003e
            boolean r9 = r8 instanceof java.net.Inet6Address     // Catch:{ Throwable -> 0x00d7 }
            if (r9 == 0) goto L_0x003e
            java.lang.String r7 = getIpAddressFromInetAddress(r8)     // Catch:{ Throwable -> 0x00d7 }
            goto L_0x003e
        L_0x007e:
            java.lang.String r0 = getBestIp(r6, r7)     // Catch:{ Throwable -> 0x00d7 }
        L_0x0082:
            boolean r5 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x00d7 }
            if (r5 != 0) goto L_0x0089
            return r0
        L_0x0089:
            java.lang.String r5 = "getAllAddresses"
            java.lang.Class[] r6 = new java.lang.Class[r3]     // Catch:{ NoSuchMethodException -> 0x0092 }
            java.lang.reflect.Method r2 = r2.getDeclaredMethod(r5, r6)     // Catch:{ NoSuchMethodException -> 0x0092 }
            goto L_0x009c
        L_0x0092:
            java.lang.String r5 = "getAddresses"
            java.lang.Class[] r6 = new java.lang.Class[r3]     // Catch:{ NoSuchMethodException -> 0x009b }
            java.lang.reflect.Method r2 = r2.getDeclaredMethod(r5, r6)     // Catch:{ NoSuchMethodException -> 0x009b }
            goto L_0x009c
        L_0x009b:
            r2 = r1
        L_0x009c:
            if (r2 == 0) goto L_0x00a4
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00d7 }
            java.lang.Object r4 = r2.invoke(r12, r3)     // Catch:{ Throwable -> 0x00d7 }
        L_0x00a4:
            boolean r12 = r4 instanceof java.util.Collection     // Catch:{ Throwable -> 0x00d7 }
            if (r12 == 0) goto L_0x00d7
            java.util.Collection r4 = (java.util.Collection) r4     // Catch:{ Throwable -> 0x00d7 }
            java.util.Iterator r12 = r4.iterator()     // Catch:{ Throwable -> 0x00d7 }
            r2 = r1
        L_0x00af:
            boolean r3 = r12.hasNext()     // Catch:{ Throwable -> 0x00d7 }
            if (r3 == 0) goto L_0x00d3
            java.lang.Object r3 = r12.next()     // Catch:{ Throwable -> 0x00d7 }
            java.net.InetAddress r3 = (java.net.InetAddress) r3     // Catch:{ Throwable -> 0x00d7 }
            if (r3 == 0) goto L_0x00af
            if (r1 != 0) goto L_0x00c8
            boolean r4 = r3 instanceof java.net.Inet4Address     // Catch:{ Throwable -> 0x00d7 }
            if (r4 == 0) goto L_0x00c8
            java.lang.String r1 = getIpAddressFromInetAddress(r3)     // Catch:{ Throwable -> 0x00d7 }
            goto L_0x00af
        L_0x00c8:
            if (r2 != 0) goto L_0x00af
            boolean r4 = r3 instanceof java.net.Inet6Address     // Catch:{ Throwable -> 0x00d7 }
            if (r4 == 0) goto L_0x00af
            java.lang.String r2 = getIpAddressFromInetAddress(r3)     // Catch:{ Throwable -> 0x00d7 }
            goto L_0x00af
        L_0x00d3:
            java.lang.String r0 = getBestIp(r1, r2)     // Catch:{ Throwable -> 0x00d7 }
        L_0x00d7:
            boolean r12 = android.text.TextUtils.isEmpty(r0)
            if (r12 != 0) goto L_0x00de
            return r0
        L_0x00de:
            java.lang.String r12 = "0.0.0.0"
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.NetworkUtil.getLocalIpAddressByReflect(android.net.ConnectivityManager):java.lang.String");
    }

    private static String getIpAddressFromInetAddress(InetAddress inetAddress) {
        if (inetAddress == null || inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
            return null;
        }
        if (inetAddress instanceof Inet4Address) {
            String hostAddress = inetAddress.getHostAddress();
            if (isValidIPv4(hostAddress)) {
                return hostAddress;
            }
            return null;
        } else if (!(inetAddress instanceof Inet6Address)) {
            return null;
        } else {
            String hostAddress2 = inetAddress.getHostAddress();
            if (isValidIPv6(hostAddress2)) {
                return hostAddress2;
            }
            return null;
        }
    }

    private static String getLocalIpAddressInNetworkInterface(Context context, ConnectivityManager connectivityManager) {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            String str = null;
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                String str2 = null;
                String str3 = null;
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        if (str2 == null && (inetAddress instanceof Inet4Address)) {
                            String hostAddress = inetAddress.getHostAddress();
                            if (isValidIPv4(hostAddress)) {
                                str2 = hostAddress;
                            }
                        } else if (str3 == null && (inetAddress instanceof Inet6Address)) {
                            String hostAddress2 = inetAddress.getHostAddress();
                            if (isValidIPv6(hostAddress2)) {
                                str3 = hostAddress2;
                            }
                        }
                    }
                }
                str = getBestIp(str2, str3);
            }
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        } catch (Throwable unused) {
        }
        return IP_NULL;
    }

    private static boolean isValidIPv4(String str) {
        return !TextUtils.isEmpty(str) && !str.equals("192.0.0.0") && !str.equals("192.0.0.1") && !str.equals("192.0.0.2") && !str.equals("192.0.0.3") && !str.equals("192.0.0.4") && !str.equals("192.0.0.5") && !str.equals("192.0.0.6") && !str.equals("192.0.0.7");
    }

    private static boolean isValidIPv6(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int indexOf = str.indexOf(37);
        if (indexOf > 0) {
            str = str.substring(0, indexOf);
        }
        String upperCase = str.toUpperCase();
        if (!upperCase.equals("::") && !upperCase.equals("::1") && ((!upperCase.contains("::") || !upperCase.contains(".")) && !upperCase.startsWith("FE80::") && !upperCase.startsWith("FEC0::") && !upperCase.startsWith("FC00::") && !upperCase.startsWith("FD00::") && (upperCase.length() <= 12 || upperCase.lastIndexOf("FF:FE") != upperCase.length() - 12))) {
            z = true;
        }
        return z;
    }

    private static String getEtherNetIP() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (true) {
                    if (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException unused) {
        }
        return IP_NULL;
    }
}
