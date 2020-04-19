package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.actions.SearchIntents;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchQuery */
public class PropertiesSearchQuery {
    protected final LogicalOperator logicalOperator;
    protected final PropertiesSearchMode mode;
    protected final String query;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchQuery$Serializer */
    static class Serializer extends StructSerializer<PropertiesSearchQuery> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertiesSearchQuery propertiesSearchQuery, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(SearchIntents.EXTRA_QUERY);
            StoneSerializers.string().serialize(propertiesSearchQuery.query, jsonGenerator);
            jsonGenerator.writeFieldName("mode");
            Serializer.INSTANCE.serialize(propertiesSearchQuery.mode, jsonGenerator);
            jsonGenerator.writeFieldName("logical_operator");
            Serializer.INSTANCE.serialize(propertiesSearchQuery.logicalOperator, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PropertiesSearchQuery deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                LogicalOperator logicalOperator = LogicalOperator.OR_OPERATOR;
                PropertiesSearchMode propertiesSearchMode = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (SearchIntents.EXTRA_QUERY.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("mode".equals(currentName)) {
                        propertiesSearchMode = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("logical_operator".equals(currentName)) {
                        logicalOperator = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"query\" missing.");
                } else if (propertiesSearchMode != null) {
                    PropertiesSearchQuery propertiesSearchQuery = new PropertiesSearchQuery(str2, propertiesSearchMode, logicalOperator);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(propertiesSearchQuery, propertiesSearchQuery.toStringMultiline());
                    return propertiesSearchQuery;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"mode\" missing.");
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

    public PropertiesSearchQuery(String str, PropertiesSearchMode propertiesSearchMode, LogicalOperator logicalOperator2) {
        if (str != null) {
            this.query = str;
            if (propertiesSearchMode != null) {
                this.mode = propertiesSearchMode;
                if (logicalOperator2 != null) {
                    this.logicalOperator = logicalOperator2;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'logicalOperator' is null");
            }
            throw new IllegalArgumentException("Required value for 'mode' is null");
        }
        throw new IllegalArgumentException("Required value for 'query' is null");
    }

    public PropertiesSearchQuery(String str, PropertiesSearchMode propertiesSearchMode) {
        this(str, propertiesSearchMode, LogicalOperator.OR_OPERATOR);
    }

    public String getQuery() {
        return this.query;
    }

    public PropertiesSearchMode getMode() {
        return this.mode;
    }

    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.query, this.mode, this.logicalOperator});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r5) == false) goto L_0x003d;
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
            if (r2 == 0) goto L_0x003f
            com.dropbox.core.v2.fileproperties.PropertiesSearchQuery r5 = (com.dropbox.core.p005v2.fileproperties.PropertiesSearchQuery) r5
            java.lang.String r2 = r4.query
            java.lang.String r3 = r5.query
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0024:
            com.dropbox.core.v2.fileproperties.PropertiesSearchMode r2 = r4.mode
            com.dropbox.core.v2.fileproperties.PropertiesSearchMode r3 = r5.mode
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0030:
            com.dropbox.core.v2.fileproperties.LogicalOperator r2 = r4.logicalOperator
            com.dropbox.core.v2.fileproperties.LogicalOperator r5 = r5.logicalOperator
            if (r2 == r5) goto L_0x003e
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = 0
        L_0x003e:
            return r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.PropertiesSearchQuery.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
