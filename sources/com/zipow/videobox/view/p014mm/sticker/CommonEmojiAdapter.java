package com.zipow.videobox.view.p014mm.sticker;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.zipow.videobox.view.EmojiTextView;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiAdapter */
public class CommonEmojiAdapter extends BaseRecyclerViewAdapter<CommonEmoji> {
    /* access modifiers changed from: private */
    public OnItemViewTouchListener mTouchListener;

    /* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiAdapter$OnItemViewTouchListener */
    public interface OnItemViewTouchListener {
        boolean onTouch(View view, MotionEvent motionEvent);
    }

    public CommonEmojiAdapter(Context context) {
        super(context);
    }

    /* access modifiers changed from: private */
    public boolean isEmptyItem(int i) {
        CommonEmoji commonEmoji = (CommonEmoji) getItem(i);
        return commonEmoji == null || commonEmoji.getOutput() == null || commonEmoji.getOutput().length() <= 0;
    }

    public int getItemViewType(int i) {
        return isEmptyItem(i) ? 4 : 2;
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BaseViewHolder(View.inflate(this.mContext, C4558R.layout.zm_mm_emoji_common_panel_item, null));
    }

    public void onBindViewHolder(@NonNull final BaseViewHolder baseViewHolder, int i) {
        CommonEmoji commonEmoji = (CommonEmoji) getItem(i);
        if (commonEmoji != null) {
            EmojiTextView emojiTextView = (EmojiTextView) baseViewHolder.itemView.findViewById(C4558R.C4560id.emojiTextView);
            if (baseViewHolder.getItemViewType() == 2) {
                emojiTextView.setText(commonEmoji.getOutput());
            } else {
                emojiTextView.setText("");
            }
            emojiTextView.setTag(commonEmoji);
            baseViewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    int adapterPosition = baseViewHolder.getAdapterPosition();
                    if (!CommonEmojiAdapter.this.isEmptyItem(adapterPosition) && CommonEmojiAdapter.this.mListener != null) {
                        CommonEmojiAdapter.this.mListener.onItemClick(baseViewHolder.itemView.findViewById(C4558R.C4560id.emojiTextView), adapterPosition);
                    }
                }
            });
            baseViewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    int adapterPosition = baseViewHolder.getAdapterPosition();
                    if (!CommonEmojiAdapter.this.isEmptyItem(adapterPosition) && CommonEmojiAdapter.this.mListener != null) {
                        return CommonEmojiAdapter.this.mListener.onItemLongClick(baseViewHolder.itemView.findViewById(C4558R.C4560id.emojiTextView), adapterPosition);
                    }
                    return false;
                }
            });
            baseViewHolder.itemView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(@NonNull View view, MotionEvent motionEvent) {
                    if (CommonEmojiAdapter.this.mTouchListener != null) {
                        return CommonEmojiAdapter.this.mTouchListener.onTouch(view.findViewById(C4558R.C4560id.emojiTextView), motionEvent);
                    }
                    return false;
                }
            });
        }
    }

    public void setOnItemViewTouchListener(OnItemViewTouchListener onItemViewTouchListener) {
        this.mTouchListener = onItemViewTouchListener;
    }
}
