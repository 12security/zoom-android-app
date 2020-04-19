package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teampolicies.TeamSharingPolicies */
public class TeamSharingPolicies {
    protected final SharedFolderJoinPolicy sharedFolderJoinPolicy;
    protected final SharedFolderMemberPolicy sharedFolderMemberPolicy;
    protected final SharedLinkCreatePolicy sharedLinkCreatePolicy;

    /* renamed from: com.dropbox.core.v2.teampolicies.TeamSharingPolicies$Serializer */
    public static class Serializer extends StructSerializer<TeamSharingPolicies> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(TeamSharingPolicies teamSharingPolicies, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_folder_member_policy");
            Serializer.INSTANCE.serialize(teamSharingPolicies.sharedFolderMemberPolicy, jsonGenerator);
            jsonGenerator.writeFieldName("shared_folder_join_policy");
            Serializer.INSTANCE.serialize(teamSharingPolicies.sharedFolderJoinPolicy, jsonGenerator);
            jsonGenerator.writeFieldName("shared_link_create_policy");
            Serializer.INSTANCE.serialize(teamSharingPolicies.sharedLinkCreatePolicy, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamSharingPolicies deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SharedFolderMemberPolicy sharedFolderMemberPolicy = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SharedFolderJoinPolicy sharedFolderJoinPolicy = null;
                SharedLinkCreatePolicy sharedLinkCreatePolicy = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_folder_member_policy".equals(currentName)) {
                        sharedFolderMemberPolicy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("shared_folder_join_policy".equals(currentName)) {
                        sharedFolderJoinPolicy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("shared_link_create_policy".equals(currentName)) {
                        sharedLinkCreatePolicy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (sharedFolderMemberPolicy == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_folder_member_policy\" missing.");
                } else if (sharedFolderJoinPolicy == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_folder_join_policy\" missing.");
                } else if (sharedLinkCreatePolicy != null) {
                    TeamSharingPolicies teamSharingPolicies = new TeamSharingPolicies(sharedFolderMemberPolicy, sharedFolderJoinPolicy, sharedLinkCreatePolicy);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamSharingPolicies, teamSharingPolicies.toStringMultiline());
                    return teamSharingPolicies;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"shared_link_create_policy\" missing.");
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

    public TeamSharingPolicies(SharedFolderMemberPolicy sharedFolderMemberPolicy2, SharedFolderJoinPolicy sharedFolderJoinPolicy2, SharedLinkCreatePolicy sharedLinkCreatePolicy2) {
        if (sharedFolderMemberPolicy2 != null) {
            this.sharedFolderMemberPolicy = sharedFolderMemberPolicy2;
            if (sharedFolderJoinPolicy2 != null) {
                this.sharedFolderJoinPolicy = sharedFolderJoinPolicy2;
                if (sharedLinkCreatePolicy2 != null) {
                    this.sharedLinkCreatePolicy = sharedLinkCreatePolicy2;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'sharedLinkCreatePolicy' is null");
            }
            throw new IllegalArgumentException("Required value for 'sharedFolderJoinPolicy' is null");
        }
        throw new IllegalArgumentException("Required value for 'sharedFolderMemberPolicy' is null");
    }

    public SharedFolderMemberPolicy getSharedFolderMemberPolicy() {
        return this.sharedFolderMemberPolicy;
    }

    public SharedFolderJoinPolicy getSharedFolderJoinPolicy() {
        return this.sharedFolderJoinPolicy;
    }

    public SharedLinkCreatePolicy getSharedLinkCreatePolicy() {
        return this.sharedLinkCreatePolicy;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedFolderMemberPolicy, this.sharedFolderJoinPolicy, this.sharedLinkCreatePolicy});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r5) == false) goto L_0x003d;
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
            if (r2 == 0) goto L_0x003f
            com.dropbox.core.v2.teampolicies.TeamSharingPolicies r5 = (com.dropbox.core.p005v2.teampolicies.TeamSharingPolicies) r5
            com.dropbox.core.v2.teampolicies.SharedFolderMemberPolicy r2 = r4.sharedFolderMemberPolicy
            com.dropbox.core.v2.teampolicies.SharedFolderMemberPolicy r3 = r5.sharedFolderMemberPolicy
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0024:
            com.dropbox.core.v2.teampolicies.SharedFolderJoinPolicy r2 = r4.sharedFolderJoinPolicy
            com.dropbox.core.v2.teampolicies.SharedFolderJoinPolicy r3 = r5.sharedFolderJoinPolicy
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0030:
            com.dropbox.core.v2.teampolicies.SharedLinkCreatePolicy r2 = r4.sharedLinkCreatePolicy
            com.dropbox.core.v2.teampolicies.SharedLinkCreatePolicy r5 = r5.sharedLinkCreatePolicy
            if (r2 == r5) goto L_0x003e
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = 0
        L_0x003e:
            return r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teampolicies.TeamSharingPolicies.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
