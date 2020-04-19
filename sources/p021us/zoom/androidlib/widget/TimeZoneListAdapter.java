package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.HashMapComparator;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeZoneUtil;

/* renamed from: us.zoom.androidlib.widget.TimeZoneListAdapter */
public class TimeZoneListAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private Context mContext;
    private List<HashMap<String, Object>> mZones;

    public long getItemId(int i) {
        return (long) i;
    }

    public TimeZoneListAdapter(Context context, boolean z) {
        this.mContext = context;
        this.mZones = TimeZoneUtil.getZones(context);
        Collections.sort(this.mZones, new HashMapComparator(z ? "name" : "offset"));
    }

    public int getCount() {
        return this.mZones.size();
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i > getCount()) {
            return null;
        }
        return ((HashMap) this.mZones.get(i)).get("id");
    }

    private HashMap<String, Object> getItemMap(int i) {
        if (i < 0 || i > getCount()) {
            return null;
        }
        return (HashMap) this.mZones.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null || !"TimeZoneListAdapter".equals(view.getTag())) {
            view = View.inflate(this.mContext, C4409R.layout.zm_time_zone_list_item, null);
            view.setTag("TimeZoneListAdapter");
        }
        TextView textView = (TextView) view.findViewById(C4409R.C4411id.txtCity);
        TextView textView2 = (TextView) view.findViewById(C4409R.C4411id.txtGMT);
        HashMap itemMap = getItemMap(i);
        if (itemMap != null) {
            String str = (String) itemMap.get("name");
            String str2 = (String) itemMap.get(TimeZoneUtil.KEY_GMT);
            if (view.isInEditMode()) {
                textView.setText("ShangHai");
                textView2.setText("GMT+08:00");
            } else {
                textView.setText(str);
                textView2.setText(str2);
            }
        }
        return view;
    }

    public String getItemDisplayName(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        return TimeZoneUtil.getFullName(str);
    }
}
