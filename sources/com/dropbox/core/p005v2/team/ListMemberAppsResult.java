package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.ListMemberAppsResult */
public class ListMemberAppsResult {
    protected final List<ApiApp> linkedApiApps;

    /* renamed from: com.dropbox.core.v2.team.ListMemberAppsResult$Serializer */
    static class Serializer extends StructSerializer<ListMemberAppsResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListMemberAppsResult listMemberAppsResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("linked_api_apps");
            StoneSerializers.list(Serializer.INSTANCE).serialize(listMemberAppsResult.linkedApiApps, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListMemberAppsResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("linked_api_apps".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    ListMemberAppsResult listMemberAppsResult = new ListMemberAppsResult(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listMemberAppsResult, listMemberAppsResult.toStringMultiline());
                    return listMemberAppsResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"linked_api_apps\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListMemberAppsResult(List<ApiApp> list) {
        if (list != null) {
            for (ApiApp apiApp : list) {
                if (apiApp == null) {
                    throw new IllegalArgumentException("An item in list 'linkedApiApps' is null");
                }
            }
            this.linkedApiApps = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'linkedApiApps' is null");
    }

    public List<ApiApp> getLinkedApiApps() {
        return this.linkedApiApps;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.linkedApiApps});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListMemberAppsResult listMemberAppsResult = (ListMemberAppsResult) obj;
        List<ApiApp> list = this.linkedApiApps;
        List<ApiApp> list2 = listMemberAppsResult.linkedApiApps;
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
