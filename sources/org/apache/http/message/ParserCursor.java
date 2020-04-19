package org.apache.http.message;

public class ParserCursor {
    private final int lowerBound;
    private int pos;
    private final int upperBound;

    public ParserCursor(int i, int i2) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Lower bound cannot be negative");
        } else if (i <= i2) {
            this.lowerBound = i;
            this.upperBound = i2;
            this.pos = i;
        } else {
            throw new IndexOutOfBoundsException("Lower bound cannot be greater then upper bound");
        }
    }

    public int getLowerBound() {
        return this.lowerBound;
    }

    public int getUpperBound() {
        return this.upperBound;
    }

    public int getPos() {
        return this.pos;
    }

    public void updatePos(int i) {
        if (i < this.lowerBound) {
            StringBuilder sb = new StringBuilder();
            sb.append("pos: ");
            sb.append(i);
            sb.append(" < lowerBound: ");
            sb.append(this.lowerBound);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i <= this.upperBound) {
            this.pos = i;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("pos: ");
            sb2.append(i);
            sb2.append(" > upperBound: ");
            sb2.append(this.upperBound);
            throw new IndexOutOfBoundsException(sb2.toString());
        }
    }

    public boolean atEnd() {
        return this.pos >= this.upperBound;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(Integer.toString(this.lowerBound));
        sb.append('>');
        sb.append(Integer.toString(this.pos));
        sb.append('>');
        sb.append(Integer.toString(this.upperBound));
        sb.append(']');
        return sb.toString();
    }
}
