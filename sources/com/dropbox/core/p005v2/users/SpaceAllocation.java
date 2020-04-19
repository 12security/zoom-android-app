package com.dropbox.core.p005v2.users;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.users.SpaceAllocation */
public final class SpaceAllocation {
    public static final SpaceAllocation OTHER = new SpaceAllocation().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public IndividualSpaceAllocation individualValue;
    /* access modifiers changed from: private */
    public TeamSpaceAllocation teamValue;

    /* renamed from: com.dropbox.core.v2.users.SpaceAllocation$Serializer */
    static class Serializer extends UnionSerializer<SpaceAllocation> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SpaceAllocation spaceAllocation, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (spaceAllocation.tag()) {
                case INDIVIDUAL:
                    jsonGenerator.writeStartObject();
                    writeTag("individual", jsonGenerator);
                    Serializer.INSTANCE.serialize(spaceAllocation.individualValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case TEAM:
                    jsonGenerator.writeStartObject();
                    writeTag("team", jsonGenerator);
                    Serializer.INSTANCE.serialize(spaceAllocation.teamValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SpaceAllocation deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            SpaceAllocation spaceAllocation;
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
                if ("individual".equals(str)) {
                    spaceAllocation = SpaceAllocation.individual(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("team".equals(str)) {
                    spaceAllocation = SpaceAllocation.team(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    spaceAllocation = SpaceAllocation.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return spaceAllocation;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.users.SpaceAllocation$Tag */
    public enum Tag {
        INDIVIDUAL,
        TEAM,
        OTHER
    }

    private SpaceAllocation() {
    }

    private SpaceAllocation withTag(Tag tag) {
        SpaceAllocation spaceAllocation = new SpaceAllocation();
        spaceAllocation._tag = tag;
        return spaceAllocation;
    }

    private SpaceAllocation withTagAndIndividual(Tag tag, IndividualSpaceAllocation individualSpaceAllocation) {
        SpaceAllocation spaceAllocation = new SpaceAllocation();
        spaceAllocation._tag = tag;
        spaceAllocation.individualValue = individualSpaceAllocation;
        return spaceAllocation;
    }

    private SpaceAllocation withTagAndTeam(Tag tag, TeamSpaceAllocation teamSpaceAllocation) {
        SpaceAllocation spaceAllocation = new SpaceAllocation();
        spaceAllocation._tag = tag;
        spaceAllocation.teamValue = teamSpaceAllocation;
        return spaceAllocation;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isIndividual() {
        return this._tag == Tag.INDIVIDUAL;
    }

    public static SpaceAllocation individual(IndividualSpaceAllocation individualSpaceAllocation) {
        if (individualSpaceAllocation != null) {
            return new SpaceAllocation().withTagAndIndividual(Tag.INDIVIDUAL, individualSpaceAllocation);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public IndividualSpaceAllocation getIndividualValue() {
        if (this._tag == Tag.INDIVIDUAL) {
            return this.individualValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INDIVIDUAL, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTeam() {
        return this._tag == Tag.TEAM;
    }

    public static SpaceAllocation team(TeamSpaceAllocation teamSpaceAllocation) {
        if (teamSpaceAllocation != null) {
            return new SpaceAllocation().withTagAndTeam(Tag.TEAM, teamSpaceAllocation);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamSpaceAllocation getTeamValue() {
        if (this._tag == Tag.TEAM) {
            return this.teamValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TEAM, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.individualValue, this.teamValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof SpaceAllocation)) {
            return false;
        }
        SpaceAllocation spaceAllocation = (SpaceAllocation) obj;
        if (this._tag != spaceAllocation._tag) {
            return false;
        }
        switch (this._tag) {
            case INDIVIDUAL:
                IndividualSpaceAllocation individualSpaceAllocation = this.individualValue;
                IndividualSpaceAllocation individualSpaceAllocation2 = spaceAllocation.individualValue;
                if (individualSpaceAllocation != individualSpaceAllocation2 && !individualSpaceAllocation.equals(individualSpaceAllocation2)) {
                    z = false;
                }
                return z;
            case TEAM:
                TeamSpaceAllocation teamSpaceAllocation = this.teamValue;
                TeamSpaceAllocation teamSpaceAllocation2 = spaceAllocation.teamValue;
                if (teamSpaceAllocation != teamSpaceAllocation2 && !teamSpaceAllocation.equals(teamSpaceAllocation2)) {
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
