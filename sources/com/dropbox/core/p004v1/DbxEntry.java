package com.dropbox.core.p004v1;

import com.dropbox.core.json.JsonArrayReader;
import com.dropbox.core.json.JsonDateReader;
import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.json.JsonReader.FieldMapping;
import com.dropbox.core.json.JsonReader.FieldMapping.Builder;
import com.dropbox.core.util.Collector;
import com.dropbox.core.util.Collector.ArrayListCollector;
import com.dropbox.core.util.DumpWriter;
import com.dropbox.core.util.Dumpable;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v1.DbxEntry */
public abstract class DbxEntry extends Dumpable implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: FM */
    private static final FieldMapping f69FM;
    private static final int FM_bytes = 1;
    private static final int FM_client_mtime = 9;
    private static final int FM_contents = 11;
    private static final int FM_hash = 10;
    private static final int FM_icon = 7;
    private static final int FM_is_deleted = 4;
    private static final int FM_is_dir = 3;
    private static final int FM_modified = 8;
    private static final int FM_path = 2;
    private static final int FM_photo_info = 12;
    private static final int FM_rev = 5;
    private static final int FM_size = 0;
    private static final int FM_thumb_exists = 6;
    private static final int FM_video_info = 13;
    public static final JsonReader<DbxEntry> Reader = new JsonReader<DbxEntry>() {
        public final DbxEntry read(JsonParser jsonParser) throws IOException, JsonReadException {
            return DbxEntry.read(jsonParser, null).entry;
        }
    };
    public static final JsonReader<DbxEntry> ReaderMaybeDeleted = new JsonReader<DbxEntry>() {
        public final DbxEntry read(JsonParser jsonParser) throws IOException, JsonReadException {
            WithChildrenC readMaybeDeleted = DbxEntry.readMaybeDeleted(jsonParser, null);
            if (readMaybeDeleted == null) {
                return null;
            }
            return readMaybeDeleted.entry;
        }
    };
    public static final long serialVersionUID = 0;
    public final String iconName;
    public final boolean mightHaveThumbnail;
    public final String name;
    public final String path;

    /* renamed from: com.dropbox.core.v1.DbxEntry$File */
    public static final class File extends DbxEntry {
        public static final JsonReader<File> Reader = new JsonReader<File>() {
            public final File read(JsonParser jsonParser) throws IOException, JsonReadException {
                JsonLocation currentLocation = jsonParser.getCurrentLocation();
                DbxEntry dbxEntry = DbxEntry.read(jsonParser, null).entry;
                if (dbxEntry instanceof File) {
                    return (File) dbxEntry;
                }
                throw new JsonReadException("Expecting a file entry, got a folder entry", currentLocation);
            }
        };
        public static final JsonReader<File> ReaderMaybeDeleted = new JsonReader<File>() {
            public final File read(JsonParser jsonParser) throws IOException, JsonReadException {
                JsonLocation currentLocation = jsonParser.getCurrentLocation();
                WithChildrenC access$100 = DbxEntry._read(jsonParser, null, true);
                if (access$100 == null) {
                    return null;
                }
                DbxEntry dbxEntry = access$100.entry;
                if (dbxEntry instanceof File) {
                    return (File) dbxEntry;
                }
                throw new JsonReadException("Expecting a file entry, got a folder entry", currentLocation);
            }
        };
        public static final long serialVersionUID = 0;
        public final Date clientMtime;
        public final String humanSize;
        public final Date lastModified;
        public final long numBytes;
        public final PhotoInfo photoInfo;
        public final String rev;
        public final VideoInfo videoInfo;

        /* renamed from: com.dropbox.core.v1.DbxEntry$File$Location */
        public static class Location extends Dumpable {
            public static JsonReader<Location> Reader = new JsonReader<Location>() {
                public Location read(JsonParser jsonParser) throws IOException, JsonReadException {
                    if (JsonArrayReader.isArrayStart(jsonParser)) {
                        JsonReader.expectArrayStart(jsonParser);
                        Location location = new Location(JsonReader.readDouble(jsonParser), JsonReader.readDouble(jsonParser));
                        JsonReader.expectArrayEnd(jsonParser);
                        return location;
                    }
                    JsonReader.skipValue(jsonParser);
                    return null;
                }
            };
            public final double latitude;
            public final double longitude;

            public Location(double d, double d2) {
                this.latitude = d;
                this.longitude = d2;
            }

            /* access modifiers changed from: protected */
            public void dumpFields(DumpWriter dumpWriter) {
                dumpWriter.mo10658f("latitude").mo10664v(this.latitude);
                dumpWriter.mo10658f("longitude").mo10664v(this.longitude);
            }

            public int hashCode() {
                long doubleToLongBits = Double.doubleToLongBits(this.latitude);
                long doubleToLongBits2 = Double.doubleToLongBits(this.longitude);
                return ((527 + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)))) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
            }

            public boolean equals(Object obj) {
                return obj != null && getClass().equals(obj.getClass()) && equals((Location) obj);
            }

            public boolean equals(Location location) {
                if (this.latitude == location.latitude && this.longitude == location.longitude) {
                    return true;
                }
                return false;
            }
        }

        /* renamed from: com.dropbox.core.v1.DbxEntry$File$PhotoInfo */
        public static final class PhotoInfo extends Dumpable {
            public static final PhotoInfo PENDING = new PhotoInfo(null, null);
            public static JsonReader<PhotoInfo> Reader = new JsonReader<PhotoInfo>() {
                public PhotoInfo read(JsonParser jsonParser) throws IOException, JsonReadException {
                    JsonReader.expectObjectStart(jsonParser);
                    Date date = null;
                    Location location = null;
                    while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                        String currentName = jsonParser.getCurrentName();
                        JsonReader.nextToken(jsonParser);
                        if (currentName.equals("lat_long")) {
                            location = (Location) Location.Reader.read(jsonParser);
                        } else if (currentName.equals("time_taken")) {
                            date = (Date) JsonDateReader.Dropbox.readOptional(jsonParser);
                        } else {
                            JsonReader.skipValue(jsonParser);
                        }
                    }
                    JsonReader.expectObjectEnd(jsonParser);
                    return new PhotoInfo(date, location);
                }
            };
            public final Location location;
            public final Date timeTaken;

            public PhotoInfo(Date date, Location location2) {
                this.timeTaken = date;
                this.location = location2;
            }

            /* access modifiers changed from: protected */
            public void dumpFields(DumpWriter dumpWriter) {
                dumpWriter.mo10658f("timeTaken").mo10672v(this.timeTaken);
                dumpWriter.mo10658f(Param.LOCATION).mo10668v((Dumpable) this.location);
            }

            public boolean equals(Object obj) {
                return obj != null && getClass().equals(obj.getClass()) && equals((PhotoInfo) obj);
            }

            public boolean equals(PhotoInfo photoInfo) {
                PhotoInfo photoInfo2 = PENDING;
                boolean z = true;
                if (photoInfo != photoInfo2 && this != photoInfo2) {
                    return LangUtil.nullableEquals(this.timeTaken, photoInfo.timeTaken) && LangUtil.nullableEquals(this.location, photoInfo.location);
                }
                if (photoInfo != this) {
                    z = false;
                }
                return z;
            }

            public int hashCode() {
                return ((0 + LangUtil.nullableHashCode(this.timeTaken)) * 31) + LangUtil.nullableHashCode(this.location);
            }
        }

        /* renamed from: com.dropbox.core.v1.DbxEntry$File$VideoInfo */
        public static final class VideoInfo extends Dumpable {
            public static final VideoInfo PENDING = new VideoInfo(null, null, null);
            public static JsonReader<VideoInfo> Reader = new JsonReader<VideoInfo>() {
                public VideoInfo read(JsonParser jsonParser) throws IOException, JsonReadException {
                    JsonReader.expectObjectStart(jsonParser);
                    Date date = null;
                    Location location = null;
                    Long l = null;
                    while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                        String currentName = jsonParser.getCurrentName();
                        JsonReader.nextToken(jsonParser);
                        if (currentName.equals("lat_long")) {
                            location = (Location) Location.Reader.read(jsonParser);
                        } else if (currentName.equals("time_taken")) {
                            date = (Date) JsonDateReader.Dropbox.readOptional(jsonParser);
                        } else if (currentName.equals("duration")) {
                            l = (Long) JsonReader.UnsignedLongReader.readOptional(jsonParser);
                        } else {
                            JsonReader.skipValue(jsonParser);
                        }
                    }
                    JsonReader.expectObjectEnd(jsonParser);
                    return new VideoInfo(date, location, l);
                }
            };
            public final Long duration;
            public final Location location;
            public final Date timeTaken;

            public VideoInfo(Date date, Location location2, Long l) {
                this.timeTaken = date;
                this.location = location2;
                this.duration = l;
            }

            /* access modifiers changed from: protected */
            public void dumpFields(DumpWriter dumpWriter) {
                dumpWriter.mo10658f("timeTaken").mo10672v(this.timeTaken);
                dumpWriter.mo10658f(Param.LOCATION).mo10668v((Dumpable) this.location);
                dumpWriter.mo10658f("duration").mo10670v(this.duration);
            }

            public boolean equals(Object obj) {
                return obj != null && getClass().equals(obj.getClass()) && equals((VideoInfo) obj);
            }

            public boolean equals(VideoInfo videoInfo) {
                VideoInfo videoInfo2 = PENDING;
                boolean z = true;
                if (videoInfo != videoInfo2 && this != videoInfo2) {
                    return LangUtil.nullableEquals(this.timeTaken, videoInfo.timeTaken) && LangUtil.nullableEquals(this.location, videoInfo.location) && LangUtil.nullableEquals(this.duration, videoInfo.duration);
                }
                if (videoInfo != this) {
                    z = false;
                }
                return z;
            }

            public int hashCode() {
                return ((((0 + LangUtil.nullableHashCode(this.timeTaken)) * 31) + LangUtil.nullableHashCode(this.location)) * 31) + LangUtil.nullableHashCode(this.duration);
            }
        }

        public File asFile() {
            return this;
        }

        /* access modifiers changed from: protected */
        public String getTypeName() {
            return "File";
        }

        public boolean isFile() {
            return true;
        }

        public boolean isFolder() {
            return false;
        }

        public File(String str, String str2, boolean z, long j, String str3, Date date, Date date2, String str4, PhotoInfo photoInfo2, VideoInfo videoInfo2) {
            super(str, str2, z);
            this.numBytes = j;
            this.humanSize = str3;
            this.lastModified = date;
            this.clientMtime = date2;
            this.rev = str4;
            this.photoInfo = photoInfo2;
            this.videoInfo = videoInfo2;
        }

        public File(String str, String str2, boolean z, long j, String str3, Date date, Date date2, String str4) {
            this(str, str2, z, j, str3, date, date2, str4, null, null);
        }

        /* access modifiers changed from: protected */
        public void dumpFields(DumpWriter dumpWriter) {
            DbxEntry.super.dumpFields(dumpWriter);
            dumpWriter.mo10658f("numBytes").mo10667v(this.numBytes);
            dumpWriter.mo10658f("humanSize").mo10671v(this.humanSize);
            dumpWriter.mo10658f("lastModified").mo10672v(this.lastModified);
            dumpWriter.mo10658f("clientMtime").mo10672v(this.clientMtime);
            dumpWriter.mo10658f("rev").mo10671v(this.rev);
            nullablePendingField(dumpWriter, "photoInfo", this.photoInfo, PhotoInfo.PENDING);
            nullablePendingField(dumpWriter, "videoInfo", this.videoInfo, VideoInfo.PENDING);
        }

        private static <T extends Dumpable> void nullablePendingField(DumpWriter dumpWriter, String str, T t, T t2) {
            if (t != null) {
                dumpWriter.mo10658f(str);
                if (t == t2) {
                    dumpWriter.verbatim("pending");
                } else {
                    dumpWriter.mo10668v((Dumpable) t);
                }
            }
        }

        public Folder asFolder() {
            throw new RuntimeException("not a folder");
        }

        public boolean equals(Object obj) {
            return obj != null && getClass().equals(obj.getClass()) && equals((File) obj);
        }

        public boolean equals(File file) {
            if (partialEquals(file) && this.numBytes == file.numBytes && this.humanSize.equals(file.humanSize) && this.lastModified.equals(file.lastModified) && this.clientMtime.equals(file.clientMtime) && this.rev.equals(file.rev) && LangUtil.nullableEquals(this.photoInfo, file.photoInfo) && LangUtil.nullableEquals(this.videoInfo, file.videoInfo)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((((((((((partialHashCode() * 31) + ((int) this.numBytes)) * 31) + this.lastModified.hashCode()) * 31) + this.clientMtime.hashCode()) * 31) + this.rev.hashCode()) * 31) + LangUtil.nullableHashCode(this.photoInfo)) * 31) + LangUtil.nullableHashCode(this.videoInfo);
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxEntry$Folder */
    public static final class Folder extends DbxEntry {
        public static final JsonReader<Folder> Reader = new JsonReader<Folder>() {
            public final Folder read(JsonParser jsonParser) throws IOException, JsonReadException {
                JsonLocation currentLocation = jsonParser.getCurrentLocation();
                DbxEntry dbxEntry = DbxEntry.read(jsonParser, null).entry;
                if (dbxEntry instanceof Folder) {
                    return (Folder) dbxEntry;
                }
                throw new JsonReadException("Expecting a file entry, got a folder entry", currentLocation);
            }
        };
        public static final long serialVersionUID = 0;

        public Folder asFolder() {
            return this;
        }

        /* access modifiers changed from: protected */
        public String getTypeName() {
            return "Folder";
        }

        public boolean isFile() {
            return false;
        }

        public boolean isFolder() {
            return true;
        }

        public Folder(String str, String str2, boolean z) {
            super(str, str2, z);
        }

        public File asFile() {
            throw new RuntimeException("not a file");
        }

        public boolean equals(Object obj) {
            return obj != null && getClass().equals(obj.getClass()) && equals((Folder) obj);
        }

        public boolean equals(Folder folder) {
            return partialEquals(folder);
        }

        public int hashCode() {
            return partialHashCode();
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxEntry$PendingReader */
    private static final class PendingReader<T> extends JsonReader<T> {
        private final T pendingValue;
        private final JsonReader<T> reader;

        public PendingReader(JsonReader<T> jsonReader, T t) {
            this.reader = jsonReader;
            this.pendingValue = t;
        }

        /* renamed from: mk */
        public static <T> PendingReader<T> m34mk(JsonReader<T> jsonReader, T t) {
            return new PendingReader<>(jsonReader, t);
        }

        public T read(JsonParser jsonParser) throws IOException, JsonReadException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_STRING) {
                return this.reader.read(jsonParser);
            }
            if (jsonParser.getText().equals("pending")) {
                jsonParser.nextToken();
                return this.pendingValue;
            }
            throw new JsonReadException("got a string, but the value wasn't \"pending\"", jsonParser.getTokenLocation());
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxEntry$WithChildren */
    public static final class WithChildren extends Dumpable implements Serializable {
        public static final JsonReader<WithChildren> Reader = new JsonReader<WithChildren>() {
            public final WithChildren read(JsonParser jsonParser) throws IOException, JsonReadException {
                WithChildrenC read = DbxEntry.read(jsonParser, new ArrayListCollector());
                return new WithChildren(read.entry, read.hash, (List) read.children);
            }
        };
        public static final JsonReader<WithChildren> ReaderMaybeDeleted = new JsonReader<WithChildren>() {
            public final WithChildren read(JsonParser jsonParser) throws IOException, JsonReadException {
                WithChildrenC readMaybeDeleted = DbxEntry.readMaybeDeleted(jsonParser, new ArrayListCollector());
                if (readMaybeDeleted == null) {
                    return null;
                }
                return new WithChildren(readMaybeDeleted.entry, readMaybeDeleted.hash, (List) readMaybeDeleted.children);
            }
        };
        public static final long serialVersionUID = 0;
        public final List<DbxEntry> children;
        public final DbxEntry entry;
        public final String hash;

        public WithChildren(DbxEntry dbxEntry, String str, List<DbxEntry> list) {
            this.entry = dbxEntry;
            this.hash = str;
            this.children = list;
        }

        public boolean equals(Object obj) {
            return obj != null && getClass().equals(obj.getClass()) && equals((WithChildren) obj);
        }

        public boolean equals(WithChildren withChildren) {
            List<DbxEntry> list = this.children;
            if (list == null ? withChildren.children != null : !list.equals(withChildren.children)) {
                return false;
            }
            if (!this.entry.equals(withChildren.entry)) {
                return false;
            }
            String str = this.hash;
            if (str == null ? withChildren.hash == null : str.equals(withChildren.hash)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.entry.hashCode() * 31;
            String str = this.hash;
            int i = 0;
            int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
            List<DbxEntry> list = this.children;
            if (list != null) {
                i = list.hashCode();
            }
            return hashCode2 + i;
        }

        /* access modifiers changed from: protected */
        public void dumpFields(DumpWriter dumpWriter) {
            dumpWriter.mo10668v((Dumpable) this.entry);
            dumpWriter.mo10658f("hash").mo10671v(this.hash);
            dumpWriter.mo10658f("children").mo10669v((Iterable<? extends Dumpable>) this.children);
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxEntry$WithChildrenC */
    public static final class WithChildrenC<C> extends Dumpable implements Serializable {
        public static final long serialVersionUID = 0;
        public final C children;
        public final DbxEntry entry;
        public final String hash;

        /* renamed from: com.dropbox.core.v1.DbxEntry$WithChildrenC$Reader */
        public static class Reader<C> extends JsonReader<WithChildrenC<C>> {
            private final Collector<DbxEntry, ? extends C> collector;

            public Reader(Collector<DbxEntry, ? extends C> collector2) {
                this.collector = collector2;
            }

            public final WithChildrenC<C> read(JsonParser jsonParser) throws IOException, JsonReadException {
                return DbxEntry.read(jsonParser, this.collector);
            }
        }

        /* renamed from: com.dropbox.core.v1.DbxEntry$WithChildrenC$ReaderMaybeDeleted */
        public static class ReaderMaybeDeleted<C> extends JsonReader<WithChildrenC<C>> {
            private final Collector<DbxEntry, ? extends C> collector;

            public ReaderMaybeDeleted(Collector<DbxEntry, ? extends C> collector2) {
                this.collector = collector2;
            }

            public final WithChildrenC<C> read(JsonParser jsonParser) throws IOException, JsonReadException {
                return DbxEntry.readMaybeDeleted(jsonParser, this.collector);
            }
        }

        public WithChildrenC(DbxEntry dbxEntry, String str, C c) {
            this.entry = dbxEntry;
            this.hash = str;
            this.children = c;
        }

        public boolean equals(Object obj) {
            return obj != null && getClass().equals(obj.getClass()) && equals((WithChildrenC) obj);
        }

        public boolean equals(WithChildrenC<?> withChildrenC) {
            C c = this.children;
            if (c == null ? withChildrenC.children != null : !c.equals(withChildrenC.children)) {
                return false;
            }
            if (!this.entry.equals(withChildrenC.entry)) {
                return false;
            }
            String str = this.hash;
            if (str == null ? withChildrenC.hash == null : str.equals(withChildrenC.hash)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.entry.hashCode() * 31;
            String str = this.hash;
            int i = 0;
            int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
            C c = this.children;
            if (c != null) {
                i = c.hashCode();
            }
            return hashCode2 + i;
        }

        /* access modifiers changed from: protected */
        public void dumpFields(DumpWriter dumpWriter) {
            dumpWriter.mo10668v((Dumpable) this.entry);
            dumpWriter.mo10658f("hash").mo10671v(this.hash);
            if (this.children != null) {
                dumpWriter.mo10658f("children").verbatim(this.children.toString());
            }
        }
    }

    public abstract File asFile();

    public abstract Folder asFolder();

    public abstract boolean isFile();

    public abstract boolean isFolder();

    private DbxEntry(String str, String str2, boolean z) {
        this.name = DbxPathV1.getName(str);
        this.path = str;
        this.iconName = str2;
        this.mightHaveThumbnail = z;
    }

    /* access modifiers changed from: protected */
    public void dumpFields(DumpWriter dumpWriter) {
        dumpWriter.mo10671v(this.path);
        dumpWriter.mo10658f("iconName").mo10671v(this.iconName);
        dumpWriter.mo10658f("mightHaveThumbnail").mo10673v(this.mightHaveThumbnail);
    }

    /* access modifiers changed from: protected */
    public boolean partialEquals(DbxEntry dbxEntry) {
        if (this.name.equals(dbxEntry.name) && this.path.equals(dbxEntry.path) && this.iconName.equals(dbxEntry.iconName) && this.mightHaveThumbnail == dbxEntry.mightHaveThumbnail) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public int partialHashCode() {
        return (((((((this.name.hashCode() * 31) + this.path.hashCode()) * 31) + this.iconName.hashCode()) * 31) + this.path.hashCode()) * 31) + (this.mightHaveThumbnail ? 1 : 0);
    }

    static {
        Builder builder = new Builder();
        builder.add("size", 0);
        builder.add("bytes", 1);
        builder.add("path", 2);
        builder.add("is_dir", 3);
        builder.add("is_deleted", 4);
        builder.add("rev", 5);
        builder.add("thumb_exists", 6);
        builder.add("icon", 7);
        builder.add("modified", 8);
        builder.add("client_mtime", 9);
        builder.add("hash", 10);
        builder.add("contents", 11);
        builder.add("photo_info", 12);
        builder.add("video_info", 13);
        f69FM = builder.build();
    }

    public static <C> WithChildrenC<C> readMaybeDeleted(JsonParser jsonParser, Collector<DbxEntry, ? extends C> collector) throws IOException, JsonReadException {
        return _read(jsonParser, collector, true);
    }

    public static <C> WithChildrenC<C> read(JsonParser jsonParser, Collector<DbxEntry, ? extends C> collector) throws IOException, JsonReadException {
        return _read(jsonParser, collector, false);
    }

    /* access modifiers changed from: private */
    public static <C> WithChildrenC<C> _read(JsonParser jsonParser, Collector<DbxEntry, ? extends C> collector, boolean z) throws IOException, JsonReadException {
        JsonLocation jsonLocation;
        JsonLocation jsonLocation2;
        DbxEntry dbxEntry;
        JsonParser jsonParser2 = jsonParser;
        Collector<DbxEntry, ? extends C> collector2 = collector;
        JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
        PhotoInfo photoInfo = null;
        String str = null;
        Object obj = null;
        Date date = null;
        Boolean bool = null;
        String str2 = null;
        String str3 = null;
        Boolean bool2 = null;
        VideoInfo videoInfo = null;
        Boolean bool3 = null;
        Date date2 = null;
        String str4 = null;
        String str5 = null;
        long j = -1;
        while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
            String currentName = jsonParser.getCurrentName();
            JsonReader.nextToken(jsonParser);
            int i = f69FM.get(currentName);
            switch (i) {
                case -1:
                    String str6 = str2;
                    long j2 = j;
                    PhotoInfo photoInfo2 = photoInfo;
                    VideoInfo videoInfo2 = videoInfo;
                    String str7 = str5;
                    long j3 = j2;
                    JsonReader.skipValue(jsonParser);
                    str2 = str6;
                    str5 = str7;
                    videoInfo = videoInfo2;
                    photoInfo = photoInfo2;
                    j = j3;
                    break;
                case 0:
                    long j4 = j;
                    str5 = (String) JsonReader.StringReader.readField(jsonParser2, currentName, str5);
                    str2 = str2;
                    videoInfo = videoInfo;
                    photoInfo = photoInfo;
                    j = j4;
                    break;
                case 1:
                    long j5 = j;
                    str2 = str2;
                    photoInfo = photoInfo;
                    j = JsonReader.readUnsignedLongField(jsonParser2, currentName, j5);
                    break;
                case 2:
                    str2 = (String) JsonReader.StringReader.readField(jsonParser2, currentName, str2);
                    break;
                case 3:
                    bool2 = (Boolean) JsonReader.BooleanReader.readField(jsonParser2, currentName, bool2);
                    str2 = str2;
                    break;
                case 4:
                    bool = (Boolean) JsonReader.BooleanReader.readField(jsonParser2, currentName, bool);
                    str2 = str2;
                    break;
                case 5:
                    str4 = (String) JsonReader.StringReader.readField(jsonParser2, currentName, str4);
                    str2 = str2;
                    break;
                case 6:
                    bool3 = (Boolean) JsonReader.BooleanReader.readField(jsonParser2, currentName, bool3);
                    str2 = str2;
                    break;
                case 7:
                    str3 = (String) JsonReader.StringReader.readField(jsonParser2, currentName, str3);
                    str2 = str2;
                    break;
                case 8:
                    date2 = (Date) JsonDateReader.Dropbox.readField(jsonParser2, currentName, date2);
                    str2 = str2;
                    break;
                case 9:
                    date = (Date) JsonDateReader.Dropbox.readField(jsonParser2, currentName, date);
                    str2 = str2;
                    break;
                case 10:
                    String str8 = str2;
                    if (collector2 != null) {
                        str = (String) JsonReader.StringReader.readField(jsonParser2, currentName, str);
                        str2 = str8;
                        break;
                    } else {
                        throw new JsonReadException("not expecting \"hash\" field, since we didn't ask for children", jsonParser.getCurrentLocation());
                    }
                case 11:
                    String str9 = str2;
                    if (collector2 != null) {
                        obj = JsonArrayReader.m18mk(Reader, collector2).readField(jsonParser2, currentName, obj);
                        str2 = str9;
                        break;
                    } else {
                        throw new JsonReadException("not expecting \"contents\" field, since we didn't ask for children", jsonParser.getCurrentLocation());
                    }
                case 12:
                    photoInfo = (PhotoInfo) PendingReader.m34mk(PhotoInfo.Reader, PhotoInfo.PENDING).readField(jsonParser2, currentName, photoInfo);
                    str2 = str2;
                    break;
                case 13:
                    videoInfo = (VideoInfo) PendingReader.m34mk(VideoInfo.Reader, VideoInfo.PENDING).readField(jsonParser2, currentName, videoInfo);
                    str2 = str2;
                    break;
                default:
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("bad index: ");
                        sb.append(i);
                        sb.append(", field = \"");
                        sb.append(currentName);
                        sb.append("\"");
                        throw new AssertionError(sb.toString());
                    } catch (JsonReadException e) {
                        throw e.addFieldContext(currentName);
                    }
            }
            collector2 = collector;
        }
        String str10 = str2;
        long j6 = j;
        PhotoInfo photoInfo3 = photoInfo;
        VideoInfo videoInfo3 = videoInfo;
        String str11 = str5;
        long j7 = j6;
        JsonReader.expectObjectEnd(jsonParser);
        if (str10 == null) {
            throw new JsonReadException("missing field \"path\"", expectObjectStart);
        } else if (str3 != null) {
            if (bool == null) {
                bool = Boolean.FALSE;
            }
            Boolean bool4 = bool;
            if (bool2 == null) {
                bool2 = Boolean.FALSE;
            }
            if (bool3 == null) {
                bool3 = Boolean.FALSE;
            }
            if (!bool2.booleanValue() || (obj == null && str == null)) {
                jsonLocation = expectObjectStart;
            } else if (str == null) {
                throw new JsonReadException("missing \"hash\", when we asked for children", expectObjectStart);
            } else if (obj != null) {
                jsonLocation = expectObjectStart;
            } else {
                throw new JsonReadException("missing \"contents\", when we asked for children", expectObjectStart);
            }
            if (bool2.booleanValue()) {
                jsonLocation2 = jsonLocation;
                dbxEntry = new Folder(str10, str3, bool3.booleanValue());
            } else if (str11 == null) {
                throw new JsonReadException("missing \"size\" for a file entry", jsonLocation);
            } else if (j7 == -1) {
                throw new JsonReadException("missing \"bytes\" for a file entry", jsonLocation);
            } else if (date2 == null) {
                throw new JsonReadException("missing \"modified\" for a file entry", jsonLocation);
            } else if (date == null) {
                throw new JsonReadException("missing \"client_mtime\" for a file entry", jsonLocation);
            } else if (str4 != null) {
                boolean booleanValue = bool3.booleanValue();
                jsonLocation2 = jsonLocation;
                File file = new File(str10, str3, booleanValue, j7, str11, date2, date, str4, photoInfo3, videoInfo3);
                dbxEntry = file;
            } else {
                throw new JsonReadException("missing \"rev\" for a file entry", jsonLocation);
            }
            if (!bool4.booleanValue()) {
                return new WithChildrenC<>(dbxEntry, str, obj);
            }
            if (z) {
                return null;
            }
            throw new JsonReadException("not expecting \"is_deleted\" entry here", jsonLocation2);
        } else {
            throw new JsonReadException("missing field \"icon\"", expectObjectStart);
        }
    }
}
