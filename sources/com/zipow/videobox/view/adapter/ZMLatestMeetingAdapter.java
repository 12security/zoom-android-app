package com.zipow.videobox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.ScheduledMeetingItem;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.videomeetings.C4558R;

public class ZMLatestMeetingAdapter extends BaseAdapter implements OnClickListener {
    public static final long UPCOMING_MEETING_CHECK_INTERVAL = 600000;
    private final Context mContext;
    private final InnerItemOnclickListener mInnerItemOnclickListener;
    private List<ScheduledMeetingItem> mScheduledMeetingItems;

    public interface InnerItemOnclickListener {
        void itemClick(View view);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public ZMLatestMeetingAdapter(Context context, InnerItemOnclickListener innerItemOnclickListener) {
        this.mContext = context;
        this.mInnerItemOnclickListener = innerItemOnclickListener;
    }

    public void refresh(List<ScheduledMeetingItem> list) {
        this.mScheduledMeetingItems = list;
        notifyDataSetChanged();
    }

    public List<ScheduledMeetingItem> getmScheduledMeetingItems() {
        return this.mScheduledMeetingItems;
    }

    public int getCount() {
        List<ScheduledMeetingItem> list = this.mScheduledMeetingItems;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public ScheduledMeetingItem getItem(int i) {
        return (ScheduledMeetingItem) this.mScheduledMeetingItems.get(i);
    }

    @Nullable
    public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
        if (view == null || !"ZMLatestMeetingAdapter".equals(view.getTag())) {
            LayoutInflater from = LayoutInflater.from(this.mContext);
            if (from == null) {
                return null;
            }
            view = from.inflate(C4558R.layout.zm_item_latest_upcoming_meeting_item, viewGroup, false);
            view.setTag("ZMLatestMeetingAdapter");
        }
        ScheduledMeetingItem item = getItem(i);
        if (item == null) {
            return view;
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtTopic);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtTime);
        TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.txtMeetingId);
        Button button = (Button) view.findViewById(C4558R.C4560id.btnStart);
        String formateHourAmPm = TimeUtil.formateHourAmPm(item.getRealStartTime());
        if (!StringUtil.isEmptyOrNull(formateHourAmPm)) {
            textView2.setText(formateHourAmPm.replace(OAuth.SCOPE_DELIMITER, FontStyleHelper.SPLITOR));
        } else {
            textView2.setVisibility(4);
        }
        textView.setText(item.getTopic());
        if (!item.ismIsZoomMeeting()) {
            textView3.setText(C4558R.string.zm_description_not_zoom_meeting_63007);
            button.setVisibility(8);
            return view;
        }
        int i2 = C4558R.string.zm_lbl_meeting_id;
        if (item.getMeetingNo() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mContext.getText(i2));
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(StringUtil.formatConfNumber(item.getMeetingNo()));
            textView3.setText(sb.toString());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.mContext.getText(i2));
            sb2.append(OAuth.SCOPE_DELIMITER);
            sb2.append(item.getPersonalLink());
            textView3.setText(sb2.toString());
        }
        button.setVisibility(0);
        if (ConfLocalHelper.isSameActiveCall(item)) {
            button.setText(C4558R.string.zm_btn_back);
        } else {
            button.setText(item.ismIsCanStartMeetingForMySelf() ? C4558R.string.zm_btn_start : C4558R.string.zm_btn_join);
        }
        button.setTag(item);
        button.setOnClickListener(this);
        return view;
    }

    public void onClick(View view) {
        this.mInnerItemOnclickListener.itemClick(view);
    }
}
