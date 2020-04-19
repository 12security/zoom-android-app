package com.dropbox.core.p005v2.team;

import com.dropbox.core.p005v2.files.ContentSyncSettingArg;
import com.dropbox.core.p005v2.files.SyncSettingArg;
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

/* renamed from: com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsArg */
class TeamFolderUpdateSyncSettingsArg extends TeamFolderIdArg {
    protected final List<ContentSyncSettingArg> contentSyncSettings;
    protected final SyncSettingArg syncSetting;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsArg$Builder */
    public static class Builder {
        protected List<ContentSyncSettingArg> contentSyncSettings;
        protected SyncSettingArg syncSetting;
        protected final String teamFolderId;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'teamFolderId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.teamFolderId = str;
                this.syncSetting = null;
                this.contentSyncSettings = null;
            } else {
                throw new IllegalArgumentException("String 'teamFolderId' does not match pattern");
            }
        }

        public Builder withSyncSetting(SyncSettingArg syncSettingArg) {
            this.syncSetting = syncSettingArg;
            return this;
        }

        public Builder withContentSyncSettings(List<ContentSyncSettingArg> list) {
            if (list != null) {
                for (ContentSyncSettingArg contentSyncSettingArg : list) {
                    if (contentSyncSettingArg == null) {
                        throw new IllegalArgumentException("An item in list 'contentSyncSettings' is null");
                    }
                }
            }
            this.contentSyncSettings = list;
            return this;
        }

        public TeamFolderUpdateSyncSettingsArg build() {
            return new TeamFolderUpdateSyncSettingsArg(this.teamFolderId, this.syncSetting, this.contentSyncSettings);
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsArg$Serializer */
    static class Serializer extends StructSerializer<TeamFolderUpdateSyncSettingsArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderUpdateSyncSettingsArg teamFolderUpdateSyncSettingsArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_folder_id");
            StoneSerializers.string().serialize(teamFolderUpdateSyncSettingsArg.teamFolderId, jsonGenerator);
            if (teamFolderUpdateSyncSettingsArg.syncSetting != null) {
                jsonGenerator.writeFieldName("sync_setting");
                StoneSerializers.nullable(com.dropbox.core.p005v2.files.SyncSettingArg.Serializer.INSTANCE).serialize(teamFolderUpdateSyncSettingsArg.syncSetting, jsonGenerator);
            }
            if (teamFolderUpdateSyncSettingsArg.contentSyncSettings != null) {
                jsonGenerator.writeFieldName("content_sync_settings");
                StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.files.ContentSyncSettingArg.Serializer.INSTANCE)).serialize(teamFolderUpdateSyncSettingsArg.contentSyncSettings, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderUpdateSyncSettingsArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SyncSettingArg syncSettingArg = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("sync_setting".equals(currentName)) {
                        syncSettingArg = (SyncSettingArg) StoneSerializers.nullable(com.dropbox.core.p005v2.files.SyncSettingArg.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("content_sync_settings".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.files.ContentSyncSettingArg.Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    TeamFolderUpdateSyncSettingsArg teamFolderUpdateSyncSettingsArg = new TeamFolderUpdateSyncSettingsArg(str2, syncSettingArg, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamFolderUpdateSyncSettingsArg, teamFolderUpdateSyncSettingsArg.toStringMultiline());
                    return teamFolderUpdateSyncSettingsArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"team_folder_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamFolderUpdateSyncSettingsArg(String str, SyncSettingArg syncSettingArg, List<ContentSyncSettingArg> list) {
        super(str);
        this.syncSetting = syncSettingArg;
        if (list != null) {
            for (ContentSyncSettingArg contentSyncSettingArg : list) {
                if (contentSyncSettingArg == null) {
                    throw new IllegalArgumentException("An item in list 'contentSyncSettings' is null");
                }
            }
        }
        this.contentSyncSettings = list;
    }

    public TeamFolderUpdateSyncSettingsArg(String str) {
        this(str, null, null);
    }

    public String getTeamFolderId() {
        return this.teamFolderId;
    }

    public SyncSettingArg getSyncSetting() {
        return this.syncSetting;
    }

    public List<ContentSyncSettingArg> getContentSyncSettings() {
        return this.contentSyncSettings;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.syncSetting, this.contentSyncSettings});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0042, code lost:
        if (r2.equals(r5) == false) goto L_0x0045;
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
            if (r2 == 0) goto L_0x0047
            com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsArg r5 = (com.dropbox.core.p005v2.team.TeamFolderUpdateSyncSettingsArg) r5
            java.lang.String r2 = r4.teamFolderId
            java.lang.String r3 = r5.teamFolderId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.teamFolderId
            java.lang.String r3 = r5.teamFolderId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0028:
            com.dropbox.core.v2.files.SyncSettingArg r2 = r4.syncSetting
            com.dropbox.core.v2.files.SyncSettingArg r3 = r5.syncSetting
            if (r2 == r3) goto L_0x0036
            if (r2 == 0) goto L_0x0045
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0036:
            java.util.List<com.dropbox.core.v2.files.ContentSyncSettingArg> r2 = r4.contentSyncSettings
            java.util.List<com.dropbox.core.v2.files.ContentSyncSettingArg> r5 = r5.contentSyncSettings
            if (r2 == r5) goto L_0x0046
            if (r2 == 0) goto L_0x0045
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            r0 = 0
        L_0x0046:
            return r0
        L_0x0047:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.TeamFolderUpdateSyncSettingsArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
