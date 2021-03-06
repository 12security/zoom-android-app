package com.zipow.videobox.confapp;

public interface CONF_CMD {
    public static final int CMD_ALLOW_MESSAGE_AND_FEEDBACK_NOTIFY = 92;
    public static final int CMD_ALLOW_PARTICIPANT_RENAME = 91;
    public static final int CMD_ALLOW_UNMUTESELF = 88;
    public static final int CMD_AUDIO_MUTEALL_STATUS_CHANGED = 37;
    public static final int CMD_AUDIO_MUTE_ON_ENTRY_STATUS_CHANGED = 38;
    public static final int CMD_AUDIO_QUALITY = 11;
    public static final int CMD_AUDIO_READY = 5;
    public static final int CMD_AUDIO_SHAREING_STATUS_CHANGED = 19;
    public static final int CMD_AUDIO_START = 54;
    public static final int CMD_AUDIO_STOP = 55;
    public static final int CMD_AUTO_SHOW_AUDIO_DEVICE_CHECK_RESULT = 36;
    public static final int CMD_AUTO_SHOW_AUDIO_SELECTION_DLG = 35;
    public static final int CMD_AUTO_START_RECORDING = 40;
    public static final int CMD_CMR_START_TIMEOUT = 45;
    public static final int CMD_CONF_1TO1 = 9;
    public static final int CMD_CONF_ADJUST_SPEAKER_VOLUME = 137;
    public static final int CMD_CONF_ADMIT_ALL_SILENT_USERS_STATUS_CHANGED = 141;
    public static final int CMD_CONF_ALLOW_ASK_QUESTION_ANONYMOUSLY_STATUS_CHANGED = 30;
    public static final int CMD_CONF_ALLOW_ATTENDEE_ANSWER_QUESTION_STATUS_CHANGED = 34;
    public static final int CMD_CONF_ALLOW_ATTENDEE_CHAT_STATUS_CHANGED = 28;
    public static final int CMD_CONF_ALLOW_ATTENDEE_UPVOTE_QUESTION_STATUS_CHANGED = 33;
    public static final int CMD_CONF_ALLOW_ATTENDEE_VIEW_ALL_QUESTION_STATUS_CHANGED = 32;
    public static final int CMD_CONF_ALLOW_MESSAGE_AND_FEEDBACK_NOTIFY_STATUS_CHANGE = 140;
    public static final int CMD_CONF_ALLOW_PANELISTS_VOTE_STATUS_CHANGED = 29;
    public static final int CMD_CONF_ALLOW_PARTICIPANT_RENAME_STATUS_CHANGED = 139;
    public static final int CMD_CONF_ALLOW_RAISE_HAND = 128;
    public static final int CMD_CONF_ALLOW_RAISE_HAND_STATUS_CHANGED = 31;
    public static final int CMD_CONF_ATTENDEE_VIDEO_CONTROL_MODE_CHANGED = 133;
    public static final int CMD_CONF_ATTENDEE_VIDEO_LAYOUT_FLAG_CHANGED = 132;
    public static final int CMD_CONF_ATTENDEE_VIDEO_LAYOUT_MODE_CHANGED = 131;
    public static final int CMD_CONF_AUDIO_COMPANION_MODE_CHANGED = 154;
    public static final int CMD_CONF_AUDIO_FORCE_DISABLE_STATUS_CHANGE = 148;
    public static final int CMD_CONF_BANDWIDTH_LIMIT_STATUS_CHANGED = 149;
    public static final int CMD_CONF_CALL_OUT_STATUS_CHANGED = 105;
    public static final int CMD_CONF_CLOSE = 53;
    public static final int CMD_CONF_CLOSEOTHERMEETING = 69;
    public static final int CMD_CONF_CONNECTION_P2P = 134;
    public static final int CMD_CONF_DISALLOW_RAISE_HAND = 129;
    public static final int CMD_CONF_ENABLE_WAITING_ROOM_ON_ENTRY_CHANGED = 138;
    public static final int CMD_CONF_FAIL = 2;
    public static final int CMD_CONF_FIRSTTIME_FREE_GIFT = 74;
    public static final int CMD_CONF_FREEMEETING_COUNTDOWN = 78;
    public static final int CMD_CONF_FREEMEETING_REMAIN_TIME = 96;
    public static final int CMD_CONF_GET_MEETING_EXT_INFO = 155;
    public static final int CMD_CONF_GET_MEETING_EXT_INFO_STATUS = 156;
    public static final int CMD_CONF_INFO_BARRIER_CHANGED = 161;
    public static final int CMD_CONF_LEAVE = 0;
    public static final int CMD_CONF_LEAVE_COMPLETE = 1;
    public static final int CMD_CONF_LEAVE_WITH_TEL_KEEP = 52;
    public static final int CMD_CONF_LIVESTREAM_START_FAIL = 50;
    public static final int CMD_CONF_LIVESTREAM_START_TIMEOUT = 49;
    public static final int CMD_CONF_LIVESTREAM_STATUS_CHANGED = 47;
    public static final int CMD_CONF_LIVESTREAM_STATUS_CHANGED_INDEX = 48;
    public static final int CMD_CONF_LIVESTREAM_STOP_BY_ACCIDENT = 51;
    public static final int CMD_CONF_LIVE_TRANSCRIPTION_STATUS_CHANGED = 145;
    public static final int CMD_CONF_LOCKSTATUS_CHANGED = 3;
    public static final int CMD_CONF_MEETING_TOPIC_CHANGED = 157;
    public static final int CMD_CONF_MEETING_TOPIC_CHANGE_FAILED = 158;
    public static final int CMD_CONF_MEETING_UPGRADED = 77;
    public static final int CMD_CONF_NEED_ADMIN_PAY_REMIND = 76;
    public static final int CMD_CONF_NETWORK_TYPE_CHANGED = 135;
    public static final int CMD_CONF_NOHOST = 68;
    public static final int CMD_CONF_PAIR_CODE_STATUS = 106;
    public static final int CMD_CONF_PAUSE_CMR = 42;
    public static final int CMD_CONF_PAYREMINDER = 73;
    public static final int CMD_CONF_PBX_AUDIO_STATUS_CHANGED = 160;
    public static final int CMD_CONF_PURE_AUDIO_SHARE_STATUS_CHANGED = 130;
    public static final int CMD_CONF_READY = 8;
    public static final int CMD_CONF_RECONNECT = 10;
    public static final int CMD_CONF_RECORD_ERROR = 70;
    public static final int CMD_CONF_RECORD_NO_AUDIO = 109;
    public static final int CMD_CONF_RECORD_STATUS = 79;
    public static final int CMD_CONF_RECV_VIDEO_PRIVILEGE_CHANGED = 151;
    public static final int CMD_CONF_REMINDER_RECORDING = 80;
    public static final int CMD_CONF_REMOTE_ADMIN_EXIST_STATUS_CHANGED = 159;
    public static final int CMD_CONF_RESUME_CMR = 43;
    public static final int CMD_CONF_SEND_RECV_VIDEO_PRIVILEGE_CHANGED = 152;
    public static final int CMD_CONF_SEND_VIDEO_PRIVILEGE_CHANGED = 150;
    public static final int CMD_CONF_SILENTMODE_CHANGED = 39;
    public static final int CMD_CONF_START_BROADCASTING = 112;
    public static final int CMD_CONF_START_CMR = 41;
    public static final int CMD_CONF_START_LIVE_TRANSCRIPTION = 143;
    public static final int CMD_CONF_START_LIVE_TRANSCRIPTION_FAILED = 146;
    public static final int CMD_CONF_STASTIC_WARNING = 46;
    public static final int CMD_CONF_STOP_CMR = 44;
    public static final int CMD_CONF_STOP_LIVE_TRANSCRIPTION = 144;
    public static final int CMD_CONF_STOP_LIVE_TRANSCRIPTION_FAILED = 147;
    public static final int CMD_CONF_THIRDTIME_PAY_REMIND = 75;
    public static final int CMD_CONF_TO_END_MESSAGE_LOOP = 104;
    public static final int CMD_CONF_VERIFY_HOSTKEY_STATUS = 107;
    public static final int CMD_CONF_VIDEOLIST_SORTED = 18;
    public static final int CMD_CONF_VIDEOSENDINGSTATUS_CHANGED = 17;
    public static final int CMD_CONF_VIDEO_COMPANION_MODE_CHANGED = 153;
    public static final int CMD_CONF_WAITING_ROOM_DATA_READY = 136;
    public static final int CMD_CONF_WEBINAR_ALLOW_ASK_QUESTION_ANONYMOUSLY = 120;
    public static final int CMD_CONF_WEBINAR_ALLOW_ATTENDEE_ANSWER_QUESTION = 126;
    public static final int CMD_CONF_WEBINAR_ALLOW_ATTENDEE_CHAT = 116;
    public static final int CMD_CONF_WEBINAR_ALLOW_ATTENDEE_UPVOTE_QUESTION = 124;
    public static final int CMD_CONF_WEBINAR_ALLOW_ATTENDEE_VIEW_ALL_QUESTION = 122;
    public static final int CMD_CONF_WEBINAR_ALLOW_PANELIST_STARTVIDEO = 113;
    public static final int CMD_CONF_WEBINAR_ALLOW_PANELIST_VOTE = 118;
    public static final int CMD_CONF_WEBINAR_DEPROMOTE_PANELIST = 111;
    public static final int CMD_CONF_WEBINAR_DISALLOW_ASK_QUESTION_ANONYMOUSLY = 121;
    public static final int CMD_CONF_WEBINAR_DISALLOW_ATTENDEE_ANSWER_QUESTION = 127;
    public static final int CMD_CONF_WEBINAR_DISALLOW_ATTENDEE_CHAT = 117;
    public static final int CMD_CONF_WEBINAR_DISALLOW_ATTENDEE_UPVOTE_QUESTION = 125;
    public static final int CMD_CONF_WEBINAR_DISALLOW_ATTENDEE_VIEW_ALL_QUESTION = 123;
    public static final int CMD_CONF_WEBINAR_DISALLOW_PANELIST_STARTVIDEO = 114;
    public static final int CMD_CONF_WEBINAR_DISALLOW_PANELIST_VOTE = 119;
    public static final int CMD_CONF_WEBINAR_EXPEL_ATTENDEE = 115;
    public static final int CMD_CONF_WEBINAR_PROMOTE_PANELIST = 110;
    public static final int CMD_DISALLOW_MESSAGE_AND_FEEDBACK_NOTIFY = 93;
    public static final int CMD_DISALLOW_PARTICIPANT_RENAME = 94;
    public static final int CMD_DISALLOW_UNMUTESELF = 89;
    public static final int CMD_DISALLOW_UNMUTESELF_STATUS_CHANGED = 90;
    public static final int CMD_EDIT_POLL_FOR_MEETING = 71;
    public static final int CMD_ENABLE_HD_VIDEO = 95;
    public static final int CMD_HIDE_ATTENDEE_NUMBER = 85;
    public static final int CMD_KUBI_CONNECTED = 60;
    public static final int CMD_KUBI_DISCONNECTED = 61;
    public static final int CMD_LOCK = 58;
    public static final int CMD_LOCKATTENDEE_ANNO_STATUS_CHANGED = 26;
    public static final int CMD_LOCKSHARE_STATUS_CHANGED = 23;
    public static final int CMD_LOCK_ATTENDEE_ANNO = 86;
    public static final int CMD_LOCK_SHARE = 82;
    public static final int CMD_NO_USER_NAME = 67;
    public static final int CMD_PARINGCODE_RETURN = 101;
    public static final int CMD_RECORD_STATUS_CHANGED = 4;
    public static final int CMD_ROSTER_1TO2_FORHOST = 100;
    public static final int CMD_ROSTER_2TO3_OR_3TO2 = 98;
    public static final int CMD_ROSTER_ATTRIBUTE_CHANGED_FORALL = 103;
    public static final int CMD_ROSTER_ATTRIBUTE_WILL_CHANGE_FORALL = 142;
    public static final int CMD_ROSTER_FIRSTTIME = 99;
    public static final int CMD_ROSTER_ONHOLD_COUNT_CHANGED = 102;
    public static final int CMD_SEND_FEEDBACK_RETURN = 72;
    public static final int CMD_SHARE_AUDIOSHAREMODE_ONOFF = 22;
    public static final int CMD_SHARE_AUTOSTART = 16;
    public static final int CMD_SHARE_PAUSE = 63;
    public static final int CMD_SHARE_QUALITY = 15;
    public static final int CMD_SHARE_READY = 62;
    public static final int CMD_SHARE_RESUME = 64;
    public static final int CMD_SHARE_START = 56;
    public static final int CMD_SHARE_STARTANOTA = 65;
    public static final int CMD_SHARE_STOP = 57;
    public static final int CMD_SHARE_STOPANOTA = 66;
    public static final int CMD_SHOWWEBINARATTENDEE_STATUS_CHANGED = 25;
    public static final int CMD_SHOW_ATTENDEE_NUMBER = 84;
    public static final int CMD_SHOW_PARCTICESESSION_STATUS_CHANGED = 27;
    public static final int CMD_SPEAKER_MUTED_FOR_AUDIO_SHARE = 13;
    public static final int CMD_TEL_SUGGESTED = 12;
    public static final int CMD_UNLOCK = 59;
    public static final int CMD_UNLOCK_ATTENDEE_ANNO = 87;
    public static final int CMD_UNLOCK_SHARE = 83;
    public static final int CMD_USER_JOIN_RING = 81;
    public static final int CMD_USER_NOT_SUPPORT_ANNOTATION_JOINED = 24;
    public static final int CMD_VIDEO_AUTOSTART = 7;
    public static final int CMD_VIDEO_LEADERSHIPMODE_ONOFF = 21;
    public static final int CMD_VIDEO_LEADERSHIPMODE_STATUS_CHANGED = 20;
    public static final int CMD_VIDEO_QUALITY = 14;
    public static final int CMD_VIDEO_READY = 6;
    public static final int CMD_VIEW_ONLY_TELEPHONY_USERCOUNT_CHANGED = 108;
    public static final int CMD_VIEW_ONLY_USERCOUNT_CHANGED = 97;
}
