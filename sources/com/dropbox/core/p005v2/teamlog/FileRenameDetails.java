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
import java.util.List;

/* renamed from: com.dropbox.core.v2.teamlog.FileRenameDetails */
public class FileRenameDetails {
    protected final List<RelocateAssetReferencesLogInfo> relocateActionDetails;

    /* renamed from: com.dropbox.core.v2.teamlog.FileRenameDetails$Serializer */
    static class Serializer extends StructSerializer<FileRenameDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileRenameDetails fileRenameDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("relocate_action_details");
            StoneSerializers.list(Serializer.INSTANCE).serialize(fileRenameDetails.relocateActionDetails, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileRenameDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("relocate_action_details".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    FileRenameDetails fileRenameDetails = new FileRenameDetails(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileRenameDetails, fileRenameDetails.toStringMultiline());
                    return fileRenameDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"relocate_action_details\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FileRenameDetails(List<RelocateAssetReferencesLogInfo> list) {
        if (list != null) {
            for (RelocateAssetReferencesLogInfo relocateAssetReferencesLogInfo : list) {
                if (relocateAssetReferencesLogInfo == null) {
                    throw new IllegalArgumentException("An item in list 'relocateActionDetails' is null");
                }
            }
            this.relocateActionDetails = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'relocateActionDetails' is null");
    }

    public List<RelocateAssetReferencesLogInfo> getRelocateActionDetails() {
        return this.relocateActionDetails;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.relocateActionDetails});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        FileRenameDetails fileRenameDetails = (FileRenameDetails) obj;
        List<RelocateAssetReferencesLogInfo> list = this.relocateActionDetails;
        List<RelocateAssetReferencesLogInfo> list2 = fileRenameDetails.relocateActionDetails;
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
