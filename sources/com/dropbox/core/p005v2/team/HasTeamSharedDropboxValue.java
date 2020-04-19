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

/* renamed from: com.dropbox.core.v2.team.HasTeamSharedDropboxValue */
public final class HasTeamSharedDropboxValue {
    public static final HasTeamSharedDropboxValue OTHER = new HasTeamSharedDropboxValue().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public Boolean hasTeamSharedDropboxValue;

    /* renamed from: com.dropbox.core.v2.team.HasTeamSharedDropboxValue$Serializer */
    static class Serializer extends UnionSerializer<HasTeamSharedDropboxValue> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(HasTeamSharedDropboxValue hasTeamSharedDropboxValue, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08261.f139xede91ee[hasTeamSharedDropboxValue.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("has_team_shared_dropbox", jsonGenerator);
            jsonGenerator.writeFieldName("has_team_shared_dropbox");
            StoneSerializers.boolean_().serialize(hasTeamSharedDropboxValue.hasTeamSharedDropboxValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public HasTeamSharedDropboxValue deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            HasTeamSharedDropboxValue hasTeamSharedDropboxValue;
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
                if ("has_team_shared_dropbox".equals(str)) {
                    expectField("has_team_shared_dropbox", jsonParser);
                    hasTeamSharedDropboxValue = HasTeamSharedDropboxValue.hasTeamSharedDropbox(((Boolean) StoneSerializers.boolean_().deserialize(jsonParser)).booleanValue());
                } else {
                    hasTeamSharedDropboxValue = HasTeamSharedDropboxValue.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return hasTeamSharedDropboxValue;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.HasTeamSharedDropboxValue$Tag */
    public enum Tag {
        HAS_TEAM_SHARED_DROPBOX,
        OTHER
    }

    private HasTeamSharedDropboxValue() {
    }

    private HasTeamSharedDropboxValue withTag(Tag tag) {
        HasTeamSharedDropboxValue hasTeamSharedDropboxValue2 = new HasTeamSharedDropboxValue();
        hasTeamSharedDropboxValue2._tag = tag;
        return hasTeamSharedDropboxValue2;
    }

    private HasTeamSharedDropboxValue withTagAndHasTeamSharedDropbox(Tag tag, Boolean bool) {
        HasTeamSharedDropboxValue hasTeamSharedDropboxValue2 = new HasTeamSharedDropboxValue();
        hasTeamSharedDropboxValue2._tag = tag;
        hasTeamSharedDropboxValue2.hasTeamSharedDropboxValue = bool;
        return hasTeamSharedDropboxValue2;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isHasTeamSharedDropbox() {
        return this._tag == Tag.HAS_TEAM_SHARED_DROPBOX;
    }

    public static HasTeamSharedDropboxValue hasTeamSharedDropbox(boolean z) {
        return new HasTeamSharedDropboxValue().withTagAndHasTeamSharedDropbox(Tag.HAS_TEAM_SHARED_DROPBOX, Boolean.valueOf(z));
    }

    public boolean getHasTeamSharedDropboxValue() {
        if (this._tag == Tag.HAS_TEAM_SHARED_DROPBOX) {
            return this.hasTeamSharedDropboxValue.booleanValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.HAS_TEAM_SHARED_DROPBOX, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.hasTeamSharedDropboxValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof HasTeamSharedDropboxValue)) {
            return false;
        }
        HasTeamSharedDropboxValue hasTeamSharedDropboxValue2 = (HasTeamSharedDropboxValue) obj;
        if (this._tag != hasTeamSharedDropboxValue2._tag) {
            return false;
        }
        switch (this._tag) {
            case HAS_TEAM_SHARED_DROPBOX:
                if (this.hasTeamSharedDropboxValue != hasTeamSharedDropboxValue2.hasTeamSharedDropboxValue) {
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
