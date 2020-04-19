package com.zipow.videobox.fragment.meeting.p011qa.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.dialog.ZMQAAskDialog */
public class ZMQAAskDialog extends ZMDialogFragment implements OnClickListener {
    private static final String TAG = "ZMQAAskDialog";
    /* access modifiers changed from: private */
    @Nullable
    public static String mLastAskQuestion;
    /* access modifiers changed from: private */
    public TextView mBtnSend;
    private CheckBox mChkAnonymously;
    private IConfUIListener mConfUIListener;
    /* access modifiers changed from: private */
    public EditText mEdtQuestion;
    @NonNull
    private Handler mHandler = new Handler();
    private long mLastClickSendTime = 0;
    private View mOptionAnonymously;
    private IZoomQAUIListener mQAUIListener;
    /* access modifiers changed from: private */
    @Nullable
    public String mQuestionId;
    @Nullable
    private Runnable mShowKeyboardRunnable = new Runnable() {
        public void run() {
            if (ZMQAAskDialog.this.mEdtQuestion != null) {
                ZMQAAskDialog.this.mEdtQuestion.requestFocus();
                UIUtil.openSoftKeyboard(ZMQAAskDialog.this.getActivity(), ZMQAAskDialog.this.mEdtQuestion);
            }
        }
    };
    private View mTxtAnonymously;

    public static void show(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null && zMActivity.isActive()) {
            new ZMQAAskDialog().show(zMActivity.getSupportFragmentManager(), ZMQAAskDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        View createContent = createContent(bundle);
        if (createContent == null) {
            return createEmptyDialog();
        }
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material_RoundRect).setView(createContent, true).create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        UIUtil.closeSoftKeyboard(getContext(), this.mEdtQuestion);
    }

    private View createContent(@Nullable Bundle bundle) {
        if (bundle != null) {
            this.mQuestionId = bundle.getString("mQuestionId");
        }
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material_RoundRect), C4558R.layout.zm_dialog_qa_ask, null);
        inflate.findViewById(C4558R.C4560id.imgClose).setOnClickListener(this);
        this.mEdtQuestion = (EditText) inflate.findViewById(C4558R.C4560id.edtQuestion);
        this.mBtnSend = (TextView) inflate.findViewById(C4558R.C4560id.btnSend);
        this.mBtnSend.setOnClickListener(this);
        this.mOptionAnonymously = inflate.findViewById(C4558R.C4560id.optionAnonymously);
        this.mChkAnonymously = (CheckBox) inflate.findViewById(C4558R.C4560id.chkAnonymously);
        this.mTxtAnonymously = inflate.findViewById(C4558R.C4560id.txtAnonymously);
        this.mOptionAnonymously.setOnClickListener(this);
        this.mChkAnonymously.setEnabled(ConfMgr.getInstance().isAllowAskQuestionAnonymously());
        this.mEdtQuestion.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 4) {
                    ZMQAAskDialog.this.onClickBtnSend();
                }
                return false;
            }
        });
        this.mEdtQuestion.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ZMQAAskDialog.mLastAskQuestion = ZMQAAskDialog.this.mEdtQuestion.getEditableText().toString();
                ZMQAAskDialog.this.mBtnSend.setEnabled(ZMQAAskDialog.mLastAskQuestion.length() != 0);
            }
        });
        if (!StringUtil.isEmptyOrNull(mLastAskQuestion) && !StringUtil.isEmptyOrNull(mLastAskQuestion)) {
            this.mEdtQuestion.setText(mLastAskQuestion);
            this.mEdtQuestion.setSelection(mLastAskQuestion.length());
            this.mBtnSend.setEnabled(true);
        }
        return inflate;
    }

    public void onPause() {
        super.onPause();
        Runnable runnable = this.mShowKeyboardRunnable;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
        ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
        ConfUI.getInstance().removeListener(this.mConfUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, long j) {
                    return ZMQAAskDialog.this.onConfStatusChanged2(i, j);
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onAddQuestion(String str, boolean z) {
                    ZMQAAskDialog.this.onAddQuestion(str, z);
                }

                public void notifyConnectResult(boolean z) {
                    ZMQAAskDialog.this.notifyConnectResult(z);
                }

                public void onQuestionMarkedAsDismissed(String str) {
                    if (StringUtil.isSameStringForNotAllowNull(str, ZMQAAskDialog.this.mQuestionId)) {
                        ZMQAAskDialog.this.onClickBtnBack();
                    }
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        updateData();
        Context context = getContext();
        if (context != null && UIUtil.isPortraitMode(context)) {
            Runnable runnable = this.mShowKeyboardRunnable;
            if (runnable != null) {
                this.mHandler.postDelayed(runnable, 100);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean onConfStatusChanged2(int i, long j) {
        if (i == 30) {
            onQAAnonymouslyStatusChanged();
        }
        return true;
    }

    private void updateData() {
        if (!StringUtil.isEmptyOrNull(this.mQuestionId)) {
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent != null) {
                ZoomQAQuestion questionByID = qAComponent.getQuestionByID(this.mQuestionId);
                if (questionByID != null) {
                    updateQuestionState(questionByID.getState());
                }
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mQuestionId", this.mQuestionId);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.imgClose) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSend) {
            onClickBtnSend();
        } else if (id == C4558R.C4560id.optionAnonymously) {
            onClickCheckAnonymously();
        }
    }

    public void onQAAnonymouslyStatusChanged() {
        if (ConfMgr.getInstance().isAllowAskQuestionAnonymously()) {
            this.mTxtAnonymously.setEnabled(true);
            this.mChkAnonymously.setEnabled(true);
            this.mOptionAnonymously.setEnabled(true);
            return;
        }
        this.mChkAnonymously.setChecked(false);
        this.mTxtAnonymously.setEnabled(false);
        this.mChkAnonymously.setEnabled(false);
        this.mOptionAnonymously.setEnabled(false);
    }

    /* access modifiers changed from: private */
    public void onClickBtnSend() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.mLastClickSendTime;
        if (j <= 0 || j >= 1000) {
            this.mLastClickSendTime = currentTimeMillis;
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtQuestion);
            String trim = this.mEdtQuestion.getText().toString().trim();
            if (trim.length() != 0) {
                ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                if (qAComponent != null) {
                    this.mQuestionId = qAComponent.addQuestion(trim, null, this.mChkAnonymously.isChecked());
                    if (StringUtil.isEmptyOrNull(this.mQuestionId)) {
                        showSendFailedMessage();
                    } else {
                        showWaitingDialog();
                    }
                }
            }
        }
    }

    private void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, "WaitingDialog");
        }
    }

    private void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.executePendingTransactions();
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    private void onClickCheckAnonymously() {
        if (ConfMgr.getInstance().isAllowAskQuestionAnonymously()) {
            CheckBox checkBox = this.mChkAnonymously;
            checkBox.setChecked(!checkBox.isChecked());
        }
    }

    /* access modifiers changed from: private */
    public void onAddQuestion(String str, boolean z) {
        if (StringUtil.isSameString(str, this.mQuestionId)) {
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent != null) {
                ZoomQAQuestion questionByID = qAComponent.getQuestionByID(this.mQuestionId);
                if (questionByID != null) {
                    updateQuestionState(questionByID.getState());
                }
            }
        }
    }

    private void updateQuestionState(int i) {
        if (i == 1) {
            mLastAskQuestion = null;
            dismissWaitingDialog();
            dismiss();
        } else if (i == 3) {
            dismissWaitingDialog();
            showSendFailedMessage();
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtQuestion);
        dismiss();
    }

    public void dismiss() {
        finishFragment(true);
    }

    /* access modifiers changed from: private */
    public void notifyConnectResult(boolean z) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null && qAComponent.isStreamConflict()) {
            dismiss();
        }
    }

    private void showSendFailedMessage() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_qa_msg_send_question_failed, 1).show();
        }
    }
}
