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

/* renamed from: com.dropbox.core.v2.teamlog.RelocateAssetReferencesLogInfo */
public class RelocateAssetReferencesLogInfo {
    protected final long destAssetIndex;
    protected final long srcAssetIndex;

    /* renamed from: com.dropbox.core.v2.teamlog.RelocateAssetReferencesLogInfo$Serializer */
    static class Serializer extends StructSerializer<RelocateAssetReferencesLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelocateAssetReferencesLogInfo relocateAssetReferencesLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("src_asset_index");
            StoneSerializers.uInt64().serialize(Long.valueOf(relocateAssetReferencesLogInfo.srcAssetIndex), jsonGenerator);
            jsonGenerator.writeFieldName("dest_asset_index");
            StoneSerializers.uInt64().serialize(Long.valueOf(relocateAssetReferencesLogInfo.destAssetIndex), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RelocateAssetReferencesLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("src_asset_index".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("dest_asset_index".equals(currentName)) {
                        l2 = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"src_asset_index\" missing.");
                } else if (l2 != null) {
                    RelocateAssetReferencesLogInfo relocateAssetReferencesLogInfo = new RelocateAssetReferencesLogInfo(l.longValue(), l2.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(relocateAssetReferencesLogInfo, relocateAssetReferencesLogInfo.toStringMultiline());
                    return relocateAssetReferencesLogInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"dest_asset_index\" missing.");
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

    public RelocateAssetReferencesLogInfo(long j, long j2) {
        this.srcAssetIndex = j;
        this.destAssetIndex = j2;
    }

    public long getSrcAssetIndex() {
        return this.srcAssetIndex;
    }

    public long getDestAssetIndex() {
        return this.destAssetIndex;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.srcAssetIndex), Long.valueOf(this.destAssetIndex)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RelocateAssetReferencesLogInfo relocateAssetReferencesLogInfo = (RelocateAssetReferencesLogInfo) obj;
        if (!(this.srcAssetIndex == relocateAssetReferencesLogInfo.srcAssetIndex && this.destAssetIndex == relocateAssetReferencesLogInfo.destAssetIndex)) {
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
