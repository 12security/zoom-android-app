package com.microsoft.aad.adal;

import com.google.gson.JsonSyntaxException;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

final class DRSMetadataRequestor extends AbstractMetadataRequestor<DRSMetadata, String> {
    private static final String CLOUD_RESOLVER_DOMAIN = "windows.net/";
    private static final String DRS_URL_PREFIX = "https://enterpriseregistration.";
    private static final String TAG = "DRSMetadataRequestor";

    enum Type {
        ON_PREM,
        CLOUD
    }

    DRSMetadataRequestor() {
    }

    /* access modifiers changed from: 0000 */
    public DRSMetadata requestMetadata(String str) throws AuthenticationException {
        try {
            return requestOnPrem(str);
        } catch (UnknownHostException unused) {
            return requestCloud(str);
        }
    }

    private DRSMetadata requestOnPrem(String str) throws UnknownHostException, AuthenticationException {
        Logger.m236v(TAG, "Requesting DRS discovery (on-prem)");
        return requestDrsDiscoveryInternal(Type.ON_PREM, str);
    }

    private DRSMetadata requestCloud(String str) throws AuthenticationException {
        Logger.m236v(TAG, "Requesting DRS discovery (cloud)");
        try {
            return requestDrsDiscoveryInternal(Type.CLOUD, str);
        } catch (UnknownHostException unused) {
            throw new AuthenticationException(ADALError.DRS_DISCOVERY_FAILED_UNKNOWN_HOST);
        }
    }

    private DRSMetadata requestDrsDiscoveryInternal(Type type, String str) throws AuthenticationException, UnknownHostException {
        try {
            URL url = new URL(buildRequestUrlByType(type, str));
            HashMap hashMap = new HashMap();
            hashMap.put("Accept", WebRequestHandler.HEADER_ACCEPT_JSON);
            if (getCorrelationId() != null) {
                hashMap.put(AAD.CLIENT_REQUEST_ID, getCorrelationId().toString());
            }
            try {
                HttpWebResponse sendGet = getWebrequestHandler().sendGet(url, hashMap);
                int statusCode = sendGet.getStatusCode();
                if (200 == statusCode) {
                    return parseMetadata(sendGet);
                }
                ADALError aDALError = ADALError.DRS_FAILED_SERVER_ERROR;
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected error code: [");
                sb.append(statusCode);
                sb.append("]");
                throw new AuthenticationException(aDALError, sb.toString());
            } catch (UnknownHostException e) {
                throw e;
            } catch (IOException unused) {
                throw new AuthenticationException(ADALError.IO_EXCEPTION);
            }
        } catch (MalformedURLException unused2) {
            throw new AuthenticationException(ADALError.DRS_METADATA_URL_INVALID);
        }
    }

    /* access modifiers changed from: 0000 */
    public DRSMetadata parseMetadata(HttpWebResponse httpWebResponse) throws AuthenticationException {
        Logger.m236v(TAG, "Parsing DRS metadata response");
        try {
            return (DRSMetadata) parser().fromJson(httpWebResponse.getBody(), DRSMetadata.class);
        } catch (JsonSyntaxException unused) {
            throw new AuthenticationException(ADALError.JSON_PARSE_ERROR);
        }
    }

    /* access modifiers changed from: 0000 */
    public String buildRequestUrlByType(Type type, String str) {
        StringBuilder sb = new StringBuilder(DRS_URL_PREFIX);
        if (Type.CLOUD == type) {
            sb.append(CLOUD_RESOLVER_DOMAIN);
            sb.append(str);
        } else if (Type.ON_PREM == type) {
            sb.append(str);
        }
        sb.append("/enrollmentserver/contract?api-version=1.0");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("URL: ");
        sb3.append(sb2);
        Logger.m237v(TAG, "Request will use DRS url. ", sb3.toString(), null);
        return sb2;
    }
}
