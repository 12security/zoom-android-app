package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessageAPI;
import com.zipow.videobox.sip.server.IPBXMessageSession;
import com.zipow.videobox.util.TextCommandHelper;
import com.zipow.videobox.util.TextCommandHelper.DraftBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.IPinnedSectionAdapter;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXMessageSessionAdapter extends BaseRecyclerViewAdapter<IPBXMessageSessionItem> implements IPinnedSectionAdapter {

    public static class PBXMessageSessionItemViewHolder extends BaseViewHolder {
        private ImageView ivError;
        /* access modifiers changed from: private */
        public OnRecyclerViewListener listener;
        private TextView tvBrief;
        private TextView tvTime;
        private TextView tvTitle;
        private TextView tvUnreadCount;

        public PBXMessageSessionItemViewHolder(View view, OnRecyclerViewListener onRecyclerViewListener) {
            super(view);
            this.tvTitle = (TextView) view.findViewById(C4558R.C4560id.tv_title);
            this.tvTime = (TextView) view.findViewById(C4558R.C4560id.tv_time);
            this.tvBrief = (TextView) view.findViewById(C4558R.C4560id.tv_brief);
            this.tvUnreadCount = (TextView) view.findViewById(C4558R.C4560id.tv_unread_count);
            this.ivError = (ImageView) view.findViewById(C4558R.C4560id.iv_error);
            this.listener = onRecyclerViewListener;
        }

        public void setData(IPBXMessageSessionItem iPBXMessageSessionItem) {
            String str;
            Context context = this.itemView.getContext();
            if (context != null) {
                this.itemView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (PBXMessageSessionItemViewHolder.this.listener != null) {
                            PBXMessageSessionItemViewHolder.this.listener.onItemClick(PBXMessageSessionItemViewHolder.this.itemView, PBXMessageSessionItemViewHolder.this.getAdapterPosition());
                        }
                    }
                });
                this.itemView.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        if (PBXMessageSessionItemViewHolder.this.listener != null) {
                            return PBXMessageSessionItemViewHolder.this.listener.onItemLongClick(PBXMessageSessionItemViewHolder.this.itemView, PBXMessageSessionItemViewHolder.this.getAdapterPosition());
                        }
                        return false;
                    }
                });
                this.tvTitle.setText(iPBXMessageSessionItem.getDisplayName());
                this.tvTime.setText(getDate(iPBXMessageSessionItem.getUpdatedTime()));
                String str2 = null;
                if (!TextUtils.isEmpty(iPBXMessageSessionItem.getDraftText())) {
                    DraftBean restoreTextCommand = TextCommandHelper.getInstance().restoreTextCommand(true, iPBXMessageSessionItem.getID());
                    if (restoreTextCommand != null) {
                        str2 = restoreTextCommand.getLabel();
                    }
                }
                if (StringUtil.isEmptyOrNull(str2)) {
                    String summary = iPBXMessageSessionItem.getSummary();
                    if (iPBXMessageSessionItem.getDirection() == 2) {
                        this.tvBrief.setText(summary);
                    } else {
                        this.tvBrief.setText(context.getString(C4558R.string.zm_sip_sms_summary_outbound_117773, new Object[]{summary}));
                    }
                } else {
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(C4558R.color.zm_ui_kit_color_red_E02828));
                    String string = context.getString(C4558R.string.zm_msg_draft_71416, new Object[]{""});
                    SpannableString spannableString = new SpannableString(context.getString(C4558R.string.zm_msg_draft_71416, new Object[]{str2}));
                    spannableString.setSpan(foregroundColorSpan, 0, string.length(), 33);
                    this.tvBrief.setText(spannableString);
                }
                if (iPBXMessageSessionItem.getLastMessageSendStatus() == 2 || iPBXMessageSessionItem.getLastMessageSendStatus() == 6) {
                    this.ivError.setVisibility(0);
                    this.tvUnreadCount.setVisibility(8);
                } else {
                    this.ivError.setVisibility(8);
                    int totalUnReadCount = iPBXMessageSessionItem.getTotalUnReadCount();
                    if (totalUnReadCount > 0) {
                        this.tvUnreadCount.setVisibility(0);
                        TextView textView = this.tvUnreadCount;
                        if (totalUnReadCount > 99) {
                            str = "99+";
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append(totalUnReadCount);
                            sb.append("");
                            str = sb.toString();
                        }
                        textView.setText(str);
                        this.tvUnreadCount.setContentDescription(context.getResources().getQuantityString(C4558R.plurals.zm_sip_desc_session_new_message_117773, totalUnReadCount, new Object[]{this.tvUnreadCount.getText().toString()}));
                    } else {
                        this.tvUnreadCount.setVisibility(8);
                    }
                }
            }
        }

        @Nullable
        private String getDate(long j) {
            Context context = this.itemView.getContext();
            if (DateUtils.isToday(j)) {
                return TimeUtil.formatTime(context, j);
            }
            if (TimeUtil.isYesterday(j)) {
                return context.getString(C4558R.string.zm_yesterday_85318);
            }
            return DateUtils.formatDateTime(context, j, 131092);
        }
    }

    public boolean isPinnedSection(int i) {
        return false;
    }

    public void onChanged() {
    }

    public PhonePBXMessageSessionAdapter(Context context) {
        super(context);
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PBXMessageSessionItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_pbx_message_session, viewGroup, false), this.mListener);
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        onBindViewHolder(baseViewHolder, i, null);
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i, @Nullable List<Object> list) {
        if (baseViewHolder instanceof PBXMessageSessionItemViewHolder) {
            ((PBXMessageSessionItemViewHolder) baseViewHolder).setData((IPBXMessageSessionItem) this.mData.get(i));
        }
    }

    public void setData(List<IPBXMessageSessionItem> list) {
        sortData(list);
        super.setData(list);
    }

    private void sortData(List<IPBXMessageSessionItem> list) {
        Collections.sort(list, new Comparator<IPBXMessageSessionItem>() {
            public int compare(IPBXMessageSessionItem iPBXMessageSessionItem, IPBXMessageSessionItem iPBXMessageSessionItem2) {
                if (iPBXMessageSessionItem == iPBXMessageSessionItem2) {
                    return 0;
                }
                if (iPBXMessageSessionItem2.isLocalSession() && !iPBXMessageSessionItem.isLocalSession()) {
                    return 1;
                }
                if (iPBXMessageSessionItem2.isLocalSession() || !iPBXMessageSessionItem.isLocalSession()) {
                    return Long.compare(iPBXMessageSessionItem2.getUpdatedTime(), iPBXMessageSessionItem.getUpdatedTime());
                }
                return -1;
            }
        });
    }

    public void syncSessions(@Nullable List<String> list, @Nullable List<String> list2, @Nullable List<String> list3) {
        if (!CollectionsUtil.isListEmpty(list) || !CollectionsUtil.isListEmpty(list2) || !CollectionsUtil.isListEmpty(list3)) {
            HashMap hashMap = new HashMap();
            for (IPBXMessageSessionItem iPBXMessageSessionItem : this.mData) {
                hashMap.put(iPBXMessageSessionItem.getID(), iPBXMessageSessionItem);
            }
            CmmSIPMessageManager instance = CmmSIPMessageManager.getInstance();
            if (!CollectionsUtil.isListEmpty(list)) {
                for (String str : list) {
                    IPBXMessageSession sessionById = instance.getSessionById(str);
                    if (sessionById != null) {
                        hashMap.put(str, IPBXMessageSessionItem.fromMessageSession(sessionById));
                    }
                }
            }
            if (!CollectionsUtil.isListEmpty(list2)) {
                for (String str2 : list2) {
                    if (hashMap.containsKey(str2)) {
                        IPBXMessageSession sessionById2 = instance.getSessionById(str2);
                        if (sessionById2 != null) {
                            IPBXMessageSessionItem iPBXMessageSessionItem2 = (IPBXMessageSessionItem) hashMap.get(str2);
                            if (iPBXMessageSessionItem2 != null) {
                                iPBXMessageSessionItem2.initialize(sessionById2);
                            }
                        }
                    }
                }
            }
            if (!CollectionsUtil.isListEmpty(list3)) {
                for (String remove : list3) {
                    hashMap.remove(remove);
                }
            }
            setData(new ArrayList(hashMap.values()));
        }
    }

    public void addNewSession(@NonNull String str) {
        IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(str);
        if (sessionById != null) {
            IPBXMessageSessionItem fromMessageSession = IPBXMessageSessionItem.fromMessageSession(sessionById);
            if (!this.mData.contains(fromMessageSession)) {
                this.mData.add(0, fromMessageSession);
                notifyItemInserted(0);
            }
        }
    }

    public void addNewLocalSession(@NonNull String str) {
        IPBXMessageAPI messageAPI = CmmSIPMessageManager.getInstance().getMessageAPI();
        if (messageAPI != null) {
            IPBXMessageSessionItem fromMessageSession = IPBXMessageSessionItem.fromMessageSession(str, messageAPI);
            if (!this.mData.contains(fromMessageSession)) {
                this.mData.add(0, fromMessageSession);
                notifyItemInserted(0);
            }
        }
    }

    public void deleteSession(@NonNull String str) {
        Iterator it = this.mData.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (StringUtil.isSameString(((IPBXMessageSessionItem) it.next()).getID(), str)) {
                it.remove();
                notifyItemRemoved(i);
                return;
            }
            i++;
        }
    }

    public void updateSession(@NonNull String str) {
        IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(str);
        if (sessionById != null) {
            for (int i = 0; i < this.mData.size(); i++) {
                IPBXMessageSessionItem iPBXMessageSessionItem = (IPBXMessageSessionItem) this.mData.get(i);
                if (TextUtils.equals(str, iPBXMessageSessionItem.getID())) {
                    iPBXMessageSessionItem.initialize(sessionById);
                    sortData(this.mData);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void updateLocalSession(@NonNull String str) {
        IPBXMessageAPI messageAPI = CmmSIPMessageManager.getInstance().getMessageAPI();
        if (messageAPI != null) {
            for (int i = 0; i < this.mData.size(); i++) {
                IPBXMessageSessionItem iPBXMessageSessionItem = (IPBXMessageSessionItem) this.mData.get(i);
                if (!CmmSIPMessageManager.getInstance().isLocalSession(str)) {
                    deleteSession(str);
                } else if (TextUtils.equals(str, iPBXMessageSessionItem.getID())) {
                    iPBXMessageSessionItem.initializeLocalSession(str, messageAPI);
                    sortData(this.mData);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void updateContactNames() {
        for (IPBXMessageSessionItem updateDisplayName : this.mData) {
            updateDisplayName.updateDisplayName();
        }
        notifyDataSetChanged();
    }
}
