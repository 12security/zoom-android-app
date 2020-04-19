package com.dropbox.core.p005v2.paper;

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

/* renamed from: com.dropbox.core.v2.paper.ListUsersOnPaperDocArgs */
class ListUsersOnPaperDocArgs extends RefPaperDoc {
    protected final UserOnPaperDocFilter filterBy;
    protected final int limit;

    /* renamed from: com.dropbox.core.v2.paper.ListUsersOnPaperDocArgs$Builder */
    public static class Builder {
        protected final String docId;
        protected UserOnPaperDocFilter filterBy;
        protected int limit;

        protected Builder(String str) {
            if (str != null) {
                this.docId = str;
                this.limit = 1000;
                this.filterBy = UserOnPaperDocFilter.SHARED;
                return;
            }
            throw new IllegalArgumentException("Required value for 'docId' is null");
        }

        public Builder withLimit(Integer num) {
            if (num.intValue() < 1) {
                throw new IllegalArgumentException("Number 'limit' is smaller than 1");
            } else if (num.intValue() <= 1000) {
                if (num != null) {
                    this.limit = num.intValue();
                } else {
                    this.limit = 1000;
                }
                return this;
            } else {
                throw new IllegalArgumentException("Number 'limit' is larger than 1000");
            }
        }

        public Builder withFilterBy(UserOnPaperDocFilter userOnPaperDocFilter) {
            if (userOnPaperDocFilter != null) {
                this.filterBy = userOnPaperDocFilter;
            } else {
                this.filterBy = UserOnPaperDocFilter.SHARED;
            }
            return this;
        }

        public ListUsersOnPaperDocArgs build() {
            return new ListUsersOnPaperDocArgs(this.docId, this.limit, this.filterBy);
        }
    }

    /* renamed from: com.dropbox.core.v2.paper.ListUsersOnPaperDocArgs$Serializer */
    static class Serializer extends StructSerializer<ListUsersOnPaperDocArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListUsersOnPaperDocArgs listUsersOnPaperDocArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("doc_id");
            StoneSerializers.string().serialize(listUsersOnPaperDocArgs.docId, jsonGenerator);
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.int32().serialize(Integer.valueOf(listUsersOnPaperDocArgs.limit), jsonGenerator);
            jsonGenerator.writeFieldName("filter_by");
            Serializer.INSTANCE.serialize(listUsersOnPaperDocArgs.filterBy, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListUsersOnPaperDocArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Integer valueOf = Integer.valueOf(1000);
                UserOnPaperDocFilter userOnPaperDocFilter = UserOnPaperDocFilter.SHARED;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("doc_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Integer) StoneSerializers.int32().deserialize(jsonParser);
                    } else if ("filter_by".equals(currentName)) {
                        userOnPaperDocFilter = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ListUsersOnPaperDocArgs listUsersOnPaperDocArgs = new ListUsersOnPaperDocArgs(str2, valueOf.intValue(), userOnPaperDocFilter);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listUsersOnPaperDocArgs, listUsersOnPaperDocArgs.toStringMultiline());
                    return listUsersOnPaperDocArgs;
                }
                throw new JsonParseException(jsonParser, "Required field \"doc_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListUsersOnPaperDocArgs(String str, int i, UserOnPaperDocFilter userOnPaperDocFilter) {
        super(str);
        if (i < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1");
        } else if (i <= 1000) {
            this.limit = i;
            if (userOnPaperDocFilter != null) {
                this.filterBy = userOnPaperDocFilter;
                return;
            }
            throw new IllegalArgumentException("Required value for 'filterBy' is null");
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000");
        }
    }

    public ListUsersOnPaperDocArgs(String str) {
        this(str, 1000, UserOnPaperDocFilter.SHARED);
    }

    public String getDocId() {
        return this.docId;
    }

    public int getLimit() {
        return this.limit;
    }

    public UserOnPaperDocFilter getFilterBy() {
        return this.filterBy;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{Integer.valueOf(this.limit), this.filterBy});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0038, code lost:
        if (r2.equals(r5) == false) goto L_0x003b;
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
            if (r2 == 0) goto L_0x003d
            com.dropbox.core.v2.paper.ListUsersOnPaperDocArgs r5 = (com.dropbox.core.p005v2.paper.ListUsersOnPaperDocArgs) r5
            java.lang.String r2 = r4.docId
            java.lang.String r3 = r5.docId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.docId
            java.lang.String r3 = r5.docId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003b
        L_0x0028:
            int r2 = r4.limit
            int r3 = r5.limit
            if (r2 != r3) goto L_0x003b
            com.dropbox.core.v2.paper.UserOnPaperDocFilter r2 = r4.filterBy
            com.dropbox.core.v2.paper.UserOnPaperDocFilter r5 = r5.filterBy
            if (r2 == r5) goto L_0x003c
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003b
            goto L_0x003c
        L_0x003b:
            r0 = 0
        L_0x003c:
            return r0
        L_0x003d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.ListUsersOnPaperDocArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
