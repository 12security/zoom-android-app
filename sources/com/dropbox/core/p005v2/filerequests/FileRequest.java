package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.filerequests.FileRequest */
public class FileRequest {
    protected final Date created;
    protected final FileRequestDeadline deadline;
    protected final String destination;
    protected final long fileCount;

    /* renamed from: id */
    protected final String f82id;
    protected final boolean isOpen;
    protected final String title;
    protected final String url;

    /* renamed from: com.dropbox.core.v2.filerequests.FileRequest$Builder */
    public static class Builder {
        protected final Date created;
        protected FileRequestDeadline deadline;
        protected String destination;
        protected final long fileCount;

        /* renamed from: id */
        protected final String f83id;
        protected final boolean isOpen;
        protected final String title;
        protected final String url;

        protected Builder(String str, String str2, String str3, Date date, boolean z, long j) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'id' is null");
            } else if (str.length() < 1) {
                throw new IllegalArgumentException("String 'id' is shorter than 1");
            } else if (Pattern.matches("[-_0-9a-zA-Z]+", str)) {
                this.f83id = str;
                if (str2 == null) {
                    throw new IllegalArgumentException("Required value for 'url' is null");
                } else if (str2.length() >= 1) {
                    this.url = str2;
                    if (str3 == null) {
                        throw new IllegalArgumentException("Required value for 'title' is null");
                    } else if (str3.length() >= 1) {
                        this.title = str3;
                        if (date != null) {
                            this.created = LangUtil.truncateMillis(date);
                            this.isOpen = z;
                            this.fileCount = j;
                            this.destination = null;
                            this.deadline = null;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'created' is null");
                    } else {
                        throw new IllegalArgumentException("String 'title' is shorter than 1");
                    }
                } else {
                    throw new IllegalArgumentException("String 'url' is shorter than 1");
                }
            } else {
                throw new IllegalArgumentException("String 'id' does not match pattern");
            }
        }

        public Builder withDestination(String str) {
            if (str == null || Pattern.matches("/(.|[\\r\\n])*", str)) {
                this.destination = str;
                return this;
            }
            throw new IllegalArgumentException("String 'destination' does not match pattern");
        }

        public Builder withDeadline(FileRequestDeadline fileRequestDeadline) {
            this.deadline = fileRequestDeadline;
            return this;
        }

        public FileRequest build() {
            FileRequest fileRequest = new FileRequest(this.f83id, this.url, this.title, this.created, this.isOpen, this.fileCount, this.destination, this.deadline);
            return fileRequest;
        }
    }

    /* renamed from: com.dropbox.core.v2.filerequests.FileRequest$Serializer */
    static class Serializer extends StructSerializer<FileRequest> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileRequest fileRequest, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(fileRequest.f82id, jsonGenerator);
            jsonGenerator.writeFieldName("url");
            StoneSerializers.string().serialize(fileRequest.url, jsonGenerator);
            jsonGenerator.writeFieldName("title");
            StoneSerializers.string().serialize(fileRequest.title, jsonGenerator);
            jsonGenerator.writeFieldName("created");
            StoneSerializers.timestamp().serialize(fileRequest.created, jsonGenerator);
            jsonGenerator.writeFieldName("is_open");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(fileRequest.isOpen), jsonGenerator);
            jsonGenerator.writeFieldName("file_count");
            StoneSerializers.int64().serialize(Long.valueOf(fileRequest.fileCount), jsonGenerator);
            if (fileRequest.destination != null) {
                jsonGenerator.writeFieldName(Param.DESTINATION);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileRequest.destination, jsonGenerator);
            }
            if (fileRequest.deadline != null) {
                jsonGenerator.writeFieldName("deadline");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fileRequest.deadline, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileRequest deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l = null;
                String str2 = null;
                String str3 = null;
                String str4 = null;
                Date date = null;
                String str5 = null;
                FileRequestDeadline fileRequestDeadline = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("url".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("title".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser);
                    } else if ("is_open".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("file_count".equals(currentName)) {
                        l = (Long) StoneSerializers.int64().deserialize(jsonParser);
                    } else if (Param.DESTINATION.equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("deadline".equals(currentName)) {
                        fileRequestDeadline = (FileRequestDeadline) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"url\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"title\" missing.");
                } else if (date == null) {
                    throw new JsonParseException(jsonParser, "Required field \"created\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"is_open\" missing.");
                } else if (l != null) {
                    FileRequest fileRequest = new FileRequest(str2, str3, str4, date, bool.booleanValue(), l.longValue(), str5, fileRequestDeadline);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileRequest, fileRequest.toStringMultiline());
                    return fileRequest;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"file_count\" missing.");
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

    public FileRequest(String str, String str2, String str3, Date date, boolean z, long j, String str4, FileRequestDeadline fileRequestDeadline) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'id' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'id' is shorter than 1");
        } else if (Pattern.matches("[-_0-9a-zA-Z]+", str)) {
            this.f82id = str;
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'url' is null");
            } else if (str2.length() >= 1) {
                this.url = str2;
                if (str3 == null) {
                    throw new IllegalArgumentException("Required value for 'title' is null");
                } else if (str3.length() >= 1) {
                    this.title = str3;
                    if (str4 == null || Pattern.matches("/(.|[\\r\\n])*", str4)) {
                        this.destination = str4;
                        if (date != null) {
                            this.created = LangUtil.truncateMillis(date);
                            this.deadline = fileRequestDeadline;
                            this.isOpen = z;
                            this.fileCount = j;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'created' is null");
                    }
                    throw new IllegalArgumentException("String 'destination' does not match pattern");
                } else {
                    throw new IllegalArgumentException("String 'title' is shorter than 1");
                }
            } else {
                throw new IllegalArgumentException("String 'url' is shorter than 1");
            }
        } else {
            throw new IllegalArgumentException("String 'id' does not match pattern");
        }
    }

    public FileRequest(String str, String str2, String str3, Date date, boolean z, long j) {
        this(str, str2, str3, date, z, j, null, null);
    }

    public String getId() {
        return this.f82id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTitle() {
        return this.title;
    }

    public Date getCreated() {
        return this.created;
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }

    public long getFileCount() {
        return this.fileCount;
    }

    public String getDestination() {
        return this.destination;
    }

    public FileRequestDeadline getDeadline() {
        return this.deadline;
    }

    public static Builder newBuilder(String str, String str2, String str3, Date date, boolean z, long j) {
        Builder builder = new Builder(str, str2, str3, date, z, j);
        return builder;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f82id, this.url, this.title, this.destination, this.created, this.deadline, Boolean.valueOf(this.isOpen), Long.valueOf(this.fileCount)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0070, code lost:
        if (r2.equals(r7) == false) goto L_0x0073;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0075
            com.dropbox.core.v2.filerequests.FileRequest r7 = (com.dropbox.core.p005v2.filerequests.FileRequest) r7
            java.lang.String r2 = r6.f82id
            java.lang.String r3 = r7.f82id
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0073
        L_0x0024:
            java.lang.String r2 = r6.url
            java.lang.String r3 = r7.url
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0073
        L_0x0030:
            java.lang.String r2 = r6.title
            java.lang.String r3 = r7.title
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0073
        L_0x003c:
            java.util.Date r2 = r6.created
            java.util.Date r3 = r7.created
            if (r2 == r3) goto L_0x0048
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0073
        L_0x0048:
            boolean r2 = r6.isOpen
            boolean r3 = r7.isOpen
            if (r2 != r3) goto L_0x0073
            long r2 = r6.fileCount
            long r4 = r7.fileCount
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x0073
            java.lang.String r2 = r6.destination
            java.lang.String r3 = r7.destination
            if (r2 == r3) goto L_0x0064
            if (r2 == 0) goto L_0x0073
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0073
        L_0x0064:
            com.dropbox.core.v2.filerequests.FileRequestDeadline r2 = r6.deadline
            com.dropbox.core.v2.filerequests.FileRequestDeadline r7 = r7.deadline
            if (r2 == r7) goto L_0x0074
            if (r2 == 0) goto L_0x0073
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x0073
            goto L_0x0074
        L_0x0073:
            r0 = 0
        L_0x0074:
            return r0
        L_0x0075:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.filerequests.FileRequest.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
