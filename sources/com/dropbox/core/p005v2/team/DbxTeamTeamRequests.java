package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.LocalizedText;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.p005v2.async.LaunchEmptyResult;
import com.dropbox.core.p005v2.async.LaunchEmptyResult.Serializer;
import com.dropbox.core.p005v2.async.PollArg;
import com.dropbox.core.p005v2.async.PollEmptyResult;
import com.dropbox.core.p005v2.async.PollError;
import com.dropbox.core.p005v2.async.PollErrorException;
import com.dropbox.core.p005v2.fileproperties.AddTemplateArg;
import com.dropbox.core.p005v2.fileproperties.AddTemplateResult;
import com.dropbox.core.p005v2.fileproperties.GetTemplateArg;
import com.dropbox.core.p005v2.fileproperties.GetTemplateResult;
import com.dropbox.core.p005v2.fileproperties.ListTemplateResult;
import com.dropbox.core.p005v2.fileproperties.ModifyTemplateError;
import com.dropbox.core.p005v2.fileproperties.ModifyTemplateErrorException;
import com.dropbox.core.p005v2.fileproperties.PropertyFieldTemplate;
import com.dropbox.core.p005v2.fileproperties.TemplateError;
import com.dropbox.core.p005v2.fileproperties.TemplateErrorException;
import com.dropbox.core.p005v2.fileproperties.UpdateTemplateArg;
import com.dropbox.core.p005v2.fileproperties.UpdateTemplateResult;
import com.dropbox.core.p005v2.files.SyncSettingArg;
import com.dropbox.core.stone.StoneSerializers;
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.DbxTeamTeamRequests */
public class DbxTeamTeamRequests {
    private final DbxRawClientV2 client;

    public DbxTeamTeamRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public ListMemberDevicesResult devicesListMemberDevices(ListMemberDevicesArg listMemberDevicesArg) throws ListMemberDevicesErrorException, DbxException {
        try {
            return (ListMemberDevicesResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/devices/list_member_devices", listMemberDevicesArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListMemberDevicesErrorException("2/team/devices/list_member_devices", e.getRequestId(), e.getUserMessage(), (ListMemberDevicesError) e.getErrorValue());
        }
    }

    public ListMemberDevicesResult devicesListMemberDevices(String str) throws ListMemberDevicesErrorException, DbxException {
        return devicesListMemberDevices(new ListMemberDevicesArg(str));
    }

    public DevicesListMemberDevicesBuilder devicesListMemberDevicesBuilder(String str) {
        return new DevicesListMemberDevicesBuilder(this, ListMemberDevicesArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public ListMembersDevicesResult devicesListMembersDevices(ListMembersDevicesArg listMembersDevicesArg) throws ListMembersDevicesErrorException, DbxException {
        try {
            return (ListMembersDevicesResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/devices/list_members_devices", listMembersDevicesArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListMembersDevicesErrorException("2/team/devices/list_members_devices", e.getRequestId(), e.getUserMessage(), (ListMembersDevicesError) e.getErrorValue());
        }
    }

    public ListMembersDevicesResult devicesListMembersDevices() throws ListMembersDevicesErrorException, DbxException {
        return devicesListMembersDevices(new ListMembersDevicesArg());
    }

    public DevicesListMembersDevicesBuilder devicesListMembersDevicesBuilder() {
        return new DevicesListMembersDevicesBuilder(this, ListMembersDevicesArg.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public ListTeamDevicesResult devicesListTeamDevices(ListTeamDevicesArg listTeamDevicesArg) throws ListTeamDevicesErrorException, DbxException {
        try {
            return (ListTeamDevicesResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/devices/list_team_devices", listTeamDevicesArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListTeamDevicesErrorException("2/team/devices/list_team_devices", e.getRequestId(), e.getUserMessage(), (ListTeamDevicesError) e.getErrorValue());
        }
    }

    @Deprecated
    public ListTeamDevicesResult devicesListTeamDevices() throws ListTeamDevicesErrorException, DbxException {
        return devicesListTeamDevices(new ListTeamDevicesArg());
    }

    @Deprecated
    public DevicesListTeamDevicesBuilder devicesListTeamDevicesBuilder() {
        return new DevicesListTeamDevicesBuilder(this, ListTeamDevicesArg.newBuilder());
    }

    public void devicesRevokeDeviceSession(RevokeDeviceSessionArg revokeDeviceSessionArg) throws RevokeDeviceSessionErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/team/devices/revoke_device_session", revokeDeviceSessionArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RevokeDeviceSessionErrorException("2/team/devices/revoke_device_session", e.getRequestId(), e.getUserMessage(), (RevokeDeviceSessionError) e.getErrorValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public RevokeDeviceSessionBatchResult devicesRevokeDeviceSessionBatch(RevokeDeviceSessionBatchArg revokeDeviceSessionBatchArg) throws RevokeDeviceSessionBatchErrorException, DbxException {
        try {
            return (RevokeDeviceSessionBatchResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/devices/revoke_device_session_batch", revokeDeviceSessionBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RevokeDeviceSessionBatchErrorException("2/team/devices/revoke_device_session_batch", e.getRequestId(), e.getUserMessage(), (RevokeDeviceSessionBatchError) e.getErrorValue());
        }
    }

    public RevokeDeviceSessionBatchResult devicesRevokeDeviceSessionBatch(List<RevokeDeviceSessionArg> list) throws RevokeDeviceSessionBatchErrorException, DbxException {
        return devicesRevokeDeviceSessionBatch(new RevokeDeviceSessionBatchArg(list));
    }

    /* access modifiers changed from: 0000 */
    public FeaturesGetValuesBatchResult featuresGetValues(FeaturesGetValuesBatchArg featuresGetValuesBatchArg) throws FeaturesGetValuesBatchErrorException, DbxException {
        try {
            return (FeaturesGetValuesBatchResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/features/get_values", featuresGetValuesBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new FeaturesGetValuesBatchErrorException("2/team/features/get_values", e.getRequestId(), e.getUserMessage(), (FeaturesGetValuesBatchError) e.getErrorValue());
        }
    }

    public FeaturesGetValuesBatchResult featuresGetValues(List<Feature> list) throws FeaturesGetValuesBatchErrorException, DbxException {
        return featuresGetValues(new FeaturesGetValuesBatchArg(list));
    }

    public TeamGetInfoResult getInfo() throws DbxApiException, DbxException {
        try {
            return (TeamGetInfoResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/get_info", null, false, StoneSerializers.void_(), Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"get_info\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public GroupFullInfo groupsCreate(GroupCreateArg groupCreateArg) throws GroupCreateErrorException, DbxException {
        try {
            return (GroupFullInfo) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/create", groupCreateArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupCreateErrorException("2/team/groups/create", e.getRequestId(), e.getUserMessage(), (GroupCreateError) e.getErrorValue());
        }
    }

    public GroupFullInfo groupsCreate(String str) throws GroupCreateErrorException, DbxException {
        return groupsCreate(new GroupCreateArg(str));
    }

    public GroupsCreateBuilder groupsCreateBuilder(String str) {
        return new GroupsCreateBuilder(this, GroupCreateArg.newBuilder(str));
    }

    public LaunchEmptyResult groupsDelete(GroupSelector groupSelector) throws GroupDeleteErrorException, DbxException {
        try {
            return (LaunchEmptyResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/delete", groupSelector, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupDeleteErrorException("2/team/groups/delete", e.getRequestId(), e.getUserMessage(), (GroupDeleteError) e.getErrorValue());
        }
    }

    public List<GroupsGetInfoItem> groupsGetInfo(GroupsSelector groupsSelector) throws GroupsGetInfoErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/get_info", groupsSelector, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupsGetInfoErrorException("2/team/groups/get_info", e.getRequestId(), e.getUserMessage(), (GroupsGetInfoError) e.getErrorValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public PollEmptyResult groupsJobStatusGet(PollArg pollArg) throws GroupsPollErrorException, DbxException {
        try {
            return (PollEmptyResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/job_status/get", pollArg, false, PollArg.Serializer.INSTANCE, PollEmptyResult.Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupsPollErrorException("2/team/groups/job_status/get", e.getRequestId(), e.getUserMessage(), (GroupsPollError) e.getErrorValue());
        }
    }

    public PollEmptyResult groupsJobStatusGet(String str) throws GroupsPollErrorException, DbxException {
        return groupsJobStatusGet(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public GroupsListResult groupsList(GroupsListArg groupsListArg) throws DbxApiException, DbxException {
        try {
            return (GroupsListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/list", groupsListArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"groups/list\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public GroupsListResult groupsList() throws DbxApiException, DbxException {
        return groupsList(new GroupsListArg());
    }

    public GroupsListResult groupsList(long j) throws DbxApiException, DbxException {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            return groupsList(new GroupsListArg(j));
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    /* access modifiers changed from: 0000 */
    public GroupsListResult groupsListContinue(GroupsListContinueArg groupsListContinueArg) throws GroupsListContinueErrorException, DbxException {
        try {
            return (GroupsListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/list/continue", groupsListContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupsListContinueErrorException("2/team/groups/list/continue", e.getRequestId(), e.getUserMessage(), (GroupsListContinueError) e.getErrorValue());
        }
    }

    public GroupsListResult groupsListContinue(String str) throws GroupsListContinueErrorException, DbxException {
        return groupsListContinue(new GroupsListContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public GroupMembersChangeResult groupsMembersAdd(GroupMembersAddArg groupMembersAddArg) throws GroupMembersAddErrorException, DbxException {
        try {
            return (GroupMembersChangeResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/members/add", groupMembersAddArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupMembersAddErrorException("2/team/groups/members/add", e.getRequestId(), e.getUserMessage(), (GroupMembersAddError) e.getErrorValue());
        }
    }

    public GroupMembersChangeResult groupsMembersAdd(GroupSelector groupSelector, List<MemberAccess> list) throws GroupMembersAddErrorException, DbxException {
        return groupsMembersAdd(new GroupMembersAddArg(groupSelector, list));
    }

    public GroupMembersChangeResult groupsMembersAdd(GroupSelector groupSelector, List<MemberAccess> list, boolean z) throws GroupMembersAddErrorException, DbxException {
        return groupsMembersAdd(new GroupMembersAddArg(groupSelector, list, z));
    }

    /* access modifiers changed from: 0000 */
    public GroupsMembersListResult groupsMembersList(GroupsMembersListArg groupsMembersListArg) throws GroupSelectorErrorException, DbxException {
        try {
            return (GroupsMembersListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/members/list", groupsMembersListArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupSelectorErrorException("2/team/groups/members/list", e.getRequestId(), e.getUserMessage(), (GroupSelectorError) e.getErrorValue());
        }
    }

    public GroupsMembersListResult groupsMembersList(GroupSelector groupSelector) throws GroupSelectorErrorException, DbxException {
        return groupsMembersList(new GroupsMembersListArg(groupSelector));
    }

    public GroupsMembersListResult groupsMembersList(GroupSelector groupSelector, long j) throws GroupSelectorErrorException, DbxException {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            return groupsMembersList(new GroupsMembersListArg(groupSelector, j));
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    /* access modifiers changed from: 0000 */
    public GroupsMembersListResult groupsMembersListContinue(GroupsMembersListContinueArg groupsMembersListContinueArg) throws GroupsMembersListContinueErrorException, DbxException {
        try {
            return (GroupsMembersListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/members/list/continue", groupsMembersListContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupsMembersListContinueErrorException("2/team/groups/members/list/continue", e.getRequestId(), e.getUserMessage(), (GroupsMembersListContinueError) e.getErrorValue());
        }
    }

    public GroupsMembersListResult groupsMembersListContinue(String str) throws GroupsMembersListContinueErrorException, DbxException {
        return groupsMembersListContinue(new GroupsMembersListContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public GroupMembersChangeResult groupsMembersRemove(GroupMembersRemoveArg groupMembersRemoveArg) throws GroupMembersRemoveErrorException, DbxException {
        try {
            return (GroupMembersChangeResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/members/remove", groupMembersRemoveArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupMembersRemoveErrorException("2/team/groups/members/remove", e.getRequestId(), e.getUserMessage(), (GroupMembersRemoveError) e.getErrorValue());
        }
    }

    public GroupMembersChangeResult groupsMembersRemove(GroupSelector groupSelector, List<UserSelectorArg> list) throws GroupMembersRemoveErrorException, DbxException {
        return groupsMembersRemove(new GroupMembersRemoveArg(groupSelector, list));
    }

    public GroupMembersChangeResult groupsMembersRemove(GroupSelector groupSelector, List<UserSelectorArg> list, boolean z) throws GroupMembersRemoveErrorException, DbxException {
        return groupsMembersRemove(new GroupMembersRemoveArg(groupSelector, list, z));
    }

    /* access modifiers changed from: 0000 */
    public List<GroupsGetInfoItem> groupsMembersSetAccessType(GroupMembersSetAccessTypeArg groupMembersSetAccessTypeArg) throws GroupMemberSetAccessTypeErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/members/set_access_type", groupMembersSetAccessTypeArg, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupMemberSetAccessTypeErrorException("2/team/groups/members/set_access_type", e.getRequestId(), e.getUserMessage(), (GroupMemberSetAccessTypeError) e.getErrorValue());
        }
    }

    public List<GroupsGetInfoItem> groupsMembersSetAccessType(GroupSelector groupSelector, UserSelectorArg userSelectorArg, GroupAccessType groupAccessType) throws GroupMemberSetAccessTypeErrorException, DbxException {
        return groupsMembersSetAccessType(new GroupMembersSetAccessTypeArg(groupSelector, userSelectorArg, groupAccessType));
    }

    public List<GroupsGetInfoItem> groupsMembersSetAccessType(GroupSelector groupSelector, UserSelectorArg userSelectorArg, GroupAccessType groupAccessType, boolean z) throws GroupMemberSetAccessTypeErrorException, DbxException {
        return groupsMembersSetAccessType(new GroupMembersSetAccessTypeArg(groupSelector, userSelectorArg, groupAccessType, z));
    }

    /* access modifiers changed from: 0000 */
    public GroupFullInfo groupsUpdate(GroupUpdateArgs groupUpdateArgs) throws GroupUpdateErrorException, DbxException {
        try {
            return (GroupFullInfo) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/groups/update", groupUpdateArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GroupUpdateErrorException("2/team/groups/update", e.getRequestId(), e.getUserMessage(), (GroupUpdateError) e.getErrorValue());
        }
    }

    public GroupFullInfo groupsUpdate(GroupSelector groupSelector) throws GroupUpdateErrorException, DbxException {
        return groupsUpdate(new GroupUpdateArgs(groupSelector));
    }

    public GroupsUpdateBuilder groupsUpdateBuilder(GroupSelector groupSelector) {
        return new GroupsUpdateBuilder(this, GroupUpdateArgs.newBuilder(groupSelector));
    }

    /* access modifiers changed from: 0000 */
    public ListMemberAppsResult linkedAppsListMemberLinkedApps(ListMemberAppsArg listMemberAppsArg) throws ListMemberAppsErrorException, DbxException {
        try {
            return (ListMemberAppsResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/linked_apps/list_member_linked_apps", listMemberAppsArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListMemberAppsErrorException("2/team/linked_apps/list_member_linked_apps", e.getRequestId(), e.getUserMessage(), (ListMemberAppsError) e.getErrorValue());
        }
    }

    public ListMemberAppsResult linkedAppsListMemberLinkedApps(String str) throws ListMemberAppsErrorException, DbxException {
        return linkedAppsListMemberLinkedApps(new ListMemberAppsArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ListMembersAppsResult linkedAppsListMembersLinkedApps(ListMembersAppsArg listMembersAppsArg) throws ListMembersAppsErrorException, DbxException {
        try {
            return (ListMembersAppsResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/linked_apps/list_members_linked_apps", listMembersAppsArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListMembersAppsErrorException("2/team/linked_apps/list_members_linked_apps", e.getRequestId(), e.getUserMessage(), (ListMembersAppsError) e.getErrorValue());
        }
    }

    public ListMembersAppsResult linkedAppsListMembersLinkedApps() throws ListMembersAppsErrorException, DbxException {
        return linkedAppsListMembersLinkedApps(new ListMembersAppsArg());
    }

    public ListMembersAppsResult linkedAppsListMembersLinkedApps(String str) throws ListMembersAppsErrorException, DbxException {
        return linkedAppsListMembersLinkedApps(new ListMembersAppsArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ListTeamAppsResult linkedAppsListTeamLinkedApps(ListTeamAppsArg listTeamAppsArg) throws ListTeamAppsErrorException, DbxException {
        try {
            return (ListTeamAppsResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/linked_apps/list_team_linked_apps", listTeamAppsArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListTeamAppsErrorException("2/team/linked_apps/list_team_linked_apps", e.getRequestId(), e.getUserMessage(), (ListTeamAppsError) e.getErrorValue());
        }
    }

    @Deprecated
    public ListTeamAppsResult linkedAppsListTeamLinkedApps() throws ListTeamAppsErrorException, DbxException {
        return linkedAppsListTeamLinkedApps(new ListTeamAppsArg());
    }

    @Deprecated
    public ListTeamAppsResult linkedAppsListTeamLinkedApps(String str) throws ListTeamAppsErrorException, DbxException {
        return linkedAppsListTeamLinkedApps(new ListTeamAppsArg(str));
    }

    /* access modifiers changed from: 0000 */
    public void linkedAppsRevokeLinkedApp(RevokeLinkedApiAppArg revokeLinkedApiAppArg) throws RevokeLinkedAppErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/team/linked_apps/revoke_linked_app", revokeLinkedApiAppArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RevokeLinkedAppErrorException("2/team/linked_apps/revoke_linked_app", e.getRequestId(), e.getUserMessage(), (RevokeLinkedAppError) e.getErrorValue());
        }
    }

    public void linkedAppsRevokeLinkedApp(String str, String str2) throws RevokeLinkedAppErrorException, DbxException {
        linkedAppsRevokeLinkedApp(new RevokeLinkedApiAppArg(str, str2));
    }

    public void linkedAppsRevokeLinkedApp(String str, String str2, boolean z) throws RevokeLinkedAppErrorException, DbxException {
        linkedAppsRevokeLinkedApp(new RevokeLinkedApiAppArg(str, str2, z));
    }

    /* access modifiers changed from: 0000 */
    public RevokeLinkedAppBatchResult linkedAppsRevokeLinkedAppBatch(RevokeLinkedApiAppBatchArg revokeLinkedApiAppBatchArg) throws RevokeLinkedAppBatchErrorException, DbxException {
        try {
            return (RevokeLinkedAppBatchResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/linked_apps/revoke_linked_app_batch", revokeLinkedApiAppBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RevokeLinkedAppBatchErrorException("2/team/linked_apps/revoke_linked_app_batch", e.getRequestId(), e.getUserMessage(), (RevokeLinkedAppBatchError) e.getErrorValue());
        }
    }

    public RevokeLinkedAppBatchResult linkedAppsRevokeLinkedAppBatch(List<RevokeLinkedApiAppArg> list) throws RevokeLinkedAppBatchErrorException, DbxException {
        return linkedAppsRevokeLinkedAppBatch(new RevokeLinkedApiAppBatchArg(list));
    }

    /* access modifiers changed from: 0000 */
    public ExcludedUsersUpdateResult memberSpaceLimitsExcludedUsersAdd(ExcludedUsersUpdateArg excludedUsersUpdateArg) throws ExcludedUsersUpdateErrorException, DbxException {
        try {
            return (ExcludedUsersUpdateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/member_space_limits/excluded_users/add", excludedUsersUpdateArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ExcludedUsersUpdateErrorException("2/team/member_space_limits/excluded_users/add", e.getRequestId(), e.getUserMessage(), (ExcludedUsersUpdateError) e.getErrorValue());
        }
    }

    public ExcludedUsersUpdateResult memberSpaceLimitsExcludedUsersAdd() throws ExcludedUsersUpdateErrorException, DbxException {
        return memberSpaceLimitsExcludedUsersAdd(new ExcludedUsersUpdateArg());
    }

    public ExcludedUsersUpdateResult memberSpaceLimitsExcludedUsersAdd(List<UserSelectorArg> list) throws ExcludedUsersUpdateErrorException, DbxException {
        if (list != null) {
            for (UserSelectorArg userSelectorArg : list) {
                if (userSelectorArg == null) {
                    throw new IllegalArgumentException("An item in list 'users' is null");
                }
            }
        }
        return memberSpaceLimitsExcludedUsersAdd(new ExcludedUsersUpdateArg(list));
    }

    /* access modifiers changed from: 0000 */
    public ExcludedUsersListResult memberSpaceLimitsExcludedUsersList(ExcludedUsersListArg excludedUsersListArg) throws ExcludedUsersListErrorException, DbxException {
        try {
            return (ExcludedUsersListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/member_space_limits/excluded_users/list", excludedUsersListArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ExcludedUsersListErrorException("2/team/member_space_limits/excluded_users/list", e.getRequestId(), e.getUserMessage(), (ExcludedUsersListError) e.getErrorValue());
        }
    }

    public ExcludedUsersListResult memberSpaceLimitsExcludedUsersList() throws ExcludedUsersListErrorException, DbxException {
        return memberSpaceLimitsExcludedUsersList(new ExcludedUsersListArg());
    }

    public ExcludedUsersListResult memberSpaceLimitsExcludedUsersList(long j) throws ExcludedUsersListErrorException, DbxException {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            return memberSpaceLimitsExcludedUsersList(new ExcludedUsersListArg(j));
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    /* access modifiers changed from: 0000 */
    public ExcludedUsersListResult memberSpaceLimitsExcludedUsersListContinue(ExcludedUsersListContinueArg excludedUsersListContinueArg) throws ExcludedUsersListContinueErrorException, DbxException {
        try {
            return (ExcludedUsersListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/member_space_limits/excluded_users/list/continue", excludedUsersListContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ExcludedUsersListContinueErrorException("2/team/member_space_limits/excluded_users/list/continue", e.getRequestId(), e.getUserMessage(), (ExcludedUsersListContinueError) e.getErrorValue());
        }
    }

    public ExcludedUsersListResult memberSpaceLimitsExcludedUsersListContinue(String str) throws ExcludedUsersListContinueErrorException, DbxException {
        return memberSpaceLimitsExcludedUsersListContinue(new ExcludedUsersListContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ExcludedUsersUpdateResult memberSpaceLimitsExcludedUsersRemove(ExcludedUsersUpdateArg excludedUsersUpdateArg) throws ExcludedUsersUpdateErrorException, DbxException {
        try {
            return (ExcludedUsersUpdateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/member_space_limits/excluded_users/remove", excludedUsersUpdateArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ExcludedUsersUpdateErrorException("2/team/member_space_limits/excluded_users/remove", e.getRequestId(), e.getUserMessage(), (ExcludedUsersUpdateError) e.getErrorValue());
        }
    }

    public ExcludedUsersUpdateResult memberSpaceLimitsExcludedUsersRemove() throws ExcludedUsersUpdateErrorException, DbxException {
        return memberSpaceLimitsExcludedUsersRemove(new ExcludedUsersUpdateArg());
    }

    public ExcludedUsersUpdateResult memberSpaceLimitsExcludedUsersRemove(List<UserSelectorArg> list) throws ExcludedUsersUpdateErrorException, DbxException {
        if (list != null) {
            for (UserSelectorArg userSelectorArg : list) {
                if (userSelectorArg == null) {
                    throw new IllegalArgumentException("An item in list 'users' is null");
                }
            }
        }
        return memberSpaceLimitsExcludedUsersRemove(new ExcludedUsersUpdateArg(list));
    }

    /* access modifiers changed from: 0000 */
    public List<CustomQuotaResult> memberSpaceLimitsGetCustomQuota(CustomQuotaUsersArg customQuotaUsersArg) throws CustomQuotaErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/member_space_limits/get_custom_quota", customQuotaUsersArg, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new CustomQuotaErrorException("2/team/member_space_limits/get_custom_quota", e.getRequestId(), e.getUserMessage(), (CustomQuotaError) e.getErrorValue());
        }
    }

    public List<CustomQuotaResult> memberSpaceLimitsGetCustomQuota(List<UserSelectorArg> list) throws CustomQuotaErrorException, DbxException {
        return memberSpaceLimitsGetCustomQuota(new CustomQuotaUsersArg(list));
    }

    /* access modifiers changed from: 0000 */
    public List<RemoveCustomQuotaResult> memberSpaceLimitsRemoveCustomQuota(CustomQuotaUsersArg customQuotaUsersArg) throws CustomQuotaErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/member_space_limits/remove_custom_quota", customQuotaUsersArg, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new CustomQuotaErrorException("2/team/member_space_limits/remove_custom_quota", e.getRequestId(), e.getUserMessage(), (CustomQuotaError) e.getErrorValue());
        }
    }

    public List<RemoveCustomQuotaResult> memberSpaceLimitsRemoveCustomQuota(List<UserSelectorArg> list) throws CustomQuotaErrorException, DbxException {
        return memberSpaceLimitsRemoveCustomQuota(new CustomQuotaUsersArg(list));
    }

    /* access modifiers changed from: 0000 */
    public List<CustomQuotaResult> memberSpaceLimitsSetCustomQuota(SetCustomQuotaArg setCustomQuotaArg) throws SetCustomQuotaErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/member_space_limits/set_custom_quota", setCustomQuotaArg, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SetCustomQuotaErrorException("2/team/member_space_limits/set_custom_quota", e.getRequestId(), e.getUserMessage(), (SetCustomQuotaError) e.getErrorValue());
        }
    }

    public List<CustomQuotaResult> memberSpaceLimitsSetCustomQuota(List<UserCustomQuotaArg> list) throws SetCustomQuotaErrorException, DbxException {
        return memberSpaceLimitsSetCustomQuota(new SetCustomQuotaArg(list));
    }

    /* access modifiers changed from: 0000 */
    public MembersAddLaunch membersAdd(MembersAddArg membersAddArg) throws DbxApiException, DbxException {
        try {
            return (MembersAddLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/add", membersAddArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"members/add\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public MembersAddLaunch membersAdd(List<MemberAddArg> list) throws DbxApiException, DbxException {
        return membersAdd(new MembersAddArg(list));
    }

    public MembersAddLaunch membersAdd(List<MemberAddArg> list, boolean z) throws DbxApiException, DbxException {
        return membersAdd(new MembersAddArg(list, z));
    }

    /* access modifiers changed from: 0000 */
    public MembersAddJobStatus membersAddJobStatusGet(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (MembersAddJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/add/job_status/get", pollArg, false, PollArg.Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/team/members/add/job_status/get", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public MembersAddJobStatus membersAddJobStatusGet(String str) throws PollErrorException, DbxException {
        return membersAddJobStatusGet(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public List<MembersGetInfoItem> membersGetInfo(MembersGetInfoArgs membersGetInfoArgs) throws MembersGetInfoErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/get_info", membersGetInfoArgs, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersGetInfoErrorException("2/team/members/get_info", e.getRequestId(), e.getUserMessage(), (MembersGetInfoError) e.getErrorValue());
        }
    }

    public List<MembersGetInfoItem> membersGetInfo(List<UserSelectorArg> list) throws MembersGetInfoErrorException, DbxException {
        return membersGetInfo(new MembersGetInfoArgs(list));
    }

    /* access modifiers changed from: 0000 */
    public MembersListResult membersList(MembersListArg membersListArg) throws MembersListErrorException, DbxException {
        try {
            return (MembersListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/list", membersListArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersListErrorException("2/team/members/list", e.getRequestId(), e.getUserMessage(), (MembersListError) e.getErrorValue());
        }
    }

    public MembersListResult membersList() throws MembersListErrorException, DbxException {
        return membersList(new MembersListArg());
    }

    public MembersListBuilder membersListBuilder() {
        return new MembersListBuilder(this, MembersListArg.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public MembersListResult membersListContinue(MembersListContinueArg membersListContinueArg) throws MembersListContinueErrorException, DbxException {
        try {
            return (MembersListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/list/continue", membersListContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersListContinueErrorException("2/team/members/list/continue", e.getRequestId(), e.getUserMessage(), (MembersListContinueError) e.getErrorValue());
        }
    }

    public MembersListResult membersListContinue(String str) throws MembersListContinueErrorException, DbxException {
        return membersListContinue(new MembersListContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public LaunchEmptyResult membersMoveFormerMemberFiles(MembersDataTransferArg membersDataTransferArg) throws MembersTransferFormerMembersFilesErrorException, DbxException {
        try {
            return (LaunchEmptyResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/move_former_member_files", membersDataTransferArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersTransferFormerMembersFilesErrorException("2/team/members/move_former_member_files", e.getRequestId(), e.getUserMessage(), (MembersTransferFormerMembersFilesError) e.getErrorValue());
        }
    }

    public LaunchEmptyResult membersMoveFormerMemberFiles(UserSelectorArg userSelectorArg, UserSelectorArg userSelectorArg2, UserSelectorArg userSelectorArg3) throws MembersTransferFormerMembersFilesErrorException, DbxException {
        return membersMoveFormerMemberFiles(new MembersDataTransferArg(userSelectorArg, userSelectorArg2, userSelectorArg3));
    }

    /* access modifiers changed from: 0000 */
    public PollEmptyResult membersMoveFormerMemberFilesJobStatusCheck(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (PollEmptyResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/move_former_member_files/job_status/check", pollArg, false, PollArg.Serializer.INSTANCE, PollEmptyResult.Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/team/members/move_former_member_files/job_status/check", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public PollEmptyResult membersMoveFormerMemberFilesJobStatusCheck(String str) throws PollErrorException, DbxException {
        return membersMoveFormerMemberFilesJobStatusCheck(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public void membersRecover(MembersRecoverArg membersRecoverArg) throws MembersRecoverErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/recover", membersRecoverArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersRecoverErrorException("2/team/members/recover", e.getRequestId(), e.getUserMessage(), (MembersRecoverError) e.getErrorValue());
        }
    }

    public void membersRecover(UserSelectorArg userSelectorArg) throws MembersRecoverErrorException, DbxException {
        membersRecover(new MembersRecoverArg(userSelectorArg));
    }

    /* access modifiers changed from: 0000 */
    public LaunchEmptyResult membersRemove(MembersRemoveArg membersRemoveArg) throws MembersRemoveErrorException, DbxException {
        try {
            return (LaunchEmptyResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/remove", membersRemoveArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersRemoveErrorException("2/team/members/remove", e.getRequestId(), e.getUserMessage(), (MembersRemoveError) e.getErrorValue());
        }
    }

    public LaunchEmptyResult membersRemove(UserSelectorArg userSelectorArg) throws MembersRemoveErrorException, DbxException {
        return membersRemove(new MembersRemoveArg(userSelectorArg));
    }

    public MembersRemoveBuilder membersRemoveBuilder(UserSelectorArg userSelectorArg) {
        return new MembersRemoveBuilder(this, MembersRemoveArg.newBuilder(userSelectorArg));
    }

    /* access modifiers changed from: 0000 */
    public PollEmptyResult membersRemoveJobStatusGet(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (PollEmptyResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/remove/job_status/get", pollArg, false, PollArg.Serializer.INSTANCE, PollEmptyResult.Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/team/members/remove/job_status/get", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public PollEmptyResult membersRemoveJobStatusGet(String str) throws PollErrorException, DbxException {
        return membersRemoveJobStatusGet(new PollArg(str));
    }

    public void membersSendWelcomeEmail(UserSelectorArg userSelectorArg) throws MembersSendWelcomeErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/send_welcome_email", userSelectorArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersSendWelcomeErrorException("2/team/members/send_welcome_email", e.getRequestId(), e.getUserMessage(), (MembersSendWelcomeError) e.getErrorValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public MembersSetPermissionsResult membersSetAdminPermissions(MembersSetPermissionsArg membersSetPermissionsArg) throws MembersSetPermissionsErrorException, DbxException {
        try {
            return (MembersSetPermissionsResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/set_admin_permissions", membersSetPermissionsArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersSetPermissionsErrorException("2/team/members/set_admin_permissions", e.getRequestId(), e.getUserMessage(), (MembersSetPermissionsError) e.getErrorValue());
        }
    }

    public MembersSetPermissionsResult membersSetAdminPermissions(UserSelectorArg userSelectorArg, AdminTier adminTier) throws MembersSetPermissionsErrorException, DbxException {
        return membersSetAdminPermissions(new MembersSetPermissionsArg(userSelectorArg, adminTier));
    }

    /* access modifiers changed from: 0000 */
    public TeamMemberInfo membersSetProfile(MembersSetProfileArg membersSetProfileArg) throws MembersSetProfileErrorException, DbxException {
        try {
            return (TeamMemberInfo) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/set_profile", membersSetProfileArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersSetProfileErrorException("2/team/members/set_profile", e.getRequestId(), e.getUserMessage(), (MembersSetProfileError) e.getErrorValue());
        }
    }

    public TeamMemberInfo membersSetProfile(UserSelectorArg userSelectorArg) throws MembersSetProfileErrorException, DbxException {
        return membersSetProfile(new MembersSetProfileArg(userSelectorArg));
    }

    public MembersSetProfileBuilder membersSetProfileBuilder(UserSelectorArg userSelectorArg) {
        return new MembersSetProfileBuilder(this, MembersSetProfileArg.newBuilder(userSelectorArg));
    }

    /* access modifiers changed from: 0000 */
    public void membersSuspend(MembersDeactivateArg membersDeactivateArg) throws MembersSuspendErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/suspend", membersDeactivateArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersSuspendErrorException("2/team/members/suspend", e.getRequestId(), e.getUserMessage(), (MembersSuspendError) e.getErrorValue());
        }
    }

    public void membersSuspend(UserSelectorArg userSelectorArg) throws MembersSuspendErrorException, DbxException {
        membersSuspend(new MembersDeactivateArg(userSelectorArg));
    }

    public void membersSuspend(UserSelectorArg userSelectorArg, boolean z) throws MembersSuspendErrorException, DbxException {
        membersSuspend(new MembersDeactivateArg(userSelectorArg, z));
    }

    /* access modifiers changed from: 0000 */
    public void membersUnsuspend(MembersUnsuspendArg membersUnsuspendArg) throws MembersUnsuspendErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/team/members/unsuspend", membersUnsuspendArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MembersUnsuspendErrorException("2/team/members/unsuspend", e.getRequestId(), e.getUserMessage(), (MembersUnsuspendError) e.getErrorValue());
        }
    }

    public void membersUnsuspend(UserSelectorArg userSelectorArg) throws MembersUnsuspendErrorException, DbxException {
        membersUnsuspend(new MembersUnsuspendArg(userSelectorArg));
    }

    /* access modifiers changed from: 0000 */
    public TeamNamespacesListResult namespacesList(TeamNamespacesListArg teamNamespacesListArg) throws DbxApiException, DbxException {
        try {
            return (TeamNamespacesListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/namespaces/list", teamNamespacesListArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"namespaces/list\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public TeamNamespacesListResult namespacesList() throws DbxApiException, DbxException {
        return namespacesList(new TeamNamespacesListArg());
    }

    public TeamNamespacesListResult namespacesList(long j) throws DbxApiException, DbxException {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            return namespacesList(new TeamNamespacesListArg(j));
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    /* access modifiers changed from: 0000 */
    public TeamNamespacesListResult namespacesListContinue(TeamNamespacesListContinueArg teamNamespacesListContinueArg) throws TeamNamespacesListContinueErrorException, DbxException {
        try {
            return (TeamNamespacesListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/namespaces/list/continue", teamNamespacesListContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamNamespacesListContinueErrorException("2/team/namespaces/list/continue", e.getRequestId(), e.getUserMessage(), (TeamNamespacesListContinueError) e.getErrorValue());
        }
    }

    public TeamNamespacesListResult namespacesListContinue(String str) throws TeamNamespacesListContinueErrorException, DbxException {
        return namespacesListContinue(new TeamNamespacesListContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public AddTemplateResult propertiesTemplateAdd(AddTemplateArg addTemplateArg) throws ModifyTemplateErrorException, DbxException {
        try {
            return (AddTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/properties/template/add", addTemplateArg, false, AddTemplateArg.Serializer.INSTANCE, AddTemplateResult.Serializer.INSTANCE, ModifyTemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ModifyTemplateErrorException("2/team/properties/template/add", e.getRequestId(), e.getUserMessage(), (ModifyTemplateError) e.getErrorValue());
        }
    }

    @Deprecated
    public AddTemplateResult propertiesTemplateAdd(String str, String str2, List<PropertyFieldTemplate> list) throws ModifyTemplateErrorException, DbxException {
        return propertiesTemplateAdd(new AddTemplateArg(str, str2, list));
    }

    /* access modifiers changed from: 0000 */
    public GetTemplateResult propertiesTemplateGet(GetTemplateArg getTemplateArg) throws TemplateErrorException, DbxException {
        try {
            return (GetTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/properties/template/get", getTemplateArg, false, GetTemplateArg.Serializer.INSTANCE, GetTemplateResult.Serializer.INSTANCE, TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/team/properties/template/get", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    @Deprecated
    public GetTemplateResult propertiesTemplateGet(String str) throws TemplateErrorException, DbxException {
        return propertiesTemplateGet(new GetTemplateArg(str));
    }

    @Deprecated
    public ListTemplateResult propertiesTemplateList() throws TemplateErrorException, DbxException {
        try {
            return (ListTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/properties/template/list", null, false, StoneSerializers.void_(), ListTemplateResult.Serializer.INSTANCE, TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/team/properties/template/list", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public UpdateTemplateResult propertiesTemplateUpdate(UpdateTemplateArg updateTemplateArg) throws ModifyTemplateErrorException, DbxException {
        try {
            return (UpdateTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/properties/template/update", updateTemplateArg, false, UpdateTemplateArg.Serializer.INSTANCE, UpdateTemplateResult.Serializer.INSTANCE, ModifyTemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ModifyTemplateErrorException("2/team/properties/template/update", e.getRequestId(), e.getUserMessage(), (ModifyTemplateError) e.getErrorValue());
        }
    }

    @Deprecated
    public UpdateTemplateResult propertiesTemplateUpdate(String str) throws ModifyTemplateErrorException, DbxException {
        return propertiesTemplateUpdate(new UpdateTemplateArg(str));
    }

    @Deprecated
    public PropertiesTemplateUpdateBuilder propertiesTemplateUpdateBuilder(String str) {
        return new PropertiesTemplateUpdateBuilder(this, UpdateTemplateArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public GetActivityReport reportsGetActivity(DateRange dateRange) throws DateRangeErrorException, DbxException {
        try {
            return (GetActivityReport) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/reports/get_activity", dateRange, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DateRangeErrorException("2/team/reports/get_activity", e.getRequestId(), e.getUserMessage(), (DateRangeError) e.getErrorValue());
        }
    }

    public GetActivityReport reportsGetActivity() throws DateRangeErrorException, DbxException {
        return reportsGetActivity(new DateRange());
    }

    public ReportsGetActivityBuilder reportsGetActivityBuilder() {
        return new ReportsGetActivityBuilder(this, DateRange.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public GetDevicesReport reportsGetDevices(DateRange dateRange) throws DateRangeErrorException, DbxException {
        try {
            return (GetDevicesReport) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/reports/get_devices", dateRange, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DateRangeErrorException("2/team/reports/get_devices", e.getRequestId(), e.getUserMessage(), (DateRangeError) e.getErrorValue());
        }
    }

    public GetDevicesReport reportsGetDevices() throws DateRangeErrorException, DbxException {
        return reportsGetDevices(new DateRange());
    }

    public ReportsGetDevicesBuilder reportsGetDevicesBuilder() {
        return new ReportsGetDevicesBuilder(this, DateRange.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public GetMembershipReport reportsGetMembership(DateRange dateRange) throws DateRangeErrorException, DbxException {
        try {
            return (GetMembershipReport) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/reports/get_membership", dateRange, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DateRangeErrorException("2/team/reports/get_membership", e.getRequestId(), e.getUserMessage(), (DateRangeError) e.getErrorValue());
        }
    }

    public GetMembershipReport reportsGetMembership() throws DateRangeErrorException, DbxException {
        return reportsGetMembership(new DateRange());
    }

    public ReportsGetMembershipBuilder reportsGetMembershipBuilder() {
        return new ReportsGetMembershipBuilder(this, DateRange.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public GetStorageReport reportsGetStorage(DateRange dateRange) throws DateRangeErrorException, DbxException {
        try {
            return (GetStorageReport) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/reports/get_storage", dateRange, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DateRangeErrorException("2/team/reports/get_storage", e.getRequestId(), e.getUserMessage(), (DateRangeError) e.getErrorValue());
        }
    }

    public GetStorageReport reportsGetStorage() throws DateRangeErrorException, DbxException {
        return reportsGetStorage(new DateRange());
    }

    public ReportsGetStorageBuilder reportsGetStorageBuilder() {
        return new ReportsGetStorageBuilder(this, DateRange.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public TeamFolderMetadata teamFolderActivate(TeamFolderIdArg teamFolderIdArg) throws TeamFolderActivateErrorException, DbxException {
        try {
            return (TeamFolderMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/activate", teamFolderIdArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamFolderActivateErrorException("2/team/team_folder/activate", e.getRequestId(), e.getUserMessage(), (TeamFolderActivateError) e.getErrorValue());
        }
    }

    public TeamFolderMetadata teamFolderActivate(String str) throws TeamFolderActivateErrorException, DbxException {
        return teamFolderActivate(new TeamFolderIdArg(str));
    }

    /* access modifiers changed from: 0000 */
    public TeamFolderArchiveLaunch teamFolderArchive(TeamFolderArchiveArg teamFolderArchiveArg) throws TeamFolderArchiveErrorException, DbxException {
        try {
            return (TeamFolderArchiveLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/archive", teamFolderArchiveArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamFolderArchiveErrorException("2/team/team_folder/archive", e.getRequestId(), e.getUserMessage(), (TeamFolderArchiveError) e.getErrorValue());
        }
    }

    public TeamFolderArchiveLaunch teamFolderArchive(String str) throws TeamFolderArchiveErrorException, DbxException {
        return teamFolderArchive(new TeamFolderArchiveArg(str));
    }

    public TeamFolderArchiveLaunch teamFolderArchive(String str, boolean z) throws TeamFolderArchiveErrorException, DbxException {
        return teamFolderArchive(new TeamFolderArchiveArg(str, z));
    }

    /* access modifiers changed from: 0000 */
    public TeamFolderArchiveJobStatus teamFolderArchiveCheck(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (TeamFolderArchiveJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/archive/check", pollArg, false, PollArg.Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/team/team_folder/archive/check", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public TeamFolderArchiveJobStatus teamFolderArchiveCheck(String str) throws PollErrorException, DbxException {
        return teamFolderArchiveCheck(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public TeamFolderMetadata teamFolderCreate(TeamFolderCreateArg teamFolderCreateArg) throws TeamFolderCreateErrorException, DbxException {
        try {
            return (TeamFolderMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/create", teamFolderCreateArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamFolderCreateErrorException("2/team/team_folder/create", e.getRequestId(), e.getUserMessage(), (TeamFolderCreateError) e.getErrorValue());
        }
    }

    public TeamFolderMetadata teamFolderCreate(String str) throws TeamFolderCreateErrorException, DbxException {
        return teamFolderCreate(new TeamFolderCreateArg(str));
    }

    public TeamFolderMetadata teamFolderCreate(String str, SyncSettingArg syncSettingArg) throws TeamFolderCreateErrorException, DbxException {
        return teamFolderCreate(new TeamFolderCreateArg(str, syncSettingArg));
    }

    /* access modifiers changed from: 0000 */
    public List<TeamFolderGetInfoItem> teamFolderGetInfo(TeamFolderIdListArg teamFolderIdListArg) throws DbxApiException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/get_info", teamFolderIdListArg, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"team_folder/get_info\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public List<TeamFolderGetInfoItem> teamFolderGetInfo(List<String> list) throws DbxApiException, DbxException {
        return teamFolderGetInfo(new TeamFolderIdListArg(list));
    }

    /* access modifiers changed from: 0000 */
    public TeamFolderListResult teamFolderList(TeamFolderListArg teamFolderListArg) throws TeamFolderListErrorException, DbxException {
        try {
            return (TeamFolderListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/list", teamFolderListArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamFolderListErrorException("2/team/team_folder/list", e.getRequestId(), e.getUserMessage(), (TeamFolderListError) e.getErrorValue());
        }
    }

    public TeamFolderListResult teamFolderList() throws TeamFolderListErrorException, DbxException {
        return teamFolderList(new TeamFolderListArg());
    }

    public TeamFolderListResult teamFolderList(long j) throws TeamFolderListErrorException, DbxException {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            return teamFolderList(new TeamFolderListArg(j));
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    /* access modifiers changed from: 0000 */
    public TeamFolderListResult teamFolderListContinue(TeamFolderListContinueArg teamFolderListContinueArg) throws TeamFolderListContinueErrorException, DbxException {
        try {
            return (TeamFolderListResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/list/continue", teamFolderListContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamFolderListContinueErrorException("2/team/team_folder/list/continue", e.getRequestId(), e.getUserMessage(), (TeamFolderListContinueError) e.getErrorValue());
        }
    }

    public TeamFolderListResult teamFolderListContinue(String str) throws TeamFolderListContinueErrorException, DbxException {
        return teamFolderListContinue(new TeamFolderListContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public void teamFolderPermanentlyDelete(TeamFolderIdArg teamFolderIdArg) throws TeamFolderPermanentlyDeleteErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/permanently_delete", teamFolderIdArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamFolderPermanentlyDeleteErrorException("2/team/team_folder/permanently_delete", e.getRequestId(), e.getUserMessage(), (TeamFolderPermanentlyDeleteError) e.getErrorValue());
        }
    }

    public void teamFolderPermanentlyDelete(String str) throws TeamFolderPermanentlyDeleteErrorException, DbxException {
        teamFolderPermanentlyDelete(new TeamFolderIdArg(str));
    }

    /* access modifiers changed from: 0000 */
    public TeamFolderMetadata teamFolderRename(TeamFolderRenameArg teamFolderRenameArg) throws TeamFolderRenameErrorException, DbxException {
        try {
            return (TeamFolderMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/rename", teamFolderRenameArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamFolderRenameErrorException("2/team/team_folder/rename", e.getRequestId(), e.getUserMessage(), (TeamFolderRenameError) e.getErrorValue());
        }
    }

    public TeamFolderMetadata teamFolderRename(String str, String str2) throws TeamFolderRenameErrorException, DbxException {
        return teamFolderRename(new TeamFolderRenameArg(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public TeamFolderMetadata teamFolderUpdateSyncSettings(TeamFolderUpdateSyncSettingsArg teamFolderUpdateSyncSettingsArg) throws TeamFolderUpdateSyncSettingsErrorException, DbxException {
        try {
            return (TeamFolderMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/team_folder/update_sync_settings", teamFolderUpdateSyncSettingsArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TeamFolderUpdateSyncSettingsErrorException("2/team/team_folder/update_sync_settings", e.getRequestId(), e.getUserMessage(), (TeamFolderUpdateSyncSettingsError) e.getErrorValue());
        }
    }

    public TeamFolderMetadata teamFolderUpdateSyncSettings(String str) throws TeamFolderUpdateSyncSettingsErrorException, DbxException {
        return teamFolderUpdateSyncSettings(new TeamFolderUpdateSyncSettingsArg(str));
    }

    public TeamFolderUpdateSyncSettingsBuilder teamFolderUpdateSyncSettingsBuilder(String str) {
        return new TeamFolderUpdateSyncSettingsBuilder(this, TeamFolderUpdateSyncSettingsArg.newBuilder(str));
    }

    public TokenGetAuthenticatedAdminResult tokenGetAuthenticatedAdmin() throws TokenGetAuthenticatedAdminErrorException, DbxException {
        try {
            return (TokenGetAuthenticatedAdminResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team/token/get_authenticated_admin", null, false, StoneSerializers.void_(), Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TokenGetAuthenticatedAdminErrorException("2/team/token/get_authenticated_admin", e.getRequestId(), e.getUserMessage(), (TokenGetAuthenticatedAdminError) e.getErrorValue());
        }
    }
}
