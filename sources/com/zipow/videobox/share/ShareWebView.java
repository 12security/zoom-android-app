package com.zipow.videobox.share;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.ZMDomainUtil;
import com.zipow.videobox.view.bookmark.BookmarkListViewFragment;
import com.zipow.videobox.view.bookmark.BookmarkMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ShareWebView extends ShareBaseView {
    private ImageView mBackBtn;
    private ImageView mBookmarkBtn;
    /* access modifiers changed from: private */
    public Context mContext;
    private ImageView mForwardBtn;
    private View mHeader;
    /* access modifiers changed from: private */
    public boolean mInWebLoading = false;
    private ProgressBar mLoadingProgress;
    private View mToolbar;
    @Nullable
    private String mUrl;
    /* access modifiers changed from: private */
    public EditText mUrlEditText;
    private ImageView mWebLoadingStop;
    private ImageView mWebRefresh;
    private ImageView mWebUrlDelete;
    /* access modifiers changed from: private */
    public WebView mWebView;
    private View mWebViewContainer;

    public ShareWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ShareWebView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ShareWebView(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    @SuppressLint({"NewApi"})
    private void init(Context context) {
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(C4558R.layout.zm_share_webview, null, false);
        this.mToolbar = inflate.findViewById(C4558R.C4560id.shareWebToolbar);
        this.mWebView = (WebView) inflate.findViewById(C4558R.C4560id.webview);
        this.mWebViewContainer = inflate.findViewById(C4558R.C4560id.webviewContainer);
        if (!isInEditMode()) {
            this.mWebView.getSettings().setAllowContentAccess(false);
            this.mWebView.getSettings().setSupportZoom(true);
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.getSettings().setLoadsImagesAutomatically(true);
        }
        this.mWebView.setScrollBarStyle(0);
        this.mWebView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ShareWebView.this.mWebView.requestFocus();
                return false;
            }
        });
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                ShareWebView.this.mUrlEditText.setText(str);
                ShareWebView.this.showWebUrlLoadingStatus();
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                ShareWebView.this.mUrlEditText.setText(str);
                ShareWebView.this.showWebUrlLoadedStatus();
                ShareWebView.this.updateWebNavigateBtn();
            }
        });
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                ShareWebView.this.displayWebLoadingProgress(webView, i);
            }
        });
        this.mHeader = inflate.findViewById(C4558R.C4560id.webheader);
        this.mLoadingProgress = (ProgressBar) inflate.findViewById(C4558R.C4560id.webLoadingProgress);
        this.mLoadingProgress.setVisibility(8);
        this.mUrlEditText = (EditText) inflate.findViewById(C4558R.C4560id.editurl);
        this.mUrlEditText.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!ShareWebView.this.mUrlEditText.hasFocus()) {
                    ShareWebView.this.mUrlEditText.requestFocus();
                }
                ShareWebView.this.showWebUrlEditStatus();
            }
        });
        this.mUrlEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 66) {
                    UIUtil.closeSoftKeyboard(ShareWebView.this.mContext, ((Activity) ShareWebView.this.mContext).getCurrentFocus(), 2);
                    ShareWebView shareWebView = ShareWebView.this;
                    shareWebView.setUrl(shareWebView.mUrlEditText.getText().toString());
                }
                return false;
            }
        });
        this.mUrlEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (view == ShareWebView.this.mUrlEditText) {
                    if (z) {
                        ShareWebView.this.showWebUrlEditStatus();
                    } else {
                        UIUtil.closeSoftKeyboard(ShareWebView.this.mContext, view);
                        if (ShareWebView.this.mInWebLoading) {
                            ShareWebView.this.showWebUrlLoadingStatus();
                        } else {
                            ShareWebView.this.showWebUrlLoadedStatus();
                        }
                    }
                }
            }
        });
        this.mWebRefresh = (ImageView) inflate.findViewById(C4558R.C4560id.urlRefresh);
        this.mWebRefresh.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ShareWebView.this.mWebView.isShown()) {
                    ShareWebView.this.mWebView.reload();
                }
            }
        });
        this.mWebUrlDelete = (ImageView) inflate.findViewById(C4558R.C4560id.urlDelete);
        this.mWebUrlDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ShareWebView.this.mUrlEditText.setText("");
                ShareWebView.this.mUrlEditText.requestFocus();
            }
        });
        this.mWebLoadingStop = (ImageView) inflate.findViewById(C4558R.C4560id.urlLoadingStop);
        this.mWebLoadingStop.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ShareWebView.this.mWebView.stopLoading();
            }
        });
        this.mBackBtn = (ImageView) inflate.findViewById(C4558R.C4560id.back);
        this.mBackBtn.setEnabled(false);
        this.mBackBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ShareWebView.this.mWebView.canGoBack()) {
                    ShareWebView.this.mWebView.goBack();
                }
            }
        });
        this.mForwardBtn = (ImageView) inflate.findViewById(C4558R.C4560id.forward);
        this.mBackBtn.setEnabled(false);
        this.mForwardBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ShareWebView.this.mWebView.canGoForward()) {
                    ShareWebView.this.mWebView.goForward();
                }
            }
        });
        this.mBookmarkBtn = (ImageView) inflate.findViewById(C4558R.C4560id.bookmark);
        this.mBookmarkBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                String title = ShareWebView.this.mWebView.getTitle();
                String url = ShareWebView.this.mWebView.getUrl();
                if (title != null && !title.isEmpty()) {
                    bundle.putString(BookmarkMgr.BOOKMARK_TITLE, title);
                }
                if (url != null && !url.isEmpty()) {
                    bundle.putString(BookmarkMgr.BOOKMARK_URL, url);
                }
                BookmarkListViewFragment.showAsActivity((ZMActivity) ShareWebView.this.mContext, bundle, 1006);
            }
        });
        addView(inflate);
    }

    /* access modifiers changed from: private */
    public void showWebUrlEditStatus() {
        if (this.mHeader.getVisibility() == 0) {
            this.mWebUrlDelete.setVisibility(0);
            this.mWebRefresh.setVisibility(8);
            this.mWebLoadingStop.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void showWebUrlLoadingStatus() {
        if (this.mHeader.getVisibility() == 0) {
            this.mLoadingProgress.setVisibility(0);
            this.mLoadingProgress.setProgress(0);
            this.mInWebLoading = true;
            this.mWebLoadingStop.setVisibility(0);
            this.mWebUrlDelete.setVisibility(8);
            this.mWebRefresh.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void showWebUrlLoadedStatus() {
        if (this.mHeader.getVisibility() == 0) {
            this.mLoadingProgress.setVisibility(4);
            this.mInWebLoading = false;
            this.mWebUrlDelete.setVisibility(8);
            this.mWebRefresh.setVisibility(0);
            this.mWebLoadingStop.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void updateWebNavigateBtn() {
        if (this.mHeader.getVisibility() == 0) {
            this.mBackBtn.setEnabled(this.mWebView.canGoBack());
            this.mForwardBtn.setEnabled(this.mWebView.canGoForward());
        }
    }

    /* access modifiers changed from: private */
    public void displayWebLoadingProgress(WebView webView, int i) {
        if (webView == this.mWebView && i >= 0 && this.mHeader.getVisibility() == 0) {
            if (i >= 100 || i <= 0) {
                this.mLoadingProgress.setProgress(0);
            } else {
                this.mLoadingProgress.setProgress(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setUrl(String str) {
        if ("".equals(str.trim())) {
            this.mUrl = null;
            return;
        }
        this.mUrl = str;
        if (!str.startsWith(ZMDomainUtil.ZM_URL_HTTP) && !str.startsWith("https://")) {
            StringBuilder sb = new StringBuilder();
            sb.append(ZMDomainUtil.ZM_URL_HTTP);
            sb.append(str);
            str = sb.toString();
        }
        WebSettings settings = this.mWebView.getSettings();
        if (settings != null) {
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
        }
        this.mWebView.loadUrl(str);
        UIUtil.closeSoftKeyboard(this.mContext, this);
    }

    public boolean setWebUrl(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        setUrl(str);
        return true;
    }

    public void drawShareContent(@Nullable Canvas canvas) {
        if (canvas != null) {
            this.mWebViewContainer.draw(canvas);
        }
    }

    public int getShareContentWidth() {
        return this.mWebViewContainer.getWidth();
    }

    public int getShareContentHeight() {
        return this.mWebViewContainer.getHeight();
    }

    public boolean handleKeydown(int i, KeyEvent keyEvent) {
        if (i != 4 || !this.mWebView.canGoBack()) {
            return false;
        }
        this.mWebView.goBack();
        return true;
    }

    public void setDrawingMode(boolean z) {
        if (z) {
            this.mHeader.setVisibility(8);
            this.mToolbar.setVisibility(0);
            return;
        }
        this.mHeader.setVisibility(0);
        this.mToolbar.setVisibility(8);
    }
}
