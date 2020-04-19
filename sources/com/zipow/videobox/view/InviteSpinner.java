package com.zipow.videobox.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.videomeetings.C4558R;

public class InviteSpinner extends Spinner {
    private OnItemSelectedListener mOnItemSelectedlistener;

    public static class CalendarAppItem {
        public static final int TYPE_CALENDAR = 0;
        public static final int TYPE_COPY_TO_CLIPBOARD = 3;
        public static final int TYPE_EMAIL = 1;
        public static final int TYPE_SMS = 2;
        public ResolveInfo app;
        public int type = 0;

        public CalendarAppItem(ResolveInfo resolveInfo, int i) {
            this.app = resolveInfo;
            this.type = i;
        }
    }

    public static class CalendarAppsAdapter extends BaseAdapter {
        private ZMActivity mActivity;
        @NonNull
        private List<CalendarAppItem> mList = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public CalendarAppsAdapter(ZMActivity zMActivity) {
            this.mActivity = zMActivity;
        }

        public void reloadAll() {
            this.mList.clear();
            for (ResolveInfo calendarAppItem : AndroidAppUtil.queryEmailActivities(this.mActivity)) {
                this.mList.add(new CalendarAppItem(calendarAppItem, 1));
            }
            for (ResolveInfo calendarAppItem2 : AndroidAppUtil.querySMSActivities(this.mActivity)) {
                this.mList.add(new CalendarAppItem(calendarAppItem2, 2));
            }
            this.mList.add(new CalendarAppItem(null, 3));
        }

        public int getCount() {
            return this.mList.size();
        }

        public Object getItem(int i) {
            return this.mList.get(i);
        }

        public void addAll(@NonNull CalendarAppItem... calendarAppItemArr) {
            for (CalendarAppItem addItem : calendarAppItemArr) {
                addItem(addItem);
            }
        }

        public void addItem(CalendarAppItem calendarAppItem) {
            this.mList.add(calendarAppItem);
        }

        public void clear() {
            this.mList.clear();
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !"selected".equals(view.getTag())) {
                view = View.inflate(this.mActivity, C4558R.layout.zm_spinner_selected_item, null);
                view.setTag("selected");
            }
            bindView(i, view);
            return view;
        }

        @Nullable
        public View getDropDownView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !"dropdown".equals(view.getTag())) {
                view = View.inflate(this.mActivity, C4558R.layout.zm_menu_item, null);
                view.setTag("dropdown");
            }
            bindView(i, view);
            return view;
        }

        private void bindView(int i, View view) {
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgIcon);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            CalendarAppItem calendarAppItem = (CalendarAppItem) getItem(i);
            if (calendarAppItem.app instanceof ResolveInfo) {
                ResolveInfo resolveInfo = calendarAppItem.app;
                textView.setText(AndroidAppUtil.getApplicationLabel((Context) this.mActivity, resolveInfo));
                imageView.setImageDrawable(AndroidAppUtil.getApplicationIcon((Context) this.mActivity, resolveInfo));
                imageView.setVisibility(0);
            } else if (calendarAppItem.type == 3) {
                textView.setText(this.mActivity.getString(C4558R.string.zm_lbl_copy_to_clipboard));
                imageView.setImageResource(C4558R.C4559drawable.zm_copy);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public InviteSpinner(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public InviteSpinner(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public InviteSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @SuppressLint({"NewApi"})
    public InviteSpinner(Context context, int i) {
        super(context, i);
    }

    public InviteSpinner(Context context) {
        super(context);
    }

    public void setSelection(int i) {
        super.setSelection(i);
        OnItemSelectedListener onItemSelectedListener = this.mOnItemSelectedlistener;
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(this, null, i, 0);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        super.setOnItemSelectedListener(onItemSelectedListener);
        this.mOnItemSelectedlistener = onItemSelectedListener;
    }
}
