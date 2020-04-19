package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.AddrBookSettingActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.videomeetings.C4558R;

public class SettingContactsFragment extends ZMDialogFragment implements OnClickListener {
    private static final int REQUEST_ENABLE_ADDRBOOK = 100;
    private Button mBtnBack;
    private View mOptionContactRequests;
    private View mOptionPhoneContacts;

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, SettingContactsFragment.class.getName(), new Bundle(), 0);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_setting_contacts, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mOptionPhoneContacts = inflate.findViewById(C4558R.C4560id.optionPhoneContacts);
        this.mOptionContactRequests = inflate.findViewById(C4558R.C4560id.optionContactRequests);
        this.mBtnBack.setOnClickListener(this);
        this.mOptionPhoneContacts.setOnClickListener(this);
        this.mOptionContactRequests.setOnClickListener(this);
        return inflate;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity instanceof IMActivity) {
                ((IMActivity) zMActivity).onAddressBookEnabled(PTApp.getInstance().isPhoneNumberRegistered());
            }
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.optionPhoneContacts) {
            onClickPhoneContacts();
        } else if (id == C4558R.C4560id.optionContactRequests) {
            onClickContactRequests();
        }
    }

    private void onClickContactRequests() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ContactRequestsFragment.showAsActivity(zMActivity, 0);
        }
    }

    private void onClickPhoneContacts() {
        AddrBookSettingActivity.show((Fragment) this, 100);
    }

    private void onClickBtnBack() {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }
}
