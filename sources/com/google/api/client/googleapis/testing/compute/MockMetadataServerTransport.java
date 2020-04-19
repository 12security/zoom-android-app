package com.google.api.client.googleapis.testing.compute;

import com.google.api.client.googleapis.auth.oauth2.OAuth2Utils;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.util.Beta;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import java.io.IOException;

@Beta
public class MockMetadataServerTransport extends MockHttpTransport {
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String METADATA_SERVER_URL = OAuth2Utils.getMetadataServerUrl();
    private static final String METADATA_TOKEN_SERVER_URL;
    String accessToken;
    Integer tokenRequestStatusCode;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(METADATA_SERVER_URL);
        sb.append("/computeMetadata/v1/instance/service-accounts/default/token");
        METADATA_TOKEN_SERVER_URL = sb.toString();
    }

    public MockMetadataServerTransport(String str) {
        this.accessToken = str;
    }

    public void setTokenRequestStatusCode(Integer num) {
        this.tokenRequestStatusCode = num;
    }

    public LowLevelHttpRequest buildRequest(String str, String str2) throws IOException {
        if (str2.equals(METADATA_TOKEN_SERVER_URL)) {
            return new MockLowLevelHttpRequest(str2) {
                public LowLevelHttpResponse execute() throws IOException {
                    if (MockMetadataServerTransport.this.tokenRequestStatusCode != null) {
                        return new MockLowLevelHttpResponse().setStatusCode(MockMetadataServerTransport.this.tokenRequestStatusCode.intValue()).setContent("Token Fetch Error");
                    }
                    if ("Google".equals(getFirstHeaderValue("Metadata-Flavor"))) {
                        GenericJson genericJson = new GenericJson();
                        genericJson.setFactory(MockMetadataServerTransport.JSON_FACTORY);
                        genericJson.put("access_token", (Object) MockMetadataServerTransport.this.accessToken);
                        genericJson.put("expires_in", (Object) Integer.valueOf(3600000));
                        genericJson.put("token_type", (Object) AAD.BEARER);
                        return new MockLowLevelHttpResponse().setContentType(Json.MEDIA_TYPE).setContent(genericJson.toPrettyString());
                    }
                    throw new IOException("Metadata request header not found.");
                }
            };
        }
        if (str2.equals(METADATA_SERVER_URL)) {
            return new MockLowLevelHttpRequest(str2) {
                public LowLevelHttpResponse execute() {
                    MockLowLevelHttpResponse mockLowLevelHttpResponse = new MockLowLevelHttpResponse();
                    mockLowLevelHttpResponse.addHeader("Metadata-Flavor", "Google");
                    return mockLowLevelHttpResponse;
                }
            };
        }
        return super.buildRequest(str, str2);
    }
}
