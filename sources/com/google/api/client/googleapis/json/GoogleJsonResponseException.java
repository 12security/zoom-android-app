package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpResponseException.Builder;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Preconditions;
import java.io.IOException;

public class GoogleJsonResponseException extends HttpResponseException {
    private static final long serialVersionUID = 409811126989994864L;
    private final transient GoogleJsonError details;

    public GoogleJsonResponseException(Builder builder, GoogleJsonError googleJsonError) {
        super(builder);
        this.details = googleJsonError;
    }

    public final GoogleJsonError getDetails() {
        return this.details;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0078 A[SYNTHETIC, Splitter:B:30:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x007e A[Catch:{ IOException -> 0x007c }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0096 A[SYNTHETIC, Splitter:B:47:0x0096] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x009a A[Catch:{ IOException -> 0x00af }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00a5 A[Catch:{ IOException -> 0x00af }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00ab A[Catch:{ IOException -> 0x00af }] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00c7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.api.client.googleapis.json.GoogleJsonResponseException from(com.google.api.client.json.JsonFactory r5, com.google.api.client.http.HttpResponse r6) {
        /*
            com.google.api.client.http.HttpResponseException$Builder r0 = new com.google.api.client.http.HttpResponseException$Builder
            int r1 = r6.getStatusCode()
            java.lang.String r2 = r6.getStatusMessage()
            com.google.api.client.http.HttpHeaders r3 = r6.getHeaders()
            r0.<init>(r1, r2, r3)
            com.google.api.client.util.Preconditions.checkNotNull(r5)
            r1 = 0
            boolean r2 = r6.isSuccessStatusCode()     // Catch:{ IOException -> 0x00b8 }
            if (r2 != 0) goto L_0x00b3
            java.lang.String r2 = "application/json; charset=UTF-8"
            java.lang.String r3 = r6.getContentType()     // Catch:{ IOException -> 0x00b8 }
            boolean r2 = com.google.api.client.http.HttpMediaType.equalsIgnoreParameters(r2, r3)     // Catch:{ IOException -> 0x00b8 }
            if (r2 == 0) goto L_0x00b3
            java.io.InputStream r2 = r6.getContent()     // Catch:{ IOException -> 0x00b8 }
            if (r2 == 0) goto L_0x00b3
            java.io.InputStream r2 = r6.getContent()     // Catch:{ IOException -> 0x008e, all -> 0x008a }
            com.google.api.client.json.JsonParser r5 = r5.createJsonParser(r2)     // Catch:{ IOException -> 0x008e, all -> 0x008a }
            com.google.api.client.json.JsonToken r2 = r5.getCurrentToken()     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            if (r2 != 0) goto L_0x003f
            com.google.api.client.json.JsonToken r2 = r5.nextToken()     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
        L_0x003f:
            if (r2 == 0) goto L_0x0075
            java.lang.String r2 = "error"
            r5.skipToKey(r2)     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            com.google.api.client.json.JsonToken r2 = r5.getCurrentToken()     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            com.google.api.client.json.JsonToken r3 = com.google.api.client.json.JsonToken.VALUE_STRING     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            if (r2 != r3) goto L_0x0053
            java.lang.String r2 = r5.getText()     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            goto L_0x0076
        L_0x0053:
            com.google.api.client.json.JsonToken r2 = r5.getCurrentToken()     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            com.google.api.client.json.JsonToken r3 = com.google.api.client.json.JsonToken.START_OBJECT     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            if (r2 != r3) goto L_0x0075
            java.lang.Class<com.google.api.client.googleapis.json.GoogleJsonError> r2 = com.google.api.client.googleapis.json.GoogleJsonError.class
            java.lang.Object r2 = r5.parseAndClose(r2)     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            com.google.api.client.googleapis.json.GoogleJsonError r2 = (com.google.api.client.googleapis.json.GoogleJsonError) r2     // Catch:{ IOException -> 0x0087, all -> 0x0084 }
            java.lang.String r1 = r2.toPrettyString()     // Catch:{ IOException -> 0x0070, all -> 0x006b }
            r4 = r2
            r2 = r1
            r1 = r4
            goto L_0x0076
        L_0x006b:
            r3 = move-exception
            r4 = r3
            r3 = r2
            r2 = r4
            goto L_0x00a3
        L_0x0070:
            r3 = move-exception
            r4 = r3
            r3 = r2
            r2 = r4
            goto L_0x0091
        L_0x0075:
            r2 = r1
        L_0x0076:
            if (r5 != 0) goto L_0x007e
            r6.ignore()     // Catch:{ IOException -> 0x007c }
            goto L_0x00bd
        L_0x007c:
            r5 = move-exception
            goto L_0x00ba
        L_0x007e:
            if (r1 != 0) goto L_0x00bd
            r5.close()     // Catch:{ IOException -> 0x007c }
            goto L_0x00bd
        L_0x0084:
            r2 = move-exception
            r3 = r1
            goto L_0x00a3
        L_0x0087:
            r2 = move-exception
            r3 = r1
            goto L_0x0091
        L_0x008a:
            r2 = move-exception
            r5 = r1
            r3 = r5
            goto L_0x00a3
        L_0x008e:
            r2 = move-exception
            r5 = r1
            r3 = r5
        L_0x0091:
            r2.printStackTrace()     // Catch:{ all -> 0x00a2 }
            if (r5 != 0) goto L_0x009a
            r6.ignore()     // Catch:{ IOException -> 0x00af }
            goto L_0x009f
        L_0x009a:
            if (r3 != 0) goto L_0x009f
            r5.close()     // Catch:{ IOException -> 0x00af }
        L_0x009f:
            r2 = r1
            r1 = r3
            goto L_0x00bd
        L_0x00a2:
            r2 = move-exception
        L_0x00a3:
            if (r5 == 0) goto L_0x00ab
            if (r3 != 0) goto L_0x00ae
            r5.close()     // Catch:{ IOException -> 0x00af }
            goto L_0x00ae
        L_0x00ab:
            r6.ignore()     // Catch:{ IOException -> 0x00af }
        L_0x00ae:
            throw r2     // Catch:{ IOException -> 0x00af }
        L_0x00af:
            r5 = move-exception
            r2 = r1
            r1 = r3
            goto L_0x00ba
        L_0x00b3:
            java.lang.String r2 = r6.parseAsString()     // Catch:{ IOException -> 0x00b8 }
            goto L_0x00bd
        L_0x00b8:
            r5 = move-exception
            r2 = r1
        L_0x00ba:
            r5.printStackTrace()
        L_0x00bd:
            java.lang.StringBuilder r5 = com.google.api.client.http.HttpResponseException.computeMessageBuffer(r6)
            boolean r6 = com.google.api.client.util.Strings.isNullOrEmpty(r2)
            if (r6 != 0) goto L_0x00d2
            java.lang.String r6 = com.google.api.client.util.StringUtils.LINE_SEPARATOR
            r5.append(r6)
            r5.append(r2)
            r0.setContent(r2)
        L_0x00d2:
            java.lang.String r5 = r5.toString()
            r0.setMessage(r5)
            com.google.api.client.googleapis.json.GoogleJsonResponseException r5 = new com.google.api.client.googleapis.json.GoogleJsonResponseException
            r5.<init>(r0, r1)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.googleapis.json.GoogleJsonResponseException.from(com.google.api.client.json.JsonFactory, com.google.api.client.http.HttpResponse):com.google.api.client.googleapis.json.GoogleJsonResponseException");
    }

    public static HttpResponse execute(JsonFactory jsonFactory, HttpRequest httpRequest) throws GoogleJsonResponseException, IOException {
        Preconditions.checkNotNull(jsonFactory);
        boolean throwExceptionOnExecuteError = httpRequest.getThrowExceptionOnExecuteError();
        if (throwExceptionOnExecuteError) {
            httpRequest.setThrowExceptionOnExecuteError(false);
        }
        HttpResponse execute = httpRequest.execute();
        httpRequest.setThrowExceptionOnExecuteError(throwExceptionOnExecuteError);
        if (!throwExceptionOnExecuteError || execute.isSuccessStatusCode()) {
            return execute;
        }
        throw from(jsonFactory, execute);
    }
}
