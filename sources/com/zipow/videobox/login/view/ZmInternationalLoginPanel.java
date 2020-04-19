package com.zipow.videobox.login.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.Nullable;
import com.zipow.videobox.login.model.ZmComboMultiLogin;
import com.zipow.videobox.login.model.ZmInternationalMultiLogin;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class ZmInternationalLoginPanel extends AbstractLoginPanel implements OnClickListener {
    private static final String TAG = "ZmInternationalLoginPanel";
    private View mBtnLoginFacebook;
    private View mBtnLoginGoogle;
    private View mLinkSSOLogin;
    private View mPanelActions;
    private View mPanelLoginViaDivider;

    public ZmInternationalLoginPanel(Context context) {
        this(context, null);
    }

    public ZmInternationalLoginPanel(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public ZmInternationalLoginPanel(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ZmInternationalMultiLogin zmInternationalMultiLogin = ZmComboMultiLogin.getInstance().getmZmInternationalMultiLogin();
        if (zmInternationalMultiLogin != null) {
            zmInternationalMultiLogin.onDestroy();
        }
    }

    public void onClick(View view) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null && zMActivity.isActive()) {
            ZmInternationalMultiLogin zmInternationalMultiLogin = ZmComboMultiLogin.getInstance().getmZmInternationalMultiLogin();
            if (zmInternationalMultiLogin != null) {
                int id = view.getId();
                if (id == C4558R.C4560id.btnLoginFacebook) {
                    zmInternationalMultiLogin.onClickBtnLoginFacebook();
                } else if (id == C4558R.C4560id.btnLoginGoogle) {
                    zmInternationalMultiLogin.onClickBtnLoginGoogle();
                } else if (id == C4558R.C4560id.linkSSOLogin) {
                    zmInternationalMultiLogin.onClickBtnLoginSSO();
                }
            }
        }
    }

    public void initVendorOptions(int i) {
        int i2;
        int i3 = 1;
        if (ZMBuildConfig.BUILD_TARGET == 0 && i == 1) {
            this.mPanelLoginViaDivider.setVisibility(8);
            this.mPanelActions.setVisibility(8);
            this.mLinkSSOLogin.setVisibility(8);
            i2 = 0;
        } else {
            Context context = getContext();
            if (context != null) {
                if (ZMPolicyUIHelper.isSupportSsoLogin(context)) {
                    this.mLinkSSOLogin.setVisibility(0);
                } else {
                    this.mLinkSSOLogin.setVisibility(8);
                    i3 = 0;
                }
                if (ZMPolicyUIHelper.isSupportGoogleLogin(context)) {
                    this.mBtnLoginGoogle.setVisibility(0);
                    i3++;
                } else {
                    this.mBtnLoginGoogle.setVisibility(8);
                }
                if (ZMPolicyUIHelper.isSupportFaceBookLogin(context)) {
                    this.mBtnLoginFacebook.setVisibility(0);
                    i2 = i3 + 1;
                } else {
                    this.mBtnLoginFacebook.setVisibility(8);
                    i2 = i3;
                }
            } else {
                return;
            }
        }
        if (i2 > 0) {
            this.mPanelLoginViaDivider.setVisibility(0);
            this.mPanelActions.setVisibility(0);
        } else {
            this.mPanelLoginViaDivider.setVisibility(8);
            this.mPanelActions.setVisibility(8);
        }
    }

    public boolean isEnableLoginType(int i) {
        boolean z = true;
        if (i == 0) {
            if (!(this.mPanelActions.getVisibility() == 0 && this.mBtnLoginFacebook.getVisibility() == 0)) {
                z = false;
            }
            return z;
        } else if (i == 2) {
            if (!(this.mPanelActions.getVisibility() == 0 && this.mBtnLoginGoogle.getVisibility() == 0)) {
                z = false;
            }
            return z;
        } else if (i != 101) {
            return false;
        } else {
            if (!(this.mPanelActions.getVisibility() == 0 && this.mLinkSSOLogin.getVisibility() == 0)) {
                z = false;
            }
            return z;
        }
    }

    private void init() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_layout_international_login, this);
        this.mBtnLoginFacebook = inflate.findViewById(C4558R.C4560id.btnLoginFacebook);
        this.mBtnLoginGoogle = inflate.findViewById(C4558R.C4560id.btnLoginGoogle);
        this.mLinkSSOLogin = inflate.findViewById(C4558R.C4560id.linkSSOLogin);
        this.mPanelLoginViaDivider = inflate.findViewById(C4558R.C4560id.panelLoginViaDivider);
        this.mPanelActions = inflate.findViewById(C4558R.C4560id.panelActions);
        this.mBtnLoginFacebook.setOnClickListener(this);
        this.mBtnLoginGoogle.setOnClickListener(this);
        this.mLinkSSOLogin.setOnClickListener(this);
        setFocusable(false);
    }
}
