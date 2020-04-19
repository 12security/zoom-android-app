package p021us.zoom.thirdparty.login.facebook;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.http.HttpHost;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: us.zoom.thirdparty.login.facebook.Util */
public final class Util {
    private static boolean ENABLE_LOG = false;

    public static String encodePostBody(Bundle bundle, String str) {
        if (bundle == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str2 : bundle.keySet()) {
            Object obj = bundle.get(str2);
            if (obj instanceof String) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Content-Disposition: form-data; name=\"");
                sb2.append(str2);
                sb2.append("\"\r\n\r\n");
                sb2.append(obj);
                sb.append(sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("\r\n--");
                sb3.append(str);
                sb3.append("\r\n");
                sb.append(sb3.toString());
            }
        }
        return sb.toString();
    }

    public static String encodeUrl(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str : bundle.keySet()) {
            if (bundle.get(str) instanceof String) {
                if (z) {
                    z = false;
                } else {
                    sb.append("&");
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(URLEncoder.encode(str));
                sb2.append("=");
                sb2.append(URLEncoder.encode(bundle.getString(str)));
                sb.append(sb2.toString());
            }
        }
        return sb.toString();
    }

    public static Bundle decodeUrl(String str) {
        Bundle bundle = new Bundle();
        if (str != null) {
            for (String split : str.split("&")) {
                String[] split2 = split.split("=");
                if (split2.length == 2) {
                    bundle.putString(URLDecoder.decode(split2[0]), URLDecoder.decode(split2[1]));
                }
            }
        }
        return bundle;
    }

    public static Bundle parseUrl(String str) {
        try {
            URL url = new URL(str.replace("fbconnect", HttpHost.DEFAULT_SCHEME_NAME));
            Bundle decodeUrl = decodeUrl(url.getQuery());
            decodeUrl.putAll(decodeUrl(url.getRef()));
            return decodeUrl;
        } catch (MalformedURLException unused) {
            return new Bundle();
        }
    }

    public static JSONObject parseJson(String str) throws JSONException, FacebookError {
        if (!str.equals("false")) {
            if (str.equals("true")) {
                str = "{value : true}";
            }
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("error")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("error");
                throw new FacebookError(jSONObject2.getString("message"), jSONObject2.getString("type"), 0);
            } else if (jSONObject.has("error_code") && jSONObject.has("error_msg")) {
                throw new FacebookError(jSONObject.getString("error_msg"), "", Integer.parseInt(jSONObject.getString("error_code")));
            } else if (jSONObject.has("error_code")) {
                throw new FacebookError("request failed", "", Integer.parseInt(jSONObject.getString("error_code")));
            } else if (jSONObject.has("error_msg")) {
                throw new FacebookError(jSONObject.getString("error_msg"));
            } else if (!jSONObject.has("error_reason")) {
                return jSONObject;
            } else {
                throw new FacebookError(jSONObject.getString("error_reason"));
            }
        } else {
            throw new FacebookError("request failed");
        }
    }

    public static void showAlert(Context context, String str, String str2) {
        Builder builder = new Builder(context);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.create().show();
    }

    public static void logd(String str, String str2) {
        boolean z = ENABLE_LOG;
    }
}
