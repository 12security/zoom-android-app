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

/* renamed from: com.dropbox.core.v2.filerequests.UpdateFileRequestArgs */
class UpdateFileRequestArgs {
    protected final UpdateFileRequestDeadline deadline;
    protected final String destination;

    /* renamed from: id */
    protected final String f86id;
    protected final Boolean open;
    protected final String title;

    /* renamed from: com.dropbox.core.v2.filerequests.UpdateFileRequestArgs$Builder */
    public static class Builder {
        protected UpdateFileRequestDeadline deadline;
        protected String destination;

        /* renamed from: id */
        protected final String f87id;
        protected Boolean open;
        protected String title;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'id' is null");
            } else if (str.length() < 1) {
                throw new IllegalArgumentException("String 'id' is shorter than 1");
            } else if (Pattern.matches("[-_0-9a-zA-Z]+", str)) {
                this.f87id = str;
                this.title = null;
                this.destination = null;
                this.deadline = UpdateFileRequestDeadline.NO_UPDATE;
                this.open = null;
            } else {
                throw new IllegalArgumentException("String 'id' does not match pattern");
            }
        }

        public Builder withTitle(String str) {
            if (str == null || str.length() >= 1) {
                this.title = str;
                return this;
            }
            throw new IllegalArgumentException("String 'title' is shorter than 1");
        }

        public Builder withDestination(String str) {
            if (str == null || Pattern.matches("/(.|[\\r\\n])*", str)) {
                this.destination = str;
                return this;
            }
            throw new IllegalArgumentException("String 'destination' does not match pattern");
        }

        public Builder withDeadline(UpdateFileRequestDeadline updateFileRequestDeadline) {
            if (updateFileRequestDeadline != null) {
                this.deadline = updateFileRequestDeadline;
            } else {
                this.deadline = UpdateFileRequestDeadline.NO_UPDATE;
            }
            return this;
        }

        public Builder withOpen(Boolean bool) {
            this.open = bool;
            return this;
        }

        public UpdateFileRequestArgs build() {
            UpdateFileRequestArgs updateFileRequestArgs = new UpdateFileRequestArgs(this.f87id, this.title, this.destination, this.deadline, this.open);
            return updateFileRequestArgs;
        }
    }

    /* renamed from: com.dropbox.core.v2.filerequests.UpdateFileRequestArgs$Serializer */
    static class Serializer extends StructSerializer<UpdateFileRequestArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UpdateFileRequestArgs updateFileRequestArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(updateFileRequestArgs.f86id, jsonGenerator);
            if (updateFileRequestArgs.title != null) {
                jsonGenerator.writeFieldName("title");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(updateFileRequestArgs.title, jsonGenerator);
            }
            if (updateFileRequestArgs.destination != null) {
                jsonGenerator.writeFieldName(Param.DESTINATION);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(updateFileRequestArgs.destination, jsonGenerator);
            }
            jsonGenerator.writeFieldName("deadline");
            Serializer.INSTANCE.serialize(updateFileRequestArgs.deadline, jsonGenerator);
            if (updateFileRequestArgs.open != null) {
                jsonGenerator.writeFieldName("open");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(updateFileRequestArgs.open, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UpdateFileRequestArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                Boolean bool = null;
                UpdateFileRequestDeadline updateFileRequestDeadline = UpdateFileRequestDeadline.NO_UPDATE;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("title".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if (Param.DESTINATION.equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("deadline".equals(currentName)) {
                        updateFileRequestDeadline = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("open".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    UpdateFileRequestArgs updateFileRequestArgs = new UpdateFileRequestArgs(str2, str3, str4, updateFileRequestDeadline, bool);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(updateFileRequestArgs, updateFileRequestArgs.toStringMultiline());
                    return updateFileRequestArgs;
                }
                throw new JsonParseException(jsonParser, "Required field \"id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UpdateFileRequestArgs(String str, String str2, String str3, UpdateFileRequestDeadline updateFileRequestDeadline, Boolean bool) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'id' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'id' is shorter than 1");
        } else if (Pattern.matches("[-_0-9a-zA-Z]+", str)) {
            this.f86id = str;
            if (str2 == null || str2.length() >= 1) {
                this.title = str2;
                if (str3 == null || Pattern.matches("/(.|[\\r\\n])*", str3)) {
                    this.destination = str3;
                    if (updateFileRequestDeadline != null) {
                        this.deadline = updateFileRequestDeadline;
                        this.open = bool;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'deadline' is null");
                }
                throw new IllegalArgumentException("String 'destination' does not match pattern");
            }
            throw new IllegalArgumentException("String 'title' is shorter than 1");
        } else {
            throw new IllegalArgumentException("String 'id' does not match pattern");
        }
    }

    public UpdateFileRequestArgs(String str) {
        this(str, null, null, UpdateFileRequestDeadline.NO_UPDATE, null);
    }

    public String getId() {
        return this.f86id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDestination() {
        return this.destination;
    }

    public UpdateFileRequestDeadline getDeadline() {
        return this.deadline;
    }

    public Boolean getOpen() {
        return this.open;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f86id, this.title, this.destination, this.deadline, this.open});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0058, code lost:
        if (r2.equals(r5) == false) goto L_0x005b;
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
            if (r2 == 0) goto L_0x005d
            com.dropbox.core.v2.filerequests.UpdateFileRequestArgs r5 = (com.dropbox.core.p005v2.filerequests.UpdateFileRequestArgs) r5
            java.lang.String r2 = r4.f86id
            java.lang.String r3 = r5.f86id
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x0024:
            java.lang.String r2 = r4.title
            java.lang.String r3 = r5.title
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x005b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x0032:
            java.lang.String r2 = r4.destination
            java.lang.String r3 = r5.destination
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x005b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x0040:
            com.dropbox.core.v2.filerequests.UpdateFileRequestDeadline r2 = r4.deadline
            com.dropbox.core.v2.filerequests.UpdateFileRequestDeadline r3 = r5.deadline
            if (r2 == r3) goto L_0x004c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x004c:
            java.lang.Boolean r2 = r4.open
            java.lang.Boolean r5 = r5.open
            if (r2 == r5) goto L_0x005c
            if (r2 == 0) goto L_0x005b
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x005b
            goto L_0x005c
        L_0x005b:
            r0 = 0
        L_0x005c:
            return r0
        L_0x005d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.filerequests.UpdateFileRequestArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
