package com.onedrive.sdk.serializer;

public interface ISerializer {
    <T> T deserializeObject(String str, Class<T> cls);

    <T> String serializeObject(T t);
}
