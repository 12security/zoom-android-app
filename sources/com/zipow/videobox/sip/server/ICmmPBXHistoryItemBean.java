package com.zipow.videobox.sip.server;

public interface ICmmPBXHistoryItemBean {
    public static final String ANONYMOUS_PEER = "+anonymous";

    long getCreateTime();

    String getId();

    boolean isRestricted();
}
