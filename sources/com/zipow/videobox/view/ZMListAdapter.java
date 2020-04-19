package com.zipow.videobox.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.IZMListItemView.IActionClickListener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.IZMListItem;
import p021us.zoom.videomeetings.C4558R;

public class ZMListAdapter<ListItem extends IZMListItem> extends BaseAdapter implements ListAdapter {
    private IActionClickListener mActionClickListener;
    private Context mContext;
    private LayoutInflater mInflater;
    @NonNull
    private List<ListItem> mList;
    private boolean mShowSelect;

    private class ViewHolder {
        ImageView ivSelect;
        TextView txtLabel;
        TextView txtSubLabel;

        private ViewHolder() {
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public ZMListAdapter(Context context) {
        this(context, null);
    }

    public ZMListAdapter(Context context, IActionClickListener iActionClickListener) {
        this.mList = new ArrayList();
        this.mShowSelect = true;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mActionClickListener = iActionClickListener;
    }

    public void setList(@Nullable List<ListItem> list) {
        if (list != null) {
            this.mList.clear();
            this.mList.addAll(list);
        }
    }

    @NonNull
    public List<ListItem> getList() {
        return this.mList;
    }

    public void addItem(ListItem listitem) {
        this.mList.add(listitem);
    }

    public void setShowSelect(boolean z) {
        this.mShowSelect = z;
    }

    public int getCount() {
        return this.mList.size();
    }

    @Nullable
    public ListItem getItem(int i) {
        if (i >= getCount()) {
            return null;
        }
        return (IZMListItem) this.mList.get(i);
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        IZMListItem iZMListItem = (IZMListItem) this.mList.get(i);
        if (iZMListItem instanceof IZMListItemView) {
            return ((IZMListItemView) iZMListItem).getView(this.mContext, i, view, viewGroup, this.mActionClickListener);
        }
        int i2 = 0;
        if (view == null) {
            view = this.mInflater.inflate(C4558R.layout.zm_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txtLabel = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            viewHolder.txtSubLabel = (TextView) view.findViewById(C4558R.C4560id.txtSubLabel);
            viewHolder.ivSelect = (ImageView) view.findViewById(C4558R.C4560id.ivSelect);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtLabel.setText(iZMListItem.getLabel());
        if (iZMListItem.getSubLabel() != null) {
            viewHolder.txtSubLabel.setVisibility(0);
            String subLabel = iZMListItem.getSubLabel();
            viewHolder.txtSubLabel.setText(subLabel);
            if (!TextUtils.isEmpty(subLabel)) {
                viewHolder.txtSubLabel.setContentDescription(StringUtil.digitJoin(subLabel.split(""), PreferencesConstants.COOKIE_DELIMITER));
            }
        } else {
            viewHolder.txtSubLabel.setVisibility(4);
        }
        boolean z = iZMListItem.isSelected() && this.mShowSelect;
        viewHolder.ivSelect.setVisibility(z ? 0 : 8);
        viewHolder.txtLabel.setSelected(z);
        viewHolder.txtSubLabel.setSelected(z);
        ImageView imageView = viewHolder.ivSelect;
        if (!z) {
            i2 = 8;
        }
        imageView.setVisibility(i2);
        return view;
    }
}
