package com.dropbox.core;

import com.dropbox.core.DbxRequestUtil.ResponseHandler;
import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.util.StringUtil;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DbxWebAuth {
    private static final int CSRF_BYTES_SIZE = 16;
    /* access modifiers changed from: private */
    public static final int CSRF_STRING_SIZE = StringUtil.urlSafeBase64Encode(new byte[16]).length();
    private static final SecureRandom RAND = new SecureRandom();
    public static final String ROLE_PERSONAL = "personal";
    public static final String ROLE_WORK = "work";
    private final DbxAppInfo appInfo;
    private final Request deprecatedRequest;
    private final DbxRequestConfig requestConfig;

    public static final class BadRequestException extends Exception {
        private static final long serialVersionUID = 0;

        public BadRequestException(String str) {
            super(str);
        }
    }

    public static final class BadStateException extends Exception {
        private static final long serialVersionUID = 0;

        public BadStateException(String str) {
            super(str);
        }
    }

    public static final class CsrfException extends Exception {
        private static final long serialVersionUID = 0;

        public CsrfException(String str) {
            super(str);
        }
    }

    public static abstract class Exception extends Exception {
        private static final long serialVersionUID = 0;

        public Exception(String str) {
            super(str);
        }
    }

    public static final class NotApprovedException extends Exception {
        private static final long serialVersionUID = 0;

        public NotApprovedException(String str) {
            super(str);
        }
    }

    public static final class ProviderException extends Exception {
        private static final long serialVersionUID = 0;

        public ProviderException(String str) {
            super(str);
        }
    }

    public static final class Request {
        private static final int MAX_STATE_SIZE = 500;
        /* access modifiers changed from: private */
        public static final Charset UTF8 = Charset.forName("UTF-8");
        /* access modifiers changed from: private */
        public final Boolean disableSignup;
        /* access modifiers changed from: private */
        public final Boolean forceReapprove;
        /* access modifiers changed from: private */
        public final String redirectUri;
        /* access modifiers changed from: private */
        public final String requireRole;
        /* access modifiers changed from: private */
        public final DbxSessionStore sessionStore;
        /* access modifiers changed from: private */
        public final String state;

        public static final class Builder {
            private Boolean disableSignup;
            private Boolean forceReapprove;
            private String redirectUri;
            private String requireRole;
            private DbxSessionStore sessionStore;
            private String state;

            private Builder() {
                this(null, null, null, null, null, null);
            }

            private Builder(String str, String str2, String str3, Boolean bool, Boolean bool2, DbxSessionStore dbxSessionStore) {
                this.redirectUri = str;
                this.state = str2;
                this.requireRole = str3;
                this.forceReapprove = bool;
                this.disableSignup = bool2;
                this.sessionStore = dbxSessionStore;
            }

            public Builder withNoRedirect() {
                this.redirectUri = null;
                this.sessionStore = null;
                return this;
            }

            public Builder withRedirectUri(String str, DbxSessionStore dbxSessionStore) {
                if (str == null) {
                    throw new NullPointerException("redirectUri");
                } else if (dbxSessionStore != null) {
                    this.redirectUri = str;
                    this.sessionStore = dbxSessionStore;
                    return this;
                } else {
                    throw new NullPointerException("sessionStore");
                }
            }

            public Builder withState(String str) {
                if (str == null || str.getBytes(Request.UTF8).length + DbxWebAuth.CSRF_STRING_SIZE <= 500) {
                    this.state = str;
                    return this;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("UTF-8 encoded state cannot be greater than ");
                sb.append(500 - DbxWebAuth.CSRF_STRING_SIZE);
                sb.append(" bytes.");
                throw new IllegalArgumentException(sb.toString());
            }

            public Builder withRequireRole(String str) {
                this.requireRole = str;
                return this;
            }

            public Builder withForceReapprove(Boolean bool) {
                this.forceReapprove = bool;
                return this;
            }

            public Builder withDisableSignup(Boolean bool) {
                this.disableSignup = bool;
                return this;
            }

            public Request build() {
                if (this.redirectUri != null || this.state == null) {
                    Request request = new Request(this.redirectUri, this.state, this.requireRole, this.forceReapprove, this.disableSignup, this.sessionStore);
                    return request;
                }
                throw new IllegalStateException("Cannot specify a state without a redirect URI.");
            }
        }

        private Request(String str, String str2, String str3, Boolean bool, Boolean bool2, DbxSessionStore dbxSessionStore) {
            this.redirectUri = str;
            this.state = str2;
            this.requireRole = str3;
            this.forceReapprove = bool;
            this.disableSignup = bool2;
            this.sessionStore = dbxSessionStore;
        }

        public Builder copy() {
            Builder builder = new Builder(this.redirectUri, this.state, this.requireRole, this.forceReapprove, this.disableSignup, this.sessionStore);
            return builder;
        }

        public static Builder newBuilder() {
            return new Builder();
        }
    }

    @Deprecated
    public DbxWebAuth(DbxRequestConfig dbxRequestConfig, DbxAppInfo dbxAppInfo, String str, DbxSessionStore dbxSessionStore) {
        if (dbxRequestConfig == null) {
            throw new NullPointerException("requestConfig");
        } else if (dbxAppInfo != null) {
            this.requestConfig = dbxRequestConfig;
            this.appInfo = dbxAppInfo;
            this.deprecatedRequest = newRequestBuilder().withRedirectUri(str, dbxSessionStore).build();
        } else {
            throw new NullPointerException("appInfo");
        }
    }

    public DbxWebAuth(DbxRequestConfig dbxRequestConfig, DbxAppInfo dbxAppInfo) {
        if (dbxRequestConfig == null) {
            throw new NullPointerException("requestConfig");
        } else if (dbxAppInfo != null) {
            this.requestConfig = dbxRequestConfig;
            this.appInfo = dbxAppInfo;
            this.deprecatedRequest = null;
        } else {
            throw new NullPointerException("appInfo");
        }
    }

    @Deprecated
    public String start(String str) {
        Request request = this.deprecatedRequest;
        if (request != null) {
            return authorizeImpl(request.copy().withState(str).build());
        }
        throw new IllegalStateException("Must use DbxWebAuth.authorize instead.");
    }

    public String authorize(Request request) {
        if (this.deprecatedRequest == null) {
            return authorizeImpl(request);
        }
        throw new IllegalStateException("Must create this instance using DbxWebAuth(DbxRequestConfig,DbxAppInfo) to call this method.");
    }

    private String authorizeImpl(Request request) {
        HashMap hashMap = new HashMap();
        hashMap.put("client_id", this.appInfo.getKey());
        hashMap.put("response_type", "code");
        if (request.redirectUri != null) {
            hashMap.put("redirect_uri", request.redirectUri);
            hashMap.put("state", appendCsrfToken(request));
        }
        if (request.requireRole != null) {
            hashMap.put("require_role", request.requireRole);
        }
        if (request.forceReapprove != null) {
            hashMap.put("force_reapprove", Boolean.toString(request.forceReapprove.booleanValue()).toLowerCase());
        }
        if (request.disableSignup != null) {
            hashMap.put("disable_signup", Boolean.toString(request.disableSignup.booleanValue()).toLowerCase());
        }
        return DbxRequestUtil.buildUrlWithParams(this.requestConfig.getUserLocale(), this.appInfo.getHost().getWeb(), "oauth2/authorize", DbxRequestUtil.toParamsArray(hashMap));
    }

    public DbxAuthFinish finishFromCode(String str) throws DbxException {
        return finish(str);
    }

    public DbxAuthFinish finishFromCode(String str, String str2) throws DbxException {
        return finish(str, str2, null);
    }

    public DbxAuthFinish finishFromRedirect(String str, DbxSessionStore dbxSessionStore, Map<String, String[]> map) throws DbxException, BadRequestException, BadStateException, CsrfException, NotApprovedException, ProviderException {
        String str2;
        if (str == null) {
            throw new NullPointerException("redirectUri");
        } else if (dbxSessionStore == null) {
            throw new NullPointerException("sessionStore");
        } else if (map != null) {
            String param = getParam(map, "state");
            if (param != null) {
                String param2 = getParam(map, "error");
                String param3 = getParam(map, "code");
                String param4 = getParam(map, "error_description");
                if (param3 == null && param2 == null) {
                    throw new BadRequestException("Missing both \"code\" and \"error\".");
                } else if (param3 != null && param2 != null) {
                    throw new BadRequestException("Both \"code\" and \"error\" are set.");
                } else if (param3 == null || param4 == null) {
                    String verifyAndStripCsrfToken = verifyAndStripCsrfToken(param, dbxSessionStore);
                    if (param2 == null) {
                        return finish(param3, str, verifyAndStripCsrfToken);
                    }
                    if (param2.equals(AAD.WEB_UI_CANCEL)) {
                        if (param4 == null) {
                            str2 = "No additional description from Dropbox";
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Additional description from Dropbox: ");
                            sb.append(param4);
                            str2 = sb.toString();
                        }
                        throw new NotApprovedException(str2);
                    }
                    if (param4 != null) {
                        param2 = String.format("%s: %s", new Object[]{param2, param4});
                    }
                    throw new ProviderException(param2);
                } else {
                    throw new BadRequestException("Both \"code\" and \"error_description\" are set.");
                }
            } else {
                throw new BadRequestException("Missing required parameter: \"state\".");
            }
        } else {
            throw new NullPointerException("params");
        }
    }

    private DbxAuthFinish finish(String str) throws DbxException {
        return finish(str, null, null);
    }

    private DbxAuthFinish finish(String str, String str2, final String str3) throws DbxException {
        if (str != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("grant_type", OAuth2.AUTHORIZATION_CODE);
            hashMap.put("code", str);
            hashMap.put(OAuth.LOCALE, this.requestConfig.getUserLocale());
            if (str2 != null) {
                hashMap.put("redirect_uri", str2);
            }
            ArrayList arrayList = new ArrayList();
            DbxRequestUtil.addBasicAuthHeader(arrayList, this.appInfo.getKey(), this.appInfo.getSecret());
            return (DbxAuthFinish) DbxRequestUtil.doPostNoAuth(this.requestConfig, DbxRawClientV2.USER_AGENT_ID, this.appInfo.getHost().getApi(), "oauth2/token", DbxRequestUtil.toParamsArray(hashMap), arrayList, new ResponseHandler<DbxAuthFinish>() {
                public DbxAuthFinish handle(Response response) throws DbxException {
                    if (response.getStatusCode() == 200) {
                        return ((DbxAuthFinish) DbxRequestUtil.readJsonFromResponse(DbxAuthFinish.Reader, response)).withUrlState(str3);
                    }
                    throw DbxRequestUtil.unexpectedStatus(response);
                }
            });
        }
        throw new NullPointerException("code");
    }

    @Deprecated
    public DbxAuthFinish finish(Map<String, String[]> map) throws DbxException, BadRequestException, BadStateException, CsrfException, NotApprovedException, ProviderException {
        Request request = this.deprecatedRequest;
        if (request != null) {
            return finishFromRedirect(request.redirectUri, this.deprecatedRequest.sessionStore, map);
        }
        throw new IllegalStateException("Must use DbxWebAuth.finishFromRedirect(..) instead.");
    }

    private static String appendCsrfToken(Request request) {
        byte[] bArr = new byte[16];
        RAND.nextBytes(bArr);
        String urlSafeBase64Encode = StringUtil.urlSafeBase64Encode(bArr);
        if (urlSafeBase64Encode.length() == CSRF_STRING_SIZE) {
            if (request.sessionStore != null) {
                request.sessionStore.set(urlSafeBase64Encode);
            }
            if (request.state == null) {
                return urlSafeBase64Encode;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(urlSafeBase64Encode);
            sb.append(request.state);
            String sb2 = sb.toString();
            if (sb2.length() <= 500) {
                return sb2;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("unexpected combined state length: ");
            sb3.append(sb2.length());
            throw new AssertionError(sb3.toString());
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("unexpected CSRF token length: ");
        sb4.append(urlSafeBase64Encode.length());
        throw new AssertionError(sb4.toString());
    }

    private static String verifyAndStripCsrfToken(String str, DbxSessionStore dbxSessionStore) throws CsrfException, BadStateException {
        String str2 = dbxSessionStore.get();
        if (str2 == null) {
            throw new BadStateException("No CSRF Token loaded from session store.");
        } else if (str2.length() >= CSRF_STRING_SIZE) {
            int length = str.length();
            int i = CSRF_STRING_SIZE;
            if (length >= i) {
                String substring = str.substring(0, i);
                if (StringUtil.secureStringEquals(str2, substring)) {
                    String substring2 = str.substring(CSRF_STRING_SIZE, str.length());
                    dbxSessionStore.clear();
                    if (substring2.isEmpty()) {
                        return null;
                    }
                    return substring2;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("expecting ");
                sb.append(StringUtil.m33jq(str2));
                sb.append(", got ");
                sb.append(StringUtil.m33jq(substring));
                throw new CsrfException(sb.toString());
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Token too small: ");
            sb2.append(str);
            throw new CsrfException(sb2.toString());
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Token retrieved from session store is too small: ");
            sb3.append(str2);
            throw new BadStateException(sb3.toString());
        }
    }

    private static String getParam(Map<String, String[]> map, String str) throws BadRequestException {
        String[] strArr = (String[]) map.get(str);
        if (strArr == null) {
            return null;
        }
        if (strArr.length == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Parameter \"");
            sb.append(str);
            sb.append("\" missing value.");
            throw new IllegalArgumentException(sb.toString());
        } else if (strArr.length == 1) {
            return strArr[0];
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("multiple occurrences of \"");
            sb2.append(str);
            sb2.append("\" parameter");
            throw new BadRequestException(sb2.toString());
        }
    }

    public static Builder newRequestBuilder() {
        return Request.newBuilder();
    }
}
