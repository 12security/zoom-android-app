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
import java.util.List;

/* renamed from: com.dropbox.core.v2.files.ListFolderResult */
public class ListFolderResult {
    protected final String cursor;
    protected final List<Metadata> entries;
    protected final boolean hasMore;

    /* renamed from: com.dropbox.core.v2.files.ListFolderResult$Serializer */
    static class Serializer extends StructSerializer<ListFolderResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFolderResult listFolderResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_ENTRIES);
            StoneSerializers.list(Serializer.INSTANCE).serialize(listFolderResult.entries, jsonGenerator);
            jsonGenerator.writeFieldName("cursor");
            StoneSerializers.string().serialize(listFolderResult.cursor, jsonGenerator);
            jsonGenerator.writeFieldName("has_more");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listFolderResult.hasMore), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFolderResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                Boolean bool = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxList.FIELD_ENTRIES.equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("cursor".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("has_more".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"entries\" missing.");
                } else if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"cursor\" missing.");
                } else if (bool != null) {
                    ListFolderResult listFolderResult = new ListFolderResult(list, str2, bool.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFolderResult, listFolderResult.toStringMultiline());
                    return listFolderResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"has_more\" missing.");
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

    public ListFolderResult(List<Metadata> list, String str, boolean z) {
        if (list != null) {
            for (Metadata metadata : list) {
                if (metadata == null) {
                    throw new IllegalArgumentException("An item in list 'entries' is null");
                }
            }
            this.entries = list;
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'cursor' is null");
            } else if (str.length() >= 1) {
                this.cursor = str;
                this.hasMore = z;
            } else {
                throw new IllegalArgumentException("String 'cursor' is shorter than 1");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'entries' is null");
        }
    }

    public List<Metadata> getEntries() {
        return this.entries;
    }

    public String getCursor() {
        return this.cursor;
    }

    public boolean getHasMore() {
        return this.hasMore;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.entries, this.cursor, Boolean.valueOf(this.hasMore)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r3) == false) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        if (r4.hasMore != r5.hasMore) goto L_0x0037;
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
            if (r2 == 0) goto L_0x0039
            com.dropbox.core.v2.files.ListFolderResult r5 = (com.dropbox.core.p005v2.files.ListFolderResult) r5
            java.util.List<com.dropbox.core.v2.files.Metadata> r2 = r4.entries
            java.util.List<com.dropbox.core.v2.files.Metadata> r3 = r5.entries
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0024:
            java.lang.String r2 = r4.cursor
            java.lang.String r3 = r5.cursor
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0030:
            boolean r2 = r4.hasMore
            boolean r5 = r5.hasMore
            if (r2 != r5) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r0 = 0
        L_0x0038:
            return r0
        L_0x0039:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.ListFolderResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
