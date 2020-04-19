package com.zipow.videobox;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.VideoBoxApplication.IConfProcessListener;
import com.zipow.videobox.dialog.FreeMeetingEndDialog;
import com.zipow.videobox.ptapp.LogoutHandler;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.UpgradeUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MeetingEndMessageActivity extends ZMActivity implements IConfProcessListener, IPTUIListener {
    public static final String ACTION_SHOW_CMR_NOTIFICATION;
    public static final String ACTION_SHOW_DEVICE_NOT_SUPPORTED;
    public static final String ACTION_SHOW_FREE_TIME_OUT_FOR_ORIGINAL_HOST;
    public static final String ACTION_SHOW_LEAVING_MESSAGE;
    public static final String ACTION_SHOW_MEETING_ENDED_MESSAGE;
    public static final String ACTION_SHOW_TOKEN_EXPIRED;
    public static final String ARG_ENDMEETING_REASON = "endMeetingReason";
    public static final String ARG_ENDMEETING_REASON_CODE = "endMeetingCode";
    public static final String ARG_GIFT_MEETING_COUNT = "giftMeetingCount";
    public static final String ARG_LEAVING_MESSAGE = "leavingMessage";
    public static final String ARG_UPGRADE_URL = "upgradeUrl";
    private static final String TAG = "MeetingEndMessageActivity";
    /* access modifiers changed from: private */
    public Dialog mDialog;
    @Nullable
    private WaitingDialog mWaitingDialog;

    public static class ExpeledDialog extends ZMDialogFragment {
        /* access modifiers changed from: private */
        public Button mBtnOK;
        /* access modifiers changed from: private */
        public String mButtonOkString;
        /* access modifiers changed from: private */
        public Handler mHandler;
        /* access modifiers changed from: private */
        public int mTimeOutSeconds = 0;
        /* access modifiers changed from: private */
        @Nullable
        public Runnable timeOutRunnalbe = new Runnable() {
            public void run() {
                ZMAlertDialog zMAlertDialog = (ZMAlertDialog) ExpeledDialog.this.getDialog();
                if (zMAlertDialog != null) {
                    if (ExpeledDialog.this.mTimeOutSeconds > 0) {
                        ExpeledDialog.this.mBtnOK = zMAlertDialog.getButton(-1);
                        String num = Integer.toString(ExpeledDialog.this.mTimeOutSeconds);
                        Button access$700 = ExpeledDialog.this.mBtnOK;
                        StringBuilder sb = new StringBuilder();
                        sb.append(ExpeledDialog.this.mButtonOkString);
                        sb.append(" ( ");
                        sb.append(num);
                        sb.append(" ) ");
                        access$700.setText(sb.toString());
                        ExpeledDialog.this.mTimeOutSeconds = ExpeledDialog.this.mTimeOutSeconds - 1;
                        ExpeledDialog.this.mHandler.postDelayed(this, 1000);
                    } else {
                        FragmentActivity activity = ExpeledDialog.this.getActivity();
                        if (activity != null) {
                            activity.finish();
                        }
                    }
                }
            }
        };

        public ExpeledDialog() {
            setCancelable(false);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            this.mTimeOutSeconds = 5;
            this.mButtonOkString = getString(C4558R.string.zm_btn_ok);
            this.mHandler = new Handler();
            Runnable runnable = this.timeOutRunnalbe;
            if (runnable != null) {
                this.mHandler.postDelayed(runnable, 1000);
            }
            return new Builder(getActivity()).setTitle(C4558R.string.zm_msg_expeled_by_host_44379).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ExpeledDialog.this.mTimeOutSeconds = 0;
                    if (ExpeledDialog.this.mHandler != null) {
                        ExpeledDialog.this.mHandler.removeCallbacks(ExpeledDialog.this.timeOutRunnalbe);
                    }
                    FragmentActivity activity = ExpeledDialog.this.getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            }).create();
        }

        public void onDestroy() {
            this.mTimeOutSeconds = 0;
            Handler handler = this.mHandler;
            if (handler != null) {
                Runnable runnable = this.timeOutRunnalbe;
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
            }
            super.onDestroy();
        }
    }

    public static class MeetingEndDialog extends ZMDialogFragment {
        /* access modifiers changed from: private */
        public Button mBtnOK;
        /* access modifiers changed from: private */
        public String mButtonOkString;
        /* access modifiers changed from: private */
        public Handler mHandler;
        /* access modifiers changed from: private */
        public int mTimeOutSeconds = 0;
        /* access modifiers changed from: private */
        @NonNull
        public Runnable timeOutRunnalbe = new Runnable() {
            public void run() {
                ZMAlertDialog zMAlertDialog = (ZMAlertDialog) MeetingEndDialog.this.getDialog();
                if (zMAlertDialog != null) {
                    if (MeetingEndDialog.this.mTimeOutSeconds > 0) {
                        MeetingEndDialog.this.mBtnOK = zMAlertDialog.getButton(-1);
                        String num = Integer.toString(MeetingEndDialog.this.mTimeOutSeconds);
                        Button access$1200 = MeetingEndDialog.this.mBtnOK;
                        StringBuilder sb = new StringBuilder();
                        sb.append(MeetingEndDialog.this.mButtonOkString);
                        sb.append(" ( ");
                        sb.append(num);
                        sb.append(" ) ");
                        access$1200.setText(sb.toString());
                        MeetingEndDialog.this.mTimeOutSeconds = MeetingEndDialog.this.mTimeOutSeconds - 1;
                        MeetingEndDialog.this.mHandler.postDelayed(this, 1000);
                    } else {
                        FragmentActivity activity = MeetingEndDialog.this.getActivity();
                        if (activity != null) {
                            activity.finish();
                        }
                    }
                }
            }
        };

        @NonNull
        public static MeetingEndDialog newMeetingEndDialog(int i) {
            MeetingEndDialog meetingEndDialog = new MeetingEndDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("msgId", i);
            meetingEndDialog.setArguments(bundle);
            return meetingEndDialog;
        }

        @NonNull
        public static MeetingEndDialog newMeetingEndDialog(String str) {
            MeetingEndDialog meetingEndDialog = new MeetingEndDialog();
            Bundle bundle = new Bundle();
            bundle.putString("message", str);
            meetingEndDialog.setArguments(bundle);
            return meetingEndDialog;
        }

        public MeetingEndDialog() {
            setCancelable(false);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            int i = 0;
            if (arguments != null) {
                i = arguments.getInt("msgId", 0);
            }
            String string = arguments != null ? arguments.getString("message") : "";
            this.mTimeOutSeconds = 5;
            if (i == 0) {
                i = C4558R.string.zm_msg_meeting_end;
            }
            this.mButtonOkString = getString(C4558R.string.zm_btn_ok);
            this.mHandler = new Handler();
            this.mHandler.postDelayed(this.timeOutRunnalbe, 1000);
            Builder builder = new Builder(getActivity());
            if (!StringUtil.isEmptyOrNull(string)) {
                builder.setTitle((CharSequence) string);
            } else {
                builder.setTitle(i);
            }
            builder.setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MeetingEndDialog.this.mTimeOutSeconds = 0;
                    if (MeetingEndDialog.this.mHandler != null) {
                        MeetingEndDialog.this.mHandler.removeCallbacks(MeetingEndDialog.this.timeOutRunnalbe);
                    }
                    FragmentActivity activity = MeetingEndDialog.this.getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            });
            return builder.create();
        }

        public void onDestroy() {
            this.mTimeOutSeconds = 0;
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeCallbacks(this.timeOutRunnalbe);
            }
            super.onDestroy();
        }
    }

    public static class TokenExpiredDialog extends ZMDialogFragment {
        public TokenExpiredDialog() {
            setCancelable(false);
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_title_meeting_cannot_start_46906).setMessage(C4558R.string.zm_msg_meeting_token_expired_46906).setPositiveButton(C4558R.string.zm_btn_login, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MeetingEndMessageActivity meetingEndMessageActivity = (MeetingEndMessageActivity) TokenExpiredDialog.this.getActivity();
                    if (meetingEndMessageActivity != null) {
                        meetingEndMessageActivity.notifyUIToLogOut();
                    }
                }
            }).setNegativeButton(C4558R.string.zm_btn_leave_meeting, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MeetingEndMessageActivity meetingEndMessageActivity = (MeetingEndMessageActivity) TokenExpiredDialog.this.getActivity();
                    if (meetingEndMessageActivity != null) {
                        meetingEndMessageActivity.doLeaveMeeting();
                    }
                }
            }).create();
        }
    }

    private boolean handleActionShowCMRNotification(Intent intent) {
        return false;
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(MeetingEndMessageActivity.class.getName());
        sb.append(".action.ACTION_SHOW_MEETING_ENDED_MESSAGE");
        ACTION_SHOW_MEETING_ENDED_MESSAGE = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(MeetingEndMessageActivity.class.getName());
        sb2.append(".action.ACTION_SHOW_LEAVING_MESSAGE");
        ACTION_SHOW_LEAVING_MESSAGE = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(MeetingEndMessageActivity.class.getName());
        sb3.append(".action.ACTION_SHOW_CMR_NOTIFICATION");
        ACTION_SHOW_CMR_NOTIFICATION = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(MeetingEndMessageActivity.class.getName());
        sb4.append(".action.ACTION_SHOW_FREE_TIME_OUT_FOR_ORIGINAL_HOST");
        ACTION_SHOW_FREE_TIME_OUT_FOR_ORIGINAL_HOST = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(MeetingEndMessageActivity.class.getName());
        sb5.append(".action.ACTION_SHOW_DEVICE_NOT_SUPPORTED");
        ACTION_SHOW_DEVICE_NOT_SUPPORTED = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(MeetingEndMessageActivity.class.getName());
        sb6.append(".action.ACTION_SHOW_TOKEN_EXPIRED");
        ACTION_SHOW_TOKEN_EXPIRED = sb6.toString();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PTUI.getInstance().addPTUIListener(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this);
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            instance.removeConfProcessListener(this);
        }
    }

    public void onResume() {
        super.onResume();
        if (VideoBoxApplication.getInstance() == null) {
            finish();
            return;
        }
        Intent intent = getIntent();
        String action = intent.getAction();
        boolean z = true;
        if (ACTION_SHOW_MEETING_ENDED_MESSAGE.equals(action)) {
            z = handleActionShowMeetingEndMessage(intent);
        } else if (ACTION_SHOW_LEAVING_MESSAGE.equalsIgnoreCase(action)) {
            z = handleActionShowLeavingMessage(intent);
        } else if (ACTION_SHOW_CMR_NOTIFICATION.equalsIgnoreCase(action)) {
            z = handleActionShowCMRNotification(intent);
        } else if (ACTION_SHOW_TOKEN_EXPIRED.equals(action)) {
            z = handleActionShowTokenExpired(intent);
        } else if (ACTION_SHOW_FREE_TIME_OUT_FOR_ORIGINAL_HOST.equalsIgnoreCase(action)) {
            z = handleActionShowFreeMeetingTimeOutForOrigianlHost(intent);
        } else if (ACTION_SHOW_DEVICE_NOT_SUPPORTED.equals(action)) {
            z = handleActionShowDeviceNotSupported();
        }
        intent.setAction(null);
        setIntent(intent);
        if (z) {
            finish();
        }
    }

    private boolean handleActionShowDeviceNotSupported() {
        if (this.mDialog == null) {
            this.mDialog = new Builder(this).setMessage(C4558R.string.zm_alert_link_error_content_106299).setTitle(C4558R.string.zm_alert_link_error_title_106299).setCancelable(false).setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    MeetingEndMessageActivity.this.finish();
                    VideoBoxApplication.getNonNullInstance().stopConfService();
                    MeetingEndMessageActivity.this.mDialog = null;
                }
            }).setVerticalOptionStyle(true).setNegativeButton(C4558R.string.zm_date_time_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setPositiveButton(C4558R.string.zm_alert_link_error_btn_106299, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    UpgradeUtil.upgradeByUrl(MeetingEndMessageActivity.this);
                    dialogInterface.dismiss();
                }
            }).create();
        }
        this.mDialog.show();
        return false;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.dismiss();
            this.mDialog = null;
        }
    }

    private boolean handleActionShowMeetingEndMessage(Intent intent) {
        int intExtra = intent.getIntExtra("endMeetingReason", 0);
        int intExtra2 = intent.getIntExtra(ARG_ENDMEETING_REASON_CODE, 0);
        switch (intExtra) {
            case 1:
                showExpeledDialog();
                break;
            case 2:
                showMeetingEndDialog();
                break;
            case 3:
                showJBHTimeoutDialog();
                break;
            case 4:
                showFreeMeetingTimeoutDialog();
                break;
            case 6:
                showEndByHostStartAnotherMeetingDialog();
                break;
            case 7:
                showEndBySDKConnectionBrokenDialog(intExtra2);
                break;
        }
        return false;
    }

    private boolean handleActionShowFreeMeetingTimeOutForOrigianlHost(Intent intent) {
        int intExtra = intent.getIntExtra(ARG_GIFT_MEETING_COUNT, -1);
        String stringExtra = intent.getStringExtra(ARG_UPGRADE_URL);
        if (intExtra <= 0 || StringUtil.isEmptyOrNull(stringExtra)) {
            showFreeMeetingTimeoutDialog();
        } else {
            FreeMeetingEndDialog.showDialog(getSupportFragmentManager(), intExtra, stringExtra);
        }
        return false;
    }

    public static void showMeetingEndedMessage(@NonNull Context context, int i) {
        showMeetingEndedMessage(context, i, 0);
    }

    public static void showFreeMeetingTimeOutForOriginalHost(@NonNull Context context, int i, @NonNull String str) {
        if (VideoBoxApplication.getInstance().isPTApp() && !VideoBoxApplication.getInstance().isSDKMode()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (!zoomMessenger.isConnectionGood() && zoomMessenger.isForceSignout()) {
                    return;
                }
            } else {
                return;
            }
        }
        Intent intent = new Intent(context, MeetingEndMessageActivity.class);
        intent.setFlags(1484783616);
        intent.setAction(ACTION_SHOW_FREE_TIME_OUT_FOR_ORIGINAL_HOST);
        intent.putExtra(ARG_GIFT_MEETING_COUNT, i);
        intent.putExtra(ARG_UPGRADE_URL, str);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    public static void showMeetingEndedMessage(@NonNull Context context, int i, int i2) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (!zoomMessenger.isConnectionGood() && zoomMessenger.isForceSignout()) {
                    return;
                }
            } else {
                return;
            }
        }
        Intent intent = new Intent(context, MeetingEndMessageActivity.class);
        intent.setFlags(1484783616);
        intent.setAction(ACTION_SHOW_MEETING_ENDED_MESSAGE);
        intent.putExtra("endMeetingReason", i);
        intent.putExtra(ARG_ENDMEETING_REASON_CODE, i2);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    private void showExpeledDialog() {
        new ExpeledDialog().showNow(getSupportFragmentManager(), ExpeledDialog.class.getSimpleName());
    }

    private void showMeetingEndDialog() {
        MeetingEndDialog.newMeetingEndDialog(0).showNow(getSupportFragmentManager(), MeetingEndDialog.class.getSimpleName());
    }

    private void showFreeMeetingTimeoutDialog() {
        MeetingEndDialog.newMeetingEndDialog(C4558R.string.zm_msg_free_meeting_timeout).showNow(getSupportFragmentManager(), MeetingEndDialog.class.getSimpleName());
    }

    private void showJBHTimeoutDialog() {
        MeetingEndDialog.newMeetingEndDialog(C4558R.string.zm_msg_jbh_meeting_timeout).showNow(getSupportFragmentManager(), MeetingEndDialog.class.getSimpleName());
    }

    private void showEndByHostStartAnotherMeetingDialog() {
        MeetingEndDialog.newMeetingEndDialog(C4558R.string.zm_msg_meeting_end_by_host_start_another_meeting).showNow(getSupportFragmentManager(), MeetingEndDialog.class.getSimpleName());
    }

    private void showEndBySDKConnectionBrokenDialog(int i) {
        MeetingEndDialog.newMeetingEndDialog(getString(C4558R.string.zm_msg_conffail_neterror_confirm, new Object[]{Integer.valueOf(i)})).showNow(getSupportFragmentManager(), MeetingEndDialog.class.getSimpleName());
    }

    private boolean handleActionShowLeavingMessage(Intent intent) {
        String stringExtra = intent.getStringExtra(ARG_LEAVING_MESSAGE);
        if (StringUtil.isEmptyOrNull(stringExtra)) {
            stringExtra = getString(C4558R.string.zm_msg_waiting);
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return false;
        }
        if (!VideoBoxApplication.getInstance().isConfProcessRunning()) {
            return true;
        }
        showWaitingDialog(stringExtra, supportFragmentManager);
        VideoBoxApplication.getInstance().addConfProcessListener(this);
        return false;
    }

    public void onConfProcessStarted() {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((MeetingEndMessageActivity) iUIElement).doFinish();
            }
        });
    }

    public void onConfProcessStopped() {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((MeetingEndMessageActivity) iUIElement).doFinish();
            }
        });
    }

    /* access modifiers changed from: private */
    public void doFinish() {
        dismissWaitingDialog();
        finish();
    }

    private void showWaitingDialog(String str, FragmentManager fragmentManager) {
        if (this.mWaitingDialog == null) {
            this.mWaitingDialog = WaitingDialog.newInstance(str);
            this.mWaitingDialog.setCancelable(true);
            this.mWaitingDialog.showNow(fragmentManager, "WaitingDialog");
        }
    }

    private void dismissWaitingDialog() {
        if (this.mWaitingDialog == null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (supportFragmentManager != null) {
                this.mWaitingDialog = (WaitingDialog) supportFragmentManager.findFragmentByTag("WaitingDialog");
            } else {
                return;
            }
        }
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog != null && waitingDialog.isVisible()) {
            this.mWaitingDialog.dismissAllowingStateLoss();
        }
        this.mWaitingDialog = null;
    }

    public static void showLeavingMessage(@NonNull Context context, String str) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (!zoomMessenger.isConnectionGood() && zoomMessenger.isForceSignout()) {
                    return;
                }
            } else {
                return;
            }
        }
        Intent intent = new Intent(context, MeetingEndMessageActivity.class);
        intent.setFlags(1484783616);
        intent.setAction(ACTION_SHOW_LEAVING_MESSAGE);
        intent.putExtra(ARG_LEAVING_MESSAGE, str);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    public static void showDeviceNotSupported(@NonNull Context context) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (!zoomMessenger.isConnectionGood() && zoomMessenger.isForceSignout()) {
                    return;
                }
            } else {
                return;
            }
        }
        Intent intent = new Intent(context, MeetingEndMessageActivity.class);
        intent.setFlags(1484783616);
        intent.setAction(ACTION_SHOW_DEVICE_NOT_SUPPORTED);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    public static void showTokenExpiredMessage(@NonNull Context context) {
        Intent intent = new Intent(context, MeetingEndMessageActivity.class);
        intent.setFlags(1484783616);
        intent.setAction(ACTION_SHOW_TOKEN_EXPIRED);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    public static void showCMRNotification(@NonNull Context context) {
        Intent intent = new Intent(context, MeetingEndMessageActivity.class);
        intent.setFlags(1484783616);
        intent.setAction(ACTION_SHOW_CMR_NOTIFICATION);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    private boolean handleActionShowTokenExpired(Intent intent) {
        new TokenExpiredDialog().showNow(getSupportFragmentManager(), TokenExpiredDialog.class.getSimpleName());
        return false;
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 1) {
            sinkWebLogout(j);
        }
    }

    private void sinkWebLogout(long j) {
        if (isActive()) {
            WelcomeActivity.show(this, false, false);
            doFinish();
        }
    }

    /* access modifiers changed from: private */
    public void doLeaveMeeting() {
        PTApp.getInstance().onCancelReloginAndRejoin();
        doFinish();
    }

    /* access modifiers changed from: private */
    public void notifyUIToLogOut() {
        LogoutHandler.getInstance().startLogout();
        showWaitingDialog(getString(C4558R.string.zm_msg_waiting), getSupportFragmentManager());
    }
}
