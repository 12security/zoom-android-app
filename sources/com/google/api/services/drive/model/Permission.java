package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import java.util.List;

public final class Permission extends GenericJson {
    @Key
    private Boolean allowFileDiscovery;
    @Key
    private Boolean deleted;
    @Key
    private String displayName;
    @Key
    private String domain;
    @Key
    private String emailAddress;
    @Key
    private DateTime expirationTime;
    @Key

    /* renamed from: id */
    private String f212id;
    @Key
    private String kind;
    @Key
    private String photoLink;
    @Key
    private String role;
    @Key
    private List<TeamDrivePermissionDetails> teamDrivePermissionDetails;
    @Key
    private String type;

    public static final class TeamDrivePermissionDetails extends GenericJson {
        @Key
        private Boolean inherited;
        @Key
        private String inheritedFrom;
        @Key
        private String role;
        @Key
        private String teamDrivePermissionType;

        public Boolean getInherited() {
            return this.inherited;
        }

        public TeamDrivePermissionDetails setInherited(Boolean bool) {
            this.inherited = bool;
            return this;
        }

        public String getInheritedFrom() {
            return this.inheritedFrom;
        }

        public TeamDrivePermissionDetails setInheritedFrom(String str) {
            this.inheritedFrom = str;
            return this;
        }

        public String getRole() {
            return this.role;
        }

        public TeamDrivePermissionDetails setRole(String str) {
            this.role = str;
            return this;
        }

        public String getTeamDrivePermissionType() {
            return this.teamDrivePermissionType;
        }

        public TeamDrivePermissionDetails setTeamDrivePermissionType(String str) {
            this.teamDrivePermissionType = str;
            return this;
        }

        public TeamDrivePermissionDetails set(String str, Object obj) {
            return (TeamDrivePermissionDetails) super.set(str, obj);
        }

        public TeamDrivePermissionDetails clone() {
            return (TeamDrivePermissionDetails) super.clone();
        }
    }

    static {
        Data.nullOf(TeamDrivePermissionDetails.class);
    }

    public Boolean getAllowFileDiscovery() {
        return this.allowFileDiscovery;
    }

    public Permission setAllowFileDiscovery(Boolean bool) {
        this.allowFileDiscovery = bool;
        return this;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Permission setDeleted(Boolean bool) {
        this.deleted = bool;
        return this;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Permission setDisplayName(String str) {
        this.displayName = str;
        return this;
    }

    public String getDomain() {
        return this.domain;
    }

    public Permission setDomain(String str) {
        this.domain = str;
        return this;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Permission setEmailAddress(String str) {
        this.emailAddress = str;
        return this;
    }

    public DateTime getExpirationTime() {
        return this.expirationTime;
    }

    public Permission setExpirationTime(DateTime dateTime) {
        this.expirationTime = dateTime;
        return this;
    }

    public String getId() {
        return this.f212id;
    }

    public Permission setId(String str) {
        this.f212id = str;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public Permission setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getPhotoLink() {
        return this.photoLink;
    }

    public Permission setPhotoLink(String str) {
        this.photoLink = str;
        return this;
    }

    public String getRole() {
        return this.role;
    }

    public Permission setRole(String str) {
        this.role = str;
        return this;
    }

    public List<TeamDrivePermissionDetails> getTeamDrivePermissionDetails() {
        return this.teamDrivePermissionDetails;
    }

    public Permission setTeamDrivePermissionDetails(List<TeamDrivePermissionDetails> list) {
        this.teamDrivePermissionDetails = list;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Permission setType(String str) {
        this.type = str;
        return this;
    }

    public Permission set(String str, Object obj) {
        return (Permission) super.set(str, obj);
    }

    public Permission clone() {
        return (Permission) super.clone();
    }
}
