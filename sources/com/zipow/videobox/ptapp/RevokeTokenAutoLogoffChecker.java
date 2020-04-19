package com.zipow.videobox.ptapp;

import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTUI.INotifyZAKListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMActivity.GlobalActivityListener;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class RevokeTokenAutoLogoffChecker {
    private static final int MSG_REVOKE_TOKEN = 1;
    private static final int MSG_ZAK_ERROR = 2;
    private static final String TAG = "RevokeTokenAutoLogoffChecker";
    private static RevokeTokenAutoLogoffChecker instance;
    @NonNull
    private GlobalActivityListener mGlobalActivityListener = new GlobalActivityListener() {
        public void onUIMoveToBackground() {
        }

        public void onUserActivityOnUI() {
        }

        public void onActivityMoveToFront(ZMActivity zMActivity) {
            RevokeTokenAutoLogoffChecker.this.checkSignout();
        }
    };
    @NonNull
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null && !zoomMessenger.isConnectionGood() && zoomMessenger.isForceSignout()) {
                        RevokeTokenAutoLogoffChecker.this.mIsSignout = true;
                        PTApp.getInstance().logout(0);
                        ZMActivity frontActivity = ZMActivity.getFrontActivity();
                        if (frontActivity != null && frontActivity.isActive()) {
                            RevokeTokenAutoLogoffChecker.this.checkSignout();
                            break;
                        }
                    }
                case 2:
                    RevokeTokenAutoLogoffChecker.this.mIsSignout = true;
                    PTApp.getInstance().logout(0);
                    ZMActivity frontActivity2 = ZMActivity.getFrontActivity();
                    if (frontActivity2 != null && frontActivity2.isActive()) {
                        RevokeTokenAutoLogoffChecker.this.checkSignout();
                        break;
                    }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsSignout;
    @NonNull
    private IZoomMessengerUIListener mListener = new SimpleZoomMessengerUIListener() {
        public void onConnectReturn(int i) {
            RevokeTokenAutoLogoffChecker.this.checkRevokeToken();
        }
    };
    @NonNull
    private INotifyZAKListener mNotifyZAKLListener = new INotifyZAKListener() {
        public void notifyZAKRefreshFailed(int i) {
            if (i == 1001 || i == 1134) {
                RevokeTokenAutoLogoffChecker.this.zakExpired();
            }
        }
    };

    public static class RovokeTokenDialog extends ZMDialogFragment {
        public static void show(@Nullable ZMActivity zMActivity) {
            if (zMActivity != null) {
                RovokeTokenDialog rovokeTokenDialog = new RovokeTokenDialog();
                rovokeTokenDialog.setArguments(new Bundle());
                rovokeTokenDialog.show(zMActivity.getSupportFragmentManager(), RovokeTokenDialog.class.getName());
            }
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_lbl_revoke_token_25029).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) null).create();
        }
    }

    public static RevokeTokenAutoLogoffChecker getInstance() {
        if (instance == null) {
            instance = new RevokeTokenAutoLogoffChecker();
        }
        return instance;
    }

    private void stopConference() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !zoomMessenger.isConnectionGood() && zoomMessenger.isForceSignout()) {
            IConfService confService = VideoBoxApplication.getInstance().getConfService();
            if (confService != null) {
                try {
                    confService.leaveCurrentMeeting(false);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    private RevokeTokenAutoLogoffChecker() {
    }

    /* access modifiers changed from: private */
    public void checkRevokeToken() {
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: private */
    public void zakExpired() {
        this.mHandler.removeMessages(2);
        this.mHandler.sendEmptyMessage(2);
    }

    /* access modifiers changed from: private */
    public void checkSignout() {
        if (this.mIsSignout) {
            this.mIsSignout = false;
            if (!PTApp.getInstance().isWebSignedOn()) {
                int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
                if (inProcessActivityCountInStack > 0) {
                    for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                        ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                        if (!(inProcessActivityInStackAt instanceof ConfActivityNormal) && inProcessActivityInStackAt != null) {
                            inProcessActivityInStackAt.finish();
                        }
                    }
                }
                LoginActivity.show(VideoBoxApplication.getGlobalContext(), false, true);
            }
        }
    }

    public void startChecker() {
        ZMActivity.addGlobalActivityListener(this.mGlobalActivityListener);
        ZoomMessengerUI.getInstance().addListener(this.mListener);
        PTUI.getInstance().addINotifyZAKListener(this.mNotifyZAKLListener);
    }

    public void stopChecker() {
        ZMActivity.removeGlobalActivityListener(this.mGlobalActivityListener);
        ZoomMessengerUI.getInstance().removeListener(this.mListener);
    }
}
