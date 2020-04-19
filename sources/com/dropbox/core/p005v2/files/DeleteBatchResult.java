package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxList;
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

/* renamed from: com.dropbox.core.v2.files.DeleteBatchResult */
public class DeleteBatchResult extends FileOpsResult {
    protected final List<DeleteBatchResultEntry> entries;

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchResult$Serializer */
    static class Serializer extends StructSerializer<DeleteBatchResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeleteBatchResult deleteBatchResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_ENTRIES);
            StoneSerializers.list(Serializer.INSTANCE).serialize(deleteBatchResult.entries, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeleteBatchResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if (BoxList.FIELD_ENTRIES.equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    DeleteBatchResult deleteBatchResult = new DeleteBatchResult(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(deleteBatchResult, deleteBatchResult.toStringMultiline());
                    return deleteBatchResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"entries\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeleteBatchResult(List<DeleteBatchResultEntry> list) {
        if (list != null) {
            for (DeleteBatchResultEntry deleteBatchResultEntry : list) {
                if (deleteBatchResultEntry == null) {
                    throw new IllegalArgumentException("An item in list 'entries' is null");
                }
            }
            this.entries = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'entries' is null");
    }

    public List<DeleteBatchResultEntry> getEntries() {
        return this.entries;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.entries});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DeleteBatchResult deleteBatchResult = (DeleteBatchResult) obj;
        List<DeleteBatchResultEntry> list = this.entries;
        List<DeleteBatchResultEntry> list2 = deleteBatchResult.entries;
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
