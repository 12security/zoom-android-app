package com.zipow.videobox.confapp;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.IRendererUnit.PicInfo;
import com.zipow.videobox.view.video.AbsVideoScene;
import p021us.zoom.androidlib.util.StringUtil;

public class GLImage implements IRendererUnit {
    private static final int PIC_BACKGROUND = 4;
    @NonNull
    private static String TAG = "GLImage";
    @Nullable
    private Drawable mBackground;
    @Nullable
    private Bitmap mBackgroundBitmap;
    private int mHeight;
    private boolean mIsDestroyed = false;
    private int mLeft;
    private OnClickListener mOnClickListener;
    private boolean mPaused;
    @Nullable
    private PicInfo mPiBackground;
    private long mRenderInfo;
    private int mTop;
    @Nullable
    private String mUnitName = null;
    private AbsVideoScene mVideoScene;
    private int mWidth;
    private boolean mbPressed = false;
    private boolean mbVisible = true;

    public interface OnClickListener {
        void onClick(GLImage gLImage);
    }

    public void clearRenderer() {
    }

    public void onIdle() {
    }

    public GLImage(long j, @Nullable RendererUnitInfo rendererUnitInfo) {
        this.mRenderInfo = j;
        if (rendererUnitInfo != null) {
            this.mLeft = rendererUnitInfo.left;
            this.mTop = rendererUnitInfo.top;
            this.mWidth = rendererUnitInfo.width;
            this.mHeight = rendererUnitInfo.height;
        }
    }

    public void setVideoScene(AbsVideoScene absVideoScene) {
        this.mVideoScene = absVideoScene;
    }

    public void setUnitName(String str) {
        this.mUnitName = str;
        if (StringUtil.isEmptyOrNull(this.mUnitName)) {
            TAG = VideoUnit.class.getSimpleName();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(VideoUnit.class.getSimpleName());
        sb.append(":");
        sb.append(this.mUnitName);
        TAG = sb.toString();
    }

    @Nullable
    public String getUnitName() {
        return this.mUnitName;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void onGLViewSizeChanged(int i, int i2) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.glViewSizeChanged(this.mRenderInfo, i, i2);
        }
    }

    public void updateUnitInfo(@Nullable RendererUnitInfo rendererUnitInfo) {
        if (rendererUnitInfo != null && !isSameInfo(rendererUnitInfo) && this.mVideoScene != null) {
            this.mLeft = rendererUnitInfo.left;
            this.mTop = rendererUnitInfo.top;
            this.mWidth = rendererUnitInfo.width;
            this.mHeight = rendererUnitInfo.height;
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                videoObj.updateUnitLayout(this.mRenderInfo, rendererUnitInfo);
            }
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getLeft() {
        return this.mLeft;
    }

    public int getTop() {
        return this.mTop;
    }

    public int getRight() {
        return this.mLeft + this.mWidth;
    }

    public int getBottom() {
        return this.mTop + this.mHeight;
    }

    public boolean contains(float f, float f2) {
        return f >= ((float) getLeft()) && f <= ((float) (getLeft() + getWidth())) && f2 >= ((float) getTop()) && f2 <= ((float) (getTop() + getHeight()));
    }

    public void pause() {
        this.mPaused = true;
    }

    public void resume() {
        this.mPaused = false;
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    public void onDestroy() {
        removeBackground();
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.destroyGLImage(this);
            this.mIsDestroyed = true;
        }
    }

    public void onCreate() {
        this.mIsDestroyed = false;
    }

    public long getRendererInfo() {
        return this.mRenderInfo;
    }

    private boolean isSameInfo(@Nullable RendererUnitInfo rendererUnitInfo) {
        boolean z = false;
        if (rendererUnitInfo == null) {
            return false;
        }
        if (this.mLeft == rendererUnitInfo.left && this.mTop == rendererUnitInfo.top && this.mWidth == rendererUnitInfo.width && this.mHeight == rendererUnitInfo.height) {
            z = true;
        }
        return z;
    }

    public void setBackground(@Nullable Drawable drawable) {
        if (this.mBackground != drawable || drawable == null) {
            this.mBackground = drawable;
            this.mPiBackground = null;
            this.mBackgroundBitmap = null;
            showBackground();
        }
    }

    @Nullable
    public Drawable getBackgroundDrawable() {
        return this.mBackground;
    }

    public void setBackground(@Nullable Bitmap bitmap) {
        if (this.mBackgroundBitmap != bitmap || bitmap == null) {
            this.mBackgroundBitmap = bitmap;
            this.mPiBackground = null;
            this.mBackground = null;
            showBackground();
        }
    }

    @Nullable
    public Drawable getBackgrounBitmap() {
        return this.mBackground;
    }

    public void setVisible(boolean z) {
        this.mbVisible = z;
        showBackground();
    }

    public boolean isVisible() {
        return this.mbVisible;
    }

    private void showBackground() {
        if (this.mVideoScene != null) {
            if (!this.mbVisible || (this.mBackground == null && this.mBackgroundBitmap == null)) {
                removeBackground();
            } else if (!this.mIsDestroyed) {
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    Bitmap bitmap = null;
                    int width = getWidth();
                    int height = getHeight();
                    if (this.mPiBackground == null) {
                        bitmap = createBackgroundBitmap();
                        if (bitmap == null) {
                            return;
                        }
                    }
                    if (this.mPiBackground == null) {
                        videoObj.removePic(this.mRenderInfo, 4);
                        long addPic = videoObj.addPic(this.mRenderInfo, 4, bitmap, 255, 0, 0, 0, width, height);
                        if (addPic != 0) {
                            this.mPiBackground = new PicInfo(addPic, bitmap.getWidth(), bitmap.getHeight());
                        }
                        if (bitmap != this.mBackgroundBitmap) {
                            bitmap.recycle();
                        }
                    } else {
                        videoObj.movePic2(this.mRenderInfo, 4, 0, 0, width, height);
                    }
                }
            }
        }
    }

    private void removeBackground() {
        if (this.mVideoScene != null && !this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.removePic(this.mRenderInfo, 4)) {
                this.mPiBackground = null;
            }
        }
    }

    private Bitmap createBackgroundBitmap() {
        Bitmap bitmap = this.mBackgroundBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        if (this.mBackground == null) {
            return null;
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            this.mBackground.setBounds(0, 0, this.mWidth - 1, this.mHeight - 1);
            this.mBackground.draw(canvas);
            return createBitmap;
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || !this.mbVisible || this.mOnClickListener == null) {
            return false;
        }
        if (motionEvent.getAction() != 0 || this.mbPressed || !isOnButton(motionEvent)) {
            if (motionEvent.getAction() == 1 && this.mbPressed) {
                this.mbPressed = false;
                if (isOnButton(motionEvent)) {
                    onClick();
                    return true;
                }
            }
            return this.mbPressed;
        }
        this.mbPressed = true;
        return true;
    }

    private boolean isOnButton(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (x < ((float) getLeft()) || x > ((float) (getLeft() + getWidth())) || y < ((float) getTop()) || y > ((float) (getTop() + getHeight()))) {
            return false;
        }
        return true;
    }

    private void onClick() {
        OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }
}
