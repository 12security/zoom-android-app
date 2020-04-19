package com.zipow.videobox.poll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.poll.PollingResultItem.Answer;

public class PollingResultListView extends ListView {
    private PollingResultListAdapter mAdapter;

    public PollingResultListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public PollingResultListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public PollingResultListView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.mAdapter = new PollingResultListAdapter(context);
        if (isInEditMode()) {
            __editMode_loadItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
    }

    public void loadPollingResult(@Nullable IPollingDoc iPollingDoc, boolean z) {
        if (iPollingDoc != null) {
            int questionCount = iPollingDoc.getQuestionCount();
            for (int i = 0; i < questionCount; i++) {
                IPollingQuestion questionAt = iPollingDoc.getQuestionAt(i);
                if (questionAt != null) {
                    PollingResultItem pollingResultItem = new PollingResultItem();
                    pollingResultItem.setQuestion(questionAt.getQuestionText());
                    pollingResultItem.setQuestionType(questionAt.getQuestionType());
                    pollingResultItem.setShowSelectedCount(z);
                    int answerCount = questionAt.getAnswerCount();
                    for (int i2 = 0; i2 < answerCount; i2++) {
                        IPollingAnswer answerAt = questionAt.getAnswerAt(i2);
                        if (answerAt != null) {
                            String answerText = answerAt.getAnswerText();
                            int selectedCount = answerAt.getSelectedCount();
                            int totalVotedUserCount = iPollingDoc.getTotalVotedUserCount();
                            float f = 0.0f;
                            if (totalVotedUserCount > 0) {
                                f = ((float) (selectedCount * 100)) / ((float) totalVotedUserCount);
                            }
                            pollingResultItem.addAnswer(new Answer(answerText, selectedCount, f));
                        }
                    }
                    this.mAdapter.addItem(pollingResultItem);
                }
            }
        }
    }

    private void __editMode_loadItems(@NonNull PollingResultListAdapter pollingResultListAdapter) {
        for (int i = 0; i < 3; i++) {
            PollingResultItem pollingResultItem = new PollingResultItem();
            StringBuilder sb = new StringBuilder();
            sb.append("Question ");
            sb.append(i);
            pollingResultItem.setQuestion(sb.toString());
            pollingResultItem.addAnswer(new Answer("Answer content 1", 1, 10.5f));
            pollingResultItem.addAnswer(new Answer("Answer content 2", 5, 50.0f));
            pollingResultItem.addAnswer(new Answer("Answer content 3", 4, 40.0f));
            pollingResultListAdapter.addItem(pollingResultItem);
        }
    }
}
