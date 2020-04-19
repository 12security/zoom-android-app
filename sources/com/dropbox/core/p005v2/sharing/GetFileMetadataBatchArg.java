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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.GetFileMetadataBatchArg */
class GetFileMetadataBatchArg {
    protected final List<FileAction> actions;
    protected final List<String> files;

    /* renamed from: com.dropbox.core.v2.sharing.GetFileMetadataBatchArg$Serializer */
    static class Serializer extends StructSerializer<GetFileMetadataBatchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetFileMetadataBatchArg getFileMetadataBatchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("files");
            StoneSerializers.list(StoneSerializers.string()).serialize(getFileMetadataBatchArg.files, jsonGenerator);
            if (getFileMetadataBatchArg.actions != null) {
                jsonGenerator.writeFieldName("actions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(getFileMetadataBatchArg.actions, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetFileMetadataBatchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("files".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("actions".equals(currentName)) {
                        list2 = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    GetFileMetadataBatchArg getFileMetadataBatchArg = new GetFileMetadataBatchArg(list, list2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getFileMetadataBatchArg, getFileMetadataBatchArg.toStringMultiline());
                    return getFileMetadataBatchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"files\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetFileMetadataBatchArg(List<String> list, List<FileAction> list2) {
        if (list == null) {
            throw new IllegalArgumentException("Required value for 'files' is null");
        } else if (list.size() <= 100) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'files' is null");
                } else if (str.length() < 1) {
                    throw new IllegalArgumentException("Stringan item in list 'files' is shorter than 1");
                } else if (!Pattern.matches("((/|id:).*|nspath:[0-9]+:.*)|ns:[0-9]+(/.*)?", str)) {
                    throw new IllegalArgumentException("Stringan item in list 'files' does not match pattern");
                }
            }
            this.files = list;
            if (list2 != null) {
                for (FileAction fileAction : list2) {
                    if (fileAction == null) {
                        throw new IllegalArgumentException("An item in list 'actions' is null");
                    }
                }
            }
            this.actions = list2;
        } else {
            throw new IllegalArgumentException("List 'files' has more than 100 items");
        }
    }

    public GetFileMetadataBatchArg(List<String> list) {
        this(list, null);
    }

    public List<String> getFiles() {
        return this.files;
    }

    public List<FileAction> getActions() {
        return this.actions;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.files, this.actions});
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
            com.dropbox.core.v2.sharing.GetFileMetadataBatchArg r5 = (com.dropbox.core.p005v2.sharing.GetFileMetadataBatchArg) r5
            java.util.List<java.lang.String> r2 = r4.files
            java.util.List<java.lang.String> r3 = r5.files
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.GetFileMetadataBatchArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
