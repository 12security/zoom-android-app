package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineBean;
import com.zipow.videobox.sip.server.CmmSIPLineCallItemBean;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPUserBean;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.PresenceStateView;
import com.zipow.videobox.view.sip.AbstractSharedLineItem.OnItemClickListener;
import com.zipow.videobox.view.sip.AbstractSharedLineItem.SharedLineItemType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.videomeetings.C4558R;

public class SharedLineUserItem extends AbstractSharedLineItem {
    /* access modifiers changed from: private */
    public boolean mIsFirstSharedUser;
    /* access modifiers changed from: private */
    public boolean mIsSLG = CmmSIPCallManager.getInstance().isSharedLineGroupEnabled();
    /* access modifiers changed from: private */
    public boolean mIsSelf;
    private String mUserId;
    private String mUserJid;

    public static class SharedLineUserItemViewHolder extends BaseViewHolder {
        private View mBottomDivider;
        private ImageView mIvFastDial;
        private PresenceStateView mPresenceStateView;
        private TextView mTvGroupName;
        private TextView mTvUserName;
        private TextView mTvUserStatus;

        public SharedLineUserItemViewHolder(View view, final OnItemClickListener onItemClickListener) {
            super(view);
            C41031 r0 = new OnClickListener() {
                public void onClick(View view) {
                    OnItemClickListener onItemClickListener = onItemClickListener;
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(view, SharedLineUserItemViewHolder.this.getAdapterPosition());
                    }
                }
            };
            view.setOnClickListener(r0);
            this.mPresenceStateView = (PresenceStateView) view.findViewById(C4558R.C4560id.presenceStateView);
            this.mPresenceStateView.setmTxtDeviceTypeGone();
            this.mTvGroupName = (TextView) view.findViewById(C4558R.C4560id.tv_group_name);
            this.mTvUserName = (TextView) view.findViewById(C4558R.C4560id.tv_user_name);
            this.mTvUserStatus = (TextView) view.findViewById(C4558R.C4560id.tv_user_status);
            this.mIvFastDial = (ImageView) view.findViewById(C4558R.C4560id.iv_fast_dial);
            this.mIvFastDial.setOnClickListener(r0);
            this.mBottomDivider = view.findViewById(C4558R.C4560id.bottom_divider);
        }

        public void setData(SharedLineUserItem sharedLineUserItem) {
            Context context = this.itemView.getContext();
            LayoutParams layoutParams = (LayoutParams) this.mTvUserName.getLayoutParams();
            layoutParams.topMargin = UIUtil.dip2px(context, getAdapterPosition() == 0 ? 31.0f : 13.0f);
            this.mTvUserName.setLayoutParams(layoutParams);
            int i = 8;
            this.mTvGroupName.setVisibility(sharedLineUserItem.mIsFirstSharedUser ? 0 : 8);
            this.mTvGroupName.setText(sharedLineUserItem.mIsSLG ? C4558R.string.zm_sip_sla_shared_group_99631 : C4558R.string.zm_sip_sla_shared_82852);
            this.mPresenceStateView.setVisibility((!sharedLineUserItem.mIsSLG || sharedLineUserItem.mIsSelf) ? 0 : 8);
            this.mTvUserStatus.setVisibility((!sharedLineUserItem.mIsSLG || sharedLineUserItem.mIsSelf) ? 0 : 8);
            this.mIvFastDial.setVisibility((sharedLineUserItem.mIsSLG || sharedLineUserItem.mIsSelf) ? 8 : 0);
            View view = this.mBottomDivider;
            if (sharedLineUserItem.getLineCallCount() <= 0) {
                i = 0;
            }
            view.setVisibility(i);
            String userName = sharedLineUserItem.getUserName();
            if (sharedLineUserItem.mIsSelf) {
                String myName = PTApp.getInstance().getMyName();
                this.mTvUserName.setPadding(0, 0, 0, 0);
                TextView textView = this.mTvUserName;
                Context context2 = this.itemView.getContext();
                int i2 = C4558R.string.zm_mm_msg_my_notes_65147;
                Object[] objArr = new Object[1];
                if (StringUtil.isEmptyOrNull(myName)) {
                    myName = userName;
                }
                objArr[0] = myName;
                textView.setText(context2.getString(i2, objArr));
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger == null) {
                    setPresenceByLineRegisterResult(CmmSIPLineManager.getInstance().getMineExtensionLineRegResult());
                } else {
                    this.mPresenceStateView.setState(zoomMessenger.getMyPresence(), zoomMessenger.getMyPresenceStatus());
                }
                CmmSIPCallItem currentCallItem = CmmSIPCallManager.getInstance().getCurrentCallItem();
                if (currentCallItem != null) {
                    this.mTvUserStatus.setText(this.itemView.getContext().getString(C4558R.string.zm_sip_sla_on_call_82852, new Object[]{CmmSIPCallManager.getInstance().getSipCallDisplayName(currentCallItem)}));
                } else {
                    this.mTvUserStatus.setText(this.mPresenceStateView.getTxtDeviceTypeText());
                }
            } else if (sharedLineUserItem.mIsSLG) {
                this.mTvUserName.setPadding(0, UIUtil.dip2px(context, 5.0f), 0, UIUtil.dip2px(context, 18.0f));
                this.mTvUserName.setText(userName);
            } else {
                CmmSIPCallRegResult cmmSIPCallRegResult = null;
                IMAddrBookItem buddyByJid = sharedLineUserItem.getUserJid() != null ? ZMBuddySyncInstance.getInsatance().getBuddyByJid(sharedLineUserItem.getUserJid()) : null;
                if (buddyByJid == null) {
                    CmmSIPLineBean lineByIndex = sharedLineUserItem.getLineByIndex(0);
                    if (lineByIndex != null) {
                        cmmSIPCallRegResult = CmmSIPLineManager.getInstance().getLineRegResult(lineByIndex.getID());
                    }
                    setPresenceByLineRegisterResult(cmmSIPCallRegResult);
                } else {
                    this.mPresenceStateView.setState(buddyByJid);
                    if (!StringUtil.isEmptyOrNull(buddyByJid.getScreenName())) {
                        userName = buddyByJid.getScreenName();
                    }
                }
                this.mTvUserName.setPadding(0, 0, 0, 0);
                this.mTvUserName.setText(userName);
                this.mTvUserStatus.setText(this.mPresenceStateView.getTxtDeviceTypeText());
            }
        }

        private void setPresenceByLineRegisterResult(CmmSIPCallRegResult cmmSIPCallRegResult) {
            this.mPresenceStateView.setState((cmmSIPCallRegResult == null || cmmSIPCallRegResult.getRegStatus() != 6) ? 0 : 3, 0);
        }
    }

    public SharedLineUserItem(@NonNull CmmSIPUserBean cmmSIPUserBean, boolean z, boolean z2) {
        this.mUserId = cmmSIPUserBean.getID();
        this.mUserJid = cmmSIPUserBean.getJid();
        this.mIsSelf = z;
        this.mIsFirstSharedUser = z2;
    }

    public static BaseViewHolder createViewHolder(ViewGroup viewGroup, OnItemClickListener onItemClickListener) {
        return new SharedLineUserItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_shared_line_user_item, viewGroup, false), onItemClickListener);
    }

    public int getViewType() {
        return SharedLineItemType.ITEM_SHARED_LINE_USER.ordinal();
    }

    public void bindViewHolder(BaseViewHolder baseViewHolder, @Nullable List<Object> list) {
        if (baseViewHolder instanceof SharedLineUserItemViewHolder) {
            ((SharedLineUserItemViewHolder) baseViewHolder).setData(this);
        }
    }

    public boolean equals(@Nullable Object obj) {
        return (obj instanceof SharedLineUserItem) && StringUtil.isSameStringForNotAllowNull(this.mUserId, ((SharedLineUserItem) obj).mUserId);
    }

    @Nullable
    public CmmSIPUserBean getUserBean() {
        return CmmSIPLineManager.getInstance().getSharedUserBeanById(this.mUserId);
    }

    @Nullable
    public CmmSIPLineBean getLineByIndex(int i) {
        CmmSIPUserBean userBean = getUserBean();
        if (userBean == null) {
            return null;
        }
        LinkedHashMap lineMap = userBean.getLineMap();
        if (i > 0 && lineMap.size() > i) {
            int i2 = 0;
            for (Entry value : lineMap.entrySet()) {
                int i3 = i2 + 1;
                if (i2 == i) {
                    return (CmmSIPLineBean) value.getValue();
                }
                i2 = i3;
            }
        }
        return null;
    }

    public String getExtension() {
        CmmSIPUserBean userBean = getUserBean();
        if (userBean == null) {
            return null;
        }
        return userBean.getExtension();
    }

    public boolean isSelf() {
        return this.mIsSelf;
    }

    public boolean isFirstSharedUser() {
        return this.mIsFirstSharedUser;
    }

    public void setIsFirstSharedUser(boolean z) {
        this.mIsFirstSharedUser = z;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public String getUserJid() {
        return this.mUserJid;
    }

    @Nullable
    public String getUserName() {
        CmmSIPUserBean userBean = getUserBean();
        if (userBean == null) {
            return null;
        }
        return userBean.getUserName();
    }

    @NonNull
    public List<CmmSIPLineCallItemBean> getLineCallItems() {
        return CmmSIPLineManager.getInstance().getUserLineCallItemsByUserId(this.mUserId);
    }

    public int getLineCallCount() {
        return getLineCallItems().size();
    }

    public boolean containsLineItem(String str) {
        CmmSIPUserBean userBean = getUserBean();
        if (userBean != null && !StringUtil.isEmptyOrNull(str)) {
            return userBean.getLineMap().containsKey(str);
        }
        return false;
    }

    public boolean containsLineCallItem(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            return this.mUserId.equals(CmmSIPLineManager.getInstance().getLineCallItemUserId(str));
        }
        return false;
    }
}
