package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFile;
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

/* renamed from: com.dropbox.core.v2.sharing.AddFileMemberArgs */
class AddFileMemberArgs {
    protected final AccessLevel accessLevel;
    protected final boolean addMessageAsComment;
    protected final String customMessage;
    protected final String file;
    protected final List<MemberSelector> members;
    protected final boolean quiet;

    /* renamed from: com.dropbox.core.v2.sharing.AddFileMemberArgs$Builder */
    public static class Builder {
        protected AccessLevel accessLevel;
        protected boolean addMessageAsComment;
        protected String customMessage;
        protected final String file;
        protected final List<MemberSelector> members;
        protected boolean quiet;

        protected Builder(String str, List<MemberSelector> list) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'file' is null");
            } else if (str.length() < 1) {
                throw new IllegalArgumentException("String 'file' is shorter than 1");
            } else if (Pattern.matches("((/|id:).*|nspath:[0-9]+:.*)|ns:[0-9]+(/.*)?", str)) {
                this.file = str;
                if (list != null) {
                    for (MemberSelector memberSelector : list) {
                        if (memberSelector == null) {
                            throw new IllegalArgumentException("An item in list 'members' is null");
                        }
                    }
                    this.members = list;
                    this.customMessage = null;
                    this.quiet = false;
                    this.accessLevel = AccessLevel.VIEWER;
                    this.addMessageAsComment = false;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'members' is null");
            } else {
                throw new IllegalArgumentException("String 'file' does not match pattern");
            }
        }

        public Builder withCustomMessage(String str) {
            this.customMessage = str;
            return this;
        }

        public Builder withQuiet(Boolean bool) {
            if (bool != null) {
                this.quiet = bool.booleanValue();
            } else {
                this.quiet = false;
            }
            return this;
        }

        public Builder withAccessLevel(AccessLevel accessLevel2) {
            if (accessLevel2 != null) {
                this.accessLevel = accessLevel2;
            } else {
                this.accessLevel = AccessLevel.VIEWER;
            }
            return this;
        }

        public Builder withAddMessageAsComment(Boolean bool) {
            if (bool != null) {
                this.addMessageAsComment = bool.booleanValue();
            } else {
                this.addMessageAsComment = false;
            }
            return this;
        }

        public AddFileMemberArgs build() {
            AddFileMemberArgs addFileMemberArgs = new AddFileMemberArgs(this.file, this.members, this.customMessage, this.quiet, this.accessLevel, this.addMessageAsComment);
            return addFileMemberArgs;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.AddFileMemberArgs$Serializer */
    static class Serializer extends StructSerializer<AddFileMemberArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AddFileMemberArgs addFileMemberArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxFile.TYPE);
            StoneSerializers.string().serialize(addFileMemberArgs.file, jsonGenerator);
            jsonGenerator.writeFieldName("members");
            StoneSerializers.list(com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE).serialize(addFileMemberArgs.members, jsonGenerator);
            if (addFileMemberArgs.customMessage != null) {
                jsonGenerator.writeFieldName("custom_message");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(addFileMemberArgs.customMessage, jsonGenerator);
            }
            jsonGenerator.writeFieldName("quiet");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(addFileMemberArgs.quiet), jsonGenerator);
            jsonGenerator.writeFieldName("access_level");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(addFileMemberArgs.accessLevel, jsonGenerator);
            jsonGenerator.writeFieldName("add_message_as_comment");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(addFileMemberArgs.addMessageAsComment), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AddFileMemberArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                AccessLevel accessLevel = AccessLevel.VIEWER;
                Boolean valueOf2 = Boolean.valueOf(false);
                String str2 = null;
                List list = null;
                String str3 = null;
                AccessLevel accessLevel2 = accessLevel;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxFile.TYPE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("members".equals(currentName)) {
                        list = (List) StoneSerializers.list(com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("custom_message".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("quiet".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("access_level".equals(currentName)) {
                        accessLevel2 = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("add_message_as_comment".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"file\" missing.");
                } else if (list != null) {
                    AddFileMemberArgs addFileMemberArgs = new AddFileMemberArgs(str2, list, str3, valueOf.booleanValue(), accessLevel2, valueOf2.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(addFileMemberArgs, addFileMemberArgs.toStringMultiline());
                    return addFileMemberArgs;
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

    public AddFileMemberArgs(String str, List<MemberSelector> list, String str2, boolean z, AccessLevel accessLevel2, boolean z2) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'file' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'file' is shorter than 1");
        } else if (Pattern.matches("((/|id:).*|nspath:[0-9]+:.*)|ns:[0-9]+(/.*)?", str)) {
            this.file = str;
            if (list != null) {
                for (MemberSelector memberSelector : list) {
                    if (memberSelector == null) {
                        throw new IllegalArgumentException("An item in list 'members' is null");
                    }
                }
                this.members = list;
                this.customMessage = str2;
                this.quiet = z;
                if (accessLevel2 != null) {
                    this.accessLevel = accessLevel2;
                    this.addMessageAsComment = z2;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'accessLevel' is null");
            }
            throw new IllegalArgumentException("Required value for 'members' is null");
        } else {
            throw new IllegalArgumentException("String 'file' does not match pattern");
        }
    }

    public AddFileMemberArgs(String str, List<MemberSelector> list) {
        this(str, list, null, false, AccessLevel.VIEWER, false);
    }

    public String getFile() {
        return this.file;
    }

    public List<MemberSelector> getMembers() {
        return this.members;
    }

    public String getCustomMessage() {
        return this.customMessage;
    }

    public boolean getQuiet() {
        return this.quiet;
    }

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public boolean getAddMessageAsComment() {
        return this.addMessageAsComment;
    }

    public static Builder newBuilder(String str, List<MemberSelector> list) {
        return new Builder(str, list);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.file, this.members, this.customMessage, Boolean.valueOf(this.quiet), this.accessLevel, Boolean.valueOf(this.addMessageAsComment)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004e, code lost:
        if (r2.equals(r3) == false) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0054, code lost:
        if (r4.addMessageAsComment != r5.addMessageAsComment) goto L_0x0057;
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
            if (r2 == 0) goto L_0x0059
            com.dropbox.core.v2.sharing.AddFileMemberArgs r5 = (com.dropbox.core.p005v2.sharing.AddFileMemberArgs) r5
            java.lang.String r2 = r4.file
            java.lang.String r3 = r5.file
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0057
        L_0x0024:
            java.util.List<com.dropbox.core.v2.sharing.MemberSelector> r2 = r4.members
            java.util.List<com.dropbox.core.v2.sharing.MemberSelector> r3 = r5.members
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0057
        L_0x0030:
            java.lang.String r2 = r4.customMessage
            java.lang.String r3 = r5.customMessage
            if (r2 == r3) goto L_0x003e
            if (r2 == 0) goto L_0x0057
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0057
        L_0x003e:
            boolean r2 = r4.quiet
            boolean r3 = r5.quiet
            if (r2 != r3) goto L_0x0057
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessLevel
            if (r2 == r3) goto L_0x0050
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0057
        L_0x0050:
            boolean r2 = r4.addMessageAsComment
            boolean r5 = r5.addMessageAsComment
            if (r2 != r5) goto L_0x0057
            goto L_0x0058
        L_0x0057:
            r0 = 0
        L_0x0058:
            return r0
        L_0x0059:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.AddFileMemberArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
