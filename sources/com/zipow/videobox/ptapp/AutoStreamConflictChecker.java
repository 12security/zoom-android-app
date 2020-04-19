package com.zipow.videobox.ptapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMActivity.GlobalActivityListener;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class AutoStreamConflictChecker {
    private static AutoStreamConflictChecker mInstance;
    @NonNull
    private GlobalActivityListener mGlobalActivityListener = new GlobalActivityListener() {
        public void onUIMoveToBackground() {
        }

        public void onUserActivityOnUI() {
        }

        public void onActivityMoveToFront(ZMActivity zMActivity) {
            AutoStreamConflictChecker.this.checkStreamConflict(zMActivity);
        }
    };
    private boolean mHasStreamConflict = false;
    @NonNull
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onConnectReturn(int i) {
            AutoStreamConflictChecker.this.onZoomMessengerConnectReturn();
        }
    };

    public static class ShowAlertDialog extends ZMDialogFragment {
        /* access modifiers changed from: private */
        public void forceConnectMessenger() {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.forceSignon();
            }
        }

        public static void showDialog(FragmentManager fragmentManager) {
            new ShowAlertDialog().show(fragmentManager, ShowAlertDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            getArguments();
            ZMAlertDialog create = new Builder(getActivity()).setTitle(C4558R.string.zm_mm_msg_stream_conflict_msg).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_btn_sign_in_again, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ShowAlertDialog.this.forceConnectMessenger();
                }
            }).create();
            create.setCanceledOnTouchOutside(false);
            return create;
        }
    }

    private AutoStreamConflictChecker() {
    }

    public static synchronized AutoStreamConflictChecker getInstance() {
        AutoStreamConflictChecker autoStreamConflictChecker;
        synchronized (AutoStreamConflictChecker.class) {
            if (mInstance == null) {
                mInstance = new AutoStreamConflictChecker();
            }
            autoStreamConflictChecker = mInstance;
        }
        return autoStreamConflictChecker;
    }

    public void startChecker() {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null && instance.isPTApp()) {
            ZMActivity.addGlobalActivityListener(this.mGlobalActivityListener);
            ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        }
    }

    public void stopChecker() {
        ZMActivity.removeGlobalActivityListener(this.mGlobalActivityListener);
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    /* access modifiers changed from: private */
    public void checkStreamConflict(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (!zMActivity.isActive() || zoomMessenger.isConnectionGood() || !zoomMessenger.isStreamConflict()) {
                    if (!zoomMessenger.isStreamConflict()) {
                        this.mHasStreamConflict = false;
                    }
                } else if (!this.mHasStreamConflict) {
                    this.mHasStreamConflict = true;
                    showStreamConflictMessage(zMActivity);
                }
            }
        }
    }

    public void showStreamConflictMessage(@Nullable FragmentActivity fragmentActivity) {
        if (fragmentActivity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (!(zoomMessenger == null || zoomMessenger.getStreamConflictReason() == 1)) {
                ShowAlertDialog.showDialog(fragmentActivity.getSupportFragmentManager());
            }
        }
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerConnectReturn() {
        checkStreamConflict(ZMActivity.getFrontActivity());
    }
}
