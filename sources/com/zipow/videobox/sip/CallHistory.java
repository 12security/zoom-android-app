package com.zipow.videobox.sip;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.io.Serializable;
import p021us.zoom.androidlib.util.StringUtil;

public class CallHistory implements Serializable {
    private static final long serialVersionUID = 1;
    @Nullable
    private String buddyJid;
    private String calleeDisplayName;
    private String calleeJid;
    private String calleeUri;
    private String callerDisplayName;
    private String callerJid;
    private String callerUri;
    private int direction;

    /* renamed from: id */
    private String f326id;
    @Nullable
    private String mZOOMDisplayName;
    private String number;
    private int state;
    private long time;
    private long timeLong;
    private int type;

    public CallHistory() {
    }

    public CallHistory(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, long j, int i2, long j2, int i3) {
        this.type = i;
        this.f326id = str;
        this.calleeDisplayName = str8;
        this.calleeJid = str7;
        this.calleeUri = str3;
        this.callerDisplayName = str5;
        this.callerJid = str4;
        this.callerUri = str3;
        this.time = j;
        this.state = i2;
        this.timeLong = j2;
        this.direction = i3;
        this.number = str2;
    }

    public void updateZOOMDisplayName() {
        String str;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int i = this.type;
            if (i == 2 || i == 1) {
                if (this.direction == 1) {
                    str = this.callerJid;
                } else {
                    str = this.calleeJid;
                }
                if (!StringUtil.isEmptyOrNull(str)) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                    if (buddyWithJID != null) {
                        this.buddyJid = buddyWithJID.getJid();
                        this.mZOOMDisplayName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                    }
                }
            } else if (!StringUtil.isEmptyOrNull(this.number)) {
                ZoomBuddy buddyWithSipPhone = zoomMessenger.getBuddyWithSipPhone(this.number);
                if (buddyWithSipPhone != null) {
                    this.buddyJid = buddyWithSipPhone.getJid();
                    this.mZOOMDisplayName = BuddyNameUtil.getBuddyDisplayName(buddyWithSipPhone, null);
                }
            }
        }
    }

    @Nullable
    public String getZOOMDisplayName() {
        if (!StringUtil.isEmptyOrNull(this.mZOOMDisplayName)) {
            return this.mZOOMDisplayName;
        }
        return this.direction == 1 ? this.callerDisplayName : this.calleeDisplayName;
    }

    @Nullable
    public String getBuddyJid() {
        return this.buddyJid;
    }

    public String getCallerUri() {
        return this.callerUri;
    }

    public void setCallerUri(String str) {
        this.callerUri = str;
    }

    public String getCallerJid() {
        return this.callerJid;
    }

    public void setCallerJid(String str) {
        this.callerJid = str;
    }

    public String getCalleeUri() {
        return this.calleeUri;
    }

    public void setCalleeUri(String str) {
        this.calleeUri = str;
    }

    public String getCalleeJid() {
        return this.calleeJid;
    }

    public void setCalleeJid(String str) {
        this.calleeJid = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getId() {
        return this.f326id;
    }

    public void setId(String str) {
        this.f326id = str;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public String getCallerDisplayName() {
        return this.callerDisplayName;
    }

    public void setCallerDisplayName(String str) {
        this.callerDisplayName = str;
    }

    public String getCalleeDisplayName() {
        return this.calleeDisplayName;
    }

    public void setCalleeDisplayName(String str) {
        this.calleeDisplayName = str;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int i) {
        this.state = i;
    }

    public long getTimeLong() {
        return this.timeLong;
    }

    public void setTimeLong(long j) {
        this.timeLong = j;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int i) {
        this.direction = i;
    }
}
