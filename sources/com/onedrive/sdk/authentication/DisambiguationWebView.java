package com.onedrive.sdk.authentication;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.logger.ILogger;
import java.util.Locale;

class DisambiguationWebView extends WebViewClient {
    private final ICallback<DisambiguationResponse> mCallback;
    private final DisambiguationDialog mDisambiguationDialog;

    public DisambiguationWebView(DisambiguationDialog disambiguationDialog, ICallback<DisambiguationResponse> iCallback) {
        this.mDisambiguationDialog = disambiguationDialog;
        this.mCallback = iCallback;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        ILogger logger = this.mDisambiguationDialog.getLogger();
        StringBuilder sb = new StringBuilder();
        sb.append("onPageStarted for url '");
        sb.append(str);
        sb.append("'");
        logger.logDebug(sb.toString());
        super.onPageStarted(webView, str, bitmap);
        Uri parse = Uri.parse(str);
        if (parse.getAuthority().equalsIgnoreCase("localhost:777")) {
            this.mDisambiguationDialog.getLogger().logDebug("Found callback from disambiguation service");
            this.mCallback.success(new DisambiguationResponse(AccountType.fromRepresentation(parse.getQueryParameter("account_type")), parse.getQueryParameter("user_email")));
            webView.stopLoading();
            this.mDisambiguationDialog.dismiss();
        }
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        super.onReceivedError(webView, i, str, str2);
        this.mCallback.failure(new ClientAuthenticatorException(String.format(Locale.ROOT, "Url %s, Error code: %d, Description %s", new Object[]{str2, Integer.valueOf(i), str}), OneDriveErrorCodes.AuthenticationFailure));
        this.mDisambiguationDialog.dismiss();
    }
}
