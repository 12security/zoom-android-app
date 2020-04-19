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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsArg */
class CreateSharedLinkWithSettingsArg {
    protected final String path;
    protected final SharedLinkSettings settings;

    /* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsArg$Serializer */
    static class Serializer extends StructSerializer<CreateSharedLinkWithSettingsArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateSharedLinkWithSettingsArg createSharedLinkWithSettingsArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(createSharedLinkWithSettingsArg.path, jsonGenerator);
            if (createSharedLinkWithSettingsArg.settings != null) {
                jsonGenerator.writeFieldName("settings");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(createSharedLinkWithSettingsArg.settings, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CreateSharedLinkWithSettingsArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SharedLinkSettings sharedLinkSettings = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("settings".equals(currentName)) {
                        sharedLinkSettings = (SharedLinkSettings) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    CreateSharedLinkWithSettingsArg createSharedLinkWithSettingsArg = new CreateSharedLinkWithSettingsArg(str2, sharedLinkSettings);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(createSharedLinkWithSettingsArg, createSharedLinkWithSettingsArg.toStringMultiline());
                    return createSharedLinkWithSettingsArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public CreateSharedLinkWithSettingsArg(String str, SharedLinkSettings sharedLinkSettings) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("(/(.|[\\r\\n])*|id:.*)|(rev:[0-9a-f]{9,})|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            this.settings = sharedLinkSettings;
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public CreateSharedLinkWithSettingsArg(String str) {
        this(str, null);
    }

    public String getPath() {
        return this.path;
    }

    public SharedLinkSettings getSettings() {
        return this.settings;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, this.settings});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsArg r5 = (com.dropbox.core.p005v2.sharing.CreateSharedLinkWithSettingsArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.sharing.SharedLinkSettings r2 = r4.settings
            com.dropbox.core.v2.sharing.SharedLinkSettings r5 = r5.settings
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.CreateSharedLinkWithSettingsArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
