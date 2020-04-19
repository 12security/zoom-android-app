package com.zipow.videobox.confapp.meeting.optimize;

import androidx.annotation.NonNull;
import java.util.List;
import p021us.zoom.androidlib.util.EventAction;

public abstract class EventActionForUserEvent extends EventAction {
    private boolean mIsLargeAmountUser;
    @NonNull
    private List<ZMConfUserActionInfo> mZMConfUserActionInfos;

    public EventActionForUserEvent(String str, @NonNull List<ZMConfUserActionInfo> list, boolean z) {
        super(str);
        this.mZMConfUserActionInfos = list;
        this.mIsLargeAmountUser = z;
    }

    @NonNull
    public List<ZMConfUserActionInfo> getmZMConfUserActionInfos() {
        return this.mZMConfUserActionInfos;
    }

    public boolean ismIsLargeAmountUser() {
        return this.mIsLargeAmountUser;
    }
}
