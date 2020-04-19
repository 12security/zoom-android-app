package com.zipow.videobox.fragment.meeting.p011qa.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.dialog.ZMQAMoreDialog */
public class ZMQAMoreDialog extends ZMDialogFragment implements OnClickListener {
    private Button mBtnBack;
    private CheckedTextView mChkAllowAskQA;
    private CheckedTextView mChkCanComment;
    private CheckedTextView mChkCanUpVote;
    private IConfUIListener mConfUIListener;
    private ImageView mImgAllQuestions;
    private ImageView mImgAnswerQaOnly;
    private View mOptionAllowAskQA;
    private View mOptionCanComment;
    private View mOptionCanUpVote;
    private TextView mTxtAllQuestions;
    private TextView mTxtAnswerQaOnly;
    private TextView mTxtCanComment;
    private TextView mTxtCanUpVote;
    private View mViewAllQuestions;
    private View mViewAnswerQaOnly;
    private View mViewDivider;

    public static void show(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null && !zMActivity.isDestroyed()) {
            new ZMQAMoreDialog().show(zMActivity.getSupportFragmentManager(), ZMQAMoreDialog.class.getName());
        }
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        SimpleActivity.show(zMActivity, ZMQAMoreDialog.class.getName(), new Bundle(), 0);
    }

    public static void dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ZMQAMoreDialog zMQAMoreDialog = (ZMQAMoreDialog) fragmentManager.findFragmentByTag(ZMQAMoreDialog.class.getName());
            if (zMQAMoreDialog != null) {
                zMQAMoreDialog.dismiss();
            }
        }
    }

    public static void refresh(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null && zMActivity.isActive()) {
            ZMQAMoreDialog zMQAMoreDialog = (ZMQAMoreDialog) zMActivity.getSupportFragmentManager().findFragmentByTag(ZMQAMoreDialog.class.getName());
            if (zMQAMoreDialog != null && zMQAMoreDialog.isAdded()) {
                zMQAMoreDialog.update();
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_dialog_qa_more, viewGroup, false);
        this.mOptionAllowAskQA = inflate.findViewById(C4558R.C4560id.optionChkAllowAskQA);
        this.mOptionCanComment = inflate.findViewById(C4558R.C4560id.optionChkCanComment);
        this.mOptionCanUpVote = inflate.findViewById(C4558R.C4560id.optionChkCanUpVote);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mOptionAllowAskQA.setOnClickListener(this);
        this.mOptionCanComment.setOnClickListener(this);
        this.mOptionCanUpVote.setOnClickListener(this);
        this.mChkAllowAskQA = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAllowAskQA);
        this.mChkCanUpVote = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkCanUpVote);
        this.mChkCanComment = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkCanComment);
        this.mTxtCanComment = (TextView) inflate.findViewById(C4558R.C4560id.txtCanComment);
        this.mTxtCanUpVote = (TextView) inflate.findViewById(C4558R.C4560id.txtCanUpVote);
        this.mViewAllQuestions = inflate.findViewById(C4558R.C4560id.llAllQuestions);
        this.mViewAnswerQaOnly = inflate.findViewById(C4558R.C4560id.llAnswerQaOnly);
        this.mImgAllQuestions = (ImageView) inflate.findViewById(C4558R.C4560id.imgSelectedAllQuestions);
        this.mImgAnswerQaOnly = (ImageView) inflate.findViewById(C4558R.C4560id.imgSelectedAnswerQaOnly);
        this.mTxtAllQuestions = (TextView) inflate.findViewById(C4558R.C4560id.txtAllQuestions);
        this.mTxtAnswerQaOnly = (TextView) inflate.findViewById(C4558R.C4560id.txtAnswerQaOnly);
        this.mViewDivider = inflate.findViewById(C4558R.C4560id.viewDivider);
        this.mViewAllQuestions.setOnClickListener(this);
        this.mViewAnswerQaOnly.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        update();
        return inflate;
    }

    public ZMQAMoreDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material).setView(createContent()).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    public void onPause() {
        super.onPause();
        ConfUI.getInstance().removeListener(this.mConfUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, long j) {
                    if (i == 30 || i == 32 || i == 33 || i == 34) {
                        ZMQAMoreDialog.this.update();
                    }
                    return true;
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        update();
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_dialog_qa_more, null);
        this.mOptionAllowAskQA = inflate.findViewById(C4558R.C4560id.optionChkAllowAskQA);
        this.mOptionCanComment = inflate.findViewById(C4558R.C4560id.optionChkCanComment);
        this.mOptionCanUpVote = inflate.findViewById(C4558R.C4560id.optionChkCanUpVote);
        this.mOptionAllowAskQA.setOnClickListener(this);
        this.mOptionCanComment.setOnClickListener(this);
        this.mOptionCanUpVote.setOnClickListener(this);
        this.mChkAllowAskQA = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAllowAskQA);
        this.mChkCanUpVote = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkCanUpVote);
        this.mChkCanComment = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkCanComment);
        this.mTxtCanComment = (TextView) inflate.findViewById(C4558R.C4560id.txtCanComment);
        this.mTxtCanUpVote = (TextView) inflate.findViewById(C4558R.C4560id.txtCanUpVote);
        this.mViewAllQuestions = inflate.findViewById(C4558R.C4560id.llAllQuestions);
        this.mViewAnswerQaOnly = inflate.findViewById(C4558R.C4560id.llAnswerQaOnly);
        this.mImgAllQuestions = (ImageView) inflate.findViewById(C4558R.C4560id.imgSelectedAllQuestions);
        this.mImgAnswerQaOnly = (ImageView) inflate.findViewById(C4558R.C4560id.imgSelectedAnswerQaOnly);
        this.mTxtAllQuestions = (TextView) inflate.findViewById(C4558R.C4560id.txtAllQuestions);
        this.mTxtAnswerQaOnly = (TextView) inflate.findViewById(C4558R.C4560id.txtAnswerQaOnly);
        this.mViewDivider = inflate.findViewById(C4558R.C4560id.viewDivider);
        this.mViewAllQuestions.setOnClickListener(this);
        this.mViewAnswerQaOnly.setOnClickListener(this);
        update();
        return inflate;
    }

    /* access modifiers changed from: private */
    public void update() {
        if (this.mChkAllowAskQA != null) {
            ConfMgr instance = ConfMgr.getInstance();
            this.mChkAllowAskQA.setChecked(instance.isAllowAskQuestionAnonymously());
            updateAllowAttendeeViewAllQuestion(instance.isAllowAttendeeViewAllQuestion());
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.optionChkAllowAskQA) {
            onClickOptionChkAllowAskQA();
        } else if (id == C4558R.C4560id.optionChkCanComment) {
            onClickOptionChkCanComment();
        } else if (id == C4558R.C4560id.optionChkCanUpVote) {
            onClickOptionChkCanUpVote();
        } else if (id == C4558R.C4560id.llAnswerQaOnly) {
            onClickAnswerQaOnly();
        } else if (id == C4558R.C4560id.llAllQuestions) {
            onClickAllQuestions();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnBack() {
        finishFragment(true);
    }

    private void onClickAnswerQaOnly() {
        ConfMgr instance = ConfMgr.getInstance();
        if (instance.isAllowAttendeeViewAllQuestion() && instance.handleConfCmd(123)) {
            updateAllowAttendeeViewAllQuestion(false);
        }
    }

    private void onClickAllQuestions() {
        ConfMgr instance = ConfMgr.getInstance();
        if (!instance.isAllowAttendeeViewAllQuestion() && instance.handleConfCmd(122)) {
            updateAllowAttendeeViewAllQuestion(true);
        }
    }

    private void onClickOptionChkAllowAskQA() {
        boolean isAllowAskQuestionAnonymously = ConfMgr.getInstance().isAllowAskQuestionAnonymously();
        if (ConfMgr.getInstance().handleConfCmd(isAllowAskQuestionAnonymously ? 121 : 120)) {
            this.mChkAllowAskQA.setChecked(!isAllowAskQuestionAnonymously);
        }
    }

    private void onClickOptionChkCanComment() {
        boolean isAllowAttendeeAnswerQuestion = ConfMgr.getInstance().isAllowAttendeeAnswerQuestion();
        if (ConfMgr.getInstance().handleConfCmd(isAllowAttendeeAnswerQuestion ? 127 : 126)) {
            this.mChkCanComment.setChecked(!isAllowAttendeeAnswerQuestion);
        }
    }

    private void onClickOptionChkCanUpVote() {
        boolean isAllowAttendeeUpvoteQuestion = ConfMgr.getInstance().isAllowAttendeeUpvoteQuestion();
        if (ConfMgr.getInstance().handleConfCmd(isAllowAttendeeUpvoteQuestion ? 125 : 124)) {
            this.mChkCanUpVote.setChecked(!isAllowAttendeeUpvoteQuestion);
        }
    }

    private void updateAllowAttendeeViewAllQuestion(boolean z) {
        ConfMgr instance = ConfMgr.getInstance();
        CmmConfContext confContext = instance.getConfContext();
        if (confContext != null) {
            Resources resources = getResources();
            if (confContext.isAllowAttendeeViewAllQuestionChangable()) {
                this.mViewAllQuestions.setEnabled(true);
                this.mViewAnswerQaOnly.setEnabled(true);
                this.mImgAllQuestions.setAlpha(1.0f);
                this.mImgAnswerQaOnly.setAlpha(1.0f);
                this.mTxtAllQuestions.setTextColor(resources.getColor(C4558R.color.zm_text_light_dark));
                this.mTxtAnswerQaOnly.setTextColor(resources.getColor(C4558R.color.zm_text_light_dark));
            } else {
                this.mViewAllQuestions.setEnabled(false);
                this.mViewAnswerQaOnly.setEnabled(false);
                this.mImgAllQuestions.setAlpha(0.3f);
                this.mImgAnswerQaOnly.setAlpha(0.3f);
                this.mTxtAllQuestions.setTextColor(resources.getColor(C4558R.color.zm_text_dim));
                this.mTxtAnswerQaOnly.setTextColor(resources.getColor(C4558R.color.zm_text_dim));
            }
            if (z) {
                this.mImgAllQuestions.setVisibility(0);
                this.mImgAnswerQaOnly.setVisibility(4);
                this.mViewDivider.setVisibility(0);
                this.mOptionCanComment.setVisibility(0);
                this.mOptionCanUpVote.setVisibility(0);
                this.mChkCanUpVote.setChecked(instance.isAllowAttendeeUpvoteQuestion());
                this.mChkCanComment.setChecked(instance.isAllowAttendeeAnswerQuestion());
                if (confContext.isAllowAttendeeUpvoteQuestionChangable()) {
                    this.mOptionCanUpVote.setEnabled(true);
                    this.mChkCanUpVote.setEnabled(true);
                    this.mTxtCanUpVote.setTextColor(resources.getColor(C4558R.color.zm_text_light_dark));
                } else {
                    this.mOptionCanUpVote.setEnabled(false);
                    this.mChkCanUpVote.setEnabled(false);
                    this.mTxtCanUpVote.setTextColor(resources.getColor(C4558R.color.zm_text_dim));
                }
                if (confContext.isAllowAttendeeAnswerQuestionChangable()) {
                    this.mOptionCanComment.setEnabled(true);
                    this.mChkCanComment.setEnabled(true);
                    this.mTxtCanComment.setTextColor(resources.getColor(C4558R.color.zm_text_light_dark));
                } else {
                    this.mOptionCanComment.setEnabled(false);
                    this.mChkCanComment.setEnabled(false);
                    this.mTxtCanComment.setTextColor(resources.getColor(C4558R.color.zm_text_dim));
                }
            } else {
                this.mImgAllQuestions.setVisibility(4);
                this.mImgAnswerQaOnly.setVisibility(0);
                this.mViewDivider.setVisibility(8);
                this.mOptionCanComment.setVisibility(8);
                this.mOptionCanUpVote.setVisibility(8);
            }
        }
    }
}
