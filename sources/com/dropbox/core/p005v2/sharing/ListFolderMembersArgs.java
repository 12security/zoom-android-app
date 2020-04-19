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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersArgs */
class ListFolderMembersArgs extends ListFolderMembersCursorArg {
    protected final String sharedFolderId;

    /* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersArgs$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.ListFolderMembersCursorArg.Builder {
        protected final String sharedFolderId;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.sharedFolderId = str;
            } else {
                throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
            }
        }

        public Builder withActions(List<MemberAction> list) {
            super.withActions(list);
            return this;
        }

        public Builder withLimit(Long l) {
            super.withLimit(l);
            return this;
        }

        public ListFolderMembersArgs build() {
            return new ListFolderMembersArgs(this.sharedFolderId, this.actions, this.limit);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersArgs$Serializer */
    static class Serializer extends StructSerializer<ListFolderMembersArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFolderMembersArgs listFolderMembersArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(listFolderMembersArgs.sharedFolderId, jsonGenerator);
            if (listFolderMembersArgs.actions != null) {
                jsonGenerator.writeFieldName("actions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(listFolderMembersArgs.actions, jsonGenerator);
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(listFolderMembersArgs.limit), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFolderMembersArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(1000);
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("actions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ListFolderMembersArgs listFolderMembersArgs = new ListFolderMembersArgs(str2, list, valueOf.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFolderMembersArgs, listFolderMembersArgs.toStringMultiline());
                    return listFolderMembersArgs;
                }
                throw new JsonParseException(jsonParser, "Required field \"shared_folder_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListFolderMembersArgs(String str, List<MemberAction> list, long j) {
        super(list, j);
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.sharedFolderId = str;
        } else {
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        }
    }

    public ListFolderMembersArgs(String str) {
        this(str, null, 1000);
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public List<MemberAction> getActions() {
        return this.actions;
    }

    public long getLimit() {
        return this.limit;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.sharedFolderId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListFolderMembersArgs listFolderMembersArgs = (ListFolderMembersArgs) obj;
        String str = this.sharedFolderId;
        String str2 = listFolderMembersArgs.sharedFolderId;
        if ((str != str2 && !str.equals(str2)) || ((this.actions != listFolderMembersArgs.actions && (this.actions == null || !this.actions.equals(listFolderMembersArgs.actions))) || this.limit != listFolderMembersArgs.limit)) {
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
