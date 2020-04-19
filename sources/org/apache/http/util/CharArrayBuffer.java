package org.apache.http.util;

import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.CharBuffer;
import org.apache.http.protocol.HTTP;

public final class CharArrayBuffer implements CharSequence, Serializable {
    private static final long serialVersionUID = -6208952725094867135L;
    private char[] buffer;
    private int len;

    public CharArrayBuffer(int i) {
        Args.notNegative(i, "Buffer capacity");
        this.buffer = new char[i];
    }

    private void expand(int i) {
        char[] cArr = new char[Math.max(this.buffer.length << 1, i)];
        System.arraycopy(this.buffer, 0, cArr, 0, this.len);
        this.buffer = cArr;
    }

    public void append(char[] cArr, int i, int i2) {
        if (cArr != null) {
            if (i >= 0 && i <= cArr.length && i2 >= 0) {
                int i3 = i + i2;
                if (i3 >= 0 && i3 <= cArr.length) {
                    if (i2 != 0) {
                        int i4 = this.len + i2;
                        if (i4 > this.buffer.length) {
                            expand(i4);
                        }
                        System.arraycopy(cArr, i, this.buffer, this.len, i2);
                        this.len = i4;
                        return;
                    }
                    return;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("off: ");
            sb.append(i);
            sb.append(" len: ");
            sb.append(i2);
            sb.append(" b.length: ");
            sb.append(cArr.length);
            throw new IndexOutOfBoundsException(sb.toString());
        }
    }

    public void append(String str) {
        if (str == null) {
            str = "null";
        }
        int length = str.length();
        int i = this.len + length;
        if (i > this.buffer.length) {
            expand(i);
        }
        str.getChars(0, length, this.buffer, this.len);
        this.len = i;
    }

    public void append(CharArrayBuffer charArrayBuffer, int i, int i2) {
        if (charArrayBuffer != null) {
            append(charArrayBuffer.buffer, i, i2);
        }
    }

    public void append(CharArrayBuffer charArrayBuffer) {
        if (charArrayBuffer != null) {
            append(charArrayBuffer.buffer, 0, charArrayBuffer.len);
        }
    }

    public void append(char c) {
        int i = this.len + 1;
        if (i > this.buffer.length) {
            expand(i);
        }
        this.buffer[this.len] = c;
        this.len = i;
    }

    public void append(byte[] bArr, int i, int i2) {
        if (bArr != null) {
            if (i >= 0 && i <= bArr.length && i2 >= 0) {
                int i3 = i + i2;
                if (i3 >= 0 && i3 <= bArr.length) {
                    if (i2 != 0) {
                        int i4 = this.len;
                        int i5 = i2 + i4;
                        if (i5 > this.buffer.length) {
                            expand(i5);
                        }
                        while (i4 < i5) {
                            this.buffer[i4] = (char) (bArr[i] & UnsignedBytes.MAX_VALUE);
                            i++;
                            i4++;
                        }
                        this.len = i5;
                        return;
                    }
                    return;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("off: ");
            sb.append(i);
            sb.append(" len: ");
            sb.append(i2);
            sb.append(" b.length: ");
            sb.append(bArr.length);
            throw new IndexOutOfBoundsException(sb.toString());
        }
    }

    public void append(ByteArrayBuffer byteArrayBuffer, int i, int i2) {
        if (byteArrayBuffer != null) {
            append(byteArrayBuffer.buffer(), i, i2);
        }
    }

    public void append(Object obj) {
        append(String.valueOf(obj));
    }

    public void clear() {
        this.len = 0;
    }

    public char[] toCharArray() {
        int i = this.len;
        char[] cArr = new char[i];
        if (i > 0) {
            System.arraycopy(this.buffer, 0, cArr, 0, i);
        }
        return cArr;
    }

    public char charAt(int i) {
        return this.buffer[i];
    }

    public char[] buffer() {
        return this.buffer;
    }

    public int capacity() {
        return this.buffer.length;
    }

    public int length() {
        return this.len;
    }

    public void ensureCapacity(int i) {
        if (i > 0) {
            int length = this.buffer.length;
            int i2 = this.len;
            if (i > length - i2) {
                expand(i2 + i);
            }
        }
    }

    public void setLength(int i) {
        if (i < 0 || i > this.buffer.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("len: ");
            sb.append(i);
            sb.append(" < 0 or > buffer len: ");
            sb.append(this.buffer.length);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        this.len = i;
    }

    public boolean isEmpty() {
        return this.len == 0;
    }

    public boolean isFull() {
        return this.len == this.buffer.length;
    }

    public int indexOf(int i, int i2, int i3) {
        if (i2 < 0) {
            i2 = 0;
        }
        int i4 = this.len;
        if (i3 > i4) {
            i3 = i4;
        }
        if (i2 > i3) {
            return -1;
        }
        while (i2 < i3) {
            if (this.buffer[i2] == i) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public int indexOf(int i) {
        return indexOf(i, 0, this.len);
    }

    public String substring(int i, int i2) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Negative beginIndex: ");
            sb.append(i);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i2 > this.len) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("endIndex: ");
            sb2.append(i2);
            sb2.append(" > length: ");
            sb2.append(this.len);
            throw new IndexOutOfBoundsException(sb2.toString());
        } else if (i <= i2) {
            return new String(this.buffer, i, i2 - i);
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("beginIndex: ");
            sb3.append(i);
            sb3.append(" > endIndex: ");
            sb3.append(i2);
            throw new IndexOutOfBoundsException(sb3.toString());
        }
    }

    public String substringTrimmed(int i, int i2) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Negative beginIndex: ");
            sb.append(i);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i2 > this.len) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("endIndex: ");
            sb2.append(i2);
            sb2.append(" > length: ");
            sb2.append(this.len);
            throw new IndexOutOfBoundsException(sb2.toString());
        } else if (i <= i2) {
            while (i < i2 && HTTP.isWhitespace(this.buffer[i])) {
                i++;
            }
            while (i2 > i && HTTP.isWhitespace(this.buffer[i2 - 1])) {
                i2--;
            }
            return new String(this.buffer, i, i2 - i);
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("beginIndex: ");
            sb3.append(i);
            sb3.append(" > endIndex: ");
            sb3.append(i2);
            throw new IndexOutOfBoundsException(sb3.toString());
        }
    }

    public CharSequence subSequence(int i, int i2) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Negative beginIndex: ");
            sb.append(i);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i2 > this.len) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("endIndex: ");
            sb2.append(i2);
            sb2.append(" > length: ");
            sb2.append(this.len);
            throw new IndexOutOfBoundsException(sb2.toString());
        } else if (i <= i2) {
            return CharBuffer.wrap(this.buffer, i, i2);
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("beginIndex: ");
            sb3.append(i);
            sb3.append(" > endIndex: ");
            sb3.append(i2);
            throw new IndexOutOfBoundsException(sb3.toString());
        }
    }

    public String toString() {
        return new String(this.buffer, 0, this.len);
    }
}
