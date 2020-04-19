package com.zipow.videobox.markdown;

import android.text.SpannableStringBuilder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class Processor {
    @Nullable
    private String content;
    @NonNull
    private final Emitter emitter = new Emitter();
    private final Reader reader;

    protected Processor(Reader reader2) {
        this.reader = reader2;
    }

    @NonNull
    public static final SpannableStringBuilder process(Reader reader2) throws IOException {
        if (!(reader2 instanceof BufferedReader)) {
            reader2 = new BufferedReader(reader2);
        }
        return new Processor(reader2).process();
    }

    public static final SpannableStringBuilder process(@NonNull String str) {
        try {
            return process((Reader) new StringReader(str));
        } catch (IOException unused) {
            return null;
        }
    }

    @NonNull
    public static final SpannableStringBuilder process(@NonNull File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        SpannableStringBuilder process = process((InputStream) fileInputStream);
        fileInputStream.close();
        return process;
    }

    @NonNull
    public static final SpannableStringBuilder process(@NonNull InputStream inputStream) throws IOException {
        return new Processor(new BufferedReader(new InputStreamReader(inputStream))).process();
    }

    private String readContent() throws IOException {
        if (this.reader == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(80);
        while (true) {
            int read = this.reader.read();
            if (read == -1) {
                return sb.toString();
            }
            sb.append((char) read);
        }
    }

    @NonNull
    private SpannableStringBuilder process() throws IOException {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        this.content = readContent();
        String str = this.content;
        if (str != null) {
            this.emitter.recursiveEmitLine(spannableStringBuilder, str, 0, MarkToken.NONE);
        }
        return spannableStringBuilder;
    }
}
