package com.zipow.videobox.eventbus;

import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;

public class ZMContactsGroupLongClickEvent {
    private MMZoomBuddyGroup group;

    public ZMContactsGroupLongClickEvent(MMZoomBuddyGroup mMZoomBuddyGroup) {
        this.group = mMZoomBuddyGroup;
    }

    public MMZoomBuddyGroup getGroup() {
        return this.group;
    }

    public void setGroup(MMZoomBuddyGroup mMZoomBuddyGroup) {
        this.group = mMZoomBuddyGroup;
    }
}
