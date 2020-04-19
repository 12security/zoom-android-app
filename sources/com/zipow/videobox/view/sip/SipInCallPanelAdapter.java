package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.zipow.videobox.view.sip.SipInCallPanelView.PanelButtonItem;
import java.util.Iterator;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;

public class SipInCallPanelAdapter extends BaseRecyclerViewAdapter<PanelButtonItem> {
    public static final int PANEL_VIEW_TYPE_MUTE = 2;
    public static final int PANEL_VIEW_TYPE_NORMAL = 0;
    public static final int PANEL_VIEW_TYPE_RECORD = 1;

    public SipInCallPanelAdapter(Context context) {
        super(context);
    }

    public PanelButtonItem getItemByAction(int i) {
        PanelButtonItem panelButtonItem = null;
        if (this.mData == null || this.mData.size() == 0) {
            return null;
        }
        Iterator it = this.mData.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            PanelButtonItem panelButtonItem2 = (PanelButtonItem) it.next();
            if (panelButtonItem2.getAction() == i) {
                panelButtonItem = panelButtonItem2;
                break;
            }
        }
        return panelButtonItem;
    }

    public int getItemViewType(int i) {
        PanelButtonItem panelButtonItem = (PanelButtonItem) this.mData.get(i);
        int i2 = 0;
        if (panelButtonItem == null) {
            return 0;
        }
        if (panelButtonItem.getAction() == 6) {
            i2 = 1;
        } else if (panelButtonItem.getAction() == 0) {
            i2 = 2;
        }
        return i2;
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == 1) {
            view = new SipInCallPanelRecordView(this.mContext);
        } else if (i == 2) {
            view = new SipInCallPanelMuteView(this.mContext);
        } else {
            view = new SipInCallPanelItemView(this.mContext);
        }
        return new BaseViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final BaseViewHolder baseViewHolder, int i) {
        PanelButtonItem panelButtonItem = (PanelButtonItem) this.mData.get(i);
        int itemViewType = baseViewHolder.getItemViewType();
        boolean z = true;
        if (itemViewType == 1) {
            ((SipInCallPanelRecordView) baseViewHolder.itemView).bindView(panelButtonItem);
        } else if (itemViewType == 2) {
            ((SipInCallPanelMuteView) baseViewHolder.itemView).bindView(panelButtonItem);
        } else {
            ((SipInCallPanelItemView) baseViewHolder.itemView).bindView(panelButtonItem);
        }
        View view = baseViewHolder.itemView;
        if (!panelButtonItem.isClickableInDisabled() && panelButtonItem.isDisable()) {
            z = false;
        }
        view.setEnabled(z);
        baseViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SipInCallPanelAdapter.this.mListener != null) {
                    SipInCallPanelAdapter.this.mListener.onItemClick(view, baseViewHolder.getAdapterPosition());
                }
            }
        });
    }
}
