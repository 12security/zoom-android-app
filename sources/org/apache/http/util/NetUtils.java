package org.apache.http.util;

public final class NetUtils {
    /* JADX WARNING: type inference failed for: r0v6, types: [java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void formatAddress(java.lang.StringBuilder r1, java.net.SocketAddress r2) {
        /*
            java.lang.String r0 = "Buffer"
            org.apache.http.util.Args.notNull(r1, r0)
            java.lang.String r0 = "Socket address"
            org.apache.http.util.Args.notNull(r2, r0)
            boolean r0 = r2 instanceof java.net.InetSocketAddress
            if (r0 == 0) goto L_0x002a
            java.net.InetSocketAddress r2 = (java.net.InetSocketAddress) r2
            java.net.InetAddress r0 = r2.getAddress()
            if (r0 == 0) goto L_0x001a
            java.lang.String r0 = r0.getHostAddress()
        L_0x001a:
            r1.append(r0)
            r0 = 58
            r1.append(r0)
            int r2 = r2.getPort()
            r1.append(r2)
            goto L_0x002d
        L_0x002a:
            r1.append(r2)
        L_0x002d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.util.NetUtils.formatAddress(java.lang.StringBuilder, java.net.SocketAddress):void");
    }
}
