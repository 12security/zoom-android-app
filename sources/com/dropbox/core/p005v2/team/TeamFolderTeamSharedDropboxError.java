package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.TeamFolderTeamSharedDropboxError */
public enum TeamFolderTeamSharedDropboxError {
    DISALLOWED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderTeamSharedDropboxError$1 */
    static /* synthetic */ class C08691 {

        /* renamed from: $SwitchMap$com$dropbox$core$v2$team$TeamFolderTeamSharedDropboxError */
        static final /* synthetic */ int[] f144x1a89d12 = null;

        static {
            f144x1a89d12 = new int[TeamFolderTeamSharedDropboxError.values().length];
            try {
                f144x1a89d12[TeamFolderTeamSharedDropboxError.DISALLOWED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderTeamSharedDropboxError$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderTeamSharedDropboxError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08691.f144x1a89d12[teamFolderTeamSharedDropboxError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("disallowed");
            }
        }

        public TeamFolderTeamSharedDropboxError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError;
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
                if ("disallowed".equals(str)) {
                    teamFolderTeamSharedDropboxError = TeamFolderTeamSharedDropboxError.DISALLOWED;
                } else {
                    teamFolderTeamSharedDropboxError = TeamFolderTeamSharedDropboxError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return teamFolderTeamSharedDropboxError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
