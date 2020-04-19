package com.dropbox.core.p004v1;

import com.dropbox.core.BadRequestException;
import com.dropbox.core.BadResponseException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxRequestUtil;
import com.dropbox.core.DbxRequestUtil.RequestMaker;
import com.dropbox.core.DbxRequestUtil.ResponseHandler;
import com.dropbox.core.DbxStreamWriter;
import com.dropbox.core.DbxStreamWriter.ByteArrayCopier;
import com.dropbox.core.DbxStreamWriter.InputStreamCopier;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.NoThrowOutputStream;
import com.dropbox.core.NoThrowOutputStream.HiddenException;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.json.JsonArrayReader;
import com.dropbox.core.json.JsonDateReader;
import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.p004v1.DbxDelta.Reader;
import com.dropbox.core.p004v1.DbxDeltaC.Entry;
import com.dropbox.core.p004v1.DbxEntry.File;
import com.dropbox.core.p004v1.DbxEntry.Folder;
import com.dropbox.core.p004v1.DbxEntry.WithChildren;
import com.dropbox.core.p004v1.DbxEntry.WithChildrenC;
import com.dropbox.core.p004v1.DbxEntry.WithChildrenC.ReaderMaybeDeleted;
import com.dropbox.core.util.Collector;
import com.dropbox.core.util.Collector.ArrayListCollector;
import com.dropbox.core.util.Collector.NullSkipper;
import com.dropbox.core.util.CountingOutputStream;
import com.dropbox.core.util.DumpWriter;
import com.dropbox.core.util.Dumpable;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.IOUtil.ReadException;
import com.dropbox.core.util.IOUtil.WriteException;
import com.dropbox.core.util.LangUtil;
import com.dropbox.core.util.Maybe;
import com.dropbox.core.util.StringUtil;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.actions.SearchIntents;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v1.DbxClientV1 */
public final class DbxClientV1 {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ChunkedUploadChunkSize = 4194304;
    private static final long ChunkedUploadThreshold = 8388608;
    /* access modifiers changed from: private */
    public static JsonReader<String> LatestCursorReader = new JsonReader<String>() {
        public String read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            String str = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                try {
                    if (currentName.equals("cursor")) {
                        str = (String) JsonReader.StringReader.readField(jsonParser, currentName, str);
                    } else {
                        JsonReader.skipValue(jsonParser);
                    }
                } catch (JsonReadException e) {
                    throw e.addFieldContext(currentName);
                }
            }
            JsonReader.expectObjectEnd(jsonParser);
            if (str != null) {
                return str;
            }
            throw new JsonReadException("missing field \"cursor\"", expectObjectStart);
        }
    };
    public static final String USER_AGENT_ID = "Dropbox-Java-SDK";
    /* access modifiers changed from: private */
    public final String accessToken;
    private final DbxHost host;
    /* access modifiers changed from: private */
    public final DbxRequestConfig requestConfig;

    /* renamed from: com.dropbox.core.v1.DbxClientV1$ChunkedUploadOutputStream */
    private final class ChunkedUploadOutputStream extends OutputStream {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        /* access modifiers changed from: private */
        public final byte[] chunk;
        /* access modifiers changed from: private */
        public int chunkPos;
        /* access modifiers changed from: private */
        public String uploadId;
        /* access modifiers changed from: private */
        public long uploadOffset;

        public void close() throws IOException {
        }

        static {
            Class<DbxClientV1> cls = DbxClientV1.class;
        }

        private ChunkedUploadOutputStream(int i) {
            this.chunkPos = 0;
            this.chunk = new byte[i];
            this.chunkPos = 0;
        }

        public void write(int i) throws IOException {
            byte[] bArr = this.chunk;
            int i2 = this.chunkPos;
            this.chunkPos = i2 + 1;
            bArr[i2] = (byte) i;
            try {
                finishChunkIfNecessary();
            } catch (DbxException e) {
                throw new IODbxException(e);
            }
        }

        private void finishChunkIfNecessary() throws DbxException {
            if (this.chunkPos == this.chunk.length) {
                finishChunk();
            }
        }

        /* access modifiers changed from: private */
        public void finishChunk() throws DbxException {
            long j;
            if (this.chunkPos != 0) {
                final String str = this.uploadId;
                if (str == null) {
                    this.uploadId = (String) DbxRequestUtil.runAndRetry(3, new RequestMaker<String, RuntimeException>() {
                        public String run() throws DbxException {
                            return DbxClientV1.this.chunkedUploadFirst(ChunkedUploadOutputStream.this.chunk, 0, ChunkedUploadOutputStream.this.chunkPos);
                        }
                    });
                    this.uploadOffset = (long) this.chunkPos;
                } else {
                    final int i = 0;
                    while (true) {
                        long longValue = ((Long) DbxRequestUtil.runAndRetry(3, new RequestMaker<Long, RuntimeException>() {
                            public Long run() throws DbxException {
                                return Long.valueOf(DbxClientV1.this.chunkedUploadAppend(str, ChunkedUploadOutputStream.this.uploadOffset, ChunkedUploadOutputStream.this.chunk, i, ChunkedUploadOutputStream.this.chunkPos - i));
                            }
                        })).longValue();
                        long j2 = this.uploadOffset;
                        j = ((long) this.chunkPos) + j2;
                        if (longValue == -1) {
                            break;
                        }
                        i += (int) (longValue - j2);
                    }
                    this.uploadOffset = j;
                }
                this.chunkPos = 0;
            }
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            int i3 = i2 + i;
            while (i < i3) {
                int min = Math.min(i3 - i, this.chunk.length - this.chunkPos);
                System.arraycopy(bArr, i, this.chunk, this.chunkPos, min);
                this.chunkPos += min;
                i += min;
                try {
                    finishChunkIfNecessary();
                } catch (DbxException e) {
                    throw new IODbxException(e);
                }
            }
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxClientV1$ChunkedUploadState */
    private static final class ChunkedUploadState extends Dumpable {
        public static final JsonReader<ChunkedUploadState> Reader = new JsonReader<ChunkedUploadState>() {
            public ChunkedUploadState read(JsonParser jsonParser) throws IOException, JsonReadException {
                JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
                String str = null;
                long j = -1;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    try {
                        if (currentName.equals("upload_id")) {
                            str = (String) JsonReader.StringReader.readField(jsonParser, currentName, str);
                        } else if (currentName.equals("offset")) {
                            j = JsonReader.readUnsignedLongField(jsonParser, currentName, j);
                        } else {
                            JsonReader.skipValue(jsonParser);
                        }
                    } catch (JsonReadException e) {
                        throw e.addFieldContext(currentName);
                    }
                }
                JsonReader.expectObjectEnd(jsonParser);
                if (str == null) {
                    throw new JsonReadException("missing field \"upload_id\"", expectObjectStart);
                } else if (j != -1) {
                    return new ChunkedUploadState(str, j);
                } else {
                    throw new JsonReadException("missing field \"offset\"", expectObjectStart);
                }
            }
        };
        public final long offset;
        public final String uploadId;

        public ChunkedUploadState(String str, long j) {
            if (str == null) {
                throw new IllegalArgumentException("'uploadId' can't be null");
            } else if (str.length() == 0) {
                throw new IllegalArgumentException("'uploadId' can't be empty");
            } else if (j >= 0) {
                this.uploadId = str;
                this.offset = j;
            } else {
                throw new IllegalArgumentException("'offset' can't be negative");
            }
        }

        /* access modifiers changed from: protected */
        public void dumpFields(DumpWriter dumpWriter) {
            dumpWriter.mo10658f("uploadId").mo10671v(this.uploadId);
            dumpWriter.mo10658f("offset").mo10667v(this.offset);
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxClientV1$ChunkedUploader */
    private final class ChunkedUploader extends Uploader {
        private final ChunkedUploadOutputStream body;
        private final long numBytes;
        /* access modifiers changed from: private */
        public final String targetPath;
        /* access modifiers changed from: private */
        public final DbxWriteMode writeMode;

        public void abort() {
        }

        public void close() {
        }

        private ChunkedUploader(String str, DbxWriteMode dbxWriteMode, long j, ChunkedUploadOutputStream chunkedUploadOutputStream) {
            this.targetPath = str;
            this.writeMode = dbxWriteMode;
            this.numBytes = j;
            this.body = chunkedUploadOutputStream;
        }

        public OutputStream getBody() {
            return this.body;
        }

        public File finish() throws DbxException {
            if (this.body.uploadId == null) {
                return DbxClientV1.this.uploadFileSingle(this.targetPath, this.writeMode, (long) this.body.chunkPos, new ByteArrayCopier(this.body.chunk, 0, this.body.chunkPos));
            }
            final String access$400 = this.body.uploadId;
            this.body.finishChunk();
            long j = this.numBytes;
            if (j == -1 || j == this.body.uploadOffset) {
                return (File) DbxRequestUtil.runAndRetry(3, new RequestMaker<File, RuntimeException>() {
                    public File run() throws DbxException {
                        return DbxClientV1.this.chunkedUploadFinish(ChunkedUploader.this.targetPath, ChunkedUploader.this.writeMode, access$400);
                    }
                });
            }
            StringBuilder sb = new StringBuilder();
            sb.append("'numBytes' is ");
            sb.append(this.numBytes);
            sb.append(" but you wrote ");
            sb.append(this.body.uploadOffset);
            sb.append(" bytes");
            throw new IllegalStateException(sb.toString());
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxClientV1$CopyRef */
    private static final class CopyRef {
        public static final JsonReader<CopyRef> Reader = new JsonReader<CopyRef>() {
            public CopyRef read(JsonParser jsonParser) throws IOException, JsonReadException {
                JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
                String str = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    try {
                        if (currentName.equals("copy_ref")) {
                            str = (String) JsonReader.StringReader.readField(jsonParser, currentName, str);
                        } else if (currentName.equals(ClientCookie.EXPIRES_ATTR)) {
                            date = (Date) JsonDateReader.Dropbox.readField(jsonParser, currentName, date);
                        } else {
                            JsonReader.skipValue(jsonParser);
                        }
                    } catch (JsonReadException e) {
                        throw e.addFieldContext(currentName);
                    }
                }
                JsonReader.expectObjectEnd(jsonParser);
                if (str == null) {
                    throw new JsonReadException("missing field \"copy_ref\"", expectObjectStart);
                } else if (date != null) {
                    return new CopyRef(str, date);
                } else {
                    throw new JsonReadException("missing field \"expires\"", expectObjectStart);
                }
            }
        };
        public final Date expires;

        /* renamed from: id */
        public final String f66id;

        private CopyRef(String str, Date date) {
            this.f66id = str;
            this.expires = date;
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxClientV1$Downloader */
    public static final class Downloader {
        public final InputStream body;
        public final File metadata;

        public Downloader(File file, InputStream inputStream) {
            this.metadata = file;
            this.body = inputStream;
        }

        /* access modifiers changed from: 0000 */
        public File copyBodyAndClose(OutputStream outputStream) throws DbxException, IOException {
            try {
                IOUtil.copyStreamToStream(this.body, outputStream);
                close();
                return this.metadata;
            } catch (ReadException e) {
                throw new NetworkIOException(e.getCause());
            } catch (WriteException e2) {
                throw e2.getCause();
            } catch (Throwable th) {
                close();
                throw th;
            }
        }

        public void close() {
            IOUtil.closeInput(this.body);
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxClientV1$IODbxException */
    public static final class IODbxException extends IOException {
        private static final long serialVersionUID = 0;
        public final DbxException underlying;

        public IODbxException(DbxException dbxException) {
            super(dbxException);
            this.underlying = dbxException;
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxClientV1$SingleUploader */
    private static final class SingleUploader extends Uploader {
        private final CountingOutputStream body;
        private final long claimedBytes;
        private com.dropbox.core.http.HttpRequestor.Uploader httpUploader;

        public SingleUploader(com.dropbox.core.http.HttpRequestor.Uploader uploader, long j) {
            if (j >= 0) {
                this.httpUploader = uploader;
                this.claimedBytes = j;
                this.body = new CountingOutputStream(uploader.getBody());
                return;
            }
            throw new IllegalArgumentException("'numBytes' must be greater than or equal to 0");
        }

        public OutputStream getBody() {
            return this.body;
        }

        public void abort() {
            com.dropbox.core.http.HttpRequestor.Uploader uploader = this.httpUploader;
            if (uploader != null) {
                this.httpUploader = null;
                uploader.abort();
                return;
            }
            throw new IllegalStateException("already called 'finish', 'abort', or 'close'");
        }

        public void close() {
            if (this.httpUploader != null) {
                abort();
            }
        }

        public File finish() throws DbxException {
            com.dropbox.core.http.HttpRequestor.Uploader uploader = this.httpUploader;
            if (uploader != null) {
                this.httpUploader = null;
                try {
                    final long bytesWritten = this.body.getBytesWritten();
                    if (this.claimedBytes == bytesWritten) {
                        Response finish = uploader.finish();
                        uploader.close();
                        return (File) DbxRequestUtil.finishResponse(finish, new ResponseHandler<File>() {
                            public File handle(Response response) throws DbxException {
                                if (response.getStatusCode() == 200) {
                                    File file = (File) DbxRequestUtil.readJsonFromResponse(File.Reader, response);
                                    if (file.numBytes == bytesWritten) {
                                        return file;
                                    }
                                    String requestId = DbxRequestUtil.getRequestId(response);
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("we uploaded ");
                                    sb.append(bytesWritten);
                                    sb.append(", but server returned metadata entry with file size ");
                                    sb.append(file.numBytes);
                                    throw new BadResponseException(requestId, sb.toString());
                                }
                                throw DbxRequestUtil.unexpectedStatus(response);
                            }
                        });
                    }
                    uploader.abort();
                    StringBuilder sb = new StringBuilder();
                    sb.append("You said you were going to upload ");
                    sb.append(this.claimedBytes);
                    sb.append(" bytes, but you wrote ");
                    sb.append(bytesWritten);
                    sb.append(" bytes to the Uploader's 'body' stream.");
                    throw new IllegalStateException(sb.toString());
                } catch (IOException e) {
                    throw new NetworkIOException(e);
                } catch (Throwable th) {
                    uploader.close();
                    throw th;
                }
            } else {
                throw new IllegalStateException("already called 'finish', 'abort', or 'close'");
            }
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxClientV1$Uploader */
    public static abstract class Uploader {
        public abstract void abort();

        public abstract void close();

        public abstract File finish() throws DbxException;

        public abstract OutputStream getBody();
    }

    public DbxClientV1(DbxRequestConfig dbxRequestConfig, String str) {
        this(dbxRequestConfig, str, DbxHost.DEFAULT);
    }

    public DbxClientV1(DbxRequestConfig dbxRequestConfig, String str, DbxHost dbxHost) {
        if (dbxRequestConfig == null) {
            throw new IllegalArgumentException("'requestConfig' is null");
        } else if (str == null) {
            throw new IllegalArgumentException("'accessToken' is null");
        } else if (dbxHost != null) {
            this.requestConfig = dbxRequestConfig;
            this.accessToken = str;
            this.host = dbxHost;
        } else {
            throw new IllegalArgumentException("'host' is null");
        }
    }

    public DbxRequestConfig getRequestConfig() {
        return this.requestConfig;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public DbxHost getHost() {
        return this.host;
    }

    public DbxEntry getMetadata(String str, boolean z) throws DbxException {
        DbxPathV1.checkArg("path", str);
        String api = this.host.getApi();
        StringBuilder sb = new StringBuilder();
        sb.append("1/metadata/auto");
        sb.append(str);
        String sb2 = sb.toString();
        String[] strArr = new String[4];
        strArr[0] = "list";
        strArr[1] = "false";
        strArr[2] = "include_media_info";
        strArr[3] = z ? "true" : null;
        return (DbxEntry) doGet(api, sb2, strArr, null, new ResponseHandler<DbxEntry>() {
            public DbxEntry handle(Response response) throws DbxException {
                if (response.getStatusCode() == 404) {
                    return null;
                }
                if (response.getStatusCode() == 200) {
                    return (DbxEntry) DbxRequestUtil.readJsonFromResponse(DbxEntry.ReaderMaybeDeleted, response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public DbxEntry getMetadata(String str) throws DbxException {
        return getMetadata(str, false);
    }

    public WithChildren getMetadataWithChildren(String str, boolean z) throws DbxException {
        return (WithChildren) getMetadataWithChildrenBase(str, z, WithChildren.ReaderMaybeDeleted);
    }

    public WithChildren getMetadataWithChildren(String str) throws DbxException {
        return getMetadataWithChildren(str, false);
    }

    public <C> WithChildrenC<C> getMetadataWithChildrenC(String str, boolean z, Collector<DbxEntry, ? extends C> collector) throws DbxException {
        return (WithChildrenC) getMetadataWithChildrenBase(str, z, new ReaderMaybeDeleted(collector));
    }

    public <C> WithChildrenC<C> getMetadataWithChildrenC(String str, Collector<DbxEntry, ? extends C> collector) throws DbxException {
        return getMetadataWithChildrenC(str, false, collector);
    }

    private <T> T getMetadataWithChildrenBase(String str, boolean z, final JsonReader<? extends T> jsonReader) throws DbxException {
        DbxPathV1.checkArg("path", str);
        String api = this.host.getApi();
        StringBuilder sb = new StringBuilder();
        sb.append("1/metadata/auto");
        sb.append(str);
        String sb2 = sb.toString();
        String[] strArr = new String[6];
        strArr[0] = "list";
        strArr[1] = "true";
        strArr[2] = "file_limit";
        strArr[3] = "25000";
        strArr[4] = "include_media_info";
        strArr[5] = z ? "true" : null;
        return doGet(api, sb2, strArr, null, new ResponseHandler<T>() {
            public T handle(Response response) throws DbxException {
                if (response.getStatusCode() == 404) {
                    return null;
                }
                if (response.getStatusCode() == 200) {
                    return DbxRequestUtil.readJsonFromResponse(jsonReader, response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public Maybe<WithChildren> getMetadataWithChildrenIfChanged(String str, boolean z, String str2) throws DbxException {
        return getMetadataWithChildrenIfChangedBase(str, z, str2, WithChildren.ReaderMaybeDeleted);
    }

    public Maybe<WithChildren> getMetadataWithChildrenIfChanged(String str, String str2) throws DbxException {
        return getMetadataWithChildrenIfChanged(str, false, str2);
    }

    public <C> Maybe<WithChildrenC<C>> getMetadataWithChildrenIfChangedC(String str, boolean z, String str2, Collector<DbxEntry, ? extends C> collector) throws DbxException {
        return getMetadataWithChildrenIfChangedBase(str, z, str2, new ReaderMaybeDeleted(collector));
    }

    public <C> Maybe<WithChildrenC<C>> getMetadataWithChildrenIfChangedC(String str, String str2, Collector<DbxEntry, ? extends C> collector) throws DbxException {
        return getMetadataWithChildrenIfChangedC(str, false, str2, collector);
    }

    private <T> Maybe<T> getMetadataWithChildrenIfChangedBase(String str, boolean z, String str2, final JsonReader<T> jsonReader) throws DbxException {
        if (str2 == null) {
            throw new IllegalArgumentException("'previousFolderHash' must not be null");
        } else if (str2.length() != 0) {
            DbxPathV1.checkArg("path", str);
            String api = this.host.getApi();
            StringBuilder sb = new StringBuilder();
            sb.append("1/metadata/auto");
            sb.append(str);
            String sb2 = sb.toString();
            String[] strArr = new String[8];
            strArr[0] = "list";
            strArr[1] = "true";
            strArr[2] = "file_limit";
            strArr[3] = "25000";
            strArr[4] = "hash";
            strArr[5] = str2;
            strArr[6] = "include_media_info";
            strArr[7] = z ? "true" : null;
            return (Maybe) doGet(api, sb2, strArr, null, new ResponseHandler<Maybe<T>>() {
                public Maybe<T> handle(Response response) throws DbxException {
                    if (response.getStatusCode() == 404) {
                        return Maybe.Just(null);
                    }
                    if (response.getStatusCode() == 304) {
                        return Maybe.Nothing();
                    }
                    if (response.getStatusCode() == 200) {
                        return Maybe.Just(DbxRequestUtil.readJsonFromResponse(jsonReader, response));
                    }
                    throw DbxRequestUtil.unexpectedStatus(response);
                }
            });
        } else {
            throw new IllegalArgumentException("'previousFolderHash' must not be empty");
        }
    }

    public DbxAccountInfo getAccountInfo() throws DbxException {
        return (DbxAccountInfo) doGet(this.host.getApi(), "1/account/info", null, null, new ResponseHandler<DbxAccountInfo>() {
            public DbxAccountInfo handle(Response response) throws DbxException {
                if (response.getStatusCode() == 200) {
                    return (DbxAccountInfo) DbxRequestUtil.readJsonFromResponse(DbxAccountInfo.Reader, response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public void disableAccessToken() throws DbxException {
        doPost(this.host.getApi(), "1/disable_access_token", null, null, new ResponseHandler<Void>() {
            public Void handle(Response response) throws DbxException {
                if (response.getStatusCode() == 200) {
                    return null;
                }
                String requestId = DbxRequestUtil.getRequestId(response);
                StringBuilder sb = new StringBuilder();
                sb.append("unexpected response code: ");
                sb.append(response.getStatusCode());
                throw new BadResponseException(requestId, sb.toString());
            }
        });
    }

    public File getFile(String str, String str2, OutputStream outputStream) throws DbxException, IOException {
        Downloader startGetFile = startGetFile(str, str2);
        if (startGetFile == null) {
            return null;
        }
        return startGetFile.copyBodyAndClose(outputStream);
    }

    public Downloader startGetFile(String str, String str2) throws DbxException {
        DbxPathV1.checkArgNonRoot("path", str);
        StringBuilder sb = new StringBuilder();
        sb.append("1/files/auto");
        sb.append(str);
        return startGetSomething(sb.toString(), new String[]{"rev", str2});
    }

    private Downloader startGetSomething(final String str, final String[] strArr) throws DbxException {
        final String content = this.host.getContent();
        return (Downloader) DbxRequestUtil.runAndRetry(this.requestConfig.getMaxRetries(), new RequestMaker<Downloader, DbxException>() {
            public Downloader run() throws DbxException {
                Response startGet = DbxRequestUtil.startGet(DbxClientV1.this.requestConfig, DbxClientV1.this.accessToken, DbxClientV1.USER_AGENT_ID, content, str, strArr, null);
                try {
                    if (startGet.getStatusCode() == 404) {
                        try {
                            startGet.getBody().close();
                        } catch (IOException unused) {
                        }
                        return null;
                    } else if (startGet.getStatusCode() == 200) {
                        return new Downloader((File) File.Reader.readFully(DbxRequestUtil.getFirstHeader(startGet, "x-dropbox-metadata")), startGet.getBody());
                    } else {
                        throw DbxRequestUtil.unexpectedStatus(startGet);
                    }
                } catch (JsonReadException e) {
                    String requestId = DbxRequestUtil.getRequestId(startGet);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Bad JSON in X-Dropbox-Metadata header: ");
                    sb.append(e.getMessage());
                    throw new BadResponseException(requestId, sb.toString(), e);
                } catch (Throwable th) {
                    try {
                        startGet.getBody().close();
                    } catch (IOException unused2) {
                    }
                    throw th;
                }
            }
        });
    }

    public File uploadFile(String str, DbxWriteMode dbxWriteMode, long j, InputStream inputStream) throws DbxException, IOException {
        return uploadFile(str, dbxWriteMode, j, (DbxStreamWriter<E>) new InputStreamCopier<E>(inputStream));
    }

    public <E extends Throwable> File uploadFile(String str, DbxWriteMode dbxWriteMode, long j, DbxStreamWriter<E> dbxStreamWriter) throws DbxException, Throwable {
        return finishUploadFile(startUploadFile(str, dbxWriteMode, j), dbxStreamWriter);
    }

    public Uploader startUploadFile(String str, DbxWriteMode dbxWriteMode, long j) throws DbxException {
        if (j < 0) {
            if (j == -1) {
                return startUploadFileChunked(str, dbxWriteMode, j);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("numBytes must be -1 or greater; given ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (j > 8388608) {
            return startUploadFileChunked(str, dbxWriteMode, j);
        } else {
            return startUploadFileSingle(str, dbxWriteMode, j);
        }
    }

    public <E extends Throwable> File finishUploadFile(Uploader uploader, DbxStreamWriter<E> dbxStreamWriter) throws DbxException, Throwable {
        NoThrowOutputStream noThrowOutputStream = new NoThrowOutputStream(uploader.getBody());
        try {
            dbxStreamWriter.write(noThrowOutputStream);
            File finish = uploader.finish();
            uploader.close();
            return finish;
        } catch (HiddenException e) {
            if (e.owner == noThrowOutputStream) {
                throw new NetworkIOException(e.getCause());
            }
            throw e;
        } catch (Throwable th) {
            uploader.close();
            throw th;
        }
    }

    public Uploader startUploadFileSingle(String str, DbxWriteMode dbxWriteMode, long j) throws DbxException {
        DbxPathV1.checkArg("targetPath", str);
        if (j >= 0) {
            String content = this.host.getContent();
            StringBuilder sb = new StringBuilder();
            sb.append("1/files_put/auto");
            sb.append(str);
            String sb2 = sb.toString();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new Header("Content-Type", "application/octet-stream"));
            arrayList.add(new Header("Content-Length", Long.toString(j)));
            return new SingleUploader(DbxRequestUtil.startPut(this.requestConfig, this.accessToken, USER_AGENT_ID, content, sb2, dbxWriteMode.params, arrayList), j);
        }
        throw new IllegalArgumentException("numBytes must be zero or greater");
    }

    public <E extends Throwable> File uploadFileSingle(String str, DbxWriteMode dbxWriteMode, long j, DbxStreamWriter<E> dbxStreamWriter) throws DbxException, Throwable {
        return finishUploadFile(startUploadFileSingle(str, dbxWriteMode, j), dbxStreamWriter);
    }

    private <E extends Throwable> Response chunkedUploadCommon(String[] strArr, long j, DbxStreamWriter<E> dbxStreamWriter) throws DbxException, Throwable {
        NoThrowOutputStream noThrowOutputStream;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Header("Content-Type", "application/octet-stream"));
        arrayList.add(new Header("Content-Length", Long.toString(j)));
        com.dropbox.core.http.HttpRequestor.Uploader startPut = DbxRequestUtil.startPut(this.requestConfig, this.accessToken, USER_AGENT_ID, this.host.getContent(), "1/chunked_upload", strArr, arrayList);
        try {
            noThrowOutputStream = new NoThrowOutputStream(startPut.getBody());
            dbxStreamWriter.write(noThrowOutputStream);
            long bytesWritten = noThrowOutputStream.getBytesWritten();
            if (bytesWritten == j) {
                Response finish = startPut.finish();
                startPut.close();
                return finish;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("'chunkSize' is ");
            sb.append(j);
            sb.append(", but 'writer' only wrote ");
            sb.append(bytesWritten);
            sb.append(" bytes");
            throw new IllegalStateException(sb.toString());
        } catch (HiddenException e) {
            if (e.owner == noThrowOutputStream) {
                throw new NetworkIOException(e.getCause());
            }
            throw e;
        } catch (IOException e2) {
            throw new NetworkIOException(e2);
        } catch (Throwable th) {
            startPut.close();
            throw th;
        }
    }

    private ChunkedUploadState chunkedUploadCheckForOffsetCorrection(Response response) throws DbxException {
        if (response.getStatusCode() != 400) {
            return null;
        }
        byte[] loadErrorBody = DbxRequestUtil.loadErrorBody(response);
        try {
            return (ChunkedUploadState) ChunkedUploadState.Reader.readFully(loadErrorBody);
        } catch (JsonReadException unused) {
            String requestId = DbxRequestUtil.getRequestId(response);
            throw new BadRequestException(requestId, DbxRequestUtil.parseErrorBody(requestId, 400, loadErrorBody));
        }
    }

    private ChunkedUploadState chunkedUploadParse200(Response response) throws BadResponseException, NetworkIOException {
        return (ChunkedUploadState) DbxRequestUtil.readJsonFromResponse(ChunkedUploadState.Reader, response);
    }

    public String chunkedUploadFirst(byte[] bArr) throws DbxException {
        return chunkedUploadFirst(bArr, 0, bArr.length);
    }

    public String chunkedUploadFirst(byte[] bArr, int i, int i2) throws DbxException {
        return chunkedUploadFirst(i2, new ByteArrayCopier(bArr, i, i2));
    }

    public <E extends Throwable> String chunkedUploadFirst(int i, DbxStreamWriter<E> dbxStreamWriter) throws DbxException, Throwable {
        long j = (long) i;
        Response chunkedUploadCommon = chunkedUploadCommon(new String[0], j, dbxStreamWriter);
        try {
            if (chunkedUploadCheckForOffsetCorrection(chunkedUploadCommon) != null) {
                throw new BadResponseException(DbxRequestUtil.getRequestId(chunkedUploadCommon), "Got offset correction response on first chunk.");
            } else if (chunkedUploadCommon.getStatusCode() == 404) {
                throw new BadResponseException(DbxRequestUtil.getRequestId(chunkedUploadCommon), "Got a 404, but we didn't send an upload_id");
            } else if (chunkedUploadCommon.getStatusCode() == 200) {
                ChunkedUploadState chunkedUploadParse200 = chunkedUploadParse200(chunkedUploadCommon);
                if (chunkedUploadParse200.offset == j) {
                    return chunkedUploadParse200.uploadId;
                }
                String requestId = DbxRequestUtil.getRequestId(chunkedUploadCommon);
                StringBuilder sb = new StringBuilder();
                sb.append("Sent ");
                sb.append(i);
                sb.append(" bytes, but returned offset is ");
                sb.append(chunkedUploadParse200.offset);
                throw new BadResponseException(requestId, sb.toString());
            } else {
                throw DbxRequestUtil.unexpectedStatus(chunkedUploadCommon);
            }
        } finally {
            IOUtil.closeInput(chunkedUploadCommon.getBody());
        }
    }

    public long chunkedUploadAppend(String str, long j, byte[] bArr) throws DbxException {
        return chunkedUploadAppend(str, j, bArr, 0, bArr.length);
    }

    public long chunkedUploadAppend(String str, long j, byte[] bArr, int i, int i2) throws DbxException {
        return chunkedUploadAppend(str, j, (long) i2, new ByteArrayCopier(bArr, i, i2));
    }

    public <E extends Throwable> long chunkedUploadAppend(String str, long j, long j2, DbxStreamWriter<E> dbxStreamWriter) throws DbxException, Throwable {
        if (str == null) {
            throw new IllegalArgumentException("'uploadId' can't be null");
        } else if (str.length() == 0) {
            throw new IllegalArgumentException("'uploadId' can't be empty");
        } else if (j >= 0) {
            Response chunkedUploadCommon = chunkedUploadCommon(new String[]{"upload_id", str, "offset", Long.toString(j)}, j2, dbxStreamWriter);
            String requestId = DbxRequestUtil.getRequestId(chunkedUploadCommon);
            try {
                ChunkedUploadState chunkedUploadCheckForOffsetCorrection = chunkedUploadCheckForOffsetCorrection(chunkedUploadCommon);
                long j3 = j2 + j;
                if (chunkedUploadCheckForOffsetCorrection != null) {
                    if (!chunkedUploadCheckForOffsetCorrection.uploadId.equals(str)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("uploadId mismatch: us=");
                        sb.append(StringUtil.m33jq(str));
                        sb.append(", server=");
                        sb.append(StringUtil.m33jq(chunkedUploadCheckForOffsetCorrection.uploadId));
                        throw new BadResponseException(requestId, sb.toString());
                    } else if (chunkedUploadCheckForOffsetCorrection.offset == j) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Corrected offset is same as given: ");
                        sb2.append(j);
                        throw new BadResponseException(requestId, sb2.toString());
                    } else if (chunkedUploadCheckForOffsetCorrection.offset < j) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("we were at offset ");
                        sb3.append(j);
                        sb3.append(", server said ");
                        sb3.append(chunkedUploadCheckForOffsetCorrection.offset);
                        throw new BadResponseException(requestId, sb3.toString());
                    } else if (chunkedUploadCheckForOffsetCorrection.offset <= j3) {
                        return chunkedUploadCheckForOffsetCorrection.offset;
                    } else {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("we were at offset ");
                        sb4.append(j);
                        sb4.append(", server said ");
                        sb4.append(chunkedUploadCheckForOffsetCorrection.offset);
                        throw new BadResponseException(requestId, sb4.toString());
                    }
                } else if (chunkedUploadCommon.getStatusCode() == 200) {
                    ChunkedUploadState chunkedUploadParse200 = chunkedUploadParse200(chunkedUploadCommon);
                    if (chunkedUploadParse200.offset == j3) {
                        IOUtil.closeInput(chunkedUploadCommon.getBody());
                        return -1;
                    }
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Expected offset ");
                    sb5.append(j3);
                    sb5.append(" bytes, but returned offset is ");
                    sb5.append(chunkedUploadParse200.offset);
                    throw new BadResponseException(requestId, sb5.toString());
                } else {
                    throw DbxRequestUtil.unexpectedStatus(chunkedUploadCommon);
                }
            } finally {
                IOUtil.closeInput(chunkedUploadCommon.getBody());
            }
        } else {
            throw new IllegalArgumentException("'offset' can't be negative");
        }
    }

    public File chunkedUploadFinish(String str, DbxWriteMode dbxWriteMode, String str2) throws DbxException {
        DbxPathV1.checkArgNonRoot("targetPath", str);
        StringBuilder sb = new StringBuilder();
        sb.append("1/commit_chunked_upload/auto");
        sb.append(str);
        return (File) doPost(this.host.getContent(), sb.toString(), (String[]) LangUtil.arrayConcat(new String[]{"upload_id", str2}, dbxWriteMode.params), null, new ResponseHandler<File>() {
            public File handle(Response response) throws DbxException {
                if (response.getStatusCode() == 200) {
                    return (File) DbxRequestUtil.readJsonFromResponse(File.Reader, response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public Uploader startUploadFileChunked(String str, DbxWriteMode dbxWriteMode, long j) {
        return startUploadFileChunked(4194304, str, dbxWriteMode, j);
    }

    public Uploader startUploadFileChunked(int i, String str, DbxWriteMode dbxWriteMode, long j) {
        DbxPathV1.checkArg("targetPath", str);
        if (dbxWriteMode != null) {
            ChunkedUploader chunkedUploader = new ChunkedUploader(str, dbxWriteMode, j, new ChunkedUploadOutputStream(i));
            return chunkedUploader;
        }
        throw new IllegalArgumentException("'writeMode' can't be null");
    }

    public <E extends Throwable> File uploadFileChunked(String str, DbxWriteMode dbxWriteMode, long j, DbxStreamWriter<E> dbxStreamWriter) throws DbxException, Throwable {
        return finishUploadFile(startUploadFileChunked(str, dbxWriteMode, j), dbxStreamWriter);
    }

    public <E extends Throwable> File uploadFileChunked(int i, String str, DbxWriteMode dbxWriteMode, long j, DbxStreamWriter<E> dbxStreamWriter) throws DbxException, Throwable {
        return finishUploadFile(startUploadFileChunked(i, str, dbxWriteMode, j), dbxStreamWriter);
    }

    public DbxDelta<DbxEntry> getDelta(String str, boolean z) throws DbxException {
        return _getDelta(str, null, z);
    }

    public DbxDelta<DbxEntry> getDelta(String str) throws DbxException {
        return getDelta(str, false);
    }

    public <C> DbxDeltaC<C> getDeltaC(Collector<Entry<DbxEntry>, C> collector, String str, boolean z) throws DbxException {
        return _getDeltaC(collector, str, null, z);
    }

    public <C> DbxDeltaC<C> getDeltaC(Collector<Entry<DbxEntry>, C> collector, String str) throws DbxException {
        return getDeltaC(collector, str, false);
    }

    public DbxDelta<DbxEntry> getDeltaWithPathPrefix(String str, String str2, boolean z) throws DbxException {
        DbxPathV1.checkArg("path", str2);
        return _getDelta(str, str2, z);
    }

    public DbxDelta<DbxEntry> getDeltaWithPathPrefix(String str, String str2) throws DbxException {
        DbxPathV1.checkArg("path", str2);
        return _getDelta(str, str2, false);
    }

    public <C> DbxDeltaC<C> getDeltaCWithPathPrefix(Collector<Entry<DbxEntry>, C> collector, String str, String str2, boolean z) throws DbxException {
        DbxPathV1.checkArg("path", str2);
        return _getDeltaC(collector, str, str2, z);
    }

    public <C> DbxDeltaC<C> getDeltaCWithPathPrefix(Collector<Entry<DbxEntry>, C> collector, String str, String str2) throws DbxException {
        return getDeltaCWithPathPrefix(collector, str, str2, false);
    }

    private DbxDelta<DbxEntry> _getDelta(String str, String str2, boolean z) throws DbxException {
        String api = this.host.getApi();
        String str3 = "1/delta";
        String[] strArr = new String[6];
        strArr[0] = "cursor";
        strArr[1] = str;
        strArr[2] = "path_prefix";
        strArr[3] = str2;
        strArr[4] = "include_media_info";
        strArr[5] = z ? "true" : null;
        return (DbxDelta) doPost(api, str3, strArr, null, new ResponseHandler<DbxDelta<DbxEntry>>() {
            public DbxDelta<DbxEntry> handle(Response response) throws DbxException {
                if (response.getStatusCode() == 200) {
                    return (DbxDelta) DbxRequestUtil.readJsonFromResponse(new Reader(DbxEntry.Reader), response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    private <C> DbxDeltaC<C> _getDeltaC(final Collector<Entry<DbxEntry>, C> collector, String str, String str2, boolean z) throws DbxException {
        String api = this.host.getApi();
        String str3 = "1/delta";
        String[] strArr = new String[6];
        strArr[0] = "cursor";
        strArr[1] = str;
        strArr[2] = "path_prefix";
        strArr[3] = str2;
        strArr[4] = "include_media_info";
        strArr[5] = z ? "true" : null;
        return (DbxDeltaC) doPost(api, str3, strArr, null, new ResponseHandler<DbxDeltaC<C>>() {
            public DbxDeltaC<C> handle(Response response) throws DbxException {
                if (response.getStatusCode() == 200) {
                    return (DbxDeltaC) DbxRequestUtil.readJsonFromResponse(new DbxDeltaC.Reader(DbxEntry.Reader, collector), response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public String getDeltaLatestCursor(boolean z) throws DbxException {
        return _getDeltaLatestCursor(null, z);
    }

    public String getDeltaLatestCursor() throws DbxException {
        return _getDeltaLatestCursor(null, false);
    }

    public String getDeltaLatestCursorWithPathPrefix(String str, boolean z) throws DbxException {
        DbxPathV1.checkArg("path", str);
        return _getDeltaLatestCursor(str, z);
    }

    public String getDeltaLatestCursorWithPathPrefix(String str) throws DbxException {
        return getDeltaLatestCursorWithPathPrefix(str, false);
    }

    private String _getDeltaLatestCursor(String str, boolean z) throws DbxException {
        String api = this.host.getApi();
        String str2 = "1/delta/latest_cursor";
        String[] strArr = new String[4];
        strArr[0] = "path_prefix";
        strArr[1] = str;
        strArr[2] = "include_media_info";
        strArr[3] = z ? "true" : null;
        return (String) doPost(api, str2, strArr, null, new ResponseHandler<String>() {
            public String handle(Response response) throws DbxException {
                if (response.getStatusCode() == 200) {
                    return (String) DbxRequestUtil.readJsonFromResponse(DbxClientV1.LatestCursorReader, response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public DbxLongpollDeltaResult getLongpollDelta(String str, int i) throws DbxException {
        if (str == null) {
            throw new IllegalArgumentException("'cursor' can't be null");
        } else if (i < 30 || i > 480) {
            throw new IllegalArgumentException("'timeout' must be >=30 and <= 480");
        } else {
            return (DbxLongpollDeltaResult) DbxRequestUtil.doGet(getRequestConfig(), getAccessToken(), USER_AGENT_ID, this.host.getNotify(), "1/longpoll_delta", new String[]{"cursor", str, "timeout", Integer.toString(i)}, null, new ResponseHandler<DbxLongpollDeltaResult>() {
                public DbxLongpollDeltaResult handle(Response response) throws DbxException {
                    if (response.getStatusCode() == 200) {
                        return (DbxLongpollDeltaResult) DbxRequestUtil.readJsonFromResponse(DbxLongpollDeltaResult.Reader, response);
                    }
                    throw DbxRequestUtil.unexpectedStatus(response);
                }
            });
        }
    }

    public File getThumbnail(DbxThumbnailSize dbxThumbnailSize, DbxThumbnailFormat dbxThumbnailFormat, String str, String str2, OutputStream outputStream) throws DbxException, IOException {
        if (outputStream != null) {
            Downloader startGetThumbnail = startGetThumbnail(dbxThumbnailSize, dbxThumbnailFormat, str, str2);
            if (startGetThumbnail == null) {
                return null;
            }
            return startGetThumbnail.copyBodyAndClose(outputStream);
        }
        throw new IllegalArgumentException("'target' can't be null");
    }

    public Downloader startGetThumbnail(DbxThumbnailSize dbxThumbnailSize, DbxThumbnailFormat dbxThumbnailFormat, String str, String str2) throws DbxException {
        DbxPathV1.checkArgNonRoot("path", str);
        if (dbxThumbnailSize == null) {
            throw new IllegalArgumentException("'size' can't be null");
        } else if (dbxThumbnailFormat != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("1/thumbnails/auto");
            sb.append(str);
            return startGetSomething(sb.toString(), new String[]{"size", dbxThumbnailSize.ident, "format", dbxThumbnailFormat.ident, "rev", str2});
        } else {
            throw new IllegalArgumentException("'format' can't be null");
        }
    }

    public List<File> getRevisions(String str) throws DbxException {
        DbxPathV1.checkArgNonRoot("path", str);
        StringBuilder sb = new StringBuilder();
        sb.append("1/revisions/auto");
        sb.append(str);
        return (List) doGet(this.host.getApi(), sb.toString(), null, null, new ResponseHandler<List<File>>() {
            public List<File> handle(Response response) throws DbxException {
                if (response.getStatusCode() == 200) {
                    return (List) DbxRequestUtil.readJsonFromResponse(JsonArrayReader.m18mk(File.ReaderMaybeDeleted, NullSkipper.m19mk(new ArrayListCollector())), response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public File restoreFile(String str, String str2) throws DbxException {
        DbxPathV1.checkArgNonRoot("path", str);
        if (str2 == null) {
            throw new IllegalArgumentException("'rev' can't be null");
        } else if (str2.length() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("1/restore/auto");
            sb.append(str);
            return (File) doGet(this.host.getApi(), sb.toString(), new String[]{"rev", str2}, null, new ResponseHandler<File>() {
                public File handle(Response response) throws DbxException {
                    if (response.getStatusCode() == 404) {
                        return null;
                    }
                    if (response.getStatusCode() == 200) {
                        return (File) DbxRequestUtil.readJsonFromResponse(File.Reader, response);
                    }
                    throw DbxRequestUtil.unexpectedStatus(response);
                }
            });
        } else {
            throw new IllegalArgumentException("'rev' can't be empty");
        }
    }

    public List<DbxEntry> searchFileAndFolderNames(String str, String str2) throws DbxException {
        DbxPathV1.checkArg("basePath", str);
        if (str2 == null) {
            throw new IllegalArgumentException("'query' can't be null");
        } else if (str2.length() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("1/search/auto");
            sb.append(str);
            return (List) doPost(this.host.getApi(), sb.toString(), new String[]{SearchIntents.EXTRA_QUERY, str2}, null, new ResponseHandler<List<DbxEntry>>() {
                public List<DbxEntry> handle(Response response) throws DbxException {
                    if (response.getStatusCode() == 200) {
                        return (List) DbxRequestUtil.readJsonFromResponse(JsonArrayReader.m17mk(DbxEntry.Reader), response);
                    }
                    throw DbxRequestUtil.unexpectedStatus(response);
                }
            });
        } else {
            throw new IllegalArgumentException("'query' can't be empty");
        }
    }

    public String createShareableUrl(String str) throws DbxException {
        DbxPathV1.checkArg("path", str);
        StringBuilder sb = new StringBuilder();
        sb.append("1/shares/auto");
        sb.append(str);
        return (String) doPost(this.host.getApi(), sb.toString(), new String[]{"short_url", "false"}, null, new ResponseHandler<String>() {
            public String handle(Response response) throws DbxException {
                if (response.getStatusCode() == 404) {
                    return null;
                }
                if (response.getStatusCode() == 200) {
                    return ((DbxUrlWithExpiration) DbxRequestUtil.readJsonFromResponse(DbxUrlWithExpiration.Reader, response)).url;
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public DbxUrlWithExpiration createTemporaryDirectUrl(String str) throws DbxException {
        DbxPathV1.checkArgNonRoot("path", str);
        StringBuilder sb = new StringBuilder();
        sb.append("1/media/auto");
        sb.append(str);
        return (DbxUrlWithExpiration) doPost(this.host.getApi(), sb.toString(), null, null, new ResponseHandler<DbxUrlWithExpiration>() {
            public DbxUrlWithExpiration handle(Response response) throws DbxException {
                if (response.getStatusCode() == 404) {
                    return null;
                }
                if (response.getStatusCode() == 200) {
                    return (DbxUrlWithExpiration) DbxRequestUtil.readJsonFromResponse(DbxUrlWithExpiration.Reader, response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public String createCopyRef(String str) throws DbxException {
        DbxPathV1.checkArgNonRoot("path", str);
        StringBuilder sb = new StringBuilder();
        sb.append("1/copy_ref/auto");
        sb.append(str);
        return (String) doPost(this.host.getApi(), sb.toString(), null, null, new ResponseHandler<String>() {
            public String handle(Response response) throws DbxException {
                if (response.getStatusCode() == 404) {
                    return null;
                }
                if (response.getStatusCode() == 200) {
                    return ((CopyRef) DbxRequestUtil.readJsonFromResponse(CopyRef.Reader, response)).f66id;
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public DbxEntry copy(String str, String str2) throws DbxException {
        DbxPathV1.checkArg("fromPath", str);
        DbxPathV1.checkArgNonRoot("toPath", str2);
        return (DbxEntry) doPost(this.host.getApi(), "1/fileops/copy", new String[]{"root", "auto", "from_path", str, "to_path", str2}, null, new ResponseHandler<DbxEntry>() {
            public DbxEntry handle(Response response) throws DbxException {
                if (response.getStatusCode() == 403) {
                    return null;
                }
                if (response.getStatusCode() == 200) {
                    WithChildren withChildren = (WithChildren) DbxRequestUtil.readJsonFromResponse(WithChildren.Reader, response);
                    if (withChildren == null) {
                        return null;
                    }
                    return withChildren.entry;
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public DbxEntry copyFromCopyRef(String str, String str2) throws DbxException {
        if (str == null) {
            throw new IllegalArgumentException("'copyRef' can't be null");
        } else if (str.length() != 0) {
            DbxPathV1.checkArgNonRoot("toPath", str2);
            return (DbxEntry) doPost(this.host.getApi(), "1/fileops/copy", new String[]{"root", "auto", "from_copy_ref", str, "to_path", str2}, null, new ResponseHandler<DbxEntry>() {
                public DbxEntry handle(Response response) throws DbxException {
                    if (response.getStatusCode() == 200) {
                        WithChildren withChildren = (WithChildren) DbxRequestUtil.readJsonFromResponse(WithChildren.Reader, response);
                        if (withChildren == null) {
                            return null;
                        }
                        return withChildren.entry;
                    }
                    throw DbxRequestUtil.unexpectedStatus(response);
                }
            });
        } else {
            throw new IllegalArgumentException("'copyRef' can't be empty");
        }
    }

    public Folder createFolder(String str) throws DbxException {
        DbxPathV1.checkArgNonRoot("path", str);
        return (Folder) doPost(this.host.getApi(), "1/fileops/create_folder", new String[]{"root", "auto", "path", str}, null, new ResponseHandler<Folder>() {
            public Folder handle(Response response) throws DbxException {
                if (response.getStatusCode() == 403) {
                    return null;
                }
                if (response.getStatusCode() == 200) {
                    return (Folder) DbxRequestUtil.readJsonFromResponse(Folder.Reader, response);
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public void delete(String str) throws DbxException {
        DbxPathV1.checkArgNonRoot("path", str);
        doPost(this.host.getApi(), "1/fileops/delete", new String[]{"root", "auto", "path", str}, null, new ResponseHandler<Void>() {
            public Void handle(Response response) throws DbxException {
                if (response.getStatusCode() == 200) {
                    return null;
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    public DbxEntry move(String str, String str2) throws DbxException {
        DbxPathV1.checkArgNonRoot("fromPath", str);
        DbxPathV1.checkArgNonRoot("toPath", str2);
        return (DbxEntry) doPost(this.host.getApi(), "1/fileops/move", new String[]{"root", "auto", "from_path", str, "to_path", str2}, null, new ResponseHandler<DbxEntry>() {
            public DbxEntry handle(Response response) throws DbxException {
                if (response.getStatusCode() == 403) {
                    return null;
                }
                if (response.getStatusCode() == 200) {
                    WithChildren withChildren = (WithChildren) DbxRequestUtil.readJsonFromResponse(WithChildren.Reader, response);
                    if (withChildren == null) {
                        return null;
                    }
                    return withChildren.entry;
                }
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        });
    }

    private <T> T doGet(String str, String str2, String[] strArr, ArrayList<Header> arrayList, ResponseHandler<T> responseHandler) throws DbxException {
        return DbxRequestUtil.doGet(this.requestConfig, this.accessToken, USER_AGENT_ID, str, str2, strArr, arrayList, responseHandler);
    }

    public <T> T doPost(String str, String str2, String[] strArr, ArrayList<Header> arrayList, ResponseHandler<T> responseHandler) throws DbxException {
        return DbxRequestUtil.doPost(this.requestConfig, this.accessToken, USER_AGENT_ID, str, str2, strArr, arrayList, responseHandler);
    }
}
