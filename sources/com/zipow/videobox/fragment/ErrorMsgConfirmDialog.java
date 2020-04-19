package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import java.io.Serializable;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ErrorMsgConfirmDialog extends ZMDialogFragment {
    private static final String ARG_ERROR_MESSAGE = "message";
    private static final String ARG_EXT_ERROR_MESSAGES = "extMessages";
    @Nullable
    private ErrorInfo mErrorMsg;
    @NonNull
    private ArrayList<ErrorInfo> mExtErrorMsg = new ArrayList<>();

    public static class ErrorInfo implements Serializable {
        private static final long serialVersionUID = 1;
        private int errorCode;
        private boolean finishActivityOnDismiss = true;
        /* access modifiers changed from: private */
        public long interval;
        /* access modifiers changed from: private */
        public String message;
        /* access modifiers changed from: private */
        public String title;

        public ErrorInfo(String str, String str2, int i) {
            this.title = str;
            this.message = str2;
            this.errorCode = i;
        }

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

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String str) {
            this.title = str;
        }

        public long getInterval() {
            return this.interval;
        }

        public void setInterval(long j) {
            this.interval = j;
        }

        public boolean isFinishActivityOnDismiss() {
            return this.finishActivityOnDismiss;
        }

        public void setFinishActivityOnDismiss(boolean z) {
            this.finishActivityOnDismiss = z;
        }
    }

    @NonNull
    public static ErrorMsgConfirmDialog newInstance(ErrorInfo errorInfo) {
        return newInstance(errorInfo, null);
    }

    @NonNull
    public static ErrorMsgConfirmDialog newInstance(ErrorInfo errorInfo, ArrayList<ErrorInfo> arrayList) {
        ErrorMsgConfirmDialog errorMsgConfirmDialog = new ErrorMsgConfirmDialog();
        errorMsgConfirmDialog.setCancelable(true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", errorInfo);
        bundle.putSerializable(ARG_EXT_ERROR_MESSAGES, arrayList);
        errorMsgConfirmDialog.setArguments(bundle);
        return errorMsgConfirmDialog;
    }

    @Nullable
    public static ErrorMsgConfirmDialog show(@Nullable ZMActivity zMActivity, ErrorInfo errorInfo) {
        if (zMActivity == null) {
            return null;
        }
        ErrorMsgConfirmDialog newInstance = newInstance(errorInfo);
        newInstance.show(zMActivity.getSupportFragmentManager(), ErrorMsgConfirmDialog.class.getName());
        return newInstance;
    }

    @Nullable
    public static ErrorMsgConfirmDialog show(@Nullable ZMActivity zMActivity, ErrorInfo errorInfo, ArrayList<ErrorInfo> arrayList) {
        if (zMActivity == null) {
            return null;
        }
        ErrorMsgConfirmDialog newInstance = newInstance(errorInfo, arrayList);
        newInstance.show(zMActivity.getSupportFragmentManager(), ErrorMsgConfirmDialog.class.getName());
        return newInstance;
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        ErrorInfo errorInfo;
        String str;
        Bundle arguments = getArguments();
        String str2 = null;
        if (arguments != null) {
            errorInfo = (ErrorInfo) arguments.getSerializable("message");
            this.mErrorMsg = errorInfo;
            ArrayList<ErrorInfo> arrayList = (ArrayList) arguments.getSerializable(ARG_EXT_ERROR_MESSAGES);
            if (arrayList != null) {
                this.mExtErrorMsg = arrayList;
            }
        } else {
            errorInfo = null;
        }
        if (bundle != null) {
            ArrayList<ErrorInfo> arrayList2 = (ArrayList) bundle.getSerializable(ARG_EXT_ERROR_MESSAGES);
            if (arrayList2 != null) {
                this.mExtErrorMsg = arrayList2;
            }
        }
        View inflate = View.inflate(getActivity(), C4558R.layout.zm_mm_error_confirm_dialog, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.title);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.msg);
        View findViewById = inflate.findViewById(C4558R.C4560id.lineButton);
        TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.btnOK);
        if (errorInfo == null) {
            str = getString(C4558R.string.zm_alert_unknown_error);
        } else {
            str = errorInfo.message;
        }
        textView2.setText(str);
        if (errorInfo != null) {
            str2 = errorInfo.title;
        }
        if (!TextUtils.isEmpty(str2)) {
            textView.setText(str2);
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        long access$200 = errorInfo != null ? errorInfo.interval : -1;
        if (access$200 > 0) {
            textView3.setVisibility(8);
            findViewById.setVisibility(8);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ErrorMsgConfirmDialog errorMsgConfirmDialog = ErrorMsgConfirmDialog.this;
                    if (errorMsgConfirmDialog.isAdded()) {
                        errorMsgConfirmDialog.dismissAllowingStateLoss();
                    }
                }
            }, access$200);
        } else {
            textView3.setVisibility(0);
            findViewById.setVisibility(0);
            textView3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ErrorMsgConfirmDialog errorMsgConfirmDialog = ErrorMsgConfirmDialog.this;
                    if (errorMsgConfirmDialog.isAdded()) {
                        errorMsgConfirmDialog.dismissAllowingStateLoss();
                    }
                }
            });
        }
        return new Builder(getActivity()).setView(inflate, true).setTheme(C4558R.style.ZMDialog_Material_RoundRect_BigCorners).create();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        FragmentActivity activity = getActivity();
        if (this.mExtErrorMsg.size() > 0) {
            newInstance((ErrorInfo) this.mExtErrorMsg.remove(0), this.mExtErrorMsg).show(getFragmentManager(), ErrorMsgConfirmDialog.class.getName());
            return;
        }
        ErrorInfo errorInfo = this.mErrorMsg;
        if (!(errorInfo == null || !errorInfo.isFinishActivityOnDismiss() || activity == null)) {
            activity.finish();
        }
    }

    public void onNewErrorMsg(ErrorInfo errorInfo) {
        this.mExtErrorMsg.add(errorInfo);
    }
}
