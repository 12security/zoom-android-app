package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersListResult */
public class ExcludedUsersListResult {
    protected final String cursor;
    protected final boolean hasMore;
    protected final List<MemberProfile> users;

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersListResult$Serializer */
    static class Serializer extends StructSerializer<ExcludedUsersListResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ExcludedUsersListResult excludedUsersListResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("users");
            StoneSerializers.list(Serializer.INSTANCE).serialize(excludedUsersListResult.users, jsonGenerator);
            jsonGenerator.writeFieldName("has_more");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(excludedUsersListResult.hasMore), jsonGenerator);
            if (excludedUsersListResult.cursor != null) {
                jsonGenerator.writeFieldName("cursor");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(excludedUsersListResult.cursor, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ExcludedUsersListResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean bool = null;
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("users".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("has_more".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("cursor".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"users\" missing.");
                } else if (bool != null) {
                    ExcludedUsersListResult excludedUsersListResult = new ExcludedUsersListResult(list, bool.booleanValue(), str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(excludedUsersListResult, excludedUsersListResult.toStringMultiline());
                    return excludedUsersListResult;
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

    public ExcludedUsersListResult(List<MemberProfile> list, boolean z, String str) {
        if (list != null) {
            for (MemberProfile memberProfile : list) {
                if (memberProfile == null) {
                    throw new IllegalArgumentException("An item in list 'users' is null");
                }
            }
            this.users = list;
            this.cursor = str;
            this.hasMore = z;
            return;
        }
        throw new IllegalArgumentException("Required value for 'users' is null");
    }

    public ExcludedUsersListResult(List<MemberProfile> list, boolean z) {
        this(list, z, null);
    }

    public List<MemberProfile> getUsers() {
        return this.users;
    }

    public boolean getHasMore() {
        return this.hasMore;
    }

    public String getCursor() {
        return this.cursor;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.users, this.cursor, Boolean.valueOf(this.hasMore)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
        if (r2.equals(r5) == false) goto L_0x0039;
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
            if (r2 == 0) goto L_0x003b
            com.dropbox.core.v2.team.ExcludedUsersListResult r5 = (com.dropbox.core.p005v2.team.ExcludedUsersListResult) r5
            java.util.List<com.dropbox.core.v2.team.MemberProfile> r2 = r4.users
            java.util.List<com.dropbox.core.v2.team.MemberProfile> r3 = r5.users
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0039
        L_0x0024:
            boolean r2 = r4.hasMore
            boolean r3 = r5.hasMore
            if (r2 != r3) goto L_0x0039
            java.lang.String r2 = r4.cursor
            java.lang.String r5 = r5.cursor
            if (r2 == r5) goto L_0x003a
            if (r2 == 0) goto L_0x0039
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r0 = 0
        L_0x003a:
            return r0
        L_0x003b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.ExcludedUsersListResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
