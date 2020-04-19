package p021us.zoom.thirdparty.cnlogin.model;

import android.app.Activity;
import androidx.annotation.NonNull;

/* renamed from: us.zoom.thirdparty.cnlogin.model.AbstractBaseCnLogin */
public abstract class AbstractBaseCnLogin {
    public abstract void init(@NonNull Activity activity);

    public abstract void onResultCallBack(@NonNull ZmCnAuthResult zmCnAuthResult);

    public abstract void registerApp();

    public abstract void unInit();
}
