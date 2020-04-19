package com.zipow.videobox.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.util.ZMWebPageUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil.OnURLSpanClickListener;
import p021us.zoom.videomeetings.C4558R;

public class SignupFragment extends ZMDialogFragment implements OnClickListener, IPTUIListener {
    private static final String BIRTH = "birth";
    private static final int STATUS_INITIAL = 0;
    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_WAITING_SEND_EMAIL = 3;
    private static final int STATUS_WAITING_SIGNUP = 2;
    private String mBirth = "";
    private Button mBtnBack;
    private Button mBtnResendActiveEmail;
    private View mBtnSignIn;
    private Button mBtnSignup;
    private CheckBox mChkAcceptTerms;
    private EditText mEdtEmail;
    private EditText mEdtFirstName;
    private EditText mEdtLastName;
    private TextView mLinkAcceptTerms;
    private View mPanelSignup;
    private View mPanelSuccess;
    private View mPanelWaiting;
    private int mSignupStatus = 0;
    private TextView mTxtEmail;
    private TextView mTxtWaiting;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void showInActivity(ZMActivity zMActivity, String str) {
        SignupFragment signupFragment = new SignupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BIRTH, str);
        signupFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, signupFragment, SignupFragment.class.getName()).commit();
    }

    @Nullable
    public static SignupFragment getSignupFragment(FragmentManager fragmentManager) {
        return (SignupFragment) fragmentManager.findFragmentByTag(SignupFragment.class.getName());
    }

    public SignupFragment() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_signup, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSignup = (Button) inflate.findViewById(C4558R.C4560id.btnSignup);
        this.mBtnResendActiveEmail = (Button) inflate.findViewById(C4558R.C4560id.btnResendActiveEmail);
        this.mLinkAcceptTerms = (TextView) inflate.findViewById(C4558R.C4560id.linkAcceptTerms);
        this.mTxtWaiting = (TextView) inflate.findViewById(C4558R.C4560id.txtWaiting);
        this.mEdtFirstName = (EditText) inflate.findViewById(C4558R.C4560id.edtFirstName);
        this.mEdtLastName = (EditText) inflate.findViewById(C4558R.C4560id.edtLastName);
        this.mEdtEmail = (EditText) inflate.findViewById(C4558R.C4560id.edtEmail);
        this.mChkAcceptTerms = (CheckBox) inflate.findViewById(C4558R.C4560id.chkAcceptTerms);
        this.mPanelSignup = inflate.findViewById(C4558R.C4560id.panelSignup);
        this.mPanelSuccess = inflate.findViewById(C4558R.C4560id.panelSuccess);
        this.mPanelWaiting = inflate.findViewById(C4558R.C4560id.panelWaiting);
        this.mTxtEmail = (TextView) inflate.findViewById(C4558R.C4560id.txtEmail);
        this.mBtnSignIn = inflate.findViewById(C4558R.C4560id.btnSignIn);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSignup.setOnClickListener(this);
        this.mBtnResendActiveEmail.setOnClickListener(this);
        this.mChkAcceptTerms.setOnClickListener(this);
        this.mBtnSignIn.setOnClickListener(this);
        this.mLinkAcceptTerms.setMovementMethod(LinkMovementMethod.getInstance());
        C30131 r4 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                SignupFragment.this.updateSignupButton();
            }
        };
        this.mEdtFirstName.addTextChangedListener(r4);
        this.mEdtLastName.addTextChangedListener(r4);
        this.mEdtEmail.addTextChangedListener(r4);
        if (bundle != null) {
            this.mSignupStatus = bundle.getInt("mSignupStatus", 0);
        }
        String uRLByType = PTApp.getInstance().getURLByType(10);
        if (!StringUtil.isEmptyOrNull(uRLByType)) {
            this.mLinkAcceptTerms.setText(ZMHtmlUtil.fromHtml(getContext(), getString(C4558R.string.zm_lbl_accept_terms, uRLByType), new OnURLSpanClickListener() {
                public void onClick(View view, String str, String str2) {
                    ZMWebPageUtil.startWebPage((Fragment) SignupFragment.this, str, str2);
                }
            }));
        }
        PTUI.getInstance().addPTUIListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mBirth = StringUtil.safeString(arguments.getString(BIRTH));
        }
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("mSignupStatus", this.mSignupStatus);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSignup) {
            onClickSignupButton();
        } else if (id == C4558R.C4560id.chkAcceptTerms) {
            onClickChkAcceptTerms();
        } else if (id == C4558R.C4560id.btnResendActiveEmail) {
            onClickBtnResendActiveEmail();
        } else if (id == C4558R.C4560id.btnSignIn) {
            onClickbtnSignIn();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickSignupButton() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtFirstName);
        if (validateInput()) {
            if (PTApp.getInstance().signup(this.mEdtFirstName.getText().toString(), this.mEdtLastName.getText().toString(), this.mEdtEmail.getText().toString(), null, this.mBirth)) {
                this.mSignupStatus = 2;
                updateUI();
            } else {
                showSignupErrorDialog();
            }
        }
    }

    private void onClickBtnResendActiveEmail() {
        if (PTApp.getInstance().sendActivationEmail(this.mEdtFirstName.getText().toString(), this.mEdtLastName.getText().toString(), this.mEdtEmail.getText().toString())) {
            this.mSignupStatus = 3;
            updateUI();
            return;
        }
        showSendEmailErrorDialog();
    }

    private void onClickChkAcceptTerms() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtFirstName);
        updateSignupButton();
    }

    private void onClickbtnSignIn() {
        dismiss();
        LoginActivity.show((Context) getActivity(), false, 0);
    }

    private void updateUI() {
        switch (this.mSignupStatus) {
            case 0:
                this.mBtnSignup.setVisibility(0);
                this.mPanelSignup.setVisibility(0);
                this.mPanelSuccess.setVisibility(8);
                this.mPanelWaiting.setVisibility(8);
                updateSignupButton();
                return;
            case 1:
                this.mBtnSignup.setVisibility(8);
                this.mPanelSignup.setVisibility(8);
                this.mPanelSuccess.setVisibility(0);
                this.mPanelWaiting.setVisibility(8);
                this.mTxtEmail.setText(this.mEdtEmail.getText().toString());
                return;
            case 2:
                this.mBtnSignup.setVisibility(8);
                this.mPanelSignup.setVisibility(8);
                this.mPanelSuccess.setVisibility(8);
                this.mPanelWaiting.setVisibility(0);
                this.mTxtWaiting.setText(C4558R.string.zm_msg_signingup);
                return;
            case 3:
                this.mBtnSignup.setVisibility(8);
                this.mPanelSignup.setVisibility(8);
                this.mPanelSuccess.setVisibility(8);
                this.mPanelWaiting.setVisibility(0);
                this.mTxtWaiting.setText(C4558R.string.zm_msg_sending_activation_email);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void updateSignupButton() {
        this.mBtnSignup.setEnabled(validateInput());
    }

    private boolean validateInput() {
        String obj = this.mEdtFirstName.getText().toString();
        String obj2 = this.mEdtLastName.getText().toString();
        if (StringUtil.isValidEmailAddress(this.mEdtEmail.getText().toString()) && obj.length() != 0 && obj2.length() != 0 && this.mChkAcceptTerms.isChecked()) {
            return true;
        }
        return false;
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onPTAppEvent(int i, long j) {
        switch (i) {
            case 40:
                onSignupRet(j);
                return;
            case 41:
                onSendActivationEmailRet(j);
                return;
            default:
                return;
        }
    }

    private void onSignupRet(final long j) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ((SignupFragment) iUIElement).handleOnSignupRet(j);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleOnSignupRet(long j) {
        int i = (int) j;
        if (i == 0 || i == 1005) {
            this.mSignupStatus = 1;
        } else {
            this.mSignupStatus = 0;
            showSignupErrorDialog();
        }
        updateUI();
    }

    private void onSendActivationEmailRet(final long j) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ((SignupFragment) iUIElement).handleOnSendActivationEmailRet(j);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleOnSendActivationEmailRet(long j) {
        if (((int) j) != 0) {
            this.mSignupStatus = 1;
            showSendEmailErrorDialog();
        } else {
            this.mSignupStatus = 1;
            showEmailSentConfirmDialog();
        }
        updateUI();
    }

    private void showEmailSentConfirmDialog() {
        String obj = this.mEdtEmail.getText().toString();
        UIUtil.showSimpleMessageDialog((Activity) getActivity(), (String) null, getString(C4558R.string.zm_msg_account_sign_up_ret_52083, obj));
    }

    private void showSignupErrorDialog() {
        UIUtil.showSimpleMessageDialog((Activity) getActivity(), 0, C4558R.string.zm_msg_signup_failed);
    }

    private void showSendEmailErrorDialog() {
        UIUtil.showSimpleMessageDialog((Activity) getActivity(), 0, C4558R.string.zm_msg_send_active_email_failed);
    }
}
