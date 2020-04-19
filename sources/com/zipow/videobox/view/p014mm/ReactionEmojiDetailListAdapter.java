package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMAddrBookItemView;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;

/* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDetailListAdapter */
public class ReactionEmojiDetailListAdapter extends BaseRecyclerViewAdapter<IMAddrBookItem> {
    public ReactionEmojiDetailListAdapter(Context context) {
        super(context);
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        IMAddrBookItemView iMAddrBookItemView = new IMAddrBookItemView(viewGroup.getContext());
        iMAddrBookItemView.setLayoutParams(layoutParams);
        return new BaseViewHolder(iMAddrBookItemView);
    }

    public void onBindViewHolder(@NonNull final BaseViewHolder baseViewHolder, int i) {
        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) getItem(i);
        if (iMAddrBookItem != null) {
            IMAddrBookItemView view = iMAddrBookItem.getView(this.mContext, baseViewHolder.itemView, false, false);
            if (view != null) {
                view.showPresenceView(true);
            }
            baseViewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (ReactionEmojiDetailListAdapter.this.mListener != null) {
                        ReactionEmojiDetailListAdapter.this.mListener.onItemClick(view, baseViewHolder.getAdapterPosition());
                    }
                }
            });
        }
    }

    public void updateBuddy(String str) {
        if (!TextUtils.isEmpty(str)) {
            List data = getData();
            if (!CollectionsUtil.isCollectionEmpty(data)) {
                for (int i = 0; i < data.size(); i++) {
                    if (TextUtils.equals(((IMAddrBookItem) data.get(i)).getJid(), str)) {
                        IMAddrBookItem buddyByJid = ZMBuddySyncInstance.getInsatance().getBuddyByJid(str, true);
                        if (buddyByJid != null) {
                            data.set(i, buddyByJid);
                            notifyItemChanged(i);
                            return;
                        }
                    }
                }
            }
        }
    }
}
