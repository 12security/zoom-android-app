package com.google.api.client.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class IOUtils {
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, true);
    }

    public static void copy(InputStream inputStream, OutputStream outputStream, boolean z) throws IOException {
        try {
            ByteStreams.copy(inputStream, outputStream);
        } finally {
            if (z) {
                inputStream.close();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public static long computeLength(StreamingContent streamingContent) throws IOException {
        ByteCountingOutputStream byteCountingOutputStream = new ByteCountingOutputStream();
        try {
            streamingContent.writeTo(byteCountingOutputStream);
            byteCountingOutputStream.close();
            return byteCountingOutputStream.count;
        } catch (Throwable th) {
            byteCountingOutputStream.close();
            throw th;
        }
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        serialize(obj, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void serialize(Object obj, OutputStream outputStream) throws IOException {
        try {
            new ObjectOutputStream(outputStream).writeObject(obj);
        } finally {
            outputStream.close();
        }
    }

    public static <S extends Serializable> S deserialize(byte[] bArr) throws IOException {
        if (bArr == null) {
            return null;
        }
        return deserialize((InputStream) new ByteArrayInputStream(bArr));
    }

    public static <S extends Serializable> S deserialize(InputStream inputStream) throws IOException {
        try {
            S s = (Serializable) new ObjectInputStream(inputStream).readObject();
            inputStream.close();
            return s;
        } catch (ClassNotFoundException e) {
            IOException iOException = new IOException("Failed to deserialize object");
            iOException.initCause(e);
            throw iOException;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0047 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0048  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isSymbolicLink(java.io.File r7) throws java.io.IOException {
        /*
            r0 = 1
            r1 = 0
            java.lang.String r2 = "java.nio.file.Files"
            java.lang.Class r2 = java.lang.Class.forName(r2)     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.String r3 = "java.nio.file.Path"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.Class<java.io.File> r4 = java.io.File.class
            java.lang.String r5 = "toPath"
            java.lang.Class[] r6 = new java.lang.Class[r1]     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.reflect.Method r4 = r4.getMethod(r5, r6)     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.Object r4 = r4.invoke(r7, r5)     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.String r5 = "isSymbolicLink"
            java.lang.Class[] r6 = new java.lang.Class[r0]     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            r6[r1] = r3     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.reflect.Method r2 = r2.getMethod(r5, r6)     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            r3 = 0
            java.lang.Object[] r5 = new java.lang.Object[r0]     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            r5[r1] = r4     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.Object r2 = r2.invoke(r3, r5)     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            java.lang.Boolean r2 = (java.lang.Boolean) r2     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            boolean r7 = r2.booleanValue()     // Catch:{ InvocationTargetException -> 0x006e, ClassNotFoundException -> 0x0040, IllegalArgumentException -> 0x003e, SecurityException -> 0x003c, IllegalAccessException -> 0x003a, NoSuchMethodException -> 0x0038 }
            return r7
        L_0x0038:
            goto L_0x0041
        L_0x003a:
            goto L_0x0041
        L_0x003c:
            goto L_0x0041
        L_0x003e:
            goto L_0x0041
        L_0x0040:
        L_0x0041:
            char r2 = java.io.File.separatorChar
            r3 = 92
            if (r2 != r3) goto L_0x0048
            return r1
        L_0x0048:
            java.lang.String r1 = r7.getParent()
            if (r1 == 0) goto L_0x0060
            java.io.File r1 = new java.io.File
            java.io.File r2 = r7.getParentFile()
            java.io.File r2 = r2.getCanonicalFile()
            java.lang.String r7 = r7.getName()
            r1.<init>(r2, r7)
            r7 = r1
        L_0x0060:
            java.io.File r1 = r7.getCanonicalFile()
            java.io.File r7 = r7.getAbsoluteFile()
            boolean r7 = r1.equals(r7)
            r7 = r7 ^ r0
            return r7
        L_0x006e:
            r7 = move-exception
            java.lang.Throwable r7 = r7.getCause()
            java.lang.Class<java.io.IOException> r0 = java.io.IOException.class
            com.google.api.client.util.Throwables.propagateIfPossible(r7, r0)
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r0.<init>(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.util.IOUtils.isSymbolicLink(java.io.File):boolean");
    }
}
