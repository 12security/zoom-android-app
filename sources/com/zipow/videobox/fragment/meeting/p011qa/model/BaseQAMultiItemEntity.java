package com.zipow.videobox.fragment.meeting.p011qa.model;

import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.p010qa.ZoomQAQuestion;
import p021us.zoom.androidlib.widget.recyclerview.ZMMultiItemEntity;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.model.BaseQAMultiItemEntity */
public abstract class BaseQAMultiItemEntity implements ZMMultiItemEntity {
    private String mItemId;
    protected int mType;
    private ZoomQAQuestion question;

    public BaseQAMultiItemEntity(String str, ZoomQAQuestion zoomQAQuestion) {
        this.mItemId = str;
        this.question = zoomQAQuestion;
    }

    public String getmItemId() {
        return this.mItemId;
    }

    public ZoomQAQuestion getQuestion() {
        return this.question;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BaseQAMultiItemEntity baseQAMultiItemEntity = (BaseQAMultiItemEntity) obj;
        if (this.mType != baseQAMultiItemEntity.mType) {
            return false;
        }
        String str = this.mItemId;
        if (str != null) {
            z = str.equals(baseQAMultiItemEntity.mItemId);
        } else if (baseQAMultiItemEntity.mItemId != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.mItemId;
        return ((str != null ? str.hashCode() : 0) * 31) + this.mType;
    }

    public int getItemType() {
        return this.mType;
    }
}
