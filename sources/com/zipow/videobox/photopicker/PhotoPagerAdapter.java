package com.zipow.videobox.photopicker;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.view.ZMGifView;
import java.io.File;
import java.util.List;
import org.apache.http.HttpHost;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.AndroidLifecycleUtils;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhotoPagerAdapter extends PagerAdapter {
    private List<String> mAllPaths;
    private RequestManager mGlide;
    /* access modifiers changed from: private */
    public OnPagerItemClickListener mItemClickListener;

    public int getItemPosition(@NonNull Object obj) {
        return -2;
    }

    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    public PhotoPagerAdapter(RequestManager requestManager, List<String> list, OnPagerItemClickListener onPagerItemClickListener) {
        this.mAllPaths = list;
        this.mGlide = requestManager;
        this.mItemClickListener = onPagerItemClickListener;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        boolean z;
        Uri uri;
        Context context = viewGroup.getContext();
        View inflate = LayoutInflater.from(context).inflate(C4558R.layout.zm_picker_picker_item_pager, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.viewImage);
        ZMGifView zMGifView = (ZMGifView) inflate.findViewById(C4558R.C4560id.viewGif);
        String str = (String) this.mAllPaths.get(i);
        if (str.startsWith(HttpHost.DEFAULT_SCHEME_NAME)) {
            uri = Uri.parse(str);
            z = false;
        } else if (str.startsWith("content:")) {
            uri = Uri.parse(str);
            String guessContentTypeFromUri = FileUtils.guessContentTypeFromUri(context, uri);
            z = !StringUtil.isEmptyOrNull(guessContentTypeFromUri) ? AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(guessContentTypeFromUri) : false;
        } else {
            uri = Uri.fromFile(new File(str));
            z = false;
        }
        boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);
        boolean equals = AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(ImageUtil.getImageMimeType(str));
        if (canLoadImage) {
            if (equals) {
                if (PTApp.getInstance().getZoomFileInfoChecker() == null || !PTApp.getInstance().getZoomFileInfoChecker().isLegalGif(str)) {
                    zMGifView.setVisibility(8);
                    imageView.setVisibility(0);
                    imageView.setImageResource(C4558R.C4559drawable.zm_image_download_error);
                } else {
                    zMGifView.setVisibility(0);
                    imageView.setVisibility(8);
                    zMGifView.setGifResourse(str);
                }
            } else if (z) {
                zMGifView.setVisibility(0);
                imageView.setVisibility(8);
                zMGifView.setGifResourse(str);
            } else {
                zMGifView.setVisibility(8);
                imageView.setVisibility(0);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate().dontTransform().override(800, 800).placeholder(C4558R.C4559drawable.zm_image_placeholder).error(C4558R.C4559drawable.zm_image_download_error);
                this.mGlide.setDefaultRequestOptions(requestOptions).load(uri).thumbnail(0.1f).into(imageView);
            }
        }
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (PhotoPagerAdapter.this.mItemClickListener != null) {
                    PhotoPagerAdapter.this.mItemClickListener.onItemClick(view);
                }
            }
        });
        viewGroup.addView(inflate);
        return inflate;
    }

    public int getCount() {
        return this.mAllPaths.size();
    }

    public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
        View view = (View) obj;
        viewGroup.removeView(view);
        this.mGlide.clear(view);
    }
}
