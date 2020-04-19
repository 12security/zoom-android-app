package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;
import java.util.Map;

public final class About extends GenericJson {
    @Key
    private Boolean appInstalled;
    @Key
    private Boolean canCreateTeamDrives;
    @Key
    private Map<String, List<String>> exportFormats;
    @Key
    private List<String> folderColorPalette;
    @Key
    private Map<String, List<String>> importFormats;
    @Key
    private String kind;
    @JsonString
    @Key
    private Map<String, Long> maxImportSizes;
    @JsonString
    @Key
    private Long maxUploadSize;
    @Key
    private StorageQuota storageQuota;
    @Key
    private List<TeamDriveThemes> teamDriveThemes;
    @Key
    private User user;

    public static final class StorageQuota extends GenericJson {
        @JsonString
        @Key
        private Long limit;
        @JsonString
        @Key
        private Long usage;
        @JsonString
        @Key
        private Long usageInDrive;
        @JsonString
        @Key
        private Long usageInDriveTrash;

        public Long getLimit() {
            return this.limit;
        }

        public StorageQuota setLimit(Long l) {
            this.limit = l;
            return this;
        }

        public Long getUsage() {
            return this.usage;
        }

        public StorageQuota setUsage(Long l) {
            this.usage = l;
            return this;
        }

        public Long getUsageInDrive() {
            return this.usageInDrive;
        }

        public StorageQuota setUsageInDrive(Long l) {
            this.usageInDrive = l;
            return this;
        }

        public Long getUsageInDriveTrash() {
            return this.usageInDriveTrash;
        }

        public StorageQuota setUsageInDriveTrash(Long l) {
            this.usageInDriveTrash = l;
            return this;
        }

        public StorageQuota set(String str, Object obj) {
            return (StorageQuota) super.set(str, obj);
        }

        public StorageQuota clone() {
            return (StorageQuota) super.clone();
        }
    }

    public static final class TeamDriveThemes extends GenericJson {
        @Key
        private String backgroundImageLink;
        @Key
        private String colorRgb;
        @Key

        /* renamed from: id */
        private String f208id;

        public String getBackgroundImageLink() {
            return this.backgroundImageLink;
        }

        public TeamDriveThemes setBackgroundImageLink(String str) {
            this.backgroundImageLink = str;
            return this;
        }

        public String getColorRgb() {
            return this.colorRgb;
        }

        public TeamDriveThemes setColorRgb(String str) {
            this.colorRgb = str;
            return this;
        }

        public String getId() {
            return this.f208id;
        }

        public TeamDriveThemes setId(String str) {
            this.f208id = str;
            return this;
        }

        public TeamDriveThemes set(String str, Object obj) {
            return (TeamDriveThemes) super.set(str, obj);
        }

        public TeamDriveThemes clone() {
            return (TeamDriveThemes) super.clone();
        }
    }

    static {
        Data.nullOf(TeamDriveThemes.class);
    }

    public Boolean getAppInstalled() {
        return this.appInstalled;
    }

    public About setAppInstalled(Boolean bool) {
        this.appInstalled = bool;
        return this;
    }

    public Boolean getCanCreateTeamDrives() {
        return this.canCreateTeamDrives;
    }

    public About setCanCreateTeamDrives(Boolean bool) {
        this.canCreateTeamDrives = bool;
        return this;
    }

    public Map<String, List<String>> getExportFormats() {
        return this.exportFormats;
    }

    public About setExportFormats(Map<String, List<String>> map) {
        this.exportFormats = map;
        return this;
    }

    public List<String> getFolderColorPalette() {
        return this.folderColorPalette;
    }

    public About setFolderColorPalette(List<String> list) {
        this.folderColorPalette = list;
        return this;
    }

    public Map<String, List<String>> getImportFormats() {
        return this.importFormats;
    }

    public About setImportFormats(Map<String, List<String>> map) {
        this.importFormats = map;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public About setKind(String str) {
        this.kind = str;
        return this;
    }

    public Map<String, Long> getMaxImportSizes() {
        return this.maxImportSizes;
    }

    public About setMaxImportSizes(Map<String, Long> map) {
        this.maxImportSizes = map;
        return this;
    }

    public Long getMaxUploadSize() {
        return this.maxUploadSize;
    }

    public About setMaxUploadSize(Long l) {
        this.maxUploadSize = l;
        return this;
    }

    public StorageQuota getStorageQuota() {
        return this.storageQuota;
    }

    public About setStorageQuota(StorageQuota storageQuota2) {
        this.storageQuota = storageQuota2;
        return this;
    }

    public List<TeamDriveThemes> getTeamDriveThemes() {
        return this.teamDriveThemes;
    }

    public About setTeamDriveThemes(List<TeamDriveThemes> list) {
        this.teamDriveThemes = list;
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public About setUser(User user2) {
        this.user = user2;
        return this;
    }

    public About set(String str, Object obj) {
        return (About) super.set(str, obj);
    }

    public About clone() {
        return (About) super.clone();
    }
}
