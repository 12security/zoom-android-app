package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.SingleItemAdapter */
/* compiled from: ZMAlertDialog */
class SingleItemAdapter extends BaseAdapter {
    private Context context;
    CharSequence[] items;
    private int selectedIndex = -1;

    public long getItemId(int i) {
        return (long) i;
    }

    public SingleItemAdapter(CharSequence[] charSequenceArr, Context context2) {
        this.items = charSequenceArr;
        this.context = context2;
    }

    public int getCount() {
        return this.items.length;
    }

    public Object getItem(int i) {
        return this.items[i];
    }

    public void setIndex(int i) {
        this.selectedIndex = i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(this.context, C4409R.layout.zm_singlechoiceitem, null);
        }
        ((TextView) view.findViewById(C4409R.C4411id.name)).setText(this.items[i]);
        CheckBox checkBox = (CheckBox) view.findViewById(C4409R.C4411id.checkbutton);
        if (this.selectedIndex == i) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        return view;
    }
}
