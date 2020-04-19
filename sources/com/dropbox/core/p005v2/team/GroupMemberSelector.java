package com.dropbox.core.p005v2.team;

import com.box.androidsdk.content.models.BoxGroup;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.GroupMemberSelector */
class GroupMemberSelector {
    protected final GroupSelector group;
    protected final UserSelectorArg user;

    /* renamed from: com.dropbox.core.v2.team.GroupMemberSelector$Serializer */
    private static class Serializer extends StructSerializer<GroupMemberSelector> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(GroupMemberSelector groupMemberSelector, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxGroup.TYPE);
            Serializer.INSTANCE.serialize(groupMemberSelector.group, jsonGenerator);
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(groupMemberSelector.user, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupMemberSelector deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            GroupSelector groupSelector = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                UserSelectorArg userSelectorArg = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxGroup.TYPE.equals(currentName)) {
                        groupSelector = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (groupSelector == null) {
                    throw new JsonParseException(jsonParser, "Required field \"group\" missing.");
                } else if (userSelectorArg != null) {
                    GroupMemberSelector groupMemberSelector = new GroupMemberSelector(groupSelector, userSelectorArg);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupMemberSelector, groupMemberSelector.toStringMultiline());
                    return groupMemberSelector;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public GroupMemberSelector(GroupSelector groupSelector, UserSelectorArg userSelectorArg) {
        if (groupSelector != null) {
            this.group = groupSelector;
            if (userSelectorArg != null) {
                this.user = userSelectorArg;
                return;
            }
            throw new IllegalArgumentException("Required value for 'user' is null");
        }
        throw new IllegalArgumentException("Required value for 'group' is null");
    }

    public GroupSelector getGroup() {
        return this.group;
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.group, this.user});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.team.GroupMemberSelector r5 = (com.dropbox.core.p005v2.team.GroupMemberSelector) r5
            com.dropbox.core.v2.team.GroupSelector r2 = r4.group
            com.dropbox.core.v2.team.GroupSelector r3 = r5.group
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r5 = r5.user
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupMemberSelector.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
