package com.dropbox.core.p005v2.team;

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
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.DevicesActive */
public class DevicesActive {

    /* renamed from: android reason: collision with root package name */
    protected final List<Long> f538android;
    protected final List<Long> ios;
    protected final List<Long> linux;
    protected final List<Long> macos;
    protected final List<Long> other;
    protected final List<Long> total;
    protected final List<Long> windows;

    /* renamed from: com.dropbox.core.v2.team.DevicesActive$Serializer */
    static class Serializer extends StructSerializer<DevicesActive> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DevicesActive devicesActive, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("windows");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(devicesActive.windows, jsonGenerator);
            jsonGenerator.writeFieldName("macos");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(devicesActive.macos, jsonGenerator);
            jsonGenerator.writeFieldName("linux");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(devicesActive.linux, jsonGenerator);
            jsonGenerator.writeFieldName("ios");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(devicesActive.ios, jsonGenerator);
            jsonGenerator.writeFieldName("android");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(devicesActive.f538android, jsonGenerator);
            jsonGenerator.writeFieldName("other");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(devicesActive.other, jsonGenerator);
            jsonGenerator.writeFieldName("total");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(devicesActive.total, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DevicesActive deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                List list2 = null;
                List list3 = null;
                List list4 = null;
                List list5 = null;
                List list6 = null;
                List list7 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("windows".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("macos".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("linux".equals(currentName)) {
                        list3 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("ios".equals(currentName)) {
                        list4 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("android".equals(currentName)) {
                        list5 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("other".equals(currentName)) {
                        list6 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("total".equals(currentName)) {
                        list7 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"windows\" missing.");
                } else if (list2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"macos\" missing.");
                } else if (list3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"linux\" missing.");
                } else if (list4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"ios\" missing.");
                } else if (list5 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"android\" missing.");
                } else if (list6 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"other\" missing.");
                } else if (list7 != null) {
                    DevicesActive devicesActive = new DevicesActive(list, list2, list3, list4, list5, list6, list7);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(devicesActive, devicesActive.toStringMultiline());
                    return devicesActive;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"total\" missing.");
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

    public DevicesActive(List<Long> list, List<Long> list2, List<Long> list3, List<Long> list4, List<Long> list5, List<Long> list6, List<Long> list7) {
        if (list != null) {
            for (Long l : list) {
                if (l == null) {
                    throw new IllegalArgumentException("An item in list 'windows' is null");
                }
            }
            this.windows = list;
            if (list2 != null) {
                for (Long l2 : list2) {
                    if (l2 == null) {
                        throw new IllegalArgumentException("An item in list 'macos' is null");
                    }
                }
                this.macos = list2;
                if (list3 != null) {
                    for (Long l3 : list3) {
                        if (l3 == null) {
                            throw new IllegalArgumentException("An item in list 'linux' is null");
                        }
                    }
                    this.linux = list3;
                    if (list4 != null) {
                        for (Long l4 : list4) {
                            if (l4 == null) {
                                throw new IllegalArgumentException("An item in list 'ios' is null");
                            }
                        }
                        this.ios = list4;
                        if (list5 != null) {
                            for (Long l5 : list5) {
                                if (l5 == null) {
                                    throw new IllegalArgumentException("An item in list 'android' is null");
                                }
                            }
                            this.f538android = list5;
                            if (list6 != null) {
                                for (Long l6 : list6) {
                                    if (l6 == null) {
                                        throw new IllegalArgumentException("An item in list 'other' is null");
                                    }
                                }
                                this.other = list6;
                                if (list7 != null) {
                                    for (Long l7 : list7) {
                                        if (l7 == null) {
                                            throw new IllegalArgumentException("An item in list 'total' is null");
                                        }
                                    }
                                    this.total = list7;
                                    return;
                                }
                                throw new IllegalArgumentException("Required value for 'total' is null");
                            }
                            throw new IllegalArgumentException("Required value for 'other' is null");
                        }
                        throw new IllegalArgumentException("Required value for 'android' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'ios' is null");
                }
                throw new IllegalArgumentException("Required value for 'linux' is null");
            }
            throw new IllegalArgumentException("Required value for 'macos' is null");
        }
        throw new IllegalArgumentException("Required value for 'windows' is null");
    }

    public List<Long> getWindows() {
        return this.windows;
    }

    public List<Long> getMacos() {
        return this.macos;
    }

    public List<Long> getLinux() {
        return this.linux;
    }

    public List<Long> getIos() {
        return this.ios;
    }

    public List<Long> getAndroid() {
        return this.f538android;
    }

    public List<Long> getOther() {
        return this.other;
    }

    public List<Long> getTotal() {
        return this.total;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.windows, this.macos, this.linux, this.ios, this.f538android, this.other, this.total});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006a, code lost:
        if (r2.equals(r5) == false) goto L_0x006d;
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
            if (r2 == 0) goto L_0x006f
            com.dropbox.core.v2.team.DevicesActive r5 = (com.dropbox.core.p005v2.team.DevicesActive) r5
            java.util.List<java.lang.Long> r2 = r4.windows
            java.util.List<java.lang.Long> r3 = r5.windows
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0024:
            java.util.List<java.lang.Long> r2 = r4.macos
            java.util.List<java.lang.Long> r3 = r5.macos
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0030:
            java.util.List<java.lang.Long> r2 = r4.linux
            java.util.List<java.lang.Long> r3 = r5.linux
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x003c:
            java.util.List<java.lang.Long> r2 = r4.ios
            java.util.List<java.lang.Long> r3 = r5.ios
            if (r2 == r3) goto L_0x0048
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0048:
            java.util.List<java.lang.Long> r2 = r4.f538android
            java.util.List<java.lang.Long> r3 = r5.f538android
            if (r2 == r3) goto L_0x0054
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0054:
            java.util.List<java.lang.Long> r2 = r4.other
            java.util.List<java.lang.Long> r3 = r5.other
            if (r2 == r3) goto L_0x0060
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0060:
            java.util.List<java.lang.Long> r2 = r4.total
            java.util.List<java.lang.Long> r5 = r5.total
            if (r2 == r5) goto L_0x006e
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x006d
            goto L_0x006e
        L_0x006d:
            r0 = 0
        L_0x006e:
            return r0
        L_0x006f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.DevicesActive.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
