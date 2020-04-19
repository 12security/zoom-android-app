package com.zipow.videobox.ptapp.p013mm;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import java.io.Serializable;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.ptapp.mm.GroupAction */
public class GroupAction implements Serializable {
    public static final int ACTION_ADD_BUDDIES = 3;
    public static final int ACTION_DELETE_GROUP = 2;
    public static final int ACTION_MAKE_GROUP = 0;
    public static final int ACTION_MODIFY_NAME = 1;
    public static final int ACTION_MODIFY_OPTION = 6;
    public static final int ACTION_NEW_ADMIN = 7;
    public static final int ACTION_QUIT_GROUP = 5;
    public static final int ACTION_REMOVE_BUDDY = 4;
    private static final String TAG = "GroupAction";
    private static final long serialVersionUID = 1;
    @Nullable
    private String actionOwner;
    private String actionOwnerId;
    private int actionType;
    private String[] buddies;
    private int buddyNotAllowReason = 0;
    private String groupDesc;
    private int groupDescAction;
    private String groupId;
    private boolean isActionOwnerMe = false;
    private boolean isChannel = false;
    private boolean isMeInBuddies = false;
    private int maxAllowed;
    private int mucFlag;
    private String newGroupName;
    private List<String> notAllowBuddies;
    private String reqId;
    private long time;

    public GroupAction(int i, @Nullable String str, String[] strArr, boolean z, boolean z2, String str2) {
        this.actionType = i;
        this.actionOwner = str;
        this.buddies = strArr;
        this.isActionOwnerMe = z;
        this.isMeInBuddies = z2;
        this.newGroupName = str2;
    }

    public boolean isChannel() {
        return this.isChannel;
    }

    public void setChannel(boolean z) {
        this.isChannel = z;
    }

    public int getActionType() {
        return this.actionType;
    }

    public void setActionType(int i) {
        this.actionType = i;
    }

    @Nullable
    public String getActionOwner() {
        return this.actionOwner;
    }

    public void setActionOwner(@Nullable String str) {
        this.actionOwner = str;
    }

    public String[] getBuddies() {
        return this.buddies;
    }

    public void setBuddies(String[] strArr) {
        this.buddies = strArr;
    }

    public boolean isActionOwnerMe() {
        return this.isActionOwnerMe;
    }

    public void setIsActionOwnerMe(boolean z) {
        this.isActionOwnerMe = z;
    }

    public boolean isMeInBuddies() {
        return this.isMeInBuddies;
    }

    public void setIsMeInBuddies(boolean z) {
        this.isMeInBuddies = z;
    }

    public String getNewGroupName() {
        return this.newGroupName;
    }

    public void setNewGroupName(String str) {
        this.newGroupName = str;
    }

    public String getActionOwnerId() {
        return this.actionOwnerId;
    }

    public void setActionOwnerId(String str) {
        this.actionOwnerId = str;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String str) {
        this.groupId = str;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public void setGroupDesc(String str) {
        this.groupDesc = str;
    }

    public String getGroupDesc() {
        return this.groupDesc;
    }

    public void setGroupDescAction(int i) {
        this.groupDescAction = i;
    }

    public int getGroupDescAction() {
        return this.groupDescAction;
    }

    @Nullable
    public String toMessage(@Nullable Context context) {
        String str = null;
        if (context == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        if (StringUtil.isEmptyOrNull(this.actionOwner) && !StringUtil.isEmptyOrNull(this.actionOwnerId)) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.actionOwnerId);
            if (buddyWithJID != null) {
                this.actionOwner = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null, false);
            }
        }
        switch (this.actionType) {
            case 0:
                str = buildMakeGroupMessage(context);
                break;
            case 1:
                str = buildModifyNameMessage(context);
                break;
            case 2:
            case 5:
                str = buildQuitGroupMessage(context);
                break;
            case 3:
                str = buildAddBuddiesMessage(context);
                break;
            case 4:
                str = buildRemoveBuddyMessage(context);
                break;
            case 6:
                str = buildModifyOptionMessage(context);
                break;
            case 7:
                str = buildNewAdminMessage(context);
                break;
        }
        return str;
    }

    private String buildMakeGroupMessage(@NonNull Context context) {
        String str = null;
        if (this.isActionOwnerMe) {
            String[] strArr = this.buddies;
            if (strArr == null || strArr.length <= 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger == null) {
                    return null;
                }
                ZoomGroup groupById = zoomMessenger.getGroupById(getGroupId());
                if (groupById == null) {
                    return null;
                }
                return context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_make_group_exception_59554 : C4558R.string.zm_mm_group_action_make_muc_exception_61520, new Object[]{groupById.getGroupDisplayName(context)});
            } else if (strArr.length != 1) {
                String string = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                String str2 = "";
                int i = 0;
                while (true) {
                    String[] strArr2 = this.buddies;
                    if (i < strArr2.length - 1) {
                        String str3 = strArr2[i];
                        if (str3 == null) {
                            return null;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(str3);
                        str2 = sb.toString();
                        String[] strArr3 = this.buddies;
                        if (strArr3.length > 2 && i < strArr3.length - 2) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str2);
                            sb2.append(string);
                            sb2.append(OAuth.SCOPE_DELIMITER);
                            str2 = sb2.toString();
                        }
                        i++;
                    } else {
                        String str4 = strArr2[strArr2.length - 1];
                        if (str4 == null) {
                            return null;
                        }
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_owner_is_me_59554 : C4558R.string.zm_mm_group_action_add_buddies_owner_is_me_muc_61520, new Object[]{context.getString(C4558R.string.zm_mm_group_names_list_and, new Object[]{str2, str4})});
                    }
                }
            } else if (strArr[0] != null) {
                str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_owner_is_me_59554 : C4558R.string.zm_mm_group_action_add_buddies_owner_is_me_muc_61520, new Object[]{this.buddies[0]});
            }
        } else {
            String[] strArr4 = this.buddies;
            if (strArr4 == null || strArr4.length <= 0) {
                if (!this.isMeInBuddies) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, ""});
                } else if (this.actionOwner != null) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_buddies_is_me_59554 : C4558R.string.zm_mm_group_action_add_buddies_buddies_is_me_muc_61520, new Object[]{this.actionOwner});
                }
            } else if (strArr4.length == 1) {
                if (this.isMeInBuddies) {
                    if (!(this.actionOwner == null || strArr4[0] == null)) {
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_you_and_xxx, new Object[]{this.buddies[0]})});
                    }
                } else if (!(this.actionOwner == null || strArr4[0] == null)) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, this.buddies[0]});
                }
            } else if (!this.isMeInBuddies) {
                String string2 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                String str5 = "";
                int i2 = 0;
                while (true) {
                    String[] strArr5 = this.buddies;
                    if (i2 < strArr5.length - 1) {
                        String str6 = strArr5[i2];
                        if (str6 == null) {
                            return null;
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str5);
                        sb3.append(str6);
                        str5 = sb3.toString();
                        String[] strArr6 = this.buddies;
                        if (strArr6.length > 2 && i2 < strArr6.length - 2) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str5);
                            sb4.append(string2);
                            sb4.append(OAuth.SCOPE_DELIMITER);
                            str5 = sb4.toString();
                        }
                        i2++;
                    } else {
                        String str7 = strArr5[strArr5.length - 1];
                        if (str7 == null) {
                            return null;
                        }
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_and, new Object[]{str5, str7})});
                    }
                }
            } else {
                String string3 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                String str8 = "";
                int i3 = 0;
                while (true) {
                    String[] strArr7 = this.buddies;
                    if (i3 < strArr7.length - 1) {
                        String str9 = strArr7[i3];
                        if (str9 == null) {
                            return null;
                        }
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str8);
                        sb5.append(str9);
                        str8 = sb5.toString();
                        String[] strArr8 = this.buddies;
                        if (strArr8.length > 2 && i3 < strArr8.length - 2) {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str8);
                            sb6.append(string3);
                            sb6.append(OAuth.SCOPE_DELIMITER);
                            str8 = sb6.toString();
                        }
                        i3++;
                    } else {
                        String str10 = strArr7[strArr7.length - 1];
                        if (str10 == null) {
                            return null;
                        }
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_you_xxx_and_xxx, new Object[]{str8, str10})});
                    }
                }
            }
        }
        if (getBuddyNotAllowReason() == 1) {
            List notAllowBuddies2 = getNotAllowBuddies();
            if (!CollectionsUtil.isListEmpty(notAllowBuddies2)) {
                String string4 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                StringBuilder sb7 = new StringBuilder();
                for (int i4 = 0; i4 < notAllowBuddies2.size(); i4++) {
                    String str11 = (String) notAllowBuddies2.get(i4);
                    if (str11 != null) {
                        if (i4 == notAllowBuddies2.size() - 1) {
                            sb7.append(str11);
                        } else {
                            sb7.append(string4);
                            sb7.append(OAuth.SCOPE_DELIMITER);
                        }
                    }
                }
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str);
                sb8.append(context.getString(C4558R.string.zm_mm_information_barries_invite_channel_115072, new Object[]{sb7.toString()}));
                str = sb8.toString();
            }
        }
        return str;
    }

    @Nullable
    private String buildModifyNameMessage(@NonNull Context context) {
        if (this.isActionOwnerMe) {
            if (this.newGroupName != null) {
                return context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_modify_group_name_you_59554 : C4558R.string.zm_mm_group_action_modify_muc_name_you_61520, new Object[]{this.newGroupName});
            }
        } else if (!(this.newGroupName == null || this.actionOwner == null)) {
            return context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_modify_group_name_other_59554 : C4558R.string.zm_mm_group_action_modify_muc_name_other_61520, new Object[]{this.actionOwner, this.newGroupName});
        }
        return null;
    }

    @Nullable
    private String buildQuitGroupMessage(@NonNull Context context) {
        if (this.isActionOwnerMe) {
            return context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_quit_group_you_59554 : C4558R.string.zm_mm_group_action_quit_muc_you_61520);
        } else if (this.actionOwner == null) {
            return null;
        } else {
            return context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_quit_group_other_59554 : C4558R.string.zm_mm_group_action_quit_muc_other_61520, new Object[]{this.actionOwner});
        }
    }

    private String buildNewAdminMessage(@NonNull Context context) {
        if (this.buddies == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        String[] strArr = this.buddies;
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return null;
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.buddies[0]);
        if (buddyWithJID == null) {
            return null;
        }
        if (this.isActionOwnerMe) {
            return context.getString(this.isChannel ? C4558R.string.zm_mm_lbl_transfer_admin_other_131024 : C4558R.string.zm_mm_lbl_transfer_admin_other_muc_131024, new Object[]{BuddyNameUtil.getBuddyDisplayName(myself, null), BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null)});
        } else if (this.isMeInBuddies) {
            return context.getString(this.isChannel ? C4558R.string.zm_mm_lbl_transfer_admin_other_131024 : C4558R.string.zm_mm_lbl_transfer_admin_other_muc_131024, new Object[]{this.actionOwner, BuddyNameUtil.getBuddyDisplayName(myself, null)});
        } else {
            return context.getString(this.isChannel ? C4558R.string.zm_mm_lbl_transfer_admin_other_131024 : C4558R.string.zm_mm_lbl_transfer_admin_other_muc_131024, new Object[]{this.actionOwner, BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null)});
        }
    }

    @Nullable
    private String buildModifyOptionMessage(@NonNull Context context) {
        if (this.isActionOwnerMe) {
            return context.getString(C4558R.string.zm_mm_group_action_modify_group_option_you_59554);
        }
        if (this.actionOwner == null) {
            return null;
        }
        return context.getString(C4558R.string.zm_mm_group_action_modify_group_option_other_59554, new Object[]{this.actionOwner});
    }

    private String buildAddBuddiesMessage(@NonNull Context context) {
        String str = null;
        if (this.isActionOwnerMe) {
            String[] strArr = this.buddies;
            if (strArr != null && strArr.length > 0) {
                if (strArr.length != 1) {
                    String string = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                    String str2 = "";
                    int i = 0;
                    while (true) {
                        String[] strArr2 = this.buddies;
                        if (i < strArr2.length - 1) {
                            String str3 = strArr2[i];
                            if (str3 == null) {
                                return null;
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append(str2);
                            sb.append(str3);
                            str2 = sb.toString();
                            String[] strArr3 = this.buddies;
                            if (strArr3.length > 2 && i < strArr3.length - 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str2);
                                sb2.append(string);
                                sb2.append(OAuth.SCOPE_DELIMITER);
                                str2 = sb2.toString();
                            }
                            i++;
                        } else {
                            String str4 = strArr2[strArr2.length - 1];
                            if (str4 == null) {
                                return null;
                            }
                            str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_owner_is_me_59554 : C4558R.string.zm_mm_group_action_add_buddies_owner_is_me_muc_61520, new Object[]{context.getString(C4558R.string.zm_mm_group_names_list_and, new Object[]{str2, str4})});
                        }
                    }
                } else if (strArr[0] != null) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_owner_is_me_59554 : C4558R.string.zm_mm_group_action_add_buddies_owner_is_me_muc_61520, new Object[]{this.buddies[0]});
                }
            }
        } else {
            String[] strArr4 = this.buddies;
            if (strArr4 == null || strArr4.length <= 0) {
                if (!this.isMeInBuddies) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, ""});
                } else if (this.actionOwner != null) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_buddies_is_me_59554 : C4558R.string.zm_mm_group_action_add_buddies_buddies_is_me_muc_61520, new Object[]{this.actionOwner});
                }
            } else if (strArr4.length == 1) {
                if (!this.isMeInBuddies) {
                    String str5 = this.actionOwner;
                    if (!(str5 == null || strArr4[0] == null)) {
                        if (str5.equals(strArr4[0])) {
                            str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_self_add_self_59554 : C4558R.string.zm_mm_group_action_self_add_self_muc_61520, new Object[]{this.actionOwner});
                        } else {
                            str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, this.buddies[0]});
                        }
                    }
                } else if (!(this.actionOwner == null || strArr4[0] == null)) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_you_and_xxx, new Object[]{this.buddies[0]})});
                }
            } else if (!this.isMeInBuddies) {
                String string2 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                String str6 = "";
                int i2 = 0;
                while (true) {
                    String[] strArr5 = this.buddies;
                    if (i2 < strArr5.length - 1) {
                        String str7 = strArr5[i2];
                        if (str7 == null) {
                            return null;
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str6);
                        sb3.append(str7);
                        str6 = sb3.toString();
                        String[] strArr6 = this.buddies;
                        if (strArr6.length > 2 && i2 < strArr6.length - 2) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str6);
                            sb4.append(string2);
                            sb4.append(OAuth.SCOPE_DELIMITER);
                            str6 = sb4.toString();
                        }
                        i2++;
                    } else {
                        String str8 = strArr5[strArr5.length - 1];
                        if (str8 == null) {
                            return null;
                        }
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_and, new Object[]{str6, str8})});
                    }
                }
            } else {
                String string3 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                String str9 = "";
                int i3 = 0;
                while (true) {
                    String[] strArr7 = this.buddies;
                    if (i3 < strArr7.length - 1) {
                        String str10 = strArr7[i3];
                        if (str10 == null) {
                            return null;
                        }
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str9);
                        sb5.append(str10);
                        str9 = sb5.toString();
                        String[] strArr8 = this.buddies;
                        if (strArr8.length > 2 && i3 < strArr8.length - 2) {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str9);
                            sb6.append(string3);
                            sb6.append(OAuth.SCOPE_DELIMITER);
                            str9 = sb6.toString();
                        }
                        i3++;
                    } else {
                        String str11 = strArr7[strArr7.length - 1];
                        if (str11 == null) {
                            return null;
                        }
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_add_buddies_59554 : C4558R.string.zm_mm_group_action_add_buddies_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_you_xxx_and_xxx, new Object[]{str9, str11})});
                    }
                }
            }
        }
        if (getBuddyNotAllowReason() == 1) {
            List notAllowBuddies2 = getNotAllowBuddies();
            if (!CollectionsUtil.isListEmpty(notAllowBuddies2)) {
                String string4 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                StringBuilder sb7 = new StringBuilder();
                for (int i4 = 0; i4 < notAllowBuddies2.size(); i4++) {
                    String str12 = (String) notAllowBuddies2.get(i4);
                    if (str12 != null) {
                        if (i4 == notAllowBuddies2.size() - 1) {
                            sb7.append(str12);
                        } else {
                            sb7.append(string4);
                            sb7.append(OAuth.SCOPE_DELIMITER);
                        }
                    }
                }
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str);
                sb8.append(context.getString(C4558R.string.zm_mm_information_barries_invite_channel_115072, new Object[]{sb7.toString()}));
                str = sb8.toString();
            }
        }
        return str;
    }

    private String buildRemoveBuddyMessage(@NonNull Context context) {
        String str = null;
        if (getBuddyNotAllowReason() == 1) {
            StringBuffer stringBuffer = new StringBuffer();
            String string = context.getString(C4558R.string.zm_mm_group_names_list_comma);
            String[] strArr = this.buddies;
            if (strArr != null && strArr.length > 0) {
                int i = 0;
                while (true) {
                    String[] strArr2 = this.buddies;
                    if (i >= strArr2.length) {
                        break;
                    }
                    if (i == 0) {
                        stringBuffer.append(strArr2[i]);
                    } else {
                        stringBuffer.append(string);
                        stringBuffer.append(OAuth.SCOPE_DELIMITER);
                        stringBuffer.append(this.buddies[i]);
                    }
                    i++;
                }
                str = context.getString(this.isChannel ? C4558R.string.zm_mm_information_barries_remove_channel_115072 : C4558R.string.zm_mm_information_barries_remove_group_chat_115072, new Object[]{stringBuffer.toString()});
            }
            return str;
        }
        if (this.isActionOwnerMe) {
            String[] strArr3 = this.buddies;
            if (strArr3 != null && strArr3.length > 0) {
                if (strArr3.length != 1) {
                    String string2 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                    String str2 = "";
                    int i2 = 0;
                    while (true) {
                        String[] strArr4 = this.buddies;
                        if (i2 < strArr4.length - 1) {
                            String str3 = strArr4[i2];
                            if (str3 == null) {
                                return null;
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append(str2);
                            sb.append(str3);
                            str2 = sb.toString();
                            String[] strArr5 = this.buddies;
                            if (strArr5.length > 2 && i2 < strArr5.length - 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str2);
                                sb2.append(string2);
                                sb2.append(OAuth.SCOPE_DELIMITER);
                                str2 = sb2.toString();
                            }
                            i2++;
                        } else {
                            String str4 = strArr4[strArr4.length - 1];
                            if (str4 == null) {
                                return null;
                            }
                            str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_remove_buddy_by_you_59554 : C4558R.string.zm_mm_group_action_remove_buddy_by_you_muc_61520, new Object[]{context.getString(C4558R.string.zm_mm_group_names_list_and, new Object[]{str2, str4})});
                        }
                    }
                } else if (strArr3[0] != null) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_remove_buddy_by_you_59554 : C4558R.string.zm_mm_group_action_remove_buddy_by_you_muc_61520, new Object[]{this.buddies[0]});
                }
            }
        } else {
            String[] strArr6 = this.buddies;
            if (strArr6 == null || strArr6.length <= 0) {
                if (!this.isMeInBuddies) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_remove_buddy_59554 : C4558R.string.zm_mm_group_action_remove_buddy_muc_61520, new Object[]{this.actionOwner, ""});
                } else if (this.actionOwner != null) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_remove_buddy_remove_you_59554 : C4558R.string.zm_mm_group_action_remove_buddy_remove_you_muc_61520, new Object[]{this.actionOwner});
                }
            } else if (strArr6.length == 1) {
                if (this.isMeInBuddies) {
                    if (!(this.actionOwner == null || strArr6[0] == null)) {
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_remove_buddy_59554 : C4558R.string.zm_mm_group_action_remove_buddy_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_you_and_xxx, new Object[]{this.buddies[0]})});
                    }
                } else if (!(this.actionOwner == null || strArr6[0] == null)) {
                    str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_remove_buddy_59554 : C4558R.string.zm_mm_group_action_remove_buddy_muc_61520, new Object[]{this.actionOwner, this.buddies[0]});
                }
            } else if (!this.isMeInBuddies) {
                String string3 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                String str5 = "";
                int i3 = 0;
                while (true) {
                    String[] strArr7 = this.buddies;
                    if (i3 < strArr7.length - 1) {
                        String str6 = strArr7[i3];
                        if (str6 == null) {
                            return null;
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str5);
                        sb3.append(str6);
                        str5 = sb3.toString();
                        String[] strArr8 = this.buddies;
                        if (strArr8.length > 2 && i3 < strArr8.length - 2) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str5);
                            sb4.append(string3);
                            sb4.append(OAuth.SCOPE_DELIMITER);
                            str5 = sb4.toString();
                        }
                        i3++;
                    } else {
                        String str7 = strArr7[strArr7.length - 1];
                        if (str7 == null) {
                            return null;
                        }
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_remove_buddy_59554 : C4558R.string.zm_mm_group_action_remove_buddy_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_and, new Object[]{str5, str7})});
                    }
                }
            } else {
                String string4 = context.getString(C4558R.string.zm_mm_group_names_list_comma);
                String str8 = "";
                int i4 = 0;
                while (true) {
                    String[] strArr9 = this.buddies;
                    if (i4 < strArr9.length - 1) {
                        String str9 = strArr9[i4];
                        if (str9 == null) {
                            return null;
                        }
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str8);
                        sb5.append(str9);
                        str8 = sb5.toString();
                        String[] strArr10 = this.buddies;
                        if (strArr10.length > 2 && i4 < strArr10.length - 2) {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str8);
                            sb6.append(string4);
                            sb6.append(OAuth.SCOPE_DELIMITER);
                            str8 = sb6.toString();
                        }
                        i4++;
                    } else {
                        String str10 = strArr9[strArr9.length - 1];
                        if (str10 == null) {
                            return null;
                        }
                        str = context.getString(this.isChannel ? C4558R.string.zm_mm_group_action_remove_buddy_59554 : C4558R.string.zm_mm_group_action_remove_buddy_muc_61520, new Object[]{this.actionOwner, context.getString(C4558R.string.zm_mm_group_names_list_you_xxx_and_xxx, new Object[]{str8, str10})});
                    }
                }
            }
        }
        return str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x003c, code lost:
        r5 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x003d, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0041, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0042, code lost:
        r4 = r2;
        r2 = r5;
        r5 = r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x003c A[ExcHandler: all (th java.lang.Throwable), Splitter:B:5:0x0009] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String serializeToString(@androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.GroupAction r5) {
        /*
            r0 = 0
            if (r5 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0054 }
            r1.<init>()     // Catch:{ IOException -> 0x0054 }
            java.io.ObjectOutputStream r2 = new java.io.ObjectOutputStream     // Catch:{ Throwable -> 0x003f, all -> 0x003c }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x003f, all -> 0x003c }
            r2.writeObject(r5)     // Catch:{ Throwable -> 0x0027, all -> 0x0024 }
            r2.close()     // Catch:{ Throwable -> 0x0027, all -> 0x0024 }
            byte[] r5 = r1.toByteArray()     // Catch:{ Throwable -> 0x0027, all -> 0x0024 }
            r3 = 0
            java.lang.String r5 = android.util.Base64.encodeToString(r5, r3)     // Catch:{ Throwable -> 0x0027, all -> 0x0024 }
            r2.close()     // Catch:{ Throwable -> 0x003f, all -> 0x003c }
            r1.close()     // Catch:{ IOException -> 0x0054 }
            return r5
        L_0x0024:
            r5 = move-exception
            r3 = r0
            goto L_0x002d
        L_0x0027:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0029 }
        L_0x0029:
            r3 = move-exception
            r4 = r3
            r3 = r5
            r5 = r4
        L_0x002d:
            if (r3 == 0) goto L_0x0038
            r2.close()     // Catch:{ Throwable -> 0x0033, all -> 0x003c }
            goto L_0x003b
        L_0x0033:
            r2 = move-exception
            r3.addSuppressed(r2)     // Catch:{ Throwable -> 0x003f, all -> 0x003c }
            goto L_0x003b
        L_0x0038:
            r2.close()     // Catch:{ Throwable -> 0x003f, all -> 0x003c }
        L_0x003b:
            throw r5     // Catch:{ Throwable -> 0x003f, all -> 0x003c }
        L_0x003c:
            r5 = move-exception
            r2 = r0
            goto L_0x0045
        L_0x003f:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0041 }
        L_0x0041:
            r2 = move-exception
            r4 = r2
            r2 = r5
            r5 = r4
        L_0x0045:
            if (r2 == 0) goto L_0x0050
            r1.close()     // Catch:{ Throwable -> 0x004b }
            goto L_0x0053
        L_0x004b:
            r1 = move-exception
            r2.addSuppressed(r1)     // Catch:{ IOException -> 0x0054 }
            goto L_0x0053
        L_0x0050:
            r1.close()     // Catch:{ IOException -> 0x0054 }
        L_0x0053:
            throw r5     // Catch:{ IOException -> 0x0054 }
        L_0x0054:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.p013mm.GroupAction.serializeToString(com.zipow.videobox.ptapp.mm.GroupAction):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0038, code lost:
        r5 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0039, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x003d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x003e, code lost:
        r4 = r2;
        r2 = r5;
        r5 = r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0038 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:6:0x000e] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.ptapp.p013mm.GroupAction loadFromString(@androidx.annotation.Nullable java.lang.String r5) {
        /*
            r0 = 0
            if (r5 != 0) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            byte[] r5 = android.util.Base64.decode(r5, r1)
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0050 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0050 }
            com.zipow.videobox.util.safe.SafeObjectInputStream r5 = new com.zipow.videobox.util.safe.SafeObjectInputStream     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
            r5.<init>(r1)     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
            java.lang.Object r2 = r5.readObject()     // Catch:{ Throwable -> 0x0023, all -> 0x0020 }
            com.zipow.videobox.ptapp.mm.GroupAction r2 = (com.zipow.videobox.ptapp.p013mm.GroupAction) r2     // Catch:{ Throwable -> 0x0023, all -> 0x0020 }
            r5.close()     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
            r1.close()     // Catch:{ Exception -> 0x0050 }
            return r2
        L_0x0020:
            r2 = move-exception
            r3 = r0
            goto L_0x0029
        L_0x0023:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0025 }
        L_0x0025:
            r3 = move-exception
            r4 = r3
            r3 = r2
            r2 = r4
        L_0x0029:
            if (r3 == 0) goto L_0x0034
            r5.close()     // Catch:{ Throwable -> 0x002f, all -> 0x0038 }
            goto L_0x0037
        L_0x002f:
            r5 = move-exception
            r3.addSuppressed(r5)     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
            goto L_0x0037
        L_0x0034:
            r5.close()     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
        L_0x0037:
            throw r2     // Catch:{ Throwable -> 0x003b, all -> 0x0038 }
        L_0x0038:
            r5 = move-exception
            r2 = r0
            goto L_0x0041
        L_0x003b:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x003d }
        L_0x003d:
            r2 = move-exception
            r4 = r2
            r2 = r5
            r5 = r4
        L_0x0041:
            if (r2 == 0) goto L_0x004c
            r1.close()     // Catch:{ Throwable -> 0x0047 }
            goto L_0x004f
        L_0x0047:
            r1 = move-exception
            r2.addSuppressed(r1)     // Catch:{ Exception -> 0x0050 }
            goto L_0x004f
        L_0x004c:
            r1.close()     // Catch:{ Exception -> 0x0050 }
        L_0x004f:
            throw r5     // Catch:{ Exception -> 0x0050 }
        L_0x0050:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.p013mm.GroupAction.loadFromString(java.lang.String):com.zipow.videobox.ptapp.mm.GroupAction");
    }

    public List<String> getNotAllowBuddies() {
        return this.notAllowBuddies;
    }

    public void setNotAllowBuddies(List<String> list) {
        this.notAllowBuddies = list;
    }

    public int getMucFlag() {
        return this.mucFlag;
    }

    public void setMucFlag(int i) {
        this.mucFlag = i;
    }

    public String getReqId() {
        return this.reqId;
    }

    public void setReqId(String str) {
        this.reqId = str;
    }

    public int getMaxAllowed() {
        return this.maxAllowed;
    }

    public void setMaxAllowed(int i) {
        this.maxAllowed = i;
    }

    public int getBuddyNotAllowReason() {
        return this.buddyNotAllowReason;
    }

    public void setBuddyNotAllowReason(int i) {
        this.buddyNotAllowReason = i;
    }
}
