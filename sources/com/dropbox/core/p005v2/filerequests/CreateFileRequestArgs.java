package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.filerequests.CreateFileRequestArgs */
class CreateFileRequestArgs {
    protected final FileRequestDeadline deadline;
    protected final String destination;
    protected final boolean open;
    protected final String title;

    /* renamed from: com.dropbox.core.v2.filerequests.CreateFileRequestArgs$Builder */
    public static class Builder {
        protected FileRequestDeadline deadline;
        protected final String destination;
        protected boolean open;
        protected final String title;

        protected Builder(String str, String str2) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'title' is null");
            } else if (str.length() >= 1) {
                this.title = str;
                if (str2 == null) {
                    throw new IllegalArgumentException("Required value for 'destination' is null");
                } else if (Pattern.matches("/(.|[\\r\\n])*", str2)) {
                    this.destination = str2;
                    this.deadline = null;
                    this.open = true;
                } else {
                    throw new IllegalArgumentException("String 'destination' does not match pattern");
                }
            } else {
                throw new IllegalArgumentException("String 'title' is shorter than 1");
            }
        }

        public Builder withDeadline(FileRequestDeadline fileRequestDeadline) {
            this.deadline = fileRequestDeadline;
            return this;
        }

        public Builder withOpen(Boolean bool) {
            if (bool != null) {
                this.open = bool.booleanValue();
            } else {
                this.open = true;
            }
            return this;
        }

        public CreateFileRequestArgs build() {
            return new CreateFileRequestArgs(this.title, this.destination, this.deadline, this.open);
        }
    }

    /* renamed from: com.dropbox.core.v2.filerequests.CreateFileRequestArgs$Serializer */
    static class Serializer extends StructSerializer<CreateFileRequestArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateFileRequestArgs createFileRequestArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("title");
            StoneSerializers.string().serialize(createFileRequestArgs.title, jsonGenerator);
            jsonGenerator.writeFieldName(Param.DESTINATION);
            StoneSerializers.string().serialize(createFileRequestArgs.destination, jsonGenerator);
            if (createFileRequestArgs.deadline != null) {
                jsonGenerator.writeFieldName("deadline");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(createFileRequestArgs.deadline, jsonGenerator);
            }
            jsonGenerator.writeFieldName("open");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(createFileRequestArgs.open), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CreateFileRequestArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                FileRequestDeadline fileRequestDeadline = null;
                Boolean valueOf = Boolean.valueOf(true);
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("title".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (Param.DESTINATION.equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("deadline".equals(currentName)) {
                        fileRequestDeadline = (FileRequestDeadline) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("open".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"title\" missing.");
                } else if (str3 != null) {
                    CreateFileRequestArgs createFileRequestArgs = new CreateFileRequestArgs(str2, str3, fileRequestDeadline, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(createFileRequestArgs, createFileRequestArgs.toStringMultiline());
                    return createFileRequestArgs;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"destination\" missing.");
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

    public CreateFileRequestArgs(String str, String str2, FileRequestDeadline fileRequestDeadline, boolean z) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'title' is null");
        } else if (str.length() >= 1) {
            this.title = str;
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'destination' is null");
            } else if (Pattern.matches("/(.|[\\r\\n])*", str2)) {
                this.destination = str2;
                this.deadline = fileRequestDeadline;
                this.open = z;
            } else {
                throw new IllegalArgumentException("String 'destination' does not match pattern");
            }
        } else {
            throw new IllegalArgumentException("String 'title' is shorter than 1");
        }
    }

    public CreateFileRequestArgs(String str, String str2) {
        this(str, str2, null, true);
    }

    public String getTitle() {
        return this.title;
    }

    public String getDestination() {
        return this.destination;
    }

    public FileRequestDeadline getDeadline() {
        return this.deadline;
    }

    public boolean getOpen() {
        return this.open;
    }

    public static Builder newBuilder(String str, String str2) {
        return new Builder(str, str2);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.title, this.destination, this.deadline, Boolean.valueOf(this.open)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (r2.equals(r3) == false) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0042, code lost:
        if (r4.open != r5.open) goto L_0x0045;
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
            if (r2 == 0) goto L_0x0047
            com.dropbox.core.v2.filerequests.CreateFileRequestArgs r5 = (com.dropbox.core.p005v2.filerequests.CreateFileRequestArgs) r5
            java.lang.String r2 = r4.title
            java.lang.String r3 = r5.title
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0024:
            java.lang.String r2 = r4.destination
            java.lang.String r3 = r5.destination
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0030:
            com.dropbox.core.v2.filerequests.FileRequestDeadline r2 = r4.deadline
            com.dropbox.core.v2.filerequests.FileRequestDeadline r3 = r5.deadline
            if (r2 == r3) goto L_0x003e
            if (r2 == 0) goto L_0x0045
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x003e:
            boolean r2 = r4.open
            boolean r5 = r5.open
            if (r2 != r5) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            r0 = 0
        L_0x0046:
            return r0
        L_0x0047:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.filerequests.CreateFileRequestArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
