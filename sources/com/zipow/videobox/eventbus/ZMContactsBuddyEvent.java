package com.zipow.videobox.eventbus;

import com.zipow.videobox.view.IMAddrBookItem;

public class ZMContactsBuddyEvent {
    private IMAddrBookItem buddy;

    public ZMContactsBuddyEvent(IMAddrBookItem iMAddrBookItem) {
        this.buddy = iMAddrBookItem;
    }

    public IMAddrBookItem getBuddy() {
        return this.buddy;
    }

    public void setBuddy(IMAddrBookItem iMAddrBookItem) {
        this.buddy = iMAddrBookItem;
    }
}
