package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.AppLinkTeamDetails */
public class AppLinkTeamDetails {
    protected final AppLogInfo appInfo;

    /* renamed from: com.dropbox.core.v2.teamlog.AppLinkTeamDetails$Serializer */
    static class Serializer extends StructSerializer<AppLinkTeamDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AppLinkTeamDetails appLinkTeamDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("app_info");
            Serializer.INSTANCE.serialize(appLinkTeamDetails.appInfo, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AppLinkTeamDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AppLogInfo appLogInfo = null;
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
                    if ("app_info".equals(currentName)) {
                        appLogInfo = (AppLogInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (appLogInfo != null) {
                    AppLinkTeamDetails appLinkTeamDetails = new AppLinkTeamDetails(appLogInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(appLinkTeamDetails, appLinkTeamDetails.toStringMultiline());
                    return appLinkTeamDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"app_info\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public AppLinkTeamDetails(AppLogInfo appLogInfo) {
        if (appLogInfo != null) {
            this.appInfo = appLogInfo;
            return;
        }
        throw new IllegalArgumentException("Required value for 'appInfo' is null");
    }

    public AppLogInfo getAppInfo() {
        return this.appInfo;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.appInfo});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        AppLinkTeamDetails appLinkTeamDetails = (AppLinkTeamDetails) obj;
        AppLogInfo appLogInfo = this.appInfo;
        AppLogInfo appLogInfo2 = appLinkTeamDetails.appInfo;
        if (appLogInfo != appLogInfo2 && !appLogInfo.equals(appLogInfo2)) {
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
