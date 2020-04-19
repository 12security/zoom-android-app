package com.eclipsesource.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.apache.http.message.TokenParser;

class JsonParser {
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final int MIN_BUFFER_SIZE = 10;
    private final char[] buffer;
    private int bufferOffset;
    private StringBuilder captureBuffer;
    private int captureStart;
    private int current;
    private int fill;
    private int index;
    private int line;
    private int lineOffset;
    private final Reader reader;

    JsonParser(String str) {
        this(new StringReader(str), Math.max(10, Math.min(1024, str.length())));
    }

    JsonParser(Reader reader2) {
        this(reader2, 1024);
    }

    JsonParser(Reader reader2, int i) {
        this.reader = reader2;
        this.buffer = new char[i];
        this.line = 1;
        this.captureStart = -1;
    }

    /* access modifiers changed from: 0000 */
    public JsonValue parse() throws IOException {
        read();
        skipWhiteSpace();
        JsonValue readValue = readValue();
        skipWhiteSpace();
        if (isEndOfText()) {
            return readValue;
        }
        throw error("Unexpected character");
    }

    private JsonValue readValue() throws IOException {
        int i = this.current;
        if (i == 34) {
            return readString();
        }
        if (i != 45) {
            if (i == 91) {
                return readArray();
            }
            if (i == 102) {
                return readFalse();
            }
            if (i == 110) {
                return readNull();
            }
            if (i == 116) {
                return readTrue();
            }
            if (i == 123) {
                return readObject();
            }
            switch (i) {
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                    break;
                default:
                    throw expected("value");
            }
        }
        return readNumber();
    }

    private JsonArray readArray() throws IOException {
        read();
        JsonArray jsonArray = new JsonArray();
        skipWhiteSpace();
        if (readChar(']')) {
            return jsonArray;
        }
        do {
            skipWhiteSpace();
            jsonArray.add(readValue());
            skipWhiteSpace();
        } while (readChar(','));
        if (readChar(']')) {
            return jsonArray;
        }
        throw expected("',' or ']'");
    }

    private JsonObject readObject() throws IOException {
        read();
        JsonObject jsonObject = new JsonObject();
        skipWhiteSpace();
        if (readChar('}')) {
            return jsonObject;
        }
        do {
            skipWhiteSpace();
            String readName = readName();
            skipWhiteSpace();
            if (readChar(':')) {
                skipWhiteSpace();
                jsonObject.add(readName, readValue());
                skipWhiteSpace();
            } else {
                throw expected("':'");
            }
        } while (readChar(','));
        if (readChar('}')) {
            return jsonObject;
        }
        throw expected("',' or '}'");
    }

    private String readName() throws IOException {
        if (this.current == 34) {
            return readStringInternal();
        }
        throw expected("name");
    }

    private JsonValue readNull() throws IOException {
        read();
        readRequiredChar('u');
        readRequiredChar('l');
        readRequiredChar('l');
        return JsonValue.NULL;
    }

    private JsonValue readTrue() throws IOException {
        read();
        readRequiredChar('r');
        readRequiredChar('u');
        readRequiredChar('e');
        return JsonValue.TRUE;
    }

    private JsonValue readFalse() throws IOException {
        read();
        readRequiredChar('a');
        readRequiredChar('l');
        readRequiredChar('s');
        readRequiredChar('e');
        return JsonValue.FALSE;
    }

    private void readRequiredChar(char c) throws IOException {
        if (!readChar(c)) {
            StringBuilder sb = new StringBuilder();
            sb.append("'");
            sb.append(c);
            sb.append("'");
            throw expected(sb.toString());
        }
    }

    private JsonValue readString() throws IOException {
        return new JsonString(readStringInternal());
    }

    private String readStringInternal() throws IOException {
        read();
        startCapture();
        while (true) {
            int i = this.current;
            if (i == 34) {
                String endCapture = endCapture();
                read();
                return endCapture;
            } else if (i == 92) {
                pauseCapture();
                readEscape();
                startCapture();
            } else if (i >= 32) {
                read();
            } else {
                throw expected("valid string character");
            }
        }
    }

    private void readEscape() throws IOException {
        read();
        int i = this.current;
        if (i == 34 || i == 47 || i == 92) {
            this.captureBuffer.append((char) this.current);
        } else if (i == 98) {
            this.captureBuffer.append(8);
        } else if (i == 102) {
            this.captureBuffer.append(12);
        } else if (i == 110) {
            this.captureBuffer.append(10);
        } else if (i != 114) {
            switch (i) {
                case 116:
                    this.captureBuffer.append(9);
                    break;
                case 117:
                    char[] cArr = new char[4];
                    int i2 = 0;
                    while (i2 < 4) {
                        read();
                        if (isHexDigit()) {
                            cArr[i2] = (char) this.current;
                            i2++;
                        } else {
                            throw expected("hexadecimal digit");
                        }
                    }
                    this.captureBuffer.append((char) Integer.parseInt(String.valueOf(cArr), 16));
                    break;
                default:
                    throw expected("valid escape sequence");
            }
        } else {
            this.captureBuffer.append(TokenParser.f495CR);
        }
        read();
    }

    private JsonValue readNumber() throws IOException {
        startCapture();
        readChar('-');
        int i = this.current;
        if (readDigit()) {
            if (i != 48) {
                do {
                } while (readDigit());
            }
            readFraction();
            readExponent();
            return new JsonNumber(endCapture());
        }
        throw expected("digit");
    }

    private boolean readFraction() throws IOException {
        if (!readChar('.')) {
            return false;
        }
        if (readDigit()) {
            do {
            } while (readDigit());
            return true;
        }
        throw expected("digit");
    }

    private boolean readExponent() throws IOException {
        if (!readChar('e') && !readChar('E')) {
            return false;
        }
        if (!readChar('+')) {
            readChar('-');
        }
        if (readDigit()) {
            do {
            } while (readDigit());
            return true;
        }
        throw expected("digit");
    }

    private boolean readChar(char c) throws IOException {
        if (this.current != c) {
            return false;
        }
        read();
        return true;
    }

    private boolean readDigit() throws IOException {
        if (!isDigit()) {
            return false;
        }
        read();
        return true;
    }

    private void skipWhiteSpace() throws IOException {
        while (isWhiteSpace()) {
            read();
        }
    }

    private void read() throws IOException {
        if (!isEndOfText()) {
            int i = this.index;
            int i2 = this.fill;
            if (i == i2) {
                int i3 = this.captureStart;
                if (i3 != -1) {
                    this.captureBuffer.append(this.buffer, i3, i2 - i3);
                    this.captureStart = 0;
                }
                this.bufferOffset += this.fill;
                Reader reader2 = this.reader;
                char[] cArr = this.buffer;
                this.fill = reader2.read(cArr, 0, cArr.length);
                this.index = 0;
                if (this.fill == -1) {
                    this.current = -1;
                    return;
                }
            }
            if (this.current == 10) {
                this.line++;
                this.lineOffset = this.bufferOffset + this.index;
            }
            char[] cArr2 = this.buffer;
            int i4 = this.index;
            this.index = i4 + 1;
            this.current = cArr2[i4];
            return;
        }
        throw error("Unexpected end of input");
    }

    private void startCapture() {
        if (this.captureBuffer == null) {
            this.captureBuffer = new StringBuilder();
        }
        this.captureStart = this.index - 1;
    }

    private void pauseCapture() {
        int i = this.current == -1 ? this.index : this.index - 1;
        StringBuilder sb = this.captureBuffer;
        char[] cArr = this.buffer;
        int i2 = this.captureStart;
        sb.append(cArr, i2, i - i2);
        this.captureStart = -1;
    }

    private String endCapture() {
        String str;
        int i = this.current == -1 ? this.index : this.index - 1;
        if (this.captureBuffer.length() > 0) {
            StringBuilder sb = this.captureBuffer;
            char[] cArr = this.buffer;
            int i2 = this.captureStart;
            sb.append(cArr, i2, i - i2);
            str = this.captureBuffer.toString();
            this.captureBuffer.setLength(0);
        } else {
            char[] cArr2 = this.buffer;
            int i3 = this.captureStart;
            str = new String(cArr2, i3, i - i3);
        }
        this.captureStart = -1;
        return str;
    }

    private ParseException expected(String str) {
        if (isEndOfText()) {
            return error("Unexpected end of input");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected ");
        sb.append(str);
        return error(sb.toString());
    }

    private ParseException error(String str) {
        int i = this.bufferOffset + this.index;
        int i2 = i - this.lineOffset;
        if (!isEndOfText()) {
            i--;
        }
        return new ParseException(str, i, this.line, i2 - 1);
    }

    private boolean isWhiteSpace() {
        int i = this.current;
        return i == 32 || i == 9 || i == 10 || i == 13;
    }

    private boolean isDigit() {
        int i = this.current;
        return i >= 48 && i <= 57;
    }

    private boolean isHexDigit() {
        int i = this.current;
        if (i < 48 || i > 57) {
            int i2 = this.current;
            if (i2 < 97 || i2 > 102) {
                int i3 = this.current;
                if (i3 < 65 || i3 > 70) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isEndOfText() {
        return this.current == -1;
    }
}
