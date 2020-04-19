package com.dropbox.core.p005v2.paper;

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

/* renamed from: com.dropbox.core.v2.paper.AddPaperDocUser */
class AddPaperDocUser extends RefPaperDoc {
    protected final String customMessage;
    protected final List<AddMember> members;
    protected final boolean quiet;

    /* renamed from: com.dropbox.core.v2.paper.AddPaperDocUser$Builder */
    public static class Builder {
        protected String customMessage;
        protected final String docId;
        protected final List<AddMember> members;
        protected boolean quiet;

        protected Builder(String str, List<AddMember> list) {
            if (str != null) {
                this.docId = str;
                if (list == null) {
                    throw new IllegalArgumentException("Required value for 'members' is null");
                } else if (list.size() <= 20) {
                    for (AddMember addMember : list) {
                        if (addMember == null) {
                            throw new IllegalArgumentException("An item in list 'members' is null");
                        }
                    }
                    this.members = list;
                    this.customMessage = null;
                    this.quiet = false;
                } else {
                    throw new IllegalArgumentException("List 'members' has more than 20 items");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'docId' is null");
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

        public AddPaperDocUser build() {
            return new AddPaperDocUser(this.docId, this.members, this.customMessage, this.quiet);
        }
    }

    /* renamed from: com.dropbox.core.v2.paper.AddPaperDocUser$Serializer */
    static class Serializer extends StructSerializer<AddPaperDocUser> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AddPaperDocUser addPaperDocUser, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("doc_id");
            StoneSerializers.string().serialize(addPaperDocUser.docId, jsonGenerator);
            jsonGenerator.writeFieldName("members");
            StoneSerializers.list(Serializer.INSTANCE).serialize(addPaperDocUser.members, jsonGenerator);
            if (addPaperDocUser.customMessage != null) {
                jsonGenerator.writeFieldName("custom_message");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(addPaperDocUser.customMessage, jsonGenerator);
            }
            jsonGenerator.writeFieldName("quiet");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(addPaperDocUser.quiet), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AddPaperDocUser deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("doc_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("members".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("custom_message".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("quiet".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"doc_id\" missing.");
                } else if (list != null) {
                    AddPaperDocUser addPaperDocUser = new AddPaperDocUser(str2, list, str3, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(addPaperDocUser, addPaperDocUser.toStringMultiline());
                    return addPaperDocUser;
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

    public AddPaperDocUser(String str, List<AddMember> list, String str2, boolean z) {
        super(str);
        if (list == null) {
            throw new IllegalArgumentException("Required value for 'members' is null");
        } else if (list.size() <= 20) {
            for (AddMember addMember : list) {
                if (addMember == null) {
                    throw new IllegalArgumentException("An item in list 'members' is null");
                }
            }
            this.members = list;
            this.customMessage = str2;
            this.quiet = z;
        } else {
            throw new IllegalArgumentException("List 'members' has more than 20 items");
        }
    }

    public AddPaperDocUser(String str, List<AddMember> list) {
        this(str, list, null, false);
    }

    public String getDocId() {
        return this.docId;
    }

    public List<AddMember> getMembers() {
        return this.members;
    }

    public String getCustomMessage() {
        return this.customMessage;
    }

    public boolean getQuiet() {
        return this.quiet;
    }

    public static Builder newBuilder(String str, List<AddMember> list) {
        return new Builder(str, list);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.members, this.customMessage, Boolean.valueOf(this.quiet)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0040, code lost:
        if (r2.equals(r3) == false) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0046, code lost:
        if (r4.quiet != r5.quiet) goto L_0x0049;
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
            if (r2 == 0) goto L_0x004b
            com.dropbox.core.v2.paper.AddPaperDocUser r5 = (com.dropbox.core.p005v2.paper.AddPaperDocUser) r5
            java.lang.String r2 = r4.docId
            java.lang.String r3 = r5.docId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.docId
            java.lang.String r3 = r5.docId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0028:
            java.util.List<com.dropbox.core.v2.paper.AddMember> r2 = r4.members
            java.util.List<com.dropbox.core.v2.paper.AddMember> r3 = r5.members
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0034:
            java.lang.String r2 = r4.customMessage
            java.lang.String r3 = r5.customMessage
            if (r2 == r3) goto L_0x0042
            if (r2 == 0) goto L_0x0049
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0042:
            boolean r2 = r4.quiet
            boolean r5 = r5.quiet
            if (r2 != r5) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r0 = 0
        L_0x004a:
            return r0
        L_0x004b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.AddPaperDocUser.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
