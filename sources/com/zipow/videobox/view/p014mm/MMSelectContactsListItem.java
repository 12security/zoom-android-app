package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.view.IMAddrBookItem;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;

/* renamed from: com.zipow.videobox.view.mm.MMSelectContactsListItem */
public class MMSelectContactsListItem extends MMBuddyItem {
    private static final long serialVersionUID = 6027100775492919094L;
    protected boolean bChecked = false;
    protected boolean disabled = false;
    protected boolean isAlternativeHost = false;
    protected boolean isManualInput = false;
    @Nullable
    protected IMAddrBookItem mAddrBookItem;
    protected boolean showNotes = false;

    public MMSelectContactsListItem() {
    }

    public MMSelectContactsListItem(@Nullable IMAddrBookItem iMAddrBookItem) {
        this.mAddrBookItem = iMAddrBookItem;
        if (iMAddrBookItem != null) {
            if (iMAddrBookItem.getPhoneNumberCount() > 0) {
                this.phoneNumber = iMAddrBookItem.getPhoneNumber(0);
            }
            this.normalizedPhoneNumber = iMAddrBookItem.getNormalizedPhoneNumber(0);
            this.buddyJid = iMAddrBookItem.getJid();
            this.itemId = this.buddyJid;
            this.screenName = iMAddrBookItem.getScreenName();
            this.sortKey = SortUtil.getSortKey(this.screenName, CompatUtils.getLocalDefault());
            this.email = iMAddrBookItem.getAccountEmail();
            this.avatar = iMAddrBookItem.getAvatarPath();
        }
    }

    public boolean isManualInput() {
        return this.isManualInput;
    }

    public void setManualInput(boolean z) {
        this.isManualInput = z;
    }

    public boolean isAlternativeHost() {
        return this.isAlternativeHost;
    }

    public void setAlternativeHost(boolean z) {
        this.isAlternativeHost = z;
    }

    public boolean isShowNotes() {
        return this.showNotes;
    }

    public void setShowNotes(boolean z) {
        this.showNotes = z;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setIsDisabled(boolean z) {
        this.disabled = z;
    }

    public boolean isAddrBookItem() {
        return this.mAddrBookItem != null;
    }

    @Nullable
    public IMAddrBookItem getAddrBookItem() {
        return this.mAddrBookItem;
    }

    public boolean isChecked() {
        return this.bChecked;
    }

    public void setIsChecked(boolean z) {
        this.bChecked = z;
    }

    @Nullable
    public View getView(Context context, View view, boolean z, boolean z2, MemCache<String, Bitmap> memCache, boolean z3, boolean z4, boolean z5) {
        return getView(context, view, z, z2, memCache, z3, false, z4, z5);
    }

    @Nullable
    public View getView(Context context, View view, boolean z, boolean z2, MemCache<String, Bitmap> memCache, boolean z3, boolean z4, boolean z5, boolean z6) {
        MMSelectContactsListItemView mMSelectContactsListItemView;
        View view2 = view;
        if (view2 instanceof MMSelectContactsListItemView) {
            mMSelectContactsListItemView = (MMSelectContactsListItemView) view2;
        } else {
            Context context2 = context;
            mMSelectContactsListItemView = new MMSelectContactsListItemView(context);
        }
        bindView(mMSelectContactsListItemView, z, z2, memCache, z3, z4, z5, z6);
        return mMSelectContactsListItemView;
    }

    private void bindView(MMSelectContactsListItemView mMSelectContactsListItemView, boolean z, boolean z2, MemCache<String, Bitmap> memCache, boolean z3, boolean z4, boolean z5, boolean z6) {
        mMSelectContactsListItemView.setCheckVisible(z);
        mMSelectContactsListItemView.setContactItem(this, memCache, z3, z5, z6);
        mMSelectContactsListItemView.setCheckDisabled(this.disabled);
        mMSelectContactsListItemView.setShowPresence(z2);
        if (z4) {
            mMSelectContactsListItemView.setSlashCommand(this);
        }
    }

    public boolean isBlockedByIB() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.buddyJid);
        if (buddyWithJID == null) {
            return false;
        }
        return buddyWithJID.isIMBlockedByIB();
    }
}
