package com.zipow.videobox.view;

import android.content.Context;
import com.zipow.videobox.sip.server.CmmSIPVoiceMailSharedRelationshipBean;
import p021us.zoom.androidlib.widget.IZMListItem;

public class VoiceMailFilterItem implements IZMListItem {
    private CmmSIPVoiceMailSharedRelationshipBean bean;
    private boolean isSelected;
    private String name;

    public String getSubLabel() {
        return null;
    }

    public VoiceMailFilterItem(CmmSIPVoiceMailSharedRelationshipBean cmmSIPVoiceMailSharedRelationshipBean) {
        this.bean = cmmSIPVoiceMailSharedRelationshipBean;
    }

    public String getId() {
        return this.bean.getExtensionId();
    }

    public void init(Context context) {
        this.name = this.bean.getDisplayExtensionName(context);
        this.isSelected = this.bean.isChecked();
    }

    public String getLabel() {
        return this.name;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
