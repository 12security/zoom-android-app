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

/* renamed from: com.dropbox.core.v2.fileproperties.TemplateFilter */
public final class TemplateFilter {
    public static final TemplateFilter FILTER_NONE = new TemplateFilter().withTag(Tag.FILTER_NONE);
    public static final TemplateFilter OTHER = new TemplateFilter().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public List<String> filterSomeValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.TemplateFilter$Serializer */
    static class Serializer extends UnionSerializer<TemplateFilter> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TemplateFilter templateFilter, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (templateFilter.tag()) {
                case FILTER_SOME:
                    jsonGenerator.writeStartObject();
                    writeTag("filter_some", jsonGenerator);
                    jsonGenerator.writeFieldName("filter_some");
                    StoneSerializers.list(StoneSerializers.string()).serialize(templateFilter.filterSomeValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case FILTER_NONE:
                    jsonGenerator.writeString("filter_none");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(templateFilter.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TemplateFilter deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TemplateFilter templateFilter;
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
                    templateFilter = TemplateFilter.filterSome((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
                } else if ("other".equals(str)) {
                    templateFilter = TemplateFilter.OTHER;
                } else if ("filter_none".equals(str)) {
                    templateFilter = TemplateFilter.FILTER_NONE;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return templateFilter;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.TemplateFilter$Tag */
    public enum Tag {
        FILTER_SOME,
        OTHER,
        FILTER_NONE
    }

    private TemplateFilter() {
    }

    private TemplateFilter withTag(Tag tag) {
        TemplateFilter templateFilter = new TemplateFilter();
        templateFilter._tag = tag;
        return templateFilter;
    }

    private TemplateFilter withTagAndFilterSome(Tag tag, List<String> list) {
        TemplateFilter templateFilter = new TemplateFilter();
        templateFilter._tag = tag;
        templateFilter.filterSomeValue = list;
        return templateFilter;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isFilterSome() {
        return this._tag == Tag.FILTER_SOME;
    }

    public static TemplateFilter filterSome(List<String> list) {
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
            return new TemplateFilter().withTagAndFilterSome(Tag.FILTER_SOME, list);
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

    public boolean isFilterNone() {
        return this._tag == Tag.FILTER_NONE;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.filterSomeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TemplateFilter)) {
            return false;
        }
        TemplateFilter templateFilter = (TemplateFilter) obj;
        if (this._tag != templateFilter._tag) {
            return false;
        }
        switch (this._tag) {
            case FILTER_SOME:
                List<String> list = this.filterSomeValue;
                List<String> list2 = templateFilter.filterSomeValue;
                if (list != list2 && !list.equals(list2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
            case FILTER_NONE:
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
