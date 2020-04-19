package com.dropbox.core.http;

import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.IOUtil.ProgressListener;
import com.dropbox.core.util.IOUtil.ReadException;
import com.dropbox.core.util.IOUtil.WriteException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public abstract class HttpRequestor {
    public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(20);
    public static final long DEFAULT_READ_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(2);

    public static final class Header {
        private final String key;
        private final String value;

        public Header(String str, String str2) {
            this.key = str;
            this.value = str2;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static final class Response {
        private final InputStream body;
        private final Map<String, List<String>> headers;
        private final int statusCode;

        public Response(int i, InputStream inputStream, Map<String, ? extends List<String>> map) {
            this.statusCode = i;
            this.body = inputStream;
            this.headers = asUnmodifiableCaseInsensitiveMap(map);
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        public InputStream getBody() {
            return this.body;
        }

        public Map<String, List<String>> getHeaders() {
            return this.headers;
        }

        private static final Map<String, List<String>> asUnmodifiableCaseInsensitiveMap(Map<String, ? extends List<String>> map) {
            TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            for (Entry entry : map.entrySet()) {
                if (!(entry.getKey() == null || ((String) entry.getKey()).trim().length() == 0)) {
                    treeMap.put(entry.getKey(), Collections.unmodifiableList((List) entry.getValue()));
                }
            }
            return Collections.unmodifiableMap(treeMap);
        }
    }

    public static abstract class Uploader {
        protected ProgressListener progressListener;

        public abstract void abort();

        public abstract void close();

        public abstract Response finish() throws IOException;

        public abstract OutputStream getBody();

        public void upload(File file) throws IOException {
            try {
                upload((InputStream) new FileInputStream(file));
            } catch (ReadException e) {
                throw e.getCause();
            } catch (WriteException e2) {
                throw e2.getCause();
            }
        }

        public void upload(InputStream inputStream, long j) throws IOException {
            upload(IOUtil.limit(inputStream, j));
        }

        public void upload(InputStream inputStream) throws IOException {
            OutputStream body = getBody();
            try {
                IOUtil.copyStreamToStream(inputStream, body);
            } finally {
                body.close();
            }
        }

        public void upload(byte[] bArr) throws IOException {
            OutputStream body = getBody();
            try {
                body.write(bArr);
            } finally {
                body.close();
            }
        }

        public void setProgressListener(ProgressListener progressListener2) {
            this.progressListener = progressListener2;
        }
    }

    public abstract Response doGet(String str, Iterable<Header> iterable) throws IOException;

    public abstract Uploader startPost(String str, Iterable<Header> iterable) throws IOException;

    public abstract Uploader startPut(String str, Iterable<Header> iterable) throws IOException;
}
