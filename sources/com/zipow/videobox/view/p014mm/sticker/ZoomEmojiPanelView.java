package com.zipow.videobox.view.p014mm.sticker;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.NonNull;
import com.zipow.videobox.util.EmojiHelper.EmojiIndex;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.ZoomEmojiPanelView */
public class ZoomEmojiPanelView extends StickerPanelView implements OnClickListener {
    private List<StickerEvent> mStickers;

    public int getCategory() {
        return 1;
    }

    public int getMaxStickerSize() {
        return 20;
    }

    public ZoomEmojiPanelView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setGravity(17);
        setOrientation(1);
    }

    private void createPanel() {
        if (!CollectionsUtil.isListEmpty(this.mStickers)) {
            ArrayList arrayList = new ArrayList();
            for (StickerEvent stickerEvent : this.mStickers) {
                if (stickerEvent.getEventType() == 1) {
                    arrayList.add(stickerEvent.getEmoji());
                }
            }
            if (arrayList.size() != 0) {
                LinearLayout linearLayout = null;
                for (int i = 0; i < 20; i++) {
                    if (linearLayout == null || linearLayout.getChildCount() == 7) {
                        linearLayout = new LinearLayout(getContext());
                        linearLayout.setOrientation(0);
                        linearLayout.setGravity(16);
                        addView(linearLayout, new LayoutParams(-1, UIUtil.dip2px(getContext(), 50.0f)));
                    }
                    ImageView imageView = new ImageView(getContext());
                    int dip2px = UIUtil.dip2px(getContext(), 8.0f);
                    imageView.setPadding(dip2px, dip2px, dip2px, dip2px);
                    imageView.setScaleType(ScaleType.FIT_CENTER);
                    imageView.setBackgroundColor(0);
                    if (i < arrayList.size()) {
                        EmojiIndex emojiIndex = (EmojiIndex) arrayList.get(i);
                        imageView.setImageResource(emojiIndex.getDrawResource());
                        imageView.setTag(emojiIndex);
                        imageView.setOnClickListener(this);
                    }
                    LayoutParams layoutParams = new LayoutParams(0, -1);
                    layoutParams.weight = 1.0f;
                    linearLayout.addView(imageView, layoutParams);
                }
                ImageView imageView2 = new ImageView(getContext());
                int dip2px2 = UIUtil.dip2px(getContext(), 8.0f);
                imageView2.setPadding(dip2px2, dip2px2, dip2px2, dip2px2);
                imageView2.setScaleType(ScaleType.FIT_CENTER);
                imageView2.setBackgroundColor(0);
                imageView2.setImageResource(C4558R.C4559drawable.zm_mm_delete_btn);
                imageView2.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (ZoomEmojiPanelView.this.mOnStickerEventLisener != null) {
                            ZoomEmojiPanelView.this.mOnStickerEventLisener.onStickerEvent(new StickerEvent());
                        }
                    }
                });
                LayoutParams layoutParams2 = new LayoutParams(0, -1);
                layoutParams2.weight = 1.0f;
                linearLayout.addView(imageView2, layoutParams2);
            }
        }
    }

    public void setContent(List<StickerEvent> list) {
        this.mStickers = list;
        createPanel();
    }

    public void onClick(@NonNull View view) {
        onEmojiClicker(view);
    }

    private void onEmojiClicker(View view) {
        Object tag = view.getTag();
        if (tag instanceof EmojiIndex) {
            EmojiIndex emojiIndex = (EmojiIndex) tag;
            if (this.mOnStickerEventLisener != null) {
                this.mOnStickerEventLisener.onStickerEvent(new StickerEvent(emojiIndex));
            }
        }
    }
}
