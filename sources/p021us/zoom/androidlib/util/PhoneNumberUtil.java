package p021us.zoom.androidlib.util;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* renamed from: us.zoom.androidlib.util.PhoneNumberUtil */
public class PhoneNumberUtil {
    public static String formatNumber(Context context, String str) {
        return formatNumber(str, CountryCodeUtil.isoCountryCode2PhoneCountryCode(CountryCodeUtil.getIsoCountryCode(context)));
    }

    public static String formatDisplayNumber(String str, String str2) {
        if (StringUtil.isEmptyOrNull(str2) || StringUtil.isEmptyOrNull(str2)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(str2);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(str);
        return sb.toString();
    }

    public static String formatNumber(String str, String str2) {
        int i;
        String str3;
        if (str2 == null) {
            str2 = "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char charAt = str.charAt(i2);
            if (charAt >= '0' && charAt <= '9') {
                sb.append(charAt);
            } else if (charAt != '+' || sb.length() != 0) {
                if (charAt == ',' || charAt == ';') {
                    break;
                }
            } else {
                sb.append(charAt);
            }
        }
        String sb2 = sb.toString();
        if (sb2.startsWith("+")) {
            return trimZeroAfterCountryCode(sb2);
        }
        if (sb2.startsWith("000")) {
            return sb2;
        }
        if (str2.equals("65") && sb2.startsWith("0011")) {
            i = 4;
            z = true;
        } else if (str2.equals("7") && sb2.startsWith("810")) {
            i = 3;
            z = true;
        } else if (str2.equals("234") && sb2.startsWith("009")) {
            i = 3;
            z = true;
        } else if (str2.equals("1") && sb2.startsWith("011")) {
            i = 3;
            z = true;
        } else if (str2.equals("47") && sb2.startsWith("095")) {
            i = 3;
            z = true;
        } else if (str2.equals("886") && sb2.startsWith("002")) {
            i = 3;
            z = true;
        } else if (str2.equals("81") && sb2.startsWith("010")) {
            i = 3;
            z = true;
        } else if (str2.equals("82") && (sb2.startsWith("001") || sb2.startsWith("002"))) {
            i = 3;
            z = true;
        } else if (str2.equals("381") && sb2.startsWith("99")) {
            i = 2;
            z = true;
        } else if (str2.equals("237") && sb2.startsWith("11")) {
            i = 2;
            z = true;
        } else if (str2.equals("27") && sb2.startsWith("09")) {
            i = 2;
            z = true;
        } else if (str2.equals("57") && sb2.startsWith("60")) {
            i = 2;
            z = true;
        } else if (str2.equals("679") && sb2.startsWith("05")) {
            i = 2;
            z = true;
        } else if (str2.equals("673") && sb2.startsWith("01")) {
            i = 2;
            z = true;
        } else if (sb2.startsWith("00")) {
            i = 2;
            z = true;
        } else {
            i = 0;
        }
        if (z) {
            String substring = sb2.substring(i);
            if (substring.startsWith("0")) {
                substring = substring.substring(1);
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("+");
            sb3.append(substring);
            return trimZeroAfterCountryCode(sb3.toString());
        } else if (StringUtil.isEmptyOrNull(str2)) {
            return sb2;
        } else {
            if (sb2.startsWith("0")) {
                if (str2.equals("39") || str2.equals("378")) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("+");
                    sb4.append(str2);
                    sb4.append(sb2);
                    str3 = sb4.toString();
                } else {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("+");
                    sb5.append(str2);
                    sb5.append(sb2.substring(1));
                    str3 = sb5.toString();
                }
                return str3;
            }
            if ("39".equals(str2)) {
                if (sb2.startsWith("3939")) {
                    sb2 = sb2.substring(2);
                }
            } else if ("48".equals(str2)) {
                if (sb2.startsWith("4848")) {
                    sb2 = sb2.substring(2);
                }
            } else if ("46".equals(str2)) {
                if (sb2.startsWith("4646")) {
                    sb2 = sb2.substring(2);
                }
            } else if ("41".equals(str2)) {
                if (sb2.startsWith("4141")) {
                    sb2 = sb2.substring(2);
                }
            } else if ("36".equals(str2)) {
                if (sb2.startsWith("3636")) {
                    sb2 = sb2.substring(2);
                }
            } else if ("43".equals(str2)) {
                if (sb2.startsWith("43") && !sb2.startsWith("4352")) {
                    sb2 = sb2.substring(2);
                }
                if (sb2.startsWith("0")) {
                    sb2 = sb2.substring(1);
                }
            } else if ("91".equals(str2)) {
                if (sb2.startsWith("9191")) {
                    sb2 = sb2.substring(2);
                }
            } else if ("63".equals(str2)) {
                if (sb2.startsWith("6363")) {
                    sb2 = sb2.substring(2);
                }
            } else if ("880".equals(str2)) {
                if (sb2.startsWith("880880")) {
                    sb2 = sb2.substring(3);
                }
            } else if ("7".equals(str2)) {
                if (sb2.startsWith("7") && !sb2.startsWith("71") && !sb2.startsWith("72") && !sb2.startsWith("73")) {
                    sb2 = sb2.substring(1);
                }
                if (sb2.startsWith("0")) {
                    sb2 = sb2.substring(1);
                }
            } else if (sb2.startsWith(str2)) {
                sb2 = sb2.substring(str2.length());
                if (!str2.equals("378") && sb2.startsWith("0")) {
                    sb2 = sb2.substring(1);
                }
            }
            StringBuilder sb6 = new StringBuilder();
            sb6.append('+');
            sb6.append(str2);
            sb6.append(sb2);
            return sb6.toString();
        }
    }

    @Nullable
    public static String getCountryCodeFromFormatedPhoneNumber(String str) {
        if (str == null || !str.startsWith("+")) {
            return null;
        }
        String substring = str.substring(1);
        for (Object[] objArr : CountryCodeUtil.countryCodeTable) {
            String valueOf = String.valueOf(objArr[1]);
            if (substring.startsWith(valueOf)) {
                return valueOf;
            }
        }
        return null;
    }

    private static String trimZeroAfterCountryCode(String str) {
        String countryCodeFromFormatedPhoneNumber = getCountryCodeFromFormatedPhoneNumber(str);
        if (countryCodeFromFormatedPhoneNumber == null || countryCodeFromFormatedPhoneNumber.length() == 0) {
            return str;
        }
        String substring = str.substring(countryCodeFromFormatedPhoneNumber.length() + 1);
        if (substring.startsWith("0") && !countryCodeFromFormatedPhoneNumber.equals("39") && !countryCodeFromFormatedPhoneNumber.equals("378")) {
            substring = substring.substring(1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(countryCodeFromFormatedPhoneNumber);
        sb.append(substring);
        return sb.toString();
    }

    @Nullable
    public static String formatDisplayPhoneNumber(String str, String str2) {
        String str3;
        if (StringUtil.isEmptyOrNull(str2)) {
            return null;
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(str);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(str2);
            str3 = sb.toString();
        } else {
            str3 = str2;
        }
        if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            int length = str2.length();
            if (str.equals("1") && (length == 10 || length == 11)) {
                String str4 = "+1 (%s) %s-%s";
                Object[] objArr = new Object[3];
                objArr[0] = str2.substring(0, 3);
                objArr[1] = length == 11 ? str2.substring(3, 7) : str2.substring(3, 6);
                objArr[2] = length == 11 ? str2.substring(7) : str2.substring(6);
                str3 = String.format(str4, objArr);
            }
        }
        return str3;
    }

    @Nullable
    public static String formatDisplayPhoneNumber(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String countryCodeFromFormatedPhoneNumber = getCountryCodeFromFormatedPhoneNumber(str);
        if (TextUtils.isEmpty(countryCodeFromFormatedPhoneNumber)) {
            return str;
        }
        int length = countryCodeFromFormatedPhoneNumber.length();
        if (str.startsWith("+")) {
            length++;
        }
        return formatDisplayPhoneNumber(countryCodeFromFormatedPhoneNumber, str.substring(length));
    }

    public static String getPhoneNumber(@NonNull String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                sb.append(charAt);
            } else if (charAt != '+' || sb.length() != 0) {
                if (charAt == ',' || charAt == ';') {
                    break;
                }
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }
}
