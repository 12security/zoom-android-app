package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.IMProtos.PersonalGroupAtcionResponse;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class AddContactGroupFragment extends ZMDialogFragment implements OnClickListener {
    private static final int REQUEST_SELECT_CONTACTS = 100;
    public static final String RESULT_GROUP_ID = "RESULT_GROUP_ID";
    private static final String TAG = "AddContactGroupFragment";
    /* access modifiers changed from: private */
    public Button btnNext;
    private EditText mEdtName;
    private String mReqId;
    @NonNull
    private IZoomMessengerUIListener messengerUIListener = new SimpleZoomMessengerUIListener() {
        public void OnPersonalGroupResponse(byte[] bArr) {
            AddContactGroupFragment.this.OnPersonalGroupResponse(bArr);
        }
    };

    public static void showAsActivity(@Nullable Fragment fragment, int i) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            SimpleActivity.show(fragment, AddContactGroupFragment.class.getName(), bundle, i, true, 1);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_add_contact_group, viewGroup, false);
        this.btnNext = (Button) inflate.findViewById(C4558R.C4560id.btnNext);
        this.mEdtName = (EditText) inflate.findViewById(C4558R.C4560id.edtName);
        this.mEdtName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                AddContactGroupFragment.this.btnNext.setEnabled(!TextUtils.isEmpty(editable.toString().trim()));
            }
        });
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
        ZoomMessengerUI.getInstance().addListener(this.messengerUIListener);
        this.btnNext.setEnabled(false);
        return inflate;
    }

    public void onDestroyView() {
        ZoomMessengerUI.getInstance().removeListener(this.messengerUIListener);
        super.onDestroyView();
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 100 && i2 == -1) {
            createCustomGroup(intent == null ? null : (List) intent.getSerializableExtra("selectedItems"));
        }
    }

    private boolean checkNameDuplicate(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Context context = getContext();
        if (context == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        String lowerCase = str.trim().toLowerCase();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(context.getResources().getString(C4558R.string.zm_mm_lbl_auto_answer_contacts_68451));
        arrayList.add(context.getResources().getString(C4558R.string.zm_mm_lbl_star_contacts_68451));
        arrayList.add(context.getResources().getString(C4558R.string.zm_mm_lbl_phone_contacts_68451));
        arrayList.add(context.getResources().getString(C4558R.string.zm_mm_lbl_external_contacts_68451));
        arrayList.add(context.getResources().getString(C4558R.string.zm_mm_lbl_room_contacts_68451));
        for (String lowerCase2 : arrayList) {
            if (StringUtil.isSameString(lowerCase2.toLowerCase(), lowerCase)) {
                return true;
            }
        }
        for (int i = 0; i < zoomMessenger.getBuddyGroupCount(); i++) {
            ZoomBuddyGroup buddyGroupAt = zoomMessenger.getBuddyGroupAt(i);
            if (buddyGroupAt != null) {
                String name = buddyGroupAt.getName();
                if (!TextUtils.isEmpty(name) && StringUtil.isSameString(name.toLowerCase(), lowerCase)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void createCustomGroup(@Nullable List<IMAddrBookItem> list) {
        String trim = this.mEdtName.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (!zoomMessenger.isConnectionGood()) {
                    showConnectionError();
                }
                ArrayList arrayList = new ArrayList();
                if (list != null) {
                    for (IMAddrBookItem jid : list) {
                        arrayList.add(jid.getJid());
                    }
                }
                List createPersonalBuddyGroup = zoomMessenger.createPersonalBuddyGroup(trim, arrayList);
                if (createPersonalBuddyGroup != null) {
                    this.mReqId = (String) createPersonalBuddyGroup.get(1);
                    showWaitingDialog();
                }
            }
        }
    }

    private void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, "WaitingDialog");
        }
    }

    private boolean dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null) {
            return false;
        }
        ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
        if (zMDialogFragment == null) {
            return false;
        }
        zMDialogFragment.dismissAllowingStateLoss();
        return true;
    }

    private void showConnectionError() {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, C4558R.string.zm_msg_disconnected_try_again, 0).show();
        }
    }

    /* access modifiers changed from: private */
    public void OnPersonalGroupResponse(byte[] bArr) {
        if (bArr != null) {
            try {
                PersonalGroupAtcionResponse parseFrom = PersonalGroupAtcionResponse.parseFrom(bArr);
                if (parseFrom != null && parseFrom.getType() == 1 && TextUtils.equals(parseFrom.getReqId(), this.mReqId)) {
                    dismissWaitingDialog();
                    if (parseFrom.getResult() == 0) {
                        UIUtil.closeSoftKeyboard(getContext(), this.mEdtName);
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_GROUP_ID, parseFrom.getGroupId());
                        finishFragment(-1, intent);
                    }
                }
            } catch (InvalidProtocolBufferException unused) {
            }
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnCancel) {
            onClickBtnCancel();
        } else if (id == C4558R.C4560id.btnNext) {
            onClickBtnNext();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    private void onClickBtnCancel() {
        UIUtil.closeSoftKeyboard(getContext(), this.mEdtName);
        dismiss();
    }

    private void onClickBtnNext() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            String trim = this.mEdtName.getText().toString().trim();
            if (!TextUtils.isEmpty(trim)) {
                if (checkNameDuplicate(trim)) {
                    Toast.makeText(getContext(), C4558R.string.zm_msg_create_custom_duplicate_79838, 1).show();
                    return;
                }
                SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
                selectContactsParamter.includeRobot = true;
                selectContactsParamter.title = zMActivity.getString(C4558R.string.zm_mm_title_add_contacts);
                selectContactsParamter.canSelectNothing = true;
                MMSelectContactsActivity.show((Fragment) this, selectContactsParamter, 100, (Bundle) null);
            }
        }
    }
}
