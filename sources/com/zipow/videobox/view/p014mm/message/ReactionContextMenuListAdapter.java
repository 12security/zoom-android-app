package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.ReactionEmojiSampleView;
import com.zipow.videobox.view.p014mm.ReactionEmojiSampleView.OnReactionEmojiSampleListener;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.IZMMenuItem;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;

/* renamed from: com.zipow.videobox.view.mm.message.ReactionContextMenuListAdapter */
public class ReactionContextMenuListAdapter<T extends IZMMenuItem> extends BaseRecyclerViewAdapter<T> implements OnReactionEmojiSampleListener {
    private static final int ITEM_VIEW_TYPE_OPTION_MENU = 2;
    private static final int ITEM_VIEW_TYPE_REACTION_SAMPLE = 1;
    /* access modifiers changed from: private */
    public OnReactionConextMenuListListener mListListener;
    private MMMessageItem messageItem;
    private boolean showReplyStyle;

    /* renamed from: com.zipow.videobox.view.mm.message.ReactionContextMenuListAdapter$OnReactionConextMenuListListener */
    public interface OnReactionConextMenuListListener extends OnRecyclerViewListener {
        void onMoreEmojiClick(MMMessageItem mMMessageItem);

        void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj);
    }

    public ReactionContextMenuListAdapter(Context context) {
        super(context);
        this.showReplyStyle = false;
    }

    public ReactionContextMenuListAdapter(Context context, MMMessageItem mMMessageItem) {
        this(context);
        this.messageItem = mMMessageItem;
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == 1) {
            view = new ReactionEmojiSampleView(this.mContext);
            LayoutParams layoutParams = new LayoutParams(-1, -2);
            layoutParams.bottomMargin = UIUtil.dip2px(this.mContext, 8.0f);
            view.setLayoutParams(layoutParams);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(C4409R.layout.zm_context_menu_item, viewGroup, false);
        }
        return new BaseViewHolder(view);
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (baseViewHolder.getItemViewType() == 1) {
            ((ReactionEmojiSampleView) baseViewHolder.itemView).bindData(this.messageItem);
            ((ReactionEmojiSampleView) baseViewHolder.itemView).setOnReactionEmojiSampleListener(this);
            return;
        }
        bind(baseViewHolder, getItem(i));
    }

    public int getItemViewType(int i) {
        return (i != 0 || !hasReactionSample()) ? 2 : 1;
    }

    public int getItemCount() {
        return hasReactionSample() ? this.mData.size() + 1 : this.mData.size();
    }

    public T getItem(int i) {
        if (hasReactionSample()) {
            i--;
        }
        return (IZMMenuItem) super.getItem(i);
    }

    public boolean hasHeader() {
        MMMessageItem mMMessageItem = this.messageItem;
        return mMMessageItem != null && isSupportReaction(mMMessageItem);
    }

    public boolean hasReactionSample() {
        MMMessageItem mMMessageItem = this.messageItem;
        return mMMessageItem != null && isSupportReaction(mMMessageItem) && ZMIMUtils.isReactionEnable() && !ZMIMUtils.isAnnouncement(this.messageItem.sessionId);
    }

    public boolean isSupportReaction(MMMessageItem mMMessageItem) {
        boolean z = false;
        if (mMMessageItem == null || mMMessageItem.messageType == 48 || mMMessageItem.messageType == 50 || mMMessageItem.messageState == 4 || mMMessageItem.messageState == 1 || mMMessageItem.messageState == 6) {
            return false;
        }
        if (!(mMMessageItem.messageType == 22 || mMMessageItem.messageType == 23 || mMMessageItem.messageType == 21 || mMMessageItem.messageType == 43 || mMMessageItem.messageType == 44 || mMMessageItem.messageType == 40 || mMMessageItem.messageType == 41)) {
            z = true;
        }
        return z;
    }

    public void setShowReplyStyle(boolean z) {
        this.showReplyStyle = z;
    }

    public boolean isShowReplyStyle() {
        return this.showReplyStyle;
    }

    private void bind(final BaseViewHolder baseViewHolder, T t) {
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(C4409R.C4411id.menu_text);
        if (textView != null) {
            textView.setText(t.getLabel());
            if (t.getTextColor() != 0) {
                textView.setTextColor(t.getTextColor());
            }
        }
        baseViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ReactionContextMenuListAdapter.this.mListListener != null) {
                    ReactionContextMenuListAdapter.this.mListListener.onItemClick(view, baseViewHolder.getAdapterPosition());
                }
            }
        });
    }

    public void onMoreEmojiClick(MMMessageItem mMMessageItem) {
        OnReactionConextMenuListListener onReactionConextMenuListListener = this.mListListener;
        if (onReactionConextMenuListListener != null) {
            onReactionConextMenuListListener.onMoreEmojiClick(mMMessageItem);
        }
    }

    public void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj) {
        OnReactionConextMenuListListener onReactionConextMenuListListener = this.mListListener;
        if (onReactionConextMenuListListener != null) {
            onReactionConextMenuListListener.onReactionEmojiClick(view, i, charSequence, obj);
        }
    }

    public void setOnReactionConextMenuListListener(OnReactionConextMenuListListener onReactionConextMenuListListener) {
        this.mListListener = onReactionConextMenuListListener;
    }
}
