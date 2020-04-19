package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.IZMListItemView.IActionClickListener;
import com.zipow.videobox.view.ZMListAdapter;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.IZMListItem;
import p021us.zoom.videomeetings.C4558R;

public class PBXCallerIDListAdapter<T extends IZMListItem> extends ZMListAdapter {

    private class ViewHolder {
        ImageView ivSelect;
        TextView txtLabel;
        TextView txtSubLabel;

        private ViewHolder() {
        }
    }

    public PBXCallerIDListAdapter(Context context) {
        super(context);
    }

    public PBXCallerIDListAdapter(Context context, IActionClickListener iActionClickListener) {
        super(context, iActionClickListener);
    }

    @Nullable
    public View getView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
        ViewHolder viewHolder;
        IZMListItem item = getItem(i);
        int i2 = 0;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_pbx_callerid_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txtLabel = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            viewHolder.txtSubLabel = (TextView) view.findViewById(C4558R.C4560id.txtSubLabel);
            viewHolder.ivSelect = (ImageView) view.findViewById(C4558R.C4560id.ivSelect);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtLabel.setText(item.getLabel());
        if (StringUtil.isEmptyOrNull(item.getSubLabel())) {
            viewHolder.txtSubLabel.setVisibility(8);
            viewHolder.txtSubLabel.setText("");
            viewHolder.txtSubLabel.setContentDescription("");
        } else {
            viewHolder.txtSubLabel.setVisibility(0);
            viewHolder.txtSubLabel.setText(item.getSubLabel());
            viewHolder.txtSubLabel.setContentDescription(StringUtil.digitJoin(item.getSubLabel().split(""), PreferencesConstants.COOKIE_DELIMITER));
        }
        boolean isSelected = item.isSelected();
        viewHolder.ivSelect.setVisibility(isSelected ? 0 : 8);
        viewHolder.txtLabel.setSelected(isSelected);
        viewHolder.txtSubLabel.setSelected(isSelected);
        ImageView imageView = viewHolder.ivSelect;
        if (!isSelected) {
            i2 = 8;
        }
        imageView.setVisibility(i2);
        return view;
    }
}
