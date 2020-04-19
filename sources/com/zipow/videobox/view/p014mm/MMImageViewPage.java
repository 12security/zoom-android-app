package com.zipow.videobox.view.p014mm;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.view.ZMGifView;
import java.io.File;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.TouchImageView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMImageViewPage */
public class MMImageViewPage extends ZMFragment implements OnClickListener {
    private static final int MAX_IMAGE_SIZE_IN_AREA = 1000000;
    private static final String TAG = "MMImageViewPage";
    @Nullable
    private Bitmap mBitmap = null;
    @Nullable
    private String mFilePath;
    private ZMGifView mGifview;
    @NonNull
    private Handler mHandler = new Handler();
    private TouchImageView mImageView;
    private ImageViewPageListener mImageViewPageListener;
    private boolean mIsFullSizeImage = false;
    private boolean mIsImageLoaded = false;
    @Nullable
    private String mMessageId = null;
    /* access modifiers changed from: private */
    public View mProgressBar;
    @Nullable
    private String mSessionId = null;
    /* access modifiers changed from: private */
    public TextView mTxtMessage;
    /* access modifiers changed from: private */
    public View mViewPlaceHolder;

    /* renamed from: com.zipow.videobox.view.mm.MMImageViewPage$ImageViewPageListener */
    public interface ImageViewPageListener {
        void downloadImage(String str, String str2);
    }

    private static boolean isUploadState(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            default:
                return false;
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        if (bundle != null) {
            this.mSessionId = bundle.getString("mSessionId");
            this.mMessageId = bundle.getString("mMessageId");
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_image_viewer_page, viewGroup, false);
        this.mImageView = (TouchImageView) inflate.findViewById(C4558R.C4560id.imageview);
        this.mProgressBar = inflate.findViewById(C4558R.C4560id.progressBar1);
        this.mViewPlaceHolder = inflate.findViewById(C4558R.C4560id.viewPlaceHolder);
        this.mTxtMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        this.mGifview = (ZMGifView) inflate.findViewById(C4558R.C4560id.gifview);
        if (this.mIsFullSizeImage) {
            String str = this.mFilePath;
            if (str != null && AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(ImageUtil.getImageMimeType(str))) {
                this.mGifview.setGifResourse(this.mFilePath);
                this.mGifview.setVisibility(0);
                this.mImageView.setVisibility(8);
                this.mViewPlaceHolder.setOnClickListener(this);
                return inflate;
            }
        }
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            this.mImageView.setImageBitmap(bitmap);
        }
        this.mGifview.setVisibility(8);
        this.mImageView.setVisibility(0);
        this.mViewPlaceHolder.setOnClickListener(this);
        return inflate;
    }

    public void setImageViewPageListener(ImageViewPageListener imageViewPageListener) {
        this.mImageViewPageListener = imageViewPageListener;
    }

    public boolean isFullSizeImage() {
        return this.mIsFullSizeImage;
    }

    public boolean isImageLoaded() {
        return this.mIsImageLoaded;
    }

    @Nullable
    public String getMessageId() {
        return this.mMessageId;
    }

    private void setImage(Bitmap bitmap) {
        this.mBitmap = bitmap;
        TouchImageView touchImageView = this.mImageView;
        if (touchImageView != null) {
            touchImageView.setImageBitmap(bitmap);
        }
    }

    private void clearImage() {
        this.mIsFullSizeImage = false;
        this.mIsImageLoaded = false;
        this.mFilePath = null;
        setImage(null);
    }

    public void onResume() {
        super.onResume();
        if (!isFullSizeImage()) {
            String str = this.mMessageId;
            if (str != null) {
                loadImage(this.mSessionId, str);
                return;
            }
        }
        View view = this.mProgressBar;
        if (view != null) {
            view.setVisibility(8);
        }
        View view2 = this.mViewPlaceHolder;
        if (view2 != null) {
            view2.setVisibility(8);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mSessionId", this.mSessionId);
        bundle.putString("mMessageId", this.mMessageId);
    }

    public void setImageMessage(String str, String str2) {
        if (!StringUtil.isSameString(this.mSessionId, str) || !StringUtil.isSameString(this.mMessageId, str2)) {
            this.mSessionId = str;
            this.mMessageId = str2;
            this.mIsFullSizeImage = false;
            this.mIsImageLoaded = false;
            this.mFilePath = null;
        }
    }

    public boolean loadImage(@Nullable String str, @Nullable String str2) {
        if (StringUtil.isSameString(str, this.mSessionId) && StringUtil.isSameString(str2, this.mMessageId) && isFullSizeImage()) {
            return true;
        }
        clearImage();
        View view = this.mProgressBar;
        if (view != null) {
            view.setVisibility(8);
        }
        View view2 = this.mViewPlaceHolder;
        if (view2 != null) {
            view2.setVisibility(8);
        }
        if (str == null || str2 == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
        if (sessionById == null) {
            return false;
        }
        ZoomMessage messageById = sessionById.getMessageById(str2);
        if (messageById == null) {
            return false;
        }
        this.mSessionId = str;
        this.mMessageId = str2;
        String localFilePath = messageById.getLocalFilePath();
        FileTransferInfo fileTransferInfo = messageById.getFileTransferInfo();
        if (StringUtil.isEmptyOrNull(localFilePath) || !new File(localFilePath).exists() || fileTransferInfo == null || (fileTransferInfo.state != 13 && !isUploadState(fileTransferInfo.state))) {
            this.mFilePath = messageById.getPicturePreviewPath();
            String str3 = this.mFilePath;
            if (str3 != null && !new File(str3).exists()) {
                this.mFilePath = null;
            }
        } else {
            this.mFilePath = localFilePath;
            this.mIsFullSizeImage = true;
        }
        String str4 = this.mFilePath;
        if (str4 != null) {
            if (AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(ImageUtil.getImageMimeType(str4))) {
                ZMGifView zMGifView = this.mGifview;
                if (zMGifView != null) {
                    zMGifView.setVisibility(0);
                    this.mGifview.setGifResourse(this.mFilePath);
                }
                TouchImageView touchImageView = this.mImageView;
                if (touchImageView != null) {
                    touchImageView.setVisibility(8);
                }
                this.mIsImageLoaded = true;
                return true;
            }
            if (this.mImageView != null) {
                this.mGifview.setVisibility(8);
                this.mImageView.setVisibility(0);
            }
            Bitmap decodeFile = ZMBitmapFactory.decodeFile(this.mFilePath, MAX_IMAGE_SIZE_IN_AREA, false, false);
            if (decodeFile != null) {
                setImage(decodeFile);
                this.mIsImageLoaded = true;
                checkShowDownloadingStatus(fileTransferInfo);
                return true;
            }
            View view3 = this.mViewPlaceHolder;
            if (view3 != null) {
                view3.setVisibility(0);
            }
            TextView textView = this.mTxtMessage;
            if (textView != null) {
                textView.setText(C4558R.string.zm_mm_msg_load_image_failed);
            }
        } else if (this.mImageView != null) {
            this.mGifview.setVisibility(8);
            this.mImageView.setVisibility(0);
        }
        this.mIsFullSizeImage = false;
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself != null && StringUtil.isSameString(messageById.getSenderID(), myself.getJid()) && (messageById.getMessageState() == 1 || messageById.getMessageState() == 4)) {
            View view4 = this.mViewPlaceHolder;
            if (view4 != null) {
                view4.setVisibility(0);
            }
            TextView textView2 = this.mTxtMessage;
            if (textView2 != null) {
                textView2.setText(C4558R.string.zm_mm_msg_load_image_failed);
            }
        }
        checkShowDownloadingStatus(fileTransferInfo);
        return false;
    }

    public void checkShowDownloadingStatus(@Nullable FileTransferInfo fileTransferInfo) {
        if (fileTransferInfo != null && fileTransferInfo.state == 10) {
            showDownloadingStatus();
        }
    }

    public void onStartDownloadingImage() {
        showDownloadingStatus();
    }

    public void showDownloadingStatus() {
        View view = this.mViewPlaceHolder;
        if (view != null) {
            view.setVisibility(8);
        }
        View view2 = this.mProgressBar;
        if (view2 != null) {
            view2.setVisibility(0);
        }
    }

    public void onDownloadImageFailed() {
        C37291 r0 = new Runnable() {
            public void run() {
                if (MMImageViewPage.this.mViewPlaceHolder != null) {
                    MMImageViewPage.this.mViewPlaceHolder.setVisibility(0);
                }
                if (MMImageViewPage.this.mProgressBar != null) {
                    MMImageViewPage.this.mProgressBar.setVisibility(8);
                }
                if (MMImageViewPage.this.mTxtMessage != null) {
                    MMImageViewPage.this.mTxtMessage.setText(C4558R.string.zm_mm_msg_download_image_failed);
                }
            }
        };
        if (isAdded()) {
            r0.run();
        } else {
            this.mHandler.post(r0);
        }
    }

    @Nullable
    public String getImageFilePath() {
        return this.mFilePath;
    }

    public void onClick(@Nullable View view) {
        if (view != null && view.getId() == C4558R.C4560id.viewPlaceHolder) {
            onClickViewPlaceHolder();
        }
    }

    private void onClickViewPlaceHolder() {
        ImageViewPageListener imageViewPageListener = this.mImageViewPageListener;
        if (imageViewPageListener != null) {
            imageViewPageListener.downloadImage(this.mSessionId, this.mMessageId);
        }
    }
}
