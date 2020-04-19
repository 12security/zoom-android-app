package p021us.zoom.androidlib.widget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.List;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.adapter.ZmSingleChoiceAdapter */
public class ZmSingleChoiceAdapter extends Adapter<SimpleChoiceViewHolder> {
    private int mSelectedPos = -1;
    /* access modifiers changed from: private */
    public List<ZmSingleChoiceItem> mZmSingleChoiceItemList;

    /* renamed from: us.zoom.androidlib.widget.adapter.ZmSingleChoiceAdapter$SimpleChoiceViewHolder */
    public class SimpleChoiceViewHolder extends ViewHolder {
        final View divider;
        final ImageView imgSelected;
        final TextView txtTitle;

        public SimpleChoiceViewHolder(View view) {
            super(view);
            this.txtTitle = (TextView) view.findViewById(C4409R.C4411id.txtTitle);
            this.imgSelected = (ImageView) view.findViewById(C4409R.C4411id.imgSelected);
            this.divider = view.findViewById(C4409R.C4411id.divider);
        }

        public void bind(int i) {
            ZmSingleChoiceItem zmSingleChoiceItem = (ZmSingleChoiceItem) ZmSingleChoiceAdapter.this.mZmSingleChoiceItemList.get(i);
            this.txtTitle.setText(zmSingleChoiceItem.getTitle());
            this.imgSelected.setImageResource(zmSingleChoiceItem.getImgIconRes());
            this.imgSelected.setContentDescription(zmSingleChoiceItem.getIconContentDescription());
            int i2 = 0;
            this.imgSelected.setVisibility(zmSingleChoiceItem.isSelected() ? 0 : 4);
            View view = this.divider;
            if (i == ZmSingleChoiceAdapter.this.getItemCount() - 1) {
                i2 = 4;
            }
            view.setVisibility(i2);
        }
    }

    public void setmZmSingleChoiceItemList(List<ZmSingleChoiceItem> list) {
        this.mZmSingleChoiceItemList = list;
        notifyDataSetChanged();
    }

    public int getmSelectedPos() {
        return this.mSelectedPos;
    }

    public void setmSelectedPos(int i) {
        int i2 = this.mSelectedPos;
        if (i2 >= 0) {
            List<ZmSingleChoiceItem> list = this.mZmSingleChoiceItemList;
            if (list != null && i2 < list.size()) {
                ((ZmSingleChoiceItem) this.mZmSingleChoiceItemList.get(this.mSelectedPos)).setSelected(false);
            }
        }
        this.mSelectedPos = i;
        int i3 = this.mSelectedPos;
        if (i3 >= 0) {
            List<ZmSingleChoiceItem> list2 = this.mZmSingleChoiceItemList;
            if (list2 != null && i3 < list2.size()) {
                ((ZmSingleChoiceItem) this.mZmSingleChoiceItemList.get(this.mSelectedPos)).setSelected(true);
            }
        }
        notifyDataSetChanged();
    }

    @Nullable
    public ZmSingleChoiceItem getItem(int i) {
        List<ZmSingleChoiceItem> list = this.mZmSingleChoiceItemList;
        if (list == null || i >= list.size()) {
            return null;
        }
        return (ZmSingleChoiceItem) this.mZmSingleChoiceItemList.get(i);
    }

    @NonNull
    public SimpleChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SimpleChoiceViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4409R.layout.zm_item_single_choice, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull SimpleChoiceViewHolder simpleChoiceViewHolder, int i) {
        simpleChoiceViewHolder.bind(i);
    }

    public int getItemCount() {
        List<ZmSingleChoiceItem> list = this.mZmSingleChoiceItemList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
