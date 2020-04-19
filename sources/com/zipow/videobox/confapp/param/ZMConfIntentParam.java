package com.zipow.videobox.confapp.param;

import com.zipow.videobox.ConfActivity;

public class ZMConfIntentParam {
    public static final String ACTION_ACCEPT_CALL;
    public static final String ACTION_CALL_MY_PHONE = ".intent.action.CallMyPhone";
    public static final String ACTION_JOIN_BY_ID;
    public static final String ACTION_JOIN_BY_URL;
    public static final String ACTION_MEETING_JBH = ".intent.action.JoinBeforeHost";
    public static final String ACTION_NEW_INCOMING_CALL;
    public static final String ACTION_PT_ASK_TO_LEAVE;
    public static final String ACTION_RETURN_TO_CONF;
    public static final String ACTION_START_CONFERENCE;
    public static final String ACTION_SWITCH_CALL;
    public static final String ARG_CONF_NUMBER = "confno";
    public static final String ARG_INVITATION = "invitation";
    public static final String ARG_IS_START = "isStart";
    public static final String ARG_LEAVE_REASON = "leaveReason";
    public static final String ARG_SCREEN_NAME = "screenName";
    public static final String ARG_URL_ACTION = "urlAction";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(ConfActivity.class.getName());
        sb.append(".action.JOIN_BY_ID");
        ACTION_JOIN_BY_ID = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(ConfActivity.class.getName());
        sb2.append(".action.JOIN_BY_URL");
        ACTION_JOIN_BY_URL = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(ConfActivity.class.getName());
        sb3.append(".action.START_CONFERENCE");
        ACTION_START_CONFERENCE = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(ConfActivity.class.getName());
        sb4.append("action.ACCEPT_CALL");
        ACTION_ACCEPT_CALL = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(ConfActivity.class.getName());
        sb5.append(".action.RETURN_TO_CONF");
        ACTION_RETURN_TO_CONF = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(ConfActivity.class.getName());
        sb6.append(".action.SWITCH_CALL");
        ACTION_SWITCH_CALL = sb6.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(ConfActivity.class.getName());
        sb7.append(".action.ACTION_NEW_INCOMING_CALL");
        ACTION_NEW_INCOMING_CALL = sb7.toString();
        StringBuilder sb8 = new StringBuilder();
        sb8.append(ConfActivity.class.getName());
        sb8.append(".action.ACTION_PT_ASK_TO_LEAVE");
        ACTION_PT_ASK_TO_LEAVE = sb8.toString();
    }
}
