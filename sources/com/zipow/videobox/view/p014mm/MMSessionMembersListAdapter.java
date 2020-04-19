package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.PresenceStateView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSessionMembersListAdapter */
public class MMSessionMembersListAdapter extends Adapter<MMSessionMembersListHolder> {
    private static final String TAG = "MMSessionMembersListAdapter";
    private List<String> mAdminList;
    /* access modifiers changed from: private */
    public List<MMBuddyItem> mBuddyItemDatas;
    private Context mContext;
    private String mFilter;
    /* access modifiers changed from: private */
    public OnRecyclerViewListener mListener;
    private MMSessionMembersListFragment mParentFragment;

    /* renamed from: com.zipow.videobox.view.mm.MMSessionMembersListAdapter$MMSessionMembersListHolder */
    public class MMSessionMembersListHolder extends ViewHolder {
        private ImageView imgBell;
        private AvatarView mAvatarView;
        private ImageView mCallImg;
        private Context mContext;
        private TextView mCustomMessageTxt;
        private View mItemView;
        private PresenceStateView mPresenceStateView;
        private ZMEllipsisTextView mRoleTxt;
        private ZMEllipsisTextView mScreenNameTxt;

        public MMSessionMembersListHolder(@NonNull View view, Context context) {
            super(view);
            this.mContext = context;
            this.mItemView = view;
            this.mAvatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            this.mPresenceStateView = (PresenceStateView) view.findViewById(C4558R.C4560id.presenceStateView);
            this.mRoleTxt = (ZMEllipsisTextView) view.findViewById(C4558R.C4560id.txtRole);
            this.mScreenNameTxt = (ZMEllipsisTextView) view.findViewById(C4558R.C4560id.txtScreenName);
            this.mCustomMessageTxt = (TextView) view.findViewById(C4558R.C4560id.txtCustomMessage);
            this.imgBell = (ImageView) view.findViewById(C4558R.C4560id.imgBell);
        }

        public void bindView(@NonNull MMBuddyItem mMBuddyItem, List<String> list) {
            if (mMBuddyItem.getLocalContact() != null) {
                this.mAvatarView.show(mMBuddyItem.getLocalContact().getAvatarParamsBuilder());
            } else {
                ParamsBuilder paramsBuilder = new ParamsBuilder();
                paramsBuilder.setPath(mMBuddyItem.getAvatar()).setName(mMBuddyItem.getScreenName(), mMBuddyItem.getBuddyJid());
                this.mAvatarView.show(paramsBuilder);
            }
            if (mMBuddyItem.getLocalContact() != null) {
                this.mPresenceStateView.setState(mMBuddyItem.getLocalContact());
                this.mPresenceStateView.setmTxtDeviceTypeGone();
                if (!TextUtils.isEmpty(mMBuddyItem.getLocalContact().getSignature())) {
                    this.mCustomMessageTxt.setText(mMBuddyItem.getLocalContact().getSignature());
                    this.mCustomMessageTxt.setVisibility(0);
                } else {
                    this.mCustomMessageTxt.setVisibility(8);
                }
            }
            this.mScreenNameTxt.setText(mMBuddyItem.getScreenName());
            boolean z = list != null && list.size() > 0 && ((String) list.get(0)).equals(mMBuddyItem.buddyJid);
            boolean z2 = mMBuddyItem.getAccountStatus() == 1;
            if (z) {
                this.mRoleTxt.setText(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_group_owner));
                this.mRoleTxt.setVisibility(0);
            } else if (z2) {
                this.mRoleTxt.setText(this.mContext.getResources().getString(C4558R.string.zm_lbl_deactivated_62074, new Object[]{""}));
                this.mRoleTxt.setVisibility(0);
            } else {
                this.mRoleTxt.setVisibility(4);
            }
            View view = this.mItemView;
            StringBuilder sb = new StringBuilder();
            sb.append(mMBuddyItem.getScreenName());
            sb.append(z ? this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_group_owner) : "");
            view.setContentDescription(sb.toString());
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSessionMembersListAdapter$OnRecyclerViewListener */
    public interface OnRecyclerViewListener {
        void onItemClick(MMBuddyItem mMBuddyItem);

        void onItemLongClick(MMBuddyItem mMBuddyItem);
    }

    public MMSessionMembersListAdapter(Context context) {
        this.mContext = context;
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.mListener = onRecyclerViewListener;
    }

    public void setData(List<MMBuddyItem> list) {
        this.mBuddyItemDatas = new ArrayList(list);
        if (TextUtils.isEmpty(this.mFilter)) {
            sort();
        } else {
            sortByFilter(this.mFilter);
        }
        notifyDataSetChanged();
    }

    public void setFilter(@Nullable String str) {
        this.mFilter = str;
    }

    public List<MMBuddyItem> getDatas() {
        return this.mBuddyItemDatas;
    }

    public void setAdminList(List<String> list) {
        this.mAdminList = list;
    }

    public void setParentFragment(MMSessionMembersListFragment mMSessionMembersListFragment) {
        this.mParentFragment = mMSessionMembersListFragment;
    }

    public void clear() {
        List<MMBuddyItem> list = this.mBuddyItemDatas;
        if (list != null) {
            list.clear();
        }
    }

    private boolean isParentFragmentResumed() {
        MMSessionMembersListFragment mMSessionMembersListFragment = this.mParentFragment;
        if (mMSessionMembersListFragment == null) {
            return false;
        }
        return mMSessionMembersListFragment.isResumed();
    }

    @Nullable
    public MMBuddyItem getItem(int i) {
        List<MMBuddyItem> list = this.mBuddyItemDatas;
        if (list == null || i < 0 || i > list.size() - 1) {
            return null;
        }
        return (MMBuddyItem) this.mBuddyItemDatas.get(i);
    }

    @NonNull
    public MMSessionMembersListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MMSessionMembersListHolder(LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_session_members_list_item, viewGroup, false), this.mContext);
    }

    public void onBindViewHolder(@NonNull MMSessionMembersListHolder mMSessionMembersListHolder, final int i) {
        mMSessionMembersListHolder.bindView((MMBuddyItem) this.mBuddyItemDatas.get(i), this.mAdminList);
        if (this.mListener != null) {
            mMSessionMembersListHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MMSessionMembersListAdapter.this.mListener.onItemClick((MMBuddyItem) MMSessionMembersListAdapter.this.mBuddyItemDatas.get(i));
                }
            });
            mMSessionMembersListHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    MMSessionMembersListAdapter.this.mListener.onItemLongClick((MMBuddyItem) MMSessionMembersListAdapter.this.mBuddyItemDatas.get(i));
                    return false;
                }
            });
        }
    }

    public int getItemCount() {
        List<MMBuddyItem> list = this.mBuddyItemDatas;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void sort() {
        Collections.sort(this.mBuddyItemDatas, new MMBuddyItemComparator(CompatUtils.getLocalDefault()));
    }

    public void sortByFilter(String str) {
        if (!CollectionsUtil.isListEmpty(this.mBuddyItemDatas)) {
            Locale localDefault = CompatUtils.getLocalDefault();
            String lowerCase = str.toLowerCase(localDefault);
            for (int size = this.mBuddyItemDatas.size() - 1; size >= 0; size--) {
                String screenName = ((MMBuddyItem) this.mBuddyItemDatas.get(size)).getScreenName();
                if (!TextUtils.isEmpty(screenName) && !screenName.toLowerCase(localDefault).startsWith(lowerCase)) {
                    this.mBuddyItemDatas.remove(size);
                }
            }
            sort();
        }
    }
}
