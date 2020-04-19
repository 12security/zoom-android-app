package com.microsoft.services.msa;

final class OAuth {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String AUTHENTICATION_TOKEN = "authentication_token";
    public static final String CLIENT_ID = "client_id";
    public static final String CODE = "code";
    public static final String DISPLAY = "display";
    public static final String ERROR = "error";
    public static final String ERROR_DESCRIPTION = "error_description";
    public static final String ERROR_URI = "error_uri";
    public static final String EXPIRES_IN = "expires_in";
    public static final String GRANT_TYPE = "grant_type";
    public static final String LOCALE = "locale";
    public static final String LOGIN_HINT = "login_hint";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String RESPONSE_TYPE = "response_type";
    public static final String SCOPE = "scope";
    public static final String SCOPE_DELIMITER = " ";
    public static final String STATE = "state";
    public static final String THEME = "theme";
    public static final String TOKEN_TYPE = "token_type";
    public static final String USER_NAME = "username";

    public enum DisplayType {
        ANDROID_PHONE,
        ANDROID_TABLET
    }

    public enum ErrorType {
        INVALID_CLIENT,
        INVALID_GRANT,
        INVALID_REQUEST,
        INVALID_SCOPE,
        UNAUTHORIZED_CLIENT,
        UNSUPPORTED_GRANT_TYPE
    }

    public enum GrantType {
        AUTHORIZATION_CODE,
        CLIENT_CREDENTIALS,
        PASSWORD,
        REFRESH_TOKEN
    }

    public enum ResponseType {
        CODE,
        TOKEN
    }

    public enum TokenType {
        BEARER
    }

    private OAuth() {
        throw new AssertionError(ErrorMessages.NON_INSTANTIABLE_CLASS);
    }
}
