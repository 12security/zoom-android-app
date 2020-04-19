package com.zipow.videobox.ptapp;

import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTUI.IIMListener;

public abstract class SimpleIMListener implements IIMListener {
    public void onIMBuddyPic(BuddyItem buddyItem) {
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
    }

    public void onIMBuddySort() {
    }

    public void onIMLocalStatusChanged(int i) {
    }

    public void onIMReceived(IMMessage iMMessage) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    public void onSubscriptionRequest() {
    }

    public void onSubscriptionUpdate() {
    }
}
