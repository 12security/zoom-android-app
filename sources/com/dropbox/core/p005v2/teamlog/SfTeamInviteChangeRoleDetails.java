package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.SfTeamInviteChangeRoleDetails */
public class SfTeamInviteChangeRoleDetails {
    protected final String newSharingPermission;
    protected final String originalFolderName;
    protected final String previousSharingPermission;
    protected final long targetAssetIndex;

    /* renamed from: com.dropbox.core.v2.teamlog.SfTeamInviteChangeRoleDetails$Builder */
    public static class Builder {
        protected String newSharingPermission;
        protected final String originalFolderName;
        protected String previousSharingPermission;
        protected final long targetAssetIndex;

        protected Builder(long j, String str) {
            this.targetAssetIndex = j;
            if (str != null) {
                this.originalFolderName = str;
                this.newSharingPermission = null;
                this.previousSharingPermission = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'originalFolderName' is null");
        }

        public Builder withNewSharingPermission(String str) {
            this.newSharingPermission = str;
            return this;
        }

        public Builder withPreviousSharingPermission(String str) {
            this.previousSharingPermission = str;
            return this;
        }

        public SfTeamInviteChangeRoleDetails build() {
            SfTeamInviteChangeRoleDetails sfTeamInviteChangeRoleDetails = new SfTeamInviteChangeRoleDetails(this.targetAssetIndex, this.originalFolderName, this.newSharingPermission, this.previousSharingPermission);
            return sfTeamInviteChangeRoleDetails;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.SfTeamInviteChangeRoleDetails$Serializer */
    static class Serializer extends StructSerializer<SfTeamInviteChangeRoleDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SfTeamInviteChangeRoleDetails sfTeamInviteChangeRoleDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("target_asset_index");
            StoneSerializers.uInt64().serialize(Long.valueOf(sfTeamInviteChangeRoleDetails.targetAssetIndex), jsonGenerator);
            jsonGenerator.writeFieldName("original_folder_name");
            StoneSerializers.string().serialize(sfTeamInviteChangeRoleDetails.originalFolderName, jsonGenerator);
            if (sfTeamInviteChangeRoleDetails.newSharingPermission != null) {
                jsonGenerator.writeFieldName("new_sharing_permission");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sfTeamInviteChangeRoleDetails.newSharingPermission, jsonGenerator);
            }
            if (sfTeamInviteChangeRoleDetails.previousSharingPermission != null) {
                jsonGenerator.writeFieldName("previous_sharing_permission");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sfTeamInviteChangeRoleDetails.previousSharingPermission, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SfTeamInviteChangeRoleDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("target_asset_index".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("original_folder_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("new_sharing_permission".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("previous_sharing_permission".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"target_asset_index\" missing.");
                } else if (str2 != null) {
                    SfTeamInviteChangeRoleDetails sfTeamInviteChangeRoleDetails = new SfTeamInviteChangeRoleDetails(l.longValue(), str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sfTeamInviteChangeRoleDetails, sfTeamInviteChangeRoleDetails.toStringMultiline());
                    return sfTeamInviteChangeRoleDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"original_folder_name\" missing.");
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

    public SfTeamInviteChangeRoleDetails(long j, String str, String str2, String str3) {
        this.targetAssetIndex = j;
        if (str != null) {
            this.originalFolderName = str;
            this.newSharingPermission = str2;
            this.previousSharingPermission = str3;
            return;
        }
        throw new IllegalArgumentException("Required value for 'originalFolderName' is null");
    }

    public SfTeamInviteChangeRoleDetails(long j, String str) {
        this(j, str, null, null);
    }

    public long getTargetAssetIndex() {
        return this.targetAssetIndex;
    }

    public String getOriginalFolderName() {
        return this.originalFolderName;
    }

    public String getNewSharingPermission() {
        return this.newSharingPermission;
    }

    public String getPreviousSharingPermission() {
        return this.previousSharingPermission;
    }

    public static Builder newBuilder(long j, String str) {
        return new Builder(j, str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.targetAssetIndex), this.originalFolderName, this.newSharingPermission, this.previousSharingPermission});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0046, code lost:
        if (r2.equals(r7) == false) goto L_0x0049;
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
            if (r2 == 0) goto L_0x004b
            com.dropbox.core.v2.teamlog.SfTeamInviteChangeRoleDetails r7 = (com.dropbox.core.p005v2.teamlog.SfTeamInviteChangeRoleDetails) r7
            long r2 = r6.targetAssetIndex
            long r4 = r7.targetAssetIndex
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x0049
            java.lang.String r2 = r6.originalFolderName
            java.lang.String r3 = r7.originalFolderName
            if (r2 == r3) goto L_0x002c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x002c:
            java.lang.String r2 = r6.newSharingPermission
            java.lang.String r3 = r7.newSharingPermission
            if (r2 == r3) goto L_0x003a
            if (r2 == 0) goto L_0x0049
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x003a:
            java.lang.String r2 = r6.previousSharingPermission
            java.lang.String r7 = r7.previousSharingPermission
            if (r2 == r7) goto L_0x004a
            if (r2 == 0) goto L_0x0049
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r0 = 0
        L_0x004a:
            return r0
        L_0x004b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SfTeamInviteChangeRoleDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
