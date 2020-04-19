package com.zipow.videobox.login;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SignupActivity;
import com.zipow.videobox.ptapp.IAgeGatingCallback;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.UIMgr;
import java.util.Calendar;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMDatePickerDialog;
import p021us.zoom.androidlib.widget.ZMDatePickerDialog.OnDateSetListener;
import p021us.zoom.videomeetings.C4558R;

public class ConfirmAgeFragment extends ZMDialogFragment implements OnClickListener {
    private String TAG = ConfirmAgeFragment.class.getName();
    /* access modifiers changed from: private */
    public String mBirth;
    private TextView mBtnCancel;
    /* access modifiers changed from: private */
    @NonNull
    public Calendar mDateFrom = Calendar.getInstance();
    /* access modifiers changed from: private */
    public int mLoginType = 102;
    private SimplePTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, long j) {
            if (i == 79) {
                ConfirmAgeFragment.this.sinkCheckAgeResult(j);
            }
        }
    };
    /* access modifiers changed from: private */
    public TextView mTxtBirth;

    /* access modifiers changed from: private */
    public void sinkCheckAgeResult(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkCheckAgeResult") {
            public void run(@NonNull IUIElement iUIElement) {
                ConfirmAgeFragment.this.checkAgeResult(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void checkAgeResult(long j) {
        showConnecting(false);
        if (j == 0) {
            FragmentActivity activity = getActivity();
            if (activity instanceof ZMActivity) {
                SignupActivity.show((ZMActivity) activity, this.mBirth);
            }
        } else if (j == 1041) {
            showFailedDialog(C4558R.string.zm_input_age_illegal_title_148333, C4558R.string.zm_input_age_illegal_msg_148333);
        } else {
            showFailedDialog(C4558R.string.zm_input_age_illegal_title_148333, C4558R.string.zm_alert_network_disconnected);
        }
    }

    public static void show(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            new ConfirmAgeFragment().showNow(fragmentManager, ConfirmAgeFragment.class.getName());
        }
    }

    @Nullable
    public static ConfirmAgeFragment getSignupFragment(FragmentManager fragmentManager) {
        return (ConfirmAgeFragment) fragmentManager.findFragmentByTag(ConfirmAgeFragment.class.getName());
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, C4558R.style.ZMDialog_NoTitle);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        setCancelable(false);
        View inflate = layoutInflater.inflate(C4558R.layout.zm_confirm_age, null);
        this.mBtnCancel = (TextView) inflate.findViewById(C4558R.C4560id.btnCancel);
        this.mTxtBirth = (TextView) inflate.findViewById(C4558R.C4560id.txtBirth);
        TextView textView = this.mBtnCancel;
        if (textView != null) {
            textView.setOnClickListener(this);
        }
        TextView textView2 = this.mTxtBirth;
        if (textView2 != null) {
            textView2.setOnClickListener(this);
        }
        this.mLoginType = PTApp.getInstance().getPTLoginType();
        PTUI.getInstance().addPTUIListener(this.mPTUIListener);
        FragmentActivity activity = getActivity();
        if (activity instanceof ZMActivity) {
            ZMActivity zMActivity = (ZMActivity) activity;
            if (ZMLoginForRealNameDialog.isBinding(zMActivity)) {
                ZMLoginForRealNameDialog.dismiss(zMActivity);
            }
        }
        return inflate;
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroyView() {
        super.onDestroyView();
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnCancel) {
            onClickBtnCancel();
        } else if (id == C4558R.C4560id.txtBirth) {
            onClickConfirmAge();
        }
    }

    private void onClickConfirmAge() {
        ZMDatePickerDialog zMDatePickerDialog = new ZMDatePickerDialog(getActivity(), new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ConfirmAgeFragment.this.mDateFrom.set(1, i);
                ConfirmAgeFragment.this.mDateFrom.set(2, i2);
                ConfirmAgeFragment.this.mDateFrom.set(5, i3);
                ConfirmAgeFragment.this.mTxtBirth.setText(TimeUtil.formatDate((Context) ConfirmAgeFragment.this.getActivity(), ConfirmAgeFragment.this.mDateFrom));
                ConfirmAgeFragment confirmAgeFragment = ConfirmAgeFragment.this;
                confirmAgeFragment.mBirth = DateFormat.format("yyyy-MM-dd", confirmAgeFragment.mDateFrom).toString();
                if (ConfirmAgeFragment.this.mLoginType != 102) {
                    int confirmAgeGating = PTApp.getInstance().confirmAgeGating(false, ConfirmAgeFragment.this.mLoginType, ConfirmAgeFragment.this.mBirth);
                    if (confirmAgeGating == 0) {
                        ConfirmAgeFragment.this.dismiss();
                        return;
                    }
                    ConfirmAgeFragment.this.sinkFailedDialog(C4558R.string.zm_input_age_illegal_sign_in_title_148333, C4558R.string.zm_alert_network_disconnected);
                    FragmentActivity activity = ConfirmAgeFragment.this.getActivity();
                    if (activity instanceof IAgeGatingCallback) {
                        ((IAgeGatingCallback) activity).onConfirmAgeFailed(confirmAgeGating);
                    }
                } else if (PTApp.getInstance().checkAgeGating(ConfirmAgeFragment.this.mBirth)) {
                    ConfirmAgeFragment.this.showConnecting(true);
                } else {
                    ConfirmAgeFragment.this.sinkFailedDialog(C4558R.string.zm_input_age_illegal_title_148333, C4558R.string.zm_alert_network_disconnected);
                }
            }
        }, this.mDateFrom.get(1), this.mDateFrom.get(2), this.mDateFrom.get(5));
        zMDatePickerDialog.show();
    }

    /* access modifiers changed from: private */
    public void sinkFailedDialog(final int i, final int i2) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkFailedDialog") {
            public void run(@NonNull IUIElement iUIElement) {
                ConfirmAgeFragment.this.showFailedDialog(i, i2);
            }
        });
    }

    /* access modifiers changed from: private */
    public void showFailedDialog(int i, int i2) {
        FragmentActivity activity = getActivity();
        if ((activity instanceof ZMActivity) && DialogUtils.isCanShowDialog((ZMActivity) activity)) {
            new Builder(activity).setTitle(i).setMessage(i2).setCancelable(false).setPositiveButton(C4409R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ConfirmAgeFragment.this.dismiss();
                }
            }).create().show();
        }
    }

    private void onClickBtnCancel() {
        if (this.mLoginType != 102) {
            PTApp.getInstance().confirmAgeGating(true, this.mLoginType, "");
            FragmentActivity activity = getActivity();
            if (activity instanceof IAgeGatingCallback) {
                ((IAgeGatingCallback) activity).onCancelAgeGating();
            }
        }
        dismiss();
    }

    /* access modifiers changed from: private */
    public void showConnecting(boolean z) {
        if (isConnecting() != z) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null && zMActivity.isActive()) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (z) {
                    WaitingDialog.newInstance(C4558R.string.zm_msg_connecting, !UIMgr.isLargeMode(zMActivity)).show(supportFragmentManager, "ConnectingDialog");
                } else {
                    WaitingDialog waitingDialog = (WaitingDialog) supportFragmentManager.findFragmentByTag("ConnectingDialog");
                    if (waitingDialog != null) {
                        waitingDialog.dismiss();
                    }
                }
            }
        }
    }

    private boolean isConnecting() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        boolean z = false;
        if (zMActivity == null) {
            return false;
        }
        if (((WaitingDialog) zMActivity.getSupportFragmentManager().findFragmentByTag("ConnectingDialog")) != null) {
            z = true;
        }
        return z;
    }
}
