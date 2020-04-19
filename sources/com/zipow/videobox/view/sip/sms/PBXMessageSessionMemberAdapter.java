package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.PresenceStateView;
import java.util.List;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.videomeetings.C4558R;

public class PBXMessageSessionMemberAdapter extends BaseRecyclerViewAdapter<PBXMessageContact> {

    static class SessionMemberViewHolder extends BaseViewHolder {
        private AvatarView avatarView;
        private View bottomDivider;
        private TextView customMessageTv;
        private View firstItemPlaceHolder;
        private View lastItemPlaceHolder;
        private PresenceStateView presenceStateView;
        private TextView screenNameTv;
        private ImageView tipArrowIv;

        public SessionMemberViewHolder(@NonNull View view, @Nullable final OnRecyclerViewListener onRecyclerViewListener) {
            super(view);
            this.firstItemPlaceHolder = view.findViewById(C4558R.C4560id.first_item_placeholder);
            this.avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            this.presenceStateView = (PresenceStateView) view.findViewById(C4558R.C4560id.presenceStateView);
            this.screenNameTv = (TextView) view.findViewById(C4558R.C4560id.txtScreenName);
            this.customMessageTv = (TextView) view.findViewById(C4558R.C4560id.txtCustomMessage);
            this.tipArrowIv = (ImageView) view.findViewById(C4558R.C4560id.iv_tip_arrow);
            this.bottomDivider = view.findViewById(C4558R.C4560id.bottom_divider);
            this.lastItemPlaceHolder = view.findViewById(C4558R.C4560id.last_item_placeholder);
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnRecyclerViewListener onRecyclerViewListener = onRecyclerViewListener;
                    if (onRecyclerViewListener != null) {
                        onRecyclerViewListener.onItemClick(view, SessionMemberViewHolder.this.getAdapterPosition());
                    }
                }
            });
        }

        public void setData(PBXMessageContact pBXMessageContact, boolean z) {
            String str;
            Context context = this.itemView.getContext();
            if (context != null) {
                int i = 0;
                this.firstItemPlaceHolder.setVisibility(getAdapterPosition() == 0 ? 0 : 8);
                if (TextUtils.isEmpty(pBXMessageContact.getNumberType())) {
                    str = pBXMessageContact.getDisplayPhoneNumber();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(pBXMessageContact.getNumberType());
                    sb.append(": ");
                    sb.append(pBXMessageContact.getDisplayPhoneNumber());
                    str = sb.toString();
                }
                IMAddrBookItem item = pBXMessageContact.getItem();
                if (item == null) {
                    this.avatarView.show(null);
                    this.presenceStateView.setVisibility(8);
                    this.screenNameTv.setText(str);
                    this.customMessageTv.setVisibility(8);
                    this.tipArrowIv.setVisibility(8);
                } else {
                    this.avatarView.show(item.getAvatarParamsBuilder());
                    if (getAdapterPosition() == 0) {
                        this.presenceStateView.setVisibility(8);
                        TextView textView = this.screenNameTv;
                        int i2 = C4558R.string.zm_pbx_you_100064;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(item.getScreenName());
                        sb2.append(OAuth.SCOPE_DELIMITER);
                        textView.setText(context.getString(i2, new Object[]{sb2.toString()}));
                        this.tipArrowIv.setVisibility(8);
                    } else {
                        this.presenceStateView.setVisibility(0);
                        this.presenceStateView.setmTxtDeviceTypeGone();
                        this.presenceStateView.setState(item);
                        this.screenNameTv.setText(item.getScreenName());
                        this.tipArrowIv.setVisibility(0);
                    }
                    this.customMessageTv.setVisibility(0);
                    this.customMessageTv.setText(str);
                }
                this.bottomDivider.setVisibility(z ? 8 : 0);
                View view = this.lastItemPlaceHolder;
                if (!z) {
                    i = 8;
                }
                view.setVisibility(i);
            }
        }
    }

    public PBXMessageSessionMemberAdapter(Context context, @NonNull List<PBXMessageContact> list, @Nullable OnRecyclerViewListener onRecyclerViewListener) {
        super(context);
        setData(list);
        setOnRecyclerViewListener(onRecyclerViewListener);
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SessionMemberViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_pbx_sms_conversation_member, viewGroup, false), this.mListener);
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (baseViewHolder instanceof SessionMemberViewHolder) {
            SessionMemberViewHolder sessionMemberViewHolder = (SessionMemberViewHolder) baseViewHolder;
            PBXMessageContact pBXMessageContact = (PBXMessageContact) getItem(baseViewHolder.getAdapterPosition());
            boolean z = true;
            if (baseViewHolder.getAdapterPosition() != getItemCount() - 1) {
                z = false;
            }
            sessionMemberViewHolder.setData(pBXMessageContact, z);
        }
    }
}
