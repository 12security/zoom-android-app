package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxItem;
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

/* renamed from: com.dropbox.core.v2.teamlog.EmmCreateExceptionsReportType */
public class EmmCreateExceptionsReportType {
    protected final String description;

    /* renamed from: com.dropbox.core.v2.teamlog.EmmCreateExceptionsReportType$Serializer */
    static class Serializer extends StructSerializer<EmmCreateExceptionsReportType> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(EmmCreateExceptionsReportType emmCreateExceptionsReportType, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxItem.FIELD_DESCRIPTION);
            StoneSerializers.string().serialize(emmCreateExceptionsReportType.description, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public EmmCreateExceptionsReportType deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxItem.FIELD_DESCRIPTION.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    EmmCreateExceptionsReportType emmCreateExceptionsReportType = new EmmCreateExceptionsReportType(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(emmCreateExceptionsReportType, emmCreateExceptionsReportType.toStringMultiline());
                    return emmCreateExceptionsReportType;
                }
                throw new JsonParseException(jsonParser, "Required field \"description\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public EmmCreateExceptionsReportType(String str) {
        if (str != null) {
            this.description = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'description' is null");
    }

    public String getDescription() {
        return this.description;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.description});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        EmmCreateExceptionsReportType emmCreateExceptionsReportType = (EmmCreateExceptionsReportType) obj;
        String str = this.description;
        String str2 = emmCreateExceptionsReportType.description;
        if (str != str2 && !str.equals(str2)) {
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
