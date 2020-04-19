package p021us.zoom.thirdparty.login.facebook;

/* renamed from: us.zoom.thirdparty.login.facebook.FacebookError */
public class FacebookError extends RuntimeException {
    private static final long serialVersionUID = 1;
    private int mErrorCode = 0;
    private String mErrorType;

    public FacebookError(String str) {
        super(str);
    }

    public FacebookError(String str, String str2, int i) {
        super(str);
        this.mErrorType = str2;
        this.mErrorCode = i;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public String getErrorType() {
        return this.mErrorType;
    }
}
