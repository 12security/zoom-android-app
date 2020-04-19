package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.SimpleMeetingMgrListener;
import com.zipow.videobox.ptapp.SBWebServiceErrorCode;
import com.zipow.videobox.ptapp.ZoomProductHelper;
import com.zipow.videobox.view.ConfNumberEditText;
import com.zipow.videobox.view.ScheduledMeetingItem;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class PMIModifyIDFragment extends ZMFragment implements OnClickListener, OnEditorActionListener {
    private final String TAG = PMIModifyIDFragment.class.getSimpleName();
    private Button mBtnApply;
    private Button mBtnBack;
    private ConfNumberEditText mEdtConfNumber;
    @Nullable
    private ScheduledMeetingItem mMeetingItem;
    private SimpleMeetingMgrListener mMeetingMgrListener;
    private int mPMIDigits = 10;
    private TextView mTxtInstructions;

    public static void showInActivity(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            PMIModifyIDFragment newInstance = newInstance();
            newInstance.setArguments(new Bundle());
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, newInstance, PMIModifyIDFragment.class.getName()).commit();
        }
    }

    @NonNull
    public static PMIModifyIDFragment newInstance() {
        return new PMIModifyIDFragment();
    }

    @Nullable
    public static PMIModifyIDFragment findFragment(@Nullable ZMActivity zMActivity) {
        if (zMActivity == null) {
            return null;
        }
        return (PMIModifyIDFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(PMIModifyIDFragment.class.getName());
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onResume() {
        super.onResume();
        if (this.mMeetingMgrListener == null) {
            this.mMeetingMgrListener = new SimpleMeetingMgrListener() {
                public void onListMeetingResult(int i) {
                    PMIModifyIDFragment.this.onListMeetingResult(i);
                }

                public void onPMIEvent(int i, int i2, @NonNull MeetingInfoProto meetingInfoProto) {
                    PMIModifyIDFragment.this.onPMIEvent(i, i2, meetingInfoProto);
                }
            };
        }
        PTUI.getInstance().addMeetingMgrListener(this.mMeetingMgrListener);
        updateUI();
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removeMeetingMgrListener(this.mMeetingMgrListener);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_pmi_modify_id, viewGroup, false);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnApply = (Button) inflate.findViewById(C4558R.C4560id.btnApply);
        this.mEdtConfNumber = (ConfNumberEditText) inflate.findViewById(C4558R.C4560id.edtConfNumber);
        this.mTxtInstructions = (TextView) inflate.findViewById(C4558R.C4560id.txtInstructions);
        this.mBtnApply.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mEdtConfNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                PMIModifyIDFragment.this.validateInput();
            }
        });
        ConfNumberEditText confNumberEditText = this.mEdtConfNumber;
        if (confNumberEditText != null) {
            confNumberEditText.setImeOptions(6);
            this.mEdtConfNumber.setOnEditorActionListener(this);
        }
        return inflate;
    }

    /* access modifiers changed from: private */
    public void validateInput() {
        this.mBtnApply.setEnabled(validateConfNumber());
    }

    private boolean validateConfNumber() {
        String replaceAll = this.mEdtConfNumber.getText().toString().replaceAll("\\s", "");
        String string = ResourcesUtil.getString((Context) getActivity(), C4558R.string.zm_config_pmi_regex);
        boolean z = false;
        if (string != null) {
            try {
                if (!replaceAll.matches(string)) {
                    return false;
                }
            } catch (Exception unused) {
            }
        }
        if (replaceAll.length() == this.mPMIDigits) {
            z = true;
        }
        return z;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    private void updateUI() {
        int integer = ResourcesUtil.getInteger((Context) getActivity(), C4558R.integer.zm_config_long_meeting_id_format_type, 1);
        if (ZMBuildConfig.BUILD_TARGET == 0) {
            ZoomProductHelper zoomProductHelper = PTApp.getInstance().getZoomProductHelper();
            if ((zoomProductHelper != null ? zoomProductHelper.getCurrentVendor() : 0) == 1) {
                this.mPMIDigits = 11;
                this.mEdtConfNumber.setFormatType(integer);
            } else {
                this.mPMIDigits = 10;
                this.mEdtConfNumber.setFormatType(0);
            }
        } else {
            this.mPMIDigits = ResourcesUtil.getInteger((Context) getActivity(), C4558R.integer.zm_config_pmi_digits_for_pso, 11);
        }
        if (this.mPMIDigits >= 11) {
            this.mTxtInstructions.setText(C4558R.string.zm_lbl_personal_meeting_id_modify_instruction_11);
            this.mEdtConfNumber.setFormatType(integer);
        } else {
            this.mTxtInstructions.setText(C4558R.string.zm_lbl_personal_meeting_id_modify_instruction_10);
            this.mEdtConfNumber.setFormatType(0);
        }
        InputFilter[] filters = this.mEdtConfNumber.getFilters();
        for (int i = 0; i < filters.length; i++) {
            if (filters[i] instanceof LengthFilter) {
                filters[i] = new LengthFilter(this.mPMIDigits + 2);
            }
        }
        this.mEdtConfNumber.setFilters(filters);
        if (this.mMeetingItem == null) {
            this.mMeetingItem = getPersonalMeeting();
            ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
            if (scheduledMeetingItem != null) {
                this.mEdtConfNumber.setText(StringUtil.formatConfNumber(scheduledMeetingItem.getMeetingNo()));
                ConfNumberEditText confNumberEditText = this.mEdtConfNumber;
                confNumberEditText.setSelection(confNumberEditText.getText().length());
                validateInput();
            }
        }
    }

    private ScheduledMeetingItem getPersonalMeeting() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            MeetingInfoProto pmiMeetingItem = meetingHelper.getPmiMeetingItem();
            if (pmiMeetingItem != null) {
                return ScheduledMeetingItem.fromMeetingInfo(pmiMeetingItem);
            }
        }
        return null;
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        } else if (view == this.mBtnApply) {
            onClickBtnSave();
        }
    }

    private void onClickBtnSave() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mBtnApply);
        if (this.mMeetingItem != null) {
            if (!NetworkUtil.hasDataNetwork(getActivity())) {
                showErrorMessage(5000);
                return;
            }
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper != null) {
                long meetingNo = this.mMeetingItem.getMeetingNo();
                long confNumber = getConfNumber();
                if (meetingNo != confNumber) {
                    if (meetingHelper.modifyPMI(meetingNo, confNumber)) {
                        showWaitingDialog();
                    } else {
                        showErrorMessage(5000);
                    }
                }
            }
        }
    }

    private long getConfNumber() {
        String replaceAll = this.mEdtConfNumber.getText().toString().replaceAll("\\s", "");
        if (replaceAll.length() <= 0) {
            return 0;
        }
        try {
            return Long.parseLong(replaceAll);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    /* access modifiers changed from: private */
    public void onListMeetingResult(int i) {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onPMIEvent(int i, int i2, @NonNull MeetingInfoProto meetingInfoProto) {
        dismissWaitingDialog();
        if (i2 == 0) {
            dismissEditSuccess(ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto));
        } else {
            showErrorMessage(i2);
        }
    }

    private void showWaitingDialog() {
        WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting_edit_meeting);
        newInstance.setCancelable(true);
        newInstance.show(getFragmentManager(), WaitingDialog.class.getName());
    }

    private void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
        }
    }

    public void dismissEditSuccess(ScheduledMeetingItem scheduledMeetingItem) {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            Intent intent = new Intent();
            intent.putExtra("meetingItem", scheduledMeetingItem);
            zMActivity.setResult(-1, intent);
            zMActivity.finish();
        }
    }

    private void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void showErrorMessage(int i) {
        String str;
        if (i != 3002) {
            if (i != 5000 && i != 5003) {
                switch (i) {
                    case SBWebServiceErrorCode.SB_ERROR_PMI_REJECTED /*3015*/:
                    case SBWebServiceErrorCode.SB_ERROR_PMI_ALREADY_USED /*3016*/:
                        str = getString(C4558R.string.zm_lbl_personal_meeting_id_change_fail_invalid);
                        break;
                    default:
                        str = getString(C4558R.string.zm_lbl_personal_meeting_id_change_fail_unknown, Integer.valueOf(i));
                        break;
                }
            } else {
                str = getString(C4558R.string.zm_lbl_profile_change_fail_cannot_connect_service);
            }
        } else {
            str = getString(C4558R.string.zm_lbl_personal_meeting_id_change_fail_meeting_started);
        }
        String string = getString(C4558R.string.zm_title_pmi_change_fail);
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        ZMErrorMessageDialog.show(getFragmentManager(), string, arrayList, "PMIModifyIDFragment error dialog");
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        onClickBtnSave();
        return true;
    }
}
