package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.eventbus.ZMChatSession;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSessionMoreOptionsFragment */
public class MMSessionMoreOptionsFragment extends ZMDialogFragment implements OnClickListener {
    public static final String EXTRA_ARGS_GROUP_JID = "groupJid";
    private static final int REQUEST_SELECT_ADMIN = 103;
    private static final int REQUEST_SELECT_ADMIN_AND_LEAVE = 101;
    public static final String RESULT_ARGS_UPDATE_SUCCESS = "updateSuccess";
    private static final String TAG = "MMSessionMoreOptionsFragment";
    private int mAssignAdminType = 0;
    private View mBtnClearHistory;
    /* access modifiers changed from: private */
    public View mBtnDeleteGroup;
    private View mBtnQuitGroup;
    private View mBtnTransferAdmin;
    private CheckedTextView mChkAccessHistory;
    /* access modifiers changed from: private */
    @Nullable
    public String mGroupId;
    private boolean mIsClearHistory;
    private boolean mOnlyTransfer = false;
    private View mOptionAccessHistory;
    private View mPanelAccessHistory;
    private View mPanelQuitGroup;
    private View mPanelTransferAdmin;
    private TextView mTxtClearHistory;
    private TextView mTxtDeleteGroup;
    private TextView mTxtQuitGroup;
    @Nullable
    private ZMDialogFragment mWaitingDialog;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void On_AssignGroupAdmins(int i, String str, String str2, List<String> list, long j) {
            MMSessionMoreOptionsFragment.this.On_AssignGroupAdmins(i, str, str2, list, j);
        }

        public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
            MMSessionMoreOptionsFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
            MMSessionMoreOptionsFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
            MMSessionMoreOptionsFragment.this.On_DestroyGroup(i, str, str2, str3, j);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMSessionMoreOptionsFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.MMSessionMoreOptionsFragment$AccessHistoryDialog */
    public static class AccessHistoryDialog extends ZMDialogFragment {
        public static void showDialog(FragmentManager fragmentManager) {
            AccessHistoryDialog accessHistoryDialog = new AccessHistoryDialog();
            accessHistoryDialog.setArguments(new Bundle());
            accessHistoryDialog.show(fragmentManager, AccessHistoryDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Builder negativeButton = new Builder(getActivity()).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AccessHistoryDialog.this.onClickDisableAccessHistory();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null);
            negativeButton.setMessage(C4558R.string.zm_msg_access_history_alert_42597);
            return negativeButton.create();
        }

        /* access modifiers changed from: private */
        public void onClickDisableAccessHistory() {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                MMSessionMoreOptionsFragment mMSessionMoreOptionsFragment = (MMSessionMoreOptionsFragment) fragmentManager.findFragmentByTag(MMSessionMoreOptionsFragment.class.getName());
                if (mMSessionMoreOptionsFragment != null) {
                    mMSessionMoreOptionsFragment.setGroupAccessHistory(false);
                }
            }
        }
    }

    public static void showAsActivity(Fragment fragment, String str, int i) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("groupJid", str);
            SimpleActivity.show(fragment, MMSessionMoreOptionsFragment.class.getName(), bundle, i, false, 1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_session_more_options, viewGroup, false);
        this.mChkAccessHistory = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAccessHistory);
        this.mPanelAccessHistory = inflate.findViewById(C4558R.C4560id.panelAccessHistory);
        this.mOptionAccessHistory = inflate.findViewById(C4558R.C4560id.optionAccessHistory);
        this.mPanelTransferAdmin = inflate.findViewById(C4558R.C4560id.panelTransferAdmin);
        this.mBtnTransferAdmin = inflate.findViewById(C4558R.C4560id.btnTransferAdmin);
        this.mBtnClearHistory = inflate.findViewById(C4558R.C4560id.btnClearHistory);
        this.mTxtClearHistory = (TextView) inflate.findViewById(C4558R.C4560id.txtClearHistory);
        this.mPanelQuitGroup = inflate.findViewById(C4558R.C4560id.panelQuitGroup);
        this.mBtnQuitGroup = inflate.findViewById(C4558R.C4560id.btnQuitGroup);
        this.mTxtQuitGroup = (TextView) inflate.findViewById(C4558R.C4560id.txtQuitGroup);
        this.mBtnDeleteGroup = inflate.findViewById(C4558R.C4560id.btnDeleteGroup);
        this.mTxtDeleteGroup = (TextView) inflate.findViewById(C4558R.C4560id.txtDeleteGroup);
        this.mOptionAccessHistory.setOnClickListener(this);
        this.mBtnTransferAdmin.setOnClickListener(this);
        this.mBtnClearHistory.setOnClickListener(this);
        this.mBtnQuitGroup.setOnClickListener(this);
        this.mBtnDeleteGroup.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
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

    /* access modifiers changed from: private */
    public void updateData() {
        if (!StringUtil.isEmptyOrNull(this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    this.mPanelAccessHistory.setVisibility((!groupById.isGroupOperatorable() || ZMIMUtils.isAnnouncement(this.mGroupId)) ? 8 : 0);
                    this.mPanelQuitGroup.setVisibility(ZMIMUtils.isAnnouncement(this.mGroupId) ? 8 : 0);
                    if ((groupById.getMucType() & 32) != 0) {
                        this.mChkAccessHistory.setChecked(false);
                    } else {
                        this.mChkAccessHistory.setChecked(true);
                    }
                    if (!groupById.isGroupOperatorable() || ZMIMUtils.isAnnouncement(this.mGroupId)) {
                        this.mPanelTransferAdmin.setVisibility(8);
                        this.mBtnDeleteGroup.setVisibility(8);
                        this.mTxtQuitGroup.setTextColor(getResources().getColor(C4558R.color.zm_ui_kit_color_red_E02828));
                    } else {
                        this.mPanelTransferAdmin.setVisibility(0);
                        this.mBtnDeleteGroup.setVisibility(0);
                        this.mTxtQuitGroup.setTextColor(getResources().getColor(C4558R.color.zm_setting_option));
                    }
                    if (groupById.isRoom()) {
                        this.mBtnQuitGroup.setContentDescription(getString(C4558R.string.zm_mm_btn_delete_and_quit_group_chat_59554));
                        this.mTxtQuitGroup.setText(getString(C4558R.string.zm_mm_btn_delete_and_quit_group_chat_59554));
                        this.mBtnDeleteGroup.setContentDescription(getString(C4558R.string.zm_mm_btn_delete_group_chat_59554));
                        this.mTxtDeleteGroup.setText(getString(C4558R.string.zm_mm_btn_delete_group_chat_59554));
                        this.mTxtClearHistory.setText(getString(C4558R.string.zm_mm_btn_clear_channel_history_59554));
                    } else {
                        this.mBtnQuitGroup.setContentDescription(getString(C4558R.string.zm_mm_btn_quit_muc_chat_108993));
                        this.mTxtQuitGroup.setText(getString(C4558R.string.zm_mm_btn_quit_muc_chat_108993));
                        this.mBtnDeleteGroup.setContentDescription(getString(C4558R.string.zm_mm_btn_delete_muc_chat_108993));
                        this.mTxtDeleteGroup.setText(getString(C4558R.string.zm_mm_btn_delete_muc_chat_108993));
                        this.mTxtClearHistory.setText(getString(C4558R.string.zm_mm_btn_clear_chat_history));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(final int i, final GroupAction groupAction, String str) {
        if (StringUtil.isSameString(groupAction.getGroupId(), this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    if (groupAction.getActionType() == 2 || groupAction.getActionType() == 5) {
                        if (StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                                AccessibilityUtil.announceForAccessibilityCompat(this.mBtnTransferAdmin, C4558R.string.zm_accessibility_leave_group_59554);
                            }
                            getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAction.ACTION_DELETE_GROUP") {
                                public void run(IUIElement iUIElement) {
                                    MMSessionMoreOptionsFragment mMSessionMoreOptionsFragment = (MMSessionMoreOptionsFragment) iUIElement;
                                    if (mMSessionMoreOptionsFragment != null) {
                                        mMSessionMoreOptionsFragment.handleGroupActionDeleteGroup(i, groupAction);
                                    }
                                }
                            });
                        }
                    } else if (groupAction.getActionType() == 6) {
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
                                    MMSessionMoreOptionsFragment mMSessionMoreOptionsFragment = (MMSessionMoreOptionsFragment) iUIElement;
                                    if (mMSessionMoreOptionsFragment != null) {
                                        mMSessionMoreOptionsFragment.handleGroupAction(i, groupAction);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionDeleteGroup(int i, GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            finishFragment(true);
        } else {
            showQuitGroupFailureMessage(i);
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupAction(int i, @NonNull GroupAction groupAction) {
        dismissWaitingDialog();
        if (i == 0) {
            updateData();
        } else if (groupAction.getActionType() == 6) {
            showChangeTypeFailureMessage(i);
        }
    }

    /* access modifiers changed from: private */
    public void On_DestroyGroup(final int i, String str, String str2, String str3, long j) {
        if (StringUtil.isSameString(str2, this.mGroupId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("DestroyGroup") {
                public void run(IUIElement iUIElement) {
                    MMSessionMoreOptionsFragment.this.dismissWaitingDialog();
                    ZMActivity zMActivity = (ZMActivity) MMSessionMoreOptionsFragment.this.getActivity();
                    if (i == 0) {
                        if (AccessibilityUtil.isSpokenFeedbackEnabled(MMSessionMoreOptionsFragment.this.getContext())) {
                            AccessibilityUtil.announceForAccessibilityCompat(MMSessionMoreOptionsFragment.this.mBtnDeleteGroup, C4558R.string.zm_accessibility_delete_group_59554);
                        }
                        MMSessionMoreOptionsFragment.this.finishFragment(true);
                    } else if (zMActivity != null) {
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            ZoomGroup groupById = zoomMessenger.getGroupById(MMSessionMoreOptionsFragment.this.mGroupId);
                            if (groupById != null) {
                                Toast.makeText(zMActivity, zMActivity.getString(groupById.isRoom() ? C4558R.string.zm_mm_msg_destory_channel_failed_59554 : C4558R.string.zm_mm_msg_destory_muc_failed_59554, new Object[]{Integer.valueOf(i)}), 1).show();
                            }
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("NotifyGroupDestroy") {
                public void run(IUIElement iUIElement) {
                    MMSessionMoreOptionsFragment.this.finishFragment(true);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            updateData();
        }
    }

    /* access modifiers changed from: private */
    public void On_AssignGroupAdmins(final int i, String str, String str2, @Nullable List<String> list, long j) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                dismissWaitingDialog();
                if (this.mAssignAdminType == 103 && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                    String str3 = "";
                    if (list != null && list.size() > 0) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID((String) list.get(0));
                        if (buddyWithJID != null) {
                            str3 = buddyWithJID.getScreenName();
                        }
                    }
                    AccessibilityUtil.announceForAccessibilityCompat(this.mBtnTransferAdmin, (CharSequence) String.format(getResources().getString(C4558R.string.zm_accessibility_transfer_admin_45931), new Object[]{str3}));
                }
                if (StringUtil.isSameString(str, myself.getJid()) && i == 0 && this.mAssignAdminType == 101) {
                    quitGroup();
                } else {
                    getNonNullEventTaskManagerOrThrowException().push(new EventAction("GroupAdminTransfer") {
                        public void run(IUIElement iUIElement) {
                            ZMActivity zMActivity = (ZMActivity) MMSessionMoreOptionsFragment.this.getActivity();
                            if (i == 0) {
                                MMSessionMoreOptionsFragment.this.updateData();
                            } else if (zMActivity != null) {
                                Toast.makeText(zMActivity, zMActivity.getString(C4558R.string.zm_mm_msg_assign_admin_failed, new Object[]{Integer.valueOf(i)}), 1).show();
                            }
                        }
                    });
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void dismissWaitingDialog() {
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

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mGroupId = getArguments().getString("groupJid");
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if ((i == 101 || i == 103) && i2 == -1 && intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
            if (!CollectionsUtil.isListEmpty(arrayList)) {
                ArrayList arrayList2 = new ArrayList();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    arrayList2.add(((IMAddrBookItem) it.next()).getJid());
                }
                transferAdmin(arrayList2, i == 101);
            }
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            if (view.getId() == C4558R.C4560id.btnBack) {
                dismiss();
            } else if (view == this.mBtnTransferAdmin) {
                onClickBtnTransferAdmin(103);
            } else if (view == this.mBtnClearHistory) {
                onClickBtnClearHistory();
            } else if (view == this.mBtnQuitGroup) {
                onClickBtnQuitGroup();
            } else if (view == this.mBtnDeleteGroup) {
                onClickBtnDeleteGroup();
            } else if (view == this.mOptionAccessHistory) {
                onChkAccessHistory();
            }
        }
    }

    private void onChkAccessHistory() {
        if (this.mChkAccessHistory.isChecked()) {
            AccessHistoryDialog.showDialog(getFragmentManager());
        } else {
            setGroupAccessHistory(true);
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnTransferAdmin(int i) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            this.mOnlyTransfer = i == 103;
            this.mAssignAdminType = i;
            SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
            selectContactsParamter.title = zMActivity.getString(C4558R.string.zm_mm_title_select_a_contact);
            selectContactsParamter.btnOkText = zMActivity.getString(C4558R.string.zm_btn_ok);
            selectContactsParamter.groupId = this.mGroupId;
            selectContactsParamter.isContainsAllInGroup = false;
            selectContactsParamter.includeRobot = false;
            selectContactsParamter.isSingleChoice = true;
            MMSelectContactsActivity.show((Fragment) this, selectContactsParamter, i, (Bundle) null);
        }
    }

    private void transferAdmin(List<String> list, boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood()) {
                showConnectionError();
                return;
            }
            if (z) {
                zoomMessenger.assignGroupAdmins(this.mGroupId, list);
            } else {
                zoomMessenger.assignGroupAdminsV2(this.mGroupId, list);
            }
            showWaitingDialog();
        }
    }

    private void onClickBtnClearHistory() {
        ZoomLogEventTracking.eventTrackClearHistory(true);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null) {
                    new Builder(activity).setTitle(!groupById.isRoom() ? C4558R.string.zm_mm_msg_delete_p2p_chat_history_confirm : C4558R.string.zm_mm_msg_delete_group_chat_history_confirm_59554).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MMSessionMoreOptionsFragment.this.clearHistory();
                        }
                    }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create().show();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void clearHistory() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mGroupId);
            if (sessionById != null && sessionById.clearAllMessages()) {
                if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                    AccessibilityUtil.announceForAccessibilityCompat(this.mBtnClearHistory, C4558R.string.zm_accessibility_history_clear_22864);
                }
                EventBus.getDefault().post(new ZMChatSession(this.mGroupId, 1));
            }
        }
    }

    private void onClickBtnQuitGroup() {
        FragmentActivity activity = getActivity();
        if (activity != null && !StringUtil.isEmptyOrNull(this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (groupById != null && zoomMessenger.getMyself() != null) {
                    if (groupById.isGroupOperatorable()) {
                        new Builder(activity).setTitle(C4558R.string.zm_msg_delete_by_admin_59554).setCancelable(true).setPositiveButton(C4558R.string.zm_mm_lbl_transfer_admin_131024, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MMSessionMoreOptionsFragment.this.onClickBtnTransferAdmin(101);
                            }
                        }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create().show();
                    } else {
                        new Builder(activity).setTitle(groupById.isRoom() ? C4558R.string.zm_mm_msg_quit_group_confirm_59554 : C4558R.string.zm_mm_msg_quit_muc_confirm_59554).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MMSessionMoreOptionsFragment.this.quitGroup();
                            }
                        }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create().show();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void quitGroup() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood()) {
                showConnectionError();
                return;
            }
            if (zoomMessenger.deleteGroup(this.mGroupId)) {
                showWaitingDialog();
            } else {
                showQuitGroupFailureMessage(1);
            }
        }
    }

    private void showQuitGroupFailureMessage(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 10) {
                showConnectionError();
                return;
            }
            Toast.makeText(activity, activity.getString(C4558R.string.zm_mm_msg_quit_group_failed_59554, new Object[]{Integer.valueOf(i)}), 1).show();
        }
    }

    private void onClickBtnDeleteGroup() {
        if (getActivity() != null && !StringUtil.isEmptyOrNull(this.mGroupId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mGroupId);
                if (!(groupById == null || zoomMessenger.getMyself() == null || !groupById.isGroupOperatorable())) {
                    confirmDestoryGroup();
                }
            }
        }
    }

    private void confirmDestoryGroup() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            new Builder(activity).setTitle(C4558R.string.zm_msg_confirm_disband_59554).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_continue_disband, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMSessionMoreOptionsFragment.this.destroyGroup();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create().show();
        }
    }

    /* access modifiers changed from: private */
    public void destroyGroup() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || StringUtil.isEmptyOrNull(this.mGroupId)) {
            return;
        }
        if (!zoomMessenger.isConnectionGood()) {
            showConnectionError();
        } else {
            zoomMessenger.destroyGroup(this.mGroupId);
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

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    /* access modifiers changed from: private */
    public void setGroupAccessHistory(boolean z) {
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
                    int mucType = groupById.getMucType();
                    int editGroupChat = zoomMessenger.editGroupChat(this.mGroupId, groupById.getGroupName(), arrayList, !z ? mucType | 32 : mucType & -33);
                    if (editGroupChat == 0) {
                        showWaitingDialog();
                    } else if (editGroupChat == 2) {
                        showChangeTypeFailureMessage(1);
                    }
                }
            }
        }
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
}
