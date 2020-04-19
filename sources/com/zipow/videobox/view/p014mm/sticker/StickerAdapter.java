package com.zipow.videobox.view.p014mm.sticker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import com.zipow.videobox.view.p014mm.sticker.StickerPanelView.OnStickerEventLisener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;

/* renamed from: com.zipow.videobox.view.mm.sticker.StickerAdapter */
public class StickerAdapter extends PagerAdapter implements OnStickerEventLisener {
    private Context mContext;
    private StickerInputView mEmojiInputView;
    private List<StickerPanelView> mPanelStickerViews;

    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    public StickerAdapter(Context context, List<StickerPanelView> list, StickerInputView stickerInputView) {
        this.mContext = context;
        this.mPanelStickerViews = list;
        this.mEmojiInputView = stickerInputView;
        registerListener();
    }

    public void updatePanelStickerViews(@Nullable List<StickerPanelView> list) {
        if (list != null) {
            List<StickerPanelView> list2 = this.mPanelStickerViews;
            if (list2 == null) {
                this.mPanelStickerViews = new ArrayList();
            } else {
                list2.clear();
            }
            this.mPanelStickerViews.addAll(list);
            registerListener();
        }
    }

    private void registerListener() {
        if (!CollectionsUtil.isListEmpty(this.mPanelStickerViews)) {
            for (StickerPanelView onStickerEventListener : this.mPanelStickerViews) {
                onStickerEventListener.setOnStickerEventListener(this);
            }
        }
    }

    public void onStickerDownloaded(String str, int i) {
        List<StickerPanelView> list = this.mPanelStickerViews;
        if (list != null) {
            for (StickerPanelView onStickerDownloaded : list) {
                onStickerDownloaded.onStickerDownloaded(str, i);
            }
        }
    }

    @Nullable
    public StickerPanelView getItem(int i) {
        if (!CollectionsUtil.isListEmpty(this.mPanelStickerViews) && i >= 0 && i < this.mPanelStickerViews.size()) {
            return (StickerPanelView) this.mPanelStickerViews.get(i);
        }
        return null;
    }

    public int getItemPosition(@NonNull Object obj) {
        List<StickerPanelView> list = this.mPanelStickerViews;
        return (list == null || list.contains(obj)) ? -1 : -2;
    }

    public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
        List<StickerPanelView> list = this.mPanelStickerViews;
        if (list != null && list.size() > i) {
            viewGroup.removeView((View) this.mPanelStickerViews.get(i));
        }
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        List<StickerPanelView> list = this.mPanelStickerViews;
        if (list == null || list.size() <= i) {
            throw new NullPointerException();
        }
        View view = (View) this.mPanelStickerViews.get(i);
        if (view == null) {
            view = new View(this.mContext);
        }
        viewGroup.addView(view);
        return view;
    }

    public int getCount() {
        if (CollectionsUtil.isListEmpty(this.mPanelStickerViews)) {
            return 0;
        }
        return this.mPanelStickerViews.size();
    }

    public void onStickerEvent(StickerEvent stickerEvent) {
        StickerInputView stickerInputView = this.mEmojiInputView;
        if (stickerInputView != null) {
            stickerInputView.onStickerEvent(stickerEvent);
        }
    }
}
