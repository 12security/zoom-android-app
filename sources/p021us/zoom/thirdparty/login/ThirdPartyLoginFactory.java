package p021us.zoom.thirdparty.login;

import android.os.Bundle;
import androidx.annotation.NonNull;

/* renamed from: us.zoom.thirdparty.login.ThirdPartyLoginFactory */
public class ThirdPartyLoginFactory {
    public static ThirdPartyLogin build(LoginType loginType, Bundle bundle) {
        switch (loginType) {
            case Facebook:
                return new FacebookLogin(bundle);
            case Google:
                return new GoogleLogin(bundle);
            case Sso:
                return new SsoLogin(bundle);
            case CustomLogin:
                return new CustomLogin(bundle);
            default:
                return new CustomLogin(bundle);
        }
    }

    public static Bundle buildFacebookBundle(@NonNull String str, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(FacebookLogin.ARGS_FACEBOOK_URL, str);
        bundle.putInt(FacebookLogin.ARGS_REQUEST_CODE, i);
        return bundle;
    }

    public static Bundle buildEmptyFacebookBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(FacebookLogin.ARGS_FACEBOOK_URL, "");
        bundle.putInt(FacebookLogin.ARGS_REQUEST_CODE, 0);
        return bundle;
    }

    public static Bundle buildGoogleBundle(@NonNull String str) {
        Bundle bundle = new Bundle();
        bundle.putString(GoogleLogin.ARGS_GOOGLE_URL, str);
        return bundle;
    }

    public static Bundle buildEmptyGoogleBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(GoogleLogin.ARGS_GOOGLE_URL, "");
        return bundle;
    }

    public static Bundle buildSsoBundle(@NonNull String str) {
        Bundle bundle = new Bundle();
        bundle.putString(SsoLogin.ARGS_SSO_URL, str);
        return bundle;
    }

    public static Bundle buildEmptySsoBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(SsoLogin.ARGS_SSO_URL, "");
        return bundle;
    }
}
