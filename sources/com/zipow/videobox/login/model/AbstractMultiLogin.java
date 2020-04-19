package com.zipow.videobox.login.model;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.fragment.MMSSOLoginFragment;
import com.zipow.videobox.login.ForceRedirectLoginDialog;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

abstract class AbstractMultiLogin implements IMultiLoginChainHandler {
    private static final String TAG = "AbstractMultiLogin";
    @Nullable
    protected IMultiLoginListener mIMultiLoginListener;

    public abstract void initLoginType(int i);

    public abstract void onCreate();

    public abstract void onDestroy();

    public abstract void restoreInstanceState(@NonNull Bundle bundle);

    public abstract void saveInstanceState(@NonNull Bundle bundle);

    AbstractMultiLogin() {
    }

    public void setILoginViewListener(@Nullable IMultiLoginListener iMultiLoginListener) {
        this.mIMultiLoginListener = iMultiLoginListener;
    }

    public boolean onWebLogin(long j) {
        if (j != 2011) {
            return false;
        }
        IMultiLoginListener iMultiLoginListener = this.mIMultiLoginListener;
        if (iMultiLoginListener == null || !iMultiLoginListener.isUiAuthorizing()) {
            return true;
        }
        ZMActivity context = getContext();
        if (context == null) {
            return true;
        }
        Fragment findFragmentByTag = context.getSupportFragmentManager().findFragmentByTag(MMSSOLoginFragment.class.getName());
        if (findFragmentByTag != null) {
            ((MMSSOLoginFragment) findFragmentByTag).onSSOSuccess();
        }
        PTApp.getInstance().setRencentJid("");
        PTApp.getInstance().logout(0);
        IMultiLoginListener iMultiLoginListener2 = this.mIMultiLoginListener;
        if (iMultiLoginListener2 != null) {
            iMultiLoginListener2.showAuthorizing(false);
        }
        ForceRedirectLoginDialog.newInstance(2).show(context.getSupportFragmentManager(), ForceRedirectLoginDialog.class.getName());
        return true;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public ZMActivity getContext() {
        return ZmComboMultiLogin.getInstance().getContext();
    }

    /* access modifiers changed from: protected */
    public void showNetworkError() {
        ZMActivity context = getContext();
        if (!(context == null || this.mIMultiLoginListener == null)) {
            this.mIMultiLoginListener.onAuthFailed(context.getResources().getString(C4558R.string.zm_alert_network_disconnected));
        }
    }

    /* access modifiers changed from: protected */
    public void showAuthError(@StringRes int i) {
        ZMActivity context = getContext();
        if (!(context == null || this.mIMultiLoginListener == null)) {
            this.mIMultiLoginListener.onAuthFailed(context.getResources().getString(i));
        }
    }
}
