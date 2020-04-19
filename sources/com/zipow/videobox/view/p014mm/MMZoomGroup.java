package com.zipow.videobox.view.p014mm;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.io.Serializable;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;

/* renamed from: com.zipow.videobox.view.mm.MMZoomGroup */
public class MMZoomGroup implements Serializable {
    private static final long serialVersionUID = 1;
    private String adminScreenName = "";
    private long createTime;
    private String groupId;
    private String groupName;
    private String groupOwnerName;
    private boolean isE2E;
    private boolean isPrivate;
    private boolean isPublic;
    private boolean isRoom;
    private int memberCount;
    private int mucType;
    private int notifyType;
    private String owner;
    private String sortKey;

    private MMZoomGroup() {
    }

    @NonNull
    public static MMZoomGroup initWithZoomGroup(@NonNull ZoomGroup zoomGroup) {
        MMZoomGroup mMZoomGroup = new MMZoomGroup();
        if (zoomGroup.isPublicRoom()) {
            mMZoomGroup.setPublic(true);
        } else if (zoomGroup.isPrivateRoom()) {
            mMZoomGroup.setPrivate(true);
        }
        mMZoomGroup.setRoom(zoomGroup.isRoom());
        mMZoomGroup.setGroupId(zoomGroup.getGroupID());
        mMZoomGroup.setGroupName(zoomGroup.getGroupDisplayName(VideoBoxApplication.getGlobalContext()));
        mMZoomGroup.setMemberCount(zoomGroup.getBuddyCount());
        mMZoomGroup.setOwner(zoomGroup.getGroupOwner());
        mMZoomGroup.setMucType(zoomGroup.getMucType());
        mMZoomGroup.setE2E(zoomGroup.isForceE2EGroup());
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(mMZoomGroup.getOwner());
            if (buddyWithJID != null) {
                mMZoomGroup.setGroupOwnerName(buddyWithJID.getScreenName());
            }
            if (zoomGroup.getGroupAdmins() != null && zoomGroup.getGroupAdmins().size() > 0) {
                ZoomBuddy buddyWithJID2 = zoomMessenger.getBuddyWithJID((String) zoomGroup.getGroupAdmins().get(0));
                if (buddyWithJID2 != null) {
                    mMZoomGroup.setAdminScreenName(buddyWithJID2.getScreenName());
                }
            }
        }
        mMZoomGroup.sortKey = SortUtil.getSortKey(mMZoomGroup.getGroupName(), CompatUtils.getLocalDefault());
        return mMZoomGroup;
    }

    public void syncGroupWithSDK(@Nullable ZoomMessenger zoomMessenger) {
        if (zoomMessenger != null && !TextUtils.isEmpty(this.groupId)) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.groupId);
            if (groupById != null) {
                if (groupById.isPublicRoom()) {
                    setPublic(true);
                } else if (groupById.isPrivateRoom()) {
                    setPrivate(true);
                }
                setRoom(groupById.isRoom());
                setGroupId(groupById.getGroupID());
                setGroupName(groupById.getGroupDisplayName(VideoBoxApplication.getGlobalContext()));
                setMemberCount(groupById.getBuddyCount());
                setOwner(groupById.getGroupOwner());
                setMucType(groupById.getMucType());
                setE2E(groupById.isForceE2EGroup());
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(getOwner());
                if (buddyWithJID != null) {
                    setGroupOwnerName(buddyWithJID.getScreenName());
                }
                this.sortKey = SortUtil.getSortKey(getGroupName(), CompatUtils.getLocalDefault());
            }
        }
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public int getMemberCount() {
        return this.memberCount;
    }

    public void setMemberCount(int i) {
        this.memberCount = i;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public void setPublic(boolean z) {
        this.isPublic = z;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String str) {
        this.owner = str;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long j) {
        this.createTime = j;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String str) {
        this.groupId = str;
    }

    public String getGroupOwnerName() {
        return this.groupOwnerName;
    }

    public void setGroupOwnerName(String str) {
        this.groupOwnerName = str;
    }

    public String getAdminScreenName() {
        return this.adminScreenName;
    }

    public void setAdminScreenName(String str) {
        this.adminScreenName = str;
    }

    public int getMucType() {
        return this.mucType;
    }

    public void setMucType(int i) {
        this.mucType = i;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setPrivate(boolean z) {
        this.isPrivate = z;
    }

    public boolean isE2E() {
        return this.isE2E;
    }

    public void setE2E(boolean z) {
        this.isE2E = z;
    }

    public boolean isRoom() {
        return this.isRoom;
    }

    public void setRoom(boolean z) {
        this.isRoom = z;
    }

    public int getNotifyType() {
        return this.notifyType;
    }

    public void setNotifyType(int i) {
        this.notifyType = i;
    }

    public String getSortKey() {
        return this.sortKey;
    }

    public void setSortKey(String str) {
        this.sortKey = str;
    }
}
