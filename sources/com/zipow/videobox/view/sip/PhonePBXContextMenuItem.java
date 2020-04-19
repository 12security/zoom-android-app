package com.zipow.videobox.view.sip;

import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;

public class PhonePBXContextMenuItem extends ZMSimpleMenuItem {
    public static final int ACTION_BLOCK_CALLER = 3;
    public static final int ACTION_CALL_BACK = 0;
    public static final int ACTION_COPY_NUMBER = 5;
    public static final int ACTION_DELETE = 1;
    public static final int ACTION_PLAY_RECORDING = 7;
    public static final int ACTION_SELECT_ITEM = 2;
    public static final int ACTION_SEND_MESSAGE = 10;
    public static final int ACTION_UNBLOCK_CALLER = 4;
    public static final int ACTION_VIEW_PROFILE = 6;
    public static final int ADD_TO_EXISTING_PHONE_CONTACT = 9;
    public static final int CREATE_PHONE_CONTACT = 8;

    public PhonePBXContextMenuItem(String str, int i) {
        super(i, str);
    }
}
