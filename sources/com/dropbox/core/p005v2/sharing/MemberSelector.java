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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.MemberSelector */
public final class MemberSelector {
    public static final MemberSelector OTHER = new MemberSelector().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String dropboxIdValue;
    /* access modifiers changed from: private */
    public String emailValue;

    /* renamed from: com.dropbox.core.v2.sharing.MemberSelector$Serializer */
    public static class Serializer extends UnionSerializer<MemberSelector> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(MemberSelector memberSelector, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (memberSelector.tag()) {
                case DROPBOX_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("dropbox_id", jsonGenerator);
                    jsonGenerator.writeFieldName("dropbox_id");
                    StoneSerializers.string().serialize(memberSelector.dropboxIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case EMAIL:
                    jsonGenerator.writeStartObject();
                    writeTag("email", jsonGenerator);
                    jsonGenerator.writeFieldName("email");
                    StoneSerializers.string().serialize(memberSelector.emailValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MemberSelector deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MemberSelector memberSelector;
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
                if ("dropbox_id".equals(str)) {
                    expectField("dropbox_id", jsonParser);
                    memberSelector = MemberSelector.dropboxId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("email".equals(str)) {
                    expectField("email", jsonParser);
                    memberSelector = MemberSelector.email((String) StoneSerializers.string().deserialize(jsonParser));
                } else {
                    memberSelector = MemberSelector.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return memberSelector;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.MemberSelector$Tag */
    public enum Tag {
        DROPBOX_ID,
        EMAIL,
        OTHER
    }

    private MemberSelector() {
    }

    private MemberSelector withTag(Tag tag) {
        MemberSelector memberSelector = new MemberSelector();
        memberSelector._tag = tag;
        return memberSelector;
    }

    private MemberSelector withTagAndDropboxId(Tag tag, String str) {
        MemberSelector memberSelector = new MemberSelector();
        memberSelector._tag = tag;
        memberSelector.dropboxIdValue = str;
        return memberSelector;
    }

    private MemberSelector withTagAndEmail(Tag tag, String str) {
        MemberSelector memberSelector = new MemberSelector();
        memberSelector._tag = tag;
        memberSelector.emailValue = str;
        return memberSelector;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isDropboxId() {
        return this._tag == Tag.DROPBOX_ID;
    }

    public static MemberSelector dropboxId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new MemberSelector().withTagAndDropboxId(Tag.DROPBOX_ID, str);
        } else {
            throw new IllegalArgumentException("String is shorter than 1");
        }
    }

    public String getDropboxIdValue() {
        if (this._tag == Tag.DROPBOX_ID) {
            return this.dropboxIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.DROPBOX_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isEmail() {
        return this._tag == Tag.EMAIL;
    }

    public static MemberSelector email(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberSelector().withTagAndEmail(Tag.EMAIL, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getEmailValue() {
        if (this._tag == Tag.EMAIL) {
            return this.emailValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.EMAIL, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.dropboxIdValue, this.emailValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof MemberSelector)) {
            return false;
        }
        MemberSelector memberSelector = (MemberSelector) obj;
        if (this._tag != memberSelector._tag) {
            return false;
        }
        switch (this._tag) {
            case DROPBOX_ID:
                String str = this.dropboxIdValue;
                String str2 = memberSelector.dropboxIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case EMAIL:
                String str3 = this.emailValue;
                String str4 = memberSelector.emailValue;
                if (str3 != str4 && !str3.equals(str4)) {
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
