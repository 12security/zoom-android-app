package com.zipow.videobox.view.video;

import com.zipow.videobox.util.PreferenceUtil;

public class VideoLayoutHelper {
    private static VideoLayoutHelper instance;

    public static synchronized VideoLayoutHelper getInstance() {
        VideoLayoutHelper videoLayoutHelper;
        synchronized (VideoLayoutHelper.class) {
            if (instance == null) {
                instance = new VideoLayoutHelper();
            }
            videoLayoutHelper = instance;
        }
        return videoLayoutHelper;
    }

    private VideoLayoutHelper() {
    }

    public boolean isHideNoVideoUsersEnabled() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.HIDE_NO_VIDEO_USERS, false);
    }

    public void setHideNoVideoUsersEnabled(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.HIDE_NO_VIDEO_USERS, z);
    }

    public boolean isSwitchVideoLayoutAccordingToUserCountEnabled() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.SWITCH_VIDEO_LAYOUT_ACCORDING_TO_USER_COUNT, false);
    }

    public void setSwitchVideoLayoutAccordingToUserCountEnabled(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SWITCH_VIDEO_LAYOUT_ACCORDING_TO_USER_COUNT, z);
    }

    public int getSwitchVideoLayoutUserCountThreshold() {
        return PreferenceUtil.readIntValue(PreferenceUtil.SWITCH_VIDEO_LAYOUT_USER_COUNT_THRESHOLD, 3);
    }

    public void setSwitchVideoLayoutUserCountThreshold(int i) {
        if (i >= 2) {
            PreferenceUtil.saveIntValue(PreferenceUtil.SWITCH_VIDEO_LAYOUT_USER_COUNT_THRESHOLD, i);
        }
    }

    public void setGalleryViewCapacity(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > 25) {
            i = 25;
        }
        PreferenceUtil.saveIntValue(PreferenceUtil.GALLERY_VIEW_CAPACITY, i);
    }

    public int getGalleryViewCapacity() {
        int readIntValue = PreferenceUtil.readIntValue(PreferenceUtil.GALLERY_VIEW_CAPACITY, 0);
        if (readIntValue < 0) {
            readIntValue = 0;
        }
        if (readIntValue > 25) {
            return 25;
        }
        return readIntValue;
    }
}
