package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.ZMListAdapter;
import p021us.zoom.androidlib.widget.IZMListItem;
import p021us.zoom.videomeetings.C4558R;

public class PBXFilterAdapter<T extends IZMListItem> extends ZMListAdapter {

    private class ViewHolder {
        ImageView ivSelect;
        TextView txtLabel;

        private ViewHolder() {
        }
    }

    public PBXFilterAdapter(Context context) {
        super(context);
    }

    @Nullable
    public View getView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
        ViewHolder viewHolder;
        IZMListItem item = getItem(i);
        int i2 = 0;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_pbx_voicemail_filter_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txtLabel = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            viewHolder.ivSelect = (ImageView) view.findViewById(C4558R.C4560id.ivSelect);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtLabel.setText(item.getLabel());
        boolean isSelected = item.isSelected();
        viewHolder.txtLabel.setSelected(isSelected);
        ImageView imageView = viewHolder.ivSelect;
        if (!isSelected) {
            i2 = 4;
        }
        imageView.setVisibility(i2);
        return view;
    }
}
