package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.AccessMethodLogInfo */
public final class AccessMethodLogInfo {
    public static final AccessMethodLogInfo OTHER = new AccessMethodLogInfo().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public WebSessionLogInfo adminConsoleValue;
    /* access modifiers changed from: private */
    public ApiSessionLogInfo apiValue;
    /* access modifiers changed from: private */
    public WebSessionLogInfo contentManagerValue;
    /* access modifiers changed from: private */
    public SessionLogInfo endUserValue;
    /* access modifiers changed from: private */
    public WebSessionLogInfo signInAsValue;

    /* renamed from: com.dropbox.core.v2.teamlog.AccessMethodLogInfo$Serializer */
    static class Serializer extends UnionSerializer<AccessMethodLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AccessMethodLogInfo accessMethodLogInfo, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (accessMethodLogInfo.tag()) {
                case END_USER:
                    jsonGenerator.writeStartObject();
                    writeTag("end_user", jsonGenerator);
                    jsonGenerator.writeFieldName("end_user");
                    Serializer.INSTANCE.serialize(accessMethodLogInfo.endUserValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case SIGN_IN_AS:
                    jsonGenerator.writeStartObject();
                    writeTag("sign_in_as", jsonGenerator);
                    Serializer.INSTANCE.serialize(accessMethodLogInfo.signInAsValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case CONTENT_MANAGER:
                    jsonGenerator.writeStartObject();
                    writeTag("content_manager", jsonGenerator);
                    Serializer.INSTANCE.serialize(accessMethodLogInfo.contentManagerValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case ADMIN_CONSOLE:
                    jsonGenerator.writeStartObject();
                    writeTag("admin_console", jsonGenerator);
                    Serializer.INSTANCE.serialize(accessMethodLogInfo.adminConsoleValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case API:
                    jsonGenerator.writeStartObject();
                    writeTag("api", jsonGenerator);
                    Serializer.INSTANCE.serialize(accessMethodLogInfo.apiValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AccessMethodLogInfo deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            AccessMethodLogInfo accessMethodLogInfo;
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
                if ("end_user".equals(str)) {
                    expectField("end_user", jsonParser);
                    accessMethodLogInfo = AccessMethodLogInfo.endUser((SessionLogInfo) Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("sign_in_as".equals(str)) {
                    accessMethodLogInfo = AccessMethodLogInfo.signInAs(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("content_manager".equals(str)) {
                    accessMethodLogInfo = AccessMethodLogInfo.contentManager(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("admin_console".equals(str)) {
                    accessMethodLogInfo = AccessMethodLogInfo.adminConsole(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("api".equals(str)) {
                    accessMethodLogInfo = AccessMethodLogInfo.api(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    accessMethodLogInfo = AccessMethodLogInfo.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return accessMethodLogInfo;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.AccessMethodLogInfo$Tag */
    public enum Tag {
        END_USER,
        SIGN_IN_AS,
        CONTENT_MANAGER,
        ADMIN_CONSOLE,
        API,
        OTHER
    }

    private AccessMethodLogInfo() {
    }

    private AccessMethodLogInfo withTag(Tag tag) {
        AccessMethodLogInfo accessMethodLogInfo = new AccessMethodLogInfo();
        accessMethodLogInfo._tag = tag;
        return accessMethodLogInfo;
    }

    private AccessMethodLogInfo withTagAndEndUser(Tag tag, SessionLogInfo sessionLogInfo) {
        AccessMethodLogInfo accessMethodLogInfo = new AccessMethodLogInfo();
        accessMethodLogInfo._tag = tag;
        accessMethodLogInfo.endUserValue = sessionLogInfo;
        return accessMethodLogInfo;
    }

    private AccessMethodLogInfo withTagAndSignInAs(Tag tag, WebSessionLogInfo webSessionLogInfo) {
        AccessMethodLogInfo accessMethodLogInfo = new AccessMethodLogInfo();
        accessMethodLogInfo._tag = tag;
        accessMethodLogInfo.signInAsValue = webSessionLogInfo;
        return accessMethodLogInfo;
    }

    private AccessMethodLogInfo withTagAndContentManager(Tag tag, WebSessionLogInfo webSessionLogInfo) {
        AccessMethodLogInfo accessMethodLogInfo = new AccessMethodLogInfo();
        accessMethodLogInfo._tag = tag;
        accessMethodLogInfo.contentManagerValue = webSessionLogInfo;
        return accessMethodLogInfo;
    }

    private AccessMethodLogInfo withTagAndAdminConsole(Tag tag, WebSessionLogInfo webSessionLogInfo) {
        AccessMethodLogInfo accessMethodLogInfo = new AccessMethodLogInfo();
        accessMethodLogInfo._tag = tag;
        accessMethodLogInfo.adminConsoleValue = webSessionLogInfo;
        return accessMethodLogInfo;
    }

    private AccessMethodLogInfo withTagAndApi(Tag tag, ApiSessionLogInfo apiSessionLogInfo) {
        AccessMethodLogInfo accessMethodLogInfo = new AccessMethodLogInfo();
        accessMethodLogInfo._tag = tag;
        accessMethodLogInfo.apiValue = apiSessionLogInfo;
        return accessMethodLogInfo;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isEndUser() {
        return this._tag == Tag.END_USER;
    }

    public static AccessMethodLogInfo endUser(SessionLogInfo sessionLogInfo) {
        if (sessionLogInfo != null) {
            return new AccessMethodLogInfo().withTagAndEndUser(Tag.END_USER, sessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SessionLogInfo getEndUserValue() {
        if (this._tag == Tag.END_USER) {
            return this.endUserValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.END_USER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isSignInAs() {
        return this._tag == Tag.SIGN_IN_AS;
    }

    public static AccessMethodLogInfo signInAs(WebSessionLogInfo webSessionLogInfo) {
        if (webSessionLogInfo != null) {
            return new AccessMethodLogInfo().withTagAndSignInAs(Tag.SIGN_IN_AS, webSessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WebSessionLogInfo getSignInAsValue() {
        if (this._tag == Tag.SIGN_IN_AS) {
            return this.signInAsValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SIGN_IN_AS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isContentManager() {
        return this._tag == Tag.CONTENT_MANAGER;
    }

    public static AccessMethodLogInfo contentManager(WebSessionLogInfo webSessionLogInfo) {
        if (webSessionLogInfo != null) {
            return new AccessMethodLogInfo().withTagAndContentManager(Tag.CONTENT_MANAGER, webSessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WebSessionLogInfo getContentManagerValue() {
        if (this._tag == Tag.CONTENT_MANAGER) {
            return this.contentManagerValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.CONTENT_MANAGER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAdminConsole() {
        return this._tag == Tag.ADMIN_CONSOLE;
    }

    public static AccessMethodLogInfo adminConsole(WebSessionLogInfo webSessionLogInfo) {
        if (webSessionLogInfo != null) {
            return new AccessMethodLogInfo().withTagAndAdminConsole(Tag.ADMIN_CONSOLE, webSessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WebSessionLogInfo getAdminConsoleValue() {
        if (this._tag == Tag.ADMIN_CONSOLE) {
            return this.adminConsoleValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ADMIN_CONSOLE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isApi() {
        return this._tag == Tag.API;
    }

    public static AccessMethodLogInfo api(ApiSessionLogInfo apiSessionLogInfo) {
        if (apiSessionLogInfo != null) {
            return new AccessMethodLogInfo().withTagAndApi(Tag.API, apiSessionLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public ApiSessionLogInfo getApiValue() {
        if (this._tag == Tag.API) {
            return this.apiValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.API, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.endUserValue, this.signInAsValue, this.contentManagerValue, this.adminConsoleValue, this.apiValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AccessMethodLogInfo)) {
            return false;
        }
        AccessMethodLogInfo accessMethodLogInfo = (AccessMethodLogInfo) obj;
        if (this._tag != accessMethodLogInfo._tag) {
            return false;
        }
        switch (this._tag) {
            case END_USER:
                SessionLogInfo sessionLogInfo = this.endUserValue;
                SessionLogInfo sessionLogInfo2 = accessMethodLogInfo.endUserValue;
                if (sessionLogInfo != sessionLogInfo2 && !sessionLogInfo.equals(sessionLogInfo2)) {
                    z = false;
                }
                return z;
            case SIGN_IN_AS:
                WebSessionLogInfo webSessionLogInfo = this.signInAsValue;
                WebSessionLogInfo webSessionLogInfo2 = accessMethodLogInfo.signInAsValue;
                if (webSessionLogInfo != webSessionLogInfo2 && !webSessionLogInfo.equals(webSessionLogInfo2)) {
                    z = false;
                }
                return z;
            case CONTENT_MANAGER:
                WebSessionLogInfo webSessionLogInfo3 = this.contentManagerValue;
                WebSessionLogInfo webSessionLogInfo4 = accessMethodLogInfo.contentManagerValue;
                if (webSessionLogInfo3 != webSessionLogInfo4 && !webSessionLogInfo3.equals(webSessionLogInfo4)) {
                    z = false;
                }
                return z;
            case ADMIN_CONSOLE:
                WebSessionLogInfo webSessionLogInfo5 = this.adminConsoleValue;
                WebSessionLogInfo webSessionLogInfo6 = accessMethodLogInfo.adminConsoleValue;
                if (webSessionLogInfo5 != webSessionLogInfo6 && !webSessionLogInfo5.equals(webSessionLogInfo6)) {
                    z = false;
                }
                return z;
            case API:
                ApiSessionLogInfo apiSessionLogInfo = this.apiValue;
                ApiSessionLogInfo apiSessionLogInfo2 = accessMethodLogInfo.apiValue;
                if (apiSessionLogInfo != apiSessionLogInfo2 && !apiSessionLogInfo.equals(apiSessionLogInfo2)) {
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
