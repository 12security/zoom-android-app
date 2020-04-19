package com.dropbox.core;

import com.dropbox.core.stone.StoneSerializer;
import com.dropbox.core.stone.StoneSerializers;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.util.ZMActionMsgUtil;
import java.io.IOException;

public final class LocalizedText {
    static final StoneSerializer<LocalizedText> STONE_SERIALIZER = new StoneSerializer<LocalizedText>() {
        public void serialize(LocalizedText localizedText, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            throw new UnsupportedOperationException("Error wrapper serialization not supported.");
        }

        public LocalizedText deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            expectStartObject(jsonParser);
            String str = null;
            String str2 = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                if (ZMActionMsgUtil.KEY_EVENT.equals(currentName)) {
                    str = (String) StoneSerializers.string().deserialize(jsonParser);
                } else if (OAuth.LOCALE.equals(currentName)) {
                    str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                } else {
                    skipValue(jsonParser);
                }
            }
            if (str == null) {
                throw new JsonParseException(jsonParser, "Required field \"text\" missing.");
            } else if (str2 != null) {
                LocalizedText localizedText = new LocalizedText(str, str2);
                expectEndObject(jsonParser);
                return localizedText;
            } else {
                throw new JsonParseException(jsonParser, "Required field \"locale\" missing.");
            }
        }
    };
    private final String locale;
    private final String text;

    public LocalizedText(String str, String str2) {
        if (str == null) {
            throw new NullPointerException(ZMActionMsgUtil.KEY_EVENT);
        } else if (str2 != null) {
            this.text = str;
            this.locale = str2;
        } else {
            throw new NullPointerException(OAuth.LOCALE);
        }
    }

    public String getText() {
        return this.text;
    }

    public String getLocale() {
        return this.locale;
    }

    public String toString() {
        return this.text;
    }
}
