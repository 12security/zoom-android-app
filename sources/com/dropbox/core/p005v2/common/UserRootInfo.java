package com.dropbox.core.p005v2.common;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.common.UserRootInfo */
public class UserRootInfo extends RootInfo {

    /* renamed from: com.dropbox.core.v2.common.UserRootInfo$Serializer */
    static class Serializer extends StructSerializer<UserRootInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserRootInfo userRootInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("user", jsonGenerator);
            jsonGenerator.writeFieldName("root_namespace_id");
            StoneSerializers.string().serialize(userRootInfo.rootNamespaceId, jsonGenerator);
            jsonGenerator.writeFieldName("home_namespace_id");
            StoneSerializers.string().serialize(userRootInfo.homeNamespaceId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserRootInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("user".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("root_namespace_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("home_namespace_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"root_namespace_id\" missing.");
                } else if (str3 != null) {
                    UserRootInfo userRootInfo = new UserRootInfo(str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(userRootInfo, userRootInfo.toStringMultiline());
                    return userRootInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"home_namespace_id\" missing.");
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

    public UserRootInfo(String str, String str2) {
        super(str, str2);
    }

    public String getRootNamespaceId() {
        return this.rootNamespaceId;
    }

    public String getHomeNamespaceId() {
        return this.homeNamespaceId;
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
        UserRootInfo userRootInfo = (UserRootInfo) obj;
        if ((this.rootNamespaceId != userRootInfo.rootNamespaceId && !this.rootNamespaceId.equals(userRootInfo.rootNamespaceId)) || (this.homeNamespaceId != userRootInfo.homeNamespaceId && !this.homeNamespaceId.equals(userRootInfo.homeNamespaceId))) {
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
