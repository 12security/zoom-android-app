package com.zipow.videobox.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ZMBundleTypeAdapterFactory implements TypeAdapterFactory {

    /* renamed from: com.zipow.videobox.util.ZMBundleTypeAdapterFactory$2 */
    static /* synthetic */ class C34092 {
        static final /* synthetic */ int[] $SwitchMap$com$google$gson$stream$JsonToken = new int[JsonToken.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|(3:15|16|18)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                com.google.gson.stream.JsonToken[] r0 = com.google.gson.stream.JsonToken.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$google$gson$stream$JsonToken = r0
                int[] r0 = $SwitchMap$com$google$gson$stream$JsonToken     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.NULL     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$google$gson$stream$JsonToken     // Catch:{ NoSuchFieldError -> 0x001f }
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.BEGIN_OBJECT     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$google$gson$stream$JsonToken     // Catch:{ NoSuchFieldError -> 0x002a }
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.NAME     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$com$google$gson$stream$JsonToken     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.END_OBJECT     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$com$google$gson$stream$JsonToken     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.BEGIN_ARRAY     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r0 = $SwitchMap$com$google$gson$stream$JsonToken     // Catch:{ NoSuchFieldError -> 0x004b }
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.BOOLEAN     // Catch:{ NoSuchFieldError -> 0x004b }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r0 = $SwitchMap$com$google$gson$stream$JsonToken     // Catch:{ NoSuchFieldError -> 0x0056 }
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.NUMBER     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                int[] r0 = $SwitchMap$com$google$gson$stream$JsonToken     // Catch:{ NoSuchFieldError -> 0x0062 }
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.STRING     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ZMBundleTypeAdapterFactory.C34092.<clinit>():void");
        }
    }

    @Nullable
    public <T> TypeAdapter<T> create(@NonNull final Gson gson, @NonNull TypeToken<T> typeToken) {
        if (!Bundle.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }
        return new TypeAdapter<Bundle>() {
            public void write(@NonNull JsonWriter jsonWriter, @Nullable Bundle bundle) throws IOException {
                if (bundle == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.beginObject();
                for (String str : bundle.keySet()) {
                    jsonWriter.name(str);
                    Object obj = bundle.get(str);
                    if (obj == null) {
                        jsonWriter.nullValue();
                    } else {
                        gson.toJson(obj, (Type) obj.getClass(), jsonWriter);
                    }
                }
                jsonWriter.endObject();
            }

            @Nullable
            public Bundle read(@NonNull JsonReader jsonReader) throws IOException {
                switch (C34092.$SwitchMap$com$google$gson$stream$JsonToken[jsonReader.peek().ordinal()]) {
                    case 1:
                        jsonReader.nextNull();
                        return null;
                    case 2:
                        return toBundle(readObject(jsonReader));
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("expecting object: ");
                        sb.append(jsonReader.getPath());
                        throw new IOException(sb.toString());
                }
            }

            @NonNull
            private Bundle toBundle(List<Pair<String, Object>> list) throws IOException {
                Bundle bundle = new Bundle();
                for (Pair pair : list) {
                    String str = (String) pair.first;
                    Object obj = pair.second;
                    if (obj instanceof String) {
                        bundle.putString(str, (String) obj);
                    } else if (obj instanceof Integer) {
                        bundle.putInt(str, ((Integer) obj).intValue());
                    } else if (obj instanceof Long) {
                        bundle.putLong(str, ((Long) obj).longValue());
                    } else if (obj instanceof Double) {
                        bundle.putDouble(str, ((Double) obj).doubleValue());
                    } else if (obj instanceof Parcelable) {
                        bundle.putParcelable(str, (Parcelable) obj);
                    } else if (obj instanceof List) {
                        bundle.putParcelable(str, toBundle((List) obj));
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unparcelable key, value: ");
                        sb.append(str);
                        sb.append(", ");
                        sb.append(obj);
                        throw new IOException(sb.toString());
                    }
                }
                return bundle;
            }

            @NonNull
            private List<Pair<String, Object>> readObject(JsonReader jsonReader) throws IOException {
                ArrayList arrayList = new ArrayList();
                jsonReader.beginObject();
                while (jsonReader.peek() != JsonToken.END_OBJECT) {
                    switch (C34092.$SwitchMap$com$google$gson$stream$JsonToken[jsonReader.peek().ordinal()]) {
                        case 3:
                            arrayList.add(new Pair(jsonReader.nextName(), readValue(jsonReader)));
                            break;
                        case 4:
                            break;
                        default:
                            StringBuilder sb = new StringBuilder();
                            sb.append("expecting object: ");
                            sb.append(jsonReader.getPath());
                            throw new IOException(sb.toString());
                    }
                }
                jsonReader.endObject();
                return arrayList;
            }

            private Object readValue(JsonReader jsonReader) throws IOException {
                switch (C34092.$SwitchMap$com$google$gson$stream$JsonToken[jsonReader.peek().ordinal()]) {
                    case 1:
                        jsonReader.nextNull();
                        return null;
                    case 2:
                        return readObject(jsonReader);
                    case 5:
                        return readArray(jsonReader);
                    case 6:
                        return Boolean.valueOf(jsonReader.nextBoolean());
                    case 7:
                        return readNumber(jsonReader);
                    case 8:
                        return jsonReader.nextString();
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("expecting value: ");
                        sb.append(jsonReader.getPath());
                        throw new IOException(sb.toString());
                }
            }

            private Object readNumber(JsonReader jsonReader) throws IOException {
                double nextDouble = jsonReader.nextDouble();
                if (nextDouble - Math.ceil(nextDouble) != 0.0d) {
                    return Double.valueOf(nextDouble);
                }
                long j = (long) nextDouble;
                if (j < -2147483648L || j > 2147483647L) {
                    return Long.valueOf(j);
                }
                return Integer.valueOf((int) j);
            }

            @NonNull
            private List readArray(JsonReader jsonReader) throws IOException {
                ArrayList arrayList = new ArrayList();
                jsonReader.beginArray();
                while (jsonReader.peek() != JsonToken.END_ARRAY) {
                    arrayList.add(readValue(jsonReader));
                }
                jsonReader.endArray();
                return arrayList;
            }
        };
    }
}
