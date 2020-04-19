package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.RemoveFolderMemberArg */
class RemoveFolderMemberArg {
    protected final boolean leaveACopy;
    protected final MemberSelector member;
    protected final String sharedFolderId;

    /* renamed from: com.dropbox.core.v2.sharing.RemoveFolderMemberArg$Serializer */
    static class Serializer extends StructSerializer<RemoveFolderMemberArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RemoveFolderMemberArg removeFolderMemberArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(removeFolderMemberArg.sharedFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("member");
            com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.serialize(removeFolderMemberArg.member, jsonGenerator);
            jsonGenerator.writeFieldName("leave_a_copy");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(removeFolderMemberArg.leaveACopy), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RemoveFolderMemberArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                MemberSelector memberSelector = null;
                Boolean bool = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("member".equals(currentName)) {
                        memberSelector = com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("leave_a_copy".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_folder_id\" missing.");
                } else if (memberSelector == null) {
                    throw new JsonParseException(jsonParser, "Required field \"member\" missing.");
                } else if (bool != null) {
                    RemoveFolderMemberArg removeFolderMemberArg = new RemoveFolderMemberArg(str2, memberSelector, bool.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(removeFolderMemberArg, removeFolderMemberArg.toStringMultiline());
                    return removeFolderMemberArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"leave_a_copy\" missing.");
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

    public RemoveFolderMemberArg(String str, MemberSelector memberSelector, boolean z) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.sharedFolderId = str;
            if (memberSelector != null) {
                this.member = memberSelector;
                this.leaveACopy = z;
                return;
            }
            throw new IllegalArgumentException("Required value for 'member' is null");
        } else {
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        }
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public MemberSelector getMember() {
        return this.member;
    }

    public boolean getLeaveACopy() {
        return this.leaveACopy;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedFolderId, this.member, Boolean.valueOf(this.leaveACopy)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r3) == false) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        if (r4.leaveACopy != r5.leaveACopy) goto L_0x0037;
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
            if (r2 == 0) goto L_0x0039
            com.dropbox.core.v2.sharing.RemoveFolderMemberArg r5 = (com.dropbox.core.p005v2.sharing.RemoveFolderMemberArg) r5
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0024:
            com.dropbox.core.v2.sharing.MemberSelector r2 = r4.member
            com.dropbox.core.v2.sharing.MemberSelector r3 = r5.member
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0030:
            boolean r2 = r4.leaveACopy
            boolean r5 = r5.leaveACopy
            if (r2 != r5) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r0 = 0
        L_0x0038:
            return r0
        L_0x0039:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.RemoveFolderMemberArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
