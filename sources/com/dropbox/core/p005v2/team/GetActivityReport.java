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
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.GetActivityReport */
public class GetActivityReport extends BaseDfbReport {
    protected final List<Long> activeSharedFolders1Day;
    protected final List<Long> activeSharedFolders28Day;
    protected final List<Long> activeSharedFolders7Day;
    protected final List<Long> activeUsers1Day;
    protected final List<Long> activeUsers28Day;
    protected final List<Long> activeUsers7Day;
    protected final List<Long> adds;
    protected final List<Long> deletes;
    protected final List<Long> edits;
    protected final List<Long> sharedLinksCreated;
    protected final List<Long> sharedLinksViewedByNotLoggedIn;
    protected final List<Long> sharedLinksViewedByOutsideUser;
    protected final List<Long> sharedLinksViewedByTeam;
    protected final List<Long> sharedLinksViewedTotal;

    /* renamed from: com.dropbox.core.v2.team.GetActivityReport$Serializer */
    static class Serializer extends StructSerializer<GetActivityReport> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetActivityReport getActivityReport, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Param.START_DATE);
            StoneSerializers.string().serialize(getActivityReport.startDate, jsonGenerator);
            jsonGenerator.writeFieldName("adds");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.adds, jsonGenerator);
            jsonGenerator.writeFieldName("edits");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.edits, jsonGenerator);
            jsonGenerator.writeFieldName("deletes");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.deletes, jsonGenerator);
            jsonGenerator.writeFieldName("active_users_28_day");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.activeUsers28Day, jsonGenerator);
            jsonGenerator.writeFieldName("active_users_7_day");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.activeUsers7Day, jsonGenerator);
            jsonGenerator.writeFieldName("active_users_1_day");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.activeUsers1Day, jsonGenerator);
            jsonGenerator.writeFieldName("active_shared_folders_28_day");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.activeSharedFolders28Day, jsonGenerator);
            jsonGenerator.writeFieldName("active_shared_folders_7_day");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.activeSharedFolders7Day, jsonGenerator);
            jsonGenerator.writeFieldName("active_shared_folders_1_day");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.activeSharedFolders1Day, jsonGenerator);
            jsonGenerator.writeFieldName("shared_links_created");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.sharedLinksCreated, jsonGenerator);
            jsonGenerator.writeFieldName("shared_links_viewed_by_team");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.sharedLinksViewedByTeam, jsonGenerator);
            jsonGenerator.writeFieldName("shared_links_viewed_by_outside_user");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.sharedLinksViewedByOutsideUser, jsonGenerator);
            jsonGenerator.writeFieldName("shared_links_viewed_by_not_logged_in");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.sharedLinksViewedByNotLoggedIn, jsonGenerator);
            jsonGenerator.writeFieldName("shared_links_viewed_total");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getActivityReport.sharedLinksViewedTotal, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetActivityReport deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            JsonParser jsonParser2 = jsonParser;
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
                List list6 = null;
                List list7 = null;
                List list8 = null;
                List list9 = null;
                List list10 = null;
                List list11 = null;
                List list12 = null;
                List list13 = null;
                List list14 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Param.START_DATE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("adds".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("edits".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("deletes".equals(currentName)) {
                        list3 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("active_users_28_day".equals(currentName)) {
                        list4 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("active_users_7_day".equals(currentName)) {
                        list5 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("active_users_1_day".equals(currentName)) {
                        list6 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("active_shared_folders_28_day".equals(currentName)) {
                        list7 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("active_shared_folders_7_day".equals(currentName)) {
                        list8 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("active_shared_folders_1_day".equals(currentName)) {
                        list9 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("shared_links_created".equals(currentName)) {
                        list10 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("shared_links_viewed_by_team".equals(currentName)) {
                        list11 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("shared_links_viewed_by_outside_user".equals(currentName)) {
                        list12 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("shared_links_viewed_by_not_logged_in".equals(currentName)) {
                        list13 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else if ("shared_links_viewed_total".equals(currentName)) {
                        list14 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"start_date\" missing.");
                } else if (list == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"adds\" missing.");
                } else if (list2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"edits\" missing.");
                } else if (list3 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"deletes\" missing.");
                } else if (list4 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"active_users_28_day\" missing.");
                } else if (list5 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"active_users_7_day\" missing.");
                } else if (list6 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"active_users_1_day\" missing.");
                } else if (list7 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"active_shared_folders_28_day\" missing.");
                } else if (list8 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"active_shared_folders_7_day\" missing.");
                } else if (list9 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"active_shared_folders_1_day\" missing.");
                } else if (list10 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"shared_links_created\" missing.");
                } else if (list11 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"shared_links_viewed_by_team\" missing.");
                } else if (list12 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"shared_links_viewed_by_outside_user\" missing.");
                } else if (list13 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"shared_links_viewed_by_not_logged_in\" missing.");
                } else if (list14 != null) {
                    GetActivityReport getActivityReport = new GetActivityReport(str2, list, list2, list3, list4, list5, list6, list7, list8, list9, list10, list11, list12, list13, list14);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getActivityReport, getActivityReport.toStringMultiline());
                    return getActivityReport;
                } else {
                    throw new JsonParseException(jsonParser2, "Required field \"shared_links_viewed_total\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser2, sb.toString());
            }
        }
    }

    public GetActivityReport(String str, List<Long> list, List<Long> list2, List<Long> list3, List<Long> list4, List<Long> list5, List<Long> list6, List<Long> list7, List<Long> list8, List<Long> list9, List<Long> list10, List<Long> list11, List<Long> list12, List<Long> list13, List<Long> list14) {
        List<Long> list15 = list;
        List<Long> list16 = list2;
        List<Long> list17 = list3;
        List<Long> list18 = list4;
        List<Long> list19 = list5;
        List<Long> list20 = list6;
        List<Long> list21 = list7;
        List<Long> list22 = list8;
        List<Long> list23 = list9;
        List<Long> list24 = list10;
        List<Long> list25 = list11;
        List<Long> list26 = list12;
        List<Long> list27 = list13;
        List<Long> list28 = list14;
        super(str);
        if (list15 != null) {
            for (Long l : list) {
                if (l == null) {
                    throw new IllegalArgumentException("An item in list 'adds' is null");
                }
            }
            this.adds = list15;
            if (list16 != null) {
                for (Long l2 : list2) {
                    if (l2 == null) {
                        throw new IllegalArgumentException("An item in list 'edits' is null");
                    }
                }
                this.edits = list16;
                if (list17 != null) {
                    for (Long l3 : list3) {
                        if (l3 == null) {
                            throw new IllegalArgumentException("An item in list 'deletes' is null");
                        }
                    }
                    this.deletes = list17;
                    if (list18 != null) {
                        for (Long l4 : list4) {
                            if (l4 == null) {
                                throw new IllegalArgumentException("An item in list 'activeUsers28Day' is null");
                            }
                        }
                        this.activeUsers28Day = list18;
                        if (list19 != null) {
                            for (Long l5 : list5) {
                                if (l5 == null) {
                                    throw new IllegalArgumentException("An item in list 'activeUsers7Day' is null");
                                }
                            }
                            this.activeUsers7Day = list19;
                            if (list20 != null) {
                                for (Long l6 : list6) {
                                    if (l6 == null) {
                                        throw new IllegalArgumentException("An item in list 'activeUsers1Day' is null");
                                    }
                                }
                                this.activeUsers1Day = list20;
                                if (list21 != null) {
                                    for (Long l7 : list7) {
                                        if (l7 == null) {
                                            throw new IllegalArgumentException("An item in list 'activeSharedFolders28Day' is null");
                                        }
                                    }
                                    this.activeSharedFolders28Day = list21;
                                    if (list22 != null) {
                                        for (Long l8 : list8) {
                                            if (l8 == null) {
                                                throw new IllegalArgumentException("An item in list 'activeSharedFolders7Day' is null");
                                            }
                                        }
                                        this.activeSharedFolders7Day = list22;
                                        if (list23 != null) {
                                            for (Long l9 : list9) {
                                                if (l9 == null) {
                                                    throw new IllegalArgumentException("An item in list 'activeSharedFolders1Day' is null");
                                                }
                                            }
                                            this.activeSharedFolders1Day = list23;
                                            if (list24 != null) {
                                                for (Long l10 : list10) {
                                                    if (l10 == null) {
                                                        throw new IllegalArgumentException("An item in list 'sharedLinksCreated' is null");
                                                    }
                                                }
                                                this.sharedLinksCreated = list24;
                                                if (list25 != null) {
                                                    for (Long l11 : list11) {
                                                        if (l11 == null) {
                                                            throw new IllegalArgumentException("An item in list 'sharedLinksViewedByTeam' is null");
                                                        }
                                                    }
                                                    this.sharedLinksViewedByTeam = list25;
                                                    if (list26 != null) {
                                                        for (Long l12 : list12) {
                                                            if (l12 == null) {
                                                                throw new IllegalArgumentException("An item in list 'sharedLinksViewedByOutsideUser' is null");
                                                            }
                                                        }
                                                        this.sharedLinksViewedByOutsideUser = list26;
                                                        if (list27 != null) {
                                                            for (Long l13 : list13) {
                                                                if (l13 == null) {
                                                                    throw new IllegalArgumentException("An item in list 'sharedLinksViewedByNotLoggedIn' is null");
                                                                }
                                                            }
                                                            this.sharedLinksViewedByNotLoggedIn = list27;
                                                            if (list28 != null) {
                                                                for (Long l14 : list14) {
                                                                    if (l14 == null) {
                                                                        throw new IllegalArgumentException("An item in list 'sharedLinksViewedTotal' is null");
                                                                    }
                                                                }
                                                                this.sharedLinksViewedTotal = list28;
                                                                return;
                                                            }
                                                            throw new IllegalArgumentException("Required value for 'sharedLinksViewedTotal' is null");
                                                        }
                                                        throw new IllegalArgumentException("Required value for 'sharedLinksViewedByNotLoggedIn' is null");
                                                    }
                                                    throw new IllegalArgumentException("Required value for 'sharedLinksViewedByOutsideUser' is null");
                                                }
                                                throw new IllegalArgumentException("Required value for 'sharedLinksViewedByTeam' is null");
                                            }
                                            throw new IllegalArgumentException("Required value for 'sharedLinksCreated' is null");
                                        }
                                        throw new IllegalArgumentException("Required value for 'activeSharedFolders1Day' is null");
                                    }
                                    throw new IllegalArgumentException("Required value for 'activeSharedFolders7Day' is null");
                                }
                                throw new IllegalArgumentException("Required value for 'activeSharedFolders28Day' is null");
                            }
                            throw new IllegalArgumentException("Required value for 'activeUsers1Day' is null");
                        }
                        throw new IllegalArgumentException("Required value for 'activeUsers7Day' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'activeUsers28Day' is null");
                }
                throw new IllegalArgumentException("Required value for 'deletes' is null");
            }
            throw new IllegalArgumentException("Required value for 'edits' is null");
        }
        throw new IllegalArgumentException("Required value for 'adds' is null");
    }

    public String getStartDate() {
        return this.startDate;
    }

    public List<Long> getAdds() {
        return this.adds;
    }

    public List<Long> getEdits() {
        return this.edits;
    }

    public List<Long> getDeletes() {
        return this.deletes;
    }

    public List<Long> getActiveUsers28Day() {
        return this.activeUsers28Day;
    }

    public List<Long> getActiveUsers7Day() {
        return this.activeUsers7Day;
    }

    public List<Long> getActiveUsers1Day() {
        return this.activeUsers1Day;
    }

    public List<Long> getActiveSharedFolders28Day() {
        return this.activeSharedFolders28Day;
    }

    public List<Long> getActiveSharedFolders7Day() {
        return this.activeSharedFolders7Day;
    }

    public List<Long> getActiveSharedFolders1Day() {
        return this.activeSharedFolders1Day;
    }

    public List<Long> getSharedLinksCreated() {
        return this.sharedLinksCreated;
    }

    public List<Long> getSharedLinksViewedByTeam() {
        return this.sharedLinksViewedByTeam;
    }

    public List<Long> getSharedLinksViewedByOutsideUser() {
        return this.sharedLinksViewedByOutsideUser;
    }

    public List<Long> getSharedLinksViewedByNotLoggedIn() {
        return this.sharedLinksViewedByNotLoggedIn;
    }

    public List<Long> getSharedLinksViewedTotal() {
        return this.sharedLinksViewedTotal;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.adds, this.edits, this.deletes, this.activeUsers28Day, this.activeUsers7Day, this.activeUsers1Day, this.activeSharedFolders28Day, this.activeSharedFolders7Day, this.activeSharedFolders1Day, this.sharedLinksCreated, this.sharedLinksViewedByTeam, this.sharedLinksViewedByOutsideUser, this.sharedLinksViewedByNotLoggedIn, this.sharedLinksViewedTotal});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00ce, code lost:
        if (r2.equals(r5) == false) goto L_0x00d1;
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
            if (r2 == 0) goto L_0x00d3
            com.dropbox.core.v2.team.GetActivityReport r5 = (com.dropbox.core.p005v2.team.GetActivityReport) r5
            java.lang.String r2 = r4.startDate
            java.lang.String r3 = r5.startDate
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.startDate
            java.lang.String r3 = r5.startDate
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x0028:
            java.util.List<java.lang.Long> r2 = r4.adds
            java.util.List<java.lang.Long> r3 = r5.adds
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x0034:
            java.util.List<java.lang.Long> r2 = r4.edits
            java.util.List<java.lang.Long> r3 = r5.edits
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x0040:
            java.util.List<java.lang.Long> r2 = r4.deletes
            java.util.List<java.lang.Long> r3 = r5.deletes
            if (r2 == r3) goto L_0x004c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x004c:
            java.util.List<java.lang.Long> r2 = r4.activeUsers28Day
            java.util.List<java.lang.Long> r3 = r5.activeUsers28Day
            if (r2 == r3) goto L_0x0058
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x0058:
            java.util.List<java.lang.Long> r2 = r4.activeUsers7Day
            java.util.List<java.lang.Long> r3 = r5.activeUsers7Day
            if (r2 == r3) goto L_0x0064
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x0064:
            java.util.List<java.lang.Long> r2 = r4.activeUsers1Day
            java.util.List<java.lang.Long> r3 = r5.activeUsers1Day
            if (r2 == r3) goto L_0x0070
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x0070:
            java.util.List<java.lang.Long> r2 = r4.activeSharedFolders28Day
            java.util.List<java.lang.Long> r3 = r5.activeSharedFolders28Day
            if (r2 == r3) goto L_0x007c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x007c:
            java.util.List<java.lang.Long> r2 = r4.activeSharedFolders7Day
            java.util.List<java.lang.Long> r3 = r5.activeSharedFolders7Day
            if (r2 == r3) goto L_0x0088
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x0088:
            java.util.List<java.lang.Long> r2 = r4.activeSharedFolders1Day
            java.util.List<java.lang.Long> r3 = r5.activeSharedFolders1Day
            if (r2 == r3) goto L_0x0094
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x0094:
            java.util.List<java.lang.Long> r2 = r4.sharedLinksCreated
            java.util.List<java.lang.Long> r3 = r5.sharedLinksCreated
            if (r2 == r3) goto L_0x00a0
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x00a0:
            java.util.List<java.lang.Long> r2 = r4.sharedLinksViewedByTeam
            java.util.List<java.lang.Long> r3 = r5.sharedLinksViewedByTeam
            if (r2 == r3) goto L_0x00ac
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x00ac:
            java.util.List<java.lang.Long> r2 = r4.sharedLinksViewedByOutsideUser
            java.util.List<java.lang.Long> r3 = r5.sharedLinksViewedByOutsideUser
            if (r2 == r3) goto L_0x00b8
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x00b8:
            java.util.List<java.lang.Long> r2 = r4.sharedLinksViewedByNotLoggedIn
            java.util.List<java.lang.Long> r3 = r5.sharedLinksViewedByNotLoggedIn
            if (r2 == r3) goto L_0x00c4
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d1
        L_0x00c4:
            java.util.List<java.lang.Long> r2 = r4.sharedLinksViewedTotal
            java.util.List<java.lang.Long> r5 = r5.sharedLinksViewedTotal
            if (r2 == r5) goto L_0x00d2
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00d1
            goto L_0x00d2
        L_0x00d1:
            r0 = 0
        L_0x00d2:
            return r0
        L_0x00d3:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GetActivityReport.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
