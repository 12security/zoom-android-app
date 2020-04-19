package com.zipow.videobox.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.CallInActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.fragment.SwitchAudioSourceDialog;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.UIMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.androidlib.widget.ZMTipLayer;
import p021us.zoom.videomeetings.C4558R;

public class AudioTip extends ZMTipFragment implements OnClickListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    private static final int REQUEST_AUDIO_PERMISSION_CALL_VIOP = 8000;
    private static final String TAG = "AudioTip";
    private View mBtnCallMe;
    private View mBtnCallViaVoIP;
    private View mBtnDialIn;
    private View mBtnDisconnectVoIP;
    private TextView mBtnMutePhone;
    private View mBtnSwitchAudioSource;
    private boolean mIsCallingVoIP = false;
    private boolean mIsMuted = false;
    private View mProgressCallVoIP;
    private TextView mTxtCallViaVoIP;

    public static void show(@Nullable FragmentManager fragmentManager, int i) {
        if (fragmentManager != null && ConfLocalHelper.hasAudioSourceToConnect()) {
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            AudioTip audioTip = new AudioTip();
            audioTip.setArguments(bundle);
            audioTip.show(fragmentManager, AudioTip.class.getName());
        }
    }

    public static void updateIfExists(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            AudioTip audioTip = (AudioTip) fragmentManager.findFragmentByTag(AudioTip.class.getName());
            if (audioTip != null) {
                audioTip.updateUI();
            }
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((AudioTip) fragmentManager.findFragmentByTag(AudioTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public void show(FragmentManager fragmentManager, String str) {
        super.show(fragmentManager, str);
        ConfMgr.getInstance().setConnectAudioDialogShowStatus(true);
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        AudioTip audioTip = (AudioTip) fragmentManager.findFragmentByTag(AudioTip.class.getName());
        if (audioTip == null) {
            return false;
        }
        audioTip.dismiss();
        return true;
    }

    public ZMTip onCreateTip(Context context, @NonNull LayoutInflater layoutInflater, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_audio_tip, null);
        this.mBtnCallViaVoIP = inflate.findViewById(C4558R.C4560id.btnCallViaVoIP);
        this.mBtnDialIn = inflate.findViewById(C4558R.C4560id.btnDialIn);
        this.mBtnCallMe = inflate.findViewById(C4558R.C4560id.btnCallMe);
        this.mProgressCallVoIP = inflate.findViewById(C4558R.C4560id.progressCallVoIP);
        this.mBtnDisconnectVoIP = inflate.findViewById(C4558R.C4560id.btnDisconnectVoIP);
        this.mBtnSwitchAudioSource = inflate.findViewById(C4558R.C4560id.btnSwitchAudioSource);
        this.mBtnMutePhone = (TextView) inflate.findViewById(C4558R.C4560id.btnMutePhone);
        this.mTxtCallViaVoIP = (TextView) inflate.findViewById(C4558R.C4560id.txtCallViaVoIP);
        Bundle arguments = getArguments();
        ZMTip zMTip = new ZMTip(context);
        zMTip.addView(inflate);
        int i = arguments.getInt("anchorId", 0);
        if (i > 0) {
            View findViewById = getActivity().findViewById(i);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, UIMgr.isLargeMode(getActivity()) ? 1 : 3);
            }
        }
        if (bundle != null) {
            this.mIsCallingVoIP = bundle.getBoolean("mIsCallingVoIP");
        }
        updateUI();
        this.mBtnCallViaVoIP.setOnClickListener(this);
        this.mBtnDialIn.setOnClickListener(this);
        this.mBtnCallMe.setOnClickListener(this);
        this.mBtnDisconnectVoIP.setOnClickListener(this);
        this.mBtnSwitchAudioSource.setOnClickListener(this);
        this.mBtnMutePhone.setOnClickListener(this);
        return zMTip;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mIsCallingVoIP", this.mIsCallingVoIP);
    }

    private void updateUI() {
        if (!ConfMgr.getInstance().isConfConnected()) {
            dismiss();
            return;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            dismiss();
            return;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            dismiss();
        } else if (ConfMgr.getInstance().getConfContext() == null) {
            dismiss();
        } else {
            long audiotype = audioStatusObj.getAudiotype();
            this.mIsMuted = audioStatusObj.getIsMuted();
            int i = 0;
            if (2 == audiotype) {
                this.mProgressCallVoIP.setVisibility(this.mIsCallingVoIP ? 0 : 8);
                this.mBtnDisconnectVoIP.setVisibility(8);
                this.mBtnSwitchAudioSource.setVisibility(8);
                this.mBtnMutePhone.setVisibility(8);
                this.mBtnCallViaVoIP.setVisibility(ConfLocalHelper.isVoIPEnabled() ? 0 : 8);
                this.mTxtCallViaVoIP.setText(C4558R.string.zm_btn_call_via_voip);
                this.mBtnDialIn.setVisibility(ConfLocalHelper.isDialInEnabled() ? 0 : 8);
                View view = this.mBtnCallMe;
                if (!ConfLocalHelper.isCallMeEnabled()) {
                    i = 8;
                }
                view.setVisibility(i);
                if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity())) {
                    if (this.mProgressCallVoIP.getVisibility() == 0) {
                        this.mProgressCallVoIP.sendAccessibilityEvent(8);
                    } else if (this.mBtnCallViaVoIP.getVisibility() == 0) {
                        this.mBtnCallViaVoIP.sendAccessibilityEvent(8);
                    } else if (this.mBtnDialIn.getVisibility() == 0) {
                        this.mBtnDialIn.sendAccessibilityEvent(8);
                    } else if (this.mBtnCallMe.getVisibility() == 0) {
                        this.mBtnCallMe.sendAccessibilityEvent(8);
                    }
                }
            } else if (0 == audiotype) {
                dismiss();
            } else if (1 == audiotype) {
                if (audioStatusObj.getIsMuted()) {
                    this.mBtnMutePhone.setText(C4558R.string.zm_btn_unmute_phone);
                } else {
                    this.mBtnMutePhone.setText(C4558R.string.zm_btn_mute_phone);
                }
                this.mBtnDisconnectVoIP.setVisibility(8);
                this.mBtnSwitchAudioSource.setVisibility(8);
                this.mBtnMutePhone.setVisibility(0);
                this.mBtnCallViaVoIP.setVisibility(ConfLocalHelper.isVoIPEnabled() ? 0 : 8);
                this.mTxtCallViaVoIP.setText(C4558R.string.zm_btn_switch_to_voip);
                View view2 = this.mProgressCallVoIP;
                if (!this.mIsCallingVoIP) {
                    i = 8;
                }
                view2.setVisibility(i);
                this.mBtnDialIn.setVisibility(8);
                this.mBtnCallMe.setVisibility(8);
                this.mBtnMutePhone.sendAccessibilityEvent(8);
            }
            ZMTip tip = getTip();
            if (tip != null) {
                ZMTipLayer zMTipLayer = (ZMTipLayer) tip.getParent();
                if (zMTipLayer != null) {
                    zMTipLayer.requestLayout();
                }
            }
        }
    }

    public void onClick(View view) {
        if (view == this.mBtnCallViaVoIP) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                onClickBtnCallViaVoIP();
            } else {
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, REQUEST_AUDIO_PERMISSION_CALL_VIOP);
            }
        } else if (view == this.mBtnDialIn) {
            onClickBtnDialIn();
        } else if (view == this.mBtnDisconnectVoIP) {
            onClickDisconnectVoIP();
        } else if (view == this.mBtnSwitchAudioSource) {
            onClickBtnSwitchAudioSource();
        } else if (view == this.mBtnMutePhone) {
            onClickBtnMute();
        } else if (view != this.mBtnCallMe) {
        } else {
            if (ConfLocalHelper.isPhoneNumberNotMatchCalloutAndOnlyUseOwnPhone()) {
                showAlertDialog(getString(C4558R.string.zm_call_by_phone_country_not_support_129757));
            } else {
                onClickBtnCallMe();
            }
        }
    }

    private void showAlertDialog(String str) {
        ZMAlertDialog create = new Builder(getContext()).setTitle((CharSequence) str).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.setCancelable(true);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C34222 r2 = new EventAction("AudioTipPermissionResult") {
            public void run(@NonNull IUIElement iUIElement) {
                ((AudioTip) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("AudioTipPermissionResult", r2);
    }

    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.RECORD_AUDIO".equals(strArr[i2]) && iArr[i2] == 0 && i == REQUEST_AUDIO_PERMISSION_CALL_VIOP) {
                    onClickBtnCallViaVoIP();
                }
                dismiss();
            }
        }
    }

    private void onClickBtnCallViaVoIP() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
            if (audioStatusObj != null) {
                long audiotype = audioStatusObj.getAudiotype();
                if (0 != audiotype) {
                    if (1 == audiotype) {
                        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                        if (confStatusObj != null) {
                            confStatusObj.hangUp();
                        }
                    }
                    this.mIsCallingVoIP = ConfLocalHelper.turnOnOffAudioSession(true);
                }
                this.mProgressCallVoIP.setVisibility(this.mIsCallingVoIP ? 0 : 8);
            }
        }
    }

    private void onClickBtnDialIn() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            CallInActivity.show(zMActivity, 1003);
        }
    }

    private void onClickBtnMute() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            confActivity.muteAudio(!this.mIsMuted);
            dismiss();
        }
    }

    private void onClickDisconnectVoIP() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
            if (audioStatusObj != null) {
                if (0 == audioStatusObj.getAudiotype()) {
                    ConfLocalHelper.turnOnOffAudioSession(false);
                }
                dismiss();
            }
        }
    }

    public void dismiss() {
        ConfMgr.getInstance().setConnectAudioDialogShowStatus(false);
        super.dismiss();
    }

    private void onClickBtnCallMe() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            ConfLocalHelper.callMe(confActivity);
        }
    }

    private void onClickBtnSwitchAudioSource() {
        SwitchAudioSourceDialog.showDialog(getFragmentManager());
        dismiss();
    }
}
