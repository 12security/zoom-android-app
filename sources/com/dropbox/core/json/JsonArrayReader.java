package com.dropbox.core.json;

import com.dropbox.core.util.Collector;
import com.dropbox.core.util.Collector.ArrayListCollector;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.util.List;

public class JsonArrayReader<T, L> extends JsonReader<L> {
    public final Collector<T, ? extends L> collector;
    public final JsonReader<? extends T> elementReader;

    public JsonArrayReader(JsonReader<? extends T> jsonReader, Collector<T, ? extends L> collector2) {
        this.elementReader = jsonReader;
        this.collector = collector2;
    }

    /* renamed from: mk */
    public static <T> JsonArrayReader<T, List<T>> m17mk(JsonReader<? extends T> jsonReader) {
        return new JsonArrayReader<>(jsonReader, new ArrayListCollector());
    }

    /* renamed from: mk */
    public static <T, L> JsonArrayReader<T, L> m18mk(JsonReader<? extends T> jsonReader, Collector<T, ? extends L> collector2) {
        return new JsonArrayReader<>(jsonReader, collector2);
    }

    public L read(JsonParser jsonParser) throws JsonReadException, IOException {
        return read(this.elementReader, this.collector, jsonParser);
    }

    public static <T, L> L read(JsonReader<? extends T> jsonReader, Collector<T, ? extends L> collector2, JsonParser jsonParser) throws JsonReadException, IOException {
        expectArrayStart(jsonParser);
        int i = 0;
        while (!isArrayEnd(jsonParser)) {
            try {
                collector2.add(jsonReader.read(jsonParser));
                i++;
            } catch (JsonReadException e) {
                throw e.addArrayContext(i);
            }
        }
        jsonParser.nextToken();
        return collector2.finish();
    }
}
