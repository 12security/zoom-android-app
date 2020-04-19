package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import com.zipow.videobox.view.BuddyInviteListView;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class BuddyInviteFragment extends ZMDialogFragment implements OnClickListener, IIMListener {
    public static final String ARG_EMAILS = "emails";
    private Button mBtnBack;
    /* access modifiers changed from: private */
    public Button mBtnSend;
    private BuddyInviteListView mBuddyInviteListView;
    private EditText mEdtEmail;

    public static class SentDoneAlertDialog extends ZMDialogFragment {
        public SentDoneAlertDialog() {
            setCancelable(false);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_msg_buddy_invite_done).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    SentDoneAlertDialog.this.onClickBtnOK();
                }
            }).create();
        }

        public void onClickBtnOK() {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                BuddyInviteFragment buddyInviteFragment = (BuddyInviteFragment) fragmentManager.findFragmentByTag(BuddyInviteFragment.class.getName());
                if (buddyInviteFragment != null) {
                    buddyInviteFragment.dismiss();
                }
            }
        }
    }

    private void onClickBtnSend() {
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
    }

    public void onIMBuddySort() {
    }

    public void onIMLocalStatusChanged(int i) {
    }

    public void onIMReceived(IMMessage iMMessage) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    public static void show(@NonNull ZMActivity zMActivity, @Nullable String str) {
        BuddyInviteFragment buddyInviteFragment = new BuddyInviteFragment();
        Bundle bundle = new Bundle();
        if (str != null) {
            bundle.putString("emails", str);
        }
        buddyInviteFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, buddyInviteFragment, BuddyInviteFragment.class.getName()).commit();
    }

    public static void showDialog(@NonNull FragmentManager fragmentManager, @Nullable String str) {
        if (getBuddyInviteFragment(fragmentManager) == null) {
            BuddyInviteFragment buddyInviteFragment = new BuddyInviteFragment();
            Bundle bundle = new Bundle();
            if (str != null) {
                bundle.putString("emails", str);
            }
            buddyInviteFragment.setArguments(bundle);
            buddyInviteFragment.show(fragmentManager, BuddyInviteFragment.class.getName());
        }
    }

    @Nullable
    public static BuddyInviteFragment getBuddyInviteFragment(FragmentManager fragmentManager) {
        return (BuddyInviteFragment) fragmentManager.findFragmentByTag(BuddyInviteFragment.class.getName());
    }

    @Nullable
    public static BuddyInviteFragment getBuddyInviteFragment(ZMActivity zMActivity) {
        return (BuddyInviteFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(BuddyInviteFragment.class.getName());
    }

    public BuddyInviteFragment() {
        setStyle(1, C4558R.style.ZMDialog_HideSoftKeyboard);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_buddy_invite, viewGroup, false);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSend = (Button) inflate.findViewById(C4558R.C4560id.btnSend);
        this.mEdtEmail = (EditText) inflate.findViewById(C4558R.C4560id.edtEmail);
        this.mBuddyInviteListView = (BuddyInviteListView) inflate.findViewById(C4558R.C4560id.buddyInviteListView);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString("emails");
            if (string != null) {
                this.mEdtEmail.setText(string);
            }
        }
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSend.setOnClickListener(this);
        this.mEdtEmail.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                BuddyInviteFragment.this.mBtnSend.setEnabled(BuddyInviteFragment.this.checkInputEmails());
            }
        });
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removeIMListener(this);
    }

    public void onResume() {
        super.onResume();
        PTUI.getInstance().addIMListener(this);
        this.mBuddyInviteListView.reloadAllItems();
        this.mBtnSend.setEnabled(checkInputEmails());
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSend) {
            onClickBtnSend();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    /* access modifiers changed from: private */
    public boolean checkInputEmails() {
        boolean z = false;
        int i = 0;
        for (String trim : this.mEdtEmail.getText().toString().split(PreferencesConstants.COOKIE_DELIMITER)) {
            if (!StringUtil.isValidEmailAddress(trim.trim())) {
                return false;
            }
            i++;
        }
        if (i > 0) {
            z = true;
        }
        return z;
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onSubscriptionRequest() {
        this.mBuddyInviteListView.reloadAllItems();
    }

    public void onSubscriptionUpdate() {
        this.mBuddyInviteListView.reloadAllItems();
    }
}
