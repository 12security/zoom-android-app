package com.google.android.gms.internal.measurement;

import org.apache.http.message.TokenParser;

final class zzhm {
    static String zzd(zzdp zzdp) {
        zzhn zzhn = new zzhn(zzdp);
        StringBuilder sb = new StringBuilder(zzhn.size());
        for (int i = 0; i < zzhn.size(); i++) {
            byte zzr = zzhn.zzr(i);
            if (zzr == 34) {
                sb.append("\\\"");
            } else if (zzr == 39) {
                sb.append("\\'");
            } else if (zzr != 92) {
                switch (zzr) {
                    case 7:
                        sb.append("\\a");
                        break;
                    case 8:
                        sb.append("\\b");
                        break;
                    case 9:
                        sb.append("\\t");
                        break;
                    case 10:
                        sb.append("\\n");
                        break;
                    case 11:
                        sb.append("\\v");
                        break;
                    case 12:
                        sb.append("\\f");
                        break;
                    case 13:
                        sb.append("\\r");
                        break;
                    default:
                        if (zzr >= 32 && zzr <= 126) {
                            sb.append((char) zzr);
                            break;
                        } else {
                            sb.append(TokenParser.ESCAPE);
                            sb.append((char) (((zzr >>> 6) & 3) + 48));
                            sb.append((char) (((zzr >>> 3) & 7) + 48));
                            sb.append((char) ((zzr & 7) + 48));
                            break;
                        }
                }
            } else {
                sb.append("\\\\");
            }
        }
        return sb.toString();
    }
}
