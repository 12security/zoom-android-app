package com.zipow.videobox.sip;

public interface CmmSIPCallHistoryFilterType {
    public static final int kSIPCallHistoryFilter_All = 1;
    public static final int kSIPCallHistoryFilter_Incoming = 4;
    public static final int kSIPCallHistoryFilter_Missed = 2;
    public static final int kSIPCallHistoryFilter_Outgoing = 5;
    public static final int kSIPCallHistoryFilter_Recordings = 3;
    public static final int kSIPCallHistoryFilter_Unknown = 0;
}
