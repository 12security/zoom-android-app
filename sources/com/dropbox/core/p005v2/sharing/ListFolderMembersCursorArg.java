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

/* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersCursorArg */
class ListFolderMembersCursorArg {
    protected final List<MemberAction> actions;
    protected final long limit;

    /* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersCursorArg$Builder */
    public static class Builder {
        protected List<MemberAction> actions = null;
        protected long limit = 1000;

        protected Builder() {
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

        public Builder withLimit(Long l) {
            if (l.longValue() < 1) {
                throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
            } else if (l.longValue() <= 1000) {
                if (l != null) {
                    this.limit = l.longValue();
                } else {
                    this.limit = 1000;
                }
                return this;
            } else {
                throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
            }
        }

        public ListFolderMembersCursorArg build() {
            return new ListFolderMembersCursorArg(this.actions, this.limit);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersCursorArg$Serializer */
    private static class Serializer extends StructSerializer<ListFolderMembersCursorArg> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(ListFolderMembersCursorArg listFolderMembersCursorArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (listFolderMembersCursorArg.actions != null) {
                jsonGenerator.writeFieldName("actions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(listFolderMembersCursorArg.actions, jsonGenerator);
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(listFolderMembersCursorArg.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFolderMembersCursorArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(1000);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("actions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                ListFolderMembersCursorArg listFolderMembersCursorArg = new ListFolderMembersCursorArg(list, valueOf.longValue());
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(listFolderMembersCursorArg, listFolderMembersCursorArg.toStringMultiline());
                return listFolderMembersCursorArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListFolderMembersCursorArg(List<MemberAction> list, long j) {
        if (list != null) {
            for (MemberAction memberAction : list) {
                if (memberAction == null) {
                    throw new IllegalArgumentException("An item in list 'actions' is null");
                }
            }
        }
        this.actions = list;
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            this.limit = j;
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    public ListFolderMembersCursorArg() {
        this(null, 1000);
    }

    public List<MemberAction> getActions() {
        return this.actions;
    }

    public long getLimit() {
        return this.limit;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.actions, Long.valueOf(this.limit)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListFolderMembersCursorArg listFolderMembersCursorArg = (ListFolderMembersCursorArg) obj;
        List<MemberAction> list = this.actions;
        List<MemberAction> list2 = listFolderMembersCursorArg.actions;
        if ((list != list2 && (list == null || !list.equals(list2))) || this.limit != listFolderMembersCursorArg.limit) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
