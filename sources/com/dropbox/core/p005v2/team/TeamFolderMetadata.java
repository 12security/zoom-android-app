package com.dropbox.core.p005v2.team;

import com.dropbox.core.p005v2.files.ContentSyncSetting;
import com.dropbox.core.p005v2.files.SyncSetting;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.team.TeamFolderMetadata */
public class TeamFolderMetadata {
    protected final List<ContentSyncSetting> contentSyncSettings;
    protected final boolean isTeamSharedDropbox;
    protected final String name;
    protected final TeamFolderStatus status;
    protected final SyncSetting syncSetting;
    protected final String teamFolderId;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderMetadata$Serializer */
    static class Serializer extends StructSerializer<TeamFolderMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderMetadata teamFolderMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_folder_id");
            StoneSerializers.string().serialize(teamFolderMetadata.teamFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(teamFolderMetadata.name, jsonGenerator);
            jsonGenerator.writeFieldName("status");
            com.dropbox.core.p005v2.team.TeamFolderStatus.Serializer.INSTANCE.serialize(teamFolderMetadata.status, jsonGenerator);
            jsonGenerator.writeFieldName("is_team_shared_dropbox");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(teamFolderMetadata.isTeamSharedDropbox), jsonGenerator);
            jsonGenerator.writeFieldName("sync_setting");
            com.dropbox.core.p005v2.files.SyncSetting.Serializer.INSTANCE.serialize(teamFolderMetadata.syncSetting, jsonGenerator);
            jsonGenerator.writeFieldName("content_sync_settings");
            StoneSerializers.list(com.dropbox.core.p005v2.files.ContentSyncSetting.Serializer.INSTANCE).serialize(teamFolderMetadata.contentSyncSettings, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                TeamFolderStatus teamFolderStatus = null;
                SyncSetting syncSetting = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("status".equals(currentName)) {
                        teamFolderStatus = com.dropbox.core.p005v2.team.TeamFolderStatus.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("is_team_shared_dropbox".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("sync_setting".equals(currentName)) {
                        syncSetting = com.dropbox.core.p005v2.files.SyncSetting.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("content_sync_settings".equals(currentName)) {
                        list = (List) StoneSerializers.list(com.dropbox.core.p005v2.files.ContentSyncSetting.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"team_folder_id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (teamFolderStatus == null) {
                    throw new JsonParseException(jsonParser, "Required field \"status\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"is_team_shared_dropbox\" missing.");
                } else if (syncSetting == null) {
                    throw new JsonParseException(jsonParser, "Required field \"sync_setting\" missing.");
                } else if (list != null) {
                    TeamFolderMetadata teamFolderMetadata = new TeamFolderMetadata(str2, str3, teamFolderStatus, bool.booleanValue(), syncSetting, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamFolderMetadata, teamFolderMetadata.toStringMultiline());
                    return teamFolderMetadata;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"content_sync_settings\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public TeamFolderMetadata(String str, String str2, TeamFolderStatus teamFolderStatus, boolean z, SyncSetting syncSetting2, List<ContentSyncSetting> list) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'teamFolderId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.teamFolderId = str;
            if (str2 != null) {
                this.name = str2;
                if (teamFolderStatus != null) {
                    this.status = teamFolderStatus;
                    this.isTeamSharedDropbox = z;
                    if (syncSetting2 != null) {
                        this.syncSetting = syncSetting2;
                        if (list != null) {
                            for (ContentSyncSetting contentSyncSetting : list) {
                                if (contentSyncSetting == null) {
                                    throw new IllegalArgumentException("An item in list 'contentSyncSettings' is null");
                                }
                            }
                            this.contentSyncSettings = list;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'contentSyncSettings' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'syncSetting' is null");
                }
                throw new IllegalArgumentException("Required value for 'status' is null");
            }
            throw new IllegalArgumentException("Required value for 'name' is null");
        } else {
            throw new IllegalArgumentException("String 'teamFolderId' does not match pattern");
        }
    }

    public String getTeamFolderId() {
        return this.teamFolderId;
    }

    public String getName() {
        return this.name;
    }

    public TeamFolderStatus getStatus() {
        return this.status;
    }

    public boolean getIsTeamSharedDropbox() {
        return this.isTeamSharedDropbox;
    }

    public SyncSetting getSyncSetting() {
        return this.syncSetting;
    }

    public List<ContentSyncSetting> getContentSyncSettings() {
        return this.contentSyncSettings;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.teamFolderId, this.name, this.status, Boolean.valueOf(this.isTeamSharedDropbox), this.syncSetting, this.contentSyncSettings});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0058, code lost:
        if (r2.equals(r5) == false) goto L_0x005b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
            com.dropbox.core.v2.team.TeamFolderMetadata r5 = (com.dropbox.core.p005v2.team.TeamFolderMetadata) r5
            java.lang.String r2 = r4.teamFolderId
            java.lang.String r3 = r5.teamFolderId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x0024:
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x0030:
            com.dropbox.core.v2.team.TeamFolderStatus r2 = r4.status
            com.dropbox.core.v2.team.TeamFolderStatus r3 = r5.status
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x003c:
            boolean r2 = r4.isTeamSharedDropbox
            boolean r3 = r5.isTeamSharedDropbox
            if (r2 != r3) goto L_0x005b
            com.dropbox.core.v2.files.SyncSetting r2 = r4.syncSetting
            com.dropbox.core.v2.files.SyncSetting r3 = r5.syncSetting
            if (r2 == r3) goto L_0x004e
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x004e:
            java.util.List<com.dropbox.core.v2.files.ContentSyncSetting> r2 = r4.contentSyncSettings
            java.util.List<com.dropbox.core.v2.files.ContentSyncSetting> r5 = r5.contentSyncSettings
            if (r2 == r5) goto L_0x005c
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x005b
            goto L_0x005c
        L_0x005b:
            r0 = 0
        L_0x005c:
            return r0
        L_0x005d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.TeamFolderMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
