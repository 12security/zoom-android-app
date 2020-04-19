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
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.AddFolderMemberArg */
class AddFolderMemberArg {
    protected final String customMessage;
    protected final List<AddMember> members;
    protected final boolean quiet;
    protected final String sharedFolderId;

    /* renamed from: com.dropbox.core.v2.sharing.AddFolderMemberArg$Builder */
    public static class Builder {
        protected String customMessage;
        protected final List<AddMember> members;
        protected boolean quiet;
        protected final String sharedFolderId;

        protected Builder(String str, List<AddMember> list) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.sharedFolderId = str;
                if (list != null) {
                    for (AddMember addMember : list) {
                        if (addMember == null) {
                            throw new IllegalArgumentException("An item in list 'members' is null");
                        }
                    }
                    this.members = list;
                    this.quiet = false;
                    this.customMessage = null;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'members' is null");
            } else {
                throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
            }
        }

        public Builder withQuiet(Boolean bool) {
            if (bool != null) {
                this.quiet = bool.booleanValue();
            } else {
                this.quiet = false;
            }
            return this;
        }

        public Builder withCustomMessage(String str) {
            if (str == null || str.length() >= 1) {
                this.customMessage = str;
                return this;
            }
            throw new IllegalArgumentException("String 'customMessage' is shorter than 1");
        }

        public AddFolderMemberArg build() {
            return new AddFolderMemberArg(this.sharedFolderId, this.members, this.quiet, this.customMessage);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.AddFolderMemberArg$Serializer */
    static class Serializer extends StructSerializer<AddFolderMemberArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AddFolderMemberArg addFolderMemberArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(addFolderMemberArg.sharedFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("members");
            StoneSerializers.list(Serializer.INSTANCE).serialize(addFolderMemberArg.members, jsonGenerator);
            jsonGenerator.writeFieldName("quiet");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(addFolderMemberArg.quiet), jsonGenerator);
            if (addFolderMemberArg.customMessage != null) {
                jsonGenerator.writeFieldName("custom_message");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(addFolderMemberArg.customMessage, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AddFolderMemberArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                Boolean valueOf = Boolean.valueOf(false);
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("members".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("quiet".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("custom_message".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_folder_id\" missing.");
                } else if (list != null) {
                    AddFolderMemberArg addFolderMemberArg = new AddFolderMemberArg(str2, list, valueOf.booleanValue(), str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(addFolderMemberArg, addFolderMemberArg.toStringMultiline());
                    return addFolderMemberArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"members\" missing.");
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

    public AddFolderMemberArg(String str, List<AddMember> list, boolean z, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.sharedFolderId = str;
            if (list != null) {
                for (AddMember addMember : list) {
                    if (addMember == null) {
                        throw new IllegalArgumentException("An item in list 'members' is null");
                    }
                }
                this.members = list;
                this.quiet = z;
                if (str2 == null || str2.length() >= 1) {
                    this.customMessage = str2;
                    return;
                }
                throw new IllegalArgumentException("String 'customMessage' is shorter than 1");
            }
            throw new IllegalArgumentException("Required value for 'members' is null");
        } else {
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        }
    }

    public AddFolderMemberArg(String str, List<AddMember> list) {
        this(str, list, false, null);
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public List<AddMember> getMembers() {
        return this.members;
    }

    public boolean getQuiet() {
        return this.quiet;
    }

    public String getCustomMessage() {
        return this.customMessage;
    }

    public static Builder newBuilder(String str, List<AddMember> list) {
        return new Builder(str, list);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedFolderId, this.members, Boolean.valueOf(this.quiet), this.customMessage});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0042, code lost:
        if (r2.equals(r5) == false) goto L_0x0045;
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
            if (r2 == 0) goto L_0x0047
            com.dropbox.core.v2.sharing.AddFolderMemberArg r5 = (com.dropbox.core.p005v2.sharing.AddFolderMemberArg) r5
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0024:
            java.util.List<com.dropbox.core.v2.sharing.AddMember> r2 = r4.members
            java.util.List<com.dropbox.core.v2.sharing.AddMember> r3 = r5.members
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0030:
            boolean r2 = r4.quiet
            boolean r3 = r5.quiet
            if (r2 != r3) goto L_0x0045
            java.lang.String r2 = r4.customMessage
            java.lang.String r5 = r5.customMessage
            if (r2 == r5) goto L_0x0046
            if (r2 == 0) goto L_0x0045
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            r0 = 0
        L_0x0046:
            return r0
        L_0x0047:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.AddFolderMemberArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
