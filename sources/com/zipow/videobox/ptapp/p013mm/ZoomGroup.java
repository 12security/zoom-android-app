package com.zipow.videobox.ptapp.p013mm;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.IMProtos.zGroupProperty;
import com.zipow.videobox.ptapp.PTApp;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomGroup */
public class ZoomGroup {
    private static final String TAG = "ZoomGroup";
    private long mNativeHandle = 0;

    private native boolean amIGroupAdminImpl(long j);

    private native boolean amIGroupOwnerImpl(long j);

    private native boolean amIInGroupImpl(long j);

    private native long getBuddyAtImpl(long j, int i);

    private native int getBuddyCountImpl(long j);

    @Nullable
    private native List<String> getE2EOnLineMembersImpl(long j);

    @Nullable
    private native List<String> getGroupAdminsImpl(long j);

    @Nullable
    private native String getGroupDescImpl(long j);

    @Nullable
    private native String getGroupIDImpl(long j);

    @Nullable
    private native String getGroupNameImpl(long j);

    @Nullable
    private native String getGroupOwnerImpl(long j);

    @Nullable
    private native byte[] getGroupPropertyImpl(long j);

    private native int getMucTypeImpl(long j);

    private native boolean isBroadcastImpl(long j);

    private native boolean isForceE2EGroupImpl(long j);

    private native boolean isGroupInfoReadyImpl(long j);

    private native boolean isNewMUCImpl(long j);

    private native boolean isPrivateRoomImpl(long j);

    private native boolean isPublicRoomImpl(long j);

    private native boolean isRoomImpl(long j);

    private native boolean setBroadcastNameImpl(long j, String str);

    public ZoomGroup(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getGroupID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getGroupIDImpl(j);
    }

    @Nullable
    public String getGroupName() {
        if (this.mNativeHandle == 0) {
            return null;
        }
        if (!isBroadcast()) {
            return getGroupNameImpl(this.mNativeHandle);
        }
        String string = VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_msg_announcements_108966);
        if (!string.equals(getGroupNameImpl(this.mNativeHandle))) {
            setBroadcastName(string);
        }
        return string;
    }

    @Nullable
    public String getGroupDesc() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getGroupDescImpl(j);
    }

    @Nullable
    public static String createDefaultMUCName(@NonNull List<String> list) {
        if (CollectionsUtil.isListEmpty(list)) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(VideoBoxApplication.getInstance().getString(C4558R.string.zm_mm_group_names_list_comma));
        sb2.append(OAuth.SCOPE_DELIMITER);
        String sb3 = sb2.toString();
        ArrayList arrayList = new ArrayList();
        for (String buddyWithJID : list) {
            ZoomBuddy buddyWithJID2 = zoomMessenger.getBuddyWithJID(buddyWithJID);
            if (buddyWithJID2 != null) {
                String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID2, null, false);
                if (!StringUtil.isEmptyOrNull(buddyDisplayName)) {
                    arrayList.add(buddyDisplayName);
                }
            }
        }
        if (arrayList.size() > 3) {
            for (int i = 0; i < 2; i++) {
                sb.append((String) arrayList.get(i));
                sb.append(sb3);
            }
        } else {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                sb.append((String) arrayList.get(i2));
                sb.append(sb3);
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - sb3.length());
            sb.trimToSize();
        }
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (arrayList.size() <= 3 || instance == null) {
            return sb.toString();
        }
        return instance.getString(C4558R.string.zm_lbl_empty_group_name_greater_3, new Object[]{sb.toString()});
    }

    @Nullable
    public String getGroupDisplayName(Context context) {
        String groupName = getGroupName();
        if (!StringUtil.isEmptyOrNull(groupName)) {
            return groupName;
        }
        String groupID = getGroupID();
        if (groupID == null) {
            return "";
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return "";
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(groupID);
        if (groupById == null) {
            return "";
        }
        int buddyCount = groupById.getBuddyCount();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < buddyCount; i++) {
            ZoomBuddy buddyAt = groupById.getBuddyAt(i);
            if (buddyAt != null) {
                arrayList.add(buddyAt.getJid());
            }
        }
        return createDefaultMUCName(arrayList);
    }

    @Nullable
    public String getGroupOwner() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getGroupOwnerImpl(j);
    }

    public int getBuddyCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getBuddyCountImpl(j);
    }

    @Nullable
    public ZoomBuddy getBuddyAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0 || i < 0) {
            return null;
        }
        long buddyAtImpl = getBuddyAtImpl(j, i);
        if (buddyAtImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyAtImpl);
    }

    public boolean amIInGroup() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return amIInGroupImpl(j);
    }

    public boolean isForceE2EGroup() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isForceE2EGroupImpl(j);
    }

    @Nullable
    public static String getGroupAvatarPath(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        String sessionDataFolder = zoomMessenger.getSessionDataFolder(str);
        if (StringUtil.isEmptyOrNull(sessionDataFolder)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sessionDataFolder);
        sb.append("/avatar.png");
        return sb.toString();
    }

    public boolean isPublicRoom() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPublicRoomImpl(j);
    }

    public boolean isPrivateRoom() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPrivateRoomImpl(j);
    }

    public boolean isNewMUC() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isNewMUCImpl(j);
    }

    public int getMucType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return getMucTypeImpl(j);
    }

    public boolean isRoom() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isRoomImpl(j);
    }

    public boolean isGroupInfoReady() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isGroupInfoReadyImpl(j);
    }

    @Nullable
    public List<String> getGroupAdmins() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getGroupAdminsImpl(j);
    }

    public boolean amIGroupAdmin() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return amIGroupAdminImpl(j);
    }

    public boolean setBroadcastName(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setBroadcastNameImpl(j, str);
    }

    public boolean amIGroupOwner() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return amIGroupOwnerImpl(j);
    }

    public boolean isGroupOperatorable() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        if (amIGroupAdmin()) {
            return true;
        }
        List groupAdmins = getGroupAdmins();
        if (groupAdmins == null || groupAdmins.size() == 0) {
            return amIGroupOwner();
        }
        return false;
    }

    @Nullable
    public List<String> getE2EOnLineMembers() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getE2EOnLineMembersImpl(j);
    }

    @Nullable
    public zGroupProperty getGroupProperty() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] groupPropertyImpl = getGroupPropertyImpl(j);
        if (groupPropertyImpl == null) {
            return null;
        }
        try {
            return zGroupProperty.parseFrom(groupPropertyImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean isBroadcast() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isBroadcastImpl(j);
    }
}
