package com.zipow.videobox.confapp.meeting.optimize;

public class ZMConfUserActionInfo {
    private int flag;
    private long userId;

    public long getUserId() {
        return this.userId;
    }

    public int getFlag() {
        return this.flag;
    }

    public ZMConfUserActionInfo(long j, int i) {
        this.userId = j;
        this.flag = i;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ZMConfUserActionInfo zMConfUserActionInfo = (ZMConfUserActionInfo) obj;
        if (this.userId != zMConfUserActionInfo.userId) {
            return false;
        }
        if (this.flag != zMConfUserActionInfo.flag) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        long j = this.userId;
        return (((int) (j ^ (j >>> 32))) * 31) + this.flag;
    }
}
