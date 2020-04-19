package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IProfileListener;
import com.zipow.videobox.ptapp.PTUI.SimpleProfileListener;
import com.zipow.videobox.ptapp.SBWebServiceErrorCode;
import com.zipow.videobox.util.ZMDomainUtil;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class VanityURLModifyFragment extends ZMDialogFragment implements OnClickListener, OnEditorActionListener {
    @NonNull
    private static String ARGS_MEETING_ROOM_NAME = "args_meeting_room_name";
    /* access modifiers changed from: private */
    @NonNull
    public static String WAITING_DIALOG_TAG = "vanity_url_edit_waiting_dialog";
    private Button btnBack;
    private Button btnSave;
    @NonNull
    private IProfileListener mListener = new SimpleProfileListener() {
        public void OnProfileFieldUpdated(@NonNull String str, int i, int i2, String str2) {
            if (!StringUtil.isEmptyOrNull(str) && str.equals(VanityURLModifyFragment.this.mRequestID)) {
                UIUtil.dismissWaitingDialog(VanityURLModifyFragment.this.getFragmentManager(), VanityURLModifyFragment.WAITING_DIALOG_TAG);
                VanityURLModifyFragment.this.handleProfileUpdate(i, i2);
            }
        }
    };
    private EditText mMeetingRoomName;
    /* access modifiers changed from: private */
    public String mRequestID;
    /* access modifiers changed from: private */
    public TextView mTxtMessage;

    private boolean isValidChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || ((c >= '0' && c <= '9') || c == '.');
    }

    /* access modifiers changed from: private */
    public void handleProfileUpdate(int i, int i2) {
        if (i == 0) {
            dismiss();
        } else {
            showErrorMessage(i);
        }
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_MEETING_ROOM_NAME, str);
        SimpleActivity.show(zMActivity, VanityURLModifyFragment.class.getName(), bundle, 0);
    }

    public static void showAsActivity(Fragment fragment, String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_MEETING_ROOM_NAME, str);
        SimpleActivity.show(fragment, VanityURLModifyFragment.class.getName(), bundle, 0);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_vanity_url, viewGroup, false);
        this.mMeetingRoomName = (EditText) inflate.findViewById(C4558R.C4560id.edtMeetingRoomName);
        this.btnSave = (Button) inflate.findViewById(C4558R.C4560id.btnApply);
        this.btnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mTxtMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        this.mTxtMessage.setVisibility(8);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtInstructions)).setText(getString(C4558R.string.zm_lbl_vanity_url_instruction, ZMDomainUtil.getWebDomainWithHttps()));
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(ARGS_MEETING_ROOM_NAME);
            if (!StringUtil.isEmptyOrNull(string)) {
                this.mMeetingRoomName.setText(string);
                EditText editText = this.mMeetingRoomName;
                editText.setSelection(editText.getText().length());
            }
        }
        this.btnSave.setEnabled(false);
        this.btnSave.setOnClickListener(this);
        this.btnBack.setOnClickListener(this);
        EditText editText2 = this.mMeetingRoomName;
        if (editText2 != null) {
            editText2.requestFocus();
            this.mMeetingRoomName.setImeOptions(6);
            this.mMeetingRoomName.setOnEditorActionListener(this);
            this.mMeetingRoomName.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    VanityURLModifyFragment.this.mTxtMessage.setVisibility(8);
                }

                public void afterTextChanged(Editable editable) {
                    VanityURLModifyFragment.this.updateUI();
                }
            });
        }
        return inflate;
    }

    public void onResume() {
        PTUI.getInstance().addProfileListener(this.mListener);
        updateUI();
        super.onResume();
    }

    public void onPause() {
        PTUI.getInstance().removeProfileListener(this.mListener);
        super.onPause();
    }

    public void updateUI() {
        this.btnSave.setEnabled(validInput());
    }

    private boolean validInput() {
        String obj = this.mMeetingRoomName.getText().toString();
        if (StringUtil.isEmptyOrNull(obj)) {
            return false;
        }
        int length = obj.length();
        if (length < 5 || length > 40) {
            return false;
        }
        char charAt = obj.toLowerCase().charAt(0);
        if (charAt < 'a' || charAt > 'z') {
            return false;
        }
        for (char isValidChar : obj.toCharArray()) {
            if (!isValidChar(isValidChar)) {
                return false;
            }
        }
        return true;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnApply) {
            onClickBtnSave();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnSave() {
        if (validInput()) {
            this.mRequestID = PTApp.getInstance().modifyVanityUrl(this.mMeetingRoomName.getText().toString());
            if (StringUtil.isEmptyOrNull(this.mRequestID)) {
                showErrorMessage(5000);
            } else {
                UIUtil.showWaitingDialog(getFragmentManager(), C4558R.string.zm_msg_waiting, WAITING_DIALOG_TAG);
            }
        }
    }

    private void showErrorMessage(int i) {
        String str;
        if (i != 0) {
            if (i != 1001) {
                if (i != 4100) {
                    if (i != 5000 && i != 5003) {
                        switch (i) {
                            case SBWebServiceErrorCode.SB_ERROR_VANITY_DISABLED /*4102*/:
                                break;
                            case SBWebServiceErrorCode.SB_ERROR_VANITY_START_ATAZ /*4103*/:
                                str = getString(C4558R.string.zm_lbl_start_with_letter);
                                break;
                            default:
                                str = getString(C4558R.string.zm_lbl_unknow_error, Integer.valueOf(i));
                                break;
                        }
                    } else {
                        str = getString(C4558R.string.zm_lbl_profile_change_fail_cannot_connect_service);
                    }
                }
                str = getString(C4558R.string.zm_lbl_vanity_url_exist);
            } else {
                str = getString(C4558R.string.zm_lbl_user_not_exist);
            }
            String string = getString(C4558R.string.zm_title_vanity_url_modify_fail);
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            ZMErrorMessageDialog.show(getFragmentManager(), string, arrayList, "VanityURLModifyFragment error dialog");
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        onClickBtnSave();
        return true;
    }
}
