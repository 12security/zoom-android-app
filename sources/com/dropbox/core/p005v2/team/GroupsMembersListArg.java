package com.dropbox.core.p005v2.team;

import com.box.androidsdk.content.models.BoxGroup;
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

/* renamed from: com.dropbox.core.v2.team.GroupsMembersListArg */
class GroupsMembersListArg {
    protected final GroupSelector group;
    protected final long limit;

    /* renamed from: com.dropbox.core.v2.team.GroupsMembersListArg$Serializer */
    static class Serializer extends StructSerializer<GroupsMembersListArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupsMembersListArg groupsMembersListArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxGroup.TYPE);
            Serializer.INSTANCE.serialize(groupsMembersListArg.group, jsonGenerator);
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(groupsMembersListArg.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupsMembersListArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            GroupSelector groupSelector = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(1000);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxGroup.TYPE.equals(currentName)) {
                        groupSelector = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (groupSelector != null) {
                    GroupsMembersListArg groupsMembersListArg = new GroupsMembersListArg(groupSelector, valueOf.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupsMembersListArg, groupsMembersListArg.toStringMultiline());
                    return groupsMembersListArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"group\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GroupsMembersListArg(GroupSelector groupSelector, long j) {
        if (groupSelector != null) {
            this.group = groupSelector;
            if (j < 1) {
                throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
            } else if (j <= 1000) {
                this.limit = j;
            } else {
                throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'group' is null");
        }
    }

    public GroupsMembersListArg(GroupSelector groupSelector) {
        this(groupSelector, 1000);
    }

    public GroupSelector getGroup() {
        return this.group;
    }

    public long getLimit() {
        return this.limit;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.group, Long.valueOf(this.limit)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        GroupsMembersListArg groupsMembersListArg = (GroupsMembersListArg) obj;
        GroupSelector groupSelector = this.group;
        GroupSelector groupSelector2 = groupsMembersListArg.group;
        if ((groupSelector != groupSelector2 && !groupSelector.equals(groupSelector2)) || this.limit != groupsMembersListArg.limit) {
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
