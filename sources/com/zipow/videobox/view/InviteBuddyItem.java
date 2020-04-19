package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomContact;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.util.MemCache;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class InviteBuddyItem extends IMBuddyItem {
    private static final long serialVersionUID = 6027100775492919094L;
    public boolean isChecked;
    public boolean isPresenceSupported;
    @Nullable
    private IMAddrBookItem mAddrBookItem;

    public InviteBuddyItem() {
        this.isChecked = false;
        this.isPresenceSupported = true;
    }

    public InviteBuddyItem(@NonNull BuddyItem buddyItem) {
        super(buddyItem, 0);
        this.isChecked = false;
        this.isPresenceSupported = true;
    }

    public InviteBuddyItem(ZoomContact zoomContact) {
        this.isChecked = false;
        this.isPresenceSupported = true;
        this.isPresenceSupported = false;
        this.userId = zoomContact.getUserID();
        this.screenName = StringUtil.formatPersonName(zoomContact.getFirstName(), zoomContact.getLastName(), PTApp.getInstance().getRegionCodeForNameFormating());
        this.email = zoomContact.getEmail();
        this.avatar = PTAppDelegation.getInstance().getFavoriteMgr().getLocalPicturePath(this.email);
        if (this.screenName != null && this.screenName.equals(this.email)) {
            this.screenName = "";
        }
        if (!StringUtil.isEmptyOrNull(this.screenName)) {
            this.sortKey = SortUtil.getSortKey(this.screenName, CompatUtils.getLocalDefault());
        } else {
            this.sortKey = SortUtil.getSortKey(this.email, CompatUtils.getLocalDefault());
        }
    }

    public InviteBuddyItem(@Nullable IMAddrBookItem iMAddrBookItem) {
        this.isChecked = false;
        this.isPresenceSupported = true;
        this.mAddrBookItem = iMAddrBookItem;
        if (iMAddrBookItem != null) {
            this.userId = String.valueOf(iMAddrBookItem.getJid());
            this.screenName = iMAddrBookItem.getScreenName();
            this.sortKey = SortUtil.getSortKey(this.screenName, CompatUtils.getLocalDefault());
            this.email = iMAddrBookItem.getAccountEmail();
            this.avatar = iMAddrBookItem.getAvatarPath();
        }
    }

    public boolean isAddrBookItem() {
        return this.mAddrBookItem != null;
    }

    @Nullable
    public IMAddrBookItem getAddrBookItem() {
        return this.mAddrBookItem;
    }

    @Nullable
    public View getView(Context context, View view, MemCache<String, Bitmap> memCache, boolean z) {
        InviteBuddyItemView inviteBuddyItemView;
        if (view instanceof InviteBuddyItemView) {
            inviteBuddyItemView = (InviteBuddyItemView) view;
        } else {
            inviteBuddyItemView = new InviteBuddyItemView(context);
        }
        bindView(inviteBuddyItemView, memCache, z);
        return inviteBuddyItemView;
    }

    private void bindView(InviteBuddyItemView inviteBuddyItemView, MemCache<String, Bitmap> memCache, boolean z) {
        inviteBuddyItemView.setBuddyListItem(this, memCache, z);
    }
}
