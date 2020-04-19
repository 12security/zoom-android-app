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

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateResult */
public class ExcludedUsersUpdateResult {
    protected final ExcludedUsersUpdateStatus status;

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateResult$Serializer */
    static class Serializer extends StructSerializer<ExcludedUsersUpdateResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ExcludedUsersUpdateResult excludedUsersUpdateResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("status");
            Serializer.INSTANCE.serialize(excludedUsersUpdateResult.status, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ExcludedUsersUpdateResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            ExcludedUsersUpdateStatus excludedUsersUpdateStatus = null;
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
                    if ("status".equals(currentName)) {
                        excludedUsersUpdateStatus = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (excludedUsersUpdateStatus != null) {
                    ExcludedUsersUpdateResult excludedUsersUpdateResult = new ExcludedUsersUpdateResult(excludedUsersUpdateStatus);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(excludedUsersUpdateResult, excludedUsersUpdateResult.toStringMultiline());
                    return excludedUsersUpdateResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"status\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ExcludedUsersUpdateResult(ExcludedUsersUpdateStatus excludedUsersUpdateStatus) {
        if (excludedUsersUpdateStatus != null) {
            this.status = excludedUsersUpdateStatus;
            return;
        }
        throw new IllegalArgumentException("Required value for 'status' is null");
    }

    public ExcludedUsersUpdateStatus getStatus() {
        return this.status;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.status});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ExcludedUsersUpdateResult excludedUsersUpdateResult = (ExcludedUsersUpdateResult) obj;
        ExcludedUsersUpdateStatus excludedUsersUpdateStatus = this.status;
        ExcludedUsersUpdateStatus excludedUsersUpdateStatus2 = excludedUsersUpdateResult.status;
        if (excludedUsersUpdateStatus != excludedUsersUpdateStatus2 && !excludedUsersUpdateStatus.equals(excludedUsersUpdateStatus2)) {
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
