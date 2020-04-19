package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.LinkedDeviceLogInfo */
public final class LinkedDeviceLogInfo {
    public static final LinkedDeviceLogInfo OTHER = new LinkedDeviceLogInfo().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public DesktopDeviceSessionLogInfo desktopDeviceSessionValue;
    /* access modifiers changed from: private */
    public LegacyDeviceSessionLogInfo legacyDeviceSessionValue;
    /* access modifiers changed from: private */
    public MobileDeviceSessionLogInfo mobileDeviceSessionValue;
    /* access modifiers changed from: private */
    public WebDeviceSessionLogInfo webDeviceSessionValue;

    /* renamed from: com.dropbox.core.v2.teamlog.LinkedDeviceLogInfo$Serializer */
    static class Serializer extends UnionSerializer<LinkedDeviceLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(LinkedDeviceLogInfo linkedDeviceLogInfo, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (linkedDeviceLogInfo.tag()) {
                case MOBILE_DEVICE_SESSION:
                    jsonGenerator.writeStartObject();
                    writeTag("mobile_device_session", jsonGenerator);
                    Serializer.INSTANCE.serialize(linkedDeviceLogInfo.mobileDeviceSessionValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case DESKTOP_DEVICE_SESSION:
                    jsonGenerator.writeStartObject();
                    writeTag("desktop_device_session", jsonGenerator);
                    Serializer.INSTANCE.serialize(linkedDeviceLogInfo.desktopDeviceSessionValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case WEB_DEVICE_SESSION:
                    jsonGenerator.writeStartObject();
                    writeTag("web_device_session", jsonGenerator);
                    Serializer.INSTANCE.serialize(linkedDeviceLogInfo.webDeviceSessionValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case LEGACY_DEVICE_SESSION:
                    jsonGenerator.writeStartObject();
                    writeTag("legacy_device_session", jsonGenerator);
                    Serializer.INSTANCE.serialize(linkedDeviceLogInfo.legacyDeviceSessionValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public LinkedDeviceLogInfo deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            LinkedDeviceLogInfo linkedDeviceLogInfo;
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
                if ("mobile_device_session".equals(str)) {
                    linkedDeviceLogInfo = LinkedDeviceLogInfo.mobileDeviceSession(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("desktop_device_session".equals(str)) {
                    linkedDeviceLogInfo = LinkedDeviceLogInfo.desktopDeviceSession(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("web_device_session".equals(str)) {
                    linkedDeviceLogInfo = LinkedDeviceLogInfo.webDeviceSession(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("legacy_device_session".equals(str)) {
                    linkedDeviceLogInfo = LinkedDeviceLogInfo.legacyDeviceSession(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    linkedDeviceLogInfo = LinkedDeviceLogInfo.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return linkedDeviceLogInfo;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.LinkedDeviceLogInfo$Tag */
    public enum Tag {
        MOBILE_DEVICE_SESSION,
        DESKTOP_DEVICE_SESSION,
        WEB_DEVICE_SESSION,
        LEGACY_DEVICE_SESSION,
        OTHER
    }

    private LinkedDeviceLogInfo() {
    }

    private LinkedDeviceLogInfo withTag(Tag tag) {
        LinkedDeviceLogInfo linkedDeviceLogInfo = new LinkedDeviceLogInfo();
        linkedDeviceLogInfo._tag = tag;
        return linkedDeviceLogInfo;
    }

    private LinkedDeviceLogInfo withTagAndMobileDeviceSession(Tag tag, MobileDeviceSessionLogInfo mobileDeviceSessionLogInfo) {
        LinkedDeviceLogInfo linkedDeviceLogInfo = new LinkedDeviceLogInfo();
        linkedDeviceLogInfo._tag = tag;
        linkedDeviceLogInfo.mobileDeviceSessionValue = mobileDeviceSessionLogInfo;
        return linkedDeviceLogInfo;
    }

    private LinkedDeviceLogInfo withTagAndDesktopDeviceSession(Tag tag, DesktopDeviceSessionLogInfo desktopDeviceSessionLogInfo) {
        LinkedDeviceLogInfo linkedDeviceLogInfo = new LinkedDeviceLogInfo();
        linkedDeviceLogInfo._tag = tag;
        linkedDeviceLogInfo.desktopDeviceSessionValue = desktopDeviceSessionLogInfo;
        return linkedDeviceLogInfo;
    }

    private LinkedDeviceLogInfo withTagAndWebDeviceSession(Tag tag, WebDeviceSessionLogInfo webDeviceSessionLogInfo) {
        LinkedDeviceLogInfo linkedDeviceLogInfo = new LinkedDeviceLogInfo();
        linkedDeviceLogInfo._tag = tag;
        linkedDeviceLogInfo.webDeviceSessionValue = webDeviceSessionLogInfo;
        return linkedDeviceLogInfo;
    }

    private LinkedDeviceLogInfo withTagAndLegacyDeviceSession(Tag tag, LegacyDeviceSessionLogInfo legacyDeviceSessionLogInfo) {
        LinkedDeviceLogInfo linkedDeviceLogInfo = new LinkedDeviceLogInfo();
        linkedDeviceLogInfo._tag = tag;
        linkedDeviceLogInfo.legacyDeviceSessionValue = legacyDeviceSessionLogInfo;
        return linkedDeviceLogInfo;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isMobileDeviceSession() {
        return this._tag == Tag.MOBILE_DEVICE_SESSION;
    }

    public static LinkedDeviceLogInfo mobileDeviceSession(MobileDeviceSessionLogInfo mobileDeviceSessionLogInfo) {
        if (mobileDeviceSessionLogInfo != null) {
            return new LinkedDeviceLogInfo().withTagAndMobileDeviceSession(Tag.MOBILE_DEVICE_SESSION, mobileDeviceSessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public MobileDeviceSessionLogInfo getMobileDeviceSessionValue() {
        if (this._tag == Tag.MOBILE_DEVICE_SESSION) {
            return this.mobileDeviceSessionValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.MOBILE_DEVICE_SESSION, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isDesktopDeviceSession() {
        return this._tag == Tag.DESKTOP_DEVICE_SESSION;
    }

    public static LinkedDeviceLogInfo desktopDeviceSession(DesktopDeviceSessionLogInfo desktopDeviceSessionLogInfo) {
        if (desktopDeviceSessionLogInfo != null) {
            return new LinkedDeviceLogInfo().withTagAndDesktopDeviceSession(Tag.DESKTOP_DEVICE_SESSION, desktopDeviceSessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DesktopDeviceSessionLogInfo getDesktopDeviceSessionValue() {
        if (this._tag == Tag.DESKTOP_DEVICE_SESSION) {
            return this.desktopDeviceSessionValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.DESKTOP_DEVICE_SESSION, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isWebDeviceSession() {
        return this._tag == Tag.WEB_DEVICE_SESSION;
    }

    public static LinkedDeviceLogInfo webDeviceSession(WebDeviceSessionLogInfo webDeviceSessionLogInfo) {
        if (webDeviceSessionLogInfo != null) {
            return new LinkedDeviceLogInfo().withTagAndWebDeviceSession(Tag.WEB_DEVICE_SESSION, webDeviceSessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WebDeviceSessionLogInfo getWebDeviceSessionValue() {
        if (this._tag == Tag.WEB_DEVICE_SESSION) {
            return this.webDeviceSessionValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.WEB_DEVICE_SESSION, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isLegacyDeviceSession() {
        return this._tag == Tag.LEGACY_DEVICE_SESSION;
    }

    public static LinkedDeviceLogInfo legacyDeviceSession(LegacyDeviceSessionLogInfo legacyDeviceSessionLogInfo) {
        if (legacyDeviceSessionLogInfo != null) {
            return new LinkedDeviceLogInfo().withTagAndLegacyDeviceSession(Tag.LEGACY_DEVICE_SESSION, legacyDeviceSessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LegacyDeviceSessionLogInfo getLegacyDeviceSessionValue() {
        if (this._tag == Tag.LEGACY_DEVICE_SESSION) {
            return this.legacyDeviceSessionValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.LEGACY_DEVICE_SESSION, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.mobileDeviceSessionValue, this.desktopDeviceSessionValue, this.webDeviceSessionValue, this.legacyDeviceSessionValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof LinkedDeviceLogInfo)) {
            return false;
        }
        LinkedDeviceLogInfo linkedDeviceLogInfo = (LinkedDeviceLogInfo) obj;
        if (this._tag != linkedDeviceLogInfo._tag) {
            return false;
        }
        switch (this._tag) {
            case MOBILE_DEVICE_SESSION:
                MobileDeviceSessionLogInfo mobileDeviceSessionLogInfo = this.mobileDeviceSessionValue;
                MobileDeviceSessionLogInfo mobileDeviceSessionLogInfo2 = linkedDeviceLogInfo.mobileDeviceSessionValue;
                if (mobileDeviceSessionLogInfo != mobileDeviceSessionLogInfo2 && !mobileDeviceSessionLogInfo.equals(mobileDeviceSessionLogInfo2)) {
                    z = false;
                }
                return z;
            case DESKTOP_DEVICE_SESSION:
                DesktopDeviceSessionLogInfo desktopDeviceSessionLogInfo = this.desktopDeviceSessionValue;
                DesktopDeviceSessionLogInfo desktopDeviceSessionLogInfo2 = linkedDeviceLogInfo.desktopDeviceSessionValue;
                if (desktopDeviceSessionLogInfo != desktopDeviceSessionLogInfo2 && !desktopDeviceSessionLogInfo.equals(desktopDeviceSessionLogInfo2)) {
                    z = false;
                }
                return z;
            case WEB_DEVICE_SESSION:
                WebDeviceSessionLogInfo webDeviceSessionLogInfo = this.webDeviceSessionValue;
                WebDeviceSessionLogInfo webDeviceSessionLogInfo2 = linkedDeviceLogInfo.webDeviceSessionValue;
                if (webDeviceSessionLogInfo != webDeviceSessionLogInfo2 && !webDeviceSessionLogInfo.equals(webDeviceSessionLogInfo2)) {
                    z = false;
                }
                return z;
            case LEGACY_DEVICE_SESSION:
                LegacyDeviceSessionLogInfo legacyDeviceSessionLogInfo = this.legacyDeviceSessionValue;
                LegacyDeviceSessionLogInfo legacyDeviceSessionLogInfo2 = linkedDeviceLogInfo.legacyDeviceSessionValue;
                if (legacyDeviceSessionLogInfo != legacyDeviceSessionLogInfo2 && !legacyDeviceSessionLogInfo.equals(legacyDeviceSessionLogInfo2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
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
