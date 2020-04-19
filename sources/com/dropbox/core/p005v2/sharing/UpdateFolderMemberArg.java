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

/* renamed from: com.dropbox.core.v2.sharing.UpdateFolderMemberArg */
class UpdateFolderMemberArg {
    protected final AccessLevel accessLevel;
    protected final MemberSelector member;
    protected final String sharedFolderId;

    /* renamed from: com.dropbox.core.v2.sharing.UpdateFolderMemberArg$Serializer */
    static class Serializer extends StructSerializer<UpdateFolderMemberArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UpdateFolderMemberArg updateFolderMemberArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(updateFolderMemberArg.sharedFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("member");
            com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.serialize(updateFolderMemberArg.member, jsonGenerator);
            jsonGenerator.writeFieldName("access_level");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(updateFolderMemberArg.accessLevel, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UpdateFolderMemberArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                AccessLevel accessLevel = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("member".equals(currentName)) {
                        memberSelector = com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("access_level".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_folder_id\" missing.");
                } else if (memberSelector == null) {
                    throw new JsonParseException(jsonParser, "Required field \"member\" missing.");
                } else if (accessLevel != null) {
                    UpdateFolderMemberArg updateFolderMemberArg = new UpdateFolderMemberArg(str2, memberSelector, accessLevel);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(updateFolderMemberArg, updateFolderMemberArg.toStringMultiline());
                    return updateFolderMemberArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"access_level\" missing.");
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

    public UpdateFolderMemberArg(String str, MemberSelector memberSelector, AccessLevel accessLevel2) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.sharedFolderId = str;
            if (memberSelector != null) {
                this.member = memberSelector;
                if (accessLevel2 != null) {
                    this.accessLevel = accessLevel2;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'accessLevel' is null");
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

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedFolderId, this.member, this.accessLevel});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r5) == false) goto L_0x003d;
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
            if (r2 == 0) goto L_0x003f
            com.dropbox.core.v2.sharing.UpdateFolderMemberArg r5 = (com.dropbox.core.p005v2.sharing.UpdateFolderMemberArg) r5
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0024:
            com.dropbox.core.v2.sharing.MemberSelector r2 = r4.member
            com.dropbox.core.v2.sharing.MemberSelector r3 = r5.member
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0030:
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessLevel
            com.dropbox.core.v2.sharing.AccessLevel r5 = r5.accessLevel
            if (r2 == r5) goto L_0x003e
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = 0
        L_0x003e:
            return r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.UpdateFolderMemberArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
