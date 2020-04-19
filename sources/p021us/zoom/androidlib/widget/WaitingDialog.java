package p021us.zoom.androidlib.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.ZMLog;

/* renamed from: us.zoom.androidlib.widget.WaitingDialog */
public class WaitingDialog extends ZMDialogFragment {
    public static final String ARG_FINISH_ACTIVITY_ON_CANCEL = "finishActivityOnCancel";
    public static final String ARG_MESSAGE = "message";
    public static final String ARG_MESSAGE_ID = "messageId";
    public static final String ARG_TITLE = "title";
    public static final String ARG_TITLE_ID = "titleId";
    /* access modifiers changed from: private */
    public static final String TAG = "us.zoom.androidlib.widget.WaitingDialog";
    private Activity mActivity = null;
    private ProgressDialog mProgressDialog = null;

    /* renamed from: us.zoom.androidlib.widget.WaitingDialog$ZMProgressDialog */
    static class ZMProgressDialog extends ProgressDialog {
        public ZMProgressDialog(Context context, int i) {
            super(context, i);
        }

        public ZMProgressDialog(Context context) {
            super(context);
        }

        public void show() {
            ZMActivity zMActivity = (ZMActivity) getOwnerActivity();
            if (zMActivity == null || !zMActivity.isActive()) {
                ZMLog.m280e(WaitingDialog.TAG, "ZMProgressDialog.show(), activity is not in foreground", new Object[0]);
                return;
            }
            try {
                super.show();
            } catch (Exception e) {
                ZMLog.m281e(WaitingDialog.TAG, e, "ZMProgressDialog.show(), exception", new Object[0]);
            }
        }
    }

    public static WaitingDialog newInstance(String str) {
        WaitingDialog waitingDialog = new WaitingDialog();
        waitingDialog.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString("message", str);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, false);
        waitingDialog.setArguments(bundle);
        return waitingDialog;
    }

    public static WaitingDialog newInstance(String str, boolean z) {
        WaitingDialog waitingDialog = new WaitingDialog();
        waitingDialog.setCancelable(z);
        Bundle bundle = new Bundle();
        bundle.putString("message", str);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, z);
        waitingDialog.setArguments(bundle);
        return waitingDialog;
    }

    public static WaitingDialog newInstance(String str, String str2) {
        WaitingDialog waitingDialog = new WaitingDialog();
        waitingDialog.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString("message", str);
        bundle.putString("title", str2);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, false);
        waitingDialog.setArguments(bundle);
        return waitingDialog;
    }

    public static WaitingDialog newInstance(String str, String str2, boolean z) {
        WaitingDialog waitingDialog = new WaitingDialog();
        waitingDialog.setCancelable(z);
        Bundle bundle = new Bundle();
        bundle.putString("message", str);
        bundle.putString("title", str2);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, z);
        waitingDialog.setArguments(bundle);
        return waitingDialog;
    }

    public static WaitingDialog newInstance(int i) {
        WaitingDialog waitingDialog = new WaitingDialog();
        waitingDialog.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", i);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, false);
        waitingDialog.setArguments(bundle);
        return waitingDialog;
    }

    public static WaitingDialog newInstance(int i, boolean z) {
        WaitingDialog waitingDialog = new WaitingDialog();
        waitingDialog.setCancelable(z);
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", i);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, z);
        waitingDialog.setArguments(bundle);
        return waitingDialog;
    }

    public static WaitingDialog newInstance(int i, int i2) {
        WaitingDialog waitingDialog = new WaitingDialog();
        waitingDialog.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", i);
        bundle.putInt(ARG_TITLE_ID, i2);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, false);
        waitingDialog.setArguments(bundle);
        return waitingDialog;
    }

    public static WaitingDialog newInstance(int i, int i2, boolean z) {
        WaitingDialog waitingDialog = new WaitingDialog();
        waitingDialog.setCancelable(z);
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", i);
        bundle.putInt(ARG_TITLE_ID, i2);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, z);
        waitingDialog.setArguments(bundle);
        return waitingDialog;
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        this.mActivity = getActivity();
        if (this.mActivity == null) {
            return createEmptyDialog();
        }
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        String string = arguments.getString("message");
        String string2 = arguments.getString("title");
        if (string == null) {
            int i = arguments.getInt("messageId");
            if (i > 0) {
                string = this.mActivity.getString(i);
            }
        }
        if (string2 == null) {
            int i2 = arguments.getInt(ARG_TITLE_ID);
            if (i2 > 0) {
                string2 = this.mActivity.getString(i2);
            }
        }
        ZMProgressDialog zMProgressDialog = new ZMProgressDialog(this.mActivity);
        zMProgressDialog.requestWindowFeature(1);
        zMProgressDialog.setMessage(string);
        zMProgressDialog.setTitle(string2);
        zMProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog = zMProgressDialog;
        return zMProgressDialog;
    }

    public void setDialogMessage(String str) {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.setMessage(str);
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getBoolean(ARG_FINISH_ACTIVITY_ON_CANCEL, false)) {
            Activity activity = this.mActivity;
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
