package com.dropbox.core;

import com.dropbox.core.DbxRequestUtil.ResponseHandler;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.p004v1.DbxClientV1;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public final class DbxOAuth1Upgrader {
    public static final JsonReader<String> ResponseReader = new JsonReader<String>() {
        public String read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            String str = null;
            String str2 = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                JsonReader.nextToken(jsonParser);
                try {
                    if (currentName.equals("token_type")) {
                        str = (String) DbxAuthFinish.BearerTokenTypeReader.readField(jsonParser, currentName, str);
                    } else if (currentName.equals("access_token")) {
                        str2 = (String) DbxAuthFinish.AccessTokenReader.readField(jsonParser, currentName, str2);
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
            } else if (str2 != null) {
                return str2;
            } else {
                throw new JsonReadException("missing field \"access_token\"", expectObjectStart);
            }
        }
    };
    private final DbxAppInfo appInfo;
    private final DbxRequestConfig requestConfig;

    public DbxOAuth1Upgrader(DbxRequestConfig dbxRequestConfig, DbxAppInfo dbxAppInfo) {
        if (dbxRequestConfig == null) {
            throw new IllegalArgumentException("'requestConfig' is null");
        } else if (dbxAppInfo != null) {
            this.requestConfig = dbxRequestConfig;
            this.appInfo = dbxAppInfo;
        } else {
            throw new IllegalArgumentException("'appInfo' is null");
        }
    }

    public String createOAuth2AccessToken(DbxOAuth1AccessToken dbxOAuth1AccessToken) throws DbxException {
        if (dbxOAuth1AccessToken != null) {
            return (String) DbxRequestUtil.doPostNoAuth(this.requestConfig, DbxClientV1.USER_AGENT_ID, this.appInfo.getHost().getApi(), "1/oauth2/token_from_oauth1", null, getHeaders(dbxOAuth1AccessToken), new ResponseHandler<String>() {
                public String handle(Response response) throws DbxException {
                    if (response.getStatusCode() == 200) {
                        return (String) DbxRequestUtil.readJsonFromResponse(DbxOAuth1Upgrader.ResponseReader, response);
                    }
                    throw DbxRequestUtil.unexpectedStatus(response);
                }
            });
        }
        throw new IllegalArgumentException("'token' can't be null");
    }

    public void disableOAuth1AccessToken(DbxOAuth1AccessToken dbxOAuth1AccessToken) throws DbxException {
        if (dbxOAuth1AccessToken != null) {
            DbxRequestUtil.doPostNoAuth(this.requestConfig, DbxClientV1.USER_AGENT_ID, this.appInfo.getHost().getApi(), "1/disable_access_token", null, getHeaders(dbxOAuth1AccessToken), new ResponseHandler<Void>() {
                public Void handle(Response response) throws DbxException {
                    if (response.getStatusCode() == 200) {
                        return null;
                    }
                    throw DbxRequestUtil.unexpectedStatus(response);
                }
            });
            return;
        }
        throw new IllegalArgumentException("'token' can't be null");
    }

    private ArrayList<Header> getHeaders(DbxOAuth1AccessToken dbxOAuth1AccessToken) {
        ArrayList<Header> arrayList = new ArrayList<>(1);
        arrayList.add(new Header("Authorization", buildOAuth1Header(dbxOAuth1AccessToken)));
        return arrayList;
    }

    private String buildOAuth1Header(DbxOAuth1AccessToken dbxOAuth1AccessToken) {
        StringBuilder sb = new StringBuilder();
        sb.append("OAuth oauth_version=\"1.0\", oauth_signature_method=\"PLAINTEXT\"");
        sb.append(", oauth_consumer_key=\"");
        sb.append(encode(this.appInfo.getKey()));
        sb.append("\"");
        sb.append(", oauth_token=\"");
        sb.append(encode(dbxOAuth1AccessToken.getKey()));
        sb.append("\"");
        sb.append(", oauth_signature=\"");
        sb.append(encode(this.appInfo.getSecret()));
        sb.append("&");
        sb.append(encode(dbxOAuth1AccessToken.getSecret()));
        sb.append("\"");
        return sb.toString();
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw LangUtil.mkAssert("UTF-8 should always be supported", e);
        }
    }
}
