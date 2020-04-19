package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxCollection;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Date;
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.sharing.CollectionLinkMetadata */
public class CollectionLinkMetadata extends LinkMetadata {

    /* renamed from: com.dropbox.core.v2.sharing.CollectionLinkMetadata$Serializer */
    static class Serializer extends StructSerializer<CollectionLinkMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CollectionLinkMetadata collectionLinkMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag(BoxCollection.TYPE, jsonGenerator);
            jsonGenerator.writeFieldName("url");
            StoneSerializers.string().serialize(collectionLinkMetadata.url, jsonGenerator);
            jsonGenerator.writeFieldName("visibility");
            Serializer.INSTANCE.serialize(collectionLinkMetadata.visibility, jsonGenerator);
            if (collectionLinkMetadata.expires != null) {
                jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(collectionLinkMetadata.expires, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CollectionLinkMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if (BoxCollection.TYPE.equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                Visibility visibility = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("url".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("visibility".equals(currentName)) {
                        visibility = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (ClientCookie.EXPIRES_ATTR.equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"url\" missing.");
                } else if (visibility != null) {
                    CollectionLinkMetadata collectionLinkMetadata = new CollectionLinkMetadata(str2, visibility, date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(collectionLinkMetadata, collectionLinkMetadata.toStringMultiline());
                    return collectionLinkMetadata;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"visibility\" missing.");
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

    public CollectionLinkMetadata(String str, Visibility visibility, Date date) {
        super(str, visibility, date);
    }

    public CollectionLinkMetadata(String str, Visibility visibility) {
        this(str, visibility, null);
    }

    public String getUrl() {
        return this.url;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public Date getExpires() {
        return this.expires;
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
        CollectionLinkMetadata collectionLinkMetadata = (CollectionLinkMetadata) obj;
        if ((this.url != collectionLinkMetadata.url && !this.url.equals(collectionLinkMetadata.url)) || ((this.visibility != collectionLinkMetadata.visibility && !this.visibility.equals(collectionLinkMetadata.visibility)) || (this.expires != collectionLinkMetadata.expires && (this.expires == null || !this.expires.equals(collectionLinkMetadata.expires))))) {
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
