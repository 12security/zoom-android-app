package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxGroup;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.ParticipantLogInfo */
public final class ParticipantLogInfo {
    public static final ParticipantLogInfo OTHER = new ParticipantLogInfo().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public GroupLogInfo groupValue;
    /* access modifiers changed from: private */
    public UserLogInfo userValue;

    /* renamed from: com.dropbox.core.v2.teamlog.ParticipantLogInfo$Serializer */
    static class Serializer extends UnionSerializer<ParticipantLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ParticipantLogInfo participantLogInfo, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (participantLogInfo.tag()) {
                case USER:
                    jsonGenerator.writeStartObject();
                    writeTag("user", jsonGenerator);
                    jsonGenerator.writeFieldName("user");
                    Serializer.INSTANCE.serialize(participantLogInfo.userValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case GROUP:
                    jsonGenerator.writeStartObject();
                    writeTag(BoxGroup.TYPE, jsonGenerator);
                    Serializer.INSTANCE.serialize(participantLogInfo.groupValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ParticipantLogInfo deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            ParticipantLogInfo participantLogInfo;
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
                if ("user".equals(str)) {
                    expectField("user", jsonParser);
                    participantLogInfo = ParticipantLogInfo.user((UserLogInfo) Serializer.INSTANCE.deserialize(jsonParser));
                } else if (BoxGroup.TYPE.equals(str)) {
                    participantLogInfo = ParticipantLogInfo.group(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    participantLogInfo = ParticipantLogInfo.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return participantLogInfo;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.ParticipantLogInfo$Tag */
    public enum Tag {
        USER,
        GROUP,
        OTHER
    }

    private ParticipantLogInfo() {
    }

    private ParticipantLogInfo withTag(Tag tag) {
        ParticipantLogInfo participantLogInfo = new ParticipantLogInfo();
        participantLogInfo._tag = tag;
        return participantLogInfo;
    }

    private ParticipantLogInfo withTagAndUser(Tag tag, UserLogInfo userLogInfo) {
        ParticipantLogInfo participantLogInfo = new ParticipantLogInfo();
        participantLogInfo._tag = tag;
        participantLogInfo.userValue = userLogInfo;
        return participantLogInfo;
    }

    private ParticipantLogInfo withTagAndGroup(Tag tag, GroupLogInfo groupLogInfo) {
        ParticipantLogInfo participantLogInfo = new ParticipantLogInfo();
        participantLogInfo._tag = tag;
        participantLogInfo.groupValue = groupLogInfo;
        return participantLogInfo;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUser() {
        return this._tag == Tag.USER;
    }

    public static ParticipantLogInfo user(UserLogInfo userLogInfo) {
        if (userLogInfo != null) {
            return new ParticipantLogInfo().withTagAndUser(Tag.USER, userLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UserLogInfo getUserValue() {
        if (this._tag == Tag.USER) {
            return this.userValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isGroup() {
        return this._tag == Tag.GROUP;
    }

    public static ParticipantLogInfo group(GroupLogInfo groupLogInfo) {
        if (groupLogInfo != null) {
            return new ParticipantLogInfo().withTagAndGroup(Tag.GROUP, groupLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public GroupLogInfo getGroupValue() {
        if (this._tag == Tag.GROUP) {
            return this.groupValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.GROUP, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.userValue, this.groupValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ParticipantLogInfo)) {
            return false;
        }
        ParticipantLogInfo participantLogInfo = (ParticipantLogInfo) obj;
        if (this._tag != participantLogInfo._tag) {
            return false;
        }
        switch (this._tag) {
            case USER:
                UserLogInfo userLogInfo = this.userValue;
                UserLogInfo userLogInfo2 = participantLogInfo.userValue;
                if (userLogInfo != userLogInfo2 && !userLogInfo.equals(userLogInfo2)) {
                    z = false;
                }
                return z;
            case GROUP:
                GroupLogInfo groupLogInfo = this.groupValue;
                GroupLogInfo groupLogInfo2 = participantLogInfo.groupValue;
                if (groupLogInfo != groupLogInfo2 && !groupLogInfo.equals(groupLogInfo2)) {
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
