package com.zipow.videobox.view;

import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.util.ZMConfUtil;
import java.io.Serializable;

public abstract class BaseAttendeeItem implements Serializable {
    public boolean audioOn;
    public long audioType = 2;
    public boolean isAllowTalked;
    public boolean isSupportTempTalk;

    /* access modifiers changed from: protected */
    public void updateAudio(long j) {
        CmmAudioStatus cmmAudioStatus = ZMConfUtil.getCmmAudioStatus(j);
        if (cmmAudioStatus != null) {
            this.audioOn = !cmmAudioStatus.getIsMuted();
            this.audioType = cmmAudioStatus.getAudiotype();
        }
    }
}
