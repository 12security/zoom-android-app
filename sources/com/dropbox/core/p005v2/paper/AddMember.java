package com.dropbox.core.p005v2.paper;

import com.dropbox.core.p005v2.sharing.MemberSelector;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.paper.AddMember */
public class AddMember {
    protected final MemberSelector member;
    protected final PaperDocPermissionLevel permissionLevel;

    /* renamed from: com.dropbox.core.v2.paper.AddMember$Serializer */
    static class Serializer extends StructSerializer<AddMember> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AddMember addMember, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("member");
            com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.serialize(addMember.member, jsonGenerator);
            jsonGenerator.writeFieldName("permission_level");
            Serializer.INSTANCE.serialize(addMember.permissionLevel, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AddMember deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            MemberSelector memberSelector = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                PaperDocPermissionLevel paperDocPermissionLevel = PaperDocPermissionLevel.EDIT;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("member".equals(currentName)) {
                        memberSelector = com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("permission_level".equals(currentName)) {
                        paperDocPermissionLevel = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (memberSelector != null) {
                    AddMember addMember = new AddMember(memberSelector, paperDocPermissionLevel);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(addMember, addMember.toStringMultiline());
                    return addMember;
                }
                throw new JsonParseException(jsonParser, "Required field \"member\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public AddMember(MemberSelector memberSelector, PaperDocPermissionLevel paperDocPermissionLevel) {
        if (paperDocPermissionLevel != null) {
            this.permissionLevel = paperDocPermissionLevel;
            if (memberSelector != null) {
                this.member = memberSelector;
                return;
            }
            throw new IllegalArgumentException("Required value for 'member' is null");
        }
        throw new IllegalArgumentException("Required value for 'permissionLevel' is null");
    }

    public AddMember(MemberSelector memberSelector) {
        this(memberSelector, PaperDocPermissionLevel.EDIT);
    }

    public MemberSelector getMember() {
        return this.member;
    }

    public PaperDocPermissionLevel getPermissionLevel() {
        return this.permissionLevel;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.permissionLevel, this.member});
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
            com.dropbox.core.v2.paper.AddMember r5 = (com.dropbox.core.p005v2.paper.AddMember) r5
            com.dropbox.core.v2.sharing.MemberSelector r2 = r4.member
            com.dropbox.core.v2.sharing.MemberSelector r3 = r5.member
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.paper.PaperDocPermissionLevel r2 = r4.permissionLevel
            com.dropbox.core.v2.paper.PaperDocPermissionLevel r5 = r5.permissionLevel
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.AddMember.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
