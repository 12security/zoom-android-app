package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.LogoutHandler;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IProfileListener;
import com.zipow.videobox.ptapp.PTUI.SimpleProfileListener;
import com.zipow.videobox.ptapp.SBWebServiceErrorCode;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class PasswordEditFragment extends ZMDialogFragment implements OnClickListener, OnEditorActionListener {
    private static final int PASSWORD_LEAST_LENGTH = 6;
    /* access modifiers changed from: private */
    @NonNull
    public static String WAITING_DIALOG_TAG = "password_edit_waiting_dialog";
    private Button btnBack;
    private Button btnSave;
    private EditText mConfirmPassword;
    @NonNull
    private IProfileListener mListener = new SimpleProfileListener() {
        public void OnProfileFieldUpdated(@NonNull String str, int i, int i2, String str2) {
            if (!StringUtil.isEmptyOrNull(str) && str.equals(PasswordEditFragment.this.mRequestID)) {
                UIUtil.dismissWaitingDialog(PasswordEditFragment.this.getFragmentManager(), PasswordEditFragment.WAITING_DIALOG_TAG);
                PasswordEditFragment.this.handleProfileUpdate(i, i2, str2);
            }
        }
    };
    private EditText mNewPassword;
    private EditText mOldPassword;
    /* access modifiers changed from: private */
    public String mRequestID;

    public static class ChangePWConfirmDialog extends ZMDialogFragment {
        public static void show(@NonNull FragmentManager fragmentManager) {
            ChangePWConfirmDialog changePWConfirmDialog = new ChangePWConfirmDialog();
            changePWConfirmDialog.setArguments(new Bundle());
            changePWConfirmDialog.show(fragmentManager, ChangePWConfirmDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setMessage(C4558R.string.zm_lbl_change_pw_confirm_message_107846).setTitle(C4558R.string.zm_lbl_change_pw_confirm_title_107846).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    FragmentManager fragmentManager = ChangePWConfirmDialog.this.getFragmentManager();
                    if (fragmentManager != null) {
                        Fragment findFragmentByTag = fragmentManager.findFragmentByTag(PasswordEditFragment.class.getName());
                        if (findFragmentByTag instanceof PasswordEditFragment) {
                            ((PasswordEditFragment) findFragmentByTag).doChangePW();
                        }
                    }
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create();
        }
    }

    /* access modifiers changed from: private */
    public void handleProfileUpdate(int i, int i2, String str) {
        if (i == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || !zoomMessenger.isConnectionGood()) {
                dismiss();
                LogoutHandler.getInstance().startLogout();
                return;
            }
            return;
        }
        showErrorMessage(i, str);
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        SimpleActivity.show(zMActivity, PasswordEditFragment.class.getName(), new Bundle(), 0);
    }

    public static void showAsActivity(Fragment fragment) {
        SimpleActivity.show(fragment, PasswordEditFragment.class.getName(), new Bundle(), 0);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_password_edit, viewGroup, false);
        this.mOldPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtOldPwd);
        this.mNewPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtNewPwd);
        this.mConfirmPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtConfirmPwd);
        this.btnSave = (Button) inflate.findViewById(C4558R.C4560id.btnSave);
        this.btnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.btnSave.setEnabled(false);
        this.btnSave.setOnClickListener(this);
        this.btnBack.setOnClickListener(this);
        EditText editText = this.mConfirmPassword;
        if (editText != null) {
            editText.setImeOptions(6);
            this.mConfirmPassword.setOnEditorActionListener(this);
        }
        this.mOldPassword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                PasswordEditFragment.this.refresh();
            }
        });
        this.mNewPassword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                PasswordEditFragment.this.refresh();
            }
        });
        this.mConfirmPassword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                PasswordEditFragment.this.refresh();
            }
        });
        return inflate;
    }

    public void onResume() {
        PTUI.getInstance().addProfileListener(this.mListener);
        refresh();
        super.onResume();
    }

    public void onPause() {
        PTUI.getInstance().removeProfileListener(this.mListener);
        super.onPause();
    }

    public void refresh() {
        this.btnSave.setEnabled(checkInputPassword());
    }

    private boolean checkInputPassword() {
        if (StringUtil.isEmptyOrNull(this.mOldPassword.getText().toString())) {
            return false;
        }
        String obj = this.mNewPassword.getText().toString();
        if (!StringUtil.isEmptyOrNull(obj) && !StringUtil.isEmptyOrNull(this.mConfirmPassword.getText().toString()) && obj.length() >= 6) {
            return true;
        }
        return false;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnSave) {
            onClickBtnSave();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnSave() {
        if (checkInputPassword()) {
            String obj = this.mOldPassword.getText().toString();
            String obj2 = this.mNewPassword.getText().toString();
            String obj3 = this.mConfirmPassword.getText().toString();
            if (obj2.equals(obj)) {
                showErrorMessage(300, "");
            } else if (!obj2.equals(obj3)) {
                showConfirmNotMatchMessage();
            } else {
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    ChangePWConfirmDialog.show(fragmentManager);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void doChangePW() {
        this.mRequestID = PTApp.getInstance().changeUserPassword(this.mOldPassword.getText().toString(), this.mNewPassword.getText().toString());
        if (StringUtil.isEmptyOrNull(this.mRequestID)) {
            showErrorMessage(5000, "");
        } else {
            UIUtil.showWaitingDialog(getFragmentManager(), C4558R.string.zm_msg_waiting, WAITING_DIALOG_TAG);
        }
    }

    @NonNull
    private String getErrorMessage(int i, @Nullable String str) {
        String str2 = "";
        if (i == 300) {
            return getString(C4558R.string.zm_lbl_password_same_fail);
        }
        if (i != 1105) {
            if (i == 1136) {
                return getString(C4558R.string.zm_lbl_password_in_blacklist_45301);
            }
            if (i == 5000 || i == 5003) {
                return getString(C4558R.string.zm_lbl_profile_change_fail_cannot_connect_service);
            }
            switch (i) {
                case 1001:
                    return getString(C4558R.string.zm_lbl_user_not_exist);
                case 1002:
                    return getString(C4558R.string.zm_lbl_password_old_incorrect);
                default:
                    switch (i) {
                        case SBWebServiceErrorCode.SB_ERROR_PASSWORD_LEASTLEN /*1124*/:
                            if (str == null) {
                                return getString(C4558R.string.zm_lbl_password_unknow_error, Integer.valueOf(i));
                            }
                            int parseInt = Integer.parseInt(str);
                            return getString(C4558R.string.zm_lbl_password_characters_limit_fail, Integer.valueOf(parseInt));
                        case SBWebServiceErrorCode.SB_ERROR_PASSWORD_LEAST_ONELETTER /*1125*/:
                            return getString(C4558R.string.zm_lbl_password_letter_limit_fail);
                        case SBWebServiceErrorCode.SB_ERROR_PASSWORD_LEAST_ONENUMBER /*1126*/:
                            return getString(C4558R.string.zm_lbl_password_number_limit_fail);
                        case SBWebServiceErrorCode.SB_ERROR_PASSWORD_LEAST_ONESPECIAL /*1127*/:
                            return getString(C4558R.string.zm_lbl_password_special_character_fail);
                        case SBWebServiceErrorCode.SB_ERROR_PASSWORD_BOTH_UPPERLOWER /*1128*/:
                            return getString(C4558R.string.zm_lbl_password_uper_lower_character_fail);
                        case SBWebServiceErrorCode.SB_ERROR_PASSWORD_REUSE_ANYLAST /*1129*/:
                            if (str == null) {
                                return getString(C4558R.string.zm_lbl_password_unknow_error, Integer.valueOf(i));
                            }
                            try {
                                int parseInt2 = Integer.parseInt(str);
                                return getString(C4558R.string.zm_lbl_password_same_with_before_fail, Integer.valueOf(parseInt2));
                            } catch (NumberFormatException unused) {
                                return str2;
                            }
                        case SBWebServiceErrorCode.SB_ERROR_PASSWORD_REUSE_SAMECHARS /*1130*/:
                            return getString(C4558R.string.zm_lbl_password_same_character_fail);
                        case SBWebServiceErrorCode.SB_ERROR_PASSWORD_CONTINUATION /*1131*/:
                            return getString(C4558R.string.zm_lbl_password_continuation_character_fail);
                        default:
                            return getString(C4558R.string.zm_lbl_password_unknow_error, Integer.valueOf(i));
                    }
            }
            return str2;
        } else if (StringUtil.isEmptyOrNull(str)) {
            return getString(C4558R.string.zm_lbl_password_unknow_error, Integer.valueOf(i));
        } else {
            int parseInt3 = Integer.parseInt(str);
            return getString(C4558R.string.zm_lbl_password_old_many_times_fail, Integer.valueOf(parseInt3));
        }
    }

    private HashMap<String, String> parseErrorMessage(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str2 = (String) keys.next();
                hashMap.put(str2, jSONObject.getString(str2));
            }
            return hashMap;
        } catch (JSONException unused) {
            return null;
        }
    }

    private void showConfirmNotMatchMessage() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(getString(C4558R.string.zm_lbl_password_confirm_not_match));
        ZMErrorMessageDialog.show(getFragmentManager(), getString(C4558R.string.zm_title_password_fail), arrayList, "VanityURLModifyFragment error dialog");
    }

    private void showErrorMessage(int i, String str) {
        ArrayList arrayList = new ArrayList();
        if (i == 1105) {
            composeOldPasswordWrongManyTimesMessages(arrayList, str);
        } else if (i != 1124) {
            composeDefaultMessages(arrayList, i);
        } else {
            composeInvalidNewPasswordMessages(arrayList, str);
        }
        ZMErrorMessageDialog.show(getFragmentManager(), getString(C4558R.string.zm_title_password_fail), arrayList, "VanityURLModifyFragment error dialog");
    }

    private void composeInvalidNewPasswordMessages(ArrayList<String> arrayList, String str) {
        HashMap parseErrorMessage = parseErrorMessage(str);
        if (parseErrorMessage != null) {
            Set<String> keySet = parseErrorMessage.keySet();
            if (keySet.size() > 0) {
                for (String str2 : keySet) {
                    try {
                        String errorMessage = getErrorMessage(Integer.parseInt(str2), (String) parseErrorMessage.get(str2));
                        if (!StringUtil.isEmptyOrNull(errorMessage)) {
                            arrayList.add(errorMessage);
                        }
                    } catch (NumberFormatException unused) {
                    }
                }
            }
        } else {
            String errorMessage2 = getErrorMessage(SBWebServiceErrorCode.SB_ERROR_PASSWORD_LEASTLEN, "8");
            if (!StringUtil.isEmptyOrNull(errorMessage2)) {
                arrayList.add(errorMessage2);
            }
        }
    }

    private void composeOldPasswordWrongManyTimesMessages(ArrayList<String> arrayList, String str) {
        HashMap parseErrorMessage = parseErrorMessage(str);
        if (parseErrorMessage != null) {
            arrayList.add(getErrorMessage(SBWebServiceErrorCode.SB_ERROR_MANY_TIMES_WRONG_PASSWORD, (String) parseErrorMessage.get(String.valueOf(SBWebServiceErrorCode.SB_ERROR_MANY_TIMES_WRONG_PASSWORD))));
        }
    }

    private void composeDefaultMessages(ArrayList<String> arrayList, int i) {
        String errorMessage = getErrorMessage(i, "");
        if (!StringUtil.isEmptyOrNull(errorMessage)) {
            arrayList.add(errorMessage);
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
