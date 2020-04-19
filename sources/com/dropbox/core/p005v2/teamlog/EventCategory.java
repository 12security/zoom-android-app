package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.kubi.KubiContract;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.EventCategory */
public enum EventCategory {
    APPS,
    COMMENTS,
    DEVICES,
    DOMAINS,
    FILE_OPERATIONS,
    FILE_REQUESTS,
    GROUPS,
    LOGINS,
    MEMBERS,
    PAPER,
    PASSWORDS,
    REPORTS,
    SHARING,
    SHOWCASE,
    SSO,
    TEAM_FOLDERS,
    TEAM_POLICIES,
    TEAM_PROFILE,
    TFA,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.EventCategory$Serializer */
    static class Serializer extends UnionSerializer<EventCategory> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(EventCategory eventCategory, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (eventCategory) {
                case APPS:
                    jsonGenerator.writeString("apps");
                    return;
                case COMMENTS:
                    jsonGenerator.writeString("comments");
                    return;
                case DEVICES:
                    jsonGenerator.writeString(KubiContract.EXTRA_DEVICES);
                    return;
                case DOMAINS:
                    jsonGenerator.writeString("domains");
                    return;
                case FILE_OPERATIONS:
                    jsonGenerator.writeString("file_operations");
                    return;
                case FILE_REQUESTS:
                    jsonGenerator.writeString("file_requests");
                    return;
                case GROUPS:
                    jsonGenerator.writeString("groups");
                    return;
                case LOGINS:
                    jsonGenerator.writeString("logins");
                    return;
                case MEMBERS:
                    jsonGenerator.writeString("members");
                    return;
                case PAPER:
                    jsonGenerator.writeString("paper");
                    return;
                case PASSWORDS:
                    jsonGenerator.writeString("passwords");
                    return;
                case REPORTS:
                    jsonGenerator.writeString("reports");
                    return;
                case SHARING:
                    jsonGenerator.writeString("sharing");
                    return;
                case SHOWCASE:
                    jsonGenerator.writeString("showcase");
                    return;
                case SSO:
                    jsonGenerator.writeString("sso");
                    return;
                case TEAM_FOLDERS:
                    jsonGenerator.writeString("team_folders");
                    return;
                case TEAM_POLICIES:
                    jsonGenerator.writeString("team_policies");
                    return;
                case TEAM_PROFILE:
                    jsonGenerator.writeString("team_profile");
                    return;
                case TFA:
                    jsonGenerator.writeString("tfa");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public EventCategory deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            EventCategory eventCategory;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("apps".equals(str)) {
                    eventCategory = EventCategory.APPS;
                } else if ("comments".equals(str)) {
                    eventCategory = EventCategory.COMMENTS;
                } else if (KubiContract.EXTRA_DEVICES.equals(str)) {
                    eventCategory = EventCategory.DEVICES;
                } else if ("domains".equals(str)) {
                    eventCategory = EventCategory.DOMAINS;
                } else if ("file_operations".equals(str)) {
                    eventCategory = EventCategory.FILE_OPERATIONS;
                } else if ("file_requests".equals(str)) {
                    eventCategory = EventCategory.FILE_REQUESTS;
                } else if ("groups".equals(str)) {
                    eventCategory = EventCategory.GROUPS;
                } else if ("logins".equals(str)) {
                    eventCategory = EventCategory.LOGINS;
                } else if ("members".equals(str)) {
                    eventCategory = EventCategory.MEMBERS;
                } else if ("paper".equals(str)) {
                    eventCategory = EventCategory.PAPER;
                } else if ("passwords".equals(str)) {
                    eventCategory = EventCategory.PASSWORDS;
                } else if ("reports".equals(str)) {
                    eventCategory = EventCategory.REPORTS;
                } else if ("sharing".equals(str)) {
                    eventCategory = EventCategory.SHARING;
                } else if ("showcase".equals(str)) {
                    eventCategory = EventCategory.SHOWCASE;
                } else if ("sso".equals(str)) {
                    eventCategory = EventCategory.SSO;
                } else if ("team_folders".equals(str)) {
                    eventCategory = EventCategory.TEAM_FOLDERS;
                } else if ("team_policies".equals(str)) {
                    eventCategory = EventCategory.TEAM_POLICIES;
                } else if ("team_profile".equals(str)) {
                    eventCategory = EventCategory.TEAM_PROFILE;
                } else if ("tfa".equals(str)) {
                    eventCategory = EventCategory.TFA;
                } else {
                    eventCategory = EventCategory.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return eventCategory;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
