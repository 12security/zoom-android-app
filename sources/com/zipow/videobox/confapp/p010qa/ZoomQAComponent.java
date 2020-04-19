package com.zipow.videobox.confapp.p010qa;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.confapp.qa.ZoomQAComponent */
public class ZoomQAComponent {
    private long mNativeHandle = 0;

    @Nullable
    private native String addAnswerImpl(long j, String str, String str2, String str3);

    @Nullable
    private native String addQuestionImpl(long j, String str, String str2, boolean z);

    private native boolean dismissQuestionImpl(long j, String str);

    private native boolean endComposingImpl(long j, String str);

    private native boolean endLivingImpl(long j, String str);

    private native long getAnswerByIDImpl(long j, String str);

    private native long getAnsweredQuestionAtImpl(long j, int i);

    private native int getAnsweredQuestionCountImpl(long j);

    private native long getBuddyAtImpl(long j, int i);

    private native long getBuddyByIDImpl(long j, String str);

    private native long getBuddyByNodeIDImpl(long j, long j2);

    private native int getBuddyCountImpl(long j);

    @Nullable
    private native long[] getBuddyListByNameFilterImpl(long j, String str);

    private native long getDismissedQuestionAtImpl(long j, int i);

    private native int getDismissedQuestionCountImpl(long j);

    @Nullable
    private native String getMyJIDImpl(long j);

    private native long getMyQuestionAtImpl(long j, int i);

    private native int getMyQuestionCountImpl(long j);

    private native long getOpenQuestionAtImpl(long j, int i);

    private native int getOpenQuestionCountImpl(long j);

    private native long getQuestionAtImpl(long j, int i);

    private native long getQuestionByIDImpl(long j, String str);

    private native int getQuestionCountImpl(long j);

    @Nullable
    private native String getUserJIDByNodeIDImpl(long j, long j2);

    @Nullable
    private native String getUserNameByJIDImpl(long j, String str);

    private native long getUserNodeIDByJIDImpl(long j, String str);

    private native boolean isConnectedImpl(long j);

    private native boolean isJIDMyselfImpl(long j, String str);

    private native boolean isStreamConflictImpl(long j);

    private native boolean isWebinarAttendeeImpl(long j);

    private native boolean isWebinarHostImpl(long j);

    private native boolean isWebinarPanelistImpl(long j);

    private native boolean markQuestionAsAnsweredImpl(long j, String str);

    private native boolean reopenQuestionImpl(long j, String str);

    private native boolean resendMessageImpl(long j, String str);

    private native boolean revokeUpvoteQuestionImpl(long j, String str);

    private native void setZoomQAUIImpl(long j, long j2);

    private native boolean startComposingImpl(long j, String str);

    private native boolean startLivingImpl(long j, String str);

    private native boolean upvoteQuestionImpl(long j, String str);

    public ZoomQAComponent(long j) {
        this.mNativeHandle = j;
    }

    public void setZoomQAUI(@Nullable ZoomQAUI zoomQAUI) {
        long j = 0;
        if (this.mNativeHandle != 0) {
            if (zoomQAUI != null) {
                j = zoomQAUI.getNativeHandle();
            }
            setZoomQAUIImpl(this.mNativeHandle, j);
        }
    }

    @Nullable
    public String getMyJID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getMyJIDImpl(j);
    }

    public int getQuestionCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getQuestionCountImpl(j);
    }

    @Nullable
    public ZoomQAQuestion getQuestionAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long questionAtImpl = getQuestionAtImpl(j, i);
        if (questionAtImpl == 0) {
            return null;
        }
        return new ZoomQAQuestion(questionAtImpl);
    }

    public int getMyQuestionCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMyQuestionCountImpl(j);
    }

    @Nullable
    public ZoomQAQuestion getMyQuestionAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long myQuestionAtImpl = getMyQuestionAtImpl(j, i);
        if (myQuestionAtImpl == 0) {
            return null;
        }
        return new ZoomQAQuestion(myQuestionAtImpl);
    }

    @Nullable
    public ZoomQAQuestion getQuestionByID(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return null;
        }
        long questionByIDImpl = getQuestionByIDImpl(j, str);
        if (questionByIDImpl == 0) {
            return null;
        }
        return new ZoomQAQuestion(questionByIDImpl);
    }

    @Nullable
    public ZoomQAAnswer getAnswerByID(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return null;
        }
        long answerByIDImpl = getAnswerByIDImpl(j, str);
        if (answerByIDImpl == 0) {
            return null;
        }
        return new ZoomQAAnswer(answerByIDImpl);
    }

    @Nullable
    public String addQuestion(@Nullable String str, @Nullable String str2, boolean z) {
        if (this.mNativeHandle == 0 || str == null) {
            return null;
        }
        String addQuestionImpl = addQuestionImpl(this.mNativeHandle, str, str2 == null ? "" : str2, z);
        if (StringUtil.isEmptyOrNull(addQuestionImpl)) {
            return null;
        }
        return addQuestionImpl;
    }

    @Nullable
    public String addAnswer(@Nullable String str, @Nullable String str2, @Nullable String str3) {
        if (this.mNativeHandle == 0 || str == null || str2 == null) {
            return null;
        }
        String addAnswerImpl = addAnswerImpl(this.mNativeHandle, str, str2, str3 == null ? "" : str3);
        if (StringUtil.isEmptyOrNull(addAnswerImpl)) {
            return null;
        }
        return addAnswerImpl;
    }

    public boolean resendMessage(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return false;
        }
        return resendMessageImpl(j, str);
    }

    public boolean markQuestionAsAnswered(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return false;
        }
        return markQuestionAsAnsweredImpl(j, str);
    }

    public boolean startComposing(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return false;
        }
        return startComposingImpl(j, str);
    }

    public boolean endComposing(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return false;
        }
        return endComposingImpl(j, str);
    }

    public boolean startLiving(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return false;
        }
        return startLivingImpl(j, str);
    }

    public boolean endLiving(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return false;
        }
        return endLivingImpl(j, str);
    }

    public boolean isWebinarAttendee() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isWebinarAttendeeImpl(j);
    }

    public boolean isWebinarPanelist() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isWebinarPanelistImpl(j);
    }

    public boolean isWebinarHost() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isWebinarHostImpl(j);
    }

    public int getOpenQuestionCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getOpenQuestionCountImpl(j);
    }

    @Nullable
    public ZoomQAQuestion getOpenQuestionAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long openQuestionAtImpl = getOpenQuestionAtImpl(j, i);
        if (openQuestionAtImpl == 0) {
            return null;
        }
        return new ZoomQAQuestion(openQuestionAtImpl);
    }

    public int getAnsweredQuestionCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getAnsweredQuestionCountImpl(j);
    }

    @Nullable
    public ZoomQAQuestion getAnsweredQuestionAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long answeredQuestionAtImpl = getAnsweredQuestionAtImpl(j, i);
        if (answeredQuestionAtImpl == 0) {
            return null;
        }
        return new ZoomQAQuestion(answeredQuestionAtImpl);
    }

    @Nullable
    public String getUserNameByJID(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return null;
        }
        return getUserNameByJIDImpl(j, str);
    }

    public long getUserNodeIDByJID(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return 0;
        }
        return getUserNodeIDByJIDImpl(j, str);
    }

    @Nullable
    public String getUserJIDByNodeID(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return null;
        }
        return getUserJIDByNodeIDImpl(j2, j);
    }

    public boolean isJIDMyself(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return false;
        }
        return isJIDMyselfImpl(j, str);
    }

    public boolean isStreamConflict() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isStreamConflictImpl(j);
    }

    public int getBuddyCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getBuddyCountImpl(j);
    }

    @Nullable
    public ZoomQABuddy getBuddyAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyAtImpl = getBuddyAtImpl(j, i);
        if (buddyAtImpl == 0) {
            return null;
        }
        return new ZoomQABuddy(buddyAtImpl);
    }

    @Nullable
    public ZoomQABuddy getBuddyByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyByIDImpl = getBuddyByIDImpl(j, str);
        if (buddyByIDImpl == 0) {
            return null;
        }
        return new ZoomQABuddy(buddyByIDImpl);
    }

    @Nullable
    public ZoomQABuddy getBuddyByNodeID(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return null;
        }
        long buddyByNodeIDImpl = getBuddyByNodeIDImpl(j2, j);
        if (buddyByNodeIDImpl == 0) {
            return null;
        }
        return new ZoomQABuddy(buddyByNodeIDImpl);
    }

    public boolean isConnected() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isConnectedImpl(j);
    }

    @Nullable
    public List<ZoomQABuddy> getBuddyListByNameFilter(@Nullable String str) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        if (str == null) {
            str = "";
        }
        long[] buddyListByNameFilterImpl = getBuddyListByNameFilterImpl(this.mNativeHandle, str);
        if (buddyListByNameFilterImpl == null) {
            return null;
        }
        if (r0 == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (long j : buddyListByNameFilterImpl) {
            if (j != 0) {
                arrayList.add(new ZoomQABuddy(j));
            }
        }
        return arrayList;
    }

    public boolean reopenQuestion(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return reopenQuestionImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean upvoteQuestion(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return upvoteQuestionImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean revokeUpvoteQuestion(String str) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return revokeUpvoteQuestionImpl(this.mNativeHandle, str);
        }
        return false;
    }

    public boolean dismissQuestion(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return false;
        }
        return dismissQuestionImpl(j, str);
    }

    public int getDismissedQuestionCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getDismissedQuestionCountImpl(j);
    }

    @Nullable
    public ZoomQAQuestion getDismissedQuestionAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long dismissedQuestionAtImpl = getDismissedQuestionAtImpl(j, i);
        if (dismissedQuestionAtImpl == 0) {
            return null;
        }
        return new ZoomQAQuestion(dismissedQuestionAtImpl);
    }
}
