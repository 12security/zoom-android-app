package com.dropbox.core.p005v2.fileproperties;

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

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchResult */
public class PropertiesSearchResult {
    protected final String cursor;
    protected final List<PropertiesSearchMatch> matches;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchResult$Serializer */
    static class Serializer extends StructSerializer<PropertiesSearchResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertiesSearchResult propertiesSearchResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("matches");
            StoneSerializers.list(Serializer.INSTANCE).serialize(propertiesSearchResult.matches, jsonGenerator);
            if (propertiesSearchResult.cursor != null) {
                jsonGenerator.writeFieldName("cursor");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(propertiesSearchResult.cursor, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PropertiesSearchResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("matches".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("cursor".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    PropertiesSearchResult propertiesSearchResult = new PropertiesSearchResult(list, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(propertiesSearchResult, propertiesSearchResult.toStringMultiline());
                    return propertiesSearchResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"matches\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PropertiesSearchResult(List<PropertiesSearchMatch> list, String str) {
        if (list != null) {
            for (PropertiesSearchMatch propertiesSearchMatch : list) {
                if (propertiesSearchMatch == null) {
                    throw new IllegalArgumentException("An item in list 'matches' is null");
                }
            }
            this.matches = list;
            if (str == null || str.length() >= 1) {
                this.cursor = str;
                return;
            }
            throw new IllegalArgumentException("String 'cursor' is shorter than 1");
        }
        throw new IllegalArgumentException("Required value for 'matches' is null");
    }

    public PropertiesSearchResult(List<PropertiesSearchMatch> list) {
        this(list, null);
    }

    public List<PropertiesSearchMatch> getMatches() {
        return this.matches;
    }

    public String getCursor() {
        return this.cursor;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.matches, this.cursor});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.fileproperties.PropertiesSearchResult r5 = (com.dropbox.core.p005v2.fileproperties.PropertiesSearchResult) r5
            java.util.List<com.dropbox.core.v2.fileproperties.PropertiesSearchMatch> r2 = r4.matches
            java.util.List<com.dropbox.core.v2.fileproperties.PropertiesSearchMatch> r3 = r5.matches
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.String r2 = r4.cursor
            java.lang.String r5 = r5.cursor
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.PropertiesSearchResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
