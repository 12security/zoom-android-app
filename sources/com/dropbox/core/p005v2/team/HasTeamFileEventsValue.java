package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.AddrBookSettingActivity;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.HasTeamFileEventsValue */
public final class HasTeamFileEventsValue {
    public static final HasTeamFileEventsValue OTHER = new HasTeamFileEventsValue().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public Boolean enabledValue;

    /* renamed from: com.dropbox.core.v2.team.HasTeamFileEventsValue$Serializer */
    static class Serializer extends UnionSerializer<HasTeamFileEventsValue> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(HasTeamFileEventsValue hasTeamFileEventsValue, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08241.$SwitchMap$com$dropbox$core$v2$team$HasTeamFileEventsValue$Tag[hasTeamFileEventsValue.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag(AddrBookSettingActivity.ARG_RESULT_ENABLED, jsonGenerator);
            jsonGenerator.writeFieldName(AddrBookSettingActivity.ARG_RESULT_ENABLED);
            StoneSerializers.boolean_().serialize(hasTeamFileEventsValue.enabledValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public HasTeamFileEventsValue deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            HasTeamFileEventsValue hasTeamFileEventsValue;
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
                if (AddrBookSettingActivity.ARG_RESULT_ENABLED.equals(str)) {
                    expectField(AddrBookSettingActivity.ARG_RESULT_ENABLED, jsonParser);
                    hasTeamFileEventsValue = HasTeamFileEventsValue.enabled(((Boolean) StoneSerializers.boolean_().deserialize(jsonParser)).booleanValue());
                } else {
                    hasTeamFileEventsValue = HasTeamFileEventsValue.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return hasTeamFileEventsValue;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.HasTeamFileEventsValue$Tag */
    public enum Tag {
        ENABLED,
        OTHER
    }

    private HasTeamFileEventsValue() {
    }

    private HasTeamFileEventsValue withTag(Tag tag) {
        HasTeamFileEventsValue hasTeamFileEventsValue = new HasTeamFileEventsValue();
        hasTeamFileEventsValue._tag = tag;
        return hasTeamFileEventsValue;
    }

    private HasTeamFileEventsValue withTagAndEnabled(Tag tag, Boolean bool) {
        HasTeamFileEventsValue hasTeamFileEventsValue = new HasTeamFileEventsValue();
        hasTeamFileEventsValue._tag = tag;
        hasTeamFileEventsValue.enabledValue = bool;
        return hasTeamFileEventsValue;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isEnabled() {
        return this._tag == Tag.ENABLED;
    }

    public static HasTeamFileEventsValue enabled(boolean z) {
        return new HasTeamFileEventsValue().withTagAndEnabled(Tag.ENABLED, Boolean.valueOf(z));
    }

    public boolean getEnabledValue() {
        if (this._tag == Tag.ENABLED) {
            return this.enabledValue.booleanValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ENABLED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.enabledValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof HasTeamFileEventsValue)) {
            return false;
        }
        HasTeamFileEventsValue hasTeamFileEventsValue = (HasTeamFileEventsValue) obj;
        if (this._tag != hasTeamFileEventsValue._tag) {
            return false;
        }
        switch (this._tag) {
            case ENABLED:
                if (this.enabledValue != hasTeamFileEventsValue.enabledValue) {
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
