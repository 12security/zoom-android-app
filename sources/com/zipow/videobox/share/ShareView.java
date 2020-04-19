package com.zipow.videobox.share;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxItem;
import com.zipow.annotate.AnnotateDrawingView;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.component.sink.share.IShareViewActionHandle;
import com.zipow.videobox.share.ShareBaseView.IShareBaseViewListener;
import com.zipow.videobox.view.bookmark.BookmarkMgr;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ShareView extends FrameLayout implements IShareView, IShareBaseViewListener, IShareViewActionHandle {
    private static final String TAG = "ShareView";
    private boolean mAutoCapture = false;
    /* access modifiers changed from: private */
    public int mBottom = 0;
    private int mCacheH = 0;
    private int mCacheW = 0;
    @Nullable
    private Bitmap mCachedBitmap;
    @Nullable
    private Canvas mCachedCanvas;
    @Nullable
    private ShareBaseView mContentView;
    private Context mContext;
    /* access modifiers changed from: private */
    @Nullable
    public Point mDrawingBtnLastPos = null;
    private ShareBaseView mDrawingView;
    /* access modifiers changed from: private */
    public GestureDetector mGestureDetector;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public int mLeft = 0;
    /* access modifiers changed from: private */
    public FrameLayout mShareContainer;
    private IShareServer mShareServer;
    private boolean mStopped = false;
    private ImageView mToolbarBtn;
    private boolean mbAnnoationEnable = false;
    private boolean mbAnnotateBtnCanVisible = false;
    private boolean mbEditStatus = false;
    private boolean mbHasBottomBar = false;
    private boolean mbSharing = true;
    private boolean mbVisibleWithConfToolbar = true;
    /* access modifiers changed from: private */
    public ShareViewListener shareListener;

    private class GuestureListener extends SimpleOnGestureListener {
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return true;
        }

        public GuestureListener() {
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            ShareView.this.switchToEditMode();
            return true;
        }

        public boolean onScroll(MotionEvent motionEvent, @NonNull MotionEvent motionEvent2, float f, float f2) {
            ShareView.this.mShareContainer.getDrawingRect(new Rect());
            float rawX = motionEvent2.getRawX() - ((float) ShareView.this.mLeft);
            float rawY = motionEvent2.getRawY() - ((float) ShareView.this.mBottom);
            if (ShareView.this.mDrawingBtnLastPos == null) {
                ShareView.this.mDrawingBtnLastPos = new Point(rawX, rawY);
            } else {
                ShareView.this.mDrawingBtnLastPos.setX(rawX);
                ShareView.this.mDrawingBtnLastPos.setY(rawY);
            }
            ShareView.this.refreshUI();
            return true;
        }
    }

    public interface ShareViewListener {
        void onShareError();

        void onStartEdit();

        void onStopEdit();
    }

    public ShareView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ShareView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mHandler = new Handler();
        if (!isInEditMode()) {
            this.mShareServer = new ShareServerImpl(this.mHandler);
        }
        View inflate = LayoutInflater.from(context).inflate(C4558R.layout.zm_sharinglayout, null, false);
        this.mShareContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.shareContainer);
        this.mToolbarBtn = (ImageView) inflate.findViewById(C4558R.C4560id.btnDrawing);
        this.mGestureDetector = new GestureDetector(context, new GuestureListener());
        this.mGestureDetector.setIsLongpressEnabled(false);
        this.mToolbarBtn.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ShareView.this.refreshUI();
                return ShareView.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        this.mToolbarBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        addView(inflate);
        this.mDrawingView = new AnnotateDrawingView(this.mContext);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.topMargin = calcTopMargin();
        this.mDrawingView.setLayoutParams(layoutParams);
        this.mDrawingView.setShareBaseViewListener(this);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mLeft = i;
        this.mBottom = i4;
        super.onLayout(z, i, i2, i3, i4);
        refreshUI();
        onRepaint();
    }

    public void start() {
        this.mShareServer.setCacheView(this);
        try {
            this.mShareServer.startShare(this.mAutoCapture);
        } catch (ShareException unused) {
        }
        refreshUI();
    }

    public void pause() {
        this.mShareServer.pauseShare();
        refreshUI();
        if (this.mbEditStatus) {
            this.mDrawingView.pause();
            this.mDrawingView.closeAnnotateView();
        }
    }

    public void resume() {
        this.mDrawingView.resume();
        this.mShareServer.resumeShare();
        refreshUI();
    }

    public void stop() {
        this.mbVisibleWithConfToolbar = true;
        this.mAutoCapture = false;
        this.mShareServer.endShare();
        release();
    }

    public void setAnnotationEnable(boolean z) {
        this.mbAnnoationEnable = z;
        setAnnoBtnCanVisible();
        if (!this.mbAnnoationEnable) {
            this.mDrawingView.notifyCloseView();
        }
        setAnnotateBtnVisible();
    }

    public void setSharePauseStatuChanged(boolean z) {
        this.mbSharing = !z;
        setAnnoBtnCanVisible();
        setAnnotateBtnVisible();
    }

    public void onAnnotateViewSizeChanged() {
        this.mDrawingView.onAnnotateViewSizeChanged();
    }

    private void onRepaint() {
        this.mShareServer.onRepaint();
    }

    private void release() {
        this.mContentView = null;
        this.mCacheW = 0;
        this.mCacheH = 0;
        setEditModel(false);
        this.mShareContainer.removeAllViews();
    }

    public boolean setImageUri(Uri uri) {
        ShareImageView shareImageView = new ShareImageView(this.mContext);
        shareImageView.setLayoutParams(new LayoutParams(-1, -1));
        shareImageView.setShareBaseViewListener(this);
        if (!shareImageView.setImageUri(uri)) {
            return false;
        }
        this.mbHasBottomBar = false;
        this.mDrawingBtnLastPos = null;
        this.mContentView = shareImageView;
        this.mShareContainer.addView(shareImageView);
        this.mAutoCapture = false;
        return true;
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        ShareImageView shareImageView = new ShareImageView(this.mContext);
        shareImageView.setLayoutParams(new LayoutParams(-1, -1));
        shareImageView.setShareBaseViewListener(this);
        if (!shareImageView.setImageBitmap(bitmap)) {
            return false;
        }
        this.mbHasBottomBar = false;
        this.mDrawingBtnLastPos = null;
        this.mContentView = shareImageView;
        this.mShareContainer.addView(shareImageView);
        this.mAutoCapture = false;
        return true;
    }

    public boolean setWhiteboardBackground() {
        ShareImageView shareImageView = new ShareImageView(this.mContext);
        shareImageView.setLayoutParams(new LayoutParams(-1, -1));
        shareImageView.setShareBaseViewListener(this);
        shareImageView.showWhiteboard();
        this.mbHasBottomBar = false;
        this.mDrawingBtnLastPos = null;
        this.mContentView = shareImageView;
        this.mShareContainer.addView(shareImageView);
        this.mAutoCapture = false;
        return true;
    }

    public boolean setUrl(@Nullable String str) {
        ShareWebView shareWebView = new ShareWebView(this.mContext);
        shareWebView.setLayoutParams(new LayoutParams(-1, -1));
        shareWebView.setShareBaseViewListener(this);
        if (!shareWebView.setWebUrl(str)) {
            return false;
        }
        this.mbHasBottomBar = false;
        this.mDrawingBtnLastPos = null;
        this.mContentView = shareWebView;
        this.mShareContainer.addView(shareWebView);
        this.mAutoCapture = true;
        return true;
    }

    public boolean setPdf(String str, String str2) {
        SharePDFView sharePDFView = new SharePDFView(this.mContext);
        sharePDFView.setLayoutParams(new LayoutParams(-1, -1));
        sharePDFView.setShareBaseViewListener(this);
        if (!sharePDFView.setPdfFile(str, str2)) {
            return false;
        }
        this.mbHasBottomBar = sharePDFView.hasBottomBar();
        this.mDrawingBtnLastPos = null;
        this.mContentView = sharePDFView;
        this.mShareContainer.addView(sharePDFView);
        this.mAutoCapture = false;
        return true;
    }

    private boolean isDrawingViewVisible() {
        int childCount = this.mShareContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.mShareContainer.getChildAt(i) == this.mDrawingView) {
                return true;
            }
        }
        return false;
    }

    private boolean isContentViewVisible() {
        if (this.mContentView == null) {
            return false;
        }
        int childCount = this.mShareContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.mShareContainer.getChildAt(i) == this.mContentView) {
                return true;
            }
        }
        return false;
    }

    private void checkCachedSize() {
        FrameLayout frameLayout = this.mShareContainer;
        if (frameLayout != null && this.mContentView != null && frameLayout.getChildCount() > 0) {
            this.mCacheW = this.mContentView.getShareContentWidth();
            this.mCacheH = this.mContentView.getShareContentHeight();
        }
    }

    private void destroyCachedBitmap() {
        Bitmap bitmap = this.mCachedBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mCachedBitmap = null;
        }
        this.mCachedCanvas = null;
    }

    private boolean checkCacheBitmap() {
        checkCachedSize();
        if (this.mCacheW <= 0 || this.mCacheH <= 0) {
            return false;
        }
        Bitmap bitmap = this.mCachedBitmap;
        if (!(bitmap == null || (bitmap.getWidth() == this.mCacheW && this.mCachedBitmap.getHeight() == this.mCacheH))) {
            destroyCachedBitmap();
        }
        if (this.mCachedBitmap == null) {
            try {
                this.mCachedBitmap = Bitmap.createBitmap(this.mCacheW, this.mCacheH, Config.ARGB_8888);
                Bitmap bitmap2 = this.mCachedBitmap;
                if (bitmap2 == null) {
                    return false;
                }
                this.mCachedCanvas = new Canvas(bitmap2);
            } catch (OutOfMemoryError unused) {
                notifyError();
                return false;
            }
        }
        return true;
    }

    private void notifyError() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (ShareView.this.shareListener != null) {
                    ShareView.this.shareListener.onShareError();
                }
            }
        });
    }

    @Nullable
    public Bitmap getCacheDrawingView() {
        if (this.mStopped || !checkCacheBitmap()) {
            return null;
        }
        if (this.mContentView != null && isContentViewVisible()) {
            this.mContentView.drawShareContent(this.mCachedCanvas);
        }
        if (isDrawingViewVisible()) {
            this.mDrawingView.drawShareContent(this.mCachedCanvas);
            this.mDrawingView.setCachedImage(this.mCachedBitmap);
        }
        return this.mCachedBitmap;
    }

    public boolean onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i != 1006) {
            return false;
        }
        if (i2 == -1 && intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String string = extras.getString(BookmarkMgr.BOOKMARK_URL);
                if (string != null && !string.isEmpty()) {
                    setUrl(string);
                }
            }
        }
        return true;
    }

    public void setShareListener(ShareViewListener shareViewListener) {
        this.shareListener = shareViewListener;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (!isDrawingViewVisible()) {
            ShareBaseView shareBaseView = this.mContentView;
            if (shareBaseView instanceof ShareWebView) {
                boolean handleKeydown = shareBaseView.handleKeydown(i, keyEvent);
                if (handleKeydown) {
                    onRepaint();
                }
                return handleKeydown;
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void showDrawingView() {
        this.mShareContainer.removeView(this.mDrawingView);
        this.mShareContainer.addView(this.mDrawingView);
        this.mDrawingView.setVisibility(0);
    }

    private void hideDrawingView() {
        if (this.mDrawingView != null) {
            ShareBaseView shareBaseView = this.mContentView;
            if (shareBaseView != null) {
                shareBaseView.setDrawingMode(false);
            }
            setEditModel(false);
        }
    }

    /* access modifiers changed from: protected */
    public void switchToEditMode() {
        this.mDrawingView.setVisibility(0);
        setEditModel(true);
        ShareViewListener shareViewListener = this.shareListener;
        if (shareViewListener != null) {
            shareViewListener.onStartEdit();
        }
        this.mbAnnotateBtnCanVisible = false;
        refreshUI();
        ShareBaseView shareBaseView = this.mContentView;
        if (shareBaseView != null) {
            shareBaseView.setDrawingMode(true);
        }
    }

    public void refreshUI() {
        setAnnotateBtnVisible();
        reLayoutDrawingBtn();
    }

    private void reLayoutDrawingBtn() {
        int i;
        int i2;
        Point point = this.mDrawingBtnLastPos;
        if (point != null) {
            i2 = (int) (((float) this.mLeft) + point.getX());
            i = (int) (((float) this.mBottom) + this.mDrawingBtnLastPos.getY());
        } else if (this.mbHasBottomBar) {
            i2 = this.mLeft + UIUtil.dip2px(this.mContext, 30.0f);
            i = this.mBottom - UIUtil.dip2px(this.mContext, 46.0f);
        } else {
            return;
        }
        int width = i2 - (this.mToolbarBtn.getWidth() / 2);
        int height = i - this.mToolbarBtn.getHeight();
        int height2 = this.mToolbarBtn.getHeight() + height;
        int width2 = this.mToolbarBtn.getWidth() + width;
        if (width < this.mShareContainer.getLeft()) {
            width = this.mShareContainer.getLeft();
            width2 = this.mToolbarBtn.getWidth() + width;
        }
        if (width2 > this.mShareContainer.getRight()) {
            width2 = this.mShareContainer.getRight();
            width = width2 - this.mToolbarBtn.getWidth();
        }
        if (height < this.mShareContainer.getTop()) {
            height = this.mShareContainer.getTop();
            height2 = this.mToolbarBtn.getHeight() + height;
        }
        if (height2 > this.mShareContainer.getBottom()) {
            height2 = this.mShareContainer.getBottom();
            height = height2 - this.mToolbarBtn.getHeight();
        }
        this.mToolbarBtn.layout(width, height, width2, height2);
    }

    private int calcTopMargin() {
        if (!UIUtil.isImmersedModeSupported() || !UIUtil.isPortraitMode(getContext())) {
            return 0;
        }
        return UIUtil.getStatusBarHeight(getContext());
    }

    public void onSavePhoto() {
        if (this.mCachedBitmap != null) {
            Media.insertImage(getContext().getContentResolver(), this.mCachedBitmap, "title", BoxItem.FIELD_DESCRIPTION);
        }
    }

    public void onRepaint(ShareBaseView shareBaseView) {
        onRepaint();
    }

    public void onCloseView(@Nullable ShareBaseView shareBaseView) {
        if (shareBaseView != null) {
            if (shareBaseView instanceof AnnotateDrawingView) {
                this.mToolbarBtn.setVisibility(0);
                hideDrawingView();
            }
            ShareViewListener shareViewListener = this.shareListener;
            if (shareViewListener != null) {
                shareViewListener.onStopEdit();
            }
            setEditModel(false);
            setAnnoBtnCanVisible();
        }
    }

    public void unregisterAnnotateListener() {
        ShareBaseView shareBaseView = this.mDrawingView;
        if (shareBaseView != null) {
            shareBaseView.unregisterAnnotateListener();
        }
    }

    private void setEditModel(boolean z) {
        this.mbEditStatus = z;
        this.mDrawingView.setEditModel(z);
    }

    private void setAnnotateBtnVisible() {
        if (this.mbAnnotateBtnCanVisible) {
            this.mToolbarBtn.setVisibility(0);
        } else {
            this.mToolbarBtn.setVisibility(8);
        }
    }

    private void setAnnoBtnCanVisible() {
        this.mbAnnotateBtnCanVisible = this.mbVisibleWithConfToolbar && this.mbAnnoationEnable && this.mbSharing && !this.mbEditStatus && !ConfMgr.getInstance().isViewOnlyMeeting();
    }

    public void setToolbarBtnPosition(int i, int i2) {
        Point point = this.mDrawingBtnLastPos;
        if (point == null) {
            this.mDrawingBtnLastPos = new Point((float) i, (float) i2);
        } else {
            point.setX((float) i);
            this.mDrawingBtnLastPos.setY((float) i2);
        }
        refreshUI();
    }

    public void setVisibleWithConfToolbar(boolean z) {
        this.mbVisibleWithConfToolbar = z;
        setAnnoBtnCanVisible();
        setAnnotateBtnVisible();
    }

    public void onAnnotateStartedUp(boolean z, long j) {
        showDrawingView();
        this.mDrawingView.onAnnotateStartedUp(z, j);
    }

    public void onAnnotateShutDown() {
        this.mbVisibleWithConfToolbar = true;
        this.mDrawingView.onAnnotateShutDown();
    }

    public void closeAnnotateView() {
        this.mDrawingView.closeAnnotateView();
    }

    public void updateWBPageNum(int i, int i2, int i3, int i4) {
        this.mDrawingView.updateWBPageNum(i, i2, i3, i4);
        getCacheDrawingView();
    }

    public boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        return this.mDrawingView.handleRequestPermissionResult(i, str, i2);
    }

    public void onConfigurationChanged(Configuration configuration) {
        ((LayoutParams) this.mDrawingView.getLayoutParams()).topMargin = calcTopMargin();
    }

    public boolean isDrawing() {
        return this.mbEditStatus;
    }
}
