package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.MMZoomBuddyGroup */
public class MMZoomBuddyGroup implements Serializable {
    public static final int MAX_BUDDY_COUNT_IN_CUSTOM_GROUP = 256;
    public static final int MAX_COUNT_CUSTOM_GROUP = 128;
    private int buddyCount;
    private boolean canEdit;
    @Nullable

    /* renamed from: id */
    private String f317id;
    private boolean isDirectoryGroup;
    private boolean isZoomRoomGroup;
    @Nullable
    private String name;
    private String sortKey = "";
    private int type;
    @Nullable
    private String xmppGroupID;

    @NonNull
    public static MMZoomBuddyGroup fromZoomBuddyGroup(@NonNull ZoomBuddyGroup zoomBuddyGroup) {
        MMZoomBuddyGroup mMZoomBuddyGroup = new MMZoomBuddyGroup();
        mMZoomBuddyGroup.canEdit = zoomBuddyGroup.canEdit();
        mMZoomBuddyGroup.buddyCount = zoomBuddyGroup.getBuddyCount();
        mMZoomBuddyGroup.f317id = zoomBuddyGroup.getID();
        mMZoomBuddyGroup.name = zoomBuddyGroup.getName();
        mMZoomBuddyGroup.type = zoomBuddyGroup.getGroupType();
        mMZoomBuddyGroup.xmppGroupID = zoomBuddyGroup.getXmppGroupID();
        mMZoomBuddyGroup.isDirectoryGroup = zoomBuddyGroup.isDirectoryGroup();
        mMZoomBuddyGroup.isZoomRoomGroup = zoomBuddyGroup.isZoomRoomGroup();
        mMZoomBuddyGroup.sortKey = SortUtil.getSortKey(mMZoomBuddyGroup.name, CompatUtils.getLocalDefault());
        return mMZoomBuddyGroup;
    }

    public boolean isInCompanyContacts() {
        switch (this.type) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    @Nullable
    public String getId() {
        return this.f317id;
    }

    public void setId(@Nullable String str) {
        this.f317id = str;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public void setName(@Nullable String str) {
        this.name = str;
    }

    public int getBuddyCount() {
        return this.buddyCount;
    }

    public void setBuddyCount(int i) {
        this.buddyCount = i;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public boolean isCanEdit() {
        return this.canEdit;
    }

    public void setCanEdit(boolean z) {
        this.canEdit = z;
    }

    @Nullable
    public String getXmppGroupID() {
        return this.xmppGroupID;
    }

    public void setXmppGroupID(@Nullable String str) {
        this.xmppGroupID = str;
    }

    public boolean isDirectoryGroup() {
        return this.isDirectoryGroup;
    }

    public void setDirectoryGroup(boolean z) {
        this.isDirectoryGroup = z;
    }

    public boolean isZoomRoomGroup() {
        return this.isZoomRoomGroup;
    }

    public void setZoomRoomGroup(boolean z) {
        this.isZoomRoomGroup = z;
    }

    public String getSortKey() {
        return this.sortKey;
    }
}
