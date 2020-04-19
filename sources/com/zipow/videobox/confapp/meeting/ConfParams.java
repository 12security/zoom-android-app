package com.zipow.videobox.confapp.meeting;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.ConfLocalHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import p021us.zoom.androidlib.util.ParamsList;
import p021us.zoom.androidlib.util.StringUtil;

public class ConfParams {
    public static final String CONF_PARAM_CLOSE_ON_LEAVE_MEETING = "close_on_leave_meeting";
    public static final String CONF_PARAM_CUSTOM_MEETING_ID = "custom_meeting_id";
    public static final String CONF_PARAM_INVITE_OPTIONS = "invite_options";
    public static final String CONF_PARAM_MEETING_VIEWS_OPTIONS = "meeting_views_options";
    public static final String CONF_PARAM_NO_BOTTOM_TOOLBAR = "no_bottom_toolbar";
    public static final String CONF_PARAM_NO_DIAL_IN = "no_dial_in_via_phone";
    public static final String CONF_PARAM_NO_DIAL_OUT = "no_dial_out_to_phone";
    public static final String CONF_PARAM_NO_DISCONNECT_AUDIO = "no_disconnect_audio";
    public static final String CONF_PARAM_NO_DRIVING_MODE = "no_driving_mode";
    public static final String CONF_PARAM_NO_INVITE = "no_invite";
    public static final String CONF_PARAM_NO_MEETING_END_MESSAGE = "no_meeting_end_message";
    public static final String CONF_PARAM_NO_MEETING_ERROR_MESSAGE = "no_meeting_error_message";
    public static final String CONF_PARAM_NO_SHARE = "no_share";
    public static final String CONF_PARAM_NO_TITLEBAR = "no_titlebar";
    public static final String CONF_PARAM_SHOW_WATER_MARK = "show_water_mark";
    public static int InfoBarrierFieldChat = 2;
    public static int InfoBarrierFieldFile = 4;
    public static int InfoBarrierFieldNone = 0;
    public static int InfoBarrierFieldShare = 1;
    private static final String TAG = "com.zipow.videobox.confapp.meeting.ConfParams";
    @Nullable
    private String mCustomMeetingId = null;
    private int mInviteOptions = 255;
    private int mMeetingViewsOptions = 0;
    private boolean mbCloseOnLeaveMeeting = false;
    private boolean mbNoBottomToolbar = false;
    private boolean mbNoDialIn = false;
    private boolean mbNoDialOut = false;
    private boolean mbNoDisconnectAudio = false;
    private boolean mbNoDrivingMode = false;
    private boolean mbNoInvite = false;
    private boolean mbNoMeetingEndMsg = false;
    private boolean mbNoMeetingErrorMsg = false;
    private boolean mbNoShare = false;
    private boolean mbNoTitlebar = false;
    private boolean mbShowSdkWaterMark = false;

    public void parseFromParamsList(@NonNull ParamsList paramsList) {
        this.mbCloseOnLeaveMeeting = paramsList.getBoolean(CONF_PARAM_CLOSE_ON_LEAVE_MEETING, this.mbCloseOnLeaveMeeting);
        this.mbNoDrivingMode = paramsList.getBoolean(CONF_PARAM_NO_DRIVING_MODE, this.mbNoDrivingMode);
        this.mbNoInvite = paramsList.getBoolean(CONF_PARAM_NO_INVITE, this.mbNoInvite);
        this.mbNoMeetingEndMsg = paramsList.getBoolean(CONF_PARAM_NO_MEETING_END_MESSAGE, this.mbNoMeetingEndMsg);
        this.mbNoTitlebar = paramsList.getBoolean(CONF_PARAM_NO_TITLEBAR, this.mbNoTitlebar);
        this.mbNoBottomToolbar = paramsList.getBoolean(CONF_PARAM_NO_BOTTOM_TOOLBAR, this.mbNoBottomToolbar);
        this.mbNoDialIn = paramsList.getBoolean(CONF_PARAM_NO_DIAL_IN, this.mbNoDialIn);
        this.mbNoDialOut = paramsList.getBoolean(CONF_PARAM_NO_DIAL_OUT, this.mbNoDialOut);
        this.mbNoDisconnectAudio = paramsList.getBoolean(CONF_PARAM_NO_DISCONNECT_AUDIO, this.mbNoDisconnectAudio);
        this.mbNoShare = paramsList.getBoolean(CONF_PARAM_NO_SHARE, this.mbNoShare);
        this.mbNoMeetingErrorMsg = paramsList.getBoolean(CONF_PARAM_NO_MEETING_ERROR_MESSAGE, this.mbNoMeetingErrorMsg);
        this.mMeetingViewsOptions = paramsList.getInt(CONF_PARAM_MEETING_VIEWS_OPTIONS, this.mMeetingViewsOptions);
        this.mInviteOptions = paramsList.getInt(CONF_PARAM_INVITE_OPTIONS, this.mInviteOptions);
        this.mCustomMeetingId = paramsList.getString(CONF_PARAM_CUSTOM_MEETING_ID, this.mCustomMeetingId);
        this.mbShowSdkWaterMark = paramsList.getBoolean(CONF_PARAM_SHOW_WATER_MARK, this.mbShowSdkWaterMark);
    }

    public void parseFromUri(@NonNull Uri uri) {
        Uri uri2 = uri;
        String queryParameter = uri2.getQueryParameter(CONF_PARAM_NO_DRIVING_MODE);
        String queryParameter2 = uri2.getQueryParameter(CONF_PARAM_CLOSE_ON_LEAVE_MEETING);
        String queryParameter3 = uri2.getQueryParameter(CONF_PARAM_NO_INVITE);
        String queryParameter4 = uri2.getQueryParameter(CONF_PARAM_NO_MEETING_END_MESSAGE);
        String queryParameter5 = uri2.getQueryParameter(CONF_PARAM_NO_TITLEBAR);
        String queryParameter6 = uri2.getQueryParameter(CONF_PARAM_NO_BOTTOM_TOOLBAR);
        String queryParameter7 = uri2.getQueryParameter(CONF_PARAM_NO_DIAL_IN);
        String queryParameter8 = uri2.getQueryParameter(CONF_PARAM_NO_DIAL_OUT);
        String queryParameter9 = uri2.getQueryParameter(CONF_PARAM_NO_DISCONNECT_AUDIO);
        String queryParameter10 = uri2.getQueryParameter(CONF_PARAM_NO_SHARE);
        String queryParameter11 = uri2.getQueryParameter(CONF_PARAM_MEETING_VIEWS_OPTIONS);
        String queryParameter12 = uri2.getQueryParameter(CONF_PARAM_INVITE_OPTIONS);
        String queryParameter13 = uri2.getQueryParameter(CONF_PARAM_NO_MEETING_ERROR_MESSAGE);
        String queryParameter14 = uri2.getQueryParameter(CONF_PARAM_CUSTOM_MEETING_ID);
        String str = queryParameter12;
        String queryParameter15 = uri2.getQueryParameter(CONF_PARAM_SHOW_WATER_MARK);
        this.mbNoDrivingMode = "1".equals(queryParameter);
        this.mbCloseOnLeaveMeeting = "1".equals(queryParameter2);
        this.mbNoInvite = "1".equals(queryParameter3);
        this.mbNoMeetingEndMsg = "1".equals(queryParameter4);
        this.mbNoTitlebar = "1".equals(queryParameter5);
        this.mbNoBottomToolbar = "1".equals(queryParameter6);
        this.mbNoDialIn = "1".equals(queryParameter7);
        this.mbNoDialOut = "1".equals(queryParameter8);
        this.mbNoDisconnectAudio = "1".equals(queryParameter9);
        this.mbNoShare = "1".equals(queryParameter10);
        this.mbNoMeetingErrorMsg = "1".equals(queryParameter13);
        this.mbShowSdkWaterMark = "1".equals(queryParameter15);
        if (!StringUtil.isEmptyOrNull(queryParameter14)) {
            try {
                this.mCustomMeetingId = URLDecoder.decode(queryParameter14, "UTF-8");
            } catch (UnsupportedEncodingException unused) {
            }
        }
        if (!StringUtil.isEmptyOrNull(queryParameter11)) {
            try {
                this.mMeetingViewsOptions = Integer.parseInt(queryParameter11);
            } catch (NumberFormatException unused2) {
            }
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            try {
                this.mInviteOptions = Integer.parseInt(str);
            } catch (NumberFormatException unused3) {
            }
        }
    }

    public void saveParamList(@NonNull ParamsList paramsList) {
        paramsList.putBoolean(CONF_PARAM_NO_DRIVING_MODE, this.mbNoDrivingMode);
        paramsList.putBoolean(CONF_PARAM_CLOSE_ON_LEAVE_MEETING, this.mbCloseOnLeaveMeeting);
        paramsList.putBoolean(CONF_PARAM_NO_INVITE, this.mbNoInvite);
        paramsList.putBoolean(CONF_PARAM_NO_MEETING_END_MESSAGE, this.mbNoMeetingEndMsg);
        paramsList.putBoolean(CONF_PARAM_NO_TITLEBAR, this.mbNoTitlebar);
        paramsList.putBoolean(CONF_PARAM_NO_BOTTOM_TOOLBAR, this.mbNoBottomToolbar);
        paramsList.putBoolean(CONF_PARAM_NO_DIAL_IN, this.mbNoDialIn);
        paramsList.putBoolean(CONF_PARAM_NO_DIAL_OUT, this.mbNoDialOut);
        paramsList.putBoolean(CONF_PARAM_NO_DISCONNECT_AUDIO, this.mbNoDisconnectAudio);
        paramsList.putBoolean(CONF_PARAM_NO_SHARE, this.mbNoShare);
        paramsList.putBoolean(CONF_PARAM_NO_MEETING_ERROR_MESSAGE, this.mbNoMeetingErrorMsg);
        paramsList.putInt(CONF_PARAM_MEETING_VIEWS_OPTIONS, this.mMeetingViewsOptions);
        paramsList.putInt(CONF_PARAM_INVITE_OPTIONS, this.mInviteOptions);
        paramsList.putString(CONF_PARAM_CUSTOM_MEETING_ID, this.mCustomMeetingId);
        paramsList.putBoolean(CONF_PARAM_SHOW_WATER_MARK, this.mbShowSdkWaterMark);
    }

    public boolean isMbCloseOnLeaveMeeting() {
        return this.mbCloseOnLeaveMeeting;
    }

    public boolean isMbNoDrivingMode() {
        return this.mbNoDrivingMode;
    }

    public boolean isMbNoInvite() {
        return this.mbNoInvite;
    }

    public boolean isMbNoMeetingEndMsg() {
        return this.mbNoMeetingEndMsg;
    }

    public boolean isMbNoTitlebar() {
        return this.mbNoTitlebar;
    }

    public boolean isMbNoBottomToolbar() {
        return this.mbNoBottomToolbar;
    }

    public boolean isMbNoDialIn() {
        return this.mbNoDialIn;
    }

    public boolean isMbNoDialOut() {
        return this.mbNoDialOut;
    }

    public boolean isMbNoDisconnectAudio() {
        return this.mbNoDisconnectAudio;
    }

    public boolean isMbNoShare() {
        return this.mbNoShare;
    }

    public boolean isMbNoMeetingErrorMsg() {
        return this.mbNoMeetingErrorMsg;
    }

    public int getmMeetingViewsOptions() {
        return this.mMeetingViewsOptions;
    }

    public int getmInviteOptions() {
        return this.mInviteOptions;
    }

    @Nullable
    public String getmCustomMeetingId() {
        return this.mCustomMeetingId;
    }

    public boolean isMbShowSdkWaterMark() {
        return this.mbShowSdkWaterMark;
    }

    public int getInviteOptions() {
        return this.mInviteOptions;
    }

    @Nullable
    public String getCustomMeetingId() {
        return this.mCustomMeetingId;
    }

    public boolean isDriverModeDisabled() {
        return this.mbNoDrivingMode;
    }

    public boolean isInviteDisabled() {
        return this.mbNoInvite;
    }

    public boolean isTitleBarDisabled() {
        return this.mbNoTitlebar;
    }

    public boolean isBottomBarDisabled() {
        return this.mbNoBottomToolbar;
    }

    public boolean isDialInDisabled() {
        return this.mbNoDialIn;
    }

    public boolean isDialOutDisabled() {
        return this.mbNoDialOut;
    }

    public boolean isDisconnectAudioDisabled() {
        return this.mbNoDisconnectAudio;
    }

    public boolean isShowSdkWaterMark() {
        return this.mbShowSdkWaterMark;
    }

    public boolean isShareButtonDisabled() {
        return (this.mMeetingViewsOptions & 4) != 0 || this.mbNoShare;
    }

    public boolean isAudioButtonDisabled() {
        return (this.mMeetingViewsOptions & 2) != 0 || !ConfLocalHelper.hasAudioSourceToConnect();
    }

    public boolean isVideoButtonDisabled() {
        return (this.mMeetingViewsOptions & 1) != 0;
    }

    public boolean isPlistButtonDisabled() {
        return (this.mMeetingViewsOptions & 8) != 0;
    }

    public boolean isMoreButtonDisabled() {
        return (this.mMeetingViewsOptions & 16) != 0;
    }

    public boolean isLeaveButtonDisabled() {
        return (this.mMeetingViewsOptions & 128) != 0;
    }

    public boolean isMeetingIdTextDisabled() {
        return (this.mMeetingViewsOptions & 32) != 0;
    }

    public boolean isPasswordTextDisabled() {
        return (this.mMeetingViewsOptions & 64) != 0;
    }

    public boolean isSwitchCameraButtonDisabled() {
        return (this.mMeetingViewsOptions & 256) != 0;
    }

    public boolean isSwitchAudioSourceButtonDisabled() {
        return (this.mMeetingViewsOptions & 512) != 0;
    }
}
