package com.dropbox.core.p005v2.users;

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

/* renamed from: com.dropbox.core.v2.users.GetAccountBatchArg */
public class GetAccountBatchArg {
    protected final List<String> accountIds;

    /* renamed from: com.dropbox.core.v2.users.GetAccountBatchArg$Serializer */
    public static class Serializer extends StructSerializer<GetAccountBatchArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(GetAccountBatchArg getAccountBatchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("account_ids");
            StoneSerializers.list(StoneSerializers.string()).serialize(getAccountBatchArg.accountIds, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetAccountBatchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("account_ids".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    GetAccountBatchArg getAccountBatchArg = new GetAccountBatchArg(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getAccountBatchArg, getAccountBatchArg.toStringMultiline());
                    return getAccountBatchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"account_ids\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetAccountBatchArg(List<String> list) {
        if (list == null) {
            throw new IllegalArgumentException("Required value for 'accountIds' is null");
        } else if (list.size() >= 1) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'accountIds' is null");
                } else if (str.length() < 40) {
                    throw new IllegalArgumentException("Stringan item in list 'accountIds' is shorter than 40");
                } else if (str.length() > 40) {
                    throw new IllegalArgumentException("Stringan item in list 'accountIds' is longer than 40");
                }
            }
            this.accountIds = list;
        } else {
            throw new IllegalArgumentException("List 'accountIds' has fewer than 1 items");
        }
    }

    public List<String> getAccountIds() {
        return this.accountIds;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accountIds});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        GetAccountBatchArg getAccountBatchArg = (GetAccountBatchArg) obj;
        List<String> list = this.accountIds;
        List<String> list2 = getAccountBatchArg.accountIds;
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
