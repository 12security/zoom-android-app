package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFile;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.UpdateFileMemberArgs */
class UpdateFileMemberArgs extends ChangeFileMemberAccessArgs {

    /* renamed from: com.dropbox.core.v2.sharing.UpdateFileMemberArgs$Serializer */
    static class Serializer extends StructSerializer<UpdateFileMemberArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UpdateFileMemberArgs updateFileMemberArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxFile.TYPE);
            StoneSerializers.string().serialize(updateFileMemberArgs.file, jsonGenerator);
            jsonGenerator.writeFieldName("member");
            com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.serialize(updateFileMemberArgs.member, jsonGenerator);
            jsonGenerator.writeFieldName("access_level");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(updateFileMemberArgs.accessLevel, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UpdateFileMemberArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                MemberSelector memberSelector = null;
                AccessLevel accessLevel = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxFile.TYPE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("member".equals(currentName)) {
                        memberSelector = com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("access_level".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"file\" missing.");
                } else if (memberSelector == null) {
                    throw new JsonParseException(jsonParser, "Required field \"member\" missing.");
                } else if (accessLevel != null) {
                    UpdateFileMemberArgs updateFileMemberArgs = new UpdateFileMemberArgs(str2, memberSelector, accessLevel);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(updateFileMemberArgs, updateFileMemberArgs.toStringMultiline());
                    return updateFileMemberArgs;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"access_level\" missing.");
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

    public UpdateFileMemberArgs(String str, MemberSelector memberSelector, AccessLevel accessLevel) {
        super(str, memberSelector, accessLevel);
    }

    public String getFile() {
        return this.file;
    }

    public MemberSelector getMember() {
        return this.member;
    }

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public int hashCode() {
        return getClass().toString().hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        UpdateFileMemberArgs updateFileMemberArgs = (UpdateFileMemberArgs) obj;
        if ((this.file != updateFileMemberArgs.file && !this.file.equals(updateFileMemberArgs.file)) || ((this.member != updateFileMemberArgs.member && !this.member.equals(updateFileMemberArgs.member)) || (this.accessLevel != updateFileMemberArgs.accessLevel && !this.accessLevel.equals(updateFileMemberArgs.accessLevel)))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
