package com.google.api.client.auth.oauth2;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpResponseException.Builder;

public class TokenResponseException extends HttpResponseException {
    private static final long serialVersionUID = 4020689092957439244L;
    private final transient TokenErrorResponse details;

    TokenResponseException(Builder builder, TokenErrorResponse tokenErrorResponse) {
        super(builder);
        this.details = tokenErrorResponse;
    }

    public final TokenErrorResponse getDetails() {
        return this.details;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0063  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.api.client.auth.oauth2.TokenResponseException from(com.google.api.client.json.JsonFactory r6, com.google.api.client.http.HttpResponse r7) {
        /*
            com.google.api.client.http.HttpResponseException$Builder r0 = new com.google.api.client.http.HttpResponseException$Builder
            int r1 = r7.getStatusCode()
            java.lang.String r2 = r7.getStatusMessage()
            com.google.api.client.http.HttpHeaders r3 = r7.getHeaders()
            r0.<init>(r1, r2, r3)
            com.google.api.client.util.Preconditions.checkNotNull(r6)
            java.lang.String r1 = r7.getContentType()
            r2 = 0
            boolean r3 = r7.isSuccessStatusCode()     // Catch:{ IOException -> 0x0054 }
            if (r3 != 0) goto L_0x004c
            if (r1 == 0) goto L_0x004c
            java.io.InputStream r3 = r7.getContent()     // Catch:{ IOException -> 0x0054 }
            if (r3 == 0) goto L_0x004c
            java.lang.String r3 = "application/json; charset=UTF-8"
            boolean r1 = com.google.api.client.http.HttpMediaType.equalsIgnoreParameters(r3, r1)     // Catch:{ IOException -> 0x0054 }
            if (r1 == 0) goto L_0x004c
            com.google.api.client.json.JsonObjectParser r1 = new com.google.api.client.json.JsonObjectParser     // Catch:{ IOException -> 0x0054 }
            r1.<init>(r6)     // Catch:{ IOException -> 0x0054 }
            java.io.InputStream r6 = r7.getContent()     // Catch:{ IOException -> 0x0054 }
            java.nio.charset.Charset r3 = r7.getContentCharset()     // Catch:{ IOException -> 0x0054 }
            java.lang.Class<com.google.api.client.auth.oauth2.TokenErrorResponse> r4 = com.google.api.client.auth.oauth2.TokenErrorResponse.class
            java.lang.Object r6 = r1.parseAndClose(r6, r3, r4)     // Catch:{ IOException -> 0x0054 }
            com.google.api.client.auth.oauth2.TokenErrorResponse r6 = (com.google.api.client.auth.oauth2.TokenErrorResponse) r6     // Catch:{ IOException -> 0x0054 }
            java.lang.String r1 = r6.toPrettyString()     // Catch:{ IOException -> 0x004a }
            r2 = r1
            goto L_0x0059
        L_0x004a:
            r1 = move-exception
            goto L_0x0056
        L_0x004c:
            java.lang.String r6 = r7.parseAsString()     // Catch:{ IOException -> 0x0054 }
            r5 = r2
            r2 = r6
            r6 = r5
            goto L_0x0059
        L_0x0054:
            r1 = move-exception
            r6 = r2
        L_0x0056:
            r1.printStackTrace()
        L_0x0059:
            java.lang.StringBuilder r7 = com.google.api.client.http.HttpResponseException.computeMessageBuffer(r7)
            boolean r1 = com.google.api.client.util.Strings.isNullOrEmpty(r2)
            if (r1 != 0) goto L_0x006e
            java.lang.String r1 = com.google.api.client.util.StringUtils.LINE_SEPARATOR
            r7.append(r1)
            r7.append(r2)
            r0.setContent(r2)
        L_0x006e:
            java.lang.String r7 = r7.toString()
            r0.setMessage(r7)
            com.google.api.client.auth.oauth2.TokenResponseException r7 = new com.google.api.client.auth.oauth2.TokenResponseException
            r7.<init>(r0, r6)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.auth.oauth2.TokenResponseException.from(com.google.api.client.json.JsonFactory, com.google.api.client.http.HttpResponse):com.google.api.client.auth.oauth2.TokenResponseException");
    }
}
