package com.microsoft.aad.adal;

import android.util.Pair;
import java.util.List;
import java.util.Map;

final class BrokerEvent extends DefaultEvent {
    BrokerEvent(String str) {
        setProperty("Microsoft.ADAL.event_name", str);
    }

    /* access modifiers changed from: 0000 */
    public void setBrokerAppName(String str) {
        setProperty("Microsoft.ADAL.broker_app", str);
    }

    /* access modifiers changed from: 0000 */
    public void setBrokerAppVersion(String str) {
        setProperty("Microsoft.ADAL.broker_version", str);
    }

    /* access modifiers changed from: 0000 */
    public void setBrokerAccountServerStartsBinding() {
        setProperty("Microsoft.ADAL.broker_account_service_starts_binding", Boolean.toString(true));
    }

    /* access modifiers changed from: 0000 */
    public void setBrokerAccountServiceBindingSucceed(boolean z) {
        setProperty("Microsoft.ADAL.broker_account_service_binding_succeed", Boolean.toString(z));
    }

    /* access modifiers changed from: 0000 */
    public void setBrokerAccountServiceConnected() {
        setProperty("Microsoft.ADAL.broker_account_service_connected", Boolean.toString(true));
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
        List<Pair> eventList = getEventList();
        map.put("Microsoft.ADAL.broker_app_used", Boolean.toString(true));
        for (Pair pair : eventList) {
            if (!((String) pair.first).equals("Microsoft.ADAL.event_name")) {
                map.put(pair.first, pair.second);
            }
        }
    }
}
