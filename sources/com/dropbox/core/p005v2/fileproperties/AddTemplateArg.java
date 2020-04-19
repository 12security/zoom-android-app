package com.dropbox.core.p005v2.fileproperties;

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
import java.util.List;

/* renamed from: com.dropbox.core.v2.fileproperties.AddTemplateArg */
public class AddTemplateArg extends PropertyGroupTemplate {

    /* renamed from: com.dropbox.core.v2.fileproperties.AddTemplateArg$Serializer */
    public static class Serializer extends StructSerializer<AddTemplateArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(AddTemplateArg addTemplateArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(addTemplateArg.name, jsonGenerator);
            jsonGenerator.writeFieldName(BoxItem.FIELD_DESCRIPTION);
            StoneSerializers.string().serialize(addTemplateArg.description, jsonGenerator);
            jsonGenerator.writeFieldName("fields");
            StoneSerializers.list(Serializer.INSTANCE).serialize(addTemplateArg.fields, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AddTemplateArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (BoxItem.FIELD_DESCRIPTION.equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("fields".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"description\" missing.");
                } else if (list != null) {
                    AddTemplateArg addTemplateArg = new AddTemplateArg(str2, str3, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(addTemplateArg, addTemplateArg.toStringMultiline());
                    return addTemplateArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"fields\" missing.");
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

    public AddTemplateArg(String str, String str2, List<PropertyFieldTemplate> list) {
        super(str, str2, list);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<PropertyFieldTemplate> getFields() {
        return this.fields;
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
        AddTemplateArg addTemplateArg = (AddTemplateArg) obj;
        if ((this.name != addTemplateArg.name && !this.name.equals(addTemplateArg.name)) || ((this.description != addTemplateArg.description && !this.description.equals(addTemplateArg.description)) || (this.fields != addTemplateArg.fields && !this.fields.equals(addTemplateArg.fields)))) {
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
