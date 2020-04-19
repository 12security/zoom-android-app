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

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersCountResult */
public class ListFileMembersCountResult {
    protected final long memberCount;
    protected final SharedFileMembers members;

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersCountResult$Serializer */
    static class Serializer extends StructSerializer<ListFileMembersCountResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFileMembersCountResult listFileMembersCountResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("members");
            Serializer.INSTANCE.serialize(listFileMembersCountResult.members, jsonGenerator);
            jsonGenerator.writeFieldName("member_count");
            StoneSerializers.uInt32().serialize(Long.valueOf(listFileMembersCountResult.memberCount), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFileMembersCountResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SharedFileMembers sharedFileMembers = null;
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
                    if ("members".equals(currentName)) {
                        sharedFileMembers = (SharedFileMembers) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("member_count".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (sharedFileMembers == null) {
                    throw new JsonParseException(jsonParser, "Required field \"members\" missing.");
                } else if (l != null) {
                    ListFileMembersCountResult listFileMembersCountResult = new ListFileMembersCountResult(sharedFileMembers, l.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFileMembersCountResult, listFileMembersCountResult.toStringMultiline());
                    return listFileMembersCountResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"member_count\" missing.");
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

    public ListFileMembersCountResult(SharedFileMembers sharedFileMembers, long j) {
        if (sharedFileMembers != null) {
            this.members = sharedFileMembers;
            this.memberCount = j;
            return;
        }
        throw new IllegalArgumentException("Required value for 'members' is null");
    }

    public SharedFileMembers getMembers() {
        return this.members;
    }

    public long getMemberCount() {
        return this.memberCount;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.members, Long.valueOf(this.memberCount)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListFileMembersCountResult listFileMembersCountResult = (ListFileMembersCountResult) obj;
        SharedFileMembers sharedFileMembers = this.members;
        SharedFileMembers sharedFileMembers2 = listFileMembersCountResult.members;
        if ((sharedFileMembers != sharedFileMembers2 && !sharedFileMembers.equals(sharedFileMembers2)) || this.memberCount != listFileMembersCountResult.memberCount) {
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
