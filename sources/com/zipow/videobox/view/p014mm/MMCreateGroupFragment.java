package com.zipow.videobox.view.p014mm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMCreateGroupFragment */
public class MMCreateGroupFragment extends ZMDialogFragment implements OnClickListener {
    private static final String ARGS_ACCESS_HISTORY = "accessHistory";
    private static final String ARGS_GROUP_TYPE = "groupType";
    private static final String ARGS_ONLY_ORGANIZAION = "mChkOnlyOrganization";
    private static final int REQUEST_INVITE_MEMEBERS = 1;
    public static final String RESULT_ARGS_ACCESS_HISTORY = "accessHistory";
    public static final String RESULT_ARGS_GROUP_TYPE = "groupType";
    public static final String RESULT_ARGS_ONLY_ORGANIZAION = "mChkOnlyOrganization";
    public static final String RESULT_ARG_GROUP_NAME = "groupName";
    public static final String RESULT_ARG_SELECTED_ITEMS = "selectedItems";
    private boolean mAccessHistory = false;
    /* access modifiers changed from: private */
    public Button mBtnNext;
    private CheckedTextView mChkAccessHistory;
    private CheckedTextView mChkOnlyOrganization;
    private EditText mEdtGroupName;
    private ImageView mImgPrivateGroupType;
    private ImageView mImgPublicGroupType;
    private boolean mIsPrivateGroup = true;
    private boolean mOnlyOrganization = false;
    private View mOptionOnlyOrganization;

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, MMCreateGroupFragment.class.getName(), bundle, i, true, 1);
    }

    public static void showAsActivity(Fragment fragment, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(fragment, MMCreateGroupFragment.class.getName(), bundle, i, true, 1);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_create_new_group, viewGroup, false);
        this.mImgPrivateGroupType = (ImageView) inflate.findViewById(C4558R.C4560id.imgPrivateGroupType);
        this.mImgPublicGroupType = (ImageView) inflate.findViewById(C4558R.C4560id.imgPublicGroupType);
        this.mEdtGroupName = (EditText) inflate.findViewById(C4558R.C4560id.edtGroupName);
        this.mBtnNext = (Button) inflate.findViewById(C4558R.C4560id.btnNext);
        this.mChkAccessHistory = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAccessHistory);
        this.mChkOnlyOrganization = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkOnlyOrganization);
        this.mOptionOnlyOrganization = inflate.findViewById(C4558R.C4560id.optionOnlyOrganization);
        inflate.findViewById(C4558R.C4560id.panelPrivateGroup).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panelPublicGroup).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.mOptionOnlyOrganization.setOnClickListener(this);
        this.mChkAccessHistory.setOnClickListener(this);
        this.mBtnNext.setOnClickListener(this);
        this.mEdtGroupName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                MMCreateGroupFragment.this.mBtnNext.setEnabled(editable.length() > 0);
            }
        });
        if (bundle != null) {
            this.mIsPrivateGroup = bundle.getBoolean("groupType", true);
            this.mAccessHistory = bundle.getBoolean("accessHistory", false);
            this.mOnlyOrganization = bundle.getBoolean("mChkOnlyOrganization", false);
        }
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("groupType", this.mIsPrivateGroup);
        bundle.putBoolean("accessHistory", this.mAccessHistory);
        bundle.putBoolean("mChkOnlyOrganization", this.mOnlyOrganization);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 1 && i2 == -1) {
            String obj = this.mEdtGroupName.getText().toString();
            if (!StringUtil.isEmptyOrNull(obj) && obj.trim().length() != 0) {
                FragmentActivity activity = getActivity();
                if (activity != null && intent != null) {
                    intent.putExtra("groupType", this.mIsPrivateGroup ? 12 : 14);
                    intent.putExtra("accessHistory", this.mAccessHistory);
                    intent.putExtra("mChkOnlyOrganization", this.mOnlyOrganization);
                    intent.putExtra(RESULT_ARG_GROUP_NAME, obj);
                    activity.setResult(-1, intent);
                    dismiss();
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        updateGroupType();
        updateOption();
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnCancel) {
                dismiss();
            } else if (id == C4558R.C4560id.panelPrivateGroup) {
                onClickPanelPrivateGroup();
            } else if (id == C4558R.C4560id.panelPublicGroup) {
                onClickPanelPublicGroup();
            } else if (id == C4558R.C4560id.btnNext) {
                onClickBtnNext();
            } else if (id == C4558R.C4560id.chkAccessHistory) {
                onClickChkAccessHistory();
            } else if (id == C4558R.C4560id.optionOnlyOrganization) {
                onClickOnlyOrganization();
            }
        }
    }

    private void onClickOnlyOrganization() {
        this.mOnlyOrganization = !this.mChkOnlyOrganization.isChecked();
        CheckedTextView checkedTextView = this.mChkOnlyOrganization;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private void onClickChkAccessHistory() {
        this.mAccessHistory = !this.mChkAccessHistory.isChecked();
        CheckedTextView checkedTextView = this.mChkAccessHistory;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private void onClickBtnNext() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (zoomMessenger.checkGroupNameIsExist(this.mEdtGroupName.getText().toString())) {
                new Builder(getActivity()).setMessage(C4558R.string.zm_mm_create_same_group_name_error_59554).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) null).show();
            } else {
                SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
                selectContactsParamter.title = getString(C4558R.string.zm_mm_title_invite_member);
                selectContactsParamter.btnOkText = getString(C4558R.string.zm_btn_create);
                selectContactsParamter.isAnimBottomTop = true;
                int i = 0;
                selectContactsParamter.isOnlySameOrganization = !this.mIsPrivateGroup || this.mOnlyOrganization;
                selectContactsParamter.isContainsAllInGroup = false;
                selectContactsParamter.includeRobot = false;
                boolean z = this.mIsPrivateGroup;
                selectContactsParamter.isAcceptNoSestion = !z;
                if (z) {
                    i = 2;
                }
                selectContactsParamter.minSelectCount = i;
                selectContactsParamter.maxSelectCount = zoomMessenger.getGroupLimitCount(!this.mIsPrivateGroup);
                MMSelectContactsActivity.show((Fragment) this, selectContactsParamter, 1, (Bundle) null);
            }
        }
    }

    private void onClickPanelPrivateGroup() {
        this.mIsPrivateGroup = true;
        updateGroupType();
        ZoomLogEventTracking.eventTrackPrivateGroup();
    }

    private void onClickPanelPublicGroup() {
        this.mIsPrivateGroup = false;
        updateGroupType();
        ZoomLogEventTracking.eventTrackPublicGroup();
    }

    private void updateGroupType() {
        if (this.mIsPrivateGroup) {
            this.mImgPrivateGroupType.setVisibility(0);
            this.mImgPublicGroupType.setVisibility(8);
            this.mOptionOnlyOrganization.setVisibility(0);
            return;
        }
        this.mImgPrivateGroupType.setVisibility(8);
        this.mImgPublicGroupType.setVisibility(0);
        this.mOptionOnlyOrganization.setVisibility(8);
    }

    private void updateOption() {
        this.mChkAccessHistory.setChecked(this.mAccessHistory);
        this.mChkOnlyOrganization.setChecked(this.mOnlyOrganization);
    }

    public void dismiss() {
        finishFragment(true);
    }
}
