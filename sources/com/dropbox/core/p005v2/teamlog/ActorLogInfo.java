package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.ActorLogInfo */
public final class ActorLogInfo {
    public static final ActorLogInfo ANONYMOUS = new ActorLogInfo().withTag(Tag.ANONYMOUS);
    public static final ActorLogInfo DROPBOX = new ActorLogInfo().withTag(Tag.DROPBOX);
    public static final ActorLogInfo OTHER = new ActorLogInfo().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public UserLogInfo adminValue;
    /* access modifiers changed from: private */
    public AppLogInfo appValue;
    /* access modifiers changed from: private */
    public ResellerLogInfo resellerValue;
    /* access modifiers changed from: private */
    public UserLogInfo userValue;

    /* renamed from: com.dropbox.core.v2.teamlog.ActorLogInfo$Serializer */
    static class Serializer extends UnionSerializer<ActorLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ActorLogInfo actorLogInfo, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (actorLogInfo.tag()) {
                case USER:
                    jsonGenerator.writeStartObject();
                    writeTag("user", jsonGenerator);
                    jsonGenerator.writeFieldName("user");
                    Serializer.INSTANCE.serialize(actorLogInfo.userValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case ADMIN:
                    jsonGenerator.writeStartObject();
                    writeTag("admin", jsonGenerator);
                    jsonGenerator.writeFieldName("admin");
                    Serializer.INSTANCE.serialize(actorLogInfo.adminValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case APP:
                    jsonGenerator.writeStartObject();
                    writeTag("app", jsonGenerator);
                    jsonGenerator.writeFieldName("app");
                    Serializer.INSTANCE.serialize(actorLogInfo.appValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case RESELLER:
                    jsonGenerator.writeStartObject();
                    writeTag("reseller", jsonGenerator);
                    Serializer.INSTANCE.serialize(actorLogInfo.resellerValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case DROPBOX:
                    jsonGenerator.writeString("dropbox");
                    return;
                case ANONYMOUS:
                    jsonGenerator.writeString("anonymous");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ActorLogInfo deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            ActorLogInfo actorLogInfo;
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
                if ("user".equals(str)) {
                    expectField("user", jsonParser);
                    actorLogInfo = ActorLogInfo.user((UserLogInfo) Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("admin".equals(str)) {
                    expectField("admin", jsonParser);
                    actorLogInfo = ActorLogInfo.admin((UserLogInfo) Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("app".equals(str)) {
                    expectField("app", jsonParser);
                    actorLogInfo = ActorLogInfo.app((AppLogInfo) Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("reseller".equals(str)) {
                    actorLogInfo = ActorLogInfo.reseller(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("dropbox".equals(str)) {
                    actorLogInfo = ActorLogInfo.DROPBOX;
                } else if ("anonymous".equals(str)) {
                    actorLogInfo = ActorLogInfo.ANONYMOUS;
                } else {
                    actorLogInfo = ActorLogInfo.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return actorLogInfo;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.ActorLogInfo$Tag */
    public enum Tag {
        USER,
        ADMIN,
        APP,
        RESELLER,
        DROPBOX,
        ANONYMOUS,
        OTHER
    }

    private ActorLogInfo() {
    }

    private ActorLogInfo withTag(Tag tag) {
        ActorLogInfo actorLogInfo = new ActorLogInfo();
        actorLogInfo._tag = tag;
        return actorLogInfo;
    }

    private ActorLogInfo withTagAndUser(Tag tag, UserLogInfo userLogInfo) {
        ActorLogInfo actorLogInfo = new ActorLogInfo();
        actorLogInfo._tag = tag;
        actorLogInfo.userValue = userLogInfo;
        return actorLogInfo;
    }

    private ActorLogInfo withTagAndAdmin(Tag tag, UserLogInfo userLogInfo) {
        ActorLogInfo actorLogInfo = new ActorLogInfo();
        actorLogInfo._tag = tag;
        actorLogInfo.adminValue = userLogInfo;
        return actorLogInfo;
    }

    private ActorLogInfo withTagAndApp(Tag tag, AppLogInfo appLogInfo) {
        ActorLogInfo actorLogInfo = new ActorLogInfo();
        actorLogInfo._tag = tag;
        actorLogInfo.appValue = appLogInfo;
        return actorLogInfo;
    }

    private ActorLogInfo withTagAndReseller(Tag tag, ResellerLogInfo resellerLogInfo) {
        ActorLogInfo actorLogInfo = new ActorLogInfo();
        actorLogInfo._tag = tag;
        actorLogInfo.resellerValue = resellerLogInfo;
        return actorLogInfo;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUser() {
        return this._tag == Tag.USER;
    }

    public static ActorLogInfo user(UserLogInfo userLogInfo) {
        if (userLogInfo != null) {
            return new ActorLogInfo().withTagAndUser(Tag.USER, userLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UserLogInfo getUserValue() {
        if (this._tag == Tag.USER) {
            return this.userValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAdmin() {
        return this._tag == Tag.ADMIN;
    }

    public static ActorLogInfo admin(UserLogInfo userLogInfo) {
        if (userLogInfo != null) {
            return new ActorLogInfo().withTagAndAdmin(Tag.ADMIN, userLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UserLogInfo getAdminValue() {
        if (this._tag == Tag.ADMIN) {
            return this.adminValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ADMIN, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isApp() {
        return this._tag == Tag.APP;
    }

    public static ActorLogInfo app(AppLogInfo appLogInfo) {
        if (appLogInfo != null) {
            return new ActorLogInfo().withTagAndApp(Tag.APP, appLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public AppLogInfo getAppValue() {
        if (this._tag == Tag.APP) {
            return this.appValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.APP, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isReseller() {
        return this._tag == Tag.RESELLER;
    }

    public static ActorLogInfo reseller(ResellerLogInfo resellerLogInfo) {
        if (resellerLogInfo != null) {
            return new ActorLogInfo().withTagAndReseller(Tag.RESELLER, resellerLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public ResellerLogInfo getResellerValue() {
        if (this._tag == Tag.RESELLER) {
            return this.resellerValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.RESELLER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isDropbox() {
        return this._tag == Tag.DROPBOX;
    }

    public boolean isAnonymous() {
        return this._tag == Tag.ANONYMOUS;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.userValue, this.adminValue, this.appValue, this.resellerValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ActorLogInfo)) {
            return false;
        }
        ActorLogInfo actorLogInfo = (ActorLogInfo) obj;
        if (this._tag != actorLogInfo._tag) {
            return false;
        }
        switch (this._tag) {
            case USER:
                UserLogInfo userLogInfo = this.userValue;
                UserLogInfo userLogInfo2 = actorLogInfo.userValue;
                if (userLogInfo != userLogInfo2 && !userLogInfo.equals(userLogInfo2)) {
                    z = false;
                }
                return z;
            case ADMIN:
                UserLogInfo userLogInfo3 = this.adminValue;
                UserLogInfo userLogInfo4 = actorLogInfo.adminValue;
                if (userLogInfo3 != userLogInfo4 && !userLogInfo3.equals(userLogInfo4)) {
                    z = false;
                }
                return z;
            case APP:
                AppLogInfo appLogInfo = this.appValue;
                AppLogInfo appLogInfo2 = actorLogInfo.appValue;
                if (appLogInfo != appLogInfo2 && !appLogInfo.equals(appLogInfo2)) {
                    z = false;
                }
                return z;
            case RESELLER:
                ResellerLogInfo resellerLogInfo = this.resellerValue;
                ResellerLogInfo resellerLogInfo2 = actorLogInfo.resellerValue;
                if (resellerLogInfo != resellerLogInfo2 && !resellerLogInfo.equals(resellerLogInfo2)) {
                    z = false;
                }
                return z;
            case DROPBOX:
                return true;
            case ANONYMOUS:
                return true;
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
