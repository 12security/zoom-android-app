package com.dropbox.core.p005v2.fileproperties;

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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.fileproperties.UpdateTemplateResult */
public class UpdateTemplateResult {
    protected final String templateId;

    /* renamed from: com.dropbox.core.v2.fileproperties.UpdateTemplateResult$Serializer */
    public static class Serializer extends StructSerializer<UpdateTemplateResult> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(UpdateTemplateResult updateTemplateResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("template_id");
            StoneSerializers.string().serialize(updateTemplateResult.templateId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UpdateTemplateResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("template_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    UpdateTemplateResult updateTemplateResult = new UpdateTemplateResult(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(updateTemplateResult, updateTemplateResult.toStringMultiline());
                    return updateTemplateResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"template_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UpdateTemplateResult(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'templateId' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'templateId' is shorter than 1");
        } else if (Pattern.matches("(/|ptid:).*", str)) {
            this.templateId = str;
        } else {
            throw new IllegalArgumentException("String 'templateId' does not match pattern");
        }
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.templateId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        UpdateTemplateResult updateTemplateResult = (UpdateTemplateResult) obj;
        String str = this.templateId;
        String str2 = updateTemplateResult.templateId;
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
