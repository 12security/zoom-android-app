package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.BoxApiMetadata;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.SearchMatch */
public class SearchMatch {
    protected final SearchMatchType matchType;
    protected final Metadata metadata;

    /* renamed from: com.dropbox.core.v2.files.SearchMatch$Serializer */
    static class Serializer extends StructSerializer<SearchMatch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SearchMatch searchMatch, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("match_type");
            Serializer.INSTANCE.serialize(searchMatch.matchType, jsonGenerator);
            jsonGenerator.writeFieldName(BoxApiMetadata.BOX_API_METADATA);
            Serializer.INSTANCE.serialize(searchMatch.metadata, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SearchMatch deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SearchMatchType searchMatchType = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Metadata metadata = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("match_type".equals(currentName)) {
                        searchMatchType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (BoxApiMetadata.BOX_API_METADATA.equals(currentName)) {
                        metadata = (Metadata) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (searchMatchType == null) {
                    throw new JsonParseException(jsonParser, "Required field \"match_type\" missing.");
                } else if (metadata != null) {
                    SearchMatch searchMatch = new SearchMatch(searchMatchType, metadata);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(searchMatch, searchMatch.toStringMultiline());
                    return searchMatch;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"metadata\" missing.");
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

    public SearchMatch(SearchMatchType searchMatchType, Metadata metadata2) {
        if (searchMatchType != null) {
            this.matchType = searchMatchType;
            if (metadata2 != null) {
                this.metadata = metadata2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'metadata' is null");
        }
        throw new IllegalArgumentException("Required value for 'matchType' is null");
    }

    public SearchMatchType getMatchType() {
        return this.matchType;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.matchType, this.metadata});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.files.SearchMatch r5 = (com.dropbox.core.p005v2.files.SearchMatch) r5
            com.dropbox.core.v2.files.SearchMatchType r2 = r4.matchType
            com.dropbox.core.v2.files.SearchMatchType r3 = r5.matchType
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.files.Metadata r2 = r4.metadata
            com.dropbox.core.v2.files.Metadata r5 = r5.metadata
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.SearchMatch.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
