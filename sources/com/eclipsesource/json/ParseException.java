package com.eclipsesource.json;

public class ParseException extends RuntimeException {
    private final int column;
    private final int line;
    private final int offset;

    ParseException(String str, int i, int i2, int i3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" at ");
        sb.append(i2);
        sb.append(":");
        sb.append(i3);
        super(sb.toString());
        this.offset = i;
        this.line = i2;
        this.column = i3;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }
}
