package com.zipow.videobox.fragment.meeting.p011qa.model;

import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.model.ZMQAAttendeeActionItemEntity */
public class ZMQAAttendeeActionItemEntity extends BaseQAMultiItemEntity {
    private boolean mIsShowFeedback;
    private int mTxtAnswerCount;

    public ZMQAAttendeeActionItemEntity(String str, ZoomQAQuestion zoomQAQuestion, boolean z, int i) {
        super(str, zoomQAQuestion);
        this.mType = 4;
        this.mIsShowFeedback = z;
        this.mTxtAnswerCount = i;
    }

    public boolean ismIsShowFeedback() {
        return this.mIsShowFeedback;
    }

    public int getmTxtAnswerCount() {
        return this.mTxtAnswerCount;
    }
}
