package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.ListFoldersContinueError */
public enum ListFoldersContinueError {
    INVALID_CURSOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.ListFoldersContinueError$1 */
    static /* synthetic */ class C07591 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$sharing$ListFoldersContinueError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$sharing$ListFoldersContinueError = new int[ListFoldersContinueError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$sharing$ListFoldersContinueError[ListFoldersContinueError.INVALID_CURSOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFoldersContinueError$Serializer */
    static class Serializer extends UnionSerializer<ListFoldersContinueError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListFoldersContinueError listFoldersContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C07591.$SwitchMap$com$dropbox$core$v2$sharing$ListFoldersContinueError[listFoldersContinueError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("invalid_cursor");
            }
        }

        public ListFoldersContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFoldersContinueError listFoldersContinueError;
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
                if ("invalid_cursor".equals(str)) {
                    listFoldersContinueError = ListFoldersContinueError.INVALID_CURSOR;
                } else {
                    listFoldersContinueError = ListFoldersContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFoldersContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
