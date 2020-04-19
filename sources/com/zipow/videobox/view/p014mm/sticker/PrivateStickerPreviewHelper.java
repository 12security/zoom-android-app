package com.zipow.videobox.view.p014mm.sticker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.LazyLoadDrawable;
import com.zipow.videobox.view.ZMGifView;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMPopupWindow;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.PrivateStickerPreviewHelper */
public class PrivateStickerPreviewHelper {
    private Context mContext;
    private String mFileId;
    /* access modifiers changed from: private */
    public ZMPopupWindow mPopWin;

    /* renamed from: com.zipow.videobox.view.mm.sticker.PrivateStickerPreviewHelper$PicPreview */
    static class PicPreview extends RelativeLayout {
        private ZMGifView mGifView;
        private ImageView mImgArrow;

        public PicPreview(Context context) {
            super(context);
            init();
        }

        public void setPicPath(@NonNull String str) {
            if (AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(ImageUtil.getImageMimeType(str))) {
                this.mGifView.setGifResourse(str);
            } else {
                this.mGifView.setImageDrawable(new LazyLoadDrawable(str));
            }
        }

        public void setArrowXPosition(int i) {
            ((LayoutParams) this.mImgArrow.getLayoutParams()).leftMargin = i - (ContextCompat.getDrawable(getContext(), C4558R.C4559drawable.zm_mm_sticker_preview_arrow).getIntrinsicWidth() / 2);
        }

        private void init() {
            LayoutParams layoutParams = new LayoutParams(-2, -2);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(C4558R.C4559drawable.zm_mm_sticker_preview_bg);
            addView(imageView, layoutParams);
            imageView.setId(C4558R.C4560id.stickerPreviewContent);
            this.mGifView = new ZMGifView(getContext());
            LayoutParams layoutParams2 = new LayoutParams(-1, -1);
            layoutParams2.addRule(5, C4558R.C4560id.stickerPreviewContent);
            layoutParams2.addRule(7, C4558R.C4560id.stickerPreviewContent);
            layoutParams2.addRule(6, C4558R.C4560id.stickerPreviewContent);
            layoutParams2.addRule(8, C4558R.C4560id.stickerPreviewContent);
            layoutParams2.topMargin = UIUtil.dip2px(getContext(), 10.0f);
            layoutParams2.bottomMargin = UIUtil.dip2px(getContext(), 20.0f);
            layoutParams2.leftMargin = UIUtil.dip2px(getContext(), 3.0f);
            layoutParams2.rightMargin = UIUtil.dip2px(getContext(), 3.0f);
            addView(this.mGifView, layoutParams2);
            LayoutParams layoutParams3 = new LayoutParams(-2, -2);
            this.mImgArrow = new ImageView(getContext());
            this.mImgArrow.setImageResource(C4558R.C4559drawable.zm_mm_sticker_preview_arrow);
            layoutParams3.addRule(8, C4558R.C4560id.stickerPreviewContent);
            this.mImgArrow.setLayoutParams(layoutParams3);
            addView(this.mImgArrow, layoutParams3);
        }
    }

    public PrivateStickerPreviewHelper(Context context) {
        this.mContext = context;
    }

    public boolean isShowing() {
        ZMPopupWindow zMPopupWindow = this.mPopWin;
        return zMPopupWindow != null && zMPopupWindow.isShowing();
    }

    public String getCurrentFileId() {
        return this.mFileId;
    }

    public void hidePreview() {
        ZMPopupWindow zMPopupWindow = this.mPopWin;
        if (zMPopupWindow != null) {
            zMPopupWindow.dismiss();
        }
    }

    public void showPreview(@Nullable View view, @Nullable String str) {
        if (this.mContext != null && view != null && !StringUtil.isEmptyOrNull(str)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID != null) {
                    hidePreview();
                    String localPath = fileWithWebFileID.getLocalPath();
                    if (StringUtil.isEmptyOrNull(localPath) || !ImageUtil.isValidImageFile(localPath)) {
                        if (!StickerManager.isStickerLocalPathDownloading(str)) {
                            MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
                            if (zoomPrivateStickerMgr != null) {
                                StickerManager.addStickerLocalPathReqId(str, zoomPrivateStickerMgr.downloadSticker(str, PendingFileDataHelper.getContenFilePath(str, fileWithWebFileID.getFileName())));
                            }
                        }
                        localPath = fileWithWebFileID.getPicturePreviewPath();
                        if (StringUtil.isEmptyOrNull(localPath)) {
                            localPath = null;
                        }
                    }
                    zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                    if (!StringUtil.isEmptyOrNull(localPath)) {
                        RelativeLayout relativeLayout = new RelativeLayout(this.mContext);
                        this.mPopWin = new ZMPopupWindow((View) relativeLayout, -1, -1);
                        this.mPopWin.setDismissOnTouchOutside(true);
                        relativeLayout.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                PrivateStickerPreviewHelper.this.mPopWin.dismiss();
                            }
                        });
                        Rect rect = new Rect();
                        view.getGlobalVisibleRect(rect);
                        Context context = this.mContext;
                        boolean isFullScreen = context instanceof Activity ? UIUtil.isFullScreen((Activity) context) : false;
                        int i = (rect.left + rect.right) / 2;
                        PicPreview picPreview = new PicPreview(this.mContext);
                        picPreview.setPicPath(localPath);
                        picPreview.measure(0, 0);
                        int measuredWidth = picPreview.getMeasuredWidth();
                        LayoutParams layoutParams = new LayoutParams(-2, -2);
                        layoutParams.topMargin = (rect.top - (isFullScreen ? 0 : UIUtil.getStatusBarHeight(this.mContext))) - picPreview.getMeasuredHeight();
                        int displayWidth = UIUtil.getDisplayWidth(this.mContext);
                        int dip2px = UIUtil.dip2px(this.mContext, 10.0f);
                        int i2 = measuredWidth / 2;
                        if (i + i2 > displayWidth - dip2px) {
                            layoutParams.leftMargin = (displayWidth - measuredWidth) - dip2px;
                        } else {
                            int i3 = i - i2;
                            if (i3 < dip2px) {
                                layoutParams.leftMargin = dip2px;
                            } else {
                                layoutParams.leftMargin = i3;
                            }
                        }
                        picPreview.setArrowXPosition(((rect.left + rect.right) / 2) - layoutParams.leftMargin);
                        relativeLayout.addView(picPreview, layoutParams);
                        this.mPopWin.showAtLocation(view.getRootView(), 48, 0, 0);
                        this.mFileId = str;
                    }
                }
            }
        }
    }
}
