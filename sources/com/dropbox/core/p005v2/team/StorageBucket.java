package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.StorageBucket */
public class StorageBucket {
    protected final String bucket;
    protected final long users;

    /* renamed from: com.dropbox.core.v2.team.StorageBucket$Serializer */
    static class Serializer extends StructSerializer<StorageBucket> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(StorageBucket storageBucket, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("bucket");
            StoneSerializers.string().serialize(storageBucket.bucket, jsonGenerator);
            jsonGenerator.writeFieldName("users");
            StoneSerializers.uInt64().serialize(Long.valueOf(storageBucket.users), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public StorageBucket deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("bucket".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("users".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"bucket\" missing.");
                } else if (l != null) {
                    StorageBucket storageBucket = new StorageBucket(str2, l.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(storageBucket, storageBucket.toStringMultiline());
                    return storageBucket;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"users\" missing.");
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

    public StorageBucket(String str, long j) {
        if (str != null) {
            this.bucket = str;
            this.users = j;
            return;
        }
        throw new IllegalArgumentException("Required value for 'bucket' is null");
    }

    public String getBucket() {
        return this.bucket;
    }

    public long getUsers() {
        return this.users;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.bucket, Long.valueOf(this.users)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        StorageBucket storageBucket = (StorageBucket) obj;
        String str = this.bucket;
        String str2 = storageBucket.bucket;
        if ((str != str2 && !str.equals(str2)) || this.users != storageBucket.users) {
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
