package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.HeaderIterator;
import org.apache.http.ParseException;
import org.apache.http.TokenIterator;
import org.apache.http.util.Args;

public class BasicTokenIterator implements TokenIterator {
    public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
    protected String currentHeader;
    protected String currentToken;
    protected final HeaderIterator headerIt;
    protected int searchPos = findNext(-1);

    /* access modifiers changed from: protected */
    public boolean isTokenSeparator(char c) {
        return c == ',';
    }

    public BasicTokenIterator(HeaderIterator headerIterator) {
        this.headerIt = (HeaderIterator) Args.notNull(headerIterator, "Header iterator");
    }

    public boolean hasNext() {
        return this.currentToken != null;
    }

    public String nextToken() throws NoSuchElementException, ParseException {
        String str = this.currentToken;
        if (str != null) {
            this.searchPos = findNext(this.searchPos);
            return str;
        }
        throw new NoSuchElementException("Iteration already finished.");
    }

    public final Object next() throws NoSuchElementException, ParseException {
        return nextToken();
    }

    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing tokens is not supported.");
    }

    /* access modifiers changed from: protected */
    public int findNext(int i) throws ParseException {
        int i2;
        if (i >= 0) {
            i2 = findTokenSeparator(i);
        } else if (!this.headerIt.hasNext()) {
            return -1;
        } else {
            this.currentHeader = this.headerIt.nextHeader().getValue();
            i2 = 0;
        }
        int findTokenStart = findTokenStart(i2);
        if (findTokenStart < 0) {
            this.currentToken = null;
            return -1;
        }
        int findTokenEnd = findTokenEnd(findTokenStart);
        this.currentToken = createToken(this.currentHeader, findTokenStart, findTokenEnd);
        return findTokenEnd;
    }

    /* access modifiers changed from: protected */
    public String createToken(String str, int i, int i2) {
        return str.substring(i, i2);
    }

    /* access modifiers changed from: protected */
    public int findTokenStart(int i) {
        int notNegative = Args.notNegative(i, "Search position");
        boolean z = false;
        while (!z) {
            String str = this.currentHeader;
            if (str == null) {
                break;
            }
            int length = str.length();
            while (!z && notNegative < length) {
                char charAt = this.currentHeader.charAt(notNegative);
                if (isTokenSeparator(charAt) || isWhitespace(charAt)) {
                    notNegative++;
                } else if (isTokenChar(this.currentHeader.charAt(notNegative))) {
                    z = true;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid character before token (pos ");
                    sb.append(notNegative);
                    sb.append("): ");
                    sb.append(this.currentHeader);
                    throw new ParseException(sb.toString());
                }
            }
            if (!z) {
                if (this.headerIt.hasNext()) {
                    this.currentHeader = this.headerIt.nextHeader().getValue();
                    notNegative = 0;
                } else {
                    this.currentHeader = null;
                }
            }
        }
        if (z) {
            return notNegative;
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public int findTokenSeparator(int i) {
        int notNegative = Args.notNegative(i, "Search position");
        int length = this.currentHeader.length();
        boolean z = false;
        while (!z && notNegative < length) {
            char charAt = this.currentHeader.charAt(notNegative);
            if (isTokenSeparator(charAt)) {
                z = true;
            } else if (isWhitespace(charAt)) {
                notNegative++;
            } else if (isTokenChar(charAt)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Tokens without separator (pos ");
                sb.append(notNegative);
                sb.append("): ");
                sb.append(this.currentHeader);
                throw new ParseException(sb.toString());
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid character after token (pos ");
                sb2.append(notNegative);
                sb2.append("): ");
                sb2.append(this.currentHeader);
                throw new ParseException(sb2.toString());
            }
        }
        return notNegative;
    }

    /* access modifiers changed from: protected */
    public int findTokenEnd(int i) {
        Args.notNegative(i, "Search position");
        int length = this.currentHeader.length();
        int i2 = i + 1;
        while (i2 < length && isTokenChar(this.currentHeader.charAt(i2))) {
            i2++;
        }
        return i2;
    }

    /* access modifiers changed from: protected */
    public boolean isWhitespace(char c) {
        return c == 9 || Character.isSpaceChar(c);
    }

    /* access modifiers changed from: protected */
    public boolean isTokenChar(char c) {
        if (Character.isLetterOrDigit(c)) {
            return true;
        }
        if (!Character.isISOControl(c) && !isHttpSeparator(c)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isHttpSeparator(char c) {
        return HTTP_SEPARATORS.indexOf(c) >= 0;
    }
}
