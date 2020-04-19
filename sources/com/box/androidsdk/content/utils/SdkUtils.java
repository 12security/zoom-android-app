package com.box.androidsdk.content.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.eclipsesource.json.JsonValue;
import com.google.common.base.Ascii;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SdkUtils {
    private static final int BUFFER_SIZE = 8192;
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    public static String generateStateToken() {
        return UUID.randomUUID().toString();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException, InterruptedException {
        Exception exc;
        byte[] bArr = new byte[8192];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    outputStream.flush();
                    break;
                } else if (!Thread.currentThread().isInterrupted()) {
                    outputStream.write(bArr, 0, read);
                } else {
                    throw new InterruptedException();
                }
            } catch (Exception e) {
                exc = e;
                if (exc instanceof IOException) {
                    throw ((IOException) exc);
                } else if (exc instanceof InterruptedException) {
                    throw ((InterruptedException) exc);
                }
            } catch (Throwable th) {
                if (exc == null) {
                    outputStream.flush();
                }
                inputStream.close();
                throw th;
            }
        }
        inputStream.close();
    }

    public static OutputStream createArrayOutputStream(final OutputStream[] outputStreamArr) {
        return new OutputStream() {
            public void close() throws IOException {
                for (OutputStream close : outputStreamArr) {
                    close.close();
                }
                super.close();
            }

            public void flush() throws IOException {
                for (OutputStream flush : outputStreamArr) {
                    flush.flush();
                }
                super.flush();
            }

            public void write(int i) throws IOException {
                for (OutputStream write : outputStreamArr) {
                    write.write(i);
                }
            }

            public void write(byte[] bArr) throws IOException {
                for (OutputStream write : outputStreamArr) {
                    write.write(bArr);
                }
            }

            public void write(byte[] bArr, int i, int i2) throws IOException {
                for (OutputStream write : outputStreamArr) {
                    write.write(bArr, i, i2);
                }
            }
        };
    }

    public static boolean isEmptyString(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    private static char[] encodeHex(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[(length << 1)];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            char[] cArr2 = HEX_CHARS;
            cArr[i] = cArr2[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr[i3] = cArr2[bArr[i2] & Ascii.f228SI];
        }
        return cArr;
    }

    public static long parseJsonValueToLong(JsonValue jsonValue) {
        try {
            return jsonValue.asLong();
        } catch (UnsupportedOperationException unused) {
            return Long.parseLong(jsonValue.asString().replace("\"", ""));
        }
    }

    public static long parseJsonValueToInteger(JsonValue jsonValue) {
        try {
            return (long) jsonValue.asInt();
        } catch (UnsupportedOperationException unused) {
            return (long) Integer.parseInt(jsonValue.asString().replace("\"", ""));
        }
    }

    public static String concatStringWithDelimiter(String[] strArr, String str) {
        StringBuilder sb = new StringBuilder();
        int length = strArr.length;
        int i = 0;
        while (true) {
            int i2 = length - 1;
            if (i < i2) {
                sb.append(strArr[i]);
                sb.append(str);
                i++;
            } else {
                sb.append(strArr[i2]);
                return sb.toString();
            }
        }
    }

    public static ThreadPoolExecutor createDefaultThreadPoolExecutor(int i, int i2, long j, TimeUnit timeUnit) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(i, i2, j, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable);
            }
        });
        return threadPoolExecutor;
    }

    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r7v0 */
    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r8v1 */
    /* JADX WARNING: type inference failed for: r8v2 */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r7v4 */
    /* JADX WARNING: type inference failed for: r8v3 */
    /* JADX WARNING: type inference failed for: r8v4 */
    /* JADX WARNING: type inference failed for: r7v6 */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r8v5 */
    /* JADX WARNING: type inference failed for: r8v6 */
    /* JADX WARNING: type inference failed for: r8v7 */
    /* JADX WARNING: type inference failed for: r8v8 */
    /* JADX WARNING: type inference failed for: r8v10 */
    /* JADX WARNING: type inference failed for: r8v11 */
    /* JADX WARNING: type inference failed for: r8v13 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r7v15 */
    /* JADX WARNING: type inference failed for: r7v16 */
    /* JADX WARNING: type inference failed for: r7v17 */
    /* JADX WARNING: type inference failed for: r7v18 */
    /* JADX WARNING: type inference failed for: r7v19 */
    /* JADX WARNING: type inference failed for: r7v20 */
    /* JADX WARNING: type inference failed for: r7v21 */
    /* JADX WARNING: type inference failed for: r7v22 */
    /* JADX WARNING: type inference failed for: r7v23 */
    /* JADX WARNING: type inference failed for: r7v24 */
    /* JADX WARNING: type inference failed for: r8v15 */
    /* JADX WARNING: type inference failed for: r8v16 */
    /* JADX WARNING: type inference failed for: r8v17 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T cloneSerializable(T r10) {
        /*
            r0 = 3
            r1 = 2
            r2 = 1
            r3 = 0
            r4 = 4
            r5 = 0
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0078, ClassNotFoundException -> 0x0066, all -> 0x0054 }
            r6.<init>()     // Catch:{ IOException -> 0x0078, ClassNotFoundException -> 0x0066, all -> 0x0054 }
            java.io.ObjectOutputStream r7 = new java.io.ObjectOutputStream     // Catch:{ IOException -> 0x0051, ClassNotFoundException -> 0x004e, all -> 0x004b }
            r7.<init>(r6)     // Catch:{ IOException -> 0x0051, ClassNotFoundException -> 0x004e, all -> 0x004b }
            r7.writeObject(r10)     // Catch:{ IOException -> 0x0048, ClassNotFoundException -> 0x0045, all -> 0x0042 }
            java.io.ByteArrayInputStream r10 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0048, ClassNotFoundException -> 0x0045, all -> 0x0042 }
            byte[] r8 = r6.toByteArray()     // Catch:{ IOException -> 0x0048, ClassNotFoundException -> 0x0045, all -> 0x0042 }
            r10.<init>(r8)     // Catch:{ IOException -> 0x0048, ClassNotFoundException -> 0x0045, all -> 0x0042 }
            java.io.ObjectInputStream r8 = new java.io.ObjectInputStream     // Catch:{ IOException -> 0x0040, ClassNotFoundException -> 0x003e, all -> 0x0038 }
            r8.<init>(r10)     // Catch:{ IOException -> 0x0040, ClassNotFoundException -> 0x003e, all -> 0x0038 }
            java.lang.Object r5 = r8.readObject()     // Catch:{ IOException -> 0x007c, ClassNotFoundException -> 0x006a, all -> 0x0033 }
            java.io.Closeable[] r4 = new java.io.Closeable[r4]
            r4[r3] = r6
            r4[r2] = r7
            r4[r1] = r10
            r4[r0] = r8
            closeQuietly(r4)
            return r5
        L_0x0033:
            r5 = move-exception
            r9 = r5
            r5 = r10
            r10 = r9
            goto L_0x0058
        L_0x0038:
            r8 = move-exception
            r9 = r5
            r5 = r10
            r10 = r8
            r8 = r9
            goto L_0x0058
        L_0x003e:
            r8 = r5
            goto L_0x006a
        L_0x0040:
            r8 = r5
            goto L_0x007c
        L_0x0042:
            r10 = move-exception
            r8 = r5
            goto L_0x0058
        L_0x0045:
            r10 = r5
            r8 = r10
            goto L_0x006a
        L_0x0048:
            r10 = r5
            r8 = r10
            goto L_0x007c
        L_0x004b:
            r10 = move-exception
            r7 = r5
            goto L_0x0057
        L_0x004e:
            r10 = r5
            r7 = r10
            goto L_0x0069
        L_0x0051:
            r10 = r5
            r7 = r10
            goto L_0x007b
        L_0x0054:
            r10 = move-exception
            r6 = r5
            r7 = r6
        L_0x0057:
            r8 = r7
        L_0x0058:
            java.io.Closeable[] r4 = new java.io.Closeable[r4]
            r4[r3] = r6
            r4[r2] = r7
            r4[r1] = r5
            r4[r0] = r8
            closeQuietly(r4)
            throw r10
        L_0x0066:
            r10 = r5
            r6 = r10
            r7 = r6
        L_0x0069:
            r8 = r7
        L_0x006a:
            java.io.Closeable[] r4 = new java.io.Closeable[r4]
            r4[r3] = r6
            r4[r2] = r7
            r4[r1] = r10
            r4[r0] = r8
            closeQuietly(r4)
            return r5
        L_0x0078:
            r10 = r5
            r6 = r10
            r7 = r6
        L_0x007b:
            r8 = r7
        L_0x007c:
            java.io.Closeable[] r4 = new java.io.Closeable[r4]
            r4[r3] = r6
            r4[r2] = r7
            r4[r1] = r10
            r4[r0] = r8
            closeQuietly(r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.box.androidsdk.content.utils.SdkUtils.cloneSerializable(java.lang.Object):java.lang.Object");
    }

    public static String convertSerializableToString(Serializable serializable) {
        ObjectOutputStream objectOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream objectOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                try {
                    objectOutputStream.writeObject(serializable);
                    String str = new String(byteArrayOutputStream.toByteArray());
                    closeQuietly(byteArrayOutputStream, objectOutputStream);
                    closeQuietly(objectOutputStream);
                    return str;
                } catch (IOException unused) {
                    closeQuietly(byteArrayOutputStream, objectOutputStream);
                    closeQuietly(objectOutputStream);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    objectOutputStream2 = objectOutputStream;
                    closeQuietly(byteArrayOutputStream, objectOutputStream2);
                    closeQuietly(objectOutputStream2);
                    throw th;
                }
            } catch (IOException unused2) {
                objectOutputStream = null;
                closeQuietly(byteArrayOutputStream, objectOutputStream);
                closeQuietly(objectOutputStream);
                return null;
            } catch (Throwable th2) {
                th = th2;
                closeQuietly(byteArrayOutputStream, objectOutputStream2);
                closeQuietly(objectOutputStream2);
                throw th;
            }
        } catch (IOException unused3) {
            byteArrayOutputStream = null;
            objectOutputStream = null;
            closeQuietly(byteArrayOutputStream, objectOutputStream);
            closeQuietly(objectOutputStream);
            return null;
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream = null;
            closeQuietly(byteArrayOutputStream, objectOutputStream2);
            closeQuietly(objectOutputStream2);
            throw th;
        }
    }

    public static void closeQuietly(Closeable... closeableArr) {
        for (Closeable close : closeableArr) {
            try {
                close.close();
            } catch (Exception unused) {
            }
        }
    }

    public static boolean deleteFolderRecursive(File file) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                return false;
            }
            for (File deleteFolderRecursive : listFiles) {
                deleteFolderRecursive(deleteFolderRecursive);
            }
        }
        return file.delete();
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
        if (networkInfo.isConnected()) {
            return true;
        }
        if (networkInfo2 == null || !networkInfo2.isConnected()) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x006b A[SYNTHETIC, Splitter:B:26:0x006b] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008a A[SYNTHETIC, Splitter:B:33:0x008a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getRawFile(android.content.Context r5, int r6) {
        /*
            r0 = 0
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0051, all -> 0x004e }
            r1.<init>()     // Catch:{ IOException -> 0x0051, all -> 0x004e }
            android.content.res.Resources r5 = r5.getResources()     // Catch:{ IOException -> 0x0051, all -> 0x004e }
            java.io.InputStream r5 = r5.openRawResource(r6)     // Catch:{ IOException -> 0x0051, all -> 0x004e }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0051, all -> 0x004e }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0051, all -> 0x004e }
            r3.<init>(r5)     // Catch:{ IOException -> 0x0051, all -> 0x004e }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0051, all -> 0x004e }
            r5 = 1
        L_0x0019:
            java.lang.String r3 = r2.readLine()     // Catch:{ IOException -> 0x004c }
            if (r3 == 0) goto L_0x002c
            if (r5 == 0) goto L_0x0023
            r5 = 0
            goto L_0x0028
        L_0x0023:
            r4 = 10
            r1.append(r4)     // Catch:{ IOException -> 0x004c }
        L_0x0028:
            r1.append(r3)     // Catch:{ IOException -> 0x004c }
            goto L_0x0019
        L_0x002c:
            java.lang.String r5 = r1.toString()     // Catch:{ IOException -> 0x004c }
            r2.close()     // Catch:{ Exception -> 0x0034 }
            goto L_0x004b
        L_0x0034:
            r0 = move-exception
            java.lang.String r1 = "getRawFile"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = ""
            r2.append(r3)
            r2.append(r6)
            java.lang.String r6 = r2.toString()
            com.box.androidsdk.content.utils.BoxLogUtils.m12e(r1, r6, r0)
        L_0x004b:
            return r5
        L_0x004c:
            r5 = move-exception
            goto L_0x0053
        L_0x004e:
            r5 = move-exception
            r2 = r0
            goto L_0x0088
        L_0x0051:
            r5 = move-exception
            r2 = r0
        L_0x0053:
            java.lang.String r1 = "getRawFile"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0087 }
            r3.<init>()     // Catch:{ all -> 0x0087 }
            java.lang.String r4 = ""
            r3.append(r4)     // Catch:{ all -> 0x0087 }
            r3.append(r6)     // Catch:{ all -> 0x0087 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0087 }
            com.box.androidsdk.content.utils.BoxLogUtils.m12e(r1, r3, r5)     // Catch:{ all -> 0x0087 }
            if (r2 == 0) goto L_0x0086
            r2.close()     // Catch:{ Exception -> 0x006f }
            goto L_0x0086
        L_0x006f:
            r5 = move-exception
            java.lang.String r1 = "getRawFile"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = ""
            r2.append(r3)
            r2.append(r6)
            java.lang.String r6 = r2.toString()
            com.box.androidsdk.content.utils.BoxLogUtils.m12e(r1, r6, r5)
        L_0x0086:
            return r0
        L_0x0087:
            r5 = move-exception
        L_0x0088:
            if (r2 == 0) goto L_0x00a5
            r2.close()     // Catch:{ Exception -> 0x008e }
            goto L_0x00a5
        L_0x008e:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ""
            r1.append(r2)
            r1.append(r6)
            java.lang.String r6 = r1.toString()
            java.lang.String r1 = "getRawFile"
            com.box.androidsdk.content.utils.BoxLogUtils.m12e(r1, r6, r0)
        L_0x00a5:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.box.androidsdk.content.utils.SdkUtils.getRawFile(android.content.Context, int):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0048 A[SYNTHETIC, Splitter:B:25:0x0048] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0057 A[SYNTHETIC, Splitter:B:33:0x0057] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getAssetFile(android.content.Context r5, java.lang.String r6) {
        /*
            android.content.res.AssetManager r5 = r5.getAssets()
            r0 = 0
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x003f, all -> 0x003d }
            r1.<init>()     // Catch:{ IOException -> 0x003f, all -> 0x003d }
            java.io.InputStream r5 = r5.open(r6)     // Catch:{ IOException -> 0x003f, all -> 0x003d }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x003f, all -> 0x003d }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x003f, all -> 0x003d }
            r3.<init>(r5)     // Catch:{ IOException -> 0x003f, all -> 0x003d }
            r2.<init>(r3)     // Catch:{ IOException -> 0x003f, all -> 0x003d }
            r5 = 1
        L_0x0019:
            java.lang.String r3 = r2.readLine()     // Catch:{ IOException -> 0x003b }
            if (r3 == 0) goto L_0x002c
            if (r5 == 0) goto L_0x0023
            r5 = 0
            goto L_0x0028
        L_0x0023:
            r4 = 10
            r1.append(r4)     // Catch:{ IOException -> 0x003b }
        L_0x0028:
            r1.append(r3)     // Catch:{ IOException -> 0x003b }
            goto L_0x0019
        L_0x002c:
            java.lang.String r5 = r1.toString()     // Catch:{ IOException -> 0x003b }
            r2.close()     // Catch:{ Exception -> 0x0034 }
            goto L_0x003a
        L_0x0034:
            r0 = move-exception
            java.lang.String r1 = "getAssetFile"
            com.box.androidsdk.content.utils.BoxLogUtils.m12e(r1, r6, r0)
        L_0x003a:
            return r5
        L_0x003b:
            r5 = move-exception
            goto L_0x0041
        L_0x003d:
            r5 = move-exception
            goto L_0x0055
        L_0x003f:
            r5 = move-exception
            r2 = r0
        L_0x0041:
            java.lang.String r1 = "getAssetFile"
            com.box.androidsdk.content.utils.BoxLogUtils.m12e(r1, r6, r5)     // Catch:{ all -> 0x0053 }
            if (r2 == 0) goto L_0x0052
            r2.close()     // Catch:{ Exception -> 0x004c }
            goto L_0x0052
        L_0x004c:
            r5 = move-exception
            java.lang.String r1 = "getAssetFile"
            com.box.androidsdk.content.utils.BoxLogUtils.m12e(r1, r6, r5)
        L_0x0052:
            return r0
        L_0x0053:
            r5 = move-exception
            r0 = r2
        L_0x0055:
            if (r0 == 0) goto L_0x0061
            r0.close()     // Catch:{ Exception -> 0x005b }
            goto L_0x0061
        L_0x005b:
            r0 = move-exception
            java.lang.String r1 = "getAssetFile"
            com.box.androidsdk.content.utils.BoxLogUtils.m12e(r1, r6, r0)
        L_0x0061:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.box.androidsdk.content.utils.SdkUtils.getAssetFile(android.content.Context, java.lang.String):java.lang.String");
    }
}
