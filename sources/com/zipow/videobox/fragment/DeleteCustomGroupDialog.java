package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class DeleteCustomGroupDialog extends ZMDialogFragment {
    private static final String ARG_GROUP = "GROUP";

    @Nullable
    public static DeleteCustomGroupDialog newInstance(@Nullable MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (mMZoomBuddyGroup == null || mMZoomBuddyGroup.getBuddyCount() == 0) {
            return null;
        }
        DeleteCustomGroupDialog deleteCustomGroupDialog = new DeleteCustomGroupDialog();
        deleteCustomGroupDialog.setCancelable(true);
        Bundle bundle = new Bundle();
        bundle.putString(ARG_GROUP, mMZoomBuddyGroup.getXmppGroupID());
        deleteCustomGroupDialog.setArguments(bundle);
        return deleteCustomGroupDialog;
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        final String str;
        Bundle arguments = getArguments();
        if (arguments == null) {
            str = null;
        } else {
            str = arguments.getString(ARG_GROUP);
        }
        return new Builder(getActivity()).setTitle(C4558R.string.zm_msg_custom_group_not_empty_68451).setPositiveButton(C4558R.string.zm_mm_lbl_delete_group_68451, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    zoomMessenger.deletePersonalBuddyGroup(str);
                }
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).create();
    }
}
