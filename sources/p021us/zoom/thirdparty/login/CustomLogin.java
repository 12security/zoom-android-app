package p021us.zoom.thirdparty.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import java.util.List;

/* renamed from: us.zoom.thirdparty.login.CustomLogin */
public class CustomLogin extends ThirdPartyLogin {
    public void login(@NonNull Activity activity, List<String> list) {
    }

    public boolean logout(@NonNull Context context) {
        return false;
    }

    public CustomLogin(Bundle bundle) {
        super(bundle);
    }
}
