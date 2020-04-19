package com.zipow.videobox.poll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.videomeetings.C4558R;

public class PollingQuestionFragment extends ZMFragment implements OnClickListener {
    public static final String ARG_IS_READONLY = "isReadOnly";
    public static final String ARG_POLLING_ID = "pollingId";
    public static final String ARG_QUESTION_ID = "questionId";
    public static final String ARG_QUESTION_INDEX = "questionIndex";
    public static final String ARG_READYONLY_MESSAGE_RES = "readOnlyMessageRes";
    private static final String TAG = "PollingQuestionFragment";
    private View mBtnBack;
    private Button mBtnNext;
    private Button mBtnPrev;
    private Button mBtnSubmitCenter;
    @NonNull
    private OnClickListener mChoiceClickListener = new OnClickListener() {
        public void onClick(View view) {
            PollingQuestionFragment.this.onClickChoiceItem(view);
        }
    };
    private boolean mIsReadOnly = false;
    private ViewGroup mPanelAnswersContainer;
    private ViewGroup mPanelButtons;
    @Nullable
    private String mPollingId;
    @Nullable
    private String mQuestionId;
    private int mQuestionIndex = -1;
    private int mQuestionType = 0;
    private int mReadOnlyMessageRes = 0;
    private TextView mTxtQuestion;
    private TextView mTxtQuestionIndex;
    private TextView mTxtReadOnlyMessage;
    private TextView mTxtTitle;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mQuestionIndex = arguments.getInt(ARG_QUESTION_INDEX, -1);
            this.mPollingId = arguments.getString("pollingId");
            this.mQuestionId = arguments.getString(ARG_QUESTION_ID);
            this.mIsReadOnly = arguments.getBoolean("isReadOnly");
            this.mReadOnlyMessageRes = arguments.getInt("readOnlyMessageRes");
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_polling_question, viewGroup, false);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnPrev = (Button) inflate.findViewById(C4558R.C4560id.btnPrev);
        this.mBtnNext = (Button) inflate.findViewById(C4558R.C4560id.btnNext);
        this.mBtnSubmitCenter = (Button) inflate.findViewById(C4558R.C4560id.btnSubmitCenter);
        this.mTxtQuestionIndex = (TextView) inflate.findViewById(C4558R.C4560id.txtQuestionIndex);
        this.mTxtQuestion = (TextView) inflate.findViewById(C4558R.C4560id.txtQuestion);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mPanelAnswersContainer = (ViewGroup) inflate.findViewById(C4558R.C4560id.panelAnswersContainer);
        this.mTxtReadOnlyMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtReadOnlyMessage);
        this.mPanelButtons = (ViewGroup) inflate.findViewById(C4558R.C4560id.panelButtons);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnPrev.setOnClickListener(this);
        this.mBtnNext.setOnClickListener(this);
        this.mBtnSubmitCenter.setOnClickListener(this);
        initView(layoutInflater, bundle);
        return inflate;
    }

    private void initView(@NonNull LayoutInflater layoutInflater, Bundle bundle) {
        PollingActivity pollingActivity = (PollingActivity) getActivity();
        if (pollingActivity != null) {
            int questionIndex = getQuestionIndex();
            int questionCount = pollingActivity.getQuestionCount();
            if (questionCount > 1) {
                this.mBtnSubmitCenter.setVisibility(8);
                if (questionIndex == questionCount - 1) {
                    if (this.mIsReadOnly) {
                        this.mBtnNext.setVisibility(8);
                    } else {
                        this.mBtnNext.setText(C4558R.string.zm_polling_btn_submit);
                    }
                }
            } else {
                this.mPanelButtons.setVisibility(8);
                if (this.mIsReadOnly) {
                    this.mBtnSubmitCenter.setVisibility(8);
                }
            }
            if (questionIndex == 0) {
                this.mBtnPrev.setVisibility(8);
            }
            int i = questionIndex + 1;
            TextView textView = this.mTxtQuestionIndex;
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(i));
            sb.append("/");
            sb.append(String.valueOf(pollingActivity.getQuestionCount()));
            textView.setText(sb.toString());
            IPollingDoc pollingDoc = getPollingDoc();
            if (pollingDoc != null) {
                String pollingName = pollingDoc.getPollingName();
                if (pollingName == null) {
                    pollingName = "";
                }
                this.mTxtTitle.setText(pollingName);
                String str = this.mQuestionId;
                if (str != null) {
                    IPollingQuestion questionById = pollingDoc.getQuestionById(str);
                    if (questionById != null) {
                        String questionText = questionById.getQuestionText();
                        if (questionCount > 1) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(i);
                            sb2.append(". ");
                            sb2.append(questionText);
                            questionText = sb2.toString();
                        }
                        if (questionById.getQuestionType() == 1) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(questionText);
                            sb3.append(" (");
                            sb3.append(getString(C4558R.string.zm_polling_multiple_choice));
                            sb3.append(")");
                            questionText = sb3.toString();
                        }
                        this.mTxtQuestion.setText(questionText);
                        buildAnswersComponents(layoutInflater, questionById);
                        updateButtonsStatus();
                        if (this.mReadOnlyMessageRes != 0) {
                            this.mTxtReadOnlyMessage.setVisibility(0);
                            this.mTxtReadOnlyMessage.setText(this.mReadOnlyMessageRes);
                        } else {
                            this.mTxtReadOnlyMessage.setVisibility(8);
                        }
                    }
                }
            }
        }
    }

    private void buildAnswersComponents(@NonNull LayoutInflater layoutInflater, IPollingQuestion iPollingQuestion) {
        this.mPanelAnswersContainer.removeAllViews();
        this.mQuestionType = iPollingQuestion.getQuestionType();
        int answerCount = iPollingQuestion.getAnswerCount();
        for (int i = 0; i < answerCount; i++) {
            IPollingAnswer answerAt = iPollingQuestion.getAnswerAt(i);
            if (answerAt != null) {
                buildAnswerComponent(layoutInflater, this.mQuestionType, answerAt, i);
            }
        }
    }

    private void buildAnswerComponent(@NonNull LayoutInflater layoutInflater, int i, IPollingAnswer iPollingAnswer, int i2) {
        String answerText = iPollingAnswer.getAnswerText();
        if (answerText == null) {
            answerText = "";
        }
        if (i == 0) {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(C4558R.layout.zm_polling_single_choice, this.mPanelAnswersContainer, false);
            TextView textView = (TextView) viewGroup.findViewById(C4558R.C4560id.txtContent);
            boolean isChecked = iPollingAnswer.isChecked();
            textView.setText(answerText);
            viewGroup.setSelected(isChecked);
            viewGroup.setTag(iPollingAnswer.getAnswerId());
            viewGroup.setEnabled(!this.mIsReadOnly);
            this.mPanelAnswersContainer.addView(viewGroup);
            viewGroup.setOnClickListener(this.mChoiceClickListener);
        } else if (i == 1) {
            ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(C4558R.layout.zm_polling_multiple_choice, this.mPanelAnswersContainer, false);
            TextView textView2 = (TextView) viewGroup2.findViewById(C4558R.C4560id.txtContent);
            boolean isChecked2 = iPollingAnswer.isChecked();
            textView2.setText(answerText);
            viewGroup2.setSelected(isChecked2);
            viewGroup2.setTag(iPollingAnswer.getAnswerId());
            viewGroup2.setEnabled(!this.mIsReadOnly);
            this.mPanelAnswersContainer.addView(viewGroup2);
            viewGroup2.setOnClickListener(this.mChoiceClickListener);
        }
    }

    /* access modifiers changed from: private */
    public void onClickChoiceItem(@Nullable View view) {
        if (view != null) {
            IPollingQuestion pollingQuestion = getPollingQuestion();
            if (pollingQuestion != null) {
                String str = (String) view.getTag();
                if (str != null) {
                    if (this.mQuestionType == 0) {
                        int childCount = this.mPanelAnswersContainer.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View childAt = this.mPanelAnswersContainer.getChildAt(i);
                            String str2 = (String) childAt.getTag();
                            if (str2 != null) {
                                IPollingAnswer answerById = pollingQuestion.getAnswerById(str2);
                                if (answerById != null) {
                                    childAt.setSelected(view == childAt);
                                    answerById.setChecked(childAt.isSelected());
                                }
                            }
                        }
                    } else {
                        view.setSelected(!view.isSelected());
                        IPollingAnswer answerById2 = pollingQuestion.getAnswerById(str);
                        if (answerById2 != null) {
                            answerById2.setChecked(view.isSelected());
                        } else {
                            return;
                        }
                    }
                    updateButtonsStatus();
                }
            }
        }
    }

    public int getQuestionIndex() {
        int i = this.mQuestionIndex;
        if (i >= 0) {
            return i;
        }
        Bundle arguments = getArguments();
        if (arguments == null) {
            return -1;
        }
        this.mQuestionIndex = arguments.getInt(ARG_QUESTION_INDEX, -1);
        return this.mQuestionIndex;
    }

    private IPollingQuestion getPollingQuestion() {
        if (this.mQuestionId == null) {
            return null;
        }
        IPollingDoc pollingDoc = getPollingDoc();
        if (pollingDoc == null) {
            return null;
        }
        return pollingDoc.getQuestionById(this.mQuestionId);
    }

    private IPollingDoc getPollingDoc() {
        if (this.mPollingId == null) {
            return null;
        }
        PollingActivity pollingActivity = (PollingActivity) getActivity();
        if (pollingActivity == null) {
            return null;
        }
        IPollingMgr pollingMgr = pollingActivity.getPollingMgr();
        if (pollingMgr == null) {
            return null;
        }
        IPollingDoc pollingDocById = pollingMgr.getPollingDocById(this.mPollingId);
        if (pollingDocById == null) {
            return null;
        }
        return pollingDocById;
    }

    private void updateButtonsStatus() {
        updateNextButtonStatus();
        updateSubmitCenterButtonStatus();
    }

    private void updateNextButtonStatus() {
        this.mBtnNext.setEnabled(this.mIsReadOnly || isAnswered());
    }

    private void updateSubmitCenterButtonStatus() {
        this.mBtnSubmitCenter.setEnabled(this.mIsReadOnly || isAnswered());
    }

    private boolean isAnswered() {
        int childCount = this.mPanelAnswersContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.mPanelAnswersContainer.getChildAt(i).isSelected()) {
                return true;
            }
        }
        return false;
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        } else if (view == this.mBtnPrev) {
            onClickBtnPrev();
        } else if (view == this.mBtnNext) {
            onClickBtnNext();
        } else if (view == this.mBtnSubmitCenter) {
            onClickBtnSubmit();
        }
    }

    private void onClickBtnBack() {
        PollingActivity pollingActivity = (PollingActivity) getActivity();
        if (pollingActivity != null) {
            pollingActivity.finish();
        }
    }

    private void onClickBtnPrev() {
        PollingActivity pollingActivity = (PollingActivity) getActivity();
        if (pollingActivity != null) {
            pollingActivity.showPreviousQuesion();
        }
    }

    private void onClickBtnNext() {
        if (this.mIsReadOnly || isAnswered()) {
            PollingActivity pollingActivity = (PollingActivity) getActivity();
            if (pollingActivity != null) {
                if (getQuestionIndex() < pollingActivity.getQuestionCount() - 1) {
                    pollingActivity.showNextQuestion();
                } else {
                    submit();
                }
            }
        }
    }

    private void onClickBtnSubmit() {
        if (this.mIsReadOnly || isAnswered()) {
            submit();
        }
    }

    private void submit() {
        PollingActivity pollingActivity = (PollingActivity) getActivity();
        if (pollingActivity != null) {
            pollingActivity.submitPolling();
        }
    }
}
