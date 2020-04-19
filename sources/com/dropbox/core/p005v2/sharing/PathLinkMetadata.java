package com.dropbox.core.p005v2.sharing;

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
import java.util.Date;
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.sharing.PathLinkMetadata */
public class PathLinkMetadata extends LinkMetadata {
    protected final String path;

    /* renamed from: com.dropbox.core.v2.sharing.PathLinkMetadata$Serializer */
    static class Serializer extends StructSerializer<PathLinkMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PathLinkMetadata pathLinkMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("path", jsonGenerator);
            jsonGenerator.writeFieldName("url");
            StoneSerializers.string().serialize(pathLinkMetadata.url, jsonGenerator);
            jsonGenerator.writeFieldName("visibility");
            Serializer.INSTANCE.serialize(pathLinkMetadata.visibility, jsonGenerator);
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(pathLinkMetadata.path, jsonGenerator);
            if (pathLinkMetadata.expires != null) {
                jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(pathLinkMetadata.expires, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PathLinkMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("path".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                Visibility visibility = null;
                String str3 = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("url".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("visibility".equals(currentName)) {
                        visibility = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("path".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (ClientCookie.EXPIRES_ATTR.equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"url\" missing.");
                } else if (visibility == null) {
                    throw new JsonParseException(jsonParser, "Required field \"visibility\" missing.");
                } else if (str3 != null) {
                    PathLinkMetadata pathLinkMetadata = new PathLinkMetadata(str2, visibility, str3, date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(pathLinkMetadata, pathLinkMetadata.toStringMultiline());
                    return pathLinkMetadata;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
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

    public PathLinkMetadata(String str, Visibility visibility, String str2, Date date) {
        super(str, visibility, date);
        if (str2 != null) {
            this.path = str2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'path' is null");
    }

    public PathLinkMetadata(String str, Visibility visibility, String str2) {
        this(str, visibility, str2, null);
    }

    public String getUrl() {
        return this.url;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public String getPath() {
        return this.path;
    }

    public Date getExpires() {
        return this.expires;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.path});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0042, code lost:
        if (r2.equals(r3) == false) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0056, code lost:
        if (r4.expires.equals(r5.expires) == false) goto L_0x0059;
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
            if (r2 == 0) goto L_0x005b
            com.dropbox.core.v2.sharing.PathLinkMetadata r5 = (com.dropbox.core.p005v2.sharing.PathLinkMetadata) r5
            java.lang.String r2 = r4.url
            java.lang.String r3 = r5.url
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.url
            java.lang.String r3 = r5.url
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0059
        L_0x0028:
            com.dropbox.core.v2.sharing.Visibility r2 = r4.visibility
            com.dropbox.core.v2.sharing.Visibility r3 = r5.visibility
            if (r2 == r3) goto L_0x0038
            com.dropbox.core.v2.sharing.Visibility r2 = r4.visibility
            com.dropbox.core.v2.sharing.Visibility r3 = r5.visibility
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0059
        L_0x0038:
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0044
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0059
        L_0x0044:
            java.util.Date r2 = r4.expires
            java.util.Date r3 = r5.expires
            if (r2 == r3) goto L_0x005a
            java.util.Date r2 = r4.expires
            if (r2 == 0) goto L_0x0059
            java.util.Date r2 = r4.expires
            java.util.Date r5 = r5.expires
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0059
            goto L_0x005a
        L_0x0059:
            r0 = 0
        L_0x005a:
            return r0
        L_0x005b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.PathLinkMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
