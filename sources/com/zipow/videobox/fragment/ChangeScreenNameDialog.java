package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ChangeScreenNameDialog extends ZMDialogFragment implements TextWatcher, OnEditorActionListener {
    private static final String ARG_TYPE = "type";
    private static final String ARG_USER_ID = "userId";
    private static final String ARG_USER_JID = "userJid";
    private static final int TYPE_USER_ID = 1;
    private static final int TYPE_USER_JID = 2;
    private Button mBtnOK = null;
    private EditText mEdtScreenName = null;

    private void updateButtons() {
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static void showDialog(@Nullable FragmentManager fragmentManager, long j) {
        if (fragmentManager != null) {
            ChangeScreenNameDialog changeScreenNameDialog = new ChangeScreenNameDialog();
            Bundle bundle = new Bundle();
            bundle.putLong("userId", j);
            bundle.putInt("type", 1);
            changeScreenNameDialog.setArguments(bundle);
            changeScreenNameDialog.show(fragmentManager, ChangeScreenNameDialog.class.getName());
        }
    }

    public static void showDialog(@Nullable FragmentManager fragmentManager, String str) {
        if (fragmentManager != null) {
            ChangeScreenNameDialog changeScreenNameDialog = new ChangeScreenNameDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_USER_JID, str);
            bundle.putInt("type", 2);
            changeScreenNameDialog.setArguments(bundle);
            changeScreenNameDialog.show(fragmentManager, ChangeScreenNameDialog.class.getName());
        }
    }

    public ChangeScreenNameDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_change_screen_name, null, false);
        this.mEdtScreenName = (EditText) inflate.findViewById(C4558R.C4560id.edtScreenName);
        this.mEdtScreenName.addTextChangedListener(this);
        return new Builder(getActivity()).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }

    public void onResume() {
        super.onResume();
        this.mBtnOK = ((ZMAlertDialog) getDialog()).getButton(-1);
        Button button = this.mBtnOK;
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (ChangeScreenNameDialog.this.checkInput()) {
                        ChangeScreenNameDialog.this.onClickBtnOK();
                    }
                }
            });
        }
        updateButtons();
        Bundle arguments = getArguments();
        if (arguments != null) {
            int i = arguments.getInt("type");
            if (i == 1) {
                CmmUser userById = ConfMgr.getInstance().getUserById(arguments.getLong("userId", 0));
                if (userById != null) {
                    this.mEdtScreenName.setText(userById.getScreenName());
                }
            } else if (i == 2) {
                String string = arguments.getString(ARG_USER_JID, "");
                ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                if (!StringUtil.isEmptyOrNull(string) && qAComponent != null) {
                    ZoomQABuddy buddyByID = qAComponent.getBuddyByID(string);
                    if (buddyByID != null) {
                        this.mEdtScreenName.setText(buddyByID.getName());
                    }
                }
            }
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        UIUtil.closeSoftKeyboardInActivity((ZMActivity) getActivity());
    }

    /* access modifiers changed from: private */
    public void onClickBtnOK() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mBtnOK);
        String trim = this.mEdtScreenName.getText().toString().trim();
        dismissAllowingStateLoss();
        Bundle arguments = getArguments();
        if (arguments != null) {
            int i = arguments.getInt("type");
            if (i == 1) {
                ConfMgr.getInstance().changeUserNameByID(trim, arguments.getLong("userId", 0));
            } else if (i == 2) {
                ConfMgr.getInstance().changeAttendeeNamebyJID(trim, arguments.getString(ARG_USER_JID, ""));
            }
        }
    }

    public void afterTextChanged(Editable editable) {
        updateButtons();
    }

    /* access modifiers changed from: private */
    public boolean checkInput() {
        return !StringUtil.isEmptyOrNull(this.mEdtScreenName.getText().toString());
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        onClickBtnOK();
        return true;
    }
}
