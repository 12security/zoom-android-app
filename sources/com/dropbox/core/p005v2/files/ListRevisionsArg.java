package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxList;
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

/* renamed from: com.dropbox.core.v2.files.ListRevisionsArg */
class ListRevisionsArg {
    protected final long limit;
    protected final ListRevisionsMode mode;
    protected final String path;

    /* renamed from: com.dropbox.core.v2.files.ListRevisionsArg$Builder */
    public static class Builder {
        protected long limit;
        protected ListRevisionsMode mode;
        protected final String path;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'path' is null");
            } else if (Pattern.matches("/(.|[\\r\\n])*|id:.*|(ns:[0-9]+(/.*)?)", str)) {
                this.path = str;
                this.mode = ListRevisionsMode.PATH;
                this.limit = 10;
            } else {
                throw new IllegalArgumentException("String 'path' does not match pattern");
            }
        }

        public Builder withMode(ListRevisionsMode listRevisionsMode) {
            if (listRevisionsMode != null) {
                this.mode = listRevisionsMode;
            } else {
                this.mode = ListRevisionsMode.PATH;
            }
            return this;
        }

        public Builder withLimit(Long l) {
            if (l.longValue() < 1) {
                throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
            } else if (l.longValue() <= 100) {
                if (l != null) {
                    this.limit = l.longValue();
                } else {
                    this.limit = 10;
                }
                return this;
            } else {
                throw new IllegalArgumentException("Number 'limit' is larger than 100L");
            }
        }

        public ListRevisionsArg build() {
            return new ListRevisionsArg(this.path, this.mode, this.limit);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.ListRevisionsArg$Serializer */
    static class Serializer extends StructSerializer<ListRevisionsArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListRevisionsArg listRevisionsArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(listRevisionsArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("mode");
            Serializer.INSTANCE.serialize(listRevisionsArg.mode, jsonGenerator);
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt64().serialize(Long.valueOf(listRevisionsArg.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListRevisionsArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                ListRevisionsMode listRevisionsMode = ListRevisionsMode.PATH;
                Long valueOf = Long.valueOf(10);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("mode".equals(currentName)) {
                        listRevisionsMode = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ListRevisionsArg listRevisionsArg = new ListRevisionsArg(str2, listRevisionsMode, valueOf.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listRevisionsArg, listRevisionsArg.toStringMultiline());
                    return listRevisionsArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListRevisionsArg(String str, ListRevisionsMode listRevisionsMode, long j) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("/(.|[\\r\\n])*|id:.*|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            if (listRevisionsMode != null) {
                this.mode = listRevisionsMode;
                if (j < 1) {
                    throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
                } else if (j <= 100) {
                    this.limit = j;
                } else {
                    throw new IllegalArgumentException("Number 'limit' is larger than 100L");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'mode' is null");
            }
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public ListRevisionsArg(String str) {
        this(str, ListRevisionsMode.PATH, 10);
    }

    public String getPath() {
        return this.path;
    }

    public ListRevisionsMode getMode() {
        return this.mode;
    }

    public long getLimit() {
        return this.limit;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, this.mode, Long.valueOf(this.limit)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r3) == false) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0036, code lost:
        if (r6.limit != r7.limit) goto L_0x0039;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003b
            com.dropbox.core.v2.files.ListRevisionsArg r7 = (com.dropbox.core.p005v2.files.ListRevisionsArg) r7
            java.lang.String r2 = r6.path
            java.lang.String r3 = r7.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0039
        L_0x0024:
            com.dropbox.core.v2.files.ListRevisionsMode r2 = r6.mode
            com.dropbox.core.v2.files.ListRevisionsMode r3 = r7.mode
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0039
        L_0x0030:
            long r2 = r6.limit
            long r4 = r7.limit
            int r7 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r7 != 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r0 = 0
        L_0x003a:
            return r0
        L_0x003b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.ListRevisionsArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
