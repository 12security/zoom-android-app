package p021us.zoom.thirdparty.login;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;

/* renamed from: us.zoom.thirdparty.login.GoogleLogin */
class GoogleLogin extends ThirdPartyLogin {
    public static final String ARGS_GOOGLE_URL = "ARGS_GOOGLE_URL";

    public GoogleLogin(Bundle bundle) {
        super(bundle);
    }

    public void login(@NonNull Activity activity, List<String> list) {
        Uri parse = Uri.parse(this.bundle.getString(ARGS_GOOGLE_URL));
        if (parse != null) {
            loginBrowser(activity, list, parse);
        }
    }

    public boolean logout(@NonNull Context context) {
        if (!StringUtil.isEmptyOrNull(FBSessionStore.getSession(context, FBSessionStore.GOOGLE_KEY).token)) {
            FBSessionStore.clear(FBSessionStore.GOOGLE_KEY, context);
        }
        return true;
    }
}
