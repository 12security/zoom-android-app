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

/* renamed from: com.dropbox.core.v2.paper.ListPaperDocsArgs */
class ListPaperDocsArgs {
    protected final ListPaperDocsFilterBy filterBy;
    protected final int limit;
    protected final ListPaperDocsSortBy sortBy;
    protected final ListPaperDocsSortOrder sortOrder;

    /* renamed from: com.dropbox.core.v2.paper.ListPaperDocsArgs$Builder */
    public static class Builder {
        protected ListPaperDocsFilterBy filterBy = ListPaperDocsFilterBy.DOCS_ACCESSED;
        protected int limit = 1000;
        protected ListPaperDocsSortBy sortBy = ListPaperDocsSortBy.ACCESSED;
        protected ListPaperDocsSortOrder sortOrder = ListPaperDocsSortOrder.ASCENDING;

        protected Builder() {
        }

        public Builder withFilterBy(ListPaperDocsFilterBy listPaperDocsFilterBy) {
            if (listPaperDocsFilterBy != null) {
                this.filterBy = listPaperDocsFilterBy;
            } else {
                this.filterBy = ListPaperDocsFilterBy.DOCS_ACCESSED;
            }
            return this;
        }

        public Builder withSortBy(ListPaperDocsSortBy listPaperDocsSortBy) {
            if (listPaperDocsSortBy != null) {
                this.sortBy = listPaperDocsSortBy;
            } else {
                this.sortBy = ListPaperDocsSortBy.ACCESSED;
            }
            return this;
        }

        public Builder withSortOrder(ListPaperDocsSortOrder listPaperDocsSortOrder) {
            if (listPaperDocsSortOrder != null) {
                this.sortOrder = listPaperDocsSortOrder;
            } else {
                this.sortOrder = ListPaperDocsSortOrder.ASCENDING;
            }
            return this;
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

        public ListPaperDocsArgs build() {
            return new ListPaperDocsArgs(this.filterBy, this.sortBy, this.sortOrder, this.limit);
        }
    }

    /* renamed from: com.dropbox.core.v2.paper.ListPaperDocsArgs$Serializer */
    static class Serializer extends StructSerializer<ListPaperDocsArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListPaperDocsArgs listPaperDocsArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("filter_by");
            Serializer.INSTANCE.serialize(listPaperDocsArgs.filterBy, jsonGenerator);
            jsonGenerator.writeFieldName("sort_by");
            Serializer.INSTANCE.serialize(listPaperDocsArgs.sortBy, jsonGenerator);
            jsonGenerator.writeFieldName("sort_order");
            Serializer.INSTANCE.serialize(listPaperDocsArgs.sortOrder, jsonGenerator);
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.int32().serialize(Integer.valueOf(listPaperDocsArgs.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListPaperDocsArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                ListPaperDocsFilterBy listPaperDocsFilterBy = ListPaperDocsFilterBy.DOCS_ACCESSED;
                ListPaperDocsSortBy listPaperDocsSortBy = ListPaperDocsSortBy.ACCESSED;
                ListPaperDocsSortOrder listPaperDocsSortOrder = ListPaperDocsSortOrder.ASCENDING;
                Integer valueOf = Integer.valueOf(1000);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("filter_by".equals(currentName)) {
                        listPaperDocsFilterBy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("sort_by".equals(currentName)) {
                        listPaperDocsSortBy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("sort_order".equals(currentName)) {
                        listPaperDocsSortOrder = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Integer) StoneSerializers.int32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                ListPaperDocsArgs listPaperDocsArgs = new ListPaperDocsArgs(listPaperDocsFilterBy, listPaperDocsSortBy, listPaperDocsSortOrder, valueOf.intValue());
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(listPaperDocsArgs, listPaperDocsArgs.toStringMultiline());
                return listPaperDocsArgs;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListPaperDocsArgs(ListPaperDocsFilterBy listPaperDocsFilterBy, ListPaperDocsSortBy listPaperDocsSortBy, ListPaperDocsSortOrder listPaperDocsSortOrder, int i) {
        if (listPaperDocsFilterBy != null) {
            this.filterBy = listPaperDocsFilterBy;
            if (listPaperDocsSortBy != null) {
                this.sortBy = listPaperDocsSortBy;
                if (listPaperDocsSortOrder != null) {
                    this.sortOrder = listPaperDocsSortOrder;
                    if (i < 1) {
                        throw new IllegalArgumentException("Number 'limit' is smaller than 1");
                    } else if (i <= 1000) {
                        this.limit = i;
                    } else {
                        throw new IllegalArgumentException("Number 'limit' is larger than 1000");
                    }
                } else {
                    throw new IllegalArgumentException("Required value for 'sortOrder' is null");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'sortBy' is null");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'filterBy' is null");
        }
    }

    public ListPaperDocsArgs() {
        this(ListPaperDocsFilterBy.DOCS_ACCESSED, ListPaperDocsSortBy.ACCESSED, ListPaperDocsSortOrder.ASCENDING, 1000);
    }

    public ListPaperDocsFilterBy getFilterBy() {
        return this.filterBy;
    }

    public ListPaperDocsSortBy getSortBy() {
        return this.sortBy;
    }

    public ListPaperDocsSortOrder getSortOrder() {
        return this.sortOrder;
    }

    public int getLimit() {
        return this.limit;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.filterBy, this.sortBy, this.sortOrder, Integer.valueOf(this.limit)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r3) == false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0040, code lost:
        if (r4.limit != r5.limit) goto L_0x0043;
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
            if (r2 == 0) goto L_0x0045
            com.dropbox.core.v2.paper.ListPaperDocsArgs r5 = (com.dropbox.core.p005v2.paper.ListPaperDocsArgs) r5
            com.dropbox.core.v2.paper.ListPaperDocsFilterBy r2 = r4.filterBy
            com.dropbox.core.v2.paper.ListPaperDocsFilterBy r3 = r5.filterBy
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0024:
            com.dropbox.core.v2.paper.ListPaperDocsSortBy r2 = r4.sortBy
            com.dropbox.core.v2.paper.ListPaperDocsSortBy r3 = r5.sortBy
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0030:
            com.dropbox.core.v2.paper.ListPaperDocsSortOrder r2 = r4.sortOrder
            com.dropbox.core.v2.paper.ListPaperDocsSortOrder r3 = r5.sortOrder
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x003c:
            int r2 = r4.limit
            int r5 = r5.limit
            if (r2 != r5) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.ListPaperDocsArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
