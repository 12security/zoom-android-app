package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxCollection;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.sharing.LinkMetadata */
public class LinkMetadata {
    protected final Date expires;
    protected final String url;
    protected final Visibility visibility;

    /* renamed from: com.dropbox.core.v2.sharing.LinkMetadata$Serializer */
    static class Serializer extends StructSerializer<LinkMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(LinkMetadata linkMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (linkMetadata instanceof PathLinkMetadata) {
                Serializer.INSTANCE.serialize((PathLinkMetadata) linkMetadata, jsonGenerator, z);
            } else if (linkMetadata instanceof CollectionLinkMetadata) {
                Serializer.INSTANCE.serialize((CollectionLinkMetadata) linkMetadata, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                jsonGenerator.writeFieldName("url");
                StoneSerializers.string().serialize(linkMetadata.url, jsonGenerator);
                jsonGenerator.writeFieldName("visibility");
                Serializer.INSTANCE.serialize(linkMetadata.visibility, jsonGenerator);
                if (linkMetadata.expires != null) {
                    jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
                    StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(linkMetadata.expires, jsonGenerator);
                }
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public LinkMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            LinkMetadata linkMetadata;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("".equals(str)) {
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
                    linkMetadata = new LinkMetadata(str2, visibility, date);
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"visibility\" missing.");
                }
            } else if ("".equals(str)) {
                linkMetadata = INSTANCE.deserialize(jsonParser, true);
            } else if ("path".equals(str)) {
                linkMetadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if (BoxCollection.TYPE.equals(str)) {
                linkMetadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
            if (!z) {
                expectEndObject(jsonParser);
            }
            StoneDeserializerLogger.log(linkMetadata, linkMetadata.toStringMultiline());
            return linkMetadata;
        }
    }

    public LinkMetadata(String str, Visibility visibility2, Date date) {
        if (str != null) {
            this.url = str;
            if (visibility2 != null) {
                this.visibility = visibility2;
                this.expires = LangUtil.truncateMillis(date);
                return;
            }
            throw new IllegalArgumentException("Required value for 'visibility' is null");
        }
        throw new IllegalArgumentException("Required value for 'url' is null");
    }

    public LinkMetadata(String str, Visibility visibility2) {
        this(str, visibility2, null);
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
        return Arrays.hashCode(new Object[]{this.url, this.visibility, this.expires});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (r2.equals(r5) == false) goto L_0x003f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
            com.dropbox.core.v2.sharing.LinkMetadata r5 = (com.dropbox.core.p005v2.sharing.LinkMetadata) r5
            java.lang.String r2 = r4.url
            java.lang.String r3 = r5.url
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0024:
            com.dropbox.core.v2.sharing.Visibility r2 = r4.visibility
            com.dropbox.core.v2.sharing.Visibility r3 = r5.visibility
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0030:
            java.util.Date r2 = r4.expires
            java.util.Date r5 = r5.expires
            if (r2 == r5) goto L_0x0040
            if (r2 == 0) goto L_0x003f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r0 = 0
        L_0x0040:
            return r0
        L_0x0041:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.LinkMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
