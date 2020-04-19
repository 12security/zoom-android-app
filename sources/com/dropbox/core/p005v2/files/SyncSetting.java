package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.SyncSetting */
public enum SyncSetting {
    DEFAULT,
    NOT_SYNCED,
    NOT_SYNCED_INACTIVE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.files.SyncSetting$Serializer */
    public static class Serializer extends UnionSerializer<SyncSetting> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SyncSetting syncSetting, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (syncSetting) {
                case DEFAULT:
                    jsonGenerator.writeString("default");
                    return;
                case NOT_SYNCED:
                    jsonGenerator.writeString("not_synced");
                    return;
                case NOT_SYNCED_INACTIVE:
                    jsonGenerator.writeString("not_synced_inactive");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SyncSetting deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SyncSetting syncSetting;
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
                if ("default".equals(str)) {
                    syncSetting = SyncSetting.DEFAULT;
                } else if ("not_synced".equals(str)) {
                    syncSetting = SyncSetting.NOT_SYNCED;
                } else if ("not_synced_inactive".equals(str)) {
                    syncSetting = SyncSetting.NOT_SYNCED_INACTIVE;
                } else {
                    syncSetting = SyncSetting.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return syncSetting;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
