package com.google.common.p007io;

import com.dropbox.core.util.StringUtil;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
/* renamed from: com.google.common.io.BaseEncoding */
public abstract class BaseEncoding {
    private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
    private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", Character.valueOf('='));
    private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", Character.valueOf('='));
    private static final BaseEncoding BASE64 = new Base64Encoding("base64()", StringUtil.Base64Digits, Character.valueOf('='));
    private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", StringUtil.UrlSafeBase64Digits, Character.valueOf('='));

    /* renamed from: com.google.common.io.BaseEncoding$Alphabet */
    private static final class Alphabet extends CharMatcher {
        final int bitsPerChar;
        final int bytesPerChunk;
        /* access modifiers changed from: private */
        public final char[] chars;
        final int charsPerChunk;
        private final byte[] decodabet;
        final int mask;
        private final String name;
        private final boolean[] validPadding;

        Alphabet(String str, char[] cArr) {
            this.name = (String) Preconditions.checkNotNull(str);
            this.chars = (char[]) Preconditions.checkNotNull(cArr);
            try {
                this.bitsPerChar = IntMath.log2(cArr.length, RoundingMode.UNNECESSARY);
                int min = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
                try {
                    this.charsPerChunk = 8 / min;
                    this.bytesPerChunk = this.bitsPerChar / min;
                    this.mask = cArr.length - 1;
                    byte[] bArr = new byte[128];
                    Arrays.fill(bArr, -1);
                    for (int i = 0; i < cArr.length; i++) {
                        char c = cArr[i];
                        Preconditions.checkArgument(CharMatcher.ascii().matches(c), "Non-ASCII character: %s", c);
                        Preconditions.checkArgument(bArr[c] == -1, "Duplicate character: %s", c);
                        bArr[c] = (byte) i;
                    }
                    this.decodabet = bArr;
                    boolean[] zArr = new boolean[this.charsPerChunk];
                    for (int i2 = 0; i2 < this.bytesPerChunk; i2++) {
                        zArr[IntMath.divide(i2 * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
                    }
                    this.validPadding = zArr;
                } catch (ArithmeticException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Illegal alphabet ");
                    sb.append(new String(cArr));
                    throw new IllegalArgumentException(sb.toString(), e);
                }
            } catch (ArithmeticException e2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Illegal alphabet length ");
                sb2.append(cArr.length);
                throw new IllegalArgumentException(sb2.toString(), e2);
            }
        }

        /* access modifiers changed from: 0000 */
        public char encode(int i) {
            return this.chars[i];
        }

        /* access modifiers changed from: 0000 */
        public boolean isValidPaddingStartPosition(int i) {
            return this.validPadding[i % this.charsPerChunk];
        }

        /* access modifiers changed from: 0000 */
        public boolean canDecode(char c) {
            return c <= 127 && this.decodabet[c] != -1;
        }

        /* access modifiers changed from: 0000 */
        public int decode(char c) throws DecodingException {
            Object obj;
            if (c <= 127) {
                byte[] bArr = this.decodabet;
                if (bArr[c] != -1) {
                    return bArr[c];
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized character: ");
            if (CharMatcher.invisible().matches(c)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("0x");
                sb2.append(Integer.toHexString(c));
                obj = sb2.toString();
            } else {
                obj = Character.valueOf(c);
            }
            sb.append(obj);
            throw new DecodingException(sb.toString());
        }

        private boolean hasLowerCase() {
            for (char isLowerCase : this.chars) {
                if (Ascii.isLowerCase(isLowerCase)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasUpperCase() {
            for (char isUpperCase : this.chars) {
                if (Ascii.isUpperCase(isUpperCase)) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public Alphabet upperCase() {
            if (!hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] cArr = new char[this.chars.length];
            int i = 0;
            while (true) {
                char[] cArr2 = this.chars;
                if (i < cArr2.length) {
                    cArr[i] = Ascii.toUpperCase(cArr2[i]);
                    i++;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.name);
                    sb.append(".upperCase()");
                    return new Alphabet(sb.toString(), cArr);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public Alphabet lowerCase() {
            if (!hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] cArr = new char[this.chars.length];
            int i = 0;
            while (true) {
                char[] cArr2 = this.chars;
                if (i < cArr2.length) {
                    cArr[i] = Ascii.toLowerCase(cArr2[i]);
                    i++;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.name);
                    sb.append(".lowerCase()");
                    return new Alphabet(sb.toString(), cArr);
                }
            }
        }

        public boolean matches(char c) {
            return CharMatcher.ascii().matches(c) && this.decodabet[c] != -1;
        }

        public String toString() {
            return this.name;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof Alphabet)) {
                return false;
            }
            return Arrays.equals(this.chars, ((Alphabet) obj).chars);
        }

        public int hashCode() {
            return Arrays.hashCode(this.chars);
        }
    }

    /* renamed from: com.google.common.io.BaseEncoding$Base16Encoding */
    static final class Base16Encoding extends StandardBaseEncoding {
        final char[] encoding;

        Base16Encoding(String str, String str2) {
            this(new Alphabet(str, str2.toCharArray()));
        }

        private Base16Encoding(Alphabet alphabet) {
            super(alphabet, null);
            this.encoding = new char[512];
            Preconditions.checkArgument(alphabet.chars.length == 16);
            for (int i = 0; i < 256; i++) {
                this.encoding[i] = alphabet.encode(i >>> 4);
                this.encoding[i | 256] = alphabet.encode(i & 15);
            }
        }

        /* access modifiers changed from: 0000 */
        public void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            for (int i3 = 0; i3 < i2; i3++) {
                byte b = bArr[i + i3] & UnsignedBytes.MAX_VALUE;
                appendable.append(this.encoding[b]);
                appendable.append(this.encoding[b | Ascii.NUL]);
            }
        }

        /* access modifiers changed from: 0000 */
        public int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException {
            Preconditions.checkNotNull(bArr);
            if (charSequence.length() % 2 != 1) {
                int i = 0;
                int i2 = 0;
                while (i < charSequence.length()) {
                    int i3 = i2 + 1;
                    bArr[i2] = (byte) ((this.alphabet.decode(charSequence.charAt(i)) << 4) | this.alphabet.decode(charSequence.charAt(i + 1)));
                    i += 2;
                    i2 = i3;
                }
                return i2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid input length ");
            sb.append(charSequence.length());
            throw new DecodingException(sb.toString());
        }

        /* access modifiers changed from: 0000 */
        public BaseEncoding newInstance(Alphabet alphabet, @Nullable Character ch) {
            return new Base16Encoding(alphabet);
        }
    }

    /* renamed from: com.google.common.io.BaseEncoding$Base64Encoding */
    static final class Base64Encoding extends StandardBaseEncoding {
        Base64Encoding(String str, String str2, @Nullable Character ch) {
            this(new Alphabet(str, str2.toCharArray()), ch);
        }

        private Base64Encoding(Alphabet alphabet, @Nullable Character ch) {
            super(alphabet, ch);
            Preconditions.checkArgument(alphabet.chars.length == 64);
        }

        /* access modifiers changed from: 0000 */
        public void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            int i3 = i + i2;
            Preconditions.checkPositionIndexes(i, i3, bArr.length);
            while (i2 >= 3) {
                int i4 = i + 1;
                int i5 = i4 + 1;
                byte b = ((bArr[i] & UnsignedBytes.MAX_VALUE) << Ascii.DLE) | ((bArr[i4] & UnsignedBytes.MAX_VALUE) << 8);
                int i6 = i5 + 1;
                byte b2 = b | (bArr[i5] & UnsignedBytes.MAX_VALUE);
                appendable.append(this.alphabet.encode(b2 >>> Ascii.DC2));
                appendable.append(this.alphabet.encode((b2 >>> Ascii.f221FF) & 63));
                appendable.append(this.alphabet.encode((b2 >>> 6) & 63));
                appendable.append(this.alphabet.encode(b2 & 63));
                i2 -= 3;
                i = i6;
            }
            if (i < i3) {
                encodeChunkTo(appendable, bArr, i, i3 - i);
            }
        }

        /* access modifiers changed from: 0000 */
        public int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException {
            Preconditions.checkNotNull(bArr);
            String trimTrailingFrom = padding().trimTrailingFrom(charSequence);
            if (this.alphabet.isValidPaddingStartPosition(trimTrailingFrom.length())) {
                int i = 0;
                int i2 = 0;
                while (i < trimTrailingFrom.length()) {
                    int i3 = i + 1;
                    int i4 = i3 + 1;
                    int decode = (this.alphabet.decode(trimTrailingFrom.charAt(i)) << 18) | (this.alphabet.decode(trimTrailingFrom.charAt(i3)) << 12);
                    int i5 = i2 + 1;
                    bArr[i2] = (byte) (decode >>> 16);
                    if (i4 < trimTrailingFrom.length()) {
                        int i6 = i4 + 1;
                        int decode2 = decode | (this.alphabet.decode(trimTrailingFrom.charAt(i4)) << 6);
                        i2 = i5 + 1;
                        bArr[i5] = (byte) ((decode2 >>> 8) & 255);
                        if (i6 < trimTrailingFrom.length()) {
                            int i7 = i6 + 1;
                            int i8 = i2 + 1;
                            bArr[i2] = (byte) ((decode2 | this.alphabet.decode(trimTrailingFrom.charAt(i6))) & 255);
                            i2 = i8;
                            i = i7;
                        } else {
                            i = i6;
                        }
                    } else {
                        i2 = i5;
                        i = i4;
                    }
                }
                return i2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid input length ");
            sb.append(trimTrailingFrom.length());
            throw new DecodingException(sb.toString());
        }

        /* access modifiers changed from: 0000 */
        public BaseEncoding newInstance(Alphabet alphabet, @Nullable Character ch) {
            return new Base64Encoding(alphabet, ch);
        }
    }

    /* renamed from: com.google.common.io.BaseEncoding$DecodingException */
    public static final class DecodingException extends IOException {
        DecodingException(String str) {
            super(str);
        }

        DecodingException(Throwable th) {
            super(th);
        }
    }

    /* renamed from: com.google.common.io.BaseEncoding$SeparatedBaseEncoding */
    static final class SeparatedBaseEncoding extends BaseEncoding {
        private final int afterEveryChars;
        private final BaseEncoding delegate;
        private final String separator;
        private final CharMatcher separatorChars;

        SeparatedBaseEncoding(BaseEncoding baseEncoding, String str, int i) {
            this.delegate = (BaseEncoding) Preconditions.checkNotNull(baseEncoding);
            this.separator = (String) Preconditions.checkNotNull(str);
            this.afterEveryChars = i;
            Preconditions.checkArgument(i > 0, "Cannot add a separator after every %s chars", i);
            this.separatorChars = CharMatcher.anyOf(str).precomputed();
        }

        /* access modifiers changed from: 0000 */
        public CharMatcher padding() {
            return this.delegate.padding();
        }

        /* access modifiers changed from: 0000 */
        public int maxEncodedSize(int i) {
            int maxEncodedSize = this.delegate.maxEncodedSize(i);
            return maxEncodedSize + (this.separator.length() * IntMath.divide(Math.max(0, maxEncodedSize - 1), this.afterEveryChars, RoundingMode.FLOOR));
        }

        @GwtIncompatible
        public OutputStream encodingStream(Writer writer) {
            return this.delegate.encodingStream(separatingWriter(writer, this.separator, this.afterEveryChars));
        }

        /* access modifiers changed from: 0000 */
        public void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            this.delegate.encodeTo(separatingAppendable(appendable, this.separator, this.afterEveryChars), bArr, i, i2);
        }

        /* access modifiers changed from: 0000 */
        public int maxDecodedSize(int i) {
            return this.delegate.maxDecodedSize(i);
        }

        public boolean canDecode(CharSequence charSequence) {
            return this.delegate.canDecode(this.separatorChars.removeFrom(charSequence));
        }

        /* access modifiers changed from: 0000 */
        public int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException {
            return this.delegate.decodeTo(bArr, this.separatorChars.removeFrom(charSequence));
        }

        @GwtIncompatible
        public InputStream decodingStream(Reader reader) {
            return this.delegate.decodingStream(ignoringReader(reader, this.separatorChars));
        }

        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding withPadChar(char c) {
            return this.delegate.withPadChar(c).withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding withSeparator(String str, int i) {
            throw new UnsupportedOperationException("Already have a separator");
        }

        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.delegate);
            sb.append(".withSeparator(\"");
            sb.append(this.separator);
            sb.append("\", ");
            sb.append(this.afterEveryChars);
            sb.append(")");
            return sb.toString();
        }
    }

    /* renamed from: com.google.common.io.BaseEncoding$StandardBaseEncoding */
    static class StandardBaseEncoding extends BaseEncoding {
        final Alphabet alphabet;
        private transient BaseEncoding lowerCase;
        @Nullable
        final Character paddingChar;
        private transient BaseEncoding upperCase;

        StandardBaseEncoding(String str, String str2, @Nullable Character ch) {
            this(new Alphabet(str, str2.toCharArray()), ch);
        }

        StandardBaseEncoding(Alphabet alphabet2, @Nullable Character ch) {
            this.alphabet = (Alphabet) Preconditions.checkNotNull(alphabet2);
            Preconditions.checkArgument(ch == null || !alphabet2.matches(ch.charValue()), "Padding character %s was already in alphabet", (Object) ch);
            this.paddingChar = ch;
        }

        /* access modifiers changed from: 0000 */
        public CharMatcher padding() {
            Character ch = this.paddingChar;
            return ch == null ? CharMatcher.none() : CharMatcher.m75is(ch.charValue());
        }

        /* access modifiers changed from: 0000 */
        public int maxEncodedSize(int i) {
            return this.alphabet.charsPerChunk * IntMath.divide(i, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }

        @GwtIncompatible
        public OutputStream encodingStream(final Writer writer) {
            Preconditions.checkNotNull(writer);
            return new OutputStream() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;

                public void write(int i) throws IOException {
                    this.bitBuffer <<= 8;
                    this.bitBuffer = (i & 255) | this.bitBuffer;
                    this.bitBufferLength += 8;
                    while (this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                        writer.write(StandardBaseEncoding.this.alphabet.encode((this.bitBuffer >> (this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar)) & StandardBaseEncoding.this.alphabet.mask));
                        this.writtenChars++;
                        this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar;
                    }
                }

                public void flush() throws IOException {
                    writer.flush();
                }

                public void close() throws IOException {
                    if (this.bitBufferLength > 0) {
                        writer.write(StandardBaseEncoding.this.alphabet.encode((this.bitBuffer << (StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength)) & StandardBaseEncoding.this.alphabet.mask));
                        this.writtenChars++;
                        if (StandardBaseEncoding.this.paddingChar != null) {
                            while (this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                                writer.write(StandardBaseEncoding.this.paddingChar.charValue());
                                this.writtenChars++;
                            }
                        }
                    }
                    writer.close();
                }
            };
        }

        /* access modifiers changed from: 0000 */
        public void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            int i3 = 0;
            while (i3 < i2) {
                encodeChunkTo(appendable, bArr, i + i3, Math.min(this.alphabet.bytesPerChunk, i2 - i3));
                i3 += this.alphabet.bytesPerChunk;
            }
        }

        /* access modifiers changed from: 0000 */
        public void encodeChunkTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            int i3 = 0;
            Preconditions.checkArgument(i2 <= this.alphabet.bytesPerChunk);
            long j = 0;
            for (int i4 = 0; i4 < i2; i4++) {
                j = (j | ((long) (bArr[i + i4] & UnsignedBytes.MAX_VALUE))) << 8;
            }
            int i5 = ((i2 + 1) * 8) - this.alphabet.bitsPerChar;
            while (i3 < i2 * 8) {
                appendable.append(this.alphabet.encode(((int) (j >>> (i5 - i3))) & this.alphabet.mask));
                i3 += this.alphabet.bitsPerChar;
            }
            if (this.paddingChar != null) {
                while (i3 < this.alphabet.bytesPerChunk * 8) {
                    appendable.append(this.paddingChar.charValue());
                    i3 += this.alphabet.bitsPerChar;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public int maxDecodedSize(int i) {
            return (int) (((((long) this.alphabet.bitsPerChar) * ((long) i)) + 7) / 8);
        }

        public boolean canDecode(CharSequence charSequence) {
            String trimTrailingFrom = padding().trimTrailingFrom(charSequence);
            if (!this.alphabet.isValidPaddingStartPosition(trimTrailingFrom.length())) {
                return false;
            }
            for (int i = 0; i < trimTrailingFrom.length(); i++) {
                if (!this.alphabet.canDecode(trimTrailingFrom.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        /* access modifiers changed from: 0000 */
        public int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException {
            Preconditions.checkNotNull(bArr);
            String trimTrailingFrom = padding().trimTrailingFrom(charSequence);
            if (this.alphabet.isValidPaddingStartPosition(trimTrailingFrom.length())) {
                int i = 0;
                int i2 = 0;
                while (i < trimTrailingFrom.length()) {
                    long j = 0;
                    int i3 = 0;
                    for (int i4 = 0; i4 < this.alphabet.charsPerChunk; i4++) {
                        j <<= this.alphabet.bitsPerChar;
                        if (i + i4 < trimTrailingFrom.length()) {
                            j |= (long) this.alphabet.decode(trimTrailingFrom.charAt(i3 + i));
                            i3++;
                        }
                    }
                    int i5 = (this.alphabet.bytesPerChunk * 8) - (i3 * this.alphabet.bitsPerChar);
                    int i6 = (this.alphabet.bytesPerChunk - 1) * 8;
                    while (i6 >= i5) {
                        int i7 = i2 + 1;
                        bArr[i2] = (byte) ((int) ((j >>> i6) & 255));
                        i6 -= 8;
                        i2 = i7;
                    }
                    i += this.alphabet.charsPerChunk;
                }
                return i2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid input length ");
            sb.append(trimTrailingFrom.length());
            throw new DecodingException(sb.toString());
        }

        @GwtIncompatible
        public InputStream decodingStream(final Reader reader) {
            Preconditions.checkNotNull(reader);
            return new InputStream() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                boolean hitPadding = false;
                final CharMatcher paddingMatcher = StandardBaseEncoding.this.padding();
                int readChars = 0;

                public int read() throws IOException {
                    while (true) {
                        int read = reader.read();
                        if (read != -1) {
                            this.readChars++;
                            char c = (char) read;
                            if (this.paddingMatcher.matches(c)) {
                                if (this.hitPadding || (this.readChars != 1 && StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
                                    this.hitPadding = true;
                                }
                            } else if (!this.hitPadding) {
                                this.bitBuffer <<= StandardBaseEncoding.this.alphabet.bitsPerChar;
                                this.bitBuffer = StandardBaseEncoding.this.alphabet.decode(c) | this.bitBuffer;
                                this.bitBufferLength += StandardBaseEncoding.this.alphabet.bitsPerChar;
                                int i = this.bitBufferLength;
                                if (i >= 8) {
                                    this.bitBufferLength = i - 8;
                                    return (this.bitBuffer >> this.bitBufferLength) & 255;
                                }
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Expected padding character but found '");
                                sb.append(c);
                                sb.append("' at index ");
                                sb.append(this.readChars);
                                throw new DecodingException(sb.toString());
                            }
                        } else if (this.hitPadding || StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
                            return -1;
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Invalid input length ");
                            sb2.append(this.readChars);
                            throw new DecodingException(sb2.toString());
                        }
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Padding cannot start at index ");
                    sb3.append(this.readChars);
                    throw new DecodingException(sb3.toString());
                }

                public void close() throws IOException {
                    reader.close();
                }
            };
        }

        public BaseEncoding omitPadding() {
            return this.paddingChar == null ? this : newInstance(this.alphabet, null);
        }

        public BaseEncoding withPadChar(char c) {
            if (8 % this.alphabet.bitsPerChar != 0) {
                Character ch = this.paddingChar;
                if (ch == null || ch.charValue() != c) {
                    return newInstance(this.alphabet, Character.valueOf(c));
                }
            }
            return this;
        }

        public BaseEncoding withSeparator(String str, int i) {
            Preconditions.checkArgument(padding().mo29307or(this.alphabet).matchesNoneOf(str), "Separator (%s) cannot contain alphabet or padding characters", (Object) str);
            return new SeparatedBaseEncoding(this, str, i);
        }

        public BaseEncoding upperCase() {
            BaseEncoding baseEncoding = this.upperCase;
            if (baseEncoding == 0) {
                Alphabet upperCase2 = this.alphabet.upperCase();
                BaseEncoding newInstance = upperCase2 == this.alphabet ? this : newInstance(upperCase2, this.paddingChar);
                this.upperCase = newInstance;
                baseEncoding = newInstance;
            }
            return baseEncoding;
        }

        public BaseEncoding lowerCase() {
            BaseEncoding baseEncoding = this.lowerCase;
            if (baseEncoding == 0) {
                Alphabet lowerCase2 = this.alphabet.lowerCase();
                BaseEncoding newInstance = lowerCase2 == this.alphabet ? this : newInstance(lowerCase2, this.paddingChar);
                this.lowerCase = newInstance;
                baseEncoding = newInstance;
            }
            return baseEncoding;
        }

        /* access modifiers changed from: 0000 */
        public BaseEncoding newInstance(Alphabet alphabet2, @Nullable Character ch) {
            return new StandardBaseEncoding(alphabet2, ch);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("BaseEncoding.");
            sb.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    sb.append(".omitPadding()");
                } else {
                    sb.append(".withPadChar('");
                    sb.append(this.paddingChar);
                    sb.append("')");
                }
            }
            return sb.toString();
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof StandardBaseEncoding)) {
                return false;
            }
            StandardBaseEncoding standardBaseEncoding = (StandardBaseEncoding) obj;
            if (this.alphabet.equals(standardBaseEncoding.alphabet) && Objects.equal(this.paddingChar, standardBaseEncoding.paddingChar)) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
        }
    }

    public abstract boolean canDecode(CharSequence charSequence);

    /* access modifiers changed from: 0000 */
    public abstract int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException;

    @GwtIncompatible
    public abstract InputStream decodingStream(Reader reader);

    /* access modifiers changed from: 0000 */
    public abstract void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException;

    @GwtIncompatible
    public abstract OutputStream encodingStream(Writer writer);

    public abstract BaseEncoding lowerCase();

    /* access modifiers changed from: 0000 */
    public abstract int maxDecodedSize(int i);

    /* access modifiers changed from: 0000 */
    public abstract int maxEncodedSize(int i);

    public abstract BaseEncoding omitPadding();

    /* access modifiers changed from: 0000 */
    public abstract CharMatcher padding();

    public abstract BaseEncoding upperCase();

    public abstract BaseEncoding withPadChar(char c);

    public abstract BaseEncoding withSeparator(String str, int i);

    BaseEncoding() {
    }

    public String encode(byte[] bArr) {
        return encode(bArr, 0, bArr.length);
    }

    public final String encode(byte[] bArr, int i, int i2) {
        Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
        StringBuilder sb = new StringBuilder(maxEncodedSize(i2));
        try {
            encodeTo(sb, bArr, i, i2);
            return sb.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @GwtIncompatible
    public final ByteSink encodingSink(final CharSink charSink) {
        Preconditions.checkNotNull(charSink);
        return new ByteSink() {
            public OutputStream openStream() throws IOException {
                return BaseEncoding.this.encodingStream(charSink.openStream());
            }
        };
    }

    private static byte[] extract(byte[] bArr, int i) {
        if (i == bArr.length) {
            return bArr;
        }
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, i);
        return bArr2;
    }

    public final byte[] decode(CharSequence charSequence) {
        try {
            return decodeChecked(charSequence);
        } catch (DecodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /* access modifiers changed from: 0000 */
    public final byte[] decodeChecked(CharSequence charSequence) throws DecodingException {
        String trimTrailingFrom = padding().trimTrailingFrom(charSequence);
        byte[] bArr = new byte[maxDecodedSize(trimTrailingFrom.length())];
        return extract(bArr, decodeTo(bArr, trimTrailingFrom));
    }

    @GwtIncompatible
    public final ByteSource decodingSource(final CharSource charSource) {
        Preconditions.checkNotNull(charSource);
        return new ByteSource() {
            public InputStream openStream() throws IOException {
                return BaseEncoding.this.decodingStream(charSource.openStream());
            }
        };
    }

    public static BaseEncoding base64() {
        return BASE64;
    }

    public static BaseEncoding base64Url() {
        return BASE64_URL;
    }

    public static BaseEncoding base32() {
        return BASE32;
    }

    public static BaseEncoding base32Hex() {
        return BASE32_HEX;
    }

    public static BaseEncoding base16() {
        return BASE16;
    }

    @GwtIncompatible
    static Reader ignoringReader(final Reader reader, final CharMatcher charMatcher) {
        Preconditions.checkNotNull(reader);
        Preconditions.checkNotNull(charMatcher);
        return new Reader() {
            public int read() throws IOException {
                int read;
                do {
                    read = reader.read();
                    if (read == -1) {
                        break;
                    }
                } while (charMatcher.matches((char) read));
                return read;
            }

            public int read(char[] cArr, int i, int i2) throws IOException {
                throw new UnsupportedOperationException();
            }

            public void close() throws IOException {
                reader.close();
            }
        };
    }

    static Appendable separatingAppendable(final Appendable appendable, final String str, final int i) {
        Preconditions.checkNotNull(appendable);
        Preconditions.checkNotNull(str);
        Preconditions.checkArgument(i > 0);
        return new Appendable() {
            int charsUntilSeparator = i;

            public Appendable append(char c) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    appendable.append(str);
                    this.charsUntilSeparator = i;
                }
                appendable.append(c);
                this.charsUntilSeparator--;
                return this;
            }

            public Appendable append(CharSequence charSequence, int i, int i2) throws IOException {
                throw new UnsupportedOperationException();
            }

            public Appendable append(CharSequence charSequence) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
    }

    @GwtIncompatible
    static Writer separatingWriter(final Writer writer, String str, int i) {
        final Appendable separatingAppendable = separatingAppendable(writer, str, i);
        return new Writer() {
            public void write(int i) throws IOException {
                separatingAppendable.append((char) i);
            }

            public void write(char[] cArr, int i, int i2) throws IOException {
                throw new UnsupportedOperationException();
            }

            public void flush() throws IOException {
                writer.flush();
            }

            public void close() throws IOException {
                writer.close();
            }
        };
    }
}
