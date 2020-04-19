package com.microsoft.aad.adal;

import android.os.Build.VERSION;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.TimeZone;
import p021us.zoom.androidlib.util.TimeUtil;

public final class DateTimeAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private static final String TAG = "DateTimeAdapter";
    private final DateFormat mEnUs24HourFormat = buildEnUs24HourDateFormat();
    private final DateFormat mEnUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
    private final DateFormat mISO8601Format = buildIso8601Format();
    private final DateFormat mLocal24HourFormat = buildLocal24HourDateFormat();
    private final DateFormat mLocalFormat = DateFormat.getDateTimeInstance(2, 2);

    private static DateFormat buildIso8601Format() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z_WITH_QUOTE, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    private static DateFormat buildEnUs24HourDateFormat() {
        return new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.US);
    }

    private static DateFormat buildLocal24HourDateFormat() {
        Locale locale;
        if (VERSION.SDK_INT >= 24) {
            locale = Locale.getDefault(Category.DISPLAY);
        } else {
            locale = Locale.getDefault();
        }
        return new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", locale);
    }

    public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String asString;
        asString = jsonElement.getAsString();
        try {
        } catch (ParseException e) {
            Logger.m232e("DateTimeAdapter:deserialize", "Could not parse date. ", e.getMessage(), ADALError.DATE_PARSING_FAILURE, e);
            StringBuilder sb = new StringBuilder();
            sb.append("Could not parse date: ");
            sb.append(asString);
            throw new JsonParseException(sb.toString());
        } catch (ParseException unused) {
            Logger.m236v("DateTimeAdapter:deserialize", "Cannot parse with ISO8601, try again with local format.");
            try {
                return this.mLocalFormat.parse(asString);
            } catch (ParseException unused2) {
                Logger.m236v("DateTimeAdapter:deserialize", "Cannot parse with local format, try again with local 24 hour format.");
                try {
                    return this.mLocal24HourFormat.parse(asString);
                } catch (ParseException unused3) {
                    Logger.m236v("DateTimeAdapter:deserialize", "Cannot parse with local 24 hour format, try again with en us format.");
                    try {
                        return this.mEnUsFormat.parse(asString);
                    } catch (ParseException unused4) {
                        Logger.m236v("DateTimeAdapter:deserialize", "Cannot parse with en us format, try again with en us 24 hour format.");
                        return this.mEnUs24HourFormat.parse(asString);
                    }
                }
            }
        }
        return this.mISO8601Format.parse(asString);
    }

    public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(this.mISO8601Format.format(date));
    }
}
