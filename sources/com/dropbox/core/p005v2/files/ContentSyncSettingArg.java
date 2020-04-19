package com.dropbox.core.p005v2.files;

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

/* renamed from: com.dropbox.core.v2.files.ContentSyncSettingArg */
public class ContentSyncSettingArg {

    /* renamed from: id */
    protected final String f91id;
    protected final SyncSettingArg syncSetting;

    /* renamed from: com.dropbox.core.v2.files.ContentSyncSettingArg$Serializer */
    public static class Serializer extends StructSerializer<ContentSyncSettingArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(ContentSyncSettingArg contentSyncSettingArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(contentSyncSettingArg.f91id, jsonGenerator);
            jsonGenerator.writeFieldName("sync_setting");
            com.dropbox.core.p005v2.files.SyncSettingArg.Serializer.INSTANCE.serialize(contentSyncSettingArg.syncSetting, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ContentSyncSettingArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SyncSettingArg syncSettingArg = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("sync_setting".equals(currentName)) {
                        syncSettingArg = com.dropbox.core.p005v2.files.SyncSettingArg.Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"id\" missing.");
                } else if (syncSettingArg != null) {
                    ContentSyncSettingArg contentSyncSettingArg = new ContentSyncSettingArg(str2, syncSettingArg);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(contentSyncSettingArg, contentSyncSettingArg.toStringMultiline());
                    return contentSyncSettingArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"sync_setting\" missing.");
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

    public ContentSyncSettingArg(String str, SyncSettingArg syncSettingArg) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'id' is null");
        } else if (str.length() < 4) {
            throw new IllegalArgumentException("String 'id' is shorter than 4");
        } else if (Pattern.matches("id:.+", str)) {
            this.f91id = str;
            if (syncSettingArg != null) {
                this.syncSetting = syncSettingArg;
                return;
            }
            throw new IllegalArgumentException("Required value for 'syncSetting' is null");
        } else {
            throw new IllegalArgumentException("String 'id' does not match pattern");
        }
    }

    public String getId() {
        return this.f91id;
    }

    public SyncSettingArg getSyncSetting() {
        return this.syncSetting;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f91id, this.syncSetting});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.files.ContentSyncSettingArg r5 = (com.dropbox.core.p005v2.files.ContentSyncSettingArg) r5
            java.lang.String r2 = r4.f91id
            java.lang.String r3 = r5.f91id
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.files.SyncSettingArg r2 = r4.syncSetting
            com.dropbox.core.v2.files.SyncSettingArg r5 = r5.syncSetting
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.ContentSyncSettingArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
