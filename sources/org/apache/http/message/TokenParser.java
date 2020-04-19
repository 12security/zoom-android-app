package org.apache.http.message;

import java.util.BitSet;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class TokenParser {

    /* renamed from: CR */
    public static final char f495CR = '\r';
    public static final char DQUOTE = '\"';
    public static final char ESCAPE = '\\';

    /* renamed from: HT */
    public static final char f496HT = '\t';
    public static final TokenParser INSTANCE = new TokenParser();

    /* renamed from: LF */
    public static final char f497LF = '\n';

    /* renamed from: SP */
    public static final char f498SP = ' ';

    public static boolean isWhitespace(char c) {
        return c == ' ' || c == 9 || c == 13 || c == 10;
    }

    public static BitSet INIT_BITSET(int... iArr) {
        BitSet bitSet = new BitSet();
        for (int i : iArr) {
            bitSet.set(i);
        }
        return bitSet;
    }

    public String parseToken(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor, BitSet bitSet) {
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        while (!parserCursor.atEnd()) {
            char charAt = charArrayBuffer.charAt(parserCursor.getPos());
            if (bitSet != null && bitSet.get(charAt)) {
                break;
            } else if (isWhitespace(charAt)) {
                skipWhiteSpace(charArrayBuffer, parserCursor);
                z = true;
            } else {
                if (z && sb.length() > 0) {
                    sb.append(f498SP);
                }
                copyContent(charArrayBuffer, parserCursor, bitSet, sb);
                z = false;
            }
        }
        return sb.toString();
    }

    public String parseValue(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor, BitSet bitSet) {
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        while (!parserCursor.atEnd()) {
            char charAt = charArrayBuffer.charAt(parserCursor.getPos());
            if (bitSet != null && bitSet.get(charAt)) {
                break;
            } else if (isWhitespace(charAt)) {
                skipWhiteSpace(charArrayBuffer, parserCursor);
                z = true;
            } else if (charAt == '\"') {
                if (z && sb.length() > 0) {
                    sb.append(f498SP);
                }
                copyQuotedContent(charArrayBuffer, parserCursor, sb);
                z = false;
            } else {
                if (z && sb.length() > 0) {
                    sb.append(f498SP);
                }
                copyUnquotedContent(charArrayBuffer, parserCursor, bitSet, sb);
                z = false;
            }
        }
        return sb.toString();
    }

    public void skipWhiteSpace(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor) {
        int pos = parserCursor.getPos();
        int pos2 = parserCursor.getPos();
        int upperBound = parserCursor.getUpperBound();
        while (pos2 < upperBound && isWhitespace(charArrayBuffer.charAt(pos2))) {
            pos++;
            pos2++;
        }
        parserCursor.updatePos(pos);
    }

    public void copyContent(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor, BitSet bitSet, StringBuilder sb) {
        int pos = parserCursor.getPos();
        int upperBound = parserCursor.getUpperBound();
        for (int pos2 = parserCursor.getPos(); pos2 < upperBound; pos2++) {
            char charAt = charArrayBuffer.charAt(pos2);
            if ((bitSet != null && bitSet.get(charAt)) || isWhitespace(charAt)) {
                break;
            }
            pos++;
            sb.append(charAt);
        }
        parserCursor.updatePos(pos);
    }

    public void copyUnquotedContent(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor, BitSet bitSet, StringBuilder sb) {
        int pos = parserCursor.getPos();
        int upperBound = parserCursor.getUpperBound();
        for (int pos2 = parserCursor.getPos(); pos2 < upperBound; pos2++) {
            char charAt = charArrayBuffer.charAt(pos2);
            if ((bitSet != null && bitSet.get(charAt)) || isWhitespace(charAt) || charAt == '\"') {
                break;
            }
            pos++;
            sb.append(charAt);
        }
        parserCursor.updatePos(pos);
    }

    public void copyQuotedContent(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor, StringBuilder sb) {
        if (!parserCursor.atEnd()) {
            int pos = parserCursor.getPos();
            int pos2 = parserCursor.getPos();
            int upperBound = parserCursor.getUpperBound();
            if (charArrayBuffer.charAt(pos) == '\"') {
                int i = pos2 + 1;
                int i2 = pos + 1;
                boolean z = false;
                while (true) {
                    if (i >= upperBound) {
                        break;
                    }
                    char charAt = charArrayBuffer.charAt(i);
                    if (z) {
                        if (!(charAt == '\"' || charAt == '\\')) {
                            sb.append(ESCAPE);
                        }
                        sb.append(charAt);
                        z = false;
                    } else if (charAt == '\"') {
                        i2++;
                        break;
                    } else if (charAt == '\\') {
                        z = true;
                    } else if (!(charAt == 13 || charAt == 10)) {
                        sb.append(charAt);
                    }
                    i++;
                    i2++;
                }
                parserCursor.updatePos(i2);
            }
        }
    }
}
