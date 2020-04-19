package com.zipow.videobox.fragment.meeting.p011qa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import com.zipow.videobox.fragment.meeting.p011qa.dialog.ZMQAAnswerDialog;
import com.zipow.videobox.fragment.meeting.p011qa.model.BaseQAMultiItemEntity;
import java.util.Collections;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter.OnItemChildClickListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAAttendeeTabFragment */
public class ZMQAAttendeeTabFragment extends Fragment {
    private static final String KEY_QUESTION_MODE = "KEY_QUESTION_MODE";
    private static final String TAG = "ZMQAAttendeeTabFragment";
    private IConfUIListener mConfUIListener;
    private View mPanelNoItemMsg;
    private IZoomQAUIListener mQAUIListener;
    private int mQuestionsMode = ZMQuestionsMode.MODE_ATTENDEE_ALL_QUESTIONS.ordinal();
    private RecyclerView mRecyclerView;
    private TextView mTxtNoItemMsg;
    /* access modifiers changed from: private */
    public ZMQAAttendeeViewerAdapter mZMQAAttendeeViewerAdapter;

    @NonNull
    public static ZMQAAttendeeTabFragment newInstance(int i) {
        ZMQAAttendeeTabFragment zMQAAttendeeTabFragment = new ZMQAAttendeeTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_QUESTION_MODE, i);
        zMQAAttendeeTabFragment.setArguments(bundle);
        return zMQAAttendeeTabFragment;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mQuestionsMode = arguments.getInt(KEY_QUESTION_MODE, ZMQuestionsMode.MODE_ATTENDEE_ALL_QUESTIONS.ordinal());
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_qa_tab_question, viewGroup, false);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mTxtNoItemMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtMsg);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.recyclerView);
        boolean isSpokenFeedbackEnabled = AccessibilityUtil.isSpokenFeedbackEnabled(getContext());
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mZMQAAttendeeViewerAdapter = new ZMQAAttendeeViewerAdapter(Collections.EMPTY_LIST, isSpokenFeedbackEnabled);
        if (isSpokenFeedbackEnabled) {
            this.mRecyclerView.setItemAnimator(null);
            this.mZMQAAttendeeViewerAdapter.setHasStableIds(true);
        }
        this.mRecyclerView.setAdapter(this.mZMQAAttendeeViewerAdapter);
        this.mZMQAAttendeeViewerAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            public void onItemChildClick(ZMBaseRecyclerViewAdapter zMBaseRecyclerViewAdapter, @NonNull View view, int i) {
                BaseQAMultiItemEntity baseQAMultiItemEntity = (BaseQAMultiItemEntity) ZMQAAttendeeTabFragment.this.mZMQAAttendeeViewerAdapter.getItem(i);
                if (baseQAMultiItemEntity != null) {
                    int itemType = baseQAMultiItemEntity.getItemType();
                    if (itemType != 1) {
                        if (itemType == 4) {
                            if (view.getId() == C4558R.C4560id.plMoreFeedback) {
                                ZMQAAttendeeTabFragment.this.onClickMoreFeedback(i);
                            } else if (view.getId() == C4558R.C4560id.btnAnswer) {
                                ZMQAAttendeeTabFragment.this.onClickAnswer(baseQAMultiItemEntity.getmItemId());
                            }
                        }
                    } else if (view.getId() == C4558R.C4560id.llUpvote) {
                        ZMQAAttendeeTabFragment.this.onClickUpVote(baseQAMultiItemEntity.getmItemId(), i);
                    }
                }
            }
        });
        return inflate;
    }

    public void onPause() {
        super.onPause();
        ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
        ConfUI.getInstance().removeListener(this.mConfUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onRefreshQAUI() {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onReceiveQuestion(String str) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onAddQuestion(String str, boolean z) {
                    if (ZMQAHelper.isQuestionSent(str)) {
                        ZMQAAttendeeTabFragment.this.updateData();
                    }
                }

                public void onAddAnswer(String str, boolean z) {
                    if (ZMQAHelper.isAnswerSent(str)) {
                        ZMQAAttendeeTabFragment.this.updateData();
                    }
                }

                public void onUserLivingReply(String str) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onReceiveAnswer(String str) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onQuestionMarkedAsAnswered(String str) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void notifyConnectResult(boolean z) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onAnswerSenderNameChanged(String str, String str2) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onQuestionMarkedAsDismissed(String str) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onReopenQuestion(String str) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onUpvoteQuestion(String str, boolean z) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }

                public void onRevokeUpvoteQuestion(String str, boolean z) {
                    ZMQAAttendeeTabFragment.this.updateData();
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, long j) {
                    if (i == 33) {
                        ZMQAAttendeeTabFragment.this.updateData();
                    } else if (i == 34) {
                        ZMQAAttendeeTabFragment.this.onAllowAnswerQuestionStatusChanged();
                    }
                    return true;
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        updateData();
    }

    /* access modifiers changed from: private */
    public void onAllowAnswerQuestionStatusChanged() {
        if (!ConfMgr.getInstance().isAllowAttendeeAnswerQuestion()) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                ZMQAAnswerDialog.dismiss(zMActivity.getSupportFragmentManager());
            }
        }
        updateData();
    }

    /* access modifiers changed from: private */
    public void onClickUpVote(String str, int i) {
        if (ZMQAHelper.isAttendeeCanUpvote()) {
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent != null && !StringUtil.isEmptyOrNull(str)) {
                ZoomQAQuestion questionByID = qAComponent.getQuestionByID(str);
                if (questionByID != null && !questionByID.isMarkedAsDismissed()) {
                    if (!questionByID.isMySelfUpvoted() ? qAComponent.upvoteQuestion(str) : qAComponent.revokeUpvoteQuestion(str)) {
                        this.mZMQAAttendeeViewerAdapter.notifyItemChanged(i);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickAnswer(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            ZMQAAnswerDialog.show((ZMActivity) getActivity(), str);
        }
    }

    /* access modifiers changed from: private */
    public void onClickMoreFeedback(int i) {
        this.mZMQAAttendeeViewerAdapter.expandOrCollapse(i);
        updateData();
    }

    /* access modifiers changed from: private */
    public void updateData() {
        ZMQAAttendeeViewerAdapter zMQAAttendeeViewerAdapter = this.mZMQAAttendeeViewerAdapter;
        zMQAAttendeeViewerAdapter.refresh(ZMQAHelper.getZoomQuestionsForAttendee(this.mQuestionsMode, zMQAAttendeeViewerAdapter.getmExpandedItems()));
        updateNoItemMessage();
    }

    private void updateUpVoteQuestion(@NonNull String str, boolean z) {
        if (z || StringUtil.isEmptyOrNull(str)) {
            ZMQAAttendeeViewerAdapter zMQAAttendeeViewerAdapter = this.mZMQAAttendeeViewerAdapter;
            zMQAAttendeeViewerAdapter.refresh(ZMQAHelper.getZoomQuestionsForPanelist(this.mQuestionsMode, zMQAAttendeeViewerAdapter.getmExpandedItems()));
            updateNoItemMessage();
        } else if (!this.mZMQAAttendeeViewerAdapter.updateUpVoteQuestion(str)) {
            ZMQAAttendeeViewerAdapter zMQAAttendeeViewerAdapter2 = this.mZMQAAttendeeViewerAdapter;
            zMQAAttendeeViewerAdapter2.refresh(ZMQAHelper.getZoomQuestionsForPanelist(this.mQuestionsMode, zMQAAttendeeViewerAdapter2.getmExpandedItems()));
            updateNoItemMessage();
        }
    }

    private void updateNoItemMessage() {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null || !qAComponent.isStreamConflict()) {
            this.mRecyclerView.setVisibility(0);
            if (ZMQAHelper.getQustionCount(this.mQuestionsMode) == 0) {
                this.mTxtNoItemMsg.setText(C4558R.string.zm_qa_msg_no_question);
                this.mPanelNoItemMsg.setVisibility(0);
                return;
            }
            this.mPanelNoItemMsg.setVisibility(8);
            return;
        }
        this.mRecyclerView.setVisibility(4);
        this.mTxtNoItemMsg.setText(C4558R.string.zm_qa_msg_stream_conflict);
        this.mPanelNoItemMsg.setVisibility(0);
    }
}
