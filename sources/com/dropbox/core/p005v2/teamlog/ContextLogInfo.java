package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.ContextLogInfo */
public final class ContextLogInfo {
    public static final ContextLogInfo ANONYMOUS = new ContextLogInfo().withTag(Tag.ANONYMOUS);
    public static final ContextLogInfo OTHER = new ContextLogInfo().withTag(Tag.OTHER);
    public static final ContextLogInfo TEAM = new ContextLogInfo().withTag(Tag.TEAM);
    private Tag _tag;
    /* access modifiers changed from: private */
    public NonTeamMemberLogInfo nonTeamMemberValue;
    /* access modifiers changed from: private */
    public TeamMemberLogInfo teamMemberValue;

    /* renamed from: com.dropbox.core.v2.teamlog.ContextLogInfo$Serializer */
    static class Serializer extends UnionSerializer<ContextLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ContextLogInfo contextLogInfo, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (contextLogInfo.tag()) {
                case TEAM_MEMBER:
                    jsonGenerator.writeStartObject();
                    writeTag("team_member", jsonGenerator);
                    Serializer.INSTANCE.serialize(contextLogInfo.teamMemberValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case NON_TEAM_MEMBER:
                    jsonGenerator.writeStartObject();
                    writeTag("non_team_member", jsonGenerator);
                    Serializer.INSTANCE.serialize(contextLogInfo.nonTeamMemberValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case ANONYMOUS:
                    jsonGenerator.writeString("anonymous");
                    return;
                case TEAM:
                    jsonGenerator.writeString("team");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ContextLogInfo deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            ContextLogInfo contextLogInfo;
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
                if ("team_member".equals(str)) {
                    contextLogInfo = ContextLogInfo.teamMember(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("non_team_member".equals(str)) {
                    contextLogInfo = ContextLogInfo.nonTeamMember(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("anonymous".equals(str)) {
                    contextLogInfo = ContextLogInfo.ANONYMOUS;
                } else if ("team".equals(str)) {
                    contextLogInfo = ContextLogInfo.TEAM;
                } else {
                    contextLogInfo = ContextLogInfo.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return contextLogInfo;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.ContextLogInfo$Tag */
    public enum Tag {
        TEAM_MEMBER,
        NON_TEAM_MEMBER,
        ANONYMOUS,
        TEAM,
        OTHER
    }

    private ContextLogInfo() {
    }

    private ContextLogInfo withTag(Tag tag) {
        ContextLogInfo contextLogInfo = new ContextLogInfo();
        contextLogInfo._tag = tag;
        return contextLogInfo;
    }

    private ContextLogInfo withTagAndTeamMember(Tag tag, TeamMemberLogInfo teamMemberLogInfo) {
        ContextLogInfo contextLogInfo = new ContextLogInfo();
        contextLogInfo._tag = tag;
        contextLogInfo.teamMemberValue = teamMemberLogInfo;
        return contextLogInfo;
    }

    private ContextLogInfo withTagAndNonTeamMember(Tag tag, NonTeamMemberLogInfo nonTeamMemberLogInfo) {
        ContextLogInfo contextLogInfo = new ContextLogInfo();
        contextLogInfo._tag = tag;
        contextLogInfo.nonTeamMemberValue = nonTeamMemberLogInfo;
        return contextLogInfo;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isTeamMember() {
        return this._tag == Tag.TEAM_MEMBER;
    }

    public static ContextLogInfo teamMember(TeamMemberLogInfo teamMemberLogInfo) {
        if (teamMemberLogInfo != null) {
            return new ContextLogInfo().withTagAndTeamMember(Tag.TEAM_MEMBER, teamMemberLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamMemberLogInfo getTeamMemberValue() {
        if (this._tag == Tag.TEAM_MEMBER) {
            return this.teamMemberValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TEAM_MEMBER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isNonTeamMember() {
        return this._tag == Tag.NON_TEAM_MEMBER;
    }

    public static ContextLogInfo nonTeamMember(NonTeamMemberLogInfo nonTeamMemberLogInfo) {
        if (nonTeamMemberLogInfo != null) {
            return new ContextLogInfo().withTagAndNonTeamMember(Tag.NON_TEAM_MEMBER, nonTeamMemberLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public NonTeamMemberLogInfo getNonTeamMemberValue() {
        if (this._tag == Tag.NON_TEAM_MEMBER) {
            return this.nonTeamMemberValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.NON_TEAM_MEMBER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAnonymous() {
        return this._tag == Tag.ANONYMOUS;
    }

    public boolean isTeam() {
        return this._tag == Tag.TEAM;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.teamMemberValue, this.nonTeamMemberValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ContextLogInfo)) {
            return false;
        }
        ContextLogInfo contextLogInfo = (ContextLogInfo) obj;
        if (this._tag != contextLogInfo._tag) {
            return false;
        }
        switch (this._tag) {
            case TEAM_MEMBER:
                TeamMemberLogInfo teamMemberLogInfo = this.teamMemberValue;
                TeamMemberLogInfo teamMemberLogInfo2 = contextLogInfo.teamMemberValue;
                if (teamMemberLogInfo != teamMemberLogInfo2 && !teamMemberLogInfo.equals(teamMemberLogInfo2)) {
                    z = false;
                }
                return z;
            case NON_TEAM_MEMBER:
                NonTeamMemberLogInfo nonTeamMemberLogInfo = this.nonTeamMemberValue;
                NonTeamMemberLogInfo nonTeamMemberLogInfo2 = contextLogInfo.nonTeamMemberValue;
                if (nonTeamMemberLogInfo != nonTeamMemberLogInfo2 && !nonTeamMemberLogInfo.equals(nonTeamMemberLogInfo2)) {
                    z = false;
                }
                return z;
            case ANONYMOUS:
                return true;
            case TEAM:
                return true;
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
