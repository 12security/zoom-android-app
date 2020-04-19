package p021us.zoom.thirdparty.login.sso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.thirdparty.login.sso.SsoUtil */
public class SsoUtil {
    private static final String AUTH_URL_TEMPLATE = "%s/saml/login?from=mobile";
    private static final String ZM_CID = "&zm-cid=";

    public static String formatUrl(@NonNull String str, @Nullable String str2) {
        if (StringUtil.isEmptyOrNull(str2)) {
            return String.format(AUTH_URL_TEMPLATE, new Object[]{str});
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(AUTH_URL_TEMPLATE, new Object[]{str}));
        sb.append(ZM_CID);
        sb.append(str2);
        return sb.toString();
    }
}
