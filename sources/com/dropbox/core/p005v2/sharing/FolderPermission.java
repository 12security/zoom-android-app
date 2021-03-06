package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.kubi.KubiContract;
import com.zipow.videobox.view.p014mm.MMContentFileViewerFragment;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.FolderPermission */
public class FolderPermission {
    protected final FolderAction action;
    protected final boolean allow;
    protected final PermissionDeniedReason reason;

    /* renamed from: com.dropbox.core.v2.sharing.FolderPermission$Serializer */
    static class Serializer extends StructSerializer<FolderPermission> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FolderPermission folderPermission, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(MMContentFileViewerFragment.RESULT_ACTION);
            Serializer.INSTANCE.serialize(folderPermission.action, jsonGenerator);
            jsonGenerator.writeFieldName("allow");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(folderPermission.allow), jsonGenerator);
            if (folderPermission.reason != null) {
                jsonGenerator.writeFieldName(KubiContract.EXTRA_REASON);
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(folderPermission.reason, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FolderPermission deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            FolderAction folderAction = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean bool = null;
                PermissionDeniedReason permissionDeniedReason = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (MMContentFileViewerFragment.RESULT_ACTION.equals(currentName)) {
                        folderAction = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("allow".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if (KubiContract.EXTRA_REASON.equals(currentName)) {
                        permissionDeniedReason = (PermissionDeniedReason) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (folderAction == null) {
                    throw new JsonParseException(jsonParser, "Required field \"action\" missing.");
                } else if (bool != null) {
                    FolderPermission folderPermission = new FolderPermission(folderAction, bool.booleanValue(), permissionDeniedReason);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(folderPermission, folderPermission.toStringMultiline());
                    return folderPermission;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"allow\" missing.");
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

    public FolderPermission(FolderAction folderAction, boolean z, PermissionDeniedReason permissionDeniedReason) {
        if (folderAction != null) {
            this.action = folderAction;
            this.allow = z;
            this.reason = permissionDeniedReason;
            return;
        }
        throw new IllegalArgumentException("Required value for 'action' is null");
    }

    public FolderPermission(FolderAction folderAction, boolean z) {
        this(folderAction, z, null);
    }

    public FolderAction getAction() {
        return this.action;
    }

    public boolean getAllow() {
        return this.allow;
    }

    public PermissionDeniedReason getReason() {
        return this.reason;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.action, Boolean.valueOf(this.allow), this.reason});
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
            com.dropbox.core.v2.sharing.FolderPermission r5 = (com.dropbox.core.p005v2.sharing.FolderPermission) r5
            com.dropbox.core.v2.sharing.FolderAction r2 = r4.action
            com.dropbox.core.v2.sharing.FolderAction r3 = r5.action
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0039
        L_0x0024:
            boolean r2 = r4.allow
            boolean r3 = r5.allow
            if (r2 != r3) goto L_0x0039
            com.dropbox.core.v2.sharing.PermissionDeniedReason r2 = r4.reason
            com.dropbox.core.v2.sharing.PermissionDeniedReason r5 = r5.reason
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.FolderPermission.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
