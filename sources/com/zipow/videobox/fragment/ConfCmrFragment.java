package com.zipow.videobox.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.zipow.videobox.SimpleInMeetingActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfCmrFragment extends ZMDialogFragment implements OnClickListener {
    private Button mBtnBack;
    private ProgressBar mLoadingProgress;
    private WebView mWebView;

    public static void showAsActivity(@Nullable ZMActivity zMActivity, int i) {
        if (zMActivity != null) {
            SimpleInMeetingActivity.show(zMActivity, ConfCmrFragment.class.getName(), (Bundle) null, i, false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_conf_crm, null);
        this.mWebView = (WebView) inflate.findViewById(C4558R.C4560id.webviewPage);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mLoadingProgress = (ProgressBar) inflate.findViewById(C4558R.C4560id.webLoadingProgress);
        this.mBtnBack.setOnClickListener(this);
        this.mLoadingProgress.setVisibility(8);
        if (!inflate.isInEditMode()) {
            this.mWebView.getSettings().setAllowContentAccess(false);
            this.mWebView.getSettings().setSupportZoom(true);
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.getSettings().setLoadsImagesAutomatically(true);
        }
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                ConfCmrFragment.this.showWebUrlLoadingStatus();
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                ConfCmrFragment.this.showWebUrlLoadedStatus();
            }
        });
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                ConfCmrFragment.this.displayWebLoadingProgress(webView, i);
            }
        });
        return inflate;
    }

    public void onResume() {
        super.onResume();
        if (this.mWebView != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                String recordingManagementURL = confContext.getRecordingManagementURL();
                if (!StringUtil.isEmptyOrNull(recordingManagementURL)) {
                    this.mWebView.loadUrl(recordingManagementURL);
                }
            }
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
