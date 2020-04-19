package com.dropbox.core.util;

import com.dropbox.core.json.JsonDateReader;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.http.message.TokenParser;

public abstract class DumpWriter {

    public static final class Multiline extends DumpWriter {
        private final StringBuilder buf;
        private int currentIndent;
        private final int indentAmount;

        /* renamed from: nl */
        boolean f62nl;

        public Multiline(StringBuilder sb, int i, int i2, boolean z) {
            this.f62nl = true;
            if (sb == null) {
                throw new IllegalArgumentException("'buf' must not be null");
            } else if (i < 0) {
                throw new IllegalArgumentException("'indentAmount' must be non-negative");
            } else if (i2 >= 0) {
                this.buf = sb;
                this.indentAmount = i;
                this.currentIndent = i2;
                this.f62nl = z;
            } else {
                throw new IllegalArgumentException("'currentIndent' must be non-negative");
            }
        }

        public Multiline(StringBuilder sb, int i, boolean z) {
            this(sb, i, 0, z);
        }

        private void prefix() {
            if (this.f62nl) {
                int i = this.currentIndent;
                for (int i2 = 0; i2 < i; i2++) {
                    this.buf.append(TokenParser.f498SP);
                }
            }
        }

        private void indentMore() {
            this.currentIndent += this.indentAmount;
        }

        private void indentLess() {
            int i = this.indentAmount;
            int i2 = this.currentIndent;
            if (i <= i2) {
                this.currentIndent = i2 - i;
                return;
            }
            throw new IllegalStateException("indent went negative");
        }

        public DumpWriter recordStart(String str) {
            prefix();
            if (str != null) {
                StringBuilder sb = this.buf;
                sb.append(str);
                sb.append(OAuth.SCOPE_DELIMITER);
            }
            this.buf.append("{\n");
            this.f62nl = true;
            indentMore();
            return this;
        }

        public DumpWriter recordEnd() {
            if (this.f62nl) {
                indentLess();
                prefix();
                this.buf.append("}\n");
                this.f62nl = true;
                return this;
            }
            throw new AssertionError("called recordEnd() in a bad state");
        }

        /* renamed from: f */
        public DumpWriter mo10658f(String str) {
            if (this.f62nl) {
                prefix();
                StringBuilder sb = this.buf;
                sb.append(str);
                sb.append(" = ");
                this.f62nl = false;
                return this;
            }
            throw new AssertionError("called fieldStart() in a bad state");
        }

        public DumpWriter listStart() {
            prefix();
            this.buf.append("[\n");
            this.f62nl = true;
            indentMore();
            return this;
        }

        public DumpWriter listEnd() {
            if (this.f62nl) {
                indentLess();
                prefix();
                this.buf.append("]\n");
                this.f62nl = true;
                return this;
            }
            throw new AssertionError("called listEnd() in a bad state");
        }

        public DumpWriter verbatim(String str) {
            prefix();
            this.buf.append(str);
            this.buf.append(10);
            this.f62nl = true;
            return this;
        }
    }

    public static final class Plain extends DumpWriter {
        private StringBuilder buf;
        private boolean needSep = false;

        public Plain(StringBuilder sb) {
            this.buf = sb;
        }

        private void sep() {
            if (this.needSep) {
                this.buf.append(", ");
            } else {
                this.needSep = true;
            }
        }

        public DumpWriter recordStart(String str) {
            if (str != null) {
                this.buf.append(str);
            }
            this.buf.append("(");
            this.needSep = false;
            return this;
        }

        public DumpWriter recordEnd() {
            this.buf.append(")");
            this.needSep = true;
            return this;
        }

        /* renamed from: f */
        public DumpWriter mo10658f(String str) {
            sep();
            StringBuilder sb = this.buf;
            sb.append(str);
            sb.append('=');
            this.needSep = false;
            return this;
        }

        public DumpWriter listStart() {
            sep();
            this.buf.append("[");
            this.needSep = false;
            return this;
        }

        public DumpWriter listEnd() {
            this.buf.append("]");
            this.needSep = true;
            return this;
        }

        public DumpWriter verbatim(String str) {
            sep();
            this.buf.append(str);
            return this;
        }
    }

    /* renamed from: f */
    public abstract DumpWriter mo10658f(String str);

    public abstract DumpWriter listEnd();

    public abstract DumpWriter listStart();

    public abstract DumpWriter recordEnd();

    public abstract DumpWriter recordStart(String str);

    public abstract DumpWriter verbatim(String str);

    public DumpWriter fieldVerbatim(String str, String str2) {
        return mo10658f(str).verbatim(str2);
    }

    /* renamed from: v */
    public DumpWriter mo10669v(Iterable<? extends Dumpable> iterable) {
        if (iterable == null) {
            verbatim("null");
        } else {
            listStart();
            for (Dumpable v : iterable) {
                mo10668v(v);
            }
            listEnd();
        }
        return this;
    }

    /* renamed from: v */
    public DumpWriter mo10671v(String str) {
        if (str == null) {
            verbatim("null");
        } else {
            verbatim(StringUtil.m33jq(str));
        }
        return this;
    }

    /* renamed from: v */
    public DumpWriter mo10666v(int i) {
        return verbatim(Integer.toString(i));
    }

    /* renamed from: v */
    public DumpWriter mo10667v(long j) {
        return verbatim(Long.toString(j));
    }

    /* renamed from: v */
    public DumpWriter mo10673v(boolean z) {
        return verbatim(Boolean.toString(z));
    }

    /* renamed from: v */
    public DumpWriter mo10665v(float f) {
        return verbatim(Float.toString(f));
    }

    /* renamed from: v */
    public DumpWriter mo10664v(double d) {
        return verbatim(Double.toString(d));
    }

    /* renamed from: v */
    public DumpWriter mo10672v(Date date) {
        return verbatim(toStringDate(date));
    }

    /* renamed from: v */
    public DumpWriter mo10670v(Long l) {
        return verbatim(l == null ? "null" : Long.toString(l.longValue()));
    }

    /* renamed from: v */
    public DumpWriter mo10668v(Dumpable dumpable) {
        if (dumpable == null) {
            verbatim("null");
        } else {
            recordStart(dumpable.getTypeName());
            dumpable.dumpFields(this);
            recordEnd();
        }
        return this;
    }

    public static String toStringDate(Date date) {
        if (date == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        GregorianCalendar gregorianCalendar = new GregorianCalendar(JsonDateReader.UTC);
        gregorianCalendar.setTime(date);
        String num = Integer.toString(gregorianCalendar.get(1));
        String zeroPad = zeroPad(Integer.toString(gregorianCalendar.get(2) + 1), 2);
        String zeroPad2 = zeroPad(Integer.toString(gregorianCalendar.get(5)), 2);
        String zeroPad3 = zeroPad(Integer.toString(gregorianCalendar.get(11)), 2);
        String zeroPad4 = zeroPad(Integer.toString(gregorianCalendar.get(12)), 2);
        String zeroPad5 = zeroPad(Integer.toString(gregorianCalendar.get(13)), 2);
        sb.append('\"');
        sb.append(num);
        sb.append("/");
        sb.append(zeroPad);
        sb.append("/");
        sb.append(zeroPad2);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(zeroPad3);
        sb.append(":");
        sb.append(zeroPad4);
        sb.append(":");
        sb.append(zeroPad5);
        sb.append(" UTC");
        sb.append('\"');
        return sb.toString();
    }

    private static String zeroPad(String str, int i) {
        while (str.length() < i) {
            StringBuilder sb = new StringBuilder();
            sb.append("0");
            sb.append(str);
            str = sb.toString();
        }
        return str;
    }
}
