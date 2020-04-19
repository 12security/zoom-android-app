package com.google.api.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Beta
public final class PemReader {
    private static final Pattern BEGIN_PATTERN = Pattern.compile("-----BEGIN ([A-Z ]+)-----");
    private static final Pattern END_PATTERN = Pattern.compile("-----END ([A-Z ]+)-----");
    private BufferedReader reader;

    public static final class Section {
        private final byte[] base64decodedBytes;
        private final String title;

        Section(String str, byte[] bArr) {
            this.title = (String) Preconditions.checkNotNull(str);
            this.base64decodedBytes = (byte[]) Preconditions.checkNotNull(bArr);
        }

        public String getTitle() {
            return this.title;
        }

        public byte[] getBase64DecodedBytes() {
            return this.base64decodedBytes;
        }
    }

    public PemReader(Reader reader2) {
        this.reader = new BufferedReader(reader2);
    }

    public Section readNextSection() throws IOException {
        return readNextSection(null);
    }

    public Section readNextSection(String str) throws IOException {
        StringBuilder sb = null;
        String str2 = null;
        while (true) {
            String readLine = this.reader.readLine();
            if (readLine == null) {
                Preconditions.checkArgument(str2 == null, "missing end tag (%s)", str2);
                return null;
            } else if (sb == null) {
                Matcher matcher = BEGIN_PATTERN.matcher(readLine);
                if (matcher.matches()) {
                    String group = matcher.group(1);
                    if (str == null || group.equals(str)) {
                        sb = new StringBuilder();
                        str2 = group;
                    }
                }
            } else {
                Matcher matcher2 = END_PATTERN.matcher(readLine);
                if (matcher2.matches()) {
                    String group2 = matcher2.group(1);
                    Preconditions.checkArgument(group2.equals(str2), "end tag (%s) doesn't match begin tag (%s)", group2, str2);
                    return new Section(str2, Base64.decodeBase64(sb.toString()));
                }
                sb.append(readLine);
            }
        }
    }

    public static Section readFirstSectionAndClose(Reader reader2) throws IOException {
        return readFirstSectionAndClose(reader2, null);
    }

    public static Section readFirstSectionAndClose(Reader reader2, String str) throws IOException {
        PemReader pemReader = new PemReader(reader2);
        try {
            return pemReader.readNextSection(str);
        } finally {
            pemReader.close();
        }
    }

    public void close() throws IOException {
        this.reader.close();
    }
}
