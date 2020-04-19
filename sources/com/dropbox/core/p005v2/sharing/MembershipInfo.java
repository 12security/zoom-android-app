package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.MembershipInfo */
public class MembershipInfo {
    protected final AccessLevel accessType;
    protected final String initials;
    protected final boolean isInherited;
    protected final List<MemberPermission> permissions;

    /* renamed from: com.dropbox.core.v2.sharing.MembershipInfo$Builder */
    public static class Builder {
        protected final AccessLevel accessType;
        protected String initials;
        protected boolean isInherited;
        protected List<MemberPermission> permissions;

        protected Builder(AccessLevel accessLevel) {
            if (accessLevel != null) {
                this.accessType = accessLevel;
                this.permissions = null;
                this.initials = null;
                this.isInherited = false;
                return;
            }
            throw new IllegalArgumentException("Required value for 'accessType' is null");
        }

        public Builder withPermissions(List<MemberPermission> list) {
            if (list != null) {
                for (MemberPermission memberPermission : list) {
                    if (memberPermission == null) {
                        throw new IllegalArgumentException("An item in list 'permissions' is null");
                    }
                }
            }
            this.permissions = list;
            return this;
        }

        public Builder withInitials(String str) {
            this.initials = str;
            return this;
        }

        public Builder withIsInherited(Boolean bool) {
            if (bool != null) {
                this.isInherited = bool.booleanValue();
            } else {
                this.isInherited = false;
            }
            return this;
        }

        public MembershipInfo build() {
            return new MembershipInfo(this.accessType, this.permissions, this.initials, this.isInherited);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.MembershipInfo$Serializer */
    private static class Serializer extends StructSerializer<MembershipInfo> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(MembershipInfo membershipInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("access_type");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(membershipInfo.accessType, jsonGenerator);
            if (membershipInfo.permissions != null) {
                jsonGenerator.writeFieldName("permissions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(membershipInfo.permissions, jsonGenerator);
            }
            if (membershipInfo.initials != null) {
                jsonGenerator.writeFieldName("initials");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(membershipInfo.initials, jsonGenerator);
            }
            jsonGenerator.writeFieldName("is_inherited");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(membershipInfo.isInherited), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembershipInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AccessLevel accessLevel = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                Boolean valueOf = Boolean.valueOf(false);
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("access_type".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("permissions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("initials".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("is_inherited".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel != null) {
                    MembershipInfo membershipInfo = new MembershipInfo(accessLevel, list, str2, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membershipInfo, membershipInfo.toStringMultiline());
                    return membershipInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"access_type\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MembershipInfo(AccessLevel accessLevel, List<MemberPermission> list, String str, boolean z) {
        if (accessLevel != null) {
            this.accessType = accessLevel;
            if (list != null) {
                for (MemberPermission memberPermission : list) {
                    if (memberPermission == null) {
                        throw new IllegalArgumentException("An item in list 'permissions' is null");
                    }
                }
            }
            this.permissions = list;
            this.initials = str;
            this.isInherited = z;
            return;
        }
        throw new IllegalArgumentException("Required value for 'accessType' is null");
    }

    public MembershipInfo(AccessLevel accessLevel) {
        this(accessLevel, null, null, false);
    }

    public AccessLevel getAccessType() {
        return this.accessType;
    }

    public List<MemberPermission> getPermissions() {
        return this.permissions;
    }

    public String getInitials() {
        return this.initials;
    }

    public boolean getIsInherited() {
        return this.isInherited;
    }

    public static Builder newBuilder(AccessLevel accessLevel) {
        return new Builder(accessLevel);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accessType, this.permissions, this.initials, Boolean.valueOf(this.isInherited)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2.equals(r3) == false) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0044, code lost:
        if (r4.isInherited != r5.isInherited) goto L_0x0047;
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
            if (r2 == 0) goto L_0x0049
            com.dropbox.core.v2.sharing.MembershipInfo r5 = (com.dropbox.core.p005v2.sharing.MembershipInfo) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0047
        L_0x0024:
            java.util.List<com.dropbox.core.v2.sharing.MemberPermission> r2 = r4.permissions
            java.util.List<com.dropbox.core.v2.sharing.MemberPermission> r3 = r5.permissions
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0047
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0047
        L_0x0032:
            java.lang.String r2 = r4.initials
            java.lang.String r3 = r5.initials
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x0047
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0047
        L_0x0040:
            boolean r2 = r4.isInherited
            boolean r5 = r5.isInherited
            if (r2 != r5) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r0 = 0
        L_0x0048:
            return r0
        L_0x0049:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.MembershipInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
