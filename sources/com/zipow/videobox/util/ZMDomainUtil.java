package com.zipow.videobox.util;

import android.util.SparseArray;
import androidx.annotation.NonNull;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.util.StringUtil;

public class ZMDomainUtil {
    private static final String TAG = "ZMDomainUtil";
    private static final String ZM_CN_DOMAIN = "zoom.com";
    private static final String ZM_GLOBAL_DOMAIN = "zoom.us";
    @NonNull
    private static String ZM_MAIN_DOMAIN = "zoom.us";
    public static final String ZM_URL_HTTP = "http://";
    public static final String ZM_URL_HTTPS = "https://";
    private static String ZM_URL_WEB_DOMAIN_WITH_HTTPS;
    public static String ZM_URL_WEB_SERVER_DOMAIN;
    private static final SparseArray<String> ZOOM_SUPPORT_CLOUD_LIST = new SparseArray<>();

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(ZM_MAIN_DOMAIN);
        ZM_URL_WEB_DOMAIN_WITH_HTTPS = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("www.");
        sb2.append(ZM_MAIN_DOMAIN);
        ZM_URL_WEB_SERVER_DOMAIN = sb2.toString();
        SparseArray<String> sparseArray = ZOOM_SUPPORT_CLOUD_LIST;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(".");
        sb3.append(ZM_MAIN_DOMAIN);
        sparseArray.put(0, sb3.toString());
        ZOOM_SUPPORT_CLOUD_LIST.put(2, ".zoomgov.com");
    }

    @NonNull
    public static String getMainDomain() {
        return ZM_MAIN_DOMAIN;
    }

    public static void initMainDomain(int i) {
        if (i == 1) {
            ZM_MAIN_DOMAIN = ZM_CN_DOMAIN;
        } else {
            ZM_MAIN_DOMAIN = ZM_GLOBAL_DOMAIN;
        }
        SparseArray<String> sparseArray = ZOOM_SUPPORT_CLOUD_LIST;
        StringBuilder sb = new StringBuilder();
        sb.append(".");
        sb.append(ZM_MAIN_DOMAIN);
        sparseArray.put(0, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://");
        sb2.append(ZM_MAIN_DOMAIN);
        ZM_URL_WEB_DOMAIN_WITH_HTTPS = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("www.");
        sb3.append(ZM_MAIN_DOMAIN);
        ZM_URL_WEB_SERVER_DOMAIN = sb3.toString();
    }

    @NonNull
    public static String getWebDomainWithHttps() {
        String zmUrlWebServerPostfix = getZmUrlWebServerPostfix();
        if (StringUtil.isEmptyOrNull(zmUrlWebServerPostfix)) {
            return getWebDomain();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(zmUrlWebServerPostfix);
        return sb.toString();
    }

    @NonNull
    public static String getZmUrlPrivacyPolicy() {
        StringBuilder sb = new StringBuilder();
        sb.append(getWebDomain());
        sb.append("/privacy?onlycontent=1");
        return sb.toString();
    }

    @NonNull
    public static String getDefaultWebDomain() {
        return ZM_URL_WEB_DOMAIN_WITH_HTTPS;
    }

    @NonNull
    public static String getWebDomain() {
        String str;
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance == null || !instance.isConfApp()) {
            str = PTApp.getInstance().getWebDomain();
        } else {
            str = ConfMgr.getInstance().getWebDomain(true);
        }
        return StringUtil.isEmptyOrNull(str) ? ZM_URL_WEB_DOMAIN_WITH_HTTPS : str;
    }

    public static final String getZmUrlWebServerWWW() {
        StringBuilder sb = new StringBuilder();
        sb.append("www.");
        sb.append(getZmUrlWebServerPostfix());
        return sb.toString();
    }

    @NonNull
    public static final String getZmUrlWebServerPostfix() {
        String webDomain = getWebDomain();
        if (StringUtil.isEmptyOrNull(webDomain)) {
            return ZM_MAIN_DOMAIN;
        }
        if (webDomain.indexOf("https://www.") >= 0) {
            return webDomain.substring(12);
        }
        if (webDomain.indexOf("https://") >= 0) {
            return webDomain.substring(8);
        }
        if (webDomain.indexOf("http://www.") >= 0) {
            return webDomain.substring(11);
        }
        if (webDomain.indexOf(ZM_URL_HTTP) >= 0) {
            return webDomain.substring(7);
        }
        if (webDomain.indexOf("www.") >= 0) {
            return webDomain.substring(4);
        }
        return ZM_MAIN_DOMAIN;
    }

    @NonNull
    public static final String getPostFixForVendor(int i) {
        String str = (String) ZOOM_SUPPORT_CLOUD_LIST.get(i);
        return str == null ? (String) ZOOM_SUPPORT_CLOUD_LIST.get(0) : str;
    }

    @NonNull
    public static final String getPostFixForGov() {
        return (String) ZOOM_SUPPORT_CLOUD_LIST.get(2);
    }

    @NonNull
    public static final String getPostFixForDef() {
        return (String) ZOOM_SUPPORT_CLOUD_LIST.get(0);
    }

    @NonNull
    public static final String getPreFixForGov(@NonNull String str) {
        String str2 = (String) ZOOM_SUPPORT_CLOUD_LIST.get(2);
        if (str.startsWith("https://")) {
            str = str.substring(8);
        }
        return str.endsWith(str2) ? str.substring(0, str.length() - str2.length()) : str;
    }
}
