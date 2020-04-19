package org.apache.http.client.utils;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.TokenParser;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class URLEncodedUtils {
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final BitSet PATHSAFE = new BitSet(256);
    private static final BitSet PUNCT = new BitSet(256);
    private static final char QP_SEP_A = '&';
    private static final char QP_SEP_S = ';';
    private static final int RADIX = 16;
    private static final BitSet RESERVED = new BitSet(256);
    private static final BitSet UNRESERVED = new BitSet(256);
    private static final BitSet URIC = new BitSet(256);
    private static final BitSet URLENCODER = new BitSet(256);
    private static final BitSet USERINFO = new BitSet(256);

    public static List<NameValuePair> parse(URI uri, String str) {
        return parse(uri, str != null ? Charset.forName(str) : null);
    }

    public static List<NameValuePair> parse(URI uri, Charset charset) {
        Args.notNull(uri, "URI");
        String rawQuery = uri.getRawQuery();
        if (rawQuery == null || rawQuery.isEmpty()) {
            return createEmptyList();
        }
        return parse(rawQuery, charset);
    }

    /* JADX INFO: finally extract failed */
    public static List<NameValuePair> parse(HttpEntity httpEntity) throws IOException {
        Args.notNull(httpEntity, "HTTP entity");
        ContentType contentType = ContentType.get(httpEntity);
        if (contentType == null || !contentType.getMimeType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
            return createEmptyList();
        }
        long contentLength = httpEntity.getContentLength();
        Args.check(contentLength <= 2147483647L, "HTTP entity is too large");
        Charset charset = contentType.getCharset() != null ? contentType.getCharset() : HTTP.DEF_CONTENT_CHARSET;
        InputStream content = httpEntity.getContent();
        if (content == null) {
            return createEmptyList();
        }
        try {
            CharArrayBuffer charArrayBuffer = new CharArrayBuffer(contentLength > 0 ? (int) contentLength : 1024);
            InputStreamReader inputStreamReader = new InputStreamReader(content, charset);
            char[] cArr = new char[1024];
            while (true) {
                int read = inputStreamReader.read(cArr);
                if (read == -1) {
                    break;
                }
                charArrayBuffer.append(cArr, 0, read);
            }
            content.close();
            if (charArrayBuffer.length() == 0) {
                return createEmptyList();
            }
            return parse(charArrayBuffer, charset, QP_SEP_A);
        } catch (Throwable th) {
            content.close();
            throw th;
        }
    }

    public static boolean isEncoded(HttpEntity httpEntity) {
        Args.notNull(httpEntity, "HTTP entity");
        Header contentType = httpEntity.getContentType();
        if (contentType != null) {
            HeaderElement[] elements = contentType.getElements();
            if (elements.length > 0) {
                return elements[0].getName().equalsIgnoreCase("application/x-www-form-urlencoded");
            }
        }
        return false;
    }

    @Deprecated
    public static void parse(List<NameValuePair> list, Scanner scanner, String str) {
        parse(list, scanner, "[&;]", str);
    }

    @Deprecated
    public static void parse(List<NameValuePair> list, Scanner scanner, String str, String str2) {
        String str3;
        String str4;
        scanner.useDelimiter(str);
        while (scanner.hasNext()) {
            String next = scanner.next();
            int indexOf = next.indexOf(NAME_VALUE_SEPARATOR);
            if (indexOf != -1) {
                str4 = decodeFormFields(next.substring(0, indexOf).trim(), str2);
                str3 = decodeFormFields(next.substring(indexOf + 1).trim(), str2);
            } else {
                str4 = decodeFormFields(next.trim(), str2);
                str3 = null;
            }
            list.add(new BasicNameValuePair(str4, str3));
        }
    }

    public static List<NameValuePair> parse(String str, Charset charset) {
        if (str == null) {
            return createEmptyList();
        }
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(str.length());
        charArrayBuffer.append(str);
        return parse(charArrayBuffer, charset, QP_SEP_A, QP_SEP_S);
    }

    public static List<NameValuePair> parse(String str, Charset charset, char... cArr) {
        if (str == null) {
            return createEmptyList();
        }
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(str.length());
        charArrayBuffer.append(str);
        return parse(charArrayBuffer, charset, cArr);
    }

    public static List<NameValuePair> parse(CharArrayBuffer charArrayBuffer, Charset charset, char... cArr) {
        Args.notNull(charArrayBuffer, "Char array buffer");
        TokenParser tokenParser = TokenParser.INSTANCE;
        BitSet bitSet = new BitSet();
        for (char c : cArr) {
            bitSet.set(c);
        }
        ParserCursor parserCursor = new ParserCursor(0, charArrayBuffer.length());
        ArrayList arrayList = new ArrayList();
        while (!parserCursor.atEnd()) {
            bitSet.set(61);
            String parseToken = tokenParser.parseToken(charArrayBuffer, parserCursor, bitSet);
            String str = null;
            if (!parserCursor.atEnd()) {
                char charAt = charArrayBuffer.charAt(parserCursor.getPos());
                parserCursor.updatePos(parserCursor.getPos() + 1);
                if (charAt == '=') {
                    bitSet.clear(61);
                    str = tokenParser.parseValue(charArrayBuffer, parserCursor, bitSet);
                    if (!parserCursor.atEnd()) {
                        parserCursor.updatePos(parserCursor.getPos() + 1);
                    }
                }
            }
            if (!parseToken.isEmpty()) {
                arrayList.add(new BasicNameValuePair(decodeFormFields(parseToken, charset), decodeFormFields(str, charset)));
            }
        }
        return arrayList;
    }

    public static String format(List<? extends NameValuePair> list, String str) {
        return format(list, (char) QP_SEP_A, str);
    }

    public static String format(List<? extends NameValuePair> list, char c, String str) {
        StringBuilder sb = new StringBuilder();
        for (NameValuePair nameValuePair : list) {
            String encodeFormFields = encodeFormFields(nameValuePair.getName(), str);
            String encodeFormFields2 = encodeFormFields(nameValuePair.getValue(), str);
            if (sb.length() > 0) {
                sb.append(c);
            }
            sb.append(encodeFormFields);
            if (encodeFormFields2 != null) {
                sb.append(NAME_VALUE_SEPARATOR);
                sb.append(encodeFormFields2);
            }
        }
        return sb.toString();
    }

    public static String format(Iterable<? extends NameValuePair> iterable, Charset charset) {
        return format(iterable, (char) QP_SEP_A, charset);
    }

    public static String format(Iterable<? extends NameValuePair> iterable, char c, Charset charset) {
        Args.notNull(iterable, "Parameters");
        StringBuilder sb = new StringBuilder();
        for (NameValuePair nameValuePair : iterable) {
            String encodeFormFields = encodeFormFields(nameValuePair.getName(), charset);
            String encodeFormFields2 = encodeFormFields(nameValuePair.getValue(), charset);
            if (sb.length() > 0) {
                sb.append(c);
            }
            sb.append(encodeFormFields);
            if (encodeFormFields2 != null) {
                sb.append(NAME_VALUE_SEPARATOR);
                sb.append(encodeFormFields2);
            }
        }
        return sb.toString();
    }

    static {
        for (int i = 97; i <= 122; i++) {
            UNRESERVED.set(i);
        }
        for (int i2 = 65; i2 <= 90; i2++) {
            UNRESERVED.set(i2);
        }
        for (int i3 = 48; i3 <= 57; i3++) {
            UNRESERVED.set(i3);
        }
        UNRESERVED.set(95);
        UNRESERVED.set(45);
        UNRESERVED.set(46);
        UNRESERVED.set(42);
        URLENCODER.or(UNRESERVED);
        UNRESERVED.set(33);
        UNRESERVED.set(126);
        UNRESERVED.set(39);
        UNRESERVED.set(40);
        UNRESERVED.set(41);
        PUNCT.set(44);
        PUNCT.set(59);
        PUNCT.set(58);
        PUNCT.set(36);
        PUNCT.set(38);
        PUNCT.set(43);
        PUNCT.set(61);
        USERINFO.or(UNRESERVED);
        USERINFO.or(PUNCT);
        PATHSAFE.or(UNRESERVED);
        PATHSAFE.set(47);
        PATHSAFE.set(59);
        PATHSAFE.set(58);
        PATHSAFE.set(64);
        PATHSAFE.set(38);
        PATHSAFE.set(61);
        PATHSAFE.set(43);
        PATHSAFE.set(36);
        PATHSAFE.set(44);
        RESERVED.set(59);
        RESERVED.set(47);
        RESERVED.set(63);
        RESERVED.set(58);
        RESERVED.set(64);
        RESERVED.set(38);
        RESERVED.set(61);
        RESERVED.set(43);
        RESERVED.set(36);
        RESERVED.set(44);
        RESERVED.set(91);
        RESERVED.set(93);
        URIC.or(RESERVED);
        URIC.or(UNRESERVED);
    }

    private static List<NameValuePair> createEmptyList() {
        return new ArrayList(0);
    }

    private static String urlEncode(String str, Charset charset, BitSet bitSet, boolean z) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        ByteBuffer encode = charset.encode(str);
        while (encode.hasRemaining()) {
            byte b = encode.get() & UnsignedBytes.MAX_VALUE;
            if (bitSet.get(b)) {
                sb.append((char) b);
            } else if (!z || b != 32) {
                sb.append("%");
                char upperCase = Character.toUpperCase(Character.forDigit((b >> 4) & 15, 16));
                char upperCase2 = Character.toUpperCase(Character.forDigit(b & Ascii.f228SI, 16));
                sb.append(upperCase);
                sb.append(upperCase2);
            } else {
                sb.append('+');
            }
        }
        return sb.toString();
    }

    private static String urlDecode(String str, Charset charset, boolean z) {
        if (str == null) {
            return null;
        }
        ByteBuffer allocate = ByteBuffer.allocate(str.length());
        CharBuffer wrap = CharBuffer.wrap(str);
        while (wrap.hasRemaining()) {
            char c = wrap.get();
            if (c == '%' && wrap.remaining() >= 2) {
                char c2 = wrap.get();
                char c3 = wrap.get();
                int digit = Character.digit(c2, 16);
                int digit2 = Character.digit(c3, 16);
                if (digit == -1 || digit2 == -1) {
                    allocate.put(37);
                    allocate.put((byte) c2);
                    allocate.put((byte) c3);
                } else {
                    allocate.put((byte) ((digit << 4) + digit2));
                }
            } else if (!z || c != '+') {
                allocate.put((byte) c);
            } else {
                allocate.put(32);
            }
        }
        allocate.flip();
        return charset.decode(allocate).toString();
    }

    private static String decodeFormFields(String str, String str2) {
        if (str == null) {
            return null;
        }
        return urlDecode(str, str2 != null ? Charset.forName(str2) : Consts.UTF_8, true);
    }

    private static String decodeFormFields(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        if (charset == null) {
            charset = Consts.UTF_8;
        }
        return urlDecode(str, charset, true);
    }

    private static String encodeFormFields(String str, String str2) {
        if (str == null) {
            return null;
        }
        return urlEncode(str, str2 != null ? Charset.forName(str2) : Consts.UTF_8, URLENCODER, true);
    }

    private static String encodeFormFields(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        if (charset == null) {
            charset = Consts.UTF_8;
        }
        return urlEncode(str, charset, URLENCODER, true);
    }

    static String encUserInfo(String str, Charset charset) {
        return urlEncode(str, charset, USERINFO, false);
    }

    static String encUric(String str, Charset charset) {
        return urlEncode(str, charset, URIC, false);
    }

    static String encPath(String str, Charset charset) {
        return urlEncode(str, charset, PATHSAFE, false);
    }
}
