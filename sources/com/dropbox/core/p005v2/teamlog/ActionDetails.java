package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.ActionDetails */
public final class ActionDetails {
    public static final ActionDetails OTHER = new ActionDetails().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public MemberRemoveActionType removeActionValue;
    /* access modifiers changed from: private */
    public JoinTeamDetails teamJoinDetailsValue;

    /* renamed from: com.dropbox.core.v2.teamlog.ActionDetails$Serializer */
    static class Serializer extends UnionSerializer<ActionDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ActionDetails actionDetails, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (actionDetails.tag()) {
                case TEAM_JOIN_DETAILS:
                    jsonGenerator.writeStartObject();
                    writeTag("team_join_details", jsonGenerator);
                    Serializer.INSTANCE.serialize(actionDetails.teamJoinDetailsValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case REMOVE_ACTION:
                    jsonGenerator.writeStartObject();
                    writeTag("remove_action", jsonGenerator);
                    jsonGenerator.writeFieldName("remove_action");
                    Serializer.INSTANCE.serialize(actionDetails.removeActionValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ActionDetails deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            ActionDetails actionDetails;
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
                if ("team_join_details".equals(str)) {
                    actionDetails = ActionDetails.teamJoinDetails(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("remove_action".equals(str)) {
                    expectField("remove_action", jsonParser);
                    actionDetails = ActionDetails.removeAction(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    actionDetails = ActionDetails.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return actionDetails;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.ActionDetails$Tag */
    public enum Tag {
        TEAM_JOIN_DETAILS,
        REMOVE_ACTION,
        OTHER
    }

    private ActionDetails() {
    }

    private ActionDetails withTag(Tag tag) {
        ActionDetails actionDetails = new ActionDetails();
        actionDetails._tag = tag;
        return actionDetails;
    }

    private ActionDetails withTagAndTeamJoinDetails(Tag tag, JoinTeamDetails joinTeamDetails) {
        ActionDetails actionDetails = new ActionDetails();
        actionDetails._tag = tag;
        actionDetails.teamJoinDetailsValue = joinTeamDetails;
        return actionDetails;
    }

    private ActionDetails withTagAndRemoveAction(Tag tag, MemberRemoveActionType memberRemoveActionType) {
        ActionDetails actionDetails = new ActionDetails();
        actionDetails._tag = tag;
        actionDetails.removeActionValue = memberRemoveActionType;
        return actionDetails;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isTeamJoinDetails() {
        return this._tag == Tag.TEAM_JOIN_DETAILS;
    }

    public static ActionDetails teamJoinDetails(JoinTeamDetails joinTeamDetails) {
        if (joinTeamDetails != null) {
            return new ActionDetails().withTagAndTeamJoinDetails(Tag.TEAM_JOIN_DETAILS, joinTeamDetails);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public JoinTeamDetails getTeamJoinDetailsValue() {
        if (this._tag == Tag.TEAM_JOIN_DETAILS) {
            return this.teamJoinDetailsValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TEAM_JOIN_DETAILS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isRemoveAction() {
        return this._tag == Tag.REMOVE_ACTION;
    }

    public static ActionDetails removeAction(MemberRemoveActionType memberRemoveActionType) {
        if (memberRemoveActionType != null) {
            return new ActionDetails().withTagAndRemoveAction(Tag.REMOVE_ACTION, memberRemoveActionType);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public MemberRemoveActionType getRemoveActionValue() {
        if (this._tag == Tag.REMOVE_ACTION) {
            return this.removeActionValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.REMOVE_ACTION, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.teamJoinDetailsValue, this.removeActionValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ActionDetails)) {
            return false;
        }
        ActionDetails actionDetails = (ActionDetails) obj;
        if (this._tag != actionDetails._tag) {
            return false;
        }
        switch (this._tag) {
            case TEAM_JOIN_DETAILS:
                JoinTeamDetails joinTeamDetails = this.teamJoinDetailsValue;
                JoinTeamDetails joinTeamDetails2 = actionDetails.teamJoinDetailsValue;
                if (joinTeamDetails != joinTeamDetails2 && !joinTeamDetails.equals(joinTeamDetails2)) {
                    z = false;
                }
                return z;
            case REMOVE_ACTION:
                MemberRemoveActionType memberRemoveActionType = this.removeActionValue;
                MemberRemoveActionType memberRemoveActionType2 = actionDetails.removeActionValue;
                if (memberRemoveActionType != memberRemoveActionType2 && !memberRemoveActionType.equals(memberRemoveActionType2)) {
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
