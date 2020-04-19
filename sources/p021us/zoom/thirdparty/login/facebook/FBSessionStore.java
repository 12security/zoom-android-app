package p021us.zoom.thirdparty.login.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import androidx.annotation.NonNull;

/* renamed from: us.zoom.thirdparty.login.facebook.FBSessionStore */
public class FBSessionStore {
    private static final String EXPIRES = "expires_in";
    public static final String FACEBOOK_KEY = "facebook-session";
    public static final String GOOGLE_KEY = "google-session";
    private static final String TOKEN = "access_token";

    public static boolean save(String str, @NonNull AuthToken authToken, Context context) {
        if (context == null) {
            return false;
        }
        Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putString("access_token", authToken.token);
        edit.putLong("expires_in", authToken.expires);
        return edit.commit();
    }

    public static void clear(String str, Context context) {
        if (context != null) {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.clear();
            edit.commit();
        }
    }

    @NonNull
    public static AuthToken getSession(@NonNull Context context, String str) {
        AuthToken authToken = new AuthToken();
        SharedPreferences sharedPreferences = context.getSharedPreferences(str, 0);
        authToken.token = sharedPreferences.getString("access_token", null);
        authToken.expires = sharedPreferences.getLong("expires_in", 0);
        return authToken;
    }
}
