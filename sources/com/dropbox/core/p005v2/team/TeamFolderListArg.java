package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.TeamFolderListArg */
class TeamFolderListArg {
    protected final long limit;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderListArg$Serializer */
    static class Serializer extends StructSerializer<TeamFolderListArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderListArg teamFolderListArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(teamFolderListArg.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderListArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
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
                    if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                TeamFolderListArg teamFolderListArg = new TeamFolderListArg(valueOf.longValue());
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(teamFolderListArg, teamFolderListArg.toStringMultiline());
                return teamFolderListArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamFolderListArg(long j) {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            this.limit = j;
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    public TeamFolderListArg() {
        this(1000);
    }

    public long getLimit() {
        return this.limit;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.limit)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        if (this.limit != ((TeamFolderListArg) obj).limit) {
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
