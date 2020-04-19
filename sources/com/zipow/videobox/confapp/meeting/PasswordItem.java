package com.zipow.videobox.confapp.meeting;

public class PasswordItem {
    private boolean isCorrect;
    private String ruleTxt;
    private long ruleType;

    public boolean isCorrect() {
        return this.isCorrect;
    }

    public void setCorrect(boolean z) {
        this.isCorrect = z;
    }

    public String getRuleTxt() {
        return this.ruleTxt;
    }

    public void setRuleTxt(String str) {
        this.ruleTxt = str;
    }

    public long getRuleType() {
        return this.ruleType;
    }

    public void setRuleType(long j) {
        this.ruleType = j;
    }
}
