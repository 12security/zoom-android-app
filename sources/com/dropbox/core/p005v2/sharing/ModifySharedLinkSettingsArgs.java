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

/* renamed from: com.dropbox.core.v2.sharing.ModifySharedLinkSettingsArgs */
class ModifySharedLinkSettingsArgs {
    protected final boolean removeExpiration;
    protected final SharedLinkSettings settings;
    protected final String url;

    /* renamed from: com.dropbox.core.v2.sharing.ModifySharedLinkSettingsArgs$Serializer */
    static class Serializer extends StructSerializer<ModifySharedLinkSettingsArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ModifySharedLinkSettingsArgs modifySharedLinkSettingsArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("url");
            StoneSerializers.string().serialize(modifySharedLinkSettingsArgs.url, jsonGenerator);
            jsonGenerator.writeFieldName("settings");
            Serializer.INSTANCE.serialize(modifySharedLinkSettingsArgs.settings, jsonGenerator);
            jsonGenerator.writeFieldName("remove_expiration");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(modifySharedLinkSettingsArgs.removeExpiration), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ModifySharedLinkSettingsArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                SharedLinkSettings sharedLinkSettings = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("url".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("settings".equals(currentName)) {
                        sharedLinkSettings = (SharedLinkSettings) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("remove_expiration".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"url\" missing.");
                } else if (sharedLinkSettings != null) {
                    ModifySharedLinkSettingsArgs modifySharedLinkSettingsArgs = new ModifySharedLinkSettingsArgs(str2, sharedLinkSettings, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(modifySharedLinkSettingsArgs, modifySharedLinkSettingsArgs.toStringMultiline());
                    return modifySharedLinkSettingsArgs;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"settings\" missing.");
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

    public ModifySharedLinkSettingsArgs(String str, SharedLinkSettings sharedLinkSettings, boolean z) {
        if (str != null) {
            this.url = str;
            if (sharedLinkSettings != null) {
                this.settings = sharedLinkSettings;
                this.removeExpiration = z;
                return;
            }
            throw new IllegalArgumentException("Required value for 'settings' is null");
        }
        throw new IllegalArgumentException("Required value for 'url' is null");
    }

    public ModifySharedLinkSettingsArgs(String str, SharedLinkSettings sharedLinkSettings) {
        this(str, sharedLinkSettings, false);
    }

    public String getUrl() {
        return this.url;
    }

    public SharedLinkSettings getSettings() {
        return this.settings;
    }

    public boolean getRemoveExpiration() {
        return this.removeExpiration;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.url, this.settings, Boolean.valueOf(this.removeExpiration)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r3) == false) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        if (r4.removeExpiration != r5.removeExpiration) goto L_0x0037;
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
            if (r2 == 0) goto L_0x0039
            com.dropbox.core.v2.sharing.ModifySharedLinkSettingsArgs r5 = (com.dropbox.core.p005v2.sharing.ModifySharedLinkSettingsArgs) r5
            java.lang.String r2 = r4.url
            java.lang.String r3 = r5.url
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0024:
            com.dropbox.core.v2.sharing.SharedLinkSettings r2 = r4.settings
            com.dropbox.core.v2.sharing.SharedLinkSettings r3 = r5.settings
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0030:
            boolean r2 = r4.removeExpiration
            boolean r5 = r5.removeExpiration
            if (r2 != r5) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r0 = 0
        L_0x0038:
            return r0
        L_0x0039:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.ModifySharedLinkSettingsArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
