package com.google.api.client.util;

public final class Joiner {
    private final com.google.common.base.Joiner wrapped;

    /* renamed from: on */
    public static Joiner m70on(char c) {
        return new Joiner(com.google.common.base.Joiner.m81on(c));
    }

    private Joiner(com.google.common.base.Joiner joiner) {
        this.wrapped = joiner;
    }

    public final String join(Iterable<?> iterable) {
        return this.wrapped.join(iterable);
    }
}
