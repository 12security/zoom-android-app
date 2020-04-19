package com.microsoft.aad.adal;

import android.content.Context;
import android.util.Pair;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

final class APIEvent extends DefaultEvent {
    private static final String TAG = DefaultEvent.class.getSimpleName();
    private final String mEventName;

    APIEvent(String str) {
        setProperty("Microsoft.ADAL.event_name", str);
        this.mEventName = str;
    }

    APIEvent(String str, Context context, String str2) {
        this(str);
        setDefaults(context, str2);
    }

    /* access modifiers changed from: 0000 */
    public void setAuthority(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            setProperty("Microsoft.ADAL.authority", str);
            URL url = StringExtensions.getUrl(str);
            if (url != null) {
                if (UrlExtensions.isADFSAuthority(url)) {
                    setAuthorityType("adfs");
                } else {
                    setAuthorityType(Broker.ACCOUNT_INITIAL_NAME);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setAuthorityType(String str) {
        setProperty("Microsoft.ADAL.authority_type", str);
    }

    /* access modifiers changed from: 0000 */
    public void setIsDeprecated(boolean z) {
        setProperty("Microsoft.ADAL.is_deprecated", String.valueOf(z));
    }

    /* access modifiers changed from: 0000 */
    public void setValidationStatus(String str) {
        setProperty("Microsoft.ADAL.authority_validation_status", str);
    }

    /* access modifiers changed from: 0000 */
    public void setExtendedExpiresOnSetting(boolean z) {
        setProperty("Microsoft.ADAL.extended_expires_on_setting", String.valueOf(z));
    }

    /* access modifiers changed from: 0000 */
    public void setPromptBehavior(String str) {
        setProperty("Microsoft.ADAL.prompt_behavior", str);
    }

    /* access modifiers changed from: 0000 */
    public void setAPIId(String str) {
        setProperty("Microsoft.ADAL.api_id", str);
    }

    /* access modifiers changed from: 0000 */
    public String getEventName() {
        return this.mEventName;
    }

    /* access modifiers changed from: 0000 */
    public void setWasApiCallSuccessful(boolean z, Exception exc) {
        setProperty("Microsoft.ADAL.is_successful", String.valueOf(z));
        if (exc != null && (exc instanceof AuthenticationException)) {
            setProperty("Microsoft.ADAL.api_error_code", ((AuthenticationException) exc).getCode().toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public void stopTelemetryAndFlush() {
        Telemetry.getInstance().stopEvent(getTelemetryRequestId(), this, getEventName());
        Telemetry.getInstance().flush(getTelemetryRequestId());
    }

    /* access modifiers changed from: 0000 */
    public void setIdToken(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            try {
                IdToken idToken = new IdToken(str);
                UserInfo userInfo = new UserInfo(idToken);
                setProperty("Microsoft.ADAL.idp", idToken.getIdentityProvider());
                try {
                    setProperty("Microsoft.ADAL.tenant_id", StringExtensions.createHash(idToken.getTenantId()));
                    setProperty("Microsoft.ADAL.user_id", StringExtensions.createHash(userInfo.getDisplayableId()));
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException unused) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TAG);
                    sb.append(":setIdToken");
                    Logger.m234i(sb.toString(), "Skipping TENANT_ID and USER_ID", "");
                }
            } catch (AuthenticationException unused2) {
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setLoginHint(String str) {
        try {
            setProperty("Microsoft.ADAL.login_hint", StringExtensions.createHash(str));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":setLoginHint");
            Logger.m234i(sb.toString(), "Skipping telemetry for LOGIN_HINT", "");
        }
    }

    /* access modifiers changed from: 0000 */
    public void setServerErrorCode(String str) {
        if (str != null && !str.equals("0")) {
            setProperty("Microsoft.ADAL.server_error_code", str.trim());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setServerSubErrorCode(String str) {
        if (str != null && !str.equals("0")) {
            setProperty("Microsoft.ADAL.server_sub_error_code", str.trim());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setRefreshTokenAge(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            setProperty("Microsoft.ADAL.rt_age", str.trim());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setSpeRing(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            setProperty("Microsoft.ADAL.spe_info", str.trim());
        }
    }

    public void processEvent(Map<String, String> map) {
        super.processEvent(map);
        for (Pair pair : getEventList()) {
            String str = (String) pair.first;
            if (str.equals("Microsoft.ADAL.authority_type") || str.equals("Microsoft.ADAL.is_deprecated") || str.equals("Microsoft.ADAL.authority_validation_status") || str.equals("Microsoft.ADAL.extended_expires_on_setting") || str.equals("Microsoft.ADAL.prompt_behavior") || str.equals("Microsoft.ADAL.is_successful") || str.equals("Microsoft.ADAL.idp") || str.equals("Microsoft.ADAL.tenant_id") || str.equals("Microsoft.ADAL.user_id") || str.equals("Microsoft.ADAL.login_hint") || str.equals("Microsoft.ADAL.response_time") || str.equals("Microsoft.ADAL.correlation_id") || str.equals("Microsoft.ADAL.request_id") || str.equals("Microsoft.ADAL.api_id") || str.equals("Microsoft.ADAL.api_error_code") || str.equals("Microsoft.ADAL.server_error_code") || str.equals("Microsoft.ADAL.server_sub_error_code") || str.equals("Microsoft.ADAL.rt_age") || str.equals("Microsoft.ADAL.spe_info")) {
                map.put(str, pair.second);
            }
        }
    }
}
