package com.zipow.videobox.view.p014mm.sticker;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.LazyLoadDrawable;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMSquareImageView;
import p021us.zoom.androidlib.widget.ZMViewPager.Page;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.PrivateStickerPanelView */
public class PrivateStickerPanelView extends StickerPanelView implements OnClickListener, OnLongClickListener, OnTouchListener, Page {
    private static final String PREFIX_DOWNLOADING = "Down Loading";
    private PrivateStickerPreviewHelper mPreViewHelper;
    private List<StickerEvent> mStickers;

    public int getCategory() {
        return 2;
    }

    public int getMaxStickerSize() {
        return 9;
    }

    public PrivateStickerPanelView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setGravity(17);
        setOrientation(1);
        this.mPreViewHelper = new PrivateStickerPreviewHelper(getContext());
    }

    public void setContent(List<StickerEvent> list) {
        this.mStickers = list;
        createPanel();
    }

    public void onStickerDownloaded(String str, int i) {
        if (i == 0) {
            String stickerPreviewFileIdByReqId = StickerManager.getStickerPreviewFileIdByReqId(str);
            if (StringUtil.isEmptyOrNull(stickerPreviewFileIdByReqId)) {
                stickerPreviewFileIdByReqId = StickerManager.getStickerLocalPathFileIdByReqId(str);
            }
            if (!StringUtil.isEmptyOrNull(stickerPreviewFileIdByReqId)) {
                updatePrivateSticker(stickerPreviewFileIdByReqId);
                PrivateStickerPreviewHelper privateStickerPreviewHelper = this.mPreViewHelper;
                if (privateStickerPreviewHelper != null && privateStickerPreviewHelper.isShowing() && StringUtil.isSameString(stickerPreviewFileIdByReqId, this.mPreViewHelper.getCurrentFileId())) {
                    for (int i2 = 0; i2 < getChildCount(); i2++) {
                        ViewGroup viewGroup = (ViewGroup) getChildAt(i2);
                        for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                            View childAt = viewGroup.getChildAt(i3);
                            Object tag = childAt.getTag();
                            if ((tag instanceof StickerEvent) && StringUtil.isSameString(((StickerEvent) tag).getStickerId(), stickerPreviewFileIdByReqId)) {
                                this.mPreViewHelper.showPreview(childAt, stickerPreviewFileIdByReqId);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updatePrivateSticker(@NonNull String str) {
        for (int i = 0; i < getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) getChildAt(i);
            for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
                Object tag = viewGroup.getChildAt(i2).getTag();
                if (tag instanceof String) {
                    String str2 = (String) tag;
                    if (str2.endsWith(PREFIX_DOWNLOADING) && str2.startsWith(str)) {
                        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                        if (zoomFileContentMgr != null) {
                            ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                            if (fileWithWebFileID != null) {
                                String picturePreviewPath = fileWithWebFileID.getPicturePreviewPath();
                                String localPath = fileWithWebFileID.getLocalPath();
                                if (StringUtil.isEmptyOrNull(picturePreviewPath)) {
                                    picturePreviewPath = localPath;
                                }
                                if (!StringUtil.isEmptyOrNull(picturePreviewPath)) {
                                    zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                                    View createStickerCell = createStickerCell(new StickerEvent(str), zoomFileContentMgr, PTApp.getInstance().getZoomPrivateStickerMgr());
                                    int dip2px = UIUtil.dip2px(getContext(), 4.0f);
                                    LayoutParams layoutParams = new LayoutParams(0, -1);
                                    layoutParams.weight = 1.0f;
                                    layoutParams.leftMargin = dip2px;
                                    layoutParams.rightMargin = dip2px;
                                    viewGroup.removeViewAt(i2);
                                    viewGroup.addView(createStickerCell, i2, layoutParams);
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    private void createPanel() {
        View view;
        if (this.mStickers != null) {
            ArrayList arrayList = new ArrayList();
            for (StickerEvent stickerEvent : this.mStickers) {
                if (stickerEvent.getEventType() == 3) {
                    arrayList.add(stickerEvent);
                }
            }
            removeAllViews();
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            LayoutParams layoutParams = new LayoutParams(-1, UIUtil.dip2px(getContext(), 70.0f));
            layoutParams.bottomMargin = UIUtil.dip2px(getContext(), 5.0f);
            addView(linearLayout, layoutParams);
            int dip2px = UIUtil.dip2px(getContext(), 4.0f);
            LinearLayout linearLayout2 = new LinearLayout(getContext());
            linearLayout2.setGravity(17);
            ZMSquareImageView zMSquareImageView = new ZMSquareImageView(getContext());
            zMSquareImageView.setScaleType(ScaleType.FIT_CENTER);
            zMSquareImageView.setImageResource(C4558R.C4559drawable.zm_mm_sticker_setting);
            linearLayout2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MMPrivateStickerFragment.showAsActivity((ZMActivity) PrivateStickerPanelView.this.getContext());
                }
            });
            LayoutParams layoutParams2 = new LayoutParams(-1, -1);
            layoutParams2.gravity = 17;
            linearLayout2.addView(zMSquareImageView, layoutParams2);
            LayoutParams layoutParams3 = new LayoutParams(0, -1);
            layoutParams3.weight = 1.0f;
            layoutParams3.leftMargin = dip2px;
            layoutParams3.rightMargin = dip2px;
            linearLayout.addView(linearLayout2, layoutParams3);
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
                if (zoomPrivateStickerMgr != null) {
                    LinearLayout linearLayout3 = linearLayout;
                    for (int i = 0; i < 9; i++) {
                        if (linearLayout3.getChildCount() == 5) {
                            linearLayout3 = new LinearLayout(getContext());
                            linearLayout3.setOrientation(0);
                            linearLayout3.setGravity(16);
                            addView(linearLayout3, new LayoutParams(-1, UIUtil.dip2px(getContext(), 70.0f)));
                        }
                        if (i < arrayList.size()) {
                            view = createStickerCell((StickerEvent) arrayList.get(i), zoomFileContentMgr, zoomPrivateStickerMgr);
                        } else {
                            view = new ImageView(getContext());
                        }
                        LayoutParams layoutParams4 = new LayoutParams(0, -1);
                        layoutParams4.weight = 1.0f;
                        layoutParams4.leftMargin = dip2px;
                        layoutParams4.rightMargin = dip2px;
                        linearLayout3.addView(view, layoutParams4);
                    }
                }
            }
        }
    }

    @NonNull
    private View createStickerCell(@Nullable StickerEvent stickerEvent, @Nullable MMFileContentMgr mMFileContentMgr, @Nullable MMPrivateStickerMgr mMPrivateStickerMgr) {
        String str;
        LinearLayout linearLayout;
        if (stickerEvent == null || StringUtil.isEmptyOrNull(stickerEvent.getStickerId()) || mMFileContentMgr == null || mMPrivateStickerMgr == null) {
            return new ImageView(getContext());
        }
        ZoomFile fileWithWebFileID = mMFileContentMgr.getFileWithWebFileID(stickerEvent.getStickerId());
        if (fileWithWebFileID == null && StringUtil.isEmptyOrNull(stickerEvent.getStickerPath())) {
            return new ImageView(getContext());
        }
        String stickerPath = stickerEvent.getStickerPath();
        if (fileWithWebFileID == null) {
            str = null;
        } else {
            str = fileWithWebFileID.getLocalPath();
        }
        if (StringUtil.isEmptyOrNull(stickerPath) && fileWithWebFileID != null) {
            stickerPath = fileWithWebFileID.getPicturePreviewPath();
            if (StringUtil.isEmptyOrNull(stickerPath)) {
                stickerPath = str;
            }
        }
        if (StringUtil.isEmptyOrNull(stickerPath) || !ImageUtil.isValidImageFile(stickerPath)) {
            if (!StickerManager.isStickerPreviewDownloading(stickerEvent.getStickerId())) {
                String downloadStickerPreview = mMPrivateStickerMgr.downloadStickerPreview(stickerEvent.getStickerId());
                if (!StringUtil.isEmptyOrNull(downloadStickerPreview)) {
                    StickerManager.addStickerPreviewReqId(stickerEvent.getStickerId(), downloadStickerPreview);
                }
            }
            linearLayout = new LinearLayout(getContext());
            ProgressBar progressBar = new ProgressBar(getContext());
            linearLayout.setGravity(17);
            linearLayout.addView(progressBar);
            StringBuilder sb = new StringBuilder();
            sb.append(stickerEvent.getStickerId());
            sb.append(PREFIX_DOWNLOADING);
            linearLayout.setTag(sb.toString());
        } else {
            linearLayout = new LinearLayout(getContext());
            int dip2px = UIUtil.dip2px(getContext(), 2.0f);
            linearLayout.setPadding(dip2px, dip2px, dip2px, dip2px);
            linearLayout.setBackgroundResource(C4558R.C4559drawable.zm_mm_private_sticker_bg);
            linearLayout.setGravity(17);
            ZMSquareImageView zMSquareImageView = new ZMSquareImageView(getContext());
            zMSquareImageView.setScaleType(ScaleType.FIT_CENTER);
            LazyLoadDrawable lazyLoadDrawable = new LazyLoadDrawable(stickerPath);
            lazyLoadDrawable.setMaxArea(UIUtil.dip2px(getContext(), 1600.0f));
            zMSquareImageView.setImageDrawable(lazyLoadDrawable);
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            layoutParams.gravity = 17;
            linearLayout.addView(zMSquareImageView, layoutParams);
            linearLayout.setTag(stickerEvent);
            linearLayout.setOnClickListener(this);
            linearLayout.setOnLongClickListener(this);
            linearLayout.setOnTouchListener(this);
        }
        if (fileWithWebFileID != null && NetworkUtil.getDataNetworkType(getContext()) == 1 && StringUtil.isEmptyOrNull(str) && !StickerManager.isStickerLocalPathDownloading(stickerEvent.getStickerId())) {
            String downloadSticker = mMPrivateStickerMgr.downloadSticker(stickerEvent.getStickerId(), PendingFileDataHelper.getContenFilePath(stickerEvent.getStickerId(), fileWithWebFileID.getFileName()));
            if (!StringUtil.isEmptyOrNull(downloadSticker)) {
                StickerManager.addStickerLocalPathReqId(stickerEvent.getStickerId(), downloadSticker);
            }
        }
        if (fileWithWebFileID != null) {
            mMFileContentMgr.destroyFileObject(fileWithWebFileID);
        }
        return linearLayout;
    }

    public void onClick(@NonNull View view) {
        onStickerOnClick(view);
    }

    private void onStickerOnClick(@NonNull View view) {
        if (this.mOnStickerEventLisener != null) {
            Object tag = view.getTag();
            if (tag instanceof StickerEvent) {
                this.mOnStickerEventLisener.onStickerEvent((StickerEvent) tag);
            }
        }
    }

    public boolean onLongClick(@NonNull View view) {
        if (getResources().getConfiguration().orientation != 1) {
            return false;
        }
        Object tag = view.getTag();
        if (tag instanceof StickerEvent) {
            this.mPreViewHelper.showPreview(view, ((StickerEvent) tag).getStickerId());
            view.setBackgroundResource(C4558R.C4559drawable.zm_mm_private_sticker_press_bg);
        }
        return false;
    }

    public boolean onTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
        Object tag = view.getTag();
        if (!(tag instanceof StickerEvent)) {
            return false;
        }
        StickerEvent stickerEvent = (StickerEvent) tag;
        if (stickerEvent.getStickerId() != null && stickerEvent.getStickerId().equals(this.mPreViewHelper.getCurrentFileId())) {
            if (motionEvent.getAction() == 1) {
                this.mPreViewHelper.hidePreview();
                view.setBackgroundResource(C4558R.C4559drawable.zm_mm_private_sticker_bg);
            } else {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                if (!rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                    this.mPreViewHelper.hidePreview();
                    view.setBackgroundResource(C4558R.C4559drawable.zm_mm_private_sticker_bg);
                }
            }
        }
        return false;
    }

    public boolean canScrollHorizontal(int i, int i2, int i3) {
        return this.mPreViewHelper.isShowing();
    }
}
