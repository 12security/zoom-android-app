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

/* renamed from: com.dropbox.core.v2.teamlog.PaperContentRemoveFromFolderDetails */
public class PaperContentRemoveFromFolderDetails {
    protected final String eventUuid;
    protected final long parentAssetIndex;
    protected final long targetAssetIndex;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperContentRemoveFromFolderDetails$Serializer */
    static class Serializer extends StructSerializer<PaperContentRemoveFromFolderDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperContentRemoveFromFolderDetails paperContentRemoveFromFolderDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(paperContentRemoveFromFolderDetails.eventUuid, jsonGenerator);
            jsonGenerator.writeFieldName("target_asset_index");
            StoneSerializers.uInt64().serialize(Long.valueOf(paperContentRemoveFromFolderDetails.targetAssetIndex), jsonGenerator);
            jsonGenerator.writeFieldName("parent_asset_index");
            StoneSerializers.uInt64().serialize(Long.valueOf(paperContentRemoveFromFolderDetails.parentAssetIndex), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperContentRemoveFromFolderDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l2 = null;
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("event_uuid".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("target_asset_index".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("parent_asset_index".equals(currentName)) {
                        l2 = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"event_uuid\" missing.");
                } else if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"target_asset_index\" missing.");
                } else if (l2 != null) {
                    PaperContentRemoveFromFolderDetails paperContentRemoveFromFolderDetails = new PaperContentRemoveFromFolderDetails(str2, l.longValue(), l2.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperContentRemoveFromFolderDetails, paperContentRemoveFromFolderDetails.toStringMultiline());
                    return paperContentRemoveFromFolderDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"parent_asset_index\" missing.");
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

    public PaperContentRemoveFromFolderDetails(String str, long j, long j2) {
        if (str != null) {
            this.eventUuid = str;
            this.targetAssetIndex = j;
            this.parentAssetIndex = j2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'eventUuid' is null");
    }

    public String getEventUuid() {
        return this.eventUuid;
    }

    public long getTargetAssetIndex() {
        return this.targetAssetIndex;
    }

    public long getParentAssetIndex() {
        return this.parentAssetIndex;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.eventUuid, Long.valueOf(this.targetAssetIndex), Long.valueOf(this.parentAssetIndex)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        PaperContentRemoveFromFolderDetails paperContentRemoveFromFolderDetails = (PaperContentRemoveFromFolderDetails) obj;
        String str = this.eventUuid;
        String str2 = paperContentRemoveFromFolderDetails.eventUuid;
        if (!((str == str2 || str.equals(str2)) && this.targetAssetIndex == paperContentRemoveFromFolderDetails.targetAssetIndex && this.parentAssetIndex == paperContentRemoveFromFolderDetails.parentAssetIndex)) {
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
