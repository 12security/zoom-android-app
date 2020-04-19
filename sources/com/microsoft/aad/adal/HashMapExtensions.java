package com.microsoft.aad.adal;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class HashMapExtensions {
    private static final String TAG = "HashMapExtensions";

    private HashMapExtensions() {
    }

    static HashMap<String, String> urlFormDecode(String str) {
        return urlFormDecodeData(str, "&");
    }

    static HashMap<String, String> urlFormDecodeData(String str, String str2) {
        String str3;
        Object obj;
        HashMap<String, String> hashMap = new HashMap<>();
        if (!StringExtensions.isNullOrBlank(str)) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, str2);
            while (stringTokenizer.hasMoreTokens()) {
                String[] split = stringTokenizer.nextToken().split("=");
                ADALError aDALError = 0;
                if (split.length == 2) {
                    try {
                        String urlFormDecode = StringExtensions.urlFormDecode(split[0].trim());
                        Object urlFormDecode2 = StringExtensions.urlFormDecode(split[1].trim());
                        str3 = urlFormDecode;
                        obj = urlFormDecode2;
                    } catch (UnsupportedEncodingException e) {
                        Logger.m235i(TAG, ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), e.getMessage(), aDALError);
                    }
                } else if (split.length == 1) {
                    try {
                        str3 = StringExtensions.urlFormDecode(split[0].trim());
                        obj = "";
                    } catch (UnsupportedEncodingException e2) {
                        Logger.m235i(TAG, ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), e2.getMessage(), aDALError);
                    }
                } else {
                    str3 = aDALError;
                    obj = aDALError;
                }
                if (!StringExtensions.isNullOrBlank(str3)) {
                    hashMap.put(str3, obj);
                }
            }
        }
        return hashMap;
    }

    static Map<String, String> getJsonResponse(HttpWebResponse httpWebResponse) throws JSONException {
        HashMap hashMap = new HashMap();
        if (httpWebResponse != null && !TextUtils.isEmpty(httpWebResponse.getBody())) {
            JSONObject jSONObject = new JSONObject(httpWebResponse.getBody());
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                hashMap.put(str, jSONObject.getString(str));
            }
        }
        return hashMap;
    }

    static HashMap<String, String> jsonStringAsMap(String str) throws JSONException {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!StringExtensions.isNullOrBlank(str)) {
            JSONObject jSONObject = new JSONObject(str);
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str2 = (String) keys.next();
                hashMap.put(str2, jSONObject.getString(str2));
            }
        }
        return hashMap;
    }

    static HashMap<String, List<String>> jsonStringAsMapList(String str) throws JSONException {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        if (!StringExtensions.isNullOrBlank(str)) {
            JSONObject jSONObject = new JSONObject(str);
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str2 = (String) keys.next();
                ArrayList arrayList = new ArrayList();
                JSONArray jSONArray = new JSONArray(jSONObject.getString(str2));
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(jSONArray.get(i).toString());
                }
                hashMap.put(str2, arrayList);
            }
        }
        return hashMap;
    }
}
