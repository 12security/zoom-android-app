package com.google.api.client.http;

import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import com.google.api.client.util.escape.CharEscapers;
import com.google.common.base.Splitter;
import com.zipow.videobox.util.TextCommandHelper;
import com.zipow.videobox.util.ZMDomainUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

public class UriTemplate {
    private static final String COMPOSITE_NON_EXPLODE_JOINER = ",";
    static final Map<Character, CompositeOutput> COMPOSITE_PREFIXES = new HashMap();

    private enum CompositeOutput {
        PLUS(Character.valueOf('+'), "", ",", false, true),
        HASH(Character.valueOf(TextCommandHelper.CHANNEL_CMD_CHAR), "#", ",", false, true),
        DOT(Character.valueOf('.'), ".", ".", false, false),
        FORWARD_SLASH(Character.valueOf('/'), "/", "/", false, false),
        SEMI_COLON(Character.valueOf(';'), ";", ";", true, false),
        QUERY(Character.valueOf('?'), "?", "&", true, false),
        AMP(Character.valueOf('&'), "&", "&", true, false),
        SIMPLE(null, "", ",", false, false);
        
        private final String explodeJoiner;
        private final String outputPrefix;
        private final Character propertyPrefix;
        private final boolean requiresVarAssignment;
        private final boolean reservedExpansion;

        private CompositeOutput(Character ch, String str, String str2, boolean z, boolean z2) {
            this.propertyPrefix = ch;
            this.outputPrefix = (String) Preconditions.checkNotNull(str);
            this.explodeJoiner = (String) Preconditions.checkNotNull(str2);
            this.requiresVarAssignment = z;
            this.reservedExpansion = z2;
            if (ch != null) {
                UriTemplate.COMPOSITE_PREFIXES.put(ch, this);
            }
        }

        /* access modifiers changed from: 0000 */
        public String getOutputPrefix() {
            return this.outputPrefix;
        }

        /* access modifiers changed from: 0000 */
        public String getExplodeJoiner() {
            return this.explodeJoiner;
        }

        /* access modifiers changed from: 0000 */
        public boolean requiresVarAssignment() {
            return this.requiresVarAssignment;
        }

        /* access modifiers changed from: 0000 */
        public int getVarNameStartIndex() {
            return this.propertyPrefix == null ? 0 : 1;
        }

        /* access modifiers changed from: 0000 */
        public String getEncodedValue(String str) {
            if (this.reservedExpansion) {
                return CharEscapers.escapeUriPath(str);
            }
            return CharEscapers.escapeUri(str);
        }

        /* access modifiers changed from: 0000 */
        public boolean getReservedExpansion() {
            return this.reservedExpansion;
        }
    }

    static {
        CompositeOutput.values();
    }

    static CompositeOutput getCompositeOutput(String str) {
        CompositeOutput compositeOutput = (CompositeOutput) COMPOSITE_PREFIXES.get(Character.valueOf(str.charAt(0)));
        return compositeOutput == null ? CompositeOutput.SIMPLE : compositeOutput;
    }

    private static Map<String, Object> getMap(Object obj) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Entry entry : Data.mapOf(obj).entrySet()) {
            Object value = entry.getValue();
            if (value != null && !Data.isNull(value)) {
                linkedHashMap.put(entry.getKey(), value);
            }
        }
        return linkedHashMap;
    }

    public static String expand(String str, String str2, Object obj, boolean z) {
        if (str2.startsWith("/")) {
            GenericUrl genericUrl = new GenericUrl(str);
            genericUrl.setRawPath(null);
            StringBuilder sb = new StringBuilder();
            sb.append(genericUrl.build());
            sb.append(str2);
            str2 = sb.toString();
        } else if (!str2.startsWith(ZMDomainUtil.ZM_URL_HTTP) && !str2.startsWith("https://")) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(str2);
            str2 = sb2.toString();
        }
        return expand(str2, obj, z);
    }

    public static String expand(String str, Object obj, boolean z) {
        Object obj2;
        Map map = getMap(obj);
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            int indexOf = str.indexOf(123, i);
            if (indexOf != -1) {
                sb.append(str.substring(i, indexOf));
                int indexOf2 = str.indexOf(125, indexOf + 2);
                int i2 = indexOf2 + 1;
                String substring = str.substring(indexOf + 1, indexOf2);
                CompositeOutput compositeOutput = getCompositeOutput(substring);
                ListIterator listIterator = Splitter.m94on(',').splitToList(substring).listIterator();
                boolean z2 = true;
                while (listIterator.hasNext()) {
                    String str2 = (String) listIterator.next();
                    boolean endsWith = str2.endsWith("*");
                    int varNameStartIndex = listIterator.nextIndex() == 1 ? compositeOutput.getVarNameStartIndex() : 0;
                    int length2 = str2.length();
                    if (endsWith) {
                        length2--;
                    }
                    String substring2 = str2.substring(varNameStartIndex, length2);
                    Object remove = map.remove(substring2);
                    if (remove != null) {
                        if (!z2) {
                            sb.append(compositeOutput.getExplodeJoiner());
                        } else {
                            sb.append(compositeOutput.getOutputPrefix());
                            z2 = false;
                        }
                        if (remove instanceof Iterator) {
                            obj2 = getListPropertyValue(substring2, (Iterator) remove, endsWith, compositeOutput);
                        } else if ((remove instanceof Iterable) || remove.getClass().isArray()) {
                            obj2 = getListPropertyValue(substring2, Types.iterableOf(remove).iterator(), endsWith, compositeOutput);
                        } else if (remove.getClass().isEnum()) {
                            if (FieldInfo.m68of((Enum) remove).getName() != null) {
                                if (compositeOutput.requiresVarAssignment()) {
                                    remove = String.format("%s=%s", new Object[]{substring2, remove});
                                }
                                obj2 = CharEscapers.escapeUriPath(remove.toString());
                            } else {
                                obj2 = remove;
                            }
                        } else if (!Data.isValueOfPrimitiveType(remove)) {
                            obj2 = getMapPropertyValue(substring2, getMap(remove), endsWith, compositeOutput);
                        } else {
                            if (compositeOutput.requiresVarAssignment()) {
                                remove = String.format("%s=%s", new Object[]{substring2, remove});
                            }
                            obj2 = compositeOutput.getReservedExpansion() ? CharEscapers.escapeUriPathWithoutReserved(remove.toString()) : CharEscapers.escapeUriPath(remove.toString());
                        }
                        sb.append(obj2);
                    }
                }
                i = i2;
            } else if (i == 0 && !z) {
                return str;
            } else {
                sb.append(str.substring(i));
            }
        }
        if (z) {
            GenericUrl.addQueryParams(map.entrySet(), sb);
        }
        return sb.toString();
    }

    private static String getListPropertyValue(String str, Iterator<?> it, boolean z, CompositeOutput compositeOutput) {
        String str2;
        if (!it.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (z) {
            str2 = compositeOutput.getExplodeJoiner();
        } else {
            str2 = ",";
            if (compositeOutput.requiresVarAssignment()) {
                sb.append(CharEscapers.escapeUriPath(str));
                sb.append("=");
            }
        }
        while (it.hasNext()) {
            if (z && compositeOutput.requiresVarAssignment()) {
                sb.append(CharEscapers.escapeUriPath(str));
                sb.append("=");
            }
            sb.append(compositeOutput.getEncodedValue(it.next().toString()));
            if (it.hasNext()) {
                sb.append(str2);
            }
        }
        return sb.toString();
    }

    private static String getMapPropertyValue(String str, Map<String, Object> map, boolean z, CompositeOutput compositeOutput) {
        String str2;
        String str3;
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (z) {
            str3 = compositeOutput.getExplodeJoiner();
            str2 = "=";
        } else {
            String str4 = ",";
            String str5 = ",";
            if (compositeOutput.requiresVarAssignment()) {
                sb.append(CharEscapers.escapeUriPath(str));
                sb.append("=");
            }
            str3 = str4;
            str2 = str5;
        }
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            String encodedValue = compositeOutput.getEncodedValue((String) entry.getKey());
            String encodedValue2 = compositeOutput.getEncodedValue(entry.getValue().toString());
            sb.append(encodedValue);
            sb.append(str2);
            sb.append(encodedValue2);
            if (it.hasNext()) {
                sb.append(str3);
            }
        }
        return sb.toString();
    }
}
