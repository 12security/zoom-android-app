package com.google.api.client.googleapis.auth.clientlogin;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Beta;
import com.google.api.client.util.ObjectParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import org.apache.http.protocol.HTTP;

@Beta
final class AuthKeyValueParser implements ObjectParser {
    public static final AuthKeyValueParser INSTANCE = new AuthKeyValueParser();

    public String getContentType() {
        return HTTP.PLAIN_TEXT_TYPE;
    }

    public <T> T parse(HttpResponse httpResponse, Class<T> cls) throws IOException {
        httpResponse.setContentLoggingLimit(0);
        InputStream content = httpResponse.getContent();
        try {
            return parse(content, cls);
        } finally {
            content.close();
        }
    }

    /* JADX WARNING: type inference failed for: r7v4, types: [java.lang.Boolean] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T parse(java.io.InputStream r7, java.lang.Class<T> r8) throws java.io.IOException {
        /*
            r6 = this;
            com.google.api.client.util.ClassInfo r0 = com.google.api.client.util.ClassInfo.m66of(r8)
            java.lang.Object r1 = com.google.api.client.util.Types.newInstance(r8)
            java.io.BufferedReader r2 = new java.io.BufferedReader
            java.io.InputStreamReader r3 = new java.io.InputStreamReader
            r3.<init>(r7)
            r2.<init>(r3)
        L_0x0012:
            java.lang.String r7 = r2.readLine()
            if (r7 != 0) goto L_0x0019
            return r1
        L_0x0019:
            r3 = 61
            int r3 = r7.indexOf(r3)
            r4 = 0
            java.lang.String r4 = r7.substring(r4, r3)
            int r3 = r3 + 1
            java.lang.String r7 = r7.substring(r3)
            java.lang.reflect.Field r3 = r0.getField(r4)
            if (r3 == 0) goto L_0x0044
            java.lang.Class r4 = r3.getType()
            java.lang.Class r5 = java.lang.Boolean.TYPE
            if (r4 == r5) goto L_0x003c
            java.lang.Class<java.lang.Boolean> r5 = java.lang.Boolean.class
            if (r4 != r5) goto L_0x0040
        L_0x003c:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
        L_0x0040:
            com.google.api.client.util.FieldInfo.setFieldValue(r3, r1, r7)
            goto L_0x0012
        L_0x0044:
            java.lang.Class<com.google.api.client.util.GenericData> r3 = com.google.api.client.util.GenericData.class
            boolean r3 = r3.isAssignableFrom(r8)
            if (r3 == 0) goto L_0x0053
            r3 = r1
            com.google.api.client.util.GenericData r3 = (com.google.api.client.util.GenericData) r3
            r3.set(r4, r7)
            goto L_0x0012
        L_0x0053:
            java.lang.Class<java.util.Map> r3 = java.util.Map.class
            boolean r3 = r3.isAssignableFrom(r8)
            if (r3 == 0) goto L_0x0012
            r3 = r1
            java.util.Map r3 = (java.util.Map) r3
            r3.put(r4, r7)
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.googleapis.auth.clientlogin.AuthKeyValueParser.parse(java.io.InputStream, java.lang.Class):java.lang.Object");
    }

    private AuthKeyValueParser() {
    }

    public <T> T parseAndClose(InputStream inputStream, Charset charset, Class<T> cls) throws IOException {
        return parseAndClose((Reader) new InputStreamReader(inputStream, charset), cls);
    }

    public Object parseAndClose(InputStream inputStream, Charset charset, Type type) {
        throw new UnsupportedOperationException("Type-based parsing is not yet supported -- use Class<T> instead");
    }

    /* JADX WARNING: type inference failed for: r3v3, types: [java.lang.Boolean] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T parseAndClose(java.io.Reader r8, java.lang.Class<T> r9) throws java.io.IOException {
        /*
            r7 = this;
            com.google.api.client.util.ClassInfo r0 = com.google.api.client.util.ClassInfo.m66of(r9)     // Catch:{ all -> 0x0060 }
            java.lang.Object r1 = com.google.api.client.util.Types.newInstance(r9)     // Catch:{ all -> 0x0060 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ all -> 0x0060 }
            r2.<init>(r8)     // Catch:{ all -> 0x0060 }
        L_0x000d:
            java.lang.String r3 = r2.readLine()     // Catch:{ all -> 0x0060 }
            if (r3 != 0) goto L_0x0017
            r8.close()
            return r1
        L_0x0017:
            r4 = 61
            int r4 = r3.indexOf(r4)     // Catch:{ all -> 0x0060 }
            r5 = 0
            java.lang.String r5 = r3.substring(r5, r4)     // Catch:{ all -> 0x0060 }
            int r4 = r4 + 1
            java.lang.String r3 = r3.substring(r4)     // Catch:{ all -> 0x0060 }
            java.lang.reflect.Field r4 = r0.getField(r5)     // Catch:{ all -> 0x0060 }
            if (r4 == 0) goto L_0x0042
            java.lang.Class r5 = r4.getType()     // Catch:{ all -> 0x0060 }
            java.lang.Class r6 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x0060 }
            if (r5 == r6) goto L_0x003a
            java.lang.Class<java.lang.Boolean> r6 = java.lang.Boolean.class
            if (r5 != r6) goto L_0x003e
        L_0x003a:
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ all -> 0x0060 }
        L_0x003e:
            com.google.api.client.util.FieldInfo.setFieldValue(r4, r1, r3)     // Catch:{ all -> 0x0060 }
            goto L_0x000d
        L_0x0042:
            java.lang.Class<com.google.api.client.util.GenericData> r4 = com.google.api.client.util.GenericData.class
            boolean r4 = r4.isAssignableFrom(r9)     // Catch:{ all -> 0x0060 }
            if (r4 == 0) goto L_0x0051
            r4 = r1
            com.google.api.client.util.GenericData r4 = (com.google.api.client.util.GenericData) r4     // Catch:{ all -> 0x0060 }
            r4.set(r5, r3)     // Catch:{ all -> 0x0060 }
            goto L_0x000d
        L_0x0051:
            java.lang.Class<java.util.Map> r4 = java.util.Map.class
            boolean r4 = r4.isAssignableFrom(r9)     // Catch:{ all -> 0x0060 }
            if (r4 == 0) goto L_0x000d
            r4 = r1
            java.util.Map r4 = (java.util.Map) r4     // Catch:{ all -> 0x0060 }
            r4.put(r5, r3)     // Catch:{ all -> 0x0060 }
            goto L_0x000d
        L_0x0060:
            r9 = move-exception
            r8.close()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.googleapis.auth.clientlogin.AuthKeyValueParser.parseAndClose(java.io.Reader, java.lang.Class):java.lang.Object");
    }

    public Object parseAndClose(Reader reader, Type type) {
        throw new UnsupportedOperationException("Type-based parsing is not yet supported -- use Class<T> instead");
    }
}
