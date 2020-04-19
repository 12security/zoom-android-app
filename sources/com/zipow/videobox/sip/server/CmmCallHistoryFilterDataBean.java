package com.zipow.videobox.sip.server;

import android.content.Context;
import p021us.zoom.videomeetings.C4558R;

public class CmmCallHistoryFilterDataBean {
    private int filterType;
    private boolean isChecked;

    public int getFilterType() {
        return this.filterType;
    }

    public void setFilterType(int i) {
        this.filterType = i;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }

    public String getDisplayName(Context context) {
        if (context == null) {
            return "";
        }
        switch (this.filterType) {
            case 1:
                return context.getString(C4558R.string.zm_pbx_call_history_filter_all_108317);
            case 2:
                return context.getString(C4558R.string.zm_pbx_call_history_filter_missed_108317);
            case 3:
                return context.getString(C4558R.string.zm_pbx_call_history_filter_recordings_108317);
            default:
                return "";
        }
    }
}
