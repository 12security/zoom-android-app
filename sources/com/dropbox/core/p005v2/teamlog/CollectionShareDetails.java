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

/* renamed from: com.dropbox.core.v2.teamlog.CollectionShareDetails */
public class CollectionShareDetails {
    protected final String albumName;

    /* renamed from: com.dropbox.core.v2.teamlog.CollectionShareDetails$Serializer */
    static class Serializer extends StructSerializer<CollectionShareDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CollectionShareDetails collectionShareDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("album_name");
            StoneSerializers.string().serialize(collectionShareDetails.albumName, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CollectionShareDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("album_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    CollectionShareDetails collectionShareDetails = new CollectionShareDetails(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(collectionShareDetails, collectionShareDetails.toStringMultiline());
                    return collectionShareDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"album_name\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public CollectionShareDetails(String str) {
        if (str != null) {
            this.albumName = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'albumName' is null");
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.albumName});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        CollectionShareDetails collectionShareDetails = (CollectionShareDetails) obj;
        String str = this.albumName;
        String str2 = collectionShareDetails.albumName;
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
