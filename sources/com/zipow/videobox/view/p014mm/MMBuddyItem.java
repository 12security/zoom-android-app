package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookItem;
import java.io.Serializable;

/* renamed from: com.zipow.videobox.view.mm.MMBuddyItem */
public class MMBuddyItem implements Serializable {
    private static final long serialVersionUID = 1;
    protected int accountStatus = 0;
    @Nullable
    protected String avatar;
    @Nullable
    protected String buddyJid;
    @Nullable
    protected String contactName;
    @Nullable
    protected String email;
    protected boolean isRobot = false;
    @Nullable
    protected String itemId;
    @Nullable
    protected IMAddrBookItem localContact;
    protected boolean myself = false;
    @Nullable
    protected String normalizedPhoneNumber;
    @Nullable
    protected String phoneNumber;
    @Nullable
    protected String screenName;
    @Nullable
    protected String sortKey;

    public MMBuddyItem() {
    }

    public void updateInfo() {
        if (!TextUtils.isEmpty(this.buddyJid)) {
            IMAddrBookItem buddyByJid = ZMBuddySyncInstance.getInsatance().getBuddyByJid(this.buddyJid, true);
            if (buddyByJid != null) {
                this.isRobot = buddyByJid.getIsRobot();
                this.contactName = buddyByJid.getScreenName();
                this.buddyJid = buddyByJid.getJid();
                if (buddyByJid.getPhoneNumberCount() > 0) {
                    this.phoneNumber = buddyByJid.getPhoneNumber(0);
                }
                this.avatar = buddyByJid.getAvatarPath();
                this.normalizedPhoneNumber = buddyByJid.getNormalizedPhoneNumber(0);
                this.screenName = buddyByJid.getScreenName();
                this.accountStatus = buddyByJid.getAccountStatus();
            }
        }
    }

    public MMBuddyItem(@Nullable ZoomBuddy zoomBuddy, @Nullable IMAddrBookItem iMAddrBookItem) {
        if (zoomBuddy != null) {
            this.buddyJid = zoomBuddy.getJid();
            if (iMAddrBookItem == null || iMAddrBookItem.getPhoneNumberCount() <= 0) {
                this.phoneNumber = zoomBuddy.getPhoneNumber();
            } else {
                this.phoneNumber = iMAddrBookItem.getPhoneNumber(0);
            }
            this.normalizedPhoneNumber = zoomBuddy.getPhoneNumber();
            this.screenName = BuddyNameUtil.getBuddyDisplayName(zoomBuddy, iMAddrBookItem, false);
            this.avatar = zoomBuddy.getLocalPicturePath();
            this.isRobot = zoomBuddy.isRobot();
            this.accountStatus = zoomBuddy.getAccountStatus();
        } else if (iMAddrBookItem != null) {
            this.isRobot = iMAddrBookItem.getIsRobot();
            this.contactName = iMAddrBookItem.getScreenName();
            this.buddyJid = iMAddrBookItem.getJid();
            if (iMAddrBookItem.getPhoneNumberCount() > 0) {
                this.phoneNumber = iMAddrBookItem.getPhoneNumber(0);
            }
            this.normalizedPhoneNumber = iMAddrBookItem.getNormalizedPhoneNumber(0);
            this.screenName = iMAddrBookItem.getScreenName();
            this.accountStatus = iMAddrBookItem.getAccountStatus();
        }
        this.localContact = iMAddrBookItem;
    }

    public MMBuddyItem(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            this.contactName = iMAddrBookItem.getScreenName();
            this.buddyJid = iMAddrBookItem.getJid();
            this.isRobot = iMAddrBookItem.getIsRobot();
            if (iMAddrBookItem.getPhoneNumberCount() > 0) {
                this.phoneNumber = iMAddrBookItem.getPhoneNumber(0);
            }
            this.normalizedPhoneNumber = iMAddrBookItem.getNormalizedPhoneNumber(0);
            this.screenName = iMAddrBookItem.getScreenName();
            this.avatar = null;
            this.accountStatus = iMAddrBookItem.getAccountStatus();
        }
    }

    public boolean isRobot() {
        return this.isRobot;
    }

    @Nullable
    public String getBuddyJid() {
        return this.buddyJid;
    }

    public void setBuddyJid(@Nullable String str) {
        this.buddyJid = str;
    }

    @Nullable
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(@Nullable String str) {
        this.phoneNumber = str;
    }

    @Nullable
    public String getNormalizedPhoneNumber() {
        return this.normalizedPhoneNumber;
    }

    public void setNormalizedPhoneNumber(@Nullable String str) {
        this.normalizedPhoneNumber = str;
    }

    @Nullable
    public String getScreenName() {
        return this.screenName;
    }

    public void setScreenName(@Nullable String str) {
        this.screenName = str;
    }

    @Nullable
    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(@Nullable String str) {
        this.contactName = str;
    }

    @Nullable
    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(@Nullable String str) {
        this.avatar = str;
    }

    @Nullable
    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(@Nullable String str) {
        this.itemId = str;
    }

    @Nullable
    public String getEmail() {
        return this.email;
    }

    public void setEmail(@Nullable String str) {
        this.email = str;
    }

    public boolean isMySelf() {
        return this.myself;
    }

    public void setIsMySelf(boolean z) {
        this.myself = z;
    }

    @Nullable
    public String getSortKey() {
        return this.sortKey;
    }

    public void setSortKey(@Nullable String str) {
        this.sortKey = str;
    }

    @Nullable
    public IMAddrBookItem getLocalContact() {
        return this.localContact;
    }

    public void setLocalContact(@Nullable IMAddrBookItem iMAddrBookItem) {
        this.localContact = iMAddrBookItem;
    }

    public int getAccountStatus() {
        return this.accountStatus;
    }

    public void setAccountStatus(int i) {
        this.accountStatus = i;
    }

    public ParamsBuilder getAvatarBuilderParams(Context context) {
        if (getLocalContact() != null) {
            return getLocalContact().getAvatarParamsBuilder();
        }
        ParamsBuilder paramsBuilder = new ParamsBuilder();
        paramsBuilder.setName(getScreenName(), getBuddyJid()).setPath(getAvatar());
        return paramsBuilder;
    }
}
