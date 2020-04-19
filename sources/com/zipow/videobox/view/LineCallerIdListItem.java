package com.zipow.videobox.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSipLineInfoForCallerID;
import p021us.zoom.videomeetings.C4558R;

public class LineCallerIdListItem extends CallerIdListItem {
    private CmmSipLineInfoForCallerID lineCaller;

    public LineCallerIdListItem(@NonNull CmmSipLineInfoForCallerID cmmSipLineInfoForCallerID) {
        this.lineCaller = cmmSipLineInfoForCallerID;
        this.label = cmmSipLineInfoForCallerID.getLineOwnerName();
        this.sublabel = cmmSipLineInfoForCallerID.getLineOwnerNumber();
    }

    public void init(Context context) {
        super.init(context);
        if (this.lineCaller != null) {
            this.sublabel = context.getString(C4558R.string.zm_pbx_caller_id_shared_104244, new Object[]{this.lineCaller.getLineOwnerNumber()});
        }
    }

    @Nullable
    public String getId() {
        return this.lineCaller.getLineId();
    }
}
