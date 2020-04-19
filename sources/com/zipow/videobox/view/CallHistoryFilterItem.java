package com.zipow.videobox.view;

import android.content.Context;
import com.zipow.videobox.sip.server.CmmCallHistoryFilterDataBean;
import p021us.zoom.androidlib.widget.IZMListItem;

public class CallHistoryFilterItem implements IZMListItem {
    private CmmCallHistoryFilterDataBean bean;
    private boolean isSelected;
    private String name;

    public String getSubLabel() {
        return null;
    }

    public CallHistoryFilterItem(CmmCallHistoryFilterDataBean cmmCallHistoryFilterDataBean) {
        this.bean = cmmCallHistoryFilterDataBean;
    }

    public int getFilterType() {
        return this.bean.getFilterType();
    }

    public void init(Context context) {
        this.name = this.bean.getDisplayName(context);
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
