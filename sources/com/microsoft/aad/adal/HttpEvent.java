package com.microsoft.aad.adal;

import android.util.Pair;
import java.net.URL;
import java.util.Map;

final class HttpEvent extends DefaultEvent {
    private static final String TAG = "HttpEvent";

    HttpEvent(String str) {
        getEventList().add(Pair.create("Microsoft.ADAL.event_name", str));
    }

    /* access modifiers changed from: 0000 */
    public void setUserAgent(String str) {
        setProperty("Microsoft.ADAL.user_agent", str);
    }

    /* access modifiers changed from: 0000 */
    public void setMethod(String str) {
        setProperty("Microsoft.ADAL.method", str);
    }

    /* access modifiers changed from: 0000 */
    public void setQueryParameters(String str) {
        setProperty("Microsoft.ADAL.query_params", str);
    }

    /* access modifiers changed from: 0000 */
    public void setResponseCode(int i) {
        setProperty("Microsoft.ADAL.response_code", String.valueOf(i));
    }

    /* access modifiers changed from: 0000 */
    public void setApiVersion(String str) {
        setProperty("Microsoft.ADAL.api_version", str);
    }

    /* access modifiers changed from: 0000 */
    public void setHttpPath(URL url) {
        String authority = url.getAuthority();
        if (Discovery.getValidHosts().contains(authority)) {
            String[] split = url.getPath().split("/");
            StringBuilder sb = new StringBuilder();
            sb.append(url.getProtocol());
            sb.append("://");
            sb.append(authority);
            sb.append("/");
            for (int i = 2; i < split.length; i++) {
                sb.append(split[i]);
                sb.append("/");
            }
            setProperty("Microsoft.ADAL.http_path", sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setOauthErrorCode(String str) {
        setProperty("Microsoft.ADAL.oauth_error_code", str);
    }

    /* access modifiers changed from: 0000 */
    public void setRequestIdHeader(String str) {
        setProperty("Microsoft.ADAL.x_ms_request_id", str);
    }

    /* access modifiers changed from: 0000 */
    public void setXMsCliTelemData(CliTelemInfo cliTelemInfo) {
        if (cliTelemInfo != null) {
            setServerErrorCode(cliTelemInfo.getServerErrorCode());
            setServerSubErrorCode(cliTelemInfo.getServerSubErrorCode());
            setRefreshTokenAge(cliTelemInfo.getRefreshTokenAge());
            setSpeRing(cliTelemInfo.getSpeRing());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setServerErrorCode(String str) {
        if (!StringExtensions.isNullOrBlank(str) && !str.equals("0")) {
            setProperty("Microsoft.ADAL.server_error_code", str.trim());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setServerSubErrorCode(String str) {
        if (!StringExtensions.isNullOrBlank(str) && !str.equals("0")) {
            setProperty("Microsoft.ADAL.server_sub_error_code", str.trim());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setRefreshTokenAge(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            setProperty("Microsoft.ADAL.rt_age", str.trim());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setSpeRing(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            setProperty("Microsoft.ADAL.spe_info", str.trim());
        }
    }

    public void processEvent(Map<String, String> map) {
        String str = (String) map.get("Microsoft.ADAL.http_event_count");
        if (str == null) {
            map.put("Microsoft.ADAL.http_event_count", "1");
        } else {
            map.put("Microsoft.ADAL.http_event_count", Integer.toString(Integer.parseInt(str) + 1));
        }
        if (map.containsKey("Microsoft.ADAL.response_code")) {
            map.put("Microsoft.ADAL.response_code", "");
        }
        if (map.containsKey("Microsoft.ADAL.oauth_error_code")) {
            map.put("Microsoft.ADAL.oauth_error_code", "");
        }
        if (map.containsKey("Microsoft.ADAL.http_path")) {
            map.put("Microsoft.ADAL.http_path", "");
        }
        if (map.containsKey("Microsoft.ADAL.x_ms_request_id")) {
            map.put("Microsoft.ADAL.x_ms_request_id", "");
        }
        if (map.containsKey("Microsoft.ADAL.server_error_code")) {
            map.remove("Microsoft.ADAL.server_error_code");
        }
        if (map.containsKey("Microsoft.ADAL.server_sub_error_code")) {
            map.remove("Microsoft.ADAL.server_sub_error_code");
        }
        if (map.containsKey("Microsoft.ADAL.rt_age")) {
            map.remove("Microsoft.ADAL.rt_age");
        }
        if (map.containsKey("Microsoft.ADAL.spe_info")) {
            map.remove("Microsoft.ADAL.spe_info");
        }
        for (Pair pair : getEventList()) {
            String str2 = (String) pair.first;
            if (str2.equals("Microsoft.ADAL.response_code") || str2.equals("Microsoft.ADAL.x_ms_request_id") || str2.equals("Microsoft.ADAL.oauth_error_code") || str2.equals("Microsoft.ADAL.http_path") || str2.equals("Microsoft.ADAL.server_error_code") || str2.equals("Microsoft.ADAL.server_sub_error_code") || str2.equals("Microsoft.ADAL.rt_age") || str2.equals("Microsoft.ADAL.spe_info")) {
                map.put(str2, pair.second);
            }
        }
    }
}
