package p021us.zoom.androidlib.material;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.widget.IZMMenuItem;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;

/* renamed from: us.zoom.androidlib.material.ZMContextMenuListAdapter */
public class ZMContextMenuListAdapter<T extends IZMMenuItem> extends BaseRecyclerViewAdapter<T> {
    public int getItemViewType(int i) {
        return 2;
    }

    public ZMContextMenuListAdapter(Context context) {
        super(context);
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BaseViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4409R.layout.zm_context_menu_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (baseViewHolder.getItemViewType() != 1) {
            bind(baseViewHolder, getItem(i));
        }
    }

    public int getItemCount() {
        return this.mData.size();
    }

    public T getItem(int i) {
        return (IZMMenuItem) super.getItem(i);
    }

    private void bind(final BaseViewHolder baseViewHolder, T t) {
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(C4409R.C4411id.menu_text);
        if (textView != null) {
            textView.setText(t.getLabel());
        }
        baseViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ZMContextMenuListAdapter.this.mListener != null) {
                    ZMContextMenuListAdapter.this.mListener.onItemClick(view, baseViewHolder.getAdapterPosition());
                }
            }
        });
    }
}
