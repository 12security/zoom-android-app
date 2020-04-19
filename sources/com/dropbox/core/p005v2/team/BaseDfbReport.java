package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.BaseDfbReport */
public class BaseDfbReport {
    protected final String startDate;

    /* renamed from: com.dropbox.core.v2.team.BaseDfbReport$Serializer */
    private static class Serializer extends StructSerializer<BaseDfbReport> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(BaseDfbReport baseDfbReport, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Param.START_DATE);
            StoneSerializers.string().serialize(baseDfbReport.startDate, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public BaseDfbReport deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Param.START_DATE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    BaseDfbReport baseDfbReport = new BaseDfbReport(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(baseDfbReport, baseDfbReport.toStringMultiline());
                    return baseDfbReport;
                }
                throw new JsonParseException(jsonParser, "Required field \"start_date\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public BaseDfbReport(String str) {
        if (str != null) {
            this.startDate = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'startDate' is null");
    }

    public String getStartDate() {
        return this.startDate;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.startDate});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        BaseDfbReport baseDfbReport = (BaseDfbReport) obj;
        String str = this.startDate;
        String str2 = baseDfbReport.startDate;
        if (str != str2 && !str.equals(str2)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
