package com.dropbox.core.p005v2.paper;

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

/* renamed from: com.dropbox.core.v2.paper.ListUsersOnFolderArgs */
class ListUsersOnFolderArgs extends RefPaperDoc {
    protected final int limit;

    /* renamed from: com.dropbox.core.v2.paper.ListUsersOnFolderArgs$Serializer */
    static class Serializer extends StructSerializer<ListUsersOnFolderArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListUsersOnFolderArgs listUsersOnFolderArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("doc_id");
            StoneSerializers.string().serialize(listUsersOnFolderArgs.docId, jsonGenerator);
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.int32().serialize(Integer.valueOf(listUsersOnFolderArgs.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListUsersOnFolderArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Integer valueOf = Integer.valueOf(1000);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("doc_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Integer) StoneSerializers.int32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ListUsersOnFolderArgs listUsersOnFolderArgs = new ListUsersOnFolderArgs(str2, valueOf.intValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listUsersOnFolderArgs, listUsersOnFolderArgs.toStringMultiline());
                    return listUsersOnFolderArgs;
                }
                throw new JsonParseException(jsonParser, "Required field \"doc_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListUsersOnFolderArgs(String str, int i) {
        super(str);
        if (i < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1");
        } else if (i <= 1000) {
            this.limit = i;
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000");
        }
    }

    public ListUsersOnFolderArgs(String str) {
        this(str, 1000);
    }

    public String getDocId() {
        return this.docId;
    }

    public int getLimit() {
        return this.limit;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{Integer.valueOf(this.limit)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListUsersOnFolderArgs listUsersOnFolderArgs = (ListUsersOnFolderArgs) obj;
        if ((this.docId != listUsersOnFolderArgs.docId && !this.docId.equals(listUsersOnFolderArgs.docId)) || this.limit != listUsersOnFolderArgs.limit) {
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
