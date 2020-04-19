package com.zipow.videobox.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;

public abstract class ShareBaseView extends FrameLayout {
    private IShareBaseViewListener mShareBaseViewListener;

    public interface IShareBaseViewListener {
        void onCloseView(ShareBaseView shareBaseView);

        void onRepaint(ShareBaseView shareBaseView);

        void onSavePhoto();
    }

    public void closeAnnotateView() {
    }

    public abstract void drawShareContent(Canvas canvas);

    public abstract int getShareContentHeight();

    public abstract int getShareContentWidth();

    public boolean handleKeydown(int i, KeyEvent keyEvent) {
        return false;
    }

    public boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        return false;
    }

    public void layout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void onAnnotateShutDown() {
    }

    public void onAnnotateStartedUp(boolean z, long j) {
    }

    public void onAnnotateViewSizeChanged() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void setCachedImage(Bitmap bitmap) {
    }

    public void setDrawingMode(boolean z) {
    }

    public void setEditModel(boolean z) {
    }

    public void stop() {
    }

    public void unregisterAnnotateListener() {
    }

    public void updateWBPageNum(int i, int i2, int i3, int i4) {
    }

    public ShareBaseView(@NonNull Context context) {
        super(context);
    }

    public ShareBaseView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ShareBaseView(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setShareBaseViewListener(IShareBaseViewListener iShareBaseViewListener) {
        this.mShareBaseViewListener = iShareBaseViewListener;
    }

    /* access modifiers changed from: protected */
    public void notifyRefresh() {
        IShareBaseViewListener iShareBaseViewListener = this.mShareBaseViewListener;
        if (iShareBaseViewListener != null) {
            iShareBaseViewListener.onRepaint(this);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyCloseView() {
        IShareBaseViewListener iShareBaseViewListener = this.mShareBaseViewListener;
        if (iShareBaseViewListener != null) {
            iShareBaseViewListener.onCloseView(this);
        }
    }

    /* access modifiers changed from: protected */
    public void notifySavePhoto() {
        IShareBaseViewListener iShareBaseViewListener = this.mShareBaseViewListener;
        if (iShareBaseViewListener != null) {
            iShareBaseViewListener.onSavePhoto();
        }
    }
}
