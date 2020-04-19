package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.FeatureValue */
public final class FeatureValue {
    public static final FeatureValue OTHER = new FeatureValue().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public HasTeamFileEventsValue hasTeamFileEventsValue;
    /* access modifiers changed from: private */
    public HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue;
    /* access modifiers changed from: private */
    public HasTeamSharedDropboxValue hasTeamSharedDropboxValue;
    /* access modifiers changed from: private */
    public UploadApiRateLimitValue uploadApiRateLimitValue;

    /* renamed from: com.dropbox.core.v2.team.FeatureValue$Serializer */
    static class Serializer extends UnionSerializer<FeatureValue> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FeatureValue featureValue, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (featureValue.tag()) {
                case UPLOAD_API_RATE_LIMIT:
                    jsonGenerator.writeStartObject();
                    writeTag("upload_api_rate_limit", jsonGenerator);
                    jsonGenerator.writeFieldName("upload_api_rate_limit");
                    Serializer.INSTANCE.serialize(featureValue.uploadApiRateLimitValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case HAS_TEAM_SHARED_DROPBOX:
                    jsonGenerator.writeStartObject();
                    writeTag("has_team_shared_dropbox", jsonGenerator);
                    jsonGenerator.writeFieldName("has_team_shared_dropbox");
                    Serializer.INSTANCE.serialize(featureValue.hasTeamSharedDropboxValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case HAS_TEAM_FILE_EVENTS:
                    jsonGenerator.writeStartObject();
                    writeTag("has_team_file_events", jsonGenerator);
                    jsonGenerator.writeFieldName("has_team_file_events");
                    Serializer.INSTANCE.serialize(featureValue.hasTeamFileEventsValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case HAS_TEAM_SELECTIVE_SYNC:
                    jsonGenerator.writeStartObject();
                    writeTag("has_team_selective_sync", jsonGenerator);
                    jsonGenerator.writeFieldName("has_team_selective_sync");
                    Serializer.INSTANCE.serialize(featureValue.hasTeamSelectiveSyncValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public FeatureValue deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            FeatureValue featureValue;
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
                if ("upload_api_rate_limit".equals(str)) {
                    expectField("upload_api_rate_limit", jsonParser);
                    featureValue = FeatureValue.uploadApiRateLimit(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("has_team_shared_dropbox".equals(str)) {
                    expectField("has_team_shared_dropbox", jsonParser);
                    featureValue = FeatureValue.hasTeamSharedDropbox(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("has_team_file_events".equals(str)) {
                    expectField("has_team_file_events", jsonParser);
                    featureValue = FeatureValue.hasTeamFileEvents(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("has_team_selective_sync".equals(str)) {
                    expectField("has_team_selective_sync", jsonParser);
                    featureValue = FeatureValue.hasTeamSelectiveSync(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    featureValue = FeatureValue.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return featureValue;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.FeatureValue$Tag */
    public enum Tag {
        UPLOAD_API_RATE_LIMIT,
        HAS_TEAM_SHARED_DROPBOX,
        HAS_TEAM_FILE_EVENTS,
        HAS_TEAM_SELECTIVE_SYNC,
        OTHER
    }

    private FeatureValue() {
    }

    private FeatureValue withTag(Tag tag) {
        FeatureValue featureValue = new FeatureValue();
        featureValue._tag = tag;
        return featureValue;
    }

    private FeatureValue withTagAndUploadApiRateLimit(Tag tag, UploadApiRateLimitValue uploadApiRateLimitValue2) {
        FeatureValue featureValue = new FeatureValue();
        featureValue._tag = tag;
        featureValue.uploadApiRateLimitValue = uploadApiRateLimitValue2;
        return featureValue;
    }

    private FeatureValue withTagAndHasTeamSharedDropbox(Tag tag, HasTeamSharedDropboxValue hasTeamSharedDropboxValue2) {
        FeatureValue featureValue = new FeatureValue();
        featureValue._tag = tag;
        featureValue.hasTeamSharedDropboxValue = hasTeamSharedDropboxValue2;
        return featureValue;
    }

    private FeatureValue withTagAndHasTeamFileEvents(Tag tag, HasTeamFileEventsValue hasTeamFileEventsValue2) {
        FeatureValue featureValue = new FeatureValue();
        featureValue._tag = tag;
        featureValue.hasTeamFileEventsValue = hasTeamFileEventsValue2;
        return featureValue;
    }

    private FeatureValue withTagAndHasTeamSelectiveSync(Tag tag, HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue2) {
        FeatureValue featureValue = new FeatureValue();
        featureValue._tag = tag;
        featureValue.hasTeamSelectiveSyncValue = hasTeamSelectiveSyncValue2;
        return featureValue;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUploadApiRateLimit() {
        return this._tag == Tag.UPLOAD_API_RATE_LIMIT;
    }

    public static FeatureValue uploadApiRateLimit(UploadApiRateLimitValue uploadApiRateLimitValue2) {
        if (uploadApiRateLimitValue2 != null) {
            return new FeatureValue().withTagAndUploadApiRateLimit(Tag.UPLOAD_API_RATE_LIMIT, uploadApiRateLimitValue2);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UploadApiRateLimitValue getUploadApiRateLimitValue() {
        if (this._tag == Tag.UPLOAD_API_RATE_LIMIT) {
            return this.uploadApiRateLimitValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.UPLOAD_API_RATE_LIMIT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isHasTeamSharedDropbox() {
        return this._tag == Tag.HAS_TEAM_SHARED_DROPBOX;
    }

    public static FeatureValue hasTeamSharedDropbox(HasTeamSharedDropboxValue hasTeamSharedDropboxValue2) {
        if (hasTeamSharedDropboxValue2 != null) {
            return new FeatureValue().withTagAndHasTeamSharedDropbox(Tag.HAS_TEAM_SHARED_DROPBOX, hasTeamSharedDropboxValue2);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public HasTeamSharedDropboxValue getHasTeamSharedDropboxValue() {
        if (this._tag == Tag.HAS_TEAM_SHARED_DROPBOX) {
            return this.hasTeamSharedDropboxValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.HAS_TEAM_SHARED_DROPBOX, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isHasTeamFileEvents() {
        return this._tag == Tag.HAS_TEAM_FILE_EVENTS;
    }

    public static FeatureValue hasTeamFileEvents(HasTeamFileEventsValue hasTeamFileEventsValue2) {
        if (hasTeamFileEventsValue2 != null) {
            return new FeatureValue().withTagAndHasTeamFileEvents(Tag.HAS_TEAM_FILE_EVENTS, hasTeamFileEventsValue2);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public HasTeamFileEventsValue getHasTeamFileEventsValue() {
        if (this._tag == Tag.HAS_TEAM_FILE_EVENTS) {
            return this.hasTeamFileEventsValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.HAS_TEAM_FILE_EVENTS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isHasTeamSelectiveSync() {
        return this._tag == Tag.HAS_TEAM_SELECTIVE_SYNC;
    }

    public static FeatureValue hasTeamSelectiveSync(HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue2) {
        if (hasTeamSelectiveSyncValue2 != null) {
            return new FeatureValue().withTagAndHasTeamSelectiveSync(Tag.HAS_TEAM_SELECTIVE_SYNC, hasTeamSelectiveSyncValue2);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public HasTeamSelectiveSyncValue getHasTeamSelectiveSyncValue() {
        if (this._tag == Tag.HAS_TEAM_SELECTIVE_SYNC) {
            return this.hasTeamSelectiveSyncValue;
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
        return Arrays.hashCode(new Object[]{this._tag, this.uploadApiRateLimitValue, this.hasTeamSharedDropboxValue, this.hasTeamFileEventsValue, this.hasTeamSelectiveSyncValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof FeatureValue)) {
            return false;
        }
        FeatureValue featureValue = (FeatureValue) obj;
        if (this._tag != featureValue._tag) {
            return false;
        }
        switch (this._tag) {
            case UPLOAD_API_RATE_LIMIT:
                UploadApiRateLimitValue uploadApiRateLimitValue2 = this.uploadApiRateLimitValue;
                UploadApiRateLimitValue uploadApiRateLimitValue3 = featureValue.uploadApiRateLimitValue;
                if (uploadApiRateLimitValue2 != uploadApiRateLimitValue3 && !uploadApiRateLimitValue2.equals(uploadApiRateLimitValue3)) {
                    z = false;
                }
                return z;
            case HAS_TEAM_SHARED_DROPBOX:
                HasTeamSharedDropboxValue hasTeamSharedDropboxValue2 = this.hasTeamSharedDropboxValue;
                HasTeamSharedDropboxValue hasTeamSharedDropboxValue3 = featureValue.hasTeamSharedDropboxValue;
                if (hasTeamSharedDropboxValue2 != hasTeamSharedDropboxValue3 && !hasTeamSharedDropboxValue2.equals(hasTeamSharedDropboxValue3)) {
                    z = false;
                }
                return z;
            case HAS_TEAM_FILE_EVENTS:
                HasTeamFileEventsValue hasTeamFileEventsValue2 = this.hasTeamFileEventsValue;
                HasTeamFileEventsValue hasTeamFileEventsValue3 = featureValue.hasTeamFileEventsValue;
                if (hasTeamFileEventsValue2 != hasTeamFileEventsValue3 && !hasTeamFileEventsValue2.equals(hasTeamFileEventsValue3)) {
                    z = false;
                }
                return z;
            case HAS_TEAM_SELECTIVE_SYNC:
                HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue2 = this.hasTeamSelectiveSyncValue;
                HasTeamSelectiveSyncValue hasTeamSelectiveSyncValue3 = featureValue.hasTeamSelectiveSyncValue;
                if (hasTeamSelectiveSyncValue2 != hasTeamSelectiveSyncValue3 && !hasTeamSelectiveSyncValue2.equals(hasTeamSelectiveSyncValue3)) {
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
