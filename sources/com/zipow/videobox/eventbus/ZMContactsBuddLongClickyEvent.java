package com.zipow.videobox.eventbus;

import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.view.IMAddrBookItem;

public class ZMContactsBuddLongClickyEvent {
    private IMAddrBookItem buddy;
    private MMZoomBuddyGroup group;

    public ZMContactsBuddLongClickyEvent(IMAddrBookItem iMAddrBookItem, MMZoomBuddyGroup mMZoomBuddyGroup) {
        this.buddy = iMAddrBookItem;
        this.group = mMZoomBuddyGroup;
    }

    public IMAddrBookItem getBuddy() {
        return this.buddy;
    }

    public void setBuddy(IMAddrBookItem iMAddrBookItem) {
        this.buddy = iMAddrBookItem;
    }

    public MMZoomBuddyGroup getGroup() {
        return this.group;
    }

    public void setGroup(MMZoomBuddyGroup mMZoomBuddyGroup) {
        this.group = mMZoomBuddyGroup;
    }
}
