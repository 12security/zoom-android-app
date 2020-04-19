package com.zipow.videobox.ptapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.VideoBoxApplication.IConfProcessListener;
import com.zipow.videobox.dialog.ShareScreenAlertDialog;
import com.zipow.videobox.fragment.ShareScreenDialog;
import com.zipow.videobox.ptapp.PTUI.IPresentToRoomStatusListener;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import java.lang.ref.WeakReference;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ShareScreenDialogHelper {
    /* access modifiers changed from: private */
    public static final String TAG = "ShareScreenDialogHelper";
    public static final String TAG_WAITING_DIALOG = "ShareScreenWaitingDialog";
    private static ShareScreenDialogHelper instance;
    /* access modifiers changed from: private */
    public boolean mIsFinishActivity = false;
    /* access modifiers changed from: private */
    public boolean mIsInputNewParingCode;
    private ShareScreenDialog mShareScreenDialog = null;
    private WeakReference<ZMActivity> mZMActivityWeakReference = null;
    private IPresentToRoomStatusListener presentToRoomStatusListener = new IPresentToRoomStatusListener() {
        public void presentToRoomStatusUpdate(int i) {
            ShareScreenDialogHelper.this.mIsInputNewParingCode = false;
            ZMActivity access$100 = ShareScreenDialogHelper.this.getActivity();
            if (access$100 != null) {
                FragmentManager supportFragmentManager = access$100.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    if (i != 35) {
                        if (i != 40) {
                            if (i != 50) {
                                switch (i) {
                                    case 20:
                                    case 21:
                                        ShareScreenDialogHelper.this.dismissWaitingDialog();
                                        if (ShareScreenDialogHelper.this.mIsFinishActivity && access$100.isActive()) {
                                            access$100.finish();
                                            break;
                                        }
                                    default:
                                        switch (i) {
                                            case 23:
                                            case 24:
                                            case 25:
                                            case 26:
                                            case 27:
                                            case 28:
                                                break;
                                        }
                                }
                            } else {
                                ShareScreenDialogHelper.this.dismissWaitingDialog();
                                PTUI.getInstance().removePresentToRoomStatusListener(this);
                                if (ShareScreenDialogHelper.this.mIsFinishActivity) {
                                    access$100.finish();
                                }
                            }
                        }
                        ShareScreenDialogHelper.this.dismissWaitingDialog();
                        PTAppDelegation.getInstance().stopPresentToRoom(false);
                        ShareScreenAlertDialog.show(supportFragmentManager, ShareScreenAlertDialog.class.getName(), i, new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShareScreenDialogHelper.this.showShareScreen();
                            }
                        });
                        PTUI.getInstance().removePresentToRoomStatusListener(this);
                    } else {
                        ShareScreenDialogHelper.this.dismissWaitingDialog();
                        ShareScreenAlertDialog.show(supportFragmentManager, ShareScreenAlertDialog.class.getName(), i, new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShareScreenDialogHelper.this.mIsInputNewParingCode = true;
                                ShareScreenDialogHelper.this.showShareScreen();
                            }
                        });
                    }
                }
            }
        }
    };

    @NonNull
    public static synchronized ShareScreenDialogHelper getInstance() {
        ShareScreenDialogHelper shareScreenDialogHelper;
        synchronized (ShareScreenDialogHelper.class) {
            if (instance == null) {
                instance = new ShareScreenDialogHelper();
            }
            shareScreenDialogHelper = instance;
        }
        return shareScreenDialogHelper;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void showShareScreen(final p021us.zoom.androidlib.app.ZMActivity r4, final boolean r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            if (r4 != 0) goto L_0x0005
            monitor-exit(r3)
            return
        L_0x0005:
            boolean r0 = p021us.zoom.androidlib.util.ZMIntentUtil.isSupportShareScreen(r4)     // Catch:{ all -> 0x0073 }
            if (r0 != 0) goto L_0x0012
            if (r5 == 0) goto L_0x0010
            r4.finish()     // Catch:{ all -> 0x0073 }
        L_0x0010:
            monitor-exit(r3)
            return
        L_0x0012:
            boolean r0 = r3.checkContext(r4, r5)     // Catch:{ all -> 0x0073 }
            if (r0 != 0) goto L_0x001a
            monitor-exit(r3)
            return
        L_0x001a:
            r0 = 0
            boolean r1 = p021us.zipow.mdm.ZMPolicyUIHelper.isDisableDirectShare()     // Catch:{ all -> 0x0073 }
            if (r1 == 0) goto L_0x0028
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_hint_direct_share_disabled_117294     // Catch:{ all -> 0x0073 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0073 }
            goto L_0x0038
        L_0x0028:
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()     // Catch:{ all -> 0x0073 }
            boolean r1 = r1.isShareDesktopDisabled()     // Catch:{ all -> 0x0073 }
            if (r1 == 0) goto L_0x0038
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_hint_share_screen_disabled_117294     // Catch:{ all -> 0x0073 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0073 }
        L_0x0038:
            if (r0 == 0) goto L_0x006e
            us.zoom.androidlib.widget.ZMAlertDialog$Builder r1 = new us.zoom.androidlib.widget.ZMAlertDialog$Builder     // Catch:{ all -> 0x0073 }
            r1.<init>(r4)     // Catch:{ all -> 0x0073 }
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_title_share_screen_disabled_117294     // Catch:{ all -> 0x0073 }
            us.zoom.androidlib.widget.ZMAlertDialog$Builder r1 = r1.setTitle(r2)     // Catch:{ all -> 0x0073 }
            int r0 = r0.intValue()     // Catch:{ all -> 0x0073 }
            us.zoom.androidlib.widget.ZMAlertDialog$Builder r0 = r1.setMessage(r0)     // Catch:{ all -> 0x0073 }
            r1 = 1
            us.zoom.androidlib.widget.ZMAlertDialog$Builder r0 = r0.setCancelable(r1)     // Catch:{ all -> 0x0073 }
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_btn_ok     // Catch:{ all -> 0x0073 }
            com.zipow.videobox.ptapp.ShareScreenDialogHelper$2 r2 = new com.zipow.videobox.ptapp.ShareScreenDialogHelper$2     // Catch:{ all -> 0x0073 }
            r2.<init>(r5, r4)     // Catch:{ all -> 0x0073 }
            us.zoom.androidlib.widget.ZMAlertDialog$Builder r0 = r0.setPositiveButton(r1, r2)     // Catch:{ all -> 0x0073 }
            us.zoom.androidlib.widget.ZMAlertDialog r0 = r0.create()     // Catch:{ all -> 0x0073 }
            com.zipow.videobox.ptapp.ShareScreenDialogHelper$3 r1 = new com.zipow.videobox.ptapp.ShareScreenDialogHelper$3     // Catch:{ all -> 0x0073 }
            r1.<init>(r5, r4)     // Catch:{ all -> 0x0073 }
            r0.setOnCancelListener(r1)     // Catch:{ all -> 0x0073 }
            r0.show()     // Catch:{ all -> 0x0073 }
            monitor-exit(r3)
            return
        L_0x006e:
            r3.showShareScreen()     // Catch:{ all -> 0x0073 }
            monitor-exit(r3)
            return
        L_0x0073:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.ShareScreenDialogHelper.showShareScreen(us.zoom.androidlib.app.ZMActivity, boolean):void");
    }

    public boolean isFinishActivity() {
        return this.mIsFinishActivity;
    }

    public boolean isInputNewParingCode() {
        return this.mIsInputNewParingCode;
    }

    /* access modifiers changed from: private */
    public void showShareScreen() {
        ZMActivity activity = getActivity();
        if (activity != null) {
            if (this.mShareScreenDialog == null) {
                this.mShareScreenDialog = new ShareScreenDialog();
            }
            Dialog dialog = this.mShareScreenDialog.getDialog();
            if (dialog == null || !dialog.isShowing()) {
                PTUI.getInstance().addPresentToRoomStatusListener(this.presentToRoomStatusListener);
                this.mShareScreenDialog.showNow(activity.getSupportFragmentManager(), ShareScreenDialog.class.getName());
            }
        }
    }

    private boolean hasConf(final ZMActivity zMActivity, final boolean z) {
        if (!PTApp.getInstance().hasActiveCall() || !VideoBoxApplication.getInstance().isConfProcessRunning()) {
            return false;
        }
        ZMAlertDialog create = new Builder(zMActivity).setTitle(C4558R.string.zm_alert_switch_call_direct_share_97592).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_no, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (z) {
                    zMActivity.finish();
                }
            }
        }).setPositiveButton(C4558R.string.zm_btn_yes, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMActivity access$100 = ShareScreenDialogHelper.this.getActivity();
                if (access$100 != null) {
                    if (access$100 == zMActivity) {
                        ZMLog.m286i(ShareScreenDialogHelper.TAG, "Avoid closing the same activity", new Object[0]);
                        ShareScreenDialogHelper.this.mIsFinishActivity = false;
                    }
                    VideoBoxApplication.getNonNullInstance().addConfProcessListener(new IConfProcessListener() {
                        public void onConfProcessStarted() {
                        }

                        public void onConfProcessStopped() {
                            VideoBoxApplication.getInstance().removeConfProcessListener(this);
                            ShareScreenDialogHelper.this.showShareScreen(zMActivity, z);
                        }
                    });
                    PTApp.getInstance().forceSyncLeaveCurrentCall();
                }
            }
        }).create();
        create.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                if (z) {
                    zMActivity.finish();
                }
            }
        });
        create.show();
        return true;
    }

    private boolean checkContext(ZMActivity zMActivity, boolean z) {
        if (hasConf(zMActivity, z)) {
            return false;
        }
        ZMActivity activity = getActivity();
        if (!(activity == null || activity == zMActivity)) {
            if (this.mShareScreenDialog != null) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("oldAct not same, activity");
                sb.append(zMActivity);
                ZMLog.m286i(str, sb.toString(), new Object[0]);
                if (this.mShareScreenDialog.getFragmentManager() != null) {
                    this.mShareScreenDialog.dismiss();
                }
                this.mShareScreenDialog = null;
            }
            PTUI.getInstance().removePresentToRoomStatusListener(this.presentToRoomStatusListener);
        }
        this.mZMActivityWeakReference = new WeakReference<>(zMActivity);
        this.mIsFinishActivity = z;
        return true;
    }

    /* access modifiers changed from: private */
    public ZMActivity getActivity() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getActivity mZMActivityWeakReference = ");
        sb.append(this.mZMActivityWeakReference);
        ZMLog.m286i(str, sb.toString(), new Object[0]);
        if (this.mZMActivityWeakReference == null) {
            return null;
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getActivity act = ");
        sb2.append(this.mZMActivityWeakReference.get());
        ZMLog.m286i(str2, sb2.toString(), new Object[0]);
        return (ZMActivity) this.mZMActivityWeakReference.get();
    }

    public void showWaitingDialog() {
        ZMActivity activity = getActivity();
        if (activity != null) {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            if (supportFragmentManager != null) {
                WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
                newInstance.setCancelable(true);
                newInstance.show(supportFragmentManager, TAG_WAITING_DIALOG);
            }
        }
    }

    public void dismissWaitingDialog() {
        ZMActivity activity = getActivity();
        if (activity != null) {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            if (supportFragmentManager != null) {
                ZMDialogFragment zMDialogFragment = (ZMDialogFragment) supportFragmentManager.findFragmentByTag(TAG_WAITING_DIALOG);
                if (zMDialogFragment != null) {
                    zMDialogFragment.dismissAllowingStateLoss();
                }
            }
        }
    }
}
