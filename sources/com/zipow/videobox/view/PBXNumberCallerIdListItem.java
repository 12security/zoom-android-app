package com.zipow.videobox.view;

import android.content.Context;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTAppProtos.PBXNumber;

public class PBXNumberCallerIdListItem extends CallerIdListItem {
    private PBXNumber pbxNumber;

    public PBXNumberCallerIdListItem(PBXNumber pBXNumber) {
        this.pbxNumber = pBXNumber;
    }

    public void init(Context context) {
        this.sublabel = this.pbxNumber.getNumber();
        this.label = this.pbxNumber.getName();
    }

    public String getLabel() {
        return this.label;
    }

    public String getSubLabel() {
        return this.sublabel;
    }

    @Nullable
    public String getNumber() {
        return this.pbxNumber.getNumber();
    }
}
