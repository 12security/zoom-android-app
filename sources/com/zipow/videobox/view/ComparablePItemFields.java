package com.zipow.videobox.view;

import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import p021us.zoom.androidlib.util.StringUtil;

public class ComparablePItemFields {
    @Nullable
    private CmmAudioStatus audioStatus;
    private long audioType;
    private boolean isCoHost;
    private boolean isHost;
    private boolean isInterpreter = false;
    private boolean isMuted = true;
    private boolean isMySelf;
    private boolean isRaiseHandState;
    private boolean isSharingComputerAudio = false;
    private long raiseHandTimestamp;
    private String screenName;
    @Nullable
    private CmmUser user;

    @Nullable
    public CmmUser getUser() {
        return this.user;
    }

    public void setUser(@Nullable CmmUser cmmUser) {
        this.user = cmmUser;
        if (cmmUser != null) {
            this.audioStatus = cmmUser.getAudioStatusObj();
            CmmAudioStatus cmmAudioStatus = this.audioStatus;
            if (cmmAudioStatus != null) {
                this.audioType = cmmAudioStatus.getAudiotype();
                this.isMuted = this.audioStatus.getIsMuted();
            } else {
                this.audioType = 2;
                this.isMuted = true;
            }
            this.isSharingComputerAudio = cmmUser.isSharingPureComputerAudio();
            this.isRaiseHandState = cmmUser.getRaiseHandState();
            if (this.isRaiseHandState) {
                this.raiseHandTimestamp = cmmUser.getRaiseHandTimestamp();
            } else {
                this.raiseHandTimestamp = 0;
            }
            this.isInterpreter = cmmUser.isInterpreter();
            return;
        }
        this.audioStatus = null;
        this.isRaiseHandState = false;
        this.raiseHandTimestamp = 0;
    }

    public boolean isMySelf() {
        return this.isMySelf;
    }

    public void setMySelf(boolean z) {
        this.isMySelf = z;
    }

    public boolean isHost() {
        return this.isHost;
    }

    public void setHost(boolean z) {
        this.isHost = z;
    }

    public boolean isCoHost() {
        return this.isCoHost;
    }

    public void setCoHost(boolean z) {
        this.isCoHost = z;
    }

    public boolean isRaiseHandState() {
        return this.isRaiseHandState;
    }

    public long getRaiseHandTimestamp() {
        return this.raiseHandTimestamp;
    }

    @Nullable
    public CmmAudioStatus getAudioStatus() {
        return this.audioStatus;
    }

    public long getAudioType() {
        return this.audioType;
    }

    public boolean isMuted() {
        return this.isMuted;
    }

    public String getScreenName() {
        return StringUtil.safeString(this.screenName);
    }

    public void setScreenName(String str) {
        this.screenName = str;
    }

    public boolean isSharingComputerAudio() {
        return this.isSharingComputerAudio;
    }

    public boolean isInterpreter() {
        return this.isInterpreter;
    }
}
