package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.ListFolderLongpollError */
public enum ListFolderLongpollError {
    RESET,
    OTHER;

    /* renamed from: com.dropbox.core.v2.files.ListFolderLongpollError$1 */
    static /* synthetic */ class C06761 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$files$ListFolderLongpollError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$files$ListFolderLongpollError = new int[ListFolderLongpollError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$files$ListFolderLongpollError[ListFolderLongpollError.RESET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.files.ListFolderLongpollError$Serializer */
    static class Serializer extends UnionSerializer<ListFolderLongpollError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListFolderLongpollError listFolderLongpollError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06761.$SwitchMap$com$dropbox$core$v2$files$ListFolderLongpollError[listFolderLongpollError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("reset");
            }
        }

        public ListFolderLongpollError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFolderLongpollError listFolderLongpollError;
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
                if ("reset".equals(str)) {
                    listFolderLongpollError = ListFolderLongpollError.RESET;
                } else {
                    listFolderLongpollError = ListFolderLongpollError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFolderLongpollError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
