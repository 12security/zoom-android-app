package com.dropbox.core.p004v1;

import com.box.androidsdk.content.BoxApiMetadata;
import com.box.androidsdk.content.models.BoxList;
import com.dropbox.core.json.JsonArrayReader;
import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.json.JsonReader.FieldMapping;
import com.dropbox.core.json.JsonReader.FieldMapping.Builder;
import com.dropbox.core.util.DumpWriter;
import com.dropbox.core.util.Dumpable;
import com.dropbox.core.util.StringUtil;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.List;

/* renamed from: com.dropbox.core.v1.DbxDelta */
public final class DbxDelta<MD extends Dumpable> extends Dumpable {
    public final String cursor;
    public final List<Entry<MD>> entries;
    public final boolean hasMore;
    public final boolean reset;

    /* renamed from: com.dropbox.core.v1.DbxDelta$Entry */
    public static final class Entry<MD extends Dumpable> extends Dumpable {
        public final String lcPath;
        public final MD metadata;

        /* renamed from: com.dropbox.core.v1.DbxDelta$Entry$Reader */
        public static final class Reader<MD extends Dumpable> extends JsonReader<Entry<MD>> {
            public final JsonReader<MD> metadataReader;

            public Reader(JsonReader<MD> jsonReader) {
                this.metadataReader = jsonReader;
            }

            public Entry<MD> read(JsonParser jsonParser) throws IOException, JsonReadException {
                return read(jsonParser, this.metadataReader);
            }

            public static <MD extends Dumpable> Entry<MD> read(JsonParser jsonParser, JsonReader<MD> jsonReader) throws IOException, JsonReadException {
                JsonLocation expectArrayStart = JsonReader.expectArrayStart(jsonParser);
                if (!JsonReader.isArrayEnd(jsonParser)) {
                    try {
                        String str = (String) JsonReader.StringReader.read(jsonParser);
                        if (!JsonReader.isArrayEnd(jsonParser)) {
                            try {
                                Dumpable dumpable = (Dumpable) jsonReader.readOptional(jsonParser);
                                if (JsonReader.isArrayEnd(jsonParser)) {
                                    jsonParser.nextToken();
                                    return new Entry<>(str, dumpable);
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("expecting a two-element array of [path, metadata], found non \"]\" token after the two elements: ");
                                sb.append(jsonParser.getCurrentToken());
                                throw new JsonReadException(sb.toString(), expectArrayStart);
                            } catch (JsonReadException e) {
                                throw e.addArrayContext(1);
                            }
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("expecting a two-element array of [path, metadata], found a one-element array: ");
                            sb2.append(StringUtil.m33jq(str));
                            throw new JsonReadException(sb2.toString(), expectArrayStart);
                        }
                    } catch (JsonReadException e2) {
                        throw e2.addArrayContext(0);
                    }
                } else {
                    throw new JsonReadException("expecting a two-element array of [path, metadata], found a zero-element array", expectArrayStart);
                }
            }
        }

        public Entry(String str, MD md) {
            this.lcPath = str;
            this.metadata = md;
        }

        /* access modifiers changed from: protected */
        public void dumpFields(DumpWriter dumpWriter) {
            dumpWriter.mo10658f("lcPath").mo10671v(this.lcPath);
            dumpWriter.mo10658f(BoxApiMetadata.BOX_API_METADATA).mo10668v((Dumpable) this.metadata);
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxDelta$Reader */
    public static final class Reader<MD extends Dumpable> extends JsonReader<DbxDelta<MD>> {

        /* renamed from: FM */
        private static final FieldMapping f67FM;
        private static final int FM_cursor = 2;
        private static final int FM_entries = 1;
        private static final int FM_has_more = 3;
        private static final int FM_reset = 0;
        public final JsonReader<MD> metadataReader;

        public Reader(JsonReader<MD> jsonReader) {
            this.metadataReader = jsonReader;
        }

        public DbxDelta<MD> read(JsonParser jsonParser) throws IOException, JsonReadException {
            return read(jsonParser, this.metadataReader);
        }

        public static <MD extends Dumpable> DbxDelta<MD> read(JsonParser jsonParser, JsonReader<MD> jsonReader) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            Boolean bool = null;
            List list = null;
            String str = null;
            Boolean bool2 = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                JsonReader.nextToken(jsonParser);
                int i = f67FM.get(currentName);
                if (i == -1) {
                    try {
                        JsonReader.skipValue(jsonParser);
                    } catch (JsonReadException e) {
                        throw e.addFieldContext(currentName);
                    }
                } else {
                    switch (i) {
                        case 0:
                            bool = (Boolean) JsonReader.BooleanReader.readField(jsonParser, currentName, bool);
                            break;
                        case 1:
                            list = (List) JsonArrayReader.m17mk(new Reader(jsonReader)).readField(jsonParser, currentName, list);
                            break;
                        case 2:
                            str = (String) JsonReader.StringReader.readField(jsonParser, currentName, str);
                            break;
                        case 3:
                            bool2 = (Boolean) JsonReader.BooleanReader.readField(jsonParser, currentName, bool2);
                            break;
                        default:
                            StringBuilder sb = new StringBuilder();
                            sb.append("bad index: ");
                            sb.append(i);
                            sb.append(", field = \"");
                            sb.append(currentName);
                            sb.append("\"");
                            throw new AssertionError(sb.toString());
                    }
                }
            }
            JsonReader.expectObjectEnd(jsonParser);
            if (bool == null) {
                throw new JsonReadException("missing field \"path\"", expectObjectStart);
            } else if (list == null) {
                throw new JsonReadException("missing field \"entries\"", expectObjectStart);
            } else if (str == null) {
                throw new JsonReadException("missing field \"cursor\"", expectObjectStart);
            } else if (bool2 != null) {
                return new DbxDelta<>(bool.booleanValue(), list, str, bool2.booleanValue());
            } else {
                throw new JsonReadException("missing field \"has_more\"", expectObjectStart);
            }
        }

        static {
            Builder builder = new Builder();
            builder.add("reset", 0);
            builder.add(BoxList.FIELD_ENTRIES, 1);
            builder.add("cursor", 2);
            builder.add("has_more", 3);
            f67FM = builder.build();
        }
    }

    public DbxDelta(boolean z, List<Entry<MD>> list, String str, boolean z2) {
        this.reset = z;
        this.entries = list;
        this.cursor = str;
        this.hasMore = z2;
    }

    /* access modifiers changed from: protected */
    public void dumpFields(DumpWriter dumpWriter) {
        dumpWriter.mo10658f("reset").mo10673v(this.reset);
        dumpWriter.mo10658f("hasMore").mo10673v(this.hasMore);
        dumpWriter.mo10658f("cursor").mo10671v(this.cursor);
        dumpWriter.mo10658f(BoxList.FIELD_ENTRIES).mo10669v((Iterable<? extends Dumpable>) this.entries);
    }
}
