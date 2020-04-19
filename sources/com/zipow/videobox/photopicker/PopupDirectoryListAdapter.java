package com.zipow.videobox.photopicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.zipow.videobox.photopicker.entity.PhotoDirectory;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.videomeetings.C4558R;

public class PopupDirectoryListAdapter extends BaseAdapter {
    private List<PhotoDirectory> directories = new ArrayList();
    /* access modifiers changed from: private */
    public RequestManager glide;

    private class ViewHolder {
        public ImageView ivCover;
        public TextView tvCount;
        public TextView tvName;

        public ViewHolder(View view) {
            this.ivCover = (ImageView) view.findViewById(C4558R.C4560id.iv_dir_cover);
            this.tvName = (TextView) view.findViewById(C4558R.C4560id.tv_dir_name);
            this.tvCount = (TextView) view.findViewById(C4558R.C4560id.tv_dir_count);
        }

        public void bindData(@NonNull PhotoDirectory photoDirectory) {
            RequestOptions requestOptions = new RequestOptions();
            int i = (int) (this.ivCover.getResources().getDisplayMetrics().density * 250.0f);
            requestOptions.dontAnimate().dontTransform().override(i, i).placeholder(C4558R.C4559drawable.zm_image_placeholder).error(C4558R.C4559drawable.zm_image_download_error);
            if (OsUtil.isAtLeastQ()) {
                PopupDirectoryListAdapter.this.glide.setDefaultRequestOptions(requestOptions).load(photoDirectory.getCoverUri()).thumbnail(0.1f).into(this.ivCover);
            } else {
                PopupDirectoryListAdapter.this.glide.setDefaultRequestOptions(requestOptions).load(photoDirectory.getCoverPath()).thumbnail(0.1f).into(this.ivCover);
            }
            this.tvName.setText(photoDirectory.getName());
            TextView textView = this.tvCount;
            textView.setText(textView.getContext().getString(C4558R.string.zm_picker_image_count, new Object[]{Integer.valueOf(photoDirectory.getPhotos().size())}));
        }
    }

    public PopupDirectoryListAdapter(RequestManager requestManager, List<PhotoDirectory> list) {
        this.directories = list;
        this.glide = requestManager;
    }

    public int getCount() {
        return this.directories.size();
    }

    public PhotoDirectory getItem(int i) {
        return (PhotoDirectory) this.directories.get(i);
    }

    public long getItemId(int i) {
        return (long) ((PhotoDirectory) this.directories.get(i)).hashCode();
    }

    @Nullable
    public View getView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_picker_item_directory, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.bindData((PhotoDirectory) this.directories.get(i));
        return view;
    }
}
