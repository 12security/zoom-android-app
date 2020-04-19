package com.zipow.videobox.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMDynTextSizeTextView;
import p021us.zoom.videomeetings.C4558R;

public class WebViewFragment extends ZMDialogFragment implements OnClickListener {
    public static final String TITLE = "title";
    public static final String URL = "url";
    private Button mBtnBack;
    private ProgressBar mLoadingProgress;
    private ZMDynTextSizeTextView mTitle;
    private WebView mWebView;
    @Nullable
    private String title;
    @Nullable
    private String url;

    public static void showAsActivity(@Nullable Fragment fragment, Bundle bundle) {
        if (fragment != null) {
            SimpleActivity.show(fragment, WebViewFragment.class.getName(), bundle, 0);
        }
    }

    public static void show(@NonNull ZMActivity zMActivity, Bundle bundle) {
        SimpleActivity.show(zMActivity, WebViewFragment.class.getName(), bundle, 0);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_webview, null);
        this.mWebView = (WebView) inflate.findViewById(C4558R.C4560id.webviewPage);
        this.mTitle = (ZMDynTextSizeTextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mLoadingProgress = (ProgressBar) inflate.findViewById(C4558R.C4560id.webLoadingProgress);
        Bundle arguments = getArguments();
        this.url = arguments.getString("url");
        this.title = arguments.getString("title");
        this.mTitle.setText(this.title);
        this.mBtnBack.setOnClickListener(this);
        this.mLoadingProgress.setVisibility(8);
        if (!inflate.isInEditMode()) {
            this.mWebView.getSettings().setAllowContentAccess(false);
            this.mWebView.getSettings().setSupportZoom(true);
            this.mWebView.getSettings().setJavaScriptEnabled(false);
            this.mWebView.getSettings().setLoadsImagesAutomatically(true);
        }
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                WebViewFragment.this.showWebUrlLoadingStatus();
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                WebViewFragment.this.showWebUrlLoadedStatus();
            }
        });
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                WebViewFragment.this.displayWebLoadingProgress(webView, i);
            }
        });
        return inflate;
    }

    public void onResume() {
        super.onResume();
        if (this.mWebView != null && !TextUtils.isEmpty(this.url)) {
            this.mWebView.loadUrl(this.url);
        }
    }

    /* access modifiers changed from: private */
    public void showWebUrlLoadingStatus() {
        this.mLoadingProgress.setVisibility(0);
        this.mLoadingProgress.setProgress(0);
    }

    /* access modifiers changed from: private */
    public void showWebUrlLoadedStatus() {
        this.mLoadingProgress.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void displayWebLoadingProgress(WebView webView, int i) {
        if (i >= 100 || i <= 0) {
            this.mLoadingProgress.setProgress(0);
        } else {
            this.mLoadingProgress.setProgress(i);
        }
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnBack) {
            onClickBack();
        }
    }

    private void onClickBack() {
        dismiss();
    }

    public void dismiss() {
        finishFragment(true);
    }
}
