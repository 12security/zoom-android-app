package com.eclipsesource.json;

import com.eclipsesource.json.JsonObject.Member;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.apache.http.message.TokenParser;

class JsonWriter {
    private static final char[] BS_CHARS = {TokenParser.ESCAPE, TokenParser.ESCAPE};
    private static final int CONTROL_CHARACTERS_END = 31;
    private static final int CONTROL_CHARACTERS_START = 0;
    private static final char[] CR_CHARS = {TokenParser.ESCAPE, 'r'};
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] LF_CHARS = {TokenParser.ESCAPE, 'n'};
    private static final char[] QUOT_CHARS = {TokenParser.ESCAPE, '\"'};
    private static final char[] TAB_CHARS = {TokenParser.ESCAPE, 't'};
    private static final char[] UNICODE_2028_CHARS = {TokenParser.ESCAPE, 'u', '2', '0', '2', '8'};
    private static final char[] UNICODE_2029_CHARS = {TokenParser.ESCAPE, 'u', '2', '0', '2', '9'};
    protected final Writer writer;

    JsonWriter(Writer writer2) {
        this.writer = writer2;
    }

    /* access modifiers changed from: 0000 */
    public void write(String str) throws IOException {
        this.writer.write(str);
    }

    /* access modifiers changed from: 0000 */
    public void writeString(String str) throws IOException {
        this.writer.write(34);
        int length = str.length();
        char[] cArr = new char[length];
        str.getChars(0, length, cArr, 0);
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char[] replacementChars = getReplacementChars(cArr[i2]);
            if (replacementChars != null) {
                this.writer.write(cArr, i, i2 - i);
                this.writer.write(replacementChars);
                i = i2 + 1;
            }
        }
        this.writer.write(cArr, i, length - i);
        this.writer.write(34);
    }

    private static char[] getReplacementChars(char c) {
        if (c == '\"') {
            return QUOT_CHARS;
        }
        if (c == '\\') {
            return BS_CHARS;
        }
        if (c == 10) {
            return LF_CHARS;
        }
        if (c == 13) {
            return CR_CHARS;
        }
        if (c == 9) {
            return TAB_CHARS;
        }
        if (c == 8232) {
            return UNICODE_2028_CHARS;
        }
        if (c == 8233) {
            return UNICODE_2029_CHARS;
        }
        if (c < 0 || c > 31) {
            return null;
        }
        char[] cArr = {TokenParser.ESCAPE, 'u', '0', '0', '0', '0'};
        char[] cArr2 = HEX_DIGITS;
        cArr[4] = cArr2[(c >> 4) & 15];
        cArr[5] = cArr2[c & 15];
        return cArr;
    }

    /* access modifiers changed from: protected */
    public void writeObject(JsonObject jsonObject) throws IOException {
        writeBeginObject();
        Iterator it = jsonObject.iterator();
        boolean z = true;
        while (it.hasNext()) {
            Member member = (Member) it.next();
            if (!z) {
                writeObjectValueSeparator();
            }
            writeString(member.getName());
            writeNameValueSeparator();
            member.getValue().write(this);
            z = false;
        }
        writeEndObject();
    }

    /* access modifiers changed from: protected */
    public void writeBeginObject() throws IOException {
        this.writer.write(123);
    }

    /* access modifiers changed from: protected */
    public void writeEndObject() throws IOException {
        this.writer.write(125);
    }

    /* access modifiers changed from: protected */
    public void writeNameValueSeparator() throws IOException {
        this.writer.write(58);
    }

    /* access modifiers changed from: protected */
    public void writeObjectValueSeparator() throws IOException {
        this.writer.write(44);
    }

    /* access modifiers changed from: protected */
    public void writeArray(JsonArray jsonArray) throws IOException {
        writeBeginArray();
        Iterator it = jsonArray.iterator();
        boolean z = true;
        while (it.hasNext()) {
            JsonValue jsonValue = (JsonValue) it.next();
            if (!z) {
                writeArrayValueSeparator();
            }
            jsonValue.write(this);
            z = false;
        }
        writeEndArray();
    }

    /* access modifiers changed from: protected */
    public void writeBeginArray() throws IOException {
        this.writer.write(91);
    }

    /* access modifiers changed from: protected */
    public void writeEndArray() throws IOException {
        this.writer.write(93);
    }

    /* access modifiers changed from: protected */
    public void writeArrayValueSeparator() throws IOException {
        this.writer.write(44);
    }
}
