package com.dropbox.core.json;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import p021us.zoom.androidlib.util.TimeUtil;

public class JsonDateReader {
    public static final JsonReader<Date> Dropbox = new JsonReader<Date>() {
        public Date read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation currentLocation = jsonParser.getCurrentLocation();
            try {
                Date parseDropboxDate = JsonDateReader.parseDropboxDate(jsonParser.getTextCharacters(), jsonParser.getTextOffset(), jsonParser.getTextLength());
                jsonParser.nextToken();
                return parseDropboxDate;
            } catch (JsonParseException e) {
                throw JsonReadException.fromJackson(e);
            } catch (ParseException e2) {
                StringBuilder sb = new StringBuilder();
                sb.append("bad date: \"");
                sb.append(e2.getMessage());
                sb.append(" at offset ");
                sb.append(e2.getErrorOffset());
                throw new JsonReadException(sb.toString(), currentLocation);
            }
        }
    };
    public static final JsonReader<Date> DropboxV2 = new JsonReader<Date>() {
        public Date read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation currentLocation = jsonParser.getCurrentLocation();
            try {
                Date parseDropbox8601Date = JsonDateReader.parseDropbox8601Date(jsonParser.getTextCharacters(), jsonParser.getTextOffset(), jsonParser.getTextLength());
                jsonParser.nextToken();
                return parseDropbox8601Date;
            } catch (JsonParseException e) {
                throw JsonReadException.fromJackson(e);
            } catch (ParseException e2) {
                StringBuilder sb = new StringBuilder();
                sb.append("bad date: \"");
                sb.append(e2.getMessage());
                sb.append(" at offset ");
                sb.append(e2.getErrorOffset());
                throw new JsonReadException(sb.toString(), currentLocation);
            }
        }
    };
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static int getMonthIndex(char c, char c2, char c3) {
        boolean z = true;
        if (c == 'A') {
            if ((c2 == 'p') && (c3 == 'r')) {
                return 3;
            }
            boolean z2 = c2 == 'u';
            if (c3 != 'g') {
                z = false;
            }
            return z2 & z ? 7 : -1;
        } else if (c == 'D') {
            boolean z3 = c2 == 'e';
            if (c3 != 'c') {
                z = false;
            }
            return z3 & z ? 11 : -1;
        } else if (c == 'F') {
            return (c2 == 'e') & (c3 == 'b') ? 1 : 0;
        } else if (c == 'J') {
            boolean z4 = c2 == 'a';
            if (c3 != 'n') {
                z = false;
            }
            if (z4 && z) {
                return 0;
            }
            if (c2 != 'u') {
                return -1;
            }
            if (c3 == 'n') {
                return 5;
            }
            return c3 == 'l' ? 6 : -1;
        } else if (c != 'S') {
            switch (c) {
                case 'M':
                    if (c2 != 'a') {
                        return -1;
                    }
                    if (c3 == 'r') {
                        return 2;
                    }
                    return c3 == 'y' ? 4 : -1;
                case 'N':
                    boolean z5 = c2 == 'o';
                    if (c3 != 'v') {
                        z = false;
                    }
                    return z5 & z ? 10 : -1;
                case 'O':
                    boolean z6 = c2 == 'c';
                    if (c3 != 't') {
                        z = false;
                    }
                    return z6 & z ? 9 : -1;
                default:
                    return -1;
            }
        } else {
            boolean z7 = c2 == 'e';
            if (c3 != 'p') {
                z = false;
            }
            return z7 & z ? 8 : -1;
        }
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isValidDayOfWeek(char c, char c2, char c3) {
        if (c == 'F') {
            return (c2 == 'r') & (c3 == 'i');
        } else if (c == 'M') {
            return (c2 == 'o') & (c3 == 'n');
        } else if (c != 'W') {
            switch (c) {
                case 'S':
                    if ((c2 == 'u') && (c3 == 'n')) {
                        return true;
                    }
                    return (c2 == 'a') & (c3 == 't');
                case 'T':
                    if ((c2 == 'u') && (c3 == 'e')) {
                        return true;
                    }
                    return (c2 == 'h') & (c3 == 'u');
                default:
                    return false;
            }
        } else {
            return (c2 == 'e') & (c3 == 'd');
        }
    }

    public static Date parseDropboxDate(char[] cArr, int i, int i2) throws ParseException {
        char[] cArr2 = cArr;
        int i3 = i;
        int i4 = i2;
        if (i4 != 31) {
            StringBuilder sb = new StringBuilder();
            sb.append("expecting date to be 31 characters, got ");
            sb.append(i4);
            throw new ParseException(sb.toString(), 0);
        } else if (cArr2.length < i3 + 31 || i3 < 0) {
            throw new IllegalArgumentException("range is not within 'b'");
        } else {
            int i5 = i3 + 3;
            int i6 = i3 + 4;
            int i7 = i3 + 7;
            int i8 = i3 + 11;
            int i9 = i3 + 16;
            int i10 = i3 + 19;
            int i11 = i3 + 22;
            int i12 = i3 + 25;
            int i13 = i3 + 26;
            int i14 = i3 + 27;
            int i15 = i3 + 28;
            int i16 = i3 + 29;
            int i17 = i15;
            boolean z = (cArr2[i5] != ',') | (cArr2[i6] != ' ') | (cArr2[i7] != ' ') | (cArr2[i8] != ' ') | (cArr2[i9] != ' ') | (cArr2[i10] != ':') | (cArr2[i11] != ':') | (cArr2[i12] != ' ') | (cArr2[i13] != '+') | (cArr2[i14] != '0') | (cArr2[i15] != '0') | (cArr2[i16] != '0');
            int i18 = i3 + 30;
            if (!z && !(cArr2[i18] != '0')) {
                int i19 = i;
                if (isValidDayOfWeek(cArr2[i19], cArr2[i19 + 1], cArr2[i19 + 2])) {
                    int monthIndex = getMonthIndex(cArr2[i19 + 8], cArr2[i19 + 9], cArr2[i19 + 10]);
                    if (monthIndex != -1) {
                        char c = cArr2[i19 + 5];
                        char c2 = cArr2[i19 + 6];
                        if (!isDigit(c) || !isDigit(c2)) {
                            throw new ParseException("invalid day of month", 5);
                        }
                        int i20 = ((c * 10) + c2) - 528;
                        char c3 = cArr2[i19 + 12];
                        char c4 = cArr2[i19 + 13];
                        char c5 = cArr2[i19 + 14];
                        char c6 = cArr2[i19 + 15];
                        if (!((!isDigit(c3)) | (!isDigit(c4)) | (!isDigit(c5))) && !(!isDigit(c6))) {
                            int i21 = ((((c3 * 1000) + (c4 * 'd')) + (c5 * 10)) + c6) - 53328;
                            char c7 = cArr2[i19 + 17];
                            char c8 = cArr2[i19 + 18];
                            if (!(!isDigit(c7)) && !(!isDigit(c8))) {
                                int i22 = ((c7 * 10) + c8) - 528;
                                char c9 = cArr2[i19 + 20];
                                char c10 = cArr2[i19 + 21];
                                if (!(!isDigit(c9)) && !(!isDigit(c10))) {
                                    int i23 = ((c9 * 10) + c10) - 528;
                                    char c11 = cArr2[i19 + 23];
                                    char c12 = cArr2[i19 + 24];
                                    if (!(!isDigit(c11)) && !(true ^ isDigit(c12))) {
                                        GregorianCalendar gregorianCalendar = new GregorianCalendar(i21, monthIndex, i20, i22, i23, ((c11 * 10) + c12) - 528);
                                        gregorianCalendar.setTimeZone(UTC);
                                        return gregorianCalendar.getTime();
                                    }
                                    throw new ParseException("invalid second", 23);
                                }
                                throw new ParseException("invalid minute", 20);
                            }
                            throw new ParseException("invalid hour", 17);
                        }
                        throw new ParseException("invalid year", 12);
                    }
                    throw new ParseException("invalid month", 8);
                }
                throw new ParseException("invalid day of week", i19);
            } else if (cArr2[i5] != ',') {
                throw new ParseException("expecting ','", 3);
            } else if (cArr2[i6] != ' ') {
                throw new ParseException("expecting ' '", 4);
            } else if (cArr2[i7] != ' ') {
                throw new ParseException("expecting ' '", 7);
            } else if (cArr2[i8] != ' ') {
                throw new ParseException("expecting ' '", 11);
            } else if (cArr2[i9] != ' ') {
                throw new ParseException("expecting ' '", 16);
            } else if (cArr2[i10] != ':') {
                throw new ParseException("expecting ':'", 19);
            } else if (cArr2[i11] != ':') {
                throw new ParseException("expecting ':'", 22);
            } else if (cArr2[i12] != ' ') {
                throw new ParseException("expecting ' '", 25);
            } else if (cArr2[i13] != '+') {
                throw new ParseException("expecting '+'", 26);
            } else if (cArr2[i14] != '0') {
                throw new ParseException("expecting '0'", 27);
            } else if (cArr2[i17] != '0') {
                throw new ParseException("expecting '0'", 28);
            } else if (cArr2[i16] != '0') {
                throw new ParseException("expecting '0'", 29);
            } else if (cArr2[i18] != '0') {
                throw new ParseException("expecting '0'", 30);
            } else {
                throw new AssertionError("unreachable");
            }
        }
    }

    public static Date parseDropbox8601Date(char[] cArr, int i, int i2) throws ParseException {
        SimpleDateFormat simpleDateFormat;
        if (i2 == 20 || i2 == 24) {
            String str = new String(cArr, i, i2);
            if (i2 == 20) {
                simpleDateFormat = new SimpleDateFormat(TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z_WITH_QUOTE);
            } else {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            }
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date parse = simpleDateFormat.parse(str);
                if (parse != null) {
                    return parse;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("invalid date");
                sb.append(str);
                throw new ParseException(sb.toString(), 0);
            } catch (IllegalArgumentException unused) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("invalid characters in date");
                sb2.append(str);
                throw new ParseException(sb2.toString(), 0);
            }
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("expecting date to be 20 or 24 characters, got ");
            sb3.append(i2);
            throw new ParseException(sb3.toString(), 0);
        }
    }
}
