package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.IMProtos.zGroupProperty;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.DialogUtils;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFileListActivity.ShowAlertDialog;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSetGroupInformationFragment */
public class MMSetGroupInformationFragment extends ZMDialogFragment implements OnClickListener {
    public static final String EXTRA_ARGS_GROUP_JID = "groupJid";
    private static final String TAG = "MMSetGroupInformationFragment";
    /* access modifiers changed from: private */
    public Button mBtnDone;
    private EditText mEdtTopic;
    /* access modifiers changed from: private */
    @Nullable
    public String mGroupId;
    private boolean mIsGroupRoom;
    private TextView mNote;
    private View mPanelConvertPrivateGroup;
    private TextView mTxtTitle;
    @Nullable
    private ZMDialogFragment mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
            MMSetGroupInformationFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            MMSetGroupInformationFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMSetGroupInformationFragment.this.on_NotifyGroupDestroyImpl(str);
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.MMSetGroupInformationFragment$ShowConvertDialog */
    public static class ShowConvertDialog extends ZMDialogFragment {
        public static void showDialog(FragmentManager fragmentManager) {
            ShowConvertDialog showConvertDialog = new ShowConvertDialog();
            showConvertDialog.setArguments(new Bundle());
            showConvertDialog.show(fragmentManager, ShowAlertDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Builder negativeButton = new Builder(getActivity()).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ShowConvertDialog.this.onClickConvertPrivateGroup();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null);
            negativeButton.setMessage(C4558R.string.zm_msg_convert_private_group_59554);
            return negativeButton.create();
        }

        /* access modifiers changed from: private */
        public void onClickConvertPrivateGroup() {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                MMSetGroupInformationFragment mMSetGroupInformationFragment = (MMSetGroupInformationFragment) fragmentManager.findFragmentByTag(MMSetGroupInformationFragment.class.getName());
                if (mMSetGroupInformationFragment != null) {
                    mMSetGroupInformationFragment.doConvertPrivateGroup();
                }
            }
        }
    }

    public static void showAsActivity(Fragment fragment, String str, int i) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("groupJid", str);
            SimpleActivity.show(fragment, MMSetGroupInformationFragment.class.getName(), bundle, i, false, 1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_set_group_information, viewGroup, false);
        this.mBtnDone = (Button) inflate.findViewById(C4558R.C4560id.btnDone);
        this.mBtnDone.setEnabled(false);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mEdtTopic = (EditText) inflate.findViewById(C4558R.C4560id.edtTopic);
        this.mPanelConvertPrivateGroup = inflate.findViewById(C4558R.C4560id.panelConvertPrivateGroup);
        this.mNote = (TextView) inflate.findViewById(C4558R.C4560id.note);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnConvertPrivateGroup).setOnClickListener(this);
        this.mBtnDone.setOnClickListener(this);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        return inflate;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        if (!StringUtil.isEmptyOrNull(this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    this.mEdtTopic.setEnabled(true);
                    this.mEdtTopic.setText(groupById.getGroupName());
                    EditText editText = this.mEdtTopic;
                    editText.setSelection(editText.length());
                    this.mIsGroupRoom = groupById.isRoom();
                    if (groupById.isRoom()) {
                        this.mNote.setVisibility(8);
                        this.mTxtTitle.setText(getString(C4558R.string.zm_mm_lbl_channel_name_108993));
                        if (!groupById.isGroupOperatorable() || groupById.isBroadcast()) {
                            this.mEdtTopic.setEnabled(false);
                            this.mBtnDone.setVisibility(8);
                        } else {
                            this.mBtnDone.setVisibility(0);
                        }
                    } else {
                        this.mTxtTitle.setText(getString(C4558R.string.zm_mm_lbl_muc_name_108993));
                        this.mBtnDone.setVisibility(0);
                        this.mNote.setVisibility(8);
                        if (!groupById.isGroupOperatorable()) {
                            this.mEdtTopic.setEnabled(false);
                            this.mBtnDone.setVisibility(8);
                        } else {
                            this.mBtnDone.setVisibility(0);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(final int i, final GroupAction groupAction, String str) {
        if (StringUtil.isSameString(groupAction.getGroupId(), this.mGroupId)) {
            if (groupAction.getActionType() != 4 || !groupAction.isMeInBuddies()) {
                if (groupAction.getActionType() == 1 || groupAction.getActionType() == 7) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomBuddy myself = zoomMessenger.getMyself();
                        if (myself != null) {
                            if (!StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                                if (isResumed()) {
                                    updateData();
                                }
                                return;
                            }
                            EventTaskManager eventTaskManager = getEventTaskManager();
                            if (eventTaskManager != null) {
                                eventTaskManager.pushLater(new EventAction("GroupAction.ACTION_MODIFY_NAME") {
                                    public void run(IUIElement iUIElement) {
                                        MMSetGroupInformationFragment mMSetGroupInformationFragment = (MMSetGroupInformationFragment) iUIElement;
                                        if (mMSetGroupInformationFragment != null) {
                                            mMSetGroupInformationFragment.handleGroupAction(i, groupAction);
                                        }
                                    }
                                });
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                return;
            }
            hideIME();
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            updateData();
        }
    }

    /* access modifiers changed from: private */
    public void on_NotifyGroupDestroyImpl(String str) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            hideIME();
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupAction(int i, @NonNull GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            updateData();
        } else if (groupAction.getActionType() == 1 || groupAction.getActionType() == 7) {
            showChangeTopicFailureMessage(i);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mGroupId = getArguments().getString("groupJid");
        this.mEdtTopic.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                MMSetGroupInformationFragment.this.mBtnDone.setEnabled(false);
                if (!StringUtil.isEmptyOrNull(MMSetGroupInformationFragment.this.mGroupId) && !TextUtils.isEmpty(editable)) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        ZoomGroup groupById = zoomMessenger.getGroupById(MMSetGroupInformationFragment.this.mGroupId);
                        if (groupById != null && !editable.toString().equalsIgnoreCase(groupById.getGroupName())) {
                            MMSetGroupInformationFragment.this.mBtnDone.setEnabled(true);
                        }
                    }
                }
            }
        });
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnBack) {
                hideIME();
                dismiss();
            } else if (id == C4558R.C4560id.btnDone) {
                onClickBtnDone();
            } else if (id == C4558R.C4560id.btnConvertPrivateGroup) {
                onClickConvertPrivateGroup();
            }
        }
    }

    private void onClickConvertPrivateGroup() {
        ShowConvertDialog.showDialog(getFragmentManager());
    }

    private void onClickBtnDone() {
        if (!StringUtil.isEmptyOrNull(this.mGroupId)) {
            String obj = this.mEdtTopic.getText().toString();
            if (!StringUtil.isEmptyOrNull(obj) && obj.trim().length() != 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                    if (groupById == null || obj.equalsIgnoreCase(groupById.getGroupName())) {
                        return;
                    }
                    if (zoomMessenger.checkGroupNameIsExist(obj)) {
                        new Builder(getActivity()).setMessage(C4558R.string.zm_mm_create_same_group_name_error_59554).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) null).show();
                        return;
                    }
                    zGroupProperty groupProperty = groupById.getGroupProperty();
                    if (groupProperty != null) {
                        if (zoomMessenger.modifyGroupProperty(this.mGroupId, obj, groupById.getGroupDesc(), groupProperty.getIsPublic(), groupProperty.getIsRestrictSameOrg(), groupProperty.getIsNewMemberCanSeeMessageHistory(), false)) {
                            showWaitingDialog();
                        }
                    }
                    showChangeTopicFailureMessage(1);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void doConvertPrivateGroup() {
        if (!StringUtil.isEmptyOrNull(this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < groupById.getBuddyCount(); i++) {
                        ZoomBuddy buddyAt = groupById.getBuddyAt(i);
                        if (buddyAt != null) {
                            arrayList.add(buddyAt.getJid());
                        }
                    }
                    int editGroupChat = zoomMessenger.editGroupChat(this.mGroupId, groupById.getGroupName(), arrayList, (groupById.getMucType() & -13 & -15) | 12);
                    if (editGroupChat == 0) {
                        showWaitingDialog();
                    } else if (editGroupChat == 2) {
                        showChangeTypeFailureMessage(1);
                    }
                }
            }
        }
    }

    private void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            this.mWaitingDialog = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            this.mWaitingDialog.setCancelable(true);
            this.mWaitingDialog.show(fragmentManager, "WaitingDialog");
        }
    }

    private void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            } else {
                ZMDialogFragment zMDialogFragment2 = this.mWaitingDialog;
                if (zMDialogFragment2 != null) {
                    try {
                        zMDialogFragment2.dismissAllowingStateLoss();
                    } catch (Exception unused) {
                    }
                }
            }
            this.mWaitingDialog = null;
        }
    }

    private void showChangeTopicFailureMessage(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
            } else if (i == 9) {
                updateData();
                showNoPrivilegeDialog();
            } else {
                Toast.makeText(activity, activity.getString(this.mIsGroupRoom ? C4558R.string.zm_mm_msg_channel_change_group_topic_failed_108993 : C4558R.string.zm_mm_msg_chat_group_topic_failed_108993), 1).show();
            }
        }
    }

    private void showNoPrivilegeDialog() {
        DialogUtils.showAlertDialog((ZMActivity) getActivity(), C4558R.string.zm_mm_set_muc_info_no_privilege_dialog_title_116724, C4558R.string.zm_mm_set_muc_info_no_privilege_dialog_msg_116724, C4558R.string.zm_btn_got_it);
    }

    private void showChangeTypeFailureMessage(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
            } else {
                Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_msg_convert_private_group_failed_59554), 1).show();
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    private void hideIME() {
        if (getActivity() != null) {
            this.mEdtTopic.clearFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService("input_method");
            if (inputMethodManager != null && inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(this.mEdtTopic.getWindowToken(), 0);
            }
        }
    }
}
