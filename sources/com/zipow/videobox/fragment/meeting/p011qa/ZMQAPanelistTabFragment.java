package com.zipow.videobox.fragment.meeting.p011qa;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter.OnItemChildClickListener;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter.OnItemLongClickListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAPanelistTabFragment */
public class ZMQAPanelistTabFragment extends Fragment {
    private static final String KEY_QUESTION_MODE = "KEY_QUESTION_MODE";
    private static final String TAG = "ZMQAPanelistTabFragment";
    private IConfUIListener mConfUIListener;
    /* access modifiers changed from: private */
    public String mDismissQuestionId;
    private View mPanelNoItemMsg;
    private IZoomQAUIListener mQAUIListener;
    /* access modifiers changed from: private */
    public int mQuestionsMode = ZMQuestionsMode.MODE_OPEN_QUESTIONS.ordinal();
    /* access modifiers changed from: private */
    public String mReOpenQuestionId;
    private RecyclerView mRecyclerView;
    private TextView mTxtNoItemMsg;
    /* access modifiers changed from: private */
    public ZMQAPanelistViewerAdapter mZMQAPanelistViewerAdapter;

    /* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAPanelistTabFragment$ContextMenuItem */
    static class ContextMenuItem extends ZMSimpleMenuItem {
        private String mLabel;

        @Nullable
        public Drawable getIcon() {
            return null;
        }

        public ContextMenuItem(String str) {
            this.mLabel = str;
        }

        @NonNull
        public String toString() {
            return this.mLabel;
        }

        public String getLabel() {
            return this.mLabel;
        }
    }

    @NonNull
    public static ZMQAPanelistTabFragment newInstance(int i) {
        ZMQAPanelistTabFragment zMQAPanelistTabFragment = new ZMQAPanelistTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_QUESTION_MODE, i);
        zMQAPanelistTabFragment.setArguments(bundle);
        return zMQAPanelistTabFragment;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mQuestionsMode = arguments.getInt(KEY_QUESTION_MODE, ZMQuestionsMode.MODE_ATTENDEE_ALL_QUESTIONS.ordinal());
        }
        if (bundle != null) {
            this.mDismissQuestionId = bundle.getString("mDismissQuestionId", null);
            this.mReOpenQuestionId = bundle.getString("mReOpenQuestionId", null);
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_qa_tab_question, viewGroup, false);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mTxtNoItemMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtMsg);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.recyclerView);
        boolean isSpokenFeedbackEnabled = AccessibilityUtil.isSpokenFeedbackEnabled(getContext());
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mZMQAPanelistViewerAdapter = new ZMQAPanelistViewerAdapter(Collections.EMPTY_LIST, this.mQuestionsMode, isSpokenFeedbackEnabled);
        if (isSpokenFeedbackEnabled) {
            this.mRecyclerView.setItemAnimator(null);
            this.mZMQAPanelistViewerAdapter.setHasStableIds(true);
        }
        this.mRecyclerView.setAdapter(this.mZMQAPanelistViewerAdapter);
        this.mZMQAPanelistViewerAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            public void onItemChildClick(ZMBaseRecyclerViewAdapter zMBaseRecyclerViewAdapter, @NonNull View view, int i) {
                BaseQAMultiItemEntity baseQAMultiItemEntity = (BaseQAMultiItemEntity) ZMQAPanelistTabFragment.this.mZMQAPanelistViewerAdapter.getItem(i);
                if (baseQAMultiItemEntity != null) {
                    int itemType = baseQAMultiItemEntity.getItemType();
                    if (itemType != 1) {
                        switch (itemType) {
                            case 6:
                                if (view.getId() != C4558R.C4560id.txtPositive) {
                                    if (view.getId() == C4558R.C4560id.txtNegative) {
                                        ZMQAPanelistTabFragment.this.onClickStartLiveAnswer(baseQAMultiItemEntity.getmItemId());
                                        break;
                                    }
                                } else {
                                    ZMQAPanelistTabFragment.this.onClickTextAnswer(baseQAMultiItemEntity.getmItemId());
                                    break;
                                }
                                break;
                            case 7:
                                if (view.getId() == C4558R.C4560id.plMoreFeedback) {
                                    ZMQAPanelistTabFragment.this.onClickMoreFeedback(i);
                                    break;
                                }
                                break;
                            case 8:
                                if (view.getId() == C4558R.C4560id.txtPositive) {
                                    ZMQAPanelistTabFragment.this.onClickMarkAsAnswered(baseQAMultiItemEntity.getmItemId());
                                    break;
                                }
                                break;
                        }
                    } else if (view.getId() == C4558R.C4560id.llUpvote) {
                        ZMQAPanelistTabFragment.this.onClickUpVote(baseQAMultiItemEntity.getmItemId(), i);
                    }
                }
            }
        });
        this.mZMQAPanelistViewerAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(ZMBaseRecyclerViewAdapter zMBaseRecyclerViewAdapter, View view, int i) {
                BaseQAMultiItemEntity baseQAMultiItemEntity = (BaseQAMultiItemEntity) ZMQAPanelistTabFragment.this.mZMQAPanelistViewerAdapter.getItem(i);
                if (baseQAMultiItemEntity != null && baseQAMultiItemEntity.getItemType() == 1) {
                    ZoomQAQuestion question = baseQAMultiItemEntity.getQuestion();
                    if (question == null || question.getLiveAnsweringCount() > 0) {
                        return false;
                    }
                    String str = baseQAMultiItemEntity.getmItemId();
                    if (!StringUtil.isEmptyOrNull(str)) {
                        if (ZMQAPanelistTabFragment.this.mQuestionsMode == ZMQuestionsMode.MODE_DISMISSED_QUESTIONS.ordinal()) {
                            ZMQAPanelistTabFragment.this.mReOpenQuestionId = str;
                            ZMQAPanelistTabFragment.this.popReopen(view);
                        } else {
                            ZMQAPanelistTabFragment.this.mDismissQuestionId = str;
                            ZMQAPanelistTabFragment.this.popDismiss(view);
                        }
                    }
                }
                return false;
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
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void onReceiveQuestion(String str) {
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void onAddQuestion(String str, boolean z) {
                    if (ZMQAPanelistTabFragment.this.mQuestionsMode == ZMQuestionsMode.MODE_OPEN_QUESTIONS.ordinal() && ZMQAHelper.isQuestionSent(str)) {
                        ZMQAPanelistTabFragment.this.updateData();
                    }
                }

                public void onAddAnswer(String str, boolean z) {
                    if (ZMQAHelper.isAnswerSent(str)) {
                        ZMQAPanelistTabFragment.this.updateData();
                    }
                }

                public void onReceiveAnswer(String str) {
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void onQuestionMarkedAsAnswered(String str) {
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void onUserLivingReply(String str) {
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void notifyConnectResult(boolean z) {
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void onAnswerSenderNameChanged(String str, String str2) {
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void onQuestionMarkedAsDismissed(String str) {
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void onReopenQuestion(String str) {
                    ZMQAPanelistTabFragment.this.updateData();
                }

                public void onUpvoteQuestion(@NonNull String str, boolean z) {
                    ZMQAPanelistTabFragment.this.updateUpVoteQuestion(str, z);
                }

                public void onRevokeUpvoteQuestion(@NonNull String str, boolean z) {
                    ZMQAPanelistTabFragment.this.updateUpVoteQuestion(str, z);
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, long j) {
                    if (i == 33) {
                        ZMQAPanelistTabFragment.this.updateData();
                    }
                    return true;
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        updateData();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (!StringUtil.isEmptyOrNull(this.mDismissQuestionId)) {
            bundle.putString("mDismissQuestionId", this.mDismissQuestionId);
        }
        if (!StringUtil.isEmptyOrNull(this.mReOpenQuestionId)) {
            bundle.putString("mReOpenQuestionId", this.mReOpenQuestionId);
        }
    }

    /* access modifiers changed from: private */
    public void onClickUpVote(String str, int i) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null && !StringUtil.isEmptyOrNull(str)) {
            ZoomQAQuestion questionByID = qAComponent.getQuestionByID(str);
            if (questionByID == null || !questionByID.isMySelfUpvoted() ? qAComponent.upvoteQuestion(str) : qAComponent.revokeUpvoteQuestion(str)) {
                this.mZMQAPanelistViewerAdapter.notifyItemChanged(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickMarkAsAnswered(String str) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null) {
            if (StringUtil.isEmptyOrNull(str) || qAComponent.endLiving(str)) {
                updateData();
            } else {
                Toast.makeText(getContext(), C4558R.string.zm_qa_msg_mark_live_answer_done_failed, 1).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickMoreFeedback(int i) {
        this.mZMQAPanelistViewerAdapter.expandOrCollapse(i);
        updateData();
    }

    /* access modifiers changed from: private */
    public void onClickTextAnswer(String str) {
        ZMQAAnswerDialog.show((ZMActivity) getActivity(), str);
    }

    /* access modifiers changed from: private */
    public void onClickStartLiveAnswer(String str) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null) {
            qAComponent.startLiving(str);
        }
    }

    private void onSelectDismissedQuestion(@NonNull String str) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null) {
            qAComponent.dismissQuestion(str);
        }
    }

    /* access modifiers changed from: private */
    public void popDismiss(View view) {
        ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getContext(), false);
        zMMenuAdapter.addItem(new ZMSimpleMenuItem(getString(C4558R.string.zm_qa_btn_dismiss_question_34305), (Drawable) null));
        ZMAlertDialog create = new Builder(getContext()).setTitle(C4558R.string.zm_qa_title_qa).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!StringUtil.isEmptyOrNull(ZMQAPanelistTabFragment.this.mDismissQuestionId)) {
                    ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                    if (qAComponent != null) {
                        qAComponent.dismissQuestion(ZMQAPanelistTabFragment.this.mDismissQuestionId);
                    }
                }
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    /* access modifiers changed from: private */
    public void popReopen(View view) {
        ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getContext(), false);
        zMMenuAdapter.addItem(new ZMSimpleMenuItem(getString(C4558R.string.zm_btn_reopen_41047), (Drawable) null));
        ZMAlertDialog create = new Builder(getContext()).setTitle(C4558R.string.zm_qa_title_qa).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!StringUtil.isEmptyOrNull(ZMQAPanelistTabFragment.this.mReOpenQuestionId)) {
                    ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                    if (qAComponent != null) {
                        qAComponent.reopenQuestion(ZMQAPanelistTabFragment.this.mReOpenQuestionId);
                    }
                }
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    /* access modifiers changed from: private */
    public void updateData() {
        ZMQAPanelistViewerAdapter zMQAPanelistViewerAdapter = this.mZMQAPanelistViewerAdapter;
        zMQAPanelistViewerAdapter.refresh(ZMQAHelper.getZoomQuestionsForPanelist(this.mQuestionsMode, zMQAPanelistViewerAdapter.getmExpandedItems()));
        updateNoItemMessage();
    }

    /* access modifiers changed from: private */
    public void updateUpVoteQuestion(@NonNull String str, boolean z) {
        if (z || StringUtil.isEmptyOrNull(str)) {
            ZMQAPanelistViewerAdapter zMQAPanelistViewerAdapter = this.mZMQAPanelistViewerAdapter;
            zMQAPanelistViewerAdapter.refresh(ZMQAHelper.getZoomQuestionsForPanelist(this.mQuestionsMode, zMQAPanelistViewerAdapter.getmExpandedItems()));
            updateNoItemMessage();
        } else if (!this.mZMQAPanelistViewerAdapter.updateUpVoteQuestion(str)) {
            ZMQAPanelistViewerAdapter zMQAPanelistViewerAdapter2 = this.mZMQAPanelistViewerAdapter;
            zMQAPanelistViewerAdapter2.refresh(ZMQAHelper.getZoomQuestionsForPanelist(this.mQuestionsMode, zMQAPanelistViewerAdapter2.getmExpandedItems()));
            updateNoItemMessage();
        }
    }

    private void updateNoItemMessage() {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null || !qAComponent.isStreamConflict()) {
            this.mRecyclerView.setVisibility(0);
            if (ZMQAHelper.getQustionCount(this.mQuestionsMode) == 0) {
                if (this.mQuestionsMode == ZMQuestionsMode.MODE_OPEN_QUESTIONS.ordinal()) {
                    this.mTxtNoItemMsg.setText(C4558R.string.zm_qa_msg_no_open_question);
                } else if (this.mQuestionsMode == ZMQuestionsMode.MODE_ANSWERED_QUESTIONS.ordinal()) {
                    this.mTxtNoItemMsg.setText(C4558R.string.zm_qa_msg_no_answered_question);
                } else if (this.mQuestionsMode == ZMQuestionsMode.MODE_DISMISSED_QUESTIONS.ordinal()) {
                    this.mTxtNoItemMsg.setText(C4558R.string.zm_qa_msg_no_dismissed_question_34305);
                }
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
