package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

@GwtIncompatible
public final class Defaults {
    private static final Map<Class<?>, Object> DEFAULTS;

    private Defaults() {
    }

    static {
        HashMap hashMap = new HashMap();
        put(hashMap, Boolean.TYPE, Boolean.valueOf(false));
        put(hashMap, Character.TYPE, Character.valueOf(0));
        put(hashMap, Byte.TYPE, Byte.valueOf(0));
        put(hashMap, Short.TYPE, Short.valueOf(0));
        put(hashMap, Integer.TYPE, Integer.valueOf(0));
        put(hashMap, Long.TYPE, Long.valueOf(0));
        put(hashMap, Float.TYPE, Float.valueOf(0.0f));
        put(hashMap, Double.TYPE, Double.valueOf(0.0d));
        DEFAULTS = Collections.unmodifiableMap(hashMap);
    }

    private static <T> void put(Map<Class<?>, Object> map, Class<T> cls, T t) {
        map.put(cls, t);
    }

    @Nullable
    public static <T> T defaultValue(Class<T> cls) {
        return DEFAULTS.get(Preconditions.checkNotNull(cls));
    }
}
