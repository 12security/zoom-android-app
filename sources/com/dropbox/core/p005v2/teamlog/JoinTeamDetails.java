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
import java.util.List;

/* renamed from: com.dropbox.core.v2.teamlog.JoinTeamDetails */
public class JoinTeamDetails {
    protected final List<UserLinkedAppLogInfo> linkedApps;
    protected final List<LinkedDeviceLogInfo> linkedDevices;
    protected final List<FolderLogInfo> linkedSharedFolders;

    /* renamed from: com.dropbox.core.v2.teamlog.JoinTeamDetails$Serializer */
    static class Serializer extends StructSerializer<JoinTeamDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(JoinTeamDetails joinTeamDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("linked_apps");
            StoneSerializers.list(Serializer.INSTANCE).serialize(joinTeamDetails.linkedApps, jsonGenerator);
            jsonGenerator.writeFieldName("linked_devices");
            StoneSerializers.list(Serializer.INSTANCE).serialize(joinTeamDetails.linkedDevices, jsonGenerator);
            jsonGenerator.writeFieldName("linked_shared_folders");
            StoneSerializers.list(Serializer.INSTANCE).serialize(joinTeamDetails.linkedSharedFolders, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public JoinTeamDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                List list3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("linked_apps".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("linked_devices".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("linked_shared_folders".equals(currentName)) {
                        list3 = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"linked_apps\" missing.");
                } else if (list2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"linked_devices\" missing.");
                } else if (list3 != null) {
                    JoinTeamDetails joinTeamDetails = new JoinTeamDetails(list, list2, list3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(joinTeamDetails, joinTeamDetails.toStringMultiline());
                    return joinTeamDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"linked_shared_folders\" missing.");
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

    public JoinTeamDetails(List<UserLinkedAppLogInfo> list, List<LinkedDeviceLogInfo> list2, List<FolderLogInfo> list3) {
        if (list != null) {
            for (UserLinkedAppLogInfo userLinkedAppLogInfo : list) {
                if (userLinkedAppLogInfo == null) {
                    throw new IllegalArgumentException("An item in list 'linkedApps' is null");
                }
            }
            this.linkedApps = list;
            if (list2 != null) {
                for (LinkedDeviceLogInfo linkedDeviceLogInfo : list2) {
                    if (linkedDeviceLogInfo == null) {
                        throw new IllegalArgumentException("An item in list 'linkedDevices' is null");
                    }
                }
                this.linkedDevices = list2;
                if (list3 != null) {
                    for (FolderLogInfo folderLogInfo : list3) {
                        if (folderLogInfo == null) {
                            throw new IllegalArgumentException("An item in list 'linkedSharedFolders' is null");
                        }
                    }
                    this.linkedSharedFolders = list3;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'linkedSharedFolders' is null");
            }
            throw new IllegalArgumentException("Required value for 'linkedDevices' is null");
        }
        throw new IllegalArgumentException("Required value for 'linkedApps' is null");
    }

    public List<UserLinkedAppLogInfo> getLinkedApps() {
        return this.linkedApps;
    }

    public List<LinkedDeviceLogInfo> getLinkedDevices() {
        return this.linkedDevices;
    }

    public List<FolderLogInfo> getLinkedSharedFolders() {
        return this.linkedSharedFolders;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.linkedApps, this.linkedDevices, this.linkedSharedFolders});
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
            com.dropbox.core.v2.teamlog.JoinTeamDetails r5 = (com.dropbox.core.p005v2.teamlog.JoinTeamDetails) r5
            java.util.List<com.dropbox.core.v2.teamlog.UserLinkedAppLogInfo> r2 = r4.linkedApps
            java.util.List<com.dropbox.core.v2.teamlog.UserLinkedAppLogInfo> r3 = r5.linkedApps
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0024:
            java.util.List<com.dropbox.core.v2.teamlog.LinkedDeviceLogInfo> r2 = r4.linkedDevices
            java.util.List<com.dropbox.core.v2.teamlog.LinkedDeviceLogInfo> r3 = r5.linkedDevices
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0030:
            java.util.List<com.dropbox.core.v2.teamlog.FolderLogInfo> r2 = r4.linkedSharedFolders
            java.util.List<com.dropbox.core.v2.teamlog.FolderLogInfo> r5 = r5.linkedSharedFolders
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.JoinTeamDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
