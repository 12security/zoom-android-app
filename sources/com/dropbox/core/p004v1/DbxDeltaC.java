package com.dropbox.core.p004v1;

import com.box.androidsdk.content.BoxApiMetadata;
import com.box.androidsdk.content.models.BoxList;
import com.dropbox.core.json.JsonArrayReader;
import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.json.JsonReader.FieldMapping;
import com.dropbox.core.json.JsonReader.FieldMapping.Builder;
import com.dropbox.core.util.Collector;
import com.dropbox.core.util.DumpWriter;
import com.dropbox.core.util.Dumpable;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v1.DbxDeltaC */
public class DbxDeltaC<C> extends Dumpable {
    public final String cursor;
    public final C entries;
    public final boolean hasMore;
    public final boolean reset;

    /* renamed from: com.dropbox.core.v1.DbxDeltaC$Entry */
    public static final class Entry<MD extends Dumpable> extends Dumpable {
        public final String lcPath;
        public final MD metadata;

        /* renamed from: com.dropbox.core.v1.DbxDeltaC$Entry$Reader */
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
                                throw new JsonReadException("expecting a two-element array of [path, metadata], found more than two elements", expectArrayStart);
                            } catch (JsonReadException e) {
                                throw e.addArrayContext(1);
                            }
                        } else {
                            throw new JsonReadException("expecting a two-element array of [path, metadata], found a one-element array", expectArrayStart);
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

    /* renamed from: com.dropbox.core.v1.DbxDeltaC$Reader */
    public static final class Reader<C, MD extends Dumpable> extends JsonReader<DbxDeltaC<C>> {

        /* renamed from: FM */
        private static final FieldMapping f68FM;
        private static final int FM_cursor = 2;
        private static final int FM_entries = 1;
        private static final int FM_has_more = 3;
        private static final int FM_reset = 0;
        public final Collector<Entry<MD>, C> entryCollector;
        public final JsonReader<MD> metadataReader;

        public Reader(JsonReader<MD> jsonReader, Collector<Entry<MD>, C> collector) {
            this.metadataReader = jsonReader;
            this.entryCollector = collector;
        }

        public DbxDeltaC<C> read(JsonParser jsonParser) throws IOException, JsonReadException {
            return read(jsonParser, this.metadataReader, this.entryCollector);
        }

        public static <C, MD extends Dumpable> DbxDeltaC<C> read(JsonParser jsonParser, JsonReader<MD> jsonReader, Collector<Entry<MD>, C> collector) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            Boolean bool = null;
            Object obj = null;
            String str = null;
            Boolean bool2 = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                JsonReader.nextToken(jsonParser);
                int i = f68FM.get(currentName);
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
                            obj = JsonArrayReader.m18mk(new Reader(jsonReader), collector).readField(jsonParser, currentName, obj);
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
            } else if (obj == null) {
                throw new JsonReadException("missing field \"entries\"", expectObjectStart);
            } else if (str == null) {
                throw new JsonReadException("missing field \"cursor\"", expectObjectStart);
            } else if (bool2 != null) {
                return new DbxDeltaC<>(bool.booleanValue(), obj, str, bool2.booleanValue());
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
            f68FM = builder.build();
        }
    }

    public DbxDeltaC(boolean z, C c, String str, boolean z2) {
        this.reset = z;
        this.entries = c;
        this.cursor = str;
        this.hasMore = z2;
    }

    /* access modifiers changed from: protected */
    public void dumpFields(DumpWriter dumpWriter) {
        dumpWriter.mo10658f("reset").mo10673v(this.reset);
        dumpWriter.mo10658f("cursor").mo10671v(this.cursor);
        dumpWriter.mo10658f(BoxList.FIELD_ENTRIES).mo10673v(this.hasMore);
    }
}
