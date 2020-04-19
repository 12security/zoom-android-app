package com.zipow.videobox.common.conf;

public class MyBandwidthLimitInfo {
    private boolean disableReceiveVideo;
    private boolean disableSendVideo;
    private int limitDown;
    private int limitUp;

    public int getLimitUp() {
        return this.limitUp;
    }

    public void setLimitUp(int i) {
        this.limitUp = i;
    }

    public int getLimitDown() {
        return this.limitDown;
    }

    public void setLimitDown(int i) {
        this.limitDown = i;
    }

    public boolean isDisableSendVideo() {
        return this.disableSendVideo;
    }

    public void setDisableSendVideo(boolean z) {
        this.disableSendVideo = z;
    }

    public boolean isDisableReceiveVideo() {
        return this.disableReceiveVideo;
    }

    public void setDisableReceiveVideo(boolean z) {
        this.disableReceiveVideo = z;
    }
}
