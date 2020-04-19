package com.dropbox.core.p005v2.sharing;

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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersBatchArg */
class ListFileMembersBatchArg {
    protected final List<String> files;
    protected final long limit;

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersBatchArg$Serializer */
    static class Serializer extends StructSerializer<ListFileMembersBatchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFileMembersBatchArg listFileMembersBatchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("files");
            StoneSerializers.list(StoneSerializers.string()).serialize(listFileMembersBatchArg.files, jsonGenerator);
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(listFileMembersBatchArg.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFileMembersBatchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(10);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("files".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    ListFileMembersBatchArg listFileMembersBatchArg = new ListFileMembersBatchArg(list, valueOf.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFileMembersBatchArg, listFileMembersBatchArg.toStringMultiline());
                    return listFileMembersBatchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"files\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListFileMembersBatchArg(List<String> list, long j) {
        if (list == null) {
            throw new IllegalArgumentException("Required value for 'files' is null");
        } else if (list.size() <= 100) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'files' is null");
                } else if (str.length() < 1) {
                    throw new IllegalArgumentException("Stringan item in list 'files' is shorter than 1");
                } else if (!Pattern.matches("((/|id:).*|nspath:[0-9]+:.*)|ns:[0-9]+(/.*)?", str)) {
                    throw new IllegalArgumentException("Stringan item in list 'files' does not match pattern");
                }
            }
            this.files = list;
            if (j <= 20) {
                this.limit = j;
                return;
            }
            throw new IllegalArgumentException("Number 'limit' is larger than 20L");
        } else {
            throw new IllegalArgumentException("List 'files' has more than 100 items");
        }
    }

    public ListFileMembersBatchArg(List<String> list) {
        this(list, 10);
    }

    public List<String> getFiles() {
        return this.files;
    }

    public long getLimit() {
        return this.limit;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.files, Long.valueOf(this.limit)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListFileMembersBatchArg listFileMembersBatchArg = (ListFileMembersBatchArg) obj;
        List<String> list = this.files;
        List<String> list2 = listFileMembersBatchArg.files;
        if ((list != list2 && !list.equals(list2)) || this.limit != listFileMembersBatchArg.limit) {
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
