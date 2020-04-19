package com.zipow.videobox.view.p014mm;

import android.os.Handler;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSelectSessionListItem */
public class MMSelectSessionListItem {
    private String avatar;
    private IMAddrBookItem fromContact;
    private boolean isGroup;
    @NonNull
    private Handler mHandler = new Handler();
    private String sessionId;
    private long timeStamp;
    private String title;

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String str) {
        this.sessionId = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String str) {
        this.avatar = str;
    }

    public boolean isGroup() {
        return this.isGroup;
    }

    public void setIsGroup(boolean z) {
        this.isGroup = z;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long j) {
        this.timeStamp = j;
    }

    public IMAddrBookItem getFromContact() {
        return this.fromContact;
    }

    public void setFromContact(IMAddrBookItem iMAddrBookItem) {
        this.fromContact = iMAddrBookItem;
    }

    public boolean isBuddyWithPhoneNumberInSession(@Nullable String str) {
        if (str == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        if (this.isGroup) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.sessionId);
            if (groupById == null) {
                return false;
            }
            int buddyCount = groupById.getBuddyCount();
            for (int i = 0; i < buddyCount; i++) {
                ZoomBuddy buddyAt = groupById.getBuddyAt(i);
                if (buddyAt != null && StringUtil.isSameString(str, buddyAt.getPhoneNumber())) {
                    return true;
                }
            }
            return false;
        }
        ZoomBuddy buddyWithPhoneNumber = zoomMessenger.getBuddyWithPhoneNumber(str);
        if (buddyWithPhoneNumber == null) {
            return false;
        }
        return StringUtil.isSameString(str, buddyWithPhoneNumber.getPhoneNumber());
    }

    public boolean isBuddyWithJIDInSession(@Nullable String str) {
        if (str == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        if (this.isGroup) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.sessionId);
            if (groupById == null) {
                return false;
            }
            int buddyCount = groupById.getBuddyCount();
            for (int i = 0; i < buddyCount; i++) {
                ZoomBuddy buddyAt = groupById.getBuddyAt(i);
                if (buddyAt != null && StringUtil.isSameString(str, buddyAt.getJid())) {
                    return true;
                }
            }
            return false;
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
        if (buddyWithJID == null) {
            return false;
        }
        return StringUtil.isSameString(this.sessionId, buddyWithJID.getJid());
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00b4  */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View getView(@androidx.annotation.Nullable android.content.Context r6, @androidx.annotation.Nullable android.view.View r7, android.view.ViewGroup r8, com.zipow.videobox.util.MemCache<java.lang.String, android.graphics.drawable.Drawable> r9, boolean r10) {
        /*
            r5 = this;
            r9 = 0
            if (r6 != 0) goto L_0x0004
            return r9
        L_0x0004:
            r10 = 0
            if (r7 == 0) goto L_0x0014
            java.lang.String r0 = "MMSelectSessionListItem"
            java.lang.Object r1 = r7.getTag()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0014
            goto L_0x0026
        L_0x0014:
            android.view.LayoutInflater r7 = android.view.LayoutInflater.from(r6)
            if (r7 != 0) goto L_0x001b
            return r9
        L_0x001b:
            int r0 = p021us.zoom.videomeetings.C4558R.layout.zm_mm_select_session_list_item
            android.view.View r7 = r7.inflate(r0, r8, r10)
            java.lang.String r8 = "MMSelectSessionListItem"
            r7.setTag(r8)
        L_0x0026:
            com.zipow.videobox.ptapp.PTApp r8 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r8 = r8.getZoomMessenger()
            if (r8 != 0) goto L_0x0031
            return r7
        L_0x0031:
            boolean r0 = r5.isAactivate()
            int r1 = p021us.zoom.videomeetings.C4558R.C4560id.avatarView
            android.view.View r1 = r7.findViewById(r1)
            com.zipow.videobox.view.AvatarView r1 = (com.zipow.videobox.view.AvatarView) r1
            int r2 = p021us.zoom.videomeetings.C4558R.C4560id.txtTitle
            android.view.View r2 = r7.findViewById(r2)
            us.zoom.androidlib.widget.ZMEllipsisTextView r2 = (p021us.zoom.androidlib.widget.ZMEllipsisTextView) r2
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.imgPresence
            android.view.View r3 = r7.findViewById(r3)
            com.zipow.videobox.view.PresenceStateView r3 = (com.zipow.videobox.view.PresenceStateView) r3
            if (r1 == 0) goto L_0x0081
            if (r0 == 0) goto L_0x0054
            r0 = 1065353216(0x3f800000, float:1.0)
            goto L_0x0056
        L_0x0054:
            r0 = 1056964608(0x3f000000, float:0.5)
        L_0x0056:
            r1.setAlpha(r0)
            boolean r0 = r5.isGroup
            if (r0 != 0) goto L_0x006f
            com.zipow.videobox.view.IMAddrBookItem r0 = r5.getFromContact()
            if (r0 == 0) goto L_0x006f
            com.zipow.videobox.view.IMAddrBookItem r9 = r5.getFromContact()
            com.zipow.videobox.view.AvatarView$ParamsBuilder r9 = r9.getAvatarParamsBuilder()
            r1.show(r9)
            goto L_0x0081
        L_0x006f:
            boolean r0 = r5.isGroup
            if (r0 == 0) goto L_0x0081
            com.zipow.videobox.view.AvatarView$ParamsBuilder r0 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r0.<init>()
            int r4 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_avatar_group
            com.zipow.videobox.view.AvatarView$ParamsBuilder r9 = r0.setResource(r4, r9)
            r1.show(r9)
        L_0x0081:
            if (r2 == 0) goto L_0x00b9
            java.lang.String r9 = r5.title
            if (r9 == 0) goto L_0x00b9
            boolean r9 = r5.isGroup
            if (r9 != 0) goto L_0x00a5
            com.zipow.videobox.view.IMAddrBookItem r9 = r5.getFromContact()
            if (r9 == 0) goto L_0x00a5
            int r0 = r9.getAccountStatus()
            r1 = 1
            if (r0 != r1) goto L_0x009b
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_deactivated_62074
            goto L_0x00a6
        L_0x009b:
            int r9 = r9.getAccountStatus()
            r0 = 2
            if (r9 != r0) goto L_0x00a5
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_terminated_62074
            goto L_0x00a6
        L_0x00a5:
            r9 = 0
        L_0x00a6:
            java.lang.String r0 = r5.title
            r2.setEllipsisText(r0, r9)
            boolean r9 = r5.isAactivate()
            if (r9 == 0) goto L_0x00b4
            int r9 = p021us.zoom.videomeetings.C4558R.style.UIKitTextView_BuddyName_Normal
            goto L_0x00b6
        L_0x00b4:
            int r9 = p021us.zoom.videomeetings.C4558R.style.UIKitTextView_BuddyName_Deactivate
        L_0x00b6:
            r2.setTextAppearance(r6, r9)
        L_0x00b9:
            boolean r6 = r5.isGroup
            if (r6 != 0) goto L_0x00e4
            com.zipow.videobox.view.IMAddrBookItem r6 = r5.getFromContact()
            if (r6 != 0) goto L_0x00c4
            return r7
        L_0x00c4:
            java.lang.String r9 = r6.getJid()
            com.zipow.videobox.ptapp.mm.ZoomBuddy r9 = r8.getBuddyWithJID(r9)
            if (r9 != 0) goto L_0x00cf
            return r7
        L_0x00cf:
            java.lang.String r6 = r6.getJid()
            r8.isMyContact(r6)
            r3.setVisibility(r10)
            com.zipow.videobox.view.IMAddrBookItem r6 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r9)
            r3.setState(r6)
            r3.setmTxtDeviceTypeGone()
            goto L_0x00e9
        L_0x00e4:
            r6 = 8
            r3.setVisibility(r6)
        L_0x00e9:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMSelectSessionListItem.getView(android.content.Context, android.view.View, android.view.ViewGroup, com.zipow.videobox.util.MemCache, boolean):android.view.View");
    }

    private boolean isAactivate() {
        IMAddrBookItem fromContact2 = getFromContact();
        if (fromContact2 == null || fromContact2.getAccountStatus() == 0) {
            return true;
        }
        return false;
    }

    private void loadPresenceStatus(ZoomMessenger zoomMessenger, @NonNull ImageView imageView, @NonNull ZoomBuddy zoomBuddy) {
        if (!zoomMessenger.isConnectionGood() || (!zoomBuddy.isDesktopOnline() && !zoomBuddy.isMobileOnline())) {
            imageView.setImageResource(C4558R.C4559drawable.zm_offline);
            imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
            return;
        }
        switch (zoomBuddy.getPresence()) {
            case 1:
                imageView.setImageResource(C4558R.C4559drawable.zm_status_idle);
                imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_idle));
                return;
            case 2:
                imageView.setImageResource(C4558R.C4559drawable.zm_status_dnd);
                imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_dnd_19903));
                return;
            case 3:
                imageView.setImageResource(C4558R.C4559drawable.zm_status_available);
                imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_available));
                return;
            case 4:
                imageView.setImageResource(C4558R.C4559drawable.zm_status_dnd);
                imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_xa_19903));
                return;
            default:
                if (zoomBuddy.isMobileOnline()) {
                    imageView.setImageResource(C4558R.C4559drawable.zm_status_available);
                    imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_available));
                    return;
                }
                imageView.setImageResource(C4558R.C4559drawable.zm_offline);
                imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
                return;
        }
    }
}
