package com.zipow.videobox.fragment.meeting.p011qa.model;

import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.model.ZMQATextAnswerItemEntity */
public class ZMQATextAnswerItemEntity extends BaseQAMultiItemEntity {
    private int index;

    public ZMQATextAnswerItemEntity(String str, ZoomQAQuestion zoomQAQuestion, int i) {
        super(str, zoomQAQuestion);
        this.mType = 3;
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        if (this.index != ((ZMQATextAnswerItemEntity) obj).index) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + this.index;
    }
}
