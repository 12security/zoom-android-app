package p021us.zoom.androidlib.p022cn.login;

import android.os.Bundle;

/* renamed from: us.zoom.androidlib.cn.login.CnLoginCallBack */
public interface CnLoginCallBack {
    void onLoginCancel(CnLoginType cnLoginType);

    void onLoginFail(CnLoginType cnLoginType, int i, String str);

    void onLoginSuccess(CnLoginType cnLoginType, Bundle bundle);

    void onNotInstalled(CnLoginType cnLoginType, String str);
}
