package p021us.zoom.thirdparty.login;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.login.facebook.AuthToken;
import p021us.zoom.thirdparty.login.facebook.FBAuthUtil;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;

/* renamed from: us.zoom.thirdparty.login.FacebookLogin */
class FacebookLogin extends ThirdPartyLogin {
    public static final String ARGS_FACEBOOK_URL = "ARGS_FACEBOOK_URL";
    public static final String ARGS_REQUEST_CODE = "ARGS_REQUEST_CODE";

    public FacebookLogin(Bundle bundle) {
        super(bundle);
    }

    public void login(@NonNull Activity activity, List<String> list) {
        Uri parse = Uri.parse(this.bundle.getString(ARGS_FACEBOOK_URL));
        if (parse != null) {
            loginBrowser(activity, list, parse);
        }
    }

    public boolean logout(@NonNull Context context) {
        AuthToken session = FBSessionStore.getSession(context, FBSessionStore.FACEBOOK_KEY);
        if (!StringUtil.isEmptyOrNull(session.token)) {
            try {
                FBAuthUtil.logout(context, session);
            } catch (Exception unused) {
            }
        }
        return true;
    }
}
