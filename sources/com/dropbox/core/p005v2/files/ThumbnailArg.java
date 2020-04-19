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

/* renamed from: com.dropbox.core.v2.files.ThumbnailArg */
public class ThumbnailArg {
    protected final ThumbnailFormat format;
    protected final ThumbnailMode mode;
    protected final String path;
    protected final ThumbnailSize size;

    /* renamed from: com.dropbox.core.v2.files.ThumbnailArg$Builder */
    public static class Builder {
        protected ThumbnailFormat format;
        protected ThumbnailMode mode;
        protected final String path;
        protected ThumbnailSize size;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'path' is null");
            } else if (Pattern.matches("(/(.|[\\r\\n])*|id:.*)|(rev:[0-9a-f]{9,})|(ns:[0-9]+(/.*)?)", str)) {
                this.path = str;
                this.format = ThumbnailFormat.JPEG;
                this.size = ThumbnailSize.W64H64;
                this.mode = ThumbnailMode.STRICT;
            } else {
                throw new IllegalArgumentException("String 'path' does not match pattern");
            }
        }

        public Builder withFormat(ThumbnailFormat thumbnailFormat) {
            if (thumbnailFormat != null) {
                this.format = thumbnailFormat;
            } else {
                this.format = ThumbnailFormat.JPEG;
            }
            return this;
        }

        public Builder withSize(ThumbnailSize thumbnailSize) {
            if (thumbnailSize != null) {
                this.size = thumbnailSize;
            } else {
                this.size = ThumbnailSize.W64H64;
            }
            return this;
        }

        public Builder withMode(ThumbnailMode thumbnailMode) {
            if (thumbnailMode != null) {
                this.mode = thumbnailMode;
            } else {
                this.mode = ThumbnailMode.STRICT;
            }
            return this;
        }

        public ThumbnailArg build() {
            return new ThumbnailArg(this.path, this.format, this.size, this.mode);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.ThumbnailArg$Serializer */
    static class Serializer extends StructSerializer<ThumbnailArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ThumbnailArg thumbnailArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(thumbnailArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("format");
            Serializer.INSTANCE.serialize(thumbnailArg.format, jsonGenerator);
            jsonGenerator.writeFieldName("size");
            Serializer.INSTANCE.serialize(thumbnailArg.size, jsonGenerator);
            jsonGenerator.writeFieldName("mode");
            Serializer.INSTANCE.serialize(thumbnailArg.mode, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ThumbnailArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                ThumbnailFormat thumbnailFormat = ThumbnailFormat.JPEG;
                ThumbnailSize thumbnailSize = ThumbnailSize.W64H64;
                ThumbnailMode thumbnailMode = ThumbnailMode.STRICT;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("format".equals(currentName)) {
                        thumbnailFormat = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("size".equals(currentName)) {
                        thumbnailSize = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("mode".equals(currentName)) {
                        thumbnailMode = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ThumbnailArg thumbnailArg = new ThumbnailArg(str2, thumbnailFormat, thumbnailSize, thumbnailMode);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(thumbnailArg, thumbnailArg.toStringMultiline());
                    return thumbnailArg;
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

    public ThumbnailArg(String str, ThumbnailFormat thumbnailFormat, ThumbnailSize thumbnailSize, ThumbnailMode thumbnailMode) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("(/(.|[\\r\\n])*|id:.*)|(rev:[0-9a-f]{9,})|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            if (thumbnailFormat != null) {
                this.format = thumbnailFormat;
                if (thumbnailSize != null) {
                    this.size = thumbnailSize;
                    if (thumbnailMode != null) {
                        this.mode = thumbnailMode;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'mode' is null");
                }
                throw new IllegalArgumentException("Required value for 'size' is null");
            }
            throw new IllegalArgumentException("Required value for 'format' is null");
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public ThumbnailArg(String str) {
        this(str, ThumbnailFormat.JPEG, ThumbnailSize.W64H64, ThumbnailMode.STRICT);
    }

    public String getPath() {
        return this.path;
    }

    public ThumbnailFormat getFormat() {
        return this.format;
    }

    public ThumbnailSize getSize() {
        return this.size;
    }

    public ThumbnailMode getMode() {
        return this.mode;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, this.format, this.size, this.mode});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0046, code lost:
        if (r2.equals(r5) == false) goto L_0x0049;
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
            if (r2 == 0) goto L_0x004b
            com.dropbox.core.v2.files.ThumbnailArg r5 = (com.dropbox.core.p005v2.files.ThumbnailArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0024:
            com.dropbox.core.v2.files.ThumbnailFormat r2 = r4.format
            com.dropbox.core.v2.files.ThumbnailFormat r3 = r5.format
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0030:
            com.dropbox.core.v2.files.ThumbnailSize r2 = r4.size
            com.dropbox.core.v2.files.ThumbnailSize r3 = r5.size
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x003c:
            com.dropbox.core.v2.files.ThumbnailMode r2 = r4.mode
            com.dropbox.core.v2.files.ThumbnailMode r5 = r5.mode
            if (r2 == r5) goto L_0x004a
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r0 = 0
        L_0x004a:
            return r0
        L_0x004b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.ThumbnailArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
