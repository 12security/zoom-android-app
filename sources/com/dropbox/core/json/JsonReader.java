package com.dropbox.core.json;

import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public abstract class JsonReader<T> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final JsonReader<byte[]> BinaryReader = new JsonReader<byte[]>() {
        public byte[] read(JsonParser jsonParser) throws IOException, JsonReadException {
            try {
                byte[] binaryValue = jsonParser.getBinaryValue();
                jsonParser.nextToken();
                return binaryValue;
            } catch (JsonParseException e) {
                throw JsonReadException.fromJackson(e);
            }
        }
    };
    public static final JsonReader<Boolean> BooleanReader = new JsonReader<Boolean>() {
        public Boolean read(JsonParser jsonParser) throws IOException, JsonReadException {
            return Boolean.valueOf(readBoolean(jsonParser));
        }
    };
    public static final JsonReader<Float> Float32Reader = new JsonReader<Float>() {
        public Float read(JsonParser jsonParser) throws IOException, JsonReadException {
            float floatValue = jsonParser.getFloatValue();
            jsonParser.nextToken();
            return Float.valueOf(floatValue);
        }
    };
    public static final JsonReader<Double> Float64Reader = new JsonReader<Double>() {
        public Double read(JsonParser jsonParser) throws IOException, JsonReadException {
            double doubleValue = jsonParser.getDoubleValue();
            jsonParser.nextToken();
            return Double.valueOf(doubleValue);
        }
    };
    public static final JsonReader<Integer> Int32Reader = new JsonReader<Integer>() {
        public Integer read(JsonParser jsonParser) throws IOException, JsonReadException {
            int intValue = jsonParser.getIntValue();
            jsonParser.nextToken();
            return Integer.valueOf(intValue);
        }
    };
    public static final JsonReader<Long> Int64Reader = new JsonReader<Long>() {
        public Long read(JsonParser jsonParser) throws IOException, JsonReadException {
            long longValue = jsonParser.getLongValue();
            jsonParser.nextToken();
            return Long.valueOf(longValue);
        }
    };
    public static final JsonReader<String> StringReader = new JsonReader<String>() {
        public String read(JsonParser jsonParser) throws IOException, JsonReadException {
            try {
                String text = jsonParser.getText();
                jsonParser.nextToken();
                return text;
            } catch (JsonParseException e) {
                throw JsonReadException.fromJackson(e);
            }
        }
    };
    public static final JsonReader<Long> UInt32Reader = new JsonReader<Long>() {
        public Long read(JsonParser jsonParser) throws IOException, JsonReadException {
            long readUnsignedLong = readUnsignedLong(jsonParser);
            if (readUnsignedLong < 4294967296L) {
                return Long.valueOf(readUnsignedLong);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("expecting a 32-bit unsigned integer, got: ");
            sb.append(readUnsignedLong);
            throw new JsonReadException(sb.toString(), jsonParser.getTokenLocation());
        }
    };
    public static final JsonReader<Long> UInt64Reader = new JsonReader<Long>() {
        public Long read(JsonParser jsonParser) throws IOException, JsonReadException {
            return Long.valueOf(readUnsignedLong(jsonParser));
        }
    };
    public static final JsonReader<Long> UnsignedLongReader = new JsonReader<Long>() {
        public Long read(JsonParser jsonParser) throws IOException, JsonReadException {
            return Long.valueOf(readUnsignedLong(jsonParser));
        }
    };
    public static final JsonReader<Object> VoidReader = new JsonReader<Object>() {
        public Object read(JsonParser jsonParser) throws IOException, JsonReadException {
            skipValue(jsonParser);
            return null;
        }
    };
    static final JsonFactory jsonFactory = new JsonFactory();

    public static final class FieldMapping {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public final HashMap<String, Integer> fields;

        public static final class Builder {
            private HashMap<String, Integer> fields = new HashMap<>();

            public void add(String str, int i) {
                HashMap<String, Integer> hashMap = this.fields;
                if (hashMap != null) {
                    int size = hashMap.size();
                    if (i != size) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("expectedIndex = ");
                        sb.append(i);
                        sb.append(", actual = ");
                        sb.append(size);
                        throw new IllegalStateException(sb.toString());
                    } else if (this.fields.put(str, Integer.valueOf(size)) != null) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("duplicate field name: \"");
                        sb2.append(str);
                        sb2.append("\"");
                        throw new IllegalStateException(sb2.toString());
                    }
                } else {
                    throw new IllegalStateException("already called build(); can't call add() anymore");
                }
            }

            public FieldMapping build() {
                HashMap<String, Integer> hashMap = this.fields;
                if (hashMap != null) {
                    this.fields = null;
                    return new FieldMapping(hashMap);
                }
                throw new IllegalStateException("already called build(); can't call build() again");
            }
        }

        static {
            Class<JsonReader> cls = JsonReader.class;
        }

        private FieldMapping(HashMap<String, Integer> hashMap) {
            this.fields = hashMap;
        }

        public int get(String str) {
            Integer num = (Integer) this.fields.get(str);
            if (num == null) {
                return -1;
            }
            return num.intValue();
        }
    }

    public static abstract class FileLoadException extends Exception {
        private static final long serialVersionUID = 0;

        public static final class IOError extends FileLoadException {
            private static final long serialVersionUID = 0;
            public final IOException reason;

            public IOError(File file, IOException iOException) {
                StringBuilder sb = new StringBuilder();
                sb.append("unable to read file \"");
                sb.append(file.getPath());
                sb.append("\": ");
                sb.append(iOException.getMessage());
                super(sb.toString());
                this.reason = iOException;
            }
        }

        public static final class JsonError extends FileLoadException {
            private static final long serialVersionUID = 0;
            public final JsonReadException reason;

            public JsonError(File file, JsonReadException jsonReadException) {
                StringBuilder sb = new StringBuilder();
                sb.append(file.getPath());
                sb.append(": ");
                sb.append(jsonReadException.getMessage());
                super(sb.toString());
                this.reason = jsonReadException;
            }
        }

        protected FileLoadException(String str) {
            super(str);
        }
    }

    public abstract T read(JsonParser jsonParser) throws IOException, JsonReadException;

    public T readFields(JsonParser jsonParser) throws IOException, JsonReadException {
        return null;
    }

    public T readFromTags(String[] strArr, JsonParser jsonParser) throws IOException, JsonReadException {
        return null;
    }

    public void validate(T t) {
    }

    public final T readField(JsonParser jsonParser, String str, T t) throws IOException, JsonReadException {
        if (t == null) {
            return read(jsonParser);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("duplicate field \"");
        sb.append(str);
        sb.append("\"");
        throw new JsonReadException(sb.toString(), jsonParser.getTokenLocation());
    }

    public final T readOptional(JsonParser jsonParser) throws IOException, JsonReadException {
        if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
            return read(jsonParser);
        }
        jsonParser.nextToken();
        return null;
    }

    public static String[] readTags(JsonParser jsonParser) throws IOException, JsonReadException {
        if (jsonParser.getCurrentToken() != JsonToken.FIELD_NAME || !".tag".equals(jsonParser.getCurrentName())) {
            return null;
        }
        jsonParser.nextToken();
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            String text = jsonParser.getText();
            jsonParser.nextToken();
            return text.split("\\.");
        }
        throw new JsonReadException("expected a string value for .tag field", jsonParser.getTokenLocation());
    }

    public static JsonToken nextToken(JsonParser jsonParser) throws IOException, JsonReadException {
        try {
            return jsonParser.nextToken();
        } catch (JsonParseException e) {
            throw JsonReadException.fromJackson(e);
        }
    }

    public static JsonLocation expectObjectStart(JsonParser jsonParser) throws IOException, JsonReadException {
        if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
            JsonLocation tokenLocation = jsonParser.getTokenLocation();
            nextToken(jsonParser);
            return tokenLocation;
        }
        throw new JsonReadException("expecting the start of an object (\"{\")", jsonParser.getTokenLocation());
    }

    public static void expectObjectEnd(JsonParser jsonParser) throws IOException, JsonReadException {
        if (jsonParser.getCurrentToken() == JsonToken.END_OBJECT) {
            nextToken(jsonParser);
        } else {
            throw new JsonReadException("expecting the end of an object (\"}\")", jsonParser.getTokenLocation());
        }
    }

    public static JsonLocation expectArrayStart(JsonParser jsonParser) throws IOException, JsonReadException {
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            JsonLocation tokenLocation = jsonParser.getTokenLocation();
            nextToken(jsonParser);
            return tokenLocation;
        }
        throw new JsonReadException("expecting the start of an array (\"[\")", jsonParser.getTokenLocation());
    }

    public static JsonLocation expectArrayEnd(JsonParser jsonParser) throws IOException, JsonReadException {
        if (jsonParser.getCurrentToken() == JsonToken.END_ARRAY) {
            JsonLocation tokenLocation = jsonParser.getTokenLocation();
            nextToken(jsonParser);
            return tokenLocation;
        }
        throw new JsonReadException("expecting the end of an array (\"[\")", jsonParser.getTokenLocation());
    }

    public static boolean isArrayEnd(JsonParser jsonParser) {
        return jsonParser.getCurrentToken() == JsonToken.END_ARRAY;
    }

    public static boolean isArrayStart(JsonParser jsonParser) {
        return jsonParser.getCurrentToken() == JsonToken.START_ARRAY;
    }

    public static void skipValue(JsonParser jsonParser) throws IOException, JsonReadException {
        try {
            jsonParser.skipChildren();
            jsonParser.nextToken();
        } catch (JsonParseException e) {
            throw JsonReadException.fromJackson(e);
        }
    }

    public static long readUnsignedLong(JsonParser jsonParser) throws IOException, JsonReadException {
        try {
            long longValue = jsonParser.getLongValue();
            if (longValue >= 0) {
                jsonParser.nextToken();
                return longValue;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("expecting a non-negative number, got: ");
            sb.append(longValue);
            throw new JsonReadException(sb.toString(), jsonParser.getTokenLocation());
        } catch (JsonParseException e) {
            throw JsonReadException.fromJackson(e);
        }
    }

    public static long readUnsignedLongField(JsonParser jsonParser, String str, long j) throws IOException, JsonReadException {
        if (j < 0) {
            return readUnsignedLong(jsonParser);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("duplicate field \"");
        sb.append(str);
        sb.append("\"");
        throw new JsonReadException(sb.toString(), jsonParser.getCurrentLocation());
    }

    public static boolean readBoolean(JsonParser jsonParser) throws IOException, JsonReadException {
        try {
            boolean booleanValue = jsonParser.getBooleanValue();
            jsonParser.nextToken();
            return booleanValue;
        } catch (JsonParseException e) {
            throw JsonReadException.fromJackson(e);
        }
    }

    public static double readDouble(JsonParser jsonParser) throws IOException, JsonReadException {
        try {
            double doubleValue = jsonParser.getDoubleValue();
            jsonParser.nextToken();
            return doubleValue;
        } catch (JsonParseException e) {
            throw JsonReadException.fromJackson(e);
        }
    }

    public static <T> T readEnum(JsonParser jsonParser, HashMap<String, T> hashMap, T t) throws IOException, JsonReadException {
        String str;
        if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
            jsonParser.nextToken();
            String[] readTags = readTags(jsonParser);
            if (readTags != null && jsonParser.getCurrentToken() == JsonToken.END_OBJECT) {
                str = readTags[0];
            } else if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                str = jsonParser.getText();
                jsonParser.nextToken();
                skipValue(jsonParser);
            } else {
                throw new JsonReadException("expecting a field name", jsonParser.getTokenLocation());
            }
            T t2 = hashMap.get(str);
            if (t2 != null) {
                t = t2;
            }
            if (t != null) {
                expectObjectEnd(jsonParser);
                return t;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Expected one of ");
            sb.append(hashMap);
            sb.append(", got: ");
            sb.append(str);
            throw new JsonReadException(sb.toString(), jsonParser.getTokenLocation());
        } else if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            String text = jsonParser.getText();
            T t3 = hashMap.get(text);
            if (t3 != null) {
                t = t3;
            }
            if (t != null) {
                jsonParser.nextToken();
                return t;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected one of ");
            sb2.append(hashMap);
            sb2.append(", got: ");
            sb2.append(text);
            throw new JsonReadException(sb2.toString(), jsonParser.getTokenLocation());
        } else {
            throw new JsonReadException("Expected a string value", jsonParser.getTokenLocation());
        }
    }

    public T readFully(InputStream inputStream) throws IOException, JsonReadException {
        try {
            return readFully(jsonFactory.createParser(inputStream));
        } catch (JsonParseException e) {
            throw JsonReadException.fromJackson(e);
        }
    }

    public T readFully(String str) throws JsonReadException {
        JsonParser createParser;
        try {
            createParser = jsonFactory.createParser(str);
            T readFully = readFully(createParser);
            createParser.close();
            return readFully;
        } catch (JsonParseException e) {
            throw JsonReadException.fromJackson(e);
        } catch (IOException e2) {
            throw LangUtil.mkAssert("IOException reading from String", e2);
        } catch (Throwable th) {
            createParser.close();
            throw th;
        }
    }

    public T readFully(byte[] bArr) throws JsonReadException {
        JsonParser createParser;
        try {
            createParser = jsonFactory.createParser(bArr);
            T readFully = readFully(createParser);
            createParser.close();
            return readFully;
        } catch (JsonParseException e) {
            throw JsonReadException.fromJackson(e);
        } catch (IOException e2) {
            throw LangUtil.mkAssert("IOException reading from byte[]", e2);
        } catch (Throwable th) {
            createParser.close();
            throw th;
        }
    }

    public T readFromFile(String str) throws FileLoadException {
        return readFromFile(new File(str));
    }

    public T readFromFile(File file) throws FileLoadException {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            T readFully = readFully((InputStream) fileInputStream);
            IOUtil.closeInput((InputStream) fileInputStream);
            return readFully;
        } catch (JsonReadException e) {
            throw new JsonError(file, e);
        } catch (IOException e2) {
            throw new IOError(file, e2);
        } catch (Throwable th) {
            IOUtil.closeInput((InputStream) fileInputStream);
            throw th;
        }
    }

    public T readFully(JsonParser jsonParser) throws IOException, JsonReadException {
        jsonParser.nextToken();
        T read = read(jsonParser);
        if (jsonParser.getCurrentToken() == null) {
            validate(read);
            return read;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The JSON library should ensure there's no tokens after the main value: ");
        sb.append(jsonParser.getCurrentToken());
        sb.append("@");
        sb.append(jsonParser.getCurrentLocation());
        throw new AssertionError(sb.toString());
    }
}
