package com.zipow.videobox.poll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.NumberFormat;
import java.util.ArrayList;
import p021us.zoom.videomeetings.C4558R;

public class PollingResultItem {
    @NonNull
    private ArrayList<Answer> mAnswers = new ArrayList<>();
    private String mQuestion;
    private int mQuestionType = 0;
    private boolean mbShowSelectedCount = true;

    public static class Answer {
        private String mContent;
        private int mCountSelected;
        private float mPercent;

        public Answer(String str, int i, float f) {
            this.mContent = str;
            this.mCountSelected = i;
            this.mPercent = f;
        }

        public String getContent() {
            return this.mContent;
        }

        public void setContent(String str) {
            this.mContent = str;
        }

        public int getCountSelected() {
            return this.mCountSelected;
        }

        public void setCountSelected(int i) {
            this.mCountSelected = i;
        }

        public float getPercent() {
            return this.mPercent;
        }

        public void setPercent(float f) {
            this.mPercent = f;
        }
    }

    public String getQuestion() {
        return this.mQuestion;
    }

    public void setQuestion(String str) {
        this.mQuestion = str;
    }

    public void setQuestionType(int i) {
        this.mQuestionType = i;
    }

    public int getQuestionType() {
        return this.mQuestionType;
    }

    public void addAnswer(@Nullable Answer answer) {
        if (answer != null) {
            this.mAnswers.add(answer);
        }
    }

    public int getAnswerCount() {
        return this.mAnswers.size();
    }

    public Answer getAnswerAt(int i) {
        return (Answer) this.mAnswers.get(i);
    }

    public void setShowSelectedCount(boolean z) {
        this.mbShowSelectedCount = z;
    }

    public boolean getShowSelectedCount() {
        return this.mbShowSelectedCount;
    }

    @NonNull
    public ArrayList<Integer> getMaxSelectedAnswers() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        int answerCount = getAnswerCount();
        if (answerCount <= 0) {
            return arrayList;
        }
        getAnswerAt(0).getCountSelected();
        int i = 0;
        for (int i2 = 0; i2 < answerCount; i2++) {
            Answer answerAt = getAnswerAt(i2);
            if (answerAt != null) {
                int countSelected = answerAt.getCountSelected();
                if (i < countSelected) {
                    i = countSelected;
                }
            }
        }
        for (int i3 = 0; i3 < answerCount; i3++) {
            Answer answerAt2 = getAnswerAt(i3);
            if (answerAt2 != null && answerAt2.getCountSelected() == i) {
                arrayList.add(Integer.valueOf(i3));
            }
        }
        return arrayList;
    }

    @Nullable
    public View getView(int i, @NonNull Context context, @Nullable View view, ViewGroup viewGroup) {
        LayoutInflater from = LayoutInflater.from(context);
        if (view == null || !"PollingResultItemView".equals(view.getTag())) {
            view = from.inflate(C4558R.layout.zm_polling_result_question, viewGroup, false);
            view.setTag("PollingResultItemView");
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtQuestion);
        ViewGroup viewGroup2 = (ViewGroup) view.findViewById(C4558R.C4560id.panelAnswersContainer);
        int i2 = i + 1;
        StringBuilder sb = new StringBuilder();
        sb.append(i2);
        sb.append(". ");
        sb.append(getQuestion());
        String sb2 = sb.toString();
        if (this.mQuestionType == 1) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(" (");
            sb3.append(context.getString(C4558R.string.zm_polling_multiple_choice));
            sb3.append(")");
            sb2 = sb3.toString();
        }
        textView.setText(sb2);
        View[] viewArr = new View[viewGroup2.getChildCount()];
        for (int i3 = 0; i3 < viewArr.length; i3++) {
            viewArr[i3] = viewGroup2.getChildAt(i3);
        }
        viewGroup2.removeAllViews();
        ArrayList maxSelectedAnswers = getMaxSelectedAnswers();
        int answerCount = getAnswerCount();
        int i4 = 0;
        while (i4 < answerCount) {
            Answer answerAt = getAnswerAt(i4);
            if (answerAt != null) {
                viewGroup2.addView(buildAnswerView(answerAt, maxSelectedAnswers.contains(Integer.valueOf(i4)), from, i4 < viewArr.length ? viewArr[i4] : null, viewGroup2));
            }
            i4++;
        }
        return view;
    }

    @NonNull
    private View buildAnswerView(@NonNull Answer answer, boolean z, @NonNull LayoutInflater layoutInflater, @Nullable View view, ViewGroup viewGroup) {
        if (view == null || !"AnswerView".equals(view.getTag())) {
            view = layoutInflater.inflate(C4558R.layout.zm_polling_result_answer, viewGroup, false);
            view.setTag("AnswerView");
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtPercent);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtSelectedCount);
        ProgressBar progressBar = (ProgressBar) view.findViewById(C4558R.C4560id.percent);
        ((TextView) view.findViewById(C4558R.C4560id.txtAnswer)).setText(answer.getContent());
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(answer.getCountSelected());
        sb.append(")");
        textView2.setText(sb.toString());
        if (getShowSelectedCount()) {
            textView2.setVisibility(0);
        } else {
            textView2.setVisibility(8);
        }
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(1);
        String format = numberInstance.format((double) answer.getPercent());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(format);
        sb2.append("%");
        textView.setText(sb2.toString());
        if (z) {
            progressBar.setProgress(0);
            progressBar.setSecondaryProgress((int) (answer.getPercent() + 0.5f));
        } else {
            progressBar.setProgress((int) (answer.getPercent() + 0.5f));
            progressBar.setSecondaryProgress(0);
        }
        return view;
    }
}
