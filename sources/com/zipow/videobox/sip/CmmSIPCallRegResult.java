package com.zipow.videobox.sip;

public class CmmSIPCallRegResult {
    private int regStatus;
    private int respCode;
    private String respCodeDetail;
    private String respDesc;

    public CmmSIPCallRegResult(int i, int i2, String str, String str2) {
        this.regStatus = i;
        this.respCode = i2;
        this.respDesc = str;
        this.respCodeDetail = str2;
    }

    public int getRegStatus() {
        return this.regStatus;
    }

    public void setRegStatus(int i) {
        this.regStatus = i;
    }

    public int getRespCode() {
        return this.respCode;
    }

    public String getRespDesc() {
        return this.respDesc;
    }

    public void setRespCode(int i) {
        this.respCode = i;
    }

    public void setRespDesc(String str) {
        this.respDesc = str;
    }

    public String getRespCodeDetail() {
        return this.respCodeDetail;
    }

    public void setRespCodeDetail(String str) {
        this.respCodeDetail = str;
    }

    public boolean isRegistered() {
        return getRegStatus() == 6;
    }

    public boolean isRegisterIdle() {
        return getRegStatus() == 0;
    }

    public boolean isRegisterError() {
        int regStatus2 = getRegStatus();
        return regStatus2 == 5 || regStatus2 == 0 || regStatus2 == 7;
    }

    public boolean isRegisterFailed() {
        return getRegStatus() == 5;
    }
}
