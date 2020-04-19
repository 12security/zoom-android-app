package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.HasTeamSelectiveSyncValue */
public final class HasTeamSelectiveSyncValue {
    public static final HasTeamSelectiveSyncValue OTHER = new HasTeamSelectiveSyncValue().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public Boolean hasTeamSelectiveSyncValue;

    /* renamed from: com.dropbox.core.v2.team.HasTeamSelectiveSyncValue$Serializer */
    static class Serializer extends UnionSerializer<HasTeamSelectiveSyncValue> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08251.f138x28e70b2e[hasTeamSelectiveSyncValue.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("has_team_selective_sync", jsonGenerator);
            jsonGenerator.writeFieldName("has_team_selective_sync");
            StoneSerializers.boolean_().serialize(hasTeamSelectiveSyncValue.hasTeamSelectiveSyncValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public HasTeamSelectiveSyncValue deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue;
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
                if ("has_team_selective_sync".equals(str)) {
                    expectField("has_team_selective_sync", jsonParser);
                    hasTeamSelectiveSyncValue = HasTeamSelectiveSyncValue.hasTeamSelectiveSync(((Boolean) StoneSerializers.boolean_().deserialize(jsonParser)).booleanValue());
                } else {
                    hasTeamSelectiveSyncValue = HasTeamSelectiveSyncValue.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return hasTeamSelectiveSyncValue;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.HasTeamSelectiveSyncValue$Tag */
    public enum Tag {
        HAS_TEAM_SELECTIVE_SYNC,
        OTHER
    }

    private HasTeamSelectiveSyncValue() {
    }

    private HasTeamSelectiveSyncValue withTag(Tag tag) {
        HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue2 = new HasTeamSelectiveSyncValue();
        hasTeamSelectiveSyncValue2._tag = tag;
        return hasTeamSelectiveSyncValue2;
    }

    private HasTeamSelectiveSyncValue withTagAndHasTeamSelectiveSync(Tag tag, Boolean bool) {
        HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue2 = new HasTeamSelectiveSyncValue();
        hasTeamSelectiveSyncValue2._tag = tag;
        hasTeamSelectiveSyncValue2.hasTeamSelectiveSyncValue = bool;
        return hasTeamSelectiveSyncValue2;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isHasTeamSelectiveSync() {
        return this._tag == Tag.HAS_TEAM_SELECTIVE_SYNC;
    }

    public static HasTeamSelectiveSyncValue hasTeamSelectiveSync(boolean z) {
        return new HasTeamSelectiveSyncValue().withTagAndHasTeamSelectiveSync(Tag.HAS_TEAM_SELECTIVE_SYNC, Boolean.valueOf(z));
    }

    public boolean getHasTeamSelectiveSyncValue() {
        if (this._tag == Tag.HAS_TEAM_SELECTIVE_SYNC) {
            return this.hasTeamSelectiveSyncValue.booleanValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.HAS_TEAM_SELECTIVE_SYNC, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.hasTeamSelectiveSyncValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof HasTeamSelectiveSyncValue)) {
            return false;
        }
        HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue2 = (HasTeamSelectiveSyncValue) obj;
        if (this._tag != hasTeamSelectiveSyncValue2._tag) {
            return false;
        }
        switch (this._tag) {
            case HAS_TEAM_SELECTIVE_SYNC:
                if (this.hasTeamSelectiveSyncValue != hasTeamSelectiveSyncValue2.hasTeamSelectiveSyncValue) {
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
