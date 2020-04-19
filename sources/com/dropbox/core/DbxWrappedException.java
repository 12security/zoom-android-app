package com.dropbox.core;

import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.p005v2.callbacks.DbxGlobalCallbackFactory;
import com.dropbox.core.p005v2.callbacks.DbxRouteErrorCallback;
import com.dropbox.core.stone.StoneSerializer;
import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import java.lang.reflect.Field;

public final class DbxWrappedException extends Exception {
    private static final long serialVersionUID = 0;
    private final Object errValue;
    private final String requestId;
    private final LocalizedText userMessage;

    public DbxWrappedException(Object obj, String str, LocalizedText localizedText) {
        this.errValue = obj;
        this.requestId = str;
        this.userMessage = localizedText;
    }

    public Object getErrorValue() {
        return this.errValue;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public LocalizedText getUserMessage() {
        return this.userMessage;
    }

    public static <T> DbxWrappedException fromResponse(StoneSerializer<T> stoneSerializer, Response response, String str) throws IOException, JsonParseException {
        String requestId2 = DbxRequestUtil.getRequestId(response);
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) new Serializer(stoneSerializer).deserialize(response.getBody());
        Object error = apiErrorResponse.getError();
        DbxGlobalCallbackFactory dbxGlobalCallbackFactory = DbxRequestUtil.sharedCallbackFactory;
        executeBlockForObject(dbxGlobalCallbackFactory, str, error);
        executeOtherBlocks(dbxGlobalCallbackFactory, str, error);
        return new DbxWrappedException(error, requestId2, apiErrorResponse.getUserMessage());
    }

    public static void executeOtherBlocks(DbxGlobalCallbackFactory dbxGlobalCallbackFactory, String str, Object obj) {
        Field[] declaredFields;
        try {
            Object invoke = obj.getClass().getMethod("tag", new Class[0]).invoke(obj, new Object[0]);
            StringBuilder sb = new StringBuilder();
            sb.append(invoke.toString().toLowerCase());
            sb.append("value");
            String sb2 = sb.toString();
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(sb2)) {
                    field.setAccessible(true);
                    executeBlockForObject(dbxGlobalCallbackFactory, str, field.get(obj));
                    return;
                }
            }
        } catch (Exception unused) {
        }
    }

    public static <T> void executeBlockForObject(DbxGlobalCallbackFactory dbxGlobalCallbackFactory, String str, T t) {
        if (dbxGlobalCallbackFactory != null) {
            DbxRouteErrorCallback createRouteErrorCallback = dbxGlobalCallbackFactory.createRouteErrorCallback(str, t);
            if (createRouteErrorCallback != null) {
                createRouteErrorCallback.setRouteError(t);
                createRouteErrorCallback.run();
            }
        }
    }
}
