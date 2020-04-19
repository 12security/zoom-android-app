package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxError;
import com.box.androidsdk.content.requests.BoxHttpResponse;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import java.net.UnknownHostException;

public class BoxException extends Exception {
    private static final long serialVersionUID = 1;
    private BoxHttpResponse boxHttpResponse;
    private String response;
    /* access modifiers changed from: private */
    public final int responseCode;

    public enum ErrorType {
        INVALID_GRANT_TOKEN_EXPIRED("invalid_grant", 400),
        INVALID_GRANT_INVALID_TOKEN("invalid_grant", 400),
        ACCESS_DENIED(AAD.WEB_UI_CANCEL, 403),
        INVALID_REQUEST("invalid_request", 400),
        INVALID_CLIENT("invalid_client", 400),
        PASSWORD_RESET_REQUIRED("password_reset_required", 400),
        TERMS_OF_SERVICE_REQUIRED("terms_of_service_required", 400),
        NO_CREDIT_CARD_TRIAL_ENDED("no_credit_card_trial_ended", 400),
        TEMPORARILY_UNAVAILABLE("temporarily_unavailable", BoxConstants.HTTP_STATUS_TOO_MANY_REQUESTS),
        SERVICE_BLOCKED("service_blocked", 400),
        UNAUTHORIZED_DEVICE("unauthorized_device", 400),
        GRACE_PERIOD_EXPIRED("grace_period_expired", 403),
        NETWORK_ERROR("bad_connection_network_error", 0),
        LOCATION_BLOCKED("access_from_location_blocked", 403),
        OTHER("", 0);
        
        private final int mStatusCode;
        private final String mValue;

        private ErrorType(String str, int i) {
            this.mValue = str;
            this.mStatusCode = i;
        }

        public static ErrorType fromErrorInfo(String str, int i) {
            ErrorType[] values;
            for (ErrorType errorType : values()) {
                if (errorType.mStatusCode == i && errorType.mValue.equals(str)) {
                    return errorType;
                }
            }
            return OTHER;
        }
    }

    public static class MaxAttemptsExceeded extends BoxException {
        private final int mTimesTried;

        public MaxAttemptsExceeded(String str, int i) {
            this(str, i, null);
        }

        public MaxAttemptsExceeded(String str, int i, BoxHttpResponse boxHttpResponse) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(i);
            super(sb.toString(), boxHttpResponse);
            this.mTimesTried = i;
        }

        public int getTimesTried() {
            return this.mTimesTried;
        }
    }

    public static class RateLimitAttemptsExceeded extends MaxAttemptsExceeded {
        public RateLimitAttemptsExceeded(String str, int i, BoxHttpResponse boxHttpResponse) {
            super(str, i, boxHttpResponse);
        }
    }

    public static class RefreshFailure extends BoxException {
        public RefreshFailure(BoxException boxException) {
            super(boxException.getMessage(), boxException.responseCode, boxException.getResponse(), boxException);
        }

        public boolean isErrorFatal() {
            ErrorType errorType = getErrorType();
            for (ErrorType errorType2 : new ErrorType[]{ErrorType.INVALID_GRANT_INVALID_TOKEN, ErrorType.INVALID_GRANT_TOKEN_EXPIRED, ErrorType.ACCESS_DENIED, ErrorType.NO_CREDIT_CARD_TRIAL_ENDED, ErrorType.SERVICE_BLOCKED, ErrorType.INVALID_CLIENT, ErrorType.UNAUTHORIZED_DEVICE, ErrorType.GRACE_PERIOD_EXPIRED, ErrorType.OTHER}) {
                if (errorType == errorType2) {
                    return true;
                }
            }
            return false;
        }
    }

    public BoxException(String str) {
        super(str);
        this.responseCode = 0;
        this.boxHttpResponse = null;
        this.response = null;
    }

    public BoxException(String str, BoxHttpResponse boxHttpResponse2) {
        super(str, null);
        this.boxHttpResponse = boxHttpResponse2;
        if (boxHttpResponse2 != null) {
            this.responseCode = boxHttpResponse2.getResponseCode();
        } else {
            this.responseCode = 0;
        }
        try {
            this.response = boxHttpResponse2.getStringBody();
        } catch (Exception unused) {
            this.response = null;
        }
    }

    public BoxException(String str, Throwable th) {
        super(str, th);
        this.responseCode = 0;
        this.response = null;
    }

    public BoxException(String str, int i, String str2, Throwable th) {
        super(str, th);
        this.responseCode = i;
        this.response = str2;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponse() {
        return this.response;
    }

    public BoxError getAsBoxError() {
        try {
            BoxError boxError = new BoxError();
            boxError.createFromJson(getResponse());
            return boxError;
        } catch (Exception unused) {
            return null;
        }
    }

    public ErrorType getErrorType() {
        if (getCause() instanceof UnknownHostException) {
            return ErrorType.NETWORK_ERROR;
        }
        BoxError asBoxError = getAsBoxError();
        return ErrorType.fromErrorInfo(asBoxError != null ? asBoxError.getError() : null, getResponseCode());
    }
}
