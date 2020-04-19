package com.dropbox.core.p005v2.paper;

import com.dropbox.core.p005v2.sharing.InviteeInfo;
import com.dropbox.core.p005v2.sharing.UserInfo;
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

/* renamed from: com.dropbox.core.v2.paper.ListUsersOnFolderResponse */
public class ListUsersOnFolderResponse {
    protected final Cursor cursor;
    protected final boolean hasMore;
    protected final List<InviteeInfo> invitees;
    protected final List<UserInfo> users;

    /* renamed from: com.dropbox.core.v2.paper.ListUsersOnFolderResponse$Serializer */
    static class Serializer extends StructSerializer<ListUsersOnFolderResponse> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListUsersOnFolderResponse listUsersOnFolderResponse, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("invitees");
            StoneSerializers.list(com.dropbox.core.p005v2.sharing.InviteeInfo.Serializer.INSTANCE).serialize(listUsersOnFolderResponse.invitees, jsonGenerator);
            jsonGenerator.writeFieldName("users");
            StoneSerializers.list(com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE).serialize(listUsersOnFolderResponse.users, jsonGenerator);
            jsonGenerator.writeFieldName("cursor");
            Serializer.INSTANCE.serialize(listUsersOnFolderResponse.cursor, jsonGenerator);
            jsonGenerator.writeFieldName("has_more");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listUsersOnFolderResponse.hasMore), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListUsersOnFolderResponse deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list2 = null;
                Cursor cursor = null;
                Boolean bool = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("invitees".equals(currentName)) {
                        list = (List) StoneSerializers.list(com.dropbox.core.p005v2.sharing.InviteeInfo.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("users".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("cursor".equals(currentName)) {
                        cursor = (Cursor) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("has_more".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"invitees\" missing.");
                } else if (list2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"users\" missing.");
                } else if (cursor == null) {
                    throw new JsonParseException(jsonParser, "Required field \"cursor\" missing.");
                } else if (bool != null) {
                    ListUsersOnFolderResponse listUsersOnFolderResponse = new ListUsersOnFolderResponse(list, list2, cursor, bool.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listUsersOnFolderResponse, listUsersOnFolderResponse.toStringMultiline());
                    return listUsersOnFolderResponse;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"has_more\" missing.");
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

    public ListUsersOnFolderResponse(List<InviteeInfo> list, List<UserInfo> list2, Cursor cursor2, boolean z) {
        if (list != null) {
            for (InviteeInfo inviteeInfo : list) {
                if (inviteeInfo == null) {
                    throw new IllegalArgumentException("An item in list 'invitees' is null");
                }
            }
            this.invitees = list;
            if (list2 != null) {
                for (UserInfo userInfo : list2) {
                    if (userInfo == null) {
                        throw new IllegalArgumentException("An item in list 'users' is null");
                    }
                }
                this.users = list2;
                if (cursor2 != null) {
                    this.cursor = cursor2;
                    this.hasMore = z;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'cursor' is null");
            }
            throw new IllegalArgumentException("Required value for 'users' is null");
        }
        throw new IllegalArgumentException("Required value for 'invitees' is null");
    }

    public List<InviteeInfo> getInvitees() {
        return this.invitees;
    }

    public List<UserInfo> getUsers() {
        return this.users;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public boolean getHasMore() {
        return this.hasMore;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.invitees, this.users, this.cursor, Boolean.valueOf(this.hasMore)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r3) == false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0040, code lost:
        if (r4.hasMore != r5.hasMore) goto L_0x0043;
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
            com.dropbox.core.v2.paper.ListUsersOnFolderResponse r5 = (com.dropbox.core.p005v2.paper.ListUsersOnFolderResponse) r5
            java.util.List<com.dropbox.core.v2.sharing.InviteeInfo> r2 = r4.invitees
            java.util.List<com.dropbox.core.v2.sharing.InviteeInfo> r3 = r5.invitees
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0024:
            java.util.List<com.dropbox.core.v2.sharing.UserInfo> r2 = r4.users
            java.util.List<com.dropbox.core.v2.sharing.UserInfo> r3 = r5.users
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0030:
            com.dropbox.core.v2.paper.Cursor r2 = r4.cursor
            com.dropbox.core.v2.paper.Cursor r3 = r5.cursor
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x003c:
            boolean r2 = r4.hasMore
            boolean r5 = r5.hasMore
            if (r2 != r5) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.ListUsersOnFolderResponse.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
