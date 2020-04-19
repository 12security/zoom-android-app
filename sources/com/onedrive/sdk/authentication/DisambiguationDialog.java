package com.onedrive.sdk.authentication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.logger.ILogger;

class DisambiguationDialog extends Dialog implements OnCancelListener {
    private static final String DISAMBIGUATION_PAGE_URL = "https://onedrive.live.com/picker/accountchooser?ru=https://localhost:777&load_login=false";
    private final DisambiguationRequest mRequest;

    public DisambiguationDialog(Context context, DisambiguationRequest disambiguationRequest) {
        super(context, 16973840);
        this.mRequest = disambiguationRequest;
    }

    public ILogger getLogger() {
        return this.mRequest.getLogger();
    }

    public void onCancel(DialogInterface dialogInterface) {
        this.mRequest.getLogger().logDebug("Disambiguation dialog canceled");
        this.mRequest.getCallback().failure(new ClientAuthenticatorException("Authentication Disambiguation Canceled", OneDriveErrorCodes.AuthenticationCancelled));
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setOnCancelListener(this);
        FrameLayout frameLayout = new FrameLayout(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        WebView webView = new WebView(getContext());
        webView.setWebViewClient(new DisambiguationWebView(this, this.mRequest.getCallback()));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(DISAMBIGUATION_PAGE_URL);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        webView.setLayoutParams(layoutParams);
        webView.setVisibility(0);
        linearLayout.addView(webView);
        linearLayout.setVisibility(0);
        frameLayout.addView(linearLayout);
        frameLayout.setVisibility(0);
        frameLayout.forceLayout();
        linearLayout.forceLayout();
        addContentView(frameLayout, layoutParams);
    }
}
