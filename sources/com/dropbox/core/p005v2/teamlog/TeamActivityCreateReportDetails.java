package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.teamlog.TeamActivityCreateReportDetails */
public class TeamActivityCreateReportDetails {
    protected final Date endDate;
    protected final Date startDate;

    /* renamed from: com.dropbox.core.v2.teamlog.TeamActivityCreateReportDetails$Serializer */
    static class Serializer extends StructSerializer<TeamActivityCreateReportDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamActivityCreateReportDetails teamActivityCreateReportDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Param.START_DATE);
            StoneSerializers.timestamp().serialize(teamActivityCreateReportDetails.startDate, jsonGenerator);
            jsonGenerator.writeFieldName(Param.END_DATE);
            StoneSerializers.timestamp().serialize(teamActivityCreateReportDetails.endDate, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamActivityCreateReportDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Date date = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Date date2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Param.START_DATE.equals(currentName)) {
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser);
                    } else if (Param.END_DATE.equals(currentName)) {
                        date2 = (Date) StoneSerializers.timestamp().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (date == null) {
                    throw new JsonParseException(jsonParser, "Required field \"start_date\" missing.");
                } else if (date2 != null) {
                    TeamActivityCreateReportDetails teamActivityCreateReportDetails = new TeamActivityCreateReportDetails(date, date2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamActivityCreateReportDetails, teamActivityCreateReportDetails.toStringMultiline());
                    return teamActivityCreateReportDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"end_date\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public TeamActivityCreateReportDetails(Date date, Date date2) {
        if (date != null) {
            this.startDate = LangUtil.truncateMillis(date);
            if (date2 != null) {
                this.endDate = LangUtil.truncateMillis(date2);
                return;
            }
            throw new IllegalArgumentException("Required value for 'endDate' is null");
        }
        throw new IllegalArgumentException("Required value for 'startDate' is null");
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.startDate, this.endDate});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.teamlog.TeamActivityCreateReportDetails r5 = (com.dropbox.core.p005v2.teamlog.TeamActivityCreateReportDetails) r5
            java.util.Date r2 = r4.startDate
            java.util.Date r3 = r5.startDate
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.util.Date r2 = r4.endDate
            java.util.Date r5 = r5.endDate
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.TeamActivityCreateReportDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
