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

/* renamed from: com.dropbox.core.v2.team.CustomQuotaResult */
public final class CustomQuotaResult {
    public static final CustomQuotaResult OTHER = new CustomQuotaResult().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public UserSelectorArg invalidUserValue;
    /* access modifiers changed from: private */
    public UserCustomQuotaResult successValue;

    /* renamed from: com.dropbox.core.v2.team.CustomQuotaResult$Serializer */
    static class Serializer extends UnionSerializer<CustomQuotaResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CustomQuotaResult customQuotaResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (customQuotaResult.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    Serializer.INSTANCE.serialize(customQuotaResult.successValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case INVALID_USER:
                    jsonGenerator.writeStartObject();
                    writeTag("invalid_user", jsonGenerator);
                    jsonGenerator.writeFieldName("invalid_user");
                    Serializer.INSTANCE.serialize(customQuotaResult.invalidUserValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public CustomQuotaResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            CustomQuotaResult customQuotaResult;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if (Param.SUCCESS.equals(str)) {
                    customQuotaResult = CustomQuotaResult.success(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("invalid_user".equals(str)) {
                    expectField("invalid_user", jsonParser);
                    customQuotaResult = CustomQuotaResult.invalidUser(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    customQuotaResult = CustomQuotaResult.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return customQuotaResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.CustomQuotaResult$Tag */
    public enum Tag {
        SUCCESS,
        INVALID_USER,
        OTHER
    }

    private CustomQuotaResult() {
    }

    private CustomQuotaResult withTag(Tag tag) {
        CustomQuotaResult customQuotaResult = new CustomQuotaResult();
        customQuotaResult._tag = tag;
        return customQuotaResult;
    }

    private CustomQuotaResult withTagAndSuccess(Tag tag, UserCustomQuotaResult userCustomQuotaResult) {
        CustomQuotaResult customQuotaResult = new CustomQuotaResult();
        customQuotaResult._tag = tag;
        customQuotaResult.successValue = userCustomQuotaResult;
        return customQuotaResult;
    }

    private CustomQuotaResult withTagAndInvalidUser(Tag tag, UserSelectorArg userSelectorArg) {
        CustomQuotaResult customQuotaResult = new CustomQuotaResult();
        customQuotaResult._tag = tag;
        customQuotaResult.invalidUserValue = userSelectorArg;
        return customQuotaResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static CustomQuotaResult success(UserCustomQuotaResult userCustomQuotaResult) {
        if (userCustomQuotaResult != null) {
            return new CustomQuotaResult().withTagAndSuccess(Tag.SUCCESS, userCustomQuotaResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UserCustomQuotaResult getSuccessValue() {
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

    public static CustomQuotaResult invalidUser(UserSelectorArg userSelectorArg) {
        if (userSelectorArg != null) {
            return new CustomQuotaResult().withTagAndInvalidUser(Tag.INVALID_USER, userSelectorArg);
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
        if (obj == null || !(obj instanceof CustomQuotaResult)) {
            return false;
        }
        CustomQuotaResult customQuotaResult = (CustomQuotaResult) obj;
        if (this._tag != customQuotaResult._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                UserCustomQuotaResult userCustomQuotaResult = this.successValue;
                UserCustomQuotaResult userCustomQuotaResult2 = customQuotaResult.successValue;
                if (userCustomQuotaResult != userCustomQuotaResult2 && !userCustomQuotaResult.equals(userCustomQuotaResult2)) {
                    z = false;
                }
                return z;
            case INVALID_USER:
                UserSelectorArg userSelectorArg = this.invalidUserValue;
                UserSelectorArg userSelectorArg2 = customQuotaResult.invalidUserValue;
                if (userSelectorArg != userSelectorArg2 && !userSelectorArg.equals(userSelectorArg2)) {
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
