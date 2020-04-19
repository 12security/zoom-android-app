package com.dropbox.core.p005v2.users;

import com.dropbox.core.p005v2.teampolicies.OfficeAddInPolicy;
import com.dropbox.core.p005v2.teampolicies.TeamSharingPolicies;
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

/* renamed from: com.dropbox.core.v2.users.FullTeam */
public class FullTeam extends Team {
    protected final OfficeAddInPolicy officeAddinPolicy;
    protected final TeamSharingPolicies sharingPolicies;

    /* renamed from: com.dropbox.core.v2.users.FullTeam$Serializer */
    static class Serializer extends StructSerializer<FullTeam> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FullTeam fullTeam, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(fullTeam.f165id, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(fullTeam.name, jsonGenerator);
            jsonGenerator.writeFieldName("sharing_policies");
            com.dropbox.core.p005v2.teampolicies.TeamSharingPolicies.Serializer.INSTANCE.serialize(fullTeam.sharingPolicies, jsonGenerator);
            jsonGenerator.writeFieldName("office_addin_policy");
            com.dropbox.core.p005v2.teampolicies.OfficeAddInPolicy.Serializer.INSTANCE.serialize(fullTeam.officeAddinPolicy, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FullTeam deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                TeamSharingPolicies teamSharingPolicies = null;
                OfficeAddInPolicy officeAddInPolicy = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("sharing_policies".equals(currentName)) {
                        teamSharingPolicies = (TeamSharingPolicies) com.dropbox.core.p005v2.teampolicies.TeamSharingPolicies.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("office_addin_policy".equals(currentName)) {
                        officeAddInPolicy = com.dropbox.core.p005v2.teampolicies.OfficeAddInPolicy.Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (teamSharingPolicies == null) {
                    throw new JsonParseException(jsonParser, "Required field \"sharing_policies\" missing.");
                } else if (officeAddInPolicy != null) {
                    FullTeam fullTeam = new FullTeam(str2, str3, teamSharingPolicies, officeAddInPolicy);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fullTeam, fullTeam.toStringMultiline());
                    return fullTeam;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"office_addin_policy\" missing.");
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

    public FullTeam(String str, String str2, TeamSharingPolicies teamSharingPolicies, OfficeAddInPolicy officeAddInPolicy) {
        super(str, str2);
        if (teamSharingPolicies != null) {
            this.sharingPolicies = teamSharingPolicies;
            if (officeAddInPolicy != null) {
                this.officeAddinPolicy = officeAddInPolicy;
                return;
            }
            throw new IllegalArgumentException("Required value for 'officeAddinPolicy' is null");
        }
        throw new IllegalArgumentException("Required value for 'sharingPolicies' is null");
    }

    public String getId() {
        return this.f165id;
    }

    public String getName() {
        return this.name;
    }

    public TeamSharingPolicies getSharingPolicies() {
        return this.sharingPolicies;
    }

    public OfficeAddInPolicy getOfficeAddinPolicy() {
        return this.officeAddinPolicy;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.sharingPolicies, this.officeAddinPolicy});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004e, code lost:
        if (r2.equals(r5) == false) goto L_0x0051;
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
            if (r2 == 0) goto L_0x0053
            com.dropbox.core.v2.users.FullTeam r5 = (com.dropbox.core.p005v2.users.FullTeam) r5
            java.lang.String r2 = r4.f165id
            java.lang.String r3 = r5.f165id
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.f165id
            java.lang.String r3 = r5.f165id
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0028:
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0038
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0038:
            com.dropbox.core.v2.teampolicies.TeamSharingPolicies r2 = r4.sharingPolicies
            com.dropbox.core.v2.teampolicies.TeamSharingPolicies r3 = r5.sharingPolicies
            if (r2 == r3) goto L_0x0044
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0044:
            com.dropbox.core.v2.teampolicies.OfficeAddInPolicy r2 = r4.officeAddinPolicy
            com.dropbox.core.v2.teampolicies.OfficeAddInPolicy r5 = r5.officeAddinPolicy
            if (r2 == r5) goto L_0x0052
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0051
            goto L_0x0052
        L_0x0051:
            r0 = 0
        L_0x0052:
            return r0
        L_0x0053:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.users.FullTeam.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
