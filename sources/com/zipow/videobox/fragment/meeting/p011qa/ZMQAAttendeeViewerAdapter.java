package com.zipow.videobox.fragment.meeting.p011qa;

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
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQAAttendeeActionItemEntity;
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

/* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAAttendeeViewerAdapter */
public class ZMQAAttendeeViewerAdapter extends ZMBaseMultiItemRecyclerViewAdapter<BaseQAMultiItemEntity, ZMBaseRecyclerViewItemHolder> {
    private final boolean mEnableStableIds;
    @NonNull
    private HashMap<String, String> mExpandedItems = new HashMap<>();
    @Nullable
    private final ZoomQAComponent mQacomponent = ConfMgr.getInstance().getQAComponent();

    public ZMQAAttendeeViewerAdapter(List<BaseQAMultiItemEntity> list, boolean z) {
        super(list);
        this.mEnableStableIds = z;
        addItemType(1, C4558R.layout.zm_qa_list_item_question);
        addItemType(2, C4558R.layout.zm_qa_list_item_live_answer);
        addItemType(3, C4558R.layout.zm_qa_list_item_answer);
        addItemType(4, C4558R.layout.zm_qa_list_item_action);
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
            if (baseQAMultiItemEntity != null && baseQAMultiItemEntity.getItemType() == 4) {
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
        if (this.mQacomponent != null) {
            ZoomQAQuestion question = baseQAMultiItemEntity.getQuestion();
            if (question != null) {
                switch (baseQAMultiItemEntity.getItemType()) {
                    case 1:
                        boolean isMarkedAsDismissed = question.isMarkedAsDismissed();
                        zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtQuestion, (CharSequence) question.getText());
                        if (this.mQacomponent.isJIDMyself(question.getSenderJID())) {
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtQuestionName, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_you));
                        } else {
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtQuestionName, (CharSequence) question.isAnonymous() ? this.mContext.getString(C4558R.string.zm_qa_msg_anonymous_attendee_asked_41047) : StringUtil.safeString(this.mQacomponent.getUserNameByJID(question.getSenderJID())));
                        }
                        zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtQuestionTime, (CharSequence) TimeUtil.formatTimeCap(this.mContext, question.getTimeStamp()));
                        boolean isAttendeeCanUpvote = ZMQAHelper.isAttendeeCanUpvote();
                        if (isAttendeeCanUpvote) {
                            int upvoteNum = question.getUpvoteNum();
                            zMBaseRecyclerViewItemHolder.setVisible(C4558R.C4560id.txtUpVoteCount, upvoteNum != 0);
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtUpVoteCount, (CharSequence) String.valueOf(question.getUpvoteNum()));
                            View view = zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.llUpvote);
                            boolean isMySelfUpvoted = question.isMySelfUpvoted();
                            zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.llUpvote, true);
                            if (isMarkedAsDismissed) {
                                zMBaseRecyclerViewItemHolder.setImageResource(C4558R.C4560id.imgUpVote, C4558R.C4559drawable.zm_ic_upvote_disable);
                                zMBaseRecyclerViewItemHolder.setTextColor(C4558R.C4560id.txtUpVoteCount, this.mContext.getResources().getColor(C4558R.color.zm_status_text_deep_grey));
                            } else {
                                if (isMySelfUpvoted) {
                                    zMBaseRecyclerViewItemHolder.setImageResource(C4558R.C4560id.imgUpVote, C4558R.C4559drawable.zm_ic_upvote_active);
                                    zMBaseRecyclerViewItemHolder.setTextColor(C4558R.C4560id.txtUpVoteCount, this.mContext.getResources().getColor(C4558R.color.zm_text_light_orange));
                                } else {
                                    zMBaseRecyclerViewItemHolder.setImageResource(C4558R.C4560id.imgUpVote, C4558R.C4559drawable.zm_ic_upvote);
                                    zMBaseRecyclerViewItemHolder.setTextColor(C4558R.C4560id.txtUpVoteCount, this.mContext.getResources().getColor(C4558R.color.zm_status_text_deep_grey));
                                }
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
                        zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.dividerLine, question.getAnswerCount() > 0 || isMarkedAsDismissed);
                        if (isMarkedAsDismissed && (isAttendeeCanUpvote || ZMQAHelper.isAttendeeCanComment())) {
                            zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtQuestion).setEnabled(false);
                            zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtQuestionName).setEnabled(false);
                            break;
                        } else {
                            zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtQuestion).setEnabled(true);
                            zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtQuestionName).setEnabled(true);
                            break;
                        }
                    case 2:
                        if (question.hasLiveAnswers() && question.getLiveAnsweringCount() == 0) {
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtLivingAnswerDesc, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_question_ansered_41047));
                            break;
                        } else {
                            zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtLivingAnswerDesc, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_waiting_live_answer_41047, new Object[]{ZMQAHelper.getLiveAnsweringNames(this.mContext, question)}));
                            break;
                        }
                    case 3:
                        ZMQATextAnswerItemEntity zMQATextAnswerItemEntity = (ZMQATextAnswerItemEntity) baseQAMultiItemEntity;
                        boolean isMarkedAsDismissed2 = question.isMarkedAsDismissed();
                        int index = zMQATextAnswerItemEntity.getIndex();
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
                                    if (userByQAAttendeeJID != null) {
                                        if (userByQAAttendeeJID.isViewOnlyUser()) {
                                            paramsBuilder.setResource(C4558R.C4559drawable.zm_no_avatar, null);
                                        } else if (userByQAAttendeeJID.isH323User()) {
                                            paramsBuilder.setResource(C4558R.C4559drawable.zm_h323_avatar, null);
                                        } else if (userByQAAttendeeJID.isPureCallInUser()) {
                                            paramsBuilder.setResource(C4558R.C4559drawable.avatar_phone_green, null);
                                        } else {
                                            paramsBuilder.setName(this.mQacomponent.getUserNameByJID(senderJID), senderJID).setPath(userByQAAttendeeJID.getSmallPicPath());
                                        }
                                        avatarView.show(paramsBuilder);
                                    } else {
                                        avatarView.show(paramsBuilder.setResource(C4558R.C4559drawable.zm_no_avatar, null));
                                    }
                                } else {
                                    avatarView.show(paramsBuilder.setResource(C4558R.C4559drawable.zm_no_avatar, null));
                                }
                                if (isMarkedAsDismissed2 && (ZMQAHelper.isAttendeeCanUpvote() || ZMQAHelper.isAttendeeCanComment())) {
                                    zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtPrivateAnswer).setEnabled(false);
                                    zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtAnswerName).setEnabled(false);
                                    zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtAnswer).setEnabled(false);
                                    break;
                                } else {
                                    zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtPrivateAnswer).setEnabled(true);
                                    zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtAnswerName).setEnabled(true);
                                    zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.txtAnswer).setEnabled(true);
                                    break;
                                }
                            }
                        }
                        break;
                    case 4:
                        ZMQAAttendeeActionItemEntity zMQAAttendeeActionItemEntity = (ZMQAAttendeeActionItemEntity) baseQAMultiItemEntity;
                        if (!zMQAAttendeeActionItemEntity.ismIsShowFeedback() && !ZMQAHelper.isAttendeeCanComment()) {
                            zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.llActionArea, false);
                            break;
                        } else {
                            zMBaseRecyclerViewItemHolder.setGone(C4558R.C4560id.llActionArea, true);
                            ImageView imageView = (ImageView) zMBaseRecyclerViewItemHolder.getView(C4558R.C4560id.imgDropdown);
                            String str = baseQAMultiItemEntity.getmItemId();
                            boolean z = str != null && this.mExpandedItems.containsKey(str);
                            imageView.setRotation(z ? 180.0f : 0.0f);
                            if (zMQAAttendeeActionItemEntity.ismIsShowFeedback()) {
                                zMBaseRecyclerViewItemHolder.setVisible(C4558R.C4560id.plMoreFeedback, true);
                                if (z) {
                                    zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtMoreFeedback, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_collapse_feedback_41047));
                                } else {
                                    zMBaseRecyclerViewItemHolder.setText(C4558R.C4560id.txtMoreFeedback, (CharSequence) this.mContext.getString(C4558R.string.zm_qa_msg_count_feedbacks_41047, new Object[]{Integer.valueOf(zMQAAttendeeActionItemEntity.getmTxtAnswerCount())}));
                                }
                                zMBaseRecyclerViewItemHolder.addOnClickListener(C4558R.C4560id.plMoreFeedback);
                            } else {
                                zMBaseRecyclerViewItemHolder.setVisible(C4558R.C4560id.plMoreFeedback, false);
                            }
                            if (ZMQAHelper.isAttendeeCanComment() && !question.isMarkedAsDismissed()) {
                                zMBaseRecyclerViewItemHolder.setVisible(C4558R.C4560id.btnAnswer, true);
                                zMBaseRecyclerViewItemHolder.addOnClickListener(C4558R.C4560id.btnAnswer);
                                break;
                            } else {
                                zMBaseRecyclerViewItemHolder.setVisible(C4558R.C4560id.btnAnswer, false);
                                break;
                            }
                        }
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
