package com.zipow.videobox.sip.server;

import android.content.Context;
import p021us.zoom.videomeetings.C4558R;

public class CmmSIPVoiceMailSharedRelationshipBean {
    private boolean checked;
    private String extensionId;
    private int extensionLevel;
    private String extensionName;

    public String getExtensionId() {
        return this.extensionId;
    }

    public void setExtensionId(String str) {
        this.extensionId = str;
    }

    public String getExtensionName() {
        return this.extensionName;
    }

    public void setExtensionName(String str) {
        this.extensionName = str;
    }

    public int getExtensionLevel() {
        return this.extensionLevel;
    }

    public void setExtensionLevel(int i) {
        this.extensionLevel = i;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean z) {
        this.checked = z;
    }

    public String getDisplayExtensionName(Context context) {
        if (getExtensionLevel() != -1) {
            return getExtensionName();
        }
        return context.getResources().getString(C4558R.string.zm_pbx_you_100064, new Object[]{getExtensionName()});
    }
}
