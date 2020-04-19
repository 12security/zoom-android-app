package com.microsoft.aad.adal;

import android.util.Pair;
import java.util.List;
import java.util.Map;

final class UIEvent extends DefaultEvent {
    UIEvent(String str) {
        getEventList().add(Pair.create("Microsoft.ADAL.event_name", str));
    }

    /* access modifiers changed from: 0000 */
    public void setRedirectCount(Integer num) {
        setProperty("Microsoft.ADAL.redirect_count", num.toString());
    }

    /* access modifiers changed from: 0000 */
    public void setNTLM(boolean z) {
        setProperty("Microsoft.ADAL.ntlm", String.valueOf(z));
    }

    /* access modifiers changed from: 0000 */
    public void setUserCancel() {
        setProperty("Microsoft.ADAL.user_cancel", "true");
    }

    public void processEvent(Map<String, String> map) {
        List<Pair> eventList = getEventList();
        String str = (String) map.get("Microsoft.ADAL.ui_event_count");
        if (str == null) {
            map.put("Microsoft.ADAL.ui_event_count", "1");
        } else {
            map.put("Microsoft.ADAL.ui_event_count", Integer.toString(Integer.parseInt(str) + 1));
        }
        if (map.containsKey("Microsoft.ADAL.user_cancel")) {
            map.put("Microsoft.ADAL.user_cancel", "");
        }
        if (map.containsKey("Microsoft.ADAL.ntlm")) {
            map.put("Microsoft.ADAL.ntlm", "");
        }
        for (Pair pair : eventList) {
            String str2 = (String) pair.first;
            if (str2.equals("Microsoft.ADAL.user_cancel") || str2.equals("Microsoft.ADAL.ntlm")) {
                map.put(str2, pair.second);
            }
        }
    }
}
