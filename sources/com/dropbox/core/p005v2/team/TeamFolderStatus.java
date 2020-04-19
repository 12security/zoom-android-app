package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.TeamFolderStatus */
public enum TeamFolderStatus {
    ACTIVE,
    ARCHIVED,
    ARCHIVE_IN_PROGRESS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderStatus$Serializer */
    public static class Serializer extends UnionSerializer<TeamFolderStatus> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TeamFolderStatus teamFolderStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderStatus) {
                case ACTIVE:
                    jsonGenerator.writeString(ConditionalUserProperty.ACTIVE);
                    return;
                case ARCHIVED:
                    jsonGenerator.writeString("archived");
                    return;
                case ARCHIVE_IN_PROGRESS:
                    jsonGenerator.writeString("archive_in_progress");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TeamFolderStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderStatus teamFolderStatus;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if (ConditionalUserProperty.ACTIVE.equals(str)) {
                    teamFolderStatus = TeamFolderStatus.ACTIVE;
                } else if ("archived".equals(str)) {
                    teamFolderStatus = TeamFolderStatus.ARCHIVED;
                } else if ("archive_in_progress".equals(str)) {
                    teamFolderStatus = TeamFolderStatus.ARCHIVE_IN_PROGRESS;
                } else {
                    teamFolderStatus = TeamFolderStatus.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return teamFolderStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
