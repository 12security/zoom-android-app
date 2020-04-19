package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class RemoveBuddyFromGroupDialogFragment extends DialogFragment {
    private static final String ARG_BUDDY = "ARG_BUDDY";
    private static final String ARG_GROUP = "ARG_GROUP";

    @Nullable
    public static RemoveBuddyFromGroupDialogFragment newInstance(@Nullable IMAddrBookItem iMAddrBookItem, @Nullable MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (iMAddrBookItem == null || mMZoomBuddyGroup == null) {
            return null;
        }
        RemoveBuddyFromGroupDialogFragment removeBuddyFromGroupDialogFragment = new RemoveBuddyFromGroupDialogFragment();
        removeBuddyFromGroupDialogFragment.setCancelable(true);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_BUDDY, iMAddrBookItem);
        bundle.putSerializable(ARG_GROUP, mMZoomBuddyGroup);
        removeBuddyFromGroupDialogFragment.setArguments(bundle);
        return removeBuddyFromGroupDialogFragment;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return new Builder(getActivity()).create();
        }
        final IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) arguments.getSerializable(ARG_BUDDY);
        final MMZoomBuddyGroup mMZoomBuddyGroup = (MMZoomBuddyGroup) arguments.getSerializable(ARG_GROUP);
        return new Builder(getActivity()).setTitle(C4558R.string.zm_msg_hint_remove_buddy_from_group_68451).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (!(iMAddrBookItem == null || zoomMessenger == null)) {
                    MMZoomBuddyGroup mMZoomBuddyGroup = mMZoomBuddyGroup;
                    if (mMZoomBuddyGroup != null && mMZoomBuddyGroup.getType() == 500) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(iMAddrBookItem.getJid());
                        zoomMessenger.removeBuddyToPersonalBuddyGroup(arrayList, mMZoomBuddyGroup.getXmppGroupID());
                    }
                }
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).create();
    }
}
