package com.zipow.videobox.fragment.meeting.p011qa.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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
import com.zipow.videobox.confapp.p010qa.ZoomQAAnswer;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.dialog.ZMQAAnswerDialog */
public class ZMQAAnswerDialog extends ZMDialogFragment implements OnClickListener {
    private static final String ARG_QUESTION_ID = "questionId";
    private static final int MAX_CACHE_COUNT = 2;
    private static final String TAG = "ZMQAAnswerDialog";
    /* access modifiers changed from: private */
    @NonNull
    public static LinkedHashMap<String, String> mDraft = new LinkedHashMap<>();
    @Nullable
    private String mAnswerId;
    /* access modifiers changed from: private */
    public View mBtnSend;
    private CheckBox mChkPrivately;
    /* access modifiers changed from: private */
    public EditText mEdtContent;
    @NonNull
    private Handler mHandler = new Handler();
    private long mLastClickSendTime = 0;
    private View mOptionPrivately;
    private IZoomQAUIListener mQAUIListener;
    /* access modifiers changed from: private */
    @Nullable
    public String mQuestionId;
    @Nullable
    private Runnable mShowKeyboardRunnable = new Runnable() {
        public void run() {
            if (ZMQAAnswerDialog.this.mEdtContent != null) {
                ZMQAAnswerDialog.this.mEdtContent.requestFocus();
                UIUtil.openSoftKeyboard(ZMQAAnswerDialog.this.getActivity(), ZMQAAnswerDialog.this.mEdtContent);
            }
        }
    };
    private View mTxtPrivately;
    private TextView mTxtQuestion;

    public static void show(@Nullable ZMActivity zMActivity, String str) {
        if (zMActivity != null && zMActivity.isActive()) {
            ZMQAAnswerDialog zMQAAnswerDialog = new ZMQAAnswerDialog();
            Bundle bundle = new Bundle();
            bundle.putString("questionId", str);
            zMQAAnswerDialog.setArguments(bundle);
            zMQAAnswerDialog.show(zMActivity.getSupportFragmentManager(), ZMQAAnswerDialog.class.getName());
        }
    }

    public static void dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ZMQAAnswerDialog zMQAAnswerDialog = (ZMQAAnswerDialog) fragmentManager.findFragmentByTag(ZMQAAnswerDialog.class.getName());
            if (zMQAAnswerDialog != null) {
                zMQAAnswerDialog.dismiss();
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mQuestionId = arguments.getString("questionId");
        }
        View createContent = createContent(bundle);
        if (createContent == null) {
            return createEmptyDialog();
        }
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material_RoundRect).setView(createContent, true).create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    private View createContent(@Nullable Bundle bundle) {
        if (bundle != null) {
            this.mQuestionId = bundle.getString("mQuestionId");
            this.mAnswerId = bundle.getString("mAnswerId");
        }
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material_RoundRect), C4558R.layout.zm_dialog_qa_answer, null);
        inflate.findViewById(C4558R.C4560id.imgClose).setOnClickListener(this);
        this.mTxtQuestion = (TextView) inflate.findViewById(C4558R.C4560id.txtQuestion);
        this.mTxtQuestion.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.mEdtContent = (EditText) inflate.findViewById(C4558R.C4560id.edtContent);
        this.mBtnSend = inflate.findViewById(C4558R.C4560id.btnSend);
        this.mBtnSend.setOnClickListener(this);
        this.mOptionPrivately = inflate.findViewById(C4558R.C4560id.optionPrivately);
        this.mChkPrivately = (CheckBox) inflate.findViewById(C4558R.C4560id.chkPrivately);
        this.mTxtPrivately = inflate.findViewById(C4558R.C4560id.txtPrivately);
        this.mOptionPrivately.setOnClickListener(this);
        this.mOptionPrivately.setVisibility(ConfMgr.getInstance().isViewOnlyMeeting() ? 4 : 0);
        this.mEdtContent.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 4) {
                    ZMQAAnswerDialog.this.onClickBtnSend();
                }
                return false;
            }
        });
        this.mEdtContent.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                String obj = ZMQAAnswerDialog.this.mEdtContent.getEditableText().toString();
                ZMQAAnswerDialog.this.mBtnSend.setEnabled(obj.length() != 0);
                if (StringUtil.isEmptyOrNull(ZMQAAnswerDialog.this.mQuestionId)) {
                    return;
                }
                if (ZMQAAnswerDialog.mDraft.containsKey(ZMQAAnswerDialog.this.mQuestionId)) {
                    if (!StringUtil.isSameString((String) ZMQAAnswerDialog.mDraft.get(ZMQAAnswerDialog.this.mQuestionId), obj)) {
                        ZMQAAnswerDialog.mDraft.remove(ZMQAAnswerDialog.this.mQuestionId);
                    }
                    ZMQAAnswerDialog.mDraft.put(ZMQAAnswerDialog.this.mQuestionId, obj);
                    return;
                }
                if (ZMQAAnswerDialog.mDraft.size() >= 2) {
                    ZMQAAnswerDialog.mDraft.remove(((Entry) ZMQAAnswerDialog.mDraft.entrySet().iterator().next()).getKey());
                }
                ZMQAAnswerDialog.mDraft.put(ZMQAAnswerDialog.this.mQuestionId, obj);
            }
        });
        if (!StringUtil.isEmptyOrNull(this.mQuestionId) && mDraft.containsKey(this.mQuestionId)) {
            String str = (String) mDraft.get(this.mQuestionId);
            if (!StringUtil.isEmptyOrNull(str)) {
                this.mEdtContent.setText(str);
                this.mEdtContent.setSelection(str.length());
                this.mBtnSend.setEnabled(true);
            }
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
    }

    public void onResume() {
        super.onResume();
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onAddAnswer(String str, boolean z) {
                    ZMQAAnswerDialog.this.onAddAnswer(str, z);
                }

                public void notifyConnectResult(boolean z) {
                    ZMQAAnswerDialog.this.notifyConnectResult(z);
                }

                public void onQuestionMarkedAsDismissed(String str) {
                    if (StringUtil.isSameStringForNotAllowNull(str, ZMQAAnswerDialog.this.mQuestionId)) {
                        ZMQAAnswerDialog.this.onClickBtnBack();
                    }
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        updateData();
    }

    private void updateData() {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null) {
            if (!StringUtil.isEmptyOrNull(this.mAnswerId)) {
                ZoomQAAnswer answerByID = qAComponent.getAnswerByID(this.mAnswerId);
                if (answerByID != null) {
                    updateAnswerState(answerByID.getState());
                }
            }
            if (StringUtil.isEmptyOrNull(this.mQuestionId)) {
                Bundle arguments = getArguments();
                if (arguments != null) {
                    this.mQuestionId = arguments.getString("questionId");
                }
            }
            ZoomQAQuestion questionByID = qAComponent.getQuestionByID(this.mQuestionId);
            if (questionByID != null) {
                this.mTxtQuestion.setText(questionByID.getText());
                Context context = getContext();
                if (context != null && UIUtil.isPortraitMode(context)) {
                    Runnable runnable = this.mShowKeyboardRunnable;
                    if (runnable != null) {
                        this.mHandler.postDelayed(runnable, 100);
                    }
                }
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mQuestionId", this.mQuestionId);
        bundle.putString("mAnswerId", this.mAnswerId);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.imgClose) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSend) {
            onClickBtnSend();
        } else if (id == C4558R.C4560id.optionPrivately) {
            onClickCheckPrivately();
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnSend() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.mLastClickSendTime;
        if (j <= 0 || j >= 1000) {
            this.mLastClickSendTime = currentTimeMillis;
            UIUtil.closeSoftKeyboard(getActivity(), this.mEdtContent);
            String trim = this.mEdtContent.getText().toString().trim();
            if (trim.length() != 0) {
                ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                if (qAComponent != null) {
                    ZoomQAQuestion questionByID = qAComponent.getQuestionByID(this.mQuestionId);
                    if (questionByID != null) {
                        String str = null;
                        if (!ConfMgr.getInstance().isViewOnlyMeeting() && this.mChkPrivately.isChecked()) {
                            str = questionByID.getSenderJID();
                        }
                        this.mAnswerId = qAComponent.addAnswer(this.mQuestionId, trim, str);
                        if (StringUtil.isEmptyOrNull(this.mAnswerId)) {
                            showSendFailedMessage();
                        } else {
                            showWaitingDialog();
                        }
                    }
                }
            }
        }
    }

    private void onClickCheckPrivately() {
        CheckBox checkBox = this.mChkPrivately;
        checkBox.setChecked(!checkBox.isChecked());
    }

    /* access modifiers changed from: private */
    public void onAddAnswer(@Nullable String str, boolean z) {
        if (str != null) {
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent != null && StringUtil.isSameString(str, this.mAnswerId)) {
                ZoomQAAnswer answerByID = qAComponent.getAnswerByID(this.mAnswerId);
                if (answerByID != null) {
                    updateAnswerState(answerByID.getState());
                }
            }
        }
    }

    private void updateAnswerState(int i) {
        if (i == 1) {
            if (!StringUtil.isEmptyOrNull(this.mQuestionId)) {
                mDraft.remove(this.mQuestionId);
            }
            dismissWaitingDialog();
            dismiss();
        } else if (i == 3) {
            dismissWaitingDialog();
            showSendFailedMessage();
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtContent);
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

    private void showSendFailedMessage() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_qa_msg_send_answer_failed, 1).show();
        }
    }
}
