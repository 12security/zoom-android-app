package com.microsoft.aad.adal;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

class WebFingerMetadataRequestor extends AbstractMetadataRequestor<WebFingerMetadata, WebFingerMetadataRequestParameters> {
    private static final String TAG = "WebFingerMetadataRequestor";

    WebFingerMetadataRequestor() {
    }

    /* access modifiers changed from: 0000 */
    public WebFingerMetadata requestMetadata(WebFingerMetadataRequestParameters webFingerMetadataRequestParameters) throws AuthenticationException {
        URL domain = webFingerMetadataRequestParameters.getDomain();
        DRSMetadata drsMetadata = webFingerMetadataRequestParameters.getDrsMetadata();
        StringBuilder sb = new StringBuilder();
        sb.append("Auth endpoint: ");
        sb.append(domain.toString());
        Logger.m234i(TAG, "Validating authority for auth endpoint. ", sb.toString());
        try {
            HttpWebResponse sendGet = getWebrequestHandler().sendGet(buildWebFingerUrl(domain, drsMetadata), new HashMap());
            if (200 == sendGet.getStatusCode()) {
                return parseMetadata(sendGet);
            }
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE);
        } catch (IOException e) {
            throw new AuthenticationException(ADALError.IO_EXCEPTION, "Unexpected error", (Throwable) e);
        }
    }

    /* access modifiers changed from: 0000 */
    public WebFingerMetadata parseMetadata(HttpWebResponse httpWebResponse) throws AuthenticationException {
        Logger.m236v(TAG, "Parsing WebFinger response.");
        try {
            return (WebFingerMetadata) parser().fromJson(httpWebResponse.getBody(), WebFingerMetadata.class);
        } catch (JsonSyntaxException unused) {
            throw new AuthenticationException(ADALError.JSON_PARSE_ERROR);
        }
    }

    static URL buildWebFingerUrl(URL url, DRSMetadata dRSMetadata) throws MalformedURLException {
        URL url2 = new URL(dRSMetadata.getIdentityProviderService().getPassiveAuthEndpoint());
        StringBuilder sb = new StringBuilder("https://");
        sb.append(url2.getHost());
        sb.append("/.well-known/webfinger?resource=");
        sb.append(url.toString());
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("WebFinger URL: ");
        sb3.append(sb2);
        Logger.m234i(TAG, "Validator will use WebFinger URL. ", sb3.toString());
        return new URL(sb2);
    }
}
