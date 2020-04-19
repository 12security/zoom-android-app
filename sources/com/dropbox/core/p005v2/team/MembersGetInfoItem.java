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

/* renamed from: com.dropbox.core.v2.team.MembersGetInfoItem */
public final class MembersGetInfoItem {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String idNotFoundValue;
    /* access modifiers changed from: private */
    public TeamMemberInfo memberInfoValue;

    /* renamed from: com.dropbox.core.v2.team.MembersGetInfoItem$Serializer */
    static class Serializer extends UnionSerializer<MembersGetInfoItem> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersGetInfoItem membersGetInfoItem, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersGetInfoItem.tag()) {
                case ID_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("id_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("id_not_found");
                    StoneSerializers.string().serialize(membersGetInfoItem.idNotFoundValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case MEMBER_INFO:
                    jsonGenerator.writeStartObject();
                    writeTag("member_info", jsonGenerator);
                    Serializer.INSTANCE.serialize(membersGetInfoItem.memberInfoValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(membersGetInfoItem.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MembersGetInfoItem deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            MembersGetInfoItem membersGetInfoItem;
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
                if ("id_not_found".equals(str)) {
                    expectField("id_not_found", jsonParser);
                    membersGetInfoItem = MembersGetInfoItem.idNotFound((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("member_info".equals(str)) {
                    membersGetInfoItem = MembersGetInfoItem.memberInfo(Serializer.INSTANCE.deserialize(jsonParser, true));
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
                return membersGetInfoItem;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersGetInfoItem$Tag */
    public enum Tag {
        ID_NOT_FOUND,
        MEMBER_INFO
    }

    private MembersGetInfoItem() {
    }

    private MembersGetInfoItem withTag(Tag tag) {
        MembersGetInfoItem membersGetInfoItem = new MembersGetInfoItem();
        membersGetInfoItem._tag = tag;
        return membersGetInfoItem;
    }

    private MembersGetInfoItem withTagAndIdNotFound(Tag tag, String str) {
        MembersGetInfoItem membersGetInfoItem = new MembersGetInfoItem();
        membersGetInfoItem._tag = tag;
        membersGetInfoItem.idNotFoundValue = str;
        return membersGetInfoItem;
    }

    private MembersGetInfoItem withTagAndMemberInfo(Tag tag, TeamMemberInfo teamMemberInfo) {
        MembersGetInfoItem membersGetInfoItem = new MembersGetInfoItem();
        membersGetInfoItem._tag = tag;
        membersGetInfoItem.memberInfoValue = teamMemberInfo;
        return membersGetInfoItem;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isIdNotFound() {
        return this._tag == Tag.ID_NOT_FOUND;
    }

    public static MembersGetInfoItem idNotFound(String str) {
        if (str != null) {
            return new MembersGetInfoItem().withTagAndIdNotFound(Tag.ID_NOT_FOUND, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getIdNotFoundValue() {
        if (this._tag == Tag.ID_NOT_FOUND) {
            return this.idNotFoundValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ID_NOT_FOUND, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isMemberInfo() {
        return this._tag == Tag.MEMBER_INFO;
    }

    public static MembersGetInfoItem memberInfo(TeamMemberInfo teamMemberInfo) {
        if (teamMemberInfo != null) {
            return new MembersGetInfoItem().withTagAndMemberInfo(Tag.MEMBER_INFO, teamMemberInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamMemberInfo getMemberInfoValue() {
        if (this._tag == Tag.MEMBER_INFO) {
            return this.memberInfoValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.MEMBER_INFO, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.idNotFoundValue, this.memberInfoValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof MembersGetInfoItem)) {
            return false;
        }
        MembersGetInfoItem membersGetInfoItem = (MembersGetInfoItem) obj;
        if (this._tag != membersGetInfoItem._tag) {
            return false;
        }
        switch (this._tag) {
            case ID_NOT_FOUND:
                String str = this.idNotFoundValue;
                String str2 = membersGetInfoItem.idNotFoundValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case MEMBER_INFO:
                TeamMemberInfo teamMemberInfo = this.memberInfoValue;
                TeamMemberInfo teamMemberInfo2 = membersGetInfoItem.memberInfoValue;
                if (teamMemberInfo != teamMemberInfo2 && !teamMemberInfo.equals(teamMemberInfo2)) {
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
