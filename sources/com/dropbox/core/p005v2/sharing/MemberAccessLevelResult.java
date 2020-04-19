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

/* renamed from: com.dropbox.core.v2.sharing.MemberAccessLevelResult */
public class MemberAccessLevelResult {
    protected final List<ParentFolderAccessInfo> accessDetails;
    protected final AccessLevel accessLevel;
    protected final String warning;

    /* renamed from: com.dropbox.core.v2.sharing.MemberAccessLevelResult$Builder */
    public static class Builder {
        protected List<ParentFolderAccessInfo> accessDetails = null;
        protected AccessLevel accessLevel = null;
        protected String warning = null;

        protected Builder() {
        }

        public Builder withAccessLevel(AccessLevel accessLevel2) {
            this.accessLevel = accessLevel2;
            return this;
        }

        public Builder withWarning(String str) {
            this.warning = str;
            return this;
        }

        public Builder withAccessDetails(List<ParentFolderAccessInfo> list) {
            if (list != null) {
                for (ParentFolderAccessInfo parentFolderAccessInfo : list) {
                    if (parentFolderAccessInfo == null) {
                        throw new IllegalArgumentException("An item in list 'accessDetails' is null");
                    }
                }
            }
            this.accessDetails = list;
            return this;
        }

        public MemberAccessLevelResult build() {
            return new MemberAccessLevelResult(this.accessLevel, this.warning, this.accessDetails);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.MemberAccessLevelResult$Serializer */
    static class Serializer extends StructSerializer<MemberAccessLevelResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberAccessLevelResult memberAccessLevelResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (memberAccessLevelResult.accessLevel != null) {
                jsonGenerator.writeFieldName("access_level");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(memberAccessLevelResult.accessLevel, jsonGenerator);
            }
            if (memberAccessLevelResult.warning != null) {
                jsonGenerator.writeFieldName("warning");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(memberAccessLevelResult.warning, jsonGenerator);
            }
            if (memberAccessLevelResult.accessDetails != null) {
                jsonGenerator.writeFieldName("access_details");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(memberAccessLevelResult.accessDetails, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberAccessLevelResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("access_level".equals(currentName)) {
                        accessLevel = (AccessLevel) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("warning".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("access_details".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                MemberAccessLevelResult memberAccessLevelResult = new MemberAccessLevelResult(accessLevel, str2, list);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(memberAccessLevelResult, memberAccessLevelResult.toStringMultiline());
                return memberAccessLevelResult;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MemberAccessLevelResult(AccessLevel accessLevel2, String str, List<ParentFolderAccessInfo> list) {
        this.accessLevel = accessLevel2;
        this.warning = str;
        if (list != null) {
            for (ParentFolderAccessInfo parentFolderAccessInfo : list) {
                if (parentFolderAccessInfo == null) {
                    throw new IllegalArgumentException("An item in list 'accessDetails' is null");
                }
            }
        }
        this.accessDetails = list;
    }

    public MemberAccessLevelResult() {
        this(null, null, null);
    }

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public String getWarning() {
        return this.warning;
    }

    public List<ParentFolderAccessInfo> getAccessDetails() {
        return this.accessDetails;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accessLevel, this.warning, this.accessDetails});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r2.equals(r5) == false) goto L_0x0043;
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
            com.dropbox.core.v2.sharing.MemberAccessLevelResult r5 = (com.dropbox.core.p005v2.sharing.MemberAccessLevelResult) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessLevel
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0026:
            java.lang.String r2 = r4.warning
            java.lang.String r3 = r5.warning
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0034:
            java.util.List<com.dropbox.core.v2.sharing.ParentFolderAccessInfo> r2 = r4.accessDetails
            java.util.List<com.dropbox.core.v2.sharing.ParentFolderAccessInfo> r5 = r5.accessDetails
            if (r2 == r5) goto L_0x0044
            if (r2 == 0) goto L_0x0043
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.MemberAccessLevelResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
