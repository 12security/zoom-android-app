package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.sharing.LinkExpiry */
public final class LinkExpiry {
    public static final LinkExpiry OTHER = new LinkExpiry().withTag(Tag.OTHER);
    public static final LinkExpiry REMOVE_EXPIRY = new LinkExpiry().withTag(Tag.REMOVE_EXPIRY);
    private Tag _tag;
    /* access modifiers changed from: private */
    public Date setExpiryValue;

    /* renamed from: com.dropbox.core.v2.sharing.LinkExpiry$Serializer */
    static class Serializer extends UnionSerializer<LinkExpiry> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(LinkExpiry linkExpiry, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (linkExpiry.tag()) {
                case REMOVE_EXPIRY:
                    jsonGenerator.writeString("remove_expiry");
                    return;
                case SET_EXPIRY:
                    jsonGenerator.writeStartObject();
                    writeTag("set_expiry", jsonGenerator);
                    jsonGenerator.writeFieldName("set_expiry");
                    StoneSerializers.timestamp().serialize(linkExpiry.setExpiryValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public LinkExpiry deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LinkExpiry linkExpiry;
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
                if ("remove_expiry".equals(str)) {
                    linkExpiry = LinkExpiry.REMOVE_EXPIRY;
                } else if ("set_expiry".equals(str)) {
                    expectField("set_expiry", jsonParser);
                    linkExpiry = LinkExpiry.setExpiry((Date) StoneSerializers.timestamp().deserialize(jsonParser));
                } else {
                    linkExpiry = LinkExpiry.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return linkExpiry;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.LinkExpiry$Tag */
    public enum Tag {
        REMOVE_EXPIRY,
        SET_EXPIRY,
        OTHER
    }

    private LinkExpiry() {
    }

    private LinkExpiry withTag(Tag tag) {
        LinkExpiry linkExpiry = new LinkExpiry();
        linkExpiry._tag = tag;
        return linkExpiry;
    }

    private LinkExpiry withTagAndSetExpiry(Tag tag, Date date) {
        LinkExpiry linkExpiry = new LinkExpiry();
        linkExpiry._tag = tag;
        linkExpiry.setExpiryValue = date;
        return linkExpiry;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isRemoveExpiry() {
        return this._tag == Tag.REMOVE_EXPIRY;
    }

    public boolean isSetExpiry() {
        return this._tag == Tag.SET_EXPIRY;
    }

    public static LinkExpiry setExpiry(Date date) {
        if (date != null) {
            return new LinkExpiry().withTagAndSetExpiry(Tag.SET_EXPIRY, date);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public Date getSetExpiryValue() {
        if (this._tag == Tag.SET_EXPIRY) {
            return this.setExpiryValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SET_EXPIRY, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.setExpiryValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof LinkExpiry)) {
            return false;
        }
        LinkExpiry linkExpiry = (LinkExpiry) obj;
        if (this._tag != linkExpiry._tag) {
            return false;
        }
        switch (this._tag) {
            case REMOVE_EXPIRY:
                return true;
            case SET_EXPIRY:
                Date date = this.setExpiryValue;
                Date date2 = linkExpiry.setExpiryValue;
                if (date != date2 && !date.equals(date2)) {
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
