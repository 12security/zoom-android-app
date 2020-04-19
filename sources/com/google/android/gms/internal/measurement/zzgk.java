package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez.zzc;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import org.apache.http.message.TokenParser;

final class zzgk {
    static String zza(zzgh zzgh, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(str);
        zza(zzgh, sb, 0);
        return sb.toString();
    }

    private static void zza(zzgh zzgh, StringBuilder sb, int i) {
        Method[] declaredMethods;
        boolean z;
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        TreeSet<String> treeSet = new TreeSet<>();
        for (Method method : zzgh.getClass().getDeclaredMethods()) {
            hashMap2.put(method.getName(), method);
            if (method.getParameterTypes().length == 0) {
                hashMap.put(method.getName(), method);
                if (method.getName().startsWith("get")) {
                    treeSet.add(method.getName());
                }
            }
        }
        for (String str : treeSet) {
            String replaceFirst = str.replaceFirst("get", "");
            if (replaceFirst.endsWith("List") && !replaceFirst.endsWith("OrBuilderList") && !replaceFirst.equals("List")) {
                String valueOf = String.valueOf(replaceFirst.substring(0, 1).toLowerCase());
                String valueOf2 = String.valueOf(replaceFirst.substring(1, replaceFirst.length() - 4));
                String concat = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
                Method method2 = (Method) hashMap.get(str);
                if (method2 != null && method2.getReturnType().equals(List.class)) {
                    zzb(sb, i, zzcs(concat), zzez.zza(method2, (Object) zzgh, new Object[0]));
                }
            }
            if (replaceFirst.endsWith("Map") && !replaceFirst.equals("Map")) {
                String valueOf3 = String.valueOf(replaceFirst.substring(0, 1).toLowerCase());
                String valueOf4 = String.valueOf(replaceFirst.substring(1, replaceFirst.length() - 3));
                String concat2 = valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3);
                Method method3 = (Method) hashMap.get(str);
                if (method3 != null && method3.getReturnType().equals(Map.class) && !method3.isAnnotationPresent(Deprecated.class) && Modifier.isPublic(method3.getModifiers())) {
                    zzb(sb, i, zzcs(concat2), zzez.zza(method3, (Object) zzgh, new Object[0]));
                }
            }
            String str2 = "set";
            String valueOf5 = String.valueOf(replaceFirst);
            if (((Method) hashMap2.get(valueOf5.length() != 0 ? str2.concat(valueOf5) : new String(str2))) != null) {
                if (replaceFirst.endsWith("Bytes")) {
                    String str3 = "get";
                    String valueOf6 = String.valueOf(replaceFirst.substring(0, replaceFirst.length() - 5));
                    if (hashMap.containsKey(valueOf6.length() != 0 ? str3.concat(valueOf6) : new String(str3))) {
                    }
                }
                String valueOf7 = String.valueOf(replaceFirst.substring(0, 1).toLowerCase());
                String valueOf8 = String.valueOf(replaceFirst.substring(1));
                String concat3 = valueOf8.length() != 0 ? valueOf7.concat(valueOf8) : new String(valueOf7);
                String str4 = "get";
                String valueOf9 = String.valueOf(replaceFirst);
                Method method4 = (Method) hashMap.get(valueOf9.length() != 0 ? str4.concat(valueOf9) : new String(str4));
                String str5 = "has";
                String valueOf10 = String.valueOf(replaceFirst);
                Method method5 = (Method) hashMap.get(valueOf10.length() != 0 ? str5.concat(valueOf10) : new String(str5));
                if (method4 != null) {
                    Object zza = zzez.zza(method4, (Object) zzgh, new Object[0]);
                    if (method5 == null) {
                        boolean z2 = zza instanceof Boolean ? !((Boolean) zza).booleanValue() : zza instanceof Integer ? ((Integer) zza).intValue() == 0 : zza instanceof Float ? ((Float) zza).floatValue() == 0.0f : zza instanceof Double ? ((Double) zza).doubleValue() == 0.0d : zza instanceof String ? zza.equals("") : zza instanceof zzdp ? zza.equals(zzdp.zzaby) : zza instanceof zzgh ? zza == ((zzgh) zza).zzmm() : zza instanceof Enum ? ((Enum) zza).ordinal() == 0 : false;
                        z = !z2;
                    } else {
                        z = ((Boolean) zzez.zza(method5, (Object) zzgh, new Object[0])).booleanValue();
                    }
                    if (z) {
                        zzb(sb, i, zzcs(concat3), zza);
                    }
                }
            }
        }
        if (zzgh instanceof zzc) {
            Iterator it = ((zzc) zzgh).zzagt.iterator();
            if (it.hasNext()) {
                ((Entry) it.next()).getKey();
                throw new NoSuchMethodError();
            }
        }
        zzez zzez = (zzez) zzgh;
        if (zzez.zzagn != null) {
            zzez.zzagn.zzb(sb, i);
        }
    }

    static final void zzb(StringBuilder sb, int i, String str, Object obj) {
        if (obj instanceof List) {
            for (Object zzb : (List) obj) {
                zzb(sb, i, str, zzb);
            }
        } else if (obj instanceof Map) {
            for (Entry zzb2 : ((Map) obj).entrySet()) {
                zzb(sb, i, str, zzb2);
            }
        } else {
            sb.append(10);
            int i2 = 0;
            for (int i3 = 0; i3 < i; i3++) {
                sb.append(TokenParser.f498SP);
            }
            sb.append(str);
            if (obj instanceof String) {
                sb.append(": \"");
                sb.append(zzhm.zzd(zzdp.zzcn((String) obj)));
                sb.append('\"');
            } else if (obj instanceof zzdp) {
                sb.append(": \"");
                sb.append(zzhm.zzd((zzdp) obj));
                sb.append('\"');
            } else if (obj instanceof zzez) {
                sb.append(" {");
                zza((zzez) obj, sb, i + 2);
                sb.append(FontStyleHelper.SPLITOR);
                while (i2 < i) {
                    sb.append(TokenParser.f498SP);
                    i2++;
                }
                sb.append("}");
            } else if (obj instanceof Entry) {
                sb.append(" {");
                Entry entry = (Entry) obj;
                int i4 = i + 2;
                zzb(sb, i4, "key", entry.getKey());
                zzb(sb, i4, "value", entry.getValue());
                sb.append(FontStyleHelper.SPLITOR);
                while (i2 < i) {
                    sb.append(TokenParser.f498SP);
                    i2++;
                }
                sb.append("}");
            } else {
                sb.append(": ");
                sb.append(obj.toString());
            }
        }
    }

    private static final String zzcs(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Character.isUpperCase(charAt)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(charAt));
        }
        return sb.toString();
    }
}
