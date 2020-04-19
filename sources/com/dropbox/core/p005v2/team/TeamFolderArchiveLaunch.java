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

/* renamed from: com.dropbox.core.v2.team.TeamFolderArchiveLaunch */
public final class TeamFolderArchiveLaunch {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;
    /* access modifiers changed from: private */
    public TeamFolderMetadata completeValue;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderArchiveLaunch$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderArchiveLaunch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderArchiveLaunch teamFolderArchiveLaunch, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderArchiveLaunch.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(teamFolderArchiveLaunch.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(teamFolderArchiveLaunch.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamFolderArchiveLaunch.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamFolderArchiveLaunch deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            TeamFolderArchiveLaunch teamFolderArchiveLaunch;
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
                if ("async_job_id".equals(str)) {
                    expectField("async_job_id", jsonParser);
                    teamFolderArchiveLaunch = TeamFolderArchiveLaunch.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    teamFolderArchiveLaunch = TeamFolderArchiveLaunch.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
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
                return teamFolderArchiveLaunch;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderArchiveLaunch$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE
    }

    private TeamFolderArchiveLaunch() {
    }

    private TeamFolderArchiveLaunch withTag(Tag tag) {
        TeamFolderArchiveLaunch teamFolderArchiveLaunch = new TeamFolderArchiveLaunch();
        teamFolderArchiveLaunch._tag = tag;
        return teamFolderArchiveLaunch;
    }

    private TeamFolderArchiveLaunch withTagAndAsyncJobId(Tag tag, String str) {
        TeamFolderArchiveLaunch teamFolderArchiveLaunch = new TeamFolderArchiveLaunch();
        teamFolderArchiveLaunch._tag = tag;
        teamFolderArchiveLaunch.asyncJobIdValue = str;
        return teamFolderArchiveLaunch;
    }

    private TeamFolderArchiveLaunch withTagAndComplete(Tag tag, TeamFolderMetadata teamFolderMetadata) {
        TeamFolderArchiveLaunch teamFolderArchiveLaunch = new TeamFolderArchiveLaunch();
        teamFolderArchiveLaunch._tag = tag;
        teamFolderArchiveLaunch.completeValue = teamFolderMetadata;
        return teamFolderArchiveLaunch;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static TeamFolderArchiveLaunch asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new TeamFolderArchiveLaunch().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
        } else {
            throw new IllegalArgumentException("String is shorter than 1");
        }
    }

    public String getAsyncJobIdValue() {
        if (this._tag == Tag.ASYNC_JOB_ID) {
            return this.asyncJobIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ASYNC_JOB_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isComplete() {
        return this._tag == Tag.COMPLETE;
    }

    public static TeamFolderArchiveLaunch complete(TeamFolderMetadata teamFolderMetadata) {
        if (teamFolderMetadata != null) {
            return new TeamFolderArchiveLaunch().withTagAndComplete(Tag.COMPLETE, teamFolderMetadata);
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

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.asyncJobIdValue, this.completeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TeamFolderArchiveLaunch)) {
            return false;
        }
        TeamFolderArchiveLaunch teamFolderArchiveLaunch = (TeamFolderArchiveLaunch) obj;
        if (this._tag != teamFolderArchiveLaunch._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = teamFolderArchiveLaunch.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
                TeamFolderMetadata teamFolderMetadata = this.completeValue;
                TeamFolderMetadata teamFolderMetadata2 = teamFolderArchiveLaunch.completeValue;
                if (teamFolderMetadata != teamFolderMetadata2 && !teamFolderMetadata.equals(teamFolderMetadata2)) {
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
