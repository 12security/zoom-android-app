package com.zipow.videobox.fragment.meeting;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.meeting.PromoteOrDowngradeItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class PromoteOrDowngradeMockFragment {
    private static final String KEY_CURRENT_ATTENDEE = "current_item";
    private static final String KEY_PENDING_ATTENDEE = "pending_item";
    @Nullable
    private PromoteOrDowngradeItem mCurPromoteOrDowngradeItem;
    @Nullable
    private PromoteOrDowngradeItem mPendingPromoteOrDowngradeItem;
    private final Fragment mZMFragment;

    public PromoteOrDowngradeMockFragment(Fragment fragment) {
        this.mZMFragment = fragment;
    }

    public void onCreateView(@Nullable Bundle bundle) {
        if (bundle != null) {
            this.mCurPromoteOrDowngradeItem = (PromoteOrDowngradeItem) bundle.getSerializable(KEY_CURRENT_ATTENDEE);
            this.mPendingPromoteOrDowngradeItem = (PromoteOrDowngradeItem) bundle.getSerializable(KEY_PENDING_ATTENDEE);
        }
    }

    public void onSaveInstanceState(@Nullable Bundle bundle) {
        if (bundle != null) {
            bundle.putSerializable(KEY_CURRENT_ATTENDEE, this.mCurPromoteOrDowngradeItem);
            bundle.putSerializable(PromoteOrDowngradeItem.class.getName(), this.mPendingPromoteOrDowngradeItem);
        }
    }

    public void promoteOrDowngrade(@NonNull PromoteOrDowngradeItem promoteOrDowngradeItem) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            if (confStatusObj.isConfLocked()) {
                confirmToUnlockMeeting(promoteOrDowngradeItem);
                return;
            }
            boolean z = true;
            if (promoteOrDowngradeItem.getmAction() != 1) {
                z = false;
            }
            if (!z ? ConfMgr.getInstance().downgradeToAttendee(promoteOrDowngradeItem.getmJid()) : ConfMgr.getInstance().promotePanelist(promoteOrDowngradeItem.getmJid())) {
                this.mCurPromoteOrDowngradeItem = (PromoteOrDowngradeItem) promoteOrDowngradeItem.clone();
                showWaitingDialog();
            }
        }
    }

    public long getCurUserId() {
        PromoteOrDowngradeItem promoteOrDowngradeItem = this.mCurPromoteOrDowngradeItem;
        if (promoteOrDowngradeItem == null) {
            return -1;
        }
        return promoteOrDowngradeItem.getmUserId();
    }

    private void showWaitingDialog() {
        Fragment fragment = this.mZMFragment;
        if (fragment != null) {
            FragmentManager fragmentManager = fragment.getFragmentManager();
            if (fragmentManager != null) {
                WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
                newInstance.setCancelable(true);
                newInstance.show(fragmentManager, "FreshWaitingDialog");
            }
        }
    }

    private void dismissWaitingDialog() {
        Fragment fragment = this.mZMFragment;
        if (fragment != null) {
            FragmentManager fragmentManager = fragment.getFragmentManager();
            if (fragmentManager != null) {
                ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("FreshWaitingDialog");
                if (zMDialogFragment != null) {
                    zMDialogFragment.dismissAllowingStateLoss();
                }
            }
        }
    }

    public void onPromotePanelistResult(int i) {
        dismissWaitingDialog();
        if (i != 0) {
            showErrorPromotePanelistMessage(i);
        } else {
            PromoteOrDowngradeItem promoteOrDowngradeItem = this.mCurPromoteOrDowngradeItem;
            if (promoteOrDowngradeItem != null && !StringUtil.isEmptyOrNull(promoteOrDowngradeItem.getmJid())) {
                showRejoinMessage(this.mCurPromoteOrDowngradeItem);
            }
        }
        this.mCurPromoteOrDowngradeItem = null;
    }

    public void onDePromotePanelist(int i) {
        dismissWaitingDialog();
        if (i != 0) {
            showErrorDePromotePanelistMessage(i);
        } else {
            PromoteOrDowngradeItem promoteOrDowngradeItem = this.mCurPromoteOrDowngradeItem;
            if (promoteOrDowngradeItem != null) {
                showRejoinMessage(promoteOrDowngradeItem);
            }
        }
        this.mCurPromoteOrDowngradeItem = null;
    }

    public void onConfLockStatusChanged() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && !confStatusObj.isConfLocked()) {
            checkContinuePromoteOrDePromote();
        }
    }

    private void showErrorPromotePanelistMessage(int i) {
        String str;
        Fragment fragment = this.mZMFragment;
        if (fragment != null) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                if (i != 3035) {
                    str = activity.getString(C4558R.string.zm_webinar_msg_failed_to_promote_panelist, new Object[]{Integer.valueOf(i)});
                } else {
                    CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                    str = activity.getString(C4558R.string.zm_webinar_msg_failed_to_promote_max_panelists, new Object[]{Integer.valueOf(confContext != null ? confContext.getParticipantLimit() : 0)});
                }
                new Builder(activity).setTitle((CharSequence) str).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
            }
        }
    }

    private void showErrorDePromotePanelistMessage(int i) {
        Fragment fragment = this.mZMFragment;
        if (fragment != null) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                new Builder(activity).setTitle((CharSequence) activity.getString(C4558R.string.zm_webinar_msg_failed_to_downgrade_to_attendee, new Object[]{Integer.valueOf(i)})).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void unlockMeeting(PromoteOrDowngradeItem promoteOrDowngradeItem) {
        this.mPendingPromoteOrDowngradeItem = promoteOrDowngradeItem;
        ConfMgr.getInstance().handleConfCmd(59);
    }

    private void checkContinuePromoteOrDePromote() {
        PromoteOrDowngradeItem promoteOrDowngradeItem = this.mPendingPromoteOrDowngradeItem;
        if (promoteOrDowngradeItem != null) {
            promoteOrDowngrade(promoteOrDowngradeItem);
            this.mPendingPromoteOrDowngradeItem = null;
        }
    }

    private void confirmToUnlockMeeting(final PromoteOrDowngradeItem promoteOrDowngradeItem) {
        Fragment fragment = this.mZMFragment;
        if (fragment != null) {
            ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
            if (zMActivity != null) {
                new Builder(zMActivity).setTitle(C4558R.string.zm_webinar_msg_change_role_on_meeting_locked).setPositiveButton(C4558R.string.zm_mi_unlock_meeting, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PromoteOrDowngradeMockFragment.this.unlockMeeting(promoteOrDowngradeItem);
                    }
                }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
            }
        }
    }

    private void showRejoinMessage(@Nullable PromoteOrDowngradeItem promoteOrDowngradeItem) {
        if (promoteOrDowngradeItem != null && promoteOrDowngradeItem.getmName() != null) {
            Fragment fragment = this.mZMFragment;
            if (fragment != null) {
                FragmentActivity activity = fragment.getActivity();
                if (activity != null) {
                    Toast.makeText(activity, activity.getString(promoteOrDowngradeItem.getmAction() == 1 ? C4558R.string.zm_webinar_msg_user_will_rejoin_as_panelist : C4558R.string.zm_webinar_msg_user_will_rejoin_as_attendee, new Object[]{promoteOrDowngradeItem.getmName()}), 1).show();
                }
            }
        }
    }
}
