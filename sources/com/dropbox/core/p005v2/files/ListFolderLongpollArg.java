package com.dropbox.core.p005v2.files;

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

/* renamed from: com.dropbox.core.v2.files.ListFolderLongpollArg */
class ListFolderLongpollArg {
    protected final String cursor;
    protected final long timeout;

    /* renamed from: com.dropbox.core.v2.files.ListFolderLongpollArg$Serializer */
    static class Serializer extends StructSerializer<ListFolderLongpollArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFolderLongpollArg listFolderLongpollArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("cursor");
            StoneSerializers.string().serialize(listFolderLongpollArg.cursor, jsonGenerator);
            jsonGenerator.writeFieldName("timeout");
            StoneSerializers.uInt64().serialize(Long.valueOf(listFolderLongpollArg.timeout), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFolderLongpollArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(30);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("cursor".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("timeout".equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ListFolderLongpollArg listFolderLongpollArg = new ListFolderLongpollArg(str2, valueOf.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFolderLongpollArg, listFolderLongpollArg.toStringMultiline());
                    return listFolderLongpollArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"cursor\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListFolderLongpollArg(String str, long j) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'cursor' is null");
        } else if (str.length() >= 1) {
            this.cursor = str;
            if (j < 30) {
                throw new IllegalArgumentException("Number 'timeout' is smaller than 30L");
            } else if (j <= 480) {
                this.timeout = j;
            } else {
                throw new IllegalArgumentException("Number 'timeout' is larger than 480L");
            }
        } else {
            throw new IllegalArgumentException("String 'cursor' is shorter than 1");
        }
    }

    public ListFolderLongpollArg(String str) {
        this(str, 30);
    }

    public String getCursor() {
        return this.cursor;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.cursor, Long.valueOf(this.timeout)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListFolderLongpollArg listFolderLongpollArg = (ListFolderLongpollArg) obj;
        String str = this.cursor;
        String str2 = listFolderLongpollArg.cursor;
        if ((str != str2 && !str.equals(str2)) || this.timeout != listFolderLongpollArg.timeout) {
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
