package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@GwtIncompatible
final class JdkPattern extends CommonPattern implements Serializable {
    private static final long serialVersionUID = 0;
    private final Pattern pattern;

    private static final class JdkMatcher extends CommonMatcher {
        final Matcher matcher;

        JdkMatcher(Matcher matcher2) {
            this.matcher = (Matcher) Preconditions.checkNotNull(matcher2);
        }

        /* access modifiers changed from: 0000 */
        public boolean matches() {
            return this.matcher.matches();
        }

        /* access modifiers changed from: 0000 */
        public boolean find() {
            return this.matcher.find();
        }

        /* access modifiers changed from: 0000 */
        public boolean find(int i) {
            return this.matcher.find(i);
        }

        /* access modifiers changed from: 0000 */
        public String replaceAll(String str) {
            return this.matcher.replaceAll(str);
        }

        /* access modifiers changed from: 0000 */
        public int end() {
            return this.matcher.end();
        }

        /* access modifiers changed from: 0000 */
        public int start() {
            return this.matcher.start();
        }
    }

    JdkPattern(Pattern pattern2) {
        this.pattern = (Pattern) Preconditions.checkNotNull(pattern2);
    }

    /* access modifiers changed from: 0000 */
    public CommonMatcher matcher(CharSequence charSequence) {
        return new JdkMatcher(this.pattern.matcher(charSequence));
    }

    /* access modifiers changed from: 0000 */
    public String pattern() {
        return this.pattern.pattern();
    }

    /* access modifiers changed from: 0000 */
    public int flags() {
        return this.pattern.flags();
    }

    public String toString() {
        return this.pattern.toString();
    }

    public int hashCode() {
        return this.pattern.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JdkPattern)) {
            return false;
        }
        return this.pattern.equals(((JdkPattern) obj).pattern);
    }
}
