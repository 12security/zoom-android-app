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
import java.util.Iterator;
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.GetStorageReport */
public class GetStorageReport extends BaseDfbReport {
    protected final List<List<StorageBucket>> memberStorageMap;
    protected final List<Long> sharedFolders;
    protected final List<Long> sharedUsage;
    protected final List<Long> totalUsage;
    protected final List<Long> unsharedUsage;

    /* renamed from: com.dropbox.core.v2.team.GetStorageReport$Serializer */
    static class Serializer extends StructSerializer<GetStorageReport> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetStorageReport getStorageReport, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Param.START_DATE);
            StoneSerializers.string().serialize(getStorageReport.startDate, jsonGenerator);
            jsonGenerator.writeFieldName("total_usage");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getStorageReport.totalUsage, jsonGenerator);
            jsonGenerator.writeFieldName("shared_usage");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getStorageReport.sharedUsage, jsonGenerator);
            jsonGenerator.writeFieldName("unshared_usage");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getStorageReport.unsharedUsage, jsonGenerator);
            jsonGenerator.writeFieldName("shared_folders");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getStorageReport.sharedFolders, jsonGenerator);
            jsonGenerator.writeFieldName("member_storage_map");
            StoneSerializers.list(StoneSerializers.list(Serializer.INSTANCE)).serialize(getStorageReport.memberStorageMap, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetStorageReport deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                List list = null;
                List list2 = null;
                List list3 = null;
                List list4 = null;
                List list5 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Param.START_DATE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("total_usage".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("shared_usage".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("unshared_usage".equals(currentName)) {
                        list3 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("shared_folders".equals(currentName)) {
                        list4 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("member_storage_map".equals(currentName)) {
                        list5 = (List) StoneSerializers.list(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"start_date\" missing.");
                } else if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"total_usage\" missing.");
                } else if (list2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_usage\" missing.");
                } else if (list3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"unshared_usage\" missing.");
                } else if (list4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_folders\" missing.");
                } else if (list5 != null) {
                    GetStorageReport getStorageReport = new GetStorageReport(str2, list, list2, list3, list4, list5);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getStorageReport, getStorageReport.toStringMultiline());
                    return getStorageReport;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"member_storage_map\" missing.");
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

    public GetStorageReport(String str, List<Long> list, List<Long> list2, List<Long> list3, List<Long> list4, List<List<StorageBucket>> list5) {
        super(str);
        if (list != null) {
            for (Long l : list) {
                if (l == null) {
                    throw new IllegalArgumentException("An item in list 'totalUsage' is null");
                }
            }
            this.totalUsage = list;
            if (list2 != null) {
                for (Long l2 : list2) {
                    if (l2 == null) {
                        throw new IllegalArgumentException("An item in list 'sharedUsage' is null");
                    }
                }
                this.sharedUsage = list2;
                if (list3 != null) {
                    for (Long l3 : list3) {
                        if (l3 == null) {
                            throw new IllegalArgumentException("An item in list 'unsharedUsage' is null");
                        }
                    }
                    this.unsharedUsage = list3;
                    if (list4 != null) {
                        for (Long l4 : list4) {
                            if (l4 == null) {
                                throw new IllegalArgumentException("An item in list 'sharedFolders' is null");
                            }
                        }
                        this.sharedFolders = list4;
                        if (list5 != null) {
                            for (List list6 : list5) {
                                if (list6 != null) {
                                    Iterator it = list6.iterator();
                                    while (true) {
                                        if (it.hasNext()) {
                                            if (((StorageBucket) it.next()) == null) {
                                                throw new IllegalArgumentException("An item in listan item in list 'memberStorageMap' is null");
                                            }
                                        }
                                    }
                                } else {
                                    throw new IllegalArgumentException("An item in list 'memberStorageMap' is null");
                                }
                            }
                            this.memberStorageMap = list5;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'memberStorageMap' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'sharedFolders' is null");
                }
                throw new IllegalArgumentException("Required value for 'unsharedUsage' is null");
            }
            throw new IllegalArgumentException("Required value for 'sharedUsage' is null");
        }
        throw new IllegalArgumentException("Required value for 'totalUsage' is null");
    }

    public String getStartDate() {
        return this.startDate;
    }

    public List<Long> getTotalUsage() {
        return this.totalUsage;
    }

    public List<Long> getSharedUsage() {
        return this.sharedUsage;
    }

    public List<Long> getUnsharedUsage() {
        return this.unsharedUsage;
    }

    public List<Long> getSharedFolders() {
        return this.sharedFolders;
    }

    public List<List<StorageBucket>> getMemberStorageMap() {
        return this.memberStorageMap;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.totalUsage, this.sharedUsage, this.unsharedUsage, this.sharedFolders, this.memberStorageMap});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0062, code lost:
        if (r2.equals(r5) == false) goto L_0x0065;
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
            if (r2 == 0) goto L_0x0067
            com.dropbox.core.v2.team.GetStorageReport r5 = (com.dropbox.core.p005v2.team.GetStorageReport) r5
            java.lang.String r2 = r4.startDate
            java.lang.String r3 = r5.startDate
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.startDate
            java.lang.String r3 = r5.startDate
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x0028:
            java.util.List<java.lang.Long> r2 = r4.totalUsage
            java.util.List<java.lang.Long> r3 = r5.totalUsage
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x0034:
            java.util.List<java.lang.Long> r2 = r4.sharedUsage
            java.util.List<java.lang.Long> r3 = r5.sharedUsage
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x0040:
            java.util.List<java.lang.Long> r2 = r4.unsharedUsage
            java.util.List<java.lang.Long> r3 = r5.unsharedUsage
            if (r2 == r3) goto L_0x004c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x004c:
            java.util.List<java.lang.Long> r2 = r4.sharedFolders
            java.util.List<java.lang.Long> r3 = r5.sharedFolders
            if (r2 == r3) goto L_0x0058
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x0058:
            java.util.List<java.util.List<com.dropbox.core.v2.team.StorageBucket>> r2 = r4.memberStorageMap
            java.util.List<java.util.List<com.dropbox.core.v2.team.StorageBucket>> r5 = r5.memberStorageMap
            if (r2 == r5) goto L_0x0066
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0065
            goto L_0x0066
        L_0x0065:
            r0 = 0
        L_0x0066:
            return r0
        L_0x0067:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GetStorageReport.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
