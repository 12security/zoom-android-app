package com.google.api.services.drive;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UriTemplate;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.Comment;
import com.google.api.services.drive.model.CommentList;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.GeneratedIds;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.drive.model.Reply;
import com.google.api.services.drive.model.ReplyList;
import com.google.api.services.drive.model.Revision;
import com.google.api.services.drive.model.RevisionList;
import com.google.api.services.drive.model.StartPageToken;
import com.google.api.services.drive.model.TeamDrive;
import com.google.api.services.drive.model.TeamDriveList;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Drive extends AbstractGoogleJsonClient {
    public static final String DEFAULT_BASE_URL = "https://www.googleapis.com/drive/v3/";
    public static final String DEFAULT_BATCH_PATH = "batch/drive/v3";
    public static final String DEFAULT_ROOT_URL = "https://www.googleapis.com/";
    public static final String DEFAULT_SERVICE_PATH = "drive/v3/";

    public class About {

        public class Get extends DriveRequest<com.google.api.services.drive.model.About> {
            private static final String REST_PATH = "about";

            protected Get() {
                super(Drive.this, "GET", REST_PATH, null, com.google.api.services.drive.model.About.class);
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public Get setAlt(String str) {
                return (Get) super.setAlt(str);
            }

            public Get setFields(String str) {
                return (Get) super.setFields(str);
            }

            public Get setKey(String str) {
                return (Get) super.setKey(str);
            }

            public Get setOauthToken(String str) {
                return (Get) super.setOauthToken(str);
            }

            public Get setPrettyPrint(Boolean bool) {
                return (Get) super.setPrettyPrint(bool);
            }

            public Get setQuotaUser(String str) {
                return (Get) super.setQuotaUser(str);
            }

            public Get setUserIp(String str) {
                return (Get) super.setUserIp(str);
            }

            public Get set(String str, Object obj) {
                return (Get) super.set(str, obj);
            }
        }

        public About() {
        }

        public Get get() throws IOException {
            Get get = new Get();
            Drive.this.initialize(get);
            return get;
        }
    }

    public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {
        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, HttpRequestInitializer httpRequestInitializer) {
            super(httpTransport, jsonFactory, Drive.DEFAULT_ROOT_URL, Drive.DEFAULT_SERVICE_PATH, httpRequestInitializer, false);
            setBatchPath(Drive.DEFAULT_BATCH_PATH);
        }

        public Drive build() {
            return new Drive(this);
        }

        public Builder setRootUrl(String str) {
            return (Builder) super.setRootUrl(str);
        }

        public Builder setServicePath(String str) {
            return (Builder) super.setServicePath(str);
        }

        public Builder setBatchPath(String str) {
            return (Builder) super.setBatchPath(str);
        }

        public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
        }

        public Builder setApplicationName(String str) {
            return (Builder) super.setApplicationName(str);
        }

        public Builder setSuppressPatternChecks(boolean z) {
            return (Builder) super.setSuppressPatternChecks(z);
        }

        public Builder setSuppressRequiredParameterChecks(boolean z) {
            return (Builder) super.setSuppressRequiredParameterChecks(z);
        }

        public Builder setSuppressAllChecks(boolean z) {
            return (Builder) super.setSuppressAllChecks(z);
        }

        public Builder setDriveRequestInitializer(DriveRequestInitializer driveRequestInitializer) {
            return (Builder) super.setGoogleClientRequestInitializer((GoogleClientRequestInitializer) driveRequestInitializer);
        }

        public Builder setGoogleClientRequestInitializer(GoogleClientRequestInitializer googleClientRequestInitializer) {
            return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
        }
    }

    public class Changes {

        public class GetStartPageToken extends DriveRequest<StartPageToken> {
            private static final String REST_PATH = "changes/startPageToken";
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private String teamDriveId;

            protected GetStartPageToken() {
                super(Drive.this, "GET", REST_PATH, null, StartPageToken.class);
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public GetStartPageToken setAlt(String str) {
                return (GetStartPageToken) super.setAlt(str);
            }

            public GetStartPageToken setFields(String str) {
                return (GetStartPageToken) super.setFields(str);
            }

            public GetStartPageToken setKey(String str) {
                return (GetStartPageToken) super.setKey(str);
            }

            public GetStartPageToken setOauthToken(String str) {
                return (GetStartPageToken) super.setOauthToken(str);
            }

            public GetStartPageToken setPrettyPrint(Boolean bool) {
                return (GetStartPageToken) super.setPrettyPrint(bool);
            }

            public GetStartPageToken setQuotaUser(String str) {
                return (GetStartPageToken) super.setQuotaUser(str);
            }

            public GetStartPageToken setUserIp(String str) {
                return (GetStartPageToken) super.setUserIp(str);
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public GetStartPageToken setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public String getTeamDriveId() {
                return this.teamDriveId;
            }

            public GetStartPageToken setTeamDriveId(String str) {
                this.teamDriveId = str;
                return this;
            }

            public GetStartPageToken set(String str, Object obj) {
                return (GetStartPageToken) super.set(str, obj);
            }
        }

        public class List extends DriveRequest<ChangeList> {
            private static final String REST_PATH = "changes";
            @Key
            private Boolean includeCorpusRemovals;
            @Key
            private Boolean includeRemoved;
            @Key
            private Boolean includeTeamDriveItems;
            @Key
            private Integer pageSize;
            @Key
            private String pageToken;
            @Key
            private Boolean restrictToMyDrive;
            @Key
            private String spaces;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private String teamDriveId;

            protected List(String str) {
                super(Drive.this, "GET", "changes", null, ChangeList.class);
                this.pageToken = (String) Preconditions.checkNotNull(str, "Required parameter pageToken must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public Boolean getIncludeCorpusRemovals() {
                return this.includeCorpusRemovals;
            }

            public List setIncludeCorpusRemovals(Boolean bool) {
                this.includeCorpusRemovals = bool;
                return this;
            }

            public boolean isIncludeCorpusRemovals() {
                Boolean bool = this.includeCorpusRemovals;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeCorpusRemovals.booleanValue();
            }

            public Boolean getIncludeRemoved() {
                return this.includeRemoved;
            }

            public List setIncludeRemoved(Boolean bool) {
                this.includeRemoved = bool;
                return this;
            }

            public boolean isIncludeRemoved() {
                Boolean bool = this.includeRemoved;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return true;
                }
                return this.includeRemoved.booleanValue();
            }

            public Boolean getIncludeTeamDriveItems() {
                return this.includeTeamDriveItems;
            }

            public List setIncludeTeamDriveItems(Boolean bool) {
                this.includeTeamDriveItems = bool;
                return this;
            }

            public boolean isIncludeTeamDriveItems() {
                Boolean bool = this.includeTeamDriveItems;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeTeamDriveItems.booleanValue();
            }

            public Integer getPageSize() {
                return this.pageSize;
            }

            public List setPageSize(Integer num) {
                this.pageSize = num;
                return this;
            }

            public Boolean getRestrictToMyDrive() {
                return this.restrictToMyDrive;
            }

            public List setRestrictToMyDrive(Boolean bool) {
                this.restrictToMyDrive = bool;
                return this;
            }

            public boolean isRestrictToMyDrive() {
                Boolean bool = this.restrictToMyDrive;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.restrictToMyDrive.booleanValue();
            }

            public String getSpaces() {
                return this.spaces;
            }

            public List setSpaces(String str) {
                this.spaces = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public List setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public String getTeamDriveId() {
                return this.teamDriveId;
            }

            public List setTeamDriveId(String str) {
                this.teamDriveId = str;
                return this;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }
        }

        public class Watch extends DriveRequest<Channel> {
            private static final String REST_PATH = "changes/watch";
            @Key
            private Boolean includeCorpusRemovals;
            @Key
            private Boolean includeRemoved;
            @Key
            private Boolean includeTeamDriveItems;
            @Key
            private Integer pageSize;
            @Key
            private String pageToken;
            @Key
            private Boolean restrictToMyDrive;
            @Key
            private String spaces;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private String teamDriveId;

            protected Watch(String str, Channel channel) {
                super(Drive.this, "POST", REST_PATH, channel, Channel.class);
                this.pageToken = (String) Preconditions.checkNotNull(str, "Required parameter pageToken must be specified.");
            }

            public Watch setAlt(String str) {
                return (Watch) super.setAlt(str);
            }

            public Watch setFields(String str) {
                return (Watch) super.setFields(str);
            }

            public Watch setKey(String str) {
                return (Watch) super.setKey(str);
            }

            public Watch setOauthToken(String str) {
                return (Watch) super.setOauthToken(str);
            }

            public Watch setPrettyPrint(Boolean bool) {
                return (Watch) super.setPrettyPrint(bool);
            }

            public Watch setQuotaUser(String str) {
                return (Watch) super.setQuotaUser(str);
            }

            public Watch setUserIp(String str) {
                return (Watch) super.setUserIp(str);
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public Watch setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public Boolean getIncludeCorpusRemovals() {
                return this.includeCorpusRemovals;
            }

            public Watch setIncludeCorpusRemovals(Boolean bool) {
                this.includeCorpusRemovals = bool;
                return this;
            }

            public boolean isIncludeCorpusRemovals() {
                Boolean bool = this.includeCorpusRemovals;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeCorpusRemovals.booleanValue();
            }

            public Boolean getIncludeRemoved() {
                return this.includeRemoved;
            }

            public Watch setIncludeRemoved(Boolean bool) {
                this.includeRemoved = bool;
                return this;
            }

            public boolean isIncludeRemoved() {
                Boolean bool = this.includeRemoved;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return true;
                }
                return this.includeRemoved.booleanValue();
            }

            public Boolean getIncludeTeamDriveItems() {
                return this.includeTeamDriveItems;
            }

            public Watch setIncludeTeamDriveItems(Boolean bool) {
                this.includeTeamDriveItems = bool;
                return this;
            }

            public boolean isIncludeTeamDriveItems() {
                Boolean bool = this.includeTeamDriveItems;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeTeamDriveItems.booleanValue();
            }

            public Integer getPageSize() {
                return this.pageSize;
            }

            public Watch setPageSize(Integer num) {
                this.pageSize = num;
                return this;
            }

            public Boolean getRestrictToMyDrive() {
                return this.restrictToMyDrive;
            }

            public Watch setRestrictToMyDrive(Boolean bool) {
                this.restrictToMyDrive = bool;
                return this;
            }

            public boolean isRestrictToMyDrive() {
                Boolean bool = this.restrictToMyDrive;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.restrictToMyDrive.booleanValue();
            }

            public String getSpaces() {
                return this.spaces;
            }

            public Watch setSpaces(String str) {
                this.spaces = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Watch setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public String getTeamDriveId() {
                return this.teamDriveId;
            }

            public Watch setTeamDriveId(String str) {
                this.teamDriveId = str;
                return this;
            }

            public Watch set(String str, Object obj) {
                return (Watch) super.set(str, obj);
            }
        }

        public Changes() {
        }

        public GetStartPageToken getStartPageToken() throws IOException {
            GetStartPageToken getStartPageToken = new GetStartPageToken();
            Drive.this.initialize(getStartPageToken);
            return getStartPageToken;
        }

        public List list(String str) throws IOException {
            List list = new List(str);
            Drive.this.initialize(list);
            return list;
        }

        public Watch watch(String str, Channel channel) throws IOException {
            Watch watch = new Watch(str, channel);
            Drive.this.initialize(watch);
            return watch;
        }
    }

    public class Channels {

        public class Stop extends DriveRequest<Void> {
            private static final String REST_PATH = "channels/stop";

            protected Stop(Channel channel) {
                super(Drive.this, "POST", REST_PATH, channel, Void.class);
            }

            public Stop setAlt(String str) {
                return (Stop) super.setAlt(str);
            }

            public Stop setFields(String str) {
                return (Stop) super.setFields(str);
            }

            public Stop setKey(String str) {
                return (Stop) super.setKey(str);
            }

            public Stop setOauthToken(String str) {
                return (Stop) super.setOauthToken(str);
            }

            public Stop setPrettyPrint(Boolean bool) {
                return (Stop) super.setPrettyPrint(bool);
            }

            public Stop setQuotaUser(String str) {
                return (Stop) super.setQuotaUser(str);
            }

            public Stop setUserIp(String str) {
                return (Stop) super.setUserIp(str);
            }

            public Stop set(String str, Object obj) {
                return (Stop) super.set(str, obj);
            }
        }

        public Channels() {
        }

        public Stop stop(Channel channel) throws IOException {
            Stop stop = new Stop(channel);
            Drive.this.initialize(stop);
            return stop;
        }
    }

    public class Comments {

        public class Create extends DriveRequest<Comment> {
            private static final String REST_PATH = "files/{fileId}/comments";
            @Key
            private String fileId;

            protected Create(String str, Comment comment) {
                super(Drive.this, "POST", REST_PATH, comment, Comment.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                checkRequiredParameter(comment, Param.CONTENT);
                checkRequiredParameter(comment.getContent(), "Comment.getContent()");
            }

            public Create setAlt(String str) {
                return (Create) super.setAlt(str);
            }

            public Create setFields(String str) {
                return (Create) super.setFields(str);
            }

            public Create setKey(String str) {
                return (Create) super.setKey(str);
            }

            public Create setOauthToken(String str) {
                return (Create) super.setOauthToken(str);
            }

            public Create setPrettyPrint(Boolean bool) {
                return (Create) super.setPrettyPrint(bool);
            }

            public Create setQuotaUser(String str) {
                return (Create) super.setQuotaUser(str);
            }

            public Create setUserIp(String str) {
                return (Create) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Create setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public Create set(String str, Object obj) {
                return (Create) super.set(str, obj);
            }
        }

        public class Delete extends DriveRequest<Void> {
            private static final String REST_PATH = "files/{fileId}/comments/{commentId}";
            @Key
            private String commentId;
            @Key
            private String fileId;

            protected Delete(String str, String str2) {
                super(Drive.this, "DELETE", REST_PATH, null, Void.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.commentId = (String) Preconditions.checkNotNull(str2, "Required parameter commentId must be specified.");
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Delete setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public Delete setCommentId(String str) {
                this.commentId = str;
                return this;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }
        }

        public class Get extends DriveRequest<Comment> {
            private static final String REST_PATH = "files/{fileId}/comments/{commentId}";
            @Key
            private String commentId;
            @Key
            private String fileId;
            @Key
            private Boolean includeDeleted;

            protected Get(String str, String str2) {
                super(Drive.this, "GET", REST_PATH, null, Comment.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.commentId = (String) Preconditions.checkNotNull(str2, "Required parameter commentId must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public Get setAlt(String str) {
                return (Get) super.setAlt(str);
            }

            public Get setFields(String str) {
                return (Get) super.setFields(str);
            }

            public Get setKey(String str) {
                return (Get) super.setKey(str);
            }

            public Get setOauthToken(String str) {
                return (Get) super.setOauthToken(str);
            }

            public Get setPrettyPrint(Boolean bool) {
                return (Get) super.setPrettyPrint(bool);
            }

            public Get setQuotaUser(String str) {
                return (Get) super.setQuotaUser(str);
            }

            public Get setUserIp(String str) {
                return (Get) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Get setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public Get setCommentId(String str) {
                this.commentId = str;
                return this;
            }

            public Boolean getIncludeDeleted() {
                return this.includeDeleted;
            }

            public Get setIncludeDeleted(Boolean bool) {
                this.includeDeleted = bool;
                return this;
            }

            public boolean isIncludeDeleted() {
                Boolean bool = this.includeDeleted;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeDeleted.booleanValue();
            }

            public Get set(String str, Object obj) {
                return (Get) super.set(str, obj);
            }
        }

        public class List extends DriveRequest<CommentList> {
            private static final String REST_PATH = "files/{fileId}/comments";
            @Key
            private String fileId;
            @Key
            private Boolean includeDeleted;
            @Key
            private Integer pageSize;
            @Key
            private String pageToken;
            @Key
            private String startModifiedTime;

            protected List(String str) {
                super(Drive.this, "GET", REST_PATH, null, CommentList.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public List setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public Boolean getIncludeDeleted() {
                return this.includeDeleted;
            }

            public List setIncludeDeleted(Boolean bool) {
                this.includeDeleted = bool;
                return this;
            }

            public boolean isIncludeDeleted() {
                Boolean bool = this.includeDeleted;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeDeleted.booleanValue();
            }

            public Integer getPageSize() {
                return this.pageSize;
            }

            public List setPageSize(Integer num) {
                this.pageSize = num;
                return this;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public String getStartModifiedTime() {
                return this.startModifiedTime;
            }

            public List setStartModifiedTime(String str) {
                this.startModifiedTime = str;
                return this;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }
        }

        public class Update extends DriveRequest<Comment> {
            private static final String REST_PATH = "files/{fileId}/comments/{commentId}";
            @Key
            private String commentId;
            @Key
            private String fileId;

            protected Update(String str, String str2, Comment comment) {
                super(Drive.this, "PATCH", REST_PATH, comment, Comment.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.commentId = (String) Preconditions.checkNotNull(str2, "Required parameter commentId must be specified.");
                checkRequiredParameter(comment, Param.CONTENT);
                checkRequiredParameter(comment.getContent(), "Comment.getContent()");
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Update setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public Update setCommentId(String str) {
                this.commentId = str;
                return this;
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }
        }

        public Comments() {
        }

        public Create create(String str, Comment comment) throws IOException {
            Create create = new Create(str, comment);
            Drive.this.initialize(create);
            return create;
        }

        public Delete delete(String str, String str2) throws IOException {
            Delete delete = new Delete(str, str2);
            Drive.this.initialize(delete);
            return delete;
        }

        public Get get(String str, String str2) throws IOException {
            Get get = new Get(str, str2);
            Drive.this.initialize(get);
            return get;
        }

        public List list(String str) throws IOException {
            List list = new List(str);
            Drive.this.initialize(list);
            return list;
        }

        public Update update(String str, String str2, Comment comment) throws IOException {
            Update update = new Update(str, str2, comment);
            Drive.this.initialize(update);
            return update;
        }
    }

    public class Files {

        public class Copy extends DriveRequest<File> {
            private static final String REST_PATH = "files/{fileId}/copy";
            @Key
            private String fileId;
            @Key
            private Boolean ignoreDefaultVisibility;
            @Key
            private Boolean keepRevisionForever;
            @Key
            private String ocrLanguage;
            @Key
            private Boolean supportsTeamDrives;

            protected Copy(String str, File file) {
                super(Drive.this, "POST", REST_PATH, file, File.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
            }

            public Copy setAlt(String str) {
                return (Copy) super.setAlt(str);
            }

            public Copy setFields(String str) {
                return (Copy) super.setFields(str);
            }

            public Copy setKey(String str) {
                return (Copy) super.setKey(str);
            }

            public Copy setOauthToken(String str) {
                return (Copy) super.setOauthToken(str);
            }

            public Copy setPrettyPrint(Boolean bool) {
                return (Copy) super.setPrettyPrint(bool);
            }

            public Copy setQuotaUser(String str) {
                return (Copy) super.setQuotaUser(str);
            }

            public Copy setUserIp(String str) {
                return (Copy) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Copy setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public Boolean getIgnoreDefaultVisibility() {
                return this.ignoreDefaultVisibility;
            }

            public Copy setIgnoreDefaultVisibility(Boolean bool) {
                this.ignoreDefaultVisibility = bool;
                return this;
            }

            public boolean isIgnoreDefaultVisibility() {
                Boolean bool = this.ignoreDefaultVisibility;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.ignoreDefaultVisibility.booleanValue();
            }

            public Boolean getKeepRevisionForever() {
                return this.keepRevisionForever;
            }

            public Copy setKeepRevisionForever(Boolean bool) {
                this.keepRevisionForever = bool;
                return this;
            }

            public boolean isKeepRevisionForever() {
                Boolean bool = this.keepRevisionForever;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.keepRevisionForever.booleanValue();
            }

            public String getOcrLanguage() {
                return this.ocrLanguage;
            }

            public Copy setOcrLanguage(String str) {
                this.ocrLanguage = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Copy setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Copy set(String str, Object obj) {
                return (Copy) super.set(str, obj);
            }
        }

        public class Create extends DriveRequest<File> {
            private static final String REST_PATH = "files";
            @Key
            private Boolean ignoreDefaultVisibility;
            @Key
            private Boolean keepRevisionForever;
            @Key
            private String ocrLanguage;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private Boolean useContentAsIndexableText;

            protected Create(File file) {
                super(Drive.this, "POST", REST_PATH, file, File.class);
            }

            protected Create(File file, AbstractInputStreamContent abstractInputStreamContent) {
                StringBuilder sb = new StringBuilder();
                sb.append("/upload/");
                sb.append(Drive.this.getServicePath());
                sb.append(REST_PATH);
                super(Drive.this, "POST", sb.toString(), file, File.class);
                initializeMediaUpload(abstractInputStreamContent);
            }

            public Create setAlt(String str) {
                return (Create) super.setAlt(str);
            }

            public Create setFields(String str) {
                return (Create) super.setFields(str);
            }

            public Create setKey(String str) {
                return (Create) super.setKey(str);
            }

            public Create setOauthToken(String str) {
                return (Create) super.setOauthToken(str);
            }

            public Create setPrettyPrint(Boolean bool) {
                return (Create) super.setPrettyPrint(bool);
            }

            public Create setQuotaUser(String str) {
                return (Create) super.setQuotaUser(str);
            }

            public Create setUserIp(String str) {
                return (Create) super.setUserIp(str);
            }

            public Boolean getIgnoreDefaultVisibility() {
                return this.ignoreDefaultVisibility;
            }

            public Create setIgnoreDefaultVisibility(Boolean bool) {
                this.ignoreDefaultVisibility = bool;
                return this;
            }

            public boolean isIgnoreDefaultVisibility() {
                Boolean bool = this.ignoreDefaultVisibility;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.ignoreDefaultVisibility.booleanValue();
            }

            public Boolean getKeepRevisionForever() {
                return this.keepRevisionForever;
            }

            public Create setKeepRevisionForever(Boolean bool) {
                this.keepRevisionForever = bool;
                return this;
            }

            public boolean isKeepRevisionForever() {
                Boolean bool = this.keepRevisionForever;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.keepRevisionForever.booleanValue();
            }

            public String getOcrLanguage() {
                return this.ocrLanguage;
            }

            public Create setOcrLanguage(String str) {
                this.ocrLanguage = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Create setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Boolean getUseContentAsIndexableText() {
                return this.useContentAsIndexableText;
            }

            public Create setUseContentAsIndexableText(Boolean bool) {
                this.useContentAsIndexableText = bool;
                return this;
            }

            public boolean isUseContentAsIndexableText() {
                Boolean bool = this.useContentAsIndexableText;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useContentAsIndexableText.booleanValue();
            }

            public Create set(String str, Object obj) {
                return (Create) super.set(str, obj);
            }
        }

        public class Delete extends DriveRequest<Void> {
            private static final String REST_PATH = "files/{fileId}";
            @Key
            private String fileId;
            @Key
            private Boolean supportsTeamDrives;

            protected Delete(String str) {
                super(Drive.this, "DELETE", REST_PATH, null, Void.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Delete setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Delete setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }
        }

        public class EmptyTrash extends DriveRequest<Void> {
            private static final String REST_PATH = "files/trash";

            protected EmptyTrash() {
                super(Drive.this, "DELETE", REST_PATH, null, Void.class);
            }

            public EmptyTrash setAlt(String str) {
                return (EmptyTrash) super.setAlt(str);
            }

            public EmptyTrash setFields(String str) {
                return (EmptyTrash) super.setFields(str);
            }

            public EmptyTrash setKey(String str) {
                return (EmptyTrash) super.setKey(str);
            }

            public EmptyTrash setOauthToken(String str) {
                return (EmptyTrash) super.setOauthToken(str);
            }

            public EmptyTrash setPrettyPrint(Boolean bool) {
                return (EmptyTrash) super.setPrettyPrint(bool);
            }

            public EmptyTrash setQuotaUser(String str) {
                return (EmptyTrash) super.setQuotaUser(str);
            }

            public EmptyTrash setUserIp(String str) {
                return (EmptyTrash) super.setUserIp(str);
            }

            public EmptyTrash set(String str, Object obj) {
                return (EmptyTrash) super.set(str, obj);
            }
        }

        public class Export extends DriveRequest<Void> {
            private static final String REST_PATH = "files/{fileId}/export";
            @Key
            private String fileId;
            @Key
            private String mimeType;

            protected Export(String str, String str2) {
                super(Drive.this, "GET", REST_PATH, null, Void.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.mimeType = (String) Preconditions.checkNotNull(str2, "Required parameter mimeType must be specified.");
                initializeMediaDownload();
            }

            public void executeMediaAndDownloadTo(OutputStream outputStream) throws IOException {
                super.executeMediaAndDownloadTo(outputStream);
            }

            public InputStream executeMediaAsInputStream() throws IOException {
                return super.executeMediaAsInputStream();
            }

            public HttpResponse executeMedia() throws IOException {
                return super.executeMedia();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public Export setAlt(String str) {
                return (Export) super.setAlt(str);
            }

            public Export setFields(String str) {
                return (Export) super.setFields(str);
            }

            public Export setKey(String str) {
                return (Export) super.setKey(str);
            }

            public Export setOauthToken(String str) {
                return (Export) super.setOauthToken(str);
            }

            public Export setPrettyPrint(Boolean bool) {
                return (Export) super.setPrettyPrint(bool);
            }

            public Export setQuotaUser(String str) {
                return (Export) super.setQuotaUser(str);
            }

            public Export setUserIp(String str) {
                return (Export) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Export setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getMimeType() {
                return this.mimeType;
            }

            public Export setMimeType(String str) {
                this.mimeType = str;
                return this;
            }

            public Export set(String str, Object obj) {
                return (Export) super.set(str, obj);
            }
        }

        public class GenerateIds extends DriveRequest<GeneratedIds> {
            private static final String REST_PATH = "files/generateIds";
            @Key
            private Integer count;
            @Key
            private String space;

            protected GenerateIds() {
                super(Drive.this, "GET", REST_PATH, null, GeneratedIds.class);
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public GenerateIds setAlt(String str) {
                return (GenerateIds) super.setAlt(str);
            }

            public GenerateIds setFields(String str) {
                return (GenerateIds) super.setFields(str);
            }

            public GenerateIds setKey(String str) {
                return (GenerateIds) super.setKey(str);
            }

            public GenerateIds setOauthToken(String str) {
                return (GenerateIds) super.setOauthToken(str);
            }

            public GenerateIds setPrettyPrint(Boolean bool) {
                return (GenerateIds) super.setPrettyPrint(bool);
            }

            public GenerateIds setQuotaUser(String str) {
                return (GenerateIds) super.setQuotaUser(str);
            }

            public GenerateIds setUserIp(String str) {
                return (GenerateIds) super.setUserIp(str);
            }

            public Integer getCount() {
                return this.count;
            }

            public GenerateIds setCount(Integer num) {
                this.count = num;
                return this;
            }

            public String getSpace() {
                return this.space;
            }

            public GenerateIds setSpace(String str) {
                this.space = str;
                return this;
            }

            public GenerateIds set(String str, Object obj) {
                return (GenerateIds) super.set(str, obj);
            }
        }

        public class Get extends DriveRequest<File> {
            private static final String REST_PATH = "files/{fileId}";
            @Key
            private Boolean acknowledgeAbuse;
            @Key
            private String fileId;
            @Key
            private Boolean supportsTeamDrives;

            protected Get(String str) {
                super(Drive.this, "GET", REST_PATH, null, File.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                initializeMediaDownload();
            }

            public void executeMediaAndDownloadTo(OutputStream outputStream) throws IOException {
                super.executeMediaAndDownloadTo(outputStream);
            }

            public InputStream executeMediaAsInputStream() throws IOException {
                return super.executeMediaAsInputStream();
            }

            public HttpResponse executeMedia() throws IOException {
                return super.executeMedia();
            }

            public GenericUrl buildHttpRequestUrl() {
                String str;
                if (!"media".equals(get("alt")) || getMediaHttpUploader() != null) {
                    str = Drive.this.getBaseUrl();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Drive.this.getRootUrl());
                    sb.append("download/");
                    sb.append(Drive.this.getServicePath());
                    str = sb.toString();
                }
                return new GenericUrl(UriTemplate.expand(str, getUriTemplate(), this, true));
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public Get setAlt(String str) {
                return (Get) super.setAlt(str);
            }

            public Get setFields(String str) {
                return (Get) super.setFields(str);
            }

            public Get setKey(String str) {
                return (Get) super.setKey(str);
            }

            public Get setOauthToken(String str) {
                return (Get) super.setOauthToken(str);
            }

            public Get setPrettyPrint(Boolean bool) {
                return (Get) super.setPrettyPrint(bool);
            }

            public Get setQuotaUser(String str) {
                return (Get) super.setQuotaUser(str);
            }

            public Get setUserIp(String str) {
                return (Get) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Get setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public Boolean getAcknowledgeAbuse() {
                return this.acknowledgeAbuse;
            }

            public Get setAcknowledgeAbuse(Boolean bool) {
                this.acknowledgeAbuse = bool;
                return this;
            }

            public boolean isAcknowledgeAbuse() {
                Boolean bool = this.acknowledgeAbuse;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.acknowledgeAbuse.booleanValue();
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Get setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Get set(String str, Object obj) {
                return (Get) super.set(str, obj);
            }
        }

        public class List extends DriveRequest<FileList> {
            private static final String REST_PATH = "files";
            @Key
            private String corpora;
            @Key
            private String corpus;
            @Key
            private Boolean includeTeamDriveItems;
            @Key
            private String orderBy;
            @Key
            private Integer pageSize;
            @Key
            private String pageToken;
            @Key

            /* renamed from: q */
            private String f206q;
            @Key
            private String spaces;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private String teamDriveId;

            protected List() {
                super(Drive.this, "GET", REST_PATH, null, FileList.class);
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public String getCorpora() {
                return this.corpora;
            }

            public List setCorpora(String str) {
                this.corpora = str;
                return this;
            }

            public String getCorpus() {
                return this.corpus;
            }

            public List setCorpus(String str) {
                this.corpus = str;
                return this;
            }

            public Boolean getIncludeTeamDriveItems() {
                return this.includeTeamDriveItems;
            }

            public List setIncludeTeamDriveItems(Boolean bool) {
                this.includeTeamDriveItems = bool;
                return this;
            }

            public boolean isIncludeTeamDriveItems() {
                Boolean bool = this.includeTeamDriveItems;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeTeamDriveItems.booleanValue();
            }

            public String getOrderBy() {
                return this.orderBy;
            }

            public List setOrderBy(String str) {
                this.orderBy = str;
                return this;
            }

            public Integer getPageSize() {
                return this.pageSize;
            }

            public List setPageSize(Integer num) {
                this.pageSize = num;
                return this;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public String getQ() {
                return this.f206q;
            }

            public List setQ(String str) {
                this.f206q = str;
                return this;
            }

            public String getSpaces() {
                return this.spaces;
            }

            public List setSpaces(String str) {
                this.spaces = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public List setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public String getTeamDriveId() {
                return this.teamDriveId;
            }

            public List setTeamDriveId(String str) {
                this.teamDriveId = str;
                return this;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }
        }

        public class Update extends DriveRequest<File> {
            private static final String REST_PATH = "files/{fileId}";
            @Key
            private String addParents;
            @Key
            private String fileId;
            @Key
            private Boolean keepRevisionForever;
            @Key
            private String ocrLanguage;
            @Key
            private String removeParents;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private Boolean useContentAsIndexableText;

            protected Update(String str, File file) {
                super(Drive.this, "PATCH", REST_PATH, file, File.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
            }

            protected Update(String str, File file, AbstractInputStreamContent abstractInputStreamContent) {
                StringBuilder sb = new StringBuilder();
                sb.append("/upload/");
                sb.append(Drive.this.getServicePath());
                sb.append(REST_PATH);
                super(Drive.this, "PATCH", sb.toString(), file, File.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                initializeMediaUpload(abstractInputStreamContent);
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Update setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getAddParents() {
                return this.addParents;
            }

            public Update setAddParents(String str) {
                this.addParents = str;
                return this;
            }

            public Boolean getKeepRevisionForever() {
                return this.keepRevisionForever;
            }

            public Update setKeepRevisionForever(Boolean bool) {
                this.keepRevisionForever = bool;
                return this;
            }

            public boolean isKeepRevisionForever() {
                Boolean bool = this.keepRevisionForever;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.keepRevisionForever.booleanValue();
            }

            public String getOcrLanguage() {
                return this.ocrLanguage;
            }

            public Update setOcrLanguage(String str) {
                this.ocrLanguage = str;
                return this;
            }

            public String getRemoveParents() {
                return this.removeParents;
            }

            public Update setRemoveParents(String str) {
                this.removeParents = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Update setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Boolean getUseContentAsIndexableText() {
                return this.useContentAsIndexableText;
            }

            public Update setUseContentAsIndexableText(Boolean bool) {
                this.useContentAsIndexableText = bool;
                return this;
            }

            public boolean isUseContentAsIndexableText() {
                Boolean bool = this.useContentAsIndexableText;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useContentAsIndexableText.booleanValue();
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }
        }

        public class Watch extends DriveRequest<Channel> {
            private static final String REST_PATH = "files/{fileId}/watch";
            @Key
            private Boolean acknowledgeAbuse;
            @Key
            private String fileId;
            @Key
            private Boolean supportsTeamDrives;

            protected Watch(String str, Channel channel) {
                super(Drive.this, "POST", REST_PATH, channel, Channel.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                initializeMediaDownload();
            }

            public void executeMediaAndDownloadTo(OutputStream outputStream) throws IOException {
                super.executeMediaAndDownloadTo(outputStream);
            }

            public InputStream executeMediaAsInputStream() throws IOException {
                return super.executeMediaAsInputStream();
            }

            public HttpResponse executeMedia() throws IOException {
                return super.executeMedia();
            }

            public GenericUrl buildHttpRequestUrl() {
                String str;
                if (!"media".equals(get("alt")) || getMediaHttpUploader() != null) {
                    str = Drive.this.getBaseUrl();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Drive.this.getRootUrl());
                    sb.append("download/");
                    sb.append(Drive.this.getServicePath());
                    str = sb.toString();
                }
                return new GenericUrl(UriTemplate.expand(str, getUriTemplate(), this, true));
            }

            public Watch setAlt(String str) {
                return (Watch) super.setAlt(str);
            }

            public Watch setFields(String str) {
                return (Watch) super.setFields(str);
            }

            public Watch setKey(String str) {
                return (Watch) super.setKey(str);
            }

            public Watch setOauthToken(String str) {
                return (Watch) super.setOauthToken(str);
            }

            public Watch setPrettyPrint(Boolean bool) {
                return (Watch) super.setPrettyPrint(bool);
            }

            public Watch setQuotaUser(String str) {
                return (Watch) super.setQuotaUser(str);
            }

            public Watch setUserIp(String str) {
                return (Watch) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Watch setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public Boolean getAcknowledgeAbuse() {
                return this.acknowledgeAbuse;
            }

            public Watch setAcknowledgeAbuse(Boolean bool) {
                this.acknowledgeAbuse = bool;
                return this;
            }

            public boolean isAcknowledgeAbuse() {
                Boolean bool = this.acknowledgeAbuse;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.acknowledgeAbuse.booleanValue();
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Watch setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Watch set(String str, Object obj) {
                return (Watch) super.set(str, obj);
            }
        }

        public Files() {
        }

        public Copy copy(String str, File file) throws IOException {
            Copy copy = new Copy(str, file);
            Drive.this.initialize(copy);
            return copy;
        }

        public Create create(File file) throws IOException {
            Create create = new Create(file);
            Drive.this.initialize(create);
            return create;
        }

        public Create create(File file, AbstractInputStreamContent abstractInputStreamContent) throws IOException {
            Create create = new Create(file, abstractInputStreamContent);
            Drive.this.initialize(create);
            return create;
        }

        public Delete delete(String str) throws IOException {
            Delete delete = new Delete(str);
            Drive.this.initialize(delete);
            return delete;
        }

        public EmptyTrash emptyTrash() throws IOException {
            EmptyTrash emptyTrash = new EmptyTrash();
            Drive.this.initialize(emptyTrash);
            return emptyTrash;
        }

        public Export export(String str, String str2) throws IOException {
            Export export = new Export(str, str2);
            Drive.this.initialize(export);
            return export;
        }

        public GenerateIds generateIds() throws IOException {
            GenerateIds generateIds = new GenerateIds();
            Drive.this.initialize(generateIds);
            return generateIds;
        }

        public Get get(String str) throws IOException {
            Get get = new Get(str);
            Drive.this.initialize(get);
            return get;
        }

        public List list() throws IOException {
            List list = new List();
            Drive.this.initialize(list);
            return list;
        }

        public Update update(String str, File file) throws IOException {
            Update update = new Update(str, file);
            Drive.this.initialize(update);
            return update;
        }

        public Update update(String str, File file, AbstractInputStreamContent abstractInputStreamContent) throws IOException {
            Update update = new Update(str, file, abstractInputStreamContent);
            Drive.this.initialize(update);
            return update;
        }

        public Watch watch(String str, Channel channel) throws IOException {
            Watch watch = new Watch(str, channel);
            Drive.this.initialize(watch);
            return watch;
        }
    }

    public class Permissions {

        public class Create extends DriveRequest<Permission> {
            private static final String REST_PATH = "files/{fileId}/permissions";
            @Key
            private String emailMessage;
            @Key
            private String fileId;
            @Key
            private Boolean sendNotificationEmail;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private Boolean transferOwnership;
            @Key
            private Boolean useDomainAdminAccess;

            protected Create(String str, Permission permission) {
                super(Drive.this, "POST", REST_PATH, permission, Permission.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                checkRequiredParameter(permission, Param.CONTENT);
                checkRequiredParameter(permission.getRole(), "Permission.getRole()");
                checkRequiredParameter(permission, Param.CONTENT);
                checkRequiredParameter(permission.getType(), "Permission.getType()");
            }

            public Create setAlt(String str) {
                return (Create) super.setAlt(str);
            }

            public Create setFields(String str) {
                return (Create) super.setFields(str);
            }

            public Create setKey(String str) {
                return (Create) super.setKey(str);
            }

            public Create setOauthToken(String str) {
                return (Create) super.setOauthToken(str);
            }

            public Create setPrettyPrint(Boolean bool) {
                return (Create) super.setPrettyPrint(bool);
            }

            public Create setQuotaUser(String str) {
                return (Create) super.setQuotaUser(str);
            }

            public Create setUserIp(String str) {
                return (Create) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Create setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getEmailMessage() {
                return this.emailMessage;
            }

            public Create setEmailMessage(String str) {
                this.emailMessage = str;
                return this;
            }

            public Boolean getSendNotificationEmail() {
                return this.sendNotificationEmail;
            }

            public Create setSendNotificationEmail(Boolean bool) {
                this.sendNotificationEmail = bool;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Create setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Boolean getTransferOwnership() {
                return this.transferOwnership;
            }

            public Create setTransferOwnership(Boolean bool) {
                this.transferOwnership = bool;
                return this;
            }

            public boolean isTransferOwnership() {
                Boolean bool = this.transferOwnership;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.transferOwnership.booleanValue();
            }

            public Boolean getUseDomainAdminAccess() {
                return this.useDomainAdminAccess;
            }

            public Create setUseDomainAdminAccess(Boolean bool) {
                this.useDomainAdminAccess = bool;
                return this;
            }

            public boolean isUseDomainAdminAccess() {
                Boolean bool = this.useDomainAdminAccess;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useDomainAdminAccess.booleanValue();
            }

            public Create set(String str, Object obj) {
                return (Create) super.set(str, obj);
            }
        }

        public class Delete extends DriveRequest<Void> {
            private static final String REST_PATH = "files/{fileId}/permissions/{permissionId}";
            @Key
            private String fileId;
            @Key
            private String permissionId;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private Boolean useDomainAdminAccess;

            protected Delete(String str, String str2) {
                super(Drive.this, "DELETE", REST_PATH, null, Void.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.permissionId = (String) Preconditions.checkNotNull(str2, "Required parameter permissionId must be specified.");
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Delete setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getPermissionId() {
                return this.permissionId;
            }

            public Delete setPermissionId(String str) {
                this.permissionId = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Delete setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Boolean getUseDomainAdminAccess() {
                return this.useDomainAdminAccess;
            }

            public Delete setUseDomainAdminAccess(Boolean bool) {
                this.useDomainAdminAccess = bool;
                return this;
            }

            public boolean isUseDomainAdminAccess() {
                Boolean bool = this.useDomainAdminAccess;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useDomainAdminAccess.booleanValue();
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }
        }

        public class Get extends DriveRequest<Permission> {
            private static final String REST_PATH = "files/{fileId}/permissions/{permissionId}";
            @Key
            private String fileId;
            @Key
            private String permissionId;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private Boolean useDomainAdminAccess;

            protected Get(String str, String str2) {
                super(Drive.this, "GET", REST_PATH, null, Permission.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.permissionId = (String) Preconditions.checkNotNull(str2, "Required parameter permissionId must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public Get setAlt(String str) {
                return (Get) super.setAlt(str);
            }

            public Get setFields(String str) {
                return (Get) super.setFields(str);
            }

            public Get setKey(String str) {
                return (Get) super.setKey(str);
            }

            public Get setOauthToken(String str) {
                return (Get) super.setOauthToken(str);
            }

            public Get setPrettyPrint(Boolean bool) {
                return (Get) super.setPrettyPrint(bool);
            }

            public Get setQuotaUser(String str) {
                return (Get) super.setQuotaUser(str);
            }

            public Get setUserIp(String str) {
                return (Get) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Get setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getPermissionId() {
                return this.permissionId;
            }

            public Get setPermissionId(String str) {
                this.permissionId = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Get setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Boolean getUseDomainAdminAccess() {
                return this.useDomainAdminAccess;
            }

            public Get setUseDomainAdminAccess(Boolean bool) {
                this.useDomainAdminAccess = bool;
                return this;
            }

            public boolean isUseDomainAdminAccess() {
                Boolean bool = this.useDomainAdminAccess;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useDomainAdminAccess.booleanValue();
            }

            public Get set(String str, Object obj) {
                return (Get) super.set(str, obj);
            }
        }

        public class List extends DriveRequest<PermissionList> {
            private static final String REST_PATH = "files/{fileId}/permissions";
            @Key
            private String fileId;
            @Key
            private Integer pageSize;
            @Key
            private String pageToken;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private Boolean useDomainAdminAccess;

            protected List(String str) {
                super(Drive.this, "GET", REST_PATH, null, PermissionList.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public List setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public Integer getPageSize() {
                return this.pageSize;
            }

            public List setPageSize(Integer num) {
                this.pageSize = num;
                return this;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public List setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Boolean getUseDomainAdminAccess() {
                return this.useDomainAdminAccess;
            }

            public List setUseDomainAdminAccess(Boolean bool) {
                this.useDomainAdminAccess = bool;
                return this;
            }

            public boolean isUseDomainAdminAccess() {
                Boolean bool = this.useDomainAdminAccess;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useDomainAdminAccess.booleanValue();
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }
        }

        public class Update extends DriveRequest<Permission> {
            private static final String REST_PATH = "files/{fileId}/permissions/{permissionId}";
            @Key
            private String fileId;
            @Key
            private String permissionId;
            @Key
            private Boolean removeExpiration;
            @Key
            private Boolean supportsTeamDrives;
            @Key
            private Boolean transferOwnership;
            @Key
            private Boolean useDomainAdminAccess;

            protected Update(String str, String str2, Permission permission) {
                super(Drive.this, "PATCH", REST_PATH, permission, Permission.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.permissionId = (String) Preconditions.checkNotNull(str2, "Required parameter permissionId must be specified.");
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Update setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getPermissionId() {
                return this.permissionId;
            }

            public Update setPermissionId(String str) {
                this.permissionId = str;
                return this;
            }

            public Boolean getRemoveExpiration() {
                return this.removeExpiration;
            }

            public Update setRemoveExpiration(Boolean bool) {
                this.removeExpiration = bool;
                return this;
            }

            public boolean isRemoveExpiration() {
                Boolean bool = this.removeExpiration;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.removeExpiration.booleanValue();
            }

            public Boolean getSupportsTeamDrives() {
                return this.supportsTeamDrives;
            }

            public Update setSupportsTeamDrives(Boolean bool) {
                this.supportsTeamDrives = bool;
                return this;
            }

            public boolean isSupportsTeamDrives() {
                Boolean bool = this.supportsTeamDrives;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.supportsTeamDrives.booleanValue();
            }

            public Boolean getTransferOwnership() {
                return this.transferOwnership;
            }

            public Update setTransferOwnership(Boolean bool) {
                this.transferOwnership = bool;
                return this;
            }

            public boolean isTransferOwnership() {
                Boolean bool = this.transferOwnership;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.transferOwnership.booleanValue();
            }

            public Boolean getUseDomainAdminAccess() {
                return this.useDomainAdminAccess;
            }

            public Update setUseDomainAdminAccess(Boolean bool) {
                this.useDomainAdminAccess = bool;
                return this;
            }

            public boolean isUseDomainAdminAccess() {
                Boolean bool = this.useDomainAdminAccess;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useDomainAdminAccess.booleanValue();
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }
        }

        public Permissions() {
        }

        public Create create(String str, Permission permission) throws IOException {
            Create create = new Create(str, permission);
            Drive.this.initialize(create);
            return create;
        }

        public Delete delete(String str, String str2) throws IOException {
            Delete delete = new Delete(str, str2);
            Drive.this.initialize(delete);
            return delete;
        }

        public Get get(String str, String str2) throws IOException {
            Get get = new Get(str, str2);
            Drive.this.initialize(get);
            return get;
        }

        public List list(String str) throws IOException {
            List list = new List(str);
            Drive.this.initialize(list);
            return list;
        }

        public Update update(String str, String str2, Permission permission) throws IOException {
            Update update = new Update(str, str2, permission);
            Drive.this.initialize(update);
            return update;
        }
    }

    public class Replies {

        public class Create extends DriveRequest<Reply> {
            private static final String REST_PATH = "files/{fileId}/comments/{commentId}/replies";
            @Key
            private String commentId;
            @Key
            private String fileId;

            protected Create(String str, String str2, Reply reply) {
                super(Drive.this, "POST", REST_PATH, reply, Reply.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.commentId = (String) Preconditions.checkNotNull(str2, "Required parameter commentId must be specified.");
            }

            public Create setAlt(String str) {
                return (Create) super.setAlt(str);
            }

            public Create setFields(String str) {
                return (Create) super.setFields(str);
            }

            public Create setKey(String str) {
                return (Create) super.setKey(str);
            }

            public Create setOauthToken(String str) {
                return (Create) super.setOauthToken(str);
            }

            public Create setPrettyPrint(Boolean bool) {
                return (Create) super.setPrettyPrint(bool);
            }

            public Create setQuotaUser(String str) {
                return (Create) super.setQuotaUser(str);
            }

            public Create setUserIp(String str) {
                return (Create) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Create setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public Create setCommentId(String str) {
                this.commentId = str;
                return this;
            }

            public Create set(String str, Object obj) {
                return (Create) super.set(str, obj);
            }
        }

        public class Delete extends DriveRequest<Void> {
            private static final String REST_PATH = "files/{fileId}/comments/{commentId}/replies/{replyId}";
            @Key
            private String commentId;
            @Key
            private String fileId;
            @Key
            private String replyId;

            protected Delete(String str, String str2, String str3) {
                super(Drive.this, "DELETE", REST_PATH, null, Void.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.commentId = (String) Preconditions.checkNotNull(str2, "Required parameter commentId must be specified.");
                this.replyId = (String) Preconditions.checkNotNull(str3, "Required parameter replyId must be specified.");
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Delete setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public Delete setCommentId(String str) {
                this.commentId = str;
                return this;
            }

            public String getReplyId() {
                return this.replyId;
            }

            public Delete setReplyId(String str) {
                this.replyId = str;
                return this;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }
        }

        public class Get extends DriveRequest<Reply> {
            private static final String REST_PATH = "files/{fileId}/comments/{commentId}/replies/{replyId}";
            @Key
            private String commentId;
            @Key
            private String fileId;
            @Key
            private Boolean includeDeleted;
            @Key
            private String replyId;

            protected Get(String str, String str2, String str3) {
                super(Drive.this, "GET", REST_PATH, null, Reply.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.commentId = (String) Preconditions.checkNotNull(str2, "Required parameter commentId must be specified.");
                this.replyId = (String) Preconditions.checkNotNull(str3, "Required parameter replyId must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public Get setAlt(String str) {
                return (Get) super.setAlt(str);
            }

            public Get setFields(String str) {
                return (Get) super.setFields(str);
            }

            public Get setKey(String str) {
                return (Get) super.setKey(str);
            }

            public Get setOauthToken(String str) {
                return (Get) super.setOauthToken(str);
            }

            public Get setPrettyPrint(Boolean bool) {
                return (Get) super.setPrettyPrint(bool);
            }

            public Get setQuotaUser(String str) {
                return (Get) super.setQuotaUser(str);
            }

            public Get setUserIp(String str) {
                return (Get) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Get setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public Get setCommentId(String str) {
                this.commentId = str;
                return this;
            }

            public String getReplyId() {
                return this.replyId;
            }

            public Get setReplyId(String str) {
                this.replyId = str;
                return this;
            }

            public Boolean getIncludeDeleted() {
                return this.includeDeleted;
            }

            public Get setIncludeDeleted(Boolean bool) {
                this.includeDeleted = bool;
                return this;
            }

            public boolean isIncludeDeleted() {
                Boolean bool = this.includeDeleted;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeDeleted.booleanValue();
            }

            public Get set(String str, Object obj) {
                return (Get) super.set(str, obj);
            }
        }

        public class List extends DriveRequest<ReplyList> {
            private static final String REST_PATH = "files/{fileId}/comments/{commentId}/replies";
            @Key
            private String commentId;
            @Key
            private String fileId;
            @Key
            private Boolean includeDeleted;
            @Key
            private Integer pageSize;
            @Key
            private String pageToken;

            protected List(String str, String str2) {
                super(Drive.this, "GET", REST_PATH, null, ReplyList.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.commentId = (String) Preconditions.checkNotNull(str2, "Required parameter commentId must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public List setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public List setCommentId(String str) {
                this.commentId = str;
                return this;
            }

            public Boolean getIncludeDeleted() {
                return this.includeDeleted;
            }

            public List setIncludeDeleted(Boolean bool) {
                this.includeDeleted = bool;
                return this;
            }

            public boolean isIncludeDeleted() {
                Boolean bool = this.includeDeleted;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.includeDeleted.booleanValue();
            }

            public Integer getPageSize() {
                return this.pageSize;
            }

            public List setPageSize(Integer num) {
                this.pageSize = num;
                return this;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }
        }

        public class Update extends DriveRequest<Reply> {
            private static final String REST_PATH = "files/{fileId}/comments/{commentId}/replies/{replyId}";
            @Key
            private String commentId;
            @Key
            private String fileId;
            @Key
            private String replyId;

            protected Update(String str, String str2, String str3, Reply reply) {
                super(Drive.this, "PATCH", REST_PATH, reply, Reply.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.commentId = (String) Preconditions.checkNotNull(str2, "Required parameter commentId must be specified.");
                this.replyId = (String) Preconditions.checkNotNull(str3, "Required parameter replyId must be specified.");
                checkRequiredParameter(reply, Param.CONTENT);
                checkRequiredParameter(reply.getContent(), "Reply.getContent()");
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Update setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public Update setCommentId(String str) {
                this.commentId = str;
                return this;
            }

            public String getReplyId() {
                return this.replyId;
            }

            public Update setReplyId(String str) {
                this.replyId = str;
                return this;
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }
        }

        public Replies() {
        }

        public Create create(String str, String str2, Reply reply) throws IOException {
            Create create = new Create(str, str2, reply);
            Drive.this.initialize(create);
            return create;
        }

        public Delete delete(String str, String str2, String str3) throws IOException {
            Delete delete = new Delete(str, str2, str3);
            Drive.this.initialize(delete);
            return delete;
        }

        public Get get(String str, String str2, String str3) throws IOException {
            Get get = new Get(str, str2, str3);
            Drive.this.initialize(get);
            return get;
        }

        public List list(String str, String str2) throws IOException {
            List list = new List(str, str2);
            Drive.this.initialize(list);
            return list;
        }

        public Update update(String str, String str2, String str3, Reply reply) throws IOException {
            Update update = new Update(str, str2, str3, reply);
            Drive.this.initialize(update);
            return update;
        }
    }

    public class Revisions {

        public class Delete extends DriveRequest<Void> {
            private static final String REST_PATH = "files/{fileId}/revisions/{revisionId}";
            @Key
            private String fileId;
            @Key
            private String revisionId;

            protected Delete(String str, String str2) {
                super(Drive.this, "DELETE", REST_PATH, null, Void.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.revisionId = (String) Preconditions.checkNotNull(str2, "Required parameter revisionId must be specified.");
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Delete setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getRevisionId() {
                return this.revisionId;
            }

            public Delete setRevisionId(String str) {
                this.revisionId = str;
                return this;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }
        }

        public class Get extends DriveRequest<Revision> {
            private static final String REST_PATH = "files/{fileId}/revisions/{revisionId}";
            @Key
            private Boolean acknowledgeAbuse;
            @Key
            private String fileId;
            @Key
            private String revisionId;

            protected Get(String str, String str2) {
                super(Drive.this, "GET", REST_PATH, null, Revision.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.revisionId = (String) Preconditions.checkNotNull(str2, "Required parameter revisionId must be specified.");
                initializeMediaDownload();
            }

            public void executeMediaAndDownloadTo(OutputStream outputStream) throws IOException {
                super.executeMediaAndDownloadTo(outputStream);
            }

            public InputStream executeMediaAsInputStream() throws IOException {
                return super.executeMediaAsInputStream();
            }

            public HttpResponse executeMedia() throws IOException {
                return super.executeMedia();
            }

            public GenericUrl buildHttpRequestUrl() {
                String str;
                if (!"media".equals(get("alt")) || getMediaHttpUploader() != null) {
                    str = Drive.this.getBaseUrl();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Drive.this.getRootUrl());
                    sb.append("download/");
                    sb.append(Drive.this.getServicePath());
                    str = sb.toString();
                }
                return new GenericUrl(UriTemplate.expand(str, getUriTemplate(), this, true));
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public Get setAlt(String str) {
                return (Get) super.setAlt(str);
            }

            public Get setFields(String str) {
                return (Get) super.setFields(str);
            }

            public Get setKey(String str) {
                return (Get) super.setKey(str);
            }

            public Get setOauthToken(String str) {
                return (Get) super.setOauthToken(str);
            }

            public Get setPrettyPrint(Boolean bool) {
                return (Get) super.setPrettyPrint(bool);
            }

            public Get setQuotaUser(String str) {
                return (Get) super.setQuotaUser(str);
            }

            public Get setUserIp(String str) {
                return (Get) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Get setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getRevisionId() {
                return this.revisionId;
            }

            public Get setRevisionId(String str) {
                this.revisionId = str;
                return this;
            }

            public Boolean getAcknowledgeAbuse() {
                return this.acknowledgeAbuse;
            }

            public Get setAcknowledgeAbuse(Boolean bool) {
                this.acknowledgeAbuse = bool;
                return this;
            }

            public boolean isAcknowledgeAbuse() {
                Boolean bool = this.acknowledgeAbuse;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.acknowledgeAbuse.booleanValue();
            }

            public Get set(String str, Object obj) {
                return (Get) super.set(str, obj);
            }
        }

        public class List extends DriveRequest<RevisionList> {
            private static final String REST_PATH = "files/{fileId}/revisions";
            @Key
            private String fileId;
            @Key
            private Integer pageSize;
            @Key
            private String pageToken;

            protected List(String str) {
                super(Drive.this, "GET", REST_PATH, null, RevisionList.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public List setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public Integer getPageSize() {
                return this.pageSize;
            }

            public List setPageSize(Integer num) {
                this.pageSize = num;
                return this;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }
        }

        public class Update extends DriveRequest<Revision> {
            private static final String REST_PATH = "files/{fileId}/revisions/{revisionId}";
            @Key
            private String fileId;
            @Key
            private String revisionId;

            protected Update(String str, String str2, Revision revision) {
                super(Drive.this, "PATCH", REST_PATH, revision, Revision.class);
                this.fileId = (String) Preconditions.checkNotNull(str, "Required parameter fileId must be specified.");
                this.revisionId = (String) Preconditions.checkNotNull(str2, "Required parameter revisionId must be specified.");
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }

            public String getFileId() {
                return this.fileId;
            }

            public Update setFileId(String str) {
                this.fileId = str;
                return this;
            }

            public String getRevisionId() {
                return this.revisionId;
            }

            public Update setRevisionId(String str) {
                this.revisionId = str;
                return this;
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }
        }

        public Revisions() {
        }

        public Delete delete(String str, String str2) throws IOException {
            Delete delete = new Delete(str, str2);
            Drive.this.initialize(delete);
            return delete;
        }

        public Get get(String str, String str2) throws IOException {
            Get get = new Get(str, str2);
            Drive.this.initialize(get);
            return get;
        }

        public List list(String str) throws IOException {
            List list = new List(str);
            Drive.this.initialize(list);
            return list;
        }

        public Update update(String str, String str2, Revision revision) throws IOException {
            Update update = new Update(str, str2, revision);
            Drive.this.initialize(update);
            return update;
        }
    }

    public class Teamdrives {

        public class Create extends DriveRequest<TeamDrive> {
            private static final String REST_PATH = "teamdrives";
            @Key
            private String requestId;

            protected Create(String str, TeamDrive teamDrive) {
                super(Drive.this, "POST", REST_PATH, teamDrive, TeamDrive.class);
                this.requestId = (String) Preconditions.checkNotNull(str, "Required parameter requestId must be specified.");
            }

            public Create setAlt(String str) {
                return (Create) super.setAlt(str);
            }

            public Create setFields(String str) {
                return (Create) super.setFields(str);
            }

            public Create setKey(String str) {
                return (Create) super.setKey(str);
            }

            public Create setOauthToken(String str) {
                return (Create) super.setOauthToken(str);
            }

            public Create setPrettyPrint(Boolean bool) {
                return (Create) super.setPrettyPrint(bool);
            }

            public Create setQuotaUser(String str) {
                return (Create) super.setQuotaUser(str);
            }

            public Create setUserIp(String str) {
                return (Create) super.setUserIp(str);
            }

            public String getRequestId() {
                return this.requestId;
            }

            public Create setRequestId(String str) {
                this.requestId = str;
                return this;
            }

            public Create set(String str, Object obj) {
                return (Create) super.set(str, obj);
            }
        }

        public class Delete extends DriveRequest<Void> {
            private static final String REST_PATH = "teamdrives/{teamDriveId}";
            @Key
            private String teamDriveId;

            protected Delete(String str) {
                super(Drive.this, "DELETE", REST_PATH, null, Void.class);
                this.teamDriveId = (String) Preconditions.checkNotNull(str, "Required parameter teamDriveId must be specified.");
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }

            public String getTeamDriveId() {
                return this.teamDriveId;
            }

            public Delete setTeamDriveId(String str) {
                this.teamDriveId = str;
                return this;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }
        }

        public class Get extends DriveRequest<TeamDrive> {
            private static final String REST_PATH = "teamdrives/{teamDriveId}";
            @Key
            private String teamDriveId;
            @Key
            private Boolean useDomainAdminAccess;

            protected Get(String str) {
                super(Drive.this, "GET", REST_PATH, null, TeamDrive.class);
                this.teamDriveId = (String) Preconditions.checkNotNull(str, "Required parameter teamDriveId must be specified.");
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public Get setAlt(String str) {
                return (Get) super.setAlt(str);
            }

            public Get setFields(String str) {
                return (Get) super.setFields(str);
            }

            public Get setKey(String str) {
                return (Get) super.setKey(str);
            }

            public Get setOauthToken(String str) {
                return (Get) super.setOauthToken(str);
            }

            public Get setPrettyPrint(Boolean bool) {
                return (Get) super.setPrettyPrint(bool);
            }

            public Get setQuotaUser(String str) {
                return (Get) super.setQuotaUser(str);
            }

            public Get setUserIp(String str) {
                return (Get) super.setUserIp(str);
            }

            public String getTeamDriveId() {
                return this.teamDriveId;
            }

            public Get setTeamDriveId(String str) {
                this.teamDriveId = str;
                return this;
            }

            public Boolean getUseDomainAdminAccess() {
                return this.useDomainAdminAccess;
            }

            public Get setUseDomainAdminAccess(Boolean bool) {
                this.useDomainAdminAccess = bool;
                return this;
            }

            public boolean isUseDomainAdminAccess() {
                Boolean bool = this.useDomainAdminAccess;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useDomainAdminAccess.booleanValue();
            }

            public Get set(String str, Object obj) {
                return (Get) super.set(str, obj);
            }
        }

        public class List extends DriveRequest<TeamDriveList> {
            private static final String REST_PATH = "teamdrives";
            @Key
            private Integer pageSize;
            @Key
            private String pageToken;
            @Key

            /* renamed from: q */
            private String f207q;
            @Key
            private Boolean useDomainAdminAccess;

            protected List() {
                super(Drive.this, "GET", REST_PATH, null, TeamDriveList.class);
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public Integer getPageSize() {
                return this.pageSize;
            }

            public List setPageSize(Integer num) {
                this.pageSize = num;
                return this;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public String getQ() {
                return this.f207q;
            }

            public List setQ(String str) {
                this.f207q = str;
                return this;
            }

            public Boolean getUseDomainAdminAccess() {
                return this.useDomainAdminAccess;
            }

            public List setUseDomainAdminAccess(Boolean bool) {
                this.useDomainAdminAccess = bool;
                return this;
            }

            public boolean isUseDomainAdminAccess() {
                Boolean bool = this.useDomainAdminAccess;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useDomainAdminAccess.booleanValue();
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }
        }

        public class Update extends DriveRequest<TeamDrive> {
            private static final String REST_PATH = "teamdrives/{teamDriveId}";
            @Key
            private String teamDriveId;
            @Key
            private Boolean useDomainAdminAccess;

            protected Update(String str, TeamDrive teamDrive) {
                super(Drive.this, "PATCH", REST_PATH, teamDrive, TeamDrive.class);
                this.teamDriveId = (String) Preconditions.checkNotNull(str, "Required parameter teamDriveId must be specified.");
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }

            public String getTeamDriveId() {
                return this.teamDriveId;
            }

            public Update setTeamDriveId(String str) {
                this.teamDriveId = str;
                return this;
            }

            public Boolean getUseDomainAdminAccess() {
                return this.useDomainAdminAccess;
            }

            public Update setUseDomainAdminAccess(Boolean bool) {
                this.useDomainAdminAccess = bool;
                return this;
            }

            public boolean isUseDomainAdminAccess() {
                Boolean bool = this.useDomainAdminAccess;
                if (bool == null || bool == Data.NULL_BOOLEAN) {
                    return false;
                }
                return this.useDomainAdminAccess.booleanValue();
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }
        }

        public Teamdrives() {
        }

        public Create create(String str, TeamDrive teamDrive) throws IOException {
            Create create = new Create(str, teamDrive);
            Drive.this.initialize(create);
            return create;
        }

        public Delete delete(String str) throws IOException {
            Delete delete = new Delete(str);
            Drive.this.initialize(delete);
            return delete;
        }

        public Get get(String str) throws IOException {
            Get get = new Get(str);
            Drive.this.initialize(get);
            return get;
        }

        public List list() throws IOException {
            List list = new List();
            Drive.this.initialize(list);
            return list;
        }

        public Update update(String str, TeamDrive teamDrive) throws IOException {
            Update update = new Update(str, teamDrive);
            Drive.this.initialize(update);
            return update;
        }
    }

    static {
        Preconditions.checkState(GoogleUtils.MAJOR_VERSION.intValue() == 1 && GoogleUtils.MINOR_VERSION.intValue() >= 15, "You are currently running with version %s of google-api-client. You need at least version 1.15 of google-api-client to run version 1.27.0 of the Drive API library.", GoogleUtils.VERSION);
    }

    public Drive(HttpTransport httpTransport, JsonFactory jsonFactory, HttpRequestInitializer httpRequestInitializer) {
        this(new Builder(httpTransport, jsonFactory, httpRequestInitializer));
    }

    Drive(Builder builder) {
        super(builder);
    }

    /* access modifiers changed from: protected */
    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
        super.initialize(abstractGoogleClientRequest);
    }

    public About about() {
        return new About();
    }

    public Changes changes() {
        return new Changes();
    }

    public Channels channels() {
        return new Channels();
    }

    public Comments comments() {
        return new Comments();
    }

    public Files files() {
        return new Files();
    }

    public Permissions permissions() {
        return new Permissions();
    }

    public Replies replies() {
        return new Replies();
    }

    public Revisions revisions() {
        return new Revisions();
    }

    public Teamdrives teamdrives() {
        return new Teamdrives();
    }
}
