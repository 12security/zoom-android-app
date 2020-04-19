package com.zipow.videobox.confapp;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.IRendererUnit.PicInfo;
import com.zipow.videobox.view.video.AbsVideoScene;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;

public class ShareUnit implements IVideoUnit {
    private static final int PIC_BORDER = 2;
    @NonNull
    private static String TAG = "ShareUnit";
    @Nullable
    private Bitmap mBmpBorder;
    private boolean mHasBorder = false;
    private int mHeight;
    private boolean mIsBorderVisible = false;
    private boolean mIsDestroyed = false;
    private boolean mIsPaused = false;
    private int mLeft;
    @Nullable
    private PicInfo mPiBorder;
    private long mRenderInfo;
    private int mTop;
    @Nullable
    private String mUnitName = null;
    private long mUserId = 0;
    private AbsVideoScene mVideoScene;
    private int mWidth;

    public ShareUnit(long j, @Nullable RendererUnitInfo rendererUnitInfo) {
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

    public long getRendererInfo() {
        return this.mRenderInfo;
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

    public void pause() {
        if (!this.mIsPaused) {
            this.mIsPaused = true;
            if (this.mUserId != 0) {
                ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
                if (shareObj != null) {
                    shareObj.stopViewShareContent(this.mRenderInfo, false);
                }
            }
        }
    }

    public void resume() {
        if (this.mIsPaused) {
            this.mIsPaused = false;
            if (this.mUserId != 0) {
                ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
                if (shareObj != null) {
                    shareObj.showShareContent(this.mRenderInfo, this.mUserId, true);
                }
            }
        }
    }

    public boolean isPaused() {
        return this.mIsPaused;
    }

    public void updateUnitInfo(@Nullable RendererUnitInfo rendererUnitInfo) {
        if (rendererUnitInfo != null && !isSameInfo(rendererUnitInfo) && this.mVideoScene != null) {
            boolean z = (this.mWidth == rendererUnitInfo.width && this.mHeight == rendererUnitInfo.height) ? false : true;
            this.mLeft = rendererUnitInfo.left;
            this.mTop = rendererUnitInfo.top;
            this.mWidth = rendererUnitInfo.width;
            this.mHeight = rendererUnitInfo.height;
            if (isBorderVisible() && z) {
                destroyBorderResources();
                createBorderResources();
            }
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                shareObj.updateUnitLayout(this.mRenderInfo, rendererUnitInfo);
                if (this.mHasBorder) {
                    showBorder();
                }
            }
        }
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

    public void setUser(long j) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            long j2 = this.mUserId;
            if (!(j2 == 0 || j2 == j)) {
                removeUser();
            }
            this.mUserId = j;
            if (!this.mIsPaused) {
                shareObj.showShareContent(this.mRenderInfo, j, true);
            }
        }
    }

    public void setBorderVisible(boolean z) {
        if (this.mIsBorderVisible != z) {
            this.mIsBorderVisible = z;
            if (this.mUserId != 0) {
                if (this.mIsBorderVisible) {
                    createBorderResources();
                    showBorder();
                } else {
                    destroyBorderResources();
                    removeBorder();
                }
            }
        }
    }

    public boolean isBorderVisible() {
        return this.mIsBorderVisible;
    }

    private void createBorderResources() {
        destroyBorderResources();
        this.mBmpBorder = createBorderBitmap();
        this.mPiBorder = null;
    }

    private void destroyBorderResources() {
        Bitmap bitmap = this.mBmpBorder;
        if (bitmap != null) {
            bitmap.recycle();
            this.mBmpBorder = null;
            this.mPiBorder = null;
        }
    }

    public long getUser() {
        return this.mUserId;
    }

    public void removeUser() {
        this.mUserId = 0;
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.showShareContent(this.mRenderInfo, this.mUserId, false);
            if (this.mVideoScene != null) {
                removeBorder();
            }
        }
    }

    public void stopViewShareContent() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.stopViewShareContent(this.mRenderInfo, false);
        }
    }

    public void clearRenderer() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.clearRenderer(this.mRenderInfo);
        }
    }

    public void onIdle() {
        if (isBorderVisible() && !this.mHasBorder) {
            showBorder();
        }
    }

    public void onCreate() {
        if (isBorderVisible() && this.mBmpBorder == null) {
            createBorderResources();
        }
        this.mIsDestroyed = false;
    }

    public void onDestroy() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.destroyShareUnit(this);
            this.mIsDestroyed = true;
        }
    }

    public void onGLViewSizeChanged(int i, int i2) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.glViewSizeChanged(this.mRenderInfo, i, i2);
        }
    }

    public void destAreaChanged(int i, int i2, int i3, int i4) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.destAreaChanged(this.mRenderInfo, i, i2, i3, i4);
        }
    }

    private void showBorder() {
        int i;
        int i2;
        if (this.mBmpBorder != null && !this.mIsDestroyed) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                PicInfo picInfo = this.mPiBorder;
                if (picInfo != null) {
                    i2 = picInfo.bmpWidth;
                    i = this.mPiBorder.bmpHeight;
                } else {
                    i2 = this.mBmpBorder.getWidth();
                    i = this.mBmpBorder.getHeight();
                }
                if (this.mPiBorder == null) {
                    shareObj.removePic(this.mRenderInfo, 2);
                    long addPic = shareObj.addPic(this.mRenderInfo, 2, this.mBmpBorder, 255, 0, 0, 0, i2, i);
                    if (addPic != 0) {
                        this.mPiBorder = new PicInfo(addPic, this.mBmpBorder.getWidth(), this.mBmpBorder.getHeight());
                    }
                } else {
                    shareObj.movePic2(this.mRenderInfo, 2, 0, 0, i2, i);
                }
                this.mHasBorder = true;
            }
        }
    }

    private Bitmap createBorderBitmap() {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            float dip2px = (float) UIUtil.dip2px(VideoBoxApplication.getInstance(), 2.0f);
            Paint paint = new Paint();
            paint.setColor(-2039584);
            paint.setStrokeWidth(dip2px);
            paint.setStyle(Style.STROKE);
            float f = dip2px / 2.0f;
            canvas.drawRect(f, f, (((float) this.mWidth) - f) - 1.0f, (((float) this.mHeight) - f) - 1.0f, paint);
            return createBitmap;
        } catch (Exception unused) {
            return null;
        }
    }

    private void removeBorder() {
        if (this.mHasBorder && !this.mIsDestroyed) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null && shareObj.removePic(this.mRenderInfo, 2)) {
                this.mPiBorder = null;
                this.mHasBorder = false;
            }
        }
    }
}
