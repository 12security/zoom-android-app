package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.dropbox.core.v2.filerequests.ListFileRequestsResult */
public class ListFileRequestsResult {
    protected final List<FileRequest> fileRequests;

    /* renamed from: com.dropbox.core.v2.filerequests.ListFileRequestsResult$Serializer */
    static class Serializer extends StructSerializer<ListFileRequestsResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFileRequestsResult listFileRequestsResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("file_requests");
            StoneSerializers.list(Serializer.INSTANCE).serialize(listFileRequestsResult.fileRequests, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFileRequestsResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
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
                    if ("file_requests".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    ListFileRequestsResult listFileRequestsResult = new ListFileRequestsResult(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFileRequestsResult, listFileRequestsResult.toStringMultiline());
                    return listFileRequestsResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"file_requests\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListFileRequestsResult(List<FileRequest> list) {
        if (list != null) {
            for (FileRequest fileRequest : list) {
                if (fileRequest == null) {
                    throw new IllegalArgumentException("An item in list 'fileRequests' is null");
                }
            }
            this.fileRequests = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'fileRequests' is null");
    }

    public List<FileRequest> getFileRequests() {
        return this.fileRequests;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.fileRequests});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListFileRequestsResult listFileRequestsResult = (ListFileRequestsResult) obj;
        List<FileRequest> list = this.fileRequests;
        List<FileRequest> list2 = listFileRequestsResult.fileRequests;
        if (list != list2 && !list.equals(list2)) {
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
