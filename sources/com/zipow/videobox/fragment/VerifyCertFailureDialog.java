package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.WelcomeActivity;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.VerifyCertEvent;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class VerifyCertFailureDialog extends ZMDialogFragment {
    private static final String ARG_EXT_VERIFY_CERT_EVENTS = "extVerifyCertEvents";
    private static final String ARG_FINISH_ACTIVITY_ON_DISMISS = "finishActivityOnDismiss";
    private static final String ARG_VERIFY_CERT_EVENT = "verifyCertEvent";
    @NonNull
    private ArrayList<VerifyCertEvent> mExtEvents = new ArrayList<>();
    private boolean mFinishActivityOnDismiss = false;
    @Nullable
    private VerifyCertEvent mVerifyCertEvent;

    @NonNull
    public static VerifyCertFailureDialog newInstance(VerifyCertEvent verifyCertEvent, boolean z) {
        return newInstance(verifyCertEvent, null, z);
    }

    @NonNull
    public static VerifyCertFailureDialog newInstance(VerifyCertEvent verifyCertEvent, @Nullable ArrayList<VerifyCertEvent> arrayList, boolean z) {
        VerifyCertFailureDialog verifyCertFailureDialog = new VerifyCertFailureDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("verifyCertEvent", verifyCertEvent);
        if (arrayList != null) {
            bundle.putSerializable(ARG_EXT_VERIFY_CERT_EVENTS, arrayList);
        }
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS, z);
        verifyCertFailureDialog.setArguments(bundle);
        return verifyCertFailureDialog;
    }

    public VerifyCertFailureDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        ZMAlertDialog zMAlertDialog;
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mVerifyCertEvent = (VerifyCertEvent) arguments.getSerializable("verifyCertEvent");
            this.mFinishActivityOnDismiss = arguments.getBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS);
            ArrayList<VerifyCertEvent> arrayList = (ArrayList) arguments.getSerializable(ARG_EXT_VERIFY_CERT_EVENTS);
            if (arrayList != null) {
                this.mExtEvents = arrayList;
            }
        }
        if (bundle != null) {
            ArrayList<VerifyCertEvent> arrayList2 = (ArrayList) bundle.getSerializable("mExtEvents");
            if (arrayList2 != null) {
                this.mExtEvents = arrayList2;
            }
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || mainboard.queryBooleanPolicyValueFromMemory()) {
            zMAlertDialog = new Builder(getActivity()).setTitle(C4558R.string.zm_security_certificate_title_42900).setMessage(C4558R.string.zm_security_certificate_question_42900).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    VerifyCertFailureDialog.this.denyCertification();
                }
            }).create();
        } else {
            getActivity();
            String str = "";
            VerifyCertEvent verifyCertEvent = this.mVerifyCertEvent;
            if (!(verifyCertEvent == null || verifyCertEvent.cert_item_ == null)) {
                str = this.mVerifyCertEvent.cert_item_.issuer_;
            }
            zMAlertDialog = new Builder(getActivity()).setTitle(C4558R.string.zm_security_certificate_title_42900).setMessage(getString(C4558R.string.zm_security_certificate_question_detail_42900, str)).setNegativeButton(C4558R.string.zm_btn_no, (OnClickListener) new OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).setPositiveButton(C4558R.string.zm_btn_yes, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    VerifyCertFailureDialog.this.trustCertification();
                }
            }).create();
        }
        zMAlertDialog.setCanceledOnTouchOutside(false);
        return zMAlertDialog;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("mExtEvents", this.mExtEvents);
    }

    public void onCancel(DialogInterface dialogInterface) {
        PTApp.getInstance().VTLSConfirmAcceptCertItem(this.mVerifyCertEvent, false, false);
        if (this.mVerifyCertEvent != null) {
            int i = 0;
            while (i < this.mExtEvents.size()) {
                VerifyCertEvent verifyCertEvent = (VerifyCertEvent) this.mExtEvents.get(i);
                if (!(this.mVerifyCertEvent.cert_item_ == null || verifyCertEvent.cert_item_ == null || !this.mVerifyCertEvent.cert_item_.equalsIgnoreHostName(verifyCertEvent.cert_item_))) {
                    PTApp.getInstance().VTLSConfirmAcceptCertItem(verifyCertEvent, false, false);
                    this.mExtEvents.remove(i);
                    i--;
                }
                i++;
            }
        }
        FragmentActivity activity = getActivity();
        if (this.mExtEvents.size() > 0) {
            newInstance((VerifyCertEvent) this.mExtEvents.remove(0), this.mExtEvents, this.mFinishActivityOnDismiss).show(getFragmentManager(), VerifyCertFailureDialog.class.getName());
            return;
        }
        if (this.mFinishActivityOnDismiss && activity != null) {
            activity.finish();
        }
        if (!PTApp.getInstance().isWebSignedOn()) {
            WelcomeActivity currentInstance = WelcomeActivity.getCurrentInstance();
            if (currentInstance != null) {
                currentInstance.setNeedBlockNextTimeAutoLogin(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void denyCertification() {
        PTApp.getInstance().VTLSConfirmAcceptCertItem(this.mVerifyCertEvent, false, false);
        if (this.mVerifyCertEvent != null) {
            int i = 0;
            while (i < this.mExtEvents.size()) {
                VerifyCertEvent verifyCertEvent = (VerifyCertEvent) this.mExtEvents.get(i);
                if (!(this.mVerifyCertEvent.cert_item_ == null || verifyCertEvent.cert_item_ == null || !this.mVerifyCertEvent.cert_item_.equalsIgnoreHostName(verifyCertEvent.cert_item_))) {
                    PTApp.getInstance().VTLSConfirmAcceptCertItem(verifyCertEvent, false, false);
                    this.mExtEvents.remove(i);
                    i--;
                }
                i++;
            }
        }
        FragmentActivity activity = getActivity();
        if (this.mExtEvents.size() > 0) {
            newInstance((VerifyCertEvent) this.mExtEvents.remove(0), this.mExtEvents, this.mFinishActivityOnDismiss).show(getFragmentManager(), VerifyCertFailureDialog.class.getName());
            return;
        }
        if (this.mFinishActivityOnDismiss && activity != null) {
            activity.finish();
        }
    }

    /* access modifiers changed from: private */
    public void trustCertification() {
        PTApp.getInstance().VTLSConfirmAcceptCertItem(this.mVerifyCertEvent, true, true);
        if (this.mVerifyCertEvent != null) {
            int i = 0;
            while (i < this.mExtEvents.size()) {
                VerifyCertEvent verifyCertEvent = (VerifyCertEvent) this.mExtEvents.get(i);
                if (!(this.mVerifyCertEvent.cert_item_ == null || verifyCertEvent.cert_item_ == null || !this.mVerifyCertEvent.cert_item_.equalsIgnoreHostName(verifyCertEvent.cert_item_))) {
                    PTApp.getInstance().VTLSConfirmAcceptCertItem(verifyCertEvent, true, true);
                    this.mExtEvents.remove(i);
                    i--;
                }
                i++;
            }
        }
        FragmentActivity activity = getActivity();
        if (this.mExtEvents.size() > 0) {
            newInstance((VerifyCertEvent) this.mExtEvents.remove(0), this.mExtEvents, this.mFinishActivityOnDismiss).show(getFragmentManager(), VerifyCertFailureDialog.class.getName());
            return;
        }
        if (this.mFinishActivityOnDismiss && activity != null) {
            activity.finish();
        }
    }

    public void onNewVerifyCertFailure(VerifyCertEvent verifyCertEvent) {
        this.mExtEvents.add(verifyCertEvent);
    }
}
