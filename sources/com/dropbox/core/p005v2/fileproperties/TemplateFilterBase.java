package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.fileproperties.TemplateFilterBase */
public final class TemplateFilterBase {
    public static final TemplateFilterBase OTHER = new TemplateFilterBase().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public List<String> filterSomeValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.TemplateFilterBase$Serializer */
    public static class Serializer extends UnionSerializer<TemplateFilterBase> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(TemplateFilterBase templateFilterBase, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06471.f79x2475bde0[templateFilterBase.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("filter_some", jsonGenerator);
            jsonGenerator.writeFieldName("filter_some");
            StoneSerializers.list(StoneSerializers.string()).serialize(templateFilterBase.filterSomeValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public TemplateFilterBase deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TemplateFilterBase templateFilterBase;
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
                if ("filter_some".equals(str)) {
                    expectField("filter_some", jsonParser);
                    templateFilterBase = TemplateFilterBase.filterSome((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
                } else {
                    templateFilterBase = TemplateFilterBase.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return templateFilterBase;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.TemplateFilterBase$Tag */
    public enum Tag {
        FILTER_SOME,
        OTHER
    }

    private TemplateFilterBase() {
    }

    private TemplateFilterBase withTag(Tag tag) {
        TemplateFilterBase templateFilterBase = new TemplateFilterBase();
        templateFilterBase._tag = tag;
        return templateFilterBase;
    }

    private TemplateFilterBase withTagAndFilterSome(Tag tag, List<String> list) {
        TemplateFilterBase templateFilterBase = new TemplateFilterBase();
        templateFilterBase._tag = tag;
        templateFilterBase.filterSomeValue = list;
        return templateFilterBase;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isFilterSome() {
        return this._tag == Tag.FILTER_SOME;
    }

    public static TemplateFilterBase filterSome(List<String> list) {
        if (list == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (list.size() >= 1) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list is null");
                } else if (str.length() < 1) {
                    throw new IllegalArgumentException("Stringan item in list is shorter than 1");
                } else if (!Pattern.matches("(/|ptid:).*", str)) {
                    throw new IllegalArgumentException("Stringan item in list does not match pattern");
                }
            }
            return new TemplateFilterBase().withTagAndFilterSome(Tag.FILTER_SOME, list);
        } else {
            throw new IllegalArgumentException("List has fewer than 1 items");
        }
    }

    public List<String> getFilterSomeValue() {
        if (this._tag == Tag.FILTER_SOME) {
            return this.filterSomeValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FILTER_SOME, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.filterSomeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TemplateFilterBase)) {
            return false;
        }
        TemplateFilterBase templateFilterBase = (TemplateFilterBase) obj;
        if (this._tag != templateFilterBase._tag) {
            return false;
        }
        switch (this._tag) {
            case FILTER_SOME:
                List<String> list = this.filterSomeValue;
                List<String> list2 = templateFilterBase.filterSomeValue;
                if (list != list2 && !list.equals(list2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
