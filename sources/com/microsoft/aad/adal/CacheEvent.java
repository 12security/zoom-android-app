package com.microsoft.aad.adal;

import android.util.Pair;
import java.util.List;
import java.util.Map;

final class CacheEvent extends DefaultEvent {
    private final String mEventName;

    CacheEvent(String str) {
        this.mEventName = str;
        setProperty("Microsoft.ADAL.event_name", str);
    }

    /* access modifiers changed from: 0000 */
    public void setSpeRing(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            setProperty("Microsoft.ADAL.spe_info", str.trim());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setTokenType(String str) {
        getEventList().add(Pair.create("Microsoft.ADAL.token_type", str));
    }

    /* access modifiers changed from: 0000 */
    public void setTokenTypeRT(boolean z) {
        setProperty("Microsoft.ADAL.is_rt", String.valueOf(z));
    }

    /* access modifiers changed from: 0000 */
    public void setTokenTypeMRRT(boolean z) {
        setProperty("Microsoft.ADAL.is_mrrt", String.valueOf(z));
    }

    /* access modifiers changed from: 0000 */
    public void setTokenTypeFRT(boolean z) {
        setProperty("Microsoft.ADAL.is_frt", String.valueOf(z));
    }

    public void processEvent(Map<String, String> map) {
        if (this.mEventName == "Microsoft.ADAL.token_cache_lookup") {
            List<Pair> eventList = getEventList();
            String str = (String) map.get("Microsoft.ADAL.cache_event_count");
            if (str == null) {
                map.put("Microsoft.ADAL.cache_event_count", "1");
            } else {
                map.put("Microsoft.ADAL.cache_event_count", Integer.toString(Integer.parseInt(str) + 1));
            }
            map.put("Microsoft.ADAL.is_frt", "");
            map.put("Microsoft.ADAL.is_mrrt", "");
            map.put("Microsoft.ADAL.is_rt", "");
            if (map.containsKey("Microsoft.ADAL.spe_info")) {
                map.remove("Microsoft.ADAL.spe_info");
            }
            for (Pair pair : eventList) {
                String str2 = (String) pair.first;
                if (str2.equals("Microsoft.ADAL.is_frt") || str2.equals("Microsoft.ADAL.is_rt") || str2.equals("Microsoft.ADAL.is_mrrt") || str2.equals("Microsoft.ADAL.spe_info")) {
                    map.put(str2, pair.second);
                }
            }
        }
    }
}
