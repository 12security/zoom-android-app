package com.dropbox.core.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.CharacterCodingException;

public class IOUtil {
    public static final OutputStream BlackHoleOutputStream = new OutputStream() {
        public void write(int i) {
        }

        public void write(byte[] bArr) {
        }

        public void write(byte[] bArr, int i, int i2) {
        }
    };
    public static final int DEFAULT_COPY_BUFFER_SIZE = 16384;
    public static final InputStream EmptyInputStream = new InputStream() {
        public int read() {
            return -1;
        }

        public int read(byte[] bArr) {
            return -1;
        }

        public int read(byte[] bArr, int i, int i2) {
            return -1;
        }
    };

    private static final class LimitInputStream extends FilterInputStream {
        private long left;

        public boolean markSupported() {
            return false;
        }

        public LimitInputStream(InputStream inputStream, long j) {
            super(inputStream);
            if (inputStream == null) {
                throw new NullPointerException("in");
            } else if (j >= 0) {
                this.left = j;
            } else {
                throw new IllegalArgumentException("limit must be non-negative");
            }
        }

        public int available() throws IOException {
            return (int) Math.min((long) this.in.available(), this.left);
        }

        public synchronized void reset() throws IOException {
            throw new IOException("mark not supported");
        }

        public int read() throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int read = this.in.read();
            if (read != -1) {
                this.left--;
            }
            return read;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            long j = this.left;
            if (j == 0) {
                return -1;
            }
            int read = this.in.read(bArr, i, (int) Math.min((long) i2, j));
            if (read != -1) {
                this.left -= (long) read;
            }
            return read;
        }

        public long skip(long j) throws IOException {
            long skip = this.in.skip(Math.min(j, this.left));
            this.left -= skip;
            return skip;
        }
    }

    public interface ProgressListener {
        void onProgress(long j);
    }

    public static final class ReadException extends WrappedException {
        private static final long serialVersionUID = 0;

        public ReadException(String str, IOException iOException) {
            super(str, iOException);
        }

        public ReadException(IOException iOException) {
            super(iOException);
        }
    }

    public static abstract class WrappedException extends IOException {
        private static final long serialVersionUID = 0;

        public WrappedException(String str, IOException iOException) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": ");
            sb.append(iOException.getMessage());
            super(sb.toString(), iOException);
        }

        public WrappedException(IOException iOException) {
            super(iOException);
        }

        public IOException getCause() {
            return (IOException) super.getCause();
        }

        public String getMessage() {
            String message = super.getCause().getMessage();
            return message == null ? "" : message;
        }
    }

    public static final class WriteException extends WrappedException {
        private static final long serialVersionUID = 0;

        public WriteException(String str, IOException iOException) {
            super(str, iOException);
        }

        public WriteException(IOException iOException) {
            super(iOException);
        }
    }

    public static Reader utf8Reader(InputStream inputStream) {
        return new InputStreamReader(inputStream, StringUtil.UTF8.newDecoder());
    }

    public static Writer utf8Writer(OutputStream outputStream) {
        return new OutputStreamWriter(outputStream, StringUtil.UTF8.newEncoder());
    }

    public static String toUtf8String(InputStream inputStream) throws ReadException, CharacterCodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            copyStreamToStream(inputStream, byteArrayOutputStream);
            return StringUtil.utf8ToString(byteArrayOutputStream.toByteArray());
        } catch (WriteException e) {
            throw new RuntimeException("impossible", e);
        }
    }

    public static void copyStreamToStream(InputStream inputStream, OutputStream outputStream) throws ReadException, WriteException {
        copyStreamToStream(inputStream, outputStream, 16384);
    }

    public static void copyStreamToStream(InputStream inputStream, OutputStream outputStream, byte[] bArr) throws ReadException, WriteException {
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    try {
                        outputStream.write(bArr, 0, read);
                    } catch (IOException e) {
                        throw new WriteException(e);
                    }
                } else {
                    return;
                }
            } catch (IOException e2) {
                throw new ReadException(e2);
            }
        }
    }

    public static void copyStreamToStream(InputStream inputStream, OutputStream outputStream, int i) throws ReadException, WriteException {
        copyStreamToStream(inputStream, outputStream, new byte[i]);
    }

    public static byte[] slurp(InputStream inputStream, int i) throws IOException {
        return slurp(inputStream, i, new byte[16384]);
    }

    public static byte[] slurp(InputStream inputStream, int i, byte[] bArr) throws IOException {
        if (i >= 0) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            copyStreamToStream(inputStream, (OutputStream) byteArrayOutputStream, bArr);
            return byteArrayOutputStream.toByteArray();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("'byteLimit' must be non-negative: ");
        sb.append(i);
        throw new RuntimeException(sb.toString());
    }

    public void copyFileToStream(File file, OutputStream outputStream) throws ReadException, WriteException {
        copyFileToStream(file, outputStream, 16384);
    }

    public void copyFileToStream(File file, OutputStream outputStream, int i) throws ReadException, WriteException {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                copyStreamToStream((InputStream) fileInputStream, outputStream, i);
            } finally {
                closeInput((InputStream) fileInputStream);
            }
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }

    public void copyStreamToFile(InputStream inputStream, File file) throws ReadException, WriteException {
        copyStreamToFile(inputStream, file, 16384);
    }

    public void copyStreamToFile(InputStream inputStream, File file, int i) throws ReadException, WriteException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                copyStreamToStream(inputStream, (OutputStream) fileOutputStream, i);
                try {
                } catch (IOException e) {
                    throw new WriteException(e);
                }
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e2) {
                    throw new WriteException(e2);
                }
            }
        } catch (IOException e3) {
            throw new WriteException(e3);
        }
    }

    public static void closeInput(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException unused) {
        }
    }

    public static void closeInput(Reader reader) {
        try {
            reader.close();
        } catch (IOException unused) {
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static InputStream limit(InputStream inputStream, long j) {
        return new LimitInputStream(inputStream, j);
    }
}
