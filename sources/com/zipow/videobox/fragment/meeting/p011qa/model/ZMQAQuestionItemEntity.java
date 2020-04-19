package com.zipow.videobox.fragment.meeting.p011qa.model;

import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.model.ZMQAQuestionItemEntity */
public class ZMQAQuestionItemEntity extends BaseQAMultiItemEntity {
    public ZMQAQuestionItemEntity(String str, ZoomQAQuestion zoomQAQuestion) {
        super(str, zoomQAQuestion);
        this.mType = 1;
    }
}
