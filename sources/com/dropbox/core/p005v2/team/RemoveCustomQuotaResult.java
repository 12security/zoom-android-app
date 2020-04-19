package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.RemoveCustomQuotaResult */
public final class RemoveCustomQuotaResult {
    public static final RemoveCustomQuotaResult OTHER = new RemoveCustomQuotaResult().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public UserSelectorArg invalidUserValue;
    /* access modifiers changed from: private */
    public UserSelectorArg successValue;

    /* renamed from: com.dropbox.core.v2.team.RemoveCustomQuotaResult$Serializer */
    static class Serializer extends UnionSerializer<RemoveCustomQuotaResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RemoveCustomQuotaResult removeCustomQuotaResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (removeCustomQuotaResult.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    jsonGenerator.writeFieldName(Param.SUCCESS);
                    Serializer.INSTANCE.serialize(removeCustomQuotaResult.successValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case INVALID_USER:
                    jsonGenerator.writeStartObject();
                    writeTag("invalid_user", jsonGenerator);
                    jsonGenerator.writeFieldName("invalid_user");
                    Serializer.INSTANCE.serialize(removeCustomQuotaResult.invalidUserValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RemoveCustomQuotaResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RemoveCustomQuotaResult removeCustomQuotaResult;
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
                if (Param.SUCCESS.equals(str)) {
                    expectField(Param.SUCCESS, jsonParser);
                    removeCustomQuotaResult = RemoveCustomQuotaResult.success(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("invalid_user".equals(str)) {
                    expectField("invalid_user", jsonParser);
                    removeCustomQuotaResult = RemoveCustomQuotaResult.invalidUser(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    removeCustomQuotaResult = RemoveCustomQuotaResult.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return removeCustomQuotaResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.RemoveCustomQuotaResult$Tag */
    public enum Tag {
        SUCCESS,
        INVALID_USER,
        OTHER
    }

    private RemoveCustomQuotaResult() {
    }

    private RemoveCustomQuotaResult withTag(Tag tag) {
        RemoveCustomQuotaResult removeCustomQuotaResult = new RemoveCustomQuotaResult();
        removeCustomQuotaResult._tag = tag;
        return removeCustomQuotaResult;
    }

    private RemoveCustomQuotaResult withTagAndSuccess(Tag tag, UserSelectorArg userSelectorArg) {
        RemoveCustomQuotaResult removeCustomQuotaResult = new RemoveCustomQuotaResult();
        removeCustomQuotaResult._tag = tag;
        removeCustomQuotaResult.successValue = userSelectorArg;
        return removeCustomQuotaResult;
    }

    private RemoveCustomQuotaResult withTagAndInvalidUser(Tag tag, UserSelectorArg userSelectorArg) {
        RemoveCustomQuotaResult removeCustomQuotaResult = new RemoveCustomQuotaResult();
        removeCustomQuotaResult._tag = tag;
        removeCustomQuotaResult.invalidUserValue = userSelectorArg;
        return removeCustomQuotaResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static RemoveCustomQuotaResult success(UserSelectorArg userSelectorArg) {
        if (userSelectorArg != null) {
            return new RemoveCustomQuotaResult().withTagAndSuccess(Tag.SUCCESS, userSelectorArg);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UserSelectorArg getSuccessValue() {
        if (this._tag == Tag.SUCCESS) {
            return this.successValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SUCCESS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isInvalidUser() {
        return this._tag == Tag.INVALID_USER;
    }

    public static RemoveCustomQuotaResult invalidUser(UserSelectorArg userSelectorArg) {
        if (userSelectorArg != null) {
            return new RemoveCustomQuotaResult().withTagAndInvalidUser(Tag.INVALID_USER, userSelectorArg);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UserSelectorArg getInvalidUserValue() {
        if (this._tag == Tag.INVALID_USER) {
            return this.invalidUserValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INVALID_USER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.successValue, this.invalidUserValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof RemoveCustomQuotaResult)) {
            return false;
        }
        RemoveCustomQuotaResult removeCustomQuotaResult = (RemoveCustomQuotaResult) obj;
        if (this._tag != removeCustomQuotaResult._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                UserSelectorArg userSelectorArg = this.successValue;
                UserSelectorArg userSelectorArg2 = removeCustomQuotaResult.successValue;
                if (userSelectorArg != userSelectorArg2 && !userSelectorArg.equals(userSelectorArg2)) {
                    z = false;
                }
                return z;
            case INVALID_USER:
                UserSelectorArg userSelectorArg3 = this.invalidUserValue;
                UserSelectorArg userSelectorArg4 = removeCustomQuotaResult.invalidUserValue;
                if (userSelectorArg3 != userSelectorArg4 && !userSelectorArg3.equals(userSelectorArg4)) {
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
