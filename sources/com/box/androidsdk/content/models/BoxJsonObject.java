package com.box.androidsdk.content.models;

import com.box.androidsdk.content.utils.BoxDateFormat;
import com.box.androidsdk.content.utils.SdkUtils;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BoxJsonObject extends BoxObject implements Serializable {
    private static final long serialVersionUID = 7174936367401884790L;
    protected final LinkedHashMap<String, Object> mProperties;

    public BoxJsonObject() {
        this.mProperties = new LinkedHashMap<>();
    }

    public BoxJsonObject(Map<String, Object> map) {
        this.mProperties = new LinkedHashMap<>(map);
    }

    public void createFromJson(String str) {
        createFromJson(JsonObject.readFrom(str));
    }

    public void createFromJson(JsonObject jsonObject) {
        Iterator it = jsonObject.iterator();
        while (it.hasNext()) {
            Member member = (Member) it.next();
            if (member.getValue().isNull()) {
                parseNullJsonMember(member);
            } else {
                parseJSONMember(member);
            }
        }
    }

    public void parseNullJsonMember(Member member) {
        if (!SdkUtils.isEmptyString(member.getName())) {
            this.mProperties.put(member.getName(), null);
        }
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (!(this instanceof BoxEntity)) {
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("unhandled json member '");
            sb.append(name);
            sb.append("' xxx  ");
            sb.append(value);
            sb.append(" current object ");
            sb.append(getClass());
            printStream.println(sb.toString());
        }
        try {
            this.mProperties.put(name, value.asString());
        } catch (UnsupportedOperationException unused) {
            this.mProperties.put(name, value.toString());
        }
    }

    public String toJson() {
        return toJsonObject().toString();
    }

    /* access modifiers changed from: protected */
    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        for (Entry entry : this.mProperties.entrySet()) {
            jsonObject.add((String) entry.getKey(), parseJsonObject(entry));
        }
        return jsonObject;
    }

    /* access modifiers changed from: protected */
    public JsonValue parseJsonObject(Entry<String, Object> entry) {
        Object value = entry.getValue();
        if (value instanceof BoxJsonObject) {
            return ((BoxJsonObject) value).toJsonObject();
        }
        if (value instanceof Integer) {
            return JsonValue.valueOf(((Integer) value).intValue());
        }
        if (value instanceof Long) {
            return JsonValue.valueOf(((Long) value).longValue());
        }
        if (value instanceof Float) {
            return JsonValue.valueOf(((Float) value).floatValue());
        }
        if (value instanceof Double) {
            return JsonValue.valueOf(((Double) value).doubleValue());
        }
        if (value instanceof Boolean) {
            return JsonValue.valueOf(((Boolean) value).booleanValue());
        }
        if (value instanceof Enum) {
            return JsonValue.valueOf(value.toString());
        }
        if (value instanceof Date) {
            return JsonValue.valueOf(BoxDateFormat.format((Date) value));
        }
        if (value instanceof String) {
            return JsonValue.valueOf((String) value);
        }
        if (value instanceof Collection) {
            return parseJsonArray((Collection) value);
        }
        return JsonValue.valueOf((String) null);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.lang.Object>, for r3v0, types: [java.util.Collection<java.lang.Object>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.eclipsesource.json.JsonArray parseJsonArray(java.util.Collection<java.lang.Object> r3) {
        /*
            r2 = this;
            com.eclipsesource.json.JsonArray r0 = new com.eclipsesource.json.JsonArray
            r0.<init>()
            java.util.Iterator r3 = r3.iterator()
        L_0x0009:
            boolean r1 = r3.hasNext()
            if (r1 == 0) goto L_0x001f
            java.lang.Object r1 = r3.next()
            java.lang.String r1 = r1.toString()
            com.eclipsesource.json.JsonValue r1 = com.eclipsesource.json.JsonValue.valueOf(r1)
            r0.add(r1)
            goto L_0x0009
        L_0x001f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.box.androidsdk.content.models.BoxJsonObject.parseJsonArray(java.util.Collection):com.eclipsesource.json.JsonArray");
    }

    public HashMap<String, Object> getPropertiesAsHashMap() {
        return (HashMap) SdkUtils.cloneSerializable(this.mProperties);
    }
}
