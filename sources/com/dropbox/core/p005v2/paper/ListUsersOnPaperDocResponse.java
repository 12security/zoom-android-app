package com.dropbox.core.p005v2.paper;

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

/* renamed from: com.dropbox.core.v2.paper.ListUsersOnPaperDocResponse */
public class ListUsersOnPaperDocResponse {
    protected final Cursor cursor;
    protected final UserInfo docOwner;
    protected final boolean hasMore;
    protected final List<InviteeInfoWithPermissionLevel> invitees;
    protected final List<UserInfoWithPermissionLevel> users;

    /* renamed from: com.dropbox.core.v2.paper.ListUsersOnPaperDocResponse$Serializer */
    static class Serializer extends StructSerializer<ListUsersOnPaperDocResponse> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListUsersOnPaperDocResponse listUsersOnPaperDocResponse, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("invitees");
            StoneSerializers.list(Serializer.INSTANCE).serialize(listUsersOnPaperDocResponse.invitees, jsonGenerator);
            jsonGenerator.writeFieldName("users");
            StoneSerializers.list(Serializer.INSTANCE).serialize(listUsersOnPaperDocResponse.users, jsonGenerator);
            jsonGenerator.writeFieldName("doc_owner");
            com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE.serialize(listUsersOnPaperDocResponse.docOwner, jsonGenerator);
            jsonGenerator.writeFieldName("cursor");
            Serializer.INSTANCE.serialize(listUsersOnPaperDocResponse.cursor, jsonGenerator);
            jsonGenerator.writeFieldName("has_more");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listUsersOnPaperDocResponse.hasMore), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListUsersOnPaperDocResponse deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                List list2 = null;
                UserInfo userInfo = null;
                Cursor cursor = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("invitees".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("users".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("doc_owner".equals(currentName)) {
                        userInfo = (UserInfo) com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE.deserialize(jsonParser);
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
                } else if (userInfo == null) {
                    throw new JsonParseException(jsonParser, "Required field \"doc_owner\" missing.");
                } else if (cursor == null) {
                    throw new JsonParseException(jsonParser, "Required field \"cursor\" missing.");
                } else if (bool != null) {
                    ListUsersOnPaperDocResponse listUsersOnPaperDocResponse = new ListUsersOnPaperDocResponse(list, list2, userInfo, cursor, bool.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listUsersOnPaperDocResponse, listUsersOnPaperDocResponse.toStringMultiline());
                    return listUsersOnPaperDocResponse;
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

    public ListUsersOnPaperDocResponse(List<InviteeInfoWithPermissionLevel> list, List<UserInfoWithPermissionLevel> list2, UserInfo userInfo, Cursor cursor2, boolean z) {
        if (list != null) {
            for (InviteeInfoWithPermissionLevel inviteeInfoWithPermissionLevel : list) {
                if (inviteeInfoWithPermissionLevel == null) {
                    throw new IllegalArgumentException("An item in list 'invitees' is null");
                }
            }
            this.invitees = list;
            if (list2 != null) {
                for (UserInfoWithPermissionLevel userInfoWithPermissionLevel : list2) {
                    if (userInfoWithPermissionLevel == null) {
                        throw new IllegalArgumentException("An item in list 'users' is null");
                    }
                }
                this.users = list2;
                if (userInfo != null) {
                    this.docOwner = userInfo;
                    if (cursor2 != null) {
                        this.cursor = cursor2;
                        this.hasMore = z;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'cursor' is null");
                }
                throw new IllegalArgumentException("Required value for 'docOwner' is null");
            }
            throw new IllegalArgumentException("Required value for 'users' is null");
        }
        throw new IllegalArgumentException("Required value for 'invitees' is null");
    }

    public List<InviteeInfoWithPermissionLevel> getInvitees() {
        return this.invitees;
    }

    public List<UserInfoWithPermissionLevel> getUsers() {
        return this.users;
    }

    public UserInfo getDocOwner() {
        return this.docOwner;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public boolean getHasMore() {
        return this.hasMore;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.invitees, this.users, this.docOwner, this.cursor, Boolean.valueOf(this.hasMore)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0046, code lost:
        if (r2.equals(r3) == false) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004c, code lost:
        if (r4.hasMore != r5.hasMore) goto L_0x004f;
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
            if (r2 == 0) goto L_0x0051
            com.dropbox.core.v2.paper.ListUsersOnPaperDocResponse r5 = (com.dropbox.core.p005v2.paper.ListUsersOnPaperDocResponse) r5
            java.util.List<com.dropbox.core.v2.paper.InviteeInfoWithPermissionLevel> r2 = r4.invitees
            java.util.List<com.dropbox.core.v2.paper.InviteeInfoWithPermissionLevel> r3 = r5.invitees
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0024:
            java.util.List<com.dropbox.core.v2.paper.UserInfoWithPermissionLevel> r2 = r4.users
            java.util.List<com.dropbox.core.v2.paper.UserInfoWithPermissionLevel> r3 = r5.users
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0030:
            com.dropbox.core.v2.sharing.UserInfo r2 = r4.docOwner
            com.dropbox.core.v2.sharing.UserInfo r3 = r5.docOwner
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x003c:
            com.dropbox.core.v2.paper.Cursor r2 = r4.cursor
            com.dropbox.core.v2.paper.Cursor r3 = r5.cursor
            if (r2 == r3) goto L_0x0048
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0048:
            boolean r2 = r4.hasMore
            boolean r5 = r5.hasMore
            if (r2 != r5) goto L_0x004f
            goto L_0x0050
        L_0x004f:
            r0 = 0
        L_0x0050:
            return r0
        L_0x0051:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.ListUsersOnPaperDocResponse.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
