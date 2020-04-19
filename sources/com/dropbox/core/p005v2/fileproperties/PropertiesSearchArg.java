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

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchArg */
class PropertiesSearchArg {
    protected final List<PropertiesSearchQuery> queries;
    protected final TemplateFilter templateFilter;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchArg$Serializer */
    static class Serializer extends StructSerializer<PropertiesSearchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertiesSearchArg propertiesSearchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("queries");
            StoneSerializers.list(Serializer.INSTANCE).serialize(propertiesSearchArg.queries, jsonGenerator);
            jsonGenerator.writeFieldName("template_filter");
            Serializer.INSTANCE.serialize(propertiesSearchArg.templateFilter, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PropertiesSearchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                TemplateFilter templateFilter = TemplateFilter.FILTER_NONE;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("queries".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("template_filter".equals(currentName)) {
                        templateFilter = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    PropertiesSearchArg propertiesSearchArg = new PropertiesSearchArg(list, templateFilter);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(propertiesSearchArg, propertiesSearchArg.toStringMultiline());
                    return propertiesSearchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"queries\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PropertiesSearchArg(List<PropertiesSearchQuery> list, TemplateFilter templateFilter2) {
        if (list == null) {
            throw new IllegalArgumentException("Required value for 'queries' is null");
        } else if (list.size() >= 1) {
            for (PropertiesSearchQuery propertiesSearchQuery : list) {
                if (propertiesSearchQuery == null) {
                    throw new IllegalArgumentException("An item in list 'queries' is null");
                }
            }
            this.queries = list;
            if (templateFilter2 != null) {
                this.templateFilter = templateFilter2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'templateFilter' is null");
        } else {
            throw new IllegalArgumentException("List 'queries' has fewer than 1 items");
        }
    }

    public PropertiesSearchArg(List<PropertiesSearchQuery> list) {
        this(list, TemplateFilter.FILTER_NONE);
    }

    public List<PropertiesSearchQuery> getQueries() {
        return this.queries;
    }

    public TemplateFilter getTemplateFilter() {
        return this.templateFilter;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.queries, this.templateFilter});
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
            com.dropbox.core.v2.fileproperties.PropertiesSearchArg r5 = (com.dropbox.core.p005v2.fileproperties.PropertiesSearchArg) r5
            java.util.List<com.dropbox.core.v2.fileproperties.PropertiesSearchQuery> r2 = r4.queries
            java.util.List<com.dropbox.core.v2.fileproperties.PropertiesSearchQuery> r3 = r5.queries
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.fileproperties.TemplateFilter r2 = r4.templateFilter
            com.dropbox.core.v2.fileproperties.TemplateFilter r5 = r5.templateFilter
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.PropertiesSearchArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
