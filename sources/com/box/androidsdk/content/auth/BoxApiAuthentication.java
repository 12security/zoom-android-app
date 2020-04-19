package com.box.androidsdk.content.auth;

import com.box.androidsdk.content.BoxApi;
import com.box.androidsdk.content.BoxConstants;
import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.auth.BoxAuthentication.BoxAuthenticationInfo;
import com.box.androidsdk.content.models.BoxMDMData;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.ContentTypes;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.box.androidsdk.content.utils.SdkUtils;
import java.util.Locale;

class BoxApiAuthentication extends BoxApi {
    static final String GRANT_TYPE = "grant_type";
    static final String GRANT_TYPE_AUTH_CODE = "authorization_code";
    static final String GRANT_TYPE_REFRESH = "refresh_token";
    static final String OAUTH_TOKEN_REQUEST_URL = "%s/oauth2/token";
    static final String OAUTH_TOKEN_REVOKE_URL = "%s/oauth2/revoke";
    static final String REFRESH_TOKEN = "refresh_token";
    static final String RESPONSE_TYPE_BASE_DOMAIN = "base_domain";
    static final String RESPONSE_TYPE_CODE = "code";
    static final String RESPONSE_TYPE_ERROR = "error";

    static class BoxCreateAuthRequest extends BoxRequest<BoxAuthenticationInfo, BoxCreateAuthRequest> {
        public BoxCreateAuthRequest(BoxSession boxSession, String str, String str2, String str3, String str4) {
            super(BoxAuthenticationInfo.class, str, boxSession);
            this.mRequestMethod = Methods.POST;
            setContentType(ContentTypes.URL_ENCODED);
            this.mBodyMap.put("grant_type", "authorization_code");
            this.mBodyMap.put("code", str2);
            this.mBodyMap.put("client_id", str3);
            this.mBodyMap.put(BoxConstants.KEY_CLIENT_SECRET, str4);
            if (boxSession.getDeviceId() != null) {
                setDevice(boxSession.getDeviceId(), boxSession.getDeviceName());
            }
            if (boxSession.getManagementData() != null) {
                setMdmData(boxSession.getManagementData());
            }
            if (boxSession.getRefreshTokenExpiresAt() != null) {
                setRefreshExpiresAt(boxSession.getRefreshTokenExpiresAt().longValue());
            }
        }

        public BoxCreateAuthRequest setMdmData(BoxMDMData boxMDMData) {
            if (boxMDMData != null) {
                this.mBodyMap.put(BoxMDMData.BOX_MDM_DATA, boxMDMData.toJson());
            }
            return this;
        }

        public BoxCreateAuthRequest setDevice(String str, String str2) {
            if (!SdkUtils.isEmptyString(str)) {
                this.mBodyMap.put(BoxConstants.KEY_BOX_DEVICE_ID, str);
            }
            if (!SdkUtils.isEmptyString(str2)) {
                this.mBodyMap.put(BoxConstants.KEY_BOX_DEVICE_NAME, str2);
            }
            return this;
        }

        public BoxCreateAuthRequest setRefreshExpiresAt(long j) {
            this.mBodyMap.put(BoxConstants.KEY_BOX_REFRESH_TOKEN_EXPIRES_AT, Long.toString(j));
            return this;
        }
    }

    static class BoxRefreshAuthRequest extends BoxRequest<BoxAuthenticationInfo, BoxRefreshAuthRequest> {
        public BoxRefreshAuthRequest(BoxSession boxSession, String str, String str2, String str3, String str4) {
            super(BoxAuthenticationInfo.class, str, boxSession);
            this.mContentType = ContentTypes.URL_ENCODED;
            this.mRequestMethod = Methods.POST;
            this.mBodyMap.put("grant_type", "refresh_token");
            this.mBodyMap.put("refresh_token", str2);
            this.mBodyMap.put("client_id", str3);
            this.mBodyMap.put(BoxConstants.KEY_CLIENT_SECRET, str4);
            if (boxSession.getDeviceId() != null) {
                setDevice(boxSession.getDeviceId(), boxSession.getDeviceName());
            }
            if (boxSession.getRefreshTokenExpiresAt() != null) {
                setRefreshExpiresAt(boxSession.getRefreshTokenExpiresAt().longValue());
            }
        }

        public BoxRefreshAuthRequest setDevice(String str, String str2) {
            if (!SdkUtils.isEmptyString(str)) {
                this.mBodyMap.put(BoxConstants.KEY_BOX_DEVICE_ID, str);
            }
            if (!SdkUtils.isEmptyString(str2)) {
                this.mBodyMap.put(BoxConstants.KEY_BOX_DEVICE_NAME, str2);
            }
            return this;
        }

        public BoxAuthenticationInfo send() throws BoxException {
            BoxAuthenticationInfo boxAuthenticationInfo = (BoxAuthenticationInfo) super.send();
            boxAuthenticationInfo.setUser(this.mSession.getUser());
            return boxAuthenticationInfo;
        }

        public BoxRefreshAuthRequest setRefreshExpiresAt(long j) {
            this.mBodyMap.put(BoxConstants.KEY_BOX_REFRESH_TOKEN_EXPIRES_AT, Long.toString(j));
            return this;
        }
    }

    static class BoxRevokeAuthRequest extends BoxRequest<BoxAuthenticationInfo, BoxRevokeAuthRequest> {
        public BoxRevokeAuthRequest(BoxSession boxSession, String str, String str2, String str3, String str4) {
            super(BoxAuthenticationInfo.class, str, boxSession);
            this.mRequestMethod = Methods.POST;
            setContentType(ContentTypes.URL_ENCODED);
            this.mBodyMap.put("client_id", str3);
            this.mBodyMap.put(BoxConstants.KEY_CLIENT_SECRET, str4);
            this.mBodyMap.put("token", str2);
        }
    }

    BoxApiAuthentication(BoxSession boxSession) {
        super(boxSession);
        this.mBaseUri = BoxConstants.OAUTH_BASE_URI;
    }

    /* access modifiers changed from: protected */
    public String getBaseUri() {
        if (this.mSession == null || this.mSession.getAuthInfo() == null || this.mSession.getAuthInfo().getBaseDomain() == null) {
            return super.getBaseUri();
        }
        return String.format(BoxConstants.OAUTH_BASE_URI_TEMPLATE, new Object[]{this.mSession.getAuthInfo().getBaseDomain()});
    }

    /* access modifiers changed from: 0000 */
    public BoxRefreshAuthRequest refreshOAuth(String str, String str2, String str3) {
        BoxRefreshAuthRequest boxRefreshAuthRequest = new BoxRefreshAuthRequest(this.mSession, getTokenUrl(), str, str2, str3);
        return boxRefreshAuthRequest;
    }

    /* access modifiers changed from: 0000 */
    public BoxCreateAuthRequest createOAuth(String str, String str2, String str3) {
        BoxCreateAuthRequest boxCreateAuthRequest = new BoxCreateAuthRequest(this.mSession, getTokenUrl(), str, str2, str3);
        return boxCreateAuthRequest;
    }

    /* access modifiers changed from: 0000 */
    public BoxRevokeAuthRequest revokeOAuth(String str, String str2, String str3) {
        BoxRevokeAuthRequest boxRevokeAuthRequest = new BoxRevokeAuthRequest(this.mSession, getTokenRevokeUrl(), str, str2, str3);
        return boxRevokeAuthRequest;
    }

    /* access modifiers changed from: protected */
    public String getTokenUrl() {
        return String.format(Locale.ENGLISH, OAUTH_TOKEN_REQUEST_URL, new Object[]{getBaseUri()});
    }

    /* access modifiers changed from: protected */
    public String getTokenRevokeUrl() {
        return String.format(Locale.ENGLISH, OAUTH_TOKEN_REVOKE_URL, new Object[]{getBaseUri()});
    }
}
