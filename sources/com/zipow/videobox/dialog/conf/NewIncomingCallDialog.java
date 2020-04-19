package com.zipow.videobox.dialog.conf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class NewIncomingCallDialog extends ZMDialogFragment {
    private static final String ARG_INVITATION = "invitation";
    /* access modifiers changed from: private */
    @Nullable
    public InvitationItem mInvitation;

    public NewIncomingCallDialog() {
        setCancelable(true);
    }

    public static void showDialog(@NonNull ConfActivity confActivity, @NonNull InvitationItem invitationItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("invitation", invitationItem);
        NewIncomingCallDialog newIncomingCallDialog = new NewIncomingCallDialog();
        newIncomingCallDialog.setArguments(bundle);
        newIncomingCallDialog.show(confActivity.getSupportFragmentManager(), NewIncomingCallDialog.class.getName());
    }

    public static void closeDialog(@NonNull ConfActivity confActivity, long j) {
        FragmentManager supportFragmentManager = confActivity.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            NewIncomingCallDialog newIncomingCallDialog = (NewIncomingCallDialog) supportFragmentManager.findFragmentByTag(NewIncomingCallDialog.class.getName());
            if (newIncomingCallDialog != null) {
                InvitationItem invitationItem = newIncomingCallDialog.mInvitation;
                if (invitationItem != null && invitationItem.getMeetingNumber() == j) {
                    newIncomingCallDialog.dismiss();
                }
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        String str;
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.mInvitation = (InvitationItem) arguments.getSerializable("invitation");
        InvitationItem invitationItem = this.mInvitation;
        if (invitationItem == null) {
            return new Builder(getActivity()).create();
        }
        if (TextUtils.isEmpty(invitationItem.getGroupID()) || TextUtils.isEmpty(this.mInvitation.getGroupName())) {
            str = getResources().getString(C4558R.string.zm_msg_calling_new_11_54639);
        } else {
            str = getResources().getString(C4558R.string.zm_msg_calling_new_group_54639, new Object[]{this.mInvitation.getGroupName(), Integer.valueOf(this.mInvitation.getGroupmembercount())});
        }
        return new Builder(getActivity()).setTitle((CharSequence) this.mInvitation.getFromUserScreenName()).setMessage(str).setNegativeButton(C4558R.string.zm_btn_decline, (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton(C4558R.string.zm_btn_accept, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                NewIncomingCallDialog newIncomingCallDialog = NewIncomingCallDialog.this;
                newIncomingCallDialog.acceptNewIncomingCall(newIncomingCallDialog.mInvitation);
            }
        }).create();
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        InvitationItem invitationItem = this.mInvitation;
        if (invitationItem != null) {
            declineNewIncomingCall(invitationItem);
        }
    }

    /* access modifiers changed from: private */
    public void acceptNewIncomingCall(InvitationItem invitationItem) {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            IntegrationActivity.acceptNewIncomingCall(confActivity.getApplicationContext(), invitationItem);
            ConfLocalHelper.leaveCall(confActivity);
        }
    }

    private void declineNewIncomingCall(InvitationItem invitationItem) {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            IntegrationActivity.declineNewIncomingCall(confActivity.getApplicationContext(), invitationItem);
        }
    }
}
