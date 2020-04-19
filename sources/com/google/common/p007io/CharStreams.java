package com.google.common.p007io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

@GwtIncompatible
@Beta
/* renamed from: com.google.common.io.CharStreams */
public final class CharStreams {

    /* renamed from: com.google.common.io.CharStreams$NullWriter */
    private static final class NullWriter extends Writer {
        /* access modifiers changed from: private */
        public static final NullWriter INSTANCE = new NullWriter();

        public Writer append(char c) {
            return this;
        }

        public void close() {
        }

        public void flush() {
        }

        public String toString() {
            return "CharStreams.nullWriter()";
        }

        public void write(int i) {
        }

        private NullWriter() {
        }

        public void write(char[] cArr) {
            Preconditions.checkNotNull(cArr);
        }

        public void write(char[] cArr, int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2 + i, cArr.length);
        }

        public void write(String str) {
            Preconditions.checkNotNull(str);
        }

        public void write(String str, int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2 + i, str.length());
        }

        public Writer append(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return this;
        }

        public Writer append(CharSequence charSequence, int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, charSequence.length());
            return this;
        }
    }

    static CharBuffer createBuffer() {
        return CharBuffer.allocate(2048);
    }

    private CharStreams() {
    }

    @CanIgnoreReturnValue
    public static long copy(Readable readable, Appendable appendable) throws IOException {
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(appendable);
        CharBuffer createBuffer = createBuffer();
        long j = 0;
        while (readable.read(createBuffer) != -1) {
            createBuffer.flip();
            appendable.append(createBuffer);
            j += (long) createBuffer.remaining();
            createBuffer.clear();
        }
        return j;
    }

    public static String toString(Readable readable) throws IOException {
        return toStringBuilder(readable).toString();
    }

    private static StringBuilder toStringBuilder(Readable readable) throws IOException {
        StringBuilder sb = new StringBuilder();
        copy(readable, sb);
        return sb;
    }

    public static List<String> readLines(Readable readable) throws IOException {
        ArrayList arrayList = new ArrayList();
        LineReader lineReader = new LineReader(readable);
        while (true) {
            String readLine = lineReader.readLine();
            if (readLine == null) {
                return arrayList;
            }
            arrayList.add(readLine);
        }
    }

    @CanIgnoreReturnValue
    public static <T> T readLines(Readable readable, LineProcessor<T> lineProcessor) throws IOException {
        String readLine;
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(lineProcessor);
        LineReader lineReader = new LineReader(readable);
        do {
            readLine = lineReader.readLine();
            if (readLine == null) {
                break;
            }
        } while (lineProcessor.processLine(readLine));
        return lineProcessor.getResult();
    }

    @CanIgnoreReturnValue
    public static long exhaust(Readable readable) throws IOException {
        CharBuffer createBuffer = createBuffer();
        long j = 0;
        while (true) {
            long read = (long) readable.read(createBuffer);
            if (read == -1) {
                return j;
            }
            j += read;
            createBuffer.clear();
        }
    }

    public static void skipFully(Reader reader, long j) throws IOException {
        Preconditions.checkNotNull(reader);
        while (j > 0) {
            long skip = reader.skip(j);
            if (skip != 0) {
                j -= skip;
            } else {
                throw new EOFException();
            }
        }
    }

    public static Writer nullWriter() {
        return NullWriter.INSTANCE;
    }

    public static Writer asWriter(Appendable appendable) {
        if (appendable instanceof Writer) {
            return (Writer) appendable;
        }
        return new AppendableWriter(appendable);
    }
}
