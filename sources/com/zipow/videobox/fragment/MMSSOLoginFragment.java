package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.common.p008pt.ZMNativeSsoCloudInfo;
import com.zipow.videobox.login.AuthFailedDialog;
import com.zipow.videobox.login.model.ZmComboMultiLogin;
import com.zipow.videobox.login.model.ZmInternationalMultiLogin;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.login.model.ZmSsoCloudSwitchNotify;
import com.zipow.videobox.login.model.ZmSsoCloudSwitchNotify.CloudSwitchNotifyListener;
import com.zipow.videobox.login.view.ZmSsoCloudSwitchPanel;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.IZMMenuItem;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMPopupMenu;
import p021us.zoom.androidlib.widget.ZMPopupMenu.OnMenuItemClickListener;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class MMSSOLoginFragment extends ZMDialogFragment implements OnClickListener, CloudSwitchNotifyListener {
    private static final String KEY_SSO_CLOUD = "ssoCloud";
    private static final String KEY_UI_MODE = "uiMode";
    private static final String TAG = "com.zipow.videobox.fragment.MMSSOLoginFragment";
    private static final int UI_MODE_DOMAIN = 1;
    private static final int UI_MODE_EMAIL = 2;
    /* access modifiers changed from: private */
    public Button mBtnContinue;
    private View mBtnUnknownCompanyDomain;
    private ZmSsoCloudSwitchPanel mCompanyCloudSwitchPanel;
    private EditText mEdtDomain;
    private EditText mEdtEmail;
    private ZmSsoCloudSwitchPanel mEmailCloudSwitchPanel;
    private View mLayoutInputDomain;
    private View mLayoutInputEmail;
    private View mLlSsoDomain;
    @NonNull
    private OnEditorActionListener mOnEditorActionListener = new OnEditorActionListener() {
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            UIUtil.closeSoftKeyboard(MMSSOLoginFragment.this.getActivity(), textView);
            MMSSOLoginFragment.this.onBtnContinueClick();
            return true;
        }
    };
    /* access modifiers changed from: private */
    public int mSsoCloud = 0;
    /* access modifiers changed from: private */
    public TextView mTxtSsoDomain;
    /* access modifiers changed from: private */
    public int mUIMode = 1;
    private TextView mViewHintDomainError;
    private View mViewHintDomainNormal;
    private TextView mViewHintEmailError;
    private View mViewHintEmailNormal;
    private View mViewLineDomainError;
    private View mViewLineDomainNormal;
    private View mViewLineEmailError;
    private View mViewLineEmailNormal;

    public static void showAsDialog(FragmentManager fragmentManager) {
        new MMSSOLoginFragment().show(fragmentManager, MMSSOLoginFragment.class.getName());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        if (bundle != null) {
            restoreSavedInstanceState(bundle);
        } else {
            this.mSsoCloud = ZmLoginHelper.getZoom3rdPartyVendor();
        }
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setView(createContent()).setTheme(C4558R.style.ZMDialog_Material_Transparent).create();
        if (ZMPolicyUIHelper.isForceSsoLogin()) {
            create.setCanceledOnTouchOutside(false);
            create.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (i == 4) {
                        FragmentActivity activity = MMSSOLoginFragment.this.getActivity();
                        if (activity != null) {
                            activity.onBackPressed();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        Window window = create.getWindow();
        if (window != null) {
            window.clearFlags(131080);
            window.setSoftInputMode(4);
        }
        return create;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(KEY_UI_MODE, this.mUIMode);
        bundle.putInt(KEY_SSO_CLOUD, this.mSsoCloud);
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_mm_login_sso, null);
        this.mLayoutInputDomain = inflate.findViewById(C4558R.C4560id.layoutInputDomain);
        this.mViewLineDomainError = inflate.findViewById(C4558R.C4560id.viewLineDomainError);
        this.mViewLineDomainNormal = inflate.findViewById(C4558R.C4560id.viewLineDomainNormal);
        this.mViewHintDomainError = (TextView) inflate.findViewById(C4558R.C4560id.viewHintDomainError);
        this.mViewHintDomainNormal = inflate.findViewById(C4558R.C4560id.viewHintDomainNormal);
        this.mLayoutInputEmail = inflate.findViewById(C4558R.C4560id.layoutInputEmail);
        this.mViewLineEmailError = inflate.findViewById(C4558R.C4560id.viewLineEmailError);
        this.mViewHintEmailError = (TextView) inflate.findViewById(C4558R.C4560id.viewHintEmailError);
        this.mViewLineEmailNormal = inflate.findViewById(C4558R.C4560id.viewLineEmailNormal);
        this.mViewHintEmailNormal = inflate.findViewById(C4558R.C4560id.viewHintEmailNormal);
        this.mBtnUnknownCompanyDomain = inflate.findViewById(C4558R.C4560id.btnUnknowCompanyDomain);
        this.mLlSsoDomain = inflate.findViewById(C4558R.C4560id.llSsoDomain);
        this.mEdtDomain = (EditText) inflate.findViewById(C4558R.C4560id.edtDomail);
        this.mEdtEmail = (EditText) inflate.findViewById(C4558R.C4560id.edtEmail);
        this.mBtnContinue = (Button) inflate.findViewById(C4558R.C4560id.btnContinue);
        this.mTxtSsoDomain = (TextView) inflate.findViewById(C4558R.C4560id.txtSsoDomain);
        View findViewById = inflate.findViewById(C4558R.C4560id.imgDownArrow);
        LayoutParams layoutParams = this.mLayoutInputDomain.getLayoutParams();
        LayoutParams layoutParams2 = this.mLayoutInputEmail.getLayoutParams();
        if (ZmLoginHelper.isCloudSwitchShow(ZmLoginHelper.getZoom3rdPartyVendor())) {
            layoutParams.height = getResources().getDimensionPixelSize(C4558R.dimen.zm_dialog_sso_content_large_h);
            layoutParams2.height = layoutParams.height;
            initCloudSwitch(inflate);
            findViewById.setVisibility(8);
        } else {
            layoutParams.height = getResources().getDimensionPixelSize(C4558R.dimen.zm_dialog_sso_content_h);
            layoutParams2.height = layoutParams.height;
            this.mTxtSsoDomain.setCompoundDrawables(null, null, null, null);
            findViewById.setVisibility(8);
            this.mTxtSsoDomain.setOnClickListener(null);
        }
        this.mLayoutInputDomain.setLayoutParams(layoutParams);
        this.mLayoutInputEmail.setLayoutParams(layoutParams2);
        initCompanyDomain();
        this.mTxtSsoDomain.setText(ZmLoginHelper.getRealZmUrlSSOCloud(this.mSsoCloud));
        this.mBtnUnknownCompanyDomain.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnManualyEnterDomain).setOnClickListener(this);
        this.mBtnContinue.setOnClickListener(this);
        this.mEdtDomain.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEdtEmail.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEdtDomain.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                boolean z = false;
                MMSSOLoginFragment.this.showDomainInputHint(true, 0);
                if (MMSSOLoginFragment.this.mUIMode == 1) {
                    Button access$300 = MMSSOLoginFragment.this.mBtnContinue;
                    if (editable.length() > 0) {
                        z = true;
                    }
                    access$300.setEnabled(z);
                }
            }
        });
        this.mEdtEmail.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                MMSSOLoginFragment.this.showEmailInputHint(true, 0);
                if (MMSSOLoginFragment.this.mUIMode == 2) {
                    MMSSOLoginFragment.this.mBtnContinue.setEnabled(StringUtil.isValidEmailAddress(editable.toString()));
                }
            }
        });
        return inflate;
    }

    private void initCloudSwitch(View view) {
        this.mCompanyCloudSwitchPanel = (ZmSsoCloudSwitchPanel) ((ViewStub) view.findViewById(C4558R.C4560id.viewCompanyCloudSwitch)).inflate().findViewById(C4558R.C4560id.zmSSOCloudSwitch);
        this.mCompanyCloudSwitchPanel.refreshCloudSwitchState();
        this.mEmailCloudSwitchPanel = (ZmSsoCloudSwitchPanel) ((ViewStub) view.findViewById(C4558R.C4560id.viewEmailCloudSwitch)).inflate().findViewById(C4558R.C4560id.zmSSOCloudSwitch);
        this.mEmailCloudSwitchPanel.refreshCloudSwitchState();
        ZmSsoCloudSwitchNotify.getInstance().addCloudSwitchNotifyListener(this);
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnUnknowCompanyDomain) {
                onBtnUnknowCompanyDomainClick();
            } else if (id == C4558R.C4560id.btnManualyEnterDomain) {
                onBtnManualyEnterDomainClick();
            } else if (id == C4558R.C4560id.btnContinue) {
                onBtnContinueClick();
            } else if (id == C4558R.C4560id.llSsoDomain) {
                onBtnSsoDomain();
            }
        }
    }

    public void onSSOSuccess() {
        dismiss();
    }

    public void onSSOError(int i) {
        if (this.mUIMode == 1) {
            showDomainInputHint(false, i);
        } else {
            showEmailInputHint(false, i);
        }
        this.mBtnContinue.setEnabled(false);
    }

    public void dismiss() {
        finishFragment(true);
    }

    private void restoreSavedInstanceState(@NonNull Bundle bundle) {
        this.mUIMode = bundle.getInt(KEY_UI_MODE);
        this.mSsoCloud = bundle.getInt(KEY_SSO_CLOUD);
    }

    private void initCompanyDomain() {
        this.mEdtDomain.setEnabled(true);
        this.mTxtSsoDomain.setEnabled(true);
        ZMNativeSsoCloudInfo sSOCloudInfo = PTApp.getInstance().getSSOCloudInfo();
        if (sSOCloudInfo != null) {
            this.mSsoCloud = sSOCloudInfo.getmSsoCloud();
            String str = sSOCloudInfo.getmPre_fix();
            boolean isEmptyOrNull = StringUtil.isEmptyOrNull(str);
            if (!isEmptyOrNull) {
                this.mEdtDomain.setText(str);
            } else {
                this.mEdtDomain.setText(null);
            }
            if (sSOCloudInfo.isMbLocked()) {
                this.mEdtDomain.setEnabled(false);
                this.mTxtSsoDomain.setEnabled(false);
                this.mBtnUnknownCompanyDomain.setVisibility(8);
            } else if (!isEmptyOrNull) {
                this.mEdtDomain.setSelection(str.length());
            }
        }
    }

    /* access modifiers changed from: private */
    public void showEmailInputHint(boolean z, int i) {
        int i2 = 0;
        this.mViewLineEmailError.setVisibility(!z ? 0 : 8);
        this.mViewHintEmailError.setVisibility(!z ? 0 : 8);
        this.mViewLineEmailNormal.setVisibility(z ? 0 : 8);
        View view = this.mViewHintEmailNormal;
        if (!z) {
            i2 = 8;
        }
        view.setVisibility(i2);
        if (z) {
            return;
        }
        if (i == 2014) {
            this.mViewHintEmailError.setText(C4558R.string.zm_mm_lbl_no_match_domain);
        } else {
            this.mViewHintEmailError.setText(C4558R.string.zm_mm_lbl_net_error_try_again);
        }
    }

    /* access modifiers changed from: private */
    public void showDomainInputHint(boolean z, int i) {
        int i2 = 0;
        this.mViewLineDomainError.setVisibility(!z ? 0 : 8);
        this.mViewHintDomainError.setVisibility(!z ? 0 : 8);
        this.mViewLineDomainNormal.setVisibility(z ? 0 : 8);
        View view = this.mViewHintDomainNormal;
        if (!z) {
            i2 = 8;
        }
        view.setVisibility(i2);
    }

    /* access modifiers changed from: private */
    public void onBtnContinueClick() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            String string = getResources().getString(C4558R.string.zm_alert_network_disconnected);
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                AuthFailedDialog.show(zMActivity, string);
            }
            return;
        }
        int i = this.mUIMode;
        if (i == 1) {
            String ssoUrl = ZmLoginHelper.getSsoUrl(this.mEdtDomain.getText().toString().trim().toLowerCase(), false, this.mSsoCloud);
            PTApp.getInstance().setSSOURL(ssoUrl, this.mSsoCloud);
            ZmInternationalMultiLogin zmInternationalMultiLogin = ZmComboMultiLogin.getInstance().getmZmInternationalMultiLogin();
            if (zmInternationalMultiLogin != null) {
                zmInternationalMultiLogin.loginSSOSite(ssoUrl);
            }
        } else if (i == 2) {
            String obj = this.mEdtEmail.getText().toString();
            if (StringUtil.isValidEmailAddress(obj)) {
                ZmInternationalMultiLogin zmInternationalMultiLogin2 = ZmComboMultiLogin.getInstance().getmZmInternationalMultiLogin();
                if (zmInternationalMultiLogin2 != null) {
                    zmInternationalMultiLogin2.querySSODomainByEmail(obj);
                }
            }
        }
    }

    private void onBtnSsoDomain() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            C27505 r4 = new ZMMenuAdapter<ZMSimpleMenuItem>(activity, false) {
                /* access modifiers changed from: protected */
                public int getLayoutId() {
                    return C4558R.layout.zm_popup_item_sso_cloud;
                }

                /* access modifiers changed from: protected */
                public void onBindView(@NonNull View view, @NonNull ZMSimpleMenuItem zMSimpleMenuItem) {
                    TextView textView = (TextView) view.findViewById(C4558R.C4560id.zm_popup_item_text);
                    if (textView != null) {
                        textView.setText(zMSimpleMenuItem.getLabel());
                    }
                }
            };
            r4.addItem(new ZMSimpleMenuItem(0, ZmLoginHelper.getRealZmUrlSSOCloud(0)));
            r4.addItem(new ZMSimpleMenuItem(2, ZmLoginHelper.getRealZmUrlSSOCloud(2)));
            final ZMPopupMenu zMPopupMenu = new ZMPopupMenu(activity, activity, C4558R.layout.zm_popup_auto_width_menu, r4, this.mLlSsoDomain, -2, -2);
            zMPopupMenu.setBackgroudResource(C4558R.C4559drawable.zm_bg_white_pop_menu);
            zMPopupMenu.setOutSideDark(true);
            zMPopupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public void onMenuItemClick(IZMMenuItem iZMMenuItem) {
                    if (iZMMenuItem instanceof ZMSimpleMenuItem) {
                        MMSSOLoginFragment.this.mSsoCloud = iZMMenuItem.getAction();
                        MMSSOLoginFragment.this.mTxtSsoDomain.setText(ZmLoginHelper.getRealZmUrlSSOCloud(MMSSOLoginFragment.this.mSsoCloud));
                        zMPopupMenu.dismiss();
                    }
                }
            });
            zMPopupMenu.show();
        }
    }

    private void onBtnManualyEnterDomainClick() {
        boolean z = true;
        this.mUIMode = 1;
        this.mLayoutInputDomain.setVisibility(0);
        this.mLayoutInputEmail.setVisibility(8);
        this.mEdtDomain.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtDomain);
        Button button = this.mBtnContinue;
        if (this.mEdtDomain.getText().toString().trim().length() <= 0) {
            z = false;
        }
        button.setEnabled(z);
    }

    private void onBtnUnknowCompanyDomainClick() {
        this.mUIMode = 2;
        this.mLayoutInputDomain.setVisibility(8);
        this.mLayoutInputEmail.setVisibility(0);
        this.mEdtEmail.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtEmail);
        this.mBtnContinue.setEnabled(StringUtil.isValidEmailAddress(this.mEdtEmail.getText().toString()));
    }

    public void updateSsoCloud() {
        this.mTxtSsoDomain.setText(ZmLoginHelper.getRealZmUrlSSOCloud(this.mSsoCloud));
        ZmLoginHelper.setSSODomain(getEdtDomain(), this.mSsoCloud);
    }

    public String getEdtDomain() {
        return this.mEdtDomain.getText().toString().trim().toLowerCase();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0032  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCloudSwitch(int r4) {
        /*
            r3 = this;
            int r0 = r3.mUIMode
            r1 = 1
            if (r0 != r1) goto L_0x000d
            com.zipow.videobox.login.view.ZmSsoCloudSwitchPanel r0 = r3.mEmailCloudSwitchPanel
            if (r0 == 0) goto L_0x000d
            r0.refreshCloudSwitchState(r4)
            goto L_0x0019
        L_0x000d:
            int r0 = r3.mUIMode
            r2 = 2
            if (r0 != r2) goto L_0x0019
            com.zipow.videobox.login.view.ZmSsoCloudSwitchPanel r0 = r3.mCompanyCloudSwitchPanel
            if (r0 == 0) goto L_0x0019
            r0.refreshCloudSwitchState(r4)
        L_0x0019:
            r3.mSsoCloud = r4
            android.widget.Button r4 = r3.mBtnContinue
            android.widget.EditText r0 = r3.mEdtDomain
            android.text.Editable r0 = r0.getText()
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = r0.trim()
            int r0 = r0.length()
            if (r0 <= 0) goto L_0x0032
            goto L_0x0033
        L_0x0032:
            r1 = 0
        L_0x0033:
            r4.setEnabled(r1)
            android.widget.TextView r4 = r3.mTxtSsoDomain
            int r0 = r3.mSsoCloud
            java.lang.String r0 = com.zipow.videobox.login.model.ZmLoginHelper.getRealZmUrlSSOCloud(r0)
            r4.setText(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMSSOLoginFragment.updateCloudSwitch(int):void");
    }

    public void onDestroyView() {
        super.onDestroyView();
        ZmSsoCloudSwitchNotify.getInstance().removeCloudSwitchNotifyListener(this);
    }
}
