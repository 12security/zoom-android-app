package com.zipow.videobox.util;

import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZMActionMsgUtil {
    public static final String KEY_EVENT = "text";
    public static final String KEY_EVENT_ID = "eventid";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_METHOD = "method";
    public static final String KEY_TYPE = "type";
    public static final String KEY_URL = "url";
    public static final String TYPE_MESSAGE = "1";
    public static final String TYPE_METHOD_GET = "GET";
    public static final String TYPE_METHOD_POST = "POST";
    public static final String TYPE_SLASH_COMMAND = "2";

    public enum ActionType {
        SENDMSG("sendMsg", "type", "message"),
        COPYMSG("copyMsg", "type", "message"),
        SENDHTTPMSG("sendHttpMsg", "method", "url");
        
        @NonNull
        private String dec;
        @NonNull
        private List<String> keys;

        @NonNull
        public String toString() {
            return this.dec;
        }

        @NonNull
        public List<String> getKeys() {
            return this.keys;
        }

        private ActionType(@NonNull String str, String... strArr) {
            this.keys = new ArrayList();
            this.dec = str;
            if (strArr.length > 0) {
                this.keys.addAll(Arrays.asList(strArr));
            }
        }

        public static ActionType parseType(@NonNull String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (str.startsWith(SENDMSG.toString())) {
                return SENDMSG;
            }
            if (str.startsWith(COPYMSG.toString())) {
                return COPYMSG;
            }
            if (str.startsWith(SENDHTTPMSG.toString())) {
                return SENDHTTPMSG;
            }
            return null;
        }
    }

    @Nullable
    public static String sendHttpMsg(Map<String, String> map) {
        return sendHttpMsg(map, false);
    }

    @Nullable
    public static String sendHttpMsg(@Nullable Map<String, String> map, boolean z) {
        if (map != null && !map.isEmpty() && map.containsKey("method")) {
            String str = (String) map.get("method");
            String str2 = (String) map.get("url");
            if (TextUtils.isEmpty(str2)) {
                return null;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return null;
            }
            if ("GET".equalsIgnoreCase(str)) {
                return zoomMessenger.sendGetHttp(str2);
            }
            if ("POST".equalsIgnoreCase(str)) {
                Uri parse = Uri.parse(str2);
                if (parse == null) {
                    return null;
                }
                Set<String> queryParameterNames = parse.getQueryParameterNames();
                HashMap hashMap = new HashMap();
                for (String str3 : queryParameterNames) {
                    hashMap.put(str3, parse.getQueryParameter(str3));
                }
                if (z) {
                    if (map.get(KEY_EVENT) != null) {
                        hashMap.put(KEY_EVENT, map.get(KEY_EVENT));
                    }
                    if (map.get(KEY_EVENT_ID) != null) {
                        hashMap.put(KEY_EVENT_ID, map.get(KEY_EVENT_ID));
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append(parse.getScheme());
                sb.append("://");
                sb.append(parse.getAuthority());
                sb.append(parse.getPath());
                return zoomMessenger.sendPostHttp(sb.toString(), hashMap);
            }
        }
        return null;
    }

    @Nullable
    public static Map<String, String> parseActionMsgParams(@NonNull String str) {
        ActionType parseType = ActionType.parseType(str);
        if (parseType != null) {
            switch (parseType) {
                case SENDMSG:
                case COPYMSG:
                    if (str.length() > 9) {
                        String substring = str.substring(8, str.length() - 1);
                        List keys = parseType.getKeys();
                        if (keys.isEmpty()) {
                            return parseActionMsgParams(substring);
                        }
                        return parseActionMsgParams(substring, (String[]) keys.toArray(new String[keys.size()]));
                    }
                    break;
                case SENDHTTPMSG:
                    if (str.length() > 13) {
                        String substring2 = str.substring(12, str.length() - 1);
                        List keys2 = parseType.getKeys();
                        if (keys2.isEmpty()) {
                            return parseActionMsgParams(substring2);
                        }
                        return parseActionMsgParams(substring2, (String[]) keys2.toArray(new String[keys2.size()]));
                    }
                    break;
            }
        }
        return null;
    }

    @NonNull
    public static Map<String, String> parseActionMsgParams(@Nullable String str, @Nullable String... strArr) {
        HashMap hashMap = new HashMap();
        if (TextUtils.isEmpty(str)) {
            return hashMap;
        }
        String[] split = str.split(PreferencesConstants.COOKIE_DELIMITER);
        if (split.length <= 0) {
            return hashMap;
        }
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
            if (split[i].startsWith("'") || split[i].startsWith("\"")) {
                if (split[i].length() <= 1 || (!split[i].endsWith("'") && !split[i].endsWith("\""))) {
                    sb.append(split[i]);
                } else {
                    arrayList.add(split[i]);
                }
            } else if (split[i].endsWith("'") || split[i].endsWith("\"")) {
                sb.append(PreferencesConstants.COOKIE_DELIMITER);
                sb.append(split[i]);
                arrayList.add(sb.toString());
                sb = new StringBuilder();
            } else if (sb.length() <= 0) {
                arrayList.add(split[i]);
            } else {
                sb.append(PreferencesConstants.COOKIE_DELIMITER);
                sb.append(split[i]);
            }
        }
        String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
        if (strArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (i2 < strArr2.length && (strArr2[i2].startsWith("'") || strArr2[i2].startsWith("\""))) {
                    int length = strArr2[i2].length();
                    String str2 = strArr2[i2];
                    int i3 = length - 1;
                    if (i3 > 0) {
                        length = i3;
                    }
                    strArr2[i2] = str2.substring(1, length);
                }
                if (strArr2.length >= strArr.length) {
                    hashMap.put(strArr[i2], strArr2[i2]);
                } else if (strArr.length - i2 > strArr2.length) {
                    hashMap.put(strArr[i2], "");
                } else {
                    hashMap.put(strArr[i2], strArr2[strArr2.length - (strArr.length - i2)]);
                }
            }
        }
        return hashMap;
    }
}
