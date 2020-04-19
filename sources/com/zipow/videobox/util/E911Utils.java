package com.zipow.videobox.util;

import com.zipow.videobox.sip.server.CmmSIPCallManager;

public class E911Utils {
    public static final String[] E911_NUMBERS = {"+1911", "+1933", "+44112", "+44999", "+61000", "+61106", "+61112", "+86110", "+86119", "+86120", "+91100", "+91102", "+91108", "+91101", "+91112", "+51911", "+57123"};

    public static boolean isE911Number(String str) {
        if (!str.startsWith("+")) {
            int countryCode = CmmSIPCallManager.getInstance().getCountryCode();
            StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(countryCode);
            sb.append(str);
            str = sb.toString();
        }
        for (String equals : E911_NUMBERS) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
