package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TeamMemberStatus */
public final class TeamMemberStatus {
    public static final TeamMemberStatus ACTIVE = new TeamMemberStatus().withTag(Tag.ACTIVE);
    public static final TeamMemberStatus INVITED = new TeamMemberStatus().withTag(Tag.INVITED);
    public static final TeamMemberStatus SUSPENDED = new TeamMemberStatus().withTag(Tag.SUSPENDED);
    private Tag _tag;
    /* access modifiers changed from: private */
    public RemovedStatus removedValue;

    /* renamed from: com.dropbox.core.v2.team.TeamMemberStatus$Serializer */
    static class Serializer extends UnionSerializer<TeamMemberStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamMemberStatus teamMemberStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamMemberStatus.tag()) {
                case ACTIVE:
                    jsonGenerator.writeString(ConditionalUserProperty.ACTIVE);
                    return;
                case INVITED:
                    jsonGenerator.writeString("invited");
                    return;
                case SUSPENDED:
                    jsonGenerator.writeString("suspended");
                    return;
                case REMOVED:
                    jsonGenerator.writeStartObject();
                    writeTag("removed", jsonGenerator);
                    Serializer.INSTANCE.serialize(teamMemberStatus.removedValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamMemberStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamMemberStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            TeamMemberStatus teamMemberStatus;
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
                if (ConditionalUserProperty.ACTIVE.equals(str)) {
                    teamMemberStatus = TeamMemberStatus.ACTIVE;
                } else if ("invited".equals(str)) {
                    teamMemberStatus = TeamMemberStatus.INVITED;
                } else if ("suspended".equals(str)) {
                    teamMemberStatus = TeamMemberStatus.SUSPENDED;
                } else if ("removed".equals(str)) {
                    teamMemberStatus = TeamMemberStatus.removed(Serializer.INSTANCE.deserialize(jsonParser, true));
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
                return teamMemberStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamMemberStatus$Tag */
    public enum Tag {
        ACTIVE,
        INVITED,
        SUSPENDED,
        REMOVED
    }

    private TeamMemberStatus() {
    }

    private TeamMemberStatus withTag(Tag tag) {
        TeamMemberStatus teamMemberStatus = new TeamMemberStatus();
        teamMemberStatus._tag = tag;
        return teamMemberStatus;
    }

    private TeamMemberStatus withTagAndRemoved(Tag tag, RemovedStatus removedStatus) {
        TeamMemberStatus teamMemberStatus = new TeamMemberStatus();
        teamMemberStatus._tag = tag;
        teamMemberStatus.removedValue = removedStatus;
        return teamMemberStatus;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isActive() {
        return this._tag == Tag.ACTIVE;
    }

    public boolean isInvited() {
        return this._tag == Tag.INVITED;
    }

    public boolean isSuspended() {
        return this._tag == Tag.SUSPENDED;
    }

    public boolean isRemoved() {
        return this._tag == Tag.REMOVED;
    }

    public static TeamMemberStatus removed(RemovedStatus removedStatus) {
        if (removedStatus != null) {
            return new TeamMemberStatus().withTagAndRemoved(Tag.REMOVED, removedStatus);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RemovedStatus getRemovedValue() {
        if (this._tag == Tag.REMOVED) {
            return this.removedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.REMOVED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.removedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TeamMemberStatus)) {
            return false;
        }
        TeamMemberStatus teamMemberStatus = (TeamMemberStatus) obj;
        if (this._tag != teamMemberStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case ACTIVE:
                return true;
            case INVITED:
                return true;
            case SUSPENDED:
                return true;
            case REMOVED:
                RemovedStatus removedStatus = this.removedValue;
                RemovedStatus removedStatus2 = teamMemberStatus.removedValue;
                if (removedStatus != removedStatus2 && !removedStatus.equals(removedStatus2)) {
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
