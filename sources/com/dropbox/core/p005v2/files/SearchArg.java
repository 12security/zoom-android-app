package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.actions.SearchIntents;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.SearchArg */
class SearchArg {
    protected final long maxResults;
    protected final SearchMode mode;
    protected final String path;
    protected final String query;
    protected final long start;

    /* renamed from: com.dropbox.core.v2.files.SearchArg$Builder */
    public static class Builder {
        protected long maxResults;
        protected SearchMode mode;
        protected final String path;
        protected final String query;
        protected long start;

        protected Builder(String str, String str2) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'path' is null");
            } else if (Pattern.matches("(/(.|[\\r\\n])*)?|id:.*|(ns:[0-9]+(/.*)?)", str)) {
                this.path = str;
                if (str2 != null) {
                    this.query = str2;
                    this.start = 0;
                    this.maxResults = 100;
                    this.mode = SearchMode.FILENAME;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'query' is null");
            } else {
                throw new IllegalArgumentException("String 'path' does not match pattern");
            }
        }

        public Builder withStart(Long l) {
            if (l != null) {
                this.start = l.longValue();
            } else {
                this.start = 0;
            }
            return this;
        }

        public Builder withMaxResults(Long l) {
            if (l.longValue() < 1) {
                throw new IllegalArgumentException("Number 'maxResults' is smaller than 1L");
            } else if (l.longValue() <= 1000) {
                if (l != null) {
                    this.maxResults = l.longValue();
                } else {
                    this.maxResults = 100;
                }
                return this;
            } else {
                throw new IllegalArgumentException("Number 'maxResults' is larger than 1000L");
            }
        }

        public Builder withMode(SearchMode searchMode) {
            if (searchMode != null) {
                this.mode = searchMode;
            } else {
                this.mode = SearchMode.FILENAME;
            }
            return this;
        }

        public SearchArg build() {
            SearchArg searchArg = new SearchArg(this.path, this.query, this.start, this.maxResults, this.mode);
            return searchArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.SearchArg$Serializer */
    static class Serializer extends StructSerializer<SearchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SearchArg searchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(searchArg.path, jsonGenerator);
            jsonGenerator.writeFieldName(SearchIntents.EXTRA_QUERY);
            StoneSerializers.string().serialize(searchArg.query, jsonGenerator);
            jsonGenerator.writeFieldName("start");
            StoneSerializers.uInt64().serialize(Long.valueOf(searchArg.start), jsonGenerator);
            jsonGenerator.writeFieldName("max_results");
            StoneSerializers.uInt64().serialize(Long.valueOf(searchArg.maxResults), jsonGenerator);
            jsonGenerator.writeFieldName("mode");
            Serializer.INSTANCE.serialize(searchArg.mode, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SearchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(0);
                Long valueOf2 = Long.valueOf(100);
                String str2 = null;
                String str3 = null;
                SearchMode searchMode = SearchMode.FILENAME;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (SearchIntents.EXTRA_QUERY.equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("start".equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("max_results".equals(currentName)) {
                        valueOf2 = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("mode".equals(currentName)) {
                        searchMode = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
                } else if (str3 != null) {
                    SearchArg searchArg = new SearchArg(str2, str3, valueOf.longValue(), valueOf2.longValue(), searchMode);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(searchArg, searchArg.toStringMultiline());
                    return searchArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"query\" missing.");
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

    public SearchArg(String str, String str2, long j, long j2, SearchMode searchMode) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("(/(.|[\\r\\n])*)?|id:.*|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            if (str2 != null) {
                this.query = str2;
                this.start = j;
                if (j2 < 1) {
                    throw new IllegalArgumentException("Number 'maxResults' is smaller than 1L");
                } else if (j2 <= 1000) {
                    this.maxResults = j2;
                    if (searchMode != null) {
                        this.mode = searchMode;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'mode' is null");
                } else {
                    throw new IllegalArgumentException("Number 'maxResults' is larger than 1000L");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'query' is null");
            }
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public SearchArg(String str, String str2) {
        this(str, str2, 0, 100, SearchMode.FILENAME);
    }

    public String getPath() {
        return this.path;
    }

    public String getQuery() {
        return this.query;
    }

    public long getStart() {
        return this.start;
    }

    public long getMaxResults() {
        return this.maxResults;
    }

    public SearchMode getMode() {
        return this.mode;
    }

    public static Builder newBuilder(String str, String str2) {
        return new Builder(str, str2);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, this.query, Long.valueOf(this.start), Long.valueOf(this.maxResults), this.mode});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
        if (r2.equals(r7) == false) goto L_0x004d;
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
            if (r2 == 0) goto L_0x004f
            com.dropbox.core.v2.files.SearchArg r7 = (com.dropbox.core.p005v2.files.SearchArg) r7
            java.lang.String r2 = r6.path
            java.lang.String r3 = r7.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004d
        L_0x0024:
            java.lang.String r2 = r6.query
            java.lang.String r3 = r7.query
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004d
        L_0x0030:
            long r2 = r6.start
            long r4 = r7.start
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x004d
            long r2 = r6.maxResults
            long r4 = r7.maxResults
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x004d
            com.dropbox.core.v2.files.SearchMode r2 = r6.mode
            com.dropbox.core.v2.files.SearchMode r7 = r7.mode
            if (r2 == r7) goto L_0x004e
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x004d
            goto L_0x004e
        L_0x004d:
            r0 = 0
        L_0x004e:
            return r0
        L_0x004f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.SearchArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
