package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class LoginAsHostAlertDialog extends ZMDialogFragment {
    @NonNull
    private static String TAG = "LoginAsHostAlertDialog";

    public static void showLoginAsHostAlertDialog(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null && ConfMgr.getInstance().isConfConnected() && ConfMgr.getInstance().getConfContext() != null) {
            LoginAsHostAlertDialog loginAsHostAlertDialog = new LoginAsHostAlertDialog();
            loginAsHostAlertDialog.setArguments(new Bundle());
            loginAsHostAlertDialog.show(zMActivity.getSupportFragmentManager(), LoginAsHostAlertDialog.class.getName());
        }
    }

    public LoginAsHostAlertDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return createEmptyDialog();
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null) {
            return createEmptyDialog();
        }
        String meetingHostName = meetingItem.getMeetingHostName();
        if (meetingHostName == null) {
            meetingHostName = "";
        }
        final String joinMeetingUrl = meetingItem.getJoinMeetingUrl();
        return new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_msg_login_as_host, meetingHostName, meetingHostName)).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginAsHostAlertDialog.this.loginAsHost(joinMeetingUrl);
            }
        }).create();
    }

    /* access modifiers changed from: private */
    public void loginAsHost(String str) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, IMActivity.class);
            intent.setAction(IMActivity.ACTION_LOGIN_AS_HOST);
            intent.putExtra(IMActivity.ARG_JOIN_MEETING_URL, str);
            intent.addFlags(131072);
            ActivityStartHelper.startActivityForeground(zMActivity, intent);
            zMActivity.finish();
        }
    }
}
