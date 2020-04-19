package com.google.api.client.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingOutputStream extends FilterOutputStream {
    private final LoggingByteArrayOutputStream logStream;

    public LoggingOutputStream(OutputStream outputStream, Logger logger, Level level, int i) {
        super(outputStream);
        this.logStream = new LoggingByteArrayOutputStream(logger, level, i);
    }

    public void write(int i) throws IOException {
        this.out.write(i);
        this.logStream.write(i);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
        this.logStream.write(bArr, i, i2);
    }

    public void close() throws IOException {
        this.logStream.close();
        super.close();
    }

    public final LoggingByteArrayOutputStream getLogStream() {
        return this.logStream;
    }
}
