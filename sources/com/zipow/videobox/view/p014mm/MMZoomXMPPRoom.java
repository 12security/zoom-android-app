package com.zipow.videobox.view.p014mm;

import java.io.Serializable;

/* renamed from: com.zipow.videobox.view.mm.MMZoomXMPPRoom */
public class MMZoomXMPPRoom implements Serializable {
    private static final long serialVersionUID = 1;
    private long count;
    private boolean isJoined;
    private String jid;
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String str) {
        this.jid = str;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long j) {
        this.count = j;
    }

    public boolean isJoined() {
        return this.isJoined;
    }

    public void setJoined(boolean z) {
        this.isJoined = z;
    }
}
