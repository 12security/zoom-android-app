package com.google.api.client.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.util.Data;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class BearerToken {
    static final Pattern INVALID_TOKEN_ERROR = Pattern.compile("\\s*error\\s*=\\s*\"?invalid_token\"?");
    static final String PARAM_NAME = "access_token";

    static final class AuthorizationHeaderAccessMethod implements AccessMethod {
        static final String HEADER_PREFIX = "Bearer ";

        AuthorizationHeaderAccessMethod() {
        }

        public void intercept(HttpRequest httpRequest, String str) throws IOException {
            HttpHeaders headers = httpRequest.getHeaders();
            StringBuilder sb = new StringBuilder();
            sb.append(HEADER_PREFIX);
            sb.append(str);
            headers.setAuthorization(sb.toString());
        }

        public String getAccessTokenFromRequest(HttpRequest httpRequest) {
            List<String> authorizationAsList = httpRequest.getHeaders().getAuthorizationAsList();
            if (authorizationAsList != null) {
                for (String str : authorizationAsList) {
                    if (str.startsWith(HEADER_PREFIX)) {
                        return str.substring(7);
                    }
                }
            }
            return null;
        }
    }

    static final class FormEncodedBodyAccessMethod implements AccessMethod {
        FormEncodedBodyAccessMethod() {
        }

        public void intercept(HttpRequest httpRequest, String str) throws IOException {
            Preconditions.checkArgument(!"GET".equals(httpRequest.getRequestMethod()), "HTTP GET method is not supported");
            getData(httpRequest).put("access_token", str);
        }

        public String getAccessTokenFromRequest(HttpRequest httpRequest) {
            Object obj = getData(httpRequest).get("access_token");
            if (obj == null) {
                return null;
            }
            return obj.toString();
        }

        private static Map<String, Object> getData(HttpRequest httpRequest) {
            return Data.mapOf(UrlEncodedContent.getContent(httpRequest).getData());
        }
    }

    static final class QueryParameterAccessMethod implements AccessMethod {
        QueryParameterAccessMethod() {
        }

        public void intercept(HttpRequest httpRequest, String str) throws IOException {
            httpRequest.getUrl().set("access_token", (Object) str);
        }

        public String getAccessTokenFromRequest(HttpRequest httpRequest) {
            Object obj = httpRequest.getUrl().get("access_token");
            if (obj == null) {
                return null;
            }
            return obj.toString();
        }
    }

    public static AccessMethod authorizationHeaderAccessMethod() {
        return new AuthorizationHeaderAccessMethod();
    }

    public static AccessMethod formEncodedBodyAccessMethod() {
        return new FormEncodedBodyAccessMethod();
    }

    public static AccessMethod queryParameterAccessMethod() {
        return new QueryParameterAccessMethod();
    }
}
