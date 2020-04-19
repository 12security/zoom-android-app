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
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.fileproperties.ListTemplateResult */
public class ListTemplateResult {
    protected final List<String> templateIds;

    /* renamed from: com.dropbox.core.v2.fileproperties.ListTemplateResult$Serializer */
    public static class Serializer extends StructSerializer<ListTemplateResult> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(ListTemplateResult listTemplateResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("template_ids");
            StoneSerializers.list(StoneSerializers.string()).serialize(listTemplateResult.templateIds, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListTemplateResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
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
                    if ("template_ids".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    ListTemplateResult listTemplateResult = new ListTemplateResult(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listTemplateResult, listTemplateResult.toStringMultiline());
                    return listTemplateResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"template_ids\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListTemplateResult(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'templateIds' is null");
                } else if (str.length() < 1) {
                    throw new IllegalArgumentException("Stringan item in list 'templateIds' is shorter than 1");
                } else if (!Pattern.matches("(/|ptid:).*", str)) {
                    throw new IllegalArgumentException("Stringan item in list 'templateIds' does not match pattern");
                }
            }
            this.templateIds = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'templateIds' is null");
    }

    public List<String> getTemplateIds() {
        return this.templateIds;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.templateIds});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListTemplateResult listTemplateResult = (ListTemplateResult) obj;
        List<String> list = this.templateIds;
        List<String> list2 = listTemplateResult.templateIds;
        if (list != list2 && !list.equals(list2)) {
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
