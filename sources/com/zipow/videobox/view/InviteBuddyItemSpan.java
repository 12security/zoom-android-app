package com.zipow.videobox.view;

import android.content.Context;

public class InviteBuddyItemSpan extends RoundRectBackGroundSpan {
    private InviteBuddyItem mItem;

    public InviteBuddyItemSpan(Context context, InviteBuddyItem inviteBuddyItem) {
        super(context);
        this.mItem = inviteBuddyItem;
    }

    public InviteBuddyItem getItem() {
        return this.mItem;
    }

    public void setItem(InviteBuddyItem inviteBuddyItem) {
        this.mItem = inviteBuddyItem;
    }
}
