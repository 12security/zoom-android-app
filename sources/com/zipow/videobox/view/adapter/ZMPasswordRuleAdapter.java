package com.zipow.videobox.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.confapp.meeting.PasswordItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.videomeetings.C4558R;

public class ZMPasswordRuleAdapter extends Adapter<viewHolder> {
    @NonNull
    private Context mContext;
    @NonNull
    private List<PasswordItem> mItemList = new ArrayList();

    public class viewHolder extends ViewHolder {
        ImageView mIvPassword;
        TextView mTvPassword;

        public viewHolder(@NonNull View view) {
            super(view);
            this.mIvPassword = (ImageView) view.findViewById(C4558R.C4560id.iv_password);
            this.mTvPassword = (TextView) view.findViewById(C4558R.C4560id.tv_password);
        }
    }

    public ZMPasswordRuleAdapter(@NonNull Context context, @NonNull List<PasswordItem> list) {
        this.mItemList = list;
        this.mContext = context;
    }

    @NonNull
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new viewHolder(LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_schedule_password_item, null));
    }

    public void onBindViewHolder(@NonNull viewHolder viewholder, int i) {
        Resources resources;
        int i2;
        if (this.mItemList.size() != 0) {
            PasswordItem passwordItem = (PasswordItem) this.mItemList.get(i);
            viewholder.mIvPassword.setImageResource(passwordItem.isCorrect() ? C4558R.C4559drawable.ic_password_correct : C4558R.C4559drawable.ic_password_uncorrect);
            viewholder.mTvPassword.setText(passwordItem.getRuleTxt());
            TextView textView = viewholder.mTvPassword;
            if (passwordItem.isCorrect()) {
                resources = this.mContext.getResources();
                i2 = C4558R.color.zm_v2_txt_success;
            } else {
                resources = this.mContext.getResources();
                i2 = C4558R.color.zm_v2_txt_primary;
            }
            textView.setTextColor(resources.getColor(i2));
        }
    }

    public void refresh(@NonNull List<PasswordItem> list) {
        this.mItemList = list;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.mItemList.size();
    }
}
