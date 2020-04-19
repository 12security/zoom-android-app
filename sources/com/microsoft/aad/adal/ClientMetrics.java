package com.microsoft.aad.adal;

import android.text.TextUtils;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

enum ClientMetrics {
    INSTANCE;
    
    private static final String CLIENT_METRICS_HEADER_LAST_ENDPOINT = "x-client-last-endpoint";
    private static final String CLIENT_METRICS_HEADER_LAST_ERROR = "x-client-last-error";
    private static final String CLIENT_METRICS_HEADER_LAST_REQUEST = "x-client-last-request";
    private static final String CLIENT_METRICS_HEADER_LAST_RESPONSE_TIME = "x-client-last-response-time";
    private boolean mIsPending;
    private UUID mLastCorrelationId;
    private String mLastEndpoint;
    private String mLastError;
    private long mLastResponseTime;
    private URL mQueryUrl;
    private long mStartTimeMillis;

    public void beginClientMetricsRecord(URL url, UUID uuid, Map<String, String> map) {
        if (UrlExtensions.isADFSAuthority(url)) {
            this.mLastCorrelationId = null;
            return;
        }
        if (this.mIsPending) {
            addClientMetricsHeadersToRequest(map);
        }
        this.mStartTimeMillis = System.currentTimeMillis();
        this.mQueryUrl = url;
        this.mLastCorrelationId = uuid;
        this.mLastError = "";
        this.mIsPending = false;
    }

    public void endClientMetricsRecord(String str, UUID uuid) {
        if (!UrlExtensions.isADFSAuthority(this.mQueryUrl)) {
            this.mLastEndpoint = str;
            if (this.mStartTimeMillis != 0) {
                this.mLastResponseTime = System.currentTimeMillis() - this.mStartTimeMillis;
                this.mLastCorrelationId = uuid;
            }
            this.mIsPending = true;
        }
    }

    public void setLastError(String str) {
        this.mLastError = str == null ? "" : str.replaceAll("[\\[\\]]", "");
    }

    public void setLastErrorCodes(String[] strArr) {
        this.mLastError = strArr == null ? null : TextUtils.join(PreferencesConstants.COOKIE_DELIMITER, strArr);
    }

    private void addClientMetricsHeadersToRequest(Map<String, String> map) {
        String str = this.mLastError;
        if (str != null) {
            map.put(CLIENT_METRICS_HEADER_LAST_ERROR, str);
        }
        UUID uuid = this.mLastCorrelationId;
        if (uuid != null) {
            map.put(CLIENT_METRICS_HEADER_LAST_REQUEST, uuid.toString());
        }
        map.put(CLIENT_METRICS_HEADER_LAST_RESPONSE_TIME, Long.toString(this.mLastResponseTime));
        map.put(CLIENT_METRICS_HEADER_LAST_ENDPOINT, this.mLastEndpoint);
    }
}
