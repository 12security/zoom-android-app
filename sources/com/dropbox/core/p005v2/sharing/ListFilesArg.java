package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.ListFilesArg */
class ListFilesArg {
    protected final List<FileAction> actions;
    protected final long limit;

    /* renamed from: com.dropbox.core.v2.sharing.ListFilesArg$Builder */
    public static class Builder {
        protected List<FileAction> actions = null;
        protected long limit = 100;

        protected Builder() {
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

        public Builder withActions(List<FileAction> list) {
            if (list != null) {
                for (FileAction fileAction : list) {
                    if (fileAction == null) {
                        throw new IllegalArgumentException("An item in list 'actions' is null");
                    }
                }
            }
            this.actions = list;
            return this;
        }

        public ListFilesArg build() {
            return new ListFilesArg(this.limit, this.actions);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFilesArg$Serializer */
    static class Serializer extends StructSerializer<ListFilesArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFilesArg listFilesArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(listFilesArg.limit), jsonGenerator);
            if (listFilesArg.actions != null) {
                jsonGenerator.writeFieldName("actions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(listFilesArg.actions, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFilesArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(100);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else if ("actions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                ListFilesArg listFilesArg = new ListFilesArg(valueOf.longValue(), list);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(listFilesArg, listFilesArg.toStringMultiline());
                return listFilesArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListFilesArg(long j, List<FileAction> list) {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 300) {
            this.limit = j;
            if (list != null) {
                for (FileAction fileAction : list) {
                    if (fileAction == null) {
                        throw new IllegalArgumentException("An item in list 'actions' is null");
                    }
                }
            }
            this.actions = list;
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 300L");
        }
    }

    public ListFilesArg() {
        this(100, null);
    }

    public long getLimit() {
        return this.limit;
    }

    public List<FileAction> getActions() {
        return this.actions;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.limit), this.actions});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        if (r2.equals(r7) == false) goto L_0x002f;
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
            if (r2 == 0) goto L_0x0031
            com.dropbox.core.v2.sharing.ListFilesArg r7 = (com.dropbox.core.p005v2.sharing.ListFilesArg) r7
            long r2 = r6.limit
            long r4 = r7.limit
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x002f
            java.util.List<com.dropbox.core.v2.sharing.FileAction> r2 = r6.actions
            java.util.List<com.dropbox.core.v2.sharing.FileAction> r7 = r7.actions
            if (r2 == r7) goto L_0x0030
            if (r2 == 0) goto L_0x002f
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x002f
            goto L_0x0030
        L_0x002f:
            r0 = 0
        L_0x0030:
            return r0
        L_0x0031:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.ListFilesArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
