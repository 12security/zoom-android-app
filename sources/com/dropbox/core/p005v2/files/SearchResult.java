package com.dropbox.core.p005v2.files;

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

/* renamed from: com.dropbox.core.v2.files.SearchResult */
public class SearchResult {
    protected final List<SearchMatch> matches;
    protected final boolean more;
    protected final long start;

    /* renamed from: com.dropbox.core.v2.files.SearchResult$Serializer */
    static class Serializer extends StructSerializer<SearchResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SearchResult searchResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("matches");
            StoneSerializers.list(Serializer.INSTANCE).serialize(searchResult.matches, jsonGenerator);
            jsonGenerator.writeFieldName("more");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(searchResult.more), jsonGenerator);
            jsonGenerator.writeFieldName("start");
            StoneSerializers.uInt64().serialize(Long.valueOf(searchResult.start), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SearchResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean bool = null;
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("matches".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("more".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("start".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"matches\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"more\" missing.");
                } else if (l != null) {
                    SearchResult searchResult = new SearchResult(list, bool.booleanValue(), l.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(searchResult, searchResult.toStringMultiline());
                    return searchResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"start\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public SearchResult(List<SearchMatch> list, boolean z, long j) {
        if (list != null) {
            for (SearchMatch searchMatch : list) {
                if (searchMatch == null) {
                    throw new IllegalArgumentException("An item in list 'matches' is null");
                }
            }
            this.matches = list;
            this.more = z;
            this.start = j;
            return;
        }
        throw new IllegalArgumentException("Required value for 'matches' is null");
    }

    public List<SearchMatch> getMatches() {
        return this.matches;
    }

    public boolean getMore() {
        return this.more;
    }

    public long getStart() {
        return this.start;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.matches, Boolean.valueOf(this.more), Long.valueOf(this.start)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SearchResult searchResult = (SearchResult) obj;
        List<SearchMatch> list = this.matches;
        List<SearchMatch> list2 = searchResult.matches;
        if (!((list == list2 || list.equals(list2)) && this.more == searchResult.more && this.start == searchResult.start)) {
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
