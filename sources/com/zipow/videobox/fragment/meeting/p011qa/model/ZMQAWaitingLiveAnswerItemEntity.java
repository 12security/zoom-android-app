package com.zipow.videobox.fragment.meeting.p011qa.model;

import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.model.ZMQAWaitingLiveAnswerItemEntity */
public class ZMQAWaitingLiveAnswerItemEntity extends BaseQAMultiItemEntity {
    public ZMQAWaitingLiveAnswerItemEntity(String str, ZoomQAQuestion zoomQAQuestion) {
        super(str, zoomQAQuestion);
        this.mType = 8;
    }
}
