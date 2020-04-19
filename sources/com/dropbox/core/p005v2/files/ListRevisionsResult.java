package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxList;
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
import java.util.List;

/* renamed from: com.dropbox.core.v2.files.ListRevisionsResult */
public class ListRevisionsResult {
    protected final List<FileMetadata> entries;
    protected final boolean isDeleted;
    protected final Date serverDeleted;

    /* renamed from: com.dropbox.core.v2.files.ListRevisionsResult$Serializer */
    static class Serializer extends StructSerializer<ListRevisionsResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListRevisionsResult listRevisionsResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("is_deleted");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listRevisionsResult.isDeleted), jsonGenerator);
            jsonGenerator.writeFieldName(BoxList.FIELD_ENTRIES);
            StoneSerializers.list(Serializer.INSTANCE).serialize(listRevisionsResult.entries, jsonGenerator);
            if (listRevisionsResult.serverDeleted != null) {
                jsonGenerator.writeFieldName("server_deleted");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(listRevisionsResult.serverDeleted, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListRevisionsResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("is_deleted".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if (BoxList.FIELD_ENTRIES.equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("server_deleted".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"is_deleted\" missing.");
                } else if (list != null) {
                    ListRevisionsResult listRevisionsResult = new ListRevisionsResult(bool.booleanValue(), list, date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listRevisionsResult, listRevisionsResult.toStringMultiline());
                    return listRevisionsResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"entries\" missing.");
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

    public ListRevisionsResult(boolean z, List<FileMetadata> list, Date date) {
        this.isDeleted = z;
        this.serverDeleted = LangUtil.truncateMillis(date);
        if (list != null) {
            for (FileMetadata fileMetadata : list) {
                if (fileMetadata == null) {
                    throw new IllegalArgumentException("An item in list 'entries' is null");
                }
            }
            this.entries = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'entries' is null");
    }

    public ListRevisionsResult(boolean z, List<FileMetadata> list) {
        this(z, list, null);
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public List<FileMetadata> getEntries() {
        return this.entries;
    }

    public Date getServerDeleted() {
        return this.serverDeleted;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Boolean.valueOf(this.isDeleted), this.serverDeleted, this.entries});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
        if (r2.equals(r5) == false) goto L_0x0039;
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
            if (r2 == 0) goto L_0x003b
            com.dropbox.core.v2.files.ListRevisionsResult r5 = (com.dropbox.core.p005v2.files.ListRevisionsResult) r5
            boolean r2 = r4.isDeleted
            boolean r3 = r5.isDeleted
            if (r2 != r3) goto L_0x0039
            java.util.List<com.dropbox.core.v2.files.FileMetadata> r2 = r4.entries
            java.util.List<com.dropbox.core.v2.files.FileMetadata> r3 = r5.entries
            if (r2 == r3) goto L_0x002a
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0039
        L_0x002a:
            java.util.Date r2 = r4.serverDeleted
            java.util.Date r5 = r5.serverDeleted
            if (r2 == r5) goto L_0x003a
            if (r2 == 0) goto L_0x0039
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r0 = 0
        L_0x003a:
            return r0
        L_0x003b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.ListRevisionsResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
