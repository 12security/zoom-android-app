package com.box.androidsdk.content.auth;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.net.http.SslCertificate.DName;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.box.androidsdk.content.BoxConfig;
import com.box.androidsdk.content.auth.BoxAuthentication.BoxAuthenticationInfo;
import com.box.androidsdk.content.utils.SdkUtils;
import com.box.sdk.android.C0469R;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Formatter;

public class OAuthWebView extends WebView {
    private static final String STATE = "state";
    private static final String URL_QUERY_LOGIN = "box_login";
    private String mBoxAccountEmail;
    private String state;

    public static class AuthFailure {
        public static final int TYPE_URL_MISMATCH = 1;
        public static final int TYPE_USER_INTERACTION = 0;
        public static final int TYPE_WEB_ERROR = 2;
        public WebViewException mWebException;
        public String message;
        public int type;

        public AuthFailure(int i, String str) {
            this.type = i;
            this.message = str;
        }

        public AuthFailure(WebViewException webViewException) {
            this(2, null);
            this.mWebException = webViewException;
        }
    }

    private static class InvalidUrlException extends Exception {
        private static final long serialVersionUID = 1;

        private InvalidUrlException() {
        }
    }

    public static class OAuthWebViewClient extends WebViewClient {
        private static final int WEB_VIEW_TIMEOUT = 30000;
        private Handler mHandler = new Handler(Looper.getMainLooper());
        private OnPageFinishedListener mOnPageFinishedListener;
        private String mRedirectUrl;
        private WebViewTimeOutRunnable mTimeOutRunnable;
        /* access modifiers changed from: private */
        public WebEventListener mWebEventListener;
        /* access modifiers changed from: private */
        public boolean sslErrorDialogContinueButtonClicked;
        private String state;

        public interface WebEventListener {
            boolean onAuthFailure(AuthFailure authFailure);

            void onReceivedAuthCode(String str);

            void onReceivedAuthCode(String str, String str2);
        }

        class WebViewTimeOutRunnable implements Runnable {
            final String mFailingUrl;
            final WeakReference<WebView> mViewHolder;

            public WebViewTimeOutRunnable(WebView webView, String str) {
                this.mFailingUrl = str;
                this.mViewHolder = new WeakReference<>(webView);
            }

            public void run() {
                OAuthWebViewClient.this.onReceivedError((WebView) this.mViewHolder.get(), -8, "loading timed out", this.mFailingUrl);
            }
        }

        public OAuthWebViewClient(WebEventListener webEventListener, String str, String str2) {
            this.mWebEventListener = webEventListener;
            this.mRedirectUrl = str;
            this.state = str2;
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            try {
                Uri uRIfromURL = getURIfromURL(str);
                String valueFromURI = getValueFromURI(uRIfromURL, "code");
                if (!SdkUtils.isEmptyString(getValueFromURI(uRIfromURL, "error"))) {
                    this.mWebEventListener.onAuthFailure(new AuthFailure(0, null));
                } else if (!SdkUtils.isEmptyString(valueFromURI)) {
                    String valueFromURI2 = getValueFromURI(uRIfromURL, BoxAuthenticationInfo.FIELD_BASE_DOMAIN);
                    if (valueFromURI2 != null) {
                        this.mWebEventListener.onReceivedAuthCode(valueFromURI, valueFromURI2);
                    } else {
                        this.mWebEventListener.onReceivedAuthCode(valueFromURI);
                    }
                }
            } catch (InvalidUrlException unused) {
                this.mWebEventListener.onAuthFailure(new AuthFailure(1, null));
            }
            WebViewTimeOutRunnable webViewTimeOutRunnable = this.mTimeOutRunnable;
            if (webViewTimeOutRunnable != null) {
                this.mHandler.removeCallbacks(webViewTimeOutRunnable);
            }
            this.mTimeOutRunnable = new WebViewTimeOutRunnable(webView, str);
            this.mHandler.postDelayed(this.mTimeOutRunnable, 30000);
        }

        public void onPageFinished(WebView webView, String str) {
            WebViewTimeOutRunnable webViewTimeOutRunnable = this.mTimeOutRunnable;
            if (webViewTimeOutRunnable != null) {
                this.mHandler.removeCallbacks(webViewTimeOutRunnable);
            }
            super.onPageFinished(webView, str);
            OnPageFinishedListener onPageFinishedListener = this.mOnPageFinishedListener;
            if (onPageFinishedListener != null) {
                onPageFinishedListener.onPageFinished(webView, str);
            }
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            WebViewTimeOutRunnable webViewTimeOutRunnable = this.mTimeOutRunnable;
            if (webViewTimeOutRunnable != null) {
                this.mHandler.removeCallbacks(webViewTimeOutRunnable);
            }
            if (!this.mWebEventListener.onAuthFailure(new AuthFailure(new WebViewException(i, str, str2)))) {
                if (i != -8) {
                    if (i == -6 || i == -2) {
                        if (!SdkUtils.isInternetAvailable(webView.getContext())) {
                            String rawFile = SdkUtils.getRawFile(webView.getContext(), C0469R.raw.offline);
                            Formatter formatter = new Formatter();
                            formatter.format(rawFile, new Object[]{webView.getContext().getString(C0469R.string.boxsdk_no_offline_access), webView.getContext().getString(C0469R.string.boxsdk_no_offline_access_detail), webView.getContext().getString(C0469R.string.boxsdk_no_offline_access_todo)});
                            webView.loadData(formatter.toString(), "text/html", "UTF-8");
                            formatter.close();
                        }
                    }
                    super.onReceivedError(webView, i, str, str2);
                }
                String rawFile2 = SdkUtils.getRawFile(webView.getContext(), C0469R.raw.offline);
                Formatter formatter2 = new Formatter();
                formatter2.format(rawFile2, new Object[]{webView.getContext().getString(C0469R.string.boxsdk_unable_to_connect), webView.getContext().getString(C0469R.string.boxsdk_unable_to_connect_detail), webView.getContext().getString(C0469R.string.boxsdk_unable_to_connect_todo)});
                webView.loadData(formatter2.toString(), "text/html", "UTF-8");
                formatter2.close();
                super.onReceivedError(webView, i, str, str2);
            }
        }

        public void onReceivedHttpAuthRequest(WebView webView, final HttpAuthHandler httpAuthHandler, String str, String str2) {
            final View inflate = LayoutInflater.from(webView.getContext()).inflate(C0469R.layout.boxsdk_alert_dialog_text_entry, null);
            new Builder(webView.getContext()).setTitle(C0469R.string.boxsdk_alert_dialog_text_entry).setView(inflate).setPositiveButton(C0469R.string.boxsdk_alert_dialog_ok, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    httpAuthHandler.proceed(((EditText) inflate.findViewById(C0469R.C0471id.username_edit)).getText().toString(), ((EditText) inflate.findViewById(C0469R.C0471id.password_edit)).getText().toString());
                }
            }).setNegativeButton(C0469R.string.boxsdk_alert_dialog_cancel, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    httpAuthHandler.cancel();
                    OAuthWebViewClient.this.mWebEventListener.onAuthFailure(new AuthFailure(0, null));
                }
            }).create().show();
        }

        public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
            String str;
            WebViewTimeOutRunnable webViewTimeOutRunnable = this.mTimeOutRunnable;
            if (webViewTimeOutRunnable != null) {
                this.mHandler.removeCallbacks(webViewTimeOutRunnable);
            }
            Resources resources = webView.getContext().getResources();
            StringBuilder sb = new StringBuilder(resources.getString(C0469R.string.f42x8d89efdd));
            sb.append(OAuth.SCOPE_DELIMITER);
            switch (sslError.getPrimaryError()) {
                case 0:
                    str = resources.getString(C0469R.string.boxsdk_ssl_error_warning_NOT_YET_VALID);
                    break;
                case 1:
                    str = resources.getString(C0469R.string.boxsdk_ssl_error_warning_EXPIRED);
                    break;
                case 2:
                    str = resources.getString(C0469R.string.boxsdk_ssl_error_warning_ID_MISMATCH);
                    break;
                case 3:
                    str = resources.getString(C0469R.string.boxsdk_ssl_error_warning_UNTRUSTED);
                    break;
                case 4:
                    str = webView.getResources().getString(C0469R.string.boxsdk_ssl_error_warning_DATE_INVALID);
                    break;
                case 5:
                    str = resources.getString(C0469R.string.boxsdk_ssl_error_warning_INVALID);
                    break;
                default:
                    str = resources.getString(C0469R.string.boxsdk_ssl_error_warning_INVALID);
                    break;
            }
            sb.append(str);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(resources.getString(C0469R.string.boxsdk_ssl_should_not_proceed));
            this.sslErrorDialogContinueButtonClicked = false;
            Builder negativeButton = new Builder(webView.getContext()).setTitle(C0469R.string.boxsdk_Security_Warning).setMessage(sb.toString()).setIcon(C0469R.C0470drawable.boxsdk_dialog_warning).setNegativeButton(C0469R.string.boxsdk_Go_back, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    OAuthWebViewClient.this.sslErrorDialogContinueButtonClicked = true;
                    sslErrorHandler.cancel();
                    OAuthWebViewClient.this.mWebEventListener.onAuthFailure(new AuthFailure(0, null));
                }
            });
            if (BoxConfig.ALLOW_SSL_ERROR) {
                negativeButton.setNeutralButton(C0469R.string.boxsdk_ssl_error_details, null);
                negativeButton.setPositiveButton(C0469R.string.boxsdk_Continue, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OAuthWebViewClient.this.sslErrorDialogContinueButtonClicked = true;
                        sslErrorHandler.proceed();
                    }
                });
            }
            AlertDialog create = negativeButton.create();
            create.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    if (!OAuthWebViewClient.this.sslErrorDialogContinueButtonClicked) {
                        OAuthWebViewClient.this.mWebEventListener.onAuthFailure(new AuthFailure(0, null));
                    }
                }
            });
            create.show();
            if (BoxConfig.ALLOW_SSL_ERROR) {
                Button button = create.getButton(-3);
                if (button != null) {
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            OAuthWebViewClient.this.showCertDialog(webView.getContext(), sslError);
                        }
                    });
                }
            }
        }

        /* access modifiers changed from: protected */
        public void showCertDialog(Context context, SslError sslError) {
            new Builder(context).setTitle(C0469R.string.boxsdk_Security_Warning).setView(getCertErrorView(context, sslError.getCertificate())).create().show();
        }

        private View getCertErrorView(Context context, SslCertificate sslCertificate) {
            View inflate = LayoutInflater.from(context).inflate(C0469R.layout.ssl_certificate, null);
            DName issuedTo = sslCertificate.getIssuedTo();
            if (issuedTo != null) {
                ((TextView) inflate.findViewById(C0469R.C0471id.to_common)).setText(issuedTo.getCName());
                ((TextView) inflate.findViewById(C0469R.C0471id.to_org)).setText(issuedTo.getOName());
                ((TextView) inflate.findViewById(C0469R.C0471id.to_org_unit)).setText(issuedTo.getUName());
            }
            DName issuedBy = sslCertificate.getIssuedBy();
            if (issuedBy != null) {
                ((TextView) inflate.findViewById(C0469R.C0471id.by_common)).setText(issuedBy.getCName());
                ((TextView) inflate.findViewById(C0469R.C0471id.by_org)).setText(issuedBy.getOName());
                ((TextView) inflate.findViewById(C0469R.C0471id.by_org_unit)).setText(issuedBy.getUName());
            }
            ((TextView) inflate.findViewById(C0469R.C0471id.issued_on)).setText(formatCertificateDate(context, sslCertificate.getValidNotBeforeDate()));
            ((TextView) inflate.findViewById(C0469R.C0471id.expires_on)).setText(formatCertificateDate(context, sslCertificate.getValidNotAfterDate()));
            return inflate;
        }

        private String formatCertificateDate(Context context, Date date) {
            return date == null ? "" : DateFormat.getDateFormat(context).format(date);
        }

        public void destroy() {
            this.mWebEventListener = null;
        }

        private Uri getURIfromURL(String str) {
            Uri parse = Uri.parse(str);
            if (!SdkUtils.isEmptyString(this.mRedirectUrl)) {
                Uri parse2 = Uri.parse(this.mRedirectUrl);
                if (parse2.getScheme() == null || !parse2.getScheme().equals(parse.getScheme()) || !parse2.getAuthority().equals(parse.getAuthority())) {
                    return null;
                }
            }
            return parse;
        }

        private String getValueFromURI(Uri uri, String str) throws InvalidUrlException {
            String str2;
            if (uri == null) {
                return null;
            }
            try {
                str2 = uri.getQueryParameter(str);
            } catch (Exception unused) {
                str2 = null;
            }
            if (!SdkUtils.isEmptyString(str2) && !SdkUtils.isEmptyString(this.state)) {
                if (!this.state.equals(uri.getQueryParameter("state"))) {
                    throw new InvalidUrlException();
                }
            }
            return str2;
        }

        public void setOnPageFinishedListener(OnPageFinishedListener onPageFinishedListener) {
            this.mOnPageFinishedListener = onPageFinishedListener;
        }
    }

    public interface OnPageFinishedListener {
        void onPageFinished(WebView webView, String str);
    }

    public static class WebViewException extends Exception {
        private final String mDescription;
        private final int mErrorCode;
        private final String mFailingUrl;

        public WebViewException(int i, String str, String str2) {
            this.mErrorCode = i;
            this.mDescription = str;
            this.mFailingUrl = str2;
        }

        public int getErrorCode() {
            return this.mErrorCode;
        }

        public String getDescription() {
            return this.mDescription;
        }

        public String getFailingUrl() {
            return this.mFailingUrl;
        }
    }

    public OAuthWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public String getStateString() {
        return this.state;
    }

    public void setBoxAccountEmail(String str) {
        this.mBoxAccountEmail = str;
    }

    public void authenticate(String str, String str2) {
        authenticate(buildUrl(str, str2));
    }

    public void authenticate(Uri.Builder builder) {
        this.state = SdkUtils.generateStateToken();
        builder.appendQueryParameter("state", this.state);
        loadUrl(builder.build().toString());
    }

    /* access modifiers changed from: protected */
    public Uri.Builder buildUrl(String str, String str2) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("app.box.com");
        builder.appendPath("api");
        builder.appendPath("oauth2");
        builder.appendPath("authorize");
        builder.appendQueryParameter("response_type", "code");
        builder.appendQueryParameter("client_id", str);
        builder.appendQueryParameter("redirect_uri", str2);
        String str3 = this.mBoxAccountEmail;
        if (str3 != null) {
            builder.appendQueryParameter(URL_QUERY_LOGIN, str3);
        }
        return builder;
    }
}
