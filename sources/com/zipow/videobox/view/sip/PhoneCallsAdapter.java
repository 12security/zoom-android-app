package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.CallHistory;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhoneCallsAdapter extends BaseAdapter implements OnClickListener {
    private Context mContext;
    @Nullable
    private LayoutInflater mInflater;
    private boolean mIsDeleteMode;
    @NonNull
    private List<CallHistory> mItems = new ArrayList();
    private PhoneCallsListview mListView;

    public long getItemId(int i) {
        return 0;
    }

    public PhoneCallsAdapter(Context context, PhoneCallsListview phoneCallsListview) {
        this.mContext = context;
        this.mListView = phoneCallsListview;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public void setDeleteMode(boolean z) {
        this.mIsDeleteMode = z;
    }

    public void updateData(@Nullable List<CallHistory> list) {
        this.mItems.clear();
        if (list != null) {
            this.mItems.addAll(list);
        }
    }

    public void updateZoomBuddyInfo(@Nullable List<String> list) {
        for (CallHistory callHistory : this.mItems) {
            String buddyJid = callHistory.getBuddyJid();
            if (!StringUtil.isEmptyOrNull(buddyJid) && list != null && list.contains(buddyJid)) {
                callHistory.updateZOOMDisplayName();
            }
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mItems.size();
    }

    @Nullable
    public CallHistory getItem(int i) {
        if (this.mItems.size() <= i) {
            return null;
        }
        return (CallHistory) this.mItems.get(i);
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        return createRecentCallItemView(i, view, viewGroup);
    }

    @NonNull
    private View createRecentCallItemView(int i, @Nullable View view, ViewGroup viewGroup) {
        if (view == null || !"recentCall".equals(view.getTag())) {
            view = this.mInflater.inflate(C4558R.layout.zm_call_contact_item, viewGroup, false);
            view.setTag("recentCall");
        }
        CallHistory item = getItem(i);
        if (item == null) {
            return view;
        }
        ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgOutCall);
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtBuddyName);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtCallNo);
        TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.txtCallTime);
        ImageView imageView2 = (ImageView) view.findViewById(C4558R.C4560id.imgDeleteCall);
        imageView2.setVisibility(this.mIsDeleteMode ? 0 : 8);
        if (item.getState() == 1 || item.getState() == 4) {
            textView.setTextColor(this.mContext.getResources().getColor(C4558R.color.zm_call_history_name_miss));
        } else {
            textView.setTextColor(this.mContext.getResources().getColor(C4558R.color.zm_call_history_name));
        }
        if (item.getDirection() == 2) {
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(4);
        }
        textView.setText(item.getZOOMDisplayName());
        if (item.getType() == 2) {
            textView2.setText(C4558R.string.zm_hint_call_zoom_audio_14480);
        } else if (item.getType() == 1) {
            textView2.setText(C4558R.string.zm_hint_call_zoom_video_14480);
        } else {
            textView2.setText(item.getNumber());
        }
        textView3.setText(formatTime(this.mContext, item.getTime()));
        imageView2.setOnClickListener(this);
        imageView2.setTag(item.getId());
        return view;
    }

    public static String formatTime(@NonNull Context context, long j) {
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = currentTimeMillis - 86400000;
        if (TimeUtil.isSameDate(j, currentTimeMillis)) {
            return TimeUtil.formatTime(context, j);
        }
        if (TimeUtil.isSameDate(j, j2)) {
            return context.getString(C4558R.string.zm_lbl_yesterday);
        }
        return TimeUtil.formatDateTime(context, j);
    }

    public boolean removeCall(String str) {
        for (int i = 0; i < this.mItems.size(); i++) {
            if (StringUtil.isSameString(str, ((CallHistory) this.mItems.get(i)).getId())) {
                this.mItems.remove(i);
                return true;
            }
        }
        return false;
    }

    public void onClick(@Nullable View view) {
        if (view != null && view.getId() == C4558R.C4560id.imgDeleteCall) {
            String str = (String) view.getTag();
            if (!StringUtil.isEmptyOrNull(str)) {
                PhoneCallsListview phoneCallsListview = this.mListView;
                if (phoneCallsListview != null) {
                    phoneCallsListview.deleteHistoryCall(str);
                }
            }
        }
    }
}
