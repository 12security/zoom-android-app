package com.zipow.videobox.fragment;

import android.content.Context;
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
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class RenameContactGroupFragment extends ZMDialogFragment implements OnClickListener {
    private static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";
    private static final String TAG = "RenameContactGroupFragment";
    /* access modifiers changed from: private */
    public Button btnNext;
    private EditText mEdtName;
    @Nullable
    private String mGroupId;
    /* access modifiers changed from: private */
    @Nullable
    public String mOriginalGroupName;

    public static void showAsActivity(@Nullable Fragment fragment, String str, int i) {
        if (fragment != null && !TextUtils.isEmpty(str)) {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_GROUP_ID, str);
            SimpleActivity.show(fragment, RenameContactGroupFragment.class.getName(), bundle, i, true, 1);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_rename_contact_group, viewGroup, false);
        this.btnNext = (Button) inflate.findViewById(C4558R.C4560id.btnNext);
        this.mEdtName = (EditText) inflate.findViewById(C4558R.C4560id.edtName);
        this.mEdtName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                String trim = editable.toString().trim();
                RenameContactGroupFragment.this.btnNext.setEnabled(!TextUtils.isEmpty(trim) && !trim.toLowerCase().equals(RenameContactGroupFragment.this.mOriginalGroupName));
            }
        });
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mGroupId = arguments.getString(EXTRA_GROUP_ID);
            if (TextUtils.isEmpty(this.mGroupId)) {
                dismiss();
                return;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                dismiss();
                return;
            }
            ZoomBuddyGroup buddyGroupByXMPPId = zoomMessenger.getBuddyGroupByXMPPId(this.mGroupId);
            if (buddyGroupByXMPPId == null) {
                dismiss();
                return;
            }
            this.mOriginalGroupName = buddyGroupByXMPPId.getName();
            String str = this.mOriginalGroupName;
            if (str != null) {
                this.mOriginalGroupName = str.toLowerCase();
            }
            this.mEdtName.setText(buddyGroupByXMPPId.getName());
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
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
        if (((ZMActivity) getActivity()) != null) {
            String trim = this.mEdtName.getText().toString().trim();
            if (!TextUtils.isEmpty(trim)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddyGroup buddyGroupByXMPPId = zoomMessenger.getBuddyGroupByXMPPId(this.mGroupId);
                    if (buddyGroupByXMPPId != null) {
                        if (TextUtils.equals(trim, buddyGroupByXMPPId.getName())) {
                            dismiss();
                        } else if (checkNameDuplicate(trim)) {
                            Toast.makeText(getContext(), C4558R.string.zm_msg_create_custom_duplicate_79838, 1).show();
                        } else {
                            zoomMessenger.modifyPersonalBuddyGroupName(this.mGroupId, trim);
                            UIUtil.closeSoftKeyboard(getContext(), this.mEdtName);
                            dismiss();
                        }
                    }
                }
            }
        }
    }
}
