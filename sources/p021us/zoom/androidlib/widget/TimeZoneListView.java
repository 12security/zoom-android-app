package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.androidlib.widget.TimeZoneListView */
public class TimeZoneListView extends ListView implements OnItemClickListener {
    private TimeZoneListAdapter mAdapter;
    private Listener mListener;
    private String mSelectedId;

    /* renamed from: us.zoom.androidlib.widget.TimeZoneListView$Listener */
    public interface Listener {
        void onSelected(String str);
    }

    public TimeZoneListView(Context context) {
        super(context);
        initView(context);
    }

    public TimeZoneListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public TimeZoneListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        this.mAdapter = new TimeZoneListAdapter(context, false);
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        setHeaderDividersEnabled(true);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void refresh() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        String str = (String) getItemAtPosition(i);
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mSelectedId = str;
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onSelected(this.mSelectedId);
            }
        }
    }

    public String getSelectedName() {
        return this.mSelectedId;
    }
}
