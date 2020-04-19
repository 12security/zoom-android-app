package com.zipow.videobox.fragment.meeting.p011qa;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQAAnswer;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;
import com.zipow.videobox.fragment.meeting.p011qa.model.BaseQAMultiItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQAPanelistExpandCollapseItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQATextAnswerItemEntity;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseMultiItemRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewItemHolder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAPanelistViewerAdapter */
public class ZMQAPanelistViewerAdapter extends ZMBaseMultiItemRecyclerViewAdapter<BaseQAMultiItemEntity, ZMBaseRecyclerViewItemHolder> {
    private final boolean mEnableStableIds;
    @NonNull
    private HashMap<String, String> mExpandedItems = new HashMap<>();
    @Nullable
    private final ZoomQAComponent mQacomponent = ConfMgr.getInstance().getQAComponent();
    private int mQuestionsMode = ZMQuestionsMode.MODE_OPEN_QUESTIONS.ordinal();

    public ZMQAPanelistViewerAdapter(List<BaseQAMultiItemEntity> list, int i, boolean z) {
        super(list);
        this.mQuestionsMode = i;
        this.mEnableStableIds = z;
        addItemType(1, C4558R.layout.zm_qa_list_item_question);
        addItemType(2, C4558R.layout.zm_qa_list_item_live_answer);
        addItemType(3, C4558R.layout.zm_qa_list_item_answer);
        addItemType(6, C4558R.layout.zm_qa_list_item_panelist_action);
        addItemType(7, C4558R.layout.zm_qa_list_item_expand_collapse);
        addItemType(8, C4558R.layout.zm_qa_list_item_waiting_live_answer);
        addItemType(5, C4558R.layout.zm_qa_list_item_divider);
    }

    @NonNull
    public HashMap<String, String> getmExpandedItems() {
        return this.mExpandedItems;
    }

    public void refresh(List<BaseQAMultiItemEntity> list) {
        setNewData(list);
    }

    public boolean updateUpVoteQuestion(@NonNull String str) {
        List<BaseQAMultiItemEntity> data = getData();
        if (!CollectionsUtil.isListEmpty(data)) {
            int i = 0;
            for (BaseQAMultiItemEntity baseQAMultiItemEntity : data) {
                if (baseQAMultiItemEntity == null || baseQAMultiItemEntity.getItemType() != 1 || !str.equals(baseQAMultiItemEntity.getmItemId())) {
                    i++;
                } else {
                    notifyItemChanged(i);
                    return true;
                }
            }
        }
        return false;
    }

    public void expandOrCollapse(int i) {
        if (i < getItemCount()) {
            BaseQAMultiItemEntity baseQAMultiItemEntity = (BaseQAMultiItemEntity) getItem(i);
            if (baseQAMultiItemEntity != null && baseQAMultiItemEntity.getItemType() == 7) {
                ZoomQAQuestion question = baseQAMultiItemEntity.getQuestion();
                if (question != null) {
                    String itemID = question.getItemID();
                    if (StringUtil.isEmptyOrNull(itemID)) {
                        return;
                    }
                    if (this.mExpandedItems.containsKey(itemID)) {
                        this.mExpandedItems.remove(itemID);
                    } else {
                        this.mExpandedItems.put(itemID, itemID);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void convert(@NonNull ZMBaseRecyclerViewItemHolder zMBaseRecyclerViewItemHolder, @NonNull BaseQAMultiItemEntity baseQAMultiItemEntity) {
        int i;
        Resources resources;
        if (this.mQacomponent != null) {
            ZoomQAQuestion question = baseQAMultiItemEntity.getQuestion();
            if (question != null) {
                switch (baseQAMultiItemEntity.getItemType()) {
                    case 1:
                        zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtQuestion, (CharSequence) question.getText());
                        zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtQuestionName, (CharSequence) question.isAnonymous() ? this.mContext.getString(C4558R.string.zm_qa_msg_anonymous_attendee_asked_41047) : StringUtil.safeString(this.mQacomponent.getUserNameByJID(question.getSenderJID())));
                        zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtQuestionTime, (CharSequence) TimeUtil.formatTimeCap(this.mContext, question.getTimeStamp()));
                        if (ZMQAHelper.isAttendeeCanUpvote()) {
                            int upvoteNum = question.getUpvoteNum();
                            zMBaseRecyclerViewItemHolder.setVisible(C4558R.C4560id.txtUpVoteCount, upvoteNum != 0);
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtUpVoteCount, (CharSequence) String.valueOf(upvoteNum));
                            View view = zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.llUpvote);
                            boolean isMySelfUpvoted = question.isMySelfUpvoted();
                            zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.llUpvote, true);
                            if (this.mQuestionsMode == ZMQuestionsMode.MODE_DISMISSED_QUESTIONS.ordinal()) {
                                view.setEnabled(false);
                                zMBaseRecyclerViewItemHolder.setImageResource(C4558R.C4560id.imgUpVote, C4558R.C4559drawable.zm_ic_upvote_disable);
                                zMBaseRecyclerViewItemHolder.setTextColor(C4558R.C4560id.txtUpVoteCount, this.mContext.getResources().getColor(C4558R.color.zm_status_text_deep_grey));
                            } else {
                                view.setEnabled(true);
                                zMBaseRecyclerViewItemHolder.setImageResource(C4558R.C4560id.imgUpVote, isMySelfUpvoted ? C4558R.C4559drawable.zm_ic_upvote_active : C4558R.C4559drawable.zm_ic_upvote);
                                int i2 = C4558R.C4560id.txtUpVoteCount;
                                if (isMySelfUpvoted) {
                                    resources = this.mContext.getResources();
                                    i = C4558R.color.zm_text_light_orange;
                                } else {
                                    resources = this.mContext.getResources();
                                    i = C4558R.color.zm_status_text_deep_grey;
                                }
                                zMBaseRecyclerViewItemHolder.setTextColor(i2, resources.getColor(i));
                                zMBaseRecyclerViewItemHolder.addOnClickListener(C4558R.C4560id.llUpvote);
                            }
                            if (upvoteNum == 0) {
                                view.setContentDescription(this.mContext.getString(C4558R.string.zm_accessibility_upvpote_45121));
                            } else {
                                view.setContentDescription(this.mContext.getString(isMySelfUpvoted ? C4558R.string.zm_accessibility_my_upvpote_45121 : C4558R.string.zm_accessibility_others_upvpote_45121, new Object[]{Integer.valueOf(upvoteNum)}));
                            }
                        } else {
                            zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.llUpvote, false);
                        }
                        zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.dividerLine, !ZMQAHelper.isShouldShowLiveAnsweringForPanelist(question));
                        break;
                    case 2:
                        if (question.hasLiveAnswers() && question.getLiveAnsweringCount() == 0) {
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtLivingAnswerDesc, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_question_ansered_41047));
                            break;
                        } else {
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtLivingAnswerDesc, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_waiting_live_answer_41047, new Object[]{ZMQAHelper.getLiveAnsweringNames(this.mContext, question)}));
                            break;
                        }
                    case 3:
                        int index = ((ZMQATextAnswerItemEntity) baseQAMultiItemEntity).getIndex();
                        if (index < question.getAnswerCount()) {
                            ZoomQAAnswer answerAt = question.getAnswerAt(index);
                            if (answerAt != null) {
                                String senderJID = answerAt.getSenderJID();
                                if (StringUtil.isEmptyOrNull(senderJID) || !StringUtil.isSameString(this.mQacomponent.getMyJID(), senderJID)) {
                                    zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtAnswerName, (CharSequence) this.mQacomponent.getUserNameByJID(senderJID));
                                } else {
                                    zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtAnswerName, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_you));
                                }
                                zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtAnswerTime, (CharSequence) TimeUtil.formatTimeCap(this.mContext, answerAt.getTimeStamp()));
                                zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtAnswer, (CharSequence) answerAt.getText());
                                zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.txtPrivateAnswer, answerAt.isPrivate());
                                AvatarView avatarView = (AvatarView) zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.avatarView);
                                ParamsBuilder paramsBuilder = new ParamsBuilder();
                                if (!StringUtil.isEmptyOrNull(senderJID)) {
                                    CmmUser userByQAAttendeeJID = ConfMgr.getInstance().getUserByQAAttendeeJID(senderJID);
                                    if (userByQAAttendeeJID == null) {
                                        paramsBuilder.setResource(C4558R.C4559drawable.zm_no_avatar, null);
                                    } else if (userByQAAttendeeJID.isViewOnlyUser()) {
                                        paramsBuilder.setResource(C4558R.C4559drawable.zm_no_avatar, null);
                                    } else if (userByQAAttendeeJID.isH323User()) {
                                        paramsBuilder.setResource(C4558R.C4559drawable.zm_h323_avatar, null);
                                    } else if (userByQAAttendeeJID.isPureCallInUser()) {
                                        paramsBuilder.setResource(C4558R.C4559drawable.avatar_phone_green, null);
                                    } else {
                                        paramsBuilder.setName(this.mQacomponent.getUserNameByJID(senderJID), senderJID).setPath(userByQAAttendeeJID.getSmallPicPath());
                                    }
                                } else {
                                    paramsBuilder.setResource(C4558R.C4559drawable.zm_no_avatar, null);
                                }
                                avatarView.show(paramsBuilder);
                                break;
                            }
                        }
                        break;
                    case 6:
                        if (this.mQuestionsMode != ZMQuestionsMode.MODE_DISMISSED_QUESTIONS.ordinal()) {
                            if (ZMQAHelper.isShouldHideLiveAnswerButtonForPanelist(question)) {
                                zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.txtNegative, false);
                            } else {
                                zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.txtNegative, true);
                            }
                            zMBaseRecyclerViewItemHolder.addOnClickListener(C4558R.C4560id.txtPositive);
                            zMBaseRecyclerViewItemHolder.addOnClickListener(C4558R.C4560id.txtNegative);
                            break;
                        } else {
                            zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.txtNegative, false);
                            zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.txtPositive, false);
                            break;
                        }
                    case 7:
                        ZMQAPanelistExpandCollapseItemEntity zMQAPanelistExpandCollapseItemEntity = (ZMQAPanelistExpandCollapseItemEntity) baseQAMultiItemEntity;
                        ImageView imageView = (ImageView) zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.imgDropdown);
                        String str = baseQAMultiItemEntity.getmItemId();
                        if (str != null && this.mExpandedItems.containsKey(str)) {
                            imageView.setRotation(180.0f);
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtMoreFeedback, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_collapse_feedback_41047));
                        } else {
                            imageView.setRotation(0.0f);
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtMoreFeedback, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_count_feedbacks_41047, new Object[]{Integer.valueOf(zMQAPanelistExpandCollapseItemEntity.getmTxtAnswerCount())}));
                        }
                        zMBaseRecyclerViewItemHolder.addOnClickListener(C4558R.C4560id.plMoreFeedback);
                        break;
                    case 8:
                        zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtWaitingLiveAnswer, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_waiting_live_answer_41047, new Object[]{this.mContext.getString(C4558R.string.zm_qa_you)}));
                        zMBaseRecyclerViewItemHolder.addOnClickListener(C4558R.C4560id.txtPositive);
                        break;
                }
            }
        }
    }

    public long getItemId(int i) {
        if (this.mEnableStableIds) {
            BaseQAMultiItemEntity baseQAMultiItemEntity = (BaseQAMultiItemEntity) getItem(i - getHeaderLayoutCount());
            if (baseQAMultiItemEntity != null) {
                return (long) baseQAMultiItemEntity.hashCode();
            }
        }
        return super.getItemId(i);
    }
}
