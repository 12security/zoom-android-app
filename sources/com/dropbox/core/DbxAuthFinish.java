package com.dropbox.core;

import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.util.StringUtil;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import com.microsoft.aad.adal.AuthenticationParameters;
import java.io.IOException;

public final class DbxAuthFinish {
    public static final JsonReader<String> AccessTokenReader = new JsonReader<String>() {
        public String read(JsonParser jsonParser) throws IOException, JsonReadException {
            try {
                String text = jsonParser.getText();
                String tokenPartError = DbxAppInfo.getTokenPartError(text);
                if (tokenPartError == null) {
                    jsonParser.nextToken();
                    return text;
                }
                throw new JsonReadException(tokenPartError, jsonParser.getTokenLocation());
            } catch (JsonParseException e) {
                throw JsonReadException.fromJackson(e);
            }
        }
    };
    public static final JsonReader<String> BearerTokenTypeReader = new JsonReader<String>() {
        public String read(JsonParser jsonParser) throws IOException, JsonReadException {
            try {
                String text = jsonParser.getText();
                if (!text.equals(AAD.BEARER)) {
                    if (!text.equals(AuthenticationParameters.BEARER)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("expecting \"Bearer\": got ");
                        sb.append(StringUtil.m33jq(text));
                        throw new JsonReadException(sb.toString(), jsonParser.getTokenLocation());
                    }
                }
                jsonParser.nextToken();
                return text;
            } catch (JsonParseException e) {
                throw JsonReadException.fromJackson(e);
            }
        }
    };
    public static final JsonReader<DbxAuthFinish> Reader = new JsonReader<DbxAuthFinish>() {
        public DbxAuthFinish read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            String str = null;
            String str2 = null;
            String str3 = null;
            String str4 = null;
            String str5 = null;
            String str6 = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                JsonReader.nextToken(jsonParser);
                try {
                    if (currentName.equals("token_type")) {
                        str = (String) DbxAuthFinish.BearerTokenTypeReader.readField(jsonParser, currentName, str);
                    } else if (currentName.equals("access_token")) {
                        str2 = (String) DbxAuthFinish.AccessTokenReader.readField(jsonParser, currentName, str2);
                    } else if (currentName.equals("uid")) {
                        str3 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str3);
                    } else if (currentName.equals("account_id")) {
                        str4 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str4);
                    } else if (currentName.equals("team_id")) {
                        str5 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str5);
                    } else if (currentName.equals("state")) {
                        str6 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str6);
                    } else {
                        JsonReader.skipValue(jsonParser);
                    }
                } catch (JsonReadException e) {
                    throw e.addFieldContext(currentName);
                }
            }
            JsonReader.expectObjectEnd(jsonParser);
            if (str == null) {
                throw new JsonReadException("missing field \"token_type\"", expectObjectStart);
            } else if (str2 == null) {
                throw new JsonReadException("missing field \"access_token\"", expectObjectStart);
            } else if (str3 == null) {
                throw new JsonReadException("missing field \"uid\"", expectObjectStart);
            } else if (str4 == null && str5 == null) {
                throw new JsonReadException("missing field \"account_id\" and missing field \"team_id\"", expectObjectStart);
            } else {
                DbxAuthFinish dbxAuthFinish = new DbxAuthFinish(str2, str3, str4, str5, str6);
                return dbxAuthFinish;
            }
        }
    };
    private final String accessToken;
    private final String accountId;
    private final String teamId;
    private final String urlState;
    private final String userId;

    public DbxAuthFinish(String str, String str2, String str3, String str4, String str5) {
        this.accessToken = str;
        this.userId = str2;
        this.accountId = str3;
        this.teamId = str4;
        this.urlState = str5;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public String getUrlState() {
        return this.urlState;
    }

    /* access modifiers changed from: 0000 */
    public DbxAuthFinish withUrlState(String str) {
        if (this.urlState == null) {
            DbxAuthFinish dbxAuthFinish = new DbxAuthFinish(this.accessToken, this.userId, this.accountId, this.teamId, str);
            return dbxAuthFinish;
        }
        throw new IllegalStateException("Already have URL state.");
    }
}
