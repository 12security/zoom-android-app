package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxList;
import com.dropbox.core.p005v2.teamcommon.TimeRange;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsArg */
class GetTeamEventsArg {
    protected final String accountId;
    protected final EventCategory category;
    protected final long limit;
    protected final TimeRange time;

    /* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsArg$Builder */
    public static class Builder {
        protected String accountId = null;
        protected EventCategory category = null;
        protected long limit = 1000;
        protected TimeRange time = null;

        protected Builder() {
        }

        public Builder withLimit(Long l) {
            if (l.longValue() < 1) {
                throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
            } else if (l.longValue() <= 1000) {
                if (l != null) {
                    this.limit = l.longValue();
                } else {
                    this.limit = 1000;
                }
                return this;
            } else {
                throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
            }
        }

        public Builder withAccountId(String str) {
            if (str != null) {
                if (str.length() < 40) {
                    throw new IllegalArgumentException("String 'accountId' is shorter than 40");
                } else if (str.length() > 40) {
                    throw new IllegalArgumentException("String 'accountId' is longer than 40");
                }
            }
            this.accountId = str;
            return this;
        }

        public Builder withTime(TimeRange timeRange) {
            this.time = timeRange;
            return this;
        }

        public Builder withCategory(EventCategory eventCategory) {
            this.category = eventCategory;
            return this;
        }

        public GetTeamEventsArg build() {
            GetTeamEventsArg getTeamEventsArg = new GetTeamEventsArg(this.limit, this.accountId, this.time, this.category);
            return getTeamEventsArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsArg$Serializer */
    static class Serializer extends StructSerializer<GetTeamEventsArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetTeamEventsArg getTeamEventsArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(getTeamEventsArg.limit), jsonGenerator);
            if (getTeamEventsArg.accountId != null) {
                jsonGenerator.writeFieldName("account_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(getTeamEventsArg.accountId, jsonGenerator);
            }
            if (getTeamEventsArg.time != null) {
                jsonGenerator.writeFieldName("time");
                StoneSerializers.nullableStruct(com.dropbox.core.p005v2.teamcommon.TimeRange.Serializer.INSTANCE).serialize(getTeamEventsArg.time, jsonGenerator);
            }
            if (getTeamEventsArg.category != null) {
                jsonGenerator.writeFieldName("category");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(getTeamEventsArg.category, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetTeamEventsArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(1000);
                String str2 = null;
                TimeRange timeRange = null;
                EventCategory eventCategory = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else if ("account_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("time".equals(currentName)) {
                        timeRange = (TimeRange) StoneSerializers.nullableStruct(com.dropbox.core.p005v2.teamcommon.TimeRange.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("category".equals(currentName)) {
                        eventCategory = (EventCategory) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                GetTeamEventsArg getTeamEventsArg = new GetTeamEventsArg(valueOf.longValue(), str2, timeRange, eventCategory);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(getTeamEventsArg, getTeamEventsArg.toStringMultiline());
                return getTeamEventsArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetTeamEventsArg(long j, String str, TimeRange timeRange, EventCategory eventCategory) {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            this.limit = j;
            if (str != null) {
                if (str.length() < 40) {
                    throw new IllegalArgumentException("String 'accountId' is shorter than 40");
                } else if (str.length() > 40) {
                    throw new IllegalArgumentException("String 'accountId' is longer than 40");
                }
            }
            this.accountId = str;
            this.time = timeRange;
            this.category = eventCategory;
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    public GetTeamEventsArg() {
        this(1000, null, null, null);
    }

    public long getLimit() {
        return this.limit;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public TimeRange getTime() {
        return this.time;
    }

    public EventCategory getCategory() {
        return this.category;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.limit), this.accountId, this.time, this.category});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0048, code lost:
        if (r2.equals(r7) == false) goto L_0x004b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004d
            com.dropbox.core.v2.teamlog.GetTeamEventsArg r7 = (com.dropbox.core.p005v2.teamlog.GetTeamEventsArg) r7
            long r2 = r6.limit
            long r4 = r7.limit
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x004b
            java.lang.String r2 = r6.accountId
            java.lang.String r3 = r7.accountId
            if (r2 == r3) goto L_0x002e
            if (r2 == 0) goto L_0x004b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x002e:
            com.dropbox.core.v2.teamcommon.TimeRange r2 = r6.time
            com.dropbox.core.v2.teamcommon.TimeRange r3 = r7.time
            if (r2 == r3) goto L_0x003c
            if (r2 == 0) goto L_0x004b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x003c:
            com.dropbox.core.v2.teamlog.EventCategory r2 = r6.category
            com.dropbox.core.v2.teamlog.EventCategory r7 = r7.category
            if (r2 == r7) goto L_0x004c
            if (r2 == 0) goto L_0x004b
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x004b
            goto L_0x004c
        L_0x004b:
            r0 = 0
        L_0x004c:
            return r0
        L_0x004d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.GetTeamEventsArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
