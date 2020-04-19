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

/* renamed from: com.dropbox.core.v2.teamlog.FileCopyDetails */
public class FileCopyDetails {
    protected final List<RelocateAssetReferencesLogInfo> relocateActionDetails;

    /* renamed from: com.dropbox.core.v2.teamlog.FileCopyDetails$Serializer */
    static class Serializer extends StructSerializer<FileCopyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileCopyDetails fileCopyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("relocate_action_details");
            StoneSerializers.list(Serializer.INSTANCE).serialize(fileCopyDetails.relocateActionDetails, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileCopyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    FileCopyDetails fileCopyDetails = new FileCopyDetails(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileCopyDetails, fileCopyDetails.toStringMultiline());
                    return fileCopyDetails;
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

    public FileCopyDetails(List<RelocateAssetReferencesLogInfo> list) {
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
        FileCopyDetails fileCopyDetails = (FileCopyDetails) obj;
        List<RelocateAssetReferencesLogInfo> list = this.relocateActionDetails;
        List<RelocateAssetReferencesLogInfo> list2 = fileCopyDetails.relocateActionDetails;
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
