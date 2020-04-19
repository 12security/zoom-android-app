package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFile;
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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.GetFileMetadataArg */
class GetFileMetadataArg {
    protected final List<FileAction> actions;
    protected final String file;

    /* renamed from: com.dropbox.core.v2.sharing.GetFileMetadataArg$Serializer */
    static class Serializer extends StructSerializer<GetFileMetadataArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetFileMetadataArg getFileMetadataArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxFile.TYPE);
            StoneSerializers.string().serialize(getFileMetadataArg.file, jsonGenerator);
            if (getFileMetadataArg.actions != null) {
                jsonGenerator.writeFieldName("actions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(getFileMetadataArg.actions, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetFileMetadataArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxFile.TYPE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("actions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    GetFileMetadataArg getFileMetadataArg = new GetFileMetadataArg(str2, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getFileMetadataArg, getFileMetadataArg.toStringMultiline());
                    return getFileMetadataArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"file\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetFileMetadataArg(String str, List<FileAction> list) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'file' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'file' is shorter than 1");
        } else if (Pattern.matches("((/|id:).*|nspath:[0-9]+:.*)|ns:[0-9]+(/.*)?", str)) {
            this.file = str;
            if (list != null) {
                for (FileAction fileAction : list) {
                    if (fileAction == null) {
                        throw new IllegalArgumentException("An item in list 'actions' is null");
                    }
                }
            }
            this.actions = list;
        } else {
            throw new IllegalArgumentException("String 'file' does not match pattern");
        }
    }

    public GetFileMetadataArg(String str) {
        this(str, null);
    }

    public String getFile() {
        return this.file;
    }

    public List<FileAction> getActions() {
        return this.actions;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.file, this.actions});
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
            com.dropbox.core.v2.sharing.GetFileMetadataArg r5 = (com.dropbox.core.p005v2.sharing.GetFileMetadataArg) r5
            java.lang.String r2 = r4.file
            java.lang.String r3 = r5.file
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.util.List<com.dropbox.core.v2.sharing.FileAction> r2 = r4.actions
            java.util.List<com.dropbox.core.v2.sharing.FileAction> r5 = r5.actions
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.GetFileMetadataArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
