package com.zipow.videobox.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.RoomDevice;
import com.zipow.videobox.view.CallRoomView;
import com.zipow.videobox.view.CallRoomView.Listener;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.videomeetings.C4558R;

public class CallRoomFragment extends ZMDialogFragment implements Listener, IPTUIListener {
    public static final String ARG_CONF_NUMBER = "hangoutNumber";
    public static final int ARG_RES_CODE_RMDEVICE_ITEM_SELECTED = 0;
    public static final String ARG_ROOM_DEVICE = "roomDeviceDate";
    public static final String ARG_URL_ACTION = "urlAction";
    @Nullable
    private CallRoomView mView;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onPTAppEvent(int i, long j) {
    }

    public static void showJoinByNumber(FragmentManager fragmentManager, String str, String str2) {
        CallRoomFragment callRoomFragment = new CallRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hangoutNumber", str);
        callRoomFragment.setArguments(bundle);
        callRoomFragment.show(fragmentManager, CallRoomFragment.class.getName());
    }

    public CallRoomFragment() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mView = new CallRoomView(getActivity());
        this.mView.setListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString("hangoutNumber");
            if (string != null && string.length() > 0) {
                this.mView.setConfNumber(string);
            }
        }
        PTUI.getInstance().addPTUIListener(this);
        return this.mView;
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroyView() {
        PTUI.getInstance().removePTUIListener(this);
        PTApp.getInstance().setVideoCallWithRoomSystemPrepareStatus(false);
        super.onDestroyView();
    }

    public void onBack() {
        finish(false);
    }

    private void finish(boolean z) {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
            activity.overridePendingTransition(C4558R.anim.zm_fade_in, C4558R.anim.zm_slide_out_bottom);
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (getShowsDialog()) {
            FragmentActivity activity = getActivity();
            if (activity instanceof JoinByURLActivity) {
                activity.finish();
            }
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (intent != null && this.mView != null) {
            this.mView.setRoomDevice((RoomDevice) intent.getParcelableExtra(ARG_ROOM_DEVICE));
        }
    }
}
