package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.TeamFolderRenameDetails */
public class TeamFolderRenameDetails {
    protected final String newFolderName;
    protected final String previousFolderName;

    /* renamed from: com.dropbox.core.v2.teamlog.TeamFolderRenameDetails$Serializer */
    static class Serializer extends StructSerializer<TeamFolderRenameDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderRenameDetails teamFolderRenameDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("previous_folder_name");
            StoneSerializers.string().serialize(teamFolderRenameDetails.previousFolderName, jsonGenerator);
            jsonGenerator.writeFieldName("new_folder_name");
            StoneSerializers.string().serialize(teamFolderRenameDetails.newFolderName, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderRenameDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("previous_folder_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("new_folder_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"previous_folder_name\" missing.");
                } else if (str3 != null) {
                    TeamFolderRenameDetails teamFolderRenameDetails = new TeamFolderRenameDetails(str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamFolderRenameDetails, teamFolderRenameDetails.toStringMultiline());
                    return teamFolderRenameDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"new_folder_name\" missing.");
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

    public TeamFolderRenameDetails(String str, String str2) {
        if (str != null) {
            this.previousFolderName = str;
            if (str2 != null) {
                this.newFolderName = str2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'newFolderName' is null");
        }
        throw new IllegalArgumentException("Required value for 'previousFolderName' is null");
    }

    public String getPreviousFolderName() {
        return this.previousFolderName;
    }

    public String getNewFolderName() {
        return this.newFolderName;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousFolderName, this.newFolderName});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.teamlog.TeamFolderRenameDetails r5 = (com.dropbox.core.p005v2.teamlog.TeamFolderRenameDetails) r5
            java.lang.String r2 = r4.previousFolderName
            java.lang.String r3 = r5.previousFolderName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.newFolderName
            java.lang.String r5 = r5.newFolderName
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.TeamFolderRenameDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
