package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxList;
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

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersArg */
class ListFileMembersArg {
    protected final List<MemberAction> actions;
    protected final String file;
    protected final boolean includeInherited;
    protected final long limit;

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersArg$Builder */
    public static class Builder {
        protected List<MemberAction> actions;
        protected final String file;
        protected boolean includeInherited;
        protected long limit;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'file' is null");
            } else if (str.length() < 1) {
                throw new IllegalArgumentException("String 'file' is shorter than 1");
            } else if (Pattern.matches("((/|id:).*|nspath:[0-9]+:.*)|ns:[0-9]+(/.*)?", str)) {
                this.file = str;
                this.actions = null;
                this.includeInherited = true;
                this.limit = 100;
            } else {
                throw new IllegalArgumentException("String 'file' does not match pattern");
            }
        }

        public Builder withActions(List<MemberAction> list) {
            if (list != null) {
                for (MemberAction memberAction : list) {
                    if (memberAction == null) {
                        throw new IllegalArgumentException("An item in list 'actions' is null");
                    }
                }
            }
            this.actions = list;
            return this;
        }

        public Builder withIncludeInherited(Boolean bool) {
            if (bool != null) {
                this.includeInherited = bool.booleanValue();
            } else {
                this.includeInherited = true;
            }
            return this;
        }

        public Builder withLimit(Long l) {
            if (l.longValue() < 1) {
                throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
            } else if (l.longValue() <= 300) {
                if (l != null) {
                    this.limit = l.longValue();
                } else {
                    this.limit = 100;
                }
                return this;
            } else {
                throw new IllegalArgumentException("Number 'limit' is larger than 300L");
            }
        }

        public ListFileMembersArg build() {
            ListFileMembersArg listFileMembersArg = new ListFileMembersArg(this.file, this.actions, this.includeInherited, this.limit);
            return listFileMembersArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersArg$Serializer */
    static class Serializer extends StructSerializer<ListFileMembersArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFileMembersArg listFileMembersArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxFile.TYPE);
            StoneSerializers.string().serialize(listFileMembersArg.file, jsonGenerator);
            if (listFileMembersArg.actions != null) {
                jsonGenerator.writeFieldName("actions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(listFileMembersArg.actions, jsonGenerator);
            }
            jsonGenerator.writeFieldName("include_inherited");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listFileMembersArg.includeInherited), jsonGenerator);
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(listFileMembersArg.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFileMembersArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(true);
                Long valueOf2 = Long.valueOf(100);
                String str2 = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxFile.TYPE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("actions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("include_inherited".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf2 = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ListFileMembersArg listFileMembersArg = new ListFileMembersArg(str2, list, valueOf.booleanValue(), valueOf2.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFileMembersArg, listFileMembersArg.toStringMultiline());
                    return listFileMembersArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"file\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListFileMembersArg(String str, List<MemberAction> list, boolean z, long j) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'file' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'file' is shorter than 1");
        } else if (Pattern.matches("((/|id:).*|nspath:[0-9]+:.*)|ns:[0-9]+(/.*)?", str)) {
            this.file = str;
            if (list != null) {
                for (MemberAction memberAction : list) {
                    if (memberAction == null) {
                        throw new IllegalArgumentException("An item in list 'actions' is null");
                    }
                }
            }
            this.actions = list;
            this.includeInherited = z;
            if (j < 1) {
                throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
            } else if (j <= 300) {
                this.limit = j;
            } else {
                throw new IllegalArgumentException("Number 'limit' is larger than 300L");
            }
        } else {
            throw new IllegalArgumentException("String 'file' does not match pattern");
        }
    }

    public ListFileMembersArg(String str) {
        this(str, null, true, 100);
    }

    public String getFile() {
        return this.file;
    }

    public List<MemberAction> getActions() {
        return this.actions;
    }

    public boolean getIncludeInherited() {
        return this.includeInherited;
    }

    public long getLimit() {
        return this.limit;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.file, this.actions, Boolean.valueOf(this.includeInherited), Long.valueOf(this.limit)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r3) == false) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003e, code lost:
        if (r6.limit != r7.limit) goto L_0x0041;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.sharing.ListFileMembersArg r7 = (com.dropbox.core.p005v2.sharing.ListFileMembersArg) r7
            java.lang.String r2 = r6.file
            java.lang.String r3 = r7.file
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            java.util.List<com.dropbox.core.v2.sharing.MemberAction> r2 = r6.actions
            java.util.List<com.dropbox.core.v2.sharing.MemberAction> r3 = r7.actions
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            boolean r2 = r6.includeInherited
            boolean r3 = r7.includeInherited
            if (r2 != r3) goto L_0x0041
            long r2 = r6.limit
            long r4 = r7.limit
            int r7 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r7 != 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.ListFileMembersArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
