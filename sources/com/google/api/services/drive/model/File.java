package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Base64;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import java.util.List;
import java.util.Map;

public final class File extends GenericJson {
    @Key
    private Map<String, String> appProperties;
    @Key
    private Capabilities capabilities;
    @Key
    private ContentHints contentHints;
    @Key
    private Boolean copyRequiresWriterPermission;
    @Key
    private DateTime createdTime;
    @Key
    private String description;
    @Key
    private Boolean explicitlyTrashed;
    @Key
    private Map<String, String> exportLinks;
    @Key
    private String fileExtension;
    @Key
    private String folderColorRgb;
    @Key
    private String fullFileExtension;
    @Key
    private Boolean hasAugmentedPermissions;
    @Key
    private Boolean hasThumbnail;
    @Key
    private String headRevisionId;
    @Key
    private String iconLink;
    @Key

    /* renamed from: id */
    private String f211id;
    @Key
    private ImageMediaMetadata imageMediaMetadata;
    @Key
    private Boolean isAppAuthorized;
    @Key
    private String kind;
    @Key
    private User lastModifyingUser;
    @Key
    private String md5Checksum;
    @Key
    private String mimeType;
    @Key
    private Boolean modifiedByMe;
    @Key
    private DateTime modifiedByMeTime;
    @Key
    private DateTime modifiedTime;
    @Key
    private String name;
    @Key
    private String originalFilename;
    @Key
    private Boolean ownedByMe;
    @Key
    private List<User> owners;
    @Key
    private List<String> parents;
    @Key
    private List<String> permissionIds;
    @Key
    private List<Permission> permissions;
    @Key
    private Map<String, String> properties;
    @JsonString
    @Key
    private Long quotaBytesUsed;
    @Key
    private Boolean shared;
    @Key
    private DateTime sharedWithMeTime;
    @Key
    private User sharingUser;
    @JsonString
    @Key
    private Long size;
    @Key
    private List<String> spaces;
    @Key
    private Boolean starred;
    @Key
    private String teamDriveId;
    @Key
    private String thumbnailLink;
    @JsonString
    @Key
    private Long thumbnailVersion;
    @Key
    private Boolean trashed;
    @Key
    private DateTime trashedTime;
    @Key
    private User trashingUser;
    @JsonString
    @Key
    private Long version;
    @Key
    private VideoMediaMetadata videoMediaMetadata;
    @Key
    private Boolean viewedByMe;
    @Key
    private DateTime viewedByMeTime;
    @Key
    private Boolean viewersCanCopyContent;
    @Key
    private String webContentLink;
    @Key
    private String webViewLink;
    @Key
    private Boolean writersCanShare;

    public static final class Capabilities extends GenericJson {
        @Key
        private Boolean canAddChildren;
        @Key
        private Boolean canChangeCopyRequiresWriterPermission;
        @Key
        private Boolean canChangeViewersCanCopyContent;
        @Key
        private Boolean canComment;
        @Key
        private Boolean canCopy;
        @Key
        private Boolean canDelete;
        @Key
        private Boolean canDeleteChildren;
        @Key
        private Boolean canDownload;
        @Key
        private Boolean canEdit;
        @Key
        private Boolean canListChildren;
        @Key
        private Boolean canMoveChildrenOutOfTeamDrive;
        @Key
        private Boolean canMoveChildrenWithinTeamDrive;
        @Key
        private Boolean canMoveItemIntoTeamDrive;
        @Key
        private Boolean canMoveItemOutOfTeamDrive;
        @Key
        private Boolean canMoveItemWithinTeamDrive;
        @Key
        private Boolean canMoveTeamDriveItem;
        @Key
        private Boolean canReadRevisions;
        @Key
        private Boolean canReadTeamDrive;
        @Key
        private Boolean canRemoveChildren;
        @Key
        private Boolean canRename;
        @Key
        private Boolean canShare;
        @Key
        private Boolean canTrash;
        @Key
        private Boolean canTrashChildren;
        @Key
        private Boolean canUntrash;

        public Boolean getCanAddChildren() {
            return this.canAddChildren;
        }

        public Capabilities setCanAddChildren(Boolean bool) {
            this.canAddChildren = bool;
            return this;
        }

        public Boolean getCanChangeCopyRequiresWriterPermission() {
            return this.canChangeCopyRequiresWriterPermission;
        }

        public Capabilities setCanChangeCopyRequiresWriterPermission(Boolean bool) {
            this.canChangeCopyRequiresWriterPermission = bool;
            return this;
        }

        public Boolean getCanChangeViewersCanCopyContent() {
            return this.canChangeViewersCanCopyContent;
        }

        public Capabilities setCanChangeViewersCanCopyContent(Boolean bool) {
            this.canChangeViewersCanCopyContent = bool;
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

        public Boolean getCanDelete() {
            return this.canDelete;
        }

        public Capabilities setCanDelete(Boolean bool) {
            this.canDelete = bool;
            return this;
        }

        public Boolean getCanDeleteChildren() {
            return this.canDeleteChildren;
        }

        public Capabilities setCanDeleteChildren(Boolean bool) {
            this.canDeleteChildren = bool;
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

        public Boolean getCanMoveChildrenOutOfTeamDrive() {
            return this.canMoveChildrenOutOfTeamDrive;
        }

        public Capabilities setCanMoveChildrenOutOfTeamDrive(Boolean bool) {
            this.canMoveChildrenOutOfTeamDrive = bool;
            return this;
        }

        public Boolean getCanMoveChildrenWithinTeamDrive() {
            return this.canMoveChildrenWithinTeamDrive;
        }

        public Capabilities setCanMoveChildrenWithinTeamDrive(Boolean bool) {
            this.canMoveChildrenWithinTeamDrive = bool;
            return this;
        }

        public Boolean getCanMoveItemIntoTeamDrive() {
            return this.canMoveItemIntoTeamDrive;
        }

        public Capabilities setCanMoveItemIntoTeamDrive(Boolean bool) {
            this.canMoveItemIntoTeamDrive = bool;
            return this;
        }

        public Boolean getCanMoveItemOutOfTeamDrive() {
            return this.canMoveItemOutOfTeamDrive;
        }

        public Capabilities setCanMoveItemOutOfTeamDrive(Boolean bool) {
            this.canMoveItemOutOfTeamDrive = bool;
            return this;
        }

        public Boolean getCanMoveItemWithinTeamDrive() {
            return this.canMoveItemWithinTeamDrive;
        }

        public Capabilities setCanMoveItemWithinTeamDrive(Boolean bool) {
            this.canMoveItemWithinTeamDrive = bool;
            return this;
        }

        public Boolean getCanMoveTeamDriveItem() {
            return this.canMoveTeamDriveItem;
        }

        public Capabilities setCanMoveTeamDriveItem(Boolean bool) {
            this.canMoveTeamDriveItem = bool;
            return this;
        }

        public Boolean getCanReadRevisions() {
            return this.canReadRevisions;
        }

        public Capabilities setCanReadRevisions(Boolean bool) {
            this.canReadRevisions = bool;
            return this;
        }

        public Boolean getCanReadTeamDrive() {
            return this.canReadTeamDrive;
        }

        public Capabilities setCanReadTeamDrive(Boolean bool) {
            this.canReadTeamDrive = bool;
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

        public Boolean getCanShare() {
            return this.canShare;
        }

        public Capabilities setCanShare(Boolean bool) {
            this.canShare = bool;
            return this;
        }

        public Boolean getCanTrash() {
            return this.canTrash;
        }

        public Capabilities setCanTrash(Boolean bool) {
            this.canTrash = bool;
            return this;
        }

        public Boolean getCanTrashChildren() {
            return this.canTrashChildren;
        }

        public Capabilities setCanTrashChildren(Boolean bool) {
            this.canTrashChildren = bool;
            return this;
        }

        public Boolean getCanUntrash() {
            return this.canUntrash;
        }

        public Capabilities setCanUntrash(Boolean bool) {
            this.canUntrash = bool;
            return this;
        }

        public Capabilities set(String str, Object obj) {
            return (Capabilities) super.set(str, obj);
        }

        public Capabilities clone() {
            return (Capabilities) super.clone();
        }
    }

    public static final class ContentHints extends GenericJson {
        @Key
        private String indexableText;
        @Key
        private Thumbnail thumbnail;

        public static final class Thumbnail extends GenericJson {
            @Key
            private String image;
            @Key
            private String mimeType;

            public String getImage() {
                return this.image;
            }

            public byte[] decodeImage() {
                return Base64.decodeBase64(this.image);
            }

            public Thumbnail setImage(String str) {
                this.image = str;
                return this;
            }

            public Thumbnail encodeImage(byte[] bArr) {
                this.image = Base64.encodeBase64URLSafeString(bArr);
                return this;
            }

            public String getMimeType() {
                return this.mimeType;
            }

            public Thumbnail setMimeType(String str) {
                this.mimeType = str;
                return this;
            }

            public Thumbnail set(String str, Object obj) {
                return (Thumbnail) super.set(str, obj);
            }

            public Thumbnail clone() {
                return (Thumbnail) super.clone();
            }
        }

        public String getIndexableText() {
            return this.indexableText;
        }

        public ContentHints setIndexableText(String str) {
            this.indexableText = str;
            return this;
        }

        public Thumbnail getThumbnail() {
            return this.thumbnail;
        }

        public ContentHints setThumbnail(Thumbnail thumbnail2) {
            this.thumbnail = thumbnail2;
            return this;
        }

        public ContentHints set(String str, Object obj) {
            return (ContentHints) super.set(str, obj);
        }

        public ContentHints clone() {
            return (ContentHints) super.clone();
        }
    }

    public static final class ImageMediaMetadata extends GenericJson {
        @Key
        private Float aperture;
        @Key
        private String cameraMake;
        @Key
        private String cameraModel;
        @Key
        private String colorSpace;
        @Key
        private Float exposureBias;
        @Key
        private String exposureMode;
        @Key
        private Float exposureTime;
        @Key
        private Boolean flashUsed;
        @Key
        private Float focalLength;
        @Key
        private Integer height;
        @Key
        private Integer isoSpeed;
        @Key
        private String lens;
        @Key
        private Location location;
        @Key
        private Float maxApertureValue;
        @Key
        private String meteringMode;
        @Key
        private Integer rotation;
        @Key
        private String sensor;
        @Key
        private Integer subjectDistance;
        @Key
        private String time;
        @Key
        private String whiteBalance;
        @Key
        private Integer width;

        public static final class Location extends GenericJson {
            @Key
            private Double altitude;
            @Key
            private Double latitude;
            @Key
            private Double longitude;

            public Double getAltitude() {
                return this.altitude;
            }

            public Location setAltitude(Double d) {
                this.altitude = d;
                return this;
            }

            public Double getLatitude() {
                return this.latitude;
            }

            public Location setLatitude(Double d) {
                this.latitude = d;
                return this;
            }

            public Double getLongitude() {
                return this.longitude;
            }

            public Location setLongitude(Double d) {
                this.longitude = d;
                return this;
            }

            public Location set(String str, Object obj) {
                return (Location) super.set(str, obj);
            }

            public Location clone() {
                return (Location) super.clone();
            }
        }

        public Float getAperture() {
            return this.aperture;
        }

        public ImageMediaMetadata setAperture(Float f) {
            this.aperture = f;
            return this;
        }

        public String getCameraMake() {
            return this.cameraMake;
        }

        public ImageMediaMetadata setCameraMake(String str) {
            this.cameraMake = str;
            return this;
        }

        public String getCameraModel() {
            return this.cameraModel;
        }

        public ImageMediaMetadata setCameraModel(String str) {
            this.cameraModel = str;
            return this;
        }

        public String getColorSpace() {
            return this.colorSpace;
        }

        public ImageMediaMetadata setColorSpace(String str) {
            this.colorSpace = str;
            return this;
        }

        public Float getExposureBias() {
            return this.exposureBias;
        }

        public ImageMediaMetadata setExposureBias(Float f) {
            this.exposureBias = f;
            return this;
        }

        public String getExposureMode() {
            return this.exposureMode;
        }

        public ImageMediaMetadata setExposureMode(String str) {
            this.exposureMode = str;
            return this;
        }

        public Float getExposureTime() {
            return this.exposureTime;
        }

        public ImageMediaMetadata setExposureTime(Float f) {
            this.exposureTime = f;
            return this;
        }

        public Boolean getFlashUsed() {
            return this.flashUsed;
        }

        public ImageMediaMetadata setFlashUsed(Boolean bool) {
            this.flashUsed = bool;
            return this;
        }

        public Float getFocalLength() {
            return this.focalLength;
        }

        public ImageMediaMetadata setFocalLength(Float f) {
            this.focalLength = f;
            return this;
        }

        public Integer getHeight() {
            return this.height;
        }

        public ImageMediaMetadata setHeight(Integer num) {
            this.height = num;
            return this;
        }

        public Integer getIsoSpeed() {
            return this.isoSpeed;
        }

        public ImageMediaMetadata setIsoSpeed(Integer num) {
            this.isoSpeed = num;
            return this;
        }

        public String getLens() {
            return this.lens;
        }

        public ImageMediaMetadata setLens(String str) {
            this.lens = str;
            return this;
        }

        public Location getLocation() {
            return this.location;
        }

        public ImageMediaMetadata setLocation(Location location2) {
            this.location = location2;
            return this;
        }

        public Float getMaxApertureValue() {
            return this.maxApertureValue;
        }

        public ImageMediaMetadata setMaxApertureValue(Float f) {
            this.maxApertureValue = f;
            return this;
        }

        public String getMeteringMode() {
            return this.meteringMode;
        }

        public ImageMediaMetadata setMeteringMode(String str) {
            this.meteringMode = str;
            return this;
        }

        public Integer getRotation() {
            return this.rotation;
        }

        public ImageMediaMetadata setRotation(Integer num) {
            this.rotation = num;
            return this;
        }

        public String getSensor() {
            return this.sensor;
        }

        public ImageMediaMetadata setSensor(String str) {
            this.sensor = str;
            return this;
        }

        public Integer getSubjectDistance() {
            return this.subjectDistance;
        }

        public ImageMediaMetadata setSubjectDistance(Integer num) {
            this.subjectDistance = num;
            return this;
        }

        public String getTime() {
            return this.time;
        }

        public ImageMediaMetadata setTime(String str) {
            this.time = str;
            return this;
        }

        public String getWhiteBalance() {
            return this.whiteBalance;
        }

        public ImageMediaMetadata setWhiteBalance(String str) {
            this.whiteBalance = str;
            return this;
        }

        public Integer getWidth() {
            return this.width;
        }

        public ImageMediaMetadata setWidth(Integer num) {
            this.width = num;
            return this;
        }

        public ImageMediaMetadata set(String str, Object obj) {
            return (ImageMediaMetadata) super.set(str, obj);
        }

        public ImageMediaMetadata clone() {
            return (ImageMediaMetadata) super.clone();
        }
    }

    public static final class VideoMediaMetadata extends GenericJson {
        @JsonString
        @Key
        private Long durationMillis;
        @Key
        private Integer height;
        @Key
        private Integer width;

        public Long getDurationMillis() {
            return this.durationMillis;
        }

        public VideoMediaMetadata setDurationMillis(Long l) {
            this.durationMillis = l;
            return this;
        }

        public Integer getHeight() {
            return this.height;
        }

        public VideoMediaMetadata setHeight(Integer num) {
            this.height = num;
            return this;
        }

        public Integer getWidth() {
            return this.width;
        }

        public VideoMediaMetadata setWidth(Integer num) {
            this.width = num;
            return this;
        }

        public VideoMediaMetadata set(String str, Object obj) {
            return (VideoMediaMetadata) super.set(str, obj);
        }

        public VideoMediaMetadata clone() {
            return (VideoMediaMetadata) super.clone();
        }
    }

    public Map<String, String> getAppProperties() {
        return this.appProperties;
    }

    public File setAppProperties(Map<String, String> map) {
        this.appProperties = map;
        return this;
    }

    public Capabilities getCapabilities() {
        return this.capabilities;
    }

    public File setCapabilities(Capabilities capabilities2) {
        this.capabilities = capabilities2;
        return this;
    }

    public ContentHints getContentHints() {
        return this.contentHints;
    }

    public File setContentHints(ContentHints contentHints2) {
        this.contentHints = contentHints2;
        return this;
    }

    public Boolean getCopyRequiresWriterPermission() {
        return this.copyRequiresWriterPermission;
    }

    public File setCopyRequiresWriterPermission(Boolean bool) {
        this.copyRequiresWriterPermission = bool;
        return this;
    }

    public DateTime getCreatedTime() {
        return this.createdTime;
    }

    public File setCreatedTime(DateTime dateTime) {
        this.createdTime = dateTime;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public File setDescription(String str) {
        this.description = str;
        return this;
    }

    public Boolean getExplicitlyTrashed() {
        return this.explicitlyTrashed;
    }

    public File setExplicitlyTrashed(Boolean bool) {
        this.explicitlyTrashed = bool;
        return this;
    }

    public Map<String, String> getExportLinks() {
        return this.exportLinks;
    }

    public File setExportLinks(Map<String, String> map) {
        this.exportLinks = map;
        return this;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public File setFileExtension(String str) {
        this.fileExtension = str;
        return this;
    }

    public String getFolderColorRgb() {
        return this.folderColorRgb;
    }

    public File setFolderColorRgb(String str) {
        this.folderColorRgb = str;
        return this;
    }

    public String getFullFileExtension() {
        return this.fullFileExtension;
    }

    public File setFullFileExtension(String str) {
        this.fullFileExtension = str;
        return this;
    }

    public Boolean getHasAugmentedPermissions() {
        return this.hasAugmentedPermissions;
    }

    public File setHasAugmentedPermissions(Boolean bool) {
        this.hasAugmentedPermissions = bool;
        return this;
    }

    public Boolean getHasThumbnail() {
        return this.hasThumbnail;
    }

    public File setHasThumbnail(Boolean bool) {
        this.hasThumbnail = bool;
        return this;
    }

    public String getHeadRevisionId() {
        return this.headRevisionId;
    }

    public File setHeadRevisionId(String str) {
        this.headRevisionId = str;
        return this;
    }

    public String getIconLink() {
        return this.iconLink;
    }

    public File setIconLink(String str) {
        this.iconLink = str;
        return this;
    }

    public String getId() {
        return this.f211id;
    }

    public File setId(String str) {
        this.f211id = str;
        return this;
    }

    public ImageMediaMetadata getImageMediaMetadata() {
        return this.imageMediaMetadata;
    }

    public File setImageMediaMetadata(ImageMediaMetadata imageMediaMetadata2) {
        this.imageMediaMetadata = imageMediaMetadata2;
        return this;
    }

    public Boolean getIsAppAuthorized() {
        return this.isAppAuthorized;
    }

    public File setIsAppAuthorized(Boolean bool) {
        this.isAppAuthorized = bool;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public File setKind(String str) {
        this.kind = str;
        return this;
    }

    public User getLastModifyingUser() {
        return this.lastModifyingUser;
    }

    public File setLastModifyingUser(User user) {
        this.lastModifyingUser = user;
        return this;
    }

    public String getMd5Checksum() {
        return this.md5Checksum;
    }

    public File setMd5Checksum(String str) {
        this.md5Checksum = str;
        return this;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public File setMimeType(String str) {
        this.mimeType = str;
        return this;
    }

    public Boolean getModifiedByMe() {
        return this.modifiedByMe;
    }

    public File setModifiedByMe(Boolean bool) {
        this.modifiedByMe = bool;
        return this;
    }

    public DateTime getModifiedByMeTime() {
        return this.modifiedByMeTime;
    }

    public File setModifiedByMeTime(DateTime dateTime) {
        this.modifiedByMeTime = dateTime;
        return this;
    }

    public DateTime getModifiedTime() {
        return this.modifiedTime;
    }

    public File setModifiedTime(DateTime dateTime) {
        this.modifiedTime = dateTime;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public File setName(String str) {
        this.name = str;
        return this;
    }

    public String getOriginalFilename() {
        return this.originalFilename;
    }

    public File setOriginalFilename(String str) {
        this.originalFilename = str;
        return this;
    }

    public Boolean getOwnedByMe() {
        return this.ownedByMe;
    }

    public File setOwnedByMe(Boolean bool) {
        this.ownedByMe = bool;
        return this;
    }

    public List<User> getOwners() {
        return this.owners;
    }

    public File setOwners(List<User> list) {
        this.owners = list;
        return this;
    }

    public List<String> getParents() {
        return this.parents;
    }

    public File setParents(List<String> list) {
        this.parents = list;
        return this;
    }

    public List<String> getPermissionIds() {
        return this.permissionIds;
    }

    public File setPermissionIds(List<String> list) {
        this.permissionIds = list;
        return this;
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public File setPermissions(List<Permission> list) {
        this.permissions = list;
        return this;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public File setProperties(Map<String, String> map) {
        this.properties = map;
        return this;
    }

    public Long getQuotaBytesUsed() {
        return this.quotaBytesUsed;
    }

    public File setQuotaBytesUsed(Long l) {
        this.quotaBytesUsed = l;
        return this;
    }

    public Boolean getShared() {
        return this.shared;
    }

    public File setShared(Boolean bool) {
        this.shared = bool;
        return this;
    }

    public DateTime getSharedWithMeTime() {
        return this.sharedWithMeTime;
    }

    public File setSharedWithMeTime(DateTime dateTime) {
        this.sharedWithMeTime = dateTime;
        return this;
    }

    public User getSharingUser() {
        return this.sharingUser;
    }

    public File setSharingUser(User user) {
        this.sharingUser = user;
        return this;
    }

    public Long getSize() {
        return this.size;
    }

    public File setSize(Long l) {
        this.size = l;
        return this;
    }

    public List<String> getSpaces() {
        return this.spaces;
    }

    public File setSpaces(List<String> list) {
        this.spaces = list;
        return this;
    }

    public Boolean getStarred() {
        return this.starred;
    }

    public File setStarred(Boolean bool) {
        this.starred = bool;
        return this;
    }

    public String getTeamDriveId() {
        return this.teamDriveId;
    }

    public File setTeamDriveId(String str) {
        this.teamDriveId = str;
        return this;
    }

    public String getThumbnailLink() {
        return this.thumbnailLink;
    }

    public File setThumbnailLink(String str) {
        this.thumbnailLink = str;
        return this;
    }

    public Long getThumbnailVersion() {
        return this.thumbnailVersion;
    }

    public File setThumbnailVersion(Long l) {
        this.thumbnailVersion = l;
        return this;
    }

    public Boolean getTrashed() {
        return this.trashed;
    }

    public File setTrashed(Boolean bool) {
        this.trashed = bool;
        return this;
    }

    public DateTime getTrashedTime() {
        return this.trashedTime;
    }

    public File setTrashedTime(DateTime dateTime) {
        this.trashedTime = dateTime;
        return this;
    }

    public User getTrashingUser() {
        return this.trashingUser;
    }

    public File setTrashingUser(User user) {
        this.trashingUser = user;
        return this;
    }

    public Long getVersion() {
        return this.version;
    }

    public File setVersion(Long l) {
        this.version = l;
        return this;
    }

    public VideoMediaMetadata getVideoMediaMetadata() {
        return this.videoMediaMetadata;
    }

    public File setVideoMediaMetadata(VideoMediaMetadata videoMediaMetadata2) {
        this.videoMediaMetadata = videoMediaMetadata2;
        return this;
    }

    public Boolean getViewedByMe() {
        return this.viewedByMe;
    }

    public File setViewedByMe(Boolean bool) {
        this.viewedByMe = bool;
        return this;
    }

    public DateTime getViewedByMeTime() {
        return this.viewedByMeTime;
    }

    public File setViewedByMeTime(DateTime dateTime) {
        this.viewedByMeTime = dateTime;
        return this;
    }

    public Boolean getViewersCanCopyContent() {
        return this.viewersCanCopyContent;
    }

    public File setViewersCanCopyContent(Boolean bool) {
        this.viewersCanCopyContent = bool;
        return this;
    }

    public String getWebContentLink() {
        return this.webContentLink;
    }

    public File setWebContentLink(String str) {
        this.webContentLink = str;
        return this;
    }

    public String getWebViewLink() {
        return this.webViewLink;
    }

    public File setWebViewLink(String str) {
        this.webViewLink = str;
        return this;
    }

    public Boolean getWritersCanShare() {
        return this.writersCanShare;
    }

    public File setWritersCanShare(Boolean bool) {
        this.writersCanShare = bool;
        return this;
    }

    public File set(String str, Object obj) {
        return (File) super.set(str, obj);
    }

    public File clone() {
        return (File) super.clone();
    }
}
