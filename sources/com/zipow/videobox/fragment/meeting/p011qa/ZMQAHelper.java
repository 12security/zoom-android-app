package com.zipow.videobox.fragment.meeting.p011qa;

import android.content.Context;
import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQAAnswer;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;
import com.zipow.videobox.fragment.meeting.p011qa.model.BaseQAMultiItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQAAttendeeActionItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQADividerItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQALiveAnswerItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQAPanelistActionItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQAPanelistExpandCollapseItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQAQuestionItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQATextAnswerItemEntity;
import com.zipow.videobox.fragment.meeting.p011qa.model.ZMQAWaitingLiveAnswerItemEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAHelper */
public class ZMQAHelper {
    public static final int MAX_SHOW_FEEDBACK_COUNT = 2;

    public static int getTabWidth(Context context, int i) {
        int displayMinWidthInDip = (int) (UIUtil.getDisplayMinWidthInDip(context) / ((float) i));
        return displayMinWidthInDip - (displayMinWidthInDip / (i * i));
    }

    public static List<BaseQAMultiItemEntity> getZoomQuestionsForAttendee(int i, @NonNull HashMap<String, String> hashMap) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (i == ZMQuestionsMode.MODE_ATTENDEE_ALL_QUESTIONS.ordinal()) {
            int questionCount = qAComponent.getQuestionCount();
            int i2 = 0;
            while (i2 < questionCount) {
                ZoomQAQuestion questionAt = qAComponent.getQuestionAt(i2);
                if (questionAt != null) {
                    int state = questionAt.getState();
                    if (!(state == 3 || state == 4)) {
                        String itemID = questionAt.getItemID();
                        splitQuestionIntoListItemsForAttendee(arrayList, questionAt, itemID != null && hashMap.containsKey(itemID), i2 != questionCount + -1);
                    }
                }
                i2++;
            }
        } else if (i == ZMQuestionsMode.MODE_ATTENDEE_MY_QUESTIONS.ordinal()) {
            int myQuestionCount = qAComponent.getMyQuestionCount();
            int i3 = 0;
            while (i3 < myQuestionCount) {
                ZoomQAQuestion myQuestionAt = qAComponent.getMyQuestionAt(i3);
                if (myQuestionAt != null) {
                    int state2 = myQuestionAt.getState();
                    if (!(state2 == 3 || state2 == 4)) {
                        String itemID2 = myQuestionAt.getItemID();
                        splitQuestionIntoListItemsForAttendee(arrayList, myQuestionAt, itemID2 != null && hashMap.containsKey(itemID2), i3 != myQuestionCount + -1);
                    }
                }
                i3++;
            }
        }
        return arrayList;
    }

    public static List<BaseQAMultiItemEntity> getZoomQuestionsForPanelist(int i, @NonNull HashMap<String, String> hashMap) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (i == ZMQuestionsMode.MODE_OPEN_QUESTIONS.ordinal()) {
            int openQuestionCount = qAComponent.getOpenQuestionCount();
            int i2 = 0;
            while (i2 < openQuestionCount) {
                ZoomQAQuestion openQuestionAt = qAComponent.getOpenQuestionAt(i2);
                if (openQuestionAt != null) {
                    int state = openQuestionAt.getState();
                    if (!(state == 3 || state == 4)) {
                        String itemID = openQuestionAt.getItemID();
                        splitQuestionIntoListItemsForPanelist(arrayList, openQuestionAt, itemID != null && hashMap.containsKey(itemID), i2 != openQuestionCount + -1, false);
                    }
                }
                i2++;
            }
        } else if (i == ZMQuestionsMode.MODE_ANSWERED_QUESTIONS.ordinal()) {
            int answeredQuestionCount = qAComponent.getAnsweredQuestionCount();
            int i3 = 0;
            while (i3 < answeredQuestionCount) {
                ZoomQAQuestion answeredQuestionAt = qAComponent.getAnsweredQuestionAt(i3);
                if (answeredQuestionAt != null) {
                    int state2 = answeredQuestionAt.getState();
                    if (!(state2 == 3 || state2 == 4)) {
                        String itemID2 = answeredQuestionAt.getItemID();
                        splitQuestionIntoListItemsForPanelist(arrayList, answeredQuestionAt, itemID2 != null && hashMap.containsKey(itemID2), i3 != answeredQuestionCount + -1, false);
                    }
                }
                i3++;
            }
        } else if (i == ZMQuestionsMode.MODE_DISMISSED_QUESTIONS.ordinal()) {
            int dismissedQuestionCount = qAComponent.getDismissedQuestionCount();
            int i4 = 0;
            while (i4 < dismissedQuestionCount) {
                ZoomQAQuestion dismissedQuestionAt = qAComponent.getDismissedQuestionAt(i4);
                if (dismissedQuestionAt != null) {
                    int state3 = dismissedQuestionAt.getState();
                    if (!(state3 == 3 || state3 == 4)) {
                        String itemID3 = dismissedQuestionAt.getItemID();
                        splitQuestionIntoListItemsForPanelist(arrayList, dismissedQuestionAt, itemID3 != null && hashMap.containsKey(itemID3), i4 != dismissedQuestionCount + -1, true);
                    }
                }
                i4++;
            }
        }
        return arrayList;
    }

    private static void splitQuestionIntoListItemsForAttendee(@NonNull List<BaseQAMultiItemEntity> list, @NonNull ZoomQAQuestion zoomQAQuestion, boolean z, boolean z2) {
        list.add(new ZMQAQuestionItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion));
        boolean isMarkedAsDismissed = zoomQAQuestion.isMarkedAsDismissed();
        if (isShouldShowLiveAnswerItemForPanelist(zoomQAQuestion)) {
            list.add(new ZMQALiveAnswerItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion));
        }
        int answerCount = zoomQAQuestion.getAnswerCount();
        if (answerCount > 0) {
            boolean z3 = false;
            int i = 0;
            for (int i2 = 0; i2 < answerCount; i2++) {
                ZoomQAAnswer answerAt = zoomQAQuestion.getAnswerAt(i2);
                if (answerAt != null && !answerAt.isLiveAnswer()) {
                    i++;
                    if (i > 2) {
                        if (!z) {
                            z3 = true;
                        } else {
                            z3 = true;
                        }
                    }
                    list.add(new ZMQATextAnswerItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion, i2));
                }
            }
            if (z3 || (isAttendeeCanComment() && !isMarkedAsDismissed)) {
                list.add(new ZMQAAttendeeActionItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion, z3, i));
            }
        } else if (isAttendeeCanComment() && !isMarkedAsDismissed) {
            list.add(new ZMQAAttendeeActionItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion, false, 1));
        }
        if (z2) {
            list.add(new ZMQADividerItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion));
        }
    }

    private static void splitQuestionIntoListItemsForPanelist(@NonNull List<BaseQAMultiItemEntity> list, @NonNull ZoomQAQuestion zoomQAQuestion, boolean z, boolean z2, boolean z3) {
        int i;
        list.add(new ZMQAQuestionItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion));
        if (isShouldShowLiveAnswerItemForPanelist(zoomQAQuestion)) {
            list.add(new ZMQALiveAnswerItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion));
        }
        int answerCount = zoomQAQuestion.getAnswerCount();
        int i2 = 0;
        if (answerCount > 0) {
            int i3 = 0;
            i = 0;
            while (i2 < answerCount) {
                ZoomQAAnswer answerAt = zoomQAQuestion.getAnswerAt(i2);
                if (answerAt != null && !answerAt.isLiveAnswer()) {
                    i++;
                    if (i > 2) {
                        if (!z) {
                            i3 = 1;
                        } else {
                            i3 = 1;
                        }
                    }
                    list.add(new ZMQATextAnswerItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion, i2));
                }
                i2++;
            }
            i2 = i3;
        } else {
            i = 0;
        }
        if (i2 != 0) {
            list.add(new ZMQAPanelistExpandCollapseItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion, i));
        }
        if (isShouldShowLiveAnsweringForPanelist(zoomQAQuestion) && !z3) {
            list.add(new ZMQAWaitingLiveAnswerItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion));
        } else if (!z3) {
            list.add(new ZMQAPanelistActionItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion));
        }
        if (z2) {
            list.add(new ZMQADividerItemEntity(zoomQAQuestion.getItemID(), zoomQAQuestion));
        }
    }

    public static boolean isShouldShowLiveAnsweringForPanelist(@NonNull ZoomQAQuestion zoomQAQuestion) {
        return !zoomQAQuestion.hasLiveAnswers() && zoomQAQuestion.amILiveAnswering();
    }

    public static boolean isShouldShowLiveAnswerItemForPanelist(@NonNull ZoomQAQuestion zoomQAQuestion) {
        if (zoomQAQuestion.hasLiveAnswers()) {
            return true;
        }
        if (zoomQAQuestion.amILiveAnswering() || zoomQAQuestion.getLiveAnsweringCount() <= 0) {
            return zoomQAQuestion.amILiveAnswering() && zoomQAQuestion.getLiveAnsweringCount() > 1;
        }
        return true;
    }

    public static boolean isShouldHideLiveAnswerButtonForPanelist(@NonNull ZoomQAQuestion zoomQAQuestion) {
        return (zoomQAQuestion.hasTextAnswers() && zoomQAQuestion.isMarkedAsAnswered()) || zoomQAQuestion.hasLiveAnswers() || zoomQAQuestion.getLiveAnsweringCount() > 0;
    }

    public static boolean isAttendeeCanUpvote() {
        ConfMgr instance = ConfMgr.getInstance();
        return instance.isAllowAttendeeViewAllQuestion() && instance.isAllowAttendeeUpvoteQuestion();
    }

    public static boolean isAttendeeCanComment() {
        ConfMgr instance = ConfMgr.getInstance();
        return instance.isAllowAttendeeViewAllQuestion() && instance.isAllowAttendeeAnswerQuestion();
    }

    public static boolean isQuestionSent(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return false;
        }
        ZoomQAQuestion questionByID = qAComponent.getQuestionByID(str);
        boolean z = true;
        if (questionByID == null || questionByID.getState() != 1) {
            z = false;
        }
        return z;
    }

    public static boolean isAnswerSent(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return false;
        }
        ZoomQAAnswer answerByID = qAComponent.getAnswerByID(str);
        boolean z = true;
        if (answerByID == null || answerByID.getState() != 1) {
            z = false;
        }
        return z;
    }

    public static int getQustionCount(int i) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return 0;
        }
        if (i == ZMQuestionsMode.MODE_ATTENDEE_ALL_QUESTIONS.ordinal()) {
            return qAComponent.getQuestionCount();
        }
        if (i == ZMQuestionsMode.MODE_ATTENDEE_MY_QUESTIONS.ordinal()) {
            return qAComponent.getMyQuestionCount();
        }
        if (i == ZMQuestionsMode.MODE_OPEN_QUESTIONS.ordinal()) {
            return qAComponent.getOpenQuestionCount();
        }
        if (i == ZMQuestionsMode.MODE_ANSWERED_QUESTIONS.ordinal()) {
            return qAComponent.getAnsweredQuestionCount();
        }
        if (i == ZMQuestionsMode.MODE_DISMISSED_QUESTIONS.ordinal()) {
            return qAComponent.getDismissedQuestionCount();
        }
        return qAComponent.getQuestionCount();
    }

    public static String getLiveAnsweringNames(@NonNull Context context, @NonNull ZoomQAQuestion zoomQAQuestion) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (zoomQAQuestion.amILiveAnswering()) {
            stringBuffer.append(context.getString(C4558R.string.zm_lbl_content_you));
        }
        int liveAnsweringCount = zoomQAQuestion.getLiveAnsweringCount();
        if (liveAnsweringCount > 0) {
            for (int i = 0; i < liveAnsweringCount; i++) {
                String liveAnsweringJIDAt = zoomQAQuestion.getLiveAnsweringJIDAt(i);
                if (!qAComponent.isJIDMyself(liveAnsweringJIDAt)) {
                    String userNameByJID = qAComponent.getUserNameByJID(liveAnsweringJIDAt);
                    if (!StringUtil.isEmptyOrNull(userNameByJID)) {
                        if (stringBuffer.length() > 0) {
                            stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                        }
                        stringBuffer.append(userNameByJID);
                    }
                }
            }
        }
        return stringBuffer.toString();
    }
}
