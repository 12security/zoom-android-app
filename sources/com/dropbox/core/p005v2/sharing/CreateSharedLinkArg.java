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

/* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkArg */
class CreateSharedLinkArg {
    protected final String path;
    protected final PendingUploadMode pendingUpload;
    protected final boolean shortUrl;

    /* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkArg$Builder */
    public static class Builder {
        protected final String path;
        protected PendingUploadMode pendingUpload;
        protected boolean shortUrl;

        protected Builder(String str) {
            if (str != null) {
                this.path = str;
                this.shortUrl = false;
                this.pendingUpload = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'path' is null");
        }

        public Builder withShortUrl(Boolean bool) {
            if (bool != null) {
                this.shortUrl = bool.booleanValue();
            } else {
                this.shortUrl = false;
            }
            return this;
        }

        public Builder withPendingUpload(PendingUploadMode pendingUploadMode) {
            this.pendingUpload = pendingUploadMode;
            return this;
        }

        public CreateSharedLinkArg build() {
            return new CreateSharedLinkArg(this.path, this.shortUrl, this.pendingUpload);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkArg$Serializer */
    static class Serializer extends StructSerializer<CreateSharedLinkArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateSharedLinkArg createSharedLinkArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(createSharedLinkArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("short_url");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(createSharedLinkArg.shortUrl), jsonGenerator);
            if (createSharedLinkArg.pendingUpload != null) {
                jsonGenerator.writeFieldName("pending_upload");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(createSharedLinkArg.pendingUpload, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CreateSharedLinkArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                PendingUploadMode pendingUploadMode = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("short_url".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("pending_upload".equals(currentName)) {
                        pendingUploadMode = (PendingUploadMode) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    CreateSharedLinkArg createSharedLinkArg = new CreateSharedLinkArg(str2, valueOf.booleanValue(), pendingUploadMode);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(createSharedLinkArg, createSharedLinkArg.toStringMultiline());
                    return createSharedLinkArg;
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

    public CreateSharedLinkArg(String str, boolean z, PendingUploadMode pendingUploadMode) {
        if (str != null) {
            this.path = str;
            this.shortUrl = z;
            this.pendingUpload = pendingUploadMode;
            return;
        }
        throw new IllegalArgumentException("Required value for 'path' is null");
    }

    public CreateSharedLinkArg(String str) {
        this(str, false, null);
    }

    public String getPath() {
        return this.path;
    }

    public boolean getShortUrl() {
        return this.shortUrl;
    }

    public PendingUploadMode getPendingUpload() {
        return this.pendingUpload;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, Boolean.valueOf(this.shortUrl), this.pendingUpload});
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
            com.dropbox.core.v2.sharing.CreateSharedLinkArg r5 = (com.dropbox.core.p005v2.sharing.CreateSharedLinkArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0039
        L_0x0024:
            boolean r2 = r4.shortUrl
            boolean r3 = r5.shortUrl
            if (r2 != r3) goto L_0x0039
            com.dropbox.core.v2.sharing.PendingUploadMode r2 = r4.pendingUpload
            com.dropbox.core.v2.sharing.PendingUploadMode r5 = r5.pendingUpload
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.CreateSharedLinkArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
