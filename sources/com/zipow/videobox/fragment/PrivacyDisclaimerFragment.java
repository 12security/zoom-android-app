package com.zipow.videobox.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.CustomizeInfo;
import com.zipow.videobox.dialog.ZMCDPRConfirmDialog;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PrivacyDisclaimerFragment extends ZMDialogFragment implements OnClickListener {
    public static final String KEY_INFO = "Listener";
    private Button mBtnAgree;
    private Button mBtnCancel;
    private CustomizeInfo mCustomizeInfo;
    private ProgressBar mLoadingProgress;
    private TextView mTxtTitle;
    /* access modifiers changed from: private */
    public WebView mWebView;

    public static void showDialog(@NonNull ZMActivity zMActivity, @NonNull CustomizeInfo customizeInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_INFO, customizeInfo);
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (shouldShow(supportFragmentManager, PrivacyDisclaimerFragment.class.getSimpleName(), bundle)) {
            PrivacyDisclaimerFragment privacyDisclaimerFragment = new PrivacyDisclaimerFragment();
            privacyDisclaimerFragment.setArguments(bundle);
            privacyDisclaimerFragment.setCancelable(false);
            privacyDisclaimerFragment.showNow(supportFragmentManager, ZMCDPRConfirmDialog.class.getName());
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, PrivacyDisclaimerFragment.class.getName(), new Bundle(), 0);
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, C4558R.style.ZMDialog_NoTitle);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mCustomizeInfo = (CustomizeInfo) getArguments().getSerializable(KEY_INFO);
        View inflate = layoutInflater.inflate(C4558R.layout.zm_privacy_disclaimer, null);
        this.mWebView = (WebView) inflate.findViewById(C4558R.C4560id.webviewPage);
        this.mBtnCancel = (Button) inflate.findViewById(C4558R.C4560id.btnCancel);
        this.mBtnAgree = (Button) inflate.findViewById(C4558R.C4560id.btnAgree);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mLoadingProgress = (ProgressBar) inflate.findViewById(C4558R.C4560id.webLoadingProgress);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnAgree.setOnClickListener(this);
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
                PrivacyDisclaimerFragment.this.showWebUrlLoadingStatus();
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                PrivacyDisclaimerFragment.this.showWebUrlLoadedStatus();
            }
        });
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                PrivacyDisclaimerFragment.this.displayWebLoadingProgress(webView, i);
            }
        });
        this.mWebView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 0 || i != 4 || !PrivacyDisclaimerFragment.this.mWebView.canGoBack()) {
                    return false;
                }
                PrivacyDisclaimerFragment.this.mWebView.goBack();
                return true;
            }
        });
        CustomizeInfo customizeInfo = this.mCustomizeInfo;
        if (customizeInfo != null && !StringUtil.isEmptyOrNull(customizeInfo.getTitle())) {
            this.mTxtTitle.setText(this.mCustomizeInfo.getTitle());
        }
        return inflate;
    }

    public void onResume() {
        super.onResume();
        CustomizeInfo customizeInfo = this.mCustomizeInfo;
        if (customizeInfo != null && this.mWebView != null && !StringUtil.isEmptyOrNull(customizeInfo.getDescription())) {
            this.mWebView.loadDataWithBaseURL(null, this.mCustomizeInfo.getDescription(), "text/html", "utf-8", null);
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
        int id = view.getId();
        if (id == C4558R.C4560id.btnCancel) {
            onClickCancel();
            dismiss();
        } else if (id == C4558R.C4560id.btnAgree) {
            onClickAgree();
            dismiss();
        }
    }

    private void onClickAgree() {
        CustomizeInfo customizeInfo = this.mCustomizeInfo;
        if (customizeInfo != null) {
            switch (customizeInfo.type) {
                case 1:
                    ZoomLogEventTracking.eventTrackHandleAppDisclaimer(49);
                    PTApp.getInstance().userAgreeLoginDisclaimer();
                    PTUI.getInstance().ClearLoginDisclaimerConfirmFlag();
                    return;
                case 2:
                    ZMConfEventTracking.logAppDisclaimer(49);
                    ConfMgr.getInstance().agreeJoinMeetingDisclaimer();
                    return;
                default:
                    return;
            }
        }
    }

    private void onClickCancel() {
        CustomizeInfo customizeInfo = this.mCustomizeInfo;
        if (customizeInfo != null) {
            switch (customizeInfo.type) {
                case 1:
                    ZoomLogEventTracking.eventTrackHandleAppDisclaimer(47);
                    PTApp.getInstance().userDisagreeLoginDisclaimer();
                    return;
                case 2:
                    FragmentActivity activity = getActivity();
                    if (activity instanceof ConfActivity) {
                        ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
                        ConfLocalHelper.leaveCall((ConfActivity) activity);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void dismiss() {
        finishFragment(true);
    }
}
