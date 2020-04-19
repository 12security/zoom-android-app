package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.SipPhoneIntegration;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.ISIPCallConfigration;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.ZMSettingsLayout;
import p021us.zoom.videomeetings.C4558R;

public class SipIntergreatedPhoneFragment extends ZMDialogFragment {
    private static final int PROTOCAL_AUTO = 3;
    private static final int PROTOCAL_TCP = 1;
    private static final int PROTOCAL_TLS = 2;
    private static final int PROTOCAL_UDP = 0;
    private Button mBtnBack;
    private View mBtnSendLog;
    @NonNull
    private ISIPCallEventListener mISIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            super.OnRequestDoneForQueryPBXUserInfo(z);
            SipIntergreatedPhoneFragment.this.fetchInfo();
        }

        public void OnRegisterResult(CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(cmmSIPCallRegResult);
            SipIntergreatedPhoneFragment.this.setRegError();
        }
    };
    private SimpleISIPLineMgrEventSinkListener mISIPLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            SipIntergreatedPhoneFragment.this.fetchInfo();
        }
    };
    private ZMSettingsLayout mSettingLayout;
    private TextView mTxtAuthorizationName;
    private TextView mTxtDomain;
    private TextView mTxtLastRegistration;
    private TextView mTxtProxyServer;
    private TextView mTxtRegError;
    private TextView mTxtRegisterServer;
    private TextView mTxtRegistrationExpiry;
    private TextView mTxtSipPassword;
    private TextView mTxtSipUsername;
    private TextView mTxtTransportProtocol;
    private TextView mTxtUserIdentity;
    private TextView mTxtVoicemail;

    private String getTransportProtocol(int i) {
        switch (i) {
            case 0:
                return "UDP";
            case 1:
                return "TCP";
            case 2:
                return "TLS";
            case 3:
                return "AUTO";
            default:
                return "";
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        fetchInfo();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_sip_intergreated_phone, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mTxtDomain = (TextView) inflate.findViewById(C4558R.C4560id.txtDomain);
        this.mTxtRegisterServer = (TextView) inflate.findViewById(C4558R.C4560id.txtRegisterServer);
        this.mTxtTransportProtocol = (TextView) inflate.findViewById(C4558R.C4560id.txtTransportProtocol);
        this.mTxtProxyServer = (TextView) inflate.findViewById(C4558R.C4560id.txtProxyServer);
        this.mTxtRegistrationExpiry = (TextView) inflate.findViewById(C4558R.C4560id.txtRegistrationExpiry);
        this.mTxtLastRegistration = (TextView) inflate.findViewById(C4558R.C4560id.txtLastRegistration);
        this.mTxtSipUsername = (TextView) inflate.findViewById(C4558R.C4560id.txtSipUsername);
        this.mTxtSipPassword = (TextView) inflate.findViewById(C4558R.C4560id.txtSipPassword);
        this.mTxtAuthorizationName = (TextView) inflate.findViewById(C4558R.C4560id.txtAuthorizationName);
        this.mTxtUserIdentity = (TextView) inflate.findViewById(C4558R.C4560id.txtUserIdentity);
        this.mTxtVoicemail = (TextView) inflate.findViewById(C4558R.C4560id.txtVoicemail);
        this.mSettingLayout = (ZMSettingsLayout) inflate.findViewById(C4558R.C4560id.settingLayout);
        this.mTxtRegError = (TextView) inflate.findViewById(C4558R.C4560id.txtRegError);
        this.mBtnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SipIntergreatedPhoneFragment.this.finish();
            }
        });
        this.mBtnSendLog = inflate.findViewById(C4558R.C4560id.btnDiagnoistic);
        this.mBtnSendLog.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DiagnosticsFragment.showAsActivity(SipIntergreatedPhoneFragment.this, 2);
            }
        });
        CmmSIPCallManager.getInstance().addListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
        return inflate;
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, SipIntergreatedPhoneFragment.class.getName(), new Bundle(), 0);
        }
    }

    /* access modifiers changed from: private */
    public void fetchInfo() {
        SipPhoneIntegration sipIntergration = CmmSIPCallManager.getInstance().getSipIntergration();
        if (sipIntergration != null) {
            this.mTxtDomain.setText(sipIntergration.getDomain());
            this.mTxtRegisterServer.setText(sipIntergration.getActiveRegisterServer());
            this.mTxtTransportProtocol.setText(getTransportProtocol(sipIntergration.getActiveProtocol()));
            this.mTxtProxyServer.setText(sipIntergration.getActiveProxyServer());
            this.mTxtRegistrationExpiry.setText(String.valueOf(sipIntergration.getRegistrationExpiry()));
            this.mTxtSipPassword.setText(sipIntergration.getPassword());
            this.mTxtAuthorizationName.setText(sipIntergration.getAuthoriztionName());
            this.mTxtVoicemail.setText(sipIntergration.getVoiceMail());
        }
        ISIPCallConfigration sipCallConfigration = CmmSIPCallManager.getInstance().getSipCallConfigration();
        if (sipCallConfigration != null) {
            long lastRegistration = sipCallConfigration.getLastRegistration();
            if (lastRegistration <= 0) {
                this.mTxtLastRegistration.setText("");
            } else {
                this.mTxtLastRegistration.setText(formatTime(lastRegistration));
            }
        }
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            this.mTxtUserIdentity.setText(currentUserProfile.getEmail());
        }
        this.mTxtSipUsername.setText(PTApp.getInstance().getMyName());
        setRegError();
    }

    private String formatTime(long j) {
        return TimeUtil.formatDateTimeShort(getContext(), j * 1000);
    }

    /* access modifiers changed from: private */
    public void setRegError() {
        String str;
        int registerErrorCode = CmmSIPLineManager.getInstance().getRegisterErrorCode();
        if (registerErrorCode == 403) {
            str = getString(C4558R.string.zm_sip_reg_error_403_88945, Integer.valueOf(registerErrorCode));
        } else if (registerErrorCode == 408) {
            str = getString(C4558R.string.zm_sip_reg_error_408_88945, Integer.valueOf(registerErrorCode));
        } else if (registerErrorCode != 503) {
            str = null;
        } else {
            str = getString(C4558R.string.zm_sip_reg_error_503_88945, Integer.valueOf(registerErrorCode));
        }
        if (TextUtils.isEmpty(str)) {
            this.mTxtRegError.setVisibility(8);
            this.mSettingLayout.setPadding(0, getResources().getDimensionPixelSize(C4558R.dimen.zm_setting_item_group_spacing), 0, 0);
            return;
        }
        this.mTxtRegError.setVisibility(0);
        this.mTxtRegError.setText(str);
        this.mSettingLayout.setPadding(0, 0, 0, 0);
    }

    /* access modifiers changed from: private */
    public void finish() {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public void onDestroyView() {
        CmmSIPCallManager.getInstance().removeListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
        super.onDestroyView();
    }
}
