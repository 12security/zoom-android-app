package com.zipow.videobox.confapp;

public interface DEVICE_CMD {
    public static final int CMD_CAM_STATUS = 3;
    public static final int CMD_DEVICE_CALLBACK = 5;
    public static final int CMD_DEVICE_HAVE_INPUT = 7;
    public static final int CMD_DEVICE_NOCALLBACK = 4;
    public static final int CMD_DEVICE_NO_INPUT = 6;
    public static final int CMD_DEVICE_SAMPLERATE_EXCEPTION = 15;
    public static final int CMD_HAS_VOICE = 14;
    public static final int CMD_INIT_AUDIO_FAIL = 9;
    public static final int CMD_INIT_AUDIO_SUCCESS = 10;
    public static final int CMD_KUBI_STATUS = 8;
    public static final int CMD_MIC_STATUS = 1;
    public static final int CMD_PLAY_AUDIO = 0;
    public static final int CMD_SELECT_CAM_CHANGED = 13;
    public static final int CMD_SELECT_MIC_CHANGED = 11;
    public static final int CMD_SELECT_SPK_CHANGED = 12;
    public static final int CMD_SPK_STATUS = 2;
    public static final int CMD_ULTRA_SOUND_DETECT_STOPED = 16;
}
