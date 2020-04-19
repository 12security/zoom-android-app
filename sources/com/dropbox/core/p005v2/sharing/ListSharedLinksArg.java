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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.ListSharedLinksArg */
class ListSharedLinksArg {
    protected final String cursor;
    protected final Boolean directOnly;
    protected final String path;

    /* renamed from: com.dropbox.core.v2.sharing.ListSharedLinksArg$Builder */
    public static class Builder {
        protected String cursor = null;
        protected Boolean directOnly = null;
        protected String path = null;

        protected Builder() {
        }

        public Builder withPath(String str) {
            if (str == null || Pattern.matches("(/(.|[\\r\\n])*|id:.*)|(rev:[0-9a-f]{9,})|(ns:[0-9]+(/.*)?)", str)) {
                this.path = str;
                return this;
            }
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }

        public Builder withCursor(String str) {
            this.cursor = str;
            return this;
        }

        public Builder withDirectOnly(Boolean bool) {
            this.directOnly = bool;
            return this;
        }

        public ListSharedLinksArg build() {
            return new ListSharedLinksArg(this.path, this.cursor, this.directOnly);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListSharedLinksArg$Serializer */
    static class Serializer extends StructSerializer<ListSharedLinksArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListSharedLinksArg listSharedLinksArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (listSharedLinksArg.path != null) {
                jsonGenerator.writeFieldName("path");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(listSharedLinksArg.path, jsonGenerator);
            }
            if (listSharedLinksArg.cursor != null) {
                jsonGenerator.writeFieldName("cursor");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(listSharedLinksArg.cursor, jsonGenerator);
            }
            if (listSharedLinksArg.directOnly != null) {
                jsonGenerator.writeFieldName("direct_only");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(listSharedLinksArg.directOnly, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListSharedLinksArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                Boolean bool = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("cursor".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("direct_only".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                ListSharedLinksArg listSharedLinksArg = new ListSharedLinksArg(str2, str3, bool);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(listSharedLinksArg, listSharedLinksArg.toStringMultiline());
                return listSharedLinksArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListSharedLinksArg(String str, String str2, Boolean bool) {
        if (str == null || Pattern.matches("(/(.|[\\r\\n])*|id:.*)|(rev:[0-9a-f]{9,})|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            this.cursor = str2;
            this.directOnly = bool;
            return;
        }
        throw new IllegalArgumentException("String 'path' does not match pattern");
    }

    public ListSharedLinksArg() {
        this(null, null, null);
    }

    public String getPath() {
        return this.path;
    }

    public String getCursor() {
        return this.cursor;
    }

    public Boolean getDirectOnly() {
        return this.directOnly;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, this.cursor, this.directOnly});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r2.equals(r5) == false) goto L_0x0043;
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
            if (r2 == 0) goto L_0x0045
            com.dropbox.core.v2.sharing.ListSharedLinksArg r5 = (com.dropbox.core.p005v2.sharing.ListSharedLinksArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0026:
            java.lang.String r2 = r4.cursor
            java.lang.String r3 = r5.cursor
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0034:
            java.lang.Boolean r2 = r4.directOnly
            java.lang.Boolean r5 = r5.directOnly
            if (r2 == r5) goto L_0x0044
            if (r2 == 0) goto L_0x0043
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.ListSharedLinksArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
