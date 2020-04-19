package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionArg */
public final class RevokeDeviceSessionArg {
    private Tag _tag;
    /* access modifiers changed from: private */
    public RevokeDesktopClientArg desktopClientValue;
    /* access modifiers changed from: private */
    public DeviceSessionArg mobileClientValue;
    /* access modifiers changed from: private */
    public DeviceSessionArg webSessionValue;

    /* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionArg$Serializer */
    static class Serializer extends UnionSerializer<RevokeDeviceSessionArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RevokeDeviceSessionArg revokeDeviceSessionArg, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (revokeDeviceSessionArg.tag()) {
                case WEB_SESSION:
                    jsonGenerator.writeStartObject();
                    writeTag("web_session", jsonGenerator);
                    Serializer.INSTANCE.serialize(revokeDeviceSessionArg.webSessionValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case DESKTOP_CLIENT:
                    jsonGenerator.writeStartObject();
                    writeTag("desktop_client", jsonGenerator);
                    Serializer.INSTANCE.serialize(revokeDeviceSessionArg.desktopClientValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case MOBILE_CLIENT:
                    jsonGenerator.writeStartObject();
                    writeTag("mobile_client", jsonGenerator);
                    Serializer.INSTANCE.serialize(revokeDeviceSessionArg.mobileClientValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(revokeDeviceSessionArg.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public RevokeDeviceSessionArg deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            RevokeDeviceSessionArg revokeDeviceSessionArg;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if ("web_session".equals(str)) {
                    revokeDeviceSessionArg = RevokeDeviceSessionArg.webSession(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("desktop_client".equals(str)) {
                    revokeDeviceSessionArg = RevokeDeviceSessionArg.desktopClient(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("mobile_client".equals(str)) {
                    revokeDeviceSessionArg = RevokeDeviceSessionArg.mobileClient(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return revokeDeviceSessionArg;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionArg$Tag */
    public enum Tag {
        WEB_SESSION,
        DESKTOP_CLIENT,
        MOBILE_CLIENT
    }

    private RevokeDeviceSessionArg() {
    }

    private RevokeDeviceSessionArg withTag(Tag tag) {
        RevokeDeviceSessionArg revokeDeviceSessionArg = new RevokeDeviceSessionArg();
        revokeDeviceSessionArg._tag = tag;
        return revokeDeviceSessionArg;
    }

    private RevokeDeviceSessionArg withTagAndWebSession(Tag tag, DeviceSessionArg deviceSessionArg) {
        RevokeDeviceSessionArg revokeDeviceSessionArg = new RevokeDeviceSessionArg();
        revokeDeviceSessionArg._tag = tag;
        revokeDeviceSessionArg.webSessionValue = deviceSessionArg;
        return revokeDeviceSessionArg;
    }

    private RevokeDeviceSessionArg withTagAndDesktopClient(Tag tag, RevokeDesktopClientArg revokeDesktopClientArg) {
        RevokeDeviceSessionArg revokeDeviceSessionArg = new RevokeDeviceSessionArg();
        revokeDeviceSessionArg._tag = tag;
        revokeDeviceSessionArg.desktopClientValue = revokeDesktopClientArg;
        return revokeDeviceSessionArg;
    }

    private RevokeDeviceSessionArg withTagAndMobileClient(Tag tag, DeviceSessionArg deviceSessionArg) {
        RevokeDeviceSessionArg revokeDeviceSessionArg = new RevokeDeviceSessionArg();
        revokeDeviceSessionArg._tag = tag;
        revokeDeviceSessionArg.mobileClientValue = deviceSessionArg;
        return revokeDeviceSessionArg;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isWebSession() {
        return this._tag == Tag.WEB_SESSION;
    }

    public static RevokeDeviceSessionArg webSession(DeviceSessionArg deviceSessionArg) {
        if (deviceSessionArg != null) {
            return new RevokeDeviceSessionArg().withTagAndWebSession(Tag.WEB_SESSION, deviceSessionArg);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DeviceSessionArg getWebSessionValue() {
        if (this._tag == Tag.WEB_SESSION) {
            return this.webSessionValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.WEB_SESSION, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isDesktopClient() {
        return this._tag == Tag.DESKTOP_CLIENT;
    }

    public static RevokeDeviceSessionArg desktopClient(RevokeDesktopClientArg revokeDesktopClientArg) {
        if (revokeDesktopClientArg != null) {
            return new RevokeDeviceSessionArg().withTagAndDesktopClient(Tag.DESKTOP_CLIENT, revokeDesktopClientArg);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RevokeDesktopClientArg getDesktopClientValue() {
        if (this._tag == Tag.DESKTOP_CLIENT) {
            return this.desktopClientValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.DESKTOP_CLIENT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isMobileClient() {
        return this._tag == Tag.MOBILE_CLIENT;
    }

    public static RevokeDeviceSessionArg mobileClient(DeviceSessionArg deviceSessionArg) {
        if (deviceSessionArg != null) {
            return new RevokeDeviceSessionArg().withTagAndMobileClient(Tag.MOBILE_CLIENT, deviceSessionArg);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DeviceSessionArg getMobileClientValue() {
        if (this._tag == Tag.MOBILE_CLIENT) {
            return this.mobileClientValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.MOBILE_CLIENT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.webSessionValue, this.desktopClientValue, this.mobileClientValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof RevokeDeviceSessionArg)) {
            return false;
        }
        RevokeDeviceSessionArg revokeDeviceSessionArg = (RevokeDeviceSessionArg) obj;
        if (this._tag != revokeDeviceSessionArg._tag) {
            return false;
        }
        switch (this._tag) {
            case WEB_SESSION:
                DeviceSessionArg deviceSessionArg = this.webSessionValue;
                DeviceSessionArg deviceSessionArg2 = revokeDeviceSessionArg.webSessionValue;
                if (deviceSessionArg != deviceSessionArg2 && !deviceSessionArg.equals(deviceSessionArg2)) {
                    z = false;
                }
                return z;
            case DESKTOP_CLIENT:
                RevokeDesktopClientArg revokeDesktopClientArg = this.desktopClientValue;
                RevokeDesktopClientArg revokeDesktopClientArg2 = revokeDeviceSessionArg.desktopClientValue;
                if (revokeDesktopClientArg != revokeDesktopClientArg2 && !revokeDesktopClientArg.equals(revokeDesktopClientArg2)) {
                    z = false;
                }
                return z;
            case MOBILE_CLIENT:
                DeviceSessionArg deviceSessionArg3 = this.mobileClientValue;
                DeviceSessionArg deviceSessionArg4 = revokeDeviceSessionArg.mobileClientValue;
                if (deviceSessionArg3 != deviceSessionArg4 && !deviceSessionArg3.equals(deviceSessionArg4)) {
                    z = false;
                }
                return z;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
