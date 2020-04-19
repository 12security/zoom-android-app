package com.zipow.videobox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.RoomDevice;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class RoomDeviceAutoCompleteTextView extends AutoCompleteTextView {
    public static final String ALL_DEVICES_MODE = "all_devices_mode";
    private static final int DATA_SOURCE_TYPE_IN_MEETING = 1;
    private static final int DATA_SOURCE_TYPE_OUT_MEETING = 0;
    private AutoCompleteAdapter mAdapter;
    private ArrayList<RoomDevice> mAllDevices = new ArrayList<>();
    private int mDataSourceType = -1;
    /* access modifiers changed from: private */
    public ArrayList<RoomDevice> mRecentDevices = new ArrayList<>();

    private class AutoCompleteAdapter extends BaseAdapter implements Filterable {
        private Context mContext;
        private AutoCompleteFilter mFilter;
        /* access modifiers changed from: private */
        public final Object mLock = new Object();
        /* access modifiers changed from: private */
        public ArrayList<RoomDevice> mOriginalValues;
        /* access modifiers changed from: private */
        public List<RoomDevice> mValues;

        private class AutoCompleteFilter extends Filter {
            private AutoCompleteFilter() {
            }

            /* access modifiers changed from: protected */
            @NonNull
            public FilterResults performFiltering(@Nullable CharSequence charSequence) {
                ArrayList arrayList;
                ArrayList arrayList2;
                ArrayList arrayList3;
                FilterResults filterResults = new FilterResults();
                if (AutoCompleteAdapter.this.mOriginalValues == null) {
                    synchronized (AutoCompleteAdapter.this.mLock) {
                        AutoCompleteAdapter.this.mOriginalValues = new ArrayList(AutoCompleteAdapter.this.mValues);
                    }
                }
                if (charSequence == null || charSequence.length() == 0) {
                    synchronized (AutoCompleteAdapter.this.mLock) {
                        arrayList = new ArrayList();
                        if (RoomDeviceAutoCompleteTextView.this.mRecentDevices.size() > 0) {
                            arrayList.add(new RoomDevice(RoomDeviceAutoCompleteTextView.this.getContext().getResources().getString(C4558R.string.zm_lbl_recent_calls_103311)));
                            arrayList.addAll(RoomDeviceAutoCompleteTextView.this.mRecentDevices);
                        }
                        AutoCompleteAdapter.this.setData(arrayList);
                    }
                    filterResults.values = arrayList;
                    filterResults.count = arrayList.size();
                } else if (RoomDeviceAutoCompleteTextView.ALL_DEVICES_MODE.equalsIgnoreCase(charSequence.toString())) {
                    synchronized (AutoCompleteAdapter.this.mLock) {
                        arrayList3 = new ArrayList();
                        if (AutoCompleteAdapter.this.mOriginalValues.size() > 0) {
                            arrayList3.add(new RoomDevice(RoomDeviceAutoCompleteTextView.this.getContext().getResources().getString(C4558R.string.zm_lbl_all_calls_103311)));
                            arrayList3.addAll(AutoCompleteAdapter.this.mOriginalValues);
                        }
                        AutoCompleteAdapter.this.setData(arrayList3);
                    }
                    filterResults.values = arrayList3;
                    filterResults.count = arrayList3.size();
                } else {
                    String lowerCase = charSequence.toString().toLowerCase(CompatUtils.getLocalDefault());
                    synchronized (AutoCompleteAdapter.this.mLock) {
                        arrayList2 = new ArrayList(AutoCompleteAdapter.this.mOriginalValues);
                        AutoCompleteAdapter.this.setData(arrayList2);
                    }
                    int size = arrayList2.size();
                    ArrayList arrayList4 = new ArrayList();
                    for (int i = 0; i < size; i++) {
                        RoomDevice roomDevice = (RoomDevice) arrayList2.get(i);
                        String displayName = roomDevice.getDisplayName();
                        if ((!StringUtil.isEmptyOrNull(displayName) && displayName.toLowerCase(CompatUtils.getLocalDefault()).contains(lowerCase)) || !StringUtil.isEmptyOrNull(roomDevice.getTitle())) {
                            arrayList4.add(roomDevice);
                        }
                    }
                    filterResults.values = arrayList4;
                    filterResults.count = arrayList4.size();
                }
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, @NonNull FilterResults filterResults) {
                AutoCompleteAdapter.this.mValues = (List) filterResults.values;
                if (filterResults.count > 0) {
                    AutoCompleteAdapter.this.notifyDataSetChanged();
                    if (TextUtils.isEmpty(charSequence)) {
                        RoomDeviceAutoCompleteTextView.this.post(new Runnable() {
                            public void run() {
                                RoomDeviceAutoCompleteTextView.this.showDropDown();
                            }
                        });
                        return;
                    }
                    return;
                }
                AutoCompleteAdapter.this.notifyDataSetInvalidated();
            }
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public AutoCompleteAdapter(Context context, List<RoomDevice> list) {
            init(context, list);
        }

        private void init(Context context, List<RoomDevice> list) {
            this.mContext = context;
            this.mValues = list;
        }

        public void setData(List<RoomDevice> list) {
            List<RoomDevice> list2 = this.mValues;
            if (list2 != null) {
                list2.clear();
                this.mValues.addAll(list);
            }
        }

        public int getCount() {
            return this.mValues.size();
        }

        @Nullable
        public String getItem(int i) {
            RoomDevice objectItem = getObjectItem(i);
            if (objectItem == null) {
                return "";
            }
            return objectItem.getDisplayName();
        }

        private RoomDevice getObjectItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            return (RoomDevice) this.mValues.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            TextView textView;
            Editable editable;
            RoomDevice objectItem = getObjectItem(i);
            if (objectItem != null) {
                if (!StringUtil.isEmptyOrNull(objectItem.getTitle())) {
                    if (view == null || !"title".equals(view.getTag())) {
                        view = LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_select_room_item_title, viewGroup, false);
                        view.setTag("title");
                    }
                } else if (view == null || !Param.CONTENT.equals(view.getTag())) {
                    view = LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_select_room_item, viewGroup, false);
                    view.setTag(Param.CONTENT);
                }
                if (!StringUtil.isEmptyOrNull(objectItem.getTitle())) {
                    editable = Factory.getInstance().newEditable(objectItem.getTitle());
                    textView = (TextView) view.findViewById(C4558R.C4560id.txtTitle);
                } else {
                    editable = Factory.getInstance().newEditable(objectItem.getDisplayName());
                    textView = (TextView) view.findViewById(C4558R.C4560id.txtTopic);
                }
                if (textView != null) {
                    textView.setText(editable.toString());
                }
            }
            return view;
        }

        public Filter getFilter() {
            if (this.mFilter == null) {
                this.mFilter = new AutoCompleteFilter();
            }
            return this.mFilter;
        }
    }

    public RoomDeviceAutoCompleteTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.RoomDeviceAutoCompleteTextView, i, 0);
        this.mDataSourceType = obtainStyledAttributes.getInt(C4558R.styleable.RoomDeviceAutoCompleteTextView_data_source_type, -1);
        obtainStyledAttributes.recycle();
        init();
    }

    public RoomDeviceAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.RoomDeviceAutoCompleteTextView);
        this.mDataSourceType = obtainStyledAttributes.getInt(C4558R.styleable.RoomDeviceAutoCompleteTextView_data_source_type, -1);
        obtainStyledAttributes.recycle();
        init();
    }

    public RoomDeviceAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        PTApp.getInstance().getAllRoomSystemList(2, this.mRecentDevices);
        if (isInEditMode() || hasCallHistory(this.mAllDevices)) {
            this.mAdapter = new AutoCompleteAdapter(getContext(), this.mAllDevices);
            setAdapter(this.mAdapter);
        }
    }

    public void performFilter(String str) {
        AutoCompleteAdapter autoCompleteAdapter = this.mAdapter;
        if (autoCompleteAdapter != null) {
            Filter filter = autoCompleteAdapter.getFilter();
            if (filter instanceof AutoCompleteFilter) {
                ((AutoCompleteFilter) filter).performFiltering(str);
            }
            showDropDown();
        }
    }

    private boolean hasCallHistory(@NonNull ArrayList<RoomDevice> arrayList) {
        int i = this.mDataSourceType;
        if (i == 0) {
            PTApp.getInstance().getAllRoomSystemList(3, arrayList);
        } else if (i == 1) {
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper == null || !meetingHelper.getRoomDevices(arrayList)) {
                return false;
            }
        }
        return arrayList.size() != 0;
    }
}
