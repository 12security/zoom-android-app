package com.zipow.videobox.fragment.meeting.p011qa.model;

import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.model.ZMQAPanelistExpandCollapseItemEntity */
public class ZMQAPanelistExpandCollapseItemEntity extends BaseQAMultiItemEntity {
    private int mTxtAnswerCount;

    public ZMQAPanelistExpandCollapseItemEntity(String str, ZoomQAQuestion zoomQAQuestion, int i) {
        super(str, zoomQAQuestion);
        this.mType = 7;
        this.mTxtAnswerCount = i;
    }

    public int getmTxtAnswerCount() {
        return this.mTxtAnswerCount;
    }
}
