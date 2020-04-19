package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TokenGetAuthenticatedAdminResult */
public class TokenGetAuthenticatedAdminResult {
    protected final TeamMemberProfile adminProfile;

    /* renamed from: com.dropbox.core.v2.team.TokenGetAuthenticatedAdminResult$Serializer */
    static class Serializer extends StructSerializer<TokenGetAuthenticatedAdminResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TokenGetAuthenticatedAdminResult tokenGetAuthenticatedAdminResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("admin_profile");
            Serializer.INSTANCE.serialize(tokenGetAuthenticatedAdminResult.adminProfile, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TokenGetAuthenticatedAdminResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TeamMemberProfile teamMemberProfile = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("admin_profile".equals(currentName)) {
                        teamMemberProfile = (TeamMemberProfile) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (teamMemberProfile != null) {
                    TokenGetAuthenticatedAdminResult tokenGetAuthenticatedAdminResult = new TokenGetAuthenticatedAdminResult(teamMemberProfile);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(tokenGetAuthenticatedAdminResult, tokenGetAuthenticatedAdminResult.toStringMultiline());
                    return tokenGetAuthenticatedAdminResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"admin_profile\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TokenGetAuthenticatedAdminResult(TeamMemberProfile teamMemberProfile) {
        if (teamMemberProfile != null) {
            this.adminProfile = teamMemberProfile;
            return;
        }
        throw new IllegalArgumentException("Required value for 'adminProfile' is null");
    }

    public TeamMemberProfile getAdminProfile() {
        return this.adminProfile;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.adminProfile});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        TokenGetAuthenticatedAdminResult tokenGetAuthenticatedAdminResult = (TokenGetAuthenticatedAdminResult) obj;
        TeamMemberProfile teamMemberProfile = this.adminProfile;
        TeamMemberProfile teamMemberProfile2 = tokenGetAuthenticatedAdminResult.adminProfile;
        if (teamMemberProfile != teamMemberProfile2 && !teamMemberProfile.equals(teamMemberProfile2)) {
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
