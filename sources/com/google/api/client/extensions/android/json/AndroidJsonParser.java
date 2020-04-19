package com.google.api.client.extensions.android.json;

import android.annotation.TargetApi;
import android.util.JsonReader;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Beta
@TargetApi(11)
class AndroidJsonParser extends JsonParser {
    private List<String> currentNameStack = new ArrayList();
    private String currentText;
    private JsonToken currentToken;
    private final AndroidJsonFactory factory;
    private final JsonReader reader;

    /* renamed from: com.google.api.client.extensions.android.json.AndroidJsonParser$1 */
    static /* synthetic */ class C10561 {
        static final /* synthetic */ int[] $SwitchMap$android$util$JsonToken = new int[android.util.JsonToken.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(23:0|(2:1|2)|3|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|23|24|(3:25|26|28)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|1|2|3|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|23|24|(3:25|26|28)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|23|24|25|26|28) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0035 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0081 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x002a */
        static {
            /*
                android.util.JsonToken[] r0 = android.util.JsonToken.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$util$JsonToken = r0
                r0 = 1
                int[] r1 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x0014 }
                android.util.JsonToken r2 = android.util.JsonToken.BEGIN_ARRAY     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x001f }
                android.util.JsonToken r3 = android.util.JsonToken.END_ARRAY     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r2 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x002a }
                android.util.JsonToken r3 = android.util.JsonToken.BEGIN_OBJECT     // Catch:{ NoSuchFieldError -> 0x002a }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r4 = 3
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r2 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x0035 }
                android.util.JsonToken r3 = android.util.JsonToken.END_OBJECT     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r4 = 4
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r2 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x0040 }
                android.util.JsonToken r3 = android.util.JsonToken.BOOLEAN     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r4 = 5
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r2 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x004b }
                android.util.JsonToken r3 = android.util.JsonToken.NULL     // Catch:{ NoSuchFieldError -> 0x004b }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r4 = 6
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r2 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x0056 }
                android.util.JsonToken r3 = android.util.JsonToken.STRING     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r4 = 7
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                int[] r2 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x0062 }
                android.util.JsonToken r3 = android.util.JsonToken.NUMBER     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r4 = 8
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                int[] r2 = $SwitchMap$android$util$JsonToken     // Catch:{ NoSuchFieldError -> 0x006e }
                android.util.JsonToken r3 = android.util.JsonToken.NAME     // Catch:{ NoSuchFieldError -> 0x006e }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x006e }
                r4 = 9
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x006e }
            L_0x006e:
                com.google.api.client.json.JsonToken[] r2 = com.google.api.client.json.JsonToken.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                $SwitchMap$com$google$api$client$json$JsonToken = r2
                int[] r2 = $SwitchMap$com$google$api$client$json$JsonToken     // Catch:{ NoSuchFieldError -> 0x0081 }
                com.google.api.client.json.JsonToken r3 = com.google.api.client.json.JsonToken.START_ARRAY     // Catch:{ NoSuchFieldError -> 0x0081 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0081 }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x0081 }
            L_0x0081:
                int[] r0 = $SwitchMap$com$google$api$client$json$JsonToken     // Catch:{ NoSuchFieldError -> 0x008b }
                com.google.api.client.json.JsonToken r2 = com.google.api.client.json.JsonToken.START_OBJECT     // Catch:{ NoSuchFieldError -> 0x008b }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x008b }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x008b }
            L_0x008b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.extensions.android.json.AndroidJsonParser.C10561.<clinit>():void");
        }
    }

    AndroidJsonParser(AndroidJsonFactory androidJsonFactory, JsonReader jsonReader) {
        this.factory = androidJsonFactory;
        this.reader = jsonReader;
        jsonReader.setLenient(true);
    }

    public void close() throws IOException {
        this.reader.close();
    }

    public String getCurrentName() {
        if (this.currentNameStack.isEmpty()) {
            return null;
        }
        List<String> list = this.currentNameStack;
        return (String) list.get(list.size() - 1);
    }

    public JsonToken getCurrentToken() {
        return this.currentToken;
    }

    public JsonFactory getFactory() {
        return this.factory;
    }

    public byte getByteValue() {
        checkNumber();
        return Byte.parseByte(this.currentText);
    }

    public short getShortValue() {
        checkNumber();
        return Short.parseShort(this.currentText);
    }

    public int getIntValue() {
        checkNumber();
        return Integer.parseInt(this.currentText);
    }

    public float getFloatValue() {
        checkNumber();
        return Float.parseFloat(this.currentText);
    }

    public BigInteger getBigIntegerValue() {
        checkNumber();
        return new BigInteger(this.currentText);
    }

    public BigDecimal getDecimalValue() {
        checkNumber();
        return new BigDecimal(this.currentText);
    }

    public double getDoubleValue() {
        checkNumber();
        return Double.parseDouble(this.currentText);
    }

    public long getLongValue() {
        checkNumber();
        return Long.parseLong(this.currentText);
    }

    private void checkNumber() {
        Preconditions.checkArgument(this.currentToken == JsonToken.VALUE_NUMBER_INT || this.currentToken == JsonToken.VALUE_NUMBER_FLOAT);
    }

    public String getText() {
        return this.currentText;
    }

    public JsonToken nextToken() throws IOException {
        android.util.JsonToken jsonToken;
        if (this.currentToken != null) {
            switch (this.currentToken) {
                case START_ARRAY:
                    this.reader.beginArray();
                    this.currentNameStack.add(null);
                    break;
                case START_OBJECT:
                    this.reader.beginObject();
                    this.currentNameStack.add(null);
                    break;
            }
        }
        try {
            jsonToken = this.reader.peek();
        } catch (EOFException unused) {
            jsonToken = android.util.JsonToken.END_DOCUMENT;
        }
        switch (C10561.$SwitchMap$android$util$JsonToken[jsonToken.ordinal()]) {
            case 1:
                this.currentText = "[";
                this.currentToken = JsonToken.START_ARRAY;
                break;
            case 2:
                this.currentText = "]";
                this.currentToken = JsonToken.END_ARRAY;
                List<String> list = this.currentNameStack;
                list.remove(list.size() - 1);
                this.reader.endArray();
                break;
            case 3:
                this.currentText = "{";
                this.currentToken = JsonToken.START_OBJECT;
                break;
            case 4:
                this.currentText = "}";
                this.currentToken = JsonToken.END_OBJECT;
                List<String> list2 = this.currentNameStack;
                list2.remove(list2.size() - 1);
                this.reader.endObject();
                break;
            case 5:
                if (!this.reader.nextBoolean()) {
                    this.currentText = "false";
                    this.currentToken = JsonToken.VALUE_FALSE;
                    break;
                } else {
                    this.currentText = "true";
                    this.currentToken = JsonToken.VALUE_TRUE;
                    break;
                }
            case 6:
                this.currentText = "null";
                this.currentToken = JsonToken.VALUE_NULL;
                this.reader.nextNull();
                break;
            case 7:
                this.currentText = this.reader.nextString();
                this.currentToken = JsonToken.VALUE_STRING;
                break;
            case 8:
                this.currentText = this.reader.nextString();
                this.currentToken = this.currentText.indexOf(46) == -1 ? JsonToken.VALUE_NUMBER_INT : JsonToken.VALUE_NUMBER_FLOAT;
                break;
            case 9:
                this.currentText = this.reader.nextName();
                this.currentToken = JsonToken.FIELD_NAME;
                List<String> list3 = this.currentNameStack;
                list3.set(list3.size() - 1, this.currentText);
                break;
            default:
                this.currentText = null;
                this.currentToken = null;
                break;
        }
        return this.currentToken;
    }

    public JsonParser skipChildren() throws IOException {
        if (this.currentToken != null) {
            switch (this.currentToken) {
                case START_ARRAY:
                    this.reader.skipValue();
                    this.currentText = "]";
                    this.currentToken = JsonToken.END_ARRAY;
                    break;
                case START_OBJECT:
                    this.reader.skipValue();
                    this.currentText = "}";
                    this.currentToken = JsonToken.END_OBJECT;
                    break;
            }
        }
        return this;
    }
}
