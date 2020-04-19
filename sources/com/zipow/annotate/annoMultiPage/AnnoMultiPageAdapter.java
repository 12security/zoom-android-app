package com.zipow.annotate.annoMultiPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.annotate.AnnoPageInfo;
import java.io.File;
import java.util.List;
import p021us.zoom.androidlib.util.AndroidLifecycleUtils;
import p021us.zoom.videomeetings.C4558R;

public class AnnoMultiPageAdapter extends Adapter<WhiteboardViewHolder> {
    /* access modifiers changed from: private */
    public AnnoDataMgr mAnnoDataMgr = AnnoDataMgr.getInstance();
    private boolean mEditMode = false;
    private final RequestManager mGlideRequestManager;
    /* access modifiers changed from: private */
    public final AnnoMultiPagesFragment mMultiPagesForWhiteboardFragment;
    /* access modifiers changed from: private */
    public boolean mSaveMode = false;
    private final RequestOptions options = new RequestOptions();

    public static class WhiteboardViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public View mBtnCheckWhiteboard;
        /* access modifiers changed from: private */
        public View mBtnDelWhiteboard;
        /* access modifiers changed from: private */
        public ImageView mShowWhiteboard;

        public WhiteboardViewHolder(@NonNull View view) {
            super(view);
            this.mShowWhiteboard = (ImageView) view.findViewById(C4558R.C4560id.show_whiteboard);
            this.mBtnDelWhiteboard = view.findViewById(C4558R.C4560id.btn_del_whiteboard);
            this.mBtnCheckWhiteboard = view.findViewById(C4558R.C4560id.btn_check_whiteboard);
        }
    }

    public AnnoMultiPageAdapter(AnnoMultiPagesFragment annoMultiPagesFragment, RequestManager requestManager) {
        this.mMultiPagesForWhiteboardFragment = annoMultiPagesFragment;
        this.mGlideRequestManager = requestManager;
        this.options.centerCrop().dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).error(C4558R.C4559drawable.zm_image_download_error);
    }

    @NonNull
    public WhiteboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WhiteboardViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_multi_pages_for_whiteboard_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final WhiteboardViewHolder whiteboardViewHolder, int i) {
        AnnoDataMgr annoDataMgr = this.mAnnoDataMgr;
        List pageList = AnnoDataMgr.getPageList();
        whiteboardViewHolder.mBtnDelWhiteboard.setVisibility((!this.mEditMode || this.mSaveMode || pageList.size() <= 1) ? 8 : 0);
        final AnnoPageInfo annoPageInfo = (AnnoPageInfo) pageList.get(i);
        String pageSnapshotPath = this.mAnnoDataMgr.getPageSnapshotPath(annoPageInfo.mPageId);
        if (this.mSaveMode) {
            annoPageInfo.mbSaveSnapahot = true;
            whiteboardViewHolder.mBtnCheckWhiteboard.setVisibility(0);
            whiteboardViewHolder.mShowWhiteboard.setSelected(true);
        }
        if (AndroidLifecycleUtils.canLoadImage(whiteboardViewHolder.mShowWhiteboard.getContext())) {
            this.mGlideRequestManager.setDefaultRequestOptions(this.options).load(new File(pageSnapshotPath)).thumbnail(0.5f).into(whiteboardViewHolder.mShowWhiteboard);
        }
        whiteboardViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnnoMultiPageAdapter.this.mSaveMode) {
                    AnnoMultiPageAdapter.this.toggleAnnoPageInfo(annoPageInfo, whiteboardViewHolder);
                } else if (!AnnoMultiPageAdapter.this.isEditMode()) {
                    AnnoMultiPageAdapter.this.mAnnoDataMgr.switchPage((long) annoPageInfo.mPageId);
                    AnnoMultiPageAdapter.this.mMultiPagesForWhiteboardFragment.dismiss();
                }
            }
        });
        whiteboardViewHolder.mBtnDelWhiteboard.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnnoMultiPageAdapter.this.mAnnoDataMgr.closePage(annoPageInfo.mPageId);
                AnnoMultiPageAdapter.this.notifyDataSetChanged();
            }
        });
        whiteboardViewHolder.mBtnCheckWhiteboard.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnnoMultiPageAdapter.this.toggleAnnoPageInfo(annoPageInfo, whiteboardViewHolder);
            }
        });
    }

    /* access modifiers changed from: private */
    public void toggleAnnoPageInfo(AnnoPageInfo annoPageInfo, WhiteboardViewHolder whiteboardViewHolder) {
        annoPageInfo.mbSaveSnapahot = !annoPageInfo.mbSaveSnapahot;
        whiteboardViewHolder.mBtnCheckWhiteboard.setVisibility(annoPageInfo.mbSaveSnapahot ? 0 : 8);
        whiteboardViewHolder.mShowWhiteboard.setSelected(annoPageInfo.mbSaveSnapahot);
    }

    public boolean isEditMode() {
        return this.mEditMode;
    }

    public void setShowMode(boolean z, boolean z2) {
        this.mEditMode = z;
        this.mSaveMode = z2;
    }

    public int getItemCount() {
        AnnoDataMgr annoDataMgr = this.mAnnoDataMgr;
        return AnnoDataMgr.getPageList().size();
    }
}
