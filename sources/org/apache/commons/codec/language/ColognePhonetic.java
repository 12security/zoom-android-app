package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder {
    private static final char[] AEIJOUY = {'A', 'E', 'I', 'J', 'O', 'U', 'Y'};
    private static final char[] AHKLOQRUX = {'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'};
    private static final char[] AHOUKQX = {'A', 'H', 'O', 'U', 'K', 'Q', 'X'};
    private static final char[] CKQ = {'C', 'K', 'Q'};
    private static final char[] GKQ = {'G', 'K', 'Q'};
    private static final char[][] PREPROCESS_MAP = {new char[]{196, 'A'}, new char[]{220, 'U'}, new char[]{214, 'O'}, new char[]{223, 'S'}};
    private static final char[] SCZ = {'S', 'C', 'Z'};

    /* renamed from: SZ */
    private static final char[] f473SZ = {'S', 'Z'};
    private static final char[] TDX = {'T', 'D', 'X'};
    private static final char[] WFPV = {'W', 'F', 'P', 'V'};

    private abstract class CologneBuffer {
        protected final char[] data;
        protected int length = 0;

        /* access modifiers changed from: protected */
        public abstract char[] copyData(int i, int i2);

        public CologneBuffer(char[] cArr) {
            this.data = cArr;
            this.length = cArr.length;
        }

        public CologneBuffer(int i) {
            this.data = new char[i];
            this.length = 0;
        }

        public int length() {
            return this.length;
        }

        public String toString() {
            return new String(copyData(0, this.length));
        }
    }

    private class CologneInputBuffer extends CologneBuffer {
        public CologneInputBuffer(char[] cArr) {
            super(cArr);
        }

        public void addLeft(char c) {
            this.length++;
            this.data[getNextPos()] = c;
        }

        /* access modifiers changed from: protected */
        public char[] copyData(int i, int i2) {
            char[] cArr = new char[i2];
            System.arraycopy(this.data, (this.data.length - this.length) + i, cArr, 0, i2);
            return cArr;
        }

        public char getNextChar() {
            return this.data[getNextPos()];
        }

        /* access modifiers changed from: protected */
        public int getNextPos() {
            return this.data.length - this.length;
        }

        public char removeNext() {
            char nextChar = getNextChar();
            this.length--;
            return nextChar;
        }
    }

    private class CologneOutputBuffer extends CologneBuffer {
        public CologneOutputBuffer(int i) {
            super(i);
        }

        public void addRight(char c) {
            this.data[this.length] = c;
            this.length++;
        }

        /* access modifiers changed from: protected */
        public char[] copyData(int i, int i2) {
            char[] cArr = new char[i2];
            System.arraycopy(this.data, i, cArr, 0, i2);
            return cArr;
        }
    }

    private static boolean arrayContains(char[] cArr, char c) {
        for (char c2 : cArr) {
            if (c2 == c) {
                return true;
            }
        }
        return false;
    }

    public String colognePhonetic(String str) {
        char c;
        if (str == null) {
            return null;
        }
        String preprocess = preprocess(str);
        CologneOutputBuffer cologneOutputBuffer = new CologneOutputBuffer(preprocess.length() * 2);
        CologneInputBuffer cologneInputBuffer = new CologneInputBuffer(preprocess.toCharArray());
        int length = cologneInputBuffer.length();
        char c2 = '/';
        char c3 = '-';
        while (length > 0) {
            char removeNext = cologneInputBuffer.removeNext();
            int length2 = cologneInputBuffer.length();
            char nextChar = length2 > 0 ? cologneInputBuffer.getNextChar() : '-';
            if (arrayContains(AEIJOUY, removeNext)) {
                c = '0';
            } else if (removeNext == 'H' || removeNext < 'A' || removeNext > 'Z') {
                if (c2 == '/') {
                    length = length2;
                } else {
                    c = '-';
                }
            } else if (removeNext == 'B' || (removeNext == 'P' && nextChar != 'H')) {
                c = '1';
            } else if ((removeNext == 'D' || removeNext == 'T') && !arrayContains(SCZ, nextChar)) {
                c = '2';
            } else if (arrayContains(WFPV, removeNext)) {
                c = '3';
            } else if (arrayContains(GKQ, removeNext)) {
                c = '4';
            } else if (removeNext != 'X' || arrayContains(CKQ, c3)) {
                c = (removeNext == 'S' || removeNext == 'Z') ? '8' : removeNext == 'C' ? c2 == '/' ? arrayContains(AHKLOQRUX, nextChar) ? '4' : '8' : (arrayContains(f473SZ, c3) || !arrayContains(AHOUKQX, nextChar)) ? '8' : '4' : arrayContains(TDX, removeNext) ? '8' : removeNext == 'R' ? '7' : removeNext == 'L' ? '5' : (removeNext == 'M' || removeNext == 'N') ? '6' : removeNext;
            } else {
                cologneInputBuffer.addLeft('S');
                length2++;
                c = '4';
            }
            if (c != '-' && ((c2 != c && (c != '0' || c2 == '/')) || c < '0' || c > '8')) {
                cologneOutputBuffer.addRight(c);
            }
            c2 = c;
            c3 = removeNext;
            length = length2;
        }
        return cologneOutputBuffer.toString();
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return encode((String) obj);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("This method's parameter was expected to be of the type ");
        sb.append(String.class.getName());
        sb.append(". But actually it was of the type ");
        sb.append(obj.getClass().getName());
        sb.append(".");
        throw new EncoderException(sb.toString());
    }

    public String encode(String str) {
        return colognePhonetic(str);
    }

    public boolean isEncodeEqual(String str, String str2) {
        return colognePhonetic(str).equals(colognePhonetic(str2));
    }

    private String preprocess(String str) {
        char[] charArray = str.toUpperCase(Locale.GERMAN).toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] > 'Z') {
                char[][] cArr = PREPROCESS_MAP;
                int length = cArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    char[] cArr2 = cArr[i2];
                    if (charArray[i] == cArr2[0]) {
                        charArray[i] = cArr2[1];
                        break;
                    }
                    i2++;
                }
            }
        }
        return new String(charArray);
    }
}
