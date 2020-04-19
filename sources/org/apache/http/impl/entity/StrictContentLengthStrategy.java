package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class StrictContentLengthStrategy implements ContentLengthStrategy {
    public static final StrictContentLengthStrategy INSTANCE = new StrictContentLengthStrategy();
    private final int implicitLen;

    public StrictContentLengthStrategy(int i) {
        this.implicitLen = i;
    }

    public StrictContentLengthStrategy() {
        this(-1);
    }

    public long determineLength(HttpMessage httpMessage) throws HttpException {
        Args.notNull(httpMessage, "HTTP message");
        Header firstHeader = httpMessage.getFirstHeader("Transfer-Encoding");
        if (firstHeader != null) {
            String value = firstHeader.getValue();
            if (HTTP.CHUNK_CODING.equalsIgnoreCase(value)) {
                if (!httpMessage.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                    return -2;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Chunked transfer encoding not allowed for ");
                sb.append(httpMessage.getProtocolVersion());
                throw new ProtocolException(sb.toString());
            } else if (HTTP.IDENTITY_CODING.equalsIgnoreCase(value)) {
                return -1;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unsupported transfer encoding: ");
                sb2.append(value);
                throw new ProtocolException(sb2.toString());
            }
        } else {
            Header firstHeader2 = httpMessage.getFirstHeader("Content-Length");
            if (firstHeader2 == null) {
                return (long) this.implicitLen;
            }
            String value2 = firstHeader2.getValue();
            try {
                long parseLong = Long.parseLong(value2);
                if (parseLong >= 0) {
                    return parseLong;
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Negative content length: ");
                sb3.append(value2);
                throw new ProtocolException(sb3.toString());
            } catch (NumberFormatException unused) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Invalid content length: ");
                sb4.append(value2);
                throw new ProtocolException(sb4.toString());
            }
        }
    }
}
