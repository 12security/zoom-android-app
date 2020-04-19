package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import java.util.List;
import p021us.zoom.videomeetings.C4558R;

public class ContactPBXNumberAdapter extends BaseAdapter {
    @Nullable
    private List<String> mDataList = null;
    private LayoutInflater mLayoutInflater;

    private class ViewHolder {
        TextView txtNumber;

        private ViewHolder() {
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public ContactPBXNumberAdapter(Context context, @Nullable List<String> list) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDataList = list;
    }

    public int getCount() {
        List<String> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public String getItem(int i) {
        return (String) this.mDataList.get(i);
    }

    @Nullable
    public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mLayoutInflater.inflate(C4558R.layout.zm_sip_contact_pbx_number_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtNumber = (TextView) view.findViewById(C4558R.C4560id.txtNumber);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtNumber.setText((CharSequence) this.mDataList.get(i));
        return view;
    }
}
