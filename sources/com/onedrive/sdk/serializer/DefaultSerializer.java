package com.onedrive.sdk.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onedrive.sdk.logger.ILogger;

public class DefaultSerializer implements ISerializer {
    private final Gson mGson;
    private final ILogger mLogger;

    public DefaultSerializer(ILogger iLogger) {
        this.mLogger = iLogger;
        this.mGson = GsonFactory.getGsonInstance(iLogger);
    }

    public <T> T deserializeObject(String str, Class<T> cls) {
        T fromJson = this.mGson.fromJson(str, cls);
        if (fromJson instanceof IJsonBackedObject) {
            ILogger iLogger = this.mLogger;
            StringBuilder sb = new StringBuilder();
            sb.append("Deserializing type ");
            sb.append(cls.getSimpleName());
            iLogger.logDebug(sb.toString());
            ((IJsonBackedObject) fromJson).setRawObject(this, (JsonObject) this.mGson.fromJson(str, JsonObject.class));
        } else {
            ILogger iLogger2 = this.mLogger;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Deserializing a non-IJsonBackedObject type ");
            sb2.append(cls.getSimpleName());
            iLogger2.logDebug(sb2.toString());
        }
        return fromJson;
    }

    public <T> String serializeObject(T t) {
        ILogger iLogger = this.mLogger;
        StringBuilder sb = new StringBuilder();
        sb.append("Serializing type ");
        sb.append(t.getClass().getSimpleName());
        iLogger.logDebug(sb.toString());
        return this.mGson.toJson((Object) t);
    }
}
