package com.zipow.videobox.login.model;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.thirdparty.AuthResult;
import p021us.zoom.androidlib.app.ZMActivity;

public class ZmComboMultiLogin implements IMultiLoginChainHandler {
    private static final ZmComboMultiLogin ourInstance = new ZmComboMultiLogin();
    @Nullable
    private ZMActivity mContext;
    @Nullable
    private ZmChinaMultiLogin mZmChinaMultiLogin;
    @Nullable
    private ZmInternationalMultiLogin mZmInternationalMultiLogin;

    public static ZmComboMultiLogin getInstance() {
        return ourInstance;
    }

    private ZmComboMultiLogin() {
    }

    @Nullable
    public ZmInternationalMultiLogin getmZmInternationalMultiLogin() {
        return this.mZmInternationalMultiLogin;
    }

    @Nullable
    public ZMActivity getContext() {
        return this.mContext;
    }

    public void setContext(@Nullable ZMActivity zMActivity) {
        this.mContext = zMActivity;
    }

    @Nullable
    public ZmChinaMultiLogin getmZmChinaMultiLogin() {
        return this.mZmChinaMultiLogin;
    }

    public void onCreate(@NonNull ZmInternationalMultiLogin zmInternationalMultiLogin, @NonNull ZmChinaMultiLogin zmChinaMultiLogin, IMultiLoginListener iMultiLoginListener) {
        this.mZmInternationalMultiLogin = zmInternationalMultiLogin;
        this.mZmChinaMultiLogin = zmChinaMultiLogin;
        this.mZmInternationalMultiLogin.setILoginViewListener(iMultiLoginListener);
        this.mZmChinaMultiLogin.setILoginViewListener(iMultiLoginListener);
        this.mZmChinaMultiLogin.onCreate();
        this.mZmInternationalMultiLogin.onCreate();
    }

    public void onDestroy() {
        ZmInternationalMultiLogin zmInternationalMultiLogin = this.mZmInternationalMultiLogin;
        if (zmInternationalMultiLogin != null) {
            zmInternationalMultiLogin.onDestroy();
            this.mZmInternationalMultiLogin.setILoginViewListener(null);
        }
        ZmChinaMultiLogin zmChinaMultiLogin = this.mZmChinaMultiLogin;
        if (zmChinaMultiLogin != null) {
            zmChinaMultiLogin.onDestroy();
            this.mZmChinaMultiLogin.setILoginViewListener(null);
        }
        this.mZmInternationalMultiLogin = null;
        this.mZmChinaMultiLogin = null;
    }

    public void onResume() {
        ZmChinaMultiLogin zmChinaMultiLogin = this.mZmChinaMultiLogin;
        if (zmChinaMultiLogin != null) {
            zmChinaMultiLogin.onResume();
        }
    }

    public boolean onIMLogin(long j, int i) {
        ZmInternationalMultiLogin zmInternationalMultiLogin = this.mZmInternationalMultiLogin;
        if (zmInternationalMultiLogin != null && zmInternationalMultiLogin.onIMLogin(j, i)) {
            return true;
        }
        ZmChinaMultiLogin zmChinaMultiLogin = this.mZmChinaMultiLogin;
        if (zmChinaMultiLogin == null || !zmChinaMultiLogin.onIMLogin(j, i)) {
            return false;
        }
        return true;
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        ZmInternationalMultiLogin zmInternationalMultiLogin = this.mZmInternationalMultiLogin;
        if (zmInternationalMultiLogin != null && zmInternationalMultiLogin.onActivityResult(i, i2, intent)) {
            return true;
        }
        ZmChinaMultiLogin zmChinaMultiLogin = this.mZmChinaMultiLogin;
        if (zmChinaMultiLogin == null || !zmChinaMultiLogin.onActivityResult(i, i2, intent)) {
            return false;
        }
        return true;
    }

    public boolean onAuthResult(AuthResult authResult) {
        ZmInternationalMultiLogin zmInternationalMultiLogin = this.mZmInternationalMultiLogin;
        if (zmInternationalMultiLogin != null && zmInternationalMultiLogin.onAuthResult(authResult)) {
            return true;
        }
        ZmChinaMultiLogin zmChinaMultiLogin = this.mZmChinaMultiLogin;
        if (zmChinaMultiLogin == null || !zmChinaMultiLogin.onAuthResult(authResult)) {
            return false;
        }
        return true;
    }

    public boolean onWebLogin(long j) {
        ZmInternationalMultiLogin zmInternationalMultiLogin = this.mZmInternationalMultiLogin;
        if (zmInternationalMultiLogin != null && zmInternationalMultiLogin.onWebLogin(j)) {
            return true;
        }
        ZmChinaMultiLogin zmChinaMultiLogin = this.mZmChinaMultiLogin;
        if (zmChinaMultiLogin == null || !zmChinaMultiLogin.onWebLogin(j)) {
            return false;
        }
        return true;
    }
}
