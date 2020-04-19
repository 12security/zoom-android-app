package com.onedrive.sdk.serializer;

import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.onedrive.sdk.logger.ILogger;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Calendar;

final class GsonFactory {
    private GsonFactory() {
    }

    public static Gson getGsonInstance(final ILogger iLogger) {
        C18081 r0 = new JsonSerializer<Calendar>() {
            @Nullable
            public JsonElement serialize(Calendar calendar, Type type, JsonSerializationContext jsonSerializationContext) {
                if (calendar == null) {
                    return null;
                }
                try {
                    return new JsonPrimitive(CalendarSerializer.serialize(calendar));
                } catch (Exception e) {
                    ILogger iLogger = iLogger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Parsing issue on ");
                    sb.append(calendar);
                    iLogger.logError(sb.toString(), e);
                    return null;
                }
            }
        };
        return new GsonBuilder().registerTypeAdapter(Calendar.class, r0).registerTypeAdapter(Calendar.class, new JsonDeserializer<Calendar>() {
            @Nullable
            public Calendar deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                if (jsonElement == null) {
                    return null;
                }
                try {
                    return CalendarSerializer.deserialize(jsonElement.getAsString());
                } catch (ParseException e) {
                    ILogger iLogger = iLogger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Parsing issue on ");
                    sb.append(jsonElement.getAsString());
                    iLogger.logError(sb.toString(), e);
                    return null;
                }
            }
        }).create();
    }
}
