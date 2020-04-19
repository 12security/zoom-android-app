package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.SyncSettingArg */
public enum SyncSettingArg {
    DEFAULT,
    NOT_SYNCED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.files.SyncSettingArg$Serializer */
    public static class Serializer extends UnionSerializer<SyncSettingArg> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SyncSettingArg syncSettingArg, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (syncSettingArg) {
                case DEFAULT:
                    jsonGenerator.writeString("default");
                    return;
                case NOT_SYNCED:
                    jsonGenerator.writeString("not_synced");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SyncSettingArg deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SyncSettingArg syncSettingArg;
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
                    syncSettingArg = SyncSettingArg.DEFAULT;
                } else if ("not_synced".equals(str)) {
                    syncSettingArg = SyncSettingArg.NOT_SYNCED;
                } else {
                    syncSettingArg = SyncSettingArg.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return syncSettingArg;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
