package org.webrtc.voiceengine;

public class VoiceEnginContext {
    private static int s_selectedPlayerStreamType = -1;

    public static int getSelectedPlayerStreamType() {
        return s_selectedPlayerStreamType;
    }

    protected static void setSelectedPlayerStreamType(int i) {
        s_selectedPlayerStreamType = i;
    }
}
