package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.TeamFolderCreateArg */
class TeamFolderCreateArg {
    protected final String name;
    protected final SyncSettingArg syncSetting;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderCreateArg$Serializer */
    static class Serializer extends StructSerializer<TeamFolderCreateArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderCreateArg teamFolderCreateArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(teamFolderCreateArg.name, jsonGenerator);
            if (teamFolderCreateArg.syncSetting != null) {
                jsonGenerator.writeFieldName("sync_setting");
                StoneSerializers.nullable(com.dropbox.core.p005v2.files.SyncSettingArg.Serializer.INSTANCE).serialize(teamFolderCreateArg.syncSetting, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderCreateArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("sync_setting".equals(currentName)) {
                        syncSettingArg = (SyncSettingArg) StoneSerializers.nullable(com.dropbox.core.p005v2.files.SyncSettingArg.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    TeamFolderCreateArg teamFolderCreateArg = new TeamFolderCreateArg(str2, syncSettingArg);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamFolderCreateArg, teamFolderCreateArg.toStringMultiline());
                    return teamFolderCreateArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamFolderCreateArg(String str, SyncSettingArg syncSettingArg) {
        if (str != null) {
            this.name = str;
            this.syncSetting = syncSettingArg;
            return;
        }
        throw new IllegalArgumentException("Required value for 'name' is null");
    }

    public TeamFolderCreateArg(String str) {
        this(str, null);
    }

    public String getName() {
        return this.name;
    }

    public SyncSettingArg getSyncSetting() {
        return this.syncSetting;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.name, this.syncSetting});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.team.TeamFolderCreateArg r5 = (com.dropbox.core.p005v2.team.TeamFolderCreateArg) r5
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.files.SyncSettingArg r2 = r4.syncSetting
            com.dropbox.core.v2.files.SyncSettingArg r5 = r5.syncSetting
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.TeamFolderCreateArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
