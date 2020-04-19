package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateStatus */
public enum ExcludedUsersUpdateStatus {
    SUCCESS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateStatus$1 */
    static /* synthetic */ class C08051 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$ExcludedUsersUpdateStatus = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$ExcludedUsersUpdateStatus = new int[ExcludedUsersUpdateStatus.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$ExcludedUsersUpdateStatus[ExcludedUsersUpdateStatus.SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateStatus$Serializer */
    static class Serializer extends UnionSerializer<ExcludedUsersUpdateStatus> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ExcludedUsersUpdateStatus excludedUsersUpdateStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08051.$SwitchMap$com$dropbox$core$v2$team$ExcludedUsersUpdateStatus[excludedUsersUpdateStatus.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString(Param.SUCCESS);
            }
        }

        public ExcludedUsersUpdateStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ExcludedUsersUpdateStatus excludedUsersUpdateStatus;
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
                if (Param.SUCCESS.equals(str)) {
                    excludedUsersUpdateStatus = ExcludedUsersUpdateStatus.SUCCESS;
                } else {
                    excludedUsersUpdateStatus = ExcludedUsersUpdateStatus.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return excludedUsersUpdateStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
