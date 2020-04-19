package com.zipow.videobox.confapp.meeting.confhelper;

import com.zipow.videobox.view.ConfChatAttendeeItem;

public class ConfDataHelper {
    private ConfChatAttendeeItem mConfChatAttendeeItem;
    private boolean mEnableAdmitAll = true;
    private boolean mIsAutoCalledOrCanceledCall;
    private boolean mIsNeedHandleCallOutStatusChangedInMeeting;
    private boolean mIsShowMyVideoInGalleryView = true;
    private boolean mIsVideoStarted = false;
    private boolean mVideoOnBeforeShare = false;

    public boolean ismIsAutoCalledOrCanceledCall() {
        return this.mIsAutoCalledOrCanceledCall;
    }

    public void setmIsAutoCalledOrCanceledCall(boolean z) {
        this.mIsAutoCalledOrCanceledCall = z;
    }

    public boolean ismIsNeedHandleCallOutStatusChangedInMeeting() {
        return this.mIsNeedHandleCallOutStatusChangedInMeeting;
    }

    public void setmIsNeedHandleCallOutStatusChangedInMeeting(boolean z) {
        this.mIsNeedHandleCallOutStatusChangedInMeeting = z;
    }

    public boolean ismIsShowMyVideoInGalleryView() {
        return this.mIsShowMyVideoInGalleryView;
    }

    public void setmIsShowMyVideoInGalleryView(boolean z) {
        this.mIsShowMyVideoInGalleryView = z;
    }

    public ConfChatAttendeeItem getmConfChatAttendeeItem() {
        return this.mConfChatAttendeeItem;
    }

    public void setmConfChatAttendeeItem(ConfChatAttendeeItem confChatAttendeeItem) {
        this.mConfChatAttendeeItem = confChatAttendeeItem;
    }

    public boolean ismEnableAdmitAll() {
        return this.mEnableAdmitAll;
    }

    public void setmEnableAdmitAll(boolean z) {
        this.mEnableAdmitAll = z;
    }

    public boolean getIsVideoOnBeforeShare() {
        return this.mVideoOnBeforeShare;
    }

    public void setIsVideoOnBeforeShare(boolean z) {
        this.mVideoOnBeforeShare = z;
    }

    public boolean ismIsVideoStarted() {
        return this.mIsVideoStarted;
    }

    public void setmIsVideoStarted(boolean z) {
        this.mIsVideoStarted = z;
    }
}
