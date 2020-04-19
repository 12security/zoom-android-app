package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TeamFolderArchiveJobStatus */
public final class TeamFolderArchiveJobStatus {
    public static final TeamFolderArchiveJobStatus IN_PROGRESS = new TeamFolderArchiveJobStatus().withTag(Tag.IN_PROGRESS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public TeamFolderMetadata completeValue;
    /* access modifiers changed from: private */
    public TeamFolderArchiveError failedValue;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderArchiveJobStatus$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderArchiveJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderArchiveJobStatus teamFolderArchiveJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderArchiveJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(teamFolderArchiveJobStatus.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    Serializer.INSTANCE.serialize(teamFolderArchiveJobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamFolderArchiveJobStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamFolderArchiveJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            TeamFolderArchiveJobStatus teamFolderArchiveJobStatus;
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
                if ("in_progress".equals(str)) {
                    teamFolderArchiveJobStatus = TeamFolderArchiveJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    teamFolderArchiveJobStatus = TeamFolderArchiveJobStatus.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    teamFolderArchiveJobStatus = TeamFolderArchiveJobStatus.failed(Serializer.INSTANCE.deserialize(jsonParser));
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
                return teamFolderArchiveJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderArchiveJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED
    }

    private TeamFolderArchiveJobStatus() {
    }

    private TeamFolderArchiveJobStatus withTag(Tag tag) {
        TeamFolderArchiveJobStatus teamFolderArchiveJobStatus = new TeamFolderArchiveJobStatus();
        teamFolderArchiveJobStatus._tag = tag;
        return teamFolderArchiveJobStatus;
    }

    private TeamFolderArchiveJobStatus withTagAndComplete(Tag tag, TeamFolderMetadata teamFolderMetadata) {
        TeamFolderArchiveJobStatus teamFolderArchiveJobStatus = new TeamFolderArchiveJobStatus();
        teamFolderArchiveJobStatus._tag = tag;
        teamFolderArchiveJobStatus.completeValue = teamFolderMetadata;
        return teamFolderArchiveJobStatus;
    }

    private TeamFolderArchiveJobStatus withTagAndFailed(Tag tag, TeamFolderArchiveError teamFolderArchiveError) {
        TeamFolderArchiveJobStatus teamFolderArchiveJobStatus = new TeamFolderArchiveJobStatus();
        teamFolderArchiveJobStatus._tag = tag;
        teamFolderArchiveJobStatus.failedValue = teamFolderArchiveError;
        return teamFolderArchiveJobStatus;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isInProgress() {
        return this._tag == Tag.IN_PROGRESS;
    }

    public boolean isComplete() {
        return this._tag == Tag.COMPLETE;
    }

    public static TeamFolderArchiveJobStatus complete(TeamFolderMetadata teamFolderMetadata) {
        if (teamFolderMetadata != null) {
            return new TeamFolderArchiveJobStatus().withTagAndComplete(Tag.COMPLETE, teamFolderMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamFolderMetadata getCompleteValue() {
        if (this._tag == Tag.COMPLETE) {
            return this.completeValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.COMPLETE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isFailed() {
        return this._tag == Tag.FAILED;
    }

    public static TeamFolderArchiveJobStatus failed(TeamFolderArchiveError teamFolderArchiveError) {
        if (teamFolderArchiveError != null) {
            return new TeamFolderArchiveJobStatus().withTagAndFailed(Tag.FAILED, teamFolderArchiveError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamFolderArchiveError getFailedValue() {
        if (this._tag == Tag.FAILED) {
            return this.failedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FAILED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.completeValue, this.failedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TeamFolderArchiveJobStatus)) {
            return false;
        }
        TeamFolderArchiveJobStatus teamFolderArchiveJobStatus = (TeamFolderArchiveJobStatus) obj;
        if (this._tag != teamFolderArchiveJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                TeamFolderMetadata teamFolderMetadata = this.completeValue;
                TeamFolderMetadata teamFolderMetadata2 = teamFolderArchiveJobStatus.completeValue;
                if (teamFolderMetadata != teamFolderMetadata2 && !teamFolderMetadata.equals(teamFolderMetadata2)) {
                    z = false;
                }
                return z;
            case FAILED:
                TeamFolderArchiveError teamFolderArchiveError = this.failedValue;
                TeamFolderArchiveError teamFolderArchiveError2 = teamFolderArchiveJobStatus.failedValue;
                if (teamFolderArchiveError != teamFolderArchiveError2 && !teamFolderArchiveError.equals(teamFolderArchiveError2)) {
                    z = false;
                }
                return z;
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
