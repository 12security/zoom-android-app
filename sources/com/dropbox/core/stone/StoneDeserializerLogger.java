package com.dropbox.core.stone;

import java.util.HashMap;
import java.util.Map;

public class StoneDeserializerLogger {
    public static Map<Class<?>, LoggerCallback> LOGGER_MAP = new HashMap();

    public interface LoggerCallback {
        void invoke(Object obj, String str);
    }

    public static <T> void registerCallback(Class<T> cls, LoggerCallback loggerCallback) {
        LOGGER_MAP.put(cls, loggerCallback);
    }

    public static void log(Object obj, String str) {
        Class cls = obj.getClass();
        if (LOGGER_MAP.containsKey(cls)) {
            ((LoggerCallback) LOGGER_MAP.get(cls)).invoke(obj, str);
        }
    }
}
