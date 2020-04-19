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

public class GLButton implements IRendererUnit {
    private static final int PIC_BACKGROUND_NORMAL = 4;
    private static final int PIC_BACKGROUND_PRESSED = 5;
    @NonNull
    private static String TAG = "GLButton";
    private Drawable mBackground;
    private int mHeight;
    private boolean mIsDestroyed = false;
    private int mLeft;
    private OnClickListener mOnClickListener;
    private boolean mPaused;
    @Nullable
    private PicInfo mPiBackgroundNormal;
    @Nullable
    private PicInfo mPiBackgroundPressed;
    private long mRenderInfo;
    private int mTop;
    @Nullable
    private String mUnitName = null;
    private AbsVideoScene mVideoScene;
    private int mWidth;
    private boolean mbPressed = false;
    private boolean mbVisible = true;

    public interface OnClickListener {
        void onClick(GLButton gLButton);
    }

    public void clearRenderer() {
    }

    public void onIdle() {
    }

    public GLButton(long j, @Nullable RendererUnitInfo rendererUnitInfo) {
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
            videoObj.destroyGLButton(this);
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

    public void setBackground(Drawable drawable) {
        if (this.mBackground != drawable) {
            this.mBackground = drawable;
            this.mPiBackgroundNormal = null;
            this.mPiBackgroundPressed = null;
            showBackground();
        }
    }

    public Drawable getBackgroundDrawable() {
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
        Bitmap bitmap;
        int i;
        int i2;
        int i3;
        int i4;
        if (this.mVideoScene != null) {
            if (!this.mbVisible || this.mBackground == null) {
                removeBackground();
            } else if (!this.mIsDestroyed) {
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    Drawable drawable = this.mBackground;
                    if (drawable != null) {
                        drawable.setState(new int[]{16842910});
                        PicInfo picInfo = this.mPiBackgroundNormal;
                        Bitmap bitmap2 = null;
                        if (picInfo != null) {
                            int i5 = picInfo.bmpWidth;
                            i2 = this.mPiBackgroundNormal.bmpHeight;
                            i = i5;
                            bitmap = null;
                        } else {
                            Bitmap createBackgroundBitmap = createBackgroundBitmap();
                            if (createBackgroundBitmap != null) {
                                bitmap = createBackgroundBitmap;
                                i = createBackgroundBitmap.getWidth();
                                i2 = createBackgroundBitmap.getHeight();
                            } else {
                                return;
                            }
                        }
                        int i6 = Integer.MIN_VALUE;
                        int i7 = this.mbPressed ? Integer.MIN_VALUE : 0;
                        int i8 = i7 + i2;
                        if (this.mPiBackgroundNormal == null) {
                            videoObj.removePic(this.mRenderInfo, 4);
                            long addPic = videoObj.addPic(this.mRenderInfo, 4, bitmap, 255, 0, 0, i7, i, i8);
                            if (addPic != 0) {
                                this.mPiBackgroundNormal = new PicInfo(addPic, bitmap.getWidth(), bitmap.getHeight());
                            }
                            bitmap.recycle();
                        } else {
                            videoObj.movePic2(this.mRenderInfo, 4, 0, i7, i, i8);
                        }
                        this.mBackground.setState(new int[]{16842919, 16842910});
                        PicInfo picInfo2 = this.mPiBackgroundPressed;
                        if (picInfo2 != null) {
                            int i9 = picInfo2.bmpWidth;
                            i4 = this.mPiBackgroundPressed.bmpHeight;
                            i3 = i9;
                        } else {
                            Bitmap createBackgroundBitmap2 = createBackgroundBitmap();
                            if (createBackgroundBitmap2 != null) {
                                bitmap2 = createBackgroundBitmap2;
                                i3 = createBackgroundBitmap2.getWidth();
                                i4 = createBackgroundBitmap2.getHeight();
                            } else {
                                return;
                            }
                        }
                        if (this.mbPressed) {
                            i6 = 0;
                        }
                        int i10 = i6 + i4;
                        if (this.mPiBackgroundPressed == null) {
                            videoObj.removePic(this.mRenderInfo, 5);
                            long addPic2 = videoObj.addPic(this.mRenderInfo, 5, bitmap2, 255, 0, 0, i6, i3, i10);
                            if (addPic2 != 0) {
                                this.mPiBackgroundPressed = new PicInfo(addPic2, bitmap2.getWidth(), bitmap2.getHeight());
                            }
                            bitmap2.recycle();
                        } else {
                            videoObj.movePic2(this.mRenderInfo, 5, 0, i6, i3, i10);
                        }
                    }
                }
            }
        }
    }

    private void removeBackground() {
        if (this.mVideoScene != null && !this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                if (videoObj.removePic(this.mRenderInfo, 4)) {
                    this.mPiBackgroundNormal = null;
                }
                if (videoObj.removePic(this.mRenderInfo, 5)) {
                    this.mPiBackgroundPressed = null;
                }
            }
        }
    }

    private Bitmap createBackgroundBitmap() {
        if (this.mBackground == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        this.mBackground.setBounds(0, 0, this.mWidth - 1, this.mHeight - 1);
        this.mBackground.draw(canvas);
        return createBitmap;
    }

    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || !this.mbVisible || this.mOnClickListener == null) {
            return false;
        }
        if (motionEvent.getAction() != 0 || this.mbPressed || !isOnButton(motionEvent)) {
            if (motionEvent.getAction() == 1 && this.mbPressed) {
                this.mbPressed = false;
                showBackground();
                if (isOnButton(motionEvent)) {
                    onClick();
                    return true;
                }
            }
            return this.mbPressed;
        }
        this.mbPressed = true;
        showBackground();
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
