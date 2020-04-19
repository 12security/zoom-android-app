package com.google.api.client.googleapis.testing.auth.oauth2;

import com.box.androidsdk.content.BoxConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.testing.TestUtils;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.util.Beta;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Beta
public class MockTokenServerTransport extends MockHttpTransport {
    static final String EXPECTED_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:jwt-bearer";
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    Map<String, String> clients;
    Map<String, String> refreshTokens;
    Map<String, String> serviceAccounts;
    final String tokenServerUrl;

    public MockTokenServerTransport() {
        this(GoogleOAuthConstants.TOKEN_SERVER_URL);
    }

    public MockTokenServerTransport(String str) {
        this.serviceAccounts = new HashMap();
        this.clients = new HashMap();
        this.refreshTokens = new HashMap();
        this.tokenServerUrl = str;
    }

    public void addServiceAccount(String str, String str2) {
        this.serviceAccounts.put(str, str2);
    }

    public void addClient(String str, String str2) {
        this.clients.put(str, str2);
    }

    public void addRefreshToken(String str, String str2) {
        this.refreshTokens.put(str, str2);
    }

    public LowLevelHttpRequest buildRequest(String str, String str2) throws IOException {
        return str2.equals(this.tokenServerUrl) ? new MockLowLevelHttpRequest(str2) {
            public LowLevelHttpResponse execute() throws IOException {
                String str;
                Map parseQuery = TestUtils.parseQuery(getContentAsString());
                String str2 = (String) parseQuery.get("client_id");
                if (str2 != null) {
                    if (MockTokenServerTransport.this.clients.containsKey(str2)) {
                        String str3 = (String) parseQuery.get(BoxConstants.KEY_CLIENT_SECRET);
                        String str4 = (String) MockTokenServerTransport.this.clients.get(str2);
                        if (str3 == null || !str3.equals(str4)) {
                            throw new IOException("Client secret not found.");
                        }
                        String str5 = (String) parseQuery.get("refresh_token");
                        if (MockTokenServerTransport.this.refreshTokens.containsKey(str5)) {
                            str = (String) MockTokenServerTransport.this.refreshTokens.get(str5);
                        } else {
                            throw new IOException("Refresh Token not found.");
                        }
                    } else {
                        throw new IOException("Client ID not found.");
                    }
                } else if (parseQuery.containsKey("grant_type")) {
                    if (MockTokenServerTransport.EXPECTED_GRANT_TYPE.equals((String) parseQuery.get("grant_type"))) {
                        JsonWebSignature parse = JsonWebSignature.parse(MockTokenServerTransport.JSON_FACTORY, (String) parseQuery.get("assertion"));
                        String issuer = parse.getPayload().getIssuer();
                        if (MockTokenServerTransport.this.serviceAccounts.containsKey(issuer)) {
                            String str6 = (String) MockTokenServerTransport.this.serviceAccounts.get(issuer);
                            String str7 = (String) parse.getPayload().get("scope");
                            if (str7 == null || str7.length() == 0) {
                                throw new IOException("Scopes not found.");
                            }
                            str = str6;
                        } else {
                            throw new IOException("Service Account Email not found as issuer.");
                        }
                    } else {
                        throw new IOException("Unexpected Grant Type.");
                    }
                } else {
                    throw new IOException("Unknown token type.");
                }
                GenericJson genericJson = new GenericJson();
                genericJson.setFactory(MockTokenServerTransport.JSON_FACTORY);
                genericJson.put("access_token", (Object) str);
                genericJson.put("expires_in", (Object) Integer.valueOf(3600000));
                genericJson.put("token_type", (Object) AAD.BEARER);
                return new MockLowLevelHttpResponse().setContentType(Json.MEDIA_TYPE).setContent(genericJson.toPrettyString());
            }
        } : super.buildRequest(str, str2);
    }
}
