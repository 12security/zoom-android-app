package com.dropbox.core.util;

public abstract class Maybe<T> {
    private static final Maybe<Object> Nothing = new Nothing();

    private static final class Just<T> extends Maybe<T> {
        private final T value;

        public boolean isJust() {
            return true;
        }

        public boolean isNothing() {
            return false;
        }

        private Just(T t) {
            super();
            this.value = t;
        }

        public T getJust() {
            return this.value;
        }

        public T get(T t) {
            return this.value;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Just(");
            sb.append(this.value);
            sb.append(")");
            return sb.toString();
        }

        public int hashCode() {
            return LangUtil.nullableHashCode(this.value) + 1;
        }

        public boolean equals(Maybe<T> maybe) {
            if (maybe instanceof Just) {
                return LangUtil.nullableEquals(this.value, ((Just) maybe).value);
            } else if (maybe instanceof Nothing) {
                return false;
            } else {
                throw LangUtil.badType(maybe);
            }
        }
    }

    private static final class Nothing<T> extends Maybe<T> {
        public boolean equals(Maybe<T> maybe) {
            return maybe == this;
        }

        public T get(T t) {
            return t;
        }

        public int hashCode() {
            return 0;
        }

        public boolean isJust() {
            return false;
        }

        public boolean isNothing() {
            return true;
        }

        public String toString() {
            return "Nothing";
        }

        private Nothing() {
            super();
        }

        public T getJust() {
            throw new IllegalStateException("can't call getJust() on a Nothing");
        }
    }

    public abstract boolean equals(Maybe<T> maybe);

    public abstract T get(T t);

    public abstract T getJust();

    public abstract int hashCode();

    public abstract boolean isJust();

    public abstract boolean isNothing();

    public abstract String toString();

    private Maybe() {
    }

    public static <T> Maybe<T> Just(T t) {
        return new Just(t);
    }

    public static <T> Maybe<T> Nothing() {
        return Nothing;
    }
}
