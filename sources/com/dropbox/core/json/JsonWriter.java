package com.dropbox.core.json;

import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public abstract class JsonWriter<T> {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", null};
    private static final String[] weekdays = {null, "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public abstract void write(T t, JsonGenerator jsonGenerator) throws IOException;

    public void writeFields(T t, JsonGenerator jsonGenerator) throws IOException {
    }

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(UTC);
        return simpleDateFormat.format(date);
    }

    public void write(T t, JsonGenerator jsonGenerator, int i) throws IOException {
        write(t, jsonGenerator);
    }

    public final String writeToString(T t, boolean z) {
        JsonGenerator createGenerator;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            createGenerator = JsonReader.jsonFactory.createGenerator((OutputStream) byteArrayOutputStream);
            if (z) {
                createGenerator = createGenerator.useDefaultPrettyPrinter();
            }
            write(t, createGenerator);
            createGenerator.flush();
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw LangUtil.mkAssert("Impossible", e);
        } catch (Throwable th) {
            createGenerator.flush();
            throw th;
        }
    }

    public final String writeToString(T t) {
        return writeToString(t, true);
    }

    public final void writeToStream(T t, OutputStream outputStream, boolean z) throws IOException {
        JsonGenerator createGenerator = JsonReader.jsonFactory.createGenerator(outputStream);
        if (z) {
            createGenerator = createGenerator.useDefaultPrettyPrinter();
        }
        try {
            write(t, createGenerator);
        } finally {
            createGenerator.flush();
        }
    }

    public final void writeToStream(T t, OutputStream outputStream) throws IOException {
        writeToStream(t, outputStream, true);
    }

    public final void writeToFile(T t, File file, boolean z) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            writeToStream(t, fileOutputStream, z);
        } finally {
            fileOutputStream.close();
        }
    }

    public final void writeToFile(T t, File file) throws IOException {
        writeToFile(t, file, true);
    }

    public final void writeToFile(T t, String str, boolean z) throws IOException {
        writeToFile(t, new File(str), z);
    }

    public final void writeToFile(T t, String str) throws IOException {
        writeToFile(t, str, true);
    }

    public final void writeDateIso(Date date, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeString(formatDate(date));
    }

    public final void writeDate(Date date, JsonGenerator jsonGenerator) throws IOException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(JsonDateReader.UTC);
        gregorianCalendar.setTime(date);
        String num = Integer.toString(gregorianCalendar.get(1));
        String str = months[gregorianCalendar.get(2)];
        String zeroPad = zeroPad(Integer.toString(gregorianCalendar.get(5)), 2);
        String zeroPad2 = zeroPad(Integer.toString(gregorianCalendar.get(11)), 2);
        String zeroPad3 = zeroPad(Integer.toString(gregorianCalendar.get(12)), 2);
        String zeroPad4 = zeroPad(Integer.toString(gregorianCalendar.get(13)), 2);
        String str2 = weekdays[gregorianCalendar.get(7)];
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(", ");
        sb.append(zeroPad);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(str);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(num);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(zeroPad2);
        sb.append(":");
        sb.append(zeroPad3);
        sb.append(":");
        sb.append(zeroPad4);
        sb.append(" +0000");
        jsonGenerator.writeString(sb.toString());
    }

    private static String zeroPad(String str, int i) {
        while (str.length() < i) {
            StringBuilder sb = new StringBuilder();
            sb.append("0");
            sb.append(str);
            str = sb.toString();
        }
        return str;
    }
}
