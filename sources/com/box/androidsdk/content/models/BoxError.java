package com.box.androidsdk.content.models;

import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class BoxError extends BoxJsonObject {
    public static final String FIELD_CODE = "code";
    public static final String FIELD_CONTEXT_INFO = "context_info";
    public static final String FIELD_ERROR = "error";
    public static final String FIELD_ERROR_DESCRIPTION = "error_description";
    public static final String FIELD_HELP_URL = "help_url";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_REQUEST_ID = "request_id";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_TYPE = "type";

    public static class ErrorContext extends BoxMapJsonObject {
        public static final String FIELD_CONFLICTS = "conflicts";

        /* access modifiers changed from: protected */
        public void parseJSONMember(Member member) {
            String name = member.getName();
            JsonValue value = member.getValue();
            if (name.equals(FIELD_CONFLICTS)) {
                ArrayList arrayList = new ArrayList();
                if (value.isArray()) {
                    Iterator it = value.asArray().iterator();
                    while (it.hasNext()) {
                        arrayList.add(BoxEntity.createEntityFromJson(((JsonValue) it.next()).asObject()));
                    }
                } else {
                    arrayList.add(BoxEntity.createEntityFromJson(value.asObject()));
                }
                this.mProperties.put(FIELD_CONFLICTS, arrayList);
                return;
            }
            super.parseJSONMember(member);
        }

        public ArrayList<BoxEntity> getConflicts() {
            return (ArrayList) this.mProperties.get(FIELD_CONFLICTS);
        }
    }

    public BoxError() {
    }

    public BoxError(Map<String, Object> map) {
        super(map);
    }

    public String getType() {
        return (String) this.mProperties.get("type");
    }

    public Integer getStatus() {
        return (Integer) this.mProperties.get("status");
    }

    public String getCode() {
        return (String) this.mProperties.get("code");
    }

    public ErrorContext getContextInfo() {
        return (ErrorContext) this.mProperties.get(FIELD_CONTEXT_INFO);
    }

    public String getFieldHelpUrl() {
        return (String) this.mProperties.get(FIELD_HELP_URL);
    }

    public String getMessage() {
        return (String) this.mProperties.get("message");
    }

    public String getRequestId() {
        return (String) this.mProperties.get(FIELD_REQUEST_ID);
    }

    public String getError() {
        return (String) this.mProperties.get("error");
    }

    public String getErrorDescription() {
        return (String) this.mProperties.get("error_description");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("type")) {
            this.mProperties.put("type", value.asString());
        } else if (name.equals("status")) {
            this.mProperties.put("status", Integer.valueOf(value.asInt()));
        } else if (name.equals("code")) {
            this.mProperties.put("code", value.asString());
        } else if (name.equals(FIELD_CONTEXT_INFO)) {
            ErrorContext errorContext = new ErrorContext();
            errorContext.createFromJson(value.asObject());
            this.mProperties.put(FIELD_CONTEXT_INFO, errorContext);
        } else if (name.equals(FIELD_HELP_URL)) {
            this.mProperties.put(FIELD_HELP_URL, value.asString());
        } else if (name.equals("message")) {
            this.mProperties.put("message", value.asString());
        } else if (name.equals(FIELD_REQUEST_ID)) {
            this.mProperties.put(FIELD_REQUEST_ID, value.asString());
        } else if (name.equals("error")) {
            this.mProperties.put("error", value.asString());
        } else if (name.equals("error_description")) {
            this.mProperties.put("error_description", value.asString());
        } else {
            super.parseJSONMember(member);
        }
    }
}
