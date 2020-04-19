package p021us.zoom.thirdparty.login;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import java.util.List;

/* renamed from: us.zoom.thirdparty.login.SsoLogin */
class SsoLogin extends ThirdPartyLogin {
    public static final String ARGS_SSO_URL = "ARGS_SSO_URL";

    public boolean logout(@NonNull Context context) {
        return true;
    }

    public SsoLogin(Bundle bundle) {
        super(bundle);
    }

    public void login(@NonNull Activity activity, List<String> list) {
        Uri parse = Uri.parse(this.bundle.getString(ARGS_SSO_URL));
        if (parse != null) {
            loginBrowser(activity, list, parse);
        }
    }
}
