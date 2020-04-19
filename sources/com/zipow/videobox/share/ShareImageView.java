package com.zipow.videobox.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.ImageUtil;
import p021us.zoom.androidlib.widget.TouchImageView;
import p021us.zoom.androidlib.widget.TouchImageView.OnViewPortChangedListener;
import p021us.zoom.videomeetings.C4558R;

public class ShareImageView extends ShareBaseView implements OnViewPortChangedListener {
    public static final int MAX_IMAGE_WIDTH_HEIGHT = 1280;
    @Nullable
    private Bitmap mBitmap;
    private Context mContext;
    private TouchImageView mImage;
    private View mToolbar;
    @Nullable
    private Uri mUri;

    public ShareImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ShareImageView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ShareImageView(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(C4558R.layout.zm_share_image_view, null, false);
        this.mImage = (TouchImageView) inflate.findViewById(C4558R.C4560id.imageview);
        this.mToolbar = inflate.findViewById(C4558R.C4560id.shareImageToolbar);
        this.mImage.setOnViewPortChangedListener(this);
        addView(inflate);
    }

    public boolean setImageUri(@Nullable Uri uri) {
        if (uri == null) {
            return false;
        }
        this.mUri = uri;
        return setImageBitmap(ImageUtil.translateImageAsSmallBitmap(getContext(), uri, (int) MAX_IMAGE_WIDTH_HEIGHT, false));
    }

    public boolean setImageBitmap(@Nullable Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        }
        this.mBitmap = bitmap;
        this.mImage.setImageBitmap(this.mBitmap);
        return true;
    }

    public boolean showWhiteboard() {
        this.mImage.setBackgroundColor(-1);
        return true;
    }

    public void drawShareContent(@Nullable Canvas canvas) {
        if (canvas != null) {
            this.mImage.draw(canvas);
        }
    }

    public int getShareContentWidth() {
        return this.mImage.getWidth();
    }

    public int getShareContentHeight() {
        return this.mImage.getHeight();
    }

    public void onViewPortChanged() {
        notifyRefresh();
    }

    public void setDrawingMode(boolean z) {
        if (z) {
            this.mToolbar.setVisibility(0);
        } else {
            this.mToolbar.setVisibility(8);
        }
    }
}
