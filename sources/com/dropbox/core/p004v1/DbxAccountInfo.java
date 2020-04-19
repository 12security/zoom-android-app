package com.dropbox.core.p004v1;

import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.json.JsonReader.FieldMapping;
import com.dropbox.core.json.JsonReader.FieldMapping.Builder;
import com.dropbox.core.util.DumpWriter;
import com.dropbox.core.util.Dumpable;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v1.DbxAccountInfo */
public class DbxAccountInfo extends Dumpable {
    /* access modifiers changed from: private */

    /* renamed from: FM */
    public static final FieldMapping f63FM;
    private static final int FM_country = 2;
    private static final int FM_display_name = 1;
    private static final int FM_email = 6;
    private static final int FM_email_verified = 7;
    private static final int FM_name_details = 5;
    private static final int FM_quota_info = 4;
    private static final int FM_referral_link = 3;
    private static final int FM_uid = 0;
    public static final JsonReader<DbxAccountInfo> Reader = new JsonReader<DbxAccountInfo>() {
        public final DbxAccountInfo read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            Boolean bool = null;
            String str = null;
            String str2 = null;
            String str3 = null;
            Quota quota = null;
            String str4 = null;
            NameDetails nameDetails = null;
            long j = -1;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                try {
                    int i = DbxAccountInfo.f63FM.get(currentName);
                    switch (i) {
                        case -1:
                            JsonReader.skipValue(jsonParser);
                            break;
                        case 0:
                            j = JsonReader.readUnsignedLongField(jsonParser, currentName, j);
                            break;
                        case 1:
                            str = (String) JsonReader.StringReader.readField(jsonParser, currentName, str);
                            break;
                        case 2:
                            str2 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str2);
                            break;
                        case 3:
                            str3 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str3);
                            break;
                        case 4:
                            quota = (Quota) Quota.Reader.readField(jsonParser, currentName, quota);
                            break;
                        case 5:
                            nameDetails = (NameDetails) NameDetails.Reader.readField(jsonParser, currentName, nameDetails);
                            break;
                        case 6:
                            str4 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str4);
                            break;
                        case 7:
                            bool = (Boolean) JsonReader.BooleanReader.readField(jsonParser, currentName, bool);
                            break;
                        default:
                            StringBuilder sb = new StringBuilder();
                            sb.append("bad index: ");
                            sb.append(i);
                            sb.append(", field = \"");
                            sb.append(currentName);
                            sb.append("\"");
                            throw new AssertionError(sb.toString());
                    }
                } catch (JsonReadException e) {
                    throw e.addFieldContext(currentName);
                }
            }
            JsonReader.expectObjectEnd(jsonParser);
            if (j < 0) {
                throw new JsonReadException("missing field \"uid\"", expectObjectStart);
            } else if (str == null) {
                throw new JsonReadException("missing field \"display_name\"", expectObjectStart);
            } else if (str2 == null) {
                throw new JsonReadException("missing field \"country\"", expectObjectStart);
            } else if (str3 == null) {
                throw new JsonReadException("missing field \"referral_link\"", expectObjectStart);
            } else if (quota == null) {
                throw new JsonReadException("missing field \"quota_info\"", expectObjectStart);
            } else if (str4 == null) {
                throw new JsonReadException("missing field \"email\"", expectObjectStart);
            } else if (nameDetails == null) {
                throw new JsonReadException("missing field \"nameDetails\"", expectObjectStart);
            } else if (bool != null) {
                DbxAccountInfo dbxAccountInfo = new DbxAccountInfo(j, str, str2, str3, quota, str4, nameDetails, bool.booleanValue());
                return dbxAccountInfo;
            } else {
                throw new JsonReadException("missing field \"emailVerified\"", expectObjectStart);
            }
        }
    };
    public final String country;
    public final String displayName;
    public final String email;
    public final boolean emailVerified;
    public final NameDetails nameDetails;
    public final Quota quota;
    public final String referralLink;
    public final long userId;

    /* renamed from: com.dropbox.core.v1.DbxAccountInfo$NameDetails */
    public static final class NameDetails extends Dumpable {
        /* access modifiers changed from: private */

        /* renamed from: FM */
        public static final FieldMapping f64FM;
        private static final int FM_familiar_name = 0;
        private static final int FM_given_name = 1;
        private static final int FM_surname = 2;
        public static final JsonReader<NameDetails> Reader = new JsonReader<NameDetails>() {
            public final NameDetails read(JsonParser jsonParser) throws IOException, JsonReadException {
                JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
                String str = null;
                String str2 = null;
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    int i = NameDetails.f64FM.get(currentName);
                    switch (i) {
                        case -1:
                            JsonReader.skipValue(jsonParser);
                            break;
                        case 0:
                            str = (String) JsonReader.StringReader.readField(jsonParser, currentName, str);
                            break;
                        case 1:
                            str3 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str3);
                            break;
                        case 2:
                            str2 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str2);
                            break;
                        default:
                            try {
                                StringBuilder sb = new StringBuilder();
                                sb.append("bad index: ");
                                sb.append(i);
                                sb.append(", field = \"");
                                sb.append(currentName);
                                sb.append("\"");
                                throw new AssertionError(sb.toString());
                            } catch (JsonReadException e) {
                                throw e.addFieldContext(currentName);
                            }
                    }
                }
                JsonReader.expectObjectEnd(jsonParser);
                if (str == null) {
                    throw new JsonReadException("missing field \"familiarName\"", expectObjectStart);
                } else if (str2 == null) {
                    throw new JsonReadException("missing field \"surname\"", expectObjectStart);
                } else if (str3 != null) {
                    return new NameDetails(str, str3, str2);
                } else {
                    throw new JsonReadException("missing field \"givenName\"", expectObjectStart);
                }
            }
        };
        public final String familiarName;
        public final String givenName;
        public final String surname;

        public NameDetails(String str, String str2, String str3) {
            this.familiarName = str;
            this.givenName = str2;
            this.surname = str3;
        }

        /* access modifiers changed from: protected */
        public void dumpFields(DumpWriter dumpWriter) {
            dumpWriter.mo10658f("familiarName").mo10671v(this.familiarName);
            dumpWriter.mo10658f("givenName").mo10671v(this.givenName);
            dumpWriter.mo10658f("surname").mo10671v(this.surname);
        }

        static {
            Builder builder = new Builder();
            builder.add("familiar_name", 0);
            builder.add("given_name", 1);
            builder.add("surname", 2);
            f64FM = builder.build();
        }
    }

    /* renamed from: com.dropbox.core.v1.DbxAccountInfo$Quota */
    public static final class Quota extends Dumpable {
        /* access modifiers changed from: private */

        /* renamed from: FM */
        public static final FieldMapping f65FM;
        private static final int FM_normal = 1;
        private static final int FM_quota = 0;
        private static final int FM_shared = 2;
        public static final JsonReader<Quota> Reader = new JsonReader<Quota>() {
            public final Quota read(JsonParser jsonParser) throws IOException, JsonReadException {
                JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
                long j = -1;
                long j2 = -1;
                long j3 = -1;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    int i = Quota.f65FM.get(currentName);
                    switch (i) {
                        case -1:
                            JsonReader.skipValue(jsonParser);
                            break;
                        case 0:
                            j = JsonReader.readUnsignedLongField(jsonParser, currentName, j);
                            break;
                        case 1:
                            j2 = JsonReader.readUnsignedLongField(jsonParser, currentName, j2);
                            break;
                        case 2:
                            j3 = JsonReader.readUnsignedLongField(jsonParser, currentName, j3);
                            break;
                        default:
                            try {
                                StringBuilder sb = new StringBuilder();
                                sb.append("bad index: ");
                                sb.append(i);
                                sb.append(", field = \"");
                                sb.append(currentName);
                                sb.append("\"");
                                throw new AssertionError(sb.toString());
                            } catch (JsonReadException e) {
                                throw e.addFieldContext(currentName);
                            }
                    }
                }
                JsonReader.expectObjectEnd(jsonParser);
                if (j < 0) {
                    throw new JsonReadException("missing field \"quota\"", expectObjectStart);
                } else if (j2 < 0) {
                    throw new JsonReadException("missing field \"normal\"", expectObjectStart);
                } else if (j3 >= 0) {
                    Quota quota = new Quota(j, j2, j3);
                    return quota;
                } else {
                    throw new JsonReadException("missing field \"shared\"", expectObjectStart);
                }
            }
        };
        public final long normal;
        public final long shared;
        public final long total;

        public Quota(long j, long j2, long j3) {
            this.total = j;
            this.normal = j2;
            this.shared = j3;
        }

        /* access modifiers changed from: protected */
        public void dumpFields(DumpWriter dumpWriter) {
            dumpWriter.mo10658f("total").mo10667v(this.total);
            dumpWriter.mo10658f("normal").mo10667v(this.normal);
            dumpWriter.mo10658f("shared").mo10667v(this.shared);
        }

        static {
            Builder builder = new Builder();
            builder.add("quota", 0);
            builder.add("normal", 1);
            builder.add("shared", 2);
            f65FM = builder.build();
        }
    }

    public DbxAccountInfo(long j, String str, String str2, String str3, Quota quota2, String str4, NameDetails nameDetails2, boolean z) {
        this.userId = j;
        this.displayName = str;
        this.country = str2;
        this.referralLink = str3;
        this.quota = quota2;
        this.email = str4;
        this.nameDetails = nameDetails2;
        this.emailVerified = z;
    }

    /* access modifiers changed from: protected */
    public void dumpFields(DumpWriter dumpWriter) {
        dumpWriter.mo10658f("userId").mo10667v(this.userId);
        dumpWriter.mo10658f("displayName").mo10671v(this.displayName);
        dumpWriter.mo10658f("country").mo10671v(this.country);
        dumpWriter.mo10658f("referralLink").mo10671v(this.referralLink);
        dumpWriter.mo10658f("quota").mo10668v((Dumpable) this.quota);
        dumpWriter.mo10658f("nameDetails").mo10668v((Dumpable) this.nameDetails);
        dumpWriter.mo10658f("email").mo10671v(this.email);
        dumpWriter.mo10658f("emailVerified").mo10673v(this.emailVerified);
    }

    static {
        Builder builder = new Builder();
        builder.add("uid", 0);
        builder.add("display_name", 1);
        builder.add("country", 2);
        builder.add("referral_link", 3);
        builder.add("quota_info", 4);
        builder.add("name_details", 5);
        builder.add("email", 6);
        builder.add("email_verified", 7);
        f63FM = builder.build();
    }
}
