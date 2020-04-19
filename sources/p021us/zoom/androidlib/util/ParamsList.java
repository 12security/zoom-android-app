package p021us.zoom.androidlib.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

/* renamed from: us.zoom.androidlib.util.ParamsList */
public class ParamsList {
    public static final String DEFAULT_SPLITER = ";";
    private static final String TAG = "ParamsList";
    private HashMap<String, String> mMap = new HashMap<>();

    public static ParamsList parseFromString(String str) {
        return parseFromString(str, ";");
    }

    public static ParamsList parseFromString(String str, String str2) {
        ParamsList paramsList = new ParamsList();
        if (str == null || str.length() == 0) {
            return paramsList;
        }
        for (String trim : str.split(str2)) {
            String trim2 = trim.trim();
            if (trim2.length() != 0) {
                String[] split = trim2.split("=");
                if (split.length == 2) {
                    String trim3 = split[0].trim();
                    String str3 = null;
                    try {
                        str3 = URLDecoder.decode(split[1].trim(), "UTF-8");
                    } catch (UnsupportedEncodingException unused) {
                    }
                    if (str3 != null) {
                        paramsList.mMap.put(trim3, str3);
                    }
                }
            }
        }
        return paramsList;
    }

    @NonNull
    public String toString() {
        return serializeToString();
    }

    public String serializeToString() {
        return serializeToString(";");
    }

    public String serializeToString(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("param spliter cannot be null or empty");
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Entry entry : this.mMap.entrySet()) {
            String str2 = (String) entry.getKey();
            String str3 = "";
            try {
                str3 = URLEncoder.encode((String) entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException unused) {
            }
            if (!z) {
                sb.append(str);
            }
            sb.append(str2);
            sb.append('=');
            sb.append(str3);
            z = false;
        }
        return sb.toString();
    }

    public void remove(String str) {
        if (str != null) {
            this.mMap.remove(str);
        }
    }

    public void putString(String str, String str2) {
        if (str2 == null) {
            this.mMap.remove(str);
        } else {
            this.mMap.put(str, str2);
        }
    }

    public void putInt(String str, int i) {
        if (str != null) {
            this.mMap.put(str, String.valueOf(i));
        }
    }

    public void putLong(String str, long j) {
        if (str != null) {
            this.mMap.put(str, String.valueOf(j));
        }
    }

    public void putBoolean(String str, boolean z) {
        if (str != null) {
            this.mMap.put(str, String.valueOf(z));
        }
    }

    @Nullable
    public String getString(String str, String str2) {
        if (str == null) {
            return null;
        }
        String str3 = (String) this.mMap.get(str);
        return str3 == null ? str2 : str3;
    }

    public int getInt(String str, int i) {
        if (str == null) {
            return i;
        }
        String str2 = (String) this.mMap.get(str);
        if (str2 == null) {
            return i;
        }
        try {
            i = Integer.parseInt(str2);
        } catch (Exception unused) {
        }
        return i;
    }

    public long getLong(String str, long j) {
        if (str == null) {
            return j;
        }
        String str2 = (String) this.mMap.get(str);
        if (str2 == null) {
            return j;
        }
        try {
            j = Long.parseLong(str2);
        } catch (Exception unused) {
        }
        return j;
    }

    public boolean getBoolean(String str, boolean z) {
        if (str == null) {
            return z;
        }
        String str2 = (String) this.mMap.get(str);
        if (str2 == null) {
            return z;
        }
        try {
            z = Boolean.parseBoolean(str2);
        } catch (Exception unused) {
        }
        return z;
    }
}
