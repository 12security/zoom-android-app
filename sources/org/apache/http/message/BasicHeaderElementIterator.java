package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class BasicHeaderElementIterator implements HeaderElementIterator {
    private CharArrayBuffer buffer;
    private HeaderElement currentElement;
    private ParserCursor cursor;
    private final HeaderIterator headerIt;
    private final HeaderValueParser parser;

    public BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser headerValueParser) {
        this.currentElement = null;
        this.buffer = null;
        this.cursor = null;
        this.headerIt = (HeaderIterator) Args.notNull(headerIterator, "Header iterator");
        this.parser = (HeaderValueParser) Args.notNull(headerValueParser, "Parser");
    }

    public BasicHeaderElementIterator(HeaderIterator headerIterator) {
        this(headerIterator, BasicHeaderValueParser.INSTANCE);
    }

    private void bufferHeaderValue() {
        this.cursor = null;
        this.buffer = null;
        while (this.headerIt.hasNext()) {
            Header nextHeader = this.headerIt.nextHeader();
            if (nextHeader instanceof FormattedHeader) {
                FormattedHeader formattedHeader = (FormattedHeader) nextHeader;
                this.buffer = formattedHeader.getBuffer();
                this.cursor = new ParserCursor(0, this.buffer.length());
                this.cursor.updatePos(formattedHeader.getValuePos());
                return;
            }
            String value = nextHeader.getValue();
            if (value != null) {
                this.buffer = new CharArrayBuffer(value.length());
                this.buffer.append(value);
                this.cursor = new ParserCursor(0, this.buffer.length());
                return;
            }
        }
    }

    private void parseNextElement() {
        HeaderElement parseHeaderElement;
        loop0:
        while (true) {
            if (this.headerIt.hasNext() || this.cursor != null) {
                ParserCursor parserCursor = this.cursor;
                if (parserCursor == null || parserCursor.atEnd()) {
                    bufferHeaderValue();
                }
                if (this.cursor != null) {
                    while (!this.cursor.atEnd()) {
                        parseHeaderElement = this.parser.parseHeaderElement(this.buffer, this.cursor);
                        if (parseHeaderElement.getName().length() == 0) {
                            if (parseHeaderElement.getValue() != null) {
                                break loop0;
                            }
                        } else {
                            break loop0;
                        }
                    }
                    if (this.cursor.atEnd()) {
                        this.cursor = null;
                        this.buffer = null;
                    }
                }
            } else {
                return;
            }
        }
        this.currentElement = parseHeaderElement;
    }

    public boolean hasNext() {
        if (this.currentElement == null) {
            parseNextElement();
        }
        return this.currentElement != null;
    }

    public HeaderElement nextElement() throws NoSuchElementException {
        if (this.currentElement == null) {
            parseNextElement();
        }
        HeaderElement headerElement = this.currentElement;
        if (headerElement != null) {
            this.currentElement = null;
            return headerElement;
        }
        throw new NoSuchElementException("No more header elements available");
    }

    public final Object next() throws NoSuchElementException {
        return nextElement();
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
