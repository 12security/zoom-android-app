package com.zipow.videobox.photopicker;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.util.List;
import p021us.zoom.androidlib.util.AndroidLifecycleUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhotoHorizentalAdapter extends Adapter<ViewHolder> {
    /* access modifiers changed from: private */
    public int currentItem;
    private int imageSize = 0;
    /* access modifiers changed from: private */
    public OnPhotoPickerHoriItemCallback mCallback;
    private RequestManager mGlide;
    /* access modifiers changed from: private */
    public List<String> mSelectedPath;

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        View coverView;
        ImageView imageView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(C4558R.C4560id.iv_photo);
            this.coverView = view.findViewById(C4558R.C4560id.cover);
        }
    }

    public PhotoHorizentalAdapter(RequestManager requestManager, List<String> list, OnPhotoPickerHoriItemCallback onPhotoPickerHoriItemCallback) {
        this.mSelectedPath = list;
        this.mGlide = requestManager;
        this.mCallback = onPhotoPickerHoriItemCallback;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_picker_horizental_item_photo, viewGroup, false));
        this.imageSize = viewGroup.getResources().getDimensionPixelSize(C4558R.dimen.zm_picker_bottom_photo_size);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if (AndroidLifecycleUtils.canLoadImage(viewHolder.imageView.getContext())) {
            RequestOptions requestOptions = new RequestOptions();
            RequestOptions dontAnimate = requestOptions.centerCrop().dontAnimate();
            int i2 = this.imageSize;
            dontAnimate.override(i2, i2).placeholder(C4558R.C4559drawable.zm_image_placeholder).error(C4558R.C4559drawable.zm_image_download_error);
            if (OsUtil.isAtLeastQ()) {
                this.mGlide.setDefaultRequestOptions(requestOptions).load(Uri.parse((String) this.mSelectedPath.get(i))).thumbnail(0.2f).into(viewHolder.imageView);
            } else {
                this.mGlide.setDefaultRequestOptions(requestOptions).load(new File((String) this.mSelectedPath.get(i))).thumbnail(0.2f).into(viewHolder.imageView);
            }
        }
        OnPhotoPickerHoriItemCallback onPhotoPickerHoriItemCallback = this.mCallback;
        boolean z = true;
        viewHolder.coverView.setVisibility(onPhotoPickerHoriItemCallback != null ? onPhotoPickerHoriItemCallback.canSelected((String) this.mSelectedPath.get(i), i) : true ? 8 : 0);
        viewHolder.imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (PhotoHorizentalAdapter.this.mCallback != null ? PhotoHorizentalAdapter.this.mCallback.canSelected((String) PhotoHorizentalAdapter.this.mSelectedPath.get(i), i) : true) {
                    PhotoHorizentalAdapter.this.currentItem = i;
                    if (PhotoHorizentalAdapter.this.mCallback != null) {
                        PhotoHorizentalAdapter.this.mCallback.onItemClick(view, (String) PhotoHorizentalAdapter.this.mSelectedPath.get(i), i);
                    }
                    PhotoHorizentalAdapter.this.notifyDataSetChanged();
                }
            }
        });
        if (this.currentItem != i) {
            z = false;
        }
        viewHolder.imageView.setSelected(z);
    }

    public int getItemCount() {
        return this.mSelectedPath.size();
    }

    public void onViewRecycled(@NonNull ViewHolder viewHolder) {
        this.mGlide.clear((View) viewHolder.imageView);
        super.onViewRecycled(viewHolder);
    }

    public void setCurrentItem(int i) {
        this.currentItem = i;
        notifyDataSetChanged();
    }
}
