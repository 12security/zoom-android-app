package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public final class TeamDrive extends GenericJson {
    @Key
    private BackgroundImageFile backgroundImageFile;
    @Key
    private String backgroundImageLink;
    @Key
    private Capabilities capabilities;
    @Key
    private String colorRgb;
    @Key
    private DateTime createdTime;
    @Key

    /* renamed from: id */
    private String f215id;
    @Key
    private String kind;
    @Key
    private String name;
    @Key
    private Restrictions restrictions;
    @Key
    private String themeId;

    public static final class BackgroundImageFile extends GenericJson {
        @Key

        /* renamed from: id */
        private String f216id;
        @Key
        private Float width;
        @Key
        private Float xCoordinate;
        @Key
        private Float yCoordinate;

        public String getId() {
            return this.f216id;
        }

        public BackgroundImageFile setId(String str) {
            this.f216id = str;
            return this;
        }

        public Float getWidth() {
            return this.width;
        }

        public BackgroundImageFile setWidth(Float f) {
            this.width = f;
            return this;
        }

        public Float getXCoordinate() {
            return this.xCoordinate;
        }

        public BackgroundImageFile setXCoordinate(Float f) {
            this.xCoordinate = f;
            return this;
        }

        public Float getYCoordinate() {
            return this.yCoordinate;
        }

        public BackgroundImageFile setYCoordinate(Float f) {
            this.yCoordinate = f;
            return this;
        }

        public BackgroundImageFile set(String str, Object obj) {
            return (BackgroundImageFile) super.set(str, obj);
        }

        public BackgroundImageFile clone() {
            return (BackgroundImageFile) super.clone();
        }
    }

    public static final class Capabilities extends GenericJson {
        @Key
        private Boolean canAddChildren;
        @Key
        private Boolean canChangeCopyRequiresWriterPermissionRestriction;
        @Key
        private Boolean canChangeDomainUsersOnlyRestriction;
        @Key
        private Boolean canChangeTeamDriveBackground;
        @Key
        private Boolean canChangeTeamMembersOnlyRestriction;
        @Key
        private Boolean canComment;
        @Key
        private Boolean canCopy;
        @Key
        private Boolean canDeleteChildren;
        @Key
        private Boolean canDeleteTeamDrive;
        @Key
        private Boolean canDownload;
        @Key
        private Boolean canEdit;
        @Key
        private Boolean canListChildren;
        @Key
        private Boolean canManageMembers;
        @Key
        private Boolean canReadRevisions;
        @Key
        private Boolean canRemoveChildren;
        @Key
        private Boolean canRename;
        @Key
        private Boolean canRenameTeamDrive;
        @Key
        private Boolean canShare;
        @Key
        private Boolean canTrashChildren;

        public Boolean getCanAddChildren() {
            return this.canAddChildren;
        }

        public Capabilities setCanAddChildren(Boolean bool) {
            this.canAddChildren = bool;
            return this;
        }

        public Boolean getCanChangeCopyRequiresWriterPermissionRestriction() {
            return this.canChangeCopyRequiresWriterPermissionRestriction;
        }

        public Capabilities setCanChangeCopyRequiresWriterPermissionRestriction(Boolean bool) {
            this.canChangeCopyRequiresWriterPermissionRestriction = bool;
            return this;
        }

        public Boolean getCanChangeDomainUsersOnlyRestriction() {
            return this.canChangeDomainUsersOnlyRestriction;
        }

        public Capabilities setCanChangeDomainUsersOnlyRestriction(Boolean bool) {
            this.canChangeDomainUsersOnlyRestriction = bool;
            return this;
        }

        public Boolean getCanChangeTeamDriveBackground() {
            return this.canChangeTeamDriveBackground;
        }

        public Capabilities setCanChangeTeamDriveBackground(Boolean bool) {
            this.canChangeTeamDriveBackground = bool;
            return this;
        }

        public Boolean getCanChangeTeamMembersOnlyRestriction() {
            return this.canChangeTeamMembersOnlyRestriction;
        }

        public Capabilities setCanChangeTeamMembersOnlyRestriction(Boolean bool) {
            this.canChangeTeamMembersOnlyRestriction = bool;
            return this;
        }

        public Boolean getCanComment() {
            return this.canComment;
        }

        public Capabilities setCanComment(Boolean bool) {
            this.canComment = bool;
            return this;
        }

        public Boolean getCanCopy() {
            return this.canCopy;
        }

        public Capabilities setCanCopy(Boolean bool) {
            this.canCopy = bool;
            return this;
        }

        public Boolean getCanDeleteChildren() {
            return this.canDeleteChildren;
        }

        public Capabilities setCanDeleteChildren(Boolean bool) {
            this.canDeleteChildren = bool;
            return this;
        }

        public Boolean getCanDeleteTeamDrive() {
            return this.canDeleteTeamDrive;
        }

        public Capabilities setCanDeleteTeamDrive(Boolean bool) {
            this.canDeleteTeamDrive = bool;
            return this;
        }

        public Boolean getCanDownload() {
            return this.canDownload;
        }

        public Capabilities setCanDownload(Boolean bool) {
            this.canDownload = bool;
            return this;
        }

        public Boolean getCanEdit() {
            return this.canEdit;
        }

        public Capabilities setCanEdit(Boolean bool) {
            this.canEdit = bool;
            return this;
        }

        public Boolean getCanListChildren() {
            return this.canListChildren;
        }

        public Capabilities setCanListChildren(Boolean bool) {
            this.canListChildren = bool;
            return this;
        }

        public Boolean getCanManageMembers() {
            return this.canManageMembers;
        }

        public Capabilities setCanManageMembers(Boolean bool) {
            this.canManageMembers = bool;
            return this;
        }

        public Boolean getCanReadRevisions() {
            return this.canReadRevisions;
        }

        public Capabilities setCanReadRevisions(Boolean bool) {
            this.canReadRevisions = bool;
            return this;
        }

        public Boolean getCanRemoveChildren() {
            return this.canRemoveChildren;
        }

        public Capabilities setCanRemoveChildren(Boolean bool) {
            this.canRemoveChildren = bool;
            return this;
        }

        public Boolean getCanRename() {
            return this.canRename;
        }

        public Capabilities setCanRename(Boolean bool) {
            this.canRename = bool;
            return this;
        }

        public Boolean getCanRenameTeamDrive() {
            return this.canRenameTeamDrive;
        }

        public Capabilities setCanRenameTeamDrive(Boolean bool) {
            this.canRenameTeamDrive = bool;
            return this;
        }

        public Boolean getCanShare() {
            return this.canShare;
        }

        public Capabilities setCanShare(Boolean bool) {
            this.canShare = bool;
            return this;
        }

        public Boolean getCanTrashChildren() {
            return this.canTrashChildren;
        }

        public Capabilities setCanTrashChildren(Boolean bool) {
            this.canTrashChildren = bool;
            return this;
        }

        public Capabilities set(String str, Object obj) {
            return (Capabilities) super.set(str, obj);
        }

        public Capabilities clone() {
            return (Capabilities) super.clone();
        }
    }

    public static final class Restrictions extends GenericJson {
        @Key
        private Boolean adminManagedRestrictions;
        @Key
        private Boolean copyRequiresWriterPermission;
        @Key
        private Boolean domainUsersOnly;
        @Key
        private Boolean teamMembersOnly;

        public Boolean getAdminManagedRestrictions() {
            return this.adminManagedRestrictions;
        }

        public Restrictions setAdminManagedRestrictions(Boolean bool) {
            this.adminManagedRestrictions = bool;
            return this;
        }

        public Boolean getCopyRequiresWriterPermission() {
            return this.copyRequiresWriterPermission;
        }

        public Restrictions setCopyRequiresWriterPermission(Boolean bool) {
            this.copyRequiresWriterPermission = bool;
            return this;
        }

        public Boolean getDomainUsersOnly() {
            return this.domainUsersOnly;
        }

        public Restrictions setDomainUsersOnly(Boolean bool) {
            this.domainUsersOnly = bool;
            return this;
        }

        public Boolean getTeamMembersOnly() {
            return this.teamMembersOnly;
        }

        public Restrictions setTeamMembersOnly(Boolean bool) {
            this.teamMembersOnly = bool;
            return this;
        }

        public Restrictions set(String str, Object obj) {
            return (Restrictions) super.set(str, obj);
        }

        public Restrictions clone() {
            return (Restrictions) super.clone();
        }
    }

    public BackgroundImageFile getBackgroundImageFile() {
        return this.backgroundImageFile;
    }

    public TeamDrive setBackgroundImageFile(BackgroundImageFile backgroundImageFile2) {
        this.backgroundImageFile = backgroundImageFile2;
        return this;
    }

    public String getBackgroundImageLink() {
        return this.backgroundImageLink;
    }

    public TeamDrive setBackgroundImageLink(String str) {
        this.backgroundImageLink = str;
        return this;
    }

    public Capabilities getCapabilities() {
        return this.capabilities;
    }

    public TeamDrive setCapabilities(Capabilities capabilities2) {
        this.capabilities = capabilities2;
        return this;
    }

    public String getColorRgb() {
        return this.colorRgb;
    }

    public TeamDrive setColorRgb(String str) {
        this.colorRgb = str;
        return this;
    }

    public DateTime getCreatedTime() {
        return this.createdTime;
    }

    public TeamDrive setCreatedTime(DateTime dateTime) {
        this.createdTime = dateTime;
        return this;
    }

    public String getId() {
        return this.f215id;
    }

    public TeamDrive setId(String str) {
        this.f215id = str;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public TeamDrive setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TeamDrive setName(String str) {
        this.name = str;
        return this;
    }

    public Restrictions getRestrictions() {
        return this.restrictions;
    }

    public TeamDrive setRestrictions(Restrictions restrictions2) {
        this.restrictions = restrictions2;
        return this;
    }

    public String getThemeId() {
        return this.themeId;
    }

    public TeamDrive setThemeId(String str) {
        this.themeId = str;
        return this;
    }

    public TeamDrive set(String str, Object obj) {
        return (TeamDrive) super.set(str, obj);
    }

    public TeamDrive clone() {
        return (TeamDrive) super.clone();
    }
}
