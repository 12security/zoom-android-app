package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import java.io.Serializable;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ErrorMsgDialog extends ZMDialogFragment {
    private static final String ARG_DISMISS_INTERVAL = "interval";
    private static final String ARG_ERROR_MESSAGE = "message";
    private static final String ARG_EXT_ERROR_MESSAGES = "extMessages";
    private static final String ARG_FINISH_ACTIVITY_ON_DISMISS = "finishActivityOnDismiss";
    @NonNull
    private ArrayList<ErrorInfo> mExtErrorMsg = new ArrayList<>();
    private boolean mFinishActivityOnDismiss = false;

    public static class ErrorInfo implements Serializable {
        private static final long serialVersionUID = 1;
        /* access modifiers changed from: private */
        public int errorCode;
        /* access modifiers changed from: private */
        public String message;

        public ErrorInfo(String str, int i) {
            this.message = str;
            this.errorCode = i;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String str) {
            this.message = str;
        }

        public int getErrorCode() {
            return this.errorCode;
        }

        public void setErrorCode(int i) {
            this.errorCode = i;
        }
    }

    @NonNull
    public static ErrorMsgDialog newInstance(String str, int i, ArrayList<ErrorInfo> arrayList, boolean z, long j) {
        ErrorMsgDialog errorMsgDialog = new ErrorMsgDialog();
        errorMsgDialog.setCancelable(true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", new ErrorInfo(str, i));
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS, z);
        bundle.putSerializable(ARG_EXT_ERROR_MESSAGES, arrayList);
        bundle.putLong(ARG_DISMISS_INTERVAL, j);
        errorMsgDialog.setArguments(bundle);
        return errorMsgDialog;
    }

    @NonNull
    public static ErrorMsgDialog newInstance(String str, int i, ArrayList<ErrorInfo> arrayList, boolean z) {
        return newInstance(str, i, arrayList, z, 5000);
    }

    @NonNull
    public static ErrorMsgDialog newInstance(String str, int i, boolean z, long j) {
        return newInstance(str, i, null, z, j);
    }

    @NonNull
    public static ErrorMsgDialog newInstance(String str, int i, boolean z) {
        return newInstance(str, i, null, z);
    }

    @NonNull
    public static ErrorMsgDialog newInstance(String str, int i) {
        return newInstance(str, i, false);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        ErrorInfo errorInfo;
        String str;
        long j;
        Bundle arguments = getArguments();
        long j2 = 5000;
        if (arguments != null) {
            this.mFinishActivityOnDismiss = arguments.getBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS, false);
            errorInfo = (ErrorInfo) arguments.getSerializable("message");
            ArrayList<ErrorInfo> arrayList = (ArrayList) arguments.getSerializable(ARG_EXT_ERROR_MESSAGES);
            if (arrayList != null) {
                this.mExtErrorMsg = arrayList;
            }
            j2 = arguments.getLong(ARG_DISMISS_INTERVAL, 5000);
        } else {
            errorInfo = null;
        }
        if (bundle != null) {
            ArrayList<ErrorInfo> arrayList2 = (ArrayList) bundle.getSerializable(ARG_EXT_ERROR_MESSAGES);
            if (arrayList2 != null) {
                this.mExtErrorMsg = arrayList2;
            }
        }
        View inflate = View.inflate(getActivity(), C4558R.layout.zm_mm_error_dialog, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtErrorMsg);
        ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.imgErrorIcon);
        if (errorInfo == null) {
            str = getString(C4558R.string.zm_alert_unknown_error);
        } else {
            str = errorInfo.message;
        }
        textView.setText(str);
        if (errorInfo == null || errorInfo.errorCode != 0) {
            if (this.mExtErrorMsg.size() != 0) {
                j2 = 2000;
            }
            j = j2;
            imageView.setImageResource(C4558R.C4559drawable.zm_ic_error_msg_attation);
        } else {
            j = 1000;
            imageView.setImageResource(C4558R.C4559drawable.zm_ic_success_msg_attation);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ErrorMsgDialog errorMsgDialog = ErrorMsgDialog.this;
                if (errorMsgDialog.isAdded()) {
                    errorMsgDialog.dismissAllowingStateLoss();
                }
            }
        }, j);
        return new Builder(getActivity()).setView(inflate).setTheme(C4558R.style.ZMDialog_Material_Transparent).create();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        FragmentActivity activity = getActivity();
        if (this.mExtErrorMsg.size() > 0) {
            ErrorInfo errorInfo = (ErrorInfo) this.mExtErrorMsg.remove(0);
            newInstance(errorInfo.message, errorInfo.errorCode, this.mExtErrorMsg, this.mFinishActivityOnDismiss).show(getFragmentManager(), ErrorMsgDialog.class.getName());
            return;
        }
        if (this.mFinishActivityOnDismiss && activity != null) {
            activity.finish();
        }
    }

    public void onNewErrorMsg(String str, int i) {
        this.mExtErrorMsg.add(new ErrorInfo(str, i));
    }
}
