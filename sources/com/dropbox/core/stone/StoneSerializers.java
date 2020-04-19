package com.dropbox.core.stone;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StoneSerializers {

    private static final class BooleanSerializer extends StoneSerializer<Boolean> {
        public static final BooleanSerializer INSTANCE = new BooleanSerializer();

        private BooleanSerializer() {
        }

        public void serialize(Boolean bool, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeBoolean(bool.booleanValue());
        }

        public Boolean deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            Boolean valueOf = Boolean.valueOf(jsonParser.getBooleanValue());
            jsonParser.nextToken();
            return valueOf;
        }
    }

    private static final class ByteArraySerializer extends StoneSerializer<byte[]> {
        public static final ByteArraySerializer INSTANCE = new ByteArraySerializer();

        private ByteArraySerializer() {
        }

        public void serialize(byte[] bArr, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeBinary(bArr);
        }

        public byte[] deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            byte[] binaryValue = jsonParser.getBinaryValue();
            jsonParser.nextToken();
            return binaryValue;
        }
    }

    private static final class DateSerializer extends StoneSerializer<Date> {
        public static final DateSerializer INSTANCE = new DateSerializer();

        private DateSerializer() {
        }

        public void serialize(Date date, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeString(Util.formatTimestamp(date));
        }

        public Date deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String stringValue = getStringValue(jsonParser);
            jsonParser.nextToken();
            try {
                return Util.parseTimestamp(stringValue);
            } catch (ParseException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Malformed timestamp: '");
                sb.append(stringValue);
                sb.append("'");
                throw new JsonParseException(jsonParser, sb.toString(), (Throwable) e);
            }
        }
    }

    private static final class DoubleSerializer extends StoneSerializer<Double> {
        public static final DoubleSerializer INSTANCE = new DoubleSerializer();

        private DoubleSerializer() {
        }

        public void serialize(Double d, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(d.doubleValue());
        }

        public Double deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            Double valueOf = Double.valueOf(jsonParser.getDoubleValue());
            jsonParser.nextToken();
            return valueOf;
        }
    }

    private static final class FloatSerializer extends StoneSerializer<Float> {
        public static final FloatSerializer INSTANCE = new FloatSerializer();

        private FloatSerializer() {
        }

        public void serialize(Float f, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(f.floatValue());
        }

        public Float deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            Float valueOf = Float.valueOf(jsonParser.getFloatValue());
            jsonParser.nextToken();
            return valueOf;
        }
    }

    private static final class IntSerializer extends StoneSerializer<Integer> {
        public static final IntSerializer INSTANCE = new IntSerializer();

        private IntSerializer() {
        }

        public void serialize(Integer num, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(num.intValue());
        }

        public Integer deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            Integer valueOf = Integer.valueOf(jsonParser.getIntValue());
            jsonParser.nextToken();
            return valueOf;
        }
    }

    private static final class ListSerializer<T> extends StoneSerializer<List<T>> {
        private final StoneSerializer<T> underlying;

        public ListSerializer(StoneSerializer<T> stoneSerializer) {
            this.underlying = stoneSerializer;
        }

        public void serialize(List<T> list, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeStartArray(list.size());
            for (T serialize : list) {
                this.underlying.serialize(serialize, jsonGenerator);
            }
            jsonGenerator.writeEndArray();
        }

        public List<T> deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            expectStartArray(jsonParser);
            ArrayList arrayList = new ArrayList();
            while (jsonParser.getCurrentToken() != JsonToken.END_ARRAY) {
                arrayList.add(this.underlying.deserialize(jsonParser));
            }
            expectEndArray(jsonParser);
            return arrayList;
        }
    }

    private static final class LongSerializer extends StoneSerializer<Long> {
        public static final LongSerializer INSTANCE = new LongSerializer();

        private LongSerializer() {
        }

        public void serialize(Long l, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(l.longValue());
        }

        public Long deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            Long valueOf = Long.valueOf(jsonParser.getLongValue());
            jsonParser.nextToken();
            return valueOf;
        }
    }

    private static final class MapSerializer<T> extends StoneSerializer<Map<String, T>> {
        private final StoneSerializer<T> underlying;

        public MapSerializer(StoneSerializer<T> stoneSerializer) {
            this.underlying = stoneSerializer;
        }

        public void serialize(Map<String, T> map, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeString(map.toString());
        }

        public Map<String, T> deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            HashMap hashMap = new HashMap();
            expectStartObject(jsonParser);
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                hashMap.put(currentName, this.underlying.deserialize(jsonParser));
            }
            expectEndObject(jsonParser);
            return hashMap;
        }
    }

    private static final class NullableSerializer<T> extends StoneSerializer<T> {
        private final StoneSerializer<T> underlying;

        public NullableSerializer(StoneSerializer<T> stoneSerializer) {
            this.underlying = stoneSerializer;
        }

        public void serialize(T t, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (t == null) {
                jsonGenerator.writeNull();
            } else {
                this.underlying.serialize(t, jsonGenerator);
            }
        }

        public T deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return this.underlying.deserialize(jsonParser);
            }
            jsonParser.nextToken();
            return null;
        }
    }

    private static final class NullableStructSerializer<T> extends StructSerializer<T> {
        private final StructSerializer<T> underlying;

        public NullableStructSerializer(StructSerializer<T> structSerializer) {
            this.underlying = structSerializer;
        }

        public void serialize(T t, JsonGenerator jsonGenerator) throws IOException {
            if (t == null) {
                jsonGenerator.writeNull();
            } else {
                this.underlying.serialize(t, jsonGenerator);
            }
        }

        public void serialize(T t, JsonGenerator jsonGenerator, boolean z) throws IOException {
            if (t == null) {
                jsonGenerator.writeNull();
            } else {
                this.underlying.serialize(t, jsonGenerator, z);
            }
        }

        public T deserialize(JsonParser jsonParser) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return this.underlying.deserialize(jsonParser);
            }
            jsonParser.nextToken();
            return null;
        }

        public T deserialize(JsonParser jsonParser, boolean z) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return this.underlying.deserialize(jsonParser, z);
            }
            jsonParser.nextToken();
            return null;
        }
    }

    private static final class StringSerializer extends StoneSerializer<String> {
        public static final StringSerializer INSTANCE = new StringSerializer();

        private StringSerializer() {
        }

        public void serialize(String str, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeString(str);
        }

        public String deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String stringValue = getStringValue(jsonParser);
            jsonParser.nextToken();
            return stringValue;
        }
    }

    private static final class VoidSerializer extends StoneSerializer<Void> {
        public static final VoidSerializer INSTANCE = new VoidSerializer();

        private VoidSerializer() {
        }

        public void serialize(Void voidR, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            jsonGenerator.writeNull();
        }

        public Void deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            skipValue(jsonParser);
            return null;
        }
    }

    public static StoneSerializer<Long> uInt64() {
        return LongSerializer.INSTANCE;
    }

    public static StoneSerializer<Long> int64() {
        return LongSerializer.INSTANCE;
    }

    public static StoneSerializer<Long> uInt32() {
        return LongSerializer.INSTANCE;
    }

    public static StoneSerializer<Integer> int32() {
        return IntSerializer.INSTANCE;
    }

    public static StoneSerializer<Double> float64() {
        return DoubleSerializer.INSTANCE;
    }

    public static StoneSerializer<Float> float32() {
        return FloatSerializer.INSTANCE;
    }

    public static StoneSerializer<Boolean> boolean_() {
        return BooleanSerializer.INSTANCE;
    }

    public static StoneSerializer<byte[]> bytes() {
        return ByteArraySerializer.INSTANCE;
    }

    public static StoneSerializer<String> string() {
        return StringSerializer.INSTANCE;
    }

    public static StoneSerializer<Date> timestamp() {
        return DateSerializer.INSTANCE;
    }

    public static StoneSerializer<Void> void_() {
        return VoidSerializer.INSTANCE;
    }

    public static <T> StoneSerializer<T> nullable(StoneSerializer<T> stoneSerializer) {
        return new NullableSerializer(stoneSerializer);
    }

    public static <T> StructSerializer<T> nullableStruct(StructSerializer<T> structSerializer) {
        return new NullableStructSerializer(structSerializer);
    }

    public static <T> StoneSerializer<List<T>> list(StoneSerializer<T> stoneSerializer) {
        return new ListSerializer(stoneSerializer);
    }

    public static <T> StoneSerializer<Map<String, T>> map(StoneSerializer<T> stoneSerializer) {
        return new MapSerializer(stoneSerializer);
    }
}
